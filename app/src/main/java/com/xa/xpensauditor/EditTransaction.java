package com.xa.xpensauditor;

import static java.lang.System.currentTimeMillis;

import java.util.*;
import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class EditTransaction extends AppCompatActivity{
    private TextInputEditText transactionAmt;
    private TextInputEditText shopName;
    private TextInputEditText cat;
    private DatePicker dat;
    private TextInputEditText message;

    private Firebase mRootRef;
    private Firebase RefUid;

    private String Tid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_transaction);
        Firebase.setAndroidContext(this);

        Button btnEdit = (Button) findViewById(R.id.btn_edit);

        mRootRef = new Firebase("https://xpensauditor-g11-default-rtdb.firebaseio.com/");

        mRootRef.keepSynced(true);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String Uid = auth.getUid();
        RefUid = mRootRef.child(Uid);

        Intent i = getIntent();

        transactionAmt = findViewById(R.id.tns_amt);
        shopName = findViewById(R.id.shp_name);
        cat = findViewById(R.id.cat);
        dat = findViewById(R.id.dateTrans);
        message = findViewById(R.id.message);

        String transactionID = i.getStringExtra("tns_id");
        String oldTransactionAmt = i.getStringExtra("tns_amt");
        String oldShopName = i.getStringExtra("shp_name");
        String oldCat = i.getStringExtra("cat");
        String oldMessage = i.getStringExtra("msg");
        String oldDateString = i.getStringExtra("dat");

        String[] splitList = oldDateString.split("/");

        int oldDay = Integer.valueOf(splitList[0]);
        int oldMonth = Integer.valueOf(splitList[1]);
        int oldYear = Integer.valueOf(splitList[2]);

        transactionAmt.setText(oldTransactionAmt);
        shopName.setText(oldShopName);
        cat.setText(oldCat);
        message.setText(oldMessage);
        dat.updateDate(oldYear, oldMonth-1, oldDay);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String currTransactionAmt = transactionAmt.getText().toString();
                String currShopName = shopName.getText().toString();
                String currCat = cat.getText().toString();
                String currDay = String.valueOf(dat.getDayOfMonth());
                String currMonth = String.valueOf(dat.getMonth()+1);
                String currYear = String.valueOf(dat.getYear());
                String currMessage = message.getText().toString();
                //String[] currSharedUserList = RefUid.child("DateRange").child(String.valueOf(oldMonth+"-"+oldYear)).child("Transactions").child(transactionID).child("Shared With").toString().split(",");

                if(!TextUtils.isEmpty(currTransactionAmt) && !TextUtils.isEmpty(currShopName) && !TextUtils.isEmpty(currCat) && !TextUtils.isEmpty(currDay) && !TextUtils.isEmpty(currMonth) && !TextUtils.isEmpty(currYear)){

                    Tid = String.valueOf(currentTimeMillis());

                    RefUid.child("DateRange").child(String.valueOf(currMonth+"-"+currYear)).child("Transactions").child(Tid).child("Amount").setValue(currTransactionAmt);
                    RefUid.child("DateRange").child(String.valueOf(currMonth+"-"+currYear)).child("Transactions").child(Tid).child("Category").setValue(currCat);
                    RefUid.child("DateRange").child(String.valueOf(currMonth+"-"+currYear)).child("Transactions").child(Tid).child("Shop Name").setValue(currShopName);
                    RefUid.child("DateRange").child(String.valueOf(currMonth+"-"+currYear)).child("Transactions").child(Tid).child("ZMessage").setValue(currMessage);
                    RefUid.child("DateRange").child(String.valueOf(currMonth+"-"+currYear)).child("Transactions").child(Tid).child("Day").setValue(currDay);
                    RefUid.child("DateRange").child(String.valueOf(currMonth+"-"+currYear)).child("Transactions").child(Tid).child("Month").setValue(currMonth);
                    RefUid.child("DateRange").child(String.valueOf(currMonth+"-"+currYear)).child("Transactions").child(Tid).child("Year").setValue(currYear);

                    RefUid.child("DateRange").child(String.valueOf(currMonth+"-"+currYear)).child("CatTran").child(currCat).child(Tid).child("Amount").setValue(currTransactionAmt);
                    RefUid.child("DateRange").child(String.valueOf(currMonth+"-"+currYear)).child("CatTran").child(currCat).child(Tid).child("Category").setValue(currCat);
                    RefUid.child("DateRange").child(String.valueOf(currMonth+"-"+currYear)).child("CatTran").child(currCat).child(Tid).child("Shop Name").setValue(currShopName);
                    RefUid.child("DateRange").child(String.valueOf(currMonth+"-"+currYear)).child("CatTran").child(currCat).child(Tid).child("ZMessage").setValue(currMessage);
                    RefUid.child("DateRange").child(String.valueOf(currMonth+"-"+currYear)).child("CatTran").child(currCat).child(Tid).child("Day").setValue(currDay);
                    RefUid.child("DateRange").child(String.valueOf(currMonth+"-"+currYear)).child("CatTran").child(currCat).child(Tid).child("Month").setValue(currMonth);
                    RefUid.child("DateRange").child(String.valueOf(currMonth+"-"+currYear)).child("CatTran").child(currCat).child(Tid).child("Year").setValue(currYear);

                    Toast.makeText(getApplicationContext(), "Transactions edited, timestamp updated!", Toast.LENGTH_SHORT).show();

                    RefUid.child("DateRange").child(String.valueOf(oldMonth+"-"+oldYear)).child("Transactions").child(transactionID).removeValue();

                    startActivity(new Intent(EditTransaction.this, HomeActivity.class));

                }else{
                    Toast.makeText(getApplicationContext(), "Please enter the data!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(EditTransaction.this, HomeActivity.class));
    }
}
