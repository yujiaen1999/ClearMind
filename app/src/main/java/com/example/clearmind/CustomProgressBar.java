package com.example.clearmind;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomProgressBar extends ProgressBar {

    private int completeNum;
    private int progressValue;
//    private String username;
    private String completeRatio;
    private DatabaseReference db;

    public CustomProgressBar(Context context) {
        super(context);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setProgressValue(int value){
        completeNum = value;
    }
//    public void getUsername(String name){
//        username = name;
//    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Calculate the percentage
//        int progress = getProgress();
        int progress = completeNum;
//        int max = getMax();
        int max = 100;
        float percentage = (float) progress / max;

//        // Draw the percentage text on the canvas
////        String progressText = (int) (percentage * 100) + "%";
//        completeRatio = completeNum + "/31";
//
//        Paint textPaint = new Paint();
//        textPaint.setAntiAlias(true);
//        int spSize = 12;  // change the size of sp
//        float scaledSizeInPixels = spSize * getResources().getDisplayMetrics().scaledDensity;
//        textPaint.setTextSize(scaledSizeInPixels); // Adjust the text size as needed
//        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
//        textPaint.setColor(getResources().getColor(android.R.color.black)); // Text color
//        textPaint.setTextAlign(Paint.Align.CENTER);
//
//        // Get the center of the progress bar
//        int x = getWidth() / 2 + 50;
//        int y = getHeight() / 2 - 10;
//
//        // Calculate the position to center the text
//        float textWidth = textPaint.measureText(completeRatio);
//        float textX = x - textWidth / 2;
//        float textY = y;
//
//        canvas.drawText(completeRatio, textX, textY, textPaint);
//
//        textY += textPaint.getTextSize();
//
//        // Draw the bottom text
//        canvas.drawText("completed", textX, textY, textPaint);
    }

}

