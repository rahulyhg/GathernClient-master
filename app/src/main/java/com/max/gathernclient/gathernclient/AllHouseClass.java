package com.max.gathernclient.gathernclient;


public class AllHouseClass {

    private String ImageUrl, mHouseName ,mHouseDescription ,mHousePrice
            ,mHouseArea ,singleTextOnImage , distance_in_km , views_count;
    private int mHouseRating ,id ;
    private boolean isUnitAvailable ;
    //constructor without ImageWidth and Height
    public AllHouseClass ( String ImageUrl ,String singleTextOnImage , String houseName , String houseDescription
            ,String housePrice , String houseArea , int houseRating ,String distance_in_km ,String views_count
            , int id , boolean isUnitAvailable)
    {
        this.ImageUrl = ImageUrl ;
        this.mHouseName = houseName ;
        this.mHouseDescription = houseDescription ;
        this.mHousePrice = housePrice ;
        this.mHouseArea = houseArea ;
        this.mHouseRating = houseRating ;
        this.singleTextOnImage = singleTextOnImage ;
        this.distance_in_km = distance_in_km ;
        this.views_count = views_count ;
        this.id = id ;
        this.isUnitAvailable = isUnitAvailable ;
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

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getDistance_in_km() {
        String s ="";
        if (distance_in_km.contains(".")){
        s = distance_in_km.substring(0 ,distance_in_km.indexOf("."));
        }
        else
            s = distance_in_km ;
            return s ;
    }

    public void setDistance_in_km(String distance_in_km) {
        this.distance_in_km = distance_in_km;
    }

    public String getViews_count() {
        return views_count;
    }

    public void setViews_count(String views_count) {
        this.views_count = views_count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isUnitAvailable() {
        return isUnitAvailable;
    }

    public void setUnitAvailable(boolean unitAvailable) {
        isUnitAvailable = unitAvailable;
    }
}