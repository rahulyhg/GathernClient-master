package com.max.gathernclient.gathernclient;
// require to open
//    id = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("ID")).toString();
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
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

public class VerificationCode extends AppCompatActivity {
TextView Next , userMobile  ;
EditText VerCode ;
Globals g ;
ImageView loading ;
String id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verification_code);
        Next = findViewById(R.id.confirm);
        VerCode =findViewById(R.id.verCode);
        g = new Globals(this);
        g.InitializeMixPanel("User Login Attempt");
        getId();
        userMobile = findViewById(R.id.userMobile);
        String s = "+966 " + g.getUserData("mobile");
        userMobile.setText(s);
        Next.setBackgroundColor(0x339456A7);
        g.InitializeMixPanel("User Login");
        Bundle param = new Bundle();
        param.putString("mobile  " , g.getUserData("mobile"));
        param.putString("platform","Android Native");
        g.facebookPixel("User Login",param);
        VerCode.requestFocus();
        VerCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String phone = VerCode.getText().toString();
                if(phone.length() ==4){
                    Next.setEnabled(true);
                    Drawable drawable =getResources().getDrawable(R.drawable.shape_color_primary_radius5) ;
                    Next.setBackground(drawable);
                }else{
                    Next.setEnabled(false);
                    Next.setBackgroundColor(0x339456A7);
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
            case R.id.confirm :
                showLoadingImage();
                sendUserCode();
                break;

        }
    }
    public void sendUserCode (){
        String verifyCode = VerCode.getText().toString();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {

                try {
                    String url = g.baseUrl("api/va/client/auth/verify");
                    //connection
                    URL hellofileur1 = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) hellofileur1.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("content-type", "application/json");
                    connection.setConnectTimeout(10000);
                    connection.setReadTimeout(10000);
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    // Write
                    JSONObject params = new JSONObject();
                    params.put("mobile",g.getUserData("mobile"));
                    params.put("code",verifyCode);

                    OutputStream outputStream = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
                    writer.write(String.valueOf(params));
                    writer.close();
                    outputStream.close();

                    // read
                    InputStreamReader stream = new InputStreamReader(connection.getInputStream());
                    BufferedReader ourstreamreader = new BufferedReader(stream);
                    String result = ourstreamreader.readLine();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String checkVerfying = "";
                            String is_new = "";
                            try {
                                JSONObject object = new JSONObject(result);
                                checkVerfying = object.getString("success");
                                is_new = object.getString("is_new");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                           // Toast.makeText(getBaseContext(),"success = "+ checkVerfying, Toast.LENGTH_SHORT).show();
                           // Toast.makeText(getBaseContext(),"is_new = "+ is_new, Toast.LENGTH_SHORT).show();


                            if(checkVerfying.equals("true")){
                                if(is_new.equals("false")){
                                    String first_name = "",last_name = "", email = "" ,birth_date = "", access_token ="" ;
                                    try {
                                    JSONObject object = new JSONObject(result);
                                    JSONObject data =object.getJSONObject("data");
                                    JSONObject profile = data.getJSONObject("profile");
                                    // get userData from server
                                    first_name = profile.getString("firstname");
                                    last_name = profile.getString("lastname");
                                    email = data.getString("email");
                                    birth_date = data.getString("birth_date");
                                    access_token = data.getString("access_token");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                    // current user , let go app
                                    g.setSingedState(1);
                                    // set user data in sqlite dataBase
                                    g.setUserData("firstName",first_name);
                                    g.setUserData("lastName",last_name);
                                    g.setUserData("email",email);
                                    String day = birth_date.substring(8,10) ;
                                    String month = birth_date.substring(4,7) ;
                                    String year = birth_date.substring(0,4) ;
                                    g.setUserData("dayOfBirth",day);
                                    g.setUserData("monthOfBirth",month);
                                    g.setUserData("yearOfBirth",year);
                                    g.setUserData("access_token",access_token);
                                    Toast.makeText(getBaseContext(),"تم تسجيل الدخول بنجاح", Toast.LENGTH_SHORT).show();
                                    finish();
                                    if (id.length() > 0){
                                        Intent submitBooking = new Intent(getBaseContext(), SubmitBooking.class);
                                        submitBooking.putExtra("ID", id);
                                            startActivity(submitBooking);
                                    }else {
                                        startActivity(new Intent(getBaseContext(), HomePage.class));
                                    }
                                  //  startActivity(new Intent(getBaseContext(), HomePage.class));// should be mainActivity
                                    ////////
                                }else {
                                    // register user data
                                    // if access token provided put in dataBase
                                    hideLoadingImage();
                                    startActivity(new Intent(getBaseContext(), EditMyData.class));
                                }

                             //   startActivity(new Intent(getBaseContext(), VerificationCode.class));
                            }else {
                                // error msg
                                hideLoadingImage();
                                try {
                                    JSONObject object = new JSONObject(result);
                                   String msg = object.getString("msg");
                                    Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                // end error msg

                                // test pass with wrong code
                               // startActivity(new Intent(getBaseContext(), EditMyData.class));
                            }
                        }
                    });

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
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
    private void hideLoadingImage (){
        Next.setBackground(getResources().getDrawable(R.drawable.shape_color_primary_radius5));
        Next.setText("تاكيد");
        loading = findViewById(R.id.loading);
        loading.setAnimation(null);
        loading.setVisibility(View.GONE);
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

    private void hideKeyPad (){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(VerCode.getWindowToken() ,0);
    }
    private void getId() {
        id = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("ID")).toString();
    }
    private void openKeyPad (){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED ,InputMethodManager.HIDE_IMPLICIT_ONLY );
    }
}
