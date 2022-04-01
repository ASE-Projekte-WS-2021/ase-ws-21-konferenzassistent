package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PersonDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "persons.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "my_persons_db";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private Context context;

    public PersonDatabase(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addPerson(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, name);

        long res = db.insert(TABLE_NAME, null, cv);
        if (res == -1) {
            Toast.makeText(context, "FEHLER beim hinzufügen des Person", Toast.LENGTH_LONG).show();
        }
    }

    public List<String> getParticipants() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        List<String> participantList = new ArrayList<>();
        while (cursor.moveToNext()) {
            participantList.add(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)));
        }
        return participantList;
    }

    public void updateName(String id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, name);
        long res = db.update(TABLE_NAME, cv, "_id=", new String[]{id});
        if (res == -1) {
            Toast.makeText(context, "FEHLER beim bearbeiten der Person", Toast.LENGTH_LONG).show();
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
            Toast.makeText(context, "FEHLER beim vernichten der Person", Toast.LENGTH_LONG).show();
        }
    }
}
