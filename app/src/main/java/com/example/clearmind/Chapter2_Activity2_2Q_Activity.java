package com.example.clearmind;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Chapter2_Activity2_2Q_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;

    private Button button_back;
    private Button button_next;
    private ImageButton button_home;

    private TextView button_hint1;
    private TextView button_hint2;
    private TextView button_hint3;
    private TextView button_hint4;
    private TextView button_hint5;

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
    private long pageOpenTime;
    private long pageCloseTime;

    private final NavigationDrawerHelper navigationDrawerHelper = new NavigationDrawerHelper(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter2_activity2_2_questions);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

        button_home = findViewById(R.id.button_home);
        button_back = findViewById(R.id.button_previous);
        button_next = findViewById(R.id.button_next);

        button_hint1 = findViewById(R.id.hint_1);
        button_hint2 = findViewById(R.id.hint_2);
        button_hint3 = findViewById(R.id.hint_3);
        button_hint4 = findViewById(R.id.hint_4);
        button_hint5 = findViewById(R.id.hint_5);

        navigationDrawerHelper.setupNavigationDrawer(username);

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
                // update progress
                Map<String, Object> chapter2_progress_update = new HashMap<>();
                chapter2_progress_update.put("4_Example", "1");
                db.child("Chapter2").child("progress").child(username).updateChildren(chapter2_progress_update);

                open_Next_Activity();
//                openPopupWindow(v);
            }
        });

        // flag for hints status
        final Integer[] flag = {0,0,0,0,0};

        // ******************************************
        // Display the hints when click button
        // ******************************************
        String txt_hints = "<b>Hint</b>: When you drive with your passengers, remember that you are the one steering the bus, not the passengers. Don’t let your passengers decide where the bus goes.";
        button_hint1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
