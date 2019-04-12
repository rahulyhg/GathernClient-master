package com.max.gathernclient.gathernclient;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class RatingFragment extends android.support.v4.app.Fragment  {

    Globals g ;
    TextView ratingDouble_text , userName , unitName , comment , date ;
    String user_Name , unit_Name , comments , mDate , scoreText;
    double points ;
    public RatingFragment (){
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rating_fragment,container,false);
        g = new Globals(getContext());
        ratingDouble_text =  view.findViewById(R.id.ratingDouble_text);
        userName =  view.findViewById(R.id.userName);
        unitName =  view.findViewById(R.id.unitName);
        comment =  view.findViewById(R.id.comment);
        date =  view.findViewById(R.id.date);
        getArg();
        String pointString = String.valueOf(points);
        String s1 = pointString + "\n" +scoreText;
        SpannableString ss1 = new SpannableString(s1);
        ss1.setSpan(new RelativeSizeSpan(2f), 0, pointString.length(), 0);
        ss1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.mywhite)), 0, pointString.length(), 0);
        ratingDouble_text.setText(ss1);
        userName.setText(user_Name);
        unitName.setText(unit_Name);
        comment.setText(comments);
        date.setText(mDate);
        return view ;
    }
    private void getArg (){
        user_Name = Objects.requireNonNull(this.getArguments()).getString("user_name");
        unit_Name = Objects.requireNonNull(this.getArguments()).getString("unit_name");
        comments = Objects.requireNonNull(this.getArguments()).getString("comment");
        mDate = Objects.requireNonNull(this.getArguments()).getString("date");
        scoreText = Objects.requireNonNull(this.getArguments()).getString("scoreText");
        points = Objects.requireNonNull(this.getArguments()).getDouble("scoreDouble");

    }

}