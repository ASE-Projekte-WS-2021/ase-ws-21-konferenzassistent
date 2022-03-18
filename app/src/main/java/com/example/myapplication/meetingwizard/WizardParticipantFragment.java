package com.example.myapplication.meetingwizard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentWizardParticipantBinding;

import java.util.ArrayList;

public class WizardParticipantFragment extends Fragment {

    FragmentWizardParticipantBinding bi;

    ArrayList<Participant> participants = new ArrayList<>();
    ParticipantBottomSheetAdapter participantBottomSheetAdapter;
    public WizardParticipantFragment() {
        // Required empty public constructor
    }

    public static WizardParticipantFragment newInstance(String param1, String param2) {
        return new WizardParticipantFragment();
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
    private void setupListeners(){
        bi.participantAdd.setOnClickListener(view -> {
            // creates a Bottom sheet to display Information
            participantBottomSheetAdapter = new ParticipantBottomSheetAdapter();
            participantBottomSheetAdapter.participants = participants;
            participantBottomSheetAdapter.show(getParentFragmentManager(), participantBottomSheetAdapter.getTag());
        });
    }

    private void getParticipantList(){
        // get the MeetingWizardActivity
        MeetingWizardActivity activity = ((MeetingWizardActivity)getActivity());

        assert activity != null;
        participants = activity.getParticipants();
    }

    public void onParticipentUpdate(){
        getParticipantList();
        participantBottomSheetAdapter.onParticipentAdded();
    }

    private void buildRecyclerView(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wizard_participant, container, false);
    }
}