package com.xa.xpensauditor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

public class Rate extends AppCompatActivity {

    RatingBar r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Firebase mRootRef, RefUid, RefRating;
        FirebaseAuth auth= FirebaseAuth.getInstance();
        mRootRef = new Firebase("https://xpensauditor-g11-default-rtdb.firebaseio.com/");
        mRootRef.keepSynced(true);
        String Uid = auth.getUid();
        RefUid = mRootRef.child(Uid);
        RefRating = RefUid.child("Rating");

        r=(RatingBar)findViewById(R.id.ratingBar);
        r.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                RefRating.addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onDataChange(DataSnapshot DS) {
                        try {
                            if(DS.getValue() != null && fromUser == true){
                                AlertDialog.Builder builder = new AlertDialog.Builder(Rate.this);
                                builder.setMessage("You already submitted a rating. Rate Again ?");
                                builder.setTitle("Already Rated!");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Yes", (DialogInterface.OnClickListener) (dialog, which) -> {
                                    Toast.makeText(getApplicationContext(), String.valueOf(ratingBar.getRating()), Toast.LENGTH_LONG).show();
                                    RefRating.setValue(String.valueOf(ratingBar.getRating()));
                                });
                                builder.setNegativeButton("No", (DialogInterface.OnClickListener) (dialog, which) -> {
                                    dialog.cancel();
                                });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            }
                            else if (fromUser == true) {
                                Toast.makeText(getApplicationContext(), String.valueOf(ratingBar.getRating()), Toast.LENGTH_LONG).show();
                                RefRating.setValue(String.valueOf(ratingBar.getRating()));
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
}