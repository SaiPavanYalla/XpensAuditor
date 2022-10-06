package com.xa.xpensauditor;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




//        SMS = (Button) findViewById(R.id.sendSMS);
//        Call = (Button)findViewById(R.id.call);
//        Email = (Button)findViewById(R.id.sendEmail);

//        SMS.setOnClickListener(this);
//        Call.setOnClickListener(this);
//        Email.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
//        switch (view.getId()) {
////            case R.id.sendSMS: {
////
////                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + "9790084862"));
////                intent.putExtra("sms_body", "Test Message");
////                startActivity(intent);
////
////            }
////            break;
//
//            case R.id.sendEmail: {
//
//
//                String[] TO = {"13pramo@gmail.com"};
//                Intent emailIntent = new Intent(Intent.ACTION_SEND);
//                emailIntent.setData(Uri.parse("mailto:"));
//                emailIntent.setType("text/plain");
//
//
//                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
//                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Some subject");
//                emailIntent.putExtra(Intent.EXTRA_TEXT, "Test message");
//
//                try {
//                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
//                    finish();
//                    Log.i("Finished sending email", "");
//                } catch (android.content.ActivityNotFoundException ex) {
//                    Toast.makeText(this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
//                }
//
//
//            }
//            break;
//
////            case R.id.call: {
////
////                Intent callIntent = new Intent(Intent.ACTION_CALL);
////                callIntent.setData(Uri.parse("tel:9491699997"));
////
////                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
////                    return;
////                }
////                startActivity(callIntent);
////
////            }
////            break;
//        }
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