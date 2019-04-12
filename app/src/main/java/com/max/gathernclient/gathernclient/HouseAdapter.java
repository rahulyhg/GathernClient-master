package com.max.gathernclient.gathernclient;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

public class HouseAdapter extends RecyclerView.Adapter<HouseAdapter.HouseHolder> {

    private List<HouseClass> mHouseList;
    private OnEntryClickListener mOnEntryClickListener ;

    public HouseAdapter(List<HouseClass> HouseList) {
        this.mHouseList = HouseList;
    }


    @NonNull
    @Override
    public HouseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  row = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.house_row, parent, false);
        return new HouseHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull HouseHolder holder, int position) {
        HouseClass House = mHouseList.get(position);
        holder.HouseName.setText(House.getmHouseName());
        holder.HouseDescription.setText(House.getmHouseDescription());
        holder.HousePrice.setText(House.getmHousePrice());
        holder.HouseArea.setText(House.getmHouseArea());
        if(House.hasText()){
            holder.frameLayout.setVisibility(View.GONE);
            holder.TextLayout.setVisibility(View.VISIBLE);
        }else {
            holder.frameLayout.setVisibility(View.VISIBLE);
            holder.SingleTextOnImage.setText(House.getSingleTextOnImage());
            if(House.getSingleTextOnImage().length() >= 10){
                holder.SingleTextOnImage.setTextSize(12f);
            }
            holder.TextLayout.setVisibility(View.GONE);
        }

        holder.HouseRating.setRating(House.getmHouseRating());
        Drawable drawable = holder.HouseRating.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#FFB400"), PorterDuff.Mode.SRC_ATOP);
        holder.HouseRating.setProgressDrawable(drawable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
           holder.StartBanner.setClipToOutline(true);
            holder.TextLayout.setClipToOutline(true);
       }
        holder.HouseImage.setImageResource(House.getmImage());
        holder.StartBanner.setImageResource(House.getmStartBanner());
    }


    @Override
    public int getItemCount() {

        return mHouseList.size();
    }

    class HouseHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView HouseName, HouseDescription, HousePrice, HouseArea
                , SingleTextOnImage;
        ImageView HouseImage , StartBanner;
        RatingBar HouseRating ;
        LinearLayout TextLayout ,parentLayout ;
        FrameLayout frameLayout ;
        public HouseHolder(View itemView) {
            super(itemView);
            HouseName = itemView.findViewById(R.id.houseName);
            HouseDescription = itemView.findViewById(R.id.houseDescription);
            HousePrice = itemView.findViewById(R.id.housePrice);
            HouseArea = itemView.findViewById(R.id.houseArea);
            HouseRating = itemView.findViewById(R.id.houseRating);
            HouseImage = itemView.findViewById(R.id.houseImage);
            TextLayout = itemView.findViewById(R.id.textLayout);
            SingleTextOnImage = itemView.findViewById(R.id.singleTextOnImage);
            parentLayout = itemView.findViewById(R.id.parentLayout);
            StartBanner = itemView.findViewById(R.id.startBanner);
            frameLayout = itemView.findViewById(R.id.frameOfStartBanner);
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