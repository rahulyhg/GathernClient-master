package com.max.gathernclient.gathernclient;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class Filter extends AppCompatActivity {
    TinyDB tinyDB ;
    ArrayList<String> savedFilterItems = new ArrayList<>();
    LinearLayout moreLayout  , afterMoreLayout;
    ImageView moreCategory ;
    boolean isReload = false ;
    boolean isMoreLayout = false ;
    Globals g;
    TextView BookinDate, derictions, City, startPrice, endPrice, startNumOfPerson
            , endNumOfPerson, numOfWc, searchResult, show;
    MyEditText enterName , enterCode ;
    RangeSeekBar singleSeekBar, priceRange, rangeNumOfPerson;
    ArrayList<String> metaWordsFromAllFragments = new ArrayList<>();
    String  sampleMeta = "", cityMeta = "", derictionMeta = "";
    DialogClass dialogClass;
    final private static   int CITY = 1;
    final private static int DERICTIONS = 2;
    String allData = "" ;
    JSONObject data ;
    JSONArray unit_cats ,amenities ,kitchen_amenities ,toilet_amenities ,free_amenities ,pool_types ,
            capacity ,reviews ,prices_list ;
    //  ArrayList<ItemNameMask> derictionNameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);
        g = new Globals(this);
        tinyDB = new TinyDB(this);
        metaWordsFromAllFragments = tinyDB.getListString("savedList");
        initRefarance();

        initJsonObject();
        updateDate();
        array_bundle_fragment();
        initSearchCode();
        initSearchName() ;
       // enterName.setText(startingMeta);
        getDataFromServer(getMetaLink());
        initFaceBook();
       // initRatingFlexBox();
      //  initPoolFlexBox();
      //  initPoolType(false);
    }
    private void initRefarance (){
        show = findViewById(R.id.show);
        moreCategory = findViewById(R.id.moreCategory);
        moreLayout = findViewById(R.id.moreLayout);
        searchResult = findViewById(R.id.searchResult);
        afterMoreLayout = findViewById(R.id.afterMoreLayout);
        BookinDate = findViewById(R.id.bookingtime);
        derictions = findViewById(R.id.chosePrice);
        City = findViewById(R.id.choseCity);
        enterCode = findViewById(R.id.enterCode);
        enterName = findViewById(R.id.enterName);


    }

    private void initJsonObject(){
        allData = g.getAppData("allData");
        try {
            JSONObject object = new JSONObject(allData);
            data = object.optJSONObject("data");

            unit_cats = data.optJSONArray("unit_cats");
            amenities = data.optJSONArray("amenities");
            kitchen_amenities = data.optJSONArray("kitchen_amenities");
            toilet_amenities = data.optJSONArray("toilet_amenities");
            free_amenities = data.optJSONArray("free_amenities");
            pool_types = data.optJSONArray("pool_types");
            capacity = data.optJSONArray("capacity");
            reviews = data.optJSONArray("reviews");
            prices_list = data.optJSONArray("prices_list");


        } catch (JSONException e) {
            e.printStackTrace();

        }
    }
    public void onClick(View view) {
        CustomDialog customDialogCity = new CustomDialog(this, getCitiesNames(), CITY);
        CustomDialog customDialogDerictions = new CustomDialog(this, getdirections(), DERICTIONS);

        switch (view.getId()) {
            case R.id.bookingtime:
                startActivity(new Intent(getBaseContext(), Calender.class));
                isReload = true ;
                break;
            case R.id.imageBack:
                finish();
                break;
            case R.id.choseCity:
                customDialogCity.show();
                // derictions.setTag("&direction=all");
                break;
            case R.id.chosePrice:
                customDialogDerictions.show();
                break;
            case R.id.show:
                Intent openUnits = new Intent(getBaseContext(), SearchResult.class);
                openUnits.putExtra("meta", getMetaLink());
                openUnits.putExtra("startingMeta", "");
                startActivity(openUnits);
                break;
            case R.id.moreLayout :
                if (!isMoreLayout){
                    afterMoreLayout.setVisibility(View.VISIBLE);
                    isMoreLayout = true ;
                    moreCategory.setImageResource(R.drawable.min);
                }else {
                    afterMoreLayout.setVisibility(View.GONE);
                    isMoreLayout = false ;
                    moreCategory.setImageResource(R.drawable.add);
                }
                break;
            case R.id.reset :
                metaWordsFromAllFragments.clear();
                tinyDB.putListString("savedList",metaWordsFromAllFragments);
                array_bundle_fragment();
                enterCode.setText("");
                enterName.setText("");
                derictions.setText("كل الإتجاهات");
                derictions.setTag("&direction=all");
                City.setText("كل المدن");
                City.setTag("&city=0");
                getDataFromServer(getMetaLink());
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        initRatingFlexBox();
//        initPoolFlexBox();
//        initPoolType(false);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isReload) {
            updateDate();
            getDataFromServer(getMetaLink());
            isReload = false ;
        }
    }

    private void updateDate() {
        String a = "موعد الحجز" ;
        String s = a+"\n"+g.finishedBookingDate();

        SpannableString s1 = new SpannableString(s);
        s1.setSpan(new ForegroundColorSpan(Color.BLACK), a.length(),  s.length(), 0);
        s1.setSpan(new RelativeSizeSpan(1.5f),  a.length(), s.length(), 0);

        BookinDate.setText(s1);
        derictions.setText("كل الإتجاهات");
        derictions.setTag("&direction=all");
        City.setText("كل المدن");
        City.setTag("&city=0");
        String cityText = tinyDB.getString("cityText");
        String cityTag = tinyDB.getString("cityTag") ;
        if (cityText.length() > 0)
        {
            City.setText(cityText);
            City.setTag(cityTag);
        }
        String directionText = tinyDB.getString("directionText");
        String directionTag = tinyDB.getString("directionTag");
        if (directionText.length() > 0)
        {
            derictions.setText(directionText);
            derictions.setTag(directionTag);
        }

    }


