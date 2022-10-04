package com.xa.xpensauditor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;

import org.apache.commons.collections4.map.MultiValueMap;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.UUID;

public class AddTransactionActivity extends AppCompatActivity {

    private Firebase mRootRef;
    private Firebase RefUid;
    private Firebase RefTran1, RefCatSum1, RefCat;
    private String Tid;
    private ArrayList<String> Catg = new ArrayList<>();
    private Button AddTran;
    private EditText Amnt;
    private EditText ShpNm;
    private Spinner catView;
    String Amount, ShopName, SelCat;
    private DatePicker dateTransac;
    String day, month, year;
    int d, m, y;
    MultiValueMap<String, String> catgTrans1 = MultiValueMap.multiValueMap(new LinkedHashMap<String, Collection<String>>(), (Class<LinkedHashSet<String>>) (Class<?>) LinkedHashSet.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        mRootRef = new Firebase("https://xpensauditor-default-rtdb.firebaseio.com/");

        mRootRef.keepSynced(true);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String Uid = auth.getUid();
        RefUid = mRootRef.child(Uid);

        RefCat = RefUid.child("Categories");
        AddTran = (Button) findViewById(R.id.btAddTransaction);
        Amnt = (EditText) findViewById(R.id.addTransAmt);
        ShpNm = (EditText) findViewById(R.id.addShopName);
        catView = (Spinner) findViewById(R.id.spinTrans);

        dateTransac = (DatePicker) findViewById(R.id.dateTrans);
        day = String.valueOf(dateTransac.getDayOfMonth());
        month = String.valueOf(dateTransac.getMonth() + 1);
        year = String.valueOf(dateTransac.getYear());
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Catg);
        catView.setAdapter(arrayAdapter);
        catView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelCat = parent.getItemAtPosition(position).toString();
                // Toast.makeText(getApplicationContext(), "??"+SelCat, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(getApplicationContext(), "Select a Category", Toast.LENGTH_SHORT).show();
            }
        });


        AddTran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean f = false;
                Calendar calendar = Calendar.getInstance();

                int thisYear = calendar.get(Calendar.YEAR);
                // Log.d(TAG, "# thisYear : " + thisYear);

                int thisMonth = calendar.get(Calendar.MONTH) + 1;
                //Log.d(TAG, "@ thisMonth : " + thisMonth);

                int thisDay = calendar.get(Calendar.DAY_OF_MONTH);
                //Log.d(TAG, "$ thisDay : " + thisDay);
                d = (dateTransac.getDayOfMonth());
                m = (dateTransac.getMonth() + 1);
                y = (dateTransac.getYear());
                day = String.valueOf(d);
                month = String.valueOf(m);
                year = String.valueOf(y);
                if (thisYear > y) {
                    f = true;
                } else {
                    if (thisYear == y) {
                        if (thisMonth > m) {
                            f = true;
                        } else {
                            if (thisMonth == m) {
                                if (thisDay >= d) {
                                    f = true;
                                }
                            }
                        }
                    }
                }


                if (f) {
                    Amount = Amnt.getText().toString().trim();
                    ShopName = ShpNm.getText().toString().trim();
                    if (!Amount.isEmpty() && !ShopName.isEmpty()) {
                        Tid = UUID.randomUUID().toString();
                        /*RefTran.child(Tid);
                        RefTran.child(Tid).child("Shop Name");
                        RefTran.child(Tid).child("Amount");
                        RefTran.child(Tid).child("Category");
                        RefTran.child(Tid).child("Amount").setValue(Amount);
                        RefTran.child(Tid).child("Category").setValue(SelCat);
                        RefTran.child(Tid).child("Shop Name").setValue(ShopName);
                        RefTran.child(Tid).child("ZMessage").setValue("Entered Manually...");
                        RefTran.child(Tid).child("Day").setValue(day);
                        RefTran.child(Tid).child("Month").setValue(month);
                        RefTran.child(Tid).child("Year").setValue(year);

                        RefUid.child("CatTran").child(SelCat).child(Tid);
                        RefUid.child("CatTran").child(SelCat).child(Tid).child("Amount").setValue(Amount);
                        RefUid.child("CatTran").child(SelCat).child(Tid).child("Category").setValue(SelCat);
                        RefUid.child("CatTran").child(SelCat).child(Tid).child("Shop Name").setValue(ShopName);
                        RefUid.child("CatTran").child(SelCat).child(Tid).child("ZMessage").setValue("Entered Manually...");
                        RefUid.child("CatTran").child(SelCat).child(Tid).child("Day").setValue(day);
                        RefUid.child("CatTran").child(SelCat).child(Tid).child("Month").setValue(month);
                        RefUid.child("CatTran").child(SelCat).child(Tid).child("Year").setValue(year);*/


                        RefUid.child("DateRange").child(String.valueOf(month+"-"+year)).child("Transactions").child(Tid).child("Amount").setValue(Amount);
                        RefUid.child("DateRange").child(String.valueOf(month+"-"+year)).child("Transactions").child(Tid).child("Category").setValue(SelCat);
                        RefUid.child("DateRange").child(String.valueOf(month+"-"+year)).child("Transactions").child(Tid).child("Shop Name").setValue(ShopName);
                        RefUid.child("DateRange").child(String.valueOf(month+"-"+year)).child("Transactions").child(Tid).child("ZMessage").setValue("Entered Manually...");
                        RefUid.child("DateRange").child(String.valueOf(month+"-"+year)).child("Transactions").child(Tid).child("Day").setValue(day);
                        RefUid.child("DateRange").child(String.valueOf(month+"-"+year)).child("Transactions").child(Tid).child("Month").setValue(month);
                        RefUid.child("DateRange").child(String.valueOf(month+"-"+year)).child("Transactions").child(Tid).child("Year").setValue(year);

                        RefUid.child("DateRange").child(String.valueOf(month+"-"+year)).child("CatTran").child(SelCat).child(Tid).child("Amount").setValue(Amount);
                        RefUid.child("DateRange").child(String.valueOf(month+"-"+year)).child("CatTran").child(SelCat).child(Tid).child("Category").setValue(SelCat);
                        RefUid.child("DateRange").child(String.valueOf(month+"-"+year)).child("CatTran").child(SelCat).child(Tid).child("Shop Name").setValue(ShopName);
                        RefUid.child("DateRange").child(String.valueOf(month+"-"+year)).child("CatTran").child(SelCat).child(Tid).child("ZMessage").setValue("Entered Manually...");
                        RefUid.child("DateRange").child(String.valueOf(month+"-"+year)).child("CatTran").child(SelCat).child(Tid).child("Day").setValue(day);
                        RefUid.child("DateRange").child(String.valueOf(month+"-"+year)).child("CatTran").child(SelCat).child(Tid).child("Month").setValue(month);
                        RefUid.child("DateRange").child(String.valueOf(month+"-"+year)).child("CatTran").child(SelCat).child(Tid).child("Year").setValue(year);



                        RefTran1 = RefUid.child("DateRange").child(String.valueOf(month+"-"+year)).child("Transactions");

                        RefCatSum1= RefUid.child("DateRange").child(String.valueOf(month+"-"+year)).child("CatSum");


                        Toast.makeText(getApplicationContext(),"Transaction added",Toast.LENGTH_SHORT).show();
                        Amnt.setText("");
                        ShpNm.setText("");
                        Toast.makeText(getApplicationContext(),"Add one more transaction or press back",Toast.LENGTH_LONG).show();

                        //Toast.makeText(getApplicationContext(), Amount + ":" + ShopName + ":" + day + "/" + month + "/" + year, Toast.LENGTH_SHORT).show();

                        // startActivity(new Intent(AddTransactionActivity.this, ProfileManagement.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Enter valid Amount and Shopname", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Enter valid date", Toast.LENGTH_LONG).show();
                }


                RefTran1.addChildEventListener(new com.firebase.client.ChildEventListener() {
                    String amount, cat, shname, shDay, shMonth, shYear;

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
        });
        RefCat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String value = dataSnapshot.getKey().trim();
                Catg.add(value);
                arrayAdapter.notifyDataSetChanged();
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

    public void onBackPressed() {
        Iterator<String> mapIter1 = catgTrans1.keySet().iterator();


        if(mapIter1.hasNext()) {

            while (mapIter1.hasNext()) {
                String key = mapIter1.next();
//                    Toast.makeText(getApplicationContext(), "Value: " + key + ":" + catgTrans.get(key), Toast.LENGTH_SHORT).show();
                Collection<String> val = catgTrans1.getCollection(key);
                SumTrans obj = new SumTrans();
                RefCatSum1.child(key).setValue(obj.computeSum(val).toString());

            }
        }

        Intent i = new Intent(AddTransactionActivity.this, HomeActivity.class);
        startActivity(i);

    }
}