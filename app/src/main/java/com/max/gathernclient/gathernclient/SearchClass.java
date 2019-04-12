package com.max.gathernclient.gathernclient;


public class SearchClass {
    private String textItem ;
    private boolean textChecked;

    public SearchClass (String textItem , boolean textChecked ){
        this.textItem = textItem ;
        this.textChecked = textChecked ;
    }


    public String getTextItem() {
        return textItem;
    }

    public void setTextItem(String textItem) {
        this.textItem = textItem;
    }

    public boolean isTextChecked() {
        return textChecked;
    }

    public void setTextChecked(boolean textChecked) {
        this.textChecked = textChecked;
    }

}