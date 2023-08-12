package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Chapter1_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;

    private ImageButton button_back_to_learn;
    private Button button_opening;
    private Button button_activity1;
    private Button button_activity2;
    private Button button_act;
    private Button button_time;
    private Button button_summary;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter1);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

        button_back_to_learn = findViewById(R.id.button_back_learn_page);

        button_opening = findViewById(R.id.button_opening);
        button_activity1 = findViewById(R.id.button2);
        button_activity2 = findViewById(R.id.button3);
        button_act = findViewById(R.id.button4);
        button_time = findViewById(R.id.button5);
        button_summary = findViewById(R.id.button6);

        button_back_to_learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openLearnActivity();
            }
        });

        button_opening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                open_Chapter1_Opening();
            }
        });

        button_activity1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                open_Chapter1_Activity1();
            }
        });

        button_activity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                open_Chapter1_Activity2();
            }
        });

        button_act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                open_Chapter1_Intro_ACT();
            }
        });

        button_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                open_Chapter1_Time();
            }
        });

        button_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                open_Chapter1_Summary();
            }
        });

    }

    private void openLearnActivity() {
        Intent intent = new Intent(this,LearnActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }


    private void open_Chapter1_Summary() {
        Intent intent = new Intent(this,Chapter1_Summary_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter1_Time() {
        Intent intent = new Intent(this,Chapter1_Time_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter1_Intro_ACT() {
        Intent intent = new Intent(this,Chapter1_Intro_ACT_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter1_Activity2() {
        Intent intent = new Intent(this,Chapter1_Activity2_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter1_Activity1() {
        Intent intent = new Intent(this,Chapter1_Activity1_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter1_Opening() {
        Intent intent = new Intent(this,Chapter1_Opening_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
