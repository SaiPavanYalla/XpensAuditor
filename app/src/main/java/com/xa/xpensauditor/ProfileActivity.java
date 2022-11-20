package com.xa.xpensauditor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {


    FirebaseAuth auth;
    private TextView NameView, EmailView, PhnView, UserDOB, UserAddress;
    private Firebase mRootRef;
    private Firebase RefUid;
    private Firebase RefName, RefEmail, RefPhnnum, RefAddress, RefDay, RefMonth, RefYear;
    private ImageButton editProf;
    private String day, month, year;
    public static Uri downloadUrl;
    private static final int galleryReq = 1;
    //todo
    //StorageReference storageReference, filepath,storageRef;
    ImageButton changePic;
    ImageView userImage;
    TextView tvHeaderName, tvHeaderMail;
    ImageView userImageNavHead;
    Uri imageUri = null;
    String Uid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NameView = (TextView) findViewById(R.id.userName);
        EmailView = (TextView) findViewById(R.id.userEmail);
        PhnView = (TextView) findViewById(R.id.userPhone);
        UserAddress = (TextView) findViewById(R.id.userAddress);
        UserDOB = (TextView) findViewById(R.id.userDOB);
        editProf = (ImageButton) findViewById(R.id.editProfile);

        auth= FirebaseAuth.getInstance();


        mRootRef = new Firebase("https://xpensauditor-g11-default-rtdb.firebaseio.com/");
        mRootRef.keepSynced(true);
        Uid = auth.getUid();
        RefUid = mRootRef.child(Uid);
        RefName = RefUid.child("Name");
        RefEmail = RefUid.child("Email");
        RefPhnnum = RefUid.child("Phone Number");
        RefAddress = RefUid.child("Address");
        RefDay = RefUid.child("Day");
        RefMonth = RefUid.child("Month");
        RefYear = RefUid.child("Year");
//todo
//        storageReference = FirebaseStorage.getInstance().getReference();
//
//        userImage = (ImageView) findViewById(R.id.userImage);


// todo
        //storageRef=storageReference.child("Profile Image").child(Uid+".jpg");
        /*storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                imuri=uri;
                //Handle whatever you're going to do with the URL here
            }
        });
*/

// todo
//        try {
//            final File localFile = File.createTempFile("images", "jpg");
//
//            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
//                    userImage.setImageBitmap(bitmap);
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                }
//            });
//        } catch (IOException e ) {}


        //userImage.setImageURI(storageReference.child("Profile Image").child(Uid+".jpg").getDownloadUrl().getResult());
       /* try {
            final File localFile = File.createTempFile("images", "jpg");
            storageReference.child("Profile Image").child(Uid).getFile(localFile).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                    userImage.setImageURI((android.net.Uri) localFile.toURI());
                }
            });

        }catch(Exception e){

        }*/

        RefName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot DS) {
                String n = DS.getValue(String.class);
                NameView.setText(n);
                try {
                    tvHeaderName.setText(n);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        RefEmail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot DS) {
                String n = DS.getValue().toString().trim();
                try {
                    tvHeaderMail.setText(n);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                EmailView.setText(n);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        RefPhnnum.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot DS) {
                String n = DS.getValue().toString().trim();

                PhnView.setText(n);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        RefAddress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot DS) {
                String n = DS.getValue().toString().trim();

                UserAddress.setText(n);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        RefDay.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot DS) {
                day = DS.getValue().toString().trim();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        RefMonth.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot DS) {
                month = DS.getValue().toString().trim();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        RefYear.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot DS) {
                year = DS.getValue().toString().trim();
                UserDOB.setText(day + "/" + month + "/" + year);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        editProf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, EditProfileActivity.class);
                i.putExtra("NAME", NameView.getText().toString().trim());
                i.putExtra("EMAIL", EmailView.getText().toString().trim());
                i.putExtra("PHONE", PhnView.getText().toString().trim());
                i.putExtra("ADDRESS", UserAddress.getText().toString().trim());
                i.putExtra("DAY", day);
                i.putExtra("MONTH", month);
                i.putExtra("YEAR", year);
                startActivity(i);
            }
        });
//todo
//        try {
//            final File localFile = File.createTempFile("images", "jpg");
//            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
//                    userImageNavHead.setImageBitmap(bitmap);
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                }
//            });
//        } catch (IOException e ) {}

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ProfileActivity.this, HomeActivity.class);
        startActivity(i);
//        super.onBackPressed();
    }

}