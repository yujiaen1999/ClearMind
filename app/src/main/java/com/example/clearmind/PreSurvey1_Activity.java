package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class PreSurvey1_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;

    private Button button_back;
    private Button button_next;
    private String txt_answer2;
    private String txt_answer4;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_survey_1);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

        button_back = findViewById(R.id.button_previous);
        button_next = findViewById(R.id.button_next);

        RadioGroup radiogroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
        RadioGroup radiogroup2 = (RadioGroup) findViewById(R.id.radioGroup2);

        radiogroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                txt_answer2 = selected_button_1.getText().toString();
            }
        });

        radiogroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                txt_answer4 = selected_button_1.getText().toString();
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

                EditText answer1 = findViewById(R.id.answer1);
//                EditText answer2 = findViewById(R.id.answer2);
                EditText answer3 = findViewById(R.id.answer3);

                String txt_answer1 = answer1.getText().toString();
//                String txt_answer2 = answer2.getText().toString();
                String txt_answer3 = answer3.getText().toString();

//                Log.d("PreSurvey", txt_answer4);

                if (txt_answer1.isEmpty() || txt_answer2==null || txt_answer3.isEmpty() || txt_answer4==null){
                    Toast.makeText(PreSurvey1_Activity.this,  "Empty input", Toast.LENGTH_SHORT).show();
                } else {
                    db.child("PreSurvey").child(username).child("1").setValue(txt_answer1);
                    db.child("PreSurvey").child(username).child("2").setValue(txt_answer2);
                    db.child("PreSurvey").child(username).child("3").setValue(txt_answer3);
                    db.child("PreSurvey").child(username).child("4").setValue(txt_answer4);
//                    Toast.makeText(PreSurvey1_Activity.this,  "Set Security Questions successful", Toast.LENGTH_SHORT).show();
                    toNextPage = true;
                }

                if (toNextPage){
                    open_Next_Activity();
                }
//                open_Next_Activity();
            }
        });

    }

    private void open_Previous_Activity() {
        Intent intent = new Intent(this,LearnActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Next_Activity() {
        Intent intent = new Intent(this,PreSurvey2_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

//    private String getRadioGroupAnswer(RadioGroup radiogroup) {
//        final String[] result = new String[1];
//        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
//                result[0] = selected_button_1.getText().toString();
//            }
//        });
//        return result[0];
//    }

}
