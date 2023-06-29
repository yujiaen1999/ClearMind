package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingActivity extends AppCompatActivity {

    private DatabaseReference db;
    private String username;
    private Button change_pswd_button;

    private Button logout_button;

//    private Button delete_account_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Intent intent = getIntent();
        change_pswd_button = (Button) findViewById(R.id.change_pswd);
        logout_button = (Button) findViewById(R.id.logout);
//        delete_account_button = (Button) findViewById(R.id.delete_account);

        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference(); //get a reference of database
        if (savedInstanceState != null){
            this.username = savedInstanceState.getString("username");
            Log.d("save_username", "onRestoreCreate");
        }

        change_pswd_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                onClickChangePswd();
            }
        });

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openMainActivity();
            }
        });
    }

    public void onClickChangePswd(){
        Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class);
        intent.putExtra("username", this.username);
        startActivity(intent);
    }

    public void onClickDeleteAccount(View view){
        Log.d("save_username", Boolean.toString(this.username == null));
        this.db.child("users")
                .child(this.username)
                .removeValue();
        this.db.child("register")
                .child(this.username)
                .removeValue();
        this.finish();
        Toast.makeText(SettingActivity.this,  "Account Deleted", Toast.LENGTH_SHORT).show();
        openMainActivity();
    }

    public void openMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

}
