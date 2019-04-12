package com.max.gathernclient.gathernclient;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.format.Formatter;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Toast;

import com.facebook.appevents.AppEventsLogger;
import com.google.gson.JsonObject;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;

public class Globals {
    private Context context;

    public Globals(Context context) {
        this.context = context;
    }

    public void insertUserLocation(double lat, double lon) {
        MySqliteDataBase dbhandler = new MySqliteDataBase(context);
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor = db.query("userLocation", null, "id=?", new String[]{String.valueOf(1)}, null, null, null);
        ContentValues r1 = new ContentValues();
        r1.put("lat", String.valueOf(lat));
        r1.put("lon", String.valueOf(lon));
        if (cursor.getCount() <= 0) {
            db.insert("userLocation", null, r1);
        } else {
            db.update("userLocation", r1, "id=?", new String[]{String.valueOf(1)});
        }
        cursor.close();
    }

    public String getLoc(String field) {
        double value = 0.0;
        MySqliteDataBase dbhandler = new MySqliteDataBase(context);
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor = db.query("userLocation", null, "id=?", new String[]{String.valueOf(1)}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            value = cursor.getDouble(cursor.getColumnIndex(field));
            cursor.close();
        }
        return String.valueOf(value);
    }

    public void setSingedState(int isSinged) {
        MySqliteDataBase dbhandler = new MySqliteDataBase(context);
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor = db.query("singedUser", null, "id=?", new String[]{String.valueOf(1)}, null, null, null);
        ContentValues r1 = new ContentValues();
        r1.put("signState", isSinged);
        if (cursor.getCount() <= 0) {
            db.insert("singedUser", null, r1);
        } else {
            db.update("singedUser", r1, "id=?", new String[]{String.valueOf(1)});
        }
        cursor.close();
    }

    public int getSingedState() {
        int value = 0;
        MySqliteDataBase dbhandler = new MySqliteDataBase(context);
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor = db.query("singedUser", null, "id=?", new String[]{String.valueOf(1)}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            value = cursor.getInt(cursor.getColumnIndex("signState"));
            cursor.close();
        }
        return value;

    }

    public String getDayOfWeek(int position) {
        if (position > 6 && position < 14) {
            position = position - 7;
        } else if (position > 13 && position < 21) {
            position = position - 14;
        } else if (position > 20 && position < 28) {
            position = position - 21;
        } else if (position > 27 && position < 35) {
            position = position - 28;
        }

        String dayOfWeek = "";
        switch (position) {
            case 0:
                dayOfWeek = "الإثنين";
                break;
            case 1:
                dayOfWeek = "الثلاثاء";
                break;
            case 2:
                dayOfWeek = "الأربعاء";
                break;
            case 3:
                dayOfWeek = "الخميس";
                break;
            case 4:
                dayOfWeek = "الجمعة";
                break;
            case 5:
                dayOfWeek = "السبت";
                break;
            case 6:
                dayOfWeek = "الأحد";
                break;


        }
        return dayOfWeek;
    }

    public void setBookingDate(String dayNumber, String dayText, String month, String year ) {
        MySqliteDataBase dbhandler = new MySqliteDataBase(context);
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor = db.query("calenderDate", null, "id=?", new String[]{String.valueOf(1)}, null, null, null);
        ContentValues r1 = new ContentValues();
        r1.put("dayNumber", dayNumber);
        r1.put("dayText", dayText);
        r1.put("month", month);
        r1.put("year", year);
        if (cursor.getCount() <= 0) {
            db.insert("calenderDate", null, r1);
        } else {
            db.update("calenderDate", r1, "id=?", new String[]{String.valueOf(1)});
        }
        cursor.close();
    }

    public String getBookingDate(String field) {
        String value = "";
        MySqliteDataBase dbhandler = new MySqliteDataBase(context);
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor = db.query("calenderDate", null, "id=?", new String[]{String.valueOf(1)}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            value = cursor.getString(cursor.getColumnIndex(field));
            cursor.close();
        }
        return value;
    }

    public String getCheckInDateForApi() {
        String day = "", month = "", year = "2019";
        day = getBookingDate("dayNumber");
        if(day.length()==1) day ="0"+day;
        month = getMonthNumber(getBookingDate("month"));
        return year + "-" + month + "-" + day;
    }

