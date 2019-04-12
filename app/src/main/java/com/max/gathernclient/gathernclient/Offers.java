package com.max.gathernclient.gathernclient;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Offers extends AppCompatActivity {
    RecyclerView recyclerView ;
    Globals g ;
    OffersAdapter adapter ;
    int pageIndex = 1 ;
     ArrayList<OffersClass> endPointList = new ArrayList<>();
    boolean hasNext = false ;
    ImageView loadingImage ;
    String serverResponse ="";
    boolean isLoading = false;
    FrameLayout headerFrameLayout ;
    TextView headerText ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // overridePendingTransition(R.anim.zooming , R.anim.zooming);
        setContentView(R.layout.offers);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        g = new Globals(this);
        headerFrameLayout = findViewById(R.id.headerFrameLayout);
        headerText = findViewById(R.id.headerText);
        setFragment();
        recyclerView = findViewById(R.id.Offers_RecyclerView);
        setBookingDate ();
        showLoadingImage(R.id.loading);
        getDataFromServer();
        new CountDownTimer(20000, 300) {
            boolean checked = false ;
            @Override
            public void onTick(long millisUntilFinished) {
                if(!checked) {
                    if(serverResponse.length()>0){
                        hideLoadingImage(R.id.loading);
                        updateList(serverResponse);
                        initAdapter ();
                        initScrollListener();
                        checked = true ;
                    }
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.houses:
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                break;
            case R.id.parties:
                startActivity(new Intent(getBaseContext(), Parties.class));
                break;
            case R.id.booking:
                startActivity(new Intent(getBaseContext(), MyBooking.class));
                break;
            case R.id.more:
                startActivity(new Intent(getBaseContext(), More.class));
                break;
            case R.id.headerFrameLayout :
                startActivity(new Intent(getBaseContext(), Calender.class));
                break;

        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setBookingDate ();
        finish();
        startActivity(getIntent());
    }
    protected void setFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("Activity","Offers");
        BottomBarFragment fragment = new BottomBarFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentBottomBar, fragment);
        fragmentTransaction.commit();
    }
    public void initAdapter ( ){
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
          adapter = new OffersAdapter(endPointList , this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setmOnEntryClickListener(new OffersAdapter.OnEntryClickListener() {
            @Override
            public void onEntryClick(View view, int position) {
                OffersClass offersClass = endPointList.get(position);
                switch (view.getId()){
                    case R.id.button_book_now :
                        if(offersClass.isUnitAvailable()) {
                            if (g.getSingedState()== 1) {
                                Intent submitBooking = new Intent(getBaseContext(), SubmitBooking.class);
                                submitBooking.putExtra("ID",offersClass.getId());
                                if(offersClass.getId().length() > 0)
                                    startActivity(submitBooking);
                                else
                                    Toast.makeText(getBaseContext(), " جرب تاريخ اخر" , Toast.LENGTH_LONG).show();
                            }else{
                                Intent open = new Intent(getBaseContext() ,MyAccount.class);
                                open.putExtra("ID",offersClass.getId());
                                startActivity(open);

                            }
                        }else {
                            startActivity(new Intent(getBaseContext(), Calender.class));
                        }
                        break;
                    case R.id.headerFrame :
                        startActivity(new Intent(getBaseContext() , Calender.class));
                        break;
                        default:
                            Intent open = new Intent(getBaseContext() , HouseDetails.class) ;
                            open.putExtra("ID",offersClass.getId());
                            startActivity(open);
                            break;

                }
            }
        });
    }
    public void getDataFromServer (){
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                String check_in = g.getCheckInDateForApi() ;
                String api = "api/va/client/offers/index?page="+pageIndex+"&check_in="+check_in ;
                String result = g.connectToApi(api , null ,"GET");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadingImage(R.id.loading);
                         serverResponse =result ;
                    }
                });
            }};
        Thread thread= new Thread(runnable);
        thread.start();
    }
    private void showLoadingImage (int id){
        loadingImage = findViewById(id);
        loadingImage.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this ,R.anim.rotation);
        animation.setInterpolator(new LinearInterpolator());
        loadingImage.setAnimation(animation);
    }
    private void hideLoadingImage (int id){
        loadingImage = findViewById(id);
        loadingImage.setAnimation(null);
        loadingImage.setVisibility(View.GONE);
    }
    @Override
    public void onBackPressed() {
    }

    private void updateList(String result){
        String chalet_title ="" , title = "" , city ="" , area ="", greenPrice="" , redPrice="" , starCount= "", code="" , imageUrl = ""
                ,views_count = "" , id = "";
        int length = 0 ;
        try {
            JSONObject object = new JSONObject(result);
            JSONObject links = object.optJSONObject("_links");
            JSONArray itemArray = object.optJSONArray("items");

            length = itemArray.length() ;
            for (int i = 0 ; i<length ; i++){
                JSONObject dataObject = itemArray.optJSONObject(i);
                chalet_title =dataObject.optString("chalet_title");
                title = dataObject.optString("title");
                id = dataObject.optString("id");
                JSONObject address = dataObject.optJSONObject("address");
                city = address.optString("city");
                area = address.optString("area");
                greenPrice = dataObject.optString("price");
                redPrice = dataObject.optString("oldPrice");
                starCount = dataObject.optString("starCount");
                JSONObject  coverphoto = dataObject.optJSONObject("coverphoto");
                imageUrl = coverphoto.optString("full");
                code = dataObject.optString("code");
                views_count = dataObject.optString("views_count");
                String singleText ="كود رقم"+"\n"+code;
                String zone =city +" , "+ area ;
                String oldPrice = "السعر"+redPrice +"ريال";
                int stars = Integer.valueOf(starCount);
                boolean isUnitAvailable = dataObject.optBoolean("isUnitAvailable");
                endPointList.add(new OffersClass(singleText,chalet_title,title,greenPrice ,zone ,stars ,imageUrl , views_count, oldPrice ,isUnitAvailable , id));
            }
            hasNext =  links.has("next");
            if(hasNext)
                pageIndex++ ;
        } catch (JSONException e) {
            e.printStackTrace();
         //   Toast.makeText(getBaseContext(), "JSONException = " + e, Toast.LENGTH_LONG).show();

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
    private void loadMore() {
        if(hasNext) {
            endPointList.add(null);
            adapter.notifyItemInserted(endPointList.size() - 1);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int index = endPointList.size() -1;
                    endPointList.remove(index);
                    adapter.notifyItemRemoved(index);
                    adapter. notifyItemRangeChanged(index, 1);
                    getDataFromServer();
                    updateList(serverResponse);
                    adapter.notifyDataSetChanged();
                    isLoading = false;
                }
            }, 2000);
        }
    }
    private void setBookingDate (){
        String checkIn = g.finishedBookingDate();
        headerText.setText(checkIn);
    }
}