package com.ase.konferenzassistent.mainscreen.settings;

import static com.ase.konferenzassistent.mainscreen.settings.PresetEditBottomSheet.PRESET_TYPE_COUNTDOWN;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ase.konferenzassistent.R;
import com.ase.konferenzassistent.databinding.BottomSheetCountdownAddNewBinding;
import com.ase.konferenzassistent.mainscreen.recycleviews.RecyclerViewCreatedChecklistAdapter;
import com.ase.konferenzassistent.mainscreen.recycleviews.RecyclerViewCreatedCountdownElementsAdapter;
import com.ase.konferenzassistent.countdown.AdvancedCountdownObject;
import com.ase.konferenzassistent.shared.CustomAlertBottomSheetAdapter;
import com.ase.konferenzassistent.shared.interfaces.Preset;
import com.ase.konferenzassistent.checklist.ChecklistItem;
import com.ase.konferenzassistent.meetingwizard.RecyclerViewAdvancedCountdownItemAdapter;
import com.ase.konferenzassistent.shared.presets.ChecklistPreset;
import com.ase.konferenzassistent.shared.presets.CountdownPreset;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class PresetAddBottomSheet extends BottomSheetDialogFragment implements CustomAlertBottomSheetAdapter.onLeaveListener {
    // max scroll before it counts as attempt to close
    final static float MIN_SCROLL_FOR_CLOSURE = 0.5f;

    BottomSheetCountdownAddNewBinding bi;
    BottomSheetBehavior<View> bottomSheetBehavior;
    Boolean warning = false;
    RecyclerViewCreatedCountdownElementsAdapter recyclerViewCreatedCountdownElementsAdapter;
    RecyclerViewCreatedChecklistAdapter recyclerViewCreatedChecklistAdapter;
    Preset preset;
    PresetEditBottomSheet parentSheet;
    editingDone listener;
    int itemPosition;

    Integer viewType;
    // Preset Lists
    private final ArrayList<AdvancedCountdownObject> advancedCountdownObjects = new ArrayList<>();
    private final ArrayList<ChecklistItem> checklistItems = new ArrayList<>();

    public void setVariables(Integer viewType, Preset preset, PresetEditBottomSheet parentSheet, int itemPosition) {
        this.viewType = viewType;
        // If the preset is not null go into edit mode
        if(preset != null){
            if(viewType == PRESET_TYPE_COUNTDOWN){
                advancedCountdownObjects.addAll(((CountdownPreset)preset).getAdvancedCountdownObject());
            }
            else
            {
                checklistItems.addAll(((ChecklistPreset)preset).getChecklistItems());
            }
            this.preset = preset;
            this.itemPosition = itemPosition;
        }
        this.parentSheet = parentSheet;
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
        View view = View.inflate(bottomSheet.getContext(), R.layout.bottom_sheet_countdown_add_new, null);

        // binding views to data binding
        bi = DataBindingUtil.bind(view);

        // setting layout with bottom sheet
        bottomSheet.setContentView(view);

        bottomSheetBehavior = BottomSheetBehavior.from((View) (view.getParent()));

        //setting Peek at the 16:9 ratio keyline of its parent
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO);

        // setting max height of bottom sheet
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        // skip it being collapsable
        bottomSheetBehavior.setSkipCollapsed(true);

        ((View) view.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent, null));

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // check if the state is collapsed while its not cancelable
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // if its not cancelable open the warning
                if (slideOffset < MIN_SCROLL_FOR_CLOSURE) {
                    openWarning();
                }
                // only allows the user to close if its cancelable
                bottomSheetBehavior.setHideable(false);
            }
        });

        // Change the Layout depending on the ViewType
        if (viewType.equals(PresetEditBottomSheet.PRESET_TYPE_CHECKLIST)) {
            bi.createNewText.setText(R.string.add_checklist_item);
            bi.createTopText.setText(R.string.create_custom_checklist);
        }

        // Set Preset Name and checks if preset already exists
        if(preset != null){
            bi.presetName.setText(preset.getTitle());
            bi.deletePreset.setVisibility(View.VISIBLE);
            bi.deletePresetButton.setOnClickListener(viewListener ->{
                    if(preset.getTitle().equals(getString(R.string.standard))) {
                        Toast.makeText(getContext(), R.string.warning_deleting_standard,
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    CustomAlertBottomSheetAdapter customAlertBottomSheetAdapter =
                            new CustomAlertBottomSheetAdapter(new CustomAlertBottomSheetAdapter.onLeaveListener() {
                        @Override
                        public void onLeaving() {
                            deletePreset();
                            dismiss();
                        }

                        @Override
                        public void clearWarnings() {

                        }
                    });
                    customAlertBottomSheetAdapter.setWarningText(getString(R.string.delete_preset_warning_1) + preset.getTitle() + getString(R.string.delete_preset_warning_2));
                    customAlertBottomSheetAdapter.setAcceptText(getString(R.string.delete_preset)); // Positives Feedback
                    customAlertBottomSheetAdapter.setDeclineText(getString(R.string.keep_preset)); // Negatives Feedback
                    customAlertBottomSheetAdapter.show(((AppCompatActivity) requireContext()).getSupportFragmentManager(), customAlertBottomSheetAdapter.getTag());

            });
        }
        // cancel button clicked
        bi.dialogCancelButton.setOnClickListener(viewListener -> openWarning());

        // On the add Button
        bi.addCountdown.setOnClickListener(viewListener -> {
            // If its the Countdown View
            if (viewType.equals(PRESET_TYPE_COUNTDOWN)) {
                // Create new countdown item
                ArrayList<RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem> children = new ArrayList<>();
                children.add(new RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem((long) 15, ""));

                // add it to the countdown and notify the recyclerView
                advancedCountdownObjects.add(new AdvancedCountdownObject("", true, children));
                recyclerViewCreatedCountdownElementsAdapter.notifyItemInserted(advancedCountdownObjects.size());
            } else {
                // create new checklist item and add it to the checklist array
                ChecklistItem item = new ChecklistItem("", "", false);
                checklistItems.add(item);
                // notify the recyclerview
                recyclerViewCreatedChecklistAdapter.notifyItemInserted(checklistItems.size());
            }

        });

        // On Save Button
        bi.dialogCreateButton.setOnClickListener(viewListener -> {
            // check view
            if (viewType.equals(PRESET_TYPE_COUNTDOWN)) {
                // Create a new Countdown Preset
                listener.onEditingDone(
                        new CountdownPreset(
                                bi.presetName.getText().toString(), advancedCountdownObjects,
                                preset == null? -1 : preset.getID()), itemPosition
                );
            } else {
                // Create a new Checklist Preset
                listener.onEditingDone(
                        new ChecklistPreset(
                                bi.presetName.getText().toString(), checklistItems,
                                preset == null? -1 : preset.getID()), itemPosition
                );
            }
            dismiss();
        });

        setStyle(CustomAlertBottomSheetAdapter.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        buildRecyclerView();

        return bottomSheet;
    }

    private void deletePreset(){
            parentSheet.deletePreset(itemPosition);
    }

    // Build and fills the recycler view depending on the preset
    private void buildRecyclerView() {
        RecyclerView recyclerView = bi.addRecycleview;
        if (viewType.equals(PRESET_TYPE_COUNTDOWN)) {
            recyclerViewCreatedCountdownElementsAdapter = new RecyclerViewCreatedCountdownElementsAdapter(
                    advancedCountdownObjects,
                    this.getContext()
            );
            recyclerView.setAdapter(recyclerViewCreatedCountdownElementsAdapter);
        } else {
            recyclerViewCreatedChecklistAdapter = new RecyclerViewCreatedChecklistAdapter(
                    checklistItems
            );
            recyclerView.setAdapter(recyclerViewCreatedChecklistAdapter);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
    }

    public void getListener(editingDone listener) {
        this.listener = listener;
    }

    // open the warning dialog
    private void openWarning() {
        // check if warning alrady open
        if (!warning) {
            // set warning as true
            warning = true;
            // creates a Bottom sheet to create a meeting
            CustomAlertBottomSheetAdapter customAlertBottomSheetAdapter = new CustomAlertBottomSheetAdapter(this);
            customAlertBottomSheetAdapter.setWarningText(getString(R.string.delete_preset_warning_general));
            customAlertBottomSheetAdapter.setAcceptText(getString(R.string.delete_preset_warning_general_positiv));
            customAlertBottomSheetAdapter.setDeclineText(getString(R.string.delete_preset_warning_general_negativ));
            customAlertBottomSheetAdapter.show(getParentFragmentManager(), customAlertBottomSheetAdapter.getTag());
        }
    }

    // resets the warning dialog so it can get opened again
    public void resetWarning() {
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

    public interface editingDone {
        void onEditingDone(Preset preset, int itemPosition);
    }
}
