package com.example.myapplication.meetingwizard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.myapplication.R;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecyclerViewAdvancedCountdownAdapter
        extends RecyclerView.Adapter<RecyclerViewAdvancedCountdownAdapter.ViewHolder>{

    public static class AdvancedCountdownObject implements Serializable {
        String mCountdownName;
        Boolean mEnabled;

        public String getmCountdownName() {
            return mCountdownName;
        }

        public void setmCountdownName(String mCountdownName) {
            this.mCountdownName = mCountdownName;
        }

        public Boolean getmEnabled() {
            return mEnabled;
        }

        public void setmEnabled(Boolean mEnabled) {
            this.mEnabled = mEnabled;
        }

        public ArrayList<RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem> getmItems() {
            return mItems;
        }

        public void setmItems(ArrayList<RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem> mItems) {
            this.mItems = mItems;
        }

        ArrayList<RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem> mItems;

        public AdvancedCountdownObject(String mCountdownName,
                                       Boolean mEnabled,
                                       ArrayList<RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem> mItems) {
            this.mCountdownName = mCountdownName;
            this.mEnabled = mEnabled;
            this.mItems = mItems;
        }
    }

    public ArrayList<AdvancedCountdownObject> getmAdvancedCountdownObjects() {
        return mAdvancedCountdownObjects;
    }

    // Content
    private final ArrayList<AdvancedCountdownObject> mAdvancedCountdownObjects;
    private final Context mContext;

    public RecyclerViewAdvancedCountdownAdapter(
            ArrayList<AdvancedCountdownObject> mAdvancedCountdownObjects,
            Context mContext)
    {
        this.mAdvancedCountdownObjects = mAdvancedCountdownObjects;
        this.mContext = mContext;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.countdownName.setText(mAdvancedCountdownObjects.get(holder.getAdapterPosition()).mCountdownName);
        holder.aSwitch.setChecked(mAdvancedCountdownObjects.get(holder.getAdapterPosition()).mEnabled);

        // Load the Child recycler view
        LinearLayoutManager childView = new LinearLayoutManager(holder.recyclerView.getContext(),
                RecyclerView.HORIZONTAL, false);

        RecyclerViewAdvancedCountdownItemAdapter recyclerViewCountdownAdapter= new RecyclerViewAdvancedCountdownItemAdapter(
                getmAdvancedCountdownObjects().get(holder.getAdapterPosition()).mItems,
                mContext);

        holder.recyclerView.setAdapter(recyclerViewCountdownAdapter);
        holder.recyclerView.setLayoutManager(childView);
        SnapHelper helper = new LinearSnapHelper();
        helper.attachToRecyclerView(holder.recyclerView);
        if(getmAdvancedCountdownObjects().get(holder.getAdapterPosition()).mItems.size() > 1)
            holder.recyclerView.addItemDecoration(new LinePagerIndicatorDecoration());

        // Setup the container visibility
        holder.countdownContainer.setVisibility(holder.aSwitch.isChecked()? View.VISIBLE : View.GONE);

        // Set on click listener on the switch
        holder.aSwitch.setOnClickListener(view1 -> {
            if(!holder.aSwitch.isChecked()){
                mAdvancedCountdownObjects.get(holder.getAdapterPosition()).mEnabled = false;
                holder.countdownContainer.setVisibility(View.GONE);
            }
            else{
                mAdvancedCountdownObjects.get(holder.getAdapterPosition()).mEnabled = true;
                holder.countdownContainer.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mAdvancedCountdownObjects.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_advanced_countdown_picker,
                parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView countdownName;
        SwitchCompat aSwitch;
        LinearLayout countdownContainer;
        RecyclerView recyclerView;

        public ViewHolder(View countdownView){
            super(countdownView);
            countdownName = countdownView.findViewById(R.id.countdown_advanced_recycler_name);
            aSwitch = countdownView.findViewById(R.id.countdown_advanced_recycler_switch);
            countdownContainer = countdownView.findViewById(R.id.countdown_advanced_recycle_container);
            recyclerView = countdownView.findViewById(R.id.countdown_child_recycle_view);

    }

}


}
