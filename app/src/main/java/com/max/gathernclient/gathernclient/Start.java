package com.max.gathernclient.gathernclient;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;

import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.payfort.fort.android.sdk.base.SdkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class Start extends AppCompatActivity implements com.google.android.gms.location.LocationListener,
        GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{
MixpanelAPI mixpanel ;
Globals g ;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    LocationRequest mLocationRequest;
    double lat=0;
    double lon=0;
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    private static final long TIME_TO_START =  1500 ;
    private static final long TICK_INTERVAL =  1000 ;
    private static final int FORCE_UPDATE = 0 ;
    private static final int OPTINAL_UPDATE = 1 ;

long leftTime = 0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        g = new Globals(this);
        // count time left
        new CountDownTimer(2000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(leftTime < 2000)
                leftTime+=100;
                else
                    leftTime = 2000 ;
            }

            @Override
            public void onFinish() {

            }
        }.start();
       initAllPage();
    }
    private void initAllPage(){
        //this.deleteDatabase("SHDB");
        initVideo();
        g.firstTimeDataBase();
        check_for_gps();
        createLocationRequest();
        createApi();
        if (g.isInternetConnected(this)) {
            startMixPanel();
            getAppDataFromApi();
            //getBootStrapFromApi();
        }else
         Toast.makeText(getBaseContext(), "تحقق من إتصال الإنترنت", Toast.LENGTH_SHORT).show();

    }
    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("يرجي تفعيل GPS أولاً")
                .setCancelable(false)
                .setPositiveButton("تفعيل الآن",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("إلغاء",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                       finishAffinity();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();

    }
    protected void createLocationRequest() {
        mLocationRequest =  LocationRequest.create();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }
    protected void check_for_gps(){
        LocationManager  locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)&&
                ! locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            showGPSDisabledAlertToUser();
        }
    }
    private void get_location_and_enter() {
        new CountDownTimer(TIME_TO_START +100 - leftTime, 10) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (null != mCurrentLocation) {
                    lat = mCurrentLocation.getLatitude();
                    lon = mCurrentLocation.getLongitude();
                    g.insertUserLocation(lat , lon);
                    // Toast.makeText(getBaseContext(), "DONE", Toast.LENGTH_SHORT).show();
                    startActivity( new Intent(Start.this, HomePage.class));
                }
            }
        }.start();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mGoogleApiClient.connect();
        initAllPage();
