package com.max.gathernclient.gathernclient;


import java.util.Calendar;

public class BookingClass {
    private String image ;
    private String name;
    private String houseNumber;
    private String expireState;
    private String date;
    private String dayOfDate;
    private String unit_id ;
    private String reservation_id ;
    private int status ;
    private boolean isUnitPublished ;



    public BookingClass (String image , String name,String houseNumber,
                         String expireState,String date , String dayOfDate
            ,boolean isUnitPublished ,String unit_id , String reservation_id , int status ){
        this.image = image ;
        this.name = name ;
        this.houseNumber = houseNumber ;
        this.expireState = expireState ;
        this.date = date ;
        this.dayOfDate = dayOfDate ;
this.isUnitPublished = isUnitPublished ;
this.unit_id = unit_id ;
this.reservation_id = reservation_id ;
this.status = status ;
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
        return date;
    }

    public void setData(String date) {
        this.date = date;
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

    public boolean isUnitPublished() {
        return isUnitPublished;
    }

    public void setUnitPublished(boolean unitPublished) {
        isUnitPublished = unitPublished;
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

   public boolean isConfirm (){
        return status == 3 ;
   }
    public boolean isExpire (){
        return status == 4 ;
    }

   public boolean isLastCheckOut (){
        boolean result = false ;
        String year = date.substring(0 ,4);
      String month =  date.substring(5 ,7);
      String day = date.substring(8);
      int _year = Integer.parseInt(year);
      int _month = Integer.parseInt(month);
      int _day = Integer.parseInt(day);

       Calendar calendar = Calendar.getInstance();
       int currentYear = calendar.get(Calendar.YEAR);
     int currenMonth = calendar.get(Calendar.MONTH)+1;// jen = 0 after edit = 1
       int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
       if(currentYear - _year >0){
           result = true ;
       }else if (currentYear - _year == 0 && currenMonth - _month >0){
           result = true ;
       }else if (currenMonth - _month == 0 && currentDay - _day >0){
           result = true ;
       }


         return result;
   }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}