package com.example.myapplication.data;

import static androidx.room.OnConflictStrategy.IGNORE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
interface MeetingWithParticipantDao {

    @Insert(onConflict = IGNORE)
    void insert(MeetingWithParticipantData join);

    @Transaction
    @Query("SELECT * FROM table_meeting_data")
    List<MeetingParticipantPair> getMeeting();

}
