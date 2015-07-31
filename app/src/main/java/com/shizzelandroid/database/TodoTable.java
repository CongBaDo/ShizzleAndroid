package com.shizzelandroid.database;

/**
 * Created by congba on 7/30/15.
 */

import android.database.sqlite.SQLiteDatabase;

public class TodoTable {

    public static final String TABLE_NAME = "EbayInfo";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ITEM_ID = "Item_id";
    public static final String COLUMN_TITLE = "Title";
    public static final String COLUMN_THUMB_URL = "Thumb_URL";
    public static final String COLUMN_CURRENT_COST = "Current_Cost";
    public static final String COLUMN_SHIPPING_COST = "Shipping_Cost";
    // Database creation sql statement
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
            + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CURRENT_COST + " TEXT , "
            + COLUMN_ITEM_ID + " TEXT , "
            + COLUMN_THUMB_URL + " TEXT , "
            + COLUMN_TITLE + " TEXT , "
            + COLUMN_SHIPPING_COST + " TEXT ); ";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

    public static String[] projection = {
            TodoTable.COLUMN_ID,
            TodoTable.COLUMN_CURRENT_COST,
            TodoTable.COLUMN_ITEM_ID,
            TodoTable.COLUMN_THUMB_URL,
            TodoTable.COLUMN_TITLE,
            TodoTable.COLUMN_SHIPPING_COST
    };
}