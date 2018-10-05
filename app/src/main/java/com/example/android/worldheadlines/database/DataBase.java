package com.example.android.worldheadlines.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "headlines.db";

    private  static final int VERSION = 1;

    public DataBase(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_HEADLINES_TABLE = "CREATE TABLE " + Contract.HeadlinesEntry.TABLE_NAME + " (" +
                Contract.HeadlinesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                Contract.HeadlinesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                Contract.HeadlinesEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                Contract.HeadlinesEntry.COLUMN_URL + " TEXT NOT NULL, " +
                Contract.HeadlinesEntry.COLUMN_IMAGE + " TEXT NOT NULL, " +
                Contract.HeadlinesEntry.COLUMN_SOURCE + " TEXT NOT NULL, " +
                Contract.HeadlinesEntry.COLUMN_DATE + " TEXT NOT NULL)";

        db.execSQL(SQL_CREATE_HEADLINES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contract.HeadlinesEntry.TABLE_NAME);
        onCreate(db);
    }
}
