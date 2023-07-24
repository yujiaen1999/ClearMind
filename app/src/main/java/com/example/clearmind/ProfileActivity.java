package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    private Button learn_button;
    private Button save_button;
    private Button achieve_button;
//    private Button profile_button;

    private Button button_to_setting;

    private Button logout_button;
    private TextView show_name;
    private TextView show_username;
    private TextView show_email;

    private String username;

    private DatabaseReference db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
//        Toast.makeText(ProfileActivity.this,  username, Toast.LENGTH_SHORT).show();

        this.db = FirebaseDatabase.getInstance().getReference();

        learn_button = (Button) findViewById(R.id.button_learn);
        save_button = (Button) findViewById(R.id.button_save);
        achieve_button = (Button) findViewById(R.id.button_achieve);
//        profile_button = (Button) findViewById(R.id.button_profile);

        button_to_setting = (Button) findViewById(R.id.setting_button);
        logout_button = (Button) findViewById(R.id.logout_button);


        show_name = findViewById(R.id.name_show);
        show_username = findViewById(R.id.username_show);
        show_email = findViewById(R.id.email_show);
//        show_name.setText("Name: " + username);

        getdata();

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openMainActivity();
            }
        });

        button_to_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                OpenSetting();
            }
        });

        learn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openLearnActivity();
            }
        });

        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openSaveActivity();
            }
        });

        achieve_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openAchieveActivity();
            }
        });

//        profile_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v){
//                openProfileActivity();
//            }
//        });

    }

    public void getdata() {

        // calling add value event listener method
        // for getting the values from database.
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = String.valueOf(snapshot.child("register").child(username).child("name").getValue());
                String email = String.valueOf(snapshot.child("register").child(username).child("email").getValue());
//                Toast.makeText(ProfileActivity.this, email, Toast.LENGTH_SHORT).show();

                show_name.setText(name);
                show_username.setText("username: " + username);
                show_email.setText("email: " + email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(ProfileActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openLearnActivity(){
        Intent intent = new Intent(this,LearnActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void openSaveActivity(){
        Intent intent = new Intent(this,SaveActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void openAchieveActivity(){
        Intent intent = new Intent(this,AchieveActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

//    public void openProfileActivity(){
//        Intent intent = new Intent(this,ProfileActivity.class);
//        startActivity(intent);
//    }

    public void OpenSetting(){
        Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
        intent.putExtra("username", this.username);
        startActivity(intent);
    }

    public void openMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}