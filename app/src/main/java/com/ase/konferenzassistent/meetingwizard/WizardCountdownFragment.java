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
import com.ase.konferenzassistent.countdown.AdvancedCountdownObject;
import com.ase.konferenzassistent.databinding.FragmentWizardCountdownBinding;

import java.util.ArrayList;


public class WizardCountdownFragment extends Fragment {

    FragmentWizardCountdownBinding bi;

    ArrayList<String> mCountdownNames = new ArrayList<>();
    ArrayList<Long> mCountdownTime = new ArrayList<>();
    ArrayList<Boolean> mEnabled = new ArrayList<>();

    ArrayList<AdvancedCountdownObject> mCountdown =
            new ArrayList<>();

    RecyclerViewAdvancedCountdownAdapter recyclerViewCountdownAdapter;

    public WizardCountdownFragment() {
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

        getCountdowns();

        // Builds the Recycler View and displays the Countdowns
        buildRecyclerView();

    }

    // Gets the Arrays form the Activity
    private void getCountdowns() {
        // get the MeetingWizardActivity
        MeetingWizardActivity activity = ((MeetingWizardActivity) getActivity());

        assert activity != null;
        mCountdownNames = activity.getmCountdownNames();
        mCountdownTime = activity.getmCountdownTime();
        mEnabled = activity.getmEnabled();
        mCountdown = activity.getAdvancedCountdownObjects();
    }

    // Sets the Arrays in the Activity
    private void setCountdowns() {
        // get the MeetingWizardActivity
        MeetingWizardActivity activity = ((MeetingWizardActivity) getActivity());

        // get the data from the adapter
        mCountdown = recyclerViewCountdownAdapter.getmAdvancedCountdownObjects();

        assert activity != null;
        activity.setmCountdownNames(mCountdownNames);
        activity.setmCountdownTime(mCountdownTime);
        activity.setmEnabled(mEnabled);
        activity.setAdvancedCountdownObjects(mCountdown);
    }

    // Build and fills the recycler view
    private void buildRecyclerView() {

        RecyclerView recyclerView = bi.countdownRecycleView;
        recyclerViewCountdownAdapter = new RecyclerViewAdvancedCountdownAdapter(
                mCountdown,
                this.getContext()
        );
        recyclerView.setAdapter(recyclerViewCountdownAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    @Override
    public void onDetach() {
        super.onDetach();

        // Set the Countdowns once User leaves the Fragment
        setCountdowns();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wizard_countdown, container, false);
    }
}