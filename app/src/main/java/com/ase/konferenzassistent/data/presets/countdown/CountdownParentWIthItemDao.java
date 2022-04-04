package com.ase.konferenzassistent.data.presets.countdown;

import static androidx.room.OnConflictStrategy.IGNORE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface CountdownParentWIthItemDao {

    @Insert(onConflict = IGNORE)
    void insert(CountdownParentWithItemData join);


    @Transaction
    @Query("SELECT * FROM table_parent_countdown")
    List<CountdownItemPair> getIemPair();

    @Transaction
    @Query("SELECT * FROM table_parent_countdown WHERE ID = :sParentID")
    CountdownItemPair getSingularCountdowns(long sParentID);

    @Transaction
    @Query("DELETE FROM table_countdown_join WHERE countdownParentID =:sCountdownParentID")
    void deleteLinking(int sCountdownParentID);

}
