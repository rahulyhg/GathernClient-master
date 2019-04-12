package com.max.gathernclient.gathernclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

public class HouseImages extends AppCompatActivity {
RecyclerView recyclerView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_images);
        recyclerView = findViewById(R.id.imageRecyclerView);
        ArrayList<String> imageUrlString = getArrayListString();
        ArrayList<BannerClass> arrayList = new ArrayList<>();
        for (int i = 0 ;i <imageUrlString.size() ;i++) {
            // add width =0 & height = 0 for match screen
            arrayList.add(new BannerClass( imageUrlString.get(i),0,0));
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this , LinearLayoutManager.VERTICAL ,false);
        BannerAdapter adapter = new BannerAdapter(arrayList , this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageBack :
                finish();
                break;
        }
    }
    public ArrayList<String> getArrayListString (){
        ArrayList<String> arrayList = new ArrayList<>();
        if (getIntent().getExtras() !=null) {
            try {
                arrayList = getIntent().getStringArrayListExtra("galleryUrlString");
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
        return arrayList ;
    }
}
