package com.xa.xpensauditor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader = new ArrayList<>();
    HashMap<String, List<String>> listDataChild = new HashMap<>();
    com.google.firebase.auth.FirebaseAuth auth;

    ImageView userImage;

    private Firebase mRootRef;
    private Firebase RefUid;
    private Firebase RefName,RefEmail;
    TextView tvHeaderName, tvHeaderMail;
    StorageReference storageReference, filepath,storageRef;
    Uri imageUri = null;
    String Uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mRootRef=new Firebase("https://xpensauditor-default-rtdb.firebaseio.com/");
        mRootRef.keepSynced(true);
        auth = com.google.firebase.auth.FirebaseAuth.getInstance();
        Uid=auth.getUid();
        RefUid= mRootRef.child(Uid);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(), listDataHeader.get(groupPosition) + " Expanded", Toast.LENGTH_SHORT).show();

            }
        });

        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(), listDataHeader.get(groupPosition) + " Collapsed", Toast.LENGTH_SHORT).show();

            }
        });

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                String check = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);

                if(check.equals("Current List of Categories"))
                {
//                    TODO
//                    Intent i = new Intent(SettingsActivity.this,CategoryList.class);
//                    startActivity(i);
                }

                else if(check.equals("% expense before alert"))
                {
//                    TODO
//                    Intent i = new Intent(SettingsActivity.this,PercentExpense.class);
//                    startActivity(i);

                }

                else if(check.equals("Add new category"))
                {
//                    TODO
//                    Intent i = new Intent(SettingsActivity.this,AddCat.class);
//                    startActivity(i);
                }

                else if(check.equals("Common Shops/category"))
                {

                }

                else if(check.equals("Bank"))
                {

                }

                else if(check.equals("Paytm"))
                {

                }

                else if(check.equals("Tez"))
                {

                }

                else if(check.equals("Shop"))
                {

                }

                else if(check.equals("Restaurants"))
                {

                }
                return false;

            }
        });

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

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Category");
        listDataHeader.add("Message pattern");

        // Adding child data
        List<String> category = new ArrayList<String>();
        category.add("Current List of Categories");
        category.add("% expense before alert");
        category.add("Add new category");
        category.add("Common Shops/category");

        List<String> patterns = new ArrayList<String>();
        patterns.add("Banks");
        patterns.add("Paytm");
        patterns.add("Tez");
        patterns.add("Shops");
        patterns.add("Restaurants");

        listDataChild.put(listDataHeader.get(0), category); // Header, Child data
        listDataChild.put(listDataHeader.get(1), patterns);
    }

}