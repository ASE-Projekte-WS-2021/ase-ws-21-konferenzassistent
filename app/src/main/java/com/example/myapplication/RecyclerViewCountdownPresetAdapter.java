package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewCountdownPresetAdapter
        extends RecyclerView.Adapter<RecyclerViewCountdownPresetAdapter.ViewHolder> implements CustomAlertBottomSheetAdapter.onLeaveListener {

    private final ArrayList<CountdownPreset> countdownPresets;
    private final Context mContext;
    private onDeletionListener listener;

    int selectedPosition;
    @Override
    public void onLeaving() {
        listener.onDelete(selectedPosition);
    }

    @Override
    public void clearWarnings() {

    }

    interface onDeletionListener{
        void onDelete(int position);
    }

    public RecyclerViewCountdownPresetAdapter(
            ArrayList<CountdownPreset> countdownPresets,
            onDeletionListener listener,
            Context mContext)
    {
        this.countdownPresets = countdownPresets;
        this.mContext = mContext;
        this.listener = listener;
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

        holder.selectIndicator.setVisibility(View.VISIBLE);
        holder.selectIndicator.setBackground(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_baseline_delete_forever_24, null));
        holder.itemContainer.setOnClickListener(view -> {
            createAlert(countdownPresets.get(holder.getAdapterPosition()).getTitle());
            selectedPosition = holder.getAdapterPosition();
        });
    }

    @Override
    public int getItemCount() {
        return countdownPresets.size();
    }

    private void createAlert(String title){
        CustomAlertBottomSheetAdapter customAlertBottomSheetAdapter = new CustomAlertBottomSheetAdapter(this);
        customAlertBottomSheetAdapter.setWarningText("Soll das Preset \"" + title + "\" wirklich gelöscht werden."); // Mitteilung
        customAlertBottomSheetAdapter.setAcceptText("Preset löschen"); // Positives Feedback
        customAlertBottomSheetAdapter.setDeclineText("Preset behalten"); // Negatives Feedback
        customAlertBottomSheetAdapter.show(((AppCompatActivity)mContext).getSupportFragmentManager() , customAlertBottomSheetAdapter.getTag());
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
