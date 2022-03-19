package com.example.myapplication.data.presets.countdown;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CountdownItemDao {

    @Insert(onConflict = REPLACE)
    long insert(CountdownItemData itemData);

    @Delete
    void delete(CountdownItemData itemData);

    @Delete
    void reset(List<CountdownItemData> itemDataList);

    @Query("Update table_preset_countdown_item SET countdown = :sCountdown," +
            "description = :sDescription")
    void update(Long sCountdown, String sDescription);

    @Query("SELECT * FROM table_preset_countdown_item")
    List<CountdownItemData> getAll();
}


