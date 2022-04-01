package com.ase.konferenzassistent.shared.presets;

import com.ase.konferenzassistent.shared.Interfaces.Preset;
import com.ase.konferenzassistent.data.RoomDB;
import com.ase.konferenzassistent.data.presets.countdown.CountdownItemData;
import com.ase.konferenzassistent.data.presets.countdown.CountdownItemPair;
import com.ase.konferenzassistent.data.presets.countdown.CountdownParentData;
import com.ase.konferenzassistent.data.presets.countdown.CountdownParentWithItemData;
import com.ase.konferenzassistent.data.presets.countdown.CountdownPresetData;
import com.ase.konferenzassistent.data.presets.countdown.CountdownPresetPair;
import com.ase.konferenzassistent.data.presets.countdown.CountdownPresetWithParentData;
import com.ase.konferenzassistent.meetingwizard.RecyclerViewAdvancedCountdownAdapter;
import com.ase.konferenzassistent.meetingwizard.RecyclerViewAdvancedCountdownItemAdapter;

import java.util.ArrayList;

public class CountdownPreset implements Preset {
    String title;
    ArrayList<RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject> advancedCountdownObject;
    int id;

    public CountdownPreset(String title, ArrayList<RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject> advancedCountdownObject, int id) {
        this.title = title;
        this.advancedCountdownObject = advancedCountdownObject;
        this.id = id;
    }

    public static void removeFromDatabase(RoomDB database, Integer id) {
        database.countdownPresetDao().delete(database.countdownPresetDao().getOne(id));
    }

    public static void convertToDatabaseEntry(RoomDB database, CountdownPreset preset) {
        String title = preset.getTitle();

        // create new preset
        CountdownPresetData presetData = new CountdownPresetData();
        presetData.setTitle(title);
        long presetId = database.countdownPresetDao().insert(presetData);

        // Write Countdown into database and link with preset
        preset.advancedCountdownObject.forEach(advancedCountdownObject -> {
            String countdownName = advancedCountdownObject.getmCountdownName();
            // create new countdown Parent
            CountdownParentData parentData = new CountdownParentData();
            parentData.setTitle(countdownName);
            long parentId = database.countdownParentDao().insert(parentData);

            // Link Parent to Preset
            CountdownPresetWithParentData presetWithParentData = new CountdownPresetWithParentData();
            presetWithParentData.setPresetID((int) presetId);
            presetWithParentData.setCountdownParentID((int) parentId);
            database.countdownPresetWIthParentDao().insert(presetWithParentData);

            // Write Timer into database and link with parent
            advancedCountdownObject.getmItems().forEach(items -> {
                String description = items.getSubCountdownDescription();
                Long timer = items.getSubCountdown();

                // new timer
                CountdownItemData countdownItemData = new CountdownItemData();
                countdownItemData.setCountdown(timer);
                countdownItemData.setDescription(description);
                long childId = database.countdownItemDao().insert(countdownItemData);

                // Link child to parent
                CountdownParentWithItemData countdownParentWithItemData = new CountdownParentWithItemData();
                countdownParentWithItemData.setCountdownParentID((int) parentId);
                countdownParentWithItemData.setCountdownItemID((int) childId);
                database.countdownParentWIthItemDao().insert(countdownParentWithItemData);

            });
        });
    }

    public static ArrayList<RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject> convertToAdvancedCountdownList(RoomDB database, CountdownPresetPair presetPair) {
        ArrayList<RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject> list = new ArrayList<>();
        presetPair.getParents().forEach(parents -> {
            String countdownName = parents.getTitle();

            ArrayList<RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem> children =
                    new ArrayList<>();

            RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject advancedCountdownObject =
                    new RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject(countdownName, true, children);

            CountdownItemPair childItems = database.countdownParentWIthItemDao().getSingularCountdowns(parents.getID());
            childItems.getCountdownItems().forEach(itemData -> {
                Long countdown = itemData.getCountdown();
                String countdownDescription = itemData.getDescription();

                RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem child =
                        new RecyclerViewAdvancedCountdownItemAdapter.AdvancedCountdownItem(
                                countdown,
                                countdownDescription);
                children.add(child);

            });


            list.add(advancedCountdownObject);
        });

        return list;
    }

    @Override
    public int getID() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject> getAdvancedCountdownObject() {
        return advancedCountdownObject;
    }

    public void setAdvancedCountdownObject(ArrayList<RecyclerViewAdvancedCountdownAdapter.AdvancedCountdownObject> advancedCountdownObject) {
        this.advancedCountdownObject = advancedCountdownObject;
    }

}
