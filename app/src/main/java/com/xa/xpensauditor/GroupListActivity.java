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

public class GroupListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbargl);
        toolbar.setTitle("XpensAuditor");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabgl);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(GroupListActivity.this,CreateGroupActivity.class);
                startActivity(i);
            }
        });

        final ListView list = findViewById(R.id.grouplist);
        ArrayList<String> groupList = new ArrayList<String>();

        Firebase mRootRef;
        Firebase RefUid, RefEmail;
        mRootRef=new Firebase("https://xpense-auditor-default-rtdb.firebaseio.com");

        mRootRef.keepSynced(true);
        com.google.firebase.auth.FirebaseAuth auth = FirebaseAuth.getInstance();
        String Uid=auth.getUid();
        RefUid= mRootRef.child(Uid);
        RefEmail=RefUid.child("Email");

        DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();

        mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(GroupListActivity.this, Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_LONG).show();
                } else {
                    boolean entryAdded = false;
                    //Loop over all the database entries to find the groups
                    for (com.google.firebase.database.DataSnapshot databaseEntry : task.getResult().getChildren()) {
                        if (databaseEntry.child("Group Name").exists()) {
                            //Loop over all the emails in the group entry to see if the user is a part of that group
                            for (com.google.firebase.database.DataSnapshot groupEntryChild : task.getResult().getChildren()) {
                                if (!groupEntryChild.getKey().equals("Group Name")) {
                                    String userEmailInGroup = groupEntryChild.getValue().toString();
                                    String loggedInUserEmail = mDatabase.child(Uid).child("Email").toString();
                                    if (userEmailInGroup.equals(loggedInUserEmail))
                                    {
                                        //logged in user is a part of the group
                                        String groupContainingUser = databaseEntry.child("Group Name").toString();
                                        groupList.add(groupContainingUser);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        });

//        groupList.add("Group1");

        CustomAdapter customAdapter = new CustomAdapter(GroupListActivity.this, groupList);
        list.setAdapter(customAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent=new Intent(GroupListActivity.this, GroupActivity.class);
                String groupName = groupList.get(position);
//                TODO: Get the group key from firebase based on groupName
                String groupKey = "";
                intent.putExtra("group_key", groupKey);
                startActivity(intent);
            }
        });
    }

}

class CustomAdapter implements ListAdapter {
    ArrayList<String> groupNames;
    Context context;

    public CustomAdapter(Activity context, ArrayList<String> groupNames) {

        this.context = context;
        this.groupNames = groupNames;

    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater = LayoutInflater.from(context);
        if(convertView==null)
            row = inflater.inflate(R.layout.group_list_row_item, null, true);
        TextView textViewGroupName = (TextView) row.findViewById(R.id.textGroupName);
        ImageView imageFlag = (ImageView) row.findViewById(R.id.imageGroupImage);

        textViewGroupName.setText(groupNames.get(position));
        imageFlag.setImageResource(R.drawable.ic_avtion_group_list_item);
        return  row;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return false;
    }
}
