package com.shizzelandroid.database;

import android.database.sqlite.SQLiteDatabase;


import com.shizzelandroid.items.ItemStore;

import java.util.List;

/**
 * Created by congba on 7/30/15.
 */

public abstract class DataSource {
    protected SQLiteDatabase mDatabase;

    public DataSource(SQLiteDatabase database) {
        mDatabase = database;
    }


    public abstract boolean insert(ItemStore entity);


    public abstract boolean delete(ItemStore entity);


    public abstract boolean update(ItemStore entity);


    public abstract List read();


    public abstract List read(String selection, String[] selectionArgs, String groupBy, String having, String orderBy);
}

