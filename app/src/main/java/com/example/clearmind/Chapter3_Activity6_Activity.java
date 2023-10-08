package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;

public class Chapter3_Activity6_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;

    private Button button_back;
    private Button button_next;
    private Button button_home;

    private TextView button_tendency;

    private EditText answer1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter3_activity6);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

        button_home = findViewById(R.id.button_home);
        button_back = findViewById(R.id.button_previous);
        button_next = findViewById(R.id.button_next);

        button_tendency = findViewById(R.id.tendency);

        answer1 = findViewById(R.id.input1);

        // Retrieve and Display user input from the database
        db.child("Chapter3").child("activity6").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                String answer_db = (String) task.getResult().getValue();
                if(!task.isSuccessful()){
                    Log.e("firebase_summary", "Error getting data", task.getException());
                }else{
                    Log.d("firebase_summary", String.valueOf(task.getResult().getValue()));
                    if(answer_db != null){
                        answer1.setText(answer_db);
                    }
                }
            }
        });


        // enable the tendency button function
        final Integer[] flag = {0};

        button_tendency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
//                openHints(v);

                if (flag[0] == 0){
                    String txt_hints = "<p style=\"text-align: center\"><b>Procrastination Tendencies</b></p>\n" +
                            "<b>Arousal procrastination:</b> It is the tendency to purposefully put off completing specific tasks until the last moment. It often arises from a desire for heightened urgency, which comes with approaching deadlines. Individuals who engage themselves in arousal procrastination believe that the pressure of a pending deadline assists them in completing tasks under tight time constraints." +
                            "<br><br>" +
                            "<b>Avoidant procrastination:</b> It is the tendency to postpone or avoid tasks out of fear of failure, task aversiveness, or even success. Fear of failure manifests as a common belief among avoidant procrastinators that they lack essential skills or abilities to accomplish them, leading to task delays. Task aversiveness is the notion that avoidant procrastinators genuinely dislike the task, leading them to postpone it. Lastly, fear of success is the notion that avoidant procrastinators fear success because they anticipate additional responsibilities and expectations that come with achieving it." +
                            "<br><br>" +
                            "<b>Decisional procrastination:</b> It is the tendency to put off making decisions in a timely manner. Decisional procrastinators often experience hesitation when faced with making decisions about a task or behavior. This procrastination tendency can arise from the perceived complexity of the task, potential conflicts with other individuals that a decision may evoke, and the tendency to protect one’s self-esteem or self-confidence.";
                    button_tendency.setText(Html.fromHtml(txt_hints));
                    flag[0] = 1;
                } else{
                    button_tendency.setText("Procrastination Tendencies");
                    flag[0] = 0;
                }
            }
        });

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
                String txt_answer1 = answer1.getText().toString();

                if (txt_answer1.isEmpty()){
                    Toast.makeText(Chapter3_Activity6_Activity.this,  "Empty input", Toast.LENGTH_SHORT).show();
                } else {
                    // Get all answers from user

                    db.child("Chapter3").child("activity6").child(username).setValue(txt_answer1);

                    // update Chapter progress
                    Map<String, Object> chapter_progress_update = new HashMap<>();
                    chapter_progress_update.put("8_Activity3_6", "1");
                    db.child("Chapter3").child("progress").child(username).updateChildren(chapter_progress_update);

                    open_Next_Activity();
                }
            }
        });

    }

    private void open_Previous_Activity() {
        Intent intent = new Intent(this,Chapter3_Activity5_sum_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Next_Activity() {
        Intent intent = new Intent(this,Chapter3_Summary_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter3() {
        Intent intent = new Intent(this,Chapter3_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

}
