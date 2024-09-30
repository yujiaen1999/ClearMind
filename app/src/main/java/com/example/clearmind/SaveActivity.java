package com.example.clearmind;

// ***********************
// This is actually the Goal Tracker
// ***********************

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
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
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import nl.bryanderidder.themedtogglebuttongroup.ThemedButton;
import nl.bryanderidder.themedtogglebuttongroup.ThemedToggleButtonGroup;

public class SaveActivity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;
    private Button learn_button;
    private Button achieve_button;
    private Button profile_button;
    private Button selfcheckin_button;
    private ImageButton newTracker_button;

//    private Button line_checkin_button;
//    private Button bar_checkin_button;
    private ArrayList<BarEntry> entries_bar;
    private List<Entry> entries_line;
    private BarChart barChart;
    private LineChart lineChart;
    private TableLayout table_layout;
    private String dateAsString;

    private GridView gridView;
    private  ArrayList<CheckinModel> DistortionModelArrayList;
    private CheckinGVAdapter adapter;

    private String txt_answer1;
    private String txt_answer2;
    private String txt_answer3;
    private String txt_answer4;
    private String txt_answer5;

    private ProgressBar progressBar;
    private TextView progressText;
    private TextView completion_score;

    private PopupWindow speechBubble;

    private String[] tooltip_date;

    private String status_finish_learn;

    private Integer iterate_num = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

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
        selfcheckin_button = (Button) findViewById(R.id.button_checkin_today);
        newTracker_button = (ImageButton) findViewById(R.id.button_new_tracker);
//        line_checkin_button = (Button) findViewById(R.id.button_checkin_line);
//        bar_checkin_button = (Button) findViewById(R.id.button_checkin_bar);
        TextView new_gaol_reminder = findViewById(R.id.new_gaol_reminder);
        new_gaol_reminder.setVisibility(View.GONE);
        achieve_button.setVisibility(View.GONE);

        ScrollView scrollView = findViewById(R.id.scrollView);
        LinearLayout locked_part = findViewById(R.id.locked_part);

        progressBar = findViewById(R.id.progressBar);
        progressText = findViewById(R.id.progressText);
        completion_score = findViewById(R.id.completion_score);

        updateProgress(0);
        // set the progress bar here
//        progressBar.setProgress(50); // 50% for example


        // ******************************
        // Lock/Unlock of the tracker function
        // ******************************
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                status_finish_learn = String.valueOf(snapshot.child("Chapter4").child("progress").child(username).child("7_Activity4_5_intro_tracker").getValue());
                HashMap<String,Object> status_isFirstRun = (HashMap<String,Object>) snapshot.child("Tracker").child(username).getValue();

                // HANDLE status
                if (!status_finish_learn.equals("1")){
                    // Tracker locked
                    scrollView.setVisibility(View.GONE);
                    newTracker_button.setVisibility(View.GONE);
                    locked_part.setVisibility(View.VISIBLE);
                }

                // TODO: change here to enable the isFirstRun judgement
                if (status_isFirstRun == null && status_finish_learn.equals("1")) {
                    final View targetButton = findViewById(R.id.button_new_tracker);
                    TapTargetView.showFor(SaveActivity.this,
                            TapTarget.forView(targetButton, "This is the Goal Start Button", "Click '+' to start tracking your new goal!")
                                    .transparentTarget(true)
                                    .targetRadius(45)
                                    .outerCircleColor(R.color.green)
                                    .outerCircleAlpha(0.99f)            // Specify the alpha amount for the outer circle
                                    .targetCircleColor(R.color.yellow)
                                    .textColor(R.color.white)
                                    .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
                                    .drawShadow(true)
                                    .tintTarget(true),
                            new TapTargetView.Listener() {
                                @Override
                                public void onTargetClick(TapTargetView view) {
                                    super.onTargetClick(view);
                                    // when user click this button
                                    // TODO: Open form, autofill the user's input from Part 4
                                    HashMap<String, String> user_input = new HashMap<String, String>();
                                    user_input.put("goal_specific", String.valueOf(snapshot.child("Chapter4").child("activity2").child(username).child("S").getValue()));
                                    user_input.put("purpose", String.valueOf(snapshot.child("Chapter4").child("activity2").child(username).child("R").getValue()));
                                    user_input.put("emotion", String.valueOf(snapshot.child("Chapter4").child("activity3").child(username).child("answer1").getValue()));
                                    user_input.put("strategy", String.valueOf(snapshot.child("Chapter4").child("activity4").child(username).child("answer1").getValue()));

                                    openPopupWindow_newTracker(view, true, user_input);
                                }
                            });

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SaveActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

//        Integer test = 1;
//        if (test == 1){
//            scrollView.setVisibility(View.GONE);
//            newTracker_button.setVisibility(View.GONE);
//            locked_part.setVisibility(View.VISIBLE);
//        }

//        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        boolean isFirstRun = prefs.getBoolean("isFirstRun", true);
//
//        if (isFirstRun && status_finish_learn.equals("1")) {
//            // 显示引导层
//            final View targetButton = findViewById(R.id.button_new_tracker); // 替换为你的按钮ID
//            TapTargetView.showFor(this,
//                    TapTarget.forView(targetButton, "This is the Start Button", "Click this to start tracking your new goal!")
//                            .transparentTarget(true)   // 按钮透明效果
//                            .targetRadius(40)
//                            .outerCircleColor(R.color.green)
//                            .outerCircleAlpha(0.99f)            // Specify the alpha amount for the outer circle
//                            .targetCircleColor(R.color.yellow) // 圆圈颜色
//                            .textColor(R.color.white)   // 文本颜色
//                            .dimColor(R.color.black)            // If set, will dim behind the view with 30% opacity of the given color
//                            .drawShadow(true)
//                            .tintTarget(true),
//            new TapTargetView.Listener() {
//                        @Override
//                        public void onTargetClick(TapTargetView view) {
//                            super.onTargetClick(view);
//                            // 当用户点击目标时的动作
//                        }
//                    });
//
//            // 更新首次运行状态
//            SharedPreferences.Editor editor = prefs.edit();
//            editor.putBoolean("isFirstRun", true);
//            editor.apply();
//        }



        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date current_date = new Date();
        dateAsString = dateFormat.format(current_date);

        TextView goal_name = (TextView) findViewById(R.id.textView_name);
//        TextView checkin_remind = (TextView) findViewById(R.id.checkin_remind);
//        checkin_remind.setVisibility(View.INVISIBLE);

        // Set up Gridview
        gridView = findViewById(R.id.gridview);
//        ArrayList<DistortionModel> DistortionModelArrayList = new ArrayList<DistortionModel>();
        DistortionModelArrayList = new ArrayList<CheckinModel>();

        // Done: Initialize gridview (add more)
        DistortionModelArrayList.add(new CheckinModel("date", R.drawable.timeliness_pending, "-"));
        DistortionModelArrayList.add(new CheckinModel("date", R.drawable.timeliness_pending, "-"));
        DistortionModelArrayList.add(new CheckinModel("date", R.drawable.timeliness_pending, "-"));
        DistortionModelArrayList.add(new CheckinModel("date", R.drawable.timeliness_pending, "-"));
        DistortionModelArrayList.add(new CheckinModel("date", R.drawable.timeliness_pending, "-"));
        DistortionModelArrayList.add(new CheckinModel("date", R.drawable.timeliness_pending, "-"));
        DistortionModelArrayList.add(new CheckinModel("date", R.drawable.timeliness_pending, "-"));

//        DistortionGVAdapter adapter = new DistortionGVAdapter(this, DistortionModelArrayList);
        adapter = new CheckinGVAdapter(this, DistortionModelArrayList);
        gridView.setAdapter(adapter);

        // Gridview image initialize
        initialize_Gridview();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Done: set item click listener to popup window
                showSpeechBubble(view, position);
            }
        });


