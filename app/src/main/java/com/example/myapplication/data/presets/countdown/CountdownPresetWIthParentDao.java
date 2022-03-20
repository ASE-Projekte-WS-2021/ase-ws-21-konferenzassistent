package com.example.myapplication.data.presets.countdown;

import static androidx.room.OnConflictStrategy.IGNORE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface CountdownPresetWIthParentDao {

    @Insert(onConflict = IGNORE)
    void insert(CountdownPresetWithParentData join);

    @Transaction
    @Query("SELECT * FROM table_preset_countdown_data")
    List<CountdownPresetPair> getCountdowns();

}
