package com.max.gathernclient.gathernclient;


import android.text.SpannableString;

public class HouseClass {

    private int mImage ,mStartBanner  ;
    private String mHouseName ,mHouseDescription  ,mHouseArea
            ,singleTextOnImage;
SpannableString mHousePrice ;
    private float mHouseRating = 0;

    //constructor without ImageWidth and Height
    public HouseClass ( int Image  , String houseName , String houseDescription
            ,SpannableString housePrice , String houseArea , float houseRating ){
        this.mImage = Image ;
        this.mHouseName = houseName ;
        this.mHouseDescription = houseDescription ;
        this.mHousePrice = housePrice ;
        this.mHouseArea = houseArea ;
        this.mHouseRating = houseRating ;
    }
    public HouseClass (int startBanner , String singleTextOnImage ){
        this.mStartBanner = startBanner ;
        this.singleTextOnImage = singleTextOnImage ;
    }

    public String getmHouseName() {
        return mHouseName;
    }
    public String getmHouseDescription() {
        return mHouseDescription;
    }
    public SpannableString getmHousePrice() {
            return mHousePrice;
    }
    public String getmHouseArea() {
        return mHouseArea;
    }
    public float getmHouseRating() {
        return mHouseRating;
    }
    public int getmImage() {
        return mImage;
    }
    public boolean hasText (){
        return mHouseName != null ;
    }
    public String getSingleTextOnImage() {
        return singleTextOnImage;
    }

    public int getmStartBanner() {
        return mStartBanner;
    }
}