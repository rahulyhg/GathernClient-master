package com.max.gathernclient.gathernclient;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PagerAdapterForSearchResult extends PagerAdapter implements View.OnClickListener {
private Context context ;
private ArrayList<String> imageUrl ;
private String id ;

    PagerAdapterForSearchResult (Context context , ArrayList<String> imageUrl , String id){
        this.context = context ;
        this.imageUrl = imageUrl ;
        this.id = id ;
    }
    @Override
    public int getCount() {
        return imageUrl.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setOnClickListener(this);
       // imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        Picasso.with(context).load(imageUrl.get(position)).fit().into(imageView);
        container.addView(imageView);
        return imageView ;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
             container.removeView((View) object);
    }

    @Override
    public void onClick(View v) {
        Intent open = new Intent(context, HouseDetails.class);
        open.putExtra("ID", id);
       context.startActivity(open);
    }
}




