package com.example.clearmind;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LearnActivity extends AppCompatActivity {

    private Button save_button;
    private Button achieve_button;
    private Button profile_button;

    private String username;

    private String name;

    private TextView welcome;
    private TextView instruction;

    private ImageButton imgBtn_presurvey;
    private ImageButton imgBtn_chapter1;
    private ImageButton imgBtn_chapter2;
    private ImageButton imgBtn_chapter3;
    private ImageButton imgBtn_chapter4;
    private ImageButton imgBtn_postsurvey;

    private DatabaseReference db;

    String status_presurvey;
    String status_chapter1;
    String status_chapter2;
    String status_chapter3;
    String status_chapter4;
    String status_postsurvey;

    private Integer progressValue = 0;

    private ValueEventListener learn_valueEventListener;

    // Use UsageStatsManager to get App Usage data
    public void fetchUsageStats() {
        UsageStatsManager usageStatsManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long endTime = calendar.getTimeInMillis();

        calendar.add(Calendar.DAY_OF_YEAR, -7);
        long startTime = calendar.getTimeInMillis();

        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);

        Map<String, Long> usageTimePerDay = new HashMap<>();

        for (UsageStats stats : usageStatsList) {
//            Log.d("This usage stats: ",String.valueOf(stats.getPackageName()));
            if (stats.getPackageName().equals(getPackageName())) {
                Log.d("This usage stats: ",String.valueOf(stats.getTotalTimeInForeground()));
                Log.d("This usage stats: ", new SimpleDateFormat("yyyy-MM-dd").format(new Date(stats.getLastTimeStamp())));

                String day = new SimpleDateFormat("yyyy-MM-dd").format(new Date(stats.getLastTimeStamp()));
                long totalTimeForeground = 0; //foreground time
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    totalTimeForeground = usageTimePerDay.getOrDefault(day, 0L);
                }
                usageTimePerDay.put(day, totalTimeForeground + stats.getTotalTimeInForeground());
            }
        }

        Integer count_days = 0;
        Integer count_hours = 0;
        Integer count_mins = 0;
        for (Map.Entry<String, Long> entry : usageTimePerDay.entrySet()) {
            Log.d("AppUsage", "Day: " + entry.getKey() + ", Usage Time: " + entry.getValue() + " (in hours: " + entry.getValue() / (1000 * 60 * 60) + " in mins: " + (entry.getValue() / (1000 * 60)) % 60);

            if(entry.getValue() != 0){
                count_days += 1;
                if (entry.getKey().equals("2024-04-28")){
                    count_hours = Math.toIntExact(entry.getValue() / (1000 * 60 * 60));
                    count_mins = Math.toIntExact((entry.getValue() / (1000 * 60)) % 60);
                }
            }

            db.child("AppUsage").child(username).child(entry.getKey()).setValue(entry.getValue());

            db.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String status_app_usage = String.valueOf(snapshot.child("AppUsage").child(username).child(entry.getKey()).getValue());
                    Log.d("AppUsage status: ", status_app_usage);

                    if (status_app_usage.equals(null)){
                        db.child("AppUsage").child(username).child(entry.getKey()).setValue(entry.getValue());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(LearnActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
                }
            });

        }
        Log.d("AppUsage", "Days: " + count_days + " Hours: " + count_hours + " Mins: " + count_mins);

    }

    public void fetchUsageStats_2() {
        UsageStatsManager usageStatsManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        calendar.add(Calendar.DAY_OF_YEAR, -1);
        long startTime = calendar.getTimeInMillis();

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        long endTime = calendar.getTimeInMillis();

        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);

        long totalTimeForeground = 0;
        for (UsageStats stats : usageStatsList) {
            if (stats.getPackageName().equals(getPackageName())) {
                Log.d("This usage stats: ",String.valueOf(stats.getTotalTimeInForeground()));
                Log.d("This usage stats: ", new SimpleDateFormat("yyyy-MM-dd").format(new Date(stats.getLastTimeStamp())));

                totalTimeForeground = stats.getTotalTimeInForeground();
                break;
            }
        }

        int hours = (int) (totalTimeForeground / (1000 * 60 * 60));
        int minutes = (int) ((totalTimeForeground / (1000 * 60)) % 60);
        String formattedTime = "Usage time yesterday: " + hours + " hours, " + minutes + " minutes";

        Log.d("AppUsage yesterday: ", formattedTime);

    }

    private Map<String, Long> fetchYesterdayUsageStats() {
        UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        calendar.add(Calendar.DAY_OF_YEAR, -1);
        long endTime = calendar.getTimeInMillis();

        calendar.add(Calendar.DAY_OF_YEAR, -6);
        long startTime = calendar.getTimeInMillis();
//
//        calendar.add(Calendar.DAY_OF_YEAR, 1);
//        long endTime = calendar.getTimeInMillis();

        Log.d("AppUsage startTime: ", String.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(startTime)));
        Log.d("AppUsage endTime: ", String.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(endTime)));

