package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Chapter4_Activity2_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;

    private Button button_back;
    private Button button_next;
    private ImageButton button_home;

    private GridView gridView;
    private  ArrayList<DistortionModel> DistortionModelArrayList;
    private DistortionGVAdapter adapter;

    private EditText answer1;
    private EditText answer2;
    private EditText answer3;
    private EditText answer4;
    private long pageOpenTime;
    private long pageCloseTime;

    private final NavigationDrawerHelper navigationDrawerHelper = new NavigationDrawerHelper(this);

    String[] star_name = {"Specific", "Trackable", "Achievable", "Relevant"};
    int[] star_image = {R.drawable.star_s, R.drawable.star_t, R.drawable.star_a, R.drawable.star_r};
    String[] star_intro = {"You need to be specific about your goal because vague goals can make success feel out of reach. Being specific helps you plan effectively and narrow your focus to what truly matters. For example, instead of setting a vague goal like \"I want to be fit,\" a specific goal would be \"I want to lose 20 pounds within three months through daily 30-minute exercise sessions and maintaining a balanced diet.\"",
        "You need a mechanism to track and measure your progress to keep on track and see how you're doing with your goal. Set a reasonable time frame to give you a sense of urgency and motivation. For example, if you want to improve your fitness and plan to run 3 miles twice a week, keep a fitness journal. In the journal, record the date, the distance you run, how long it takes, and how you feel during your run. Over time, this record will show how you're doing and motivate you to stick with your goal.",
        "Pushing your limits while keeping your goals realistically attainable is essential. You should evaluate your available resources and abilities to ensure that your goals align with what you can realistically achieve. It would be beneficial to take your past experiences and other people’s experiences with similar goals into account, which helps you to evaluate the feasibility of your goals.",
        "Your goals should be relevant to your life. You want to ensure your goal aligns with your values and long-term ambitions. This alignment between your goals and values is crucial because it provides a profound sense of purpose and motivation. How does this goal connect with your values and the person you're striving to be?"};


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter4_activity2);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

        button_home = findViewById(R.id.button_home);
        button_back = findViewById(R.id.button_previous);
        button_next = findViewById(R.id.button_next);

        answer1 = findViewById(R.id.input1);
        answer2 = findViewById(R.id.input2);
        answer3 = findViewById(R.id.input3);
        answer4 = findViewById(R.id.input4);

        // Set up Gridview
        gridView = findViewById(R.id.gridview);
//        ArrayList<DistortionModel> DistortionModelArrayList = new ArrayList<DistortionModel>();
        DistortionModelArrayList = new ArrayList<DistortionModel>();

        // Done: Initialize gridview (add more)
        DistortionModelArrayList.add(new DistortionModel("Specific", R.drawable.star_s));
        DistortionModelArrayList.add(new DistortionModel("Trackable", R.drawable.star_t));
        DistortionModelArrayList.add(new DistortionModel("Achievable", R.drawable.star_a));
        DistortionModelArrayList.add(new DistortionModel("Relevant", R.drawable.star_r));

//        DistortionGVAdapter adapter = new DistortionGVAdapter(this, DistortionModelArrayList);
        adapter = new DistortionGVAdapter(this, DistortionModelArrayList);
        gridView.setAdapter(adapter);

//        // Gridview image initialize
//        initialize_Gridview();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Done: set item click listener to popup window
                openPopupWindow(view, position);
            }
        });


        // Set text format
        TextView textView_sum = findViewById(R.id.textView2);
//        TextView textView_specific = findViewById(R.id.textView3);
//        TextView textView_trackable = findViewById(R.id.textView4);
//        TextView textView_achievable = findViewById(R.id.textView5);
//        TextView textView_relevant = findViewById(R.id.textView6);
        TextView example = findViewById(R.id.example);

        String txt_sum = "Let's first introduce the STAR framework for goal setting. The STAR framework can be applied to create well-defined and achievable goals. STAR stands for <b>Specific, Trackable, Achievable, and Relevant</b>. Here's a breakdown of each component:";
