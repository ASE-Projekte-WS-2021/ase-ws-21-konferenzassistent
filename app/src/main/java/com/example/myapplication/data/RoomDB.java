package com.example.myapplication.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.myapplication.data.presets.checklist.ChecklistItemDao;
import com.example.myapplication.data.presets.checklist.ChecklistItemData;
import com.example.myapplication.data.presets.checklist.ChecklistPresetDao;
import com.example.myapplication.data.presets.checklist.ChecklistPresetWithItemDao;
import com.example.myapplication.data.presets.countdown.CountdownItemDao;
import com.example.myapplication.data.presets.countdown.CountdownItemData;
import com.example.myapplication.data.presets.countdown.CountdownParentDao;
import com.example.myapplication.data.presets.countdown.CountdownParentData;
import com.example.myapplication.data.presets.countdown.CountdownParentWIthItemDao;
import com.example.myapplication.data.presets.countdown.CountdownParentWithItemData;
import com.example.myapplication.data.presets.countdown.CountdownPresetDao;
import com.example.myapplication.data.presets.countdown.CountdownPresetData;
import com.example.myapplication.data.presets.countdown.CountdownPresetWIthParentDao;
import com.example.myapplication.data.presets.countdown.CountdownPresetWithParentData;

@Database(entities = {MeetingData.class, ParticipantData.class, CountdownPresetWithParentData.class, MeetingWithParticipantData.class, CountdownPresetData.class, CountdownItemData.class, CountdownParentData.class, CountdownParentWithItemData.class}, version = 2, exportSchema = false)
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

    public abstract CountdownItemDao countdownItemDao();
    public abstract CountdownParentWIthItemDao countdownParentWIthItemDao();
    public abstract CountdownParentDao countdownParentDao();
    public abstract CountdownPresetDao countdownPresetDao();
    public abstract CountdownPresetWIthParentDao countdownPresetWIthParentDao();

    public abstract ChecklistItemDao checklistItemDao();
    public abstract ChecklistPresetDao checklistPresetDao();
    public abstract ChecklistPresetWithItemDao checklistPresetWithItemDao();
}
