package com.example.myapplication.data.presets.countdown;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"countdownItemID", "countdownParentID"},tableName = "table_countdown_join")
public class CountdownParentWithItemData {

    private int countdownItemID;
    private int countdownParentID;

    public int getCountdownItemID() {
        return countdownItemID;
    }

    public void setCountdownItemID(int countdownItemID) {
        this.countdownItemID = countdownItemID;
    }

    public int getCountdownParentID() {
        return countdownParentID;
    }

    public void setCountdownParentID(int countdownParentID) {
        this.countdownParentID = countdownParentID;
    }
}
