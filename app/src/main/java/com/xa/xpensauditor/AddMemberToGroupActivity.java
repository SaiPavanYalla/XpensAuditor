package com.xa.xpensauditor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class AddMemberToGroupActivity extends AppCompatActivity {

    private Button btnBack, btnInviteMember;
    private EditText emailId;

    private Firebase mRootRef;
    private Firebase RefNewGroup;
    private DatabaseReference RefNewUserEmailAddedToGroup;
    private DatabaseReference RefGroupMemberCount;
    private Firebase RefNewUserEmailAddedToNewGroup;

    private String groupKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member_to_group);

        Intent intent = getIntent();
        groupKey = intent.getExtras().getString("group_key");

        btnBack = (Button) findViewById(R.id.back_button);
//        btnInviteMember = (Button)findViewById(R.id.btn_invite_member);
        emailId = (EditText)findViewById(R.id.emailIds);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddMemberToGroupActivity.this, GroupListActivity.class);
                startActivity(i);
            }
        });

        Firebase.setAndroidContext(this);
        //Get Firebase auth instance
        mRootRef = new Firebase("https://xpense-auditor-default-rtdb.firebaseio.com");

        // Add user email to group
        btnInviteMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String groupName = groupKey;
                String userEmailsAddedToGroup = emailId.getText().toString();

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                mDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(AddMemberToGroupActivity.this, Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_LONG).show();
                        }
                        else {
//                            RefUid = mRootRef.child(Uid);
//                            RefTran = RefUid.child("DateRange").child(currentMonth + "-" + currentYear).child("Transactions");
//                            mDatabase.child(groupName).child("Member Count").setValue(userEmailsAddedToGroup.length);

//                            if (!task.getResult().child(groupName).child("Member Count").exists())
//                            {
//                                RefGroupMemberCount.setValue("1");
//                            }
//                            else {
//                                    String currentMemberCount = task.getResult().child(groupName).child("Member Count").getValue().toString();
//                                    String newMemberCount = Integer.toString(Integer.parseInt(currentMemberCount)+1);
//                                    Toast.makeText(CreateGroupActivity.this, newMemberCount, Toast.LENGTH_LONG).show();
//                                    RefGroupMemberCount.setValue(newMemberCount);
//                            }

                            RefNewGroup = mRootRef.child(groupName);
                            RefNewGroup.child("Group Name").setValue(groupName);
//                            for(int i = 0; i < userEmailsAddedToGroup.length; i++) {
//                                RefNewUserEmailAddedToNewGroup = RefNewGroup.child("GroupMembers").push();
//                                RefNewUserEmailAddedToNewGroup.setValue(userEmailsAddedToGroup);
//                            }

//                            RefCat=RefNewGroup.child("Categories");
//                            RefFood=RefCat.child("Food");
//                            RefFood.setValue("");
//                            RefHealth=RefCat.child("Health");
//                            RefHealth.setValue("");
//                            RefTravel=RefCat.child("Travel");
//                            RefTravel.setValue("");
//                            RefEdu=RefCat.child("Education");
//                            RefEdu.setValue("");
//                            RefBills=RefCat.child("Bills");
//                            RefBills.setValue("");
//                            RefHomeNeeds=RefCat.child("Home Needs");
//                            RefHomeNeeds.setValue("");
//                            RefOthers=RefCat.child("Others");
//                            RefOthers.setValue("");

                            Intent i = new Intent(AddMemberToGroupActivity.this, GroupListActivity.class);
                            startActivity(i);
                        }
                    }

                });

                //finish();
            }
        });


    }
}