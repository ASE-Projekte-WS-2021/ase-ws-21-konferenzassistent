package com.example.myapplication.meetingwizard;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.ContactCreationBottomSheetAdapter;
import com.example.myapplication.DialogUserInfoViewCreator;
import com.example.myapplication.R;
import com.example.myapplication.data.ParticipantData;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

public class RecyclerViewParticipantListAdapter extends RecyclerView.Adapter<RecyclerViewParticipantListAdapter.ViewHolder> {

    // Content
    private final ArrayList<Participant> mParticipants;
    private final Context mContext;
    private final ArrayList<Participant> mParticipantsCopy = new ArrayList<>();
    private final boolean openedFromWizard;
    private ContactCreationBottomSheetAdapter.OnParticipantCreatedListener listener;

    public RecyclerViewParticipantListAdapter(ArrayList<Participant> mParticipants, Context mContext, boolean openedFromWizard) {
        this.mParticipants = mParticipants;
        this.mContext = mContext;
        mParticipantsCopy.addAll(mParticipants);
        this.openedFromWizard = openedFromWizard;
        this.listener = null;
    }

    public RecyclerViewParticipantListAdapter(ArrayList<Participant> mParticipants, Context mContext, boolean openedFromWizard, ContactCreationBottomSheetAdapter.OnParticipantCreatedListener listener) {
        this.mParticipants = mParticipants;
        this.mContext = mContext;
        mParticipantsCopy.addAll(mParticipants);
        this.openedFromWizard = openedFromWizard;
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Participant participant = mParticipants.get(holder.getAdapterPosition());
        holder.participantName.setText(participant.getName());
        holder.isParticipant.setVisibility(participant.getSelected() ? View.VISIBLE : View.GONE);
        holder.participantStatus.setText(participant.getStatus());

        ParticipantData asParticipantData = new ParticipantData();
        asParticipantData.setID(participant.getId());
        asParticipantData.setName(participant.getName());
        asParticipantData.setEmail(participant.getEmail());
        asParticipantData.setStatus(participant.getStatus());

        if (openedFromWizard) {
            // Set on click listener on the container
            holder.participantContainer.setOnClickListener(containerView -> {
                // Invert the status
                Boolean wasSelected = participant.getSelected();
                participant.setSelected(!wasSelected);

                // Set visibility on indicator
                holder.isParticipant.setVisibility(!wasSelected ? View.VISIBLE : View.GONE);
            });
        } else {
            FragmentManager manager = ((AppCompatActivity) mContext).getSupportFragmentManager();
            holder.participantContainer.setOnClickListener(containerView -> {
                View alertDialogView = DialogUserInfoViewCreator.createView(mContext, asParticipantData, true, manager);
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(mContext);
                builder.setView(alertDialogView);
                ImageButton editBtn = alertDialogView.findViewById(R.id.dialog_user_info_topbar_edit_button);
                AlertDialog dialog = builder.create();
                dialog.show();
                editBtn.setOnClickListener(view -> {
                    ContactCreationBottomSheetAdapter contactCreationBottomSheetAdapter =
                            new ContactCreationBottomSheetAdapter(listener, asParticipantData);
                    contactCreationBottomSheetAdapter.show(manager,
                            contactCreationBottomSheetAdapter.getTag());
                    dialog.dismiss();
                });
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

    public void updateCopy(ArrayList<Participant> participants) {
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_participant_item,
                parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView participantName;
        TextView participantStatus;
        ImageView isParticipant;
        LinearLayout participantContainer;

        public ViewHolder(View countdownView) {
            super(countdownView);
            participantName = countdownView.findViewById(R.id.participant_name);
            participantStatus = countdownView.findViewById(R.id.participant_status);
            isParticipant = countdownView.findViewById(R.id.participant_is_participant);
            participantContainer = countdownView.findViewById(R.id.participant_container);

        }

    }

}





