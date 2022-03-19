package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewCountdownPresetAdapter
        extends RecyclerView.Adapter<RecyclerViewCountdownPresetAdapter.ViewHolder> {

    private final ArrayList<CountdownPreset> countdownPresets;
    private final Context mContext;

    public RecyclerViewCountdownPresetAdapter(
            ArrayList<CountdownPreset> countdownPresets,
            Context mContext)
    {
        this.countdownPresets = countdownPresets;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_preset_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.presetName.setText(countdownPresets.get(holder.getAdapterPosition()).getTitle());

        holder.itemContainer.setOnClickListener(view -> {
        });
    }

    @Override
    public int getItemCount() {
        return countdownPresets.size();
    }

    // View holder Class
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView presetName;
        ImageView selectIndicator;
        RelativeLayout itemContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            presetName = itemView.findViewById(R.id.item_name);
            selectIndicator = itemView.findViewById(R.id.item_select_indicator);
            itemContainer = itemView.findViewById(R.id.item_container);
        }
    }
}