//        ======================
//        Map<String, Long> usageTimePerDay = new HashMap<>();
//
//        for (UsageStats stats : usageStatsList) {
////            Log.d("This usage stats: ",String.valueOf(stats.getPackageName()));
//            if (stats.getPackageName().equals(getPackageName())) {
//                Log.d("This usage stats: ",String.valueOf(stats.getTotalTimeInForeground()));
//                Log.d("This usage stats: ", new SimpleDateFormat("yyyy-MM-dd").format(new Date(stats.getLastTimeStamp())));
//
//                String day = new SimpleDateFormat("yyyy-MM-dd").format(new Date(stats.getLastTimeStamp()));
//                long totalTimeForeground = 0; //foreground time
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
//                    totalTimeForeground = usageTimePerDay.getOrDefault(day, 0L);
//                }
//                usageTimePerDay.put(day, totalTimeForeground + stats.getTotalTimeInForeground());
//            }
//        }

//        ======================


        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);
        Map<String, Long> usageTimePerDay = new HashMap<>();

//        long totalTimeForeground = 0;
        for (UsageStats stats : usageStatsList) {
            if (stats.getPackageName().equals(getPackageName())) {
//                Log.d("This usage stats: ",String.valueOf(stats.getTotalTimeInForeground()));
//                Log.d("This usage stats: ", new SimpleDateFormat("yyyy-MM-dd").format(new Date(stats.getLastTimeStamp())));

//                totalTimeForeground = stats.getTotalTimeInForeground();
                String day = new SimpleDateFormat("yyyy-MM-dd").format(new Date(stats.getLastTimeStamp()));

//                Log.d("This usage stats: ",String.valueOf(stats.getTotalTimeInForeground()));
                Log.d("This usage stats: ", day + " -> " + String.valueOf(stats.getTotalTimeInForeground()));

                long totalTimeForeground = 0; //foreground time
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    totalTimeForeground = usageTimePerDay.getOrDefault(day, 0L);
                }
                usageTimePerDay.put(day, totalTimeForeground + stats.getTotalTimeInForeground());


//                break;
            }
        }

//        int hours = (int) (totalTimeForeground / (1000 * 60 * 60));
//        int minutes = (int) ((totalTimeForeground / (1000 * 60)) % 60);
//        String formattedTime = "Usage time yesterday: " + hours + " hours, " + minutes + " minutes";
//
//        Log.d("AppUsage yesterday: ", formattedTime);

        return usageTimePerDay;
    }

    private void storeUsageTimeIfNotExists(String date, long totalTimeForeground) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        String dateKey = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());

        db.child("AppUsage").child(username).child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    db.child("AppUsage").child(username).child(date).setValue(totalTimeForeground);
                    Log.d("FirebaseDB set", "Stored new data for " + date);
                } else {
                    // data existed
                    Log.d("FirebaseDB set", "Data for " + date + " already exists.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("FirebaseDB", "Failed to read value.", databaseError.toException());
            }
        });
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        Intent intent = getIntent();

        // Enable full screen display and avoid nav bar overlap
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_learn), (v, windowInsets) -> {
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
        this.db = FirebaseDatabase.getInstance().getReference();
//        getData();

        save_button = (Button) findViewById(R.id.button_save);
        achieve_button = (Button) findViewById(R.id.button_achieve);
        profile_button = (Button) findViewById(R.id.button_profile);

        achieve_button.setVisibility(View.GONE);

        welcome = findViewById(R.id.hello);
        instruction = findViewById(R.id.instruction);
//        welcome.setText("Hi, " + name + "! Welcome to ClearMind Learn page. You can find 4 chapters here, let's see your challenge for today.");

        imgBtn_presurvey = findViewById(R.id.imgbutton_presurvey);
        imgBtn_chapter1 = findViewById(R.id.imgbutton_chapter1);
        imgBtn_chapter2 = findViewById(R.id.imgbutton_chapter2);
        imgBtn_chapter3 = findViewById(R.id.imgbutton_chapter3);
        imgBtn_chapter4 = findViewById(R.id.imgbutton_chapter4);
        imgBtn_postsurvey = findViewById(R.id.imgbutton_postsurvey);

        // get the App Usage data
//        fetchUsageStats_2();
        Map<String, Long> usageTime = fetchYesterdayUsageStats();
        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        }

        for (int i = 1; i < 8; i++) {
            LocalDate date = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                date = LocalDate.now().minusDays(i);
            }
            String dateString = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                dateString = date.format(formatter);
            }

//            Log.d("Get dates: ", dateString);

            if (usageTime.containsKey(dateString)) {
                Log.d("Get dates: ", dateString + " -> " + usageTime.get(dateString));
                Log.d("Call storeUsage", "called");
                storeUsageTimeIfNotExists(dateString, usageTime.get(dateString));
            } else {
                Log.d("Get dates: ", dateString + " -> " + "XXXXXXXX");
                Log.d("Call storeUsage", "called");
                storeUsageTimeIfNotExists(dateString, 0L);
            }
        }
        Log.d("Loop completed", "for loop has finished executing");

        // Done: calculate and display in getData()
        getData();

