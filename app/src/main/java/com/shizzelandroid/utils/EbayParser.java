
package com.shizzelandroid.utils;

import android.util.Log;

import com.shizzelandroid.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by congba on 7/30/15.
 */
public class EbayParser {
    private final static String TAG = "EbayParser";
    private static SimpleDateFormat dateFormat;
//	private Resources resources;

    public EbayParser() {
        synchronized (this) {
            if (dateFormat == null) {
                dateFormat = new SimpleDateFormat("[\"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'\"]");
            }
        }
//		this.resources=context.getResources();
    }

    public List<Listing> parseData(JSONObject jsonResponse) {
        List<Listing> listings = new ArrayList<Listing>();
        JSONArray itemList = null;
        try {

            itemList = jsonResponse
                    .getJSONArray(AppConstant.TAG_FIND_ITEM_BY_KEYWORDS)
                    .getJSONObject(0)
                    .getJSONArray(AppConstant.TAG_SEARCH_RESULT)
                    .getJSONObject(0)
                    .getJSONArray(AppConstant.TAG_ITEM);
        } catch (Exception e) {
        }
        if (itemList == null) {
            return listings;
        }
        int itemCount = itemList.length();


        for (int itemIndex = 0; itemIndex < itemCount; itemIndex++) {
            try {
                Listing item = this.parseListing(itemList.getJSONObject(itemIndex));
                Log.e(TAG, "parseListings: jsonResponse=" + item.getTitle());
                item.setAuctionSource(AppConstant.TAG_EBAY);
                listings.add(item);
            } catch (JSONException jx) {
                /* if something goes wrong log & move to the next item */
                Log.e(TAG, "parseListings: jsonResponse=" + jsonResponse, jx);
            }
        }
        return (listings);
    }

    private Listing parseListing(JSONObject jsonObj) throws JSONException {
		/*
		 * Things outside of a try/catch block are fields that are required and should throw an exception if not found
		 * Things inside of a try/catch block are fields we can live without
		 */
        Listing listing = new Listing();
		/* get items at the root of the object
		 * id, title, and URL are required
		 * image and location are optional */
        listing.setId(jsonObj.getString(AppConstant.TAG_ITEM_ID));
        listing.setTitle(this.stripWrapper(jsonObj.getString(AppConstant.TAG_TITLE)));
        listing.setListingUrl(this.stripWrapper(jsonObj.getString(AppConstant.TAG_ITEM_URL)));
        try {
            listing.setImageUrl(this.stripWrapper(jsonObj.getString(AppConstant.TAG_GALLERY)));
        } catch (JSONException jx) {
            Log.e(TAG, "parseListing: parsing image URL", jx);
            listing.setImageUrl(null);
        }
        try {
            listing.setLocation(this.stripWrapper(jsonObj.getString(AppConstant.TAG_LOCATION)));
        } catch (JSONException jx) {
            Log.e(TAG, "parseListing: parsing location", jx);
            listing.setLocation(null);
        }
        //get stuff under sellingStatus - required
        JSONObject sellingStatusObj = jsonObj.getJSONArray(AppConstant.TAG_SELLING_STATUS).getJSONObject(0);
        JSONObject currentPriceObj = sellingStatusObj.getJSONArray(AppConstant.TAG_CURRENT_PRICE).getJSONObject(0);
        listing.setCurrentPrice(this.formatCurrency(currentPriceObj.getString(AppConstant.TAG_VALUE), currentPriceObj.getString(AppConstant.TAG_CURRENCY_ID)));
        //get stuff under shippingInfo - optional
        try {
            JSONObject shippingInfoObj = jsonObj.getJSONArray(AppConstant.TAG_SHIPPING_INFO).getJSONObject(0);
            JSONObject shippingServiceCostObj = shippingInfoObj.getJSONArray(AppConstant.TAG_SHIPPING_SERVICE_COST).getJSONObject(0);
            listing.setShippingCost(this.formatCurrency(shippingServiceCostObj.getString(AppConstant.TAG_VALUE), currentPriceObj.getString(AppConstant.TAG_CURRENCY_ID)));
        } catch (JSONException jx) {
            Log.e(TAG, "parseListing: parsing shipping cost", jx);
            listing.setShippingCost("Not listed");
        }
        //get stuff under listingInfo
        try {
            JSONObject listingInfoObj = jsonObj.getJSONArray(AppConstant.TAG_LISTING_INFO).getJSONObject(0);
            //get start and end dates - optional
            try {
                Date startTime = dateFormat.parse(listingInfoObj.getString(AppConstant.TAG_START_TIME));
                listing.setStartTime(startTime);
                Date endTime = dateFormat.parse(listingInfoObj.getString(AppConstant.TAG_END_TIME));
                listing.setEndTime(endTime);
            } catch (Exception x) { //generic - both ParseException and JSONException can be thrown, same result either way
                Log.e(TAG, "parseListing: parsing start and end dates", x);
                listing.setStartTime(null);
                listing.setEndTime(null);
            }
        } catch (JSONException jx) {
            Log.e(TAG, "parseListing: parsing listing info", jx);
            listing.setStartTime(null);
            listing.setEndTime(null);
        }
        //alright, all done
        return (listing);
    }

    private String formatCurrency(String amount, String currencyCode) {
        StringBuffer formattedText = new StringBuffer(amount);
        try {
            //add trailing zeros
            int indexOf = formattedText.indexOf(".");
            if (indexOf >= 0) {
                if (formattedText.length() - indexOf == 2) {
                    formattedText.append("0");
                }
            }
            //add dollar sign
            if (currencyCode.equalsIgnoreCase("USD")) {
                formattedText.insert(0, "$");
            } else {
                formattedText.append(" ");
                formattedText.append(currencyCode);
            }
        } catch (Exception x) {
            Log.e(TAG, "formatCurrency", x);
        }
        return (formattedText.toString());
    }

    private String stripWrapper(String s) {
        try {
            int end = s.length() - 2;
            return (s.substring(2, end));
        } catch (Exception x) {
            Log.e(TAG, "stripWrapper", x);
            return (s);
        }
    }
}