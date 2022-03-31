package com.example.myapplication;

import static com.example.myapplication.CountdownPreset.convertToAdvancedCountdownList;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.checklist.ChecklistItem;
import com.example.myapplication.data.RoomDB;
import com.example.myapplication.data.presets.checklist.ChecklistPresetPair;
import com.example.myapplication.data.presets.countdown.CountdownItemData;
import com.example.myapplication.data.presets.countdown.CountdownParentData;
import com.example.myapplication.data.presets.countdown.CountdownParentWithItemData;
import com.example.myapplication.data.presets.countdown.CountdownPresetData;
import com.example.myapplication.data.presets.countdown.CountdownPresetPair;
import com.example.myapplication.data.presets.countdown.CountdownPresetWithParentData;
import com.example.myapplication.databinding.FragmentSettingsBinding;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment implements PresetEditBottomSheet.onCloseListener {
    FragmentSettingsBinding bi;
    RoomDB database;
    ArrayList<CountdownPreset> countdownPresets = new ArrayList<>();
    ArrayList<ChecklistPreset> checklistPreset = new ArrayList<>();

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = RoomDB.getInstance(getContext());
        createCountdownList();
        createChecklistList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // binding views to data binding
        bi = DataBindingUtil.bind(view);

        // on new checklist button
        bi.buttonAddChecklist.setOnClickListener(viewListener ->{
            PresetEditBottomSheet presetEditBottomSheet = new PresetEditBottomSheet();
            presetEditBottomSheet.setupView(checklistPreset, PresetEditBottomSheet.PRESET_TYPE_CHECKLIST, this);
            presetEditBottomSheet.show(getParentFragmentManager(), presetEditBottomSheet.getTag());

        });

        // on new countdown button
        bi.buttonAddCountdown.setOnClickListener(viewListener ->{
            createCountdownList();
            PresetEditBottomSheet presetEditBottomSheet = new PresetEditBottomSheet();
            presetEditBottomSheet.setupView(countdownPresets, PresetEditBottomSheet.PRESET_TYPE_COUNTDOWN, this);
            presetEditBottomSheet.show(getParentFragmentManager(), presetEditBottomSheet.getTag());
        });
    }

    private void createCountdownList(){
        // Load Countdowns Objects
        List<CountdownPresetPair> d = new ArrayList<>();
        d = database.countdownPresetWIthParentDao().getCountdowns();

        countdownPresets.clear();
        d.forEach(preset ->{
            String presetName = preset.getPresets().getTitle();
            Integer presetId = preset.getPresets().getID();

            countdownPresets.add(new CountdownPreset(presetName, convertToAdvancedCountdownList(database, preset), presetId));
        });
    }

    private void createChecklistList(){
        List<ChecklistPresetPair> d = new ArrayList<>();
        d = database.checklistPresetWithItemDao().getPresets();

        checklistPreset.clear();
        d.forEach(preset ->{
            String presetName = preset.getPresets().getTitle();
            Integer presetId = preset.getPresets().getID();

            checklistPreset.add(new ChecklistPreset(presetName, ChecklistPreset.convertToChecklistItems(database, preset), presetId ));
        });
    }


    @Override
    public void onClose() {
        createCountdownList();
        createChecklistList();
    }
}

/*



 */