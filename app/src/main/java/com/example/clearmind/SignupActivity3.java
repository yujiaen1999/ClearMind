package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity3 extends AppCompatActivity {

    private String username;
    private String password;
    private String fullName;
    private String email;
    private EditText answer1;
    private EditText answer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup3);

        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.password = intent.getStringExtra("password");
        this.fullName = intent.getStringExtra("fullName");
        this.email = intent.getStringExtra("email");

        Button nextButton = findViewById(R.id.entry_button);
        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);

        Spinner question1 = findViewById(R.id.spinner1);
        Spinner question2 = findViewById(R.id.spinner2);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.secure_questions_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        question1.setAdapter(adapter);
        question2.setAdapter(adapter);

        nextButton.setOnClickListener(view -> {
            boolean toNextPage = false;
            String txt_question1 = question1.getSelectedItem().toString();
            String txt_question2 = question2.getSelectedItem().toString();

            String txt_answer1 = answer1.getText().toString();
            String txt_answer2 = answer2.getText().toString();

            if (txt_answer1.isEmpty() || txt_answer2.isEmpty()){
                Toast.makeText(SignupActivity3.this,  "Empty input", Toast.LENGTH_SHORT).show();
            } else {
                toNextPage = true;
            }

            if (toNextPage){
                Intent intent1 = new Intent(getApplicationContext(), SignupActivity4.class);
                intent1.putExtra("username", username);
                intent1.putExtra("password", password);
                intent1.putExtra("fullName", fullName);
                intent1.putExtra("email", email);
                intent1.putExtra("question1", txt_question1);
                intent1.putExtra("answer1", txt_answer1);
                intent1.putExtra("question2", txt_question2);
                intent1.putExtra("answer2", txt_answer2);
                startActivity(intent1);  //go to main page (activity)
            }
        });
    }
}