//        table_layout = (TableLayout) findViewById(R.id.table_layout);
//
//        for (int i = 0; i < 5; i++) {
//            TableRow row = new TableRow(this);
//            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
//            row.setLayoutParams(lp);
//            TextView textView1 = new TextView(this);
//            textView1.setText("a");
//            textView1.setGravity(Gravity.CENTER);
//            TextView textView2 = new TextView(this);
//            textView2.setText("b");
//            textView2.setGravity(Gravity.CENTER);
//            TextView textView3 = new TextView(this);
//            textView3.setText("c");
//            textView3.setGravity(Gravity.CENTER);
//
//            row.addView(textView1);
//            row.addView(textView2);
//            row.addView(textView3);
//
//            table_layout.addView(row);
//        }


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
//        xAxis_bar.setValueFormatter(new DateAxisValueFormatter(dateStrings));
//        xAxis.setValueFormatter(new BarXAxisValueFormatter());  // set value of Axis
//        float text_size = 12;
//        xAxis.setTextSize(text_size);
//        yAxis.setTextSize(text_size);

        // Set the max visible range on the X-axis (number of values displayed)
        xAxis_bar.setAxisMaximum(7.5f);



        barChart.setDrawGridBackground(false);
        barChart.getDescription().setEnabled(false);
        barChart.setTouchEnabled(false);
        barChart.setDragEnabled(false);
        barChart.setScaleEnabled(false);
//        barChart.max
        barChart.setNoDataText("Self-CheckIn to start tracking your current goal!");

        // Bar Chart NOT in use: Bar Chart Initialization based on db
        // Bar Chart NOT in use: Customize X axis (date string)

         test:
        for(int i=0; i<5; i++){
            float value = (float) Math.random() * 10 ; // substitute to actual data
            BarEntry bar_entry = new BarEntry(entries_bar.size(), Math.round(value));
            entries_bar.add(bar_entry);
        }
        update_BarChart();

        barChart.setVisibility(View.GONE);



        // =================
        // Add line Chart
        // =================
        // Initialize the line chart
        lineChart = findViewById(R.id.chart);

// Disable background grid and enable description
        lineChart.setDrawGridBackground(false);
        lineChart.getDescription().setEnabled(false);

// Interaction with the chart
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);

// Customize the X axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

// Enable both left and right Y axis
        YAxis leftAxis = lineChart.getAxisLeft();
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(true); // Enable the right Y axis

        leftAxis.setAxisMinimum(0f); // Set minimum to 0
        leftAxis.setAxisMaximum(100f); // Set maximum to 100
        rightAxis.setAxisMinimum(0f); // Set minimum to 1
        rightAxis.setAxisMaximum(5f); // Set maximum to 5

        leftAxis.setGranularity(100f);
        rightAxis.setGranularity(5f);


        // Get the legend object from your chart
        Legend legend = lineChart.getLegend();

// Set the vertical alignment to top
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);

// Set the horizontal alignment to right
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);

// Set the orientation to horizontal (optional, depending on your preference)
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);

// Optionally, you can adjust the legend's position relative to the chart
        legend.setDrawInside(false); // Set to true if you want it inside, false if outside

//// Refresh the chart to apply the new legend configuration
//        lineChart.invalidate();



//// Create entries for the first line
//        ArrayList<Entry> entriesLine1 = new ArrayList<>();
//        for(int i = 1; i < 6; i++) {
//            float value = (float) (Math.random() * 5); // Adjust values as needed
//            entriesLine1.add(new Entry(i, value));
//        }
//
//// Create entries for the second line
//        ArrayList<Entry> entriesLine2 = new ArrayList<>();
//        for(int i = 1; i < 6; i++) {
//            float value = (float) (Math.random() * 100); // Adjust values as needed, different range for illustration
//            entriesLine2.add(new Entry(i, value));
//        }
//
//// Create a dataset for the first line (to be plotted against the left Y axis)
//        LineDataSet lineDataSet1 = new LineDataSet(entriesLine1, "Timeliness");
//        lineDataSet1.setColor(Color.BLUE);
//        lineDataSet1.setValueTextColor(Color.BLACK);
//        lineDataSet1.setAxisDependency(YAxis.AxisDependency.LEFT); // Set dependency to left Y axis
//
//// Create a dataset for the second line (to be plotted against the right Y axis)
//        LineDataSet lineDataSet2 = new LineDataSet(entriesLine2, "Completion");
//        lineDataSet2.setColor(Color.GREEN);
//        lineDataSet2.setValueTextColor(Color.BLACK);
//        lineDataSet2.setAxisDependency(YAxis.AxisDependency.RIGHT); // Set dependency to right Y axis
//
//// Combine both datasets into LineData and set it to the chart
//        LineData data = new LineData(lineDataSet1, lineDataSet2);
//        lineChart.setData(data);

// Refresh chart
        lineChart.invalidate();

        // ***************************************************************
        // Initialization: initialize line chart from the database
        // ***************************************************************
        initialize_LineChart("Both");


        // react to the selection change of Toggle Buttons
        ThemedToggleButtonGroup toggleButtonGroup = findViewById(R.id.toggle_buttons);
        ThemedButton buttonBoth = findViewById(R.id.button_both);

        // Initialize button_both to be selected at beginning
        toggleButtonGroup.post(new Runnable() {
            @Override
            public void run() {
                toggleButtonGroup.selectButton(R.id.button_both);
            }
        });


        // Set a listener for selection changes
        toggleButtonGroup.setOnSelectListener((ThemedButton btn) -> {
            // handle selected button
//            return kotlin.Unit.INSTANCE;
            String name_button = btn.getText();
//            Toast.makeText(SaveActivity.this, name_button, Toast.LENGTH_SHORT).show();
            Log.d("Check toggle", name_button);

            initialize_LineChart(name_button);

//            // call initialize_LineChart() here based on
//            switch (name_button) {
//                case "Completion":
////                    Toast.makeText(SaveActivity.this, "1. This is " + name_button, Toast.LENGTH_SHORT).show();
//                    initialize_LineChart(name_button);
//                    break;
//                case "Both":
////                    Toast.makeText(SaveActivity.this, "2. This is " + name_button, Toast.LENGTH_SHORT).show();
//                    initialize_LineChart(name_button);
//                    break;
//                case "Timeliness":
////                    Toast.makeText(SaveActivity.this, "3. This is " + name_button, Toast.LENGTH_SHORT).show();
//                    initialize_LineChart(name_button);
//                    break;
//            }

            return kotlin.Unit.INSTANCE;
        });

