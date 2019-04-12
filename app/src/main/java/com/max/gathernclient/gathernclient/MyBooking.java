package com.max.gathernclient.gathernclient;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyBooking extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView TitleText , bookingText;
    LinearLayout linearLayout;
    int signState = 0;
    Globals g ;
    final ArrayList<BookingClass> bookingList = new ArrayList<>();
    boolean isLoading = false;
    boolean hasNext = false ;
    int pageIndex = 1 ;
    BookingAdapter adapter ;
    String serverResponse = "" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_booking);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        // overridePendingTransition(R.anim.zooming , R.anim.zooming);
        g = new Globals(this);
        setFragment();
        checkForSignedState();
        initAllPage();
    }
    private void initAllPage(){
        getDataFromServer ();
        new CountDownTimer(20000, 300) {
            boolean checked = false ;
            @Override
            public void onTick(long millisUntilFinished) {
                if(!checked) {
                    if(serverResponse.length()>0){
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
    private void checkForSignedState(){
    linearLayout = findViewById(R.id.layoutContainer);
    TitleText = findViewById(R.id.titleText);
        recyclerView = findViewById(R.id.recyclerView);
        signState = g.getSingedState();
    if (signState == 0) {
        recyclerView.setVisibility(View.GONE);
        linearLayout.setVisibility(View.VISIBLE);
        TitleText.setText("تسجيل الدخول");
    } else if (signState == 1) {
        recyclerView.setVisibility(View.VISIBLE);
        linearLayout.setVisibility(View.GONE);
        TitleText.setText("حجوزاتي");
    }
}
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signIn:
                Intent open = new Intent(getBaseContext() ,MyAccount.class);
                open.putExtra("ID","");
                startActivity(open);
                break;
            case R.id.imageBack:
                finish();
                break;
        }
    }
    @Override
    public void onBackPressed() {
    }
    protected void setFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("Activity","MyBooking");
        BottomBarFragment fragment = new BottomBarFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentBottomBar, fragment);
        fragmentTransaction.commit();
    }
    private void initAdapter(){
//        for (int i = 1; i < 32; i++) {
//            bookingList.add(new BookingClass(R.drawable.altscreen, "سعد , "
//                    , "شالية الثالث", "منتهي الصلاحية ", "2018-10" , "18"));
//        }
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
         adapter = new BookingAdapter(bookingList , this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setmOnEntryClickListener(new BookingAdapter.OnEntryClickListener() {
            @Override
            public void onEntryClick(View view, int position) {
                BookingClass bookingClass = bookingList.get(position);

                switch (view.getId()){
                    case R.id.bookAgain :
                        Intent houseDetails = new Intent(getBaseContext(), HouseDetails.class);
                        houseDetails.putExtra("ID",bookingClass.getUnit_id());
                        startActivity(houseDetails);
                   break;
                   default:
                       if(bookingClass.isConfirm() && !bookingClass.isLastCheckOut()){
                           Intent congrat =  new Intent(getBaseContext() , Congratulations.class);
                           congrat.putExtra("reservation_id", bookingClass.getReservation_id());
                           startActivity(congrat);
                       }else {
                           Intent reservation_id = new Intent(getBaseContext(), ReservationDetails.class);
                           reservation_id.putExtra("reservation_id", bookingClass.getReservation_id());
                           startActivity(reservation_id);
                       }
                }

            }
        });
    }

    public void getDataFromServer (){
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                String api = "api/va/client/reservation/index?page="+pageIndex +"&expand=chalet" ;
                String result = g.connectToApi(api , null ,"GET");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        serverResponse = result ;
                    }
                });
            }};
        Thread thread= new Thread(runnable);
        thread.start();
    }
    private void updateList (String result){
        String  imgUrl , unitName , chaletName , check_in , statusText , unit_id , id ;
        boolean isUnitPublished ;
        int status ;
        try {
            JSONObject ob = new JSONObject(result);
            JSONArray items = ob.optJSONArray("items");
           JSONObject links = ob.optJSONObject("_links");

            for (int i = 0 ; i<items.length();i++){
                check_in = items.optJSONObject(i).optString("check_in");
                unit_id =  items.optJSONObject(i).optString("unit_id");
                imgUrl =  items.optJSONObject(i).optJSONObject("chalet").optJSONObject("coverphoto").optString("thumb");
                chaletName =items.optJSONObject(i).optString("chaletName");
                unitName = items.optJSONObject(i).optString("unitName");
                statusText = items.optJSONObject(i).optString("statusText");
                isUnitPublished = items.optJSONObject(i).optBoolean("isUnitPublished");
                String lastTowNumber = check_in.substring(8);
                id = items.optJSONObject(i).optString("id");
                status = items.optJSONObject(i).optInt("status");
                bookingList.add(new BookingClass(imgUrl ,chaletName ,unitName ,statusText
                        ,check_in ,lastTowNumber , isUnitPublished , unit_id , id  ,status));
            }
            hasNext =  links.has("next");
            if(hasNext)
                pageIndex++ ;
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "e = " + e, Toast.LENGTH_LONG).show();

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
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == bookingList.size() - 1) {
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
            bookingList.add(null);
            adapter.notifyItemInserted(bookingList.size() - 1);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int index = bookingList.size() -1;
                    bookingList.remove(index);
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

    @Override
    protected void onRestart() {
        super.onRestart();
        checkForSignedState();
        initAllPage();
    }
}
