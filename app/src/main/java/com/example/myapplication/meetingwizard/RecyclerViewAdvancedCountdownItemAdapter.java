package com.example.myapplication.meetingwizard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.io.Serializable;
import java.util.ArrayList;

public class RecyclerViewAdvancedCountdownItemAdapter
        extends RecyclerView.Adapter<RecyclerViewAdvancedCountdownItemAdapter.ViewHolder> {

    // Content
    private final ArrayList<AdvancedCountdownItem> mAdvancedCountdownItem;
    private final Context mContext;

    public RecyclerViewAdvancedCountdownItemAdapter(
            ArrayList<AdvancedCountdownItem> mAdvancedCountdownItem,
            Context mContext) {
        this.mAdvancedCountdownItem = mAdvancedCountdownItem;
        this.mContext = mContext;
    }

    public ArrayList<AdvancedCountdownItem> getmAdvancedCountdownItem() {
        return mAdvancedCountdownItem;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.subCountdownDescription.setText(mAdvancedCountdownItem.get(holder.getAdapterPosition()).subCountdownDescription);
        holder.subCountdownPicker.setValue(Math.toIntExact(mAdvancedCountdownItem.get(holder.getAdapterPosition()).subCountdown));

        // If there is only one item remove description
        if (mAdvancedCountdownItem.size() == 1) {
            holder.descriptionContainer.setVisibility(View.GONE);
        }

        holder.subCountdownPicker.setOnValueChangedListener(
                (numberPicker, i, i1) -> mAdvancedCountdownItem.get(holder.getAdapterPosition()).subCountdown = (long) i1);
    }

    @Override
    public int getItemCount() {
        return mAdvancedCountdownItem.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_advanced_countdown_picker_items,
                parent, false);
        return new ViewHolder(view);
    }

    public static class AdvancedCountdownItem implements Serializable {
        Long subCountdown;
        String subCountdownDescription;

        public AdvancedCountdownItem(Long subCountdown,
                                     String onSubCountdownDescription) {
            this.subCountdown = subCountdown;
            this.subCountdownDescription = onSubCountdownDescription;
        }

        public Long getSubCountdown() {
            return subCountdown;
        }

        public void setSubCountdown(Long subCountdown) {
            this.subCountdown = subCountdown;
        }

        public String getSubCountdownDescription() {
            return subCountdownDescription;
        }

        public void setSubCountdownDescription(String subCountdownDescription) {
            this.subCountdownDescription = subCountdownDescription;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView subCountdownDescription;
        LinearLayout descriptionContainer;
        CustomNumberPicker subCountdownPicker;

        public ViewHolder(View countdownView) {
            super(countdownView);
            subCountdownDescription = countdownView.findViewById(R.id.countdown_recycler_picker_description);
            subCountdownPicker = countdownView.findViewById(R.id.countdown_recycler_picker_item);
            descriptionContainer = countdownView.findViewById(R.id.countdown_recycler_description_container);
        }

    }


}
