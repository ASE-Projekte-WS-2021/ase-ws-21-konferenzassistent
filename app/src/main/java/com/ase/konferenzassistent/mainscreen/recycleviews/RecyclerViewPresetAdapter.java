package com.ase.konferenzassistent.mainscreen.recycleviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.konferenzassistent.R;
import com.ase.konferenzassistent.mainscreen.MainScreenActivity;

import java.util.ArrayList;

public class RecyclerViewPresetAdapter
        extends RecyclerView.Adapter<RecyclerViewPresetAdapter.ViewHolder> {

    private final ArrayList<String> mPresetNames;
    private final ArrayList<Integer> mSelectIndicators;
    private final Context mContext;

    public RecyclerViewPresetAdapter(
            ArrayList<String> mPresetNames,
            ArrayList<Integer> mSelectIndicators,
            Context mContext) {
        this.mPresetNames = mPresetNames;
        this.mSelectIndicators = mSelectIndicators;
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
        holder.presetName.setText(mPresetNames.get(holder.getAdapterPosition()));
        holder.selectIndicator.setVisibility(mSelectIndicators.get(holder.getAdapterPosition()));

        holder.itemContainer.setOnClickListener(view -> {
            if (mContext instanceof MainScreenActivity) {
                ((MainScreenActivity) mContext).getMeetingAdapter().signalPresetChange(holder.getAdapterPosition());
            }

        });
    }

    @Override
    public int getItemCount() {
        return mPresetNames.size();
    }

    // View holder Class
    public static class ViewHolder extends RecyclerView.ViewHolder {
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
