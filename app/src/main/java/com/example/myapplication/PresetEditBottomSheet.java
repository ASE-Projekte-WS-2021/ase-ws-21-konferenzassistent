package com.example.myapplication;

import static com.example.myapplication.ChecklistPreset.convertToChecklistDatabaseEntry;
import static com.example.myapplication.ChecklistPreset.removeChecklistFromDatabase;
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
import java.util.Collection;

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
        if(viewType.equals(PRESET_TYPE_COUNTDOWN)){
            int id = countdownObjects.get(position).id;
            countdownObjects.remove(position);
            recyclerViewPresetAdapter.notifyItemRemoved(countdownObjects.size());
            removeFromDatabase(RoomDB.getInstance(getContext()),id);
        }
        else
        {
            int id = checklistPresets.get(position).id;
            checklistPresets.remove(position);
            recyclerViewPresetAdapter.notifyItemRemoved(checklistPresets.size());
            removeChecklistFromDatabase(RoomDB.getInstance(getContext()), id);
        }
    }

    interface onCloseListener{
        void onClose();
    }

    // Preset Lists
    private ArrayList<CountdownPreset> countdownObjects = new ArrayList<>();

    private ArrayList<ChecklistPreset> checklistPresets = new ArrayList<>();

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
            presetAddCountdownBottomSheet.getViewType(viewType);
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
            bi.presetHeaderText.setText("Deine Custon Countdownsets");
            bi.buttonCreateText.setText("Neues Countdownset erstellen");
        }
        else{
            bi.presetHeaderText.setText("Checkliste bearbeiten");
            bi.buttonCreateText.setText("Neue Checkliste erstellen");
        }
    }
    
    // Set the text of the header and reads all important information
    public void setupView(ArrayList<?> object, int viewType, onCloseListener listener){
        this.viewType = viewType;
        if(viewType == PRESET_TYPE_COUNTDOWN){
            countdownObjects = new ArrayList<>();
            countdownObjects.addAll((Collection<? extends CountdownPreset>) object);
        }
        else
        {
            checklistPresets = new ArrayList<>();
            checklistPresets.addAll((Collection<? extends ChecklistPreset>) object);
        }

        this.listener = listener;
    }



    // Build and fills the recycler view
    private void buildRecyclerView(){
        RecyclerView recyclerView = bi.presetRecyclerView;
        // Checks what type of view it is and fills it with the right Preset
        recyclerViewPresetAdapter = new RecyclerViewCountdownPresetAdapter(
                viewType.equals(PRESET_TYPE_COUNTDOWN)? countdownObjects: checklistPresets,
                this,
                this.getContext()
                );
        recyclerView.setAdapter(recyclerViewPresetAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    @Override
    public void onEditingDone(Preset preset) {
        // Checks what view type is active
        if(viewType.equals(PRESET_TYPE_COUNTDOWN)){
            countdownObjects.add((CountdownPreset) preset);
            recyclerViewPresetAdapter.notifyItemInserted(countdownObjects.size());
            writeCountdownPresetToDatabase((CountdownPreset) preset);
        }
        else
        {
            checklistPresets.add((ChecklistPreset)preset);
            recyclerViewPresetAdapter.notifyItemInserted(checklistPresets.size());
            writeChecklistPresetToDatabase((ChecklistPreset) preset);
        }

        listener.onClose();
    }

    private void writeCountdownPresetToDatabase(CountdownPreset preset){
        convertToDatabaseEntry(RoomDB.getInstance(getContext()), preset);
    }

    private void writeChecklistPresetToDatabase(ChecklistPreset preset){
        convertToChecklistDatabaseEntry(RoomDB.getInstance(getContext()), preset);
    }


}
