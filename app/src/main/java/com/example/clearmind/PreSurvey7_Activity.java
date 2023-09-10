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

public class PreSurvey7_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;
    private Button button_back;
    private Button button_next;

    private String scale_answer30;
    private String scale_answer31;
    private String scale_answer32;
    private String scale_answer33;
    private String scale_answer34;
    private String scale_answer35;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_survey_7);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

        button_back = findViewById(R.id.button_previous);
        button_next = findViewById(R.id.button_next);

        RadioGroup radiogroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
        RadioGroup radiogroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
        RadioGroup radiogroup3 = (RadioGroup) findViewById(R.id.radioGroup3);
        RadioGroup radiogroup4 = (RadioGroup) findViewById(R.id.radioGroup4);
        RadioGroup radiogroup5 = (RadioGroup) findViewById(R.id.radioGroup5);
        RadioGroup radiogroup6 = (RadioGroup) findViewById(R.id.radioGroup6);

        radiogroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                scale_answer30 = selected_button_1.getText().toString();
            }
        });

        radiogroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                scale_answer31 = selected_button_1.getText().toString();
            }
        });

        radiogroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                scale_answer32 = selected_button_1.getText().toString();
            }
        });

        radiogroup4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                scale_answer33 = selected_button_1.getText().toString();
            }
        });

        radiogroup5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                scale_answer34 = selected_button_1.getText().toString();
            }
        });

        radiogroup6.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                scale_answer35 = selected_button_1.getText().toString();
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

                if (scale_answer30==null || scale_answer31==null || scale_answer32==null || scale_answer33==null || scale_answer34==null || scale_answer35==null){
                    Toast.makeText(PreSurvey7_Activity.this,  "Empty input", Toast.LENGTH_SHORT).show();
                } else {
                    db.child("PreSurvey").child(username).child("30").setValue(scale_answer30);
                    db.child("PreSurvey").child(username).child("31").setValue(scale_answer31);
                    db.child("PreSurvey").child(username).child("32").setValue(scale_answer32);
                    db.child("PreSurvey").child(username).child("33").setValue(scale_answer33);
                    db.child("PreSurvey").child(username).child("34").setValue(scale_answer34);
                    db.child("PreSurvey").child(username).child("35").setValue(scale_answer35);

                    toNextPage = true;
                }

                if (toNextPage){
                    open_Next_Activity();
                }
            }
        });

    }

    private void open_Previous_Activity() {
        Intent intent = new Intent(this,PreSurvey6_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Next_Activity() {
        Intent intent = new Intent(this,PreSurvey8_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
