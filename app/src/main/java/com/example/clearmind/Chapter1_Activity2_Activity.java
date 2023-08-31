package com.example.clearmind;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
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

//    private String answer1;
//    private String answer2;
//    private String answer3;
//    private String answer4;
//    private String answer5;
//
//    private TextView question1_response;
//    private TextView question2_response;
//    private TextView question3_response;
//    private TextView question4_response;
//    private TextView question5_response;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter1_activity2);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

        button_home = findViewById(R.id.button_home);
        button_back = findViewById(R.id.button_previous);
        button_next = findViewById(R.id.button_next);

        TextView textView3 = findViewById(R.id.textView3);
        String txt_textView3 = "<b>Here is the detailed definition of each type:</b>" +
                "<br><br><b>Arousal procrastination</b>: It is the tendency to purposefully put off completing specific tasks until the last moment. It often arises from a desire for heightened urgency, which comes with approaching deadlines. Individuals who engage themselves in arousal procrastination believe that the pressure of a pending deadline assists them in completing tasks under tight time constraints." +
                "<br><br><b>Avoidant procrastination</b>: It is the tendency to postpone or avoid tasks out of fear of failure, task aversiveness, or even success. Fear of failure manifests as a common belief among avoidant procrastinators that they lack essential skills or abilities to accomplish them, leading to task delays. Task aversiveness is the notion that avoidant procrastinators genuinely dislike the task, leading them to postpone it. Lastly, fear of success is the notion that avoidant procrastinators fear success because they anticipate additional responsibilities and expectations that come with achieving it." +
                "<br><br><b>Decisional procrastination</b>: It is the tendency to put off making decisions in a timely manner. Decisional procrastinators often experience hesitation when faced with making decisions about a task or behavior. This procrastination tendency can arise from the perceived complexity of the task, potential conflicts with other individuals that a decision may evoke, and the tendency to protect one’s self-esteem or self-confidence." +
                "<br><br>Now let’s test your understanding of arousal, avoidant, and decisional procrastination tendencies. For each multiple-choice question, you will need about 30 seconds to complete.";
        textView3.setText(Html.fromHtml(txt_textView3));

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
//                openPopupWindow(v);
            }
        });

