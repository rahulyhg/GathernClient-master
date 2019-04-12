package com.max.gathernclient.gathernclient;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.VectorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;

public class Test2 extends AppCompatActivity {
Globals g ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test2);
        g = new Globals(this);
        VideoView  videoView = findViewById(R.id.videoView);
//        Uri videoUri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.);
//        videoView.setVideoURI(videoUri);
//        videoView.start();
//        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.setLooping(true);
//            }
//        });

//        houseRating = findViewById(R.id.houseRating);
//        text = findViewById(R.id.text);
//        text.setText(g.getUserData("access_token"));
//        houseRating.setRating(3f);

//        LayerDrawable stars = (LayerDrawable) houseRating.getProgressDrawable();
//        stars .setDrawableByLayerId(0 , getResources().getDrawable(R.drawable.emptystar));
//       stars .setDrawableByLayerId(2 , getResources().getDrawable(R.drawable.fullstar));

//        stars .getDrawable(2).setColorFilter(Color.parseColor("#FFB400"), PorterDuff.Mode.SRC_ATOP);
//        stars .getDrawable(0).setColorFilter(Color.parseColor("#FFB400"), PorterDuff.Mode.SRC_ATOP);
//        stars .getDrawable(1).setColorFilter(Color.parseColor("#FFB400"), PorterDuff.Mode.SRC_ATOP);

    }

}
