package com.xa.xpensauditor;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class CreateGroupActivity extends AppCompatActivity {
    private Firebase RefUid,RefTran;

    private AppCompatEditText inputGroupName;
    private AppCompatEditText inputUserEmailAddedToGroup;


    private Firebase mRootRef;
    private Firebase RefNewGroup;
    private DatabaseReference RefNewUserEmailAddedToGroup;
    private DatabaseReference RefGroupMemberCount;
    private Firebase RefNewUserEmailAddedToNewGroup;
    private Firebase RefCat,RefFood,RefHealth,RefTravel,RefEdu,RefBills,RefHomeNeeds,RefOthers;

    private Button btnAddUser, btnBack;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        Firebase.setAndroidContext(this);
        //Get Firebase auth instance
        mRootRef = new Firebase("https://xpense-auditor-default-rtdb.firebaseio.com");

        btnAddUser = (Button) findViewById(R.id.btn_invite_member);
        btnBack = (Button) findViewById(R.id.back_button);
        inputGroupName = (AppCompatEditText) findViewById(R.id.groupName);
        inputUserEmailAddedToGroup = (AppCompatEditText) findViewById(R.id.emailIds);


        // Add user email to group
        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String groupName = inputGroupName.getText().toString().trim();
                String userEmailsAddedToGroup[] = inputUserEmailAddedToGroup.getText().toString().trim().split(" ");


                if (userEmailsAddedToGroup.length == 0) {
                    Toast.makeText(getApplicationContext(), "Enter email addresses!", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                mDatabase.get().addOnCompleteListener(new OnCompleteListener<com.google.firebase.database.DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<com.google.firebase.database.DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(CreateGroupActivity.this, Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_LONG).show();
                        }
                        else {
                            mDatabase.child(groupName).child("Member Count").setValue(userEmailsAddedToGroup.length);

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
                            for(int i = 0; i < userEmailsAddedToGroup.length; i++) {
                                RefNewUserEmailAddedToNewGroup = RefNewGroup.child("GroupMembers").push();
                                RefNewUserEmailAddedToNewGroup.setValue(userEmailsAddedToGroup[i]);
                            }

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

                            Intent i = new Intent(CreateGroupActivity.this, GroupListActivity.class);
                            startActivity(i);
                        }
                    }

                });

                //finish();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateGroupActivity.this, GroupListActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //progressBar.setVisibility(View.GONE);
    }

}