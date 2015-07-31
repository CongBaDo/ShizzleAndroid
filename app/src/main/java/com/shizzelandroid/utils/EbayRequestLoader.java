package com.shizzelandroid.utils;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by congba on 7/30/15.
 */
public class EbayRequestLoader  extends AsyncTaskLoader<List<Listing>> {

    private static final String TAG = "EbayRequestLoader";

    private String keyValue;

    public EbayRequestLoader(Context context, String value) {
        super(context);
        this.keyValue = value;
        Log.e(TAG, "REPONSE "+keyValue);
    }

    @Override
    public List<Listing> loadInBackground() {

        JSONObject json = null;
        try{
            String response = AppUtils.search(this.keyValue);
            json = new JSONObject(response);
            //Log.e(TAG, "REPONSE " + response);
        }catch(Exception e){}

        EbayParser parser = new EbayParser();

        List<Listing> list = parser.parseData(json);

        return list;
    }

}