//        getData_new();


        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openSaveActivity();
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

    protected void onDestroy() {
        super.onDestroy();
        removeValueEventListener();
    }

    private void removeValueEventListener() {
        if (learn_valueEventListener != null) {
            db.removeEventListener(learn_valueEventListener);
            learn_valueEventListener = null; // Clear the reference
        }
    }

    private void openPreSurveyACtivity() {
        Intent intent = new Intent(this,PreSurvey1_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void openPostSurveyACtivity() {
        Intent intent = new Intent(this,PostSurvey1_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void openSaveActivity(){
        Intent intent = new Intent(this,SaveActivity.class);
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

    public void getData_new() {
        learn_valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, HashMap<String, HashMap>> db_learn= (HashMap<String, HashMap<String, HashMap>>) snapshot.getValue();
                Log.d("Check getData_2 ", String.valueOf(db_learn));

                ScrollView sv = (ScrollView) findViewById(R.id.scrollView);

                Log.d("Inside check: ", "fetch appUsage start");
                Map<String, Long> appUsage = db_learn.get("AppUsage").get(username);
                Log.d("Inside check: ", "fetch appUsage end");

//                    Log.d("Get AppUsage in DB: ", appUsage.toString());
                Long totalUsage = 0L;
                Integer totalDates = 0;
                Long yesterdayUsage = 0L;

                DateTimeFormatter formatter = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                }

                for (int i = 1; i < 8; i++) {
                    LocalDate date = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        date = LocalDate.now().minusDays(i);
                    }
                    String dateString = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        dateString = date.format(formatter);
                    }


                    Log.d("Inside check: ", String.valueOf(i));

                    if (appUsage!=null && appUsage.containsKey(dateString)){
                        Log.d("Get dates in DB: ", dateString);
                        Log.d("Get time in DB:  ", appUsage.get(dateString).toString());
                        if (i == 1){
                            yesterdayUsage = appUsage.get(dateString);
                        }
                        if (appUsage.get(dateString) > 0L){
                            totalUsage += appUsage.get(dateString);
                            totalDates += 1;
                        }
//                            totalUsage += appUsage.get(dateString);
//                            totalDates += 1;
                    }

                }

                Log.d("Display totalUsage: ", totalUsage.toString());
                Log.d("Display totalDates: ", totalDates.toString());
                Log.d("Display yesterdayU: ", yesterdayUsage.toString());

                Integer count_hours_yesterday = Math.toIntExact(yesterdayUsage / (1000 * 60 * 60));
                Integer count_mins_yesterday = Math.toIntExact((yesterdayUsage / (1000 * 60)) % 60);

                Integer count_hours_average = Math.toIntExact((totalUsage/7) / (1000 * 60 * 60));
                Integer count_mins_average = Math.toIntExact(((totalUsage/7) / (1000 * 60)) % 60);

                // Get user info and
                // Done: generate customized welcome/instructions here
                name = db_learn.get("register").get(username).get("name").toString();
                welcome.setText("Hi, " + name + "!");

                // New instruction text in 2 rows
                String text_instruction_new = "Signed in <u>" + totalDates.toString();
                if (totalDates > 1){
                    text_instruction_new = text_instruction_new + " days</u> this week (<u>";
                } else {
                    text_instruction_new = text_instruction_new + " day</u> this week (<u>";
                }


                Integer count_mins_average_new = Math.toIntExact((totalUsage/7) / (1000 * 60));

                if (count_mins_average > 1){
                    text_instruction_new = text_instruction_new + count_mins_average_new + " mins</u> avg.)<br>" + "Spent <u>";
                } else{
                    text_instruction_new = text_instruction_new + count_mins_average_new + " min</u>/ avg.)<br>" + "Spent <u>";
                }

                if (count_hours_yesterday > 0){
                    if (count_hours_yesterday == 1){
                        text_instruction_new = text_instruction_new + count_hours_yesterday + " hour ";
                    } else{
                        text_instruction_new = text_instruction_new + count_hours_yesterday + " hours ";
                    }
                }

                if (count_mins_yesterday > 1){
                    text_instruction_new = text_instruction_new + count_mins_yesterday + " mins</u> yesterday";
                } else{
                    text_instruction_new = text_instruction_new + count_mins_yesterday + " min</u> yesterday";
                }


                instruction.setText(Html.fromHtml(text_instruction_new));


                progressValue = 0;
                // Done: Handle progress bar initialization
                ArrayList<String> chapter_list = new ArrayList<String>();
                chapter_list.add("Chapter1");
                chapter_list.add("Chapter2");
                chapter_list.add("Chapter3");
                chapter_list.add("Chapter4");

                for (String chapter : chapter_list) {
                    HashMap<String, String> progress_chapter = (HashMap<String, String>) db_learn.get(chapter).get("progress").get(username);
                    if (progress_chapter != null) {
                        for (String value : progress_chapter.values()) {
                            if (value.equals("1")) {
//                                Log.d("firebase_progress", "value: " + value);
                                progressValue += 1;
//                                Log.d("firebase_progress", "progressValue: " + String.valueOf(progressValue));
                            }
                        }
                    }
                }

                Log.d("ProgressBar_value", String.valueOf(progressValue));

                CustomProgressBar progressBar = findViewById(R.id.progressBar2);

                Log.d("Calculate result", String.valueOf(progressValue));
                float ratio = (float) progressValue / 29;
                float cur_progress = (float) ratio * 100;
                Log.d("Calculate result", String.valueOf(Math.round(cur_progress)));
//                progressBar.getUsername(username);
                progressBar.setProgress(Math.round(cur_progress));
                progressBar.setProgressValue(progressValue);
                progressBar.invalidate();

                int int_progress = Math.round(cur_progress);
                // make sure int_progress in range of [0,100]
                if (int_progress < 0) {
                    int_progress = 0;
                } else if (int_progress > 100) {
                    int_progress = 100;
                }

                TextView progressbar_text = findViewById(R.id.progressBar2_text);
                progressbar_text.setText(int_progress + "%");
//                    progressbar_text.setText(progressValue+"/29");

                // ============================================
                // Initialize chapter buttons
                status_presurvey = db_learn.get("progress").get(username).get("presurvey").toString();
                status_chapter1 = db_learn.get("progress").get(username).get("chapter1").toString();
                status_chapter2 = db_learn.get("progress").get(username).get("chapter2").toString();
                status_chapter3 = db_learn.get("progress").get(username).get("chapter3").toString();
                status_chapter4 = db_learn.get("progress").get(username).get("chapter4").toString();
                status_postsurvey = db_learn.get("progress").get(username).get("postsurvey").toString();

                // HANDLE PRE-SURVEY BUTTON
                if (status_presurvey.equals("0")) {
                    imgBtn_presurvey.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(LearnActivity.this, "Open PreSurvey page", Toast.LENGTH_SHORT).show();
//                                openPreSurveyACtivity();
                            openPopupWindow_presurey(view);
                        }
                    });
                } else {
                    // status == "2"
                    imgBtn_presurvey.setImageResource(R.drawable.imgbutton_presurvey_2);
                    imgBtn_presurvey.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(LearnActivity.this, "You have finished the PreSurvey", Toast.LENGTH_SHORT).show();
//                                openPreSurveyACtivity();
//                                openPopupWindow_presurey(view);
                        }
                    });
                }

                // HANDLE CHAPTER 1 BUTTON
                if (status_chapter1.equals("0")){
                    imgBtn_chapter1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(status_presurvey.equals("2")){
                                Toast.makeText(LearnActivity.this, "Open Chapter 1 page", Toast.LENGTH_SHORT).show();
                                // openPreSurveyACtivity();
                                openChapterOneActivity();
                            } else{
                                Toast.makeText(LearnActivity.this, "Please complete previous Chapter first!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else if (status_chapter1.equals("1")) {
                    imgBtn_chapter1.setImageResource(R.drawable.imgbutton_chapter1_1);
                    imgBtn_chapter1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(LearnActivity.this, "Open Chapter 1 page", Toast.LENGTH_SHORT).show();
                            openChapterOneActivity();
                        }
                    });
                } else {
                    // status == "2"
                    imgBtn_chapter1.setImageResource(R.drawable.imgbutton_chapter1_2);
                    imgBtn_chapter1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(LearnActivity.this, "Open Chapter 1 page", Toast.LENGTH_SHORT).show();
                            openChapterOneActivity();
                        }
                    });
                }

                // HANDLE CHAPTER 2 BUTTON
                if (status_chapter2.equals("0")){
                    imgBtn_chapter2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(status_chapter1.equals("2")){
                                Toast.makeText(LearnActivity.this, "Open Chapter 2 page", Toast.LENGTH_SHORT).show();
                                openChapterTwoActivity();
                            } else {
                                Toast.makeText(LearnActivity.this, "Please complete previous Chapter first!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else if (status_chapter2.equals("1")) {
                    imgBtn_chapter2.setImageResource(R.drawable.imgbutton_chapter2_1);
                    imgBtn_chapter2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(LearnActivity.this, "Open Chapter 2 page", Toast.LENGTH_SHORT).show();
                            openChapterTwoActivity();
                        }
                    });
                } else {
                    // status == "2"

                    //Set up starting position of ScrollView
                    sv.post(new Runnable() {
                        @Override
                        public void run() {
                            //setting position here :
//                            sv.scrollTo(0, 300*3);  // without animation
                            sv.smoothScrollTo(0,900);  // with animation
                        }
                    });

                    imgBtn_chapter2.setImageResource(R.drawable.imgbutton_chapter2_2);
                    imgBtn_chapter2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(LearnActivity.this, "Open Chapter 2 page", Toast.LENGTH_SHORT).show();
                            openChapterTwoActivity();
                        }
                    });
                }


                // Handle really chapter3
                // HANDLE CHAPTER 3 BUTTON
                if (status_chapter3.equals("0")){
                    imgBtn_chapter3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(status_chapter2.equals("2")){
                                Toast.makeText(LearnActivity.this, "Open Chapter 3 page", Toast.LENGTH_SHORT).show();
                                openChapterThreeActivity();
                            } else {
                                Toast.makeText(LearnActivity.this, "Please complete previous Chapter first!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else if (status_chapter3.equals("1")) {
                    imgBtn_chapter3.setImageResource(R.drawable.imgbutton_chapter3_1);
                    imgBtn_chapter3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(LearnActivity.this, "Open Chapter 3 page", Toast.LENGTH_SHORT).show();
                            openChapterThreeActivity();
                        }
                    });
                } else {
                    // status == "2"
                    imgBtn_chapter3.setImageResource(R.drawable.imgbutton_chapter3_2);
                    imgBtn_chapter3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(LearnActivity.this, "Open Chapter 3 page", Toast.LENGTH_SHORT).show();
                            openChapterThreeActivity();
                        }
                    });
                }


                // Handle really chapter4
                // HANDLE CHAPTER 4 BUTTON
                if (status_chapter4.equals("0")){
                    imgBtn_chapter4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(status_chapter3.equals("2")){
                                Toast.makeText(LearnActivity.this, "Open Chapter 4 page", Toast.LENGTH_SHORT).show();
                                openChapterFourActivity();
                            } else {
                                Toast.makeText(LearnActivity.this, "Please complete previous Chapter first!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else if (status_chapter4.equals("1")) {
                    imgBtn_chapter4.setImageResource(R.drawable.imgbutton_chapter4_1);
                    imgBtn_chapter4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(LearnActivity.this, "Open Chapter 4 page", Toast.LENGTH_SHORT).show();
                            openChapterFourActivity();
                        }
                    });
                } else {
                    // status == "2"
                    imgBtn_chapter4.setImageResource(R.drawable.imgbutton_chapter4_2);
                    imgBtn_chapter4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(LearnActivity.this, "Open Chapter 4 page", Toast.LENGTH_SHORT).show();
                            openChapterFourActivity();
                        }
                    });
                }

                // HANDLE POST-SURVEY BUTTON
                if (status_postsurvey.equals("0")) {
                    imgBtn_postsurvey.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(status_chapter4.equals("2")){
                                Toast.makeText(LearnActivity.this, "Open PostSurvey page", Toast.LENGTH_SHORT).show();
                                openPostSurveyACtivity();
                            } else {
                                Toast.makeText(LearnActivity.this, "Please complete previous Chapter first!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // status == "2"
                    imgBtn_postsurvey.setImageResource(R.drawable.imgbutton_postsurvey_2);
                    imgBtn_postsurvey.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(LearnActivity.this, "You have finished the PostSurvey", Toast.LENGTH_SHORT).show();
//                                openPostSurveyACtivity();
                        }
                    });
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LearnActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        };

        db.addValueEventListener(learn_valueEventListener);
    }


    public void getData() {
        db.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    Log.e("Learn Page", "Error getting data", task.getException());
                }else{
//                    HashMap<String, String> hashmap_presurvey= (HashMap<String, String>) task.getResult().getValue();
//                    Log.d("Learn Page", String.valueOf(task.getResult().getValue()));
                    ScrollView sv = (ScrollView) findViewById(R.id.scrollView);

                    HashMap<String, HashMap<String, HashMap>> db_learn= (HashMap<String, HashMap<String, HashMap>>) task.getResult().getValue();
//                    Log.d("Learn Page", String.valueOf(hashmap_learn.get("Chapter1")));

                    Log.d("Inside check: ", "fetch appUsage start");
                    Map<String, Long> appUsage = db_learn.get("AppUsage").get(username);
                    Log.d("Inside check: ", "fetch appUsage end");

//                    Log.d("Get AppUsage in DB: ", appUsage.toString());
                    Long totalUsage = 0L;
                    Integer totalDates = 0;
                    Long yesterdayUsage = 0L;

                    DateTimeFormatter formatter = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    }

                    for (int i = 1; i < 8; i++) {
                        LocalDate date = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            date = LocalDate.now().minusDays(i);
                        }
                        String dateString = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            dateString = date.format(formatter);
                        }

