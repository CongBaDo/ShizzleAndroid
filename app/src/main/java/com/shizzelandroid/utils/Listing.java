
package com.shizzelandroid.utils;


import java.util.Date;

/**
 * Created by congba on 7/30/15.
 */

public class Listing implements Comparable<Listing> {
    private String id;
    private String title;
    private String imageUrl;
    private String listingUrl;
    private String location;
    private String shippingCost;
    private String currentPrice;
    private String auctionSource;
    private Date startTime;
    private Date endTime;
    private boolean auction;
    private boolean buyItNow;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {

        if (imageUrl == null) {
            this.imageUrl = "";
        } else {
            CharSequence target = "\\/";
            CharSequence replace = "/";
            String fixedUrl = imageUrl.replace(target, replace);
            this.imageUrl = fixedUrl;
        }
    }

    public String getListingUrl() {
        return listingUrl;
    }

    public void setListingUrl(String listingUrl) {
        CharSequence target = "\\/";
        CharSequence replace = "/";
        String fixedUrl = listingUrl.replace(target, replace);
        this.listingUrl = fixedUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getShippingCost() {
        return shippingCost;
    }

    public void setShippingCost(String shippingCost) {
        this.shippingCost = shippingCost;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public boolean isAuction() {
        return auction;
    }

    public void setAuction(boolean auction) {
        this.auction = auction;
    }

    public boolean isBuyItNow() {
        return buyItNow;
    }

    public void setBuyItNow(boolean buyItNow) {
        this.buyItNow = buyItNow;
    }

    public String getAuctionSource() {
        return auctionSource;
    }

    public void setAuctionSource(String auctionSource) {
        this.auctionSource = auctionSource;
    }

    @Override
    public int compareTo(Listing another) {
        return (another.startTime.compareTo(this.startTime));
    }
}