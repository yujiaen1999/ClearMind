package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Chapter3_Activity3_3_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;

    private Button button_back;
    private Button button_next;
    private ImageButton button_home;
    private TextView button_transcript;
    private long pageOpenTime;
    private long pageCloseTime;

    private final NavigationDrawerHelper navigationDrawerHelper = new NavigationDrawerHelper(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter3_activity3_3);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

        button_home = findViewById(R.id.button_home);
        button_back = findViewById(R.id.button_previous);
        button_next = findViewById(R.id.button_next);

        button_next.setVisibility(View.GONE);

        TextView textView2 = findViewById(R.id.textView2);
        TextView textView3 = findViewById(R.id.textView3);
//        TextView textView5 = findViewById(R.id.textView5);
//        TextView textView6 = findViewById(R.id.textView6);

        textView2.setText(Html.fromHtml("Mindfulness is more than just sitting still to meditate. You can practice mindfulness in <b><u>any</u></b> activity you do: as long as you perform the activity with a full, present engagement, the activity becomes a meditation session."));
        textView3.setText(Html.fromHtml("<b>During the walk you can:</b><br> Breathe in and start to walk slowly, focusing closely on the bottom of your feet. Each step offers an opportunity to reconnect with both yourself and the earth. As you walk, be mindful of every breath you take and count your steps as you inhale and exhale. Allow your breathing to flow naturally without trying to control it. Maintain your focus on the pace of your breathing and your steps. Remember, the key is counting."));
//        textView5.setText(Html.fromHtml("<b>Wash the Dishes:</b><br> Feel the water's temperature and texture on your hands, hear the dishes clinking, and notice how you move while scrubbing and rinsing. Allow yourself to enjoy making the dishes clean again. If your mind starts to wander, it's completely fine. Just acknowledge it and gently guide your focus back to washing. Focus all your attention on what you're doing."));
//        textView6.setText(Html.fromHtml("<b>Workout:</b><br> Pay attention to your body's sensations—observe your muscles, movement speed, breathing, and any tension or resistance. Feel the stretch and intensity. Simply notice your emotions without judgment. Do you feel accomplished? Are you being too harsh on yourself, or comparing your body shape with others?"));

        // Set scroll view start position
        ScrollView sv = (ScrollView) findViewById(R.id.scrollView);
        sv.post(new Runnable() {
            @Override
            public void run() {
                //setting position here :
                sv.scrollTo(0, 0);  // without animation
//                sv.smoothScrollTo(0,900);  // with animation
            }
        });

        // get screen size
        WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        LinearLayout layout_wash = findViewById(R.id.wash_dishes);
        LinearLayout layout_workout = findViewById(R.id.workout);
        TextView textView_wash =findViewById(R.id.wash_content);
        TextView textView_workout = findViewById(R.id.workout_content);

        final Integer[] flag = {0,0};
        layout_wash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (flag[0] == 0){
                    textView_wash.setVisibility(View.VISIBLE);
                    flag[0] = 1;
                } else{
                    textView_wash.setVisibility(View.GONE);
                    flag[0] = 0;
                }
            }
        });

        layout_workout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if (flag[1] == 0){
                    textView_workout.setVisibility(View.VISIBLE);
                    flag[1] = 1;
                } else{
                    textView_workout.setVisibility(View.GONE);
                    flag[1] = 0;
                }
            }
        });

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
                // update meditation progress
                Map<String, Object> meditation_progress_update = new HashMap<>();
                meditation_progress_update.put("meditation_2", "1");
                db.child("Chapter3").child("activity3").child(username).updateChildren(meditation_progress_update);

                open_Previous_Activity();
            }
        });

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // update meditation progress
                Map<String, Object> meditation_progress_update = new HashMap<>();
                meditation_progress_update.put("meditation_2", "1");
                db.child("Chapter3").child("activity3").child(username).updateChildren(meditation_progress_update);

                open_Next_Activity();
            }
        });
    }

    private void open_Previous_Activity() {
        Intent intent = new Intent(this,Chapter3_Activity3_new_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Next_Activity() {
        Intent intent = new Intent(this,Chapter3_Activity3_new_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter3() {
        Intent intent = new Intent(this,Chapter3_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        pageOpenTime = System.currentTimeMillis(); // get the page open time
    }

    @Override
    protected void onPause() {
        super.onPause();
        pageCloseTime = System.currentTimeMillis(); // get the page close time

        if (pageCloseTime - pageOpenTime > 1999){   // Only if the view time >= 2 seconds
            sendTimeStampsToFirebase(); // store the Time Stamp to Firebase
        }
//        sendTimeStampsToFirebase();
    }

    private void sendTimeStampsToFirebase() {
        DatabaseReference activityRef = db.child("userActivity").child(username).child("Part3_3_Activity3_Meditation3");

        // create a new activityID
        String activityId = activityRef.push().getKey();

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");

        Date resultdate_open = new Date(pageOpenTime);
        Date resultdate_close = new Date(pageCloseTime);

        Map<String, Object> activityData = new HashMap<>();
        activityData.put("openTime_ms", pageOpenTime);
        activityData.put("closeTime_ms", pageCloseTime);
        activityData.put("duration", pageCloseTime - pageOpenTime);

        activityData.put("openTime_str", String.valueOf(resultdate_open));
        activityData.put("closeTime_str", String.valueOf(resultdate_close));

        if (activityId != null) {
            activityRef.child(activityId).setValue(activityData)
                    .addOnSuccessListener(aVoid -> Log.d("Firebase", "Activity time recorded successfully"))
                    .addOnFailureListener(e -> Log.d("Firebase", "Failed to record activity time", e));
        }
    }
}
