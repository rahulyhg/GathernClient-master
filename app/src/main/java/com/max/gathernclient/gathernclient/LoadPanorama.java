package com.max.gathernclient.gathernclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.Objects;

public class LoadPanorama extends AppCompatActivity {
String url ;
WebView webView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_panorama);
        getId();
        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webView.loadUrl(url);

    }


    private void getId() {
        url = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("URL")).toString();
    }
}
