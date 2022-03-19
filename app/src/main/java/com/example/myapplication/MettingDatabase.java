package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class MettingDatabase extends SQLiteOpenHelper {

    private Context context;
    private static final String DB_NAME = "meetings.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "my_meetings_db";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_DATE = "meeting_date";
    private static final String COLUMN_DATE_END = "meeting_date_end";
    private static final String COLUMN_POSITION = "meeting_position";
    private static final String COLUMN_DURATION = "meeting_duration";
    private static final String COLUMN_TITLE = "meeting_title";
    private static final String COLUMN_NUMBER_OF_PARTICIPANTS = "meeting_number_participants";

    public MettingDatabase(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE "
                + TABLE_NAME  +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_DATE_END + " TEXT, " +
                COLUMN_POSITION + " TEXT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DURATION + " TEXT, " +
                COLUMN_NUMBER_OF_PARTICIPANTS + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addMeeting(String date, String dateEnd, String position, String title, String duration, String numberParticipants){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_DATE_END, dateEnd);
        cv.put(COLUMN_POSITION, position);
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_DURATION, duration);
        cv.put(COLUMN_NUMBER_OF_PARTICIPANTS, numberParticipants);

        long res = db.insert(TABLE_NAME, null, cv);
        if(res == -1){
            Toast.makeText(context, "FEHLER beim hinzufügen des Meetings", Toast.LENGTH_LONG).show();
        }
    }

    public Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    public Cursor findDataById(String id) {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + "=" + id;
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query,null);
        }
        return cursor;
    }

    public List<String> getLocations() {
        String query = "SELECT " + COLUMN_POSITION + " FROM " + TABLE_NAME;
        SQLiteDatabase db= this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query,null);
        }
        ArrayList<String> locations = new ArrayList<>();
        while (cursor.moveToNext()) {
            locations.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_POSITION)));
        }
        HashSet<String> uniqueLocationsSet = new HashSet<>(locations);
        List<String> uniqueLocationsList = new ArrayList<>(uniqueLocationsSet);
        Collections.sort(uniqueLocationsList);
        return uniqueLocationsList;
    }

    public void updateData(String id, String dateEnd, String position, String date, String title, String duration, String numberParticipants){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_DATE_END, dateEnd);
        cv.put(COLUMN_POSITION, position);
        cv.put(COLUMN_DURATION, duration);
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_NUMBER_OF_PARTICIPANTS, numberParticipants);
        long res = db.update(TABLE_NAME, cv, "_id=", new String[]{id});
        if(res == -1){
            Toast.makeText(context, "FEHLER beim bearbeiten des Meetings", Toast.LENGTH_LONG).show();
        }
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void deleteOne(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        long res = db.delete(TABLE_NAME, "_id=?", new String[]{id});
        if(res == -1){
            Toast.makeText(context, "FEHLER beim vernichten des Meetings", Toast.LENGTH_LONG).show();
        }
    }
}