//        initVideo();
//        get_location_and_enter();
    }

    public void createApi() {
        if ( mGoogleApiClient ==null) {

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }
    protected void startLocationUpdates() {


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED
                &&ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},1);

        }else {
            mCurrentLocation=LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(mCurrentLocation==null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,  this);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       if (requestCode == 1){
           if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
               startLocationUpdates();
               initAllPage();
           }else
           {
               finish();
           }
       }
    }// end part

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient,  this);

    }



    @Override
    protected void onStart(){
        mGoogleApiClient.connect();
        super.onStart();

    }

    @Override
    protected void onStop(){
        super.onStop();
        mGoogleApiClient.disconnect();


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
        initAllPage();
        //get_location_and_enter();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        initAllPage();
        //get_location_and_enter();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }


    @Override
    protected void onResume(){
        super.onResume();


    }
private void startMixPanel(){
    mixpanel = MixpanelAPI.getInstance(this, g.MIXPANEL_TOKEN());
    String deviceId= g.getDeviceId();
    mixpanel.alias(deviceId ,deviceId);
    mixpanel.identify(deviceId);
    mixpanel.getPeople().identify(deviceId);
}
//    public void getJsonMetaFromApi (){
//
//        Runnable runnable = () ->{
//            String result = g.connectToApi("api/va/chalet/search/meta",null , "GET");
//            runOnUiThread(()->{
//                try {
//                    JSONObject mainObject= new JSONObject(result);
//                    JSONArray cities = mainObject.optJSONArray("cities");
//                    JSONObject prices = mainObject.optJSONObject("prices");
//                    g.setJsonMeta("cities",cities.toString());
//                    g.setJsonMeta("prices",prices.toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            });
//        };
//        Thread thread = new Thread(runnable);
//        thread.start();
//    }
    public void getAppDataFromApi (){
        String versionName = "";
//        try {
//            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(),0);
//            versionName = pInfo.versionName ;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        String finalVersionName = versionName;
        Runnable runnable = () ->{
//            JSONObject param = new JSONObject();
//            try {
//                param.put("platform","Android Native");
//                param.put("version", finalVersionName);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            String result = g.connectToApi("api/vb/main/appdata",null , "GET");
            runOnUiThread(()->{
             //   Toast.makeText(getBaseContext(), "result ="+result, Toast.LENGTH_SHORT).show();

                try {
                    JSONObject ob =new JSONObject(result);
                    boolean success = ob.getBoolean("success");
                    if(success) {
                         g.setAppData("allData" ,result);
                         g.setAppData("dataVersion" ,"1");
                         if(result.length() >0)
                         getBootStrapFromApi();
                      //  Toast.makeText(getBaseContext(), "success get App Data", Toast.LENGTH_SHORT).show();

                    }else{
                       // Toast.makeText(getBaseContext(), "Error get App Data", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    public void getBootStrapFromApi (){
        String versionName = "";
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(),0);
            versionName = pInfo.versionName ;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String finalVersionName = versionName;

        Runnable runnable = () ->{
            JSONObject param = new JSONObject();
            try {
                param.put("platform","Android Native");
                param.put("version", finalVersionName);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            String result = g.connectToApi("api/vb/main/bootstrap",null , "GET");
            runOnUiThread(()->{
                try {
                    JSONObject ob = new JSONObject(result);
                    boolean success = ob.getBoolean("success");
                    boolean require_updates = ob.getBoolean("require_updates");
                    boolean force_update = ob.getBoolean("force_update");
                   // boolean require_review = ob.optBoolean("require_review");
                    JSONObject require_review_object = ob.optJSONObject("require_review");
                    if(success){
                        if(force_update){
                              // force to update or exit app
                            CustomDialog customDialog = new CustomDialog(this , FORCE_UPDATE);
                            customDialog.show();
                       }else if(require_updates){
                            // optional update
                            CustomDialog customDialog = new CustomDialog(this , OPTINAL_UPDATE);
                            customDialog.show();
                        }else {
                            // direct enter
                            if(require_review_object == null) {
                                get_location_and_enter();
                            }
                            else
                               mustReview(require_review_object);

                        }
                    }else{
                        // failed connect to API check connection
                        Toast.makeText(getBaseContext(), "تحقق من إتصال الإنترنت ", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                      Toast.makeText(getBaseContext(), "تحقق من إتصال الإنترنت ", Toast.LENGTH_SHORT).show();

                }
                //  Toast.makeText(getBaseContext(), "BootStrap = "+result, Toast.LENGTH_SHORT).show();

            });
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void mustReview(JSONObject review) {
        String imageUrl = review.optString("chaletCover");
        String chaletName = review.optString("chaletName");
        String unitName = review.optString("unitName");
        String check_in_label = review.optString("check_in_label");// اليوم
        String check_in = review.optString("check_in");//التاريخ
        int total = review.optInt("total");
        int id = review.optInt("id");


        Intent openReview = new Intent(Start.this, Reviews.class);
        openReview.putExtra("imageUrl",imageUrl);
        openReview.putExtra("chaletName",chaletName);
        openReview.putExtra("unitName",unitName);
        openReview.putExtra("check_in_label",check_in_label);
        openReview.putExtra("check_in",check_in);
        openReview.putExtra("total",total);
        openReview.putExtra("id",id);
        startActivity(openReview);



    }

    private void initVideo (){
        /*
        SimpleExoPlayerView videoView = findViewById(R.id.videoView);
        Uri videoUri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.splash_video);
        DefaultTrackSelector trackSelector = new DefaultTrackSelector();

        SimpleExoPlayer player = ExoPlayerFactory.
                newSimpleInstance(this ,trackSelector);
        videoView.setPlayer(player);
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "gathernclient"));
// This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(videoUri);
// Prepare the player with the source.
        player.prepare(videoSource);
*/

VideoView videoView = findViewById(R.id.videoView);
        Uri videoUri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.splash_video_keep);
        videoView.setVideoURI(videoUri);
        videoView.start();
        videoView.setOnPreparedListener(mp ->
                mp.setLooping(true));
    }
    private class CustomDialog extends Dialog implements View.OnClickListener{
        TextView positiveButton, negativeButton , title;
        RecyclerView dialogRecyclerView;
        int dialogType;

        public CustomDialog(Context context, int dialogType) {
            super(context);
            this.dialogType = dialogType;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.start_dialog);
            Objects.requireNonNull(getWindow()).setLayout(g.getScreenDpi(240), LinearLayout.LayoutParams.WRAP_CONTENT);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            positiveButton = findViewById(R.id.positiveButton);
            negativeButton = findViewById(R.id.negativeButton);
            title = findViewById(R.id.title);
            setTitle();
            this.setCancelable(false);
            dialogRecyclerView = findViewById(R.id.dialogRecyclerView);
            negativeButton.setOnClickListener(this);
            positiveButton.setOnClickListener(this);
        }
        private void setTitle(){
            switch (dialogType){
                case FORCE_UPDATE :
                    title.setText("هذة النسخة من التطبيق لم تعد مدعومة يرجي تحديث التطبيق");
                   break;
                case OPTINAL_UPDATE :
                    title.setText("للحصول علي أفضل أداء يرجي تحديث التطبيق");
                    break;
            }
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.positiveButton:
                  //    Toast.makeText(getBaseContext(), "Google Play Link ", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.negativeButton:
                    switch (dialogType){
                        case FORCE_UPDATE :
                            finishAffinity();
                            break;
                        case OPTINAL_UPDATE :
                            get_location_and_enter();
                            break;
                    }
                    finishAffinity();
                    break;
            }
        }

    }


}
