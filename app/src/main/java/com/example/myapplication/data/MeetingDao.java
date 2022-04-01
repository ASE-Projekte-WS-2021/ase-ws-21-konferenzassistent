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
    long insert(MeetingData meetingData);

    @Delete
    void delete(MeetingData meetingData);

    @Delete
    void reset(List<MeetingData> meetingDataList);

    @Query("Update table_meeting_data SET title = :sTitle," +
            "start_date = :sStartDate," +
            "end_date = :sEndDate," +
            "duration = :sDuration," +
            "location = :sLocation")
    void update(String sTitle, String sStartDate, String sEndDate, Integer sDuration, String sLocation);

    @Query("SELECT DISTINCT location FROM table_meeting_data")
    List<String> getLocations();

    @Query("SELECT * FROM table_meeting_data")
    List<MeetingData> getAll();

    @Query("SELECT * FROM table_meeting_data WHERE ID = :sID")
    MeetingData getOne(int sID);
}
