package com.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Akshay on 14-08-2017.
 */

public class EazyExitDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "EazyExitDatabase.db";

    public EazyExitDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        addNodeTable(db);
    }

    private void addNodeTable(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + EazyExitContract.NodeEntry.TABLE_NAME + " ( " +
                        EazyExitContract.NodeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        EazyExitContract.NodeEntry.COLUMN_NAME + " TEXT NOT NULL," +
                        EazyExitContract.NodeEntry.COLUMN_SSID + " TEXT NOT NULL," +
                        EazyExitContract.NodeEntry.COLUMN_STATE + " TEXT NOT NULL," +
                        EazyExitContract.NodeEntry.COLUMN_TYPE + " TEXT NOT NULL," +
                        EazyExitContract.NodeEntry.COLUMN_LEVEL + " TEXT NOT NULL," +
                        EazyExitContract.NodeEntry.COLUMN_LOCATION + " TEXT NOT NULL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
