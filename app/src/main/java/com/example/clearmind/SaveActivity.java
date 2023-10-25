package com.example.clearmind;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.Entry;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;


import java.util.ArrayList;
import java.util.List;

public class SaveActivity extends AppCompatActivity {
    private Button learn_button;
    private Button achieve_button;
    private Button profile_button;

    private Button line_checkin_button;
    private Button bar_checkin_button;

    private String username;
    private ArrayList<BarEntry> entries_bar;
    private List<Entry> entries_line;
    private BarChart barChart;
    private LineChart lineChart;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        Intent intent = getIntent();

        // Enable full screen display and avoid nav bar overlap
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_save), (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Apply the insets as a margin to the view. This solution sets only the
            // bottom, left, and right dimensions, but you can apply whichever insets are
            // appropriate to your layout. You can also update the view padding if that's
            // more appropriate.
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
//            mlp.leftMargin = insets.left;
//            mlp.rightMargin = insets.right;
            mlp.bottomMargin = insets.bottom;
            v.setLayoutParams(mlp);

            // Return CONSUMED if you don't want want the window insets to keep passing
            // down to descendant views.
            return WindowInsetsCompat.CONSUMED;
        });

        this.username = intent.getStringExtra("username");
//        Toast.makeText(SaveActivity.this,  username, Toast.LENGTH_SHORT).show();

        learn_button = (Button) findViewById(R.id.button_learn);
        achieve_button = (Button) findViewById(R.id.button_achieve);
        profile_button = (Button) findViewById(R.id.button_profile);
        line_checkin_button = (Button) findViewById(R.id.button_checkin_line);
        bar_checkin_button = (Button) findViewById(R.id.button_checkin_bar);

        // =================
        // Add bar chart
        // =================
        barChart = findViewById(R.id.barChart);
        entries_bar = new ArrayList<>();

        // Customize the Axis
        XAxis xAxis_bar = barChart.getXAxis();
        YAxis yAxis_bar = barChart.getAxisLeft();

//        String[] dateStrings = {"10-01", "10-02", "10-03", "10-04", "10-05", "10-06", "10-07"};

        String[] dateStrings = {};

        barChart.getAxisRight().setEnabled(false);
        xAxis_bar.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis_bar.setValueFormatter(new DateAxisValueFormatter(dateStrings));
//        xAxis.setValueFormatter(new BarXAxisValueFormatter());  // set value of Axis
//        float text_size = 12;
//        xAxis.setTextSize(text_size);
//        yAxis.setTextSize(text_size);

        // Set the max visible range on the X-axis (number of values displayed)
        xAxis_bar.setAxisMaximum(5.5f);



        barChart.setDrawGridBackground(false);
        barChart.getDescription().setEnabled(false);
        barChart.setTouchEnabled(false);
        barChart.setDragEnabled(false);
        barChart.setScaleEnabled(false);
//        barChart.max
        barChart.setNoDataText("Self-CheckIn to start tracking your procrastination status");

        // TODO: Bar Chart Initialization based on db
        // TODO: Customize X axis (date string)

        // test:
//        for(int i=0; i<7; i++){
//            float value = (float) Math.random() * 10 ; // substitute to actual data
//            BarEntry bar_entry = new BarEntry(entries_bar.size(), Math.round(value));
//            entries_bar.add(bar_entry);
//        }
//        update_BarChart();



        // =================
        // Add line Chart
        // =================
        entries_line = new ArrayList<>();
        lineChart = findViewById(R.id.chart);

        lineChart.setDrawGridBackground(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.setTouchEnabled(false);
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);

        // Customize the Axis
        XAxis xAxis_line = lineChart.getXAxis();
        YAxis yAxis_line = lineChart.getAxisLeft();

        lineChart.getAxisRight().setEnabled(false);
        xAxis_line.setPosition(XAxis.XAxisPosition.BOTTOM);

        // Line Chart Initialization based on db


        bar_checkin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                float value = (float) Math.random() * 10 ; // substitute to actual data
                BarEntry bar_entry = new BarEntry(entries_bar.size(), Math.round(value));
                entries_bar.add(bar_entry);

//                if (entries_bar.size() >= 10){
//                    entries_bar.remove(0);
//                }
                // update bar chart
                update_BarChart();
            }
        });

        line_checkin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                float value = (float) Math.random() * 10; // substitute to actual data
                Entry entry = new Entry(entries_line.size(), Math.round(value));
                entries_line.add(entry);

//                if (entries_line.size() >= 10){
//                    entries_line.remove(0);
//                }
                // update
                update_LineChart();
            }
        });

        learn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openLearnActivity();
            }
        });

        achieve_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openAchieveActivity();
            }
        });

        profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openProfileActivity();
            }
        });

    }

    private void update_BarChart() {
        BarDataSet dataSet = new BarDataSet(entries_bar, "Procrastination Self-Evaluate");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        // Create the BarData and set the dataset
        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
        barChart.invalidate(); // Refresh the chart
    }

    private void update_LineChart() {
        LineDataSet dataSet = new LineDataSet(entries_line, "Data point");
        dataSet.setDrawValues(false);
        dataSet.setColor(Color.BLUE);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    public void openLearnActivity(){
        Intent intent = new Intent(this,LearnActivity.class);
        intent.putExtra("username", username);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    public void openAchieveActivity(){
        Intent intent = new Intent(this,AchieveActivity.class);
        intent.putExtra("username", username);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    public void openProfileActivity(){
        Intent intent = new Intent(this,ProfileActivity.class);
        intent.putExtra("username", username);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}
