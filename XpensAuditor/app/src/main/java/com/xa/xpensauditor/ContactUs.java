package com.xa.xpensauditor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ContactUs extends AppCompatActivity implements View.OnClickListener{

    Button SMS,Call,Email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        SMS = (Button) findViewById(R.id.sendSMS);
        Call = (Button)findViewById(R.id.call);
        Email = (Button)findViewById(R.id.sendEmail);

        SMS.setOnClickListener(this);
        Call.setOnClickListener(this);
        Email.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendSMS: {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + "9790084862"));
                intent.putExtra("sms_body", "Test Message");
                startActivity(intent);

            }
            break;

            case R.id.sendEmail: {


                String[] TO = {"13pramo@gmail.com"};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");


                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Some subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Test message");

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    finish();
                    Log.i("Finished sending email", "");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }


            }
            break;

            case R.id.call: {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:9491699997"));

                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);

            }
            break;
        }
    }
}