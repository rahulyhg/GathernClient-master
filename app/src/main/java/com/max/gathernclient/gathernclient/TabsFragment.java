package com.max.gathernclient.gathernclient;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class TabsFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    FlexboxLayout flexBoxLayout ;
    ImageView imageView ;
    TextView textView ;
//    private OnEntryClickListener mOnEntryClickListener ;
    String codeNumber;
    String coverPhotoUrl ;
    ArrayList<String> galleryUrlString ;
    Globals g ;
    public TabsFragment (){
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragments_tab,container,false);
        imageView = view.findViewById(R.id.fragmentImage);
        textView = view.findViewById(R.id.codeNumber);
        flexBoxLayout = view.findViewById(R.id.flexBoxLayout);
        g = new Globals(getContext());
        codeNumber = Objects.requireNonNull(this.getArguments()).getString("codeNumber");
        coverPhotoUrl = Objects.requireNonNull(this.getArguments()).getString("coverPhotoUrl");
        galleryUrlString = Objects.requireNonNull(this.getArguments()).getStringArrayList("galleryUrlString");
        createTextView(galleryUrlString.size());
        Picasso.with(getContext()).load(coverPhotoUrl).fit().into(imageView);
        String myCode = "كود رقم"+"\n"+codeNumber;
       GradientDrawable drawable = g.shapeColorString("#CC89719D",g.getScreenDpi(5));
       textView.setBackground(drawable);
        textView.setText(myCode);
        imageView.setOnClickListener(this);
        return view ;
    }

    @Override
    public void onClick(View v) {
       // Toast.makeText(getContext(),"TEST CLICK" , Toast.LENGTH_SHORT).show();
        Intent open = new Intent(getContext(), HouseImages.class);
        open.putStringArrayListExtra("galleryUrlString",galleryUrlString);
         startActivity(open);

//        if(mOnEntryClickListener!=null){
//            mOnEntryClickListener.onEntryClick(v);
//        }
    }
//    public interface OnEntryClickListener{
//        void onEntryClick(View view);
//    }
//    public void setmOnEntryClickListener(OnEntryClickListener onEntryClickListener){
//        mOnEntryClickListener=onEntryClickListener;
//    }
    private void createTextView (int numOfViews){
        for (int i = 0 ; i<numOfViews ; i++) {
            View view = new View(getContext());
            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(g.getScreenDpi(8),g.getScreenDpi(8));
            params.setMargins(g.getScreenDpi(6),g.getScreenDpi(6),g.getScreenDpi(6),g.getScreenDpi(6));
            view.setLayoutParams(params);
            view.setBackground(g.shapeColorString("#32635F5F", g.getScreenDpi(60)));
            flexBoxLayout.addView(view);
        }
    }
}