//
//        toggleButtonGroup.setOnSelectListener(new ThemedToggleButtonGroup.OnSelectListener() {
//            @Override
//            public void onSelect(ThemedButton button) {
//                // Handle the button selection
//                switch (button.getId()) {
//                    case R.id.button_completion:
//                        // Do something when "Completion" is selected
//                        System.out.println("Completion selected");
//                        break;
//                    case R.id.button_both:
//                        // Do something when "Both" is selected
//                        System.out.println("Both selected");
//                        break;
//                    case R.id.button_timeliness:
//                        // Do something when "Timeliness" is selected
//                        System.out.println("Timeliness selected");
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });


//        entries_line = new ArrayList<>();
//        lineChart = findViewById(R.id.chart);
//
//        lineChart.setDrawGridBackground(false);
//        lineChart.getDescription().setEnabled(false);
//        lineChart.setTouchEnabled(false);
//        lineChart.setDragEnabled(false);
//        lineChart.setScaleEnabled(false);
//
//        // Customize the Axis
//        XAxis xAxis_line = lineChart.getXAxis();
//        // Enable both left and right Y axes
//        YAxis leftAxis = lineChart.getAxisLeft();
//        YAxis rightAxis = lineChart.getAxisRight();
//        rightAxis.setEnabled(true); // Enable the right Y axis
//
//        lineChart.getAxisRight().setEnabled(false);
//        xAxis_line.setPosition(XAxis.XAxisPosition.BOTTOM);
//
//        // Create entries for the first line
//        ArrayList<Entry> entriesLine1 = new ArrayList<>();
//        for(int i = 1; i < 6; i++) {
//            float value = (float) (Math.random() * 10); // Substitute with actual data
//            entriesLine1.add(new Entry(i, i));
//        }
//
//        // Create entries for the second line
//        ArrayList<Entry> entriesLine2 = new ArrayList<>();
//        for(int i = 1; i < 6; i++) {
//            float value = (float) (Math.random() * 100); // Substitute with actual data
//            entriesLine2.add(new Entry(i, value));
//        }
//
//        // Create a dataset and give it a type (for the first line)
//        LineDataSet lineDataSet1 = new LineDataSet(entriesLine1, "Timeliness");
//        lineDataSet1.setColor(Color.BLUE);
//        lineDataSet1.setValueTextColor(Color.BLACK); // Styling options
//        lineDataSet1.setAxisDependency(YAxis.AxisDependency.LEFT); // Set dependency to left Y axis
//
//        // Create a dataset and give it a type (for the second line)
//        LineDataSet lineDataSet2 = new LineDataSet(entriesLine2, "Completion");
//        lineDataSet2.setColor(Color.GREEN);
//        lineDataSet2.setValueTextColor(Color.BLACK); // Styling options
//        lineDataSet2.setAxisDependency(YAxis.AxisDependency.RIGHT); // Set dependency to right Y axis
//
//        // Combine both lines into a LineData object
//        LineData data = new LineData(lineDataSet1, lineDataSet2);
//        lineChart.setData(data);
//
//        // Refresh chart
//        lineChart.invalidate();


//        for(int i=0; i<5; i++){
//            float value = (float) Math.random() * 10 ; // substitute to actual data
//            BarEntry line_entry = new BarEntry(entries_line.size(), Math.round(value));
//            entries_line.add(line_entry);
//        }
//        update_LineChart();

        // Line Chart Initialization based on db

