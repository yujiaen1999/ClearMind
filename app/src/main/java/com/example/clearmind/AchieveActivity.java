package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AchieveActivity extends AppCompatActivity {
    private Button learn_button;
    private Button save_button;
//    private Button achieve_button;
    private Button profile_button;

    private String username;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achieve);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        Toast.makeText(AchieveActivity.this,  username, Toast.LENGTH_SHORT).show();

        learn_button = (Button) findViewById(R.id.button_learn);
        save_button = (Button) findViewById(R.id.button_save);
//        achieve_button = (Button) findViewById(R.id.button_achieve);
        profile_button = (Button) findViewById(R.id.button_profile);

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

//        achieve_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v){
//                openAchieveActivity();
//            }
//        });

        profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openProfileActivity();
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

//    public void openAchieveActivity(){
//        Intent intent = new Intent(this,AchieveActivity.class);
//        startActivity(intent);
//    }

    public void openProfileActivity(){
        Intent intent = new Intent(this,ProfileActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
