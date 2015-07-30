package com.shizzelandroid.database;

/**
 * Created by congba on 7/30/15.
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.shizzelandroid.items.ItemStore;

import java.util.ArrayList;
import java.util.List;

public class AppDataSource extends DataSource {
    public static final String TABLE_NAME = "EbayInfo";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ITEM_ID = "Item_id";
    public static final String COLUMN_TITLE = "Title";
    public static final String COLUMN_THUMB_URL = "Thumb_URL";
    public static final String COLUMN_CURRENT_COST = "Current_Cost";
    public static final String COLUMN_SHIPPING_COST = "Shipping_Cost";
    // Database creation sql statement
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
            + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ITEM_ID + "INTEGER, "
            + COLUMN_TITLE + " TEXT , "
            + COLUMN_THUMB_URL + " TEXT , "
            + COLUMN_CURRENT_COST + " TEXT , "
            + COLUMN_SHIPPING_COST + " TEXT ); ";

    public AppDataSource(SQLiteDatabase database) {
        super(database);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean insert(ItemStore entity) {
        if (entity == null) {
            return false;
        }
        long result = mDatabase.insert(TABLE_NAME, null,
                putValueFromObject(entity));
        return result != -1;
    }

    @Override
    public boolean delete(ItemStore entity) {
        if (entity == null) {
            return false;
        }
        int result = mDatabase.delete(TABLE_NAME,
                COLUMN_ITEM_ID + " = " + entity.getId(), null);
        return result != 0;
    }

    @Override
    public boolean update(ItemStore entity) {
        if (entity == null) {
            return false;
        }
        int result = mDatabase.update(TABLE_NAME,
                putValueFromObject(entity), COLUMN_ITEM_ID + " = "
                        + entity.getId(), null);
        return result != 0;
    }

    @Override
    public List read() {
        Cursor cursor = mDatabase.query(TABLE_NAME, getAllColumns(), null,
                null, null, null, null);
        List values = new ArrayList();
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                values.add(getObjectFromCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return values;
    }

    @Override
    public List read(String selection, String[] selectionArgs,
                     String groupBy, String having, String orderBy) {
        Cursor cursor = mDatabase.query(TABLE_NAME, getAllColumns(), selection, selectionArgs, groupBy, having, orderBy);
        List values = new ArrayList();
        if (cursor != null && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                values.add(getObjectFromCursor(cursor));
                cursor.moveToNext();
            }
            cursor.close();
        }
        return values;
    }

    public String[] getAllColumns() {
        return new String[]{COLUMN_ID, COLUMN_TITLE, COLUMN_ITEM_ID, COLUMN_SHIPPING_COST, COLUMN_CURRENT_COST, COLUMN_THUMB_URL};
    }

    public ItemStore getObjectFromCursor(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        ItemStore item = new ItemStore();
        item.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
        item.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
        item.setCurrentPrice(cursor.getString(cursor.getColumnIndex(COLUMN_CURRENT_COST)));
        item.setShippingCost(cursor.getString(cursor.getColumnIndex(COLUMN_SHIPPING_COST)));
        item.setThumbUrl(cursor.getString(cursor.getColumnIndex(COLUMN_THUMB_URL)));

        return item;
    }

    public ContentValues putValueFromObject(ItemStore entity) {
        if (entity == null) {
            return null;
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_ID, entity.getId());
        values.put(COLUMN_CURRENT_COST, entity.getCurrentPrice());
        values.put(COLUMN_SHIPPING_COST, entity.getShippingCost());
        values.put(COLUMN_THUMB_URL, entity.getThumbUrl());
        values.put(COLUMN_TITLE, entity.getTitle());
        return values;
    }
}