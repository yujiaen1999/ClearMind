package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class PreSurvey14_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;
    private Button button_back;
    private Button button_next;

    private String scale_answer66;
    private String scale_answer67;
    private String scale_answer68;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_survey_14);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

        button_back = findViewById(R.id.button_previous);
        button_next = findViewById(R.id.button_next);

        RadioGroup radiogroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
        RadioGroup radiogroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
        RadioGroup radiogroup3 = (RadioGroup) findViewById(R.id.radioGroup3);

        radiogroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                scale_answer66 = selected_button_1.getText().toString();
            }
        });

        radiogroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                scale_answer67 = selected_button_1.getText().toString();
            }
        });

        radiogroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                scale_answer68 = selected_button_1.getText().toString();
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
                boolean toNextPage = false;

                if (scale_answer66==null || scale_answer67==null || scale_answer68==null){
                    Toast.makeText(PreSurvey14_Activity.this,  "Empty input", Toast.LENGTH_SHORT).show();
                } else {
                    db.child("PreSurvey").child(username).child("66").setValue(scale_answer66);
                    db.child("PreSurvey").child(username).child("67").setValue(scale_answer67);
                    db.child("PreSurvey").child(username).child("68").setValue(scale_answer68);

                    toNextPage = true;
                }

                if (toNextPage){
                    Map<String, Object> update = new HashMap<>();
                    update.put("presurvey", "2");
                    db.child("progress").child(username).updateChildren(update);

                    Toast.makeText(PreSurvey14_Activity.this,  "Pre-Survey Submitted", Toast.LENGTH_SHORT).show();

                    open_Next_Activity();
                }
            }
        });

    }

    private void open_Previous_Activity() {
        Intent intent = new Intent(this,PreSurvey13_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Next_Activity() {
        Intent intent = new Intent(this,LearnActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
