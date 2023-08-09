package com.example.clearmind;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Chapter1_Activity2_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;

    private Button button_back;
    private Button button_next;
    private Button button_home;

    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String answer5;

    private TextView question1_response;
    private TextView question2_response;
    private TextView question3_response;
    private TextView question4_response;
    private TextView question5_response;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter1_activity2);
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
                open_Next_Activity();
            }
        });

        question1_response = findViewById(R.id.question1_response);
        question2_response = findViewById(R.id.question2_response);
        question3_response = findViewById(R.id.question3_response);
        question4_response = findViewById(R.id.question4_response);
        question5_response = findViewById(R.id.question5_response);


        RadioGroup radiogroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
        RadioGroup radiogroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
        RadioGroup radiogroup3 = (RadioGroup) findViewById(R.id.radioGroup3);
        RadioGroup radiogroup4 = (RadioGroup) findViewById(R.id.radioGroup4);
        RadioGroup radiogroup5 = (RadioGroup) findViewById(R.id.radioGroup5);


        radiogroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                answer1 = selected_button_1.getText().toString();

//                Toast.makeText(getApplicationContext(), "your choice is " + answer1, Toast.LENGTH_LONG).show();
                if(answer1.equals("A. Arousal")){
                    question1_response.setText("Good job! That is the correct one. Arousal procrastination often occurs because some individuals believe that the pressure that comes with imminent deadlines assists them in completing tasks productively. In Lynn’s case, she believes that the pressure of the impending deadline helps her to focus.");
                    question1_response.setTextColor(Color.parseColor("#008000"));
                } else if (answer1.equals("B. Avoidant")) {
                    question1_response.setText("Not quite right! Avoidant procrastination often happens because some individuals want to avoid negative emotions or situations associated with the tasks. In Lynn’s case, she doesn’t seem to avoid her homework because of fear or negative feelings.");
                    question1_response.setTextColor(Color.parseColor("#FF0000"));
                } else if (answer1.equals("C. Decisional")) {
                    question1_response.setText("Not quite right! Decisional procrastinators often experience hesitation when making decisions about a task or behavior. In Lynn’s case, she doesn’t experience any apparent indecision.");
                    question1_response.setTextColor(Color.parseColor("#FF0000"));
                }
            }
        });

        radiogroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_2 = (RadioButton) findViewById(checkedId);
                answer2 = selected_button_2.getText().toString();

//                Toast.makeText(getApplicationContext(), "your choice is " + answer1, Toast.LENGTH_LONG).show();
                if(answer2.equals("A. Arousal")){
                    question2_response.setText("Not quite right! Arousal procrastination often occurs because some individuals believe that the pressure that comes with imminent deadlines assists them in completing tasks productively. This doesn't seem to be the case, because Tommy is not seeking the pressure and productivity associated with the imminent deadline.\n");
                    question2_response.setTextColor(Color.parseColor("#FF0000"));
                } else if (answer2.equals("B. Avoidant")) {
                    question2_response.setText("Good job! That is the correct one. Avoidant procrastination often happens because some individuals want to avoid negative emotions or situations associated with the tasks. In Tommy’s case, he procrastinated on his English essay because he perceived the class as boring.");
                    question2_response.setTextColor(Color.parseColor("#008000"));
                } else if (answer2.equals("C. Decisional")) {
                    question2_response.setText("Not quite right! Decisional procrastinators often experience hesitation when making decisions about a task or behavior. In Tommy’s case, he doesn’t experience any apparent indecision.");
                    question2_response.setTextColor(Color.parseColor("#FF0000"));
                }
            }
        });

        radiogroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_3 = (RadioButton) findViewById(checkedId);
                answer3 = selected_button_3.getText().toString();

