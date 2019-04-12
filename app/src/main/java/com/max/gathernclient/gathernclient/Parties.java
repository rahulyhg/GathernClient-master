package com.max.gathernclient.gathernclient;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Parties extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageView loadingImage;
    Globals g;
    String serverResponse = "";
    int pageIndex = 1;
    final ArrayList<PartiesClass> endPointList = new ArrayList<>();
    boolean hasNext = false;
    PartiesAdapter adapter;
    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        g = new Globals(this);
       // overridePendingTransition(R.anim.zooming, R.anim.zooming);
        setContentView(R.layout.parties);
        showLoadingImage(R.id.loading);
        recyclerView = findViewById(R.id.parties_recyclerview);
        setFragment();
        getDataFromServer();


        new CountDownTimer(20000, 300) {
            boolean checked = false;

            @Override
            public void onTick(long millisUntilFinished) {
                if (!checked) {
                    if (serverResponse.length() > 0) {
                        hideLoadingImage(R.id.loading);
                        updateList(serverResponse);
                        initAdapter();
                        initScrollListener();
                        checked = true;
                    }
                }
            }

            @Override
            public void onFinish() {
                // Toast.makeText(getBaseContext(),serverResponse,Toast.LENGTH_LONG).show();

            }
        }.start();

    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.houses:
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                break;

            case R.id.offers:
                startActivity(new Intent(getBaseContext(), Offers.class));
                break;
            case R.id.booking:
                startActivity(new Intent(getBaseContext(), MyBooking.class));
                break;
            case R.id.more:
                startActivity(new Intent(getBaseContext(), More.class));
                break;

        }
    }

    protected void setFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("Activity", "Parties");
        BottomBarFragment fragment = new BottomBarFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentBottomBar, fragment);
        fragmentTransaction.commit();
    }

    public void getDataFromServer() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String api = "api/va/client/party/index?page=" + pageIndex;
                String result = g.connectToApi(api, null, "GET");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        serverResponse = result;
                    }
                });
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void initAdapter() {
        adapter = new PartiesAdapter(endPointList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setmOnEntryClickListener(new PartiesAdapter.OnEntryClickListener() {
            @Override
            public void onEntryClick(View view, int position) {
                // Toast.makeText(getBaseContext(),"position = "+position+"List size = "+endPointList.size(),Toast.LENGTH_SHORT).show();
                switch (view.getId()) {
                    case R.id.textCall:
                        Intent call = new Intent(Intent.ACTION_DIAL);
                        call.setData(Uri.parse("tel:" + "920007858"));
                        startActivity(call);
                        break;
                }
            }
        });
    }

    private void showLoadingImage(int id) {
        loadingImage = findViewById(id);
        loadingImage.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotation);
        animation.setInterpolator(new LinearInterpolator());
        loadingImage.setAnimation(animation);
    }

    private void hideLoadingImage(int id) {
        loadingImage = findViewById(id);
        loadingImage.setAnimation(null);
        loadingImage.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
    }

    private void loadMore() {
        if (hasNext) {
            endPointList.add(null);
            adapter.notifyItemInserted(endPointList.size() - 1);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int index = endPointList.size() - 1;
                    endPointList.remove(index);
                    adapter.notifyItemRemoved(index);
                    adapter.notifyItemRangeChanged(index, 1);
                    getDataFromServer();
                    updateList(serverResponse);
                    adapter.notifyDataSetChanged();
                    isLoading = false;
                }
            }, 2000);
        }
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == endPointList.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });


    }

    private void updateList(String result) {
        int length = 0;
        String name = "", price = "", desc = "", photo = "";
        try {
            JSONObject object = new JSONObject(result);
            JSONObject links = object.getJSONObject("_links");
            JSONArray itemArray = object.getJSONArray("items");
            length = itemArray.length();

            for (int i = 0; i < length; i++) {
                JSONObject dataObject = itemArray.getJSONObject(i);
                name = dataObject.getString("name");
                desc = dataObject.getString("desc");
                price = dataObject.getString("price");
                photo = dataObject.getString("photo");
                endPointList.add(new PartiesClass(name, desc, price, photo));
            }
            hasNext = links.has("next");
            if (hasNext)
                pageIndex++;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
