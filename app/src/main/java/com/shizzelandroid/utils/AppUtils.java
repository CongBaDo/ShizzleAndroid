package com.shizzelandroid.utils;

import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by congba on 7/30/15.
 */
public class AppUtils {

    private static final String TAG = "AppUtils";

    private Resources resources;
//    private OkHttpClient httpClient;

    public static String search(String keyword) throws Exception {
        String jsonResponse = null;
        jsonResponse = requestData(keyword);
        if ((jsonResponse == null) || (jsonResponse.length() < 1)) {
            throw (new Exception("No result received from invokeEbayRest(" + keyword + ")"));
        }
        return (jsonResponse);
    }

    private static String getRequestURL(String keyword) {
        CharSequence requestURL = TextUtils.expandTemplate(AppConstant.EBAY_TEMPLATE, AppConstant.EBAY_API_PRODUCTION, AppConstant.APP_ID_PRODUCTION, keyword);

        Log.e(TAG, "getRequestURL" + requestURL.toString());
        return (requestURL.toString());
    }

    private static String requestData(String keyword) throws Exception {

        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(getRequestURL(keyword))
                .build();

        Response response = httpClient.newCall(request).execute();

        String result = response.body().string();

        Log.e(TAG, "requestData "+result);
        return result;
    }
}
