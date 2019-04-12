package com.max.gathernclient.gathernclient;

import android.graphics.drawable.Drawable;

public class PartiesClass {
    private String partiesName , partiesDescription, partiesPrice  , partiesImageUrl;
    public PartiesClass (String partiesName , String partiesDescription , String partiesPrice , String partiesImageUrl){
        this.partiesName = partiesName ;
        this.partiesDescription = partiesDescription ;
        this.partiesPrice = partiesPrice ;
        this.partiesImageUrl = partiesImageUrl ;
    }
    public String getPartiesImageUrl() {
        return partiesImageUrl;
    }
    public String getPartiesName() {
        return partiesName;
    }
    public String getPartiesDescription() {
        return partiesDescription;
    }
    public String getPartiesPrice() {
        return partiesPrice;
    }

}