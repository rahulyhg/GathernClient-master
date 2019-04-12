package com.max.gathernclient.gathernclient;


import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchHolder> {
    Context context ;
    private String[] mSearchList;
    private OnEntryClickListener mOnEntryClickListener ;

    public SearchAdapter(String[] SearchList , Context context) {
        this.mSearchList = SearchList;
        this.context = context ;
    }


    @NonNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  row  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_row, parent, false);
        return new SearchHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHolder holder, int position) {
        //SearchClass Search = mSearchList[position];
        holder.TextItem.setText(mSearchList[position]);
        holder.TextItem.setTag("unSelected");


//        if(!Search.isTextChecked()){
//            holder.TextItem.setBackground(drawableUnCheked);
//            holder.TextItem.setTextColor(colorUnCheked);
//
//        }else {
//            holder.TextItem.setBackground(drawableCheked);
//            holder.TextItem.setTextColor(colorCheked);
//        }
    }


    @Override
    public int getItemCount() {

        return mSearchList.length;
    }

    class SearchHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView TextItem ;

        public SearchHolder(View itemView) {
            super(itemView);
            TextItem = itemView.findViewById(R.id.textItem);
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