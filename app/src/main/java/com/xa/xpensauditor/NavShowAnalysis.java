package com.xa.xpensauditor;

import static java.lang.System.currentTimeMillis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Position;
import com.firebase.client.ChildEventListener;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NavShowAnalysis extends AppCompatActivity {
    private Firebase mRootRef;
    private Firebase RefUid,RefTran,RefUid1;
    private Firebase RefName,RefEmail,RefPhnnum;
    private Firebase RefCat,RefFood,RefHealth,RefTravel,RefEdu,RefBills,RefHomeNeeds,RefOthers,RefUncat;
    private List<Transaction> transList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TransactionAdapter mAdapter;
    private DatePicker dateTransac1;
    private DatePicker dateTransac2;
    private Button Vis;
    private String Tid;
    int tot=0;

    String tot1="Yes";
    FirebaseAuth auth;
    String Uid;
    String day1, month1, year1;
    String day2, month2, year2;
    int d1, m1, y1;
    int d2, m2, y2;
    private Firebase RefTran3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_show_analysis);
        auth = FirebaseAuth.getInstance();
        mRootRef=new Firebase("https://xpensauditor-g11-default-rtdb.firebaseio.com/");
        mRootRef.keepSynced(true);
        Uid=auth.getUid();
        RefUid= mRootRef.child(Uid);
        RefName = RefUid.child("Name");
        RefEmail=RefUid.child("Email");
        TextView valuee;
        valuee = (TextView)findViewById(R.id.textView2);
       // RefUid.child("DateRange").child(String.valueOf(month + "-" + year)).child("Transactions").child(Tid).child("Amount").setValue(Amount);

        dateTransac1 = (DatePicker) findViewById(R.id.dateTrans3);
        dateTransac2 = (DatePicker) findViewById(R.id.dateTrans2);
        Vis = (Button) findViewById(R.id.button4);
        day1 = String.valueOf(dateTransac1.getDayOfMonth());
        month1 = String.valueOf(dateTransac1.getMonth());
        year1 = String.valueOf(dateTransac1.getYear());
        day2 = String.valueOf(dateTransac2.getDayOfMonth());
        month2 = String.valueOf(dateTransac2.getMonth());
        year2 = String.valueOf(dateTransac2.getYear());
//        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
List<Integer> arr=new ArrayList<>();
        d1 = (dateTransac1.getDayOfMonth());
        m1 = (dateTransac1.getMonth());
        y1 = (dateTransac1.getYear());
        day1 = String.valueOf(d1);
        month1 = String.valueOf(m1)+1;
        year1 = String.valueOf(y1);
        d2 = (dateTransac2.getDayOfMonth());
        m2 = (dateTransac2.getMonth())+1;
        y2 = (dateTransac2.getYear());
        day2 = String.valueOf(d2);
        month2 = String.valueOf(m2);
        year2 = String.valueOf(y2);


        List<DataEntry> data = new ArrayList<>();
        Vis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent c=new Intent(NavShowAnalysis.this,Day_wise_visualization.class);
                Bundle b = new Bundle();
                b.putString("m2",month2);
                b.putString("y2",year2);
                b.putString("d2",day2);
                c.putExtras(b);
                startActivity(c);
            }

    });










}}