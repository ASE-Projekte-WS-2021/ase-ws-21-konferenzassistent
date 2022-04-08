package com.ase.konferenzassistent.shared.presets;

import com.ase.konferenzassistent.shared.interfaces.Preset;
import com.ase.konferenzassistent.checklist.ChecklistItem;
import com.ase.konferenzassistent.data.RoomDB;
import com.ase.konferenzassistent.data.presets.checklist.ChecklistItemData;
import com.ase.konferenzassistent.data.presets.checklist.ChecklistPresetData;
import com.ase.konferenzassistent.data.presets.checklist.ChecklistPresetPair;
import com.ase.konferenzassistent.data.presets.checklist.ChecklistPresetWithItemData;

import java.util.ArrayList;
import java.util.List;

public class ChecklistPreset implements Preset {
    String title;
    final ArrayList<ChecklistItem> checklistItems;
    Integer id;

    public ChecklistPreset(String title, ArrayList<ChecklistItem> checklistItems, Integer id) {
        this.title = title;
        this.checklistItems = checklistItems;
        this.id = id;
    }

    public static void removeChecklistFromDatabase(RoomDB database, Integer id) {
        database.checklistPresetDao().delete(database.checklistPresetDao().getOne(id));
    }

    public static long convertToChecklistDatabaseEntry(RoomDB database, ChecklistPreset preset) {
        String title = preset.getTitle();

        // create new preset
        ChecklistPresetData presetData = new ChecklistPresetData();
        presetData.setTitle(title);
        long presetId = database.checklistPresetDao().insert(presetData);

        // Write checklist into database and link with preset
        preset.checklistItems.forEach(items -> {
            String itemName = items.getTitle();
            String hint = items.getHint();
            ChecklistItemData item = new ChecklistItemData();
            item.setTitle(itemName);
            item.setHint(hint);
            long itemId = database.checklistItemDao().insert(item);

            // Link Items to Preset
            ChecklistPresetWithItemData presetWithItemData = new ChecklistPresetWithItemData();
            presetWithItemData.setPresetID((int) presetId);
            presetWithItemData.setItemID((int) itemId);
            database.checklistPresetWithItemDao().insert(presetWithItemData);
        });

        return presetId;
    }

    public static void updateChecklistDatabaseEntry(RoomDB database, ChecklistPreset preset){
        String title = preset.getTitle();
        int presetId = preset.getID();

        // Update preset
        database.checklistPresetDao().update(title, presetId);

        // delete old checklist items
        List<ChecklistItemData> data = database.checklistPresetWithItemDao().getSingularPreset(presetId).getItems();
        if(data != null)
        data.forEach(checklistItemData -> database.checklistItemDao().delete(checklistItemData));

        // delete the linking entries
        database.checklistPresetWithItemDao().deleteLinking(presetId);

        // Update checklist into database
        preset.checklistItems.forEach(items ->{
            String itemName = items.getTitle();
            String hint = items.getHint();

            ChecklistItemData item = new ChecklistItemData();
            item.setTitle(itemName);
            item.setHint(hint);
            long itemId = database.checklistItemDao().insert(item);

            // Link Items to Preset
            ChecklistPresetWithItemData presetWithItemData = new ChecklistPresetWithItemData();
            presetWithItemData.setPresetID(presetId);
            presetWithItemData.setItemID((int) itemId);
            database.checklistPresetWithItemDao().insert(presetWithItemData);
        });
    }

    public static ArrayList<ChecklistItem> convertToChecklistItems(ChecklistPresetPair checklistPresetPair) {
        ArrayList<ChecklistItem> list = new ArrayList<>();
        checklistPresetPair.getItems().forEach(items -> {
            String itemName = items.getTitle();
            String hint = items.getHint();

            list.add(new ChecklistItem(itemName, hint));
        });
        return list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getID() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ArrayList<ChecklistItem> getChecklistItems() {
        return checklistItems;
    }
}
