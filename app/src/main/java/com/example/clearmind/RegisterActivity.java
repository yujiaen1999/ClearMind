package com.example.clearmind;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.example.clearmind.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseReference db;

    private EditText username;
    private EditText name;
    private EditText email;

    private EditText answer4;

    private EditText answer5;
    private Button submit;

    private String signup_username;
    private String signup_password;

    private String txt_question1;
    private String txt_question2;
    private String txt_answer1;
    private String txt_answer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_register);
//        this.usernameInput = (EditText)(findViewById(R.id.username_input));
//        this.passwordInput = (EditText)(findViewById(R.id.password_input));
//        this.db = FirebaseDatabase.getInstance().getReference(); //get a reference of database
//        login = findViewById(R.id.entry_button);

        Intent intent = getIntent();
        this.signup_username = intent.getStringExtra("username");
        this.signup_password = intent.getStringExtra("password");
        this.txt_question1 = intent.getStringExtra("txt_question1");
        this.txt_question2 = intent.getStringExtra("txt_question2");
        this.txt_answer1 = intent.getStringExtra("txt_answer1");
        this.txt_answer1 = intent.getStringExtra("txt_answer1");

        this.db = FirebaseDatabase.getInstance().getReference();
        username = findViewById(R.id.answer1);
        name = findViewById(R.id.answer2);
        email = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);
        answer5 = findViewById(R.id.answer5);

        submit = findViewById(R.id.submit_button);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean toMainPage = false;
                String txt_username = username.getText().toString();
                String txt_name = name.getText().toString();
                String txt_email = email.getText().toString();
                String txt_answer4 = answer4.getText().toString();
                String txt_answer5 = answer5.getText().toString();


                if (txt_username.isEmpty() || txt_name.isEmpty() || txt_email.isEmpty() || txt_answer4.isEmpty() || txt_answer5.isEmpty() || !txt_username.equals(signup_username)){
                    if (!txt_username.equals(signup_username)){
                        Toast.makeText(RegisterActivity.this,  "Different username received", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this,  "Empty input", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("username", txt_username);
                    map.put("name", txt_name);
                    map.put("email", txt_email);
                    map.put("Question4", txt_answer4);
                    map.put("Question5", txt_answer5);


                    HashMap<String, Object> security_questions = new HashMap<>();
                    security_questions.put("question1", txt_question1);
                    security_questions.put("answer1", txt_answer1);
                    security_questions.put("question2", txt_question2);
                    security_questions.put("answer2", txt_answer2);

                    register_username_password(signup_username, signup_password);
                    register_security(signup_username, security_questions);
                    register(signup_username, map);

                    Toast.makeText(RegisterActivity.this,  "Register successful", Toast.LENGTH_SHORT).show();
                    toMainPage = true;
                    Log.d("REGISTER: ", "Register finished");
                }

                if (toMainPage){
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                    intent.putExtra("username", txt_username);
                    startActivity(intent);  //go to main page (activity)
                }
            }
        });

    }

    private void register_username_password(String username, String password){
        // TODO: Add all progress setting in Database here

        // user' username and password
        this.db.child("users").child(username).setValue(new User(username, password, "1"));

        // General progress
        HashMap<String, Object> progress_map = new HashMap<>();
        progress_map.put("presurvey", "0");
        progress_map.put("chapter1", "0");
        progress_map.put("chapter2", "0");
        progress_map.put("chapter3", "0");
        progress_map.put("chapter4", "0");
        progress_map.put("postsurvey", "0");

        this.db.child("progress").child(username).setValue(progress_map);

        // Chapter 1 progress
        HashMap<String, Object> progress_chapter1 = new HashMap<>();
        progress_chapter1.put("1_Opening", "0");
        progress_chapter1.put("2_Activity1_1", "0");
        progress_chapter1.put("3_Activity1_2_Discover", "0");
        progress_chapter1.put("4_Activity1_2_Questions", "0");
        progress_chapter1.put("5_Summary_and_ACT", "0");
        progress_chapter1.put("6_Time_Management", "0");
        progress_chapter1.put("7_Summary", "0");

        this.db.child("Chapter1").child("progress").child(username).setValue(progress_chapter1);

        // Chapter 2 progress
        HashMap<String, Object> progress_chapter2 = new HashMap<>();
        progress_chapter2.put("1_Opening", "0");
        progress_chapter2.put("2_Activity2_1", "0");
        progress_chapter2.put("3_Passengers_On_The_Bus", "0");
        progress_chapter2.put("4_Example", "0");
        progress_chapter2.put("5_Identify_your_passengers", "0");
        progress_chapter2.put("6_Diagram", "0");
        progress_chapter2.put("7_Willingness_to_Carry_On", "0");
        progress_chapter2.put("8_Summary", "0");
        this.db.child("Chapter2").child("progress").child(username).setValue(progress_chapter2);

        // Chapter 3 progress
        HashMap<String, Object> progress_chapter3 = new HashMap<>();
        progress_chapter3.put("1_Opening", "0");
        progress_chapter3.put("2_Activity3_1", "0");
        progress_chapter3.put("3_Activity3_2", "0");
        progress_chapter3.put("4_Activity3_3", "0");
        progress_chapter3.put("5_Activity3_4", "0");
        progress_chapter3.put("6_Activity3_5_1", "0");
        progress_chapter3.put("7_Activity3_5_2", "0");
        progress_chapter3.put("8_Activity3_6", "0");
        progress_chapter3.put("9_Summary", "0");
        this.db.child("Chapter3").child("progress").child(username).setValue(progress_chapter3);

        // Chapter 4 progress
        HashMap<String, Object> progress_chapter4 = new HashMap<>();
        progress_chapter4.put("1_Opening", "0");
        progress_chapter4.put("2_Activity4_1", "0");
        progress_chapter4.put("3_Activity4_2", "0");
        progress_chapter4.put("4_Activity4_3", "0");
        progress_chapter4.put("5_Activity4_4", "0");
        progress_chapter4.put("6_Summary", "0");
        progress_chapter4.put("7_Activity4_5_intro_tracker", "0");
        this.db.child("Chapter4").child("progress").child(username).setValue(progress_chapter4);

        Log.d("REGISTER: ", "Set Parts initialization successful");

        // put Usage Data placeholder for NEW user
        DateTimeFormatter formatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        }

        for (int i = 1; i < 8; i++) {
            LocalDate date = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                date = LocalDate.now().minusDays(i);
            }
            String dateString = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                dateString = date.format(formatter);
            }

            Log.d("Get dates: ", dateString);
            storeUsageTimeIfNotExists(dateString, 0L);
        }
        Log.d("REGISTER: ", "Set UsageData placeholder loop end");

    }

    private void register_security(String username, HashMap<String, Object> map) {
        this.db.child("security").child(username).setValue(map);
        Log.d("REGISTER: ", "Set Security Questions successful");
    }

    private void register(String username, HashMap<String, Object> map) {
        this.db.child("register").child(username).setValue(map);
        Log.d("REGISTER: ", "Set Other Questions successful");
    }


    private void storeUsageTimeIfNotExists(String date, long totalTimeForeground) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        String dateKey = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime());

        db.child("AppUsage").child(signup_username).child(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    db.child("AppUsage").child(signup_username).child(date).setValue(totalTimeForeground);
                    Log.d("FirebaseDB set", "Stored new data for " + date);
                } else {
                    // data existed
                    Log.d("FirebaseDB set", "Data for " + date + " already exists.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("FirebaseDB", "Failed to read value.", databaseError.toException());
            }
        });
    }
}
