package com.xa.xpensauditor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

public class EditProfile extends AppCompatActivity {

    DatePicker dob;
    Button submit;
    EditText userName, userEmail, userPhone, userAddr;
    private Firebase mRootRef;
    private Firebase RefUid;
    private Firebase RefName,RefEmail,RefPhnnum;
    String day,month,year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dob = (DatePicker)findViewById(R.id.userDOB);
        submit = (Button)findViewById(R.id.submitButton);
        userAddr = (EditText)findViewById(R.id.userAddress);
        userEmail = (EditText)findViewById(R.id.userEmail);
        userName = (EditText)findViewById(R.id.userName);
        userPhone = (EditText)findViewById(R.id.userPhone);

        Intent intent = getIntent();
        userName.setText(intent.getStringExtra("NAME"));
        userEmail.setText(intent.getStringExtra("EMAIL"));
        userPhone.setText(intent.getStringExtra("PHONE"));
        userAddr.setText(intent.getStringExtra("ADDRESS"));
        day=intent.getStringExtra("DAY");
        month=intent.getStringExtra("MONTH");
        year=intent.getStringExtra("YEAR");
        dob.init(Integer.parseInt(year),Integer.parseInt(month)-1,Integer.parseInt(day),null);
        Toast.makeText(getApplicationContext(),day+month+year,Toast.LENGTH_SHORT).show();
        mRootRef=new Firebase("https://expense-2a69a.firebaseio.com/");
        mRootRef.keepSynced(true);
        com.google.firebase.auth.FirebaseAuth auth = FirebaseAuth.getInstance();
        String Uid=auth.getUid();
        RefUid= mRootRef.child(Uid);
        RefName = RefUid.child("Name");
        RefEmail=RefUid.child("Email");
        RefPhnnum=RefUid.child("Phone Number");


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String day = String.valueOf(dob.getDayOfMonth());
                String month = String.valueOf(dob.getMonth()+1);
                String year = String.valueOf(dob.getYear());
                String dateOfBirth = day+"/"+month+"/"+year;
                //Toast.makeText(getApplicationContext(),dateOfBirth,Toast.LENGTH_LONG).show();

                String name = userName.getText().toString();
                String phone = userPhone.getText().toString();
                String addr = userAddr.getText().toString();
                String email = userEmail.getText().toString();
                RefName.setValue(name);
                RefEmail.setValue(email);
                RefPhnnum.setValue(phone);
                RefUid.child("Address").setValue(addr);
                RefUid.child("Day").setValue(day);
                RefUid.child("Month").setValue(month);
                RefUid.child("Year").setValue(year);

                //Toast.makeText(getApplicationContext(),name+","+addr+","+phone+","+email,Toast.LENGTH_LONG).show();
//                TODO
//                startActivity(new Intent(EditProfile.this,ProfileActivity.class));
            }


        });




    }

}