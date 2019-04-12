package com.max.gathernclient.gathernclient;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Enumeration;

public class Test extends AppCompatActivity {
    Globals g;
    MyScrollView dayScrollView, monthScrollView, yearScrollView;
    TextView done, cancel, firstText;
    LinearLayout yearLinearLayout, monthLinearLayout, dayLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        g = new Globals(this);
        initRefrance();
        initYearScroll();
        initDayScroll();
        setAllScrollListener(monthScrollView);
    }

    private void setAllScrollListener(MyScrollView scrollView) {
        scrollView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    scrollView.startScrollerTask();
                }

                return false;
            }
        });

        scrollView.setOnScrollStoppedListener(new MyScrollView.OnScrollStoppedListener() {
            @Override
            public void onScrollStopped() {
                scrollToFinish(scrollView.getScrollY() ,scrollView);

            }
        });
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scroll = scrollView.getScrollY();
                done.setText(String.valueOf(scroll));
            }
        });
    }

    private void initYearScroll() {
        // add year scroll
        LinearLayout.LayoutParams textViewParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, g.getScreenDpi(50));
        textViewParam.setMargins(g.getScreenDpi(2), g.getScreenDpi(2), g.getScreenDpi(2), g.getScreenDpi(2));

        for (int i = 2021; i >= 1917; i--) {
            TextView textView = new TextView(this);
            if (i != 2021 && i != 2020 && i != 1917 && i != 1918)
                textView.setText(String.valueOf(i));
            textView.setTextSize(20);
            textView.setTextColor(getResources().getColor(R.color.myblack));
            textView.setGravity(Gravity.START);
            textView.setLayoutParams(textViewParam);
            yearLinearLayout.addView(textView);
        }
        setAllScrollListener(yearScrollView);
    }

    private void initDayScroll() {

// add month scroll
        LinearLayout.LayoutParams textViewParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, g.getScreenDpi(50));
        textViewParam.setMargins(g.getScreenDpi(2), g.getScreenDpi(2), g.getScreenDpi(2), g.getScreenDpi(2));

        for (int i = -1; i <= 33; i++) {
            TextView textView = new TextView(this);
            if (i != -1 && i != 0 && i != 32 && i != 33)
                textView.setText(String.valueOf(i));
            textView.setTextSize(20);
            textView.setTextColor(getResources().getColor(R.color.myblack));
            textView.setGravity(Gravity.END);
            textView.setLayoutParams(textViewParam);
            dayLinearLayout.addView(textView);
        }
        setAllScrollListener(dayScrollView);

    }

    private void initRefrance() {
        monthScrollView = findViewById(R.id.mScrollView);
        dayScrollView = findViewById(R.id.dayScrollView);
        yearScrollView = findViewById(R.id.yearScrollView);
        done = findViewById(R.id.done);
        cancel = findViewById(R.id.cancel);
        firstText = findViewById(R.id.firstText);
        yearLinearLayout = findViewById(R.id.yearLinearLayout);
        monthLinearLayout = findViewById(R.id.monthLinearLayout);
        dayLinearLayout = findViewById(R.id.dayLinearLayout);
    }

    private void setScroll(int scrollTo , MyScrollView scrollView) {
        ObjectAnimator anim = ObjectAnimator.ofInt(scrollView, "ScrollY", scrollTo);
        anim.setDuration(1000);
        anim.start();
    }

    public void scrollToFinish(int currentPotion ,  MyScrollView scrollView) {
        if (currentPotion > 0 && currentPotion < 100)
            setScroll(100 , scrollView);
        if (currentPotion > 100 && currentPotion < 200)
            setScroll(200, scrollView);
        if (currentPotion > 200 && currentPotion < 300)
            setScroll(300 , scrollView);
        if (currentPotion > 300 && currentPotion < 400)
            setScroll(400 , scrollView);
        if (currentPotion > 400 && currentPotion < 500)
            setScroll(500, scrollView);
        if (currentPotion > 500 && currentPotion < 600)
            setScroll(600 , scrollView);
        if (currentPotion > 600 && currentPotion < 700)
            setScroll(700, scrollView);
        if (currentPotion > 700 && currentPotion < 800)
            setScroll(800, scrollView);
        if (currentPotion > 800 && currentPotion < 900)
            setScroll(900, scrollView);
        if (currentPotion > 900 && currentPotion < 1000)
            setScroll(1000, scrollView);
        if (currentPotion > 1000 && currentPotion < 1100)
            setScroll(1100, scrollView);
        if (currentPotion > 1100 && currentPotion < 1200)
            setScroll(1200, scrollView);
    }

}
