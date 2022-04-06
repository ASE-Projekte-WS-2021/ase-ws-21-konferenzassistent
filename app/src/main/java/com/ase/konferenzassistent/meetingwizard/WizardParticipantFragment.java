package com.ase.konferenzassistent.meetingwizard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.konferenzassistent.R;
import com.ase.konferenzassistent.databinding.FragmentWizardParticipantBinding;

import java.util.ArrayList;

public class WizardParticipantFragment extends Fragment {

    FragmentWizardParticipantBinding bi;

    RecycleViewAttendingParticipantList recycleViewParticipantList;

    ArrayList<Participant> participants = new ArrayList<>();
    ParticipantBottomSheetAdapter participantBottomSheetAdapter;

    public WizardParticipantFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bi = DataBindingUtil.bind(view);
        assert bi != null;

        setupListeners();

        getParticipantList();

        // Builds the Recycler View and displays the Participants (Should still be empty at this point)
        buildRecyclerView();
    }

    // Setup listeners
    private void setupListeners() {
        bi.participantAdd.setOnClickListener(view -> {
            // creates a Bottom sheet to display Information
            participantBottomSheetAdapter = new ParticipantBottomSheetAdapter();
            participantBottomSheetAdapter.participants = participants;
            participantBottomSheetAdapter.show(getParentFragmentManager(), participantBottomSheetAdapter.getTag());
        });
    }

    private void getParticipantList() {
        // get the MeetingWizardActivity
        MeetingWizardActivity activity = ((MeetingWizardActivity) getActivity());

        assert activity != null;
        participants = activity.getParticipants();
    }

    public void onParticipentUpdate() {
        getParticipantList();
        participantBottomSheetAdapter.onParticipentAdded();
    }

    public void updateDataSet() {
        recycleViewParticipantList.notifyDataSetChanged();
    }

    // Build and fills the recycler view
    private void buildRecyclerView() {

        RecyclerView recyclerView = bi.participantRecycleView;
        recycleViewParticipantList = new RecycleViewAttendingParticipantList(
                participants,
                this.getContext()
        );
        recyclerView.setAdapter(recycleViewParticipantList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wizard_participant, container, false);
    }
}