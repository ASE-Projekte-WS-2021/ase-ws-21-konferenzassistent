package com.ase.konferenzassistent.meetingwizard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.konferenzassistent.R;

import java.util.ArrayList;

public class RecycleViewAttendingParticipantList extends RecyclerView.Adapter<RecycleViewAttendingParticipantList.ViewHolder> {

    private final ArrayList<Participant> mParticipants;

    public RecycleViewAttendingParticipantList(ArrayList<Participant> mParticipants, Context mContext) {
        this.mParticipants = mParticipants;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Participant participant = mParticipants.get(holder.getAdapterPosition());
        holder.participantName.setText(participant.getName());
        holder.participantStatus.setText(participant.getStatus());

        holder.participantContainer.setVisibility(participant.getSelected() ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return mParticipants.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_participant_item,
                parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView participantName;
        final TextView participantStatus;
        final ImageView isParticipant;
        final LinearLayout participantContainer;

        public ViewHolder(View countdownView) {
            super(countdownView);
            participantName = countdownView.findViewById(R.id.participant_name);
            participantStatus = countdownView.findViewById(R.id.participant_status);
            isParticipant = countdownView.findViewById(R.id.participant_is_participant);
            participantContainer = countdownView.findViewById(R.id.participant_container);

        }
    }
}





