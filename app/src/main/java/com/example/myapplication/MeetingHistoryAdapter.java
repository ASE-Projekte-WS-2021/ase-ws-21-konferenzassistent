package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MeetingHistoryAdapter extends RecyclerView.Adapter<MeetingHistoryAdapter.MeetingHistoryViewHolder> {

    private Context ct;
    private ArrayList<String> meetingsList;

    public MeetingHistoryAdapter(Context ct, ArrayList<String> meetingsList) {
        this.ct = ct;
        this.meetingsList = meetingsList;
    }

    @NonNull
    @Override
    public MeetingHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ct);
        View view = inflater.inflate(R.layout.meeting_history_row,parent,false);
        return new MeetingHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MeetingHistoryViewHolder holder, int position) {
        holder.tvDate.setText(meetingsList.get(position));

        holder.cardView.setOnClickListener(view -> {
            Intent intent = new Intent(ct, PastMeetingInfoActivity.class);
            ct.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return meetingsList.size();
    }

    public class MeetingHistoryViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView tvDate;

        public MeetingHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDateRow);
            cardView = itemView.findViewById(R.id.cvRow);
        }

    }
}
