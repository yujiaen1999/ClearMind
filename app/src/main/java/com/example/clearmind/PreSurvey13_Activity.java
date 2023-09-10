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

public class PreSurvey13_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;
    private Button button_back;
    private Button button_next;

    private String scale_answer61;
    private String scale_answer62;
    private String scale_answer63;
    private String scale_answer64;
    private String scale_answer65;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_survey_13);
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

        radiogroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                scale_answer61 = selected_button_1.getText().toString();
            }
        });

        radiogroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                scale_answer62 = selected_button_1.getText().toString();
            }
        });

        radiogroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                scale_answer63 = selected_button_1.getText().toString();
            }
        });

        radiogroup4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                scale_answer64 = selected_button_1.getText().toString();
            }
        });

        radiogroup5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                scale_answer65 = selected_button_1.getText().toString();
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

                if (scale_answer61==null || scale_answer62==null || scale_answer63==null || scale_answer64==null || scale_answer65==null){
                    Toast.makeText(PreSurvey13_Activity.this,  "Empty input", Toast.LENGTH_SHORT).show();
                } else {
                    db.child("PreSurvey").child(username).child("61").setValue(scale_answer61);
                    db.child("PreSurvey").child(username).child("62").setValue(scale_answer62);
                    db.child("PreSurvey").child(username).child("63").setValue(scale_answer63);
                    db.child("PreSurvey").child(username).child("64").setValue(scale_answer64);
                    db.child("PreSurvey").child(username).child("65").setValue(scale_answer65);

                    toNextPage = true;
                }

                if (toNextPage){
                    open_Next_Activity();
                }
            }
        });

    }

    private void open_Previous_Activity() {
        Intent intent = new Intent(this,PreSurvey12_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Next_Activity() {
        Intent intent = new Intent(this,PreSurvey14_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
