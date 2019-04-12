package com.max.gathernclient.gathernclient;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class PaymentPage extends AppCompatActivity {
ImageView close ;
Globals globals ;
ImageView madaIcon ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_page);
        globals = new Globals(this);
        close = findViewById(R.id.close);
        madaIcon = findViewById(R.id.madaIcon);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(globals.getScreenDpi(60));
        drawable.setColor(Color.parseColor("#898A8C"));
        close.setBackground(drawable);
      if(getStrings("ID").equals("masterCard")){
          madaIcon.setVisibility(View.GONE);
      }

    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.close :
                finish();
                break;

        }
    }
    public String getStrings ( String key){
        String var ="";
        if (getIntent().getExtras() !=null) {
            try {
                var = getIntent().getExtras().get(key).toString();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
        return var ;
    }
}
