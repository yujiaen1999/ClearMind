package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

// Not in Use
public class PreSurveyActivity extends AppCompatActivity {

    private String username;
    private DatabaseReference db;
//    private WebView webview_presurvey;

    private Button submit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presurvey);

        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

        WebView webview_presurvey = (WebView) findViewById(R.id.webview_presurvey);

        submit = findViewById(R.id.submit_button);

        webview_presurvey.clearCache(true);

//        webview_presurvey.loadUrl("https://www.surveymonkey.com/r/MTVHND3");
//        webview_presurvey.loadUrl("https://www.google.com/");
//        webview_presurvey.loadUrl("https://docs.google.com/forms/d/e/1FAIpQLScybYJrxW3GMlpdcFwVv-fkrXXdTKtTw3JiLSmqxXjQSUxOwA/viewform?usp=sf_link");

        // pre survey link:
        webview_presurvey.loadUrl("https://docs.google.com/forms/d/e/1FAIpQLSfF_Q69c_Uk-SVXplx21WIP-_hSRpJNfKtrEvEdqL2zIBBi1w/viewform?usp=sf_link");
//        webview_presurvey.loadUrl("https://www.surveymonkey.com/r/wi23preworkshopsurvey");

        WebSettings webSettings = webview_presurvey.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultTextEncodingName("utf-8");
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        webview_presurvey.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //use the webview to load url
                view.loadUrl(url);
                //return true
                return true;
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
//                Map<String, Object> update = new HashMap<>();
//                update.put("presurvey", "1");
//                db.child("progress").child(username).updateChildren(update);

//                openLearnActivity();
                openPopupWindow(v);
            }
        });

    }


    public void openLearnActivity(){
        Intent intent = new Intent(this,LearnActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void openPopupWindow(View view){
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View viewPopupWindow = layoutInflater.inflate(R.layout.activity_popup_window, null);

        final PopupWindow popupWindow = new PopupWindow(viewPopupWindow, 900, 700, true);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        Button button_yes = (Button) viewPopupWindow.findViewById(R.id.button_yes);
        Button button_no_stay = (Button) viewPopupWindow.findViewById(R.id.button_no_stay);
        Button button_no_backtohome = (Button) viewPopupWindow.findViewById(R.id.button_no_backtohome);

        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Map<String, Object> update = new HashMap<>();
                update.put("presurvey", "2");
                db.child("progress").child(username).updateChildren(update);

                openLearnActivity();
            }
        });

        button_no_stay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                popupWindow.dismiss();
            }
        });

        button_no_backtohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openLearnActivity();
            }
        });


    }

}
