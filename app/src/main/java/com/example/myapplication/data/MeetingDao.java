package com.example.myapplication.data;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MeetingDao {

    @Insert(onConflict = REPLACE)
    void insert(MeetingData meetingData);

    @Delete
    void delete(MeetingData meetingData);

    @Delete
    void reset(List<MeetingData> meetingDataList);

    @Query("Update table_meeting_data SET title = :sTitle," +
            "date = :sDate," +
            "duration = :sDuration," +
            "location = :sLocation," +
            "participants = :sParticipants")
    void update(String sTitle, String sDate, Integer sDuration, String sLocation, ParticipantData sParticipants);

    @Query("SELECT * FROM table_meeting_data")
    List<MeetingData> getAll();
}
