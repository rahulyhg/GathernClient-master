package com.max.gathernclient.gathernclient;


import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PartiesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PartiesClass> mPartiesList;
    private OnEntryClickListener mOnEntryClickListener ;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
Context context ;
    public PartiesAdapter(List<PartiesClass> PartiesList , Context context) {
        this.mPartiesList = PartiesList;
        this.context = context ;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_ITEM) {
            View row = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.parties_row, parent, false);
            return new PartiesHolder(row);
        }else{
            View row = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(row);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof PartiesHolder) {
            populateItemRows((PartiesHolder) viewHolder, position);
        }
        else if (viewHolder instanceof LoadingViewHolder)
        {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }
       }


    @Override
    public int getItemCount() {
        return mPartiesList == null ? 0 : mPartiesList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return mPartiesList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    class PartiesHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView PartiesName ,PartiesDescription ,PartiesPrice , textCall;
        ImageView  PartiesImage;
        LinearLayout baseLayout , headerLayout ;
        public PartiesHolder(View itemView) {
            super(itemView);
            PartiesName = itemView.findViewById(R.id.prts_name);
            PartiesDescription = itemView.findViewById(R.id.parties_description);
            PartiesPrice = itemView.findViewById(R.id.parties_price);
            PartiesImage = itemView.findViewById(R.id.parties_image);
            baseLayout = itemView.findViewById(R.id.baseLayout);
            headerLayout = itemView.findViewById(R.id.headerLayout);
            textCall = itemView.findViewById(R.id.textCall);
            textCall.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mOnEntryClickListener!=null){
                mOnEntryClickListener.onEntryClick(v,getLayoutPosition());
            }
        }
    }
    private class LoadingViewHolder extends RecyclerView.ViewHolder {
     //   ProgressBar progressBar;
        ImageView loadingView ;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            //progressBar = itemView.findViewById(R.id.progressBar);
            loadingView = itemView.findViewById(R.id.loading);
        }
    }
    public interface OnEntryClickListener{
        void onEntryClick(View view,int position);
    }
    public void setmOnEntryClickListener(OnEntryClickListener onEntryClickListener){
        mOnEntryClickListener=onEntryClickListener;
    }
    private void populateItemRows(PartiesHolder holder, int position) {
        PartiesClass House = mPartiesList.get(position);
        if (position == 0){
            holder.headerLayout.setVisibility(View.VISIBLE);
            holder.baseLayout.setVisibility(View.GONE);
            String s = "920007858" +"\n" + "  9:30 am: 12:30 am";
            SpannableString ss1 = new SpannableString(s);
            ss1.setSpan(new RelativeSizeSpan(2.5f), 0,10 ,0);
            holder.textCall.setText(ss1);
        }else {
            holder.headerLayout.setVisibility(View.GONE);
            holder.baseLayout.setVisibility(View.VISIBLE);
            holder.PartiesName.setText(House.getPartiesName());
            holder.PartiesDescription.setText(House.getPartiesDescription());

            // SpannableString ss1 = new SpannableString(House.getPartiesPrice());
            // ss1.setSpan(new RelativeSizeSpan(1.5f), 4,7 ,0);
            holder.PartiesPrice.setText(House.getPartiesPrice());
            Picasso.with(context).load(House.getPartiesImageUrl()).into(holder.PartiesImage);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.PartiesImage.setClipToOutline(true);
            }
        }
    }
    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        Animation animation = AnimationUtils.loadAnimation(context ,R.anim.rotation);
        animation.setInterpolator(new LinearInterpolator());
        viewHolder.loadingView.setAnimation(animation);

    }
}