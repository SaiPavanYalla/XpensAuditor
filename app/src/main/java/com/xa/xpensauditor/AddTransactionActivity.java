package com.xa.xpensauditor;

import static java.lang.System.currentTimeMillis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextDirectionHeuristic;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import org.apache.commons.collections4.map.MultiValueMap;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddTransactionActivity extends AppCompatActivity {

    private Firebase mRootRef;
    private Firebase RefUid;
    private Firebase RefTran1, RefCatSum1, RefCat; //, UnCatTran;
    private String tid;
    private ArrayList<String> categoryList = new ArrayList<>();
    private Button addButton;
    private EditText amountEditText;
    private EditText shopNameEditText;
    private TextView categoryTextView;
    Dialog dialog;
    String amountStr, shopNameStr, categoryStr;
    private DatePicker transactionDatePicker;
    String day, month, year;
    int d, m, y;
//    Activity activity;
    MultiValueMap<String, String> catgTrans1 = MultiValueMap.multiValueMap(new LinkedHashMap<String, Collection<String>>(), (Class<LinkedHashSet<String>>) (Class<?>) LinkedHashSet.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
//        activity = this;

        mRootRef = new Firebase("https://xpense-auditor-default-rtdb.firebaseio.com");

        mRootRef.keepSynced(true);
        FirebaseAuth auth = FirebaseAuth.getInstance();String Uid1 = auth.getUid();
        if (getIntent().getExtras() != null && !getIntent().getExtras().getString("group_key").isEmpty()) {
            Uid1 = getIntent().getExtras().getString("group_key");
        }

        final String Uid = Uid1;
        RefUid = mRootRef.child(Uid);

        RefCat = RefUid.child("Categories");
//        UnCatTran = RefUid.child("UnCatTran");

        addButton = (Button) findViewById(R.id.btAddTransaction);
        amountEditText = (EditText) findViewById(R.id.addTransAmt);
        shopNameEditText = (EditText) findViewById(R.id.addShopName);

        categoryTextView = (TextView) findViewById(R.id.textViewCategory);

        transactionDatePicker = (DatePicker) findViewById(R.id.dateTrans);
        day = String.valueOf(transactionDatePicker.getDayOfMonth());
        month = String.valueOf(transactionDatePicker.getMonth() + 1);
        year = String.valueOf(transactionDatePicker.getYear());

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


        amountEditText.setFilters(new InputFilter[]{filter});

        categoryTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Initialize dialog
                dialog = new Dialog(AddTransactionActivity.this);

                // set custom dialog
                dialog.setContentView(R.layout.dialog_searchable_spinner);

                // set custom height and width
                dialog.getWindow().setLayout(720,1080);

                // set transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                // show dialog
                dialog.show();

                // Initialize and assign variable
                EditText editTextCat = dialog.findViewById(R.id.edittext_category);
                ListView listViewCat = dialog.findViewById(R.id.listview_category);

                // Initialize array adapter
                ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(AddTransactionActivity.this, android.R.layout.simple_list_item_1, categoryList);

                // set adapter
                listViewCat.setAdapter(arrayAdapter);
                editTextCat.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        arrayAdapter.getFilter().filter(s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                listViewCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // when item selected from list
                        // set selected item on textView
                        categoryTextView.setText(arrayAdapter.getItem(position));
                        categoryStr = arrayAdapter.getItem(position);
                        // Dismiss dialog
                        dialog.dismiss();
                    }
                });

                editTextCat.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        // If the event is a key-down event on the "enter" button
                        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                                (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            // Perform action on key press
                            categoryList.add(editTextCat.getText().toString());
                            arrayAdapter.notifyDataSetChanged();
                            categoryTextView.setText(editTextCat.getText().toString());
                            categoryStr = editTextCat.getText().toString();
                            // Dismiss dialog
                            dialog.dismiss();
                            Toast.makeText(AddTransactionActivity.this, "Added category - " + editTextCat.getText().toString(), Toast.LENGTH_SHORT).show();
                            RefCat.child(editTextCat.getText().toString()).setValue("");

                            return true;
                        }
                        return false;
                    }
                });
            }
        });


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean flag = false;
                Calendar calendar = Calendar.getInstance();

                int thisYear = calendar.get(Calendar.YEAR);
                // Log.d(TAG, "# thisYear : " + thisYear);

                int thisMonth = calendar.get(Calendar.MONTH) + 1;
                //Log.d(TAG, "@ thisMonth : " + thisMonth);

                int thisDay = calendar.get(Calendar.DAY_OF_MONTH);
                //Log.d(TAG, "$ thisDay : " + thisDay);
                d = (transactionDatePicker.getDayOfMonth());
                m = (transactionDatePicker.getMonth() + 1);
                y = (transactionDatePicker.getYear());
                day = String.valueOf(d);
                month = String.valueOf(m);
                year = String.valueOf(y);
                if (thisYear > y) {
                    flag = true;
                } else {
                    if (thisYear == y) {
                        if (thisMonth > m) {
                            flag = true;
                        } else {
                            if (thisMonth == m) {
                                if (thisDay >= d) {
                                    flag = true;
                                }
                            }
                        }
                    }
                }


                if (flag) {
                    amountStr = amountEditText.getText().toString().trim().replaceAll(",", "");
                    shopNameStr = shopNameEditText.getText().toString().trim();
                    if (!amountStr.isEmpty() && !shopNameStr.isEmpty()) {
                        tid = String.valueOf(currentTimeMillis());
                        ;

                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("Transactions").child(tid).child("Amount").setValue(amountStr);
                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("Transactions").child(tid).child("Category").setValue(categoryStr);
                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("Transactions").child(tid).child("Shop Name").setValue(shopNameStr);
                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("Transactions").child(tid).child("ZMessage").setValue("Entered Manually...");
                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("Transactions").child(tid).child("Day").setValue(day);
                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("Transactions").child(tid).child("Month").setValue(month);
                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("Transactions").child(tid).child("Year").setValue(year);


//                        if (selCat == "Uncategorised") {
//                            UnCatTran.child(Tid);
//                            UnCatTran.child(Tid).child("Amount").setValue(Amount);
//                            UnCatTran.child(Tid).child("Category").setValue(selCat);
//                            UnCatTran.child(Tid).child("Shop Name").setValue(ShopName);
//                            UnCatTran.child(Tid).child("ZMessage").setValue("Entered Manually...");
//                            UnCatTran.child(Tid).child("Day").setValue(day);
//                            UnCatTran.child(Tid).child("Month").setValue(month);
//                            UnCatTran.child(Tid).child("Year").setValue(year);
//                        } else {

                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("CatTran").child(categoryStr).child(tid).child("Amount").setValue(amountStr);
                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("CatTran").child(categoryStr).child(tid).child("Category").setValue(categoryStr);
                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("CatTran").child(categoryStr).child(tid).child("Shop Name").setValue(shopNameStr);
                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("CatTran").child(categoryStr).child(tid).child("ZMessage").setValue("Entered Manually...");
                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("CatTran").child(categoryStr).child(tid).child("Day").setValue(day);
                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("CatTran").child(categoryStr).child(tid).child("Month").setValue(month);
                        RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("CatTran").child(categoryStr).child(tid).child("Year").setValue(year);
//                        }


                        RefTran1 = RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("Transactions");

                        RefCatSum1 = RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("CatSum");


                        Toast.makeText(getApplicationContext(), "Transaction added", Toast.LENGTH_SHORT).show();
                        amountEditText.setText("");
                        shopNameEditText.setText("");
                        Toast.makeText(getApplicationContext(), "Add one more transaction or press back", Toast.LENGTH_LONG).show();

                        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                        mDatabase.child(Uid).child("Group Name").get().addOnCompleteListener(new OnCompleteListener<com.google.firebase.database.DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<com.google.firebase.database.DataSnapshot> task) {
                                if (task.isSuccessful()) {
                                    try {
                                        boolean entryAdded = false;
                                        String groupName = task.getResult().getValue().toString();
                                        notifyUsers(groupName, shopNameStr, amountStr);
                                    } catch(Exception e) {

                                    } catch (Error e) {

                                    }
                                }
                            }
                        });

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
                categoryList.add(value);
