package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SecureQuestionActivity extends AppCompatActivity {

//    private Spinner question1;

    private DatabaseReference db;

    private String username;
    private String password;
    private EditText answer1;
    private EditText answer2;
    private Button submit_button;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_securequestion);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.password = intent.getStringExtra("password");

        this.db = FirebaseDatabase.getInstance().getReference();

        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);

        submit_button = findViewById(R.id.submit_button);

        Spinner question1 = (Spinner) findViewById(R.id.spinner1);
        Spinner question2 = (Spinner) findViewById(R.id.spinner2);

//        String[] questions = {"question test 1", "question test 2", "question test 3"};

//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, questions);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.secure_questions_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        question1.setAdapter(adapter);
        question2.setAdapter(adapter);

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
//            public void onClick(View v){
//                Toast.makeText(SecureQuestionActivity.this,  question1.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(SecureQuestionActivity.this,  question2.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
//            }

            public void onClick(View view) {
                boolean toNextPage = false;
                String txt_question1 = question1.getSelectedItem().toString();
                String txt_question2 = question2.getSelectedItem().toString();

                String txt_answer1 = answer1.getText().toString();
                String txt_answer2 = answer2.getText().toString();

                if (txt_answer1.isEmpty() || txt_answer2.isEmpty()){
                    Toast.makeText(SecureQuestionActivity.this,  "Empty input", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, Object> security_questions = new HashMap<>();
                    security_questions.put("question1", txt_question1);
                    security_questions.put("answer1", txt_answer1);
                    security_questions.put("question2", txt_question2);
                    security_questions.put("answer2", txt_answer2);

//                    register(username, security_questions);
//                    Toast.makeText(SecureQuestionActivity.this,  "Set Security Questions successful", Toast.LENGTH_SHORT).show();
                    toNextPage = true;
                }

                if (toNextPage){
                    Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);
                    intent.putExtra("question1", txt_question1);
                    intent.putExtra("answer1", txt_answer1);
                    intent.putExtra("question2", txt_question2);
                    intent.putExtra("answer2", txt_answer2);
                    startActivity(intent);  //go to main page (activity)
                }
            }
        });

//        Toast.makeText(SecureQuestionActivity.this,  question1.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

//        question1.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

    }

    private void register(String username, HashMap<String, Object> map) {
        this.db.child("security").child(username).setValue(map);
    }
}
