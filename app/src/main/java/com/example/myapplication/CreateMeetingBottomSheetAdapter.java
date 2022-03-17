package com.example.myapplication;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.example.myapplication.databinding.CreateMeetingBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class CreateMeetingBottomSheetAdapter extends BottomSheetDialogFragment {

    CreateMeetingBottomSheetBinding bi;
    BottomSheetBehavior<View> bottomSheetBehavior;

    // Input fields
    String title;
    String location;

    // Preset lists
    ArrayList<String> itemNames = new ArrayList<>();
    ArrayList<Integer> selectValues = new ArrayList<>();

    // Location list
    ArrayList<String> locationNames = new ArrayList<>();

    // TODO: remove Debug
    ArrayList<String> timerItemNames = new ArrayList<>();
    ArrayList<Integer> timerSelectValues = new ArrayList<>();
    ArrayList<String> checklistItemNames = new ArrayList<>();
    ArrayList<Integer> checklistSelectValues = new ArrayList<>();

    PresetSelectBottomSheet presetSelectBottomSheet;
    LocationSelectBottomSheet locationSelectBottomSheet;
    // should the sheet be leave able
    boolean cancelable = true;
    boolean warning = false;

    final static Integer IS_CHECKLIST = 0;
    final static Integer IS_TIMER = 1;

    // check what preset is open
    int presetOpen = -1;

    // max scroll before it counts as attempt to close
    final static float MIN_SCROLL_FOR_CLOSURE = 0.5f;
    // Make the background Transparent
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the style so the background is Transparent
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

        // TODO: Remove debug Data
        checklistItemNames.add("Standard");
        checklistSelectValues.add(View.VISIBLE);

        checklistItemNames.add("Uni Konzept");
        checklistSelectValues.add(View.INVISIBLE);

        checklistItemNames.add("Web Meeting");
        checklistSelectValues.add(View.INVISIBLE);

        checklistItemNames.add("Arbeits Konzept");
        checklistSelectValues.add(View.INVISIBLE);

        // TODO: Remove debug Data
        timerItemNames.add("Standard");
        timerSelectValues.add(View.VISIBLE);

        timerItemNames.add("uni Timer");
        timerSelectValues.add(View.INVISIBLE);

        timerItemNames.add("Arbeits Timer");
        timerSelectValues.add(View.INVISIBLE);

        timerItemNames.add("Test Timer");
        timerSelectValues.add(View.INVISIBLE);

        timerItemNames.add("Weiterer Timer");
        timerSelectValues.add(View.INVISIBLE);


    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
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
                if(newState == BottomSheetBehavior.STATE_COLLAPSED && !cancelable){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // if its not cancelable open the warning
                if(!cancelable && slideOffset < MIN_SCROLL_FOR_CLOSURE){
                    openWarning();
                }

                // only allows the user to close if its cancelable
                bottomSheetBehavior.setHideable(cancelable);
            }
        });

        // cancel button clicked
        bi.dialogCancelButton.setOnClickListener(viewListener -> {
            // if cancelable close else show a warning
            if(cancelable)
                dismiss();
            else
                openWarning();
        });

        // edit button clicked
        bi.dialogCreateButton.setOnClickListener(viewListener -> {
            dismiss();

            // Open the Meeting wizard
            if(((MainActivity)getActivity()) != null)
                ((MainActivity)getActivity()).startMeetingWizard(title, location);
        });

        // location button clicked
        bi.buttonOrt.setOnClickListener(viewListener -> {
            // Open the Location Sheet
            locationSelectBottomSheet = new LocationSelectBottomSheet();

            // TODO: Load locations
            // TODO: Remove debug Data
            locationNames = new ArrayList<>();
            locationNames.add("Regensburg");
            locationNames.add("Passau");
            locationNames.add("Schwandorf");
            locationNames.add("München");
            locationNames.add("Hörsaal H2");

            // feeds the locations into the checklist recycler view
            locationSelectBottomSheet.initLocation(locationNames);
            locationSelectBottomSheet.show(getParentFragmentManager() , locationSelectBottomSheet.getTag());
        });

        // checklist preset button clicked
        bi.buttonChecklistPreset.setOnClickListener(viewListener -> {
            // Create a new Preset Bottom Sheet and set the title to checklist before showing
            presetSelectBottomSheet = new PresetSelectBottomSheet();
            presetSelectBottomSheet.setTitle("Checklist Preset");

            // TODO: Load all Presets
            itemNames = checklistItemNames;
            selectValues = checklistSelectValues;

            // feeds the presets into the checklist recycler view
            presetSelectBottomSheet.initPreset(itemNames, selectValues);
            presetSelectBottomSheet.show(getParentFragmentManager(), presetSelectBottomSheet.getTag());

            presetOpen = IS_CHECKLIST;
        });

        // timer preset button clicked
        bi.buttonTimerPreset.setOnClickListener(viewListener -> {
            // Create a new Preset Bottom Sheet and set the title to timer before showing
            presetSelectBottomSheet = new PresetSelectBottomSheet();
            presetSelectBottomSheet.setTitle("Timer Preset");

            // TODO: Load all Presets
            itemNames = timerItemNames;
            selectValues = timerSelectValues;

            // feeds the presets into the checklist recycler view
            presetSelectBottomSheet.initPreset(itemNames, selectValues);
            presetSelectBottomSheet.show(getParentFragmentManager(), presetSelectBottomSheet.getTag());

            presetOpen = IS_TIMER;
        });


        // Add a Text Change Listener to update the Title once text got changed
        bi.textInputMeeting.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // check if string is empty
                if(charSequence.length() != 0){
                    // activate the creation and save the String
                    title = "" + charSequence;
                }
                else{
                    // Change the title back to null
                    title = null;
                }

                // update the create Button
                isCreateable();
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        // Setup the create Button
        isCreateable();
        return bottomSheet;
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    // open the warning dialog
    private void openWarning(){
        // check if warning alrady open
        if(!warning){
            // set warning as true
            warning = true;
            // creates a Bottom sheet to create a meeting
            CustomAlertBottomSheetAdapter customAlertBottomSheetAdapter = new CustomAlertBottomSheetAdapter();
            customAlertBottomSheetAdapter.show(getParentFragmentManager() , customAlertBottomSheetAdapter.getTag());
        }
    }

    // resets the warning dialog so it can get opened again
    public void resetWarning(){
        warning = false;
    }

    // Sets the meeting location and displays it on the location Button
    public void setLocation(String location){
        this.location = location;
        bi.locationSelectedName.setText(location);
    }

    // Enables button if title got set
    private void isCreateable(){
        // if title is set enable button and set color to red
        if(title != null){
            bi.dialogCreateButton.setClickable(true);
            bi.dialogCreateButton.setTextColor(getResources().getColor(R.color.red, null));
            cancelable = false;
        }
        // if title is not set disable button and set color to gray
        else{
            bi.dialogCreateButton.setClickable(false);
            bi.dialogCreateButton.setTextColor(getResources().getColor(R.color.gray, null));
            cancelable = true;
        }
    }

    // Dismisses the newly created Meeting
    public void dismissMeeting(){
        dismiss();
    }

    // Change the displayed Preset on method call
    public void signalPresetChange(int adapterPosition) {
        // Checks what screen was open
        if(presetOpen == IS_CHECKLIST){
            // Sets the Text to the chosen one
            bi.checklistSelectedName.setText(itemNames.get(adapterPosition));
            // TODO: save changes
            checklistSelectValues.replaceAll(integer -> View.INVISIBLE);
            checklistSelectValues.set(adapterPosition, View.VISIBLE);
        }

        // Checks what screen was open
        if(presetOpen == IS_TIMER){
            // Sets the Text to the chosen one
            bi.timerSelectedName.setText(itemNames.get(adapterPosition));
            // TODO: save changes
            timerSelectValues.replaceAll(integer -> View.INVISIBLE);
            timerSelectValues.set(adapterPosition, View.VISIBLE);
        }

        // Set preset open to -1 before closing the sheet
        presetOpen = -1;

        // Reset the arrays to prevent double entries
        itemNames = new ArrayList<>();
        selectValues = new ArrayList<>();

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
}