    public String getMonthNumber(String monthText) {
        String monthNumber = "";
        switch (monthText) {
            case "يناير":
                monthNumber = "01";
                break;
            case "فبراير":
                monthNumber = "02";
                break;
            case "مارس":
                monthNumber = "03";
                break;
            case "إبريل":
                monthNumber = "04";
                break;
            case "مايو":
                monthNumber = "05";
                break;
            case "يونيو":
                monthNumber = "06";
                break;
            case "يوليو":
                monthNumber = "07";
                break;
            case "أغسطس":
                monthNumber = "08";
                break;
            case "سبتمبر":
                monthNumber = "09";
                break;
            case "أكتوبر":
                monthNumber = "10";
                break;
            case "نوفمبر":
                monthNumber = "11";
                break;
            case "ديسمبر":
                monthNumber = "12";
                break;
        }
        return monthNumber;
    }

    public String finishedBookingDate() {

        String b = getBookingDate("dayNumber") + "";
        String c = getBookingDate("dayText") + ",";
        String d = getBookingDate("month") + ",";
        String e = getBookingDate("year") + "";
        return  c + b + d + e;
    }

    public void setAppData(String field, String value) {
        MySqliteDataBase dbhandler = new MySqliteDataBase(context);
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor = db.query("appData", null, "id=?", new String[]{String.valueOf(1)}, null, null, null);
        ContentValues r1 = new ContentValues();
        r1.put(field, value);
        if (cursor.getCount() <= 0) {
            db.insert("appData", null, r1);
        } else {
            db.update("appData", r1, "id=?", new String[]{String.valueOf(1)});
        }
        cursor.close();
    }

    public String getAppData(String field) {
        String value = "";
        MySqliteDataBase dbhandler = new MySqliteDataBase(context);
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor = db.query("appData", null, "id=?", new String[]{String.valueOf(1)}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            value = cursor.getString(cursor.getColumnIndex(field));
            cursor.close();
        }
        return value;
    }
    public SpannableString getPrice(String price) {
        String p = "" + price;
        String s = "السعر " + p + "ريال";
        SpannableString ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 6, 6 + p.length(), 0);
        ss1.setSpan(new StyleSpan(Typeface.BOLD), 6, 6 + p.length(), 0);
//        ss1.setSpan(new ForegroundColorSpan(Color.RED),4,6,0);
        return ss1;
    }

    public void firstTimeDataBase() {
        Calendar calendar = Calendar.getInstance();
       int currentDay = calendar.get(Calendar.DAY_OF_MONTH); // start from day 1 = 1
       int currentMonth = calendar.get(Calendar.MONTH); // start from jen = 0
       String[]  month = new String[]{"يناير", "فبراير", "مارس", "إبريل", "مايو", "يونيو", "يوليو", "أغسطس", "سبتمبر", "أكتوبر", "نوفمبر", "ديسمبر",};

        MySqliteDataBase dbhandler = new MySqliteDataBase(context);
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor1 = db.query("calenderDate", null, "id=?", new String[]{String.valueOf(1)}, null, null, null);

        ContentValues r1 = new ContentValues();
        r1.put("dayNumber", ""+currentDay);
        r1.put("dayText", "");
        r1.put("month",month[currentMonth]);
        r1.put("year", "2019");
        if (cursor1.getCount() <= 0) {
            db.insert("calenderDate", null, r1);
        }
        cursor1.close();
    }

    public String getHouseField(String id, String field) {
        String value = "";
        MySqliteDataBase dbhandler = new MySqliteDataBase(context);
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor = db.query("house", null, "id=?", new String[]{id}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            value = cursor.getString(cursor.getColumnIndex(field));
            cursor.close();
        }
        return value;
    }
