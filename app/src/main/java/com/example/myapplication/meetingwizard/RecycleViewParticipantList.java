package com.example.myapplication.meetingwizard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class RecycleViewParticipantList extends RecyclerView.Adapter<RecycleViewParticipantList.ViewHolder>{

    // Content
    private final ArrayList<Participant> mparticipants;
    private final Context mContext;

    public RecycleViewParticipantList(ArrayList<Participant> mparticipants, Context mContext) {
        this.mparticipants = mparticipants;
        this.mContext = mContext;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Participant participant = mparticipants.get(holder.getAdapterPosition());
            holder.participantName.setText(participant.getName());
            holder.isParticipant.setVisibility(participant.getSelected()? View.VISIBLE : View.GONE);
            holder.participantStatus.setText(participant.getStatus());


            // Set on click listener on the container
            holder.participantContainer.setOnClickListener(containerView -> {
                // Invert the status
                Boolean wasSelected = participant.getSelected();
                participant.setSelected(!wasSelected);

                // Set visibility on indicator
                holder.isParticipant.setVisibility(!wasSelected? View.VISIBLE : View.GONE);
            });
            }

    @Override
    public int getItemCount() {
            return mparticipants.size();
            }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_participant_item,
            parent, false);
            return new ViewHolder(view);
            }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView participantName;
        TextView participantStatus;
        ImageView isParticipant;
        LinearLayout participantContainer;

        public ViewHolder(View countdownView){
            super(countdownView);
            participantName = countdownView.findViewById(R.id.participant_name);
            participantStatus = countdownView.findViewById(R.id.participant_status);
            isParticipant = countdownView.findViewById(R.id.participant_is_participant);
            participantContainer = countdownView.findViewById(R.id.participant_container);

        }

    }

}





