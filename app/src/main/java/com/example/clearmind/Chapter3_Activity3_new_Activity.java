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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Chapter3_Activity3_new_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;

    private Button button_back;
    private Button button_next;
    private ImageButton button_home;

    private GridView gridView;
    private ArrayList<DistortionModel> DistortionModelArrayList;
    private DistortionGVAdapter adapter;

    private long pageOpenTime;
    private long pageCloseTime;

    String[] meditation_name = {"The 20 Breath Meditation", "Relaxing Breathing (4-7-8) Exercise", "Mindful Daily Tasks", "Leaves on a Stream"};

    int[] meditation_image = {R.drawable.icon_20breath, R.drawable.icon_478, R.drawable.icon_everyday, R.drawable.icon_stream};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter3_activity3_new);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

        button_home = findViewById(R.id.button_home);
        button_back = findViewById(R.id.button_previous);
        button_next = findViewById(R.id.button_next);

        TextView textView3 = findViewById(R.id.textView3);
        textView3.setText(Html.fromHtml("You are required to try <b><u>2 out of 4</u></b> of these activities to unlock future content, but feel free to come back to try more. You can also look up more meditation techniques on your own. Remember, each technique helps you relax in its own way, so pick the one that feels right for you."));

        db.child("Chapter3").child("activity3").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                HashMap<String, String> hashmap_activity3 = (HashMap<String, String>) task.getResult().getValue();
//                hashmap_activity5 = hashmap_here;
                Log.d("Hashmap Check inside", String.valueOf(hashmap_activity3));
                if(!task.isSuccessful()){
                    Log.e("firebase_activity5", "Error getting data", task.getException());
                }else{
                    Log.d("firebase_activity5", String.valueOf(task.getResult().getValue()));
                    if(hashmap_activity3 == null){
                        // Initialize the db activity5
                        Log.d("firebase_activity3", "Initialized the db activity3");

                        Map<String, Object> activity_update = new HashMap<>();
                        activity_update.put("meditation_0", "0");
                        activity_update.put("meditation_1", "0");
                        activity_update.put("meditation_2", "0");
                        activity_update.put("meditation_3", "0");

                        db.child("Chapter3").child("activity3").child(username).updateChildren(activity_update);
                    }
                }
            }
        });

        // Set up Gridview
        gridView = findViewById(R.id.gridview);
//        ArrayList<DistortionModel> DistortionModelArrayList = new ArrayList<DistortionModel>();
        DistortionModelArrayList = new ArrayList<DistortionModel>();

        // Done: Initialize gridview (add more)
        DistortionModelArrayList.add(new DistortionModel("The 20 Breath Meditation", R.drawable.distortion_question_2));
        DistortionModelArrayList.add(new DistortionModel("Relaxing Breathing (4-7-8) Exercise", R.drawable.distortion_question_2));
        DistortionModelArrayList.add(new DistortionModel("Mindful Daily Tasks", R.drawable.distortion_question_2));
        DistortionModelArrayList.add(new DistortionModel("Leaves on a Stream", R.drawable.distortion_question_2));

