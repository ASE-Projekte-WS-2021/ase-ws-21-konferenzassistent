package com.example.myapplication;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.meetingwizard.RecyclerViewAdvancedCountdownAdapter;
import com.example.myapplication.meetingwizard.cdServiceObject;

import java.util.ArrayList;

public class RecycleViewPlaceholderAdapter extends RecyclerView.Adapter<RecycleViewPlaceholderAdapter.ViewHolder> {

    private final ArrayList<cdServiceObject> mCountdowns;
    Integer pastColor;
    Context mContext;
    private RecycleViewPlaceholderAdapter.ViewHolder lastItem;

    public RecycleViewPlaceholderAdapter(
            ArrayList<cdServiceObject> mCountdowns,
            Context mContext) {
        this.mCountdowns = mCountdowns;
        this.mContext = mContext;
        this.pastColor = mContext.getResources().getColor(R.color.corona_blue); // Start color
    }

    // For color change effect
    //https://stackoverflow.com/a/37667578
    private static int modifyBrightness(int color, float factor) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= factor;
        return Color.HSVToColor(hsv);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_placeholder_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        cdServiceObject countdownObject = mCountdowns.get(position);

        RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject countdown =
                countdownObject.getTimer();


        // Ignore if Timer is disabled
        if (countdown.getmEnabled()) {
            ViewCompat.setBackgroundTintList(holder.countdownContainer, ColorStateList.valueOf(pastColor));

            pastColor = modifyBrightness(pastColor, 0.8f);
            if (position < getItemCount() - 1)
                holder.countdownPastContainer.setBackgroundColor(pastColor);
            else {
                holder.countdownPastContainer.setBackgroundColor(mContext.getColor(R.color.transparent));
            }

            // Mark this as the last item
            lastItem = holder;

        } else {
            // check if there is an item before and if its the last entry
            if (lastItem != null && position == getItemCount() - 1) {
                lastItem.countdownPastContainer.setBackgroundColor(mContext.getColor(R.color.transparent));
            }
            holder.countdownPastContainer.setVisibility(View.GONE);
        }

        if (position == getItemCount() - 1) {
            pastColor = mContext.getResources().getColor(R.color.corona_blue); // reset color
        }
    }

    @Override
    public int getItemCount() {
        return mCountdowns.size();
    }

    private void pauseButtonToggle(ImageButton button, boolean enabled) {
        if (enabled)
            button.setBackground(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_baseline_play_arrow_24, null));
        else
            button.setBackground(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_baseline_pause_24, null));
    }

    // View holder Class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout countdownPastContainer;
        LinearLayout countdownContainer;

        RelativeLayout locationContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            countdownPastContainer = itemView.findViewById(R.id.countdown_last_container);
            countdownContainer = itemView.findViewById(R.id.countdown_container);
        }
    }

}