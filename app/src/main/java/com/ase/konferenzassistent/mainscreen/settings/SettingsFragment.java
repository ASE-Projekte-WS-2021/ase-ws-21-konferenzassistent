package com.ase.konferenzassistent.mainscreen.settings;

import static com.ase.konferenzassistent.shared.presets.CountdownPreset.convertToAdvancedCountdownList;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.ase.konferenzassistent.databinding.FragmentSettingsBinding;
import com.ase.konferenzassistent.shared.presets.ChecklistPreset;
import com.ase.konferenzassistent.shared.presets.CountdownPreset;
import com.ase.konferenzassistent.R;
import com.ase.konferenzassistent.data.RoomDB;
import com.ase.konferenzassistent.data.presets.checklist.ChecklistPresetPair;
import com.ase.konferenzassistent.data.presets.countdown.CountdownPresetPair;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment implements PresetEditBottomSheet.onCloseListener {
    FragmentSettingsBinding bi;
    RoomDB database;

    final ArrayList<CountdownPreset> countdownPresets = new ArrayList<>();
    final ArrayList<ChecklistPreset> checklistPreset = new ArrayList<>();

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = RoomDB.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // binding views to data binding
        bi = DataBindingUtil.bind(view);

        // on new checklist button
        assert bi != null;
        bi.buttonAddChecklist.setOnClickListener(viewListener -> {
            createChecklistList();
            PresetEditBottomSheet presetEditBottomSheet = new PresetEditBottomSheet();
            presetEditBottomSheet.setupView(checklistPreset, PresetEditBottomSheet.PRESET_TYPE_CHECKLIST, this);
            presetEditBottomSheet.show(getParentFragmentManager(), presetEditBottomSheet.getTag());

        });

        // on new countdown button
        bi.buttonAddCountdown.setOnClickListener(viewListener -> {
            createCountdownList();
            PresetEditBottomSheet presetEditBottomSheet = new PresetEditBottomSheet();
            presetEditBottomSheet.setupView(countdownPresets, PresetEditBottomSheet.PRESET_TYPE_COUNTDOWN, this);
            presetEditBottomSheet.show(getParentFragmentManager(), presetEditBottomSheet.getTag());
        });
    }

    private void createCountdownList() {
        // Load Countdowns Objects
        List<CountdownPresetPair> d;
        d = database.countdownPresetWIthParentDao().getCountdowns();

        countdownPresets.clear();
        d.forEach(preset -> {
            String presetName = preset.getPresets().getTitle();
            int presetId = preset.getPresets().getID();

            countdownPresets.add(new CountdownPreset(presetName, convertToAdvancedCountdownList(database, preset), presetId));
        });
    }

    private void createChecklistList() {
        List<ChecklistPresetPair> d;
        d = database.checklistPresetWithItemDao().getPresets();

        checklistPreset.clear();
        d.forEach(preset -> {
            String presetName = preset.getPresets().getTitle();
            Integer presetId = preset.getPresets().getID();

            checklistPreset.add(new ChecklistPreset(presetName, ChecklistPreset.convertToChecklistItems(preset), presetId));
        });
    }


    @Override
    public void onClose() {
        createCountdownList();
        createChecklistList();
    }
}
