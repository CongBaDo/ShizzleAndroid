package com.shizzelandroid.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by congba on 7/30/15.
 */

public class AppDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Nemo.db";
    private static final int DATABASE_VERSION = 1;
    public AppDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(AppDataSource.CREATE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + AppDataSource.TABLE_NAME);
        onCreate(db);
    }
}
