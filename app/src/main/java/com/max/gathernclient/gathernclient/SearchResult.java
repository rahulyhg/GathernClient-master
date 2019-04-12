package com.max.gathernclient.gathernclient;
// require to open
//         meta = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("meta")).toString();
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.FontRequest;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class SearchResult extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    RecyclerView recyclerView;
    Spinner spinner ;
    SearchResultAdapter adapter;
    int pageIndex = 1;
    View view1, view2, view3  , view4;
    Globals g;
    FrameLayout frameLayoutSpinner ;
    TextView bookingDate , imageFilter , textSpinner;
    String title, chalet_title, code, thumb, price, oldPrice,
            distance_in_km, views_count, city, areaName , poolType ,text,personsCapacity, serverResponse = "", meta = "";
    int id, areaCapacity, starCount , general_persons_capacity ;
    double points ;
    boolean isUnitAvailable, hasNext;
    ArrayList<SearchResultClass> allUnits = new ArrayList<>();
    boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);
        g = new Globals(this);
        initRefrance();
        initSpinner();
        getMeta();
        initAllPage(meta);

    }


    private void initRefrance (){
        spinner = findViewById(R.id.spinner);
        recyclerView = findViewById(R.id.AllHouse_RecyclerView);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);
        view4= findViewById(R.id.view4);
        imageFilter = findViewById(R.id.imageFilter);
        bookingDate = findViewById(R.id.bookingDate);
        textSpinner = findViewById(R.id.textSpinner);
        frameLayoutSpinner = findViewById(R.id.frameLayoutSpinner);
        // set Shapes
        Drawable drawable = g.shape(R.color.mywhite ,g.getScreenDpi(5),1,R.color.mydarkgray) ;
        imageFilter.setBackground(drawable);
        frameLayoutSpinner.setBackground(drawable);
    }
    private void initSpinner (){
        String[] items = new String[]{"ترتيب","الأقرب لموقعي", "أقل سعر", "أعلى سعر" , "الأعلى تقييماً"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, items);
        spinner.setAdapter(adapter);
//        int initialSelectedPosition=spinner.getSelectedItemPosition();
//        spinner.setSelection(initialSelectedPosition, false); //clear selection
        spinner.setOnItemSelectedListener(this);

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
                startActivity(new Intent(getBaseContext(), Filter.class));
                break;
            case R.id.call:
                Intent openTransActivity = new Intent(getBaseContext(), TransparentActivity.class);
                openTransActivity.putExtra("ID", "bottomDialog");
                startActivity(openTransActivity);
                break;
            case R.id.textSpinner :
                spinner.performClick();
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
            String api = "api/va/chalet/search/search?" + "page=" + pageIndex+ "&check_in=" + check_in + "&lat=" + lat + "&lng=" + lng + meta +"&expand=chalet,options";
        // String api = "api/vb/main/appdata";
            String result = g.connectToApi(api, null, "GET");
            runOnUiThread(() -> {
                //  Toast.makeText(getBaseContext(), api, Toast.LENGTH_LONG).show();
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
                JSONObject quickOptions = ob.optJSONObject("quickOptions");
                JSONObject options = ob.optJSONObject("options");
                JSONObject main = options.optJSONObject("main");
                JSONObject chalet = ob.optJSONObject("chalet");
                JSONObject totalReview =chalet.optJSONObject("totalReview");
                JSONArray boxGallery = ob.optJSONArray("boxGallery");

                ArrayList<String> boxGalleryUrl = new ArrayList<>();
                for (int x = 0;x <boxGallery.length();x++ ){
                    boxGalleryUrl.add(boxGallery.optString(x));
                }
                points = totalReview.optDouble("points");
                text = totalReview.optString("text");

                general_persons_capacity = main.optInt("general_persons_capacity");

                poolType = quickOptions.optString("poolType");

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
                String km = distance_in_km.substring(0, 5);
                allUnits.add(new SearchResultClass(thumb, singleText, chalet_title ,title, price, city
                        ,areaName, starCount, km, views_count, id, isUnitAvailable ,poolType
                        ,String.valueOf(general_persons_capacity),text ,points , boxGalleryUrl));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        serverResponse = "";
    }

    private void initAdapter() {
        ////////////////
        bookingDate.setText(g.finishedBookingDate());
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        adapter = new SearchResultAdapter(allUnits, this  );
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
       // recyclerView.setItemViewCacheSize(10);
        recyclerView.getRecycledViewPool().clear();
        adapter.setmOnEntryClickListener((view, position) -> {

            SearchResultClass resultClass = allUnits.get(position);

          //  Toast.makeText(getBaseContext(), "size ="+resultClass.getBoxGalleryUrl().size(), Toast.LENGTH_LONG).show();

            Intent open = new Intent(getBaseContext(), HouseDetails.class);
            open.putExtra("ID", resultClass.getId());
            startActivity(open);
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
     TextView cur = (TextView) view;
        textSpinner .setText(cur.getText().toString());
        switch (position){
            case 0 :
                textSpinner .setText(cur.getText().toString());

                break;
            case 1 :
                nearestLocation();
                break;
            case 2 :
                lowPrice();
                break;
            case 3 :
                highPrice();
                break;
            case 4 :
                hiRating();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
