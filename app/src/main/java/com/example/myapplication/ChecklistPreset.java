package com.example.myapplication;

import com.example.myapplication.checklist.ChecklistItem;
import com.example.myapplication.data.RoomDB;
import com.example.myapplication.data.presets.checklist.ChecklistItemData;
import com.example.myapplication.data.presets.checklist.ChecklistPresetData;
import com.example.myapplication.data.presets.checklist.ChecklistPresetPair;
import com.example.myapplication.data.presets.checklist.ChecklistPresetWithItemData;
import com.example.myapplication.data.presets.countdown.CountdownItemData;
import com.example.myapplication.data.presets.countdown.CountdownItemPair;
import com.example.myapplication.data.presets.countdown.CountdownParentData;
import com.example.myapplication.data.presets.countdown.CountdownParentWithItemData;
import com.example.myapplication.data.presets.countdown.CountdownPresetData;
import com.example.myapplication.data.presets.countdown.CountdownPresetPair;
import com.example.myapplication.data.presets.countdown.CountdownPresetWithParentData;
import com.example.myapplication.meetingwizard.RecyclerViewAdvancedCountdownAdapter;
import com.example.myapplication.meetingwizard.RecyclerViewAdvancedCountdownItemAdapter;

import java.util.ArrayList;

public class ChecklistPreset implements Preset {
    String title;
    ArrayList<ChecklistItem> checklistItems;
    Integer id;

    public ChecklistPreset(String title, ArrayList<ChecklistItem> checklistItems, Integer id) {
        this.title = title;
        this.checklistItems = checklistItems;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<ChecklistItem> getChecklistItems() {
        return checklistItems;
    }

    public void setChecklistItems(ArrayList<ChecklistItem> checklistItems) {
        this.checklistItems = checklistItems;
    }

    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public static void removeChecklistFromDatabase(RoomDB database, Integer id){
        database.checklistPresetDao().delete(database.checklistPresetDao().getOne(id));
    }

    public static void convertToChecklistDatabaseEntry(RoomDB database, ChecklistPreset preset){
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
            presetWithItemData.setPresetID((int)presetId);
            presetWithItemData.setItemID((int)itemId);
            database.checklistPresetWithItemDao().insert(presetWithItemData);
        });
    }

    public static ArrayList<ChecklistItem> convertToChecklistItems(RoomDB database, ChecklistPresetPair checklistPresetPair){
        ArrayList<ChecklistItem> list = new ArrayList<>();
        checklistPresetPair.getItems().forEach(items ->{
            String itemName = items.getTitle();
            String hint = items.getHint();

            list.add(new ChecklistItem(itemName, hint));
        });
        return list;
    }

}
