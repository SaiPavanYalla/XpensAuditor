package com.xa.xpensauditor;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.xa.xpensauditor.databinding.ActivityHomeBinding;

import java.io.File;
import java.io.IOException;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int SMS_PERMISSION_CODE =101;
    //todo delete
    //private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    FirebaseAuth auth;
    ImageView userImage;

    private Firebase mRootRef,LastRefreshDate;
    private Firebase RefUid;
    private Firebase RefName,RefEmail;
    TextView tvHeaderName, tvHeaderMail;
    //todo
    //StorageReference storageReference, filepath,storageRef;
    Uri imageUri = null;
    String Uid,StrLastRefDate="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// todo delete
//     binding = ActivityHomeBinding.inflate(getLayoutInflater());
//     setContentView(binding.getRoot());
//
//        setSupportActionBar(binding.appBarHome.toolbar);
//        binding.appBarHome.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //todo
//                //Intent i=new Intent(getApplicationContext(),Transac.class);
//                //startActivity(i);
//            }
//        });
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("XpensAuditor");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),AddTransactionActivity.class);
                startActivity(i);
            }
        });

// todo
//        DrawerLayout drawer = binding.drawerLayout;
//        NavigationView navigationView = binding.navView;
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.nav_home)
//                .setOpenableLayout(drawer)
//                .build();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Firebase.setAndroidContext(this);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() == null) {
            //Toast.makeText(getApplicationContext(), auth.getCurrentUser().toString() , Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
            finish();
        }
        mRootRef=new Firebase("https://xpensauditor-default-rtdb.firebaseio.com/");
        mRootRef.keepSynced(true);
        Uid=auth.getUid();
        RefUid= mRootRef.child(Uid);
        RefName = RefUid.child("Name");
        RefEmail=RefUid.child("Email");
        LastRefreshDate = RefUid.child("LastRefreshDate");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View navHeaderView =  navigationView.getHeaderView(0);
        tvHeaderName = (TextView)navHeaderView.findViewById(R.id.headerName);
        tvHeaderMail = (TextView)navHeaderView.findViewById(R.id.headerEmail);
        userImage = (ImageView)navHeaderView.findViewById(R.id.imageView);
// todo
//        storageReference = FirebaseStorage.getInstance().getReference();
//        storageRef=storageReference.child("Profile Image").child(Uid+".jpg");

//todo
//        try {
//            final File localFile = File.createTempFile("images", "jpg");
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


        navigationView.setNavigationItemSelectedListener(this);


        RefName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(auth.getCurrentUser()!=null){
                    auth.getCurrentUser().reload();
                }
                if (auth.getCurrentUser()!=null) {
                    tvHeaderName.setText(dataSnapshot.getValue().toString().trim());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getApplicationContext(), "Failed to load details", Toast.LENGTH_SHORT).show();
            }
        });

        RefEmail.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(auth.getCurrentUser()!=null){
                    auth.getCurrentUser().reload();
                }
                if (auth.getCurrentUser()!=null) {
                    //Toast.makeText(getApplicationContext(), auth.getCurrentUser().toString(), Toast.LENGTH_SHORT).show();
                    try {
                        tvHeaderMail.setText(dataSnapshot.getValue().toString().trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
        LastRefreshDate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (auth.getCurrentUser()!=null) {
                    try {
                        StrLastRefDate = dataSnapshot.getValue().toString().trim();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


// todo delete
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
//        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
//        NavigationUI.setupWithNavController(navigationView, navController);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
            /*ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager());
            viewPager.setAdapter(adapter);*/
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new TabFragment(),"ALL TRANSACTION");
        adapter.addFragment(new UncategorisedFragment(),"UNCATEGORISED TRANSACTION");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //noinspection SimplifiableIfStatement
        if (id == R.id.account_settings) {

            Intent i=new Intent(this,AccountSettingsActivity.class);
            startActivity(i);
        }
        else if(id== R.id.action_settings)
        {
            Toast.makeText(getApplicationContext(), "To be updated in later versions", Toast.LENGTH_SHORT).show();
//todo prefSettings
//            Intent i=new Intent(this,PrefSettingsActivity.class);
//            startActivity(i);
        }

        else if(id==R.id.action_contact_us){
            Intent i=new Intent(this,ContactUs.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
//        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
//                || super.onSupportNavigateUp();
//    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_home) {

            Intent i=new Intent(this,HomeActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_profile) {
            Intent i=new Intent(this,ProfileActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_show_analysis) {
            Toast.makeText(getApplicationContext(), "To be updated in later versions", Toast.LENGTH_SHORT).show();
// todo
//            Intent i=new Intent(this,AnalysisActivity.class);
//            startActivity(i);

        } else if (id == R.id.nav_settings) {
            Toast.makeText(getApplicationContext(), "To be updated in later versions", Toast.LENGTH_SHORT).show();
//todo
//            Intent i=new Intent(this,SettingsActivity.class);
//            startActivity(i);

        } else if (id == R.id.nav_logout) {
            auth.signOut();
            Intent i = new Intent(this,LoginActivity.class);
            startActivity(i);
        }
        else if (id == R.id.nav_rate) {
            Intent i = new Intent(this, Rate.class);
            startActivity(i);

        } else if (id == R.id.nav_suggest) {
            Intent i=new Intent(this,Suggest.class);
            startActivity(i);

        } else if (id == R.id.nav_share) {

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "I recommend you to try this app and comment about it";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "XpensAuditor");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        } else if (id == R.id.nav_refresh) {
            Intent i=new Intent(this,SMSReaderActivity.class);
            if(isSmsPermissionGranted())
            {
                i.putExtra("StrLastRefDate", StrLastRefDate);
                startActivity(i);
            }
            else
            {
                requestReadAndSendSmsPermission();
            }


        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public boolean isSmsPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestReadAndSendSmsPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS},SMS_PERMISSION_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case SMS_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent i = new Intent(this, SMSReaderActivity.class);
                    i.putExtra("StrLastRefDate", StrLastRefDate);
                    startActivity(i);


                } else {
                    Toast.makeText(getApplicationContext(), "SMS read permission is required for this feature to work, Enabled it in under app settings", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(this, HomeActivity.class);
                    startActivity(i);
                }
                return;
            }

        }
    }
}