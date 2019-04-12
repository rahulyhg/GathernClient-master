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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class AllHouseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AllHouseClass> mHouseList;
    private OnEntryClickListener mOnEntryClickListener ;
    Context context ;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    Globals g ;

    public AllHouseAdapter(List<AllHouseClass> HouseList ,  Context context ) {
        this.mHouseList = HouseList;
        this.context = context ;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM) {
            View row = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.allhouse_row, parent, false);
            return new ItemViewHolder(row);
        }else{
            View row = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(row);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            populateItemRows((ItemViewHolder) holder, position);
        }
        else if (holder instanceof LoadingViewHolder)
        {
            showLoadingView((LoadingViewHolder) holder, position);
        }
    }


    @Override
    public int getItemCount() {
        return mHouseList == null ? 0: mHouseList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return mHouseList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }
    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView HouseName, HouseDescription, HousePrice, HouseArea
                , SingleTextOnImage ,buttonBookNow ,buttonMoreDetails , houseKm , viewCount;
        ImageView HouseImage ;
        RatingBar HouseRating ;
        LinearLayout   layout_buttons_allhouse ,parentLayout;
        public ItemViewHolder(View itemView) {
            super(itemView);
            HouseName = itemView.findViewById(R.id.houseName);
            HouseDescription = itemView.findViewById(R.id.houseDescription);
            HousePrice = itemView.findViewById(R.id.housePrice);
            HouseArea = itemView.findViewById(R.id.houseArea);
            HouseRating = itemView.findViewById(R.id.houseRating);
            HouseImage = itemView.findViewById(R.id.houseImage);
            SingleTextOnImage = itemView.findViewById(R.id.singleTextOnImage);
            layout_buttons_allhouse = itemView.findViewById(R.id.layout_buttons_allhouse);
            parentLayout = itemView.findViewById(R.id.parentLayout);
            buttonBookNow = itemView.findViewById(R.id.button_book_now);
            buttonMoreDetails = itemView.findViewById(R.id.button_more_details);
            houseKm = itemView.findViewById(R.id.houseKm);
            viewCount = itemView.findViewById(R.id.viewCount);
            itemView.setOnClickListener(this);
            buttonBookNow.setOnClickListener(this);
          //  buttonMoreDetails.setOnClickListener(this);
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
    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        ImageView loadingView ;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            loadingView = itemView.findViewById(R.id.loading);
        }
    }
    private void populateItemRows(ItemViewHolder holder, int position) {
        AllHouseClass AllHouse = mHouseList.get(position);
        g = new Globals(context);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.parentLayout.setClipToOutline(true);
        }
        holder.HouseName.setText(AllHouse.getmHouseName());
        holder.HouseDescription.setText(AllHouse.getmHouseDescription());
        holder.HousePrice.setText(AllHouse.getmHousePrice());
        holder.HouseArea.setText(AllHouse.getmHouseArea());
        holder.HouseRating.setRating(AllHouse.getmHouseRating());
        Drawable drawable = holder.HouseRating.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#FFB400"), PorterDuff.Mode.SRC_ATOP);
        holder.HouseRating.setProgressDrawable(drawable);
        Picasso.with(context).load(AllHouse.getImageUrl()).into(holder.HouseImage);
        if(AllHouse.getDistance_in_km().length() >0) {
            String s = AllHouse.getDistance_in_km() + "km" ;
            holder.houseKm.setText(s);
        }else
            holder.houseKm.setVisibility(View.GONE);
        holder.viewCount.setText(AllHouse.getViews_count());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            holder.HouseImage.setClipToOutline(true);
        }
        if(!AllHouse.isUnitAvailable()){
            holder.buttonBookNow.setText("غير متوفر في هذا التاريخ جرب تاريخ اخر");
            Drawable drawable2= g.shapeColorString("#594EBD3A",g.getScreenDpi(5));
            holder.buttonBookNow.setBackground(drawable2);
        }
        holder.SingleTextOnImage.setText(AllHouse.getSingleTextOnImage());
    }
    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        Animation animation = AnimationUtils.loadAnimation(context ,R.anim.rotation);
        animation.setInterpolator(new LinearInterpolator());
        viewHolder.loadingView.setAnimation(animation);
    }
}