//        String txt_specific = "<b>Specific</b>: You need to be specific about your goal because vague goals can make success feel out of reach. Being specific helps you plan effectively and narrow your focus to what truly matters. For example, Instead of setting a vague goal like “I want to be fit,” a specific goal would be “I want to lose 20 pounds within three months through daily 30-minute exercise sessions and maintaining a balanced diet.”";
//        String txt_trackable = "<b>Trackable</b>: You need a mechanism to track and measure your progress to keep on track and see how you're doing with your goal. Set a reasonable time frame to give you a sense of urgency and motivation. Consider using a simple recording system. For example, if you want to improve your fitness and plan to run 3 miles twice a week, keep a fitness journal. In the journal, note the date, the distance you run, how long it takes, and how you feel during your run. Over time, this record will show how you're doing and motivate you to stick with your goal.";
//        String txt_achievable = "<b>Achievable</b>: Pushing your limits while keeping your goals realistically attainable is essential. You should evaluate your available resources and abilities to ensure that your goals align with what you can realistically achieve. It would be beneficial to take your past experiences and other people’s experiences with similar goals into account, which helps you to evaluate the feasibility of your goals.";
//        String txt_relevant = "<b>Relevant</b>: Your goals should be relevant and meaningful to your life. You want to ensure your goal aligns with your values and long-term ambitions. This alignment between your goals and values is crucial because it provides a profound sense of purpose and motivation. How does this goal connect with your values and the person you're striving to be? Consider how it shapes the person you want to be and how it brings you closer to what truly matters to you.";
        String txt_example = "<b>Example:</b>"+
                "<br><b>Specific</b>: Next week, I commit to dedicating at least two hours of concentrated work to my final review every day." +
                "<br><b>Trackable</b>: Starting next Monday, I will follow this routine for the upcoming week." +
                "<br><b>Achievable</b>: I will break down my final review into seven chapters, which is manageable alongside my other responsibilities." +
                "<br><b>Relevant</b>: This goal helps me to perform better on my test and align with my values of  productivity and reducing procrastination tendencies.";

        textView_sum.setText(Html.fromHtml(txt_sum));
//        textView_specific.setText(Html.fromHtml(txt_specific));
//        textView_trackable.setText(Html.fromHtml(txt_trackable));
//        textView_achievable.setText(Html.fromHtml(txt_achievable));
//        textView_relevant.setText(Html.fromHtml(txt_relevant));
        example.setText(Html.fromHtml(txt_example));

        // Retrieve and Display user input from the database
        db.child("Chapter4").child("activity2").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                HashMap<String, String> hashmap_chapter4 = (HashMap<String, String>) task.getResult().getValue();
                if(!task.isSuccessful()){
                    Log.e("firebase_summary", "Error getting data", task.getException());
                }else{
                    Log.d("firebase_summary", String.valueOf(task.getResult().getValue()));
                    if(hashmap_chapter4 != null){
                        answer1.setText(hashmap_chapter4.get("S"));
                        answer2.setText(hashmap_chapter4.get("T"));
                        answer3.setText(hashmap_chapter4.get("A"));
                        answer4.setText(hashmap_chapter4.get("R"));
                    }
                }
            }
        });

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
                String txt_answer1 = answer1.getText().toString();
                String txt_answer2 = answer2.getText().toString();
                String txt_answer3 = answer3.getText().toString();
                String txt_answer4 = answer4.getText().toString();

                if (txt_answer1.isEmpty() || txt_answer2.isEmpty() || txt_answer3.isEmpty() || txt_answer4.isEmpty()){
                    Toast.makeText(Chapter4_Activity2_Activity.this,  "Empty input", Toast.LENGTH_SHORT).show();
                } else {
                    // Get all answers from user
                    db.child("Chapter4").child("activity2").child(username).child("S").setValue(txt_answer1);
                    db.child("Chapter4").child("activity2").child(username).child("T").setValue(txt_answer2);
                    db.child("Chapter4").child("activity2").child(username).child("A").setValue(txt_answer3);
                    db.child("Chapter4").child("activity2").child(username).child("R").setValue(txt_answer4);

                    // update Chapter progress
                    Map<String, Object> chapter_progress_update = new HashMap<>();
                    chapter_progress_update.put("3_Activity4_2", "1");
                    db.child("Chapter4").child("progress").child(username).updateChildren(chapter_progress_update);

                    open_Next_Activity();
                }
            }
        });
    }

    private void open_Previous_Activity() {
        Intent intent = new Intent(this,Chapter4_Activity1_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Next_Activity() {
        Intent intent = new Intent(this,Chapter4_Activity3_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter4() {
        Intent intent = new Intent(this,Chapter4_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }


    // Popup the corresponding window with content based on "position"
    private void openPopupWindow(View view, int position) {
        // initialize popup window
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View viewPopupWindow = layoutInflater.inflate(R.layout.activity_popup_star, null);
//        final PopupWindow popupWindow = new PopupWindow(viewPopupWindow, 600, 600, true);
        final PopupWindow popupWindow = new PopupWindow(viewPopupWindow);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // initialize elements
        TextView show_name = (TextView) viewPopupWindow.findViewById(R.id.name);
        TextView show_intro = (TextView) viewPopupWindow.findViewById(R.id.textView_intro);
//        TextView textView_intro = (TextView) viewPopupWindow.findViewById(R.id.textView_intro);
        ImageView imageView = (ImageView) viewPopupWindow.findViewById(R.id.image);

        Button button_confirm = (Button) viewPopupWindow.findViewById(R.id.button_confirm);

//        button_confirm.setVisibility(View.GONE);

        imageView.setImageResource(star_image[position]);
        show_name.setText(star_name[position]);
        String txt_intro = star_intro[position];
        show_intro.setText(Html.fromHtml(txt_intro));

        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                popupWindow.dismiss();
            }
        });
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
        DatabaseReference activityRef = db.child("userActivity").child(username).child("Part4_2_Activity2");

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
