package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

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

        // Enable full screen display and avoid nav bar overlap
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_achieve), (v, windowInsets) -> {
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

        this.username = intent.getStringExtra("username");
//        Toast.makeText(AchieveActivity.this,  username, Toast.LENGTH_SHORT).show();

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
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    public void openSaveActivity(){
        Intent intent = new Intent(this,SaveActivity.class);
        intent.putExtra("username", username);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

//    public void openAchieveActivity(){
//        Intent intent = new Intent(this,AchieveActivity.class);
//        startActivity(intent);
//    }

    public void openProfileActivity(){
        Intent intent = new Intent(this,ProfileActivity.class);
        intent.putExtra("username", username);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}
