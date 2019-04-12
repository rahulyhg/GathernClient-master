package com.max.gathernclient.gathernclient;


import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;

public class MyTextView extends android.support.v7.widget.AppCompatTextView {


    public MyTextView(Context context) {
        super(context);
        init();

    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
       init();
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
private void init(){


    if (!getTypeface().isBold()&&!getTypeface().isItalic()){

        Typeface roman = ResourcesCompat.getFont(getContext() ,R.font.cairo);
        this.setTypeface(roman);

    }else if (getTypeface().isBold()){
        Typeface bold = ResourcesCompat.getFont(getContext() ,R.font.cairo_bold);
                //Typeface.createFromAsset(getContext().getAssets() , "fonts/FrutigerLTArabic-65Bold.ttf");
        this.setTypeface(bold);
   }else if (getTypeface().isItalic()){
        Typeface light =  ResourcesCompat.getFont(getContext() ,R.font.cairo_light );
                //Typeface.createFromAsset(getContext().getAssets() , "fonts/FrutigerLTArabic-45Light.ttf");
        this.setTypeface(light);
    }

    }

   }
