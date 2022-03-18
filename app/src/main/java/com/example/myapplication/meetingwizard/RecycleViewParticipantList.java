package com.example.myapplication.meetingwizard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class RecycleViewParticipantList extends RecyclerView.Adapter<RecycleViewParticipantList.ViewHolder>{

    // Content
    private final ArrayList<Participant> mParticipants;
    private final ArrayList<Participant> mParticipantsCopy = new ArrayList<>();
    private final Context mContext;

    public RecycleViewParticipantList(ArrayList<Participant> mParticipants, Context mContext) {
        this.mParticipants = mParticipants;
        this.mContext = mContext;
        mParticipantsCopy.addAll(mParticipants);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Participant participant = mParticipants.get(holder.getAdapterPosition());
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
            return mParticipants.size();
            }

    // Filter the Participants
    // https://stackoverflow.com/a/37562572
    public void filter(String text) {
        mParticipants.clear();
        if (text.isEmpty()) {
            mParticipants.addAll(mParticipantsCopy);
        } else {
            text = text.toLowerCase();
            for (Participant item : mParticipantsCopy) {
                if (item.getName().toLowerCase().contains(text)) {
                    mParticipants.add(item);
                }
            }
        }
        notifyDataSetChanged();
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





