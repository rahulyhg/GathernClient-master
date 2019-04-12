package com.max.gathernclient.gathernclient;


import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerHolder> {

    private List<BannerClass> mBannerList;
    private OnEntryClickListener mOnEntryClickListener ;
Context context ;
    public BannerAdapter(List<BannerClass> BannerList , Context context) {
        this.mBannerList = BannerList;
        this.context  = context ;
    }


    @NonNull
    @Override
    public BannerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  row =LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.banner_row, parent, false);
        return new BannerHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerHolder holder, int position) {
        BannerClass Banner = mBannerList.get(position);
        Picasso.with(context).load(Banner.getImageUrl()).into(holder.Image);
        if(Banner.getImageId() !=0){
            holder.Image.setImageResource(Banner.getImageId());
        }
        if(Banner.getImageHeight() > 0 && Banner.getImageWidth()> 0) {
            holder.BannerLayout.getLayoutParams().height   = Banner.getImageHeight();
            holder.BannerLayout.getLayoutParams().width    = Banner.getImageWidth();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.Image.setClipToOutline(true);
            }
        }
    }
    @Override
    public int getItemCount() {
        return mBannerList.size();
    }

    class BannerHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView Image ;
        LinearLayout BannerLayout ;
        public BannerHolder(View itemView) {
            super(itemView);
            Image = itemView.findViewById(R.id.bannerImage);
            BannerLayout = itemView.findViewById(R.id.bannerLayout);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mOnEntryClickListener!=null){
                mOnEntryClickListener.onEntryClick(v,getLayoutPosition());
            }

        }
    }
    public interface OnEntryClickListener{
        void onEntryClick(View view,int position);
    }
    public void setmOnEntryClickListener(OnEntryClickListener onEntryClickListener){
        mOnEntryClickListener=onEntryClickListener;
    }


}