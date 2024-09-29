package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// *****************************
// Not In Use !!!!!!!
// *****************************
public class Chapter3_Activity4_breathe_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;

    private Button button_back;
    private Button button_next;
    private ImageButton button_home;

    private final NavigationDrawerHelper navigationDrawerHelper = new NavigationDrawerHelper(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter3_activity4_breathe);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

        button_home = findViewById(R.id.button_home);
        button_back = findViewById(R.id.button_previous);
        button_next = findViewById(R.id.button_next);

        // Set text format
        TextView textView = findViewById(R.id.textView2);
        String txt_breathe = "<b>Breathe:</b>" +
                "<br>Now, if possible, adjust your sitting position. Sit up straight to fully use your lung’s capacity. Uncross your legs and rest your feet flat on the floor. Take slow breaths in and out. You can also try the 20 Breaths Meditation during this step if you like." +
                "<br>Once you feel calm and focused, you can move on to Believe.";
        textView.setText(Html.fromHtml(txt_breathe));

        navigationDrawerHelper.setupNavigationDrawer(username);

        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                open_Chapter3();
            }
        });

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                open_Previous_Activity();
            }
        });

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // update Chapter1 progress
//                Map<String, Object> chapter1_progress_update = new HashMap<>();
//                chapter1_progress_update.put("3_Passengers_On_The_Bus", "1");
//                db.child("Chapter2").child("progress").child(username).updateChildren(chapter1_progress_update);

                open_Next_Activity();
//                openPopupWindow(v);
            }
        });

    }

    private void open_Previous_Activity() {
        Intent intent = new Intent(this,Chapter3_Activity4_stop_Activity.class);
        intent.putExtra("username", username);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void open_Next_Activity() {
        Intent intent = new Intent(this,Chapter3_Activity4_believe_Activity.class);
        intent.putExtra("username", username);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void open_Chapter3() {
        Intent intent = new Intent(this,Chapter3_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
