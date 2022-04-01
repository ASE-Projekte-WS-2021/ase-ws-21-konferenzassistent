package com.example.myapplication.data;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ParticipantDao {

    @Insert(onConflict = REPLACE)
    long insert(ParticipantData participantData);

    @Delete
    void delete(ParticipantData participantData);

    @Delete
    void reset(List<ParticipantData> participantDataList);

    @Query("Update table_participant_data SET name = :sName," +
            "email = :sEmail, status = :sStatus")
    void update(String sName, String sEmail, String sStatus);

    @Query("SELECT * FROM table_participant_data")
    List<ParticipantData> getAll();

    @Query("SELECT ID FROM table_participant_data WHERE name = :sName")
    int getIDbyName(String sName);
}
