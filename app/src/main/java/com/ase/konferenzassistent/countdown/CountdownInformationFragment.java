package com.ase.konferenzassistent.countdown;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.konferenzassistent.R;
import com.ase.konferenzassistent.databinding.FragmentCountdownInformationBinding;
import com.ase.konferenzassistent.mainscreen.recycleviews.RecycleViewParticipantInfoListAdapter;
import com.ase.konferenzassistent.mainscreen.recycleviews.RecycleViewPlaceholderAdapter;
import com.ase.konferenzassistent.meetingwizard.Participant;
import com.ase.konferenzassistent.meetingwizard.cdServiceObject;

import java.util.ArrayList;

public class CountdownInformationFragment extends Fragment {

    FragmentCountdownInformationBinding bi;
    RecycleViewPlaceholderAdapter recycleViewPlaceholderAdapter;
    final ArrayList<cdServiceObject> advancedCountdownObjects;
    final ArrayList<Participant> participants;

    private TextView ortTextView;
    private TextView startZeitTextView;
    private TextView teilnehmerTextView;

    private String ort;
    private String zeit;
    private String teilnehmerZahl;

    public CountdownInformationFragment(ArrayList<cdServiceObject> advancedCountdownObjects, ArrayList<Participant> participants) {
        this.advancedCountdownObjects = advancedCountdownObjects;
        this.participants = participants;
    }

    public static CountdownInformationFragment newInstance(ArrayList<cdServiceObject> advancedCountdownObjects, ArrayList<Participant> participants) {
        return new CountdownInformationFragment(advancedCountdownObjects, participants);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // binding views to data binding
        bi = DataBindingUtil.bind(view);

        // init Views
        initViews(view);

        // fill Views
        fillViews();

        // build Recycle View
        buildBackgroundRecyclerView();
        buildParticipantRecyclerView();
    }

    // Gets Views from IDs
    private void initViews(View view) {
        ortTextView = view.findViewById(R.id.meeting_ort_text_view);
        startZeitTextView = view.findViewById(R.id.meeting_start_zeit_text_view);
        teilnehmerTextView = view.findViewById(R.id.meeting_teilnehmer_text_view);
    }

    // Get the values for the TextViews
    public void getValuesForTextViews(String ort, String zeit, String teilnehmerZahl) {
        // Save values localy cause TexViews are still null at this point
        this.ort = ort;
        this.zeit = zeit;
        this.teilnehmerZahl = teilnehmerZahl;
    }

    private void fillViews() {
        ortTextView.setText(ort);
        startZeitTextView.setText(zeit);
        teilnehmerTextView.setText(teilnehmerZahl);
    }

    // Build and fills the recycler view
    private void buildBackgroundRecyclerView() {

        RecyclerView recyclerView = bi.placeholderView;
        recycleViewPlaceholderAdapter = new RecycleViewPlaceholderAdapter(
                advancedCountdownObjects,
                this.requireContext()
        );
        recyclerView.setAdapter(recycleViewPlaceholderAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    // Build and fills the recycler view
    private void buildParticipantRecyclerView() {

        RecyclerView recyclerView = bi.recyclerViewParticipants;
        RecycleViewParticipantInfoListAdapter recycleViewParticipantInfoListAdapter = new RecycleViewParticipantInfoListAdapter(
                participants,
                this.getContext()
        );
        recyclerView.setAdapter(recycleViewParticipantInfoListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_countdown_information, container, false);
    }
}