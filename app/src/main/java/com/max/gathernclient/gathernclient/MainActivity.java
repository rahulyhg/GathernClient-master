package com.max.gathernclient.gathernclient;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.onesignal.OneSignal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    RecyclerView imgRecyclerView, txtRecyclerView1, txtRecyclerView2, txtRecyclerView3,
            txtRecyclerView4, txtRecyclerView5;
    ScrollView scrollView;
    Globals g;
    TextView BookingDate, ShowAllHouse, City, Price, enterCode, enterName;
    String priceMeta, cityMeta;
    DialogClass dialogClass;
    final private static int CITY = 1;
    final private static int PRICES = 2;

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.zooming, R.anim.zooming);
        setContentView(R.layout.activity_main);
        //  Toast.makeText(getBaseContext(), "onCreate", Toast.LENGTH_SHORT).show();
        g = new Globals(this);
        BookingDate = findViewById(R.id.bookingtime);
        g.InitializeMixPanel("Home Page");
        ShowAllHouse = findViewById(R.id.showAllHouse);
        City = findViewById(R.id.choseCity);
        Price = findViewById(R.id.chosePrice);
        scrollView = findViewById(R.id.scrollView);
        enterCode = findViewById(R.id.enterCode);
        enterName = findViewById(R.id.enterName);

        Bundle param = new Bundle();
        param.putString("checkin ", g.getCheckInDateForApi());
        param.putString("platform", "Android Native");
        g.facebookPixel("Home Page", param);
        oneSignal();
