package com.ase.konferenzassistent.data.presets.checklist;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChecklistPresetDao {
    @Insert(onConflict = REPLACE)
    long insert(ChecklistPresetData presetData);

    @Delete
    void delete(ChecklistPresetData presetData);

    @Delete
    void reset(List<ChecklistPresetData> presetDataList);

    @Query("Update table_preset_checklist_data SET title = :sTitle WHERE ID = :sID")
    void update(String sTitle, Integer sID);

    @Query("SELECT * FROM table_preset_checklist_data")
    List<ChecklistPresetData> getAll();

    @Query("SELECT * FROM table_preset_checklist_data WHERE ID = :sID")
    ChecklistPresetData getOne(int sID);
}