//                        Log.d("Get dates in DB: ", dateString);
//                        if (dateString != null) {
//                            if (appUsage.containsKey(dateString)){
//                                Log.d("Get dates in DB: ", dateString);
//                                Log.d("Get time in DB:  ", appUsage.get(dateString).toString());
//                                if (i == 1){
//                                    yesterdayUsage = appUsage.get(dateString);
//                                }
//                                if (appUsage.get(dateString) > 0L){
//                                    totalUsage += appUsage.get(dateString);
//                                    totalDates += 1;
//                                }
////                            totalUsage += appUsage.get(dateString);
////                            totalDates += 1;
//                            }
//                        }


                        Log.d("Inside check: ", String.valueOf(i));

                        if (appUsage!=null && appUsage.containsKey(dateString)){
                            Log.d("Get dates in DB: ", dateString);
                            Log.d("Get time in DB:  ", appUsage.get(dateString).toString());
                            if (i == 1){
                                yesterdayUsage = appUsage.get(dateString);
                            }
                            if (appUsage.get(dateString) > 0L){
                                totalUsage += appUsage.get(dateString);
                                totalDates += 1;
                            }
//                            totalUsage += appUsage.get(dateString);
//                            totalDates += 1;
                        }
                    }

                    Log.d("Display totalUsage: ", totalUsage.toString());
                    Log.d("Display totalDates: ", totalDates.toString());
                    Log.d("Display yesterdayU: ", yesterdayUsage.toString());

                    Integer count_hours_yesterday = Math.toIntExact(yesterdayUsage / (1000 * 60 * 60));
                    Integer count_mins_yesterday = Math.toIntExact((yesterdayUsage / (1000 * 60)) % 60);

                    Integer count_hours_average = Math.toIntExact((totalUsage/7) / (1000 * 60 * 60));
                    Integer count_mins_average = Math.toIntExact(((totalUsage/7) / (1000 * 60)) % 60);

                    // Get user info and
                    // Done: generate customized welcome/instructions here
                    name = db_learn.get("register").get(username).get("name").toString();
                    welcome.setText("Hi, " + name + "!");

