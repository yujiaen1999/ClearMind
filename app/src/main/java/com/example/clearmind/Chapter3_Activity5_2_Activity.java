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
    // Done: Magnification example missing!!!
    // Done: Emotional Reasoning solution need confirm
    String[] distortion_name = {"Mental filtering", "All-or-nothing thinking", "Overgeneralization", "Discounting the positive", "Jumping to Conclusions", "Magnification", "Emotional Reasoning", "“Should” Statements", "Labeling", "Personalization and Blame"};
    String[] distortion_intro = {"Mental filtering is like viewing the world through a negative lens. It involves focusing solely on the negative aspects while entirely disregarding the positive things in one's life.",
            "All-or-nothing thinking is also known as polarized thinking. This type of thinking involves perceiving situations and outcomes in rigid and extreme terms. All good or all bad, all right or all wrong, everything or nothing, success or failure. This distortion can result in unrealistic standards for yourself and others, potentially affecting your relationships and motivation.",
            "Overgeneralization thinking occurs when an individual forms a rule or draws a conclusion based on a single event or a lone piece of negative evidence. In this thought pattern, words like “always,” “never,” “forever,” often dominate their thinking. This cognitive distortion can lead to mistaken belief that all similar future events will result in the same failure or negative experience.",
            "Discounting the positive is when a person doesn't fully accept or believe the good things that have happened to them. It's somewhat similar to mental filtering; however, these good things are actively invalidated or rejected in discounting the positive instead of simply being ignored.",
            "There are two different types for this distortion, mind reading and fortune telling. <br><br>1. Mind reading: This distortion occurs when you presume that you have the ability to read others' minds and understand their thoughts. It involves anticipating a specific reaction from someone or attributing thoughts to them that may not be their actual thinking. <br><br>2. Fortune telling: This distortion arises when you make predictions about how events will unfold in a specific way, based on little or no evidence. This type of thinking is usually a way to avoid facing challenging situations or tasks.",
            "Magnification is an over-exaggeration of a thought. It arises when you make your problems and flaws seem much bigger and more important than they really are. This kind of thinking can cause worries to grow rapidly and imagine the worst possible outcomes. Magnification tends to happen when there are uncertainties beyond a person's control.",
            "Emotional reasoning is the way of evaluating yourself or a situation based on emotions. It assumes that if you have negative emotions, it must be an accurate reflection of reality and abilities. For instance, if you feel guilty, emotional reasoning would make you believe that you are inherently a bad person.",
            "\"Should\" statements are thoughts that create a sense of obligation or unrealistic expectations about how you or others should behave or how things should be. Instead of approaching situations with flexibility and understanding, individuals who frequently use \"should\" statements impose rigid rules and judgments on themselves and others, which can lead to self-criticism, anxiety, and depression.",
            "Labeling involves making negative generalizations and classifications about oneself or others based on an undesirable event or a few isolated incidents. Labeling is an extreme form of overgeneralization, which can lead to unfair and harsh criticism.",
            "Personalization manifests when you assign responsibility to yourself for situations that are irrelevant to your actions or are beyond your control. Individuals prone to this cognitive distortion tend to perceive situations as personal even when they lack a direct connection, which can result in self-blame and self-criticism."
    };
    String[] distortion_example = {"You gave a presentation at work, and your colleagues gave you positive feedback, saying your ideas were awesome and you explained things well. However, one person offered a minor critique about your slides for potential improvement. Instead of recognizing the positive feedback, you focused solely on that one piece of criticism, feeling like your entire presentation was a failure.",
            "Jack set a fitness goal of exercising three times a week. However, in the final week, he was busy at school and couldn’t make it to the gym at all. This led him to perceive his entire fitness journey as a failure and gave up on both his fitness goals and routines.",
            "Annie scratched her car twice on the curb while parking in two separate instances. Based on these two incidents, her parents concluded that she was a terrible driver and would keep damaging her car in the future.",
            "Stella did exceptionally well in her Physics class and received top grades consistently. However, she consistently downplays her accomplishments, believing that her success is solely due to easy tests or luck, not due to her hard work.",
            "1. Justin sent a text message to his girlfriend, but she didn’t respond immediately. Justin immediately assumed she was upset and wanted to break up with him. In reality, his girlfriend was busy with work and had many meetings. <br><br>2. Sara has a job interview coming up, but she has already convinced herself that she will do horribly and not get the job. She starts to worry excessively about it, assuming that her future is ruined, even though the interview hasn't happened yet.",
            "After a minor disagreement with a friend, you started believing that you had damaged your friendship irreparably. You worried that your friend now hated you and feared that no one else would ever want to be friends with you.",
            "Lisa wakes up feeling anxious and worried about an upcoming presentation at work. She thinks to herself, \"I feel so anxious; I must be terrible at public speaking.\"",
            "You believe that a person in a leadership role should always make decisions quickly and confidently, never showing any uncertainty or seeking input from others.",
            "You notice your new teammate eating pizza after school, and you immediately label them as \"unhealthy\" based solely on their food choice.",
            "Your friend gets into a minor car accident, and you immediately blame yourself for having recommended the route he took."
    };
    String[] distortion_solution = {"Journaling can be a valuable method to help overcome mental filtering. Keeping a diary or journal that involves writing down your thoughts and emotions about the events in your life will help redirect your attention away from the negatives and actively seek out the neutral or positive aspects of a situation.",
            "To overcome All-or-nothing thinking, it's essential to understand that success and progress are not binary, all-or-nothing concepts. You need to acknowledge and address this type of thinking, and by substituting self-defeating thoughts, you can become more aware of your strengths and focus more on your progress instead of outcomes.",
            "To counter overgeneralization, it's helpful to use more realistic language with a positive tone. Instead of saying, \"I always do that!\" you can say something like, \"That happens occasionally, but I'm committed to improving in the future.\"",
            "To conquer this cognitive distortion, try changing how you explain events. Instead of dismissing positive results as luck, pay more attention to how your strengths, abilities, and hard work played a role in achieving them.",
            "To conquer this cognitive distortion, you can follow the following steps: <br><br>1. Check the facts: Gather as much information as possible before making judgments or decisions. <br>2. Challenge your thoughts: Actively question your assumptions and explore alternative explanations. <br>3. Communication: Instead of assuming what others think, communicate your concerns and seek direct answers to avoid confusion or misunderstanding. <br>4. Try an alternative viewpoint: View the situation from an outsider's standpoint, considering the information needed for a more accurate understanding.",
            "To overcome magnification, you can work on recognizing these exaggerated thoughts and deliberately replacing them with more constructive and positive thinking patterns.",
            "You can practice defusion to get distance from your emotions. For example, instead of “I feel overwhelmed”, you tell yourself, “I’m aware that I’m feeling overwhelmed.” This simple shift can promote a more objective and balanced perspective on your emotions.",
            "One way to combat this cognitive distortion is to concentrate on cultivating self-compassion. Swap those unrealistic thoughts with more practical ones and focus on embracing yourself for who you truly are, not who you believe you should be.",
            "To overcome labeling, challenge the validity of your assumptions. Find evidence contradicting your negative thinking, and remember the distinction between personal opinions and objective facts.",
            "To combat personalization and blame, explore additional factors that could have influenced your situation. Instead of placing all the blame on yourself, consider external circumstances or the actions of others that might have also played a part."
    };
    int[] distortion_image = {R.drawable.distortion_mental_filtering_1, R.drawable.distortion_all_or_nothing_1, R.drawable.distortion_overgeneralization_1, R.drawable.distortion_discounting_the_positive_1, R.drawable.distortion_jumping_to_conclusions_1, R.drawable.distortion_magnification_1, R.drawable.distortion_emotional_reasoning_1, R.drawable.distortion_should_1, R.drawable.distortion_labeling_1, R.drawable.distortion_personalization_and_blame_1};

    int[] distortion_disabled_image = {R.drawable.distortion_mental_filtering_2, R.drawable.distortion_all_or_nothing_2, R.drawable.distortion_overgeneralization_2, R.drawable.distortion_discounting_the_positive_2, R.drawable.distortion_jumping_to_conclusions_2, R.drawable.distortion_magnification_2, R.drawable.distortion_emotional_reasoning_2, R.drawable.distortion_should_2, R.drawable.distortion_labeling_2, R.drawable.distortion_personalization_and_blame_2};

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
        // Done: need to be initialized somewhere
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

        db.child("Chapter3").child("activity5").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                HashMap<String, String> hashmap_activity5 = (HashMap<String, String>) task.getResult().getValue();
