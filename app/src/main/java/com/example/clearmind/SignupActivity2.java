package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity2 extends AppCompatActivity {

    private String username;
    private String password;
    private EditText fullName;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup2);

        this.fullName = findViewById(R.id.full_name_input);
        this.email = findViewById(R.id.email_input);
        Button nextButton = findViewById(R.id.entry_button);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.password = intent.getStringExtra("password");

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean toNextPage = false;
                String fullNameStr = fullName.getText().toString();
                String emailStr = email.getText().toString();

                if (fullNameStr.isEmpty() || emailStr.isEmpty()){
                    Toast.makeText(SignupActivity2.this,  "Empty input", Toast.LENGTH_SHORT).show();
                } else {
                    toNextPage = true;
                }

                if (toNextPage){
                    Intent intent1 = new Intent(getApplicationContext(), SignupActivity3.class);
                    intent1.putExtra("username", username);
                    intent1.putExtra("password", password);
                    intent1.putExtra("fullName", fullNameStr);
                    intent1.putExtra("email", emailStr);
                    startActivity(intent1);
                }
            }
        });
    }
}
