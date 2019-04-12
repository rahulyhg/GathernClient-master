package com.max.gathernclient.gathernclient;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class HomePage extends AppCompatActivity {
    ImageView imgFamily, imgFriends, waterImg, campusImg, couplesImg, singleImg, offerImg, eventImg, cover_1, cover_2;
    TextView callCenter, city, check_in;
    EditText unitName, unitCode;
    Globals g;
    TinyDB tinyDB;

    RecyclerView waitingRecyclerView;
    final private static int CITY = 1;
    final private static int PRICES = 2;
    DialogClass dialogClass;
    String cityMeta = "", bannerMeta = "";
    ArrayList<WaitingClass> waitingClassArrayList = new ArrayList<>();
    boolean hasOpenCalender = false;
    boolean isFamily = false;
    boolean isFreinds = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        g = new Globals(this);
        tinyDB = new TinyDB(this);
        //Toast.makeText(getBaseContext(), tinydb.getListString("savedList").toString(), Toast.LENGTH_SHORT).show();

//
//        ArrayList<String> sList = tinydb.getListString("mList");
//        if (sList.size() > 0)
//        Toast.makeText(getBaseContext(),sList.get(0), Toast.LENGTH_SHORT).show();
//else
//            Toast.makeText(getBaseContext(),"list size = 0", Toast.LENGTH_SHORT).show();
        initrefrances();
        setShapesToImg();
        setAddUnitText();
        setImages();
        setCallCenter();
        setFragment();
        setDate();
        g.InitializeMixPanel("Home Page");
        oneSignal();
        initFaceBook();
        checkIfWatingReserve();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setDate();
        waitingClassArrayList.clear();
        checkIfWatingReserve();
    }

    private void setCallCenter() {
        callCenter = findViewById(R.id.callCenter);
        String s = "خدمة العملاء" + "\n" + "920007858" + "\n" + "9:30am - 12:00pm";
        SpannableString s1 = new SpannableString(s);
        s1.setSpan(new RelativeSizeSpan(1.5f), 12, 12 + 10, 0);
        s1.setSpan(new RelativeSizeSpan(.7f), 22, 22 + 17, 0);
        callCenter.setText(s1);
    }

    private void setImages() {
        // waterImg , campusImg ,couplesImg ,singleImg,offerImg ,eventImg;
        waterImg = findViewById(R.id.waterImg);
        campusImg = findViewById(R.id.campusImg);
        couplesImg = findViewById(R.id.couplesImg);
        singleImg = findViewById(R.id.singleImg);
        offerImg = findViewById(R.id.offerImg);
        eventImg = findViewById(R.id.eventImg);
        Picasso.with(this).load(R.drawable.water_play1).fit().into(waterImg);
        Picasso.with(this).load(R.drawable.campus1).fit().into(campusImg);
        Picasso.with(this).load(R.drawable.couples).fit().into(couplesImg);
        Picasso.with(this).load(R.drawable.single1).fit().into(singleImg);
        Picasso.with(this).load(R.drawable.offers_img1).fit().into(offerImg);//R.drawable.offers_img1
        Picasso.with(this).load(R.drawable.events_img1).fit().into(eventImg);


    }

    private void initrefrances() {
        waitingRecyclerView = findViewById(R.id.waitingRecyclerView);
        cover_1 = findViewById(R.id.cover_1);
        cover_2 = findViewById(R.id.cover_2);
        imgFamily = findViewById(R.id.imgFamily);
        imgFriends = findViewById(R.id.imgFriends);
        city = findViewById(R.id.city);
        check_in = findViewById(R.id.check_in);
        unitName = findViewById(R.id.unitName);
        unitCode = findViewById(R.id.unitCode);


    }

    private void initWatingAdapter() {
        WaitingPayAdapter adapter = new WaitingPayAdapter(waitingClassArrayList, this);
        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        waitingRecyclerView.setLayoutManager(linearLayoutManager);
        waitingRecyclerView.setAdapter(adapter);
        waitingRecyclerView.setNestedScrollingEnabled(false);
        adapter.setmOnEntryClickListener(new WaitingPayAdapter.OnEntryClickListener() {
            @Override
            public void onEntryClick(View view, int position) {
                WaitingClass waitingClass = waitingClassArrayList.get(position);
                Intent reservation_id = new Intent(getBaseContext(), ReservationDetails.class);
                reservation_id.putExtra("reservation_id", waitingClass.getReservation_id());
                startActivity(reservation_id);
            }
        });
    }

    private void checkIfWatingReserve() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String api = "api/vb/client/reservation/pending?expand=chalet";
                String result = g.connectToApi(api, null, "GET");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateList(result);
                        initWatingAdapter();
                    }
                });
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void updateList(String result) {
        String _check_in, unit_id, imgUrl, chaletName, _unitName, statusText, id;
        int secondsAgo;
        try {
            JSONObject ob = new JSONObject(result);
            JSONArray items = ob.optJSONArray("items");
            for (int i = 0; i < items.length(); i++) {
                _check_in = items.optJSONObject(i).optString("check_in");
                unit_id = items.optJSONObject(i).optString("unit_id");
                imgUrl = items.optJSONObject(i).optJSONObject("chalet").optJSONObject("coverphoto").optString("thumb");
                chaletName = items.optJSONObject(i).optString("chaletName");
                _unitName = items.optJSONObject(i).optString("unitName");
                statusText = items.optJSONObject(i).optString("statusText");
                String lastTowNumber = _check_in.substring(8);
                id = items.optJSONObject(i).optString("id");
                secondsAgo = items.optJSONObject(i).optInt("secondsAgo");
                waitingClassArrayList.add(new WaitingClass(imgUrl, chaletName, _unitName, statusText, _check_in, lastTowNumber, unit_id, id, secondsAgo));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setDate() {
        String a = "موعد الحجز :";
        String b = g.getBookingDate("dayNumber") + "";
        String c = g.getBookingDate("dayText") + ",";
        String d = g.getBookingDate("month") + ",";
        String e = g.getBookingDate("year") + "";
        String f = a + c + b + d + e;
        check_in.setText(f);
    }

    private void setShapesToImg() {
        Drawable corner = g.shapeStringColor("", g.getScreenDpi(60), g.getScreenDpi(1), "#5E5221");
        imgFamily.setBackground(corner);
        imgFriends.setBackground(corner);
    }

    private void choseFamilyImage() {
        Drawable fillImage = g.shapeStringColor("#5E5221", g.getScreenDpi(60), 0, "");
        Drawable emptyImage = g.shapeStringColor("", g.getScreenDpi(60), 0, "");
        isFreinds = false;
        if (!isFamily) {
            cover_1.setBackground(fillImage);
            cover_2.setBackground(emptyImage);
            isFamily = true;
        } else {
            cover_1.setBackground(emptyImage);
            cover_2.setBackground(emptyImage);
            isFamily = false;
        }
    }

    private void choseFreindsImage() {
        Drawable fillImage = g.shapeStringColor("#5E5221", g.getScreenDpi(60), 0, "");
        Drawable emptyImage = g.shapeStringColor("", g.getScreenDpi(60), 0, "");
        isFamily = false;
        if (!isFreinds) {
            cover_1.setBackground(emptyImage);
            cover_2.setBackground(fillImage);
            isFreinds = true;
        } else {
            cover_1.setBackground(emptyImage);
            cover_2.setBackground(emptyImage);
            isFreinds = false;
        }
    }

    private void setAddUnitText() {
        TextView addUnit = findViewById(R.id.addUnit);
        String s = "أضف شاليهك مع قاذر إن";
        SpannableString s1 = new SpannableString(s);
        s1.setSpan(new StyleSpan(Typeface.BOLD), 14, 14 + 7, 0);
        s1.setSpan(new ForegroundColorSpan(Color.BLACK), 14, 14 + 7, 0);
        addUnit.setText(s1);
    }

    public void onClick(View view) {
        CustomDialog cityDialog = new CustomDialog(this, getCitiesNames(), CITY);
        switch (view.getId()) {
            case R.id.cityLayout:
                cityDialog.show();
                break;
            case R.id.check_inLayout:
                startActivity(new Intent(getBaseContext(), Calender.class));
                hasOpenCalender = true;

                break;
            case R.id.unitNameLayout:
                break;
            case R.id.unitCodeLayout:
                break;
            case R.id.search_2:
                Bundle param = new Bundle();
                param.putString("checkin ", g.getCheckInDateForApi());
                param.putString("platform", "Android Native");
                g.facebookPixel("Search Primary", param);
                if (!hasOpenCalender && city.getText().toString().equals("المدينة") && unitName.getText().toString().equals("") && unitCode.getText().toString().equals("")) {
                    cityDialog.show();
                } else
                    sendMetaLink();
                //  Toast.makeText(getBaseContext(),city.getTag().toString(), Toast.LENGTH_SHORT).show();

                break;
            case R.id.addUnitLayout:

//                ArrayList<String> testArray = new ArrayList<>();
//                testArray.add(0,unitName.getText().toString());
//                tinydb.putListString("mList",testArray);

                Intent openBrowser = new Intent(Intent.ACTION_VIEW, Uri.parse("https://gathern.co/business/auth"));
                startActivity(openBrowser);
                break;

            case R.id.familyText:
                choseFamilyImage();
                break;
            case R.id.imgFamily:
                choseFamilyImage();
                break;
            case R.id.friendsText:
                choseFreindsImage();
                break;
            case R.id.imgFriends:
                choseFreindsImage();
                break;// start banner image
            case R.id.waterImg:
                bannerMeta = "&is_pool=1&pool_types%5B%5D=5";
resetFilter(bannerMeta);
sendMetaLink();
                break;
            case R.id.campusImg:
                bannerMeta = "&query=مخيم";
                resetFilter("");
                tinyDB.putString("nameText","مخيم");
                sendMetaLink();
                break;
            case R.id.couplesImg:
                bannerMeta = "&cats%5B%5D=5";
               resetFilter(bannerMeta);
                sendMetaLink();
                break;
            case R.id.singleImg:
                bannerMeta = "&cats%5B%5D=6";
               resetFilter(bannerMeta);
                sendMetaLink();
                break;
            case R.id.offerImg:
                startActivity(new Intent(getBaseContext(), Offers.class));

                //  startActivity(new Intent(getBaseContext(), MyPanorama.class));

//                Intent pay = new Intent(getBaseContext(), PayFortSdkSample.class);
//                pay.putExtra("orderId" ,"1234");
//                pay.putExtra("price" ,"150");
//                pay.putExtra("email" ,"sherif@gmail.com");
                //     startActivity(pay);
                break;
            case R.id.eventImg:
                bannerMeta = "&cats%5B%5D=1";
                resetFilter(bannerMeta);
                sendMetaLink();
                break;
            case R.id.callLayout:
                Intent openTransActivity = new Intent(getBaseContext(), TransparentActivity.class);
                openTransActivity.putExtra("ID", "bottomDialog");
                startActivity(openTransActivity);
                break;
            case R.id.unitName:
                unitName.setFocusable(true);
                break;
            case R.id.unitCode:
                unitCode.setFocusable(true);
                break;


        }
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

    private class CustomDialog extends Dialog {
        TextView positiveButton, negativeButton, title;
        RecyclerView dialogRecyclerView;
        ArrayList<Search.ItemNameMask> mArrayList;
        int dialogType;

        public CustomDialog(Context context, ArrayList<Search.ItemNameMask> arrayList, int dialogType) {
            super(context);
            this.mArrayList = arrayList;
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
//            negativeButton.setOnClickListener(this);
//            positiveButton.setOnClickListener(this);
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
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.positiveButton:
//                    break;
//                case R.id.negativeButton:
//                    break;
//            }
//        }

        private void initAdapter() {
            final ArrayList<DialogClass> dialogList = new ArrayList<>();

            for (int i = 0; i < mArrayList.size(); i++) {
                Search.ItemNameMask itemNameMask = mArrayList.get(i);
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
// test apply changes
                switch (dialogType) {
                    case CITY:
                        if (dialogClass != null) {
                            city.setText(dialogClass.getItemName());
                            city.setTag(dialogClass.getItemMask());
                            cityMeta = city.getTag().toString();
                            dialogClass = null;
                        }

                        startActivity(new Intent(getBaseContext(), Calender.class));
                        hasOpenCalender = true;

                        dismiss();
                        break;
                    case PRICES:

                        break;
                }
                // end test part

            });
        }
    }

    private ArrayList<Search.ItemNameMask> getCitiesNames() {
        ArrayList<Search.ItemNameMask> cityList = new ArrayList<>();
        cityList.add(new Search.ItemNameMask("كل المدن", "&city=0"));
        // Toast.makeText(getBaseContext(), g.getAppData("allData"), Toast.LENGTH_SHORT).show();

        try {
            JSONObject ob = new JSONObject(g.getAppData("allData"));
            JSONObject data = ob.optJSONObject("data");
            JSONArray cities = data.optJSONArray("cities");

            for (int i = 0; i < cities.length(); i++) {
                String cityName = cities.optJSONObject(i).optString("name");
                String cityMask = "&city=" + cities.optJSONObject(i).optInt("id");
                cityList.add(new Search.ItemNameMask(cityName, cityMask));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return cityList;
    }

    private void sendMetaLink() {
        String name = unitName.getText().toString();
        String code = unitCode.getText().toString();
        String meta = "&unit_code=" + code + "&query=" + name + cityMeta + bannerMeta;
        // Toast.makeText(getBaseContext(), meta, Toast.LENGTH_SHORT).show();
        if (hasOpenCalender) {
            Intent open = new Intent(getBaseContext(), SearchResult.class);
            open.putExtra("meta", meta);
            startActivity(open);
        } else {
            startActivity(new Intent(getBaseContext(), Calender.class));
            hasOpenCalender = true;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }

    private void oneSignal() {
        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }

    private void initFaceBook() {
        Bundle param = new Bundle();
        param.putString("checkin ", g.getCheckInDateForApi());
        param.putString("platform", "Android Native");
        g.facebookPixel("Home Page", param);
    }
    private void resetFilter (String blockMeta){
        ArrayList<String> metaWordsFromAllFragments = new ArrayList<>();
        metaWordsFromAllFragments.clear();
        metaWordsFromAllFragments.add(blockMeta);
        tinyDB.putListString("savedList",metaWordsFromAllFragments);
        tinyDB.putString("codeText","");
        tinyDB.putString("nameText","");
        tinyDB.putString("directionText","");
        tinyDB.putString("directionTag","");
        tinyDB.putString("cityText","");
        tinyDB.putString("cityTag","");
    }
}
