package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Chapter2_Diagram_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;

    private Button button_back;
    private Button button_next;
    private Button button_home;

    private EditText answer1;
    private EditText answer2;
    private EditText answer3;
    private EditText answer4;
    private EditText answer5;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter2_activity2_4_diagram);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

        button_home = findViewById(R.id.button_home);
        button_back = findViewById(R.id.button_previous);
        button_next = findViewById(R.id.button_next);

        answer1 = findViewById(R.id.input1);
        answer2 = findViewById(R.id.input2);
        answer3 = findViewById(R.id.input3);
        answer4 = findViewById(R.id.input4);
        answer5 = findViewById(R.id.input5);


        // Retrieve and Display user input from the database
        db.child("Chapter2").child("activity2").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                HashMap<String, String> hashmap_chapter2 = (HashMap<String, String>) task.getResult().getValue();
                if(!task.isSuccessful()){
                    Log.e("firebase_summary", "Error getting data", task.getException());
                }else{
                    Log.d("firebase_summary", String.valueOf(task.getResult().getValue()));
                    if(hashmap_chapter2 != null){
                        answer1.setText(hashmap_chapter2.get("diagram_destination"));
                        answer2.setText(hashmap_chapter2.get("diagram_passenger_A"));
                        answer3.setText(hashmap_chapter2.get("diagram_passenger_B"));
                        answer4.setText(hashmap_chapter2.get("diagram_passenger_C"));
                        answer5.setText(hashmap_chapter2.get("diagram_persuasion"));
                    }
                }
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
                String txt_answer1 = answer1.getText().toString();
                String txt_answer2 = answer2.getText().toString();
                String txt_answer3 = answer3.getText().toString();
                String txt_answer4 = answer4.getText().toString();
                String txt_answer5 = answer5.getText().toString();

                if (txt_answer1.isEmpty() || txt_answer2.isEmpty() || txt_answer3.isEmpty() || txt_answer4.isEmpty() || txt_answer5.isEmpty()){
                    Toast.makeText(Chapter2_Diagram_Activity.this,  "Empty input", Toast.LENGTH_SHORT).show();
                } else {
                    // Get all answers from user
//                    HashMap<String, Object> map = new HashMap<>();
//                    map.put("diagram_destination", txt_answer1);
//                    map.put("diagram_passenger_A", txt_answer2);
//                    map.put("diagram_passenger_B", txt_answer3);
//                    map.put("diagram_passenger_C", txt_answer4);
//
//                    db.child("Chapter2").child("activity2").child(username).setValue(map);

                    db.child("Chapter2").child("activity2").child(username).child("diagram_destination").setValue(txt_answer1);
                    db.child("Chapter2").child("activity2").child(username).child("diagram_passenger_A").setValue(txt_answer2);
                    db.child("Chapter2").child("activity2").child(username).child("diagram_passenger_B").setValue(txt_answer3);
                    db.child("Chapter2").child("activity2").child(username).child("diagram_passenger_C").setValue(txt_answer4);
                    db.child("Chapter2").child("activity2").child(username).child("diagram_persuasion").setValue(txt_answer5);


                    // update Chapter progress
                    Map<String, Object> chapter_progress_update = new HashMap<>();
                    chapter_progress_update.put("6_Diagram", "1");
                    db.child("Chapter2").child("progress").child(username).updateChildren(chapter_progress_update);

                    open_Next_Activity();
                }
            }
        });
    }

    private void open_Previous_Activity() {
        Intent intent = new Intent(this,Chapter2_Activity2_3_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Next_Activity() {
        Intent intent = new Intent(this,Chapter2_Activity2_5_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter1() {
        Intent intent = new Intent(this,Chapter2_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