//                openHints(v);

                if (flag[0] == 0){
//                    String txt_hints = "<b>General Hint</b>: When you drive with your passengers, remember that you are steering the bus, instead of your passengers. Don’t let your passengers decide where the bus goes.";
                    button_hint1.setText(Html.fromHtml(txt_hints));
                    flag[0] = 1;
                } else{
                    button_hint1.setText("hints?");
                    flag[0] = 0;
                }
            }
        });

        button_hint2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag[1] == 0) {
//                    String txt_hints = "<b>General Summary</b>: It is the hint template"
//                            + "<br><br><b>General Summary</b>: It is the hints template"
//                            + "<br><br><b>General Summary</b>: It is the hints template";
                    button_hint2.setText(Html.fromHtml(txt_hints));
                    flag[1] = 1;
                } else {
                    button_hint2.setText("hints?");
                    flag[1] = 0;
                }
            }
        });

        button_hint3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag[2] == 0) {
//                    String txt_hints = "<b>General Summary</b>: It is the hint template"
//                            + "<br><br><b>General Summary</b>: It is the hints template"
//                            + "<br><br><b>General Summary</b>: It is the hints template";
                    button_hint3.setText(Html.fromHtml(txt_hints));
                    flag[2] = 1;
                } else {
                    button_hint3.setText("hints?");
                    flag[2] = 0;
                }
            }
        });

        button_hint4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag[3] == 0) {
//                    String txt_hints = "<b>General Summary</b>: It is the hint template"
//                            + "<br><br><b>General Summary</b>: It is the hints template"
//                            + "<br><br><b>General Summary</b>: It is the hints template";
                    button_hint4.setText(Html.fromHtml(txt_hints));
                    flag[3] = 1;
                } else {
                    button_hint4.setText("hints?");
                    flag[3] = 0;
                }
            }
        });

        button_hint5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag[4] == 0) {
//                    String txt_hints = "<b>General Summary</b>: It is the hint template"
//                            + "<br><br><b>General Summary</b>: It is the hints template"
//                            + "<br><br><b>General Summary</b>: It is the hints template";
                    button_hint5.setText(Html.fromHtml(txt_hints));
                    flag[4] = 1;
                } else {
                    button_hint5.setText("hints?");
                    flag[4] = 0;
                }
            }
        });


        // ******************************************
        // handle the user response for the questions
        // ******************************************
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
                if(answer1.equals("Put off studying and constantly check social media")){
                    question1_response.setText("Not quite right! You are letting your challenging passengers control your journey, and you can't make progress towards your destination if you follow what your passengers want you to do. ");
                    question1_response.setTextColor(Color.parseColor("#FF0000"));
                } else if (answer1.equals("Complete the assignment regardless of what your minds say")) {
                    question1_response.setText("You got it! Complete the assignment regardless of what your minds say is an example of driving with your passengers. You don’t let your passengers control your journey.");
                    question1_response.setTextColor(Color.parseColor("#008000"));
                }
            }
        });

        radiogroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_2 = (RadioButton) findViewById(checkedId);
                answer2 = selected_button_2.getText().toString();

                if(answer2.equals("Embrace the uncertainty and fear and go to your dream school")){
                    question2_response.setText("You got it! Embracing the uncertainty and fear and going to your dream school is an example of driving with your passengers. You don’t let your passengers impede your goals.\n");
                    question2_response.setTextColor(Color.parseColor("#008000"));
                } else if (answer2.equals("Decline the offer letter and go to a local college")) {
                    question2_response.setText("Not quite right! You are letting your challenging passengers control your journey, and you can't make progress towards your destination if you follow what your passengers want you to do. ");
                    question2_response.setTextColor(Color.parseColor("#FF0000"));
                }
            }
        });

        radiogroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_3 = (RadioButton) findViewById(checkedId);
                answer3 = selected_button_3.getText().toString();

                if(answer3.equals("Ignore and suppress these emotions")){
                    question3_response.setText("Not quite right! Ignoring and suppressing your emotions is an example of throwing your passengers off the bus.You are letting your challenging passengers control your journey, and you can't make progress towards your destination if you follow what your passengers want you to do.");
                    question3_response.setTextColor(Color.parseColor("#FF0000"));
                } else if (answer3.equals("Allow yourself to feel sad but open to seek support from friends and family")) {
                    question3_response.setText("You got it! Allowing yourself to feel sad but open to seek support from friends and family is an example of driving with your passengers towards your academic goal. You should allow the challenging passengers on the bus, but don’t let them impede you.");
                    question3_response.setTextColor(Color.parseColor("#008000"));
                }
            }
        });

        radiogroup4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_4 = (RadioButton) findViewById(checkedId);
                answer4 = selected_button_4.getText().toString();

                if(answer4.equals("Give up on playing guitar")){
                    question4_response.setText("Not quite right! Giving up on playing guitar halfway is an example of following what your passengers want you to do. You are letting your challenging passengers control your journey, and you can't make progress towards your destination if you follow what your passengers want you to do.");
                    question4_response.setTextColor(Color.parseColor("#FF0000"));
                } else if (answer4.equals("Embrace the learning process and don’t solely focus on the end result")) {
                    question4_response.setText("You got it! Embracing the learning process itself and not solely focusing on the end result is an example of driving with your passengers.You should allow the challenging passengers on the bus, but don’t let them impede your goals.");
                    question4_response.setTextColor(Color.parseColor("#008000"));
                }
            }
        });

        radiogroup5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_5 = (RadioButton) findViewById(checkedId);
                answer5 = selected_button_5.getText().toString();

                if(answer5.equals("Practice mindful eating and ask yourself if you're really hungry")){
                    question5_response.setText("You got it! Practicing mindful eating and asking yourself whether you are really hungry or not is an example of driving with your passengers. You should allow the challenging passengers on the bus, but don’t let them impede your goals.");
                    question5_response.setTextColor(Color.parseColor("#008000"));
                } else if (answer5.equals("Eat whatever you want and commit to the diet next time")) {
                    question5_response.setText("Not quite right! Eating whatever you want and committing to the diet next time is an example of doing what your passengers told you to do.  You are letting your challenging passengers control your journey, and you can't make progress towards your destination if you follow what your passengers want you to do.");
                    question5_response.setTextColor(Color.parseColor("#FF0000"));
                }
            }
        });



    }


    private void openPopupWindow(View view) {
        Integer score = 0;

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View viewPopupWindow = layoutInflater.inflate(R.layout.activity_chapter1_activity2_score, null);
//        final PopupWindow popupWindow = new PopupWindow(viewPopupWindow, 600, 600, true);
        final PopupWindow popupWindow = new PopupWindow(viewPopupWindow);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        TextView show_score = (TextView) viewPopupWindow.findViewById(R.id.textView_score);
        Button button_confirm = (Button) viewPopupWindow.findViewById(R.id.button_confirm);

        if(answer1 != null && answer1.equals("Complete the assignment regardless of what your minds say")){
//            Toast.makeText(Chapter1_Activity2_Activity.this, "answer 1 is right", Toast.LENGTH_SHORT).show();
            score += 20;
        }
        if(answer2 != null && answer2.equals("Embrace the uncertainty and fear and go to your dream school")){
//            Toast.makeText(Chapter1_Activity2_Activity.this, "answer 2 is right", Toast.LENGTH_SHORT).show();
            score += 20;
        }
        if(answer3 != null && answer3.equals("Allow yourself to feel sad but open to seek support from friends and family")){
//            Toast.makeText(Chapter1_Activity2_Activity.this, "answer 3 is right", Toast.LENGTH_SHORT).show();
            score += 20;
        }
        if(answer4 != null && answer4.equals("Embrace the learning process and don’t solely focus on the end result")){
//            Toast.makeText(Chapter1_Activity2_Activity.this, "answer 4 is right", Toast.LENGTH_SHORT).show();
            score += 20;
        }
        if(answer5 != null && answer5.equals("Practice mindful eating and ask yourself if you're really hungry")){
//            Toast.makeText(Chapter1_Activity2_Activity.this, "answer 5 is right", Toast.LENGTH_SHORT).show();
            score += 20;
        }

        String show_txt = "You got " + score + " out of 100 points. ";
        if (score >= 80){
            show_txt += "Good job!";
        }

        show_score.setText(show_txt);

        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // update progress
                Map<String, Object> chapter2_progress_update = new HashMap<>();
                chapter2_progress_update.put("4_Example", "1");
                db.child("Chapter2").child("progress").child(username).updateChildren(chapter2_progress_update);

                open_Next_Activity();
//                openPopupWindow(v);
            }
        });
    }


    private void open_Previous_Activity() {
        Intent intent = new Intent(this,Chapter2_Activity2_1_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Next_Activity() {
        Intent intent = new Intent(this,Chapter2_Diagram_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter1() {
        Intent intent = new Intent(this,Chapter2_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pageOpenTime = System.currentTimeMillis(); // get the page open time
    }

    @Override
    protected void onPause() {
        super.onPause();
        pageCloseTime = System.currentTimeMillis(); // get the page close time

        if (pageCloseTime - pageOpenTime > 1999){   // Only if the view time >= 2 seconds
            sendTimeStampsToFirebase(); // store the Time Stamp to Firebase
        }
//        sendTimeStampsToFirebase();
    }

    private void sendTimeStampsToFirebase() {
        DatabaseReference activityRef = db.child("userActivity").child(username).child("Part2_4_Activity2_Questions");

        // create a new activityID
        String activityId = activityRef.push().getKey();

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");

        Date resultdate_open = new Date(pageOpenTime);
        Date resultdate_close = new Date(pageCloseTime);

        Map<String, Object> activityData = new HashMap<>();
        activityData.put("openTime_ms", pageOpenTime);
        activityData.put("closeTime_ms", pageCloseTime);
        activityData.put("duration", pageCloseTime - pageOpenTime);

        activityData.put("openTime_str", String.valueOf(resultdate_open));
        activityData.put("closeTime_str", String.valueOf(resultdate_close));

        if (activityId != null) {
            activityRef.child(activityId).setValue(activityData)
                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "Activity time recorded successfully"))
                    .addOnFailureListener(e -> Log.d("Firebase", "Failed to record activity time", e));
        }
    }

}
