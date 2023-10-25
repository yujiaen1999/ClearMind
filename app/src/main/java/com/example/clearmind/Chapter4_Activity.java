package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Chapter4_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;
    private ImageButton button_back_to_learn;

    private Button button_opening;
    private Button button_activity1_1;
    private Button button_activity1_2;
    private Button button_activity2;
    private Button button_activity3;
    private Button button_activity4;
    private Button button_activity5;
    private Button button_summary;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter4);
        Intent intent = getIntent();

        // Enable full screen display and avoid nav bar overlap
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_chapter4), (v, windowInsets) -> {
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
        this.db = FirebaseDatabase.getInstance().getReference();

        button_back_to_learn = findViewById(R.id.button_back_learn_page);

        button_opening = findViewById(R.id.button_1);
        button_activity1_1 = findViewById(R.id.button2);
        button_activity1_2 = findViewById(R.id.button3);
        button_activity2 = findViewById(R.id.button4);
        button_activity3 = findViewById(R.id.button5);
        button_activity4 = findViewById(R.id.button6);
        button_activity5 = findViewById(R.id.button7);
//        button_activity6 = findViewById(R.id.button8);
        button_summary = findViewById(R.id.button8);

//        HashMap<String, Object> progress_map = new HashMap<>();
//        progress_map.put("1_Opening", "0");
//        progress_map.put("2_Activity4_1_1", "0");
//        progress_map.put("3_Activity4_1_2", "0");
//        progress_map.put("4_Activity4_2", "0");
//        progress_map.put("5_Activity4_3", "0");
//        progress_map.put("6_Activity4_4", "0");
//        progress_map.put("7_Activity4_5", "0");
//        progress_map.put("9_Summary", "0");
//        this.db.child("Chapter4").child("progress").child(username).setValue(progress_map);

//        getData();

        button_back_to_learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openLearnActivity();
            }
        });

    }

    private void openLearnActivity() {
        Intent intent = new Intent(this,LearnActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

//    private void open_Chapter4_Opening() {
//        Intent intent = new Intent(this,Chapter4_Opening_Activity.class);
//        intent.putExtra("username", username);
//        startActivity(intent);
//    }
}