//                hashmap_activity5 = hashmap_here;
                Log.d("Hashmap Check inside", String.valueOf(hashmap_activity5));
                if(!task.isSuccessful()){
                    Log.e("firebase_activity5", "Error getting data", task.getException());
                }else{
                    Log.d("firebase_activity5", String.valueOf(task.getResult().getValue()));
                    if(hashmap_activity5 == null){
                        // Initialize the db activity5
                        Log.d("firebase_activity5", "Initialized the db activity5");

                        Map<String, Object> activity_update = new HashMap<>();
                        activity_update.put("distortion_0", "0");
                        activity_update.put("distortion_1", "0");
                        activity_update.put("distortion_2", "0");
                        activity_update.put("distortion_3", "0");
                        activity_update.put("distortion_4", "0");
                        activity_update.put("distortion_5", "0");
                        activity_update.put("distortion_6", "0");
                        activity_update.put("distortion_7", "0");
                        activity_update.put("distortion_8", "0");
                        activity_update.put("distortion_9", "0");

                        db.child("Chapter3").child("activity5").child(username).updateChildren(activity_update);
                    }
                }
            }
        });


        // Set up Gridview
        gridView = findViewById(R.id.gridview);
//        ArrayList<DistortionModel> DistortionModelArrayList = new ArrayList<DistortionModel>();
        DistortionModelArrayList = new ArrayList<DistortionModel>();

        // Done: Initialize gridview (add more)
        DistortionModelArrayList.add(new DistortionModel(distortion_name[0], R.drawable.distortion_question));
        DistortionModelArrayList.add(new DistortionModel(distortion_name[1], R.drawable.distortion_question));
        DistortionModelArrayList.add(new DistortionModel(distortion_name[2], R.drawable.distortion_question));
        DistortionModelArrayList.add(new DistortionModel(distortion_name[3], R.drawable.distortion_question));
        DistortionModelArrayList.add(new DistortionModel(distortion_name[4], R.drawable.distortion_question));
        DistortionModelArrayList.add(new DistortionModel(distortion_name[5], R.drawable.distortion_question));
        DistortionModelArrayList.add(new DistortionModel(distortion_name[6], R.drawable.distortion_question));
        DistortionModelArrayList.add(new DistortionModel(distortion_name[7], R.drawable.distortion_question));
        DistortionModelArrayList.add(new DistortionModel(distortion_name[8], R.drawable.distortion_question));
        DistortionModelArrayList.add(new DistortionModel(distortion_name[9], R.drawable.distortion_question));


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
                // Done: set item click listener to popup window
                openPopupWindow(view, position);
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
                db.child("Chapter3").child("activity5").child(username).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        HashMap<String, String> distortion_status = (HashMap<String, String>) task.getResult().getValue();
                        Log.d("distortion_status Check", String.valueOf(distortion_status));
                        if(!task.isSuccessful()){
                            Log.e("firebase_activity5", "Error getting data", task.getException());
                        }else{
                            Log.d("firebase_activity5", String.valueOf(task.getResult().getValue()));
                            if(distortion_status != null){
                                // Check all status before go next page
                                Boolean next_page_flage = true;

                                for (String value : distortion_status.values()) {
                                    Log.d("Test status: ", value);
                                    if(value.equals("0")){
                                        next_page_flage = false;
                                    }
                                }

                                if (next_page_flage){
                                    // update Chapter progress
                                    Map<String, Object> chapter_progress_update = new HashMap<>();
                                    chapter_progress_update.put("6_Activity3_5_1", "1");
                                    db.child("Chapter3").child("progress").child(username).updateChildren(chapter_progress_update);

                                    open_Next_Activity();
                                } else{
                                    Toast.makeText(Chapter3_Activity5_2_Activity.this, "Explore all cognitive distortions to proceed to the next page", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });

//                // update Chapter progress
//                Map<String, Object> chapter_progress_update = new HashMap<>();
//                chapter_progress_update.put("6_Activity3_5_1", "1");
//                db.child("Chapter3").child("progress").child(username).updateChildren(chapter_progress_update);
//
//                open_Next_Activity();
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
        TextView textView_intro = (TextView) viewPopupWindow.findViewById(R.id.textView_intro);
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
                textView_intro.setVisibility(View.GONE);
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

    private void icon_setup(String status, int position) {
        switch (status) {
            case "1":
                DistortionModelArrayList.get(position).setImgid(distortion_image[position]);
                break;
            case "2":
                DistortionModelArrayList.get(position).setImgid(distortion_disabled_image[position]);
                break;
            default:
                break;
        }

    }

    private void initialize_Gridview() {
        // Gridview image initialize
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
                        String status_4 = hashmap_activity5.get("distortion_4");
                        String status_5 = hashmap_activity5.get("distortion_5");
                        String status_6 = hashmap_activity5.get("distortion_6");
                        String status_7 = hashmap_activity5.get("distortion_7");
                        String status_8 = hashmap_activity5.get("distortion_8");
                        String status_9 = hashmap_activity5.get("distortion_9");

                        icon_setup(status_0, 0);
                        icon_setup(status_1, 1);
                        icon_setup(status_2, 2);
                        icon_setup(status_3, 3);
                        icon_setup(status_4, 4);
                        icon_setup(status_5, 5);
                        icon_setup(status_6, 6);
                        icon_setup(status_7, 7);
                        icon_setup(status_8, 8);
                        icon_setup(status_9, 9);
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
                String status_4 = String.valueOf(snapshot.child("Chapter3").child("activity5").child(username).child("distortion_4").getValue());
                String status_5 = String.valueOf(snapshot.child("Chapter3").child("activity5").child(username).child("distortion_5").getValue());
                String status_6 = String.valueOf(snapshot.child("Chapter3").child("activity5").child(username).child("distortion_6").getValue());
                String status_7 = String.valueOf(snapshot.child("Chapter3").child("activity5").child(username).child("distortion_7").getValue());
                String status_8 = String.valueOf(snapshot.child("Chapter3").child("activity5").child(username).child("distortion_8").getValue());
                String status_9 = String.valueOf(snapshot.child("Chapter3").child("activity5").child(username).child("distortion_9").getValue());

                icon_setup(status_0, 0);
                icon_setup(status_1, 1);
                icon_setup(status_2, 2);
                icon_setup(status_3, 3);
                icon_setup(status_4, 4);
                icon_setup(status_5, 5);
                icon_setup(status_6, 6);
                icon_setup(status_7, 7);
                icon_setup(status_8, 8);
                icon_setup(status_9, 9);

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
        Intent intent = new Intent(this,Chapter3_Activity5_sum_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    private void open_Chapter3() {
        Intent intent = new Intent(this,Chapter3_Activity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }
}
