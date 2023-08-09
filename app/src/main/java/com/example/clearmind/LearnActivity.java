package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
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

public class LearnActivity extends AppCompatActivity {

    private Button save_button;
    private Button achieve_button;
    private Button profile_button;

    private String username;

    private String name;

    private TextView welcome;

    private ImageButton imgBtn_presurvey;
    private ImageButton imgBtn_chapter1;
    private ImageButton imgBtn_chapter2;
    private ImageButton imgBtn_chapter3;
    private ImageButton imgBtn_chapter4;
    private ImageButton imgBtn_postsurvey;

    private DatabaseReference db;

    String status_presurvey;
    String status_chapter1;
    String status_chapter2;
    String status_chapter3;
    String status_chapter4;
    String status_postsurvey;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();
//        getData();

        save_button = (Button) findViewById(R.id.button_save);
        achieve_button = (Button) findViewById(R.id.button_achieve);
        profile_button = (Button) findViewById(R.id.button_profile);

        welcome = findViewById(R.id.welcome);
//        welcome.setText("Hi, " + name + "! Welcome to ClearMind Learn page. You can find 4 chapters here, let's see your challenge for today.");

        imgBtn_presurvey = findViewById(R.id.imgbutton_presurvey);
        imgBtn_chapter1 = findViewById(R.id.imgbutton_chapter1);
        imgBtn_chapter2 = findViewById(R.id.imgbutton_chapter2);
        imgBtn_chapter3 = findViewById(R.id.imgbutton_chapter3);
        imgBtn_chapter4 = findViewById(R.id.imgbutton_chapter4);
        imgBtn_postsurvey = findViewById(R.id.imgbutton_postsurvey);

