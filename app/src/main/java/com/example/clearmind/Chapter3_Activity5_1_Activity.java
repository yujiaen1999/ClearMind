package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Chapter3_Activity5_1_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;

    private Button button_back;
    private Button button_next;
    private Button button_home;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter3_activity5_1);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

        button_home = findViewById(R.id.button_home);
        button_back = findViewById(R.id.button_previous);
        button_next = findViewById(R.id.button_next);

        // Set text format
//        TextView textView = findViewById(R.id.textView2);
//        String txt_sum =
//                "Now that we've covered the basics of the Stop Breathe Believe practice, let's connect these activities to the reason why we do them." +
//                        "<br><br><b>Stop: </b>" +
//                        "This exercise is designed to help you redirect your attention to the thoughts in your mind. By becoming more aware of the thoughts in your mind and allowing those feelings to run through your mind without judgment, you are ready to move on to the next step: Breath." +
//                        "<br><br><b>Breathe:</b>" +
//                        "This exercise is designed to help you direct your awareness to your breath's natural rhythm and allow it to flow in and out. It also helps your muscles to relax and address any discomfort or tension you may be experiencing." +
//                        "<br><br><b>Belief:</b>" +
//                        "You can use your belief statement like a lighthouse during tough times. You can also connect your belief statement with your breathing, making it a part of your new, positive mindset with each breath. For instance, when you breathe in, think, “I am not defined by my challenges,” and when you breathe out, think, “I am learning new ways to improve."
//                ;
//        textView.setText(Html.fromHtml(txt_sum));

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
                // update Chapter progress
//                Map<String, Object> chapter_progress_update = new HashMap<>();
//                chapter_progress_update.put("5_Activity3_4", "1");
//                db.child("Chapter3").child("progress").child(username).updateChildren(chapter_progress_update);

                open_Next_Activity();
//                openPopupWindow(v);
            }
        });

    }

    private void open_Previous_Activity() {
        Intent intent = new Intent(this,Chapter3_Activity4_sum_Activity.class);
        intent.putExtra("username", username);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void open_Next_Activity() {
        Intent intent = new Intent(this,Chapter3_Activity5_2_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter3() {
        Intent intent = new Intent(this,Chapter3_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
