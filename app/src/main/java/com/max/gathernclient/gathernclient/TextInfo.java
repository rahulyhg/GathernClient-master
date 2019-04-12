package com.max.gathernclient.gathernclient;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class TextInfo extends AppCompatActivity  {
TextView titleText , bodyText;
String title = "";
ImageView loading ;
Globals g ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_info);
        titleText = findViewById(R.id.titleText);
        bodyText = findViewById(R.id.bodyText);
        loading = findViewById(R.id.loading);
        Animation animation = AnimationUtils.loadAnimation(this ,R.anim.rotation);
        animation.setInterpolator(new LinearInterpolator());
        loading.setAnimation(animation);
        g = new Globals(this);

        if (getIntent().getExtras() !=null) {
            try {
                title = getIntent().getExtras().get("titleText").toString();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
        titleText.setText(title);

        getHtm();


    }
    public void getHtm() {

        String linkTerms = "https://gathern.co/api/va/client/page/terms" ;
        String linkPrivacy = "https://gathern.co/api/va/client/page/privacy" ;
        String linkAbout = "https://gathern.co/api/va/client/page/about" ;

        if( MyConnectionManager.isInternetConnected(this))
        {
            HTTPTask httpTask = new HTTPTask(this);
            switch (title){
                case "الأسئلة المتكررة":
                    bodyText.setText(getData());
                break;
                case "سياسة الخصوصية" :
                    httpTask.execute(linkPrivacy);
                    break;
                case "شروط الإستخدام" :
                    httpTask.execute(linkTerms);
                    break;
                    default:
                        httpTask.execute(linkAbout);
            }


            httpTask.setDelegate(new HTTPTask.AsyncResponse() {
                @Override
                public void processFinish(String output) {
                    bodyText.setText(Html.fromHtml((output)));
                    removeImage();
                }
            });

        }else {
            Toast.makeText(getBaseContext() , "Check Internet",Toast.LENGTH_SHORT).show();
        }

    }
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageBack :
                finish();
                break;

        }
    }

public void removeImage (){
loading.setAnimation(null);
loading.setVisibility(View.GONE);
}

    public String getData (){
        final String[] result = {""};
        Runnable runnable=new Runnable() {
            @Override
            public void run() {

                try {
                    String url = g.baseUrl("api/va/client/faqs");
                    //connection
                    URL hellofileur1 = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) hellofileur1.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("content-type", "application/json");
                    connection.setConnectTimeout(10000);
                    connection.setReadTimeout(10000);
                    connection.setDoInput(true);
                  //  connection.setDoOutput(true);
                    // read
                    InputStreamReader stream = new InputStreamReader(connection.getInputStream());
                    BufferedReader ourstreamreader = new BufferedReader(stream);
                     result[0] = ourstreamreader.readLine();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String quiz ="" , answer = "";
                            StringBuilder stringBuilder = new StringBuilder();

                            try {
                                JSONObject object = new JSONObject(result[0]);
                                JSONArray itemArray = object.getJSONArray("items");

                                for (int i = 0 ; i<itemArray.length() ; i++){
                                    JSONObject dataObject = itemArray.getJSONObject(i);
                                    quiz =dataObject.getString("quiz");
                                    answer = dataObject.getString("answer");
                                    stringBuilder.append(quiz);
                                    stringBuilder.append(answer);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                              removeImage();
                            String response =  stringBuilder.toString();
                              //bodyText.setText(result[0]);
                            bodyText.setText(Html.fromHtml(response));

                            //  Toast.makeText(getBaseContext(), result[0], Toast.LENGTH_SHORT).show();

                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }};
        Thread thread= new Thread(runnable);
        thread.start();
return result[0];
    }
}
