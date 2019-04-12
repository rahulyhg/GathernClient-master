package com.max.gathernclient.gathernclient;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;

public class MyDialogBox extends Dialog implements android.view.View.OnClickListener {


    public MyDialogBox(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.chose_city);
    }

    @Override
    public void onClick(View v) {

    }
}