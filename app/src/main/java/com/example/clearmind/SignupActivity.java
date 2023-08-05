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
        this.db.child("users").child(username).setValue(new User(username, password, "1"));

        HashMap<String, Object> progress_map = new HashMap<>();
        progress_map.put("presurvey", "0");
        progress_map.put("chapter1", "0");
        progress_map.put("chapter2", "0");
        progress_map.put("chapter3", "0");
        progress_map.put("chapter4", "0");
        progress_map.put("postsurvey", "0");

        this.db.child("progress").child(username).setValue(progress_map);
    }

}
