package com.max.gathernclient.gathernclient;


import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
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

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<RatingPage.ReviewsClass> reviewsList;
    private OnEntryClickListener mOnEntryClickListener ;
    Globals g ;
    Context context ;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    String totalReview , totalText ;

    public ReviewsAdapter(List<RatingPage.ReviewsClass> reviewsList , Context context ,  String totalReview , String totalText) {
        this.reviewsList = reviewsList;
        this.context = context ;
        this.totalReview = totalReview ;
        this.totalText = totalText ;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        g = new Globals(context);
        if(viewType == VIEW_TYPE_ITEM) {
            View row = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rating_row, parent, false);
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
    return  reviewsList == null ? 0 : reviewsList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return reviewsList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }
    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView userName , unitName ,ratingDouble_text , comment , date , rating , numberOfRating;
        LinearLayout headerLayout , recycleLayout , ratingHeader ,cleanLayout ,stuffLayout , chaletLayout;

//        String pointString = String.valueOf(points);
//        String s1 = pointString + "\n" +text;
//        SpannableString ss1 = new SpannableString(s1);
//        ss1.setSpan(new RelativeSizeSpan(2f), 0, pointString.length(), 0);
//        //ss1.setSpan(new StyleSpan(Typeface.BOLD),0,3,0);
//        ss1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.mywhite)), 0, pointString.length(), 0);

        public ItemViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            unitName = itemView.findViewById(R.id.unitName);
            comment = itemView.findViewById(R.id.comment);
            date = itemView.findViewById(R.id.date);
            ratingDouble_text = itemView.findViewById(R.id.ratingDouble_text);
            headerLayout = itemView.findViewById(R.id.headerLayout);
            recycleLayout = itemView.findViewById(R.id.recycleLayout);
            ratingHeader = itemView.findViewById(R.id.ratingHeader);
            rating = itemView.findViewById(R.id.rating);
            numberOfRating = itemView.findViewById(R.id.numberOfRating);

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

        if(position == 0){
            holder.headerLayout.setVisibility(View.VISIBLE);
            holder.recycleLayout.setVisibility(View.GONE);
            holder.ratingHeader.setBackground(g.shape(R.color.mygray, g.getScreenDpi(10),0,0 ));

            String totalScore = totalReview + "\n" +totalText;
            SpannableString totalScore2 = new SpannableString(totalScore);
            totalScore2.setSpan(new RelativeSizeSpan(2.5f), 0, totalReview.length(), 0);
            //ss1.setSpan(new StyleSpan(Typeface.BOLD),0,3,0);
            totalScore2.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")), 0, totalReview.length(), 0);
            holder.rating.setText(totalScore2);
            String s =""+ reviewsList.size()+"تقييم";
            holder.numberOfRating.setText(s);
        }else {
            holder.headerLayout.setVisibility(View.GONE);
            holder.recycleLayout.setVisibility(View.VISIBLE);
            RatingPage.ReviewsClass Reviews = reviewsList.get(position);
            holder.userName.setText(Reviews.getUser_name());
            holder.unitName.setText(Reviews.getUnit_name());
            holder.date.setText(Reviews.getDate());
            holder.comment.setText(Reviews.getComment());
            String sScore = String.valueOf(Reviews.getScore());
            String s1 = sScore + "\n" + Reviews.getScore_text();
            SpannableString ss1 = new SpannableString(s1);
            ss1.setSpan(new RelativeSizeSpan(2f), 0, sScore.length(), 0);
            ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")), 0, sScore.length(), 0);
            holder.ratingDouble_text.setText(ss1);


        }






    }
    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        Animation animation = AnimationUtils.loadAnimation(context ,R.anim.rotation);
        animation.setInterpolator(new LinearInterpolator());
        viewHolder.loadingView.setAnimation(animation);
    }
}