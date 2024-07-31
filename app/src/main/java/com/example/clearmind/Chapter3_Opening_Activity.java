package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Chapter3_Opening_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;

    private Button button_back;
    private Button button_next;
    private Button button_home;
    private long pageOpenTime;
    private long pageCloseTime;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter3_opening);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

        button_home = findViewById(R.id.button_home);
        button_back = findViewById(R.id.button_previous);
        button_next = findViewById(R.id.button_next);

        TextView textView2 = findViewById(R.id.textView2);
//        TextView textView3 = findViewById(R.id.textView3);

        String text2 = "So far we learned that:" + "<ol> <li>Procrastination is often a resulting behavior triggered by some challenging emotions (part 1)</li> <li>You can allow those challenging emotions to exist but just do not let them sabotage your goal/value (part 2)</li></ol>";
        text2  = text2 + "\nIn this part, we'll guide you through a step-by-step approach for managing challenging emotions. We will start with identifying your challenging emotions (Activity 3.1), followed by noticing how these emotions manifest physically within your body (Activity 3.2). Next, you'll engage in some meditation exercises to help you stay calm and focused (Activity 3.3). In Activity 3.4,  you'll create a compassionate self-talk statement to help manage your challenging emotions. To understand where these emotions come from, try Activity 3.5.";

        textView2.setText(Html.fromHtml(text2));

        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                open_Chapter3();
            }
        });

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                // update Chapter3 progress
                Map<String, Object> chapter1_progress_update = new HashMap<>();
                chapter1_progress_update.put("1_Opening", "1");
                db.child("Chapter3").child("progress").child(username).updateChildren(chapter1_progress_update);

                // Update the whole progress
                db.child("progress").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(!task.isSuccessful()){
                            Log.e("firebase", "Error getting data", task.getException());
                        } else{
                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
                            HashMap<String, Object> hashmap_progress = (HashMap<String, Object>) task.getResult().getValue();
                            Integer cur_status = Integer.parseInt(hashmap_progress.get("chapter3").toString());
                            if (cur_status == 0){
                                Map < String, Object > update = new HashMap<>();
                                update.put("chapter3", "1");
                                db.child("progress").child(username).updateChildren(update);
                            }
                        }
                    }
                });

                open_Next_Activity();
            }
        });
    }

    private void open_Next_Activity() {
        Intent intent = new Intent(this,Chapter3_Activity1_Activity.class);
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
        DatabaseReference activityRef = db.child("userActivity").child(username).child("Part3_0_Opening");

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
