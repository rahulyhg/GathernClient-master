package com.max.gathernclient.gathernclient;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;

import com.google.common.net.HttpHeaders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MyConnectionManager  {



    public static boolean isInternetConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }

        return networkInfo != null && networkInfo.isConnected();
    }

    public static HttpsURLConnection openConnection(String link) throws IOException {
        URL url = new URL(link);
        HttpsURLConnection httpURLConnection = (HttpsURLConnection) url.openConnection();
//        httpURLConnection.setRequestMethod(httpURLConnection.getRequestMethod());
//        httpURLConnection.setConnectTimeout(1000);
//        httpURLConnection.setReadTimeout(10000);
        httpURLConnection.setDoInput(true);
       // httpURLConnection.setDoOutput(true);

        httpURLConnection.setRequestProperty("content-type", "application/json");
        //httpURLConnection.setRequestProperty("charset","utf-8");
        return httpURLConnection ;
    }


    public static String getResult(HttpURLConnection httpURLConnection) throws IOException {
        InputStream inputStream = httpURLConnection.getInputStream();
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }


}