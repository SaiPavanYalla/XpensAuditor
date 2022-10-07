package com.xa.xpensauditor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.MonthDisplayHelper;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMSReaderActivity extends AppCompatActivity {
    String strDate, shopName = "Unknown", smsMessagestr = "Couldn't fetch";
    String StrLastRefDate="0";
    long MaxDate=0;
    long year, day, month;
    int yyyy,dd,mm;
    private Firebase mRootRef;
    private Firebase RefUid, UnCatTran, RefDUncatTran, LastRefreshDate;
    private String Tid;
    Double amt = 0.00;
    boolean flmsg = false, flsh = false, flamt = false, fldate = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsreader);
        StrLastRefDate = getIntent().getStringExtra("StrLastRefDate");
        mRootRef = new Firebase("https://xpensauditor-default-rtdb.firebaseio.com/");
        mRootRef.keepSynced(true);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String Uid = auth.getUid();
        RefUid = mRootRef.child(Uid);
        UnCatTran = RefUid.child("UnCatTran");
        LastRefreshDate = RefUid.child("LastRefreshDate");


        MaxDate=Long.parseLong(StrLastRefDate);

        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                String msgData = "";
                for (int idx = 0; idx < cursor.getColumnCount(); idx++) {
                    msgData += " " + cursor.getColumnName(idx) + ":" + cursor.getString(idx);
                }

                if(msgData.contains("debited")) {
                    String[] words = msgData.split(" ");

                    for (int i = 0; i < words.length; i++) {
                        if (words[i].contains("date_sent:")) {
                            strDate = words[i].substring(10);
                            Log.d("msgcomp","msgdate:"+strDate+" "+"fetchDate:"+StrLastRefDate);
                            if(Long.parseLong(strDate) <= Long.parseLong(StrLastRefDate)){
                                Log.d("msgcomp","Break");
                                break;
                            }
                            if(MaxDate < Long.parseLong(strDate)){
                                MaxDate = Long.parseLong(strDate);
                            }
                            long value = Long.parseLong(strDate);
                            strDate = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date(value));
                            year = Long.parseLong(strDate.substring(6, 10));
                            month = Long.parseLong(strDate.substring(0, 2));
                            day = Long.parseLong(strDate.substring(3, 5));
                            yyyy=(int)year;
                            mm=(int) month;
                            dd=(int) day;
                            strDate = day + "/" + month + "/" + year;
                            fldate = true;
                            smsMessagestr="";
                        }
                        if (words[i].startsWith("service_center:")) {
                            flmsg = false;
                        }
                        if (flmsg) {
                            smsMessagestr = smsMessagestr + words[i] + " ";
                        }
                        if (words[i].startsWith("body:")) {
                            flmsg = true;
                            smsMessagestr = words[i].substring(5) + " ";
                        }

                        if (words[i].contains("$")) {
                            amt = Double.parseDouble(words[i].substring(1));
                            flamt = true;
                        }
                        if (words[i].equals("at")) {
                            shopName = words[i + 1];
                            flsh = true;
                        }


                    }
                    if (flsh && flamt && fldate) {
                        RefUid.child("LastRefreshDate").setValue(MaxDate);
                        Tid = UUID.randomUUID().toString();
                        RefUid.child("UnCatTran").child(Tid);
                        RefUid.child("UnCatTran").child(Tid).child("Amount").setValue(amt);
                        RefUid.child("UnCatTran").child(Tid).child("Category").setValue("Uncategorised");
                        RefUid.child("UnCatTran").child(Tid).child("Shop Name").setValue(shopName);
                        RefUid.child("UnCatTran").child(Tid).child("ZMessage").setValue(smsMessagestr);
                        RefUid.child("UnCatTran").child(Tid).child("Day").setValue(String.valueOf(dd));
                        RefUid.child("UnCatTran").child(Tid).child("Month").setValue(String.valueOf(mm));
                        RefUid.child("UnCatTran").child(Tid).child("Year").setValue(String.valueOf(yyyy));

                        RefUid.child("DateRange");
                        RefUid.child("DateRange").child(String.valueOf(mm+"-"+yyyy));
                        RefUid.child("DateRange").child(String.valueOf(mm+"-"+yyyy)).child("Transactions");
                        RefUid.child("DateRange").child(String.valueOf(mm+"-"+yyyy)).child("Transactions").child(Tid);
                        RefUid.child("DateRange").child(String.valueOf(mm+"-"+yyyy)).child("Transactions").child(Tid).child("Amount").setValue(amt);
                        RefUid.child("DateRange").child(String.valueOf(mm+"-"+yyyy)).child("Transactions").child(Tid).child("Category").setValue("Uncategorised");
                        RefUid.child("DateRange").child(String.valueOf(mm+"-"+yyyy)).child("Transactions").child(Tid).child("Shop Name").setValue(shopName);
                        RefUid.child("DateRange").child(String.valueOf(mm+"-"+yyyy)).child("Transactions").child(Tid).child("ZMessage").setValue(smsMessagestr);
                        RefUid.child("DateRange").child(String.valueOf(mm+"-"+yyyy)).child("Transactions").child(Tid).child("Day").setValue(dd);
                        RefUid.child("DateRange").child(String.valueOf(mm+"-"+yyyy)).child("Transactions").child(Tid).child("Month").setValue(mm);
                        RefUid.child("DateRange").child(String.valueOf(mm+"-"+yyyy)).child("Transactions").child(Tid).child("Year").setValue(yyyy);
                        flsh=false;
                        flamt=false;
                        fldate=false;
                    }
                    }

                }
                while (cursor.moveToNext()) ;
            }
        Intent i=new Intent(this,HomeActivity.class);
        startActivity(i);
        }

    }
