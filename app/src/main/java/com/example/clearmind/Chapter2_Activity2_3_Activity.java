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

public class Chapter2_Activity2_3_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;

    private Button button_back;
    private Button button_next;
    private Button button_home;

//    private EditText answer1;
//    private EditText answer2;
//    private EditText answer3;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter2_activity2_3);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

        button_home = findViewById(R.id.button_home);
        button_back = findViewById(R.id.button_previous);
        button_next = findViewById(R.id.button_next);

//        answer1 = findViewById(R.id.input1);
//        answer2 = findViewById(R.id.input2);
//        answer3 = findViewById(R.id.input3);

        // Retrieve and Display user input from the database
//        db.child("Chapter2").child("activity2").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                HashMap<String, String> hashmap_chapter2 = (HashMap<String, String>) task.getResult().getValue();
//                if(!task.isSuccessful()){
//                    Log.e("firebase_summary", "Error getting data", task.getException());
//                }else{
//                    Log.d("firebase_summary", String.valueOf(task.getResult().getValue()));
//                    if(hashmap_chapter2 != null){
//                        answer1.setText(hashmap_chapter2.get("identify_goal"));
//                        answer2.setText(hashmap_chapter2.get("identify_passengers"));
//                        answer3.setText(hashmap_chapter2.get("identify_persuasion"));
//                    }
//                }
//            }
//        });


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
                // update Chapter progress
                Map<String, Object> chapter_progress_update = new HashMap<>();
                chapter_progress_update.put("5_Identify_your_passengers", "1");
                db.child("Chapter2").child("progress").child(username).updateChildren(chapter_progress_update);

                open_Next_Activity();

                // Save answers to database
//                String txt_answer1 = answer1.getText().toString();
//                String txt_answer2 = answer2.getText().toString();
//                String txt_answer3 = answer3.getText().toString();
//
//                if (txt_answer1.isEmpty() || txt_answer2.isEmpty() || txt_answer3.isEmpty()){
//                    Toast.makeText(Chapter2_Activity2_3_Activity.this,  "Empty input", Toast.LENGTH_SHORT).show();
//                } else {
//                    // Get all answers from user
////                    HashMap<String, Object> map = new HashMap<>();
////                    map.put("identify_goal", txt_answer1);
////                    map.put("identify_passengers", txt_answer2);
////                    map.put("identify_persuasion", txt_answer3);
////
////                    db.child("Chapter2").child("activity2").child(username).setValue(map);
//
//                    db.child("Chapter2").child("activity2").child(username).child("identify_goal").setValue(txt_answer1);
//                    db.child("Chapter2").child("activity2").child(username).child("identify_passengers").setValue(txt_answer2);
//                    db.child("Chapter2").child("activity2").child(username).child("identify_persuasion").setValue(txt_answer3);
//
//                    // update Chapter progress
//                    Map<String, Object> chapter_progress_update = new HashMap<>();
//                    chapter_progress_update.put("5_Identify_your_passengers", "1");
//                    db.child("Chapter2").child("progress").child(username).updateChildren(chapter_progress_update);
//
//                    open_Next_Activity();
//                }
            }
        });

    }

    private void open_Previous_Activity() {
        Intent intent = new Intent(this,Chapter2_Activity2_2Q_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Next_Activity() {
        Intent intent = new Intent(this,Chapter2_Diagram_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter1() {
        Intent intent = new Intent(this,Chapter2_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
