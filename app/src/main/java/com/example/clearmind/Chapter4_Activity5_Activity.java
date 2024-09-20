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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Chapter4_Activity5_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;

    private Button button_back;
    private Button button_next;
    private Button button_home;

    private WebView webView;
    private TextView button_transcript;

    private long pageOpenTime;
    private long pageCloseTime;

    private final NavigationDrawerHelper navigationDrawerHelper = new NavigationDrawerHelper(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter4_activity5);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

        button_home = findViewById(R.id.button_home);
        button_back = findViewById(R.id.button_previous);
        button_next = findViewById(R.id.button_next);

        // enable video transcript button
        button_transcript = findViewById(R.id.transcript);
        final Integer[] flag = {0};

        button_transcript.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
//                openHints(v);

                if (flag[0] == 0){
                    String txt_hints = "<p style=\"text-align: center\"><b>Can’t play the video?</b></p>\n" +
                            "Our app's tracker feature is designed to support your goal achievement. Utilizing the information gathered earlier, we've established a weekly goal spanning 7 days for you. It's essential to update your progress daily and click on the <b>self-check-in button</b> once you've completed your daily goal. This allows you to record your progress and difficult emotions, as well as reflect on any cognitive strategies you’ve used to overcome procrastination.<br><br>"+
                            "You will then participate in the procrastination scale assessment to evaluate your procrastination tendencies for each day. Following the assessment, you'll be directed to the main page of our goal tracker feature.<br><br>"+
                            "At the top of the screen, you'll find the timeliness overview for the current week. Successfully checking in for your goal will turn your daily update sticker green with a check mark. If you don't have a daily goal, the sticker will remain gray. In the event that you forget to check in for a day, the sticker will turn red with an exclamation mark. Your weekly goal completion will be displayed below.<br><br>"+
                            "Don't worry if you miss a day; you have the flexibility to check in late. Our app is designed to accommodate occasional delays, ensuring you can maintain engagement with your goals. Once you complete your weekly goal, your weekly average score will be calculated and displayed at the bottom of the screen. This feature provides more insights into your historial timeliness and procrastination management skills development over time.<br><br>"+
                            "If you don't have a specific goal for a particular day, you are still welcome to engage with our app. In moments of anxiety or temptation to procrastinate, don't hesitate to revisit our course materials for guidance.";
                    button_transcript.setText(Html.fromHtml(txt_hints));
                    flag[0] = 1;
                } else{
                    button_transcript.setText("Can’t play the video?");
                    flag[0] = 0;
                }
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
        String video_4 = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/LqdDLGPipF4?si=lsN_aM_GJzdNOP-g\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; encrypted-media; gyroscope; picture-in-picture\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
//        String breathing_video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/xDm2c5FDLNI?si=XEKPUTAFdo30xKlT\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>";
//        String embeded_video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/jt7nITE3qaM?si=WQ8I-GiyDrY5s8BT\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>";
//        String video_youtube = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/fHCemviY06Y?si=O_OUVoA42Bkgn5rh\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webView.loadData(video_4, "text/html", "utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new Chapter4_Activity5_Activity.MyChrome2());


        // Set the size of the youtube player based on the screen size
        ViewGroup.LayoutParams vc = webView.getLayoutParams();
        vc.height=width/9*16;
        vc.width=width;
        webView.setLayoutParams(vc);

        navigationDrawerHelper.setupNavigationDrawer(username);

        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                open_Chapter4();
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
                Map<String, Object> chapter_progress_update = new HashMap<>();
                chapter_progress_update.put("7_Activity4_5_intro_tracker", "1");
                db.child("Chapter4").child("progress").child(username).updateChildren(chapter_progress_update);

                open_Next_Activity();
            }
        });

    }

    private void open_Previous_Activity() {
        Intent intent = new Intent(this,Chapter4_Summary_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Next_Activity() {
        Intent intent = new Intent(this,SaveActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter4() {
        Intent intent = new Intent(this,Chapter4_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
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
        DatabaseReference activityRef = db.child("userActivity").child(username).child("Part4_6_Activity5");

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
