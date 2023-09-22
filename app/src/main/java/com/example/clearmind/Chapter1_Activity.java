package com.example.clearmind;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Chapter1_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;

    private ImageButton button_back_to_learn;
    private Button button_opening;
    private Button button_activity1;
    private Button button_activity2;
    private Button button_activity2_questions;
    private Button button_act;
    private Button button_time;
    private Button button_summary;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter1);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

//        HashMap<String, Object> progress_map = new HashMap<>();
//        progress_map.put("1_Opening", "0");
//        progress_map.put("2_Activity1_1", "0");
//        progress_map.put("3_Activity1_2_Discover", "0");
//        progress_map.put("4_Activity1_2_Questions", "0");
//        progress_map.put("5_Summary_and_ACT", "0");
//        progress_map.put("6_Time_Management", "0");
//        progress_map.put("7_Summary", "0");
//        this.db.child("Chapter1").child("progress").child(username).setValue(progress_map);


        button_back_to_learn = findViewById(R.id.button_back_learn_page);

        button_opening = findViewById(R.id.button_opening);
        button_activity1 = findViewById(R.id.button2);
        button_activity2 = findViewById(R.id.button3);
        button_activity2_questions = findViewById(R.id.button3_1);
        button_act = findViewById(R.id.button4);
        button_time = findViewById(R.id.button5);
        button_summary = findViewById(R.id.button6);

        getData();

//        Drawable icon_done = getResources().getDrawable(R.drawable.icon_opening_done);
//        icon_done.setBounds(0,0,icon_done.getMinimumWidth(), icon_done.getMinimumHeight());
//        button_opening.setCompoundDrawables(icon_done, null, null, null);

        button_back_to_learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openLearnActivity();
            }
        });

