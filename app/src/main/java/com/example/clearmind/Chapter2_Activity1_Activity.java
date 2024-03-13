package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Chapter2_Activity1_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;

    private Button button_back;
    private Button button_next;
    private Button button_home;
    private Button button_submit;
    private CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6,
            checkBox7, checkBox8;

    private long pageOpenTime;
    private long pageCloseTime;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter2_activity1);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

        button_home = findViewById(R.id.button_home);
        button_back = findViewById(R.id.button_previous);
        button_next = findViewById(R.id.button_next);
        button_submit = findViewById(R.id.button_submit);

        checkBox1 = findViewById(R.id.checkbox1);
        checkBox2 = findViewById(R.id.checkbox2);
        checkBox3 = findViewById(R.id.checkbox3);
        checkBox4 = findViewById(R.id.checkbox4);
        checkBox5 = findViewById(R.id.checkbox5);
        checkBox6 = findViewById(R.id.checkbox6);
        checkBox7 = findViewById(R.id.checkbox7);
        checkBox8 = findViewById(R.id.checkbox8);

        // handle the status of these check Box
        // when click next, store them to our database?

        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                open_Chapter2();
            }
        });

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                Set<String> user_choice = new HashSet<>();

//                Toast.makeText(Chapter1_Activity1_Activity.this, String.valueOf(user_choice), Toast.LENGTH_SHORT).show();

                if (checkBox1.isChecked()) {
                    user_choice.add(checkBox1.getText().toString());
                }
                if (checkBox2.isChecked()) {
                    user_choice.add(checkBox2.getText().toString());
                }
                if (checkBox3.isChecked()) {
                    user_choice.add(checkBox3.getText().toString());
                }
                if (checkBox4.isChecked()) {
                    user_choice.add(checkBox4.getText().toString());
                }
                if (checkBox4.isChecked()) {
                    user_choice.add(checkBox4.getText().toString());
                }
                if (checkBox5.isChecked()) {
                    user_choice.add(checkBox5.getText().toString());
                }
                if (checkBox6.isChecked()) {
                    user_choice.add(checkBox6.getText().toString());
                }
                if (checkBox7.isChecked()) {
                    user_choice.add(checkBox7.getText().toString());
                }
                if (checkBox8.isChecked()) {
                    EditText user_input2 = findViewById(R.id.input2);
                    if (user_input2.getText().toString().equals("")){
                        Log.d("firebase_check", checkBox8.getText().toString());
                        user_choice.add(checkBox8.getText().toString());
                    } else{
                        user_choice.add(user_input2.getText().toString());
                    }
                    Log.d("firebase_set", String.valueOf(user_choice));
                }

                // update hashmap of Firebase database: Chapter2-activity1
                // Done: Do we need to give any feedback after user chose something?
                // Solution: feedback top 3 common thought
                updateHashmap(user_choice);

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
                chapter1_progress_update.put("2_Activity2_1", "1");
                db.child("Chapter2").child("progress").child(username).updateChildren(chapter1_progress_update);

                open_Next_Activity();
            }
        });

    }


    private void updateHashmap(Set<String> user_choice) {
//        ==========================
//        Read database once
//        ==========================
        db.child("Chapter2").child("activity1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                // might have error if task is not success
                HashMap<String, Object> hashmap_reasons = (HashMap<String, Object>) task.getResult().getValue();

                if(!task.isSuccessful()){
                    Log.e("firebase", "Error getting data", task.getException());
                }else{
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    HashMap<String, Object> new_map = new HashMap<>();
                    for(String value: user_choice){
                        if(hashmap_reasons != null && hashmap_reasons.containsKey(value)){
                            Object obj = hashmap_reasons.get(value);
                            new_map.put(value, Integer.parseInt(obj.toString())+1);
                        } else{
                            new_map.put(value, 1);
                        }
                    }
                    // update hashmap in database
                    db.child("Chapter2").child("activity1").updateChildren(new_map);
                }

                HashMap<Integer, HashSet<String>> sort_map = new HashMap<>();
                Integer total = 0;
                for(String key: hashmap_reasons.keySet()){
                    total = total + Integer.parseInt(hashmap_reasons.get(key).toString());
                    if (sort_map.containsKey(Integer.parseInt(hashmap_reasons.get(key).toString()))){
                        sort_map.get(Integer.parseInt(hashmap_reasons.get(key).toString())).add(key);
                    } else{
                        HashSet<String> new_set = new HashSet<>();
                        new_set.add(key);
                        sort_map.put(Integer.parseInt(hashmap_reasons.get(key).toString()), new_set);
                    }
                }

                ArrayList<Integer> sortedKeys = new ArrayList<Integer>(sort_map.keySet());
                Collections.sort(sortedKeys);
                Collections.reverse(sortedKeys);

                String ranking_list = "Top 3 Common Thoughts: \n";


                int count = 1;
                for(Integer nums: sortedKeys){
                    if (count > 3){
                        break;
                    }

                    DecimalFormat df = new DecimalFormat("0.00");
                    String percentage_str = df.format(nums / (float)total * 100);

                    ranking_list = ranking_list + count + ". " + String.valueOf(sort_map.get(nums)).replace('[',' ').replace(']',' ')  + "- "+  percentage_str + "%\n";
                    sort_map.remove(nums);
                    count += 1;
                }

                TextView ask_for_submit = findViewById(R.id.textView3);
                ask_for_submit.setText("Thank you for your responses. Here are the top 3 common thoughts provided by other users:");

                TextView ranking = findViewById(R.id.textView4);
                ranking.setText(ranking_list);

            }
        });

        // Store each user’s selection in database (Chapter2 > activity_user_input) for potential need in future
        db.child("Chapter2").child("activity1_user_input").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    Log.e("firebase", "Error getting data", task.getException());
                } else{
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
//                            List<String> user_choice_in_database = task.getResult().getValue(List.class);
                    List<String> user_choice_in_database = (List) task.getResult().getValue();

                    if(user_choice_in_database == null){
                        Log.d("firebase_test", "here");
                        List<String> user_choice_arr = new ArrayList<>();
                        user_choice_arr.addAll(user_choice);
                        db.child("Chapter2").child("activity1_user_input").child(username).setValue(user_choice_arr);
                    } else{
                        Log.d("firebase_test", "Not NULL");
                        for (String item: user_choice){
                            if (user_choice_in_database.contains(item)){
                                continue;
                            }
                            user_choice_in_database.add(item);
                        }
                        db.child("Chapter2").child("activity1_user_input").child(username).setValue(user_choice_in_database);
                    }
                }
            }
        });
    }


    private void open_Previous_Activity() {
        Intent intent = new Intent(this,Chapter2_Opening_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Next_Activity() {
        Intent intent = new Intent(this,Chapter2_Activity2_1_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter2() {
        Intent intent = new Intent(this,Chapter2_Activity.class);
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
        DatabaseReference activityRef = db.child("userActivity").child(username).child("Part2_2_Activity1");

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
