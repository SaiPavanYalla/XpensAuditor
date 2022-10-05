package com.xa.xpensauditor;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMSReaderActivity extends AppCompatActivity {
    String strDate, shopName="";
    int flag, preflag, reCheck, year,day,month;
    private Firebase mRootRef;
    private Firebase RefUid,UnCatTran,RefDUncatTran;
    private String Tid ;
    Double amt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsreader);
        mRootRef=new Firebase("https://xpensauditor-default-rtdb.firebaseio.com/");
        mRootRef.keepSynced(true);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String Uid=auth.getUid();
        RefUid= mRootRef.child(Uid);
        UnCatTran = RefUid.child("UnCatTran");
        //todo
        //RefDUncatTran = RefUid.child(String.valueOf(mm)+"-"+ String.valueOf(y)).child("UnCatTran");

        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                String msgData = "";
                for(int idx=0;idx<cursor.getColumnCount();idx++)
                {
                    msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);
                }

                String[] words = msgData.split(" ");

                for(int i=0;i< words.length;i++){
                    if(words[i].contains("date_sent:")){
                        strDate = words[i].substring(10);
                        long value = Long.parseLong(strDate);
                        strDate = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date (value));
                        value=value/1000;
                        year = Integer.parseInt(strDate.substring(6,10));
                        month = Integer.parseInt(strDate.substring(0,2));
                        day = Integer.parseInt(strDate.substring(3,5));
                        strDate = day+"/"+month+"/"+year;
                    }
                }





            } while (cursor.moveToNext());
        }
    }
}