// ====================
// Not in Use
// ====================

package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChapterOneActivity extends AppCompatActivity {

    private String username;
    private DatabaseReference db;

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

        button_opening = findViewById(R.id.button_opening);

        button_opening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                open_Chapter1_Opening();
            }
        });

    }

    private void open_Chapter1_Opening() {
        Intent intent = new Intent(this,Chapter1_Opening_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
