package com.example.myapplication;

import static com.example.myapplication.CountdownPreset.convertToDatabaseEntry;
import static com.example.myapplication.CountdownPreset.removeFromDatabase;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.data.RoomDB;
import com.example.myapplication.databinding.BottomSheetEditPresetsBinding;
import com.example.myapplication.databinding.BottomSheetPresetsBinding;
import com.example.myapplication.meetingwizard.RecyclerViewAdvancedCountdownAdapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class PresetEditBottomSheet extends BottomSheetDialogFragment  implements PresetAddCountdownBottomSheet.editingDone, RecyclerViewCountdownPresetAdapter.onDeletionListener {
    BottomSheetEditPresetsBinding bi;
    BottomSheetBehavior<View> bottomSheetBehavior;

    RecyclerViewCountdownPresetAdapter recyclerViewPresetAdapter;

    onCloseListener listener;

    public static final int PRESET_TYPE_COUNTDOWN = 0;
    public static final int PRESET_TYPE_CHECKLIST = 1;

    Integer viewType;

    @Override
    public void onDelete(int position) {
        int id = countdownObjects.get(position).id;
        Log.i("TAG", "onDelete: " +id);
        countdownObjects.remove(position);
        recyclerViewPresetAdapter.notifyItemRemoved(countdownObjects.size());
        removeFromDatabase(RoomDB.getInstance(getContext()),id);
    }

    interface onCloseListener{
        void onClose();
    }

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
        View view = View.inflate(bottomSheet.getContext(), R.layout.bottom_sheet_edit_presets, null);

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

        // create button event
        bi.buttonCreate.setOnClickListener(viewListener ->{
            PresetAddCountdownBottomSheet presetAddCountdownBottomSheet = new PresetAddCountdownBottomSheet();
            presetAddCountdownBottomSheet.getListener(this);
            presetAddCountdownBottomSheet.show(getParentFragmentManager(), presetAddCountdownBottomSheet.getTag());
        });

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
            bi.buttonCreateText.setText("Neuen Timer erstellen");
        }
        else{
            bi.presetHeaderText.setText("Checkliste bearbeiten");
            bi.buttonCreateText.setText("Neue Checkliste erstellen");
        }
    }
    
    // Set the text of the header
    public void setupView(ArrayList<CountdownPreset> object, int viewType, onCloseListener listener){
        this.viewType = viewType;
        countdownObjects = new ArrayList<>();
        countdownObjects.addAll(object);
        this.listener = listener;
    }


    // Build and fills the recycler view
    private void buildRecyclerView(){
        RecyclerView recyclerView = bi.presetRecyclerView;
        recyclerViewPresetAdapter = new RecyclerViewCountdownPresetAdapter(
                countdownObjects,
                this,
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

    @Override
    public void onEditingDone(CountdownPreset preset) {
        countdownObjects.add(preset);
        recyclerViewPresetAdapter.notifyItemInserted(countdownObjects.size());
        writePresetToDatabase(preset);
        listener.onClose();
    }

    private void writePresetToDatabase(CountdownPreset preset){
        convertToDatabaseEntry(RoomDB.getInstance(getContext()), preset);
    }


}