////                    instruction.setText("Welcome to ClearMind Learn page. You can find 4 Parts here, let's see your challenge for today.");
//                    String text_instruction = "Signed in <u>" + totalDates.toString();
//                    if (totalDates > 1){
//                        text_instruction = text_instruction + " days</u> this week<br>(<u>";
//                    } else {
//                        text_instruction = text_instruction + " day</u> this week<br>(<u>";
//                    }
//
//                    if (count_hours_average > 0) {
//                        if (count_hours_average == 1){
//                            text_instruction = text_instruction + count_hours_average + " hour ";
//                        } else{
//                            text_instruction = text_instruction + count_hours_average + " hours ";
//                        }
//                    }
//
//                    if (count_mins_average > 1){
//                        text_instruction = text_instruction + count_mins_average + " mins</u>/day average)<br>" + "Spent <u>";
//                    } else{
//                        text_instruction = text_instruction + count_mins_average + " min</u>/day average)<br>" + "Spent <u>";
//                    }
//
//                    if (count_hours_yesterday > 0){
//                        if (count_hours_yesterday == 1){
//                            text_instruction = text_instruction + count_hours_yesterday + " hour ";
//                        } else{
//                            text_instruction = text_instruction + count_hours_yesterday + " hours ";
//                        }
//                    }
//
//                    if (count_mins_yesterday > 1){
//                        text_instruction = text_instruction + count_mins_yesterday + " mins</u> yesterday";
//                    } else{
//                        text_instruction = text_instruction + count_mins_yesterday + " min</u> yesterday";
//                    }

                    // Hard code example