//                Toast.makeText(getApplicationContext(), "your choice is " + answer1, Toast.LENGTH_LONG).show();
                if(answer3.equals("A. Arousal")){
                    question3_response.setText("Not quite right! Arousal procrastination often occurs because some individuals believe that the pressure that comes with imminent deadlines assists them in completing tasks productively. This doesn't seem to be the case, because Anna is not seeking the pressure and productivity associated with the imminent deadline.");
                    question3_response.setTextColor(Color.parseColor("#FF0000"));
                } else if (answer3.equals("B. Avoidant")) {
                    question3_response.setText("Good job! That is the correct one. Avoidant procrastination often happens because some individuals want to avoid negative emotions or situations associated with the tasks. In Anna’s case, she procrastinated on seeking medical advice because of her anxiety about potential diagnoses.");
                    question3_response.setTextColor(Color.parseColor("#008000"));
                } else if (answer3.equals("C. Decisional")) {
                    question3_response.setText("Not quite right! Decisional procrastinators often experience hesitation when making decisions about a task or behavior. In Anna’s case, her procrastination is not because of the indecision about what action to take, but because of her avoidance of potential negative outcomes.");
                    question3_response.setTextColor(Color.parseColor("#FF0000"));
                }
            }
        });

        radiogroup4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_4 = (RadioButton) findViewById(checkedId);
                answer4 = selected_button_4.getText().toString();

//                Toast.makeText(getApplicationContext(), "your choice is " + answer1, Toast.LENGTH_LONG).show();
                if(answer4.equals("A. Arousal")){
                    question4_response.setText("Not quite right! Arousal procrastination often occurs because some individuals believe that the pressure that comes with imminent deadlines assists them in completing tasks productively. This doesn't seem to be the case, because Vincent is not seeking the pressure and productivity associated with the imminent deadline.");
                    question4_response.setTextColor(Color.parseColor("#FF0000"));
                } else if (answer4.equals("B. Avoidant")) {
                    question4_response.setText("Good job! That is the correct one. Avoidant procrastination often happens because some individuals want to avoid negative emotions or situations associated with the tasks. In Vincent’s case, he procrastinated on submitting his artwork because of his anxiety about potential criticism.");
                    question4_response.setTextColor(Color.parseColor("#008000"));
                } else if (answer4.equals("C. Decisional")) {
                    question4_response.setText("Not quite right! Decisional procrastinators often experience hesitation when making decisions about a task or behavior. In Vincent’s case, his procrastination doesn’t come from the indecision about what action to take, but comes from his avoidance of potential negative criticism.");
                    question4_response.setTextColor(Color.parseColor("#FF0000"));
                }
            }
        });

        radiogroup5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_5 = (RadioButton) findViewById(checkedId);
                answer5 = selected_button_5.getText().toString();

//                Toast.makeText(getApplicationContext(), "your choice is " + answer1, Toast.LENGTH_LONG).show();
                if(answer5.equals("A. Arousal")){
                    question5_response.setText("Not quite right! Arousal procrastination often occurs because some individuals believe that the pressure that comes with imminent deadlines assists them in completing tasks productively. This doesn't seem applicable to his situation, because John is not seeking the pressure and productivity associated with the imminent deadline.");
                    question5_response.setTextColor(Color.parseColor("#FF0000"));
                } else if (answer5.equals("B. Avoidant")) {
                    question5_response.setText("Not quite right!Avoidant procrastination often happens because some individuals want to avoid negative emotions or situations associated with the tasks. In John’s case, his procrastination on declaring his college major doesn’t seem to be connected to ");
                    question5_response.setTextColor(Color.parseColor("#FF0000"));
                } else if (answer5.equals("C. Decisional")) {
                    question5_response.setText("Good job! Decisional procrastinators often experience hesitation when making decisions about a task or behavior. In Vincent’s case, his procrastination doesn’t come from the indecision about what action to take, but comes from his avoidance of potential negative criticism.");
                    question5_response.setTextColor(Color.parseColor("#008000"));
                }
            }
        });

//        radiogroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                RadioButton selected_button_2 = (RadioButton) findViewById(checkedId);
//                answer2 = selected_button_2.getText().toString();
//
////                Toast.makeText(getApplicationContext(), "your choice is " + answer1, Toast.LENGTH_LONG).show();
//                if(answer1.equals("A. Arousal")){
//                    question1_response.setText("");
//                    question1_response.setTextColor(Color.parseColor("#008000"));
//                } else if (answer1.equals("B. Avoidant")) {
//                    question1_response.setText("");
//                    question1_response.setTextColor(Color.parseColor("#FF0000"));
//                } else if (answer1.equals("C. Decisional")) {
//                    question1_response.setText("");
//                    question1_response.setTextColor(Color.parseColor("#FF0000"));
//                }
//            }
//        });


    }
    private void open_Previous_Activity() {
        Intent intent = new Intent(this,Chapter1_Activity1_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Next_Activity() {
        Intent intent = new Intent(this,Chapter1_Intro_ACT_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter1() {
        Intent intent = new Intent(this,Chapter1_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
