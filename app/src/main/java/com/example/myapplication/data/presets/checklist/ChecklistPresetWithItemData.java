package com.example.myapplication.data.presets.checklist;

import androidx.room.Entity;

@Entity(primaryKeys = {"presetID", "itemID"},tableName = "table_checklist_preset_with_item")
public class ChecklistPresetWithItemData {
    private int presetID;
    private int itemID;

    public int getPresetID() {
        return presetID;
    }

    public void setPresetID(int presetID) {
        this.presetID = presetID;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }
}