//        Log.d("test what get? ", db.child("Tracker").child(username).child("current_plan").child("end_date").toString());


        // ***************************************************************
        // Initialization: Retrieve and Display Current goal information from the database
        // ***************************************************************
        db.child("Tracker").child(username).child("current_plan").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                HashMap<String, String> hashmap_tracker = (HashMap<String, String>) task.getResult().getValue();
                Map<String, Map> map_tracker = (Map<String, Map>) task.getResult().getValue();
                if(!task.isSuccessful()){
                    Log.e("firebase_summary", "Error getting data", task.getException());
                }else{
                    Log.d("firebase_summary", String.valueOf(task.getResult().getValue()));
                    if(hashmap_tracker != null){
                        goal_name.setText(hashmap_tracker.get("goal"));
                        String end_date = hashmap_tracker.get("end_date");

                        Map<String, Map> tracker_progress = new HashMap<>();
                        tracker_progress = map_tracker.get("progress");
                        Log.d("test tracker progress", tracker_progress.toString());
//                        Log.d("test tracker progress", tracker_progress.get("20240130").toString());
//                        Log.d("test tracker progress", tracker_progress.get("20240130").get("status").toString());

                        // update Completion this week
                        Integer cur_completion = Integer.valueOf(hashmap_tracker.get("completion").toString());
                        updateProgress(cur_completion);

                        // Calculate the current average timeliness score
                        double timeliness_avg = 0;
                        double count = 0.0;
                        for (String key:tracker_progress.keySet()) {
                            String cur_status = tracker_progress.get(key).get("status").toString();
                            if (cur_status.equals("1")) {
                                timeliness_avg += Double.valueOf(tracker_progress.get(key).get("score_timeliness").toString());
                                count += 1;
                            }
                        }

                        timeliness_avg = timeliness_avg / count;
                        double timeliness_rounded = Math.round(timeliness_avg * 10) / 10.0;

                        TextView cur_avg_score = findViewById(R.id.cur_avg_score);
//                        cur_avg_score.setText("Average Timeliness Score: " + String.valueOf(timeliness_rounded));
                        cur_avg_score.setText(String.valueOf(timeliness_rounded));

                        // move current_plan to history plan if finished
                        String end_date_status = tracker_progress.get(end_date).get("status").toString();
                        String start_date = hashmap_tracker.get("start_date").toString();

                        if (end_date_status.equals("0") || end_date_status.equals("1")){
                            // calculate the average timeliness score
//                            double timeliness_avg = 0;
//                            double count = 0.0;
//                            for (String key:tracker_progress.keySet()) {
//                                String cur_status = tracker_progress.get(key).get("status").toString();
//                                if (cur_status.equals("1")){
//                                    timeliness_avg += Double.valueOf(tracker_progress.get(key).get("score_timeliness").toString());
//                                    count += 1;
//                                }
//
//                            }

//                            timeliness_avg = timeliness_avg / count;

                            db.child("Tracker").child(username).child("history_plan").child(start_date).setValue(map_tracker);
                            db.child("Tracker").child(username).child("history_completion").child(start_date).setValue(map_tracker.get("completion"));
                            db.child("Tracker").child(username).child("history_timeliness_avg").child(start_date).setValue(timeliness_avg);
                            new_gaol_reminder.setVisibility(View.VISIBLE);
                        }

//                        if (end_date.equals(removeSlashes(dateAsString))){
//                            String end_date_status = tracker_progress.get(end_date).get("status").toString();
//                            String start_date = hashmap_tracker.get("start_date").toString();
//                            if (end_date_status.equals("0") || end_date_status.equals("1")){
//                                db.child("Tracker").child(username).child("history_plan").child(start_date).setValue(map_tracker);
//                            }
//                        }

//                        for (){
//                            float value = (float) Math.random() * 10 ; // substitute to actual data
//                            BarEntry bar_entry = new BarEntry(entries_bar.size(), Math.round(value));
//                            entries_bar.add(bar_entry);
//                        }

                        // build BarEntry list
                        List<BarEntry> entries = new ArrayList<>();

//                        // add entries，use index as the label of X axis
//                        entries.add(new BarEntry(0f, 10f));
//                        entries.add(new BarEntry(1f, 20f));
//                        entries.add(new BarEntry(2f, 30f));
//                        entries.add(new BarEntry(3f, 0f));
//                        entries.add(new BarEntry(5f, 20f));

                        // set X axis label
//                        String[] labels = new String[] {"Label1", "Label2", "Label3", "Label4", "Label5", "Label6", "Label7"};
                        String[] keys = tracker_progress.keySet().toArray(new String[0]);
                        Arrays.sort(keys);

                        List<String> status_list = new ArrayList<>();

                        Integer idx = 0;
                        for (String key : keys){
                            Log.d("test key value: ", key + " + " + tracker_progress.get(key).get("status").toString());
                            status_list.add(tracker_progress.get(key).get("status").toString());
                            if (tracker_progress.get(key).get("status").toString().equals("1")){
                                entries.add(new BarEntry(idx, 10f));
                            } else if (tracker_progress.get(key).get("status").toString().equals("-1")) {
                                entries.add(new BarEntry(idx, -1f));
                            } else {  // status equals 0
                                // do nothing
                            }
                            idx++;
                        }

                        boolean reminder_result = hasMinusOneBeforeOneOrZero(status_list);
                        if (reminder_result){
//                            checkin_remind.setVisibility(View.VISIBLE);
//                            checkin_remind.setText("");
                        }

                        // build BarDataSet
                        BarDataSet barDataSet = new BarDataSet(entries, "");

                        // build BarData and set it to chart
                        BarData barData = new BarData(barDataSet);
                        barChart.setData(barData);


                        String[] modified_keys = removeFirstFourChars(keys);
                        XAxis xAxis = barChart.getXAxis();
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(modified_keys));
                        xAxis.setLabelRotationAngle(25f);

                        // refresh chart
                        barChart.invalidate();
                    }
                }
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

        selfcheckin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // Determine whether there is an exist current plan
                db.child("Tracker").child(username).child("current_plan").child("goal").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        String current_plan_name = task.getResult().getValue().toString();
                        if(!task.isSuccessful()){
                            Log.e("firebase_tracker", "Error getting data", task.getException());
                            Toast.makeText(SaveActivity.this, "Please set up a new goal first", Toast.LENGTH_SHORT).show();
                        }else{
                            Log.d("firebase_tracker", String.valueOf(task.getResult().getValue()));
                            if(current_plan_name != null){
                                // if exist a current plan, open check in window
                                openPopupWindow(v, dateAsString);
                            } else{
                                Toast.makeText(SaveActivity.this, "Please set up a new goal first", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });

        newTracker_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openPopupWindow_newTracker(v, false, new HashMap<String,String>());
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

//    private void update_LineChart() {
//        LineDataSet dataSet = new LineDataSet(entries_line, "Data point");
//        dataSet.setDrawValues(false);
//        dataSet.setColor(Color.BLUE);
//
//        LineData lineData = new LineData(dataSet);
//        lineChart.setData(lineData);
//        lineChart.invalidate();
//    }

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

    public void openSaveActivity(){
        Intent intent = new Intent(this,SaveActivity.class);
        intent.putExtra("username", username);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void openPopupWindow(View view, String date_selected) {
        // initialize popup window
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View viewPopupWindow = layoutInflater.inflate(R.layout.activity_self_checkin_1, null);
//        final PopupWindow popupWindow = new PopupWindow(viewPopupWindow, 600, 600, true);
        final PopupWindow popupWindow = new PopupWindow(viewPopupWindow);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // Dim the background when popup the window
        View container = (View) popupWindow.getContentView().getParent();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        // add flag
        p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.4f;
        wm.updateViewLayout(container, p);

        // initialize elements
        EditText input_date = (EditText) viewPopupWindow.findViewById(R.id.input1);
        TextView textView_date = (TextView) viewPopupWindow.findViewById(R.id.textView_intro);
//        EditText input_feel = (EditText) viewPopupWindow.findViewById(R.id.input2);

//        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
//        Date current_date = new Date();
//        String dateAsString = dateFormat.format(current_date);

        Log.d("Current date: ", dateAsString);
        Log.d("Selected date: ", date_selected);

//        input_date.setText(dateAsString);
        input_date.setText(date_selected);
        textView_date.setText("Date: " + date_selected);

        Button button_yes = (Button) viewPopupWindow.findViewById(R.id.button_yes);
        Button button_no = (Button) viewPopupWindow.findViewById(R.id.button_no);
        String date_without_slash = removeSlashes(input_date.getText().toString());

        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
//                String current_date = input_date.toString();
//                db.child("Tracker").child(username).child("current_plan").child(current_date).setValue(input_feel.toString());

                // handle status change based on user's selection
//                db.child("Tracker").child(username).child("current_plan").child("progress").child(date_without_slash).child("status").setValue(1);

                popupWindow.dismiss();
                openPopupWindow2(v, input_date.getText().toString());
            }
        });

        button_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
//                String current_date = input_date.toString();
//                db.child("Tracker").child(username).child("current_plan").child(current_date).setValue(input_feel.toString());

                // handle status change based on user's selection
                db.child("Tracker").child(username).child("current_plan").child("progress").child(removeSlashes(input_date.getText().toString())).child("status").setValue(0);

