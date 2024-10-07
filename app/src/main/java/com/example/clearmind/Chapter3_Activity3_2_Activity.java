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
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Chapter3_Activity3_2_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;

    private Button button_back;
    private Button button_next;
    private ImageButton button_home;
    private WebView webView;
    private TextView button_transcript;
    private long pageOpenTime;
    private long pageCloseTime;

    private final NavigationDrawerHelper navigationDrawerHelper = new NavigationDrawerHelper(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter3_activity3_2);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

        button_home = findViewById(R.id.button_home);
        button_back = findViewById(R.id.button_previous);
        button_next = findViewById(R.id.button_next);

        button_next.setVisibility(View.GONE);

        // enable video transcript button
        button_transcript = findViewById(R.id.transcript);
        final Integer[] flag = {0};

        button_transcript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
//                openHints(v);

                if (flag[0] == 0){
                    String txt_hints = "<p style=\"text-align: center\"><b>Can’t play the video?</b></p>\n" +
                            "Here’s how to do it:<br><br>" +
                            "1. Sit straight and relax your shoulders.<br>"+
                            "2. Place your tongue behind your upper front teeth and keep it there.<br>"+
                            "3. Breathe in through your nose for 4 seconds.<br>"+
                            "4. Hold your breath for 7 seconds.<br>"+
                            "5. Exhale from your mouth for 8 seconds, making a whoosh sound.<br><br>"+
                            "That's one round. Repeat this 3 to 4 times." +
                            "<br><br>" +
                            "Remember, the key point is to keep the counts of 4, 7, and 8. If holding your breath is hard, go faster but keep the same pattern. Use this anytime you feel stressed to help calm down quickly.";
                    button_transcript.setText(Html.fromHtml(txt_hints));
                    flag[0] = 1;
                } else{
                    button_transcript.setText("Can’t play the video?");
                    flag[0] = 0;
                }
            }
        });

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


        // Set up Youtube player
        webView = findViewById(R.id.webView);
        String breathing_video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/TlZa9dBK2LY?si=7-pOLZo2XixzM-VT&amp;controls=0\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>";
//        String embeded_video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/jt7nITE3qaM?si=WQ8I-GiyDrY5s8BT\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>";
//        String video_youtube = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/fHCemviY06Y?si=O_OUVoA42Bkgn5rh\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webView.loadData(breathing_video, "text/html", "utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new Chapter3_Activity3_2_Activity.MyChrome2());


        // Set the size of the youtube player based on the screen size
        ViewGroup.LayoutParams vc = webView.getLayoutParams();
        vc.height=width/16*9;
        vc.width=width;
        webView.setLayoutParams(vc);

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
                meditation_progress_update.put("meditation_1", "1");
                db.child("Chapter3").child("activity3").child(username).updateChildren(meditation_progress_update);

                open_Previous_Activity();
            }
        });

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // update meditation progress
                Map<String, Object> meditation_progress_update = new HashMap<>();
                meditation_progress_update.put("meditation_1", "1");
                db.child("Chapter3").child("activity3").child(username).updateChildren(meditation_progress_update);

                open_Next_Activity();
            }
        });
    }

    private class MyChrome2 extends WebChromeClient {
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        MyChrome2() {}


        public void onHideCustomView()
        {
            ((FrameLayout)getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback)
        {
            if (this.mCustomView != null)
            {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout)getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
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
        DatabaseReference activityRef = db.child("userActivity").child(username).child("Part3_3_Activity3_Meditation2");

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
