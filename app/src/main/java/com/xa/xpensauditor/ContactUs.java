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
    }

    private void sendmail(String mail) {
        String mailto = "mailto:"+mail +
                "?cc=" +
                "&subject=" + Uri.encode("Support Needed") +
                "&body=" + Uri.encode("i need support");
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse(mailto));

        try {
            startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), "Error to open email app", Toast.LENGTH_SHORT).show();
        }
    }


    public void callSupport(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:0123456789"));
        startActivity(intent);
    }

    public void SendMail(View view) {
        sendmail("xpensauditor@gmail.com");
    }
}