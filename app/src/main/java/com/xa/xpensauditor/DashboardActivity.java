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
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.LinearLayout;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.xa.xpensauditor.databinding.ActivityDashboardBinding;

import java.util.Arrays;

public class DashboardActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    private XYPlot expenseHistoryPlot;
    private PieChart monthlyExpensePlot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        expenseHistoryPlot = findViewById(R.id.expense_history_plot);
        monthlyExpensePlot = findViewById(R.id.monthly_expense_plot);

        plotExpenseHistory();
        plotMonthlyExpense();
    }

    private void plotExpenseHistory() {
        Number[] series1Numbers = {1, 4, 2, 8, 4, 16, 8, 32, 16, 64};

        XYSeries series1 = new SimpleXYSeries(
                Arrays.asList(series1Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Expense occured");

        LineAndPointFormatter series1Format =
                new LineAndPointFormatter(Color.RED, Color.RED, null, null);

        expenseHistoryPlot.addSeries(series1, series1Format);
    }

    private void plotMonthlyExpense() {
        monthlyExpensePlot.addSegment(new Segment("Segment1", 20), new SegmentFormatter(Color.RED));
        monthlyExpensePlot.addSegment(new Segment("Segment2", 25), new SegmentFormatter(Color.BLUE));
        monthlyExpensePlot.addSegment(new Segment("Segment3", 30), new SegmentFormatter(Color.GREEN));
        monthlyExpensePlot.addSegment(new Segment("Segment4", 25), new SegmentFormatter(Color.YELLOW));
    }
}