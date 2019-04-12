package com.max.gathernclient.gathernclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PrivacyPolicy extends AppCompatActivity {
Globals g ;
LinearLayout mainLayout ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_policy);
        g = new Globals(this);
        mainLayout = findViewById(R.id.mainLayout);
        getDataFromServer();
    }

    public void onClick(View view) {
        finish();
    }

    public void getDataFromServer (){
        showLoadingImage();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                String api = "api/va/client/page/privacy";
                String result = g.connectToApi(api, null, "GET");
                runOnUiThread(() -> {
                    hideLoadingImage();
                    String body ="" ;
                    try {
                        JSONObject object = new JSONObject(result);
                       body = object.optString("body");

                       setTextView(body);

                    } catch (JSONException e) {
                        e.printStackTrace();
                       // Toast.makeText(getBaseContext(), "e ="+ e, Toast.LENGTH_SHORT).show();

                    }
                    //  Toast.makeText(getBaseContext(), result[0], Toast.LENGTH_SHORT).show();

                });

            }};
        Thread thread= new Thread(runnable);
        thread.start();

    }
    private void showLoadingImage() {
        ImageView loadingImage = findViewById(R.id.loading);
        loadingImage.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotation);
        animation.setInterpolator(new LinearInterpolator());
        loadingImage.setAnimation(animation);
    }

    private void hideLoadingImage() {
        ImageView loadingImage = findViewById(R.id.loading);
        loadingImage.setAnimation(null);
        loadingImage.setVisibility(View.GONE);

    }
    private void setTextView(String quize ){
        LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textParam.setMargins(g.getScreenDpi(4),g.getScreenDpi(4) , g.getScreenDpi(4) ,g.getScreenDpi(4));

        MyTextView qu = new MyTextView(this);
        qu .setText(Html.fromHtml(quize));
        mainLayout.addView(qu);

    }
}
