package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewLocationAdapter
        extends RecyclerView.Adapter<RecyclerViewLocationAdapter.ViewHolder> {

    private final ArrayList<String> mLocationNames;
    private final ArrayList<String> mLocationNamesCopy = new ArrayList<>();
    private final Context mContext;

    public RecyclerViewLocationAdapter(
            ArrayList<String> mLocationNames,
            Context mContext)
    {
        this.mLocationNames = mLocationNames;
        this.mContext = mContext;
        mLocationNamesCopy.addAll(mLocationNames);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_location_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.locationName.setText(mLocationNames.get(holder.getAdapterPosition()));

        holder.locationContainer.setOnClickListener(view -> {
            if(mContext instanceof MainActivity){
                ((MainActivity)mContext).getMeetingAdapter().signalLocationChange(holder.getAdapterPosition());
            }

        });
    }

    // Filter the Locations
    // https://stackoverflow.com/a/37562572
    public void filter(String text) {
        mLocationNames.clear();
        if(text.isEmpty()){
            mLocationNames.addAll(mLocationNamesCopy);
        } else{
            text = text.toLowerCase();
            for(String item: mLocationNamesCopy){
                if(item.toLowerCase().contains(text)){
                    mLocationNames.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mLocationNames.size();
    }

    // View holder Class
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView locationName;
        RelativeLayout locationContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            locationName = itemView.findViewById(R.id.location_name);
            locationContainer = itemView.findViewById(R.id.location_container);
        }
    }
}
