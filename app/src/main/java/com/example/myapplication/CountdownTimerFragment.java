package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myapplication.databinding.FragmentCountdownTimerBinding;
import com.example.myapplication.meetingwizard.MeetingWizardActivity;
import com.example.myapplication.meetingwizard.RecycleViewContactList;
import com.example.myapplication.meetingwizard.RecyclerViewAdvancedCountdownAdapter;
import com.example.myapplication.meetingwizard.cdServiceObject;
import com.google.android.material.timepicker.TimeFormat;

import java.util.ArrayList;


public class CountdownTimerFragment extends Fragment implements RecycleViewCountdownAdapter.countDownButtonPressed {

    FragmentCountdownTimerBinding bi;
    RecycleViewCountdownAdapter recycleViewCountdownAdapter;
    ArrayList<cdServiceObject> advancedCountdownObjects;

    public static CountdownTimerFragment newInstance(
            ArrayList<cdServiceObject> advancedCountdownObjects){
        return new CountdownTimerFragment(advancedCountdownObjects);
    }

    public CountdownTimerFragment(ArrayList<cdServiceObject> advancedCountdownObjects) {
        this.advancedCountdownObjects = advancedCountdownObjects;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_countdown_timer, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // binding views to data binding
        bi = DataBindingUtil.bind(view);

        buildRecyclerView();
    }

    // Build and fills the recycler view
    private void buildRecyclerView(){

        RecyclerView recyclerView = bi.countdownRecycleViewContainer;
        recycleViewCountdownAdapter = new RecycleViewCountdownAdapter(
                advancedCountdownObjects,
                this,
                this.getContext()
        );
        recyclerView.setAdapter(recycleViewCountdownAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));


    }

    public void updateView(ArrayList<cdServiceObject> objects){
        advancedCountdownObjects.clear();
        advancedCountdownObjects.addAll(objects);
        recycleViewCountdownAdapter.notifyDataSetChanged();
    }

    @Override
    public void pausePressed(int id) {
        CountdownActivity activity = ((CountdownActivity)getActivity());

        assert activity != null;
        activity.pauseCountdown(id);
    }

    @Override
    public void restartPressed(int id) {
        CountdownActivity activity = ((CountdownActivity)getActivity());

        assert activity != null;
        activity.startCountdown(id);
    }
}