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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class OffersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<OffersClass> mOffersList;
    private OnEntryClickListener mOnEntryClickListener ;
    Globals globals ;
    Context context ;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public OffersAdapter(List<OffersClass> OffersList , Context context) {
        this.mOffersList = OffersList;
        this.context = context ;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM) {
            View row = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.offers_row, parent, false);
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
        return mOffersList == null ? 0 : mOffersList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return mOffersList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }
    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView HouseName, HouseDescription, HousePrice, HouseArea
                , SingleTextOnImage ,buttonBookNow ,buttonMoreDetails , views_count , oldPrice;//, headerView
        ImageView HouseImage ;
        RatingBar HouseRating ;
        LinearLayout   layout_buttons_allhouse ,parentLayout;
        FrameLayout  headerFrame ,oldPriceFrameLayOut;//
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
            headerFrame = itemView.findViewById(R.id.headerFrame);
           // headerView = itemView.findViewById(R.id.headerText);
            views_count = itemView.findViewById(R.id.views_count);
            oldPrice = itemView.findViewById(R.id.oldPrice);
            oldPriceFrameLayOut =itemView.findViewById(R.id.oldPriceFrameLayOut);
            itemView.setOnClickListener(this);
            buttonBookNow.setOnClickListener(this);
          //  buttonMoreDetails.setOnClickListener(this);
          //  headerFrame.setOnClickListener(this);
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

        OffersClass Offers = mOffersList.get(position);
        globals = new Globals(context);
        String bookingDates = globals.finishedBookingDate() ;
        if (position == 0){
            holder.headerFrame.setVisibility(View.VISIBLE);
            // holder.headerView.setText(bookingDates);
            holder.parentLayout.setVisibility(View.GONE);
        }else {
            holder.headerFrame.setVisibility(View.GONE);
            holder.parentLayout.setVisibility(View.VISIBLE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.parentLayout.setClipToOutline(true);
        }
        if(!Offers.isUnitAvailable()){
            holder.buttonBookNow.setText("غير متوفر في هذا التاريخ جرب تاريخ اخر");
            Drawable drawable= globals.shapeColorString("#594EBD3A",globals.getScreenDpi(5));
            holder.buttonBookNow.setBackground(drawable);
        }
        holder.HouseName.setText(Offers.getmHouseName());
        holder.HouseDescription.setText(Offers.getmHouseDescription());
        holder.HousePrice.setText(Offers.getmHousePrice());
        holder.HouseArea.setText(Offers.getmHouseArea());
        holder.HouseRating.setRating(Offers.getmHouseRating());
        Drawable drawable = holder.HouseRating.getProgressDrawable();
        drawable.setColorFilter(Color.parseColor("#FFB400"), PorterDuff.Mode.SRC_ATOP);
        holder.HouseRating.setProgressDrawable(drawable);
        holder.views_count.setText(Offers.getViews_count());
        holder.oldPrice.setText(Offers.getOldPrice());
        if(Offers.getOldPrice().equals(""))
            holder.oldPriceFrameLayOut.setVisibility(View.GONE);
        Picasso.with(context).load(Offers.getmImageUrl()).fit().into(holder.HouseImage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
            holder.HouseImage.setClipToOutline(true);
        }
        holder.SingleTextOnImage.setText(Offers.getSingleTextOnImage());

    }
    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        Animation animation = AnimationUtils.loadAnimation(context ,R.anim.rotation);
        animation.setInterpolator(new LinearInterpolator());
        viewHolder.loadingView.setAnimation(animation);
    }
    }