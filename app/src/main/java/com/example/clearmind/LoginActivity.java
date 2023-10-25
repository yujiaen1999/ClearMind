package com.example.clearmind;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.clearmind.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private DatabaseReference db;
    private Button login;

    private Button button_register;
    private Button button_forgot_password;

    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Enable full screen display and avoid nav bar overlap
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_login), (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Apply the insets as a margin to the view. This solution sets only the
            // bottom, left, and right dimensions, but you can apply whichever insets are
            // appropriate to your layout. You can also update the view padding if that's
            // more appropriate.
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
//            mlp.leftMargin = insets.left;
//            mlp.rightMargin = insets.right;
            mlp.bottomMargin = insets.bottom;
            v.setLayoutParams(mlp);

            // Return CONSUMED if you don't want want the window insets to keep passing
            // down to descendant views.
            return WindowInsetsCompat.CONSUMED;
        });

        this.usernameInput = (EditText)(findViewById(R.id.username_input));
        this.passwordInput = (EditText)(findViewById(R.id.password_input));
        this.db = FirebaseDatabase.getInstance().getReference(); //get a reference of database
        login = findViewById(R.id.entry_button);
        button_register = findViewById(R.id.button_register);
        button_forgot_password = findViewById(R.id.button_forgot_passward);

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openSignupActivity();
            }
        });

        button_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                username = usernameInput.getText().toString();
//                Toast.makeText(LoginActivity.this,  username, Toast.LENGTH_SHORT).show();
                if (username.isEmpty()){
                    Toast.makeText(LoginActivity.this,  "Please input username", Toast.LENGTH_SHORT).show();
                }
                else{
                    openChangePasswordActivity();
                }
            }
        });

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
                    boolean toMainPage = false;
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    Map<String, User> map = (Map<String, User>) task.getResult().getValue();
                    //Log.d("Login", map.toString());
                    //Log.d("Login", Integer.toString(map.size()));

                    User user = task.getResult().getValue(User.class);
//                    Log.d("LoginUser", user.toString());
                    if (username.isEmpty()){
                        Toast.makeText(LoginActivity.this,  "Please enter username", Toast.LENGTH_SHORT).show();
                    }
                    else if (user == null) {
                        // the username doesn't exist in db
                        // need to register
                        Toast.makeText(LoginActivity.this,  "New User, please register first", Toast.LENGTH_SHORT).show();
//                        register(username, password);
//                        toMainPage = true;
                    }
                    else
                    { //the username exists in db
                        //need to login
                        boolean verify = user.getPassword().equals(password);
//                        Toast.makeText(LoginActivity.this, "HERE: "+ verify, Toast.LENGTH_SHORT).show();
                        //check if the entered password == the password in db
                        if (!verify){
                            Toast.makeText(LoginActivity.this,  "Wrong Password", Toast.LENGTH_SHORT).show();
                        }
                        toMainPage = verify;
                        Log.d("LoginUser", user.toString());
                        Log.d("Login", user.getPassword());
                        Log.d("Login", password);
                        Log.d("Login", Boolean.toString(verify));
                        Log.d("Login", Boolean.toString(toMainPage));
                    }

                    if (toMainPage){
//                        Intent intent = new Intent(getApplicationContext(), LoginPageActivity.class);
                        Intent intent = new Intent(getApplicationContext(), LearnActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);  //go to main page (activity)
                    }

                }
            }
        });

//        ValueEventListener userListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                //User user = dataSnapshot.getValue(User.class);
//                Map<String, User> map = (Map<String, User>) dataSnapshot.getValue();
//                //Log.w("firebase", user.toString());
//                Log.w("firebase", map.toString());
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Getting Post failed, log a message
//                Log.w("firebase", "loadPost:onCancelled", databaseError.toException());
//            }
//        };
//        db.child("users").child(username).addValueEventListener(userListener);


        //db.child("users").child("user3").setValue(new User("name", "pswd", "id1"));


    }

    private void register(String username, String password){
        this.db.child("users").child(username).setValue(new User(username, password, "1"));
    }

    public void openSignupActivity(){
        Intent intent = new Intent(this,SignupActivity.class);
        startActivity(intent);
    }

    public void openChangePasswordActivity(){
        Intent intent = new Intent(this,ForgotPasswordActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }



    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected  void onPause() {

        super.onPause();
    }

    @Override
    protected  void onStop() {
        super.onStop();
    }

    @Override
    protected  void onRestart() {
        super.onRestart();
    }

    @Override
    protected  void onDestroy() {
        super.onDestroy();
    }



}
