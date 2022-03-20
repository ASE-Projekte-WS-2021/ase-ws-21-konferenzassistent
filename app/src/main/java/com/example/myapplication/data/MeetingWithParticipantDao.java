package com.example.myapplication.data;

import static androidx.room.OnConflictStrategy.IGNORE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface MeetingWithParticipantDao {

    @Insert(onConflict = IGNORE)
    void insert(MeetingWithParticipantData join);

    @Transaction
    @Query("SELECT * FROM table_meeting_data")
    List<MeetingParticipantPair> getMeetings();

    @Query("SELECT meetingID FROM table_meeting_with_participant_data WHERE participantID = :sID")
    List<Integer> getMeetingIDsByParticipantID(String sID);

}
