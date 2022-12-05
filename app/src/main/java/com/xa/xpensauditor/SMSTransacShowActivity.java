package com.xa.xpensauditor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

public class SMSTransacShowActivity extends AppCompatActivity {

    TextView smstid,smstamnt,smsshpname,smscat,smsdate,sms;
    private Firebase mRootRef;
    private Firebase RefUid;
    String d,m,y;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smstransac_show);
        Intent i = getIntent();
        String tid = i.getStringExtra("indexPos");
        //Toast.makeText(getApplicationContext()," yo : "+tid,Toast.LENGTH_SHORT).show();
        smstid=findViewById(R.id.smstid);
        smstid.setText(tid);
        smstamnt=findViewById(R.id.smstamnt);
        smsshpname=findViewById(R.id.smsshpname);
        smscat=findViewById(R.id.smscat);
        smsdate=findViewById(R.id.smsdate);
        sms=findViewById(R.id.sms);


        mRootRef=new Firebase("https://xpense-auditor-default-rtdb.firebaseio.com");

        mRootRef.keepSynced(true);
        com.google.firebase.auth.FirebaseAuth auth = FirebaseAuth.getInstance();
        String Uid=auth.getUid();
        RefUid= mRootRef.child(Uid);

        String CAT_KEY = "CatTran";
        Firebase childObj = RefUid.child(CAT_KEY).child(tid);

        childObj.child("Amount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                smstamnt.setText(dataSnapshot.getValue().toString().trim());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        childObj.child("Category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                smscat.setText(dataSnapshot.getValue().toString().trim());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        childObj.child("Shop Name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                smsshpname.setText(dataSnapshot.getValue().toString().trim());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        childObj.child("ZMessage").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sms.setText(dataSnapshot.getValue().toString().trim());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        childObj.child("Day").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                d=dataSnapshot.getValue().toString().trim();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        childObj.child("Month").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                m=dataSnapshot.getValue().toString().trim();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        childObj.child("Year").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                y=dataSnapshot.getValue().toString().trim();
                smsdate.setText(d+"/"+m+"/"+y);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



    }
}