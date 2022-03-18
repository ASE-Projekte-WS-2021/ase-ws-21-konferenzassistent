package com.example.myapplication;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.meetingwizard.RecyclerViewAdvancedCountdownAdapter;
import com.example.myapplication.meetingwizard.RecyclerViewAdvancedCountdownItemAdapter;

import java.util.ArrayList;

public class RecycleViewCountdownAdapter extends  RecyclerView.Adapter<RecycleViewCountdownAdapter.ViewHolder> {

    private final ArrayList<RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject> mCountdowns;
    private final Context mContext;
    private Integer pastColor;

    public RecycleViewCountdownAdapter(
            ArrayList<RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject> mCountdowns,
            Context mContext) {
        this.mCountdowns = mCountdowns;
        this.mContext = mContext;
        this.pastColor = mContext.getResources().getColor(R.color.corona_blue); // Start color
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_countdown_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject countdown =
                mCountdowns.get(holder.getAdapterPosition());

        RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem timer =
                countdown.getmItems().get(0);

        ViewCompat.setBackgroundTintList(holder.countdownContainer, ColorStateList.valueOf(pastColor));
        pastColor = modifyBrightness(pastColor, 0.8f);
        if(position < getItemCount() -1)
            holder.countdownPastContainer.setBackgroundColor(pastColor);
        else{
            holder.countdownPastContainer.setBackgroundColor(mContext.getColor(R.color.transparent));
        }

        holder.countdownName.setText(countdown.getmCountdownName());
        holder.countdownTimer.setText("" + timer.getSubCountdown());
        holder.countdownDescription.setText(timer.getSubCountdownDescription());

    }

    @Override
    public int getItemCount() {
        return mCountdowns.size();
    }

    // View holder Class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout countdownPastContainer;
        LinearLayout countdownContainer;
        TextView countdownName;
        TextView countdownTimer;
        TextView countdownDescription;
        ImageButton replayButton;
        ImageButton pauseButton;

        RelativeLayout locationContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            countdownPastContainer = itemView.findViewById(R.id.countdown_last_container);
            countdownContainer = itemView.findViewById(R.id.countdown_container);
            countdownName = itemView.findViewById(R.id.countdown_name);
            countdownTimer = itemView.findViewById(R.id.countdown_timer);
            countdownDescription = itemView.findViewById(R.id.countdown_description);
            replayButton = itemView.findViewById(R.id.replay_button);
            pauseButton = itemView.findViewById(R.id.pause_button);
        }
    }

    // For color change effect
    //https://stackoverflow.com/a/37667578
    private static int modifyBrightness(int color, float factor) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= factor;
        return Color.HSVToColor(hsv);

    }
}