//                openPopupWindow(v);
                popupWindow.dismiss();
                openSaveActivity();
            }
        });
    }

    private void openPopupWindow2(View view, String dateAsString) {
        // initialize popup window
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View viewPopupWindow = layoutInflater.inflate(R.layout.activity_self_checkin_2, null);
//        final PopupWindow popupWindow = new PopupWindow(viewPopupWindow, 600, 600, true);
        final PopupWindow popupWindow = new PopupWindow(viewPopupWindow);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // Dim the background when popup the window
        View container = (View) popupWindow.getContentView().getParent();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        // add flag
        p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.4f;
        wm.updateViewLayout(container, p);

        // initialize elements
        TextView textView_date = (TextView) viewPopupWindow.findViewById(R.id.textView_date);
        TextView textView_daily_comp = (TextView) viewPopupWindow.findViewById(R.id.textView_daily);
        TextView textView_weekly_comp = (TextView) viewPopupWindow.findViewById(R.id.textView_weekly);

        EditText input_daily_goal = (EditText) viewPopupWindow.findViewById(R.id.input1);
        EditText input_weekly_goal = (EditText) viewPopupWindow.findViewById(R.id.input2);
        EditText input_emotion = (EditText) viewPopupWindow.findViewById(R.id.input3);
        EditText input_strategy = (EditText) viewPopupWindow.findViewById(R.id.input4);

//        Button button_yes = (Button) viewPopupWindow.findViewById(R.id.button_yes);
//        Button button_no = (Button) viewPopupWindow.findViewById(R.id.button_no);
        Button button_next = (Button) viewPopupWindow.findViewById(R.id.button_confirm);

        textView_date.setText("Date: " + dateAsString);
        String date_without_slash = removeSlashes(dateAsString);

        String txt_daily = "How much (in %) did you complete today out of your <b>daily</b> goal?";
        String txt_weekly = "How much (in %) did you complete out of your <b>weekly</b> goal?";

        textView_daily_comp.setText(Html.fromHtml(txt_daily));
        textView_weekly_comp.setText(Html.fromHtml(txt_weekly));

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
//                String current_date = input_date.toString();
//                db.child("Tracker").child(username).child("current_plan").child(current_date).setValue(input_feel.toString());

                String txt_daily_goal = input_daily_goal.getText().toString();
                String txt_weekly_goal = input_weekly_goal.getText().toString();
                String txt_cur_emotion = input_emotion.getText().toString();
                String txt_cur_strategy = input_strategy.getText().toString();

                Integer int_daily = Integer.parseInt(txt_daily_goal);
                Integer int_weekly = Integer.parseInt(txt_weekly_goal);

                Log.d("Print daily: ", int_daily.toString());
                Log.d("Print weekly: ", int_weekly.toString());

                if (txt_daily_goal.isEmpty() || txt_weekly_goal.isEmpty() || txt_cur_emotion.isEmpty() || txt_cur_strategy.isEmpty()){
                    Toast.makeText(SaveActivity.this,  "Empty input", Toast.LENGTH_SHORT).show();
                } else if (int_daily<0 || int_daily>100 || int_weekly<0 || int_weekly>100) {
                    Toast.makeText(SaveActivity.this,  "Invalid input", Toast.LENGTH_SHORT).show();
                } else {
                    // Daily check in
                    db.child("Tracker").child(username).child("current_plan").child("progress").child(date_without_slash).child("goal_daily").setValue(txt_daily_goal);
                    db.child("Tracker").child(username).child("current_plan").child("progress").child(date_without_slash).child("goal_weekly").setValue(txt_weekly_goal);
                    db.child("Tracker").child(username).child("current_plan").child("progress").child(date_without_slash).child("emotion").setValue(txt_cur_emotion);
                    db.child("Tracker").child(username).child("current_plan").child("progress").child(date_without_slash).child("strategy").setValue(txt_cur_strategy);
                    db.child("Tracker").child(username).child("current_plan").child("completion").setValue(txt_weekly_goal);

                    popupWindow.dismiss();
                    openPopupWindow3(v, dateAsString);
                }
//                popupWindow.dismiss();
//                openPopupWindow3(v, dateAsString);

            }
        });
    }

    private void openPopupWindow3(View view, String dateAsString) {
        // initialize popup window
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View viewPopupWindow = layoutInflater.inflate(R.layout.activity_self_checkin_3, null);
//        final PopupWindow popupWindow = new PopupWindow(viewPopupWindow, 600, 600, true);
        final PopupWindow popupWindow = new PopupWindow(viewPopupWindow);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // Dim the background when popup the window
        View container = (View) popupWindow.getContentView().getParent();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        // add flag
        p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.4f;
        wm.updateViewLayout(container, p);

        String date_without_slash = removeSlashes(dateAsString);

        // initialize elements
        RadioGroup radiogroup1 = (RadioGroup) viewPopupWindow.findViewById(R.id.radioGroup1);
        RadioGroup radiogroup2 = (RadioGroup) viewPopupWindow.findViewById(R.id.radioGroup2);
        RadioGroup radiogroup3 = (RadioGroup) viewPopupWindow.findViewById(R.id.radioGroup3);
        RadioGroup radiogroup4 = (RadioGroup) viewPopupWindow.findViewById(R.id.radioGroup4);
        RadioGroup radiogroup5 = (RadioGroup) viewPopupWindow.findViewById(R.id.radioGroup5);

//        Integer int_answer1;
//        Integer int_answer2;
//        Integer int_answer3;
//        Integer int_answer4;
//        Integer int_answer5;


//        Integer score_result = 0;

        Button button_next = (Button) viewPopupWindow.findViewById(R.id.button_confirm);

        // get user's new radio button chose
        radiogroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button = (RadioButton) viewPopupWindow.findViewById(checkedId);
                txt_answer1 = selected_button.getText().toString();
            }
        });

        radiogroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button = (RadioButton) viewPopupWindow.findViewById(checkedId);
                txt_answer2 = selected_button.getText().toString();
            }
        });

        radiogroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button = (RadioButton) viewPopupWindow.findViewById(checkedId);
                txt_answer3 = selected_button.getText().toString();
            }
        });

        radiogroup4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button = (RadioButton) viewPopupWindow.findViewById(checkedId);
                txt_answer4 = selected_button.getText().toString();
            }
        });

        radiogroup5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button = (RadioButton) viewPopupWindow.findViewById(checkedId);
                txt_answer5 = selected_button.getText().toString();
            }
        });



        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
//                String current_date = input_date.toString();
//                db.child("Tracker").child(username).child("current_plan").child(current_date).setValue(input_feel.toString());
//                openPopupWindow3();
                if (txt_answer1 == null || txt_answer2 == null || txt_answer3 == null || txt_answer4 == null || txt_answer5 == null){
                    Toast.makeText(SaveActivity.this,  "Empty input", Toast.LENGTH_SHORT).show();
                } else {
                    // Attention: Question 2 is a reverse scale question
                    Log.d("old answer2", txt_answer2);
                    String[] scores = {"1", "2", "3", "4", "5"};
                    List<String> score_list = Arrays.asList(scores);
                    int newIndex = 2 - (score_list.indexOf(txt_answer2) - 2);
                    txt_answer2  = scores[newIndex];
                    Log.d("new answer2", txt_answer2);


                    Integer score_result = 0;
                    score_result += Integer.parseInt(txt_answer1);
                    score_result += Integer.parseInt(txt_answer2);
                    score_result += Integer.parseInt(txt_answer3);
                    score_result += Integer.parseInt(txt_answer4);
                    score_result += Integer.parseInt(txt_answer5);

                    // make the procrastination score result to timliness score
                    double score_timeliness = Math.round((6.0 - score_result / 5.0) * 10) / 10.0;

                    db.child("Tracker").child(username).child("current_plan").child("progress").child(date_without_slash).child("score_procrastination").setValue(score_result);
                    db.child("Tracker").child(username).child("current_plan").child("progress").child(date_without_slash).child("score_timeliness").setValue(score_timeliness);
                    db.child("Tracker").child(username).child("current_plan").child("progress").child(date_without_slash).child("status").setValue(1);

                    String end_date = db.child("Tracker").child(username).child("current_plan").child("end_date").get().toString();
//                    if (date_without_slash)

                    popupWindow.dismiss();
                    openSaveActivity();
                }

