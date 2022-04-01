package com.ase.konferenzassistent.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ase.konferenzassistent.data.presets.checklist.ChecklistItemDao;
import com.ase.konferenzassistent.data.presets.checklist.ChecklistItemData;
import com.ase.konferenzassistent.data.presets.checklist.ChecklistPresetDao;
import com.ase.konferenzassistent.data.presets.checklist.ChecklistPresetData;
import com.ase.konferenzassistent.data.presets.checklist.ChecklistPresetWithItemDao;
import com.ase.konferenzassistent.data.presets.checklist.ChecklistPresetWithItemData;
import com.ase.konferenzassistent.data.presets.countdown.CountdownItemDao;
import com.ase.konferenzassistent.data.presets.countdown.CountdownItemData;
import com.ase.konferenzassistent.data.presets.countdown.CountdownParentDao;
import com.ase.konferenzassistent.data.presets.countdown.CountdownParentData;
import com.ase.konferenzassistent.data.presets.countdown.CountdownParentWIthItemDao;
import com.ase.konferenzassistent.data.presets.countdown.CountdownParentWithItemData;
import com.ase.konferenzassistent.data.presets.countdown.CountdownPresetDao;
import com.ase.konferenzassistent.data.presets.countdown.CountdownPresetData;
import com.ase.konferenzassistent.data.presets.countdown.CountdownPresetWIthParentDao;
import com.ase.konferenzassistent.data.presets.countdown.CountdownPresetWithParentData;

@Database(entities = {ChecklistPresetData.class, ChecklistItemData.class, ChecklistPresetWithItemData.class, MeetingData.class, ParticipantData.class, CountdownPresetWithParentData.class, MeetingWithParticipantData.class, CountdownPresetData.class, CountdownItemData.class, CountdownParentData.class, CountdownParentWithItemData.class}, version = 3, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    private static final String DATABASE_NAME = "database";
    private static RoomDB database;

    public synchronized static RoomDB getInstance(Context context) {
        if (database == null) {
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