//                    String text_example = "Signed in 4 days this week (148min avg.)<br>";
//                    text_example += "Spent 3 hours 20 mins yesterday";
//                    text_example += "(32 mins/day average)";


                    // New instruction text in 2 rows
                    String text_instruction_new = "Signed in <u>" + totalDates.toString();
                    if (totalDates > 1){
                        text_instruction_new = text_instruction_new + " days</u> this week (<u>";
                    } else {
                        text_instruction_new = text_instruction_new + " day</u> this week (<u>";
                    }


                    Integer count_mins_average_new = Math.toIntExact((totalUsage/7) / (1000 * 60));

                    if (count_mins_average > 1){
                        text_instruction_new = text_instruction_new + count_mins_average_new + " mins</u> avg.)<br>" + "Spent <u>";
                    } else{
                        text_instruction_new = text_instruction_new + count_mins_average_new + " min</u>/ avg.)<br>" + "Spent <u>";
                    }

                    if (count_hours_yesterday > 0){
                        if (count_hours_yesterday == 1){
                            text_instruction_new = text_instruction_new + count_hours_yesterday + " hour ";
                        } else{
                            text_instruction_new = text_instruction_new + count_hours_yesterday + " hours ";
                        }
                    }

                    if (count_mins_yesterday > 1){
                        text_instruction_new = text_instruction_new + count_mins_yesterday + " mins</u> yesterday";
                    } else{
                        text_instruction_new = text_instruction_new + count_mins_yesterday + " min</u> yesterday";
                    }


                    instruction.setText(Html.fromHtml(text_instruction_new));

                    // Done: Handle progress bar initialization
                    ArrayList<String> chapter_list = new ArrayList<String>();
                    chapter_list.add("Chapter1");
                    chapter_list.add("Chapter2");
                    chapter_list.add("Chapter3");
                    chapter_list.add("Chapter4");

                    for (String chapter : chapter_list) {
                        HashMap<String, String> progress_chapter = (HashMap<String, String>) db_learn.get(chapter).get("progress").get(username);
                        if (progress_chapter != null) {
                            for (String value : progress_chapter.values()) {
                                if (value.equals("1")) {
//                                Log.d("firebase_progress", "value: " + value);
                                    progressValue += 1;
//                                Log.d("firebase_progress", "progressValue: " + String.valueOf(progressValue));
                                }
                            }
                        }
                    }

                    Log.d("ProgressBar_value", String.valueOf(progressValue));

                    CustomProgressBar progressBar = findViewById(R.id.progressBar2);

                    Log.d("Calculate result", String.valueOf(progressValue));
                    float ratio = (float) progressValue / 29;
                    float cur_progress = (float) ratio * 100;
                    Log.d("Calculate result", String.valueOf(Math.round(cur_progress)));
