package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.data.ParticipantData;
import com.example.myapplication.data.RoomDB;
import com.example.myapplication.databinding.FragmentMiTeilnehmerverwaltungBinding;
import com.example.myapplication.meetingwizard.Participant;
import com.example.myapplication.meetingwizard.RecycleViewParticipantList;

import java.util.ArrayList;
import java.util.List;

public class TeilnehmerverwaltungFragment extends Fragment {
    FragmentMiTeilnehmerverwaltungBinding bi;
    private RoomDB db;

    public TeilnehmerverwaltungFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mi_teilnehmerverwaltung, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bi = DataBindingUtil.bind(view);

        // on new participant button
        bi.buttonAddContact.setOnClickListener(viewListener -> {
            ContactCreationBottomSheetAdapter contactCreationBottomSheetAdapter =
                    new ContactCreationBottomSheetAdapter();
            contactCreationBottomSheetAdapter.show(getParentFragmentManager(),
                    contactCreationBottomSheetAdapter.getTag());
        });

        db = RoomDB.getInstance(getContext());
        List<ParticipantData> participantDataList = db.participantDao().getAll();
        ArrayList<Participant> participants = new ArrayList<>();
        for (ParticipantData participantData : participantDataList) {
            participants.add(new Participant(participantData.getName(), participantData.getEmail(), participantData.getStatus(), false, participantData.getID()));
        }

        RecycleViewParticipantList adapter = new RecycleViewParticipantList(participants, getActivity(), false);
        bi.teilnehmerverwaltungParticipantRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        bi.teilnehmerverwaltungParticipantRv.setAdapter(adapter);
    }
}