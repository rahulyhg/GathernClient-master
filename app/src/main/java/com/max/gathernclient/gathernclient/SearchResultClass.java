package com.max.gathernclient.gathernclient;


import java.util.ArrayList;

public class SearchResultClass {

    private String ImageUrl, mHouseName ,mHouseDescription ,mHousePrice
            ,city , area ,singleTextOnImage , distance_in_km , views_count , poolType , general_persons_capacity , text;
    private int mHouseRating ,id ;
   private double points ;
    private boolean isUnitAvailable ;
   private ArrayList<String> boxGalleryUrl ;
    //constructor without ImageWidth and Height
    public SearchResultClass ( String ImageUrl ,String singleTextOnImage , String houseName , String houseDescription
            ,String housePrice , String city , String area , int houseRating ,String distance_in_km ,String views_count
            , int id , boolean isUnitAvailable , String poolType ,String general_persons_capacity
            ,String text , double points ,ArrayList<String> boxGalleryUrl )
    {
        this.ImageUrl = ImageUrl ;
        this.mHouseName = houseName ;
        this.mHouseDescription = houseDescription ;
        this.mHousePrice = housePrice ;
        this.city = city ;
        this.area = area ;
        this.mHouseRating = houseRating ;
        this.singleTextOnImage = singleTextOnImage ;
        this.distance_in_km = distance_in_km ;
        this.views_count = views_count ;
        this.id = id ;
        this.isUnitAvailable = isUnitAvailable ;
        this.poolType = poolType ;
        this.general_persons_capacity = general_persons_capacity ;
        this.text = text ;
        this.points = points ;
        this.boxGalleryUrl = boxGalleryUrl ;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPoolType() {
        return poolType;
    }

    public void setPoolType(String poolType) {
        this.poolType = poolType;
    }

    public String getGeneral_persons_capacity() {

        return  "يسع ل" + general_persons_capacity + "شخص";
    }

    public void setGeneral_persons_capacity(String general_persons_capacity) {
        this.general_persons_capacity = general_persons_capacity;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public ArrayList<String> getBoxGalleryUrl() {
        return boxGalleryUrl;
    }

    public void setBoxGalleryUrl(ArrayList<String> boxGalleryUrl) {
        this.boxGalleryUrl = boxGalleryUrl;
    }
}