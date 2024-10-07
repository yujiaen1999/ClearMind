package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PostSurvey8_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;
    private Button button_back;
    private Button button_next;

    private String scale_answer37;
    private String scale_answer38;
    private String scale_answer39;
    private String scale_answer40;
    private String scale_answer41;

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
        setContentView(R.layout.activity_pre_survey_9);
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

        activityRef = db.child("userActivity").child(username).child("PostSurvey_8");
        activityId = intent.getStringExtra("activityId");

        TextView title = findViewById(R.id.textView1);
        title.setText("ClearMind Post-Survey");

        retrieveData();

        radiogroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                scale_answer37 = selected_button_1.getText().toString();
            }
        });

        radiogroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                scale_answer38 = selected_button_1.getText().toString();
            }
        });

        radiogroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                scale_answer39 = selected_button_1.getText().toString();
            }
        });

        radiogroup4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                scale_answer40 = selected_button_1.getText().toString();
            }
        });

        radiogroup5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selected_button_1 = (RadioButton) findViewById(checkedId);
                scale_answer41 = selected_button_1.getText().toString();
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

                if (scale_answer37==null || scale_answer38==null || scale_answer39==null || scale_answer40==null || scale_answer41==null){
                    Toast.makeText(PostSurvey8_Activity.this,  "Empty input", Toast.LENGTH_SHORT).show();
                } else {
                    db.child("PostSurvey").child(username).child("37").setValue(scale_answer37);
                    db.child("PostSurvey").child(username).child("38").setValue(scale_answer38);
                    db.child("PostSurvey").child(username).child("39").setValue(scale_answer39);
                    db.child("PostSurvey").child(username).child("40").setValue(scale_answer40);
                    db.child("PostSurvey").child(username).child("41").setValue(scale_answer41);

                    toNextPage = true;
                }

                if (toNextPage){
                    open_Next_Activity();
                }
            }
        });

    }

    private void open_Previous_Activity() {
        Intent intent = new Intent(this,PostSurvey7_Activity.class);
        intent.putExtra("username", username);
        intent.putExtra("activityId", activityId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void open_Next_Activity() {
        Intent intent = new Intent(this,PostSurvey9_Activity.class);
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

            // Record the open time of the Scale 3
            db.child("userActivity").child(username).child("PostSurvey_0_Scale_3").child(activityId).child("openTime_ms").setValue(pageOpenTime);
            db.child("userActivity").child(username).child("PostSurvey_0_Scale_3").child(activityId).child("openTime_str").setValue(String.valueOf(resultdate_open));
        }
    }

    private void retrieveData() {
        // Retrieve and Display user input from the database
        db.child("PostSurvey").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                ArrayList<String> hashmap_presurvey = (ArrayList<String>) task.getResult().getValue();
                if(!task.isSuccessful()){
                    Log.e("firebase_postsurvey", "Error getting data", task.getException());
                }else{
                    Log.d("firebase_postsurvey", String.valueOf(task.getResult().getValue()));
                    if(hashmap_presurvey.size() > 37){

                        String current_answer37 = hashmap_presurvey.get(37);
                        switch (current_answer37) {
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
                        };

                        String current_answer38 = hashmap_presurvey.get(38);
                        switch (current_answer38) {
                            case "1":
                                radiogroup2.check(R.id.radiobtn_5);
                                break;
                            case "2":
                                radiogroup2.check(R.id.radiobtn_6);
                                break;
                            case "3":
                                radiogroup2.check(R.id.radiobtn_7);
                                break;
                            case "4":
                                radiogroup2.check(R.id.radiobtn_8);
                                break;
                        };

                        String current_answer39 = hashmap_presurvey.get(39);
                        switch (current_answer39) {
                            case "1":
                                radiogroup3.check(R.id.radiobtn_9);
                                break;
                            case "2":
                                radiogroup3.check(R.id.radiobtn_10);
                                break;
                            case "3":
                                radiogroup3.check(R.id.radiobtn_11);
                                break;
                            case "4":
                                radiogroup3.check(R.id.radiobtn_12);
                                break;
                        };

                        String current_answer40 = hashmap_presurvey.get(40);
                        switch (current_answer40) {
                            case "1":
                                radiogroup4.check(R.id.radiobtn_13);
                                break;
                            case "2":
                                radiogroup4.check(R.id.radiobtn_14);
                                break;
                            case "3":
                                radiogroup4.check(R.id.radiobtn_15);
                                break;
                            case "4":
                                radiogroup4.check(R.id.radiobtn_16);
                                break;
                        };

                        String current_answer41 = hashmap_presurvey.get(41);
                        switch (current_answer41) {
                            case "1":
                                radiogroup5.check(R.id.radiobtn_17);
                                break;
                            case "2":
                                radiogroup5.check(R.id.radiobtn_18);
                                break;
                            case "3":
                                radiogroup5.check(R.id.radiobtn_19);
                                break;
                            case "4":
                                radiogroup5.check(R.id.radiobtn_20);
                                break;
                        };
                    }
                }
            }
        });
    }

}