//                popupWindow.dismiss();
            }
        });
    }

    private void openPopupWindow_newTracker(View view, Boolean autofill, HashMap<String, String> user_input) {
        // initialize popup window
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View viewPopupWindow = layoutInflater.inflate(R.layout.activity_tracker_new, null);
//        final PopupWindow popupWindow = new PopupWindow(viewPopupWindow, 600, 600, true);
        final PopupWindow popupWindow = new PopupWindow(viewPopupWindow);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // Dim the background when popup the window
        View container = (View) popupWindow.getContentView().getParent();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        // add flag
        p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.4f;
        wm.updateViewLayout(container, p);

        // initialize elements
        EditText input_goal = (EditText) viewPopupWindow.findViewById(R.id.input1);
        EditText input_purpose = (EditText) viewPopupWindow.findViewById(R.id.input2);
        EditText input_date = (EditText) viewPopupWindow.findViewById(R.id.input3);
        EditText input_duration = (EditText) viewPopupWindow.findViewById(R.id.input4);
        EditText input_emotion = (EditText) viewPopupWindow.findViewById(R.id.input5);
        EditText input_strategy = (EditText) viewPopupWindow.findViewById(R.id.input6);

        input_date.setText(dateAsString);
        input_duration.setText("7 days");

        if (autofill){
            TextView textView_reminder = (TextView) viewPopupWindow.findViewById(R.id.reminder);
            textView_reminder.setVisibility(View.VISIBLE);
            textView_reminder.setText("Reminder: " + user_input.get("goal_specific"));

            input_purpose.setText(user_input.get("purpose"));
            input_emotion.setText(user_input.get("emotion"));
            input_strategy.setText(user_input.get("strategy"));
        }

        Button button_start = (Button) viewPopupWindow.findViewById(R.id.button_confirm);

        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
//                String current_date = input_date.toString();
//                db.child("Tracker").child(username).child("current_plan").child(current_date).setValue(input_feel.toString());
//                openPopupWindow3();

                String txt_goal = input_goal.getText().toString();
                String txt_purpose = input_purpose.getText().toString();
                String txt_date = removeSlashes(input_date.getText().toString());
                String txt_duration = input_duration.getText().toString();
                String txt_emotion = input_emotion.getText().toString();
                String txt_strategy = input_strategy.getText().toString();
                String txt_end_date = txt_date;

                Map<String, Map> tracker_progress = new HashMap<>();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                Calendar cur = Calendar.getInstance();

                for(int i=0; i<7; i++){

                    try {
                        cur.setTime(sdf.parse(txt_date));
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }

                    cur.add(Calendar.DATE, i);
                    String txt_cur_date = sdf.format(cur.getTime());

                    Map<String, Object> progress_info = new HashMap<>();
                    progress_info.put("status", -1); // -1 == have not check-in; 0 == do not have check-in; 1 == have checked in
                    tracker_progress.put(txt_cur_date, progress_info);

                    if (i == 6){
                        txt_end_date = txt_cur_date;
                    }

                    Log.d("test Date", txt_cur_date);
                }

                Log.d("test map", tracker_progress.toString());


                if (txt_goal.isEmpty() || txt_purpose.isEmpty() || txt_date.isEmpty() || txt_duration.isEmpty() || txt_emotion.isEmpty() || txt_strategy.isEmpty() || txt_end_date.isEmpty()){
                    Toast.makeText(SaveActivity.this,  "Empty input", Toast.LENGTH_SHORT).show();
                } else {
                    // Start a new goal
                    db.child("Tracker").child(username).child("current_plan").child("goal").setValue(txt_goal);
                    db.child("Tracker").child(username).child("current_plan").child("purpose").setValue(txt_purpose);
                    db.child("Tracker").child(username).child("current_plan").child("start_date").setValue(txt_date);
                    db.child("Tracker").child(username).child("current_plan").child("duration").setValue(txt_duration);
                    db.child("Tracker").child(username).child("current_plan").child("emotion").setValue(txt_emotion);
                    db.child("Tracker").child(username).child("current_plan").child("strategy").setValue(txt_strategy);
                    db.child("Tracker").child(username).child("current_plan").child("progress").setValue(tracker_progress);
                    db.child("Tracker").child(username).child("current_plan").child("completion").setValue("0");
                    db.child("Tracker").child(username).child("current_plan").child("end_date").setValue(txt_end_date);

                    popupWindow.dismiss();

                    openSaveActivity();
                }
//                popupWindow.dismiss();
            }
        });
    }

    public static String removeSlashes(String date) {
        return date.replace("/", "");
    }

    public static String formatWithSlashes(String date) {
        if (date == null || date.length() != 8) {
            return null; // or throw an IllegalArgumentException
        }
        String year = date.substring(0, 4);
        String month = date.substring(4, 6);
        String day = date.substring(6, 8);

        return year + "/" + month + "/" + day;
    }

    public static String[] removeFirstFourChars(String[] labels) {
        String[] result = new String[labels.length];

        for (int i = 0; i < labels.length; i++) {
            result[i] = (labels[i].length() > 4) ? labels[i].substring(4) : "";
        }

        for (int i = 0; i < labels.length; i++) {
            String month = result[i].substring(0, 2);
            String day = result[i].substring(2, 4);

            result[i] = (labels[i].length() > 4) ? month + "/" + day : result[i];
        }

        return result;
    }

    public static boolean hasMinusOneBeforeOneOrZero(List<String> list) {
        boolean foundMinusOne = false;

        for (String number : list) {
            if ("-1".equals(number)) {
                foundMinusOne = true;
            } else if (("0".equals(number) || "1".equals(number)) && foundMinusOne) {
                return true;
            }
        }

        return false;
    }

    private void icon_setup(int position, String date, Map<String, Map> tracker_progress, String[] modified_date) {
        String status = tracker_progress.get(date).get("status").toString();

        DistortionModelArrayList.get(position).set_date(modified_date[position]);
//        DistortionModelArrayList.get(position)
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date current_date = new Date();
        String cur_date_str = dateFormat.format(current_date);
//        Log.d("test cur date match: ", String.valueOf(Integer.parseInt(date) < Integer.parseInt(cur_date_str)));

        switch (status) {
//            DistortionModelArrayList.get(position).s
            case "-1": // -1 == have not check-in;
                DistortionModelArrayList.get(position).set_score("-");
                if (Integer.parseInt(date) < Integer.parseInt(cur_date_str)){
                    DistortionModelArrayList.get(position).setImgid(R.drawable.timeliness_past);
                } else{
                    DistortionModelArrayList.get(position).setImgid(R.drawable.timeliness_pending);
                }
                break;
            case "0":  // 0 == not to have daily check-in;
                DistortionModelArrayList.get(position).set_score("-");
                DistortionModelArrayList.get(position).setImgid(R.drawable.timeliness_no);
                break;
            case "1":  // 1 == have checked in
                String cur_score = tracker_progress.get(date).get("score_timeliness").toString();
                double score_timeliness = Double.valueOf(cur_score);
                if (score_timeliness <= 1.5){
                    DistortionModelArrayList.get(position).setImgid(R.drawable.timeliness_yes1);
                } else if (1.5 < score_timeliness && score_timeliness <= 2.5) {
                    DistortionModelArrayList.get(position).setImgid(R.drawable.timeliness_yes2);
                } else if (2.5 < score_timeliness && score_timeliness <= 3.5) {
                    DistortionModelArrayList.get(position).setImgid(R.drawable.timeliness_yes3);
                } else if (3.5 < score_timeliness && score_timeliness <= 4.5) {
                    DistortionModelArrayList.get(position).setImgid(R.drawable.timeliness_yes4);
                } else if (4.5 < score_timeliness) {
                    DistortionModelArrayList.get(position).setImgid(R.drawable.timeliness_yes5);
                }
                DistortionModelArrayList.get(position).set_score(cur_score);
//                DistortionModelArrayList.get(position).setImgid(R.drawable.timeliness_yes);
                break;
            default:
                break;
        }

    }

    private void initialize_Gridview() {
        // Gridview image initialize
        db.child("Tracker").child(username).child("current_plan").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                HashMap<String, String> hashmap_tracker = (HashMap<String, String>) task.getResult().getValue();
                Map<String, Map> map_tracker = (Map<String, Map>) task.getResult().getValue();
                if(!task.isSuccessful()){
                    Log.e("firebase_summary", "Error getting data", task.getException());
                }else{
//                    Log.d("firebase_summary", String.valueOf(task.getResult().getValue()));
                    if(hashmap_tracker != null) {
                        Map<String, Map> tracker_progress = new HashMap<>();
                        tracker_progress = map_tracker.get("progress");
                        Log.d("test tracker progress", tracker_progress.toString());
//                        Log.d("test tracker progress", tracker_progress.get("20240130").toString());
//                        Log.d("test tracker progress", tracker_progress.get("20240130").get("status").toString());

                        // set X axis label
//                        String[] labels = new String[] {"Label1", "Label2", "Label3", "Label4", "Label5", "Label6", "Label7"};
                        String[] keys = tracker_progress.keySet().toArray(new String[0]);
                        Arrays.sort(keys);
                        String[] modified_date = removeFirstFourChars(keys);

                        List<String> status_list = new ArrayList<>();

                        for (int idx = 0; idx < 7; idx++) {
                            icon_setup(idx, keys[idx], tracker_progress, modified_date);
//                            Log.d("test key value: ", key + " + " + tracker_progress.get(key).get("status").toString());
//                            status_list.add(tracker_progress.get(key).get("status").toString());
                        }

                    }

                }
                gridView.setAdapter(adapter);
            }
        });
    }


    private void initialize_LineChart(String toggle_selection) {
        // Line Chart initialize
        db.child("Tracker").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String, Map> map_tracker = (Map<String, Map>) task.getResult().getValue();
                if(!task.isSuccessful()){
                    Log.e("firebase_summary", "Error getting data", task.getException());
                }else{
                    Log.d("firebase_summary", String.valueOf(task.getResult().getValue()));
                    if (map_tracker != null){
                        Map<String, String> map_completion = map_tracker.get("history_completion");
                        Map<String, String> map_timeliness = map_tracker.get("history_timeliness_avg");

                        if (map_completion != null && map_timeliness != null){
                            String[] keys = map_completion.keySet().toArray(new String[0]);  // Error here!!!!!!
                            Arrays.sort(keys);
                            String[] modified_date = removeFirstFourChars(keys);

                            List<String> completion_list = new ArrayList<>();
                            List<String> timeliness_list = new ArrayList<>();

                            ArrayList<Entry> entriesLine1 = new ArrayList<>();
                            ArrayList<Entry> entriesLine2 = new ArrayList<>();

                            Integer idx = 0;
                            for (String key : keys){
                                completion_list.add(map_completion.get(key).toString());
                                timeliness_list.add(String.valueOf(map_timeliness.get(key)));

                                // Timeliness
                                entriesLine1.add(new Entry(idx, Float.valueOf(String.valueOf(map_timeliness.get(key)))));
                                // Completion
                                entriesLine2.add(new Entry(idx, Float.valueOf(map_completion.get(key).toString())));

                                idx++;
                            }

                            Log.d("map_completion", String.valueOf(map_completion));
                            Log.d("map_timeliness", String.valueOf(map_timeliness));

                            // Create a dataset for the first line (to be plotted against the left Y axis)
                            LineDataSet lineDataSet1 = new LineDataSet(entriesLine1, "Timeliness");
//                            lineDataSet1.setColor(Color.RED);
                            lineDataSet1.setValueTextColor(Color.BLACK);
                            lineDataSet1.setAxisDependency(YAxis.AxisDependency.RIGHT); // Set dependency to left Y axis

                            // Create a dataset for the second line (to be plotted against the right Y axis)
                            LineDataSet lineDataSet2 = new LineDataSet(entriesLine2, "Completion");
//                            lineDataSet2.setColor(Color.GREEN);
                            lineDataSet2.setValueTextColor(Color.BLACK);
                            lineDataSet2.setAxisDependency(YAxis.AxisDependency.LEFT); // Set dependency to right Y axis

                            // Set the line width and text size
                            lineDataSet1.setLineWidth(2f);
                            lineDataSet1.setValueTextSize(10f);

                            lineDataSet2.setLineWidth(2f);
                            lineDataSet2.setValueTextSize(10f);

                            lineDataSet1.setColor(Color.parseColor("#1402B9"));
                            lineDataSet2.setColor(Color.parseColor("#60AC50"));

                            String[] modified_keys = removeFirstFourChars(keys);
                            tooltip_date = modified_keys;

//                        XAxis xAxis = lineChart.getXAxis();
//                        xAxis.setValueFormatter(new IndexAxisValueFormatter(modified_keys));
//                        xAxis.setLabelRotationAngle(25f);

                            // Set up the X-axis with custom labels
                            XAxis xAxis = lineChart.getXAxis();
                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                            xAxis.setGranularity(1f); // Minimum interval between the axis values
                            xAxis.setGranularityEnabled(true); // Enable granularity to prevent skipping labels

                            // Custom ValueFormatter to convert index to label from the keys array
                            xAxis.setValueFormatter(new ValueFormatter() {
                                @Override
                                public String getFormattedValue(float value) {
                                    // Convert float value to int and use as index to fetch label from keys array
                                    int index = (int) value;
                                    // Check index bounds to prevent IndexOutOfBoundsException
                                    if (index >= 0 && index < modified_keys.length) {
                                        // return an empty X-axis value
                                        return "";
//                                        return modified_keys[index];
                                    } else {
                                        return "";
                                    }
                                }
                            });

                            // Combine both datasets into LineData and set it to the chart
                            LineData data = new LineData(lineDataSet1, lineDataSet2);
//                            LineData data1 = new LineData(lineDataSet1);
//                            LineData data2 = new LineData(lineDataSet2);

                            // React based on the value of toggle_selection
                            if ("Completion".equals(toggle_selection)) {
                                // Handle case when toggle_selection is "Completion"
                                data = new LineData(lineDataSet2);
                            } else if ("Both".equals(toggle_selection)) {
                                // Handle case when toggle_selection is "Both"
                                data = new LineData(lineDataSet1, lineDataSet2);
                            } else if ("Timeliness".equals(toggle_selection)) {
                                // Handle case when toggle_selection is "Timeliness"
                                data = new LineData(lineDataSet1);
                            } else {
                                // Handle any default or error case
                                data = new LineData(lineDataSet1, lineDataSet2);
                            }

//                            if (iterate_num % 3 == 0){
//                                data = new LineData(lineDataSet1, lineDataSet2);
//                            } else if (iterate_num % 3 == 1) {
//                                data = new LineData(lineDataSet1);
//                            } else {
//                                data = new LineData(lineDataSet2);
//                            }
//
//                            iterate_num += 1;

                            lineChart.setData(data);

                            // Highlight the last data points
                            int lastPointIndex1 = entriesLine1.size() - 1;
                            int lastPointIndex2 = entriesLine2.size() - 1;

                            lineDataSet1.setCircleColors(Color.BLUE);
                            lineDataSet2.setCircleColors(Color.GREEN);

                            Drawable drawable1 = ContextCompat.getDrawable(SaveActivity.this, R.drawable.highlight_point1);
                            Drawable drawable2 = ContextCompat.getDrawable(SaveActivity.this, R.drawable.highlight_point2);

                            // Set the icon for the last entry of lineDataSet1
                            if (lastPointIndex1 >= 0 && lastPointIndex1 < lineDataSet1.getEntryCount()) {
                                Entry lastEntry1 = lineDataSet1.getEntryForIndex(lastPointIndex1);
                                lastEntry1.setIcon(drawable1);
                            }

                            // Set the icon for the last entry of lineDataSet2
                            if (lastPointIndex2 >= 0 && lastPointIndex2 < lineDataSet2.getEntryCount()) {
                                Entry lastEntry2 = lineDataSet2.getEntryForIndex(lastPointIndex2);
                                lastEntry2.setIcon(drawable2);
                            }

                            CustomMarkerView markerView = new CustomMarkerView(SaveActivity.this, R.layout.marker_view_layout);
                            lineChart.setMarker(markerView);

                            // Refresh chart
                            lineChart.invalidate();
                        }
                    }
                }
            }
        });
    }

    private void updateProgress(int progress) {
        progressBar.setProgress(progress);
        String progressPercentage = progress + "%";
        progressText.setText(progressPercentage);
        completion_score.setText(progressPercentage);
    }

    private void showSpeechBubble(View anchorView, int position) {
//        if (speechBubble == null) {
//            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//            View bubbleView = layoutInflater.inflate(R.layout.speech_bubble_layout, null);
////            View bubbleView = LayoutInflater.from(this).inflate(R.layout.speech_bubble_layout, null);
//            speechBubble = new PopupWindow(bubbleView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
//            // Customize the PopupWindow as needed
//        }
//        Log.d("get width: ", String.valueOf(anchorView.getWidth()));
//        Log.d("get height: ", String.valueOf(anchorView.getHeight()));
//        speechBubble.showAsDropDown(anchorView, anchorView.getWidth(), -anchorView.getHeight()+120);

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View bubbleView = layoutInflater.inflate(R.layout.speech_bubble_layout, null);
//            View bubbleView = LayoutInflater.from(this).inflate(R.layout.speech_bubble_layout, null);
        speechBubble = new PopupWindow(bubbleView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        TextView bubble_text = bubbleView.findViewById(R.id.speechBubbleText);
        Button button_check_in = bubbleView.findViewById(R.id.button_checkin);

//        bubble_text.setText(String.valueOf(position));

        // set the shape of the bubble
        if (position == 5){
            bubbleView.setBackgroundResource(R.drawable.speech_bubble2);
        } else if (position == 6) {
            bubbleView.setBackgroundResource(R.drawable.speech_bubble3);
        }

        db.child("Tracker").child(username).child("current_plan").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Map<String, Map> map_tracker = (Map<String, Map>) task.getResult().getValue();
                if(!task.isSuccessful()){
                    Log.e("firebase_summary", "Error getting data", task.getException());
                }else{
                    Map<String, Map> map_progress = map_tracker.get("progress");
                    String[] keys = map_progress.keySet().toArray(new String[0]);
                    Arrays.sort(keys);

                    // selected date: keys[position]
                    // Extract year, month, and day from the string
                    String year = keys[position].substring(0, 4);
                    String month = keys[position].substring(4, 6);
                    String day = keys[position].substring(6, 8);

                    // Format the string as YYYY/MM/DD
                    String selected_date = String.format("%s/%s/%s", year, month, day);

                    String content_pending = "";
//                    String content = "";
//                    String htmlString = "<h2>Title</h2><br><p>This is a sample paragraph of <b>HTML</b> text.</p>";

                    String current_status = (String) map_progress.get(keys[position]).get("status").toString();

                    switch (current_status) {
                        case "1":
                            String content_html = "Completion-daily: " + map_progress.get(keys[position]).get("goal_daily") + "%<br>"
                                    + "Completion-weekly: " + map_progress.get(keys[position]).get("goal_weekly") + "%<br>"
                                    + "Emotion: " + map_progress.get(keys[position]).get("emotion") + "<br>"
                                    + "Strategy: " + map_progress.get(keys[position]).get("strategy") + "<br>";
//                            if (position == 5 || position == 6){
//                                content_html = "<br>" + content_html;
//                            }

                            bubble_text.setText(Html.fromHtml(content_html));

                            button_check_in.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#C9C9C9")));
                            break;
                        case "0":
                            String content_html2 = "You didn’t have a task to complete on this date.";
                            bubble_text.setText(Html.fromHtml(content_html2));

                            // Enbale check in button
                            button_check_in.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v){
                                    // Determine whether there is an exist current plan
                                    db.child("Tracker").child(username).child("current_plan").child("goal").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            String current_plan_name = task.getResult().getValue().toString();
                                            if(!task.isSuccessful()){
                                                Log.e("firebase_tracker", "Error getting data", task.getException());
                                                Toast.makeText(SaveActivity.this, "Please set up a new goal first", Toast.LENGTH_SHORT).show();
                                            }else{
                                                Log.d("firebase_tracker", String.valueOf(task.getResult().getValue()));
                                                if(current_plan_name != null){
                                                    // if exist a current plan, open check in window
                                                    speechBubble.dismiss(); // need to dismiss the current speech bubble first
                                                    openPopupWindow(v, selected_date);
                                                } else{
                                                    Toast.makeText(SaveActivity.this, "Please set up a new goal first", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    });
                                }
                            });

                            break;
                        case "-1":
                            String content_html3 = "You have not recorded your progress for this day. Submit one through the “Self-Checkin” button.";
                            bubble_text.setText(Html.fromHtml(content_html3));

                            // Enbale check in button
                            button_check_in.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v){
                                    // Determine whether there is an exist current plan
                                    db.child("Tracker").child(username).child("current_plan").child("goal").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                                            String current_plan_name = task.getResult().getValue().toString();
                                            if(!task.isSuccessful()){
                                                Log.e("firebase_tracker", "Error getting data", task.getException());
                                                Toast.makeText(SaveActivity.this, "Please set up a new goal first", Toast.LENGTH_SHORT).show();
                                            }else{
                                                Log.d("firebase_tracker", String.valueOf(task.getResult().getValue()));
                                                if(current_plan_name != null){
                                                    // if exist a current plan, open check in window
                                                    speechBubble.dismiss(); // need to dismiss the current speech bubble first
                                                    openPopupWindow(v, selected_date);
                                                } else{
                                                    Toast.makeText(SaveActivity.this, "Please set up a new goal first", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    });
                                }
                            });
                            break;
                        default:
                            break;
                    }

