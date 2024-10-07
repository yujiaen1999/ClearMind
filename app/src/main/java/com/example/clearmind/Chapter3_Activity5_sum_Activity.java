package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Chapter3_Activity5_sum_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;

    private Button button_back;
    private Button button_next;
    private ImageButton button_home;
    private long pageOpenTime;
    private long pageCloseTime;

    private final NavigationDrawerHelper navigationDrawerHelper = new NavigationDrawerHelper(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter3_activity5_summary);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

        button_home = findViewById(R.id.button_home);
        button_back = findViewById(R.id.button_previous);
        button_next = findViewById(R.id.button_next);

        LinearLayout distortion_0 = findViewById(R.id.distortion_0);
        LinearLayout distortion_1 = findViewById(R.id.distortion_1);
        LinearLayout distortion_2 = findViewById(R.id.distortion_2);
        LinearLayout distortion_3 = findViewById(R.id.distortion_3);
        LinearLayout distortion_4 = findViewById(R.id.distortion_4);
        LinearLayout distortion_5 = findViewById(R.id.distortion_5);
        LinearLayout distortion_6 = findViewById(R.id.distortion_6);
        LinearLayout distortion_7 = findViewById(R.id.distortion_7);
        LinearLayout distortion_8 = findViewById(R.id.distortion_8);
        LinearLayout distortion_9 = findViewById(R.id.distortion_9);

        // get data from bd
        db.child("Chapter3").child("activity5").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                HashMap<String, String> hashmap_activity5 = (HashMap<String, String>) task.getResult().getValue();
//                hashmap_activity5 = hashmap_here;
                Log.d("Hashmap Check inside", String.valueOf(hashmap_activity5));
                if(!task.isSuccessful()){
                    Log.e("firebase_summary", "Error getting data", task.getException());
                }else{
                    Log.d("firebase_summary", String.valueOf(task.getResult().getValue()));
                    if(hashmap_activity5 != null){
                        String status_0 = hashmap_activity5.get("distortion_0");
                        String status_1 = hashmap_activity5.get("distortion_1");
                        String status_2 = hashmap_activity5.get("distortion_2");
                        String status_3 = hashmap_activity5.get("distortion_3");
                        String status_4 = hashmap_activity5.get("distortion_4");
                        String status_5 = hashmap_activity5.get("distortion_5");
                        String status_6 = hashmap_activity5.get("distortion_6");
                        String status_7 = hashmap_activity5.get("distortion_7");
                        String status_8 = hashmap_activity5.get("distortion_8");
                        String status_9 = hashmap_activity5.get("distortion_9");


//                        Log.d("Status Check", hashmap_activity5.get("distortion_0"));

                        if (!status_0.equals("1")){
                            // distortion invisible
                            distortion_0.setVisibility(View.GONE);
                        }

                        if (!status_1.equals("1")){
                            // distortion invisible
                            distortion_1.setVisibility(View.GONE);
                        }

                        if (!status_2.equals("1")){
                            // distortion invisible
                            distortion_2.setVisibility(View.GONE);
                        }

                        if (!status_3.equals("1")){
                            // distortion invisible
                            distortion_3.setVisibility(View.GONE);
                        }

                        if (!status_4.equals("1")){
                            // distortion invisible
                            distortion_4.setVisibility(View.GONE);
                        }

                        if (!status_5.equals("1")){
                            // distortion invisible
                            distortion_5.setVisibility(View.GONE);
                        }

                        if (!status_6.equals("1")){
                            // distortion invisible
                            distortion_6.setVisibility(View.GONE);
                        }

                        if (!status_7.equals("1")){
                            // distortion invisible
                            distortion_7.setVisibility(View.GONE);
                        }

                        if (!status_8.equals("1")){
                            // distortion invisible
                            distortion_8.setVisibility(View.GONE);
                        }

                        if (!status_9.equals("1")){
                            // distortion invisible
                            distortion_9.setVisibility(View.GONE);
                        }

                    }
                }
            }
        });

        navigationDrawerHelper.setupNavigationDrawer(username);

        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                open_Chapter3();
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
                // update Chapter progress
                Map<String, Object> chapter_progress_update = new HashMap<>();
                chapter_progress_update.put("7_Activity3_5_2", "1");
                db.child("Chapter3").child("progress").child(username).updateChildren(chapter_progress_update);

                open_Next_Activity();
//                openPopupWindow(v);
            }
        });

    }

    private void open_Previous_Activity() {
        Intent intent = new Intent(this,Chapter3_Activity5_2_Activity.class);
        intent.putExtra("username", username);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void open_Next_Activity() {
        Intent intent = new Intent(this,Chapter3_Activity6_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter3() {
        Intent intent = new Intent(this,Chapter3_Activity.class);
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
        DatabaseReference activityRef = db.child("userActivity").child(username).child("Part3_5_Activity5_3");

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
