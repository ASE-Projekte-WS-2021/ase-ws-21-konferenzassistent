package com.ase.konferenzassistent.data.presets.checklist;

import static androidx.room.OnConflictStrategy.IGNORE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface ChecklistPresetWithItemDao {
    @Insert(onConflict = IGNORE)
    void insert(ChecklistPresetWithItemData join);

    @Transaction
    @Query("SELECT * FROM table_preset_checklist_data")
    List<ChecklistPresetPair> getPresets();

    @Transaction
    @Query("SELECT * FROM table_preset_checklist_data WHERE ID = :sPresetID")
    ChecklistPresetPair getSingularPreset(long sPresetID);

    @Transaction
    @Query("DELETE FROM table_checklist_preset_with_item WHERE presetID =:sPresetID")
    void deleteLinking(int sPresetID);
}