package com.max.gathernclient.gathernclient;


public class CalenderClass {
    private int day , monthIndex ;
    private boolean imageChecked;


    public CalenderClass (int day ,int monthIndex , boolean imageChecked){
        this.day = day ;
        this.monthIndex = monthIndex ;
        this.imageChecked = imageChecked ;
    }


    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public boolean isImageChecked() {
        return imageChecked;
    }

    public void setImageChecked(boolean imageChecked) {
        this.imageChecked = imageChecked;
    }

    public int getMonthIndex() {
        return monthIndex;
    }

    public void setMonthIndex(int monthIndex) {
        this.monthIndex = monthIndex;
    }
}