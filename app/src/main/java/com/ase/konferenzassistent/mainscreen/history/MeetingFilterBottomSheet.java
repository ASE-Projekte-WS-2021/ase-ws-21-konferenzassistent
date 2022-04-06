package com.ase.konferenzassistent.mainscreen.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;

import com.ase.konferenzassistent.R;
import com.ase.konferenzassistent.data.MeetingData;
import com.ase.konferenzassistent.shared.Interfaces.OnFilterButtonClickListener;
import com.ase.konferenzassistent.data.ParticipantData;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MeetingFilterBottomSheet extends BottomSheetDialogFragment {

    private final List<MeetingData> meetingList;
    private final List<ParticipantData> participantList;
    private final OnFilterButtonClickListener listener;

    private Button resetButton, filterButton;
    private ChipGroup peopleChipGroup, locationChipGroup;
    private TextInputEditText countMinEditText, countMaxEditText, dateStartEditText, dateEndEditText;
    private Long dateStart, dateEnd;

    public MeetingFilterBottomSheet(List<MeetingData> meetingList, List<ParticipantData> participantList, OnFilterButtonClickListener listener) {
        super();

        this.meetingList = meetingList;
        this.participantList = participantList;
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the style so the background is Transparent
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_filter_meetings, container, false);

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

        peopleList = participantList.stream()
                .map(ParticipantData::getName)
                .collect(Collectors.toList());
        locationList = meetingList.stream()
                .map(MeetingData::getLocation)
                .distinct()
                .collect(Collectors.toList());
        locationList.remove(getString(R.string.meeting_data_no_location));

        for (String person : peopleList) {
            generateChipAndAppend(person, peopleChipGroup);
        }
        for (String location : locationList) {
            generateChipAndAppend(location, locationChipGroup);
        }
    }

    private void generateChipAndAppend(String s, ChipGroup cg) {
        Chip chip = new Chip(requireContext());
        chip.setId(ViewCompat.generateViewId());
        chip.setText(s);
        chip.setCheckable(true);
        cg.addView(chip);
    }

    private void setListeners() {
        resetButton.setOnClickListener(view -> {
            listener.onFilterButtonClicked(new VerlaufFragment.FilterData(false)); // pass empty filter, should return
            dismiss();
        });
        filterButton.setOnClickListener(view -> {
            boolean isValid = onFilterButtonClicked();
            if (isValid) {
                dismiss();
            }
        });

        MaterialDatePicker<Pair<Long, Long>> picker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("test")
                .setTheme(R.style.CustomDatePickerStyle)
                .build();

        picker.addOnPositiveButtonClickListener(selection -> {
            dateStart = selection.first;
            dateEnd = selection.second;
            DateFormat dateFormat = DateFormat.getDateInstance();
            dateStartEditText.setText(dateFormat.format(dateStart));
            dateEndEditText.setText(dateFormat.format(dateEnd));
        });

        picker.addOnNegativeButtonClickListener(view -> {
            dateStartEditText.setText("");
            dateEndEditText.setText("");
        });

        dateStartEditText.setOnClickListener(view -> picker.show(getParentFragmentManager(), getTag()));
        dateEndEditText.setOnClickListener(view -> picker.show(getParentFragmentManager(), getTag()));
    }

    private boolean onFilterButtonClicked() {
        VerlaufFragment.FilterData filterData = new VerlaufFragment.FilterData(true);

        List<String> filterDataPeople = new ArrayList<>();
        for (Integer id : peopleChipGroup.getCheckedChipIds()) {
            Chip chip = requireView().findViewById(id);
            filterDataPeople.add(chip.getText().toString());
        }
        filterData.setPeopleList(filterDataPeople);

        String countMinString = Objects.requireNonNull(countMinEditText.getText()).toString();
        if (countMinString.equals("")) {
            filterData.setMinCount(-1);
        } else {
            filterData.setMinCount(Integer.parseInt(countMinString));
        }
        String countMaxString = Objects.requireNonNull(countMaxEditText.getText()).toString();
        if (countMaxString.equals("")) {
            filterData.setMaxCount(-1);
        } else {
            filterData.setMaxCount(Integer.parseInt(countMaxString));
        }

        if (!countMaxString.equals("") && !countMinString.equals("")) {
            if (Integer.parseInt(countMaxString) < Integer.parseInt(countMinString)) {
                Toast toast = Toast.makeText(getContext(), R.string.more_min_then_max_warning, Toast.LENGTH_LONG);
                toast.show();
                return false;
            }
        }

        Chip chip = requireView().findViewById(locationChipGroup.getCheckedChipId());
        if (chip == null) {
            filterData.setLocation(null);
        } else {
            filterData.setLocation(chip.getText().toString());
        }

        filterData.setDateStart(dateStart);
        filterData.setDateEnd(dateEnd);

        listener.onFilterButtonClicked(filterData);
        return true;
    }
}
