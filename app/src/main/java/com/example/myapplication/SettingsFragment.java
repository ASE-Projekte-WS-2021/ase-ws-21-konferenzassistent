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

import com.example.myapplication.data.RoomDB;
import com.example.myapplication.data.presets.countdown.CountdownItemData;
import com.example.myapplication.data.presets.countdown.CountdownParentData;
import com.example.myapplication.data.presets.countdown.CountdownParentWithItemData;
import com.example.myapplication.data.presets.countdown.CountdownPresetData;
import com.example.myapplication.data.presets.countdown.CountdownPresetPair;
import com.example.myapplication.data.presets.countdown.CountdownPresetWithParentData;
import com.example.myapplication.databinding.FragmentSettingsBinding;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment {
    FragmentSettingsBinding bi;
    RoomDB database;
    ArrayList<CountdownPreset> countdownPresets = new ArrayList<>();

    public SettingsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = RoomDB.getInstance(getContext());
        getDatabaseData();
        createCountdownList();
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

        // on new participant button
        bi.buttonAddContact.setOnClickListener(viewListener ->{
            ContactCreationBottomSheetAdapter contactCreationBottomSheetAdapter = new ContactCreationBottomSheetAdapter();
            contactCreationBottomSheetAdapter.show(getParentFragmentManager(), contactCreationBottomSheetAdapter.getTag());
        });

        // on new participant button
        bi.buttonAddChecklist.setOnClickListener(viewListener ->{
            PresetEditBottomSheet presetEditBottomSheet = new PresetEditBottomSheet();
            presetEditBottomSheet.show(getParentFragmentManager(), presetEditBottomSheet.getTag());
        });


        // on new participant button
        bi.buttonAddCountdown.setOnClickListener(viewListener ->{
            PresetEditBottomSheet presetEditBottomSheet = new PresetEditBottomSheet();
            presetEditBottomSheet.setupView(countdownPresets, PresetEditBottomSheet.PRESET_TYPE_COUNTDOWN);
            presetEditBottomSheet.show(getParentFragmentManager(), presetEditBottomSheet.getTag());
        });
    }

    private void getDatabaseData(){

    }

    private void createCountdownList(){

        // Load Countdowns Objects
        List<CountdownPresetPair> d = new ArrayList<>();
        d = database.countdownPresetWIthParentDao().getCountdowns();

        // Create Standard Countdown
        if(d.size() < 1){
            createStandard();
            d = database.countdownPresetWIthParentDao().getCountdowns();
        }

        d.forEach(preset ->{
            String presetName = preset.getPresets().getTitle();
            countdownPresets.add(new CountdownPreset(presetName, convertToAdvancedCountdownList(database, preset)));
        });
    }

    // TODO: simplify
    private void createStandard(){

        CountdownPresetData preset = new CountdownPresetData();
            preset.setTitle("Standard");

        CountdownParentData parentData1 = new CountdownParentData();
            parentData1.setTitle("Lüftungs Timer");

        CountdownParentData parentData2 = new CountdownParentData();
            parentData2.setTitle("AbstandsTimer");

        CountdownItemData countdownItemData1 = new CountdownItemData();
            countdownItemData1.setCountdown((long)15);
            countdownItemData1.setDescription("Fenster sollte geöffnet sein");

        CountdownItemData countdownItemData2 = new CountdownItemData();
            countdownItemData2.setCountdown((long)10);
            countdownItemData2.setDescription("Fenster sollte geschlossen sein");

        CountdownItemData countdownItemData3 = new CountdownItemData();
            countdownItemData3.setCountdown((long)30);

        long presetId = database.countdownPresetDao().insert(preset);

        long parent1Id = database.countdownParentDao().insert(parentData1);
        long parent2Id = database.countdownParentDao().insert(parentData2);

        long child1Id = database.countdownItemDao().insert(countdownItemData1);
        long child2Id = database.countdownItemDao().insert(countdownItemData2);
        long child3Id = database.countdownItemDao().insert(countdownItemData3);

        CountdownPresetWithParentData standardData1 = new CountdownPresetWithParentData();
            standardData1.setPresetID((int)presetId);
            standardData1.setCountdownParentID((int)parent1Id);

        CountdownPresetWithParentData standardData2 = new CountdownPresetWithParentData();
            standardData2.setPresetID((int)presetId);
            standardData2.setCountdownParentID((int)parent2Id);

        database.countdownPresetWIthParentDao().insert(standardData1);
        database.countdownPresetWIthParentDao().insert(standardData2);

        CountdownParentWithItemData countdownParentWithItemData1  = new CountdownParentWithItemData();
            countdownParentWithItemData1.setCountdownParentID((int)parent1Id);
            countdownParentWithItemData1.setCountdownItemID((int)child1Id);

        CountdownParentWithItemData countdownParentWithItemData2  = new CountdownParentWithItemData();
        countdownParentWithItemData2.setCountdownParentID((int)parent1Id);
        countdownParentWithItemData2.setCountdownItemID((int)child2Id);

        CountdownParentWithItemData countdownParentWithItemData3  = new CountdownParentWithItemData();
        countdownParentWithItemData3.setCountdownParentID((int)parent2Id);
        countdownParentWithItemData3.setCountdownItemID((int)child3Id);

        database.countdownParentWIthItemDao().insert(countdownParentWithItemData1);
        database.countdownParentWIthItemDao().insert(countdownParentWithItemData2);
        database.countdownParentWIthItemDao().insert(countdownParentWithItemData3);
    }
}

/*



 */