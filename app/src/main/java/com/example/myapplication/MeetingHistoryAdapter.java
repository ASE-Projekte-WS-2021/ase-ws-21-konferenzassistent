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
import java.util.List;

public class MeetingHistoryAdapter extends RecyclerView.Adapter<MeetingHistoryAdapter.MeetingHistoryViewHolder> {

    private Context ct;
    private List<Meeting> meetingsList;

    public MeetingHistoryAdapter(Context ct, List<Meeting> meetingsList) {
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
        holder.tvDate.setText(meetingsList.get(position).getDate());
        holder.tvDuration.setText(meetingsList.get(position).getDuration() + " min");
        holder.tvNumParticipants.setText(meetingsList.get(position).getNumberParticipants());

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
        TextView tvDate, tvDuration, tvNumParticipants;

        public MeetingHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDateRow);
            cardView = itemView.findViewById(R.id.cvRow);
            tvDuration = itemView.findViewById(R.id.tvDurationRow);
            tvNumParticipants = itemView.findViewById(R.id.tvParticipantsRow);
        }

    }
}
