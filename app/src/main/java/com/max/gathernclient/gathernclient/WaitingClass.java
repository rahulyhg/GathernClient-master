package com.max.gathernclient.gathernclient;


import java.util.Calendar;

public class WaitingClass {
    private String image ;
    private String name;
    private String houseNumber;
    private String expireState;
    private String data;
    private String dayOfDate;
    private String unit_id ;
    private String reservation_id ;
    private int secondsAgo ;


    public WaitingClass (String image , String name,String houseNumber,
                         String expireState,String data , String dayOfDate,String unit_id , String reservation_id ,int secondsAgo){
        this.image = image ;
        this.name = name ;
        this.houseNumber = houseNumber ;
        this.expireState = expireState ;
        this.data = data ;
        this.dayOfDate = dayOfDate ;
        this.unit_id = unit_id ;
        this.reservation_id = reservation_id ;
        this.secondsAgo = secondsAgo ;
    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getExpireState() {
        return expireState;
    }

    public void setExpireState(String expireState) {
        this.expireState = expireState;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDayOfDate() {
        return dayOfDate;
    }

    public void setDayOfDate(String dayOfDate) {
        this.dayOfDate = dayOfDate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(String reservationId) {
        this.unit_id = reservationId;
    }

    public String getReservation_id() {
        return reservation_id;
    }

    public void setReservation_id(String reservation_id) {
        this.reservation_id = reservation_id;
    }


    public int getSecondsAgo() {
        return secondsAgo;
    }

    public void setSecondsAgo(int secondsAgo) {
        this.secondsAgo = secondsAgo;
    }
}