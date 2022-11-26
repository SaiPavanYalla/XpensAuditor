package com.xa.xpensauditor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

public class Suggest extends AppCompatActivity implements View.OnClickListener{

    EditText e;
    Button b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Firebase mRootRef, RefUid, RefSuggestion;
        FirebaseAuth auth= FirebaseAuth.getInstance();
        mRootRef = new Firebase("https://xpensauditor-g11-default-rtdb.firebaseio.com/");
        mRootRef.keepSynced(true);
        String Uid = auth.getUid();
        RefUid = mRootRef.child(Uid);
        RefSuggestion = RefUid.child("Suggestion");


        e=(EditText)findViewById(R.id.editText5);
        b=(Button)findViewById(R.id.button3);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RefSuggestion.addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onDataChange(DataSnapshot DS) {
                        try {
                            String suggestion = e.getText().toString();
                            if (DS.getValue() != null) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Suggest.this);
                                builder.setMessage("You already submitted a rating. Rate Again ?");
                                builder.setTitle("Already Rated!");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                                    Toast.makeText(Suggest.this, "Suggestion submitted", Toast.LENGTH_SHORT).show();
                                    RefSuggestion.setValue(suggestion);
                                    Toast.makeText(Suggest.this, "Suggestion submitted", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(Suggest.this, HomeActivity.class);
                                    startActivity(i);
                                });
                                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                                    dialog.cancel();
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            } else {
                                RefSuggestion.setValue(suggestion);
                                Toast.makeText(Suggest.this, "Suggestion submitted", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Suggest.this, HomeActivity.class);
                                startActivity(i);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }
        });
        }

    @Override
    public void onClick(View view) {

    }
}