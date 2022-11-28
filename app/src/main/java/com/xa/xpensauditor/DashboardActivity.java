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
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
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
    private HashMap<String, Integer> expenseHistory = new HashMap<String , Integer>();
    private HashMap<String, Integer> monthlyExpense = new HashMap<String, Integer>();
    private ArrayList<String> dates = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        expenseHistoryPlot = findViewById(R.id.expense_history_plot);
        monthlyExpensePlot = findViewById(R.id.monthly_expense_plot);

//        monthlyExpensePlot.addSegment(new Segment("Segment1", 20), new SegmentFormatter(Color.RED));
//        monthlyExpensePlot.addSegment(new Segment("Segment2", 25), new SegmentFormatter(Color.BLUE));
//        monthlyExpensePlot.clear();
//        monthlyExpensePlot.addSegment(new Segment("Segment3", 30), new SegmentFormatter(Color.GREEN));
//        monthlyExpensePlot.addSegment(new Segment("Segment4", 25), new SegmentFormatter(Color.YELLOW));

        Firebase.setAndroidContext(this);
        //Get Firebase auth instance
        FirebaseAuth auth = FirebaseAuth.getInstance();
        mRootRef=new Firebase("https://xpense-auditor-default-rtdb.firebaseio.com");

        mRootRef.keepSynced(true);

        String Uid;

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("Uid")) {
            Uid = extras.getString("Uid");
        } else {
            Uid=auth.getUid();
        }

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
                            String date = transaction.child("Month").getValue() + "-" + transaction.child("Year").getValue();
                            String amount = Objects.requireNonNull(transaction.child("Amount").getValue()).toString();
                            String category = Objects.requireNonNull(transaction.child("Category").getValue()).toString();

                            //Creating ExpenseHistory hashmap
                            if (!expenseHistory.containsKey(date))
                            {
                                expenseHistory.put(date, Integer.parseInt(amount));
                                dates.add(date);
                            }
                            else {
                                expenseHistory.put(date, expenseHistory.get(date) + Integer.parseInt(amount));
                            }

                        }
                        Collections.sort(dates);

                        for (com.google.firebase.database.DataSnapshot transaction: S.child("Transactions").getChildren())
                        {
                            String date = transaction.child("Month").getValue() + "-" + transaction.child("Year").getValue();
                            String amount = Objects.requireNonNull(transaction.child("Amount").getValue()).toString();
                            String category = Objects.requireNonNull(transaction.child("Category").getValue()).toString();

                            //Creating MonthlyExpense hashmap
                            if(date.equals(dates.get(0)))
                            {
                                if (!monthlyExpense.containsKey(category))
                                {
                                    monthlyExpense.put(category, Integer.parseInt(amount));
                                }
                                else
                                {
                                    monthlyExpense.put(category, monthlyExpense.get(category) + Integer.parseInt(amount));
                                }
                            }


                        }

                    }
                }

                plotExpenseHistory(expenseHistory);
                plotMonthlyExpense(monthlyExpense);
            }

        });
    }

    private void plotExpenseHistory(HashMap<String, Integer> expenseHistory) {
        System.out.println(expenseHistory.toString());
        Number[] series1Numbers = {1, 4, 2, 8, 4, 16, 8, 32, 16, 64};

        Iterator hmIterator = expenseHistory.entrySet().iterator();

        List<Number> xAxis = new ArrayList<>();
        List<Number> yAxis = new ArrayList<>();
        int count = 1;
        while (hmIterator.hasNext()) {
            Map.Entry month = (Map.Entry)hmIterator.next();
//            System.out.println(Double.parseDouble(month.getKey().toString().replace("-",".")));
            xAxis.add(count);
            yAxis.add((Integer) month.getValue());
            count++;
        }

        System.out.println(yAxis.toString());

        expenseHistoryPlot.clear();

        XYSeries series1 = new SimpleXYSeries(xAxis, yAxis, "Expense occured");

        LineAndPointFormatter series1Format =
                new LineAndPointFormatter(Color.RED, Color.RED, null, null);

        expenseHistoryPlot.addSeries(series1, series1Format);

        expenseHistoryPlot.redraw();
    }

    private void plotMonthlyExpense(HashMap<String, Integer> monthlyExpense) {
        Integer colors[] = new Integer[] {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA, Color.CYAN, Color.GRAY, Color.WHITE};

        Iterator hmIterator = monthlyExpense.entrySet().iterator();

        int count = 0;
        while (hmIterator.hasNext()) {
            Map.Entry category = (Map.Entry)hmIterator.next();
            monthlyExpensePlot.addSegment(new Segment(category.getKey().toString()+ ": $" + category.getValue(), (Number)category.getValue()), new SegmentFormatter(colors[count%colors.length]));
            count++;
        }

        monthlyExpensePlot.redraw();
    }
}