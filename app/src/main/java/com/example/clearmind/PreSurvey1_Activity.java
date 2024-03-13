package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PreSurvey1_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;

    private Button button_back;
    private Button button_next;
    private String txt_answer2;
    private String txt_answer4;

    private long pageOpenTime;
    private long pageCloseTime;

    private DatabaseReference activityRef;
    private String activityId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_survey_1);
        Intent intent = getIntent();

//        // Enable full screen display and avoid nav bar overlap
//        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_pre_survey_1), (v, windowInsets) -> {
//            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
//            // Apply the insets as a margin to the view. This solution sets only the
//            // bottom, left, and right dimensions, but you can apply whichever insets are
//            // appropriate to your layout. You can also update the view padding if that's
//            // more appropriate.
//            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
////            mlp.leftMargin = insets.left;
////            mlp.rightMargin = insets.right;
//            mlp.bottomMargin = insets.bottom;
//            v.setLayoutParams(mlp);
//
//            // Return CONSUMED if you don't want want the window insets to keep passing
//            // down to descendant views.
//            return WindowInsetsCompat.CONSUMED;
//        });

        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

        button_back = findViewById(R.id.button_previous);
        button_next = findViewById(R.id.button_next);

        RadioGroup radiogroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
        RadioGroup radiogroup2 = (RadioGroup) findViewById(R.id.radioGroup2);

        activityRef = db.child("userActivity").child(username).child("PreSurvey_1");
        activityId = activityRef.push().getKey();

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
                    db.child("PreSurvey").child(username).child("0_age").setValue(txt_answer1);
                    db.child("PreSurvey").child(username).child("0_gender").setValue(txt_answer2);
                    db.child("PreSurvey").child(username).child("0_major").setValue(txt_answer3);
                    db.child("PreSurvey").child(username).child("0_yrs_school").setValue(txt_answer4);
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

            db.child("userActivity").child(username).child("PreSurvey_0_All").child(activityId).child("openTime_ms").setValue(pageOpenTime);
            db.child("userActivity").child(username).child("PreSurvey_0_All").child(activityId).child("openTime_str").setValue(String.valueOf(resultdate_open));
        }
    }

}
