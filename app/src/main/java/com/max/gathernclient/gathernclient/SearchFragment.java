package com.max.gathernclient.gathernclient;


import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.Objects;

public class SearchFragment extends Fragment implements View.OnClickListener {
    Globals g ;
    TinyDB tinyDB ;
    ArrayList<String> savedList ;
    android.support.v7.widget.GridLayout  gridLayout ;
    public OnFragListener mOnFragListener ;
    Drawable drawableCheked ;
    Drawable drawableUnCheked ;
    public SearchFragment(){
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_fragment,container,false);
        gridLayout = rootView.findViewById(R.id.parentLayout);
        g = new Globals(getContext());
        tinyDB = new TinyDB(getContext());
        savedList = tinyDB.getListString("savedList");
        //Toast.makeText(getContext(),savedList.toString(),Toast.LENGTH_LONG).show();
        drawableCheked = g.shape(R.color.colorPrimary ,g.getScreenDpi(5) ,0,0);
        drawableUnCheked =g.shape(R.color.mywhite ,g.getScreenDpi(5) ,1,R.color.mydarkgray);


         ArrayList<String> searchItems = this.getArguments() != null ? this.getArguments().getStringArrayList("List") : new ArrayList<>();
        ArrayList<String> searchItems_ids = this.getArguments() != null ? this.getArguments().getStringArrayList("List_ids") : new ArrayList<>();

        String kind = this.getArguments() != null ? this.getArguments().getString("Kind") : "";

        for(int i = 0; i< (searchItems != null ? searchItems.size() : 0); i++) {

            ImageView imageView = new ImageView(getContext());
            imageView.setImageResource(R.drawable.done);
            imageView.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.mywhite), PorterDuff.Mode.SRC_IN);
            imageView.setOnClickListener(this);
            imageView.setPadding(g.getScreenDpi(2),g.getScreenDpi(2),g.getScreenDpi(2),g.getScreenDpi(2));
            LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(g.getScreenDpi(24) , g.getScreenDpi(24));
            params.setMargins(g.getScreenDpi(4),g.getScreenDpi(4),g.getScreenDpi(4),g.getScreenDpi(4));
            imageView.setLayoutParams(params);


            MyTextView textView  = new MyTextView(getContext());
            textView.setText(searchItems.get(i));
            textView.setMinWidth(g.getScreenDpi(100));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP , 12f);
            textView.setPadding(g.getScreenDpi(4),g.getScreenDpi(4),g.getScreenDpi(4),g.getScreenDpi(4));
            int colorUnCheked = Objects.requireNonNull(getContext()).getResources().getColor(R.color.textBlackColor);
             textView.setTextColor(colorUnCheked);



            imageView.setTag(R.id.kind,kind);
            imageView.setTag(R.id.serch_ids, searchItems_ids != null ? searchItems_ids.get(i) : "");
            String currentTag = imageView.getTag(R.id.kind).toString() + imageView.getTag(R.id.serch_ids).toString() ;
           // String test = "&unit_reviews=8%2B";
            if (tinyDB.getListString("savedList").contains(currentTag))
                imageView.setTag("Selected");
            else
                imageView.setTag("unSelected");

if(imageView.getTag().equals("unSelected"))
            imageView.setBackground(drawableUnCheked);
else
    imageView.setBackground(drawableCheked);

//            textView.setTag(R.id.tag3,"key 3");


            LinearLayout mHorLayout = new LinearLayout(getContext());
            mHorLayout.setOrientation(LinearLayout.HORIZONTAL);
            mHorLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

            mHorLayout.addView(imageView);
            mHorLayout.addView(textView);

            gridLayout.addView(mHorLayout);


        }
        return rootView ;
    }

    @Override
    public void onClick(View v) {

        if (v.getTag().toString().equals("unSelected")) {
                v.setBackground(drawableCheked);
                v.setTag("Selected");
            } else {
                v.setBackground(drawableUnCheked);
                v.setTag("unSelected");
            }

        if(mOnFragListener !=null){
            mOnFragListener.onEntryClick(v);
        }
    }
    public interface OnFragListener {
        void onEntryClick(View view);
    }
    public void myListener (OnFragListener onFragListener){
        mOnFragListener = onFragListener ;

    }
}