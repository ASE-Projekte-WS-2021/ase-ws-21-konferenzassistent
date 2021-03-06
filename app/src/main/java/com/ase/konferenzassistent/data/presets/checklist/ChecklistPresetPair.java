package com.ase.konferenzassistent.data.presets.checklist;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.ase.konferenzassistent.shared.interfaces.PresetPair;

import java.util.List;

public class ChecklistPresetPair implements PresetPair {
    @Embedded
    ChecklistPresetData presets;
    @Relation(
            parentColumn = "ID",
            entity = ChecklistItemData.class,
            entityColumn = "ID",
            associateBy = @Junction(
                    value = ChecklistPresetWithItemData.class,
                    parentColumn = "presetID",
                    entityColumn = "itemID")
    )
    List<ChecklistItemData> items;

    public ChecklistPresetData getPresets() {
        return presets;
    }

    public void setPresets(ChecklistPresetData presets) {
        this.presets = presets;
    }

    public List<ChecklistItemData> getItems() {
        return items;
    }

    public void setItems(List<ChecklistItemData> items) {
        this.items = items;
    }
}