//        button_opening.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v){
//                open_Chapter1_Opening();
//            }
//        });
//
//        button_activity1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v){
//                open_Chapter1_Activity1();
//            }
//        });
//
//        button_activity2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v){
//                open_Chapter1_Activity2();
//            }
//        });
//
//        button_activity2_questions.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v){
//                open_Chapter1_Activity2_Questions();
//            }
//        });
//
//        button_act.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v){
//                open_Chapter1_Intro_ACT();
//            }
//        });
//
//        button_time.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v){
//                open_Chapter1_Time();
//            }
//        });
//
//        button_summary.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v){
//                open_Chapter1_Summary();
//            }
//        });

    }

    private void openLearnActivity() {
        Intent intent = new Intent(this,LearnActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }


    private void open_Chapter1_Summary() {
        Intent intent = new Intent(this,Chapter1_Summary_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter1_Time() {
        Intent intent = new Intent(this,Chapter1_Time_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter1_Intro_ACT() {
        Intent intent = new Intent(this,Chapter1_Intro_ACT_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter1_Activity2() {
        Intent intent = new Intent(this,Chapter1_Activity2_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter1_Activity2_Questions() {
        Intent intent = new Intent(this,Chapter1_Activity2_Question_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter1_Activity1() {
        Intent intent = new Intent(this,Chapter1_Activity1_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter1_Opening() {
        Intent intent = new Intent(this,Chapter1_Opening_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void getData() {

        // calling add value event listener method
        // for getting the values from database.
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String status_1 = String.valueOf(snapshot.child("Chapter1").child("progress").child(username).child("1_Opening").getValue());
                String status_2 = String.valueOf(snapshot.child("Chapter1").child("progress").child(username).child("2_Activity1_1").getValue());
                String status_3 = String.valueOf(snapshot.child("Chapter1").child("progress").child(username).child("3_Activity1_2_Discover").getValue());
                String status_4 = String.valueOf(snapshot.child("Chapter1").child("progress").child(username).child("4_Activity1_2_Questions").getValue());
                String status_5 = String.valueOf(snapshot.child("Chapter1").child("progress").child(username).child("5_Summary_and_ACT").getValue());
                String status_6 = String.valueOf(snapshot.child("Chapter1").child("progress").child(username).child("6_Time_Management").getValue());
                String status_7 = String.valueOf(snapshot.child("Chapter1").child("progress").child(username).child("7_Summary").getValue());

                Log.d("Chapter 1 Status1", status_1);
                Log.d("Chapter 1 Status2", status_2);
                Log.d("Chapter 1 Status3", status_3);


                // HANDLE status_1
                if (status_1.equals("0")) {
                    button_opening.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            open_Chapter1_Opening();
                        }
                    });

                } else {
                    // status == "1"
                    Drawable icon_done = getResources().getDrawable(R.drawable.icon_opening_done);
                    icon_done.setBounds(0,0,icon_done.getMinimumWidth(), icon_done.getMinimumHeight());
                    button_opening.setCompoundDrawables(icon_done, null, null, null);
                    button_opening.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            open_Chapter1_Opening();
                        }
                    });
                }

                // HANDLE status_2
                if (status_2.equals("0")){
                    button_activity1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(status_1.equals("1")){
                                Toast.makeText(Chapter1_Activity.this, "Open Activity1", Toast.LENGTH_SHORT).show();
                                open_Chapter1_Activity1();
                            } else{
                                Toast.makeText(Chapter1_Activity.this, "Please complete previous content first!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // status == "1"
                    Drawable icon_done = getResources().getDrawable(R.drawable.icon_practice_done);
                    icon_done.setBounds(0,0,icon_done.getMinimumWidth(), icon_done.getMinimumHeight());
                    button_activity1.setCompoundDrawables(icon_done, null, null, null);
                    button_activity1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(Chapter1_Activity.this, "Open Chapter 1 page", Toast.LENGTH_SHORT).show();
                            open_Chapter1_Activity1();
                        }
                    });
                }

                // HANDLE status_3
                if (status_3.equals("0")){
                    button_activity2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(status_2.equals("1")){
                                Toast.makeText(Chapter1_Activity.this, "Open", Toast.LENGTH_SHORT).show();
                                open_Chapter1_Activity2();
                            } else{
                                Toast.makeText(Chapter1_Activity.this, "Please complete previous content first!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // status == "1"
                    Drawable icon_done = getResources().getDrawable(R.drawable.icon_text_done);
                    icon_done.setBounds(0,0,icon_done.getMinimumWidth(), icon_done.getMinimumHeight());
                    button_activity2.setCompoundDrawables(icon_done, null, null, null);
                    button_activity2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(Chapter1_Activity.this, "Open Chapter 1 page", Toast.LENGTH_SHORT).show();
                            open_Chapter1_Activity2();
                        }
                    });
                }

                // HANDLE status_4
                if (status_4.equals("0")){
                    button_activity2_questions.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(status_3.equals("1")){
                                Toast.makeText(Chapter1_Activity.this, "Open", Toast.LENGTH_SHORT).show();
                                open_Chapter1_Activity2_Questions();
                            } else{
                                Toast.makeText(Chapter1_Activity.this, "Please complete previous content first!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // status == "1"
                    Drawable icon_done = getResources().getDrawable(R.drawable.icon_practice_done);
                    icon_done.setBounds(0,0,icon_done.getMinimumWidth(), icon_done.getMinimumHeight());
                    button_activity2_questions.setCompoundDrawables(icon_done, null, null, null);
                    button_activity2_questions.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            open_Chapter1_Activity2_Questions();
                        }
                    });
                }

                // HANDLE status_5
                if (status_5.equals("0")){
                    button_act.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(status_4.equals("1")){
                                Toast.makeText(Chapter1_Activity.this, "Open", Toast.LENGTH_SHORT).show();
                                open_Chapter1_Intro_ACT();
                            } else{
                                Toast.makeText(Chapter1_Activity.this, "Please complete previous content first!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // status == "1"
                    Drawable icon_done = getResources().getDrawable(R.drawable.icon_text_done);
                    icon_done.setBounds(0,0,icon_done.getMinimumWidth(), icon_done.getMinimumHeight());
                    button_act.setCompoundDrawables(icon_done, null, null, null);
                    button_act.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            open_Chapter1_Intro_ACT();
                        }
                    });
                }

                // HANDLE status_6
                if (status_6.equals("0")){
                    button_time.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(status_5.equals("1")){
                                Toast.makeText(Chapter1_Activity.this, "Open", Toast.LENGTH_SHORT).show();
                                open_Chapter1_Time();
                            } else{
                                Toast.makeText(Chapter1_Activity.this, "Please complete previous content first!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // status == "1"
                    Drawable icon_done = getResources().getDrawable(R.drawable.icon_practice_done);
                    icon_done.setBounds(0,0,icon_done.getMinimumWidth(), icon_done.getMinimumHeight());
                    button_time.setCompoundDrawables(icon_done, null, null, null);
                    button_time.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            open_Chapter1_Time();
                        }
                    });
                }

                // HANDLE status_7
                if (status_7.equals("0")){
                    button_summary.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(status_6.equals("1")){
                                Toast.makeText(Chapter1_Activity.this, "Open", Toast.LENGTH_SHORT).show();
                                open_Chapter1_Summary();
                            } else{
                                Toast.makeText(Chapter1_Activity.this, "Please complete previous content first!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // status == "1"
                    Drawable icon_done = getResources().getDrawable(R.drawable.icon_summary_done);
                    icon_done.setBounds(0,0,icon_done.getMinimumWidth(), icon_done.getMinimumHeight());
                    button_summary.setCompoundDrawables(icon_done, null, null, null);
                    button_summary.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            open_Chapter1_Summary();
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(Chapter1_Activity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
