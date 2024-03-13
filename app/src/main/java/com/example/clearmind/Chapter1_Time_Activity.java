package com.example.clearmind;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Chapter1_Time_Activity  extends AppCompatActivity {
    private String username;
    private DatabaseReference db;

    private Button button_back;
    private Button button_next;
    private Button button_home;

    private String answer1;
    private String answer2;

    private TextView response;

    private Integer frequency = 1;
    private long pageOpenTime;
    private long pageCloseTime;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter1_time);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

        createNotificationChannel("@time", "@TimeManagement", NotificationManager.IMPORTANCE_HIGH);

        button_home = findViewById(R.id.button_home);
        button_back = findViewById(R.id.button_previous);
        button_next = findViewById(R.id.button_next);

        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                open_Chapter1();
            }
        });

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                open_Previous_Activity();
            }
        });

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String notification_content = "Time notification set successfully. You will get a system notification every ";
                if (frequency == 1){
                    notification_content = notification_content + frequency.toString() + " day.";
                } else{
                    notification_content = notification_content + frequency.toString() + " days.";
                }
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                Notification notification = new NotificationCompat.Builder(Chapter1_Time_Activity.this, "@time")
                        .setContentTitle("Time Notification enabled")
                        .setContentText(notification_content)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.notification_icon)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .build();
                manager.notify(1, notification);

                Intent myIntent = new Intent(getApplicationContext(), TimeBroadcast.class);
//                myIntent.putExtra("username", username);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent, PendingIntent.FLAG_IMMUTABLE);
//                myIntent.setAction("CLOCK_IN");

                // Use AlarmManager to send notification at regular intervals
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, 10);
                calendar.set(Calendar.MINUTE, 00);
                calendar.set(Calendar.SECOND, 00);

                Log.d("NotificationFrequency", frequency.toString());

                // intervalMillis: hours * minutes
//                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), frequency * 1000 ,pendingIntent); // test
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), frequency * 24 * 60 * 60 * 1000, pendingIntent);

                // update Chapter1 progress
                Map<String, Object> chapter1_progress_update = new HashMap<>();
                chapter1_progress_update.put("6_Time_Management", "1");
                db.child("Chapter1").child("progress").child(username).updateChildren(chapter1_progress_update);

                open_Next_Activity();
            }
        });

        RadioGroup radiogroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
        RadioGroup radiogroup2 = (RadioGroup) findViewById(R.id.radioGroup2);

        response = findViewById(R.id.textView3);

        radiogroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                answer1 = selected_button_1.getText().toString();
                response.setText("Your planned frequency to use the App is: " + answer1 + "; And for each visit, the time you plan to spend is: " +  answer2);

                switch (answer1){
                    case "A. Daily":
                        frequency = 1;
                        break;
                    case "B. Once a week":
                        frequency = 7;
                        break;
                    case "C. 2-3 times a week":
                        frequency = 3;
                        break;
                    default:
                        break;
                }
            }
        });

        radiogroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_2 = (RadioButton) findViewById(checkedId);
                answer2 = selected_button_2.getText().toString();
                response.setText("Your planned frequency to use the App is: " + answer1 + "; And for each visit, the time you plan to spend is: " +  answer2);
            }
        });

    }

    private String createNotificationChannel(String channelId, String channelName, int level){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(channelId, channelName, level);
            channel.setDescription("Notification level " + level) ;

            manager.createNotificationChannel(channel);
        }

        return channelId;
    }


    private void open_Previous_Activity() {
//        Intent intent = new Intent(this,Chapter1_Intro_ACT_Activity.class);
        Intent intent = new Intent(this, Chapter1_Activity2_Question_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Next_Activity() {
        Intent intent = new Intent(this,Chapter1_Summary_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter1() {
        Intent intent = new Intent(this,Chapter1_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pageOpenTime = System.currentTimeMillis(); // get the page open time
    }

    @Override
    protected void onPause() {
        super.onPause();
        pageCloseTime = System.currentTimeMillis(); // get the page close time

        if (pageCloseTime - pageOpenTime > 1999){   // Only if the view time >= 2 seconds
            sendTimeStampsToFirebase(); // store the Time Stamp to Firebase
        }
//        sendTimeStampsToFirebase();
    }

    private void sendTimeStampsToFirebase() {
        DatabaseReference activityRef = db.child("userActivity").child(username).child("Part1_4_How_To_Use");

        // create a new activityID
        String activityId = activityRef.push().getKey();

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");

        Date resultdate_open = new Date(pageOpenTime);
        Date resultdate_close = new Date(pageCloseTime);

        Map<String, Object> activityData = new HashMap<>();
        activityData.put("openTime_ms", pageOpenTime);
        activityData.put("closeTime_ms", pageCloseTime);
        activityData.put("duration", pageCloseTime - pageOpenTime);

        activityData.put("openTime_str", String.valueOf(resultdate_open));
        activityData.put("closeTime_str", String.valueOf(resultdate_close));

        if (activityId != null) {
            activityRef.child(activityId).setValue(activityData)
                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "Activity time recorded successfully"))
                    .addOnFailureListener(e -> Log.d("Firebase", "Failed to record activity time", e));
        }
    }

}
