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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PreSurvey2_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;
    private Button button_back;
    private Button button_next;

    private String scale_answer1;
    private String scale_answer2;
    private String scale_answer3;
    private String scale_answer4;
    private String scale_answer5;

    private RadioGroup radiogroup1;
    private RadioGroup radiogroup2;
    private RadioGroup radiogroup3;
    private RadioGroup radiogroup4;
    private RadioGroup radiogroup5;

    private long pageOpenTime;
    private long pageCloseTime;

    private DatabaseReference activityRef;
    private String activityId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_survey_2);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

        button_back = findViewById(R.id.button_previous);
        button_next = findViewById(R.id.button_next);

        radiogroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
        radiogroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
        radiogroup3 = (RadioGroup) findViewById(R.id.radioGroup3);
        radiogroup4 = (RadioGroup) findViewById(R.id.radioGroup4);
        radiogroup5 = (RadioGroup) findViewById(R.id.radioGroup5);

        activityRef = db.child("userActivity").child(username).child("PreSurvey_2");
        activityId = intent.getStringExtra("activityId");

        retrieveData();

        radiogroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                scale_answer1 = selected_button_1.getText().toString();
            }
        });

        radiogroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                scale_answer2 = selected_button_1.getText().toString();
            }
        });

        radiogroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                scale_answer3 = selected_button_1.getText().toString();
            }
        });

        radiogroup4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                scale_answer4 = selected_button_1.getText().toString();
            }
        });

        radiogroup5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                scale_answer5 = selected_button_1.getText().toString();
            }
        });


        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
//                Toast.makeText(PreSurvey2_Activity.this,  "Finish the following questions in one sitting, it will take about 9 minutes", Toast.LENGTH_SHORT).show();
                open_Previous_Activity();
            }
        });

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                boolean toNextPage = false;

                if (scale_answer1==null || scale_answer2==null || scale_answer3==null || scale_answer4==null || scale_answer5==null){
                    Toast.makeText(PreSurvey2_Activity.this,  "Empty input", Toast.LENGTH_SHORT).show();
                } else {
                    db.child("PreSurvey").child(username).child("1").setValue(scale_answer1);
                    db.child("PreSurvey").child(username).child("2").setValue(scale_answer2);
                    db.child("PreSurvey").child(username).child("3").setValue(scale_answer3);
                    db.child("PreSurvey").child(username).child("4").setValue(scale_answer4);
                    db.child("PreSurvey").child(username).child("5").setValue(scale_answer5);

                    toNextPage = true;
                }

                if (toNextPage){
                    open_Next_Activity();
                }
            }
        });

    }

    private void open_Previous_Activity() {
        Intent intent = new Intent(this,PreSurvey1_Activity.class);
        intent.putExtra("username", username);
        intent.putExtra("activityId", activityId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void open_Next_Activity() {
        Intent intent = new Intent(this,PreSurvey3_Activity.class);
        intent.putExtra("username", username);
        intent.putExtra("activityId", activityId);
        Log.d("activityId: ", activityId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
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

//        if (pageCloseTime - pageOpenTime > 1999){   // Only if the view time >= 2 seconds
//            sendTimeStampsToFirebase(); // store the Time Stamp to Firebase
//        }
        sendTimeStampsToFirebase();
    }

    private void sendTimeStampsToFirebase() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");

        Date resultdate_open = new Date(pageOpenTime);
        Date resultdate_close = new Date(pageCloseTime);

        Map<String, Object> activityData = new HashMap<>();
        activityData.put("openTime_ms", pageOpenTime);
        activityData.put("openTime_str", String.valueOf(resultdate_open));

        activityData.put("closeTime_ms", pageCloseTime);
        activityData.put("closeTime_str", String.valueOf(resultdate_close));

        activityData.put("duration", pageCloseTime - pageOpenTime);

        if (activityId != null) {
            activityRef.child(activityId).setValue(activityData)
                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "Activity time recorded successfully"))
                    .addOnFailureListener(e -> Log.d("Firebase", "Failed to record activity time", e));

            db.child("userActivity").child(username).child("PreSurvey_0_Scale_1").child(activityId).child("openTime_ms").setValue(pageOpenTime);
            db.child("userActivity").child(username).child("PreSurvey_0_Scale_1").child(activityId).child("openTime_str").setValue(String.valueOf(resultdate_open));
        }
    }

    private void retrieveData() {
        // Retrieve and Display user input from the database
        db.child("PreSurvey").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                HashMap<String, String> hashmap_presurvey= (HashMap<String, String>) task.getResult().getValue();
                if(!task.isSuccessful()){
                    Log.e("firebase_presurvey", "Error getting data", task.getException());
                }else{
                    Log.d("firebase_presurvey", String.valueOf(task.getResult().getValue()));
                    if(hashmap_presurvey.get("1") != null){

                        String current_answer1 = hashmap_presurvey.get("1");
                        switch (current_answer1) {
                            case "1":
                                radiogroup1.check(R.id.radiobtn_1);
                                break;
                            case "2":
                                radiogroup1.check(R.id.radiobtn_2);
                                break;
                            case "3":
                                radiogroup1.check(R.id.radiobtn_3);
                                break;
                            case "4":
                                radiogroup1.check(R.id.radiobtn_4);
                                break;
                            case "5":
                                radiogroup1.check(R.id.radiobtn_5);
                                break;
                        };

                        String current_answer2 = hashmap_presurvey.get("2");
                        switch (current_answer2) {
                            case "1":
                                radiogroup2.check(R.id.radiobtn_6);
                                break;
                            case "2":
                                radiogroup2.check(R.id.radiobtn_7);
                                break;
                            case "3":
                                radiogroup2.check(R.id.radiobtn_8);
                                break;
                            case "4":
                                radiogroup2.check(R.id.radiobtn_9);
                                break;
                            case "5":
                                radiogroup2.check(R.id.radiobtn_10);
                                break;
                        };

                        String current_answer3 = hashmap_presurvey.get("3");
                        switch (current_answer3) {
                            case "1":
                                radiogroup3.check(R.id.radiobtn_11);
                                break;
                            case "2":
                                radiogroup3.check(R.id.radiobtn_12);
                                break;
                            case "3":
                                radiogroup3.check(R.id.radiobtn_13);
                                break;
                            case "4":
                                radiogroup3.check(R.id.radiobtn_14);
                                break;
                            case "5":
                                radiogroup3.check(R.id.radiobtn_15);
                                break;
                        };

                        String current_answer4 = hashmap_presurvey.get("4");
                        switch (current_answer4) {
                            case "1":
                                radiogroup4.check(R.id.radiobtn_16);
                                break;
                            case "2":
                                radiogroup4.check(R.id.radiobtn_17);
                                break;
                            case "3":
                                radiogroup4.check(R.id.radiobtn_18);
                                break;
                            case "4":
                                radiogroup4.check(R.id.radiobtn_19);
                                break;
                            case "5":
                                radiogroup4.check(R.id.radiobtn_20);
                                break;
                        };

                        String current_answer5 = hashmap_presurvey.get("5");
                        switch (current_answer5) {
                            case "1":
                                radiogroup5.check(R.id.radiobtn_21);
                                break;
                            case "2":
                                radiogroup5.check(R.id.radiobtn_22);
                                break;
                            case "3":
                                radiogroup5.check(R.id.radiobtn_23);
                                break;
                            case "4":
                                radiogroup5.check(R.id.radiobtn_24);
                                break;
                            case "5":
                                radiogroup5.check(R.id.radiobtn_25);
                                break;
                        };
                    }
                }
            }
        });
    }
}
