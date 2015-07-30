package com.shizzelandroid.utils;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

/**
 * Created by congba on 7/30/15.
 */


public class AppRequestTask extends AsyncTask<Void, Void, JSONObject> {

    private static final String TAG = "AppRequestTask";
    private String keyValue;

    public interface RequestCallback {
        public void onResultPost(JSONObject statusObj);
        public void fail();
    }

    private RequestCallback callBack;

    public AppRequestTask(String keyValue, RequestCallback callBack){
        this.keyValue = keyValue;
        this.callBack = callBack;

        Log.e(TAG, "AppRequestTask " + this.keyValue);
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        // TODO Auto-generated method stub

        JSONObject json = null;
        try{
            String response = AppUtils.search(this.keyValue);
            json = new JSONObject(response);
            //Log.e(TAG, "REPONSE " + response);
        }catch(Exception e){}


        return json;
    }

    @Override
    protected void onPostExecute(JSONObject statusObj) {
        if(statusObj == null){
            callBack.fail();
        }else{
            callBack.onResultPost(statusObj);
        }
    }

    public void onCancelled(){
        this.cancel(true);
    }
}

