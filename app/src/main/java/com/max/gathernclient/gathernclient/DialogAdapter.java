package com.max.gathernclient.gathernclient;


import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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

public class DialogAdapter extends RecyclerView.Adapter<DialogAdapter.DialogHolder> {
    private List<DialogClass> mDialogClass;
    private OnEntryClickListener mOnEntryClickListener ;
    Context context ;
    public DialogAdapter(List<DialogClass> dialogList , Context context) {
        this.mDialogClass = dialogList;
        this.context = context ;
    }
    @NonNull
    @Override
    public DialogHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  row  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dialog_row, parent, false);
        return new DialogHolder(row);
    }
    @Override
    public void onBindViewHolder(@NonNull DialogHolder holder, int position) {
        DialogClass Dialog = mDialogClass.get(position);
        holder.ItemName.setText(Dialog.getItemName());
       // holder.ImageChecked.setColorFilter(ContextCompat.getColor(context,R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);

        if(!Dialog.isImageChecked()){
            holder.ImageChecked.setVisibility(View.GONE);
            holder.ItemName.setTextColor(context.getResources().getColor(R.color.myblack));

        }else {
            holder.ImageChecked.setVisibility(View.VISIBLE);
            holder.ItemName.setTextColor(context.getResources().getColor(R.color.colorPrimary));

        }
    }
    @Override
    public int getItemCount() {
        return mDialogClass.size();
    }
    class DialogHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView ItemName ;
        ImageView ImageChecked ;
        public DialogHolder(View itemView) {
            super(itemView);
            ItemName = itemView.findViewById(R.id.itemName);
            ImageChecked = itemView.findViewById(R.id.imageChecked);

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