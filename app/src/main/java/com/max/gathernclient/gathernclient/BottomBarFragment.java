package com.max.gathernclient.gathernclient;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

public class BottomBarFragment extends Fragment implements View.OnClickListener {
    Globals g ;
    LinearLayout linearLayout ,houses , booking ,more , favLayout;
    ImageView houseslogo ,bookImage ,moreImage ,favImage;
    TextView moreText ,bookText   ,houseText , favText ;
    String activity ;
    public BottomBarFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bottom_bar_fragment,container,false);
        // add layouts
        houses = rootView.findViewById(R.id.houses);
        booking = rootView.findViewById(R.id.booking);
        more = rootView.findViewById(R.id.more);
        favLayout =rootView.findViewById(R.id.favLayout);

        linearLayout = rootView.findViewById(R.id.parentLayout);
        // add images
        houseslogo = rootView.findViewById(R.id.houseslogo);
        bookImage = rootView.findViewById(R.id.bookImage);
        moreImage = rootView.findViewById(R.id.moreImage);
        favImage = rootView.findViewById(R.id.favImage);
        // add text
        moreText = rootView.findViewById(R.id.moreText);
        bookText = rootView.findViewById(R.id.bookText);
        houseText = rootView.findViewById(R.id.houseText);
        favText = rootView.findViewById(R.id.favText);
        // set listeners

        houses.setOnClickListener(this);
        booking.setOnClickListener(this);
        more.setOnClickListener(this);
        favLayout.setOnClickListener(this);

        g = new Globals(getContext());
         activity = Objects.requireNonNull(this.getArguments()).getString("Activity");
        switch (Objects.requireNonNull(activity)){
            case "MainActivity" :
                setColoredViews(houseslogo,houseText);
                break;
            case "MyBooking" :
                setColoredViews(bookImage,bookText);
                break;
            case "More" :
                setColoredViews(moreImage,moreText);
                break;
            case "Fav" :
                setColoredViews(favImage,favText);
                break;

        }
        return rootView ;
    }

    @Override
    public void onClick (View view){
        switch (view.getId()){
            case R.id.houses :
                setColoredViews(houseslogo,houseText);
                unColorView(bookImage , bookText);
                unColorView(moreImage , moreText);
                if(!activity.equals("MainActivity"))
                startActivity(new Intent(getContext() , HomePage.class));
                break;
            case R.id.parties :
                unColorView(houseslogo,houseText);
                unColorView(bookImage , bookText);
                unColorView(moreImage , moreText);                if(!activity.equals("Parties"))
                startActivity(new Intent(getContext() , Parties.class));
                break;
            case R.id.favLayout :
                unColorView(houseslogo,houseText);
                unColorView(bookImage , bookText);
                unColorView(moreImage , moreText);
                setColoredViews(favImage,favText);
                if(!activity.equals("Fav"))
                startActivity(new Intent(getContext() , Favorite.class));
                break;
            case R.id.booking :
                unColorView(houseslogo,houseText);
                setColoredViews(bookImage , bookText);
                unColorView(moreImage , moreText);
                if(!activity.equals("MyBooking"))
                startActivity(new Intent(getContext() , MyBooking.class));
                break;
            case R.id.more :
                unColorView(houseslogo,houseText);
                unColorView(bookImage , bookText);
                setColoredViews(moreImage , moreText);
                if(!activity.equals("More"))
                startActivity(new Intent(getContext() , More.class));
                break;


        }

}
private void setColoredViews (ImageView imageView , TextView textView){
    imageView.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
    textView.setTextColor(getResources().getColor(R.color.colorPrimary));
}
private void unColorView(ImageView imageView , TextView textView){
    imageView.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.tintGray), PorterDuff.Mode.SRC_IN);
}
}