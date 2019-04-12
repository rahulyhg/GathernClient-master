package com.max.gathernclient.gathernclient;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class EditMyData extends AppCompatActivity implements TextWatcher {
EditText FirstName , FamilyName ,Email  ;
TextView Note_1 , Note_2 , Save , DateOfBirth ;
Globals g ;
MixpanelAPI mixpanel ;
ImageView loading ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_my_data);
        g = new Globals(this);
        FirstName = findViewById(R.id.firstName);
        FamilyName = findViewById(R.id.familyName);
        Email = findViewById(R.id.email);
        DateOfBirth = findViewById(R.id.dateOfBirth);
        Note_1 = findViewById(R.id.note_1);
        Note_2 = findViewById(R.id.note_2);
        Save = findViewById(R.id.save);
        FirstName.addTextChangedListener(this);
        FamilyName.addTextChangedListener(this);
        updateUserDataFromSqlite();

    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageBack :
                finish();
                break;
            case R.id.save :
                if(g.getSingedState() ==1){
                    updateUserData();

                }else {
                    registerNewUser();
                }

                break;
            case R.id.dateOfBirth :
                CustomDialog customDialog = new CustomDialog(this);
                customDialog.show();
                break;
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(FirstName.getText().toString().length()== 0){
            Note_1.setVisibility(View.VISIBLE);
            Save.setBackground(getResources().getDrawable(R.drawable.shape_color_transprimary_radius5));
        }else{
            Note_1.setVisibility(View.GONE);
            if(FamilyName.getText().toString().length()!= 0) {
                Save.setBackground(getResources().getDrawable(R.drawable.shape_color_primary_radius5));
            }
        }
        if(FamilyName.getText().toString().length()== 0){
            Note_2.setVisibility(View.VISIBLE);
            Save.setBackground(getResources().getDrawable(R.drawable.shape_color_transprimary_radius5));
        }else{
            Note_2.setVisibility(View.GONE);
            if(FirstName.getText().toString().length()!= 0) {
                Save.setBackground(getResources().getDrawable(R.drawable.shape_color_primary_radius5));
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void registerNewUser (){

        JSONObject param = new JSONObject();
        try {
            param.put("source", Build.MODEL);
            param.put("mobile",g.getUserData("mobile"));
            param.put("email",Email.getText().toString());
            param.put("first_name",FirstName.getText().toString());
            param.put("last_name",FamilyName.getText().toString());
            param.put("year", g.getUserData("yearOfBirth"));
            param.put("day", g.getUserData("monthOfBirth"));
            param.put("month", g.getUserData("dayOfBirth"));
            param.put("lat", g.getLoc("lat"));
            param.put("lng", g.getLoc("lon"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                String api =g.baseUrl("api/va/client/auth/complete");
                String result = g.connectToApi(api ,param ,"POST");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // Toast.makeText(getBaseContext(),"result = "+ result, Toast.LENGTH_SHORT).show();

                        String response ="" , msg="";
                        try {
                            JSONObject object = new JSONObject(result);
                            response = object.getString("success");
                          //  Toast.makeText(getBaseContext(),"success = "+ response, Toast.LENGTH_SHORT).show();
                            if(response.equals("true")){
                                JSONObject data =object.getJSONObject("data");
                                String access_token = data.getString("access_token");
                                String birth_date = data.optString("birth_date");
                                String day = birth_date.substring(8,9) ;
                                String month = birth_date.substring(5,6) ;
                                String year = birth_date.substring(0,3) ;
                                g.setSingedState(1);
                                // set user data in sqlite dataBase
                                g.setUserData("firstName",FirstName.getText().toString());
                                g.setUserData("lastName",FamilyName.getText().toString());
                                g.setUserData("email",Email.getText().toString());
                                g.setUserData("dayOfBirth",day);
                                g.setUserData("monthOfBirth",month);
                                g.setUserData("yearOfBirth",year);                                 g.setUserData("access_token",access_token);
                                g.InitializeMixPanel("User Registration");
                                Toast.makeText(getBaseContext(),"تم تسجيل البيانات بنجاح ", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getBaseContext(), More.class));


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }};
        Thread thread= new Thread(runnable);
        thread.start();



    }

    private void updateUserData (){
        showLoadingImage();
        Runnable runnableUpdate= () -> {
            JSONObject params = new JSONObject();
            try {
                params.put("first_name", FirstName.getText().toString());
                params.put("last_name", FamilyName.getText().toString());
                params.put("city", "1");
                params.put("email", Email.getText().toString());
                params.put("year", g.getUserData("yearOfBirth"));
                params.put("day", g.getUserData("monthOfBirth"));
                params.put("month", g.getUserData("dayOfBirth"));

            } catch (JSONException e) {
                e.printStackTrace();
            }
             String api =g.baseUrl("api/va/client/profile/update");
             String result =   g.connectToApi(api , params , "POST");
             runOnUiThread(() -> {
                 hideLoadingImage();
                 try {
                     JSONObject object = new JSONObject(result);
                     JSONObject user = object.optJSONObject("user");
                     String msg = object.optString("message");
                     String success = object.optString("success");
                     String access_token = user.optString("access_token");
                     String birth_date = user.optString("birth_date");
                     String day = birth_date.substring(8,9) ;
                     String month = birth_date.substring(5,6) ;
                     String year = birth_date.substring(0,3) ;
                     Toast.makeText(getBaseContext(),msg, Toast.LENGTH_SHORT).show();
                     if(success.equals("true")){
                         g.setUserData("firstName",FirstName.getText().toString());
                         g.setUserData("lastName",FamilyName.getText().toString());
                         g.setUserData("email",Email.getText().toString());
                         g.setUserData("dayOfBirth",day);
                         g.setUserData("monthOfBirth",month);
                         g.setUserData("yearOfBirth",year);
                         g.setUserData("access_token",access_token);
                         hideLoadingImage();
                         startActivity(new Intent(getBaseContext(), More.class));

                     }

                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
             });


        };
        Thread threadUpdate= new Thread(runnableUpdate);
        threadUpdate.start();
    }


    private void updateUserDataFromSqlite (){
      FirstName.setText( g.getUserData("firstName"));
       FamilyName.setText(g.getUserData("lastName"));
       Email.setText(g.getUserData("email"));
       String s = g.getUserData("yearOfBirth") +"/"+g.getUserData("monthOfBirth") +"/"+g.getUserData("dayOfBirth") ;
        DateOfBirth.setText(s);
    }
    private void showLoadingImage (){
        Save.setBackground(getResources().getDrawable(R.drawable.shape_color_transprimary_radius5));
        Save.setText("");
        loading = findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this ,R.anim.rotation);
        animation.setInterpolator(new LinearInterpolator());
        loading.setAnimation(animation);
    }
    private void hideLoadingImage (){
        Save.setBackground(getResources().getDrawable(R.drawable.shape_color_primary_radius5));
        Save.setText("تاكيد");
        loading = findViewById(R.id.loading);
        loading.setAnimation(null);
        loading.setVisibility(View.GONE);
    }

    private class CustomDialog extends Dialog implements View.OnClickListener {
LinearLayout dayLinearLayout , monthLinearLayout ,yearLinearLayout ;
MyScrollView   dayScrollView,monthScrollView ,yearScrollView ;
TextView day , month , year , done , cancel;
int DaysInCurrentMonth = 31 ;
boolean isDay = false ;

        public CustomDialog(Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_birth_date);
            Objects.requireNonNull(getWindow()).setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams wlp = getWindow().getAttributes();
            wlp.gravity = Gravity.BOTTOM;
            getWindow().setAttributes(wlp);
            initRefrance();
            initDayLinearLayout();
            initMonthLinearLayout();
            initYearLinearLayout();
            done.setOnClickListener(this);
            cancel.setOnClickListener(this);
         int x =    dayLinearLayout.getChildCount();
         //    Toast.makeText(getBaseContext(),"ChildCount = " +x, Toast.LENGTH_SHORT).show();

        }
private void initRefrance (){
    dayLinearLayout = findViewById(R.id.dayLinearLayout);
    monthLinearLayout = findViewById(R.id.monthLinearLayout);
    yearLinearLayout = findViewById(R.id.yearLinearLayout);
    dayScrollView = findViewById(R.id.dayScrollView);
    monthScrollView = findViewById(R.id.monthScrollView);
    yearScrollView = findViewById(R.id.yearScrollView);
day = findViewById(R.id.day);
month = findViewById(R.id.month);
year = findViewById(R.id.year);
    done = findViewById(R.id.done);
    cancel = findViewById(R.id.cancel);


}

        private void showScrollPotionForDays (){
            TextView visiblrText = (TextView) dayLinearLayout.getChildAt(0);
            day.setText(visiblrText.getText().toString());

            dayScrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {

                int scroll = dayScrollView.getScrollY();
                int index = 0 ;
                int heightDb = g.getScreenDpi(80);
                int scrollDb = g.getScreenDpi(scroll);
                for (int i = 0 ; i<scrollDb ;i++){
                    if(scrollDb >= heightDb){
                        scrollDb = scrollDb - heightDb ;
                        index++ ;
                    }
                }
                TextView visiblrText1 = (TextView) dayLinearLayout.getChildAt(index);
                if (visiblrText1 != null)
                    day.setText(visiblrText1.getText().toString());
            });
        }
        private void showScrollPotion (MyScrollView scrollView , TextView textView , LinearLayout linearLayout ){

            TextView visiblrText = (TextView) linearLayout.getChildAt(0);
            textView.setText(visiblrText.getText().toString());

       scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {

           int scroll = scrollView.getScrollY();
           int index = 0 ;
           int heightDb = g.getScreenDpi(80);
           int scrollDb = g.getScreenDpi(scroll);
           for (int i = 0 ; i<scrollDb ;i++){
               if(scrollDb >= heightDb){
                   scrollDb = scrollDb - heightDb ;
                   index++ ;
               }
           }
          TextView visiblrText1 = (TextView) linearLayout.getChildAt(index);
           if (visiblrText1 != null)
           textView.setText(visiblrText1.getText().toString());

               DaysInCurrentMonth = setLast3Days(month.getText().toString(), year.getText().toString());
               addRemoveLast3Days ();

       });
}
private int setLast3Days (String month , String year ){
    int numberOfDays = 31 ;
int years = Integer.parseInt(year);
int result = years /4 ;
int x = years - (result * 4);

    switch (month){
        case "02" :
            if (x != 0)
                numberOfDays = 28 ;
            else
                numberOfDays = 29;
            break;
        case "04" :
            numberOfDays = 30 ;
            break;
        case "06" :
            numberOfDays = 30 ;
            break;
        case "09" :
            numberOfDays = 30 ;
            break;
        case "11" :
            numberOfDays = 30 ;
            break;
    }
    return numberOfDays ;
}

        private void initDayLinearLayout (){
    LinearLayout.LayoutParams textViewParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, g.getScreenDpi(40));

      for (int i = 1; i <= 28; i++) {
        TextView textView = new TextView(getContext());
        if(i<10)
        textView.setText("0"+String.valueOf(i));
        else
            textView.setText(String.valueOf(i));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP ,20f);
        textView.setTextColor(getResources().getColor(R.color.myblack));
        textView.setGravity(Gravity.END|Gravity.CENTER_VERTICAL);
        textView.setLayoutParams(textViewParam);
        dayLinearLayout.addView(textView);
             }
            showScrollPotionForDays();
        }
        private void addRemoveLast3Days (){
            LinearLayout.LayoutParams textViewParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, g.getScreenDpi(40));
            while (dayLinearLayout.getChildCount() < DaysInCurrentMonth) {
                    TextView textView = new TextView(getContext());
                    textView.setText(String.valueOf(dayLinearLayout.getChildCount() + 1));
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
                    textView.setTextColor(getResources().getColor(R.color.myblack));
                    textView.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
                    textView.setLayoutParams(textViewParam);
                    dayLinearLayout.addView(textView);

            }
            while (dayLinearLayout.getChildCount() > DaysInCurrentMonth){
                dayLinearLayout.removeViewAt(dayLinearLayout.getChildCount() - 1);
            }
            showScrollPotionForDays();
        }
        private void initMonthLinearLayout (){
            LinearLayout.LayoutParams textViewParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, g.getScreenDpi(40));
           // textViewParam.setMargins(g.getScreenDpi(2), g.getScreenDpi(2), g.getScreenDpi(2), g.getScreenDpi(2));

            for (int i = 1; i <= 12; i++) {
                TextView textView = new TextView(getContext());
                if(i<10)
                    textView.setText("0"+String.valueOf(i));
                else
                    textView.setText(String.valueOf(i));
                textView.setTextSize(20);
                textView.setTextColor(getResources().getColor(R.color.myblack));
                textView.setGravity(Gravity.CENTER);
                textView.setLayoutParams(textViewParam);
                monthLinearLayout.addView(textView);
            }
            showScrollPotion(monthScrollView ,month , monthLinearLayout );

        }
        private void initYearLinearLayout (){
            LinearLayout.LayoutParams textViewParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, g.getScreenDpi(40));
           // textViewParam.setMargins(g.getScreenDpi(2), g.getScreenDpi(2), g.getScreenDpi(2), g.getScreenDpi(2));

            for (int i = 2001; i >=1919 ; i--) {
                TextView textView = new TextView(getContext());
                textView.setText(String.valueOf(i));
                textView.setTextSize(20);
                textView.setTextColor(getResources().getColor(R.color.myblack));
                textView.setGravity(Gravity.START|Gravity.CENTER_VERTICAL);
                textView.setLayoutParams(textViewParam);
                yearLinearLayout.addView(textView);
            }
            showScrollPotion(yearScrollView ,year , yearLinearLayout );

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.done :
                    String s = day.getText().toString() +"/"+month.getText().toString()+"/"+year.getText().toString();
                    g.setUserData("yearOfBirth" ,year.getText().toString());
                    g.setUserData("monthOfBirth" ,month.getText().toString());
                    g.setUserData("dayOfBirth" , day.getText().toString());
                    DateOfBirth.setText(s);
                    dismiss();
                    break;
                case R.id.cancel :
                    dismiss();
                    break;
            }
        }
    }

}