//                arrayAdapter.notifyDataSetChanged();
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
            super.onBackPressed();

            Intent i = new Intent(AddTransactionActivity.this, HomeActivity.class);
            if(getIntent().getExtras()!=null && !getIntent().getExtras().getString("group_key").isEmpty())
            {
                i= new Intent(AddTransactionActivity.this, GroupActivity.class);
            }
            startActivity(i);
    }

    public void notifyUsers(String groupName, String name, String amount) {

        OkHttpClient client = new OkHttpClient();
        String url = "https://fcm.googleapis.com/fcm/send";

        JSONObject notificationJSON = new JSONObject();
        JSONObject bodyJSON = new JSONObject();

        try {
            notificationJSON.put("title", "A new expense was posted on group " + groupName);
            notificationJSON.put("body", "Name: " + name + " Amount: " + amount);

            bodyJSON.put("to", "/topics/" + groupName.replace(" ", "-"));
            bodyJSON.put("notification", notificationJSON);
        } catch (
                JSONException e) {
            e.printStackTrace();
            System.out.println("Error JSON: " + e.toString());
        }

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, bodyJSON.toString());
        Request request;

        request = new Request.Builder()
                .url(url)
                .header("Authorization", "key=AAAAl9THbZI:APA91bG0gBooKWzZemcz4bRaJCXvrAmQ_2KqcdS4Dq3gvel24EqnPPNYZ6a7-9KMl5Ud29xGEAGT593c4yp6Q8tCfgPCzcHP0mqn3mAboaG5jQSEj0pIyNjE0n5_nJjiJwqqxM0PGIiH")
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws
                    IOException {
                    System.out.println("Response " + response.code());
            }
        });
    }

}