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

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false); // disable the button
            actionBar.setDisplayHomeAsUpEnabled(false); // remove the left caret
            actionBar.setDisplayShowHomeEnabled(false); // remove the icon
        }
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

    public void SendMailvineeth(View view) {
        sendmail("vineethdasi22@gmail.com");
    }

    public void SendMailsunandini(View view) {
        sendmail("sunandini.medisetti@gmail.com");
    }

    public void SendMailsahiti(View view) {
        sendmail("sahithi.ammana@gmail.com");
    }

    public void SendMailpavan(View view) {
        sendmail("ysaipavan99@gmail.com");
    }

    public void SendMailmithila(View view) {
        sendmail("mithilareddy1999@gmail.com");
    }

    public void callSupport(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:0123456789"));
        startActivity(intent);
    }
}