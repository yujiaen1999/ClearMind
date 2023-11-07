package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import com.example.clearmind.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private DatabaseReference db;
    private Button signup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_signup);
        this.usernameInput = (EditText)(findViewById(R.id.username_input));
        this.passwordInput = (EditText)(findViewById(R.id.password_input));
        this.db = FirebaseDatabase.getInstance().getReference(); //get a reference of database
        signup = findViewById(R.id.entry_button);
    }

    public void openMain(View view){
        boolean toMainPage = false;
        String username = usernameInput.getText().toString(); //get username & password
        String password = passwordInput.getText().toString();


        //DatabaseReference db = FirebaseDatabase.getInstance().getReference(); //get a reference of database
        this.db.child("users").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    //error, we might want to prompt a toast
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    boolean toQuestionPage = false;
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    Map<String, User> map = (Map<String, User>) task.getResult().getValue();
                    //Log.d("Login", map.toString());
                    //Log.d("Login", Integer.toString(map.size()));

                    User user = task.getResult().getValue(User.class);
//                    Log.d("LoginUser", user.toString());
                    if (user == null){
                        // the username doesn't exist in db
                        // need to register
                        Toast.makeText(SignupActivity.this,  "Welcome New User ", Toast.LENGTH_SHORT).show();
                        register(username, password);
                        toQuestionPage = true;
                    }
                    else
                    { //the username exists in db
                        Toast.makeText(SignupActivity.this,  "Existed username", Toast.LENGTH_SHORT).show();
                    }

                    if (toQuestionPage){
                        Intent intent = new Intent(getApplicationContext(), SecureQuestionActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);  //go to main page (activity)
                    }

                }
            }
        });
    }

    private void register(String username, String password){
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


    }

}
