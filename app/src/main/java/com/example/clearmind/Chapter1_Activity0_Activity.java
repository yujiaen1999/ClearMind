package com.example.clearmind;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Chapter1_Activity0_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;

    private Button button_back;
    private Button button_next;
    private ImageButton button_home;
    private Button button_submit;

    private long pageOpenTime;
    private long pageCloseTime;

    private RecyclerView recyclerView;
    private DragValueAdapter adapter;
    private ItemTouchHelper touchHelper;
    private String topChoice = "Career";

    private final NavigationDrawerHelper navigationDrawerHelper = new NavigationDrawerHelper(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter1_activity0);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

        button_home = findViewById(R.id.button_home);
        button_back = findViewById(R.id.button_previous);
        button_next = findViewById(R.id.button_next);
        button_submit = findViewById(R.id.button_submit);
        recyclerView = findViewById(R.id.life_values_list);
        adapter = new DragValueAdapter(Arrays.asList("Marriage or Intimate relationships", "Family"
                , "Career", "Health", "Friendships", "Personal Growth (such as Self-awareness and Reflection)"
                , "Financial Security", "Leisure and Hobbies", "Community Involvement", "Spirituality or Religion"), viewHolder -> touchHelper.startDrag(viewHolder));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new DragCallback(adapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        navigationDrawerHelper.setupNavigationDrawer(username);

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                List<String> sortedValues = adapter.getValues();

                db.child("Chapter1").child("activity0_user_input").child(username).setValue(sortedValues)
                        .addOnSuccessListener(aVoid -> Log.d("Firebase", "Data saved successfully"))
                        .addOnFailureListener(e -> Log.e("Firebase", "Failed to save data", e));

                // update hashmap of Firebase database: Chapter1-activity0
                updateHashmap(sortedValues);
            }
        });

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
                // update Chapter1 progress
                Map<String, Object> chapter1_progress_update = new HashMap<>();
                chapter1_progress_update.put("2_Activity1_0", "1");
                db.child("Chapter1").child("progress").child(username).updateChildren(chapter1_progress_update);

                open_Next_Activity();
            }
        });
    }

    private void updateHashmap(List<String> user_choice) {
//        ==========================
//        Read database once
//        ==========================
        db.child("Chapter1").child("activity0").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                Log.d("firebase", "In updateHashmap");
                // might have error if task is not success
                HashMap<String, Object> hashmap_reasons = (HashMap<String, Object>) task.getResult().getValue();
                HashMap<String, Integer> new_map = new HashMap<>();

                if(!task.isSuccessful()){
                    Log.e("firebase", "Error getting data", task.getException());
                }else{
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    topChoice = user_choice.get(0);

                    for(int i = 0; i < user_choice.size(); i++) {
                        String value = user_choice.get(i);
                        if(hashmap_reasons != null && hashmap_reasons.containsKey(value)){
                            Object obj = hashmap_reasons.get(value);
                            assert obj != null;
                            new_map.put(value, Integer.parseInt(obj.toString()) + (10 - i));
                        } else{
                            new_map.put(value, 10 - i);
                        }
                    }
                    // update hashmap in database
                    Log.d("firebase", "Updating Chapter1 activity0");
                    db.child("Chapter1").child("activity0").setValue(new_map)
                            .addOnSuccessListener(aVoid -> Log.d("Firebase", "Data updated successfully"))
                            .addOnFailureListener(e -> Log.e("Firebase", "Failed to update data", e));
                    Log.d("firebase", "Finished updating Chapter1 activity0");
                }


                if (hashmap_reasons != null) {
                    HashMap<Integer, HashSet<String>> sort_map = new HashMap<>();
                    int total = 0;
                    for(String key: new_map.keySet()){
                        total += new_map.get(key);
                        int score = new_map.get(key);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            sort_map.computeIfAbsent(score, k -> new HashSet<>()).add(key);
                        }
                    }

                    ArrayList<Integer> sortedKeys = new ArrayList<>(sort_map.keySet());
                    Collections.sort(sortedKeys, Collections.reverseOrder());

                    StringBuilder ranking_list = new StringBuilder("Top 5 Answers: \n");
                    int count = 1;
                    for (Integer num : sortedKeys) {
                        if (count > 5) break;
                        DecimalFormat df = new DecimalFormat("0.00");
                        String percentage_str = df.format(num / (float) total * 100);
                        ranking_list.append(count).append(". ")
                                .append(Objects.requireNonNull(sort_map.get(num)).toString()
                                        .replace('[', ' ')
                                        .replace(']', ' '))
                                .append("- ")
                                .append(percentage_str)
                                .append("%\n");
                        count++;
                    }
                    TextView ranking = findViewById(R.id.textView4);
                    ranking.setText(ranking_list.toString());
                } else {
                    TextView ranking = findViewById(R.id.textView4);
                    ranking.setText("No data available.");
                }
            }
        });
    }

    private void open_Previous_Activity() {
        Intent intent = new Intent(this, Chapter1_Opening_Activity.class);
        intent.putExtra("username", username);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void open_Next_Activity() {
        Intent intent = new Intent(this, Chapter1_Activity1_Activity.class);
        intent.putExtra("username", username);
        intent.putExtra("topChoice", topChoice);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void open_Chapter1() {
        Intent intent = new Intent(this, Chapter1_Activity.class);
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
        DatabaseReference activityRef = db.child("userActivity").child(username).child("Part1_1_Activity0");

        // create a new activityID
        String activityId = activityRef.push().getKey();

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");

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
