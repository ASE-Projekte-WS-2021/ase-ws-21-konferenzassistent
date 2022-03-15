package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.MeetingBottomSheetBinding;

import java.util.ArrayList;
import java.util.List;

public class MeetingHistoryAdapter extends RecyclerView.Adapter<MeetingHistoryAdapter.MeetingHistoryViewHolder> {

    private Context ct;
    private FragmentManager manager;
    private List<Meeting> meetingsList;

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

        holder.tvDate.setText(date);
        holder.tvTime.setText(startTime + " - " + endTime);
        holder.tvLocation.setText(ort);
        holder.tvDuration.setText(duration + " min");
        holder.tvNumParticipants.setText(participants);

        holder.cardView.setOnClickListener(view -> {
            // create a new bottom sheet and set the values for the view
            MeetingBottomSheetAdapter meetingBottomSheetAdapter = new MeetingBottomSheetAdapter();
            meetingBottomSheetAdapter.show(manager , meetingBottomSheetAdapter.getTag());
            meetingBottomSheetAdapter.setValues(duration + " Minuten", date, startTime, endTime, participants, ort);

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

    public class MeetingHistoryViewHolder extends RecyclerView.ViewHolder {

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
