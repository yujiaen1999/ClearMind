package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class Chapter1_Activity1_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;

    private Button button_back;
    private Button button_next;
    private Button button_home;
    private Button button_submit;
    private CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6,
                     checkBox7, checkBox8, checkBox9, checkBox10, checkBox11, checkBox12;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter1_activity1);
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
        checkBox9 = findViewById(R.id.checkbox9);
        checkBox10 = findViewById(R.id.checkbox10);
        checkBox11 = findViewById(R.id.checkbox11);
        checkBox12 = findViewById(R.id.checkbox12);

//        Set<String> user_choice = new HashSet<>();

//        Toast.makeText(Chapter1_Activity1_Activity.this, checkBox1.getText().toString(), Toast.LENGTH_SHORT).show();

//        if (checkBox1.isChecked()) {
//            user_choice.add(checkBox1.getText().toString());
//        }

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
                    user_choice.add(checkBox8.getText().toString());
                }
                if (checkBox9.isChecked()) {
                    user_choice.add(checkBox9.getText().toString());
                }
                if (checkBox10.isChecked()) {
                    user_choice.add(checkBox10.getText().toString());
                }
                if (checkBox11.isChecked()) {
                    user_choice.add(checkBox11.getText().toString());
                }

//                EditText user_input2 = findViewById(R.id.input2);
//                if (checkBox12.isChecked() && user_input2.getText() != null){
//                    user_choice.add(user_input2.getText().toString());
//                } else{
//                    user_choice.add(checkBox12.getText().toString());
//                }

                if (checkBox12.isChecked()) {
                    EditText user_input2 = findViewById(R.id.input2);
                    Toast.makeText(Chapter1_Activity1_Activity.this, user_input2.getText().toString(), Toast.LENGTH_SHORT).show();
                    if (user_input2.getText().toString().equals("")){
//                        user_choice.add(user_input2.getText().toString());
                        Log.d("firebase_check", checkBox12.getText().toString());
                        Toast.makeText(Chapter1_Activity1_Activity.this, checkBox12.getText().toString(), Toast.LENGTH_SHORT).show();
                        user_choice.add(checkBox12.getText().toString());
                    } else{
                        user_choice.add(user_input2.getText().toString());
                    }
                    Log.d("firebase_set", String.valueOf(user_choice));
//                    user_choice.add(checkBox12.getText().toString());
                }

                // update hashmap of Firebase database: Chapter1-activity1
                updateHashmap(user_choice);

                // Store each user’s selection in database for potential need in future
                db.child("Chapter1").child("activity1_user_input").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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
                                db.child("Chapter1").child("activity1_user_input").child(username).setValue(user_choice_arr);
                            } else{
                                Log.d("firebase_test", "Not NULL");
                                for (String item: user_choice){
                                    if (user_choice_in_database.contains(item)){
                                        continue;
                                    }
                                    user_choice_in_database.add(item);
                                }
                                db.child("Chapter1").child("activity1_user_input").child(username).setValue(user_choice_in_database);
                            }
                        }
                    }
                });

//                HashMap<String, Object> map = new HashMap<>();
//                map.put("Lack of Motivation", 0);
//                map.put("Fear of Failure", 0);
//                map.put("Perfectionism", 0);
//                map.put("Task Difficulty", 0);
//                map.put("Poor Time Management", 0);
//                map.put("Lack of Clear Goals", 0);
//                map.put("Distractions", 0);
//                map.put("Low Self-Discipline", 0);
//                map.put("Overwhelm", 0);
//                map.put("Delayed Gratification", 0);
//                map.put("Decision Paralysis", 0);
//                map.put("Other", 0);
//
////                   register
//                db.child("Chapter1").child("activity1").setValue(map);
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
                open_Next_Activity();
            }
        });
    }

    private void updateHashmap(Set<String> user_choice) {
//        ==========================
//        Read database once
//        ==========================
        db.child("Chapter1").child("activity1").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                // might have error if task is not success
                HashMap<String, Object> hashmap_reasons = (HashMap<String, Object>) task.getResult().getValue();

                if(!task.isSuccessful()){
                    Log.e("firebase", "Error getting data", task.getException());
                }else{
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
//                    HashMap<String, Object> hashmap_reasons = (HashMap<String, Object>) task.getResult().getValue();
                    HashMap<String, Object> new_map = new HashMap<>();
                    for(String value: user_choice){
                        if(hashmap_reasons.containsKey(value)){
                            Object obj = hashmap_reasons.get(value);
//                            Log.d("firebase_hashmap", String.valueOf(hashmap_reasons.get(value).getClass()));
                            new_map.put(value, Integer.parseInt(obj.toString())+1);
                        } else{
                            new_map.put(value, 1);
                        }
                    }
                    // update hashmap in database
                    db.child("Chapter1").child("activity1").updateChildren(new_map);
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

                String ranking_list = "Top 5 Answers: \n";


                int count = 1;
                for(Integer nums: sortedKeys){
                    if (count > 5){
                        break;
                    }

                    DecimalFormat df = new DecimalFormat("0.00");
                    String percentage_str = df.format(nums / (float)total * 100);

                    ranking_list = ranking_list + count + ". " + String.valueOf(sort_map.get(nums)).replace('[',' ').replace(']',' ')  + "- "+  percentage_str + "%\n";
                    sort_map.remove(nums);
                    count += 1;
                }

                TextView ranking = findViewById(R.id.textView4);
                ranking.setText(ranking_list);
            }
        });
    }

    private void open_Previous_Activity() {
        Intent intent = new Intent(this,Chapter1_Opening_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Next_Activity() {
        Intent intent = new Intent(this,Chapter1_Activity2_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter1() {
        Intent intent = new Intent(this,Chapter1_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
