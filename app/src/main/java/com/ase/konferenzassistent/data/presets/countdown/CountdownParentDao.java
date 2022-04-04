package com.ase.konferenzassistent.data.presets.countdown;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CountdownParentDao {

    @Insert(onConflict = REPLACE)
    long insert(CountdownParentData parentData);

    @Delete
    void delete(CountdownParentData parentData);

    @Delete
    void reset(List<CountdownParentData> parentDataList);

    @Query("Update table_parent_countdown SET title = :sTitle WHERE ID = :sID")
    void update(String sTitle, Integer sID);

    @Query("SELECT * FROM table_parent_countdown")
    List<CountdownParentData> getAll();

    @Query("SELECT * FROM table_parent_countdown WHERE ID = :sID")
    CountdownParentData getOne(int sID);
}
