package com.xa.xpensauditor;

import android.graphics.Color;
import android.os.Bundle;

import com.androidplot.pie.PieChart;
import com.androidplot.pie.Segment;
import com.androidplot.pie.SegmentFormatter;
import com.androidplot.ui.widget.TextLabelWidget;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.xa.xpensauditor.databinding.ActivityDashboardBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DashboardActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    private XYPlot expenseHistoryPlot;
    private PieChart monthlyExpensePlot;

    private Firebase mRootRef;
    private Firebase RefUid,RefTran;
    private Firebase RefName,RefEmail,RefPhnnum;
    private Firebase RefCat,RefFood,RefHealth,RefTravel,RefEdu,RefBills,RefHomeNeeds,RefOthers,RefUncat;
    private HashMap<String, Number> monthlyExpense = new HashMap<String , Number>();
    private HashMap<String, Number> categoryWiseSpending = new HashMap<String, Number>();
    private List<Transaction> transList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        expenseHistoryPlot = findViewById(R.id.expense_history_plot);
        monthlyExpensePlot = findViewById(R.id.monthly_expense_plot);

        mRootRef=new Firebase("https://xpense-auditor-default-rtdb.firebaseio.com");

        mRootRef.keepSynced(true);
        com.google.firebase.auth.FirebaseAuth auth = FirebaseAuth.getInstance();
        String Uid=auth.getUid();
        RefUid= mRootRef.child(Uid);
        RefTran = RefUid.child("Transactions");
        RefCat=RefUid.child("Categories");
        RefUncat=RefCat.child("Uncategorised");
        DatabaseReference mDatabase=FirebaseDatabase.getInstance().getReference();
        mDatabase.child(Uid).child("DateRange").get().addOnCompleteListener(new OnCompleteListener<com.google.firebase.database.DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<com.google.firebase.database.DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(DashboardActivity.this, Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_LONG).show();
                }
                else {
                    for (com.google.firebase.database.DataSnapshot S:task.getResult().getChildren()) {
                        for (com.google.firebase.database.DataSnapshot transaction: S.child("Transactions").getChildren())
                        {
                            Toast.makeText(DashboardActivity.this, String.valueOf(transaction.getValue()), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

//        RefTran.addChildEventListener(new ChildEventListener() {
//            String amount,cat,shname,shDay,shMonth,shYear,shMsg;
//
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                int i=0;
//                String tid = dataSnapshot.getKey().toString().trim();
//
//                for (DataSnapshot S:dataSnapshot.getChildren()) {
//
//                    switch(i)
//                    {
//                        case 0:
//                            amount=S.getValue().toString().trim();
//                            break;
//                        case 1:
//                            cat=S.getValue().toString().trim();
//                            break;
//                        case 2:
//                            shDay=S.getValue().toString().trim();
//                            break;
//                        case 3:
//                            shMonth=S.getValue().toString().trim();
//                            break;
//                        case 4:
//                            shname=S.getValue().toString().trim();
//                            break;
//                        case 5:
//                            shYear=S.getValue().toString().trim();
//                            break;
//                        case 6:
//                            shMsg=S.getValue().toString().trim();
//                            break;
//                    }
//
//                    i++;
//                }
//                String shdate= shDay+" - "+shMonth+" - "+shYear;
//                Transaction transaction=new Transaction(tid,amount,cat,shname,shdate,shMsg);
//                amount="";
//                cat="";
//                shname="";
//                shdate="";
//                transList.add(transaction);
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });



        plotExpenseHistory(transList);
        plotMonthlyExpense(transList);
    }

    private void plotExpenseHistory(List<Transaction> transList) {
        Number[] series1Numbers = {1, 4, 2, 8, 4, 16, 8, 32, 16, 64};

        XYSeries series1 = new SimpleXYSeries(
                Arrays.asList(series1Numbers), SimpleXYSeries.ArrayFormat.XY_VALS_INTERLEAVED, "Expense occured");

        LineAndPointFormatter series1Format =
                new LineAndPointFormatter(Color.RED, Color.RED, null, null);

        expenseHistoryPlot.addSeries(series1, series1Format);
    }

    private void plotMonthlyExpense(List<Transaction> transList) {

        HashMap<String, Number> categoryWiseSpending = new HashMap<String, Number>();


        for (Transaction transaction : transList)
        {
            String category=transaction.getT_cat();
            if (categoryWiseSpending.containsKey(category))
            {
                Object cellContents = categoryWiseSpending.get(category)+(transaction.getT_amt());
                Number num=null;
                num=(Number) cellContents;
                categoryWiseSpending.put(category, num);
            }
            else
            {
                Object cellContents = (transaction.getT_amt());
                Number num=null;
                num=(Number) cellContents;
                categoryWiseSpending.put(category, num);
            }
        }

        for (Map.Entry<String,Number> category : categoryWiseSpending.entrySet())
        {
            monthlyExpensePlot.addSegment(new Segment(category.getKey(), category.getValue()), new SegmentFormatter(Color.RED));
        }

//        monthlyExpensePlot.addSegment(new Segment("Segment1", 20), new SegmentFormatter(Color.RED));
//        monthlyExpensePlot.addSegment(new Segment("Segment2", 25), new SegmentFormatter(Color.BLUE));
//        monthlyExpensePlot.addSegment(new Segment("Segment3", 30), new SegmentFormatter(Color.GREEN));
//        monthlyExpensePlot.addSegment(new Segment("Segment4", 25), new SegmentFormatter(Color.YELLOW));
    }
}