package com.xa.xpensauditor;

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
    private TextInputEditText dat;
    private TextInputEditText message;

    private String transactionID;
    private String oldTransactionAmt;
    private String oldShopName;
    private String oldCat;
    private String oldMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_transaction);

        Intent i = getIntent();

        transactionAmt = findViewById(R.id.tns_amt);
        shopName = findViewById(R.id.shp_name);
        cat = findViewById(R.id.cat);
        message = findViewById(R.id.message);

        transactionID = i.getStringExtra("tns_id");
        oldTransactionAmt = i.getStringExtra("tns_amt");
        oldShopName = i.getStringExtra("shp_name");
        oldCat = i.getStringExtra("cat");
        oldMessage = i.getStringExtra("msg");

        // TODO: Remove this print statement
        System.out.println(transactionID);
        transactionAmt.setText(oldTransactionAmt);
        shopName.setText(oldShopName);
        cat.setText(oldCat);
        message.setText(oldMessage);


    }
}