        getData();



        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openSaveActivity();
            }
        });

        achieve_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openAchieveActivity();
            }
        });

        profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openProfileActivity();
            }
        });

    }

    private void openPreSurveyACtivity() {
        Intent intent = new Intent(this,PreSurveyActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void openSaveActivity(){
        Intent intent = new Intent(this,SaveActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void openAchieveActivity(){
        Intent intent = new Intent(this,AchieveActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void getData() {

        // calling add value event listener method
        // for getting the values from database.
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name = String.valueOf(snapshot.child("register").child(username).child("name").getValue());
                welcome.setText("Hi, " + name + "! Welcome to ClearMind Learn page. You can find 4 Chapters here, let's see your challenge for today.");

                status_presurvey = String.valueOf(snapshot.child("progress").child(username).child("presurvey").getValue());
                status_chapter1 = String.valueOf(snapshot.child("progress").child(username).child("chapter1").getValue());
                status_chapter2 = String.valueOf(snapshot.child("progress").child(username).child("chapter2").getValue());
                status_chapter3 = String.valueOf(snapshot.child("progress").child(username).child("chapter3").getValue());
                status_chapter4 = String.valueOf(snapshot.child("progress").child(username).child("chapter4").getValue());
                status_postsurvey = String.valueOf(snapshot.child("progress").child(username).child("postsurvey").getValue());

//                Toast.makeText(LearnActivity.this, status_presurvey, Toast.LENGTH_SHORT).show();

                // HANDLE PRE-SURVEY BUTTON
                if (status_presurvey.equals("0")){
                    imgBtn_presurvey.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(LearnActivity.this, "Open PreSurvey page", Toast.LENGTH_SHORT).show();
                            openPreSurveyACtivity();

                            // update
//                            Map<String, Object> update = new HashMap<>();
//                            update.put("presurvey", "1");
//                            db.child("progress").child(username).updateChildren(update);
                        }
                    });
                } else {
                    // status == "1"
                    imgBtn_presurvey.setImageResource(R.drawable.presurvey_done);
                    imgBtn_presurvey.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(LearnActivity.this, "Open PreSurvey page", Toast.LENGTH_SHORT).show();
                            openPreSurveyACtivity();
                        }
                    });
                }

                // HANDLE CHAPTER 1 BUTTON
                if (status_chapter1.equals("0")){
                    imgBtn_chapter1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(status_presurvey.equals("1")){
                                Toast.makeText(LearnActivity.this, "Open Chapter 1 page", Toast.LENGTH_SHORT).show();
                                // openPreSurveyACtivity();
                                openChapterOneActivity();

                                // update
                                Map<String, Object> update = new HashMap<>();
                                update.put("chapter1", "1");
                                db.child("progress").child(username).updateChildren(update);
                            } else{
                                Toast.makeText(LearnActivity.this, "Please complete previous Chapter first!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // status == "1"
                    imgBtn_chapter1.setImageResource(R.drawable.done);
                    imgBtn_chapter1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(LearnActivity.this, "Open Chapter 1 page", Toast.LENGTH_SHORT).show();
                            openChapterOneActivity();
                        }
                    });
                }

                // HANDLE CHAPTER 2 BUTTON
                if (status_chapter2.equals("0")){
                    imgBtn_chapter2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(status_chapter1.equals("1")){
                                Toast.makeText(LearnActivity.this, "Open Chapter 2 page", Toast.LENGTH_SHORT).show();
                                // openPreSurveyACtivity();

                                // update
                                Map<String, Object> update = new HashMap<>();
                                update.put("chapter2", "1");
                                db.child("progress").child(username).updateChildren(update);
                            } else {
                                Toast.makeText(LearnActivity.this, "Please complete previous Chapter first!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // status == "1"
                    imgBtn_chapter2.setImageResource(R.drawable.done);
                    imgBtn_chapter2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(LearnActivity.this, "Open Chapter 2 page", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


                // HANDLE CHAPTER 3 BUTTON
                if (status_chapter3.equals("0")){
                    imgBtn_chapter3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(status_chapter2.equals("1")){
                                Toast.makeText(LearnActivity.this, "Open Chapter 3 page", Toast.LENGTH_SHORT).show();
                                // openPreSurveyACtivity();

                                // update
                                Map<String, Object> update = new HashMap<>();
                                update.put("chapter3", "1");
                                db.child("progress").child(username).updateChildren(update);
                            } else {
                                Toast.makeText(LearnActivity.this, "Please complete previous Chapter first!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // status == "1"
                    imgBtn_chapter3.setImageResource(R.drawable.done);
                    imgBtn_chapter3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(LearnActivity.this, "Open Chapter 3 page", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


                // HANDLE CHAPTER 4 BUTTON
                if (status_chapter4.equals("0")){
                    imgBtn_chapter4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(status_chapter3.equals("1")){
                                Toast.makeText(LearnActivity.this, "Open Chapter 4 page", Toast.LENGTH_SHORT).show();
                                // openPreSurveyACtivity();

                                // update
                                Map<String, Object> update = new HashMap<>();
                                update.put("chapter4", "1");
                                db.child("progress").child(username).updateChildren(update);
                            } else {
                                Toast.makeText(LearnActivity.this, "Please complete previous Chapter first!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // status == "1"
                    imgBtn_chapter4.setImageResource(R.drawable.done);
                    imgBtn_chapter4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(LearnActivity.this, "Open Chapter4 page", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


                // HANDLE POST-SURVEY BUTTON
                if (status_postsurvey.equals("0")){
                    imgBtn_postsurvey.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(status_chapter4.equals("1")){
                                Toast.makeText(LearnActivity.this, "Open PostSurvey page", Toast.LENGTH_SHORT).show();
                                // openPreSurveyACtivity();

                                // update
                                Map<String, Object> update = new HashMap<>();
                                update.put("postsurvey", "1");
                                db.child("progress").child(username).updateChildren(update);
                            } else {
                                Toast.makeText(LearnActivity.this, "Please complete previous Chapter first!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // status == "1"
                    imgBtn_postsurvey.setImageResource(R.drawable.done);
                    imgBtn_postsurvey.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(LearnActivity.this, "Open PostSurvey page", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(LearnActivity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void openProfileActivity(){
        Intent intent = new Intent(this,ProfileActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void openChapterOneActivity() {
        Intent intent = new Intent(this,Chapter1_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

//    public void handleImgButton(String status, ImageButton button){
//        if (status.equals("0")){
//            button.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(LearnActivity.this, "Open PreSurvey page", Toast.LENGTH_SHORT).show();
////                            openPreSurveyACtivity();
//
//                    // update
//                    Map<String, Object> update = new HashMap<>();
//                    update.put("presurvey", 1);
//                    db.child("progress").child(username).updateChildren(update);
//                }
//            });
//        } else {
//            // status == "1"
//            button.setImageResource(R.drawable.presurvey_done);
//        }
//    }

}
