package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Chapter2_Opening_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;

    private Button button_back;
    private Button button_next;
    private Button button_home;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter2_opening);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

        button_home = findViewById(R.id.button_home);
        button_back = findViewById(R.id.button_previous);
        button_next = findViewById(R.id.button_next);

        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                open_Chapter2();
            }
        });

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                // update Chapter1 progress
                Map<String, Object> chapter1_progress_update = new HashMap<>();
                chapter1_progress_update.put("1_Opening", "1");
                db.child("Chapter2").child("progress").child(username).updateChildren(chapter1_progress_update);

                // Update the whole progress
                db.child("progress").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(!task.isSuccessful()){
                            Log.e("firebase", "Error getting data", task.getException());
                        } else{
                            Log.d("firebase", String.valueOf(task.getResult().getValue()));
                            HashMap<String, Object> hashmap_progress = (HashMap<String, Object>) task.getResult().getValue();
                            Integer cur_status = Integer.parseInt(hashmap_progress.get("chapter2").toString());
                            if (cur_status == 0){
                                Map < String, Object > update = new HashMap<>();
                                update.put("chapter2", "1");
                                db.child("progress").child(username).updateChildren(update);
                            }
                        }
                    }
                });

//                Map < String, Object > update = new HashMap<>();
//                update.put("chapter1", "1");
//                db.child("progress").child(username).updateChildren(update);

                open_Next_Activity();
            }
        });

    }

    private void open_Next_Activity() {
        Intent intent = new Intent(this,Chapter2_Activity1_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter2() {
        Intent intent = new Intent(this,Chapter2_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