//        question1_response = findViewById(R.id.question1_response);
//        question2_response = findViewById(R.id.question2_response);
//        question3_response = findViewById(R.id.question3_response);
//        question4_response = findViewById(R.id.question4_response);
//        question5_response = findViewById(R.id.question5_response);
//
//
//        RadioGroup radiogroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
//        RadioGroup radiogroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
//        RadioGroup radiogroup3 = (RadioGroup) findViewById(R.id.radioGroup3);
//        RadioGroup radiogroup4 = (RadioGroup) findViewById(R.id.radioGroup4);
//        RadioGroup radiogroup5 = (RadioGroup) findViewById(R.id.radioGroup5);
//
//
//        radiogroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
//                answer1 = selected_button_1.getText().toString();
//
////                Toast.makeText(getApplicationContext(), "your choice is " + answer1, Toast.LENGTH_LONG).show();
//                if(answer1.equals("A. Arousal")){
//                    question1_response.setText("Good job! That is the correct one. Arousal procrastination often occurs because some individuals believe that the pressure that comes with imminent deadlines assists them in completing tasks productively. In Lynn’s case, she believes that the pressure of the impending deadline helps her to focus.");
//                    question1_response.setTextColor(Color.parseColor("#008000"));
//                } else if (answer1.equals("B. Avoidant")) {
//                    question1_response.setText("Not quite right! Avoidant procrastination often happens because some individuals want to avoid negative emotions or situations associated with the tasks. In Lynn’s case, she doesn’t seem to avoid her homework because of fear or negative feelings.");
//                    question1_response.setTextColor(Color.parseColor("#FF0000"));
//                } else if (answer1.equals("C. Decisional")) {
//                    question1_response.setText("Not quite right! Decisional procrastinators often experience hesitation when making decisions about a task or behavior. In Lynn’s case, she doesn’t experience any apparent indecision.");
//                    question1_response.setTextColor(Color.parseColor("#FF0000"));
//                }
//            }
//        });
//
//        radiogroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                RadioButton selected_button_2 = (RadioButton) findViewById(checkedId);
//                answer2 = selected_button_2.getText().toString();
//
////                Toast.makeText(getApplicationContext(), "your choice is " + answer1, Toast.LENGTH_LONG).show();
//                if(answer2.equals("A. Arousal")){
//                    question2_response.setText("Not quite right! Arousal procrastination often occurs because some individuals believe that the pressure that comes with imminent deadlines assists them in completing tasks productively. This doesn't seem to be the case, because Tommy is not seeking the pressure and productivity associated with the imminent deadline.\n");
//                    question2_response.setTextColor(Color.parseColor("#FF0000"));
//                } else if (answer2.equals("B. Avoidant")) {
//                    question2_response.setText("Good job! That is the correct one. Avoidant procrastination often happens because some individuals want to avoid negative emotions or situations associated with the tasks. In Tommy’s case, he procrastinated on his English essay because he perceived the class as boring.");
//                    question2_response.setTextColor(Color.parseColor("#008000"));
//                } else if (answer2.equals("C. Decisional")) {
//                    question2_response.setText("Not quite right! Decisional procrastinators often experience hesitation when making decisions about a task or behavior. In Tommy’s case, he doesn’t experience any apparent indecision.");
//                    question2_response.setTextColor(Color.parseColor("#FF0000"));
//                }
//            }
//        });
//
//        radiogroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                RadioButton selected_button_3 = (RadioButton) findViewById(checkedId);
//                answer3 = selected_button_3.getText().toString();
//
////                Toast.makeText(getApplicationContext(), "your choice is " + answer1, Toast.LENGTH_LONG).show();
//                if(answer3.equals("A. Arousal")){
//                    question3_response.setText("Not quite right! Arousal procrastination often occurs because some individuals believe that the pressure that comes with imminent deadlines assists them in completing tasks productively. This doesn't seem to be the case, because Anna is not seeking the pressure and productivity associated with the imminent deadline.");
//                    question3_response.setTextColor(Color.parseColor("#FF0000"));
//                } else if (answer3.equals("B. Avoidant")) {
//                    question3_response.setText("Good job! That is the correct one. Avoidant procrastination often happens because some individuals want to avoid negative emotions or situations associated with the tasks. In Anna’s case, she procrastinated on seeking medical advice because of her anxiety about potential diagnoses.");
//                    question3_response.setTextColor(Color.parseColor("#008000"));
//                } else if (answer3.equals("C. Decisional")) {
//                    question3_response.setText("Not quite right! Decisional procrastinators often experience hesitation when making decisions about a task or behavior. In Anna’s case, her procrastination is not because of the indecision about what action to take, but because of her avoidance of potential negative outcomes.");
//                    question3_response.setTextColor(Color.parseColor("#FF0000"));
//                }
//            }
//        });
//
//        radiogroup4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                RadioButton selected_button_4 = (RadioButton) findViewById(checkedId);
//                answer4 = selected_button_4.getText().toString();
//
////                Toast.makeText(getApplicationContext(), "your choice is " + answer1, Toast.LENGTH_LONG).show();
//                if(answer4.equals("A. Arousal")){
//                    question4_response.setText("Not quite right! Arousal procrastination often occurs because some individuals believe that the pressure that comes with imminent deadlines assists them in completing tasks productively. This doesn't seem to be the case, because Vincent is not seeking the pressure and productivity associated with the imminent deadline.");
//                    question4_response.setTextColor(Color.parseColor("#FF0000"));
//                } else if (answer4.equals("B. Avoidant")) {
//                    question4_response.setText("Good job! That is the correct one. Avoidant procrastination often happens because some individuals want to avoid negative emotions or situations associated with the tasks. In Vincent’s case, he procrastinated on submitting his artwork because of his anxiety about potential criticism.");
//                    question4_response.setTextColor(Color.parseColor("#008000"));
//                } else if (answer4.equals("C. Decisional")) {
//                    question4_response.setText("Not quite right! Decisional procrastinators often experience hesitation when making decisions about a task or behavior. In Vincent’s case, his procrastination doesn’t come from the indecision about what action to take, but comes from his avoidance of potential negative criticism.");
//                    question4_response.setTextColor(Color.parseColor("#FF0000"));
//                }
//            }
//        });
//
//        radiogroup5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                RadioButton selected_button_5 = (RadioButton) findViewById(checkedId);
//                answer5 = selected_button_5.getText().toString();
//
////                Toast.makeText(getApplicationContext(), "your choice is " + answer1, Toast.LENGTH_LONG).show();
//                if(answer5.equals("A. Arousal")){
//                    question5_response.setText("Not quite right! Arousal procrastination often occurs because some individuals believe that the pressure that comes with imminent deadlines assists them in completing tasks productively. This doesn't seem applicable to his situation, because John is not seeking the pressure and productivity associated with the imminent deadline.");
//                    question5_response.setTextColor(Color.parseColor("#FF0000"));
//                } else if (answer5.equals("B. Avoidant")) {
//                    question5_response.setText("Not quite right!Avoidant procrastination often happens because some individuals want to avoid negative emotions or situations associated with the tasks. In John’s case, his procrastination on declaring his college major doesn’t seem to be connected to difficult emotions.");
//                    question5_response.setTextColor(Color.parseColor("#FF0000"));
//                } else if (answer5.equals("C. Decisional")) {
//                    question5_response.setText("Good job! Decisional procrastinators often experience hesitation when making decisions about a task or behavior. In John’s case, his procrastination comes from the indecision about what action to take.");
//                    question5_response.setTextColor(Color.parseColor("#008000"));
//                }
//            }
//        });
    }
