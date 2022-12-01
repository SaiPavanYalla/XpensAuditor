package com.xa.xpensauditor;

import static java.lang.System.currentTimeMillis;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
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
import com.google.firebase.database.core.Tag;

import org.apache.commons.collections4.map.MultiValueMap;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.UUID;

// add mailjet libraries for sending emails etc.
import com.mailjet.*;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.resource.Emailv31;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddTransactionActivity extends AppCompatActivity {

    private Firebase mRootRef;
    private Firebase RefUid;
    private Firebase RefTran1, RefCatSum1, RefCat, UnCatTran;
    private String Tid;
    private ArrayList<String> Catg = new ArrayList<>();
    private Button AddTran;
    private EditText Amnt;
    private EditText ShpNm;
    private EditText SharedUsersString;
    private EditText ownCatEditText;
    private Spinner catView;
    String Amount, ShopName, SelCat;
    private DatePicker dateTransac;
    String day, month, year;
    int d, m, y;
    Activity activity;
    String[] SharedUsersList;
    boolean ownCat;
    MultiValueMap<String, String> catgTrans1 = MultiValueMap.multiValueMap(new LinkedHashMap<String, Collection<String>>(), (Class<LinkedHashSet<String>>) (Class<?>) LinkedHashSet.class);

    protected void sendEmailUpdateToSharedUsers(String[] emails, String amount, String shop_name, String category) {
        Toast.makeText(activity, "TODO - SEND EMAIL TO THE USERS IN THE LIST", Toast.LENGTH_SHORT).show();
        for(String email: emails) {
            new EmailSender(email, amount, shop_name, category).execute();
        }
        return;
    }

    public static void printStringList(String[] values) {
        for(String j: values)
            printLog(j);
    }

    public static void printLog(String message) {
        Log.e("app core error message", message);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        activity = this;

        mRootRef = new Firebase("https://xpensauditor-g11-default-rtdb.firebaseio.com/");

        mRootRef.keepSynced(true);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String Uid = auth.getUid();
        RefUid = mRootRef.child(Uid);

        RefCat = RefUid.child("Categories");
        UnCatTran = RefUid.child("UnCatTran");

        AddTran = (Button) findViewById(R.id.btAddTransaction);
        Amnt = (EditText) findViewById(R.id.addTransAmt);
        ShpNm = (EditText) findViewById(R.id.addShopName);
        catView = (Spinner) findViewById(R.id.spinTrans);
        SharedUsersString = (EditText) findViewById(R.id.addSharedUserEmail);
    //    ownCatEditText = (EditText) findViewById(R.id.addOwnCategory);

        dateTransac = (DatePicker) findViewById(R.id.dateTrans);
        day = String.valueOf(dateTransac.getDayOfMonth());
        month = String.valueOf(dateTransac.getMonth() + 1);
        year = String.valueOf(dateTransac.getYear());
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Catg);
        catView.setAdapter(arrayAdapter);

        InputFilter filter = new InputFilter() {

            final int maxDigitsBeforeDecimalPoint = 8;
            final int maxDigitsAfterDecimalPoint = 2;

            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                StringBuilder builder = new StringBuilder(dest);
                builder.replace(dstart, dend, source
                        .subSequence(start, end).toString());
                if (!builder.toString().matches(
                        "(([1-9]{1})([0-9]{0," + (maxDigitsBeforeDecimalPoint - 1) + "})?)?(\\.[0-9]{0," + maxDigitsAfterDecimalPoint + "})?"

                )) {
                    if (source.length() == 0)
                        return dest.subSequence(dstart, dend);
                    return "";
                }
                return null;

            }
        };


        Amnt.setFilters(new InputFilter[]{filter});


        catView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelCat = parent.getItemAtPosition(position).toString();
                if(SelCat == "Create Your Own Category"){
                    ownCatEditText.setVisibility(View.VISIBLE);
                    ownCat = true;
                }
                else{
                    ownCatEditText.setVisibility(View.GONE);
                    ownCat = false;
                }
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
                
                // day, month, year retrieval
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
                    Amount = Amnt.getText().toString().trim().replaceAll(",", "");
                    ShopName = ShpNm.getText().toString().trim();
                    SharedUsersList = SharedUsersString.getText().toString().split(",");
                    SelCat = ownCat ? ownCatEditText.getText().toString() : SelCat;
                    if (!Amount.isEmpty() && !ShopName.isEmpty() && !SelCat.isEmpty()) {
                        Tid = String.valueOf(currentTimeMillis());

                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("Transactions").child(Tid).child("Amount").setValue(Amount);
                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("Transactions").child(Tid).child("Category").setValue(SelCat);
                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("Transactions").child(Tid).child("Shop Name").setValue(ShopName);
                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("Transactions").child(Tid).child("Shared With").setValue(SharedUsersList);
                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("Transactions").child(Tid).child("ZMessage").setValue("Entered Manually...");
                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("Transactions").child(Tid).child("Day").setValue(day);
                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("Transactions").child(Tid).child("Month").setValue(month);
                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("Transactions").child(Tid).child("Year").setValue(year);


                        if (SelCat == "Uncategorised") {
                            UnCatTran.child(Tid);
                            UnCatTran.child(Tid).child("Amount").setValue(Amount);
                            UnCatTran.child(Tid).child("Category").setValue(SelCat);
                            UnCatTran.child(Tid).child("Shop Name").setValue(ShopName);
                            UnCatTran.child(Tid).child("Shared With").setValue(SharedUsersList);
                            UnCatTran.child(Tid).child("ZMessage").setValue("Entered Manually...");
                            UnCatTran.child(Tid).child("Day").setValue(day);
                            UnCatTran.child(Tid).child("Month").setValue(month);
                            UnCatTran.child(Tid).child("Year").setValue(year);
                        } else {
                            RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("CatTran").child(SelCat).child(Tid).child("Amount").setValue(Amount);
                            RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("CatTran").child(SelCat).child(Tid).child("Category").setValue(SelCat);
                            RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("CatTran").child(SelCat).child(Tid).child("Shop Name").setValue(ShopName);
                            RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("CatTran").child(SelCat).child(Tid).child("Shared With").setValue(SharedUsersList);
                            RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("CatTran").child(SelCat).child(Tid).child("ZMessage").setValue("Entered Manually...");
                            RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("CatTran").child(SelCat).child(Tid).child("Day").setValue(day);
                            RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("CatTran").child(SelCat).child(Tid).child("Month").setValue(month);
                            RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("CatTran").child(SelCat).child(Tid).child("Year").setValue(year);
                        }


                        RefTran1 = RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("Transactions");

                        RefCatSum1 = RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("CatSum");

                        if ( SharedUsersList.length != 0 ) {
                            printLog("shared email");
                            printStringList(SharedUsersList);
                            printLog("amount: ");
                            printLog(Amount);
                            printLog("shop name: ");
                            printLog(ShopName);
                            printLog("category : ");
                            printLog(SelCat);
                            sendEmailUpdateToSharedUsers(SharedUsersList, Amount, ShopName, SelCat);
                        }

                        Toast.makeText(getApplicationContext(), "Transaction added", Toast.LENGTH_SHORT).show();
                        Amnt.setText("");
                        ShpNm.setText("");
                        SharedUsersString.setText("");
                        Toast.makeText(getApplicationContext(), "Add one more transaction or press back", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getApplicationContext(), "Enter valid Amount, Shopname and Category", Toast.LENGTH_LONG).show();
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
            int i=0;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            i++;
            Intent j = new Intent(AddTransactionActivity.this, HomeActivity.class);
            startActivity(j);

    }
}
