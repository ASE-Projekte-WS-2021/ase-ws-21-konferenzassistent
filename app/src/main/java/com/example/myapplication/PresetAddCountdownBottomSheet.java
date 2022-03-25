package com.example.myapplication;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.checklist.ChecklistItem;
import com.example.myapplication.databinding.BottomSheetCountdownAddNewBinding;
import com.example.myapplication.meetingwizard.RecyclerViewAdvancedCountdownAdapter;
import com.example.myapplication.meetingwizard.RecyclerViewAdvancedCountdownItemAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class PresetAddCountdownBottomSheet extends BottomSheetDialogFragment implements CustomAlertBottomSheetAdapter.onLeaveListener{
    BottomSheetCountdownAddNewBinding bi;
    BottomSheetBehavior<View> bottomSheetBehavior;

    Boolean warning = false;

    // max scroll before it counts as attempt to close
    final static float MIN_SCROLL_FOR_CLOSURE = 0.5f;

    RecyclerViewCreatedCountdownElementsAdapter recyclerViewCreatedCountdownElementsAdapter;
    RecyclerViewCreatedChecklistAdapter recyclerViewCreatedChecklistAdapter;
    editingDone listener;

    Integer viewType;


    public void getViewType(Integer viewType) {
        this.viewType = viewType;
    }

    interface editingDone{
        void onEditingDone(Preset preset);
    }

    // Preset Lists
    private ArrayList<RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject> advancedCountdownObjects;
    private ArrayList<ChecklistItem> checklistItems;

    // Make the background Transparent
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        advancedCountdownObjects = new ArrayList<>();
        checklistItems = new ArrayList<>();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        // inflating Layout
        View view = View.inflate(bottomSheet.getContext(), R.layout.bottom_sheet_countdown_add_new, null);

        // binding views to data binding
        bi = DataBindingUtil.bind(view);

        // setting layout with bottom sheet
        bottomSheet.setContentView(view);

        bottomSheetBehavior = BottomSheetBehavior.from((View) (view.getParent()));

        //setting Peek at the 16:9 ratio keyline of its parent
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);

        // setting max height of bottom sheet
        //bi.extraSpace.setMinimumHeight((Resources.getSystem().getDisplayMetrics().heightPixels));
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        // skip it being collapsable
        bottomSheetBehavior.setSkipCollapsed(true);

        ((View) view.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent, null));

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // check if the state is collapsed while its not cancelable
                if(newState == BottomSheetBehavior.STATE_COLLAPSED){
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // if its not cancelable open the warning
                if(slideOffset < MIN_SCROLL_FOR_CLOSURE){
                    openWarning();
                }

                // only allows the user to close if its cancelable
                bottomSheetBehavior.setHideable(false);
            }
        });

        // Change the Layout depending on the ViewType
        if(viewType.equals(PresetEditBottomSheet.PRESET_TYPE_CHECKLIST)){
            bi.createNewText.setText("Checklistenitem hinzufügen");
            bi.createTopText.setText("Custom Checkliste erstellen");
        }

        // cancel button clicked
        bi.dialogCancelButton.setOnClickListener(viewListener -> openWarning());

        // On the add Button
        bi.addCountdown.setOnClickListener(viewListener -> {
            // If its the Countdown View
            if(viewType.equals(PresetEditBottomSheet.PRESET_TYPE_COUNTDOWN)){
                // Create new countdown item
                ArrayList<RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem> children = new ArrayList<>();
                children.add( new RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem((long)15, ""));

                // add it to the countdown and notify the recyclerView
                advancedCountdownObjects.add(new RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject("", true, children));
                recyclerViewCreatedCountdownElementsAdapter.notifyItemInserted(advancedCountdownObjects.size());
            }
            else
            {
                // create new checklist item and add it to the checklist array
                ChecklistItem item = new ChecklistItem("", "", false);
                checklistItems.add(item);
                // notify the recyclerview
                recyclerViewCreatedChecklistAdapter.notifyItemInserted(checklistItems.size());
            }

        });

        // On Save Button
        bi.dialogCreateButton.setOnClickListener(viewListener ->{
                    // check view
                    if(viewType.equals(PresetEditBottomSheet.PRESET_TYPE_COUNTDOWN)){
                        // Create a new Countdown Preset
                        listener.onEditingDone(
                            new CountdownPreset(
                                bi.presetName.getText().toString(), advancedCountdownObjects, -1)
                        );
                    }
                    else
                    {
                        // Create a new Checklist Preset
                        listener.onEditingDone(
                                new ChecklistPreset(
                                        bi.presetName.getText().toString(), checklistItems, -1)
                        );
                    }
                    dismiss();
                });

        setStyle(CustomAlertBottomSheetAdapter.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

        buildRecyclerView();

        return bottomSheet;
    }

    // Build and fills the recycler view depending on the preset
    private void buildRecyclerView(){
        RecyclerView recyclerView = bi.addRecycleview;
        if(viewType.equals(PresetEditBottomSheet.PRESET_TYPE_COUNTDOWN)){
            recyclerViewCreatedCountdownElementsAdapter = new RecyclerViewCreatedCountdownElementsAdapter(
                    advancedCountdownObjects,
                    this.getContext()
            );
            recyclerView.setAdapter(recyclerViewCreatedCountdownElementsAdapter);
        }
        else
        {
            recyclerViewCreatedChecklistAdapter = new RecyclerViewCreatedChecklistAdapter(
                    checklistItems
            );
            recyclerView.setAdapter(recyclerViewCreatedChecklistAdapter);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    public void getListener(editingDone listener){
        this.listener = listener;
    }

    // open the warning dialog
    private void openWarning(){
        // check if warning alrady open
        if(!warning){
            // set warning as true
            warning = true;
            // creates a Bottom sheet to create a meeting
            CustomAlertBottomSheetAdapter customAlertBottomSheetAdapter = new CustomAlertBottomSheetAdapter(this);
            customAlertBottomSheetAdapter.setWarningText("Soll dieses neue Preset verworfen werden?");
            customAlertBottomSheetAdapter.setAcceptText("Änderungen Verwerfen");
            customAlertBottomSheetAdapter.setDeclineText("Weiter Bearbeiten");
            customAlertBottomSheetAdapter.show(getParentFragmentManager() , customAlertBottomSheetAdapter.getTag());
        }
    }

    // resets the warning dialog so it can get opened again
    public void resetWarning(){
        warning = false;
    }

    @Override
    public void onLeaving() {
        dismiss();
    }

    @Override
    public void clearWarnings() {
        resetWarning();
    }
}
