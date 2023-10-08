package com.example.clearmind;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Chapter3_Activity3_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;

    private Button button_back;
    private Button button_next;
    private Button button_home;

//    private VideoView videoView;
    private WebView webView;

    private TextView button_transcript;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter3_activity3);
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
                            "Focus on your breath. Without changing how you breathe, become aware of all the sensations that come with breathing. Start counting your exhales using your inner voice. As you exhale for the first time, say \"one\" silently. Continue this pattern with the next exhale, saying \"two.\" Keep up until you've counted ten full breath cycles. After that, count another ten cycles, but this time go in reverse order, starting from ten and counting down to one." +
                            "<br><br>" +
                            "Give each breath your full attention. Observe the subtle differences in the sensations of your breath. Remember, you're not changing your breath; you're simply using it as a focal point to keep your mind anchored." +
                            "<br><br>" +
                            "After completing a cycle of 20 breaths, you can take a few extra moments to rest while observing the rhythm of your breath flowing in and out. If you’d like, you can do another round of 20 breaths.";
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
        String video_youtube = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/fHCemviY06Y?si=O_OUVoA42Bkgn5rh\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webView.loadData(video_youtube, "text/html", "utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebChromeClient(new MyChrome2());


        // Set the size of the youtube player based on the screen size
        ViewGroup.LayoutParams vc = webView.getLayoutParams();
        vc.height=width/16*9;
        vc.width=width;
        webView.setLayoutParams(vc);


//        // set up the video from local storage
//        videoView = findViewById(R.id.videoview);
//        String videoUrl = "android.resource://" + getPackageName() + "/" + R.raw.test_video;
//        Uri uri = Uri.parse(videoUrl);
//
////        videoView.setVideoPath("/res/raw/test.mp4");
//        videoView.setVideoURI(uri);
//        MediaController mediaController = new MediaController(this);
//        mediaController.setAnchorView(videoView);
//        // sets the media player to the videoView
//        mediaController.setMediaPlayer(videoView);
//        // sets the media controller to the videoView
//        videoView.setMediaController(mediaController);
//        // starts the video
////        videoView.start();


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
                Map<String, Object> chapter_progress_update = new HashMap<>();
                chapter_progress_update.put("4_Activity3_3", "1");
                db.child("Chapter3").child("progress").child(username).updateChildren(chapter_progress_update);

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

//        public Bitmap getDefaultVideoPoster()
//        {
//            if (mCustomView == null) {
//                return null;
//            }
//            return BitmapFactory.decodeResource(getApplicationContext().getResources(), 2130837573);
//        }

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


    private class MyChrome extends WebChromeClient
    {
        View fullscreen = null;

        @Override
        public void onHideCustomView()
        {
            fullscreen.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
        }
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback)
        {
            view.requestFocus();
            webView.setVisibility(View.GONE);

            if(fullscreen != null)
            {
                ((FrameLayout)getWindow().getDecorView()).removeView(fullscreen);
            }

            fullscreen = view;
            ((FrameLayout)getWindow().getDecorView()).addView(fullscreen, new FrameLayout.LayoutParams(-1, -1));
            fullscreen.setVisibility(View.VISIBLE);

//            view.requestFocus();
        }
    }

//    private static String getScreenResolution(Context context)
//    {
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//        DisplayMetrics metrics = new DisplayMetrics();
//        display.getMetrics(metrics);
//        int width = metrics.widthPixels;
//        int height = metrics.heightPixels;
//
//        return "{" + width + "," + height + "}";
//    }

    private void open_Previous_Activity() {
        Intent intent = new Intent(this,Chapter3_Activity2_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Next_Activity() {
        Intent intent = new Intent(this,Chapter3_Activity4_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter3() {
        Intent intent = new Intent(this,Chapter3_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }


}
