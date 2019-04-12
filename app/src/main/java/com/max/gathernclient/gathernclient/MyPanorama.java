package com.max.gathernclient.gathernclient;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.vr.sdk.widgets.pano.VrPanoramaView;
import com.google.vr.vrcore.base.api.VrCoreUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class MyPanorama extends AppCompatActivity {
    private VrPanoramaView mVRPanoramaView;
    String url ;
            //= "https://gathern.s3.eu-central-1.amazonaws.com/1/xaudGZIAqTRoP4FKo-o-aZUtzqZD1mw3-4096x.jpg" ;
Target mTarget ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panorama);
        mVRPanoramaView = (VrPanoramaView) findViewById(R.id.vrPanoramaView);
        getId();
        getTarget();
        Picasso.with(this).load(url).into(mTarget);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mVRPanoramaView.pauseRendering();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mVRPanoramaView.resumeRendering();
    }

    @Override
    protected void onDestroy() {
        mVRPanoramaView.shutdown();
        super.onDestroy();
    }
    private void loadPhotoSphere(Bitmap bitmap) {
        VrPanoramaView.Options options = new VrPanoramaView.Options();
        options.inputType = VrPanoramaView.Options.TYPE_MONO;
        mVRPanoramaView.loadImageFromBitmap(bitmap, options);

//        InputStream inputStream ;
//        AssetManager assetManager = getAssets();
//        try {
//            inputStream = assetManager.open("sh.jpg");
//            // bitmap = BitmapFactory.decodeStream(inputStream) ;
//            inputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }
    private void getId() {
        try {
            url = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("URL")).toString();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }


    private  void getTarget(){
        mTarget = new Target(){

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                loadPhotoSphere(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageBack :
            finish();
            break;
        }
    }
}


