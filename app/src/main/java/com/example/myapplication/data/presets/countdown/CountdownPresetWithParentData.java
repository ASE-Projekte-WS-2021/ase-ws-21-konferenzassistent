package com.example.myapplication.data.presets.countdown;

import androidx.room.Entity;

@Entity(primaryKeys = {"presetID", "countdownParentID"}, tableName = "table_countdown_preset_with_parent")
public class CountdownPresetWithParentData {

    private int presetID;
    private int countdownParentID;

    public int getPresetID() {
        return presetID;
    }

    public void setPresetID(int presetID) {
        this.presetID = presetID;
    }

    public int getCountdownParentID() {
        return countdownParentID;
    }

    public void setCountdownParentID(int countdownParentID) {
        this.countdownParentID = countdownParentID;
    }
}
