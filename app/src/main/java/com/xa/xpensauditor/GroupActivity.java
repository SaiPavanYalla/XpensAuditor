package com.xa.xpensauditor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
//import androidx.viewpager.widget.ViewPager;

//import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.xa.xpensauditor.databinding.ActivityGroupBinding;

public class GroupActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int SMS_PERMISSION_CODE = 101;

//    private Toolbar toolbar;
//    private TabLayout tabLayout;
    //    private ViewPager viewPager;
//    FirebaseAuth auth;
    ImageView userImage;

    private Firebase mRootRef;
    private Firebase RefUid;
    private Firebase RefName, RefEmail;
    private static int currentpage = 0;
    TextView tvHeaderName, tvHeaderMail;
    Uri imageUri = null;
    String Uid;
    final String GROUP_KEY = "group_key";
    private AllTransactionsFragment transactionsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);
        Intent intent = getIntent();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddTransactionActivity.class);
                String groupKey = getGroupName();
                i.putExtra(GROUP_KEY, groupKey);
                startActivity(i);
            }
        });

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

        Firebase.setAndroidContext(this);
        mRootRef = new Firebase("https://xpense-auditor-default-rtdb.firebaseio.com");
        mRootRef.keepSynced(true);
        Uid = intent.getExtras().getString("group_key");
        System.out.println(Uid + "Uid");
        RefUid = mRootRef.child(Uid);
        RefName = RefUid.child("Group Name");

//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//
//        View navHeaderView = navigationView.getHeaderView(0);
//        tvHeaderName = (TextView) navHeaderView.findViewById(R.id.headerName);
//        tvHeaderMail = (TextView) navHeaderView.findViewById(R.id.headerEmail);
//        userImage = (ImageView) navHeaderView.findViewById(R.id.imageView);

//        navigationView.setNavigationItemSelectedListener(this);


        RefName.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    tvHeaderName.setText(dataSnapshot.getValue().toString().trim());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getApplicationContext(), "Failed to load details", Toast.LENGTH_SHORT).show();
            }
        });

//        viewPager = (ViewPager) findViewById(R.id.viewpager);
//        setupViewPager(viewPager);
//        viewPager.setCurrentItem(currentpage);

//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                if(position==0)
//                {
//                    currentpage=0;
//                    Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
//                    startActivity(intent);
//                }
//                if(position==1)
//                {
//                    currentpage=1;
//                    Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
//                    startActivity(intent);
//
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//

        inflateFragment();

    }

    private void inflateFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        transactionsFragment = new AllTransactionsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("group_key", getIntent().getExtras().getString("group_key"));
        transactionsFragment.setArguments(bundle);
        ft.replace(R.id.fragmentContainerView, transactionsFragment);
        ft.commit();
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

//            Intent i = new Intent(this, ListGroupMembersActivity.class);
//            startActivity(i);
        } else if (id == R.id.group_analytics) {

            Intent i = new Intent(this, DashboardActivity.class);
            i.putExtra("group_key", Uid);
            startActivity(i);
        } else if (id == R.id.add_people_to_group) {

            Intent i = new Intent(this, AddMemberToGroupActivity.class);
            startActivity(i);
        } else if (id == R.id.leave_group) {
//            TODO: Remove the user from the current group and redirect to home page
            Toast.makeText(getApplicationContext(), "To be updated in later versions", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
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

    public String getGroupName() {
        return getIntent().getExtras().getString(GROUP_KEY);
    }
}