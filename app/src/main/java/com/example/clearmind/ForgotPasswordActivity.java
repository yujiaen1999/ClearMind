package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ForgotPasswordActivity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;

    private String question1;
    private String question2;
    private String answer1;
    private String answer2;

    private EditText answerInput1;
    private EditText answerInput2;

    private TextView show_question1;
    private TextView show_question2;

    private Button submit;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");

        this.db = FirebaseDatabase.getInstance().getReference();

        show_question1 = findViewById(R.id.question1_show);
        show_question2 = findViewById(R.id.question2_show);

        answerInput1 = findViewById(R.id.answer1);
        answerInput2 = findViewById(R.id.answer2);

        submit = findViewById(R.id.submit_button);

        System.out.println("In setting security questions");
        getData();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean toNextPage = false;
//                String txt_username = username.getText().toString();
//                String txt_name = name.getText().toString();
//                String txt_email = email.getText().toString();
                String txt_answerInput1 = answerInput1.getText().toString();
                String txt_answerInput2 = answerInput2.getText().toString();

                if (txt_answerInput1.isEmpty() || txt_answerInput2.isEmpty()){
                    Toast.makeText(ForgotPasswordActivity.this,  "Empty input", Toast.LENGTH_SHORT).show();
                } else {
                    if (txt_answerInput1.equals(answer1) && txt_answerInput2.equals(answer2)){
                        Toast.makeText(ForgotPasswordActivity.this,  "Corret Answer", Toast.LENGTH_SHORT).show();
                        toNextPage = true;
                    }
                    else {
                        Toast.makeText(ForgotPasswordActivity.this,  "Wrong Answer", Toast.LENGTH_SHORT).show();
                        Toast.makeText(ForgotPasswordActivity.this,  "If you can not find your password back, please contact us: clearmind@ucsd.edu", Toast.LENGTH_SHORT).show();
                    }
//                    toMainPage = true;
                }

                if (toNextPage){
                    Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
                    intent.putExtra("username", username);
                    startActivity(intent);  //go to main page (activity)
                }
            }
        });
    }

    public void getData() {

        // calling add value event listener method
        // for getting the values from database.
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("In setting security questions");
                question1 = String.valueOf(snapshot.child("security").child(username).child("question1").getValue());
                question2 = String.valueOf(snapshot.child("security").child(username).child("question2").getValue());

                answer1 = String.valueOf(snapshot.child("security").child(username).child("answer1").getValue());
                answer2 = String.valueOf(snapshot.child("security").child(username).child("answer2").getValue());

//                Toast.makeText(ProfileActivity.this, email, Toast.LENGTH_SHORT).show();
                Log.i("FP", question1);
                Log.i("FP", question2);
                show_question1.setText(question1);
                show_question2.setText(question2);
//                show_email.setText("email: " + email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(ForgotPasswordActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
