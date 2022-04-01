package com.example.myapplication.meetingwizard;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DialogUserInfoViewCreator;
import com.example.myapplication.R;
import com.example.myapplication.data.ParticipantData;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

public class RecycleViewParticipantList extends RecyclerView.Adapter<RecycleViewParticipantList.ViewHolder>{

    // Content
    private final ArrayList<Participant> mParticipants;
    private ArrayList<Participant> mParticipantsCopy = new ArrayList<>();
    private final Context mContext;
    private boolean openedFromWizard;

    public RecycleViewParticipantList(ArrayList<Participant> mParticipants, Context mContext, boolean openedFromWizard) {
        this.mParticipants = mParticipants;
        this.mContext = mContext;
        mParticipantsCopy.addAll(mParticipants);
        this.openedFromWizard = openedFromWizard;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Participant participant = mParticipants.get(holder.getAdapterPosition());
            holder.participantName.setText(participant.getName());
            holder.isParticipant.setVisibility(participant.getSelected()? View.VISIBLE : View.GONE);
            holder.participantStatus.setText(participant.getStatus());

            ParticipantData asParticipantData = new ParticipantData();
            asParticipantData.setID(participant.getId());
            asParticipantData.setName(participant.getName());
            asParticipantData.setEmail(participant.getEmail());
            asParticipantData.setStatus(participant.getStatus());

            if (openedFromWizard){
                // Set on click listener on the container
                holder.participantContainer.setOnClickListener(containerView -> {
                    // Invert the status
                    Boolean wasSelected = participant.getSelected();
                    participant.setSelected(!wasSelected);

                    // Set visibility on indicator
                    holder.isParticipant.setVisibility(!wasSelected? View.VISIBLE : View.GONE);
                });
            } else {
                holder.participantContainer.setOnClickListener(containerView -> {
                    View alertDialogView = DialogUserInfoViewCreator.createView(mContext, asParticipantData, false); // TODO make button open participant edit sheet
                    new MaterialAlertDialogBuilder(mContext)
                            .setView(alertDialogView)
                            .show();
                });
            }
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

    public void updateCopy(ArrayList<Participant> participants){
        mParticipantsCopy.clear();
        Log.i("TAG", "updateCopy: " + participants);
        mParticipantsCopy.addAll(participants);
    }

    // onDestroy reset Search
    public void onDestroy() {
        mParticipants.clear();
        mParticipants.addAll(mParticipantsCopy);
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





