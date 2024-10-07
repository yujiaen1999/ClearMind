package com.example.clearmind;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

public class DragValueAdapter extends RecyclerView.Adapter<DragValueAdapter.ValueViewHolder> {
    private List<String> values;
    private final OnStartDragListener startDragListener;

    public DragValueAdapter(List<String> values, OnStartDragListener startDragListener) {
        this.values = values;
        this.startDragListener = startDragListener;
    }

    @NonNull
    @Override
    public ValueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_chapter1_activity0_item_value, parent, false);
        return new ValueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ValueViewHolder holder, int position) {
        holder.textView.setText(values.get(position));
        holder.itemView.setOnLongClickListener(v -> {
            startDragListener.onStartDrag(holder);
            return true;
        });
    }

    // Method to handle the item movement
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(values, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(values, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    public List<String> getValues() {
        return values;
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public static class ValueViewHolder extends RecyclerView.ViewHolder {
        public TextView textView; // make public if needed

        public ValueViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_value);
        }
    }
}
