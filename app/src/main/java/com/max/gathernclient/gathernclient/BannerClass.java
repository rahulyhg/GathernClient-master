package com.max.gathernclient.gathernclient;

public class BannerClass {
    private int imageId = 0, imageWidth , imageHeight;
    private String imageUrl ;
    public BannerClass (String imageUrl , int imageWidth , int  imageHeight){
       this.imageUrl = imageUrl ;
       this.imageWidth = imageWidth ;
       this.imageHeight = imageHeight ;
    }
    public BannerClass (int imageId , int imageWidth , int  imageHeight){
        this.imageWidth = imageWidth ;
        this.imageHeight = imageHeight ;
        this.imageId = imageId ;
    }


    public int getImageWidth() {
        return imageWidth;
    }
    public int getImageHeight() {
        return imageHeight;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}