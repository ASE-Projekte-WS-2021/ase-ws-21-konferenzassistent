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

import com.example.myapplication.meetingwizard.RecyclerViewAdvancedCountdownItemAdapter;

import java.util.ArrayList;

public class RecyclerItemPresetAdapter
        extends RecyclerView.Adapter<RecyclerItemPresetAdapter.ViewHolder> {

    private final Context mContext;
    private final ArrayList<RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem> countdownItem;

    public RecyclerItemPresetAdapter(
            ArrayList<RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem> countdownItem,
            Context mContext)
    {
        this.countdownItem = countdownItem;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_countdown_item_creation,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.presetName.setText(countdownItem.get(position).getSubCountdownDescription());

        holder.itemContainer.setOnClickListener(view -> {
            // Open Editor

        });
    }

    @Override
    public int getItemCount() {
        return countdownItem.size();
    }

    // View holder Class
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView presetName;
        RelativeLayout itemContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            presetName = itemView.findViewById(R.id.item_description);
            itemContainer = itemView.findViewById(R.id.item_container);
        }
    }
}
