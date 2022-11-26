package com.xa.xpensauditor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Calendar;

public class SMSDBFetchActivity extends AppCompatActivity {

    TextView smstid,smstamnt,smsshpname,smscat,smsdate,sms;
    private Firebase mRootRef;
    private Firebase RefUid;
    private Button btnEdit;
    String d,m,y;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsdbfetch);
        Firebase.setAndroidContext(this);
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

        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH)+1;
        int year = c.get(Calendar.YEAR);

        btnEdit = (Button) findViewById(R.id.edit_transaction);

        mRootRef=new Firebase("https://xpensauditor-g11-default-rtdb.firebaseio.com/");

        mRootRef.keepSynced(true);
        com.google.firebase.auth.FirebaseAuth auth = com.google.firebase.auth.FirebaseAuth.getInstance();
        String Uid=auth.getUid();
        RefUid= mRootRef.child(Uid);

        RefUid.child("DateRange").child(month+"-"+year).child("Transactions").child(tid).child("Amount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null){
                    startActivity(new Intent(SMSDBFetchActivity.this, HomeActivity.class));
                }
                smstamnt.setText(dataSnapshot.getValue().toString().trim());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        RefUid.child("DateRange").child(month+"-"+year).child("Transactions").child(tid).child("Category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                smscat.setText(dataSnapshot.getValue().toString().trim());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        RefUid.child("DateRange").child(month+"-"+year).child("Transactions").child(tid).child("Shop Name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                smsshpname.setText(dataSnapshot.getValue().toString().trim());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        RefUid.child("DateRange").child(month+"-"+year).child("Transactions").child(tid).child("ZMessage").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sms.setText(dataSnapshot.getValue().toString().trim());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        RefUid.child("DateRange").child(month+"-"+year).child("Transactions").child(tid).child("Day").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                d=dataSnapshot.getValue().toString().trim();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        RefUid.child("DateRange").child(month+"-"+year).child("Transactions").child(tid).child("Month").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                m=dataSnapshot.getValue().toString().trim();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        RefUid.child("DateRange").child(month+"-"+year).child("Transactions").child(tid).child("Year").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                y=dataSnapshot.getValue().toString().trim();
                smsdate.setText(d+"/"+m+"/"+y);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Remove the print statement
                System.out.println("Edit button was clicked!");

                Intent edit_intent = new Intent(getApplicationContext(), EditTransaction.class);

                edit_intent.putExtra("tns_id",smstid.getText().toString());
                edit_intent.putExtra("tns_amt",smstamnt.getText().toString());
                edit_intent.putExtra("shp_name",smsshpname.getText().toString());
                edit_intent.putExtra("cat",smscat.getText().toString());
                edit_intent.putExtra("dat",smsdate.getText().toString());
                edit_intent.putExtra("msg",sms.getText().toString());
                startActivity(edit_intent);
            }
        });

    }
}