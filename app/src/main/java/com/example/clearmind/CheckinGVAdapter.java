package com.example.clearmind;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CheckinGVAdapter extends ArrayAdapter<CheckinModel> {

    public CheckinGVAdapter(@NonNull Context context, ArrayList<CheckinModel> courseModelArrayList) {
        super(context, 0, courseModelArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.card_item3, parent, false);
        }

        CheckinModel courseModel = getItem(position);
        TextView courseTV = listitemView.findViewById(R.id.idTVCourse);
        ImageView courseIV = listitemView.findViewById(R.id.idIVcourse);
        TextView score = listitemView.findViewById(R.id.score);

        DateFormat dateFormat = new SimpleDateFormat("MM/dd");
        Date current_date = new Date();
        String dateAsString = dateFormat.format(current_date);

        // Highlight the current date
        if (dateAsString.equals(courseModel.get_date())){
            listitemView.setBackgroundResource(R.drawable.check_mark_edge);
        }

        courseTV.setText(courseModel.get_date());
        courseIV.setImageResource(courseModel.getImgid());
        score.setText(courseModel.get_score());
        return listitemView;
    }
}
