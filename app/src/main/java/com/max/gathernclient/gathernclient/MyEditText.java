package com.max.gathernclient.gathernclient;


import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

public class MyEditText extends AppCompatEditText {


    public MyEditText(Context context) {
        super(context);
        init();

    }

    public MyEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
       init();
    }

    public MyEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
       init();
    }
    private void init(){

        Typeface light = Typeface.createFromAsset(getContext().getAssets() , "fonts/FrutigerLTArabic-45Light.ttf");
        Typeface roman = Typeface.createFromAsset(getContext().getAssets() , "fonts/FrutigerLTArabic-55Roman.ttf");
        Typeface bold = Typeface.createFromAsset(getContext().getAssets() , "fonts/FrutigerLTArabic-65Bold.ttf");
        if (getTypeface().isBold()){
            this.setTypeface(bold);
        }else if (getTypeface().isItalic()){
            this.setTypeface(light);
        }else {
            this.setTypeface(roman);
        }
    }

}
