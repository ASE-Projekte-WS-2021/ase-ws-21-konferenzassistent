package com.ase.konferenzassistent.countdown;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.konferenzassistent.R;
import com.ase.konferenzassistent.meetingwizard.RecyclerViewAdvancedCountdownItemAdapter;
import com.ase.konferenzassistent.meetingwizard.cdServiceObject;

import java.util.ArrayList;

public class RecycleViewCountdownAdapter extends RecyclerView.Adapter<RecycleViewCountdownAdapter.ViewHolder> {

    private final ArrayList<cdServiceObject> mCountdowns;
    private final Context mContext;
    private Integer pastColor;
    private ViewHolder lastItem;
    private final countDownButtonPressed listener;
    public RecycleViewCountdownAdapter(
            ArrayList<cdServiceObject> mCountdowns,
            countDownButtonPressed listener,
            Context mContext) {
        this.mCountdowns = mCountdowns;
        this.mContext = mContext;
        this.listener = listener;
        this.pastColor = ResourcesCompat.getColor(mContext.getResources(),R.color.corona_blue,null); // Start color
    }

    // For color change effect
    //https://stackoverflow.com/a/37667578
    private static int modifyBrightness(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= (float) 0.8;
        return Color.HSVToColor(hsv);

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

        cdServiceObject countdownObject = mCountdowns.get(position);

        AdvancedCountdownObject countdown = countdownObject.getTimer();
        RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem timer =
                countdown.getmItems().get(countdownObject.getCountdownPosition());

        // Ignore if Timer is disabled
        if (countdown.getmEnabled()) {
            ViewCompat.setBackgroundTintList(holder.countdownContainer, ColorStateList.valueOf(pastColor));

            pastColor = modifyBrightness(pastColor);
            if (position < getItemCount() - 1)
                holder.countdownPastContainer.setBackgroundColor(pastColor);
            else {
                holder.countdownPastContainer.setBackgroundColor(mContext.getColor(R.color.transparent));
            }

            holder.countdownName.setText(countdown.getmCountdownName());
            holder.countdownTimer.setText(timeStringBuilder(countdownObject.getCurrentTime()));
            holder.countdownDescription.setText(timer.getSubCountdownDescription());
            // Mark this as the last item
            lastItem = holder;

        } else {
            // check if there is an item before and if its the last entry
            if (lastItem != null && position == getItemCount() - 1) {
                lastItem.countdownPastContainer.setBackgroundColor(mContext.getColor(R.color.transparent));
            }
            holder.countdownPastContainer.setVisibility(View.GONE);
        }

        holder.replayButton.setEnabled(false);
        holder.pauseButton.setEnabled(true);
        ViewCompat.setBackgroundTintList(holder.replayButton, ColorStateList.valueOf(
                ResourcesCompat.getColor(mContext.getResources(), R.color.dark_gray, null)));

        // if timer is done enable the replay button
        if (countdownObject.getTimerDone()) {
            ViewCompat.setBackgroundTintList(holder.replayButton, ColorStateList.valueOf(
                    ResourcesCompat.getColor(mContext.getResources(), R.color.white, null)));
            holder.replayButton.setEnabled(true);
            holder.pauseButton.setEnabled(false);
        }

        holder.pauseButton.setOnClickListener(view -> {
            listener.pausePressed(holder.getAdapterPosition());
            pauseButtonToggle(holder.pauseButton, countdownObject.getTimerRunning());
        });

        holder.replayButton.setOnClickListener(view -> listener.restartPressed(holder.getAdapterPosition()));


        if (position == getItemCount() - 1) {
            pastColor = ResourcesCompat.getColor(mContext.getResources(), R.color.corona_blue, null); // reset color
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

    // Builds a String to show the Timer
    private String timeStringBuilder(long timer) {
        // Convert to minutes and seconds
        int minutes = (int) timer / 60000;
        int seconds = (int) timer % 60000 / 1000;

        // Build a string
        String timeLeft;

        timeLeft = "" + minutes;
        timeLeft += ":";
        // Add a leading 0 to seconds
        if (seconds < 10) timeLeft += "0";
        timeLeft += seconds;

        return timeLeft;
    }

    interface countDownButtonPressed {
        void pausePressed(int id);

        void restartPressed(int id);
    }

    // View holder Class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        final LinearLayout countdownPastContainer;
        final LinearLayout countdownContainer;
        final TextView countdownName;
        final TextView countdownTimer;
        final TextView countdownDescription;
        final ImageButton replayButton;
        final ImageButton pauseButton;


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
}