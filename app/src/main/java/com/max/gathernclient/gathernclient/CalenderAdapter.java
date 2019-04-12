package com.max.gathernclient.gathernclient;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

public class CalenderAdapter extends RecyclerView.Adapter<CalenderAdapter.CalenderHolder> {
Context context ;
    private List<CalenderClass> mCalenderList;
    private OnEntryClickListener mOnEntryClickListener ;

    public CalenderAdapter(List<CalenderClass> CalenderList , Context context) {
        this.mCalenderList = CalenderList;
        this.context = context ;
    }


    @NonNull
    @Override
    public CalenderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  row  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.calender_row, parent, false);
        return new CalenderHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull CalenderHolder holder, int position) {
        CalenderClass mCalender = mCalenderList.get(position);
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH); // start from day 1 = 1
        int currentMonth = calendar.get(Calendar.MONTH); // start from jen = 0

        int mCurrentDay = mCalender.getDay();

        Drawable drawableBlue = context.getResources().getDrawable(R.drawable.shape_color_blue_radius60);
        Drawable drawableLIiteGreen = context.getResources().getDrawable(R.drawable.shape_color_litegreen_radius60);
        if ( mCalender.getDay() < currentDay && mCalender.getDay() > 0 && mCalender.getMonthIndex() == currentMonth){
            holder.Day.setTextColor(context.getResources().getColor(R.color.mydarkgray));
            holder.CalenderLayout.setBackgroundColor(0xffffff);
        }else if( mCurrentDay == currentDay && !mCalender.isImageChecked() && currentMonth == mCalender.getMonthIndex()){
            holder.CalenderLayout.setBackground(drawableLIiteGreen);
            holder.Day.setTextColor(context.getResources().getColor(R.color.mygreen));
        }else if(!mCalender.isImageChecked()){
            holder.CalenderLayout.setBackgroundColor(0xffffff);
        } else {
            holder.CalenderLayout.setBackground(drawableBlue);
        }


         if(mCalender.getDay() !=0){
            String s = "" +mCalender.getDay() ;
            holder.Day.setText(s);
        } else {
            String s = "";
            holder.Day.setText(s);
        }


    }


    @Override
    public int getItemCount() {

        return mCalenderList.size();
    }

    class CalenderHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView Day ;
        LinearLayout CalenderLayout ;

        public CalenderHolder(View itemView) {
            super(itemView);
            Day = itemView.findViewById(R.id.day);
            CalenderLayout = itemView.findViewById(R.id.calenderLayout);
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