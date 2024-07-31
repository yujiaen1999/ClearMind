package com.example.clearmind;

import android.util.Log;

import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.components.AxisBase;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.time.LocalDateTime;

public class DateAxisValueFormatter extends ValueFormatter {
    private final String[] dateStrings;

    public DateAxisValueFormatter(String[] dateStrings) {
        this.dateStrings = dateStrings;
    }

    @Override
    public String getFormattedValue(float value) {
        int index = (int) value;
        if (index >= 0 && index < dateStrings.length) {
            return dateStrings[index];
        } else {
            // Get the current date
            Date currentDate = new Date();
//            Log.d("Current Date", String.valueOf(currentDate));
            // Define the date format you want
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd", Locale.getDefault());
            // Format the date as a string
            String cur_dateString = dateFormat.format(currentDate);

            return cur_dateString;
        }
    }

    // Official example:
//    private String[] mValues;
//
//    public DateAxisValueFormatter(String[] values) {
//        this.mValues = values;
//    }
//
//    @Override
//    public String getFormattedValue(float value) {
//        // "value" represents the position of the label on the axis (x or y)
//        return mValues[(int) value];
//    }

}