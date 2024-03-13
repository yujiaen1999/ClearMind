package com.example.clearmind;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Chapter4_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;
    private ImageButton button_back_to_learn;

    private Button button_opening;
    private Button button_activity1;
    private Button button_activity2;
    private Button button_activity3;
    private Button button_activity4;
    private Button button_activity5;
    private Button button_summary;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter4);
        Intent intent = getIntent();

        // Enable full screen display and avoid nav bar overlap
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_chapter4), (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Apply the insets as a margin to the view. This solution sets only the
            // bottom, left, and right dimensions, but you can apply whichever insets are
            // appropriate to your layout. You can also update the view padding if that's
            // more appropriate.
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
//            mlp.leftMargin = insets.left;
//            mlp.rightMargin = insets.right;
            mlp.bottomMargin = insets.bottom;
            v.setLayoutParams(mlp);

            // Return CONSUMED if you don't want want the window insets to keep passing
            // down to descendant views.
            return WindowInsetsCompat.CONSUMED;
        });

        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

        button_back_to_learn = findViewById(R.id.button_back_learn_page);

        button_opening = findViewById(R.id.button_1);
        button_activity1 = findViewById(R.id.button2);
//        button_activity1_2 = findViewById(R.id.button3);
        button_activity2 = findViewById(R.id.button3);
        button_activity3 = findViewById(R.id.button5);
        button_activity4 = findViewById(R.id.button6);
        button_activity5 = findViewById(R.id.button7);
//        button_activity6 = findViewById(R.id.button8);
        button_summary = findViewById(R.id.button8);

