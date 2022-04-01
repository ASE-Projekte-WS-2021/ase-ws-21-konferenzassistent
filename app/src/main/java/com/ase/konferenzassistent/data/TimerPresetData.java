package com.ase.konferenzassistent.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "table_timer_preset_data")
public class TimerPresetData {
    @PrimaryKey(autoGenerate = true)
    private int ID;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "duration")
    private int duration;

    @ColumnInfo(name = "description")
    private String description;

    // TODO:
}