//        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//            @Override
//            public void onScrollChanged() {
//                String s = ""+scrollView.getScrollY() ;
//            }
//        });
        initBanner();
        initRecycler1();
        initRecycler2();
        initRecycler3();
        initRecycler4();
        initRecycler5();
        setFragment();
        setDate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //   Toast.makeText(getBaseContext(), "onStart", Toast.LENGTH_SHORT).show();

    }

    public void onClickAdapters(HouseAdapter houseAdapter) {
        houseAdapter.setmOnEntryClickListener(new HouseAdapter.OnEntryClickListener() {
            @Override
            public void onEntryClick(View view, int position) {
                //Toast.makeText(getBaseContext(),""+position,Toast.LENGTH_SHORT).show();
                Intent open = new Intent(getBaseContext(), HouseDetails.class);
                open.putExtra("ID", "19");
                startActivity(open);
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.showAllHouse:
//                ObjectAnimator anim = ObjectAnimator.ofInt(scrollView , "ScrollY", 500);
//                 anim.setDuration(3000);
//                 anim.start();
                // Toast.makeText(getBaseContext(),""+g.getJsonMeta("cities"),Toast.LENGTH_SHORT).show();
            //    showAllUnits();
                 startActivity(new Intent(getBaseContext(), HomePage.class));

                break;
            case R.id.search:
                  // startActivity(new Intent(getBaseContext(), HomePage.class));

                  startActivity(new Intent(getBaseContext(), Search.class));
                break;
            case R.id.search_2:
                Bundle param = new Bundle();
                param.putString("checkin ", g.getCheckInDateForApi());
                param.putString("platform", "Android Native");
                g.facebookPixel("Search Primary", param);
                sendMetaLink();
                break;
            case R.id.choseCity:
                CustomDialog cityDialog = new CustomDialog(this, getCitiesNames(), CITY);
                cityDialog.show();
                break;
            case R.id.chosePrice:
                CustomDialog priceDialog = new CustomDialog(this, getPriceList(), PRICES);
                priceDialog.show();
                break;
            case R.id.bookingtime:
                startActivity(new Intent(getBaseContext(), Calender.class));
                break;

        }
    }

    private void setDate() {
        String a = "موعد الحجز :";
        String b = g.getBookingDate("dayNumber") + "";
        String c = g.getBookingDate("dayText") + ",";
        String d = g.getBookingDate("month") + ",";
        String e = g.getBookingDate("year") + "";
        String f = a + c + b + d + e;
        BookingDate.setText(f);
        City.setText("كل المدن");
        City.setTag("&city=0");
        Price.setText("كل الأسعار");
        Price.setTag("&price=0-15000");
        cityMeta = City.getTag().toString();
        priceMeta = Price.getTag().toString();

    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setDate();
        //  Toast.makeText(getBaseContext(), "onRestart", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Toast.makeText(getBaseContext(), "onDestroy", Toast.LENGTH_SHORT).show();
        System.runFinalization();
        System.gc();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Toast.makeText(getBaseContext(), "onPause", Toast.LENGTH_SHORT).show();
        System.runFinalization();
        System.gc();

    }

    @Override
    protected void onStop() {
        super.onStop();
        // Toast.makeText(getBaseContext(), "onStop", Toast.LENGTH_SHORT).show();
        System.runFinalization();
        System.gc();
    }

    public SpannableString getPrice(String price) {
        String s = "السعر " + price + "ريال";
        SpannableString ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.5f), 6, 6 + price.length(), 0);
        ss1.setSpan(new StyleSpan(Typeface.BOLD), 6, 6 + price.length(), 0);
//        ss1.setSpan(new ForegroundColorSpan(Color.RED),4,6,0);
        return ss1;
    }

    protected void setFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("Activity", "MainActivity");
        BottomBarFragment fragment = new BottomBarFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentBottomBar, fragment);
        fragmentTransaction.commit();
    }

    private void oneSignal() {
        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }

    private void sendMetaLink() {
        String name = enterName.getText().toString();
        String code = enterCode.getText().toString();
        String meta = "&unit_code=" + code + "&query=" + name + cityMeta + priceMeta;
       // Toast.makeText(getBaseContext(), meta, Toast.LENGTH_SHORT).show();

        Intent open = new Intent(getBaseContext(), AllHouse.class);
        open.putExtra("meta", meta);
        startActivity(open);

    }

    private void showAllUnits() {
        Intent open = new Intent(getBaseContext(), AllHouse.class);
        open.putExtra("meta", "");
        startActivity(open);

    }

    private class CustomDialog extends Dialog implements View.OnClickListener {
        TextView positiveButton, negativeButton, title;
        RecyclerView dialogRecyclerView;
        ArrayList<Search.ItemNameMask> arrayList;
        int dialogType;

        public CustomDialog(Context context, ArrayList<Search.ItemNameMask> arrayList, int dialogType) {
            super(context);
            this.arrayList = arrayList;
            this.dialogType = dialogType;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.custom_dialog_layout);
            Objects.requireNonNull(getWindow()).setLayout(g.getScreenDpi(240), LinearLayout.LayoutParams.WRAP_CONTENT);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            positiveButton = findViewById(R.id.positiveButton);
            negativeButton = findViewById(R.id.negativeButton);
            title = findViewById(R.id.title);
            setTitle();
            dialogRecyclerView = findViewById(R.id.dialogRecyclerView);
            negativeButton.setOnClickListener(this);
            positiveButton.setOnClickListener(this);
            initAdapter();
        }

        private void setTitle() {
            switch (dialogType) {
                case CITY:
                    title.setText("حدد المدينة");
                    break;
                case PRICES:
                    title.setText("حدد السعر");
                    break;
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.positiveButton:
                    switch (dialogType) {
                        case CITY:
                            if (dialogClass != null) {
                                City.setText(dialogClass.getItemName());
                                City.setTag(dialogClass.getItemMask());
                                cityMeta = City.getTag().toString();
                                dialogClass = null;
                            }
                            dismiss();
                            break;
                        case PRICES:
                            if (dialogClass != null) {
                                Price.setText(dialogClass.getItemName());
                                Price.setTag(dialogClass.getItemMask());
                                priceMeta = Price.getTag().toString();
                                dialogClass = null;
                            }
                            dismiss();
                            break;
                    }
                    break;
                case R.id.negativeButton:
                    cancel();
                    break;
            }
        }

        private void initAdapter() {
            final ArrayList<DialogClass> dialogList = new ArrayList<>();

            for (int i = 0; i < arrayList.size(); i++) {
                Search.ItemNameMask itemNameMask = arrayList.get(i);
                dialogList.add(new DialogClass(itemNameMask.getItemName(), itemNameMask.getItemMask(), false));
            }
            DialogAdapter adapter = new DialogAdapter(dialogList, getContext());
            final LinearLayoutManager linearLayoutManager =
                    new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            dialogRecyclerView.setLayoutManager(linearLayoutManager);
            dialogRecyclerView.setAdapter(adapter);
            adapter.setmOnEntryClickListener((view, position) -> {
                DialogClass Dialog = dialogList.get(position);
                dialogClass = Dialog; // used in positiveButton
                Dialog.setImageChecked(true);
                dialogList.set(position, Dialog); // add selected mark

                // loop to remove selected mark of all lines
                for (int i = 0; i < dialogList.size(); i++) {
                    if (i != position) {
                        Dialog = dialogList.get(i);
                        Dialog.setImageChecked(false);
                        dialogList.set(i, Dialog);
                    }
                }
                // apply changes
                adapter.notifyDataSetChanged();


            });
        }
    }

    private ArrayList<Search.ItemNameMask> getCitiesNames() {
        ArrayList<Search.ItemNameMask> cityList = new ArrayList<>();
        cityList.add(new Search.ItemNameMask("كل المدن", "&city=0"));
//        try {
//            JSONArray cities = new JSONArray(g.getJsonMeta("cities"));
//            for (int i = 0; i < cities.length(); i++) {
//                String cityName = cities.optJSONObject(i).optString("name");
//                String cityMask = "&city=" + cities.optJSONObject(i).optString("id");
//                cityList.add(new Search.ItemNameMask(cityName, cityMask));
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        return cityList;
    }

    private ArrayList<Search.ItemNameMask> getPriceList() {
        ArrayList<Search.ItemNameMask> priceList = new ArrayList<>();
        priceList.add(new Search.ItemNameMask("كل الأسعار", "&price=0-15000"));// it comes from 2 array : names and mask
//        try {
//            JSONObject prices = new JSONObject(g.getJsonMeta("prices"));
//            Iterator<String> iter = prices.keys();
//            while (iter.hasNext()) {
//                String key = iter.next();
//                String value = prices.optString(key);
//                if(!key.equals("2000+"))
//                priceList.add(new Search.ItemNameMask(value, "&price=" + key));
//                else
//                    priceList.add(new Search.ItemNameMask(value, "&price=" + "2000-15000"));
//
//
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        return priceList;
    }

    private void initBanner() {
        int pixelsWidth = g.getScreenDpi(320);
        int pixelsHeight = g.getScreenDpi(130);
        // first top banner
        final ArrayList<BannerClass> bannerHouseList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            bannerHouseList.add(new BannerClass(R.drawable.camps, pixelsWidth, pixelsHeight));
            imgRecyclerView = findViewById(R.id.imgRecyclerView);
            LinearLayoutManager imglinearLayoutManager =
                    new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            BannerAdapter imgAdapter = new BannerAdapter(bannerHouseList, this);
            imgRecyclerView.setLayoutManager(imglinearLayoutManager);
            imgRecyclerView.setAdapter(imgAdapter);
            imgRecyclerView.setNestedScrollingEnabled(false);
        }
    }

    private void initRecycler1() {
        txtRecyclerView1 = findViewById(R.id.txtRecyclerView1);
        final ArrayList<HouseClass> txthouseList_1 = new ArrayList<>();

        HouseClass imgElreyad = new HouseClass(R.drawable.ryad, "الرياض");
        txthouseList_1.add(0, imgElreyad);
        for (int i = 0; i < 5; i++) {
            txthouseList_1.add(new HouseClass(R.drawable.altscreen, g.getHouseField("1", "name"), g.getHouseField("1", "name"), getPrice(g.getHouseField("1", "price")), g.getHouseField("1", "zone"), 3.0f));
        }
        LinearLayoutManager txtlinearLayoutManager_1 =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        HouseAdapter txtAdapter1 = new HouseAdapter(txthouseList_1);
        txtRecyclerView1.setLayoutManager(txtlinearLayoutManager_1);
        txtRecyclerView1.setAdapter(txtAdapter1);
        txtRecyclerView1.setNestedScrollingEnabled(false);
        onClickAdapters(txtAdapter1);
    }

    private void initRecycler2() {
        txtRecyclerView2 = findViewById(R.id.txtRecyclerView2);
        final ArrayList<HouseClass> txthouseList_2 = new ArrayList<>();
        HouseClass jadda = new HouseClass(R.drawable.ryad, "جدة");
        txthouseList_2.add(0, jadda);
        for (int i = 0; i < 5; i++) {
            txthouseList_2.add(new HouseClass(R.drawable.altscreen, g.getHouseField("2", "name"), g.getHouseField("2", "name"), getPrice(g.getHouseField("2", "price")), g.getHouseField("2", "zone"), 2.0f));
        }
        HouseAdapter txtAdapter2 = new HouseAdapter(txthouseList_2);
        LinearLayoutManager txtlinearLayoutManager_2 =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        txtRecyclerView2.setLayoutManager(txtlinearLayoutManager_2);
        txtRecyclerView2.setAdapter(txtAdapter2);
        txtRecyclerView2.setNestedScrollingEnabled(false);
        onClickAdapters(txtAdapter2);

    }

    private void initRecycler3() {
        txtRecyclerView3 = findViewById(R.id.txtRecyclerView3);
        final ArrayList<HouseClass> txthouseList_3 = new ArrayList<>();
        HouseClass eastArea = new HouseClass(R.drawable.ryad, "المنطقة الشرقية");
        txthouseList_3.add(0, eastArea);
        for (int i = 0; i < 5; i++) {
            txthouseList_3.add(new HouseClass(R.drawable.altscreen, g.getHouseField("3", "name"), g.getHouseField("3", "name"), getPrice(g.getHouseField("3", "price")), g.getHouseField("3", "zone"), 5.0f));
        }
        LinearLayoutManager txtlinearLayoutManager_3 =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        HouseAdapter txtAdapter3 = new HouseAdapter(txthouseList_3);
        txtRecyclerView3.setLayoutManager(txtlinearLayoutManager_3);
        txtRecyclerView3.setAdapter(txtAdapter3);
        txtRecyclerView3.setNestedScrollingEnabled(false);
        onClickAdapters(txtAdapter3);


    }

    private void initRecycler4() {
        txtRecyclerView4 = findViewById(R.id.txtRecyclerView4);
        final ArrayList<HouseClass> txthouseList_4 = new ArrayList<>();
        HouseClass imgElqasim = new HouseClass(R.drawable.elqasim, "القصيم");
        txthouseList_4.add(0, imgElqasim);
        for (int i = 0; i < 5; i++) {
            txthouseList_4.add(new HouseClass(R.drawable.altscreen, g.getHouseField("4", "name"), g.getHouseField("4", "name"), getPrice(g.getHouseField("4", "price")), g.getHouseField("4", "zone"), 4.0f));
        }
        LinearLayoutManager txtlinearLayoutManager_4 =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        HouseAdapter txtAdapter4 = new HouseAdapter(txthouseList_4);
        txtRecyclerView4.setLayoutManager(txtlinearLayoutManager_4);
        txtRecyclerView4.setAdapter(txtAdapter4);
        txtRecyclerView4.setNestedScrollingEnabled(false);
        onClickAdapters(txtAdapter4);

    }

    private void initRecycler5() {
        txtRecyclerView5 = findViewById(R.id.txtRecyclerView5);
        final ArrayList<HouseClass> txthouseList_5 = new ArrayList<>();
        HouseClass imgElKharg = new HouseClass(R.drawable.elkharg, "الخرج");
        txthouseList_5.add(0, imgElKharg);
        for (int i = 0; i < 5; i++) {
            txthouseList_5.add(new HouseClass(R.drawable.altscreen, g.getHouseField("5", "name"), g.getHouseField("5", "name"), getPrice(g.getHouseField("5", "price")), g.getHouseField("5", "zone"), 1.0f));
        }
        LinearLayoutManager txtlinearLayoutManager_5 =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        HouseAdapter txtAdapter5 = new HouseAdapter(txthouseList_5);
        txtRecyclerView5.setLayoutManager(txtlinearLayoutManager_5);
        txtRecyclerView5.setAdapter(txtAdapter5);
        txtRecyclerView5.setNestedScrollingEnabled(false);
        onClickAdapters(txtAdapter5);


    }
}
