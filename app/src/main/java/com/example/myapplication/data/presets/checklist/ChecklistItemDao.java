package com.example.myapplication.data.presets.checklist;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChecklistItemDao {
    @Insert(onConflict = REPLACE)
    long insert(ChecklistItemData itemData);

    @Delete
    void delete(ChecklistItemData itemData);

    @Delete
    void reset(List<ChecklistItemData> itemDataList);


    @Query("Update table_preset_checklist_item SET title = :sTitle," +
            "hint = :sHint")
    void update(String sTitle, String sHint);

    @Query("SELECT * FROM table_preset_checklist_item")
    List<ChecklistItemData> getAll();
}
