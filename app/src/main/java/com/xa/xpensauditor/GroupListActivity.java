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
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import java.util.Collections;

import java.util.Objects;

public class GroupListActivity extends AppCompatActivity {
    private Firebase mRootRef;
    private Firebase RefUid,RefTran;
    String Uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        Firebase.setAndroidContext(this);

        mRootRef = new Firebase("https://xpense-auditor-default-rtdb.firebaseio.com");

        mRootRef.keepSynced(true);
        com.google.firebase.auth.FirebaseAuth auth = FirebaseAuth.getInstance();
        Uid=auth.getUid();
        RefUid = mRootRef.child(Uid);

        DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.child(Uid).child("Groups").get().addOnCompleteListener(new OnCompleteListener<com.google.firebase.database.DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                      Toast.makeText(GroupListActivity.this, "Couldn't retrieve group list", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(GroupListActivity.this, task.getResult().toString(), Toast.LENGTH_LONG).show();
//                    task.getResult().getValue();
                }

            }

        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabgl);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(GroupListActivity.this,CreateGroupActivity.class);
                startActivity(i);
            }
        });

        final ListView groupListView = findViewById(R.id.grouplist);
        ArrayList<String> groupNameList = new ArrayList<>();
        ArrayList<String> groupCountList = new ArrayList<>();

//        TODO: Get the list of group names and members count from firebase and populate arrayList


        Firebase mRootRef;
        Firebase RefUid, RefEmail;
        mRootRef=new Firebase("https://xpense-auditor-default-rtdb.firebaseio.com");

        mRootRef.keepSynced(true);
//        com.google.firebase.auth.FirebaseAuth auth = FirebaseAuth.getInstance();
//        String Uid=auth.getUid();
        RefUid= mRootRef.child(Uid);
        RefEmail=RefUid.child("Email");
        String loggedInUserEmail="";

//        DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();



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
                                                                  //Loop over all the emails in the found group entry to see if the user is a part of that group
                                                                  for (com.google.firebase.database.DataSnapshot groupEntryChild : databaseEntry.child("GroupMembers").getChildren()) {
                                                                      if (!groupEntryChild.getKey().equals("Group Name")) {
                                                                          //Emails are stored as key value pairs in the group object. If the key is "Group Name", it means that that key value pair does not store a user email
                                                                          String userEmailInGroup = groupEntryChild.getValue().toString();
                                                                          //RefEmail is the reference to the email field of the logged in user
                                                                          RefEmail.addValueEventListener(new ValueEventListener() {
                                                                              @Override
                                                                              public void onDataChange(com.firebase.client.DataSnapshot DS) {
                                                                                  String loggedInUserEmail = DS.getValue().toString();
                                                                                  if (userEmailInGroup.equals(loggedInUserEmail)) {
                                                                                      //logged in user is a part of the group
                                                                                      String groupContainingUser = Objects.requireNonNull(databaseEntry.child("Group Name").getValue()).toString();
                                                                                      String numberOfGroupMembers = Objects.requireNonNull(databaseEntry.child("Member Count").getValue()).toString();
                                                                                      groupNameList.add(groupContainingUser);
                                                                                      groupCountList.add(numberOfGroupMembers);
                                                                                      CustomGroupList customGroupList = new CustomGroupList(GroupListActivity.this, groupNameList, groupCountList);
                                                                                      groupListView.setAdapter(customGroupList);
                                                                                  }
                                                                              }

                                                                              @Override
                                                                              public void onCancelled(FirebaseError firebaseError) {

                                                                              }
                                                                          });
                                                                      }
                                                                  }
                                                              }
                                                          }
                                                      }
                                                  }
                                              });

        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent=new Intent(GroupListActivity.this, GroupActivity.class);
                String groupName = groupNameList.get(position);
//                TODO: Get the group key from firebase based on groupName
                String groupKey = groupName;
                Toast.makeText(GroupListActivity.this, groupKey, Toast.LENGTH_LONG).show();
                intent.putExtra("group_key", groupKey);
                intent.putExtra("personal_uid", Uid);
                startActivity(intent);
            }
        });
    }

}

class CustomGroupList extends ArrayAdapter {
    ArrayList<String> groupNames;
    ArrayList<String> groupCount;
    Activity context;

    public CustomGroupList(Activity context, ArrayList<String> groupNames, ArrayList<String> groupCount) {
        super(context, R.layout.group_list_row_item, groupNames);
        this.context = context;
        this.groupNames = groupNames;
        this.groupCount = groupCount;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        if(convertView==null)
            row = inflater.inflate(R.layout.group_list_row_item, null, true);
        TextView textGroupName = (TextView) row.findViewById(R.id.textGroupName);
        TextView textGroupCount = (TextView) row.findViewById(R.id.textGroupCount);
        ImageView imageGroup = (ImageView) row.findViewById(R.id.imageGroupImage);

        textGroupName.setText(groupNames.get(position));
        String groupCountStr = groupCount.get(position)+" Group Members";
        textGroupCount.setText(groupCountStr);
//        imageGroup.setImageResource(R.drawable.ic_avtion_group_list_item);
        return  row;
    }
}