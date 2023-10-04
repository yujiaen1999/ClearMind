package com.example.clearmind;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Chapter3_Activity2_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;

    private Button button_back;
    private Button button_next;
    private Button button_home;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter3_activity2);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

        button_home = findViewById(R.id.button_home);
        button_back = findViewById(R.id.button_previous);
        button_next = findViewById(R.id.button_next);

        EditText other_input = findViewById(R.id.input1);
        other_input.setVisibility(View.GONE);

        // Set up the spinners' options
        Spinner spinner_1 = (Spinner) findViewById(R.id.spinner_1);
        Spinner spinner_2 = (Spinner) findViewById(R.id.spinner_2);

        ArrayAdapter<CharSequence> adapter_1 = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.body_array, android.R.layout.simple_spinner_item);
        adapter_1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_1.setAdapter(adapter_1);

        ArrayAdapter<CharSequence> adapter_2 = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.sensation_array, android.R.layout.simple_spinner_item);
        adapter_2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_2.setAdapter(adapter_2);

        // Retrieve and Display user input from the database
        db.child("Chapter3").child("activity2").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                HashMap<String, String> hashmap_chapter3 = (HashMap<String, String>) task.getResult().getValue();
                if(!task.isSuccessful()){
                    Log.e("firebase_summary", "Error getting data", task.getException());
                }else{
                    Log.d("firebase_summary", String.valueOf(task.getResult().getValue()));
                    if(hashmap_chapter3 != null){
                        int position_1 = adapter_1.getPosition(hashmap_chapter3.get("body"));
                        int position_2 = adapter_2.getPosition(hashmap_chapter3.get("sensation"));
//                        answer1.setText(hashmap_chapter3.get("emotion"));
//                        answer2.setText(hashmap_chapter3.get("event"));

                        spinner_1.setSelection(position_1);
                        spinner_2.setSelection(position_2);
                        if (position_2 == 13){
                            other_input.setText(hashmap_chapter3.get("other_sensation"));
                        }
                    }
                }
            }
        });

        // Set a spinner item selected listener
        spinner_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                Log.d("Spinner Check", String.valueOf(position));
                if (position == 13){     // Item "Other" is in position 13
                    other_input.setVisibility(View.VISIBLE);
                } else{
                    other_input.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
                Log.d("Spinner Check", "nothing selected");
            }
        });


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
//                String txt_answer1 = answer1.getText().toString();
//                String txt_answer2 = answer2.getText().toString();

                String txt_answer1 = spinner_1.getSelectedItem().toString();
                String txt_answer2 = spinner_2.getSelectedItem().toString();
                String txt_other_input = other_input.getText().toString();

                if (txt_answer1.isEmpty() || txt_answer2.isEmpty()) {
                    Toast.makeText(Chapter3_Activity2_Activity.this, "Empty input", Toast.LENGTH_SHORT).show();
                } else if (txt_answer2.equals("Other") && txt_other_input.isEmpty()) {
                    Toast.makeText(Chapter3_Activity2_Activity.this, "Please input other physical sensation", Toast.LENGTH_SHORT).show();
                } else {
                    // Get all answers from user
                    db.child("Chapter3").child("activity2").child(username).child("body").setValue(txt_answer1);
                    db.child("Chapter3").child("activity2").child(username).child("sensation").setValue(txt_answer2);
                    if(txt_answer2.equals("Other")){
                        db.child("Chapter3").child("activity2").child(username).child("other_sensation").setValue(txt_other_input);
                    }

                    // update Chapter progress
                    Map<String, Object> chapter_progress_update = new HashMap<>();
                    chapter_progress_update.put("3_Activity3_2", "1");
                    db.child("Chapter3").child("progress").child(username).updateChildren(chapter_progress_update);

                    open_Next_Activity();
                }
            }
        });

    }

    private void open_Previous_Activity() {
        Intent intent = new Intent(this,Chapter3_Activity1_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Next_Activity() {
        Intent intent = new Intent(this,Chapter3_Activity3_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter3() {
        Intent intent = new Intent(this,Chapter3_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

}
