package com.ase.konferenzassistent.data.presets.countdown;

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

    @Transaction
    @Query("DELETE FROM table_countdown_preset_with_parent WHERE presetID =:sPresetID")
    void deleteLinking(int sPresetID);

}
