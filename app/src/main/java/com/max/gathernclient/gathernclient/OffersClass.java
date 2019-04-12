package com.max.gathernclient.gathernclient;


public class OffersClass {


    private String mHouseName ,mHouseDescription ,mHousePrice ,mHouseArea ,singleTextOnImage , mImageUrl , views_count , oldPrice ,id;
    private int mHouseRating = 0;
    private boolean isUnitAvailable ;
    //constructor without ImageWidth and Height
    public OffersClass ( String singleTextOnImage , String houseName , String houseDescription,String housePrice , String houseArea , int houseRating
            ,String mImageUrl , String views_count ,String oldPrice , boolean isUnitAvailable ,String id){
        this.mImageUrl = mImageUrl ;
        this.mHouseName = houseName ;
        this.mHouseDescription = houseDescription ;
        this.mHousePrice = housePrice ;
        this.mHouseArea = houseArea ;
        this.mHouseRating = houseRating ;
        this.singleTextOnImage = singleTextOnImage ;
        this.views_count = views_count ;
        this.oldPrice = oldPrice ;
        this.isUnitAvailable = isUnitAvailable ;
        this.id = id ;
    }

    public String getmHouseName() {
        return mHouseName;
    }

    public String getmHouseDescription() {
        return mHouseDescription;
    }

    public String getmHousePrice() {
        return mHousePrice;
    }

    public String getmHouseArea() {
        return mHouseArea;
    }

    public int getmHouseRating() {
        return mHouseRating;
    }


    public String getSingleTextOnImage() {
        return singleTextOnImage;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public String getViews_count() {
        return views_count;
    }

    public void setViews_count(String views_count) {
        this.views_count = views_count;
    }

    public String getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(String oldPrice) {
        this.oldPrice = oldPrice;
    }

    public boolean isUnitAvailable() {
        return isUnitAvailable;
    }

    public void setUnitAvailable(boolean unitAvailable) {
        isUnitAvailable = unitAvailable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}