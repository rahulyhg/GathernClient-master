package com.max.gathernclient.gathernclient;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class Calender extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView viewMonth, fMonthText, fDayNumber, DayOfWeek;
    String[] month;
    String date = "", day = "", dateAndYear = "", dayOfWeek = "";
    Globals g;
    private static int JEN = 1;
    private static int FEB = 2;
    private static int MAR = 3;
    private static int APRL = 4;
    private static int MAY = 5;
    private static int JUN = 6;
    private static int JUL = 7;
    private static int AUG = 8;
    private static int SEP = 9;
    private static int OCT = 10;
    private static int NOV = 11;
    private static int DEC = 12;
    int index = -1;
    int currentMonth , currentDay;
    ArrayList<CalenderClass> monthList = new ArrayList<>();
    ArrayList<CalenderClass> jen, feb, mar, aprl, may, jun, jul, aug, sep, oct, nov, dec;
    CalenderAdapter adapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        setContentView(R.layout.activity_calender);
        recyclerView = findViewById(R.id.calenderRecyclerView);
        g = new Globals(this);

        viewMonth = findViewById(R.id.viewMonth);
        fMonthText = findViewById(R.id.fmonthText);
        fDayNumber = findViewById(R.id.fdaynumber);
        DayOfWeek = findViewById(R.id.fDayOfWeek);
        Calendar calendar = Calendar.getInstance();
        currentDay = calendar.get(Calendar.DAY_OF_MONTH); // start from day 1 = 1
        currentMonth = calendar.get(Calendar.MONTH); // start from jen = 0
        index = currentMonth ;
        month = new String[]{"يناير", "فبراير", "مارس", "إبريل", "مايو", "يونيو", "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر",};
        jen = getMonthList(1, 31 , 0);
        feb = getMonthList(4, 28 , 1);
        mar = getMonthList(4, 31 , 2);
        aprl = getMonthList(0, 30 , 3);
        may = getMonthList(2, 31 , 4);
        jun = getMonthList(5, 30 , 5);
        jul = getMonthList(0, 31 , 6);
        aug = getMonthList(3, 31 , 7);
        sep = getMonthList(6, 30 , 8);
        oct = getMonthList(1, 31 , 9);
        nov = getMonthList(4, 30 , 10);
        dec = getMonthList(6, 31 , 11);
        setMonthList(index);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 7, 1, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        updateAdapter();
        setTextViews();


    }

    @Override
    public void onBackPressed() {

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                finish();
                break;
            case R.id.next:
                if (index < 11) {
                    index++;
                    setMonthList(index);
                }
                break;
            case R.id.back:
                if (index > currentMonth) {
                    index--;
                    setMonthList(index);
                }
                break;
            case R.id.confirmBooking:
                String dayNumber = fDayNumber.getText().toString();
                String dayText = DayOfWeek.getText().toString();
                String m = fMonthText.getText().toString();
                String year = "2019";
                g.setBookingDate(dayNumber, dayText, m, year);
                // Toast.makeText(getBaseContext(),globals.getBookingDate("dayText"),Toast.LENGTH_SHORT).show();
                finish();
                break;


        }
    }

    private ArrayList<CalenderClass> getMonthList(int startDayAfter, int numberOfDays , int monthIndex) {

        final ArrayList<CalenderClass> calenderList = new ArrayList<>();
        for (int x = 0; x < startDayAfter; x++) {
            calenderList.add(new CalenderClass(0, monthIndex,false));
        }
        for (int i = 1; i <= numberOfDays; i++) {
            calenderList.add(new CalenderClass(i,monthIndex ,false));
        }

        return calenderList;
    }

    private void setMonthList(int index) {
        date = month[index];
        dateAndYear = date + " 2019 ";
        viewMonth.setText(dateAndYear);

        switch (index) {
            case 0:
                monthList = jen;
                break;
            case 1:
                monthList = feb ;
                break;
            case 2:
                monthList = mar ;
                break;
            case 3:
                monthList = aprl ;
                break;
            case 4:
                monthList = may ;
                break;
            case 5:
                monthList = jun ;
                break;
            case 6:
                monthList = jul ;
                break;
            case 7:
                monthList = aug ;
                break;
            case 8:
                monthList = sep ;
                break;
            case 9:
                monthList = oct ;
                break;
            case 10:
                monthList = nov ;
                break;
            case 11:
                monthList = dec ;
                break;
        }
        updateAdapter();
    }
    private void updateAdapter (){
        adapter = new CalenderAdapter(monthList, this);
        recyclerView.setAdapter(adapter);
        adapter.setmOnEntryClickListener((view, position) -> {

            CalenderClass Calender = monthList.get(position);
            day = "" +Calender.getDay();
            dayOfWeek = g.getDayOfWeek(position);
            g.setBookingDate(day,dayOfWeek,"","2019");
            if (Calender.getDay() !=0 ) {
                if(Calender.getMonthIndex() != currentMonth){
                    setTextViews ();
                }else if (Calender.getDay() >= currentDay){
                    setTextViews ();
                }
            }
                //
                Calender.setImageChecked(true);
                monthList.set(position, Calender);
                for (int i = 0; i < monthList.size(); i++) {
                    if (i != position) {
                        Calender = monthList.get(i);
                        Calender.setImageChecked(false);
                        monthList.set(i, Calender);
                    }
                }
                adapter.notifyDataSetChanged();


        });

    }
    public void setTextViews (){
        fDayNumber.setText(g.getBookingDate("dayNumber"));
        DayOfWeek.setText(g.getBookingDate("dayText"));
        fMonthText.setText(month[index]);
    }
}
