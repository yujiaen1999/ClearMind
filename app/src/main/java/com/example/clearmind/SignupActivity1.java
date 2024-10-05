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

public class SignupActivity1 extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup1);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        this.usernameInput = findViewById(R.id.username_input);
        this.passwordInput = findViewById(R.id.password_input);
        this.db = FirebaseDatabase.getInstance().getReference(); //get a reference of database
        Button nextButton = findViewById(R.id.entry_button);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String username = usernameInput.getText().toString(); //get username & password
                String password = passwordInput.getText().toString();


                //DatabaseReference db = FirebaseDatabase.getInstance().getReference(); //get a reference of database
                db.child("users").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
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
                                Toast.makeText(SignupActivity1.this,  "Welcome New User ", Toast.LENGTH_SHORT).show();
                                toQuestionPage = true;
                            }
                            else
                            {
                                Toast.makeText(SignupActivity1.this,  "Existed username", Toast.LENGTH_SHORT).show();
                            }

                            if (toQuestionPage){
                                Intent intent = new Intent(getApplicationContext(), SignupActivity2.class);
                                intent.putExtra("username", username);
                                intent.putExtra("password", password);
                                startActivity(intent);  //go to next page (activity)
                            }

                        }
                    }
                });
            }
        });
    }
}
