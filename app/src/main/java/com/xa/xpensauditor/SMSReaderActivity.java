package com.xa.xpensauditor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.MonthDisplayHelper;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.apache.commons.collections4.map.MultiValueMap;

import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SMSReaderActivity extends AppCompatActivity {
    String strDate,epochDate, shopName = "Unknown", smsMessagestr = "Couldn't fetch";
    long year, day, month;
    int yyyy,dd,mm;
    private Firebase mRootRef;
    private Firebase RefUid, UnCatTran, RefDUncatTran, RefTran1, RefCatSum1;
    private String Tid;
    Double amt = 0.00,DoubSum=0.0;
    boolean flsh = false, flamt = false, fldate = false;
    MultiValueMap<String, String> catgTrans1 = MultiValueMap.multiValueMap(new LinkedHashMap<String, Collection<String>>(), (Class<LinkedHashSet<String>>) (Class<?>) LinkedHashSet.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsreader);
        mRootRef = new Firebase("https://xpense-auditor-default-rtdb.firebaseio.com");
        mRootRef.keepSynced(true);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String Uid = auth.getUid();
        RefUid = mRootRef.child(Uid);
        UnCatTran = RefUid.child("UnCatTran");
        MultiValueMap<String, String> catgTrans1 = MultiValueMap.multiValueMap(new LinkedHashMap<String, Collection<String>>(), (Class<LinkedHashSet<String>>) (Class<?>) LinkedHashSet.class);

        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/inbox"), null, null, null, null);
        if (cursor.moveToFirst()) { // must check the result to prevent exception
            do {
                for (int idx = 0; idx < cursor.getColumnCount(); idx++) {
                    //Log.d("yomsg",cursor.getColumnName(idx));
                    if (cursor.getColumnName(idx).equals("date")) {
                        //Log.d("yomsg",cursor.getColumnName(idx));
                        strDate = cursor.getString(idx);
                        epochDate = strDate;
                        //Log.d("msgcomp", "msgdate:" + strDate);
                        long value = Long.parseLong(strDate);
                        strDate = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date(value));
                        year = Long.parseLong(strDate.substring(6, 10));
                        month = Long.parseLong(strDate.substring(0, 2));
                        day = Long.parseLong(strDate.substring(3, 5));
                        yyyy = (int) year;
                        mm = (int) month;
                        dd = (int) day;
                        strDate = day + "/" + month + "/" + year;
                        fldate = true;
                    }
                    if (cursor.getColumnName(idx).equals("body")) {
                        smsMessagestr = cursor.getString(idx);
                        //Log.d("yomsg",smsMessagestr);
                        if (smsMessagestr.contains("debited")) {
                            String[] words = smsMessagestr.split(" ");
                            for (int i = 0; i < words.length; i++) {
                                if (words[i].contains("$")) {
                                    amt = Double.parseDouble(words[i].substring(1));
                                    flamt = true;
                                }
                                if (words[i].equals("at")) {
                                    shopName = words[i + 1];
                                    flsh = true;
                                }
                            }
                        }
                    }
                }
                if (flsh && flamt && fldate) {
                    Tid = epochDate;

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

                    RefTran1 = RefUid.child("DateRange").child(String.valueOf(mm+"-"+yyyy)).child("Transactions");
                    RefTran1.addChildEventListener(new com.firebase.client.ChildEventListener() {
                        String amount, cat;
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            int i = 0;

                            for (DataSnapshot S : dataSnapshot.getChildren()) {

                                switch (i) {
                                    case 0:
                                        amount = S.getValue().toString().trim();
                                        break;
                                    case 1:
                                        cat = S.getValue().toString().trim();
                                        break;
                                }
                                i++;
                            }
                            catgTrans1.put(cat, amount);
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });


                }


                }
                while (cursor.moveToNext()) ;
            }

        Iterator<String> mapIter1 = catgTrans1.keySet().iterator();
        Log.d("yokey",String.valueOf(mapIter1.hasNext()));
        //RefCatSum1= RefUid.child("DateRange").child(String.valueOf(month+"-"+year)).child("CatSum");
//        if(mapIter1.hasNext()) {
//
//            while (mapIter1.hasNext()) {
//                String key = mapIter1.next();
//
//                Collection<String> val = catgTrans1.getCollection(key);
//                SumTrans obj = new SumTrans();
//                RefCatSum1.child(key).setValue(obj.computeSum(val).toString());
//
//            }
//        }

        Intent i=new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(i);

        }
    }
