package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

// *********************
// Not actively in use!
// *********************
public class Chapter1_Intro_ACT_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;

    private Button button_back;
    private Button button_next;
    private Button button_home;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter1_intro_act);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

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
                // update Chapter1 progress
                Map<String, Object> chapter1_progress_update = new HashMap<>();
                chapter1_progress_update.put("5_Summary_and_ACT", "1");
                db.child("Chapter1").child("progress").child(username).updateChildren(chapter1_progress_update);

                open_Next_Activity();
            }
        });
    }

    private void open_Previous_Activity() {
        Intent intent = new Intent(this,Chapter1_Activity2_Question_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Next_Activity() {
        Intent intent = new Intent(this,Chapter1_Time_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter1() {
        Intent intent = new Intent(this,Chapter1_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
