package com.example.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.myapplication.data.ParticipantData;
import com.example.myapplication.data.RoomDB;
import com.example.myapplication.databinding.MeetingBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

/**
    https://betterprogramming.pub/bottom-sheet-android-340703e114d2
 */

public class MeetingBottomSheetAdapter extends  BottomSheetDialogFragment{
    BottomSheetBehavior bottomSheetBehavior;
    MeetingBottomSheetBinding bi;
    private int id;
    private String duration;
    private String startTime;
    private String endTime;
    private String participants;
    private String ort;
    private String meetingDate;

    private boolean participantDropDownOpened = false;

    private RoomDB database;

    // Make the background Transparent
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the style so the background is Transparent
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        database = RoomDB.getInstance(getContext());

        // inflating Layout
        View view = View.inflate(bottomSheet.getContext(), R.layout.meeting_bottom_sheet, null);

        // binding views to data binding
        bi = DataBindingUtil.bind(view);

        // setting layout with bottom sheet
        bottomSheet.setContentView(view);

        bottomSheetBehavior = BottomSheetBehavior.from((View) (view.getParent()));

        //setting Peek at the 16:9 ratio keyline of its parent
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);

        // setting max height of bottom sheet
        bi.extraSpace.setMinimumHeight((Resources.getSystem().getDisplayMetrics().heightPixels) / 2);

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {


            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(BottomSheetBehavior.STATE_EXPANDED == newState){
                    showView(bi.appBarLayout, getActionBarSize());
                }

                if(BottomSheetBehavior.STATE_COLLAPSED == newState){
                    hideAppBar(bi.appBarLayout);
                }

                if(BottomSheetBehavior.STATE_HIDDEN == newState){
                    dismiss();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        // cancel buttun clicked
        bi.cancelBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                dismiss();
            }
        });

        // edit button clicked
        bi.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Edit Funktion fehlt noch", Toast.LENGTH_SHORT).show();
            }
        });

        // more button clicked
        bi.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Brauchen wir den?", Toast.LENGTH_SHORT).show();
            }
        });

        // hiding bar at the start
        hideAppBar(bi.appBarLayout);

        // setup the View
        setView(id, duration, meetingDate, startTime,endTime,participants,ort);

        return bottomSheet;
    }

    @Override
    public void onStart(){
        super.onStart();

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void hideAppBar(View view){
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = 0;
        view.setLayoutParams(params);
    }

    private void showView(View view, int size){
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = size;
        view.setLayoutParams(params);
    }

    private int getActionBarSize() {
        final TypedArray array = getContext().getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        return (int) array.getDimension(0, 0);
    }

    private void setView(int id, String duration, String meetingDate, String startTime, String endTime, String participants, String ort){
        bi.duration.setText(duration);
        bi.meetingDate.setText(meetingDate);
        bi.startTime.setText(startTime);
        bi.endTime.setText(endTime);
        bi.participantCount.setText(participants);
        bi.ort.setText(ort);

        List<ParticipantData> participantList = database.meetingWithParticipantDao().getMeetingByID(id).getParticipants();

        for (ParticipantData p : participantList) {
            Chip chip = new Chip(requireContext());
            chip.setText(p.getName());
            bi.meetingBottomSheetParticipantChipgroup.addView(chip);

            chip.setOnClickListener(view -> {
                View alertDialogView = DialogUserInfoViewCreator.createView(requireContext(), p, false);
                new MaterialAlertDialogBuilder(requireContext())
                        .setView(alertDialogView)
                        .show();
            });
        }

        bi.meetingBottomSheetParticipantsContainer.setOnClickListener(view -> {
            if (!participantDropDownOpened) {
                participantDropDownOpened = true;
                bi.meetingBottomSheetParticipantChipgroup.setVisibility(View.VISIBLE);
                bi.meetingBottomSheetParticipantsDropdownIndicator.setRotation(180);
            } else {
                participantDropDownOpened = false;
                bi.meetingBottomSheetParticipantChipgroup.setVisibility(View.GONE);
                bi.meetingBottomSheetParticipantsDropdownIndicator.setRotation(0);
            }
        });
    }

    public void setValues(int id, String duration, String meetingDate, String startTime, String endTime, String participants, String ort){
        this.id = id;
        this.duration = duration;
        this.meetingDate = meetingDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.participants = participants;
        this.ort = ort;
    }
}