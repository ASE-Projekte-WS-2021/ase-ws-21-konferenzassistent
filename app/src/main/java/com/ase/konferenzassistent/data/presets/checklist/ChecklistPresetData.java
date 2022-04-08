package com.ase.konferenzassistent.data.presets.checklist;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.ase.konferenzassistent.shared.interfaces.Preset;

import java.io.Serializable;

@Entity(tableName = "table_preset_checklist_data")
public class ChecklistPresetData implements Serializable, Preset {
    @PrimaryKey(autoGenerate = true)
    private int ID;

    @ColumnInfo(name = "title")
    private String title;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}