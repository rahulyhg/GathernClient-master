package com.max.gathernclient.gathernclient;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SearchResultClass> mHouseList;
    private OnEntryClickListener mOnEntryClickListener;
    Context context;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    Globals g;


    public SearchResultAdapter(List<SearchResultClass> HouseList, Context context) {
        this.mHouseList = HouseList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View row = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_result_row, parent, false);
            return new ItemViewHolder(row);
        } else {
            View row = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(row);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            populateItemRows((ItemViewHolder) holder, position);
        } else if (holder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) holder, position);
        }
    }


    @Override
    public int getItemCount() {
        return mHouseList == null ? 0 : mHouseList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mHouseList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView HouseName, HouseDescription, HousePrice, SingleTextOnImage, area, city, distance, viewCount, capacity, poolType, reviews;
        //ImageView HouseImage;
        View v1 ,v2 ,v3 ,v4 ,v5 ;
        LinearLayout layout_buttons_allhouse, parentLayout;
        ViewPager viewPager;

        public ItemViewHolder(View itemView) {
            super(itemView);
            HouseName = itemView.findViewById(R.id.houseName);
            HouseDescription = itemView.findViewById(R.id.houseDescription);
            HousePrice = itemView.findViewById(R.id.housePrice);
           // HouseImage = itemView.findViewById(R.id.houseImage);
            SingleTextOnImage = itemView.findViewById(R.id.singleTextOnImage);
            layout_buttons_allhouse = itemView.findViewById(R.id.layout_buttons_allhouse);
            parentLayout = itemView.findViewById(R.id.parentLayout);
            area = itemView.findViewById(R.id.area);
            city = itemView.findViewById(R.id.city);
            distance = itemView.findViewById(R.id.distance);
            viewCount = itemView.findViewById(R.id.viewCount);
            capacity = itemView.findViewById(R.id.capacity);
            poolType = itemView.findViewById(R.id.isPool);
            reviews = itemView.findViewById(R.id.reviews);
            viewPager = itemView.findViewById(R.id.viewPager);
            v1 = itemView.findViewById(R.id.view1);
            v2 = itemView.findViewById(R.id.view2);
            v3 = itemView.findViewById(R.id.view3);
            v4 = itemView.findViewById(R.id.view4);
            v5 = itemView.findViewById(R.id.view5);
            viewPager.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnEntryClickListener != null) {
                mOnEntryClickListener.onEntryClick(v, getLayoutPosition());
            }
        }
    }

    public interface OnEntryClickListener {
        void onEntryClick(View view, int position);
    }

    public void setmOnEntryClickListener(OnEntryClickListener onEntryClickListener) {
        mOnEntryClickListener = onEntryClickListener;
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        ImageView loadingView;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            loadingView = itemView.findViewById(R.id.loading);
        }
    }

    private void populateItemRows(ItemViewHolder holder, int position) {
        SearchResultClass resultClass = mHouseList.get(position);
        g = new Globals(context);
        // set pager
        PagerAdapterForSearchResult adapter =
                new PagerAdapterForSearchResult(context, resultClass.getBoxGalleryUrl() , String.valueOf(resultClass.getId()));

        holder.viewPager.setAdapter(adapter);
       // holder.viewPager.setId(position+1);
        holder.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                setViewsAsChecked(i , holder.v1, holder.v2, holder.v3,holder.v4 ,holder.v5 );
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

    // end part of set fragment
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.parentLayout.setClipToOutline(true);
        }
        holder.HouseName.setText(resultClass.getmHouseName());
        holder.HouseDescription.setText(resultClass.getmHouseDescription());
        holder.HousePrice.setText(resultClass.getmHousePrice());
        holder.area.setText(resultClass.getArea());
        holder.city.setText(resultClass.getCity());
        holder.capacity.setText(resultClass.getGeneral_persons_capacity());
        holder.poolType.setText(resultClass.getPoolType());
        String review = "" + resultClass.getPoints() + "\n" + resultClass.getText();
        holder.reviews.setText(review);
       // Picasso.with(context).load(resultClass.getImageUrl()).into(holder.HouseImage);

        if (resultClass.getDistance_in_km().length() > 0) {
            String s = resultClass.getDistance_in_km() + "km";
            holder.distance.setText(s);
        } else
            holder.distance.setVisibility(View.GONE);
        holder.viewCount.setText(resultClass.getViews_count());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          //  holder.HouseImage.setClipToOutline(true);
        }
        if (!resultClass.isUnitAvailable()) {
            Drawable drawable2 = g.shapeColorString("#594EBD3A", g.getScreenDpi(5));
        }
        holder.SingleTextOnImage.setText(resultClass.getSingleTextOnImage());
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.rotation);
        animation.setInterpolator(new LinearInterpolator());
        viewHolder.loadingView.setAnimation(animation);
    }
    private void setViewsAsChecked(int postion ,View v1 ,View v2,View v3,View v4,View v5) {
        if(postion >4)
            postion = postion-5;
        if(postion >9)
            postion = postion-10;

            Drawable unChecked = g.shapeColorString("#808080", g.getScreenDpi(60));
        Drawable checked = g.shapeColorString("#F3F3F3", g.getScreenDpi(60));
        switch (postion) {
            case 0:
                v1.setBackground(checked);
                v2.setBackground(unChecked);
                v3.setBackground(unChecked);
                v4.setBackground(unChecked);
                v5.setBackground(unChecked);

                break;
            case 1:
                v1.setBackground(unChecked);
                v2.setBackground(checked);
                v3.setBackground(unChecked);
                v4.setBackground(unChecked);
                v5.setBackground(unChecked);
                break;
            case 2:
                v1.setBackground(unChecked);
                v2.setBackground(unChecked);
                v3.setBackground(checked);
                v4.setBackground(unChecked);
                v5.setBackground(unChecked);
                break;
            case 3:
                v1.setBackground(unChecked);
                v2.setBackground(unChecked);
                v3.setBackground(unChecked);
                v4.setBackground(checked);
                v5.setBackground(unChecked);
                break;
            case 4:
                v1.setBackground(unChecked);
                v2.setBackground(unChecked);
                v3.setBackground(unChecked);
                v4.setBackground(unChecked);
                v5.setBackground(checked);
                break;

        }

    }
}