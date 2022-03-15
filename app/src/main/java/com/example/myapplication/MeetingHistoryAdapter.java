package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MeetingHistoryAdapter extends RecyclerView.Adapter<MeetingHistoryAdapter.MeetingHistoryViewHolder> {

    private final Context ct;
    private final FragmentManager manager;
    private final List<Meeting> meetingsList;

    public MeetingHistoryAdapter(Context ct, FragmentManager manager, List<Meeting> meetingsList) {
        this.ct = ct;
        this.manager = manager;
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
        // Get the Values from the meeting List
        String duration = Integer.parseInt(meetingsList.get(position).getDuration())/60 + "";
        String startTime = meetingsList.get(position).getDate().substring(11);
        String endTime = meetingsList.get(position).getDateEnd().substring(11);
        String participants = meetingsList.get(position).getNumberParticipants();
        String ort = meetingsList.get(position).getLocation();
        String date = meetingsList.get(position).getDate().substring(0,10);

        // set the Text Values of the Holder
        holder.tvDate.setText(date);
        holder.tvTime.setText(String.format(ct.getString(R.string.meeting_history_minutes_divider),startTime,endTime));
        holder.tvLocation.setText(ort);
        holder.tvDuration.setText(String.format(ct.getString(R.string.meeting_history_minutes_short),duration));
        holder.tvNumParticipants.setText(participants);

        // set onclick listener on the cardView
        holder.cardView.setOnClickListener(view -> {
            // create a new bottom sheet and set the values for the view
            MeetingBottomSheetAdapter meetingBottomSheetAdapter = new MeetingBottomSheetAdapter();
            meetingBottomSheetAdapter.show(manager , meetingBottomSheetAdapter.getTag());
            meetingBottomSheetAdapter.setValues(
                    String.format(ct.getString(R.string.meeting_history_minutes_long),duration),
                    date,
                    startTime,
                    endTime,
                    participants,
                    ort);

            /*
            Intent intent = new Intent(ct, PastMeetingInfoActivity.class);
            intent.putExtra("Database_ID",Integer.parseInt(meetingsList.get(position).getId()));
            ct.startActivity(intent);
            */
        });
    }

    @Override
    public int getItemCount() {
        return meetingsList.size();
    }

    public static class MeetingHistoryViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView tvDate, tvTime, tvLocation, tvDuration, tvNumParticipants;

        public MeetingHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDateRow);
            tvTime = itemView.findViewById(R.id.tvTimeRow);
            cardView = itemView.findViewById(R.id.cvRow);
            tvLocation = itemView.findViewById(R.id.tvLocationRow);
            tvDuration = itemView.findViewById(R.id.tvDurationRow);
            tvNumParticipants = itemView.findViewById(R.id.tvParticipantsRow);
        }

    }
}
