package com.ase.konferenzassistent.mainscreen.participants;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ase.konferenzassistent.R;
import com.ase.konferenzassistent.data.ParticipantData;
import com.ase.konferenzassistent.data.RoomDB;
import com.ase.konferenzassistent.databinding.FragmentMiTeilnehmerverwaltungBinding;
import com.ase.konferenzassistent.meetingwizard.Participant;
import com.ase.konferenzassistent.meetingwizard.RecyclerViewParticipantListAdapter;

import java.util.ArrayList;
import java.util.List;

public class TeilnehmerverwaltungFragment extends Fragment implements ContactCreationBottomSheetAdapter.OnParticipantModifiedListener {
    FragmentMiTeilnehmerverwaltungBinding bi;
    private RoomDB db;
    private RecyclerViewParticipantListAdapter adapter;
    private ArrayList<Participant> participants;

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
        assert bi != null;
        bi.buttonAddContact.setOnClickListener(viewListener -> {
            ContactCreationBottomSheetAdapter contactCreationBottomSheetAdapter =
                    new ContactCreationBottomSheetAdapter(this, null);
            contactCreationBottomSheetAdapter.show(getParentFragmentManager(),
                    contactCreationBottomSheetAdapter.getTag());
        });

        db = RoomDB.getInstance(getContext());
        participants = new ArrayList<>();

        adapter = new RecyclerViewParticipantListAdapter(participants, getActivity(), false, this);
        bi.teilnehmerverwaltungParticipantRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        bi.teilnehmerverwaltungParticipantRv.setAdapter(adapter);

        populateParticipantList();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void populateParticipantList() {
        List<ParticipantData> participantDataList = db.participantDao().getAll();
        participants.clear();
        for (ParticipantData participantData : participantDataList) {
            participants.add(new Participant(participantData.getName(), participantData.getEmail(), participantData.getStatus(), false, participantData.getID()));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onParticipantModified() {
        populateParticipantList();
    }
}