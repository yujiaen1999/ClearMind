package com.example.clearmind;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {

//    private DatabaseReference db;
//    private String username;  //TODO: needs to save the data before enter next activity
    private Button button_to_login;
    private Button button_to_register;

    private Button button_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.w("MainActivity", "onCreate");
//        Intent intent = getIntent();
//        this.username = intent.getStringExtra("username");
//        this.db = FirebaseDatabase.getInstance().getReference(); //get a reference of database
//        if (savedInstanceState != null){
//            this.username = savedInstanceState.getString("username");
//            Log.d("save_username", "onRestoreCreate");
//        }
        button_to_login = (Button) findViewById(R.id.button);
        button_to_register = (Button) findViewById(R.id.button2);

        button_test = (Button) findViewById(R.id.button3);

        button_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openLoginActivity();
            }
        });

        // click register button
        button_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openRegisterActivity();
            }
        });

        button_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                OpenTest();
            }
        });
    }

    public void openLoginActivity(){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

    public void openRegisterActivity(){
//        Intent intent = new Intent(this,RegisterActivity.class);
        Intent intent = new Intent(this,SignupActivity.class);
        startActivity(intent);
    }


    public void OpenTest(){
        Intent intent = new Intent(this, PreSurveyActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w("MainActivity", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "onResume");
    }

    @Override
    protected  void onPause() {

        super.onPause();
        Log.w("MainActivity", "onPause");
    }

    @Override
    protected  void onStop() {
        super.onStop();
        Log.w("MainActivity", "onStop");
    }

    @Override
    protected  void onRestart() {
        super.onRestart();
        Log.w("MainActivity", "onRestart");
    }

    @Override
    protected  void onDestroy() {
        super.onDestroy();
        Log.w("MainActivity", "onDestroy");
    }

//    //for future use
//    public void onClickDeleteAccount(View view){
//        Log.d("save_username", Boolean.toString(this.username == null));
//        this.db.child("users")
//                .child(this.username)
//                .removeValue();
//        this.finish();
//    }
//
//    //for future use
//    public void onClickChangePswd(View view){
//        Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
//        intent.putExtra("username", this.username);
//        startActivity(intent);
//    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle savedInstanceState) {
//        Log.w("MainActivity", "onSave");
//        savedInstanceState.putString("username", this.username);
//        super.onSaveInstanceState(savedInstanceState);
//    }
//
//    @Override
//    public void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        Log.w("MainActivity", "onRestore");
//        this.username = savedInstanceState.getString("username");
//    }
}