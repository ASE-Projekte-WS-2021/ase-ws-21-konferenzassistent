package com.example.myapplication;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.databinding.BottomSheetPresetsBinding;
import com.example.myapplication.meetingwizard.RecyclerViewAdvancedCountdownAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class PresetEditBottomSheet extends BottomSheetDialogFragment {
    BottomSheetPresetsBinding bi;
    BottomSheetBehavior<View> bottomSheetBehavior;

    RecyclerViewCountdownPresetAdapter recyclerViewPresetAdapter;

    public static final int PRESET_TYPE_COUNTDOWN = 0;
    public static final int PRESET_TYPE_CHECKLIST = 1;

    Integer viewType;

    // Preset Lists
    private ArrayList<CountdownPreset> countdownObjects = new ArrayList<>();

    // TODO CHECKLIST
    //private ArrayList<Checklist> countdownObjects = new ArrayList<>();

    // Make the background Transparent
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        BottomSheetDialog bottomSheet = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        // inflating Layout
        View view = View.inflate(bottomSheet.getContext(), R.layout.bottom_sheet_presets, null);

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
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


        // cancel button clicked
        bi.buttonDismiss.setOnClickListener(viewListener -> dismiss());

        setStyle(CustomAlertBottomSheetAdapter.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

        // Builds the View with the right content
        configView();
        buildRecyclerView();

        return bottomSheet;
    }
    
    // Configs the view to match the right Preset
    private void configView(){
        if(viewType == PRESET_TYPE_COUNTDOWN){
            bi.presetHeaderText.setText("Timer bearbeiten");
        }
        else{
            bi.presetHeaderText.setText("Checkliste bearbeiten");
        }
    }
    
    // Set the text of the header
    public void setupView(ArrayList<CountdownPreset> object, int viewType){
        this.viewType = viewType;
        countdownObjects = new ArrayList<>();
        countdownObjects.addAll(object);
        Log.i("TAG", "setupView: " + object);
    }


    // Build and fills the recycler view
    private void buildRecyclerView(){
        RecyclerView recyclerView = bi.presetRecyclerView;
        recyclerViewPresetAdapter = new RecyclerViewCountdownPresetAdapter(
                countdownObjects,
                this.getContext()
                );
        recyclerView.setAdapter(recyclerViewPresetAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }


    @Override
    public void onStart(){
        super.onStart();
        //bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

}
