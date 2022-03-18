package com.example.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.util.Pair;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MeetingFilterBottomSheet extends BottomSheetDialogFragment {

    private List<Meeting> meetingList;
    private OnFilterButtonClickListener listener;

    private Button resetButton, filterButton;
    private ChipGroup peopleChipGroup, locationChipGroup;
    private TextInputEditText countMinEditText, countMaxEditText, dateStartEditText, dateEndEditText;

    public MeetingFilterBottomSheet(List<Meeting> meetingList, OnFilterButtonClickListener listener) {
        super();

        this.meetingList = meetingList;
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the style so the background is Transparent
        // setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_filter_meetings,container,false);

        findViews(view);
        populateChipGroups();
        setListeners();

        return view;
    }

    private void findViews(@NonNull View v) {
        resetButton = v.findViewById(R.id.bottom_sheet_filter_meetings_reset_button);
        filterButton = v.findViewById(R.id.bottom_sheet_filter_meetings_filter_button);

        peopleChipGroup = v.findViewById(R.id.bottom_sheet_filter_meetings_people_chip_group);

        countMinEditText = v.findViewById(R.id.bottom_sheet_filter_meetings_count_min);
        countMaxEditText = v.findViewById(R.id.bottom_sheet_filter_meetings_count_max);

        locationChipGroup = v.findViewById(R.id.bottom_sheet_filter_meetings_location_chip_group);

        dateStartEditText = v.findViewById(R.id.bottom_sheet_filter_meetings_date_start);
        dateEndEditText = v.findViewById(R.id.bottom_sheet_filter_meetings_date_end);
    }

    private void populateChipGroups() {
        List<String> peopleList, locationList;

        // Acquire list of people and locations, based on the MeetingsList
        // Placeholder for now TODO
        peopleList = new ArrayList<>();
        peopleList.add("Max Mustermann");
        peopleList.add("Max Mustermann");
        peopleList.add("Max Mustermann");
        peopleList.add("Max Mustermann");
        peopleList.add("Max Mustermann");
        locationList = new ArrayList<>();
        locationList.add("Sauna");
        locationList.add("Sauna");
        locationList.add("Sauna");
        locationList.add("Sauna");
        locationList.add("Sauna");
        // End of Placeholder

        for (String person : peopleList) {
            generateChipAndAppend(person, peopleChipGroup);
        }
        for (String location : locationList) {
            generateChipAndAppend(location, locationChipGroup);
        }
    }

    private void generateChipAndAppend(String s, ChipGroup cg) {
        Chip chip = new Chip(getContext());
        chip.setId(ViewCompat.generateViewId());
        chip.setText(s);
        chip.setCheckable(true);
        cg.addView(chip);
    }

    private void setListeners() {
        resetButton.setOnClickListener(view -> {
            dismiss();
        });
        filterButton.setOnClickListener(view -> {
            dismiss();
        });

        MaterialDatePicker picker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("test")
                .setTheme(R.style.CustomDatePickerStyle)
                .build();

        picker.addOnPositiveButtonClickListener((MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>) selection -> {
            Long start, end;
            start = selection.first;
            end = selection.second;
            DateFormat dateFormat = DateFormat.getDateInstance();
            dateStartEditText.setText(dateFormat.format(start));
            dateEndEditText.setText(dateFormat.format(end));
        });

        picker.addOnNegativeButtonClickListener(view -> {
            dateStartEditText.setText("");
            dateEndEditText.setText("");
        });

        dateStartEditText.setOnClickListener(view -> picker.show(getParentFragmentManager(),getTag()));
        dateEndEditText.setOnClickListener(view -> picker.show(getParentFragmentManager(),getTag()));
    }
}
