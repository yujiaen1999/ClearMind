package com.example.clearmind;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NavigationDrawerHelper {
    private final Activity activity;
    private FrameLayout navigationDrawer;
    private View navigationButton;
    private View dimmingOverlay;
    private boolean isDrawerOpen = false;
    private String username;
    private final DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    private static final int[][] LINK_IDS = {
            {R.id.opening1, R.id.activity1_0, R.id.activity1_1, R.id.activity1_2, R.id.activity1_3, R.id.activity1_4, R.id.activity1_5},
            {R.id.opening2, R.id.activity2_1, R.id.activity2_2, R.id.activity2_3, R.id.activity2_4, R.id.activity2_5, R.id.activity2_6},
            {R.id.opening3, R.id.activity3_1, R.id.activity3_2, R.id.activity3_3, R.id.activity3_4, R.id.activity3_5, R.id.activity3_6, R.id.activity3_7, R.id.activity3_8},
            {R.id.opening4, R.id.activity4_1, R.id.activity4_2, R.id.activity4_3, R.id.activity4_4, R.id.activity4_5, R.id.activity4_6}
    };

    private static final String[][] LINK_TEXTS = {
            {"Opening", "Prioritize Your Life Values", "Discover Procrastination Reasons", "Procrastination Tendencies", "Tendencies Questions", "How to Use the App", "Summary"},
            {"Opening", "Your Challenging Emotions", "Passengers On The Bus", "Example of Driving the bus", "Identify your passengers", "Willingness to Carry On", "Summary"},
            {"Opening", "Label the Passengers on the Bus", "Identify how it feels in your body", "Learn How to Meditate", "Make a Belief Statement", "Be aware of cognitive distortions", "Summary of cognitive distortions", "Reflection on cognitive distortions", "Summary"},
            {"Opening", "Introduce a New Framework", "Commit to a new goal", "Identify your passengers", "Willingness to carry on", "Summary", "Introduction to Goal Tracker"}
    };

    private static final Class<?>[][] TARGET_ACTIVITIES = {
            {Chapter1_Opening_Activity.class, Chapter1_Activity0_Activity.class, Chapter1_Activity1_Activity.class, Chapter1_Activity2_Activity.class, Chapter1_Activity2_Question_Activity.class, Chapter1_Time_Activity.class, Chapter1_Summary_Activity.class},
            {Chapter2_Opening_Activity.class, Chapter2_Activity1_Activity.class, Chapter2_Activity2_1_Activity.class, Chapter2_Activity2_2Q_Activity.class, Chapter2_Diagram_Activity.class, Chapter2_Activity2_5_Activity.class, Chapter2_Summary_Activity.class},
            {Chapter3_Opening_Activity.class, Chapter3_Activity1_Activity.class, Chapter3_Activity2_Activity.class, Chapter3_Activity3_new_Activity.class, Chapter3_Activity4_Activity.class, Chapter3_Activity5_1_Activity.class, Chapter3_Activity5_sum_Activity.class, Chapter3_Activity6_Activity.class, Chapter3_Summary_Activity.class},
            {Chapter4_Opening_Activity.class, Chapter4_Activity1_Activity.class, Chapter4_Activity2_Activity.class, Chapter4_Activity3_Activity.class, Chapter4_Activity4_Activity.class, Chapter4_Summary_Activity.class, Chapter4_Activity5_Activity.class}
    };

    private static final String[] CHAPTERS = {"Chapter1", "Chapter2", "Chapter3", "Chapter4"};

    private static final String[][] ACTIVITIES_ORDER = {
            // Chapter 1
            {"1_Opening", "2_Activity1_0", "2_Activity1_1", "3_Activity1_2_Discover", "4_Activity1_2_Questions", "6_Time_Management", "7_Summary"},
            // Chapter 2
            {"1_Opening", "2_Activity2_1", "3_Passengers_On_The_Bus", "4_Example", "6_Diagram", "7_Willingness_to_Carry_On", "8_Summary"},
            // Chapter 3
            {"1_Opening", "2_Activity3_1", "3_Activity3_2", "4_Activity3_3", "5_Activity3_4", "6_Activity3_5_1", "7_Activity3_5_2", "8_Activity3_6", "9_Summary"},
            // Chapter 4
            {"1_Opening", "2_Activity4_1", "3_Activity4_2", "4_Activity4_3", "5_Activity4_4", "6_Summary", "7_Activity4_5_intro_tracker"}
    };


    public NavigationDrawerHelper(Activity activity) {
        this.activity = activity;
    }

    public void setupNavigationDrawer(String username) {
        this.username = username;
        navigationButton = activity.findViewById(R.id.button_navigation);
        navigationDrawer = activity.findViewById(R.id.navigation_drawer);

        dimmingOverlay = new View(activity);
        int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
        int overlayWidth = screenWidth / 4;

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(overlayWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.START;
        dimmingOverlay.setLayoutParams(params);
        dimmingOverlay.setBackgroundColor(Color.parseColor("#80000000")); // 半透明黑色
        dimmingOverlay.setVisibility(View.GONE);
        dimmingOverlay.setOnClickListener(v -> {
            if (isDrawerOpen) {
                collapseDrawer();
            }
        });

        ViewGroup contentView = activity.findViewById(android.R.id.content);
        int drawerIndex = contentView.indexOfChild(navigationDrawer);
        contentView.addView(dimmingOverlay, drawerIndex);
        contentView.bringChildToFront(navigationDrawer);

        if (navigationButton != null) {
            navigationButton.setOnClickListener(v -> {
                if (!isDrawerOpen) {
                    expandDrawer();
                } else {
                    collapseDrawer();
                }
            });
        }

        initializeNavigationLinks();
        getData();
    }

    private void initializeNavigationLinks() {
        List<List<String>> defaultStatuses = new ArrayList<>();

        for (int i = 0; i < CHAPTERS.length; i++) {
            List<String> subDefaultStatuses = new ArrayList<>();
            for(int j = 0; j < ACTIVITIES_ORDER[i].length; j++) {
                subDefaultStatuses.add("0");
            }
            defaultStatuses.add(subDefaultStatuses);
        }
        updateNavigationLinks(defaultStatuses, new int[]{-1, -1});
    }

    public void getData() {
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<List<String>> statuses = new ArrayList<>();
                int[] lastPos = new int[]{-1, -1};

                for (int i = 0; i < CHAPTERS.length; i++) {
                    List<String> subStatuses = new ArrayList<>();
                    for(int j = 0; j < ACTIVITIES_ORDER[i].length; j++) {
                        String status = getStatus(snapshot, CHAPTERS[i], ACTIVITIES_ORDER[i][j]);
                        subStatuses.add(status);
                        if ("1".equals(status)) {
                            lastPos[0] = i;
                            lastPos[1] = j;
                        }
                    }
                    statuses.add(subStatuses);
                }
                lastPos = getNextCoordinate(lastPos);
                updateNavigationLinks(statuses, lastPos);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
            }
        });
    }

    public static int[] getNextCoordinate(int[] currentCoordinate) {
        int currentRow = currentCoordinate[0];
        int currentColumn = currentCoordinate[1];

        if (currentRow == -1 && currentColumn == -1) {
            return new int[]{0, 0};
        } else if (currentColumn < ACTIVITIES_ORDER[currentRow].length - 1) {
            // Same row, next column
            return new int[]{currentRow, currentColumn + 1};
        } else if (currentRow < ACTIVITIES_ORDER.length - 1) {
            // Next row, first column
            return new int[]{currentRow + 1, 0};
        } else {
            // No next coordinate (you can handle this case as you wish)
            return new int[]{-1, -1};
        }
    }

    private String getStatus(DataSnapshot snapshot, String chapter, String activity) {
        Object value = snapshot.child(chapter).child("progress").child(username).child(activity).getValue();
        return value != null ? value.toString() : "0"; // Default value "0"
    }

    private void updateNavigationLinks(List<List<String>> statuses, int[] lastPos) {
        for (int i = 0; i < LINK_IDS.length; i++) {
            for(int j = 0; j < LINK_IDS[i].length; j++) {
                setNavigationLink(i, j, LINK_IDS[i][j], TARGET_ACTIVITIES[i][j], LINK_TEXTS[i][j], statuses, lastPos);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void setNavigationLink(int chapterPos, int sectionPos, int linkId, final Class<?> targetActivity, String linkText, List<List<String>> statuses, int[] lastPos) {
        View linkView = navigationDrawer.findViewById(linkId);
        String status = statuses.get(chapterPos).get(sectionPos);

        if (linkView instanceof TextView) {
            TextView textView = (TextView) linkView;
            if (status != null && status.equals("1")) {
                textView.setText("√ " + linkText);
                linkView.setOnClickListener(v -> {
                    Intent intent = new Intent(activity, targetActivity);
                    intent.putExtra("username", username);
                    activity.startActivity(intent);
                    collapseDrawer();
                });
            } else {
                if (chapterPos == lastPos[0] && sectionPos == lastPos[1]) {
                    textView.setText("→ " + linkText);

                    GradientDrawable background = new GradientDrawable();
                    background.setShape(GradientDrawable.RECTANGLE);
                    background.setColor(Color.parseColor("#FFA500"));
                    background.setCornerRadius(16);
                    textView.setBackground(background);

                    textView.setTypeface(null, Typeface.BOLD);

                    linkView.setOnClickListener(v -> {
                        Intent intent = new Intent(activity, targetActivity);
                        intent.putExtra("username", username);
                        activity.startActivity(intent);
                        collapseDrawer();
                    });
                } else {
                    textView.setText("○ " + linkText);
                }
            }
        } else {
            Log.e("NavigationDrawerHelper", "LinkView is not a TextView for " + linkId);
        }
    }

    private void expandDrawer() {
        isDrawerOpen = true;
        navigationButton.setVisibility(View.GONE);
        navigationDrawer.setVisibility(View.VISIBLE);
        navigationDrawer.getLayoutParams().width = 0;

        dimmingOverlay.setVisibility(View.GONE);
        dimmingOverlay.setAlpha(0f);

        ViewGroup contentView = activity.findViewById(android.R.id.content);
        contentView.bringChildToFront(dimmingOverlay);
        contentView.bringChildToFront(navigationDrawer);

        final int targetWidth = activity.getResources().getDisplayMetrics().widthPixels * 3 / 4;

        Animation expandAnimation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                navigationDrawer.getLayoutParams().width = interpolatedTime == 1
                        ? targetWidth
                        : (int) (targetWidth * interpolatedTime);
                navigationDrawer.requestLayout();
            }
        };

        expandAnimation.setDuration(300); // 300ms for smooth expansion
        expandAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // No-op
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dimmingOverlay.setVisibility(View.VISIBLE);
                ObjectAnimator fadeInAnimator = ObjectAnimator.ofFloat(dimmingOverlay, "alpha", 0f, 1f);
                fadeInAnimator.setDuration(100);
                fadeInAnimator.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // No-op
            }
        });

        navigationDrawer.startAnimation(expandAnimation);
    }

    private void collapseDrawer() {
        isDrawerOpen = false;
        navigationButton.setVisibility(View.VISIBLE);

        ObjectAnimator fadeOutAnimator = ObjectAnimator.ofFloat(dimmingOverlay, "alpha", 1f, 0f);
        fadeOutAnimator.setDuration(100);
        fadeOutAnimator.start();
        fadeOutAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                dimmingOverlay.setVisibility(View.GONE);

                final int initialWidth = navigationDrawer.getWidth();

                Animation collapseAnimation = new Animation() {
                    @Override
                    protected void applyTransformation(float interpolatedTime, Transformation t) {
                        if (interpolatedTime == 1) {
                            navigationDrawer.setVisibility(View.GONE);
                        } else {
                            navigationDrawer.getLayoutParams().width = initialWidth - (int) (initialWidth * interpolatedTime);
                            navigationDrawer.requestLayout();
                        }
                    }
                };

                collapseAnimation.setDuration(300); // 300ms for smooth collapse
                navigationDrawer.startAnimation(collapseAnimation);
            }
        });
    }
}
