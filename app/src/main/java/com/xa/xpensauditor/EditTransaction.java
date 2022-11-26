package com.xa.xpensauditor;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_transaction);

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

        // TODO: Remove this print statement
        System.out.println(transactionID);
        transactionAmt.setText(oldTransactionAmt);
        shopName.setText(oldShopName);
        cat.setText(oldCat);
        message.setText(oldMessage);
        dat.updateDate(oldYear, oldMonth-1, oldDay);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO: Remove the print Statements
                System.out.println("Editing the transactions now!!!");

                String currTransactionAmt = transactionAmt.getText().toString();
                String currShopName = shopName.getText().toString();
                String currCat = cat.getText().toString();
                String day = String.valueOf(dat.getDayOfMonth());
                String month = String.valueOf(dat.getMonth()+1);
                String year = String.valueOf(dat.getYear());
                String currMessage = message.getText().toString();

                System.out.printf("Amount: %s \nShop: %s\nCategory: %s\nMessage: %s\nDay: %s\nMonth: %s\nYear: %s\n", currTransactionAmt, currShopName, currCat, currMessage, day, month, year);

                //System.out.printf("Old value in DB: %s", RefUid.child("DateRange").child(String.valueOf()))

            }
        });

    }
}
