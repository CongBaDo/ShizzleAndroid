package com.shizzelandroid.utils;

import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;

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

    public static String search(String keyword) throws Exception{
        String jsonResponse = null;
        jsonResponse = requestData(keyword);
        if((jsonResponse == null)||(jsonResponse.length() < 1)){
            throw(new Exception("No result received from invokeEbayRest("+keyword+")"));
        }
        return(jsonResponse);
    }

    private static String getRequestURL(String keyword){
        CharSequence requestURL = TextUtils.expandTemplate(AppConstant.EBAY_TEMPLATE, AppConstant.EBAY_API_PRODUCTION, AppConstant.APP_ID_PRODUCTION, keyword);

        Log.e(TAG, "getRequestURL"+requestURL.toString());
        return(requestURL.toString());
    }

    private static String requestData(String keyword) throws Exception{
        String result=null;
        HttpClient httpClient=new DefaultHttpClient();
        HttpGet httpGet=new HttpGet(getRequestURL(keyword));
        HttpResponse response=httpClient.execute(httpGet);
        HttpEntity httpEntity=response.getEntity();
        if(httpEntity!=null){
            InputStream in=httpEntity.getContent();
            BufferedReader reader=new BufferedReader(new InputStreamReader(in));
            StringBuffer temp=new StringBuffer();
            String currentLine=null;
            while((currentLine=reader.readLine())!=null){
                temp.append(currentLine);
            }
            result=temp.toString();
            in.close();
        }
        return(result);
    }
}
