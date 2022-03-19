package com.example.myapplication.data.presets.countdown;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CountdownPresetDao {

    @Insert(onConflict = REPLACE)
    long insert(CountdownPresetData presetData);

    @Delete
    void delete(CountdownPresetData presetData);

    @Delete
    void reset(List<CountdownPresetData> presetDataList);

    @Query("Update table_preset_countdown_data SET title = :sTitle")
    void update(String sTitle);

    @Query("SELECT * FROM table_preset_countdown_data")
    List<CountdownPresetData> getAll();

    @Query("SELECT * FROM table_preset_countdown_data WHERE ID = :sID")
    CountdownPresetData getOne(int sID);
}
