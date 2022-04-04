package com.ase.konferenzassistent.mainscreen.settings;

import static com.ase.konferenzassistent.shared.presets.ChecklistPreset.convertToChecklistDatabaseEntry;
import static com.ase.konferenzassistent.shared.presets.ChecklistPreset.removeChecklistFromDatabase;
import static com.ase.konferenzassistent.shared.presets.CountdownPreset.convertToAdvancedCountdownList;
import static com.ase.konferenzassistent.shared.presets.CountdownPreset.convertToDatabaseEntry;
import static com.ase.konferenzassistent.shared.presets.CountdownPreset.removeFromDatabase;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.konferenzassistent.R;
import com.ase.konferenzassistent.databinding.BottomSheetEditPresetsBinding;
import com.ase.konferenzassistent.mainscreen.recycleviews.RecyclerViewCountdownPresetAdapter;
import com.ase.konferenzassistent.shared.CustomAlertBottomSheetAdapter;
import com.ase.konferenzassistent.shared.Interfaces.Preset;
import com.ase.konferenzassistent.data.RoomDB;
import com.ase.konferenzassistent.data.presets.checklist.ChecklistPresetPair;
import com.ase.konferenzassistent.data.presets.countdown.CountdownPresetPair;
import com.ase.konferenzassistent.shared.presets.ChecklistPreset;
import com.ase.konferenzassistent.shared.presets.CountdownPreset;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class PresetEditBottomSheet extends BottomSheetDialogFragment implements PresetAddBottomSheet.editingDone {
    public static final int PRESET_TYPE_COUNTDOWN = 0;
    public static final int PRESET_TYPE_CHECKLIST = 1;
    BottomSheetEditPresetsBinding bi;
    BottomSheetBehavior<View> bottomSheetBehavior;
    RecyclerViewCountdownPresetAdapter recyclerViewPresetAdapter;
    onCloseListener listener;
    Integer viewType;
    // Preset Lists
    private ArrayList<CountdownPreset> countdownObjects = new ArrayList<>();
    private ArrayList<ChecklistPreset> checklistPresets = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void deletePreset(int position) {
        RoomDB database = RoomDB.getInstance(getContext());
        if (viewType.equals(PRESET_TYPE_COUNTDOWN)) {
            // Get database entries to prevent null ids
            countdownObjects.clear();
            ArrayList<CountdownPresetPair> d = new ArrayList<>(database.countdownPresetWIthParentDao().getCountdowns());
            d.forEach(countdownPreset -> {
                String presetName = countdownPreset.getPresets().getTitle();
                int presetId = countdownPreset.getPresets().getID();

                countdownObjects.add(
                        new CountdownPreset(presetName,
                                convertToAdvancedCountdownList(database, countdownPreset), presetId));
            });


            int id =  countdownObjects.get(position).getID();
            countdownObjects.remove(position);
            recyclerViewPresetAdapter.notifyItemRemoved(countdownObjects.size());
            removeFromDatabase(RoomDB.getInstance(getContext()), id);
        } else {
            // Get database entries to prevent null ids
            checklistPresets.clear();
            ArrayList<ChecklistPresetPair> d = new ArrayList<>(database.checklistPresetWithItemDao().getPresets());
            d.forEach(checklistPreset -> {
                String presetName = checklistPreset.getPresets().getTitle();
                Integer presetId = checklistPreset.getPresets().getID();

                checklistPresets.add(
                        new ChecklistPreset(presetName,
                                ChecklistPreset.convertToChecklistItems(checklistPreset), presetId));
            });

            int id =  checklistPresets.get(position).getID();
            checklistPresets.remove(position);
            recyclerViewPresetAdapter.notifyItemRemoved(checklistPresets.size());
            removeChecklistFromDatabase(RoomDB.getInstance(getContext()), id);

        }
        recyclerViewPresetAdapter.notifyDataSetChanged();
    }

    // Make the background Transparent
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
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

        // cancel button clicked
        bi.buttonDismiss.setOnClickListener(viewListener -> dismiss());

        // create button event
        bi.buttonCreate.setOnClickListener(viewListener -> {
            PresetAddBottomSheet presetAddBottomSheet = new PresetAddBottomSheet();
            presetAddBottomSheet.getListener(this);
            presetAddBottomSheet.setVariables(viewType, null, this, -1);
            presetAddBottomSheet.show(getParentFragmentManager(), presetAddBottomSheet.getTag());
        });

        setStyle(CustomAlertBottomSheetAdapter.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);

        // Builds the View with the right content
        configView();
        buildRecyclerView();

        return bottomSheet;
    }

    // Configs the view to match the right Preset
    private void configView() {
        if (viewType == PRESET_TYPE_COUNTDOWN) {
            bi.presetHeaderText.setText("Deine Custon Countdownsets");
            bi.buttonCreateText.setText("Neues Countdownset erstellen");
        } else {
            bi.presetHeaderText.setText("Checkliste bearbeiten");
            bi.buttonCreateText.setText("Neue Checkliste erstellen");
        }
    }

    // Set the text of the header and reads all important information
    @SuppressWarnings("unchecked")
    public void setupView(ArrayList<?> object, int viewType, onCloseListener listener) {
        this.viewType = viewType;
        if (viewType == PRESET_TYPE_COUNTDOWN) {
            countdownObjects = new ArrayList<>();
            countdownObjects.addAll((Collection<? extends CountdownPreset>) object);
        } else {
            checklistPresets = new ArrayList<>();
            checklistPresets.addAll((Collection<? extends ChecklistPreset>) object);
        }

        this.listener = listener;
    }

    // Build and fills the recycler view
    private void buildRecyclerView() {
        RecyclerView recyclerView = bi.presetRecyclerView;
        // Checks what type of view it is and fills it with the right Preset
        recyclerViewPresetAdapter = new RecyclerViewCountdownPresetAdapter(
                viewType.equals(PRESET_TYPE_COUNTDOWN) ? countdownObjects : checklistPresets,
                this.getContext(),
                this.viewType,
                this
        );
        recyclerView.setAdapter(recyclerViewPresetAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    @Override
    public void onEditingDone(Preset preset) {
        // Checks what view type is active
        if (viewType.equals(PRESET_TYPE_COUNTDOWN)) {
            countdownObjects.add((CountdownPreset) preset);
            recyclerViewPresetAdapter.notifyItemInserted(countdownObjects.size());
            writeCountdownPresetToDatabase((CountdownPreset) preset);
        } else {
            checklistPresets.add((ChecklistPreset) preset);
            recyclerViewPresetAdapter.notifyItemInserted(checklistPresets.size());
            writeChecklistPresetToDatabase((ChecklistPreset) preset);
        }
        listener.onClose();
    }

    private void writeCountdownPresetToDatabase(CountdownPreset preset) {
        convertToDatabaseEntry(RoomDB.getInstance(getContext()), preset);
    }

    private void writeChecklistPresetToDatabase(ChecklistPreset preset) {
        convertToChecklistDatabaseEntry(RoomDB.getInstance(getContext()), preset);
    }

    public interface onCloseListener {
        void onClose();
    }


}
