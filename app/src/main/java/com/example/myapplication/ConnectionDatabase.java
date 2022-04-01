package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class ConnectionDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "connection.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "my_persons.db";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_MEETING = "meeting";
    private static final String COLUMN_PERSON = "person";
    private Context context;

    public ConnectionDatabase(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_MEETING + " TEXT, " + COLUMN_MEETING + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addConnection(String meeting, String person) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_MEETING, meeting);
        cv.put(COLUMN_PERSON, person);

        long res = db.insert(TABLE_NAME, null, cv);
        if (res == -1) {
            Toast.makeText(context, "FEHLER beim hinzufügen des connection", Toast.LENGTH_LONG).show();
        }
    }

    public Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public void updateName(String id, String meeting, String person) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_MEETING, meeting);
        cv.put(COLUMN_PERSON, person);
        long res = db.update(TABLE_NAME, cv, "_id=", new String[]{id});
        if (res == -1) {
            Toast.makeText(context, "FEHLER beim bearbeiten der connection", Toast.LENGTH_LONG).show();
        }
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void deleteOne(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long res = db.delete(TABLE_NAME, "_id=?", new String[]{id});
        if (res == -1) {
            Toast.makeText(context, "FEHLER beim vernichten der connection", Toast.LENGTH_LONG).show();
        }
    }
}