//    private void initSeekBars() {
//        singleSeekBar = findViewById(R.id.singleSeekBar);
//        priceRange = findViewById(R.id.priceRange);
//        rangeNumOfPerson = findViewById(R.id.rangeNumOfPerson);
//
//        singleSeekBar.setSelectedMaxValue(0);
//
//        priceRange.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
//            @Override
//            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
//
//                String min = "SAR " + minValue.toString();
//                startPrice = findViewById(R.id.startPrice);
//                startPrice.setText(min);
//                String max = "SAR " + maxValue.toString();
//                endPrice = findViewById(R.id.endPrice);
//                endPrice.setText(max);
//                metaPrice = "&price=" + minValue.toString() + "-" + maxValue.toString();
//                getDataFromServer(getMetaLink());
//            }
//        });
//
//        rangeNumOfPerson.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
//            @Override
//            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
//                String min = "من : " + minValue.toString();
//                startNumOfPerson = findViewById(R.id.startNumOfPerson);
//                startNumOfPerson.setText(min);
//                String max = "إلى : " + maxValue.toString();
//                endNumOfPerson = findViewById(R.id.endNumOfPerson);
//                endNumOfPerson.setText(max);
//                metaPerson = "&general_cpacity=" + minValue.toString() + "-" + maxValue.toString();
//                getDataFromServer(getMetaLink());
//            }
//        });
//        singleSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
//            @Override
//            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
//                numOfWc = findViewById(R.id.numOfWc);
//                numOfWc.setText(maxValue.toString());
//                metaWc = "&toilet_count=" + maxValue.toString();
//                getDataFromServer(getMetaLink());
//
//            }
//        });
//    }

    protected void setFragment(int id, Bundle bundle) {

        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.commit();
        fragment.myListener(view -> {
          //  ImageView textView = (ImageView) view;
            String metaWord = view.getTag(R.id.kind).toString() + view.getTag(R.id.serch_ids).toString();
            //  Toast.makeText(getBaseContext(), metaWord, Toast.LENGTH_SHORT).show();

            if (view.getTag().toString().equals("Selected"))
                metaWordsFromAllFragments.add(metaWord);
            else
                metaWordsFromAllFragments.remove(metaWord);

            tinyDB.putListString("savedList" ,metaWordsFromAllFragments);
            getDataFromServer(getMetaLink());
        });
    }

    private class CustomDialog extends Dialog {
        TextView positiveButton, negativeButton , title;
        RecyclerView dialogRecyclerView;
        ArrayList<ItemNameMask> arrayList;
        int dialogType;

        public CustomDialog(Context context, ArrayList<ItemNameMask> arrayList, int dialogType) {
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
            initAdapter();
        }

        private void setTitle(){
            switch (dialogType){
                case CITY :
                    title.setText("حدد المدينة");
                    break;
                case DERICTIONS :
                    title.setText("حدد الإتجاه");
                    break;
            }
        }


        private void initAdapter() {
            final ArrayList<DialogClass> dialogList = new ArrayList<>();

            for (int i = 0; i < arrayList.size(); i++) {
                ItemNameMask itemNameMask = arrayList.get(i);
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
                            City.setText(dialogClass.getItemName());
                            City.setTag(dialogClass.getItemMask());
                            tinyDB.putString("cityText",dialogClass.getItemName());
                            tinyDB.putString("cityTag",dialogClass.getItemMask());
                            cityMeta = City.getTag().toString();
                            getDataFromServer(getMetaLink());
                            dialogClass = null;
                        }
                        dismiss();
                        break;
                    case DERICTIONS:
                        if (dialogClass != null) {
                            derictions.setText(dialogClass.getItemName());
                            derictions.setTag(dialogClass.getItemMask());
                            tinyDB.putString("directionText",dialogClass.getItemName());
                            tinyDB.putString("directionTag",dialogClass.getItemMask());
                            derictionMeta = derictions.getTag().toString();
                            getDataFromServer(getMetaLink());
                            dialogClass = null;
                        }
                        dismiss();
                        break;
                }
                // end test part

            });
        }
    }


    public void getDataFromServer(String meta) {
        showLoadingImage();
        String lat = g.getLoc("lat");
        String lng = g.getLoc("lon");
        Runnable runnable = () -> {
            String check_in = g.getCheckInDateForApi();
            String api = "api/va/chalet/search/search?" + "&check_in=" + check_in + "&lat=" + lat + "&lng=" + lng + meta ;
            String result = g.connectToApi(api, null, "GET");
            runOnUiThread(() -> {
             //     Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject mainObject = new JSONObject(result);
                    JSONObject _meta = mainObject.getJSONObject("_meta");
                    int totalCount = _meta.optInt("totalCount");
                    hideLoadingImage();
                    showSearchResult(totalCount);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void showSearchResult(int number) {
        String s1 = "1200";
        String s2 = " شالية";
        String s3 =number +"/"+ s1 + s2;
        searchResult.setText(s3);
        if(number <= 0) {
            searchResult.setEnabled(false);
        }

    }

    private void showLoadingImage() {
        ImageView loadingImage = findViewById(R.id.loading);
        loadingImage.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotation);
        animation.setInterpolator(new LinearInterpolator());
        loadingImage.setAnimation(animation);
       // searchResult.setBackgroundColor(getResources().getColor(R.color.transprimary));
        show.setEnabled(false);
        show.setBackground(g.shapeColorString("#8F0058FF",g.getScreenDpi(5)));
    }

    private void hideLoadingImage() {
        ImageView loadingImage = findViewById(R.id.loading);
        loadingImage.setAnimation(null);
        loadingImage.setVisibility(View.GONE);
        //searchResult.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        show.setEnabled(true);
        show.setBackground(g.shapeColorString("#0058FF",g.getScreenDpi(5)));
    }

    private String getMetaLink() {
        String code = Objects.requireNonNull(enterCode.getText()).toString();
        String name = Objects.requireNonNull(enterName.getText()).toString();
        cityMeta = City.getTag().toString();
        derictionMeta = derictions.getTag().toString();
        sampleMeta = "&unit_code=" + code + "&query=" + name + derictionMeta + cityMeta;

        StringBuilder link = new StringBuilder();
        for (int i = 0; i < metaWordsFromAllFragments.size(); i++) {
            link.append(metaWordsFromAllFragments.get(i));
        }
        return String.valueOf(link) + sampleMeta;
    }

    private ArrayList<ItemNameMask> getCitiesNames() {
        ArrayList<ItemNameMask> cityList = new ArrayList<>();
        cityList.add(new ItemNameMask("كل المدن", "&city=0"));
        try {
            JSONObject ob = new JSONObject(g.getAppData("allData"));
            JSONObject data = ob.optJSONObject("data");
            JSONArray cities = data.optJSONArray("cities");

            for (int i = 0; i < cities.length(); i++) {
                String cityName = cities.optJSONObject(i).optString("name");
                String cityMask = "&city=" + cities.optJSONObject(i).optInt("id");
                cityList.add(new Filter.ItemNameMask(cityName, cityMask));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return cityList;
    }

    private ArrayList<ItemNameMask> getdirections() {
        ArrayList<ItemNameMask> derictionNameList = new ArrayList<>();
        derictionNameList.add(new ItemNameMask("كل الإتجاهات", "&direction=all"));
        String cityName = City.getText().toString();
        try {
            JSONObject ob = new JSONObject(g.getAppData("allData"));
            JSONObject data = ob.optJSONObject("data");
            JSONArray cities =data.optJSONArray("cities");

            for (int i = 0; i < cities.length(); i++) {
                if (cities.optJSONObject(i).optString("name").equals(cityName)) {
                    // find directions for specific city name
                    JSONArray dirs = cities.optJSONObject(i).optJSONArray("dirs");
                    for (int x = 0 ; x<dirs.length();x++){
                        String value = dirs.optJSONObject(x).optString("value");
                        String id = dirs.optJSONObject(x).optString("id");
                        derictionNameList.add(new ItemNameMask(value, "&direction="+id));
                    }
                }
            }
            for (int i = 0; i < derictionNameList.size(); i++) {
//                directionsList.remove("");
//                directionsList.remove("");

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        derictions.setText("كل الإتجاهات");
        derictions.setTag("&direction=all");
        return derictionNameList;
    }

    private void initFaceBook() {
        Bundle param = new Bundle();
        param.putString("checkin ", g.getCheckInDateForApi());
        param.putString("platform", "Android Native");
        g.facebookPixel("Search Advanced", param);
    }


    public static class ItemNameMask {
        String itemNmae, itemMask;

        public ItemNameMask(String itemNmae, String itemMask) {
            this.itemNmae = itemNmae;
            this.itemMask = itemMask;
        }

        public String getItemMask() {
            return itemMask;
        }

        public String getItemName() {
            return itemNmae;
        }
    }

    private void array_bundle_fragment(){
        ArrayList<String> unit_cats_title = new ArrayList<>();
        ArrayList<String> unit_cats_id = new ArrayList<>();

        ArrayList<String> amenities_title = new ArrayList<>();
        ArrayList<String> amenities_id = new ArrayList<>();

        ArrayList<String> kitchen_amenities_title = new ArrayList<>();
        ArrayList<String> kitchen_amenities_id = new ArrayList<>();

        ArrayList<String> toilet_amenities_title = new ArrayList<>();
        ArrayList<String> toilet_amenities_id = new ArrayList<>();

        ArrayList<String> free_amenities_title = new ArrayList<>();
        ArrayList<String> free_amenities_id = new ArrayList<>();



        ArrayList<String> capacity_title = new ArrayList<>();
        ArrayList<String> capacity_id = new ArrayList<>();

        ArrayList<String> prices_list_title = new ArrayList<>();
        ArrayList<String> prices_list_id = new ArrayList<>();

        ArrayList<String> bedRoomsList_title = new ArrayList<>();
        bedRoomsList_title.add("مفرد");
        bedRoomsList_title.add("دبل");
        bedRoomsList_title.add("ماستر");
        ArrayList<String> bedRoomsList_id = new ArrayList<>();
        bedRoomsList_id.add("&b_s_count=1");
        bedRoomsList_id.add("&b_d_count=1");
        bedRoomsList_id.add("&b_m_count=1");
////// reviews
        ArrayList<String> reviews_title = new ArrayList<>();
        ArrayList<String> reviews_id = new ArrayList<>();
        reviews_title.add("الكل");
        reviews_id.add("0-10");
        for(int i = 0 ; i<reviews.length();i++){
            String id = reviews.optJSONObject(i).optString("id");
            String title = reviews.optJSONObject(i).optString("value");
            reviews_title.add(title);
            String _id = id.replace("+","%2B");
            reviews_id.add(_id);
        }
        //////
        for(int i = 0 ; i<unit_cats.length();i++){
            String id = unit_cats.optJSONObject(i).optString("id");
            String title = unit_cats.optJSONObject(i).optString("title");
            unit_cats_title.add(title);
            unit_cats_id.add(id);
        }
        for(int i = 0 ; i<amenities.length();i++){
            String id = amenities.optJSONObject(i).optString("id");
            String title = amenities.optJSONObject(i).optString("title");
            amenities_title.add(title);
            amenities_id.add(id);
        }
        for(int i = 0 ; i<kitchen_amenities.length();i++){
            String id = kitchen_amenities.optJSONObject(i).optString("id");
            String title = kitchen_amenities.optJSONObject(i).optString("name");
            kitchen_amenities_title.add(title);
            kitchen_amenities_id.add(id);
        }

        for(int i = 0 ; i<toilet_amenities.length();i++){
            String id = toilet_amenities.optJSONObject(i).optString("id");
            String title = toilet_amenities.optJSONObject(i).optString("name");
            toilet_amenities_title.add(title);
            toilet_amenities_id.add(id);
        }
        for(int i = 0 ; i<free_amenities.length();i++){
            String id = free_amenities.optJSONObject(i).optString("id");
            String title = free_amenities.optJSONObject(i).optString("name");
            free_amenities_title.add(title);
            free_amenities_id.add(id);
        }

        for(int i = 0 ; i<capacity.length();i++){
            String id = capacity.optJSONObject(i).optString("id");
            String title = capacity.optJSONObject(i).optString("value");
            capacity_title.add(title);
            capacity_id.add(id);
        }

        for(int i = 0 ; i<prices_list.length();i++){
            String id = prices_list.optJSONObject(i).optString("id");
            String title = prices_list.optJSONObject(i).optString("value");
            prices_list_title.add(title);
            String _id = id.replace("+","%2B");
            prices_list_id.add(_id);
        }

/*
        //  static array never used

        String[] classesList = {"مناسبات", "جمعة بنات", "عوائل مع أطفال", "أزواج", "عزاب"};//unit_cats
        String[] classesList_ids = {"1", "2", "3", "5", "6"};

        String[] elmarafeqList = {"إنترنت", "إضاءة", "سماعات", "ألعاب أطفال", "ركن شواء", "جاكوزي", "موقد حطب", "ملعب كرة قدم", "ملعب كرة طائرة", "غرفة عبايات"
                , "سونا", "غرفة ألعاب أطفال", "بروجيكتر", "بيت شعر", "ستيج مضئ", "طاولة تنس", "غرفة تجهيز عروس", "صالة طعام بدون طاولات"
                , "مسطح أخضر", "نطيطة", "ملعب كرة سلة", "دولاب عبايات", "خيمة", "بلاي إستيشن", "بيت شعر ملكي"
                , "جلسة عريش", "بلياردو", "كرسي مساج", "برميل مندي", "ملحق خارجي مستقل", "رذاذ", "ألعاب رملية"
                , "صالة إضافية", "فرفيرة", "هوكي", "برادة ماء", "تزلج علي الرمل"};//amenities
        String[] elmarafeqList_ids = {"10", "18", "19", "24", "26", "27", "31", "32", "33", "34", "42", "44", "45", "46", "48", "49", "50"
                , "51", "52", "53", "54", "55", "58", "59", "60", "61", "62", "63", "64", "65", "67", "69", "70", "71", "72", "73", "74",};

        String[] marafeqElmatbakhList = {"فرن", "ثلاجة", "مايكرو ويف", "غلاية", "أواني طبخ", "آلة قهوة"};//kitchen_amenities
        String[] marafeqElmatbakhList_ids = {"10", "11", "12", "13", "14", "15"};//kitchen_amenities

        String[] wcList = {"مناديل", "صابون", "حوض إستحمام", "شامبو", "سلبر", "رداء حمام", "دش"};//toilet_amenities
        String[] wcList_ids = {"16", "17", "18", "19", "20", "21", "22"};//toilet_amenities

        String[] freeList = {"قهوة وشاهى", "حطب", "سينما", "سماعات", "إنترنت", "ملعب كرة قدم", "ملعب كرة طائرة", "موقد حطب"};//free_amenities
        String[] freeList_ids = {"1", "2", "9", "23", "24", "27", "28", "29"};//free_amenities

*/

        Bundle price = new Bundle();
        price.putStringArrayList("List", prices_list_title);
        price.putStringArrayList("List_ids", prices_list_id);
        price.putString("Kind", "&price=");

        Bundle reviews = new Bundle();
        reviews.putStringArrayList("List", reviews_title);
        reviews.putStringArrayList("List_ids", reviews_id);
        reviews.putString("Kind", "&unit_reviews=");


        Bundle elmarafeq = new Bundle();
        elmarafeq.putStringArrayList("List", amenities_title);
        elmarafeq.putStringArrayList("List_ids", amenities_id);
        elmarafeq.putString("Kind", "&amenities=");

        Bundle bedRooms = new Bundle();
        bedRooms.putStringArrayList("List", bedRoomsList_title);
        bedRooms.putStringArrayList("List_ids", bedRoomsList_id);
        bedRooms.putString("Kind", "");

        Bundle classes = new Bundle();
        classes.putStringArrayList("List", unit_cats_title);
        classes.putStringArrayList("List_ids", unit_cats_id);
        classes.putString("Kind", "&cats%5B%5D=");

        Bundle marafeqElmatbakh = new Bundle();
        marafeqElmatbakh.putStringArrayList("List", kitchen_amenities_title);
        marafeqElmatbakh.putStringArrayList("List_ids", kitchen_amenities_id);
        marafeqElmatbakh.putString("Kind", "&amenities_kitchen%5B%5D=");

        Bundle wc = new Bundle();
        wc.putStringArrayList("List", toilet_amenities_title);
        wc.putStringArrayList("List_ids", toilet_amenities_id);
        wc.putString("Kind", "&amenities_toilet=");

        Bundle free = new Bundle();
        free.putStringArrayList("List", free_amenities_title);
        free.putStringArrayList("List_ids", free_amenities_id);
        free.putString("Kind", "&amenities_frees=");

        setFragment(R.id.priceFrameLayout, price);
        setFragment(R.id.reviewsFrameLayout, reviews);
        setFragment(R.id.bedRoomsFrameLayout, bedRooms);
        setFragment(R.id.classesFrameLayout, classes);
        setFragment(R.id.elmarafeqFramelayout, elmarafeq);
        setFragment(R.id.marafeqElmatbakhFrameLayout, marafeqElmatbakh);
        setFragment(R.id.wcFrameLayout, wc);
        setFragment(R.id.freeServicesFrameLayout, free);
    }

    private void initSearchCode(){
        enterCode.setText(tinyDB.getString("codeText"));
        enterCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tinyDB.putString("codeText",enterCode.getText().toString());
                getDataFromServer(getMetaLink());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initSearchName() {
        enterName.setText(tinyDB.getString("nameText"));

        enterName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tinyDB.putString("nameText",enterName.getText().toString());
                getDataFromServer(getMetaLink());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void initRatingFlexBox(){

        ArrayList<String> reviews_title = new ArrayList<>();
        ArrayList<String> reviews_id = new ArrayList<>();
        reviews_title.add("الكل");
        reviews_id.add("0-10");
        for(int i = 0 ; i<reviews.length();i++){
            String id = reviews.optJSONObject(i).optString("id");
            String title = reviews.optJSONObject(i).optString("value");
            reviews_title.add(title);
            String _id = id.replace("+","%2B");
            reviews_id.add(_id);
        }
        //String[] ratingList = {"الكل", "ممتاز+9", "جيد جداً+8", "جيد+7", "مرضي+6"};
        //String[] ratingList_ids = {"0-10", "9%2B", "8%2B", "7%2B", "6%2B"};//&unit_reviews=
        int colorUnCheked = getResources().getColor(R.color.myblack);
        Drawable drawableUnCheked = Objects.requireNonNull(getResources().getDrawable(R.drawable.shape_search_unchecked));
        int colorCheked = getResources().getColor(R.color.mywhite);
        Drawable drawableCheked = Objects.requireNonNull(getResources().getDrawable(R.drawable.shape_color_primary_radius60));

        for(int i = 0; i < reviews_title.size(); i++) {
            MyTextView textView = new MyTextView(this);

            textView.setText(reviews_title.get(i));
            textView.setPadding(g.getScreenDpi(8),g.getScreenDpi(16),g.getScreenDpi(8),g.getScreenDpi(16));
            FlexboxLayout.LayoutParams params = new  FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT , FlexboxLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(g.getScreenDpi(2),g.getScreenDpi(4),g.getScreenDpi(2),g.getScreenDpi(4));

            textView.setLayoutParams(params);
            textView.setTag(R.id.kind,"&unit_reviews=");
            textView.setTag(R.id.serch_ids, reviews_id.get(i));

           // ratingFlexBox.addView(textView);

            initShapesForRating();

//            textView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    textView.setBackground(drawableCheked);
//                    textView.setTextColor(colorCheked);
//                    metaWordReview = v.getTag(R.id.kind).toString()+ v.getTag(R.id.serch_ids).toString() ;
//                    getDataFromServer(getMetaLink());
//                    // remove checked from all other views
//                    for (int i=0;i<ratingFlexBox.getChildCount();i++){
//                        TextView randomText = (TextView) ratingFlexBox.getChildAt(i);
//                        if(!randomText.getText().toString().equals(((TextView) v).getText().toString())) {
//                            TextView currentTextInList = (TextView) ratingFlexBox.getChildAt(i);
//                            currentTextInList.setBackground(drawableUnCheked);
//                            currentTextInList.setTextColor(colorUnCheked);
//                        }
//                    }
//                }
//            });
        }

    }
    private void initShapesForRating(){
        int colorUnCheked = getResources().getColor(R.color.myblack);
        int colorCheked = getResources().getColor(R.color.mywhite);
        Drawable drawableCheked = Objects.requireNonNull(getResources().getDrawable(R.drawable.shape_color_primary_radius60));
        Drawable drawableUnChek = g.shapeColorString("#EDEBEB" ,g.getScreenDpi(60));
//        for (int i=0;i<ratingFlexBox.getChildCount();i++){
//            TextView textView = (TextView) ratingFlexBox.getChildAt(i);
//            if(i == 0){
//                textView.setBackground(drawableCheked);
//                textView.setTextColor(colorCheked);
//            }else{
//                textView.setBackground(drawableUnChek);
//                textView.setTextColor(colorUnCheked);
//            }
//        }
    }
    private void initPoolFlexBox(){

        String[] swimmingPoolList = {"لايهم", "يوجد مسبح", "لايوجد مسبح"};//is_pool
        String[] swimmingPoolList_ids = {"false", "1", "0"};
        int colorUnCheked = getResources().getColor(R.color.myblack);
        Drawable drawableUnCheked = Objects.requireNonNull(getResources().getDrawable(R.drawable.shape_search_unchecked));
        Drawable drawableCheked = Objects.requireNonNull(getResources().getDrawable(R.drawable.shape_color_primary_radius60));
        int colorCheked = getResources().getColor(R.color.mywhite);

        for(int i = 0; i < swimmingPoolList.length; i++) {
            MyTextView textView = new MyTextView(this);

            textView.setText(swimmingPoolList[i]);
            textView.setPadding(g.getScreenDpi(8),g.getScreenDpi(16),g.getScreenDpi(8),g.getScreenDpi(16));
            FlexboxLayout.LayoutParams params = new  FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT , FlexboxLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(g.getScreenDpi(2),g.getScreenDpi(4),g.getScreenDpi(2),g.getScreenDpi(4));

            textView.setLayoutParams(params);
            textView.setTag(R.id.kind,"&is_pool=");
            textView.setTag(R.id.serch_ids, swimmingPoolList_ids[i]);

           // poolFlexBox.addView(textView);
            if(i == 0){
                textView.setBackground(drawableCheked);
                textView.setTextColor(colorCheked);
            }else{
                textView.setBackground(drawableUnCheked);
                textView.setTextColor(colorUnCheked);
            }
//            textView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    TextView currentText= (TextView) v;
//                    if(currentText.getText().toString().equals("يوجد مسبح"))
//                        initPoolType(true);
//                    else
//                        initPoolType(false);
//                    textView.setBackground(drawableCheked);
//                    textView.setTextColor(colorCheked);
//                    metaWordReview = v.getTag(R.id.kind).toString()+ v.getTag(R.id.serch_ids).toString() ;
//                    getDataFromServer(getMetaLink());
//                    // remove checked from all other views
//                    for (int i=0;i<poolFlexBox.getChildCount();i++){
//                        TextView randomText = (TextView) poolFlexBox.getChildAt(i);
//                        if(!randomText.getText().toString().equals(((TextView) v).getText().toString())) {
//                            TextView currentTextInList = (TextView) poolFlexBox.getChildAt(i);
//                            currentTextInList.setBackground(drawableUnCheked);
//                            currentTextInList.setTextColor(colorUnCheked);
//                        }
//                    }
//                }
//            });
        }
    }
    private void initPoolType(boolean isVisibile){
        ArrayList<String> pool_types_title = new ArrayList<>();
        ArrayList<String> pool_types_id = new ArrayList<>();
        for(int i = 0 ; i<pool_types.length();i++){
            String id = String.valueOf(pool_types.optJSONObject(i).optInt("id"));
            String title = pool_types.optJSONObject(i).optString("value");
            pool_types_title.add(title);
            pool_types_id.add(id);
        }
        TextView poolTypeText = findViewById(R.id.poolTypeText);
        FlexboxLayout PoolTypeFlexBox = findViewById(R.id.PoolTypeFlexBox);
        PoolTypeFlexBox.removeAllViews();
        if(!isVisibile) {
            poolTypeText.setVisibility(View.GONE);
            PoolTypeFlexBox.setVisibility(View.GONE);
            metaWordsFromAllFragments.remove("&pool_types%5B%5D=1");
            metaWordsFromAllFragments.remove("&pool_types%5B%5D=2");
            metaWordsFromAllFragments.remove("&pool_types%5B%5D=3");
            metaWordsFromAllFragments.remove("&pool_types%5B%5D=4");
            metaWordsFromAllFragments.remove("&pool_types%5B%5D=5");
            metaWordsFromAllFragments.remove("&pool_types%5B%5D=6");
            metaWordsFromAllFragments.remove("&pool_types%5B%5D=7");

        } else {
            poolTypeText.setVisibility(View.VISIBLE);
            PoolTypeFlexBox.setVisibility(View.VISIBLE);
//            String[] PoolTypeList = {"مسبح داخلي بحاجز", "مسبح داخلي بدون حاجز", "مسبح خارجي بحاجز" , "مسبح خارجي بدون حاجز", "مسبج بألعاب مائية"
//                    , "مسبح بألعاب مائية بحاجز", "مسبح بتدفئة"};//is_pool
//
//            String[] swimmingPoolList_ids = {"1","2","3","4","5","6","7"};
            int colorUnCheked = getResources().getColor(R.color.myblack);
            Drawable drawableUnCheked = Objects.requireNonNull(getResources().getDrawable(R.drawable.shape_search_unchecked));
            Drawable drawableCheked = Objects.requireNonNull(getResources().getDrawable(R.drawable.shape_color_primary_radius60));
            int colorCheked = getResources().getColor(R.color.mywhite);

            for(int i = 0; i < pool_types_title.size(); i++) {
                MyTextView textView = new MyTextView(this);

                textView.setText(pool_types_title.get(i));
                textView.setPadding(g.getScreenDpi(8),g.getScreenDpi(16),g.getScreenDpi(8),g.getScreenDpi(16));
                FlexboxLayout.LayoutParams params = new  FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT , FlexboxLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(g.getScreenDpi(2),g.getScreenDpi(4),g.getScreenDpi(2),g.getScreenDpi(4));

                textView.setLayoutParams(params);
                textView.setTag(R.id.kind,"&pool_types%5B%5D=");
                textView.setTag(R.id.serch_ids, pool_types_id.get(i));
                textView.setTag("unSelected");
                PoolTypeFlexBox.addView(textView);


                textView.setBackground(drawableUnCheked);
                textView.setTextColor(colorUnCheked);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP ,12f);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (v.getTag().toString().equals("unSelected")) {
                            v.setBackground(drawableCheked);
                            ((TextView) v).setTextColor(colorCheked);
                            v.setTag("Selected");
                        } else {
                            v.setBackground(drawableUnCheked);
                            ((TextView) v).setTextColor(colorUnCheked);
                            v.setTag("unSelected");
                        }
                        String metaWord = v.getTag(R.id.kind).toString()+ v.getTag(R.id.serch_ids).toString() ;
                        if (textView.getTag().toString().equals("Selected"))
                            metaWordsFromAllFragments.add(metaWord);
                        else
                            metaWordsFromAllFragments.remove(metaWord);
                        getDataFromServer(getMetaLink());
                    }
                });
            }
        }

    }


}