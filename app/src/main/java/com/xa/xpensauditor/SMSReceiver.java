package com.xa.xpensauditor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class SMSReceiver extends BroadcastReceiver {
    public static final String SMS_BUNDLE = "pdus";
    String smsMessagestr, shopName="";
    int flag, preflag, reCheck;
    private Firebase mRootRef;
    private Firebase RefUid,UnCatTran,RefDUncatTran;
    private String Tid ;
    Double amt;

    @Override
    public void onReceive(Context context, Intent intent) {

        Calendar c = Calendar.getInstance();
        int d = c.get(Calendar.DAY_OF_MONTH);
        int mm = c.get(Calendar.MONTH)+1;
        int y = c.get(Calendar.YEAR);

        mRootRef=new Firebase("https://xpensauditor-default-rtdb.firebaseio.com/");
        mRootRef.keepSynced(true);
        com.google.firebase.auth.FirebaseAuth auth = FirebaseAuth.getInstance();
        String Uid=auth.getUid();
        RefUid= mRootRef.child(Uid);
        UnCatTran = RefUid.child("UnCatTran");
        RefDUncatTran = RefUid.child(String.valueOf(mm)+"-"+ String.valueOf(y)).child("UnCatTran");



        Bundle intentExtras = intent.getExtras();
        if(intentExtras!=null){
            Object[] sms = (Object[])intentExtras.get(SMS_BUNDLE);
            smsMessagestr="";
            for(int i=0;i<sms.length;i++){
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[])sms[i]);
                String smsBody = smsMessage.getMessageBody().toString();
                String address = smsMessage.getOriginatingAddress();

                smsMessagestr += "SMS From: "+address+"\n";
                smsMessagestr += smsBody+"\n";
                Toast.makeText(context, smsMessagestr, Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(context, smsMessagestr, Toast.LENGTH_SHORT).show();

            String[] words = smsMessagestr.split(" ");
            for(int k=0; k<words.length;k++){
                if(words[k].equalsIgnoreCase("SBI")){
                    flag=1;
                    break;
                }

            }

            String word="";
            if(flag==1) {
                for (int k = 0; k < words.length; k++) {
                    if (words[k].contains("Rs")) {
                        word = words[k].replaceAll(",", "");
                        amt = Double.parseDouble((word.substring(2)));
                        break;
                    }
                }


                Calendar calendar = Calendar.getInstance();
                int day = (calendar.get(Calendar.DAY_OF_MONTH));
                int month = (calendar.get(Calendar.MONTH)+1);
                int year = (calendar.get(Calendar.YEAR));
                SimpleDateFormat mdFormat = new SimpleDateFormat("dd/MM/yyyy");
                String strDate = "Current date: " + mdFormat.format(calendar.getTime());

                int k;

                for (k = 1; k < words.length; k++) {
                    if (words[k - 1].equalsIgnoreCase("at") || words[k - 1].equalsIgnoreCase("@")) {
                        preflag = 1;
                        reCheck = k;

                    }

                    for (; reCheck < words.length; reCheck++) {
                        if (words[reCheck].equalsIgnoreCase("txn#"))
                            break;
                    }


                    if (preflag == 1)
                        break;
                }
                for (int m = k; m < reCheck; m++)
                    shopName += words[m];
                Toast.makeText(context, "Amount:" + amt + "Shop Name:" + shopName + day+"/"+month+"/"+year, Toast.LENGTH_SHORT).show();



                Tid = UUID.randomUUID().toString();

                UnCatTran.child(Tid).child("Amount").setValue((String.valueOf(Math.round(amt))));
                UnCatTran.child(Tid).child("Category").setValue("Uncategorised");
                UnCatTran.child(Tid).child("Shop Name").setValue(shopName);
                UnCatTran.child(Tid).child("ZMessage").setValue(smsMessagestr);
                UnCatTran.child(Tid).child("Day").setValue(String.valueOf(day));
                UnCatTran.child(Tid).child("Month").setValue(String.valueOf(month));
                UnCatTran.child(Tid).child("Year").setValue(String.valueOf(year));

                Toast.makeText(context,smsMessagestr,Toast.LENGTH_LONG).show();

            }
        }
    }
}