//                    bubble_text.setText(String.valueOf(current_status));
                }
            }
        });


        // Calculate x and y coordinates for the PopupWindow
        int[] location = new int[2];
        anchorView.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1] + anchorView.getHeight(); // Below the button

        // Show the PopupWindow
        speechBubble.showAtLocation(anchorView, Gravity.NO_GRAVITY, x, y);
    }

    public class CustomMarkerView extends MarkerView {
        private TextView tvContent;

        public CustomMarkerView(Context context, int layoutResource) {
            super(context, layoutResource);
            // Find your TextView by its id
            tvContent = findViewById(R.id.tvContent);
        }

        // This method is called every time the MarkerView is redrawn,
        // use it to update the content (user-interface)
        @Override
        public void refreshContent(Entry e, Highlight highlight) {
            String txt_content = "Date: " + tooltip_date[(int)e.getX()] + "\n"
                        + "Value: " + e.getY();
            tvContent.setText(txt_content);
//             tvContent.setText(String.format("Value: %s", e.getY()));
            super.refreshContent(e, highlight);
        }

        @Override
        public MPPointF getOffset() {
            // This provides the offset for the MarkerView. It's the difference between the touch point and the marker view
            return new MPPointF(-(getWidth() / 2), getHeight()-45);
        }
    }


}
