package com.xa.xpensauditor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class ListGroupMembersActivity extends AppCompatActivity {

    String Uid;
    ArrayList<String> nameList = new ArrayList<String>();
    ArrayList<String> emailList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_group_members);

        Intent intent = getIntent();
        Uid = intent.getExtras().getString("group_key");

        DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.child(Uid).child("Groups").get().addOnCompleteListener(new OnCompleteListener<com.google.firebase.database.DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(ListGroupMembersActivity.this, "Couldn't retrieve group list", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ListGroupMembersActivity.this, task.getResult().toString(), Toast.LENGTH_LONG).show();
//                    task.getResult().getValue();
                }

            }

        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent i = new Intent(getApplicationContext(), AddMemberToGroupActivity.class);
                    i.putExtra("group_key", Uid);
                    startActivity(i);
                } catch(Exception e) {
                    System.out.println("Error: " + e.toString());
                }
            }
        });

        final ListView list = findViewById(R.id.grouplist);

//        Get the list of group names from firebase and populate arrayList
        emailList.add("Dummy@gmail.com");
        nameList.add("Dummy");

        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(ListGroupMembersActivity.this, Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_LONG).show();
                }
                else {
//                    System.out.println("---------------------------------------------");
                    for (com.google.firebase.database.DataSnapshot groupEntryChild : task.getResult().child(Uid).child("GroupMembers").getChildren()) {
                        String userEmailInGroup = groupEntryChild.getValue().toString();
//                        System.out.println("Email - "+userEmailInGroup);
                        int index = userEmailInGroup.indexOf('@');
                        String userName = userEmailInGroup.substring(0,index);
                        nameList.add(userName);
                        emailList.add(userEmailInGroup);
                        GroupMembersAdaptor groupMembersAdaptor = new GroupMembersAdaptor(ListGroupMembersActivity.this, nameList, emailList);
                        list.setAdapter(groupMembersAdaptor);
                    }
                }
            }
        });

        System.out.println("---------------------------------------------");
        for(int i=0; i < emailList.size(); i++){
            System.out.println("Email - "+emailList.get(i));
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent i=new Intent(getApplicationContext(), GroupActivity.class);
                startActivity(i);
            }
        });

    }

    public void getNameEmail(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(ListGroupMembersActivity.this, Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_LONG).show();
                }
                else {
                    System.out.println("---------------------------------------------");
                    for (com.google.firebase.database.DataSnapshot groupEntryChild : task.getResult().child(Uid).child("GroupMembers").getChildren()) {
                        String userEmailInGroup = groupEntryChild.getValue().toString();
                        System.out.println("Email - "+userEmailInGroup);
                        int index = userEmailInGroup.indexOf('@');
                        String userName = userEmailInGroup.substring(0,index);
                        nameList.add(userName);
                        emailList.add(userEmailInGroup);
                    }
                }
            }

        });
    }
}

class GroupMembersAdaptor extends ArrayAdapter {
    ArrayList<String> names;
    ArrayList<String> emailIds;
    Activity context;

    public GroupMembersAdaptor(Activity context, ArrayList<String> names, ArrayList<String> emailIds) {
        super(context, R.layout.list_group_member_row, names);
        this.context = context;
        this.names = names;
        this.emailIds = emailIds;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.list_group_member_row, null, true);
        TextView textViewName = (TextView) row.findViewById(R.id.textViewName);
        TextView textViewEmail = (TextView) row.findViewById(R.id.textViewEmail);
        ImageView imageFlag = (ImageView) row.findViewById(R.id.imageViewMember);

        textViewName.setText(names.get(position));
        textViewEmail.setText(emailIds.get(position));
        imageFlag.setImageResource(R.drawable.ic_action_group_member_item);
        return  row;
    }
}