package com.example.myapplication;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.example.myapplication.data.RoomDB;
import com.example.myapplication.data.presets.checklist.ChecklistPresetPair;
import com.example.myapplication.data.presets.countdown.CountdownPresetPair;
import com.example.myapplication.databinding.CreateMeetingBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class CreateMeetingBottomSheetAdapter extends BottomSheetDialogFragment implements CustomAlertBottomSheetAdapter.onLeaveListener {

    final static Integer IS_CHECKLIST = 0;
    final static Integer IS_TIMER = 1;
    // max scroll before it counts as attempt to close
    final static float MIN_SCROLL_FOR_CLOSURE = 0.5f;
    CreateMeetingBottomSheetBinding bi;
    BottomSheetBehavior<View> bottomSheetBehavior;
    RoomDB database;
    // Input fields
    String title;
    String location;
    ArrayList<Integer> timerSelectValues = new ArrayList<>();
    CountdownPresetPair selectedPair;
    ChecklistPresetPair selectedChecklist;
    // Preset lists
    ArrayList<String> itemNames = new ArrayList<>();
    List<CountdownPresetPair> presetPairs;
    List<ChecklistPresetPair> checklistPresetPairs;
    // Location list
    ArrayList<String> locationNames = new ArrayList<>();
    ArrayList<Integer> checklistSelectValues = new ArrayList<>();
    PresetSelectBottomSheet presetSelectBottomSheet;
    LocationSelectBottomSheet locationSelectBottomSheet;
    // should the sheet be leave able
    boolean cancelable = true;
    boolean warning = false;
    // check what preset is open
    int presetOpen = -1;

    // Make the background Transparent
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the style so the background is Transparent
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

        database = RoomDB.getInstance(getContext());

        // Checklists
        checklistPresetPairs = database.checklistPresetWithItemDao().getPresets();
        checklistPresetPairs.forEach(preset -> {
            checklistSelectValues.add(View.INVISIBLE);
        });
        checklistSelectValues.set(0, View.VISIBLE);

        // Countdowns
        presetPairs = database.countdownPresetWIthParentDao().getCountdowns();
        presetPairs.forEach(preset -> {
            timerSelectValues.add(View.INVISIBLE);
        });
        timerSelectValues.set(0, View.VISIBLE);

        // Load locations
        locationNames = new ArrayList<>();
        List<String> location = database.meetingDao().getLocations();

        locationNames.addAll(location);

        locationNames.remove(getString(R.string.meeting_data_no_location));
        Log.i("TAG", "onCreate: " + locationNames);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        // inflating Layout
        View view = View.inflate(bottomSheet.getContext(), R.layout.create_meeting_bottom_sheet, null);

        // binding views to data binding
        bi = DataBindingUtil.bind(view);

        // setting layout with bottom sheet
        bottomSheet.setContentView(view);

        bottomSheetBehavior = BottomSheetBehavior.from((View) (view.getParent()));

        //setting Peek at the 16:9 ratio keyline of its parent
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);

        // setting max height of bottom sheet
        bi.extraSpace.setMinimumHeight((Resources.getSystem().getDisplayMetrics().heightPixels));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        // skip it being collapsable
        bottomSheetBehavior.setSkipCollapsed(true);

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // check if the state is collapsed while its not cancelable
                if (newState == BottomSheetBehavior.STATE_COLLAPSED && !cancelable) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // if its not cancelable open the warning
                if (!cancelable && slideOffset < MIN_SCROLL_FOR_CLOSURE) {
                    openWarning();
                }

                // only allows the user to close if its cancelable
                bottomSheetBehavior.setHideable(cancelable);
            }
        });

        // cancel button clicked
        bi.dialogCancelButton.setOnClickListener(viewListener -> {
            // if cancelable close else show a warning
            if (cancelable)
                dismiss();
            else
                openWarning();
        });

        // Start button clicked
        bi.dialogCreateButton.setOnClickListener(viewListener -> {
            dismiss();

            // if nothing got selected
            if (selectedPair == null) {
                selectedPair = presetPairs.get(0);
            }

            if (selectedChecklist == null) {
                selectedChecklist = checklistPresetPairs.get(0);
            }

            // Open the Meeting wizard
            if (((MainActivity) getActivity()) != null)
                ((MainActivity) getActivity()).startMeetingWizard(title, location, selectedPair, selectedChecklist);
        });

        // location button clicked
        bi.buttonOrt.setOnClickListener(viewListener -> {
            // Open the Location Sheet
            locationSelectBottomSheet = new LocationSelectBottomSheet();


            // feeds the locations into the checklist recycler view
            locationSelectBottomSheet.initLocation(locationNames);
            locationSelectBottomSheet.show(getParentFragmentManager(), locationSelectBottomSheet.getTag());
        });

        // checklist preset button clicked
        bi.buttonChecklistPreset.setOnClickListener(viewListener -> {
            // Create a new Preset Bottom Sheet and set the title to checklist before showing
            presetSelectBottomSheet = new PresetSelectBottomSheet();
            presetSelectBottomSheet.setTitle("Checklist Preset");

            // feeds the presets into the checklist recycler view
            presetSelectBottomSheet.initPreset(checklistPresetPairs, checklistSelectValues);
            presetSelectBottomSheet.show(getParentFragmentManager(), presetSelectBottomSheet.getTag());

            presetOpen = IS_CHECKLIST;
        });

        // timer preset button clicked
        bi.buttonTimerPreset.setOnClickListener(viewListener -> {
            // Create a new Preset Bottom Sheet and set the title to timer before showing
            presetSelectBottomSheet = new PresetSelectBottomSheet();
            presetSelectBottomSheet.setTitle("Timer Preset");

            // feeds the presets into the checklist recycler view
            presetSelectBottomSheet.initPreset(presetPairs, timerSelectValues);
            presetSelectBottomSheet.show(getParentFragmentManager(), presetSelectBottomSheet.getTag());

            presetOpen = IS_TIMER;
        });


        // Add a Text Change Listener to update the Title once text got changed
        bi.textInputMeeting.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // check if string is empty
                if (charSequence.length() != 0) {
                    // activate the creation and save the String
                    title = "" + charSequence;
                } else {
                    // Change the title back to null
                    title = null;
                }

                // update the create Button
                isCreateable();
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // Setup the create Button
        isCreateable();
        return bottomSheet;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    // open the warning dialog
    private void openWarning() {
        // check if warning alrady open
        if (!warning) {
            // set warning as true
            warning = true;
            // creates a Bottom sheet to create a meeting
            CustomAlertBottomSheetAdapter customAlertBottomSheetAdapter = new CustomAlertBottomSheetAdapter(this);
            customAlertBottomSheetAdapter.setWarningText("Solle dieses neue Meeting wirlich gelöscht werden?");
            customAlertBottomSheetAdapter.setAcceptText("Änderungen Verwerfen");
            customAlertBottomSheetAdapter.setDeclineText("Weiter Bearbeiten");
            customAlertBottomSheetAdapter.show(getParentFragmentManager(), customAlertBottomSheetAdapter.getTag());
        }
    }

    // resets the warning dialog so it can get opened again
    public void resetWarning() {
        warning = false;
    }

    // Sets the meeting location and displays it on the location Button
    public void setLocation(String location) {
        this.location = location;
        bi.locationSelectedName.setText(location);
    }

    // Enables button if title got set
    private void isCreateable() {
        // if title is set enable button and set color to red
        if (title != null) {
            bi.dialogCreateButton.setClickable(true);
            bi.dialogCreateButton.setTextColor(getResources().getColor(R.color.white, null));
            bi.dialogCreateButton.setBackground(getResources().getDrawable(R.drawable.btn_round));
            cancelable = false;
        }
        // if title is not set disable button and set color to gray
        else {
            bi.dialogCreateButton.setClickable(false);
            //bi.dialogCreateButton.setTextColor(getResources().getColor(R.color.gray, null));
            bi.dialogCreateButton.setBackground(getResources().getDrawable(R.drawable.btn_round_disabled));
            bi.dialogCreateButton.setTextColor(getResources().getColor(R.color.dark_gray));
            cancelable = true;
        }
    }

    // Dismisses the newly created Meeting
    public void dismissMeeting() {
        dismiss();
    }

    // Change the displayed Preset on method call
    public void signalPresetChange(int adapterPosition) {
        // Checks what screen was open
        if (presetOpen == IS_CHECKLIST) {
            // Sets the Text to the chosen one
            bi.checklistSelectedName.setText(checklistPresetPairs.get(adapterPosition).getPresets().getTitle());
            selectedChecklist = checklistPresetPairs.get(adapterPosition);

            checklistSelectValues.replaceAll(integer -> View.INVISIBLE);
            checklistSelectValues.set(adapterPosition, View.VISIBLE);
        }

        // Checks what screen was open
        if (presetOpen == IS_TIMER) {
            // Sets the Text to the chosen one
            bi.timerSelectedName.setText(presetPairs.get(adapterPosition).getPresets().getTitle());
            selectedPair = presetPairs.get(adapterPosition);


            timerSelectValues.replaceAll(integer -> View.INVISIBLE);
            timerSelectValues.set(adapterPosition, View.VISIBLE);
        }

        // Set preset open to -1 before closing the sheet
        presetOpen = -1;

        // close the sheet
        presetSelectBottomSheet.closePresets();
    }

    public void signalLocationChange(int adapterPosition) {
        // Set the location
        location = locationNames.get(adapterPosition);

        // Set the location text
        bi.locationSelectedName.setText(location);

        // close the sheet
        locationSelectBottomSheet.closeLocation();
    }

    @Override
    public void onLeaving() {
        dismissMeeting();
    }

    @Override
    public void clearWarnings() {
        resetWarning();
    }
}