//
//    private void openPopupWindow(View view) {
//        Integer score = 0;
//
//        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//
//        View viewPopupWindow = layoutInflater.inflate(R.layout.activity_chapter1_activity2_score, null);
//
//        final PopupWindow popupWindow = new PopupWindow(viewPopupWindow, 900, 700, true);
//
//        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
//
//        TextView show_score = (TextView) viewPopupWindow.findViewById(R.id.textView_score);
//        Button button_confirm = (Button) viewPopupWindow.findViewById(R.id.button_confirm);
//
//        if(answer1 != null && answer1.equals("A. Arousal")){
////            Toast.makeText(Chapter1_Activity2_Activity.this, "answer 1 is right", Toast.LENGTH_SHORT).show();
//            score += 20;
//        }
//        if(answer2 != null && answer2.equals("B. Avoidant")){
////            Toast.makeText(Chapter1_Activity2_Activity.this, "answer 2 is right", Toast.LENGTH_SHORT).show();
//            score += 20;
//        }
//        if(answer3 != null && answer3.equals("B. Avoidant")){
////            Toast.makeText(Chapter1_Activity2_Activity.this, "answer 3 is right", Toast.LENGTH_SHORT).show();
//            score += 20;
//        }
//        if(answer4 != null && answer4.equals("B. Avoidant")){
////            Toast.makeText(Chapter1_Activity2_Activity.this, "answer 4 is right", Toast.LENGTH_SHORT).show();
//            score += 20;
//        }
//        if(answer5 != null && answer5.equals("C. Decisional")){
////            Toast.makeText(Chapter1_Activity2_Activity.this, "answer 5 is right", Toast.LENGTH_SHORT).show();
//            score += 20;
//        }
//
//        String show_txt = "You got " + score + " out of 100 points. ";
//        if (score >= 80){
//            show_txt += "Good job!";
//        }
//
//        show_score.setText(show_txt);
//
//        button_confirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v){
//                open_Next_Activity();
////                openPopupWindow(v);
//            }
//        });
//    }

    private void open_Previous_Activity() {
        Intent intent = new Intent(this,Chapter1_Activity1_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Next_Activity() {
        Intent intent = new Intent(this,Chapter1_Activity2_Question_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter1() {
        Intent intent = new Intent(this,Chapter1_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
