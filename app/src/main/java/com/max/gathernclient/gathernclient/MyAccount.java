package com.max.gathernclient.gathernclient;
// require to open
//    id = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("ID")).toString();
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

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
import java.util.Objects;

import static java.net.Proxy.Type.HTTP;

public class MyAccount extends AppCompatActivity {
TextView Next , countryCode;
EditText UserPhoone ;
    TextView WarningText ;
    Globals g ;
    ImageView loading ;
    String id = "" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_account);
        g = new Globals(this);
        g.InitializeMixPanel("User Login");
        getId();
        Bundle param = new Bundle();
        param.putString("mobile  " , g.getUserData("mobile"));
        param.putString("platform","Android Native");
        g.facebookPixel("User Login Attempt",param);
        if(g.getSingedState() ==1){
            startActivity(new Intent(getBaseContext() , MyBooking.class));
        }
        Next = findViewById(R.id.next);
        UserPhoone = findViewById(R.id.userPhone);
        WarningText = findViewById(R.id.warningText);
        countryCode = findViewById(R.id.countryCode);
        GradientDrawable leftRadius = new GradientDrawable();
        float[] leftradii ={5,5,0,0,0,0,5,5};
        float[] rightRadii = {0,0,5,5,5,5,0,0};
        leftRadius.setCornerRadii(leftradii);
        leftRadius.setColor(Color.parseColor("#D6D0D0"));
        countryCode.setBackground(leftRadius);
        GradientDrawable rightRadius = new GradientDrawable();
        rightRadius.setCornerRadii(rightRadii);
        rightRadius.setColor(getResources().getColor(R.color.mywhite));
        UserPhoone.setBackground(rightRadius);
        Next.setBackground(g.shape(R.color.transprimary,5,0,0));
        UserPhoone.requestLayout();
        UserPhoone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String phone = UserPhoone.getText().toString();
                if(phone.length()>0){
                    Next.setEnabled(true);
                    WarningText.setVisibility(View.GONE);
                    Next.setBackground(g.shape(R.color.colorPrimary ,5,0,0));
                }else{

                    Next.setEnabled(false);
                    WarningText.setVisibility(View.VISIBLE);
                    Next.setBackground(g.shape(R.color.transprimary,5,0,0));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

openKeyPad();

    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageBack :
                finish();
                break;
            case R.id.next :
                showLoadingImage();
                sendUserPhone ();
                break;

        }
    }

    @Override
    public void onBackPressed() {

    }
    public void sendUserPhone (){

        String phone = UserPhoone.getText().toString();

        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                JSONObject params = new JSONObject();
                try {
                    params.put("mobile",phone);
                    params.put("device", Build.MODEL);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String result = g.connectToApi("api/va/client/auth",params , "POST");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String response ="";
                            try {
                                JSONObject object = new JSONObject(result);
                                response = object.getString("success");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            //Toast.makeText(getBaseContext(),response, Toast.LENGTH_SHORT).show();
                           if(response.equals("true")){
                               g.setUserData("mobile", phone);
                               g.setUserData("deviceMODEL", Build.MODEL);
                               hideLoadingImage();
                               Intent open = new Intent(getBaseContext(), VerificationCode.class);
                               open.putExtra("ID" ,id);
                               startActivity(open);
                               finish();
                           }else {
                               hideLoadingImage();
                               Toast.makeText(getBaseContext(),response, Toast.LENGTH_SHORT).show();

                           }
                        }
                    });


            }};
        Thread thread= new Thread(runnable);
        thread.start();
    }

    private void showLoadingImage (){
        loading = findViewById(R.id.loading);
        Next.setBackground(getResources().getDrawable(R.drawable.shape_color_transprimary_radius5));
        Next.setText("");
        loading.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this ,R.anim.rotation);
        animation.setInterpolator(new LinearInterpolator());
        loading.setAnimation(animation);
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideKeyPad();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideKeyPad();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        openKeyPad();
    }
    private void hideLoadingImage (){
        Next.setBackground(getResources().getDrawable(R.drawable.shape_color_primary_radius5));
        Next.setText("تاكيد");
        loading = findViewById(R.id.loading);
        loading.setAnimation(null);
        loading.setVisibility(View.GONE);
    }
    private void getId() {
        id = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("ID")).toString();
    }
    private void openKeyPad (){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED ,InputMethodManager.HIDE_IMPLICIT_ONLY );
    }
    private void hideKeyPad (){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(UserPhoone.getWindowToken() ,0);
    }
}
