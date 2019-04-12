package com.max.gathernclient.gathernclient;
// require to open
//         startingMeta = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("startingMeta")).toString();
//         meta = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("meta")).toString();
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class AllHouse extends AppCompatActivity {
    RecyclerView recyclerView;
    AllHouseAdapter adapter;
    int pageIndex = 1;
    View view1, view2, view3 , view4;
    Globals g;
    TextView bookingDate;
    String title, chalet_title, code, thumb, price, oldPrice, distance_in_km, views_count, city, areaName, serverResponse = "", meta = "";
    int id, areaCapacity, starCount;
    boolean isUnitAvailable, hasNext;
    ArrayList<AllHouseClass> allUnits = new ArrayList<>();
    boolean isLoading = false;
    String startingMeta = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_house);
        g = new Globals(this);
        initRefrance();
        getMeta();
        initAllPage(meta);
        getStartingMeta();
    }
    private void getStartingMeta() {
        startingMeta = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("startingMeta")).toString();
    }
private void initRefrance (){
    recyclerView = findViewById(R.id.AllHouse_RecyclerView);
    view1 = findViewById(R.id.view1);
    view2 = findViewById(R.id.view2);
    view3 = findViewById(R.id.view3);
    view4 = findViewById(R.id.view4);
    bookingDate = findViewById(R.id.bookingDate);

}
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageBack:
                finish();
                break;
            case R.id.nearestLocation:
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.INVISIBLE);
                view3.setVisibility(View.INVISIBLE);
                view4.setVisibility(View.INVISIBLE);
                nearestLocation();
                break;
            case R.id.lowPrice:
                view1.setVisibility(View.INVISIBLE);
                view2.setVisibility(View.VISIBLE);
                view3.setVisibility(View.INVISIBLE);
                view4.setVisibility(View.INVISIBLE);
                lowPrice();
                break;
            case R.id.highPrice:
                view1.setVisibility(View.INVISIBLE);
                view2.setVisibility(View.INVISIBLE);
                view3.setVisibility(View.INVISIBLE);
                view4.setVisibility(View.VISIBLE);
              highPrice();
                break;
            case R.id.hiRating:
                view1.setVisibility(View.INVISIBLE);
                view2.setVisibility(View.INVISIBLE);
                view3.setVisibility(View.VISIBLE);
                view4.setVisibility(View.INVISIBLE);
                hiRating();
                break;
            case R.id.imageEdit:
                startActivity(new Intent(getBaseContext(), Calender.class));
                break;
            case R.id.imageFilter:
                Intent openSearch= new Intent(getBaseContext(), Search.class);
                openSearch.putExtra("startingMeta", startingMeta);
                startActivity(openSearch);
                break;
            case R.id.call:
                Intent openTransActivity = new Intent(getBaseContext(), TransparentActivity.class);
                openTransActivity.putExtra("ID", "bottomDialog");
                startActivity(openTransActivity);
                break;
        }
    }

    @Override
    public void onBackPressed() {

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        reloadPage();
    }

    public void getDataFromServer(String meta) {
        String lat = g.getLoc("lat");
        String lng = g.getLoc("lon");
        Runnable runnable = () -> {
            String check_in = g.getCheckInDateForApi();
            String api = "api/va/chalet/search/search?" + "page=" + pageIndex+ "&check_in=" + check_in + "&lat=" + lat + "&lng=" + lng + meta;
            String result = g.connectToApi(api, null, "GET");
            runOnUiThread(() -> {
             //  Toast.makeText(getBaseContext(), "api = " + api, Toast.LENGTH_LONG).show();
                hideLoadingImage();
                serverResponse = result;
                //  Toast.makeText(getBaseContext(), "hasNext = " + hasNext, Toast.LENGTH_LONG).show();
            });
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void updateList(String response) {
        try {
            JSONObject mainObject = new JSONObject(response);
            JSONArray items = mainObject.getJSONArray("items");
            JSONObject _links = mainObject.getJSONObject("_links");
            hasNext = _links.has("next");
            for (int i = 0; i < items.length(); i++) {
                JSONObject ob = items.getJSONObject(i);
                JSONObject coverphoto = ob.getJSONObject("coverphoto");
                JSONObject address = ob.getJSONObject("address");

                id = ob.optInt("id");
                title = ob.optString("title");
                chalet_title = ob.optString("chalet_title");
                code = ob.optString("code");
                thumb = coverphoto.optString("thumb");
                areaCapacity = ob.optInt("area");
                isUnitAvailable = ob.optBoolean("isUnitAvailable");
                price = ob.optString("price");
                oldPrice = ob.optString("oldPrice");
                distance_in_km = ob.optString("distance_in_km");
                views_count = ob.optString("views_count");
                city = address.optString("city");
                areaName = address.optString("area");
                starCount = ob.optInt("starCount");
                String singleText = "كود رقم" + "\n" + code;
                String loc = city + "," + areaName;
                String km = distance_in_km.substring(0, 5);
                allUnits.add(new AllHouseClass(thumb, singleText, title, chalet_title, price, loc, starCount, km, views_count, id, isUnitAvailable));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        serverResponse = "";
    }

    private void initAdapter() {

        bookingDate.setText(g.finishedBookingDate());
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        adapter = new AllHouseAdapter(allUnits, this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemViewCacheSize(10);
        adapter.setmOnEntryClickListener((view, position) -> {

            AllHouseClass allHouse = allUnits.get(position);
            switch (view.getId()) {
                case R.id.button_book_now:
                    if (allHouse.isUnitAvailable()) {
                        if (g.getSingedState() == 1) {
                            Intent submitBooking = new Intent(getBaseContext(), SubmitBooking.class);
                            submitBooking.putExtra("ID", allHouse.getId());
                            if(allHouse.getId() > 0)
                                startActivity(submitBooking);
                            else Toast.makeText(getBaseContext(), " جرب تاريخ اخر" , Toast.LENGTH_LONG).show();
                        } else {
                            Intent open = new Intent(getBaseContext() ,MyAccount.class);
                            open.putExtra("ID",allHouse.getId());
                            startActivity(open);
                        }
                    } else {
                        startActivity(new Intent(getBaseContext(), Calender.class));
                    }
                    break;
                    default:
                    Intent open = new Intent(getBaseContext(), HouseDetails.class);
                    open.putExtra("ID", allHouse.getId());
                    startActivity(open);
                    break;
            }
        });
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
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == allUnits.size() - 1 && allUnits.size() > 0) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadMore() {
        if (hasNext) {
            pageIndex++;
            getDataFromServer(meta);
            allUnits.add(null);
            adapter.notifyItemInserted(allUnits.size() - 1);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int index = allUnits.size() - 1;
                    allUnits.remove(index);
                    adapter.notifyItemRemoved(index);
                    adapter.notifyItemRangeChanged(index, 1);
                    updateList(serverResponse);
                    adapter.notifyDataSetChanged();
                    isLoading = false;
                }
            }, 2000);
        }
    }

    private void showLoadingImage() {
        ImageView loadingImage = findViewById(R.id.loading);
        loadingImage.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotation);
        animation.setInterpolator(new LinearInterpolator());
        loadingImage.setAnimation(animation);
    }

    private void hideLoadingImage() {
        ImageView loadingImage = findViewById(R.id.loading);
        loadingImage.setAnimation(null);
        loadingImage.setVisibility(View.GONE);
    }

    private void nearestLocation() {
        meta = "&orderby=nearest";
        reloadRecyclerView(meta);
    }

    private void lowPrice() {
        meta = "&orderby=price_lowest";
        reloadRecyclerView(meta);
    }
    private void highPrice() {
        meta = "&orderby=price_highest";
        reloadRecyclerView(meta);
    }
    private void hiRating() {
        meta = "&orderby=rating";
        reloadRecyclerView(meta);
    }

    private void reloadPage() {
        finish();
        startActivity(getIntent());
    }

    private void initAllPage(String meta) {
        showLoadingImage();
        getDataFromServer(meta);
        new CountDownTimer(20000, 300) {
            boolean checked = false;

            @Override
            public void onTick(long millisUntilFinished) {
                if (!checked) {
                    if (serverResponse.length() > 0) {
                        hideLoadingImage();
                        updateList(serverResponse);
                        initAdapter();
                        initScrollListener();
                        checked = true;
                    }
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    private void reloadRecyclerView(String meta) {
        allUnits.clear();
        pageIndex = 1;
        initAllPage(meta);
    }

    private void getMeta() {
        meta = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("meta")).toString();
    }
}