/*
    public ArrayList<String> searchBetween2Values(String from, String to, String field) {
        ArrayList<String> idList = new ArrayList<>();
        int start = Integer.parseInt(from);
        int end = Integer.parseInt(to);
        int price;
        String value = "";
        String id = "";
        MySqliteDataBase dbhandler = new MySqliteDataBase(context);
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor = db.query("house", null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            value = cursor.getString(cursor.getColumnIndex(field));
            price = Integer.parseInt(value);
            if (price >= start && price <= end) {
                id = cursor.getString(cursor.getColumnIndex("id"));
                idList.add(id);
            }

        }
        cursor.close();
        return idList;
    }

    public ArrayList<String> searchLessThanValue(String maxValue, String field) {
        ArrayList<String> idList = new ArrayList<>();
        int maxVal = Integer.parseInt(maxValue);
        int realValue;
        String s = "";
        String id = "";
        MySqliteDataBase dbhandler = new MySqliteDataBase(context);
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor = db.query("house", null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            s = cursor.getString(cursor.getColumnIndex(field));
            realValue = Integer.parseInt(s);
            if (realValue <= maxVal) {
                id = cursor.getString(cursor.getColumnIndex("id"));
                idList.add(id);
            }

        }
        cursor.close();
        return idList;
    }

    public int getCountOfHouses() {
        int numOfUnits;
        MySqliteDataBase dbhandler = new MySqliteDataBase(context);
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor = db.query("house", null, null, null, null, null, null);
        numOfUnits = cursor.getCount();
        cursor.close();
        return numOfUnits;
    }
*/
    public int getScreenDpi(int pixels) {
        float scale = context.getResources().getDisplayMetrics().density;
        pixels = (int) (pixels * scale + 0.5f);
        return pixels;
    }

    public GradientDrawable shape(int colorId, int radius, int strokeWidth, int strokeColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(radius);
        if (colorId != 0) {
            shape.setColor(context.getResources().getColor(colorId));
        }
        if (strokeColor != 0) {
            shape.setStroke(getScreenDpi(strokeWidth), context.getResources().getColor(strokeColor));
        }
        return shape;
    }
    public GradientDrawable shapeStringColor(String color, int radius, int strokeWidth, String strokeColor) {
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(radius);
        if (!color.equals("")) {
            shape.setColor(Color.parseColor(color));
        }
        if (!strokeColor.equals("")) {
            shape.setStroke(getScreenDpi(strokeWidth), Color.parseColor(strokeColor));
        }
        return shape;
    }
    public GradientDrawable shapeColorString(String color, int radius) {
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(radius);
            shape.setColor(Color.parseColor(color));
        return shape;
    }

    public void setUserData(String field, String value) {

        MySqliteDataBase dbhandler = new MySqliteDataBase(context);
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor = db.query("userData", null, "id=?", new String[]{String.valueOf(1)}, null, null, null);
        ContentValues r1 = new ContentValues();
        r1.put(field, value);

        if (cursor.getCount() <= 0) {
            db.insert("userData", null, r1);
        } else {
            db.update("userData", r1, "id=?", new String[]{String.valueOf(1)});
        }
        cursor.close();
    }

    public String getUserData(String field) {
        String value = "";
        MySqliteDataBase dbhandler = new MySqliteDataBase(context);
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor = db.query("userData", null, "id=?", new String[]{String.valueOf(1)}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            value = cursor.getString(cursor.getColumnIndex(field));
            cursor.close();
        }
        return value;

    }

    public String baseUrl(String api) {
        return "https://staging.gathern.co/" + api;
    }

    public  String MIXPANEL_TOKEN() {
        String allData = getAppData("allData");
        String mixPanelToken = "";
        try {
            JSONObject ob = new JSONObject(allData);
         mixPanelToken = ob.optJSONObject("keys").optString("mixpanel");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mixPanelToken;
    }

    public String getNormalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        // Toast.makeText(getBaseContext(),ip,Toast.LENGTH_LONG).show();
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String getHash256Ip() {
        String ip = getNormalIpAddress();

        try {
            final MessageDigest digest = MessageDigest.getInstance("sha256");
            digest.update(ip.getBytes());
            final byte[] bytes = digest.digest();
            final StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(String.format("%02X", aByte));
            }
            return sb.toString().toLowerCase();
        } catch (Exception exc) {
            return ""; // Impossibru!
        }
    }

    public String getDeviceId() {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public void InitializeMixPanel(String eventNameForTrack) {
        MixpanelAPI mixpanel = MixpanelAPI.getInstance(context, MIXPANEL_TOKEN());
        String deviceId = getDeviceId();
        mixpanel.identify(deviceId);
        mixpanel.getPeople().identify(deviceId);
        JSONObject SuperProps = new JSONObject();

        try {
            SuperProps.put("Platform", "Native Android");
            SuperProps.put("Ip", getNormalIpAddress());
            if (getSingedState() == 1) {
                SuperProps.put("$first_name", getUserData("firstName"));
                SuperProps.put("$last_name", getUserData("lastName"));
                SuperProps.put("$email", getUserData("email"));
                SuperProps.put("Mobile", getUserData("mobile"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mixpanel.getPeople().set(SuperProps);
        mixpanel.track(eventNameForTrack);

    }

    public String connectToApi(String api, JSONObject params, String postOrGet) {

        String url = baseUrl(api);
        String access_token = getUserData("access_token");
        String result = "";
        try {
            //connection
            URL hellofileur1 = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) hellofileur1.openConnection();
            connection.setRequestMethod(postOrGet);
            connection.setRequestProperty("content-type", "application/json");
            if (getSingedState() == 1 )
                connection.setRequestProperty("Authorization", "Bearer " + access_token);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setDoInput(true);
            if (postOrGet.equals("POST"))
                connection.setDoOutput(true);
            // Write
            if (params != null) {
                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                writer.write(String.valueOf(params));
                writer.close();
                outputStream.close();
            }
            // read
            InputStreamReader stream = new InputStreamReader(connection.getInputStream());
            BufferedReader ourstreamreader = new BufferedReader(stream);
            result = ourstreamreader.readLine();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public String connectPayFortApiToken( JSONObject params , String api) {

        String result = "";
        try {
            //connection
            URL hellofileur1 = new URL(api);
            HttpURLConnection connection = (HttpURLConnection) hellofileur1.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("content-type", "application/json");
           // connection.setRequestProperty("content-type", "application/x-www-form-urlencoded; charset=utf-8");
           // connection.setRequestProperty("accept", "application/json");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            // Write
            if (params != null) {
                OutputStream outputStream = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                writer.write(String.valueOf(params));
                writer.close();
                outputStream.close();
            }
            // read
            InputStreamReader stream = new InputStreamReader(connection.getInputStream());
            BufferedReader ourstreamreader = new BufferedReader(stream);
            result = ourstreamreader.readLine();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public void facebookPixel(String eventName , Bundle param){
        AppEventsLogger logger = AppEventsLogger.newLogger(context);
        logger.logEvent(eventName , param);
    }
    public int getNumberFromString(String number) {
       if(number == null)
           number = "";
        String value = "";
        if (number.contains("."))
            value = number.substring(0, number.indexOf("."));
        else value = number;
        String val2 = value.replaceAll(",", "");
        if (val2.length() > 0)
            return Integer.parseInt(val2);
        else return 0;
    }
    public SpannableString setSARText(String price){
        SpannableString s1 = new SpannableString("SAR " + price);
        //s1.setSpan(new RelativeSizeSpan(1.5f), 6,10 ,0);
        s1.setSpan(new StyleSpan(Typeface.BOLD), 4, 4 + price.length(), 0);
        s1.setSpan(new ForegroundColorSpan(Color.BLACK), 4, 4 + price.length(), 0);
        return s1 ;
    }
    public  boolean isInternetConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }

        return networkInfo != null && networkInfo.isConnected();
    }
    public void setSavedFilterItems(ArrayList<String> arrayList) {
        MySqliteDataBase dbhandler = new MySqliteDataBase(context);
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor = db.query("savedFilterItems", null, "id=?", new String[]{String.valueOf(1)}, null, null, null);
        ContentValues r1 = new ContentValues();

        r1.put("arrayList", arrayList.toString());
        if (cursor.getCount() <= 0) {
            db.insert("savedFilterItems", null, r1);
        } else {
            db.update("savedFilterItems", r1, "id=?", new String[]{String.valueOf(1)});
        }
        cursor.close();
    }

    public String getSavedFilterItems() {
        String value = "";
        MySqliteDataBase dbhandler = new MySqliteDataBase(context);
        SQLiteDatabase db = dbhandler.getWritableDatabase();
        Cursor cursor = db.query("savedFilterItems", null, "id=?", new String[]{String.valueOf(1)}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToNext();
            value = cursor.getString(cursor.getColumnIndex("arrayList"));
            cursor.close();
        }

        return value;

    }
}