//                progressBar.getUsername(username);
                    progressBar.setProgress(Math.round(cur_progress));
                    progressBar.setProgressValue(progressValue);
                    progressBar.invalidate();

                    int int_progress = Math.round(cur_progress);
                    // make sure int_progress in range of [0,100]
                    if (int_progress < 0) {
                        int_progress = 0;
                    } else if (int_progress > 100) {
                        int_progress = 100;
                    }

                    TextView progressbar_text = findViewById(R.id.progressBar2_text);
                    progressbar_text.setText(int_progress + "%");
//                    progressbar_text.setText(progressValue+"/29");

                    // ============================================
                    // Initialize chapter buttons
                    status_presurvey = db_learn.get("progress").get(username).get("presurvey").toString();
                    status_chapter1 = db_learn.get("progress").get(username).get("chapter1").toString();
                    status_chapter2 = db_learn.get("progress").get(username).get("chapter2").toString();
                    status_chapter3 = db_learn.get("progress").get(username).get("chapter3").toString();
                    status_chapter4 = db_learn.get("progress").get(username).get("chapter4").toString();
                    status_postsurvey = db_learn.get("progress").get(username).get("postsurvey").toString();

                    // HANDLE PRE-SURVEY BUTTON
                    if (status_presurvey.equals("0")) {
                        imgBtn_presurvey.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(LearnActivity.this, "Open PreSurvey page", Toast.LENGTH_SHORT).show();
//                                openPreSurveyACtivity();
                                openPopupWindow_presurey(view);
                            }
                        });
                    } else {
                        // status == "2"
                        imgBtn_presurvey.setImageResource(R.drawable.imgbutton_presurvey_2);
                        imgBtn_presurvey.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(LearnActivity.this, "You have finished the PreSurvey", Toast.LENGTH_SHORT).show();
//                                openPreSurveyACtivity();
//                                openPopupWindow_presurey(view);
                            }
                        });
                    }

                    // HANDLE CHAPTER 1 BUTTON
                    if (status_chapter1.equals("0")){
                        imgBtn_chapter1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(status_presurvey.equals("2")){
                                    Toast.makeText(LearnActivity.this, "Open Chapter 1 page", Toast.LENGTH_SHORT).show();
                                    // openPreSurveyACtivity();
                                    openChapterOneActivity();
                                } else{
                                    Toast.makeText(LearnActivity.this, "Please complete previous Chapter first!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else if (status_chapter1.equals("1")) {
                        imgBtn_chapter1.setImageResource(R.drawable.imgbutton_chapter1_1);
                        imgBtn_chapter1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(LearnActivity.this, "Open Chapter 1 page", Toast.LENGTH_SHORT).show();
                                openChapterOneActivity();
                            }
                        });
                    } else {
                        // status == "2"
                        imgBtn_chapter1.setImageResource(R.drawable.imgbutton_chapter1_2);
                        imgBtn_chapter1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(LearnActivity.this, "Open Chapter 1 page", Toast.LENGTH_SHORT).show();
                                openChapterOneActivity();
                            }
                        });
                    }

                    // HANDLE CHAPTER 2 BUTTON
                    if (status_chapter2.equals("0")){
                        imgBtn_chapter2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(status_chapter1.equals("2")){
                                    Toast.makeText(LearnActivity.this, "Open Chapter 2 page", Toast.LENGTH_SHORT).show();
                                    openChapterTwoActivity();
                                } else {
                                    Toast.makeText(LearnActivity.this, "Please complete previous Chapter first!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else if (status_chapter2.equals("1")) {
                        imgBtn_chapter2.setImageResource(R.drawable.imgbutton_chapter2_1);
                        imgBtn_chapter2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(LearnActivity.this, "Open Chapter 2 page", Toast.LENGTH_SHORT).show();
                                openChapterTwoActivity();
                            }
                        });
                    } else {
                        // status == "2"

                        //Set up starting position of ScrollView
                        sv.post(new Runnable() {
                            @Override
                            public void run() {
                                //setting position here :
//                            sv.scrollTo(0, 300*3);  // without animation
                                sv.smoothScrollTo(0,900);  // with animation
                            }
                        });

                        imgBtn_chapter2.setImageResource(R.drawable.imgbutton_chapter2_2);
                        imgBtn_chapter2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(LearnActivity.this, "Open Chapter 2 page", Toast.LENGTH_SHORT).show();
                                openChapterTwoActivity();
                            }
                        });
                    }


                    // Handle really chapter3
                    // HANDLE CHAPTER 3 BUTTON
                    if (status_chapter3.equals("0")){
                        imgBtn_chapter3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(status_chapter2.equals("2")){
                                    Toast.makeText(LearnActivity.this, "Open Chapter 3 page", Toast.LENGTH_SHORT).show();
                                    openChapterThreeActivity();
                                } else {
                                    Toast.makeText(LearnActivity.this, "Please complete previous Chapter first!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else if (status_chapter3.equals("1")) {
                        imgBtn_chapter3.setImageResource(R.drawable.imgbutton_chapter3_1);
                        imgBtn_chapter3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(LearnActivity.this, "Open Chapter 3 page", Toast.LENGTH_SHORT).show();
                                openChapterThreeActivity();
                            }
                        });
                    } else {
                        // status == "2"
                        imgBtn_chapter3.setImageResource(R.drawable.imgbutton_chapter3_2);
                        imgBtn_chapter3.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(LearnActivity.this, "Open Chapter 3 page", Toast.LENGTH_SHORT).show();
                                openChapterThreeActivity();
                            }
                        });
                    }


                    // Handle really chapter4
                    // HANDLE CHAPTER 4 BUTTON
                    if (status_chapter4.equals("0")){
                        imgBtn_chapter4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(status_chapter3.equals("2")){
                                    Toast.makeText(LearnActivity.this, "Open Chapter 4 page", Toast.LENGTH_SHORT).show();
                                    openChapterFourActivity();
                                } else {
                                    Toast.makeText(LearnActivity.this, "Please complete previous Chapter first!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else if (status_chapter4.equals("1")) {
                        imgBtn_chapter4.setImageResource(R.drawable.imgbutton_chapter4_1);
                        imgBtn_chapter4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(LearnActivity.this, "Open Chapter 4 page", Toast.LENGTH_SHORT).show();
                                openChapterFourActivity();
                            }
                        });
                    } else {
                        // status == "2"
                        imgBtn_chapter4.setImageResource(R.drawable.imgbutton_chapter4_2);
                        imgBtn_chapter4.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(LearnActivity.this, "Open Chapter 4 page", Toast.LENGTH_SHORT).show();
                                openChapterFourActivity();
                            }
                        });
                    }

                    // HANDLE POST-SURVEY BUTTON
                    if (status_postsurvey.equals("0")) {
                        imgBtn_postsurvey.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(status_chapter4.equals("2")){
                                    Toast.makeText(LearnActivity.this, "Open PostSurvey page", Toast.LENGTH_SHORT).show();
                                    openPostSurveyACtivity();
                                } else {
                                    Toast.makeText(LearnActivity.this, "Please complete previous Chapter first!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        // status == "2"
                        imgBtn_postsurvey.setImageResource(R.drawable.imgbutton_postsurvey_2);
                        imgBtn_postsurvey.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(LearnActivity.this, "You have finished the PostSurvey", Toast.LENGTH_SHORT).show();
//                                openPostSurveyACtivity();
                            }
                        });
                    }

                }
            }
        });

    }

    public void openProfileActivity(){
        Intent intent = new Intent(this,ProfileActivity.class);
        intent.putExtra("username", username);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void openChapterOneActivity() {
        Intent intent = new Intent(this,Chapter1_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void openChapterTwoActivity() {
        Intent intent = new Intent(this,Chapter2_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void openChapterThreeActivity() {
        Intent intent = new Intent(this,Chapter3_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void openChapterFourActivity() {
        Intent intent = new Intent(this,Chapter4_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void openPopupWindow_presurey(View view) {
        // initialize popup window
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View viewPopupWindow = layoutInflater.inflate(R.layout.activity_popup_pre_survey, null);
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
        p.dimAmount = 0.7f;
        wm.updateViewLayout(container, p);

        // initialize elements
        Button button_confirm = (Button) viewPopupWindow.findViewById(R.id.button_confirm);

        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openPreSurveyACtivity();
                popupWindow.dismiss();
            }
        });
    }

}
