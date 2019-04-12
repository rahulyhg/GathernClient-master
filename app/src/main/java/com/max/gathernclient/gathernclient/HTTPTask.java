package com.max.gathernclient.gathernclient;


import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.HttpURLConnection;

public class HTTPTask extends AsyncTask<String, Void, String> {

    private final Context context;
public AsyncResponse delegate= null ;
    public HTTPTask(Context context) {
        this.context = context;

    }


    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... strings) {
        String d = "no data";
        try {
            HttpURLConnection httpURLConnection = MyConnectionManager.openConnection(strings[0]);
            d = MyConnectionManager.getResult(httpURLConnection);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return d;
    }

    @Override
    protected void onPostExecute(String s) {
        Gson gson = new Gson();
        FixedPageApi fixedPageApi = gson.fromJson(s , FixedPageApi.class);
       // Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
        delegate.processFinish(fixedPageApi.getBody());

    }
    public  interface AsyncResponse{
        void processFinish(String output);
    }
    public void setDelegate(AsyncResponse asyncResponse){
        delegate=asyncResponse;
    }
}