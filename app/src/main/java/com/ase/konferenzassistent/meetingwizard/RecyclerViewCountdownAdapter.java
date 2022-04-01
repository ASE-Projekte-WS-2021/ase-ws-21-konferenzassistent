package com.ase.konferenzassistent.meetingwizard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.konferenzassistent.R;
import com.ase.konferenzassistent.shared.CustomNumberPicker;

import java.util.ArrayList;

public class RecyclerViewCountdownAdapter
        extends RecyclerView.Adapter<RecyclerViewCountdownAdapter.ViewHolder> {

    // Content
    private final ArrayList<String> mCountdownNames;
    private final ArrayList<Long> mCountdownTime;
    private final ArrayList<Boolean> mEnabled;
    private final Context mContext;

    public RecyclerViewCountdownAdapter(
            ArrayList<String> mCountdownNames,
            ArrayList<Long> mCountdownTime,
            ArrayList<Boolean> mEnabled,
            Context mContext) {
        this.mCountdownNames = mCountdownNames;
        this.mCountdownTime = mCountdownTime;
        this.mEnabled = mEnabled;
        this.mContext = mContext;
    }

    public ArrayList<String> getmCountdownNames() {
        return mCountdownNames;
    }

    public ArrayList<Long> getmCountdownTime() {
        return mCountdownTime;
    }

    public ArrayList<Boolean> getmEnabled() {
        return mEnabled;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.countdownName.setText(mCountdownNames.get(holder.getAdapterPosition()));
        holder.aSwitch.setChecked(mEnabled.get(holder.getAdapterPosition()));
        holder.timePicker.setValue(Math.toIntExact(mCountdownTime.get(holder.getAdapterPosition())));

        // Setup the container visibility
        holder.countdownContainer.setVisibility(holder.aSwitch.isChecked() ? View.VISIBLE : View.GONE);

        // Set on click listener on the switch
        holder.aSwitch.setOnClickListener(view1 -> {
            if (!holder.aSwitch.isChecked()) {
                mEnabled.set(holder.getAdapterPosition(), false);
                holder.countdownContainer.setVisibility(View.GONE);
            } else {
                mEnabled.set(holder.getAdapterPosition(), true);
                holder.countdownContainer.setVisibility(View.VISIBLE);
            }
        });

        holder.timePicker.setOnValueChangedListener(
                (numberPicker, i, i1) -> mCountdownTime.set(holder.getAdapterPosition(), (long) i1));
    }

    @Override
    public int getItemCount() {
        return mCountdownNames.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_countdown_picker,
                parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView countdownName;
        CustomNumberPicker timePicker;
        SwitchCompat aSwitch;
        LinearLayout countdownContainer;

        public ViewHolder(View countdownView) {
            super(countdownView);
            countdownName = countdownView.findViewById(R.id.countdown_recycler_name);
            timePicker = countdownView.findViewById(R.id.countdown_recycler_picker);
            aSwitch = countdownView.findViewById(R.id.countdown_recycler_switch);
            countdownContainer = countdownView.findViewById(R.id.countdown_recycle_container);

        }

    }


}