//        DistortionGVAdapter adapter = new DistortionGVAdapter(this, DistortionModelArrayList);
        adapter = new DistortionGVAdapter(this, DistortionModelArrayList);
        gridView.setAdapter(adapter);

        // Gridview image initialize
        initialize_Gridview();

        // Gridview element change handle
        change_Gridview();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Done: set item click listener to Meditation
                switch (position) {
                    case 0:
                        open_Meditation1();
                        break;
                    case 1:
                        open_Meditation2();
                        break;
                    case 2:
                        open_Meditation3();
                        break;
                    case 3:
                        open_Meditation4();
                        break;
                };
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
                db.child("Chapter3").child("activity3").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        HashMap<String, String> meditation_status = (HashMap<String, String>) task.getResult().getValue();
                        Log.d("distortion_status Check", String.valueOf(meditation_status));
                        if(!task.isSuccessful()){
                            Log.e("firebase_activity3", "Error getting data", task.getException());
                        }else{
                            Log.d("firebase_activity3", String.valueOf(task.getResult().getValue()));
                            if(meditation_status != null){
                                // Check all status before go next page
                                int next_page_flage = 0;

                                for (String value : meditation_status.values()) {
                                    Log.d("Test status: ", value);
                                    if(value.equals("1")){
                                        next_page_flage += 1;
                                    }
                                }

                                if (next_page_flage > 2){
                                    // update Chapter progress
                                    Map<String, Object> chapter_progress_update = new HashMap<>();
                                    chapter_progress_update.put("4_Activity3_3", "1");
                                    db.child("Chapter3").child("progress").child(username).updateChildren(chapter_progress_update);

                                    Toast.makeText(Chapter3_Activity3_new_Activity.this, "Processing to the next page", Toast.LENGTH_SHORT).show();

                                    open_Next_Activity();
                                } else{
                                    Toast.makeText(Chapter3_Activity3_new_Activity.this, "Explore more meditations to proceed to the next page", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });

//                // update Chapter progress
//                Map<String, Object> chapter_progress_update = new HashMap<>();
//                chapter_progress_update.put("4_Activity3_3", "1");
//                db.child("Chapter3").child("progress").child(username).updateChildren(chapter_progress_update);
//
//                open_Next_Activity();
            }
        });
    }

    private void initialize_Gridview() {
        // Gridview image initialize
        db.child("Chapter3").child("activity3").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                HashMap<String, String> hashmap_activity3 = (HashMap<String, String>) task.getResult().getValue();
//                hashmap_activity5 = hashmap_here;
                Log.d("Hashmap Check inside", String.valueOf(hashmap_activity3));
                if(!task.isSuccessful()){
                    Log.e("firebase_summary", "Error getting data", task.getException());
                }else{
                    Log.d("firebase_summary", String.valueOf(task.getResult().getValue()));
                    if(hashmap_activity3 != null){
                        String status_0 = hashmap_activity3.get("meditation_0");
                        String status_1 = hashmap_activity3.get("meditation_1");
                        String status_2 = hashmap_activity3.get("meditation_2");
                        String status_3 = hashmap_activity3.get("meditation_3");

                        icon_setup(status_0, 0);
                        icon_setup(status_1, 1);
                        icon_setup(status_2, 2);
                        icon_setup(status_3, 3);
                    }
                }
                gridView.setAdapter(adapter);
            }
        });
    }

    private void change_Gridview() {
        // TODO: use value change listener to update gridview based on user selection
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String status_0 = String.valueOf(snapshot.child("Chapter3").child("activity3").child(username).child("meditation_0").getValue());
                String status_1 = String.valueOf(snapshot.child("Chapter3").child("activity3").child(username).child("meditation_1").getValue());
                String status_2 = String.valueOf(snapshot.child("Chapter3").child("activity3").child(username).child("meditation_2").getValue());
                String status_3 = String.valueOf(snapshot.child("Chapter3").child("activity3").child(username).child("meditation_3").getValue());

                icon_setup(status_0, 0);
                icon_setup(status_1, 1);
                icon_setup(status_2, 2);
                icon_setup(status_3, 3);

                gridView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(Chapter3_Activity3_new_Activity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void icon_setup(String status, int position) {
        switch (status) {
            case "1":
                DistortionModelArrayList.get(position).setImgid(meditation_image[position]);
                break;
            default:
                break;
        }

    }

    private void open_Meditation1() {
        Intent intent = new Intent(this,Chapter3_Activity3_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Meditation2() {
        Intent intent = new Intent(this,Chapter3_Activity3_2_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Meditation3() {
        Intent intent = new Intent(this,Chapter3_Activity3_3_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Meditation4() {
        Intent intent = new Intent(this,Chapter3_Activity3_4_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }


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

    @Override
    protected void onResume() {
        super.onResume();
        pageOpenTime = System.currentTimeMillis(); // get the page open time
    }

    @Override
    protected void onPause() {
        super.onPause();
        pageCloseTime = System.currentTimeMillis(); // get the page close time

        if (pageCloseTime - pageOpenTime > 1){   // Only if the view time >= 2 seconds
            sendTimeStampsToFirebase(); // store the Time Stamp to Firebase
        }
//        sendTimeStampsToFirebase();
    }

    private void sendTimeStampsToFirebase() {
        DatabaseReference activityRef = db.child("userActivity").child(username).child("Part3_3_Activity3_All");

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