//        HashMap<String, Object> progress_map = new HashMap<>();
//        progress_map.put("1_Opening", "0");
//        progress_map.put("2_Activity4_1", "0");
//        progress_map.put("3_Activity4_2", "0");
//        progress_map.put("4_Activity4_3", "0");
//        progress_map.put("5_Activity4_4", "0");
//        progress_map.put("6_Activity4_5", "0");
//        progress_map.put("7_Summary", "0");
//        this.db.child("Chapter4").child("progress").child(username).setValue(progress_map);

        getData();

        button_back_to_learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openLearnActivity();
            }
        });

    }

    private void openLearnActivity() {
        Intent intent = new Intent(this,LearnActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter4_Opening() {
        Intent intent = new Intent(this,Chapter4_Opening_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter4_Activity1() {
        Intent intent = new Intent(this,Chapter4_Activity1_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter4_Activity2() {
        Intent intent = new Intent(this,Chapter4_Activity2_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
    private void open_Chapter4_Activity3() {
        Intent intent = new Intent(this,Chapter4_Activity3_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter4_Activity4() {
        Intent intent = new Intent(this,Chapter4_Activity4_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter4_Activity5() {
        Intent intent = new Intent(this,Chapter4_Activity5_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter4_Summary() {
        Intent intent = new Intent(this,Chapter4_Summary_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void getData() {
        // calling add value event listener method
        // for getting the values from database.
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String status_1 = String.valueOf(snapshot.child("Chapter4").child("progress").child(username).child("1_Opening").getValue());
                String status_2 = String.valueOf(snapshot.child("Chapter4").child("progress").child(username).child("2_Activity4_1").getValue());
                String status_3 = String.valueOf(snapshot.child("Chapter4").child("progress").child(username).child("3_Activity4_2").getValue());
                String status_4 = String.valueOf(snapshot.child("Chapter4").child("progress").child(username).child("4_Activity4_3").getValue());
                String status_5 = String.valueOf(snapshot.child("Chapter4").child("progress").child(username).child("5_Activity4_4").getValue());
                String status_6 = String.valueOf(snapshot.child("Chapter4").child("progress").child(username).child("6_Activity4_5").getValue());
                String status_7 = String.valueOf(snapshot.child("Chapter4").child("progress").child(username).child("7_Summary").getValue());

                // HANDLE status_1
                if (status_1.equals("0")) {
                    button_opening.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            open_Chapter4_Opening();
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
                            open_Chapter4_Opening();
                        }
                    });
                }

                // HANDLE status_2
                if (status_2.equals("0")){
                    button_activity1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(status_1.equals("1")){
                                Toast.makeText(Chapter4_Activity.this, "Open Activity1", Toast.LENGTH_SHORT).show();
                                open_Chapter4_Activity1();
                            } else{
                                Toast.makeText(Chapter4_Activity.this, "Please complete previous content first!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // status == "1"
                    Drawable icon_done = getResources().getDrawable(R.drawable.icon_text_done);
                    icon_done.setBounds(0,0,icon_done.getMinimumWidth(), icon_done.getMinimumHeight());
                    button_activity1.setCompoundDrawables(icon_done, null, null, null);
                    button_activity1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(Chapter4_Activity.this, "Open", Toast.LENGTH_SHORT).show();
                            open_Chapter4_Activity1();
                        }
                    });
                }

                // HANDLE status_3
                if (status_3.equals("0")){
                    button_activity2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
    //                            Log.d("Chapter2 Check", status_2);
                            if(status_2.equals("1")){
                                Toast.makeText(Chapter4_Activity.this, "Open", Toast.LENGTH_SHORT).show();
                                open_Chapter4_Activity2();
                            } else{
                                Toast.makeText(Chapter4_Activity.this, "Please complete previous content first!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // status == "1"
                    Drawable icon_done = getResources().getDrawable(R.drawable.icon_practice_done);
                    icon_done.setBounds(0,0,icon_done.getMinimumWidth(), icon_done.getMinimumHeight());
                    button_activity2.setCompoundDrawables(icon_done, null, null, null);
                    button_activity2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(Chapter4_Activity.this, "Open", Toast.LENGTH_SHORT).show();
                            open_Chapter4_Activity2();
                        }
                    });
                }

                // HANDLE status_4
                if (status_4.equals("0")){
                    button_activity3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(status_3.equals("1")){
                                Toast.makeText(Chapter4_Activity.this, "Open", Toast.LENGTH_SHORT).show();
                                open_Chapter4_Activity3();
                            } else{
                                Toast.makeText(Chapter4_Activity.this, "Please complete previous content first!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // status == "1"
                    Drawable icon_done = getResources().getDrawable(R.drawable.icon_practice_done);
                    icon_done.setBounds(0,0,icon_done.getMinimumWidth(), icon_done.getMinimumHeight());
                    button_activity3.setCompoundDrawables(icon_done, null, null, null);
                    button_activity3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            open_Chapter4_Activity3();
                        }
                    });
                }

                // HANDLE status_5
                if (status_5.equals("0")){
                    button_activity4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(status_4.equals("1")){
                                Toast.makeText(Chapter4_Activity.this, "Open", Toast.LENGTH_SHORT).show();
                                open_Chapter4_Activity4();
                            } else{
                                Toast.makeText(Chapter4_Activity.this, "Please complete previous content first!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // status == "1"
                    Drawable icon_done = getResources().getDrawable(R.drawable.icon_practice_done);
                    icon_done.setBounds(0,0,icon_done.getMinimumWidth(), icon_done.getMinimumHeight());
                    button_activity4.setCompoundDrawables(icon_done, null, null, null);
                    button_activity4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            open_Chapter4_Activity4();
                        }
                    });
                }

                // HANDLE status_6
                if (status_6.equals("0")){
                    button_activity5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(status_5.equals("1")){
                                Toast.makeText(Chapter4_Activity.this, "Open", Toast.LENGTH_SHORT).show();
                                open_Chapter4_Activity5();
                            } else{
                                Toast.makeText(Chapter4_Activity.this, "Please complete previous content first!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // status == "1"
                    Drawable icon_done = getResources().getDrawable(R.drawable.icon_diagram_done);
                    icon_done.setBounds(0,0,icon_done.getMinimumWidth(), icon_done.getMinimumHeight());
                    button_activity5.setCompoundDrawables(icon_done, null, null, null);
                    button_activity5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            open_Chapter4_Activity5();
                        }
                    });
                }

                // HANDLE status_7
                if (status_7.equals("0")){
                    button_summary.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(status_6.equals("1")){
    //                                Toast.makeText(Chapter3_Activity.this, "Open", Toast.LENGTH_SHORT).show();
                                open_Chapter4_Summary();
                            } else{
                                Toast.makeText(Chapter4_Activity.this, "Please complete previous content first!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    // status == "1"
                    Drawable icon_done = getResources().getDrawable(R.drawable.icon_summary_done);
                    icon_done.setBounds(0,0,icon_done.getMinimumWidth(), icon_done.getMinimumHeight());
                    button_summary.setCompoundDrawables(icon_done, null, null, null);

                    // Add finish word
                    TextView finish_word = findViewById(R.id.finish_word);
                    String txt_word = "&#127881; You have finished this Chapter!";
                    finish_word.setText(Html.fromHtml(txt_word));
    //                    finish_word.setText("You have finished this Chapter!");
                    finish_word.setBackgroundResource(R.drawable.shape_hints);

                    button_summary.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            open_Chapter4_Summary();
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(Chapter4_Activity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
