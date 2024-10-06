package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {

    private DatabaseReference db;
    private EditText newPasswordInput;
    private EditText reEnterPasswordInput;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.newPasswordInput = (EditText)(findViewById(R.id.newPassword));
        this.reEnterPasswordInput = (EditText)(findViewById(R.id.reEnterPassword));
        this.db = FirebaseDatabase.getInstance().getReference(); //get a reference of database
    }

    public void onClickDone(View view){
        String newPassword = this.newPasswordInput.getText().toString();
        String reEnteredPassword = this.reEnterPasswordInput.getText().toString();

        if (newPassword.equals(reEnteredPassword)){
            Map<String, Object> update = new HashMap<>();
            update.put("password", newPassword);
            this.db.child("users").child(this.username)
                    .updateChildren(update);

            this.finish();
            openLoginActivity();
        }
        else{
            //passwords don't match
            Toast.makeText(ChangePasswordActivity.this, "passwords don't match", Toast.LENGTH_SHORT).show();
        }
//        this.finish();
//        openLoginActivity();
    }

    public void openLoginActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}