package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

// ====================================
// Template scale questions for survey
// NOT in use currently
// ====================================
public class RegisterActivity2 extends AppCompatActivity {
    private String username;
    private DatabaseReference db;

    private String scale_answer1;
    private String scale_answer2;
    private String scale_answer3;
    private String scale_answer4;
    private String scale_answer5;

    private Button submit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_register_2);

        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

        RadioGroup radiogroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
        RadioGroup radiogroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
        RadioGroup radiogroup3 = (RadioGroup) findViewById(R.id.radioGroup3);
        RadioGroup radiogroup4 = (RadioGroup) findViewById(R.id.radioGroup4);
        RadioGroup radiogroup5 = (RadioGroup) findViewById(R.id.radioGroup5);

        submit = findViewById(R.id.submit_button);

        radiogroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                scale_answer1 = selected_button_1.getText().toString();
//                Toast.makeText(getApplicationContext(), "your choice is " + selected_button.getText(), Toast.LENGTH_LONG).show();
            }
        });

        radiogroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_2 = (RadioButton) findViewById(checkedId);
                scale_answer2 = selected_button_2.getText().toString();
            }
        });

        radiogroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_3 = (RadioButton) findViewById(checkedId);
                scale_answer3 = selected_button_3.getText().toString();
            }
        });

        radiogroup4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_4 = (RadioButton) findViewById(checkedId);
                scale_answer4 = selected_button_4.getText().toString();
            }
        });

        radiogroup5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_5 = (RadioButton) findViewById(checkedId);
                scale_answer5 = selected_button_5.getText().toString();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean toMainPage = false;
//                String txt_username = username.getText().toString();
//                String txt_name = name.getText().toString();
//                String txt_email = email.getText().toString();
//                String txt_answer4 = answer4.getText().toString();
//                String txt_answer5 = answer5.getText().toString();


                if (scale_answer1.isEmpty() || scale_answer2.isEmpty() || scale_answer3.isEmpty() || scale_answer4.isEmpty() || scale_answer5.isEmpty()){
                    Toast.makeText(RegisterActivity2.this,  "Empty input", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("scale_question1", scale_answer1);
                    map.put("scale_question2", scale_answer2);
                    map.put("scale_question3", scale_answer3);
                    map.put("scale_question4", scale_answer4);
                    map.put("scale_question5", scale_answer5);

                    register(username, map);
                    Toast.makeText(RegisterActivity2.this,  "Register successful", Toast.LENGTH_SHORT).show();
                    toMainPage = true;
                }

                if (toMainPage){
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                    intent.putExtra("username", txt_username);
                    startActivity(intent);  //go to main page (activity)
                }
            }
        });

    }

    private void register(String username, HashMap<String, Object> map) {
        this.db.child("register").child(username).setValue(map);
    }
}
