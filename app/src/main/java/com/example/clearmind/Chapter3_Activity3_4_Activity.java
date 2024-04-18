package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Chapter3_Activity3_4_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;

    private Button button_back;
    private Button button_next;
    private Button button_home;
    private WebView webView;
    private TextView button_transcript;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter3_activity3_4);
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
                            "1. Sit in a comfortable position and close your eyes.<br>"+
                            "2. Imagine sitting beside a stream with leaves floating by. Pause for 10 seconds.<br>"+
                            "3. Notice the thoughts you're experiencing. Place each thought on a leaf and let it float away. Try not to hold on to it or push it down the stream further.<br>"+
                            "4. If you do not have any thoughts for a moment, just keep watching the stream. Another thought will come up soon. Wait 20 seconds.<br>"+
                            "5. Let the stream flow naturally. Don't rush your thoughts.<br>"+
                            "6. Negative thoughts like \"This is silly\" and \"This is boring\" go on leaves too. Wait 20 seconds.<br>"+
                            "7. If a leaf gets stuck, that’s okay. It will move when ready. If the thought comes up again, just watch it float by another time. Wait 20 seconds.<br>"+
                            "8. When experiencing challenging thoughts, simply acknowledge them. Say to yourself, “I notice that I’m feeling bored/upset/anxious,” and let them float on a leaf.<br>"+
                            "9. Getting distracted is normal. When you notice it, gently bring your attention back to the stream.";
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
        String video_4 = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/r1C8hwj5LXw?si=1sSYbDfG4lVaiwRs&amp;controls=0\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; encrypted-media; gyroscope; picture-in-picture\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>";
//        String breathing_video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/xDm2c5FDLNI?si=XEKPUTAFdo30xKlT\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>";
//        String embeded_video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/jt7nITE3qaM?si=WQ8I-GiyDrY5s8BT\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>";
//        String video_youtube = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/fHCemviY06Y?si=O_OUVoA42Bkgn5rh\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webView.loadData(video_4, "text/html", "utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new Chapter3_Activity3_4_Activity.MyChrome2());


        // Set the size of the youtube player based on the screen size
        ViewGroup.LayoutParams vc = webView.getLayoutParams();
        vc.height=width/16*9;
        vc.width=width;
        webView.setLayoutParams(vc);


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
                meditation_progress_update.put("meditation_3", "1");
                db.child("Chapter3").child("activity3").child(username).updateChildren(meditation_progress_update);

                open_Previous_Activity();
            }
        });

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // update meditation progress
                Map<String, Object> meditation_progress_update = new HashMap<>();
                meditation_progress_update.put("meditation_3", "1");
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

}
