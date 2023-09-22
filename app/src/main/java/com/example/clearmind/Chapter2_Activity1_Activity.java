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
import java.util.ArrayList;
import java.util.Collections;
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
    private CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6,
            checkBox7, checkBox8;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter2_activity1);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

        button_home = findViewById(R.id.button_home);
        button_back = findViewById(R.id.button_previous);
        button_next = findViewById(R.id.button_next);

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

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                open_Previous_Activity();
            }
        });

        button_next.setOnClickListener(new View.OnClickListener() {
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
                // TODO: Do we need to give any feedback after user chose something?
                updateHashmap(user_choice);


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

}
