package com.max.gathernclient.gathernclient;



import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class WaitingPayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<WaitingClass> mBookingList;
    private OnEntryClickListener mOnEntryClickListener ;
    Context context ;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public WaitingPayAdapter(List<WaitingClass> BookingList , Context context) {
        this.mBookingList = BookingList;
        this.context = context ;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM) {
            View row = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.wating_row, parent, false);
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
        return mBookingList == null ? 0 : mBookingList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return mBookingList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }
    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imageView ;
        TextView name ,expire ,day ,date , timerText;

        public ItemViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageHouse);
            name = itemView.findViewById(R.id.nameAndHouseNumber);
            expire = itemView.findViewById(R.id.expireState);
            day = itemView.findViewById(R.id.dayOfDate);
            date = itemView.findViewById(R.id.date);
            timerText = itemView.findViewById(R.id.timerText);
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

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        ImageView loadingView ;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            loadingView = itemView.findViewById(R.id.loading);
        }
    }
    private void   populateItemRows(ItemViewHolder holder,int position){
        WaitingClass Booking = mBookingList.get(position);
        // holder.imageView.setImageResource(Booking.getImage());
        Picasso.with(context).load(Booking.getImage()).into(holder.imageView);
        String s = Booking.getName()+","+Booking.getHouseNumber();
        holder.name.setText(s);
        holder.expire.setText(Booking.getExpireState());
        holder.day.setText(Booking.getDayOfDate());
        holder.date.setText(Booking.getData());
        int  seconed =3600 - Booking.getSecondsAgo();

        int remMinetu = seconed /60 ;
        int remSeconed =seconed - (remMinetu *60 );

        new CountDownTimer(60 * 60 * 1000, 1000) {
            int minute = remMinetu ;// 54
            int mSeconed = remSeconed ;// 14
            @Override
            public void onTick(long millisUntilFinished) {
                if(minute >0) {
                    if (mSeconed > 0) {
                        mSeconed--;
                    } else {
                        mSeconed = 59;
                        minute--;
                    }
                    String remainingTime = "" + minute + ":" + mSeconed;
                    holder.timerText.setText(remainingTime);

                }else {
                    holder.timerText.setText("منتهي الصلاحية");

                }
            }

            @Override
            public void onFinish() {

            }
        }.start();

    }
    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        Animation animation = AnimationUtils.loadAnimation(context ,R.anim.rotation);
        animation.setInterpolator(new LinearInterpolator());
        viewHolder.loadingView.setAnimation(animation);
    }
}