package com.example.clearmind;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Chapter3_Activity5_2_Activity extends AppCompatActivity {
    private String username;
    private DatabaseReference db;

    private Button button_back;
    private Button button_next;
    private Button button_home;

    private GridView gridView;
    private HashMap<String, String> hashmap_activity5;
    private  ArrayList<DistortionModel> DistortionModelArrayList;
    private DistortionGVAdapter adapter;

    // Distortion elements
    // TODO: Add more
    String[] distortion_name = {"Mental filtering"};
    String[] distortion_intro = {"Mental filtering is like viewing the world through a negative lens. It involves focusing solely on the negative aspects while entirely disregarding the positive things in one's life."
    };
    String[] distortion_example = {"You gave a presentation at work, and your colleagues gave you positive feedback, saying your ideas were awesome and you explained things well. However, one person offered a minor critique about your slides for potential improvement. Instead of recognizing the positive feedback, you focused solely on that one piece of criticism, feeling like your entire presentation was a failure."
    };
    String[] distortion_solution = {"Journaling can be a valuable method to help overcome mental filtering. Keeping a diary or journal that involves writing down your thoughts and emotions about the events in your life will help redirect your attention away from the negatives and actively seek out the neutral or positive aspects of a situation."
    };
    int[] distortion_image = {R.drawable.distortion_mental_filtering_1};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter3_activity5_2);
        Intent intent = getIntent();
        this.username = intent.getStringExtra("username");
        this.db = FirebaseDatabase.getInstance().getReference();

        button_home = findViewById(R.id.button_home);
        button_back = findViewById(R.id.button_previous);
        button_next = findViewById(R.id.button_next);

        // Set activity database
        // TODO: need to be initialized somewhere
//        Map<String, Object> activity_update = new HashMap<>();
//        activity_update.put("distortion_0", "0");
//        activity_update.put("distortion_1", "0");
//        activity_update.put("distortion_2", "0");
//        activity_update.put("distortion_3", "0");
//        activity_update.put("distortion_4", "0");
//        chapter5_update.put("distortion_5", "0");
//        chapter5_update.put("distortion_6", "0");
//        chapter5_update.put("distortion_7", "0");
//        chapter5_update.put("distortion_8", "0");
//        chapter5_update.put("distortion_9", "0");
//
//        db.child("Chapter3").child("activity5").child(username).updateChildren(activity_update);


        // Set up Gridview
        gridView = findViewById(R.id.gridview);
//        ArrayList<DistortionModel> DistortionModelArrayList = new ArrayList<DistortionModel>();
        DistortionModelArrayList = new ArrayList<DistortionModel>();

        // TODO: Initialize gridview (add more)
        DistortionModelArrayList.add(new DistortionModel("Mental filtering", R.drawable.distortion_question));
        DistortionModelArrayList.add(new DistortionModel("All-or-nothing thinking", R.drawable.distortion_question));
        DistortionModelArrayList.add(new DistortionModel("Overgeneralization", R.drawable.distortion_question));
        DistortionModelArrayList.add(new DistortionModel("Discounting the positive", R.drawable.distortion_question));

//        DistortionGVAdapter adapter = new DistortionGVAdapter(this, DistortionModelArrayList);
        adapter = new DistortionGVAdapter(this, DistortionModelArrayList);
        gridView.setAdapter(adapter);

        // Gridview image initialize
        initialize_Gridview();

        // Gridview element change handle
        change_Gridview();

//        DistortionGVAdapter adapter = new DistortionGVAdapter(this, DistortionModelArrayList);
//        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO: set item click listener to popup window
                switch (position) {
                    case 0:
                        openPopupWindow(view, position);
//                        db.child("Chapter3").child("activity5").child(username).child("distortion_0").setValue("1");
                        Toast.makeText(Chapter3_Activity5_2_Activity.this, "00000", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(Chapter3_Activity5_2_Activity.this, "11111", Toast.LENGTH_SHORT).show();
                        break;

                }
//                gridView.setAdapter(adapter);
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
                // update Chapter progress
                Map<String, Object> chapter_progress_update = new HashMap<>();
                chapter_progress_update.put("6_Activity3_5_1", "1");
                db.child("Chapter3").child("progress").child(username).updateChildren(chapter_progress_update);

                open_Next_Activity();
//                openPopupWindow(v);
            }
        });

    }

    // Popup the corresponding window with content based on "position"
    private void openPopupWindow(View view, int position) {
        // initialize popup window
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View viewPopupWindow = layoutInflater.inflate(R.layout.activity_popup_distortion, null);
//        final PopupWindow popupWindow = new PopupWindow(viewPopupWindow, 600, 600, true);
        final PopupWindow popupWindow = new PopupWindow(viewPopupWindow);
        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // initialize elements
        TextView show_name = (TextView) viewPopupWindow.findViewById(R.id.name);
        TextView show_intro = (TextView) viewPopupWindow.findViewById(R.id.textView_intro);
        TextView show_example = (TextView) viewPopupWindow.findViewById(R.id.textView_example);
        TextView textView2 = (TextView) viewPopupWindow.findViewById(R.id.textView2);
        ImageView imageView = (ImageView) viewPopupWindow.findViewById(R.id.image);

        Button button_yes = (Button) viewPopupWindow.findViewById(R.id.button_yes);
        Button button_no = (Button) viewPopupWindow.findViewById(R.id.button_no);
        Button button_confirm = (Button) viewPopupWindow.findViewById(R.id.button_confirm);

        button_confirm.setVisibility(View.GONE);

        imageView.setImageResource(distortion_image[position]);
        show_name.setText(distortion_name[position]);
        String txt_intro = distortion_intro[position];
        show_intro.setText(Html.fromHtml(txt_intro));

        String txt_example = "<b>Example: </b>" + distortion_example[position];
        show_example.setText(Html.fromHtml(txt_example));

        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // Set distortion status
                String child_path = "distortion_" + String.valueOf(position);
                db.child("Chapter3").child("activity5").child(username).child(child_path).setValue("1");

                // Show solution page
                String txt_solution = "<b>Solution: </b>" + distortion_solution[position];
                show_example.setText(Html.fromHtml(txt_solution));

                button_confirm.setVisibility(View.VISIBLE);
                button_yes.setVisibility(View.GONE);
                button_no.setVisibility(View.GONE);
                textView2.setVisibility(View.GONE);
            }
        });

        button_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String child_path = "distortion_" + String.valueOf(position);
                db.child("Chapter3").child("activity5").child(username).child(child_path).setValue("2");
//                openPopupWindow(v);
                popupWindow.dismiss();
            }
        });

        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                popupWindow.dismiss();
            }
        });
    }


//    private void openPopup_Solution(View view, int position) {
//        // initialize popup window
//        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//        View viewPopupWindow = layoutInflater.inflate(R.layout.activity_popup_distortion, null);
////        final PopupWindow popupWindow = new PopupWindow(viewPopupWindow, 600, 600, true);
//        final PopupWindow popupWindow = new PopupWindow(viewPopupWindow);
//        popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
//        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
//        popupWindow.setFocusable(true);
//
//        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
//
//        // initialize elements
//        TextView show_intro = (TextView) viewPopupWindow.findViewById(R.id.textView_intro);
//        TextView show_example = (TextView) viewPopupWindow.findViewById(R.id.textView_example);
//        Button button_yes = (Button) viewPopupWindow.findViewById(R.id.button_yes);
//        Button button_no = (Button) viewPopupWindow.findViewById(R.id.button_no);
//
//        String txt_intro = "distortion introduction";
//        show_intro.setText(Html.fromHtml(txt_intro));
//
//        String txt_example = "<b>Example: </b>" + "distortion example";
//        String txt_solution = "<b>Solution: </b>" + "distortion solution";
//        show_example.setText(Html.fromHtml(txt_solution));
//
//        button_yes.setText("Confirm");
//        button_no.setText("Next");
//        button_no.setVisibility(View.GONE);
//
//        button_yes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v){
////                String child_path = "distortion_" + String.valueOf(position);
////                db.child("Chapter3").child("activity5").child(username).child(child_path).setValue("1");
////                openPopupWindow(v);
//                popupWindow.dismiss();
//            }
//        });
//
//        button_no.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v){
////                String child_path = "distortion_" + String.valueOf(position);
////                db.child("Chapter3").child("activity5").child(username).child(child_path).setValue("2");
////                openPopupWindow(v);
//                popupWindow.dismiss();
//            }
//        });
//    }


    private void initialize_Gridview() {
        // Gridview image initialize
        // TODO: image initialize based on status in database
        db.child("Chapter3").child("activity5").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                HashMap<String, String> hashmap_activity5 = (HashMap<String, String>) task.getResult().getValue();
//                hashmap_activity5 = hashmap_here;
                Log.d("Hashmap Check inside", String.valueOf(hashmap_activity5));
                if(!task.isSuccessful()){
                    Log.e("firebase_summary", "Error getting data", task.getException());
                }else{
                    Log.d("firebase_summary", String.valueOf(task.getResult().getValue()));
                    if(hashmap_activity5 != null){
                        String status_0 = hashmap_activity5.get("distortion_0");
                        String status_1 = hashmap_activity5.get("distortion_1");
                        String status_2 = hashmap_activity5.get("distortion_2");
                        String status_3 = hashmap_activity5.get("distortion_3");

                        Log.d("Status Check", hashmap_activity5.get("distortion_0"));

                        switch (status_0) {
                            case "1":
                                DistortionModelArrayList.get(0).setImgid(R.drawable.distortion_mental_filtering_1);
                                break;
                            case "2":
                                DistortionModelArrayList.get(0).setImgid(R.drawable.distortion_mental_filtering_2);
                                break;
                            default:
                                break;
                        }

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
                String status_0 = String.valueOf(snapshot.child("Chapter3").child("activity5").child(username).child("distortion_0").getValue());
                String status_1 = String.valueOf(snapshot.child("Chapter3").child("activity5").child(username).child("distortion_1").getValue());
                String status_2 = String.valueOf(snapshot.child("Chapter3").child("activity5").child(username).child("distortion_2").getValue());
                String status_3 = String.valueOf(snapshot.child("Chapter3").child("activity5").child(username).child("distortion_3").getValue());
//                String status_4 = String.valueOf(snapshot.child("Chapter3").child("activity5").child(username).child("distortion_4").getValue());

                switch (status_0) {
                    case "1":
                        DistortionModelArrayList.get(0).setImgid(R.drawable.distortion_mental_filtering_1);
                        break;
                    case "2":
                        DistortionModelArrayList.get(0).setImgid(R.drawable.distortion_mental_filtering_2);
                        break;
                    default:
                        break;
                }

                gridView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(Chapter3_Activity5_2_Activity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void open_Previous_Activity() {
        Intent intent = new Intent(this,Chapter3_Activity5_1_Activity.class);
        intent.putExtra("username", username);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    private void open_Next_Activity() {
        Intent intent = new Intent(this,Chapter3_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter3() {
        Intent intent = new Intent(this,Chapter3_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
