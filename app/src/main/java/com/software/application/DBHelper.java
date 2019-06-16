package com.software.application;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import  android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "mainDb";
    public static final String TABLE_INTERVIEW = "interview";//Имя таблицы

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_NOTES = "notes";
    public static final String KEY_DATE = "date";
    public static final String KEY_DATA = "text";

    public static final String TABLE_INTERVIEWER_NAME = "interview_name";//Имя таблицы
    public static final String KEY_ID_NAME = "_id";
    public static final String KEY_NAMEINTERVIEWER = "interviewname";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_INTERVIEW + "(" + KEY_ID
                + " integer primary key," + KEY_NAME + " text," + KEY_NOTES + " text,"
                + KEY_DATE + " text," + KEY_DATA + " text" + ")");

        ContentValues contentValues = new ContentValues();
        db.execSQL("create table " + TABLE_INTERVIEWER_NAME + "(" + KEY_ID_NAME + " integer primary key," + KEY_NAMEINTERVIEWER + " text" + ")");
        contentValues.put(DBHelper.KEY_NAMEINTERVIEWER, "Я");
        db.insert(DBHelper.TABLE_INTERVIEWER_NAME,null,contentValues);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_INTERVIEW);
        onCreate(db);
    }
}
