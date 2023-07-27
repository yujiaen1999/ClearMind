package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseReference db;

    private EditText username;
    private EditText name;
    private EditText email;

    private EditText answer4;

    private EditText answer5;
    private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        this.usernameInput = (EditText)(findViewById(R.id.username_input));
//        this.passwordInput = (EditText)(findViewById(R.id.password_input));
//        this.db = FirebaseDatabase.getInstance().getReference(); //get a reference of database
//        login = findViewById(R.id.entry_button);

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


                if (txt_username.isEmpty() || txt_name.isEmpty() || txt_email.isEmpty() || txt_answer4.isEmpty() || txt_answer5.isEmpty()){
                    Toast.makeText(RegisterActivity.this,  "Empty input", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("username", txt_username);
                    map.put("name", txt_name);
                    map.put("email", txt_email);
                    map.put("Question4", txt_answer4);
                    map.put("Question5", txt_answer5);

                    register(txt_username, map);
                    Toast.makeText(RegisterActivity.this,  "Register successful", Toast.LENGTH_SHORT).show();
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
