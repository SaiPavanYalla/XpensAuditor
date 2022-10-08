package com.xa.xpensauditor;

import androidx.annotation.AnyRes;
import androidx.annotation.NonNull;
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

public class SignupActivity extends AppCompatActivity {

    private TextInputEditText inputEmail;
    private TextInputEditText inputName;
    private TextInputEditText inputPhnnum;


    private Firebase mRootRef;
    private Firebase RefUid;
    private Firebase RefName;
    private Firebase RefEmail;
    private Firebase RefPhnnum;
    private Firebase RefCat,RefFood,RefHealth,RefTravel,RefEdu,RefBills,RefHomeNeeds,RefOthers,RefUncat,RefAddress;

    private StorageReference storageReference,filepath;


    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Firebase.setAndroidContext(this);
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        mRootRef = new Firebase("https://xpensauditor-default-rtdb.firebaseio.com/");
        //todo
        //storageReference = FirebaseStorage.getInstance().getReference();

        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (TextInputEditText) findViewById(R.id.email);
        inputName = (TextInputEditText) findViewById(R.id.Name);
        inputPhnnum = (TextInputEditText) findViewById(R.id.phnum);


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = inputEmail.getText().toString().trim();
                String password = "defaultpw9";

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, task.getException().toString().substring(task.getException().toString().lastIndexOf(":") + 1),
                                            Toast.LENGTH_SHORT).show();
                                    //Log.d("yotask", task.getException().toString());
                                } else {

                                    auth.sendPasswordResetEmail(email)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(SignupActivity.this, "We have sent you instructions to Set your password!", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(SignupActivity.this, "Failed to send password set email!", Toast.LENGTH_SHORT).show();
                                                    }

                                                    progressBar.setVisibility(View.GONE);
                                                }
                                            });

                                    String Uid=auth.getUid();
                                    String name=inputName.getText().toString().trim();
                                    String Phnnum=inputPhnnum.getText().toString().trim();

                                    RefUid= mRootRef.child(Uid);

                                    RefName = RefUid.child("Name");
                                    RefName.setValue(name);
                                    RefEmail=RefUid.child("Email");
                                    RefEmail.setValue(email);
                                    RefPhnnum=RefUid.child("Phone Number");
                                    RefPhnnum.setValue(Phnnum);
                                    RefAddress=RefUid.child("Address");
                                    RefAddress.setValue("Add Address");
                                    RefUid.child("Day").setValue("0");
                                    RefUid.child("Month").setValue("0");
                                    RefUid.child("Year").setValue("0");

//                                    RefUid.child("LastRefreshDate");
//                                    RefUid.child("LastRefreshDate").setValue(0);

                                    RefCat=RefUid.child("Categories");
                                    RefFood=RefCat.child("Food");
                                    RefFood.setValue("");
                                    RefHealth=RefCat.child("Health");
                                    RefHealth.setValue("");
                                    RefTravel=RefCat.child("Travel");
                                    RefTravel.setValue("");
                                    RefEdu=RefCat.child("Education");
                                    RefEdu.setValue("");
                                    RefBills=RefCat.child("Bills");
                                    RefBills.setValue("");
                                    RefHomeNeeds=RefCat.child("Home Needs");
                                    RefHomeNeeds.setValue("");
                                    RefOthers=RefCat.child("Others");
                                    RefOthers.setValue("");

// todo profile picture
//                                        filepath=storageReference.child("Profile Image").child(Uid+".jpg");
//
//                                        Uri imuri=getUriToDrawable(getApplicationContext(),R.drawable.def_profile_pic);
//
//                                        StorageReference storageReference= FirebaseStorage.getInstance().getReference();
//
//                                        final StorageReference ref = storageReference.child("picture.jpg");
//                                        filepath.putFile(imuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                            @Override
//                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                                    @Override
//                                                    public void onSuccess(Uri uri) {
//                                                        final Uri downloadUrl = uri;
//                                                    }
//                                                });
//                                            }
//                                        });
                                    auth.signOut();
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    public static final Uri getUriToDrawable(@NonNull Context context,
                                             @AnyRes int drawableId) {
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId) );
        return imageUri;
    }
}