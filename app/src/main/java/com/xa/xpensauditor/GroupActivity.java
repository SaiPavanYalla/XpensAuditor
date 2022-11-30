package com.xa.xpensauditor;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import androidx.core.view.GravityCompat;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import com.xa.xpensauditor.databinding.ActivityHomeBinding;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class GroupActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int SMS_PERMISSION_CODE =101;

    private ActivityHomeBinding binding;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    FirebaseAuth auth;
    ImageView userImage;

    private Firebase mRootRef;
    private Firebase RefUid;
    private Firebase RefName,RefEmail;
    private static int currentpage=0;
    TextView tvHeaderName, tvHeaderMail;
    Uri imageUri = null;
    String Uid;


    // String groupKey = intent.getStringExtra("group_key");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Intent intent = getIntent();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),GroupTransactionActivity.class);
                i.putExtra("group_key",  intent.getExtras().getString("group_key"));
                startActivity(i);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Firebase.setAndroidContext(this);
        auth = FirebaseAuth.getInstance();
        mRootRef=new Firebase("https://xpense-auditor-default-rtdb.firebaseio.com");
        mRootRef.keepSynced(true);
        Uid=auth.getUid();
        RefUid= mRootRef.child(Uid);
        RefName = RefUid.child("Name");
        RefEmail=RefUid.child("Email");


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        View navHeaderView =  navigationView.getHeaderView(0);
        tvHeaderName = (TextView)navHeaderView.findViewById(R.id.headerName);
        tvHeaderMail = (TextView)navHeaderView.findViewById(R.id.headerEmail);
        userImage = (ImageView)navHeaderView.findViewById(R.id.imageView);

        navigationView.setNavigationItemSelectedListener(this);


        RefName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(auth.getCurrentUser()!=null){
                    auth.getCurrentUser().reload();
                }
                if (auth.getCurrentUser()!=null) {
                    try {
                        tvHeaderName.setText(dataSnapshot.getValue().toString().trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setCurrentItem(currentpage);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0)
                {
                    currentpage=0;
                    Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                    startActivity(intent);
                }
                if(position==1)
                {
                    currentpage=1;
                    Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                    startActivity(intent);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new TabFragment(),"ALL TRANSACTION");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.group_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.group_members) {

            Intent i=new Intent(this,ListGroupMembersActivity.class);
            startActivity(i);
        }
        else if (id == R.id.group_analytics) {

            Intent i=new Intent(this,DashboardActivity.class);
            startActivity(i);
        }
        else if (id == R.id.add_people_to_group) {

            Intent i=new Intent(this,AddMemberToGroupActivity.class);
            startActivity(i);
        }
        else if(id== R.id.leave_group)
        {
//            TODO: Remove the user from the current group and redirect to home page
            Toast.makeText(getApplicationContext(), "To be updated in later versions", Toast.LENGTH_SHORT).show();
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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}