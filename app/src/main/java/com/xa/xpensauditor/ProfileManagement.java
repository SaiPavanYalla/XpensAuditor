package com.xa.xpensauditor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ProfileManagement extends AppCompatActivity {
    private TextView NameView,EmailView,PhnView,AddressView;
    private Button Func;
    private ListView catView;
//    private ArrayList<String> Catg=new ArrayList<>();
    private Firebase mRootRef;
    private Firebase RefUid;
    private Firebase RefName,RefEmail,RefPhnnum;
    private Firebase RefCat,RefFood,RefHealth,RefTravel,RefEdu,RefBills,RefHomeNeeds,RefOthers,RefUncat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_management);

        NameView = (TextView) findViewById(R.id.getName);
        EmailView = (TextView) findViewById(R.id.getMail);
        PhnView = (TextView) findViewById(R.id.getPhnnum);
        Func = (Button) findViewById(R.id.Funct);
        Func.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TODO
//                startActivity(new Intent(ProfileManagement.this,Functionalities.class));
                Toast.makeText(getApplicationContext(),"To be updated in later version",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
