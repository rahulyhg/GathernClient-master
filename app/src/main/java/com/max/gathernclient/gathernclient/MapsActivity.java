package com.max.gathernclient.gathernclient;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Globals globals;
    String lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps);
        globals = new Globals(this);
        getLatLng();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    public void onClick(View view) {
        finish();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        double lat = globals.getDoubleValueFromSqlite("userLocation", "lat");
//        double lon = globals.getDoubleValueFromSqlite("userLocation", "lon");
        double latValue = Double.valueOf(lat);
        double lngValue = Double.valueOf(lng);
        LatLng userLatlng = new LatLng(latValue, lngValue);
        mMap.addMarker(new MarkerOptions().position(userLatlng).title("My Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatlng));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatlng, 15));
        mMap.addCircle(new CircleOptions().center(userLatlng).radius(1000).strokeColor(0x9456A7).fillColor(0x339456A7));
    }

    private void getLatLng() {
        lat = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("lat")).toString();
        lng = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("lng")).toString();
    }
}