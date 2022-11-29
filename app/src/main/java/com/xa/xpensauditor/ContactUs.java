package com.xa.xpensauditor;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ContactUs extends AppCompatActivity implements View.OnClickListener{

//    Button Email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);


        getSupportActionBar().setDisplayHomeAsUpEnabled(false);


    }

    @Override
    public void onClick(View view) {
        //listener
    }
}
