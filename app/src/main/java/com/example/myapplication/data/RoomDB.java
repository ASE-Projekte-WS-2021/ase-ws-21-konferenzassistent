package com.example.myapplication.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {MeetingData.class, ParticipantData.class, MeetingWithParticipantData.class}, version = 1, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    private static RoomDB database;
    private static String DATABASE_NAME = "database";

    public synchronized static RoomDB getInstance(Context context){
        if(database == null){
            database = Room.databaseBuilder(context.getApplicationContext(),
                    RoomDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public abstract MeetingDao meetingDao();
    public abstract ParticipantDao participantDao();
    public abstract MeetingWithParticipantDao meetingWithParticipantDao();

}
