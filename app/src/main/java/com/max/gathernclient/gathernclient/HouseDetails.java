package com.max.gathernclient.gathernclient;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
public class HouseDetails extends AppCompatActivity {
    boolean isReload = false ;
    boolean fav = false ;
    LinearLayout mainLayout, setLayout;
    MyTextView allCapacity , cleanTextView , stuffTextView , chaletStatusTextView;
    ImageView favoriteImage ,loadingHeart ;
    Globals g;
    LinearLayout sets, poolLayout, wcLayout, elmarafeqlayout, marafeqElWC, bedRoomsLayout, marafeqElmatbakhLayout;
    // start define json data
    String id, chalet_id, title, chalet_title, code, price, views_count, photoUrl, city, area, check_in_hour, check_out_hour, chalet_for_text, cancelPolicy, reserveConditions, map_image, lat, lng, text;
    // Strings for reviews
    String unit_name, user_name, score_text, date, comment,clean_label ,clean_text ,
            staff_label ,staff_text ,value_label ,value_text ;
double cleanPoint ,staff_point ,value_point ;

    double score = 0.0;

    boolean isUnitAvailable, isPanorama = false, isRatingLayout = false;
    int starCount = 0, general_persons_capacity = 0, main_hall_capacity = 0, extra_hall_capacity = 0,
            outer_molhq_capacity = 0, food_table_capacity = 0, toilet_count = 0, outer_hall_capacity = 0,
            bedroom_single_count = 0, bedroom_double_count = 0, bedroom_master_count = 0, capacity = 0,
            capacityByArea = 0 , favouriteNumber ;
    double points = 0.0;
    ArrayList<String> amenitiesStringList = new ArrayList<>();
    ArrayList<String> galleryUrlString = new ArrayList<>();
    ArrayList<JSONObject> poolsObjectList = new ArrayList<>();
    ArrayList<MyPools> myPoolsArrayList = new ArrayList<>();
    ArrayList<String> poolsList = new ArrayList<>();
    ArrayList<String> kitchenStringList = new ArrayList<>();
    ArrayList<String> toiletStringList = new ArrayList<>();
    ArrayList<JSONObject> panoramaObjectList = new ArrayList<>();
    ArrayList<RatingPage.ReviewsClass> reviewsList = new ArrayList<>();
    ViewPager ratingPager, pager;
    CountDownTimer timerImage;
    CountDownTimer timerRating;
String response ="";// forward to submit booking activity
    // end part
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.house_details);
        g = new Globals(this);
        initRefrance();
        showLoadingImage();
        setLayout.setVisibility(View.GONE);
        mainLayout.setVisibility(View.GONE);
        getId();
        getDataFromServer();

    }
    private void checkHeart (){
        favoriteImage.setImageResource(R.drawable.fill_heart);
        fav = true ;
    }
    private void UnCheckHeart (){
        favoriteImage.setImageResource(R.drawable.empty_heart);
        fav = false ;
    }
    private void showLoadingHeart(){
        loadingHeart.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slow_rotation);
        animation.setInterpolator(new LinearInterpolator());
        loadingHeart.setAnimation(animation);
        favoriteImage.setVisibility(View.GONE);
    }
    private void hideLoadingHeart(){
        loadingHeart.setAnimation(null);
        loadingHeart.setVisibility(View.GONE);
        favoriteImage.setVisibility(View.VISIBLE);
    }
    private void initRefrance(){
    mainLayout = findViewById(R.id.mainLayout);
    setLayout = findViewById(R.id.setLayout);
    cleanTextView = findViewById(R.id.cleanTextView);
    stuffTextView = findViewById(R.id.stuffTextView);
    chaletStatusTextView = findViewById(R.id.chaletStatusTextView);
        favoriteImage = findViewById(R.id.favoriteImage);
        loadingHeart = findViewById(R.id.loadingHeart);
}
    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // setDate();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(isReload) {
            finish();
            startActivity(getIntent());
            isReload = false ;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.favoriteImage :
                addRemoveFavorite();
                break;
            case R.id.openMapImage:
                Intent openMap = new Intent(getBaseContext(), MapsActivity.class);
                openMap.putExtra("lat", lat);
                openMap.putExtra("lng", lng);
                startActivity(openMap);
                break;
//            case R.id.ImageShare:
//                String app_url = "https://play.google.com/store/apps/details?";
//                Intent share_app = new Intent(Intent.ACTION_SEND);
//                share_app.setType("text/plain");
//                share_app.putExtra(Intent.EXTRA_TEXT, app_url);
//                startActivity(Intent.createChooser(share_app, "مشاركة علي "));
//                break;
            case R.id.imageBack:
                finish();
                break;
            case R.id.bookNow:
                if (isUnitAvailable) {
                    if (g.getSingedState() == 1) {
                        Intent submitBooking = new Intent(getBaseContext(), SubmitBooking.class);
                        submitBooking.putExtra("ID",id);
                        if(id.length() > 0)
                        startActivity(submitBooking);
                        else Toast.makeText(getBaseContext(), " جرب تاريخ اخر" , Toast.LENGTH_LONG).show();
                    } else {
                        Intent open = new Intent(getBaseContext() ,MyAccount.class);
                        open.putExtra("ID",id);
                        startActivity(open);
                    }
                } else {
                    startActivity(new Intent(getBaseContext(), Calender.class));
                    isReload = true ;
                }
                break;
            case R.id.callNow:

//                Uri uri = Uri.parse("smsto:" + "+201007538470");
//                Intent i = new Intent(Intent.ACTION_SENDTO, uri);
//                i.putExtra("sms_body", "");
//                i.setPackage("com.whatsapp");
//                startActivity(i);
//                  Toast.makeText(getBaseContext(), " whatsapp" , Toast.LENGTH_LONG).show();

                Intent openTransActivity = new Intent(getBaseContext(), TransparentActivity.class);
                openTransActivity.putExtra("ID", "bottomDialog");
                startActivity(openTransActivity);


//                Intent call = new Intent(Intent.ACTION_DIAL);
//                call.setData(Uri.parse("tel:" + "920007858"));
//                startActivity(call);
                break;
            case R.id.frameBooking:
                startActivity(new Intent(getBaseContext(), Calender.class));
                isReload = true ;
                break;
            case R.id.dayNumber:
                startActivity(new Intent(getBaseContext(), Calender.class));
                isReload = true ;
                break;
            case R.id.dayText:
                startActivity(new Intent(getBaseContext(), Calender.class));
                isReload = true ;
                break;
            case R.id.month:
                startActivity(new Intent(getBaseContext(), Calender.class));
                isReload = true ;
                break;
            case R.id.cancelation:
                Intent open = new Intent(getBaseContext(), TermsAndCancelation.class);
                open.putExtra("cancelPolicy", cancelPolicy);
                open.putExtra("reserveConditions", reserveConditions);
                startActivity(open);
                break;
            case R.id.ratingPart:
                Intent openRating = new Intent(getBaseContext(), RatingPage.class);
                openRating.putExtra("ID", chalet_id);
                startActivity(openRating);
                break;
            case R.id.twitter :
                openTiwtter();
                break;
            case R.id.whatsapp :
                openWhatsApp();
                break;
        }

    }

    private void addRemoveFavorite() {
        showLoadingHeart();
        Runnable runnable = () -> {
            String api = "api/va/client/favourite/add";
            JSONObject param = new JSONObject();
            try {
                param.put("unit_id" ,id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String result = g.connectToApi(api, param, "POST");
            runOnUiThread(() -> {
                hideLoadingHeart();
                try {
                    JSONObject ob = new JSONObject(result);
                    boolean success = ob.optBoolean("success");
                    String message = ob.optString("message");
                    if (success){
                        changeFavorite();
                    }
                    Toast.makeText(getBaseContext(), message , Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void changeFavorite() {
        if(!fav){
            checkHeart();

        }else {
            UnCheckHeart();

        }
    }

    public void setDate_Calender_Button() {
        String dayNumber = g.getBookingDate("dayNumber");
        String dayText = g.getBookingDate("dayText");
        String month = g.getBookingDate("month");
        TextView DayText, DayNumber, Month;
        DayText = findViewById(R.id.dayText);
        DayNumber = findViewById(R.id.dayNumber);
        Month = findViewById(R.id.month);
        DayNumber.setText(dayNumber);
        DayText.setText(dayText);
        Month.setText(month);
        // set checkIn for button
        TextView BookNowButton = findViewById(R.id.bookNow);

        if (!isUnitAvailable) {
            BookNowButton.setText("غير متوفر في هذا التاريخ جرب تاريخ اخر");
            Drawable drawable = g.shapeColorString("#81BE79", g.getScreenDpi(5));
            BookNowButton.setBackground(drawable);
        } else {
            String d = "حجز" + "\n" + g.getCheckInDateForApi();
            BookNowButton.setText(d);
            Drawable drawable = g.shapeColorString("#34A938", g.getScreenDpi(5));
            BookNowButton.setBackground(drawable);
        }


    }

    public void getDataFromServer() {
        Runnable runnable = () -> {
            // String id = "714";
            String check_in = g.getCheckInDateForApi();
            String expand = "gallery,panorama,options,futureBusyDays,amenities,chalet,reviews,reviewsOverview";//
            String api = "api/va/chalet/main/unit?id=" + id + "&check_in=" + check_in + "&expand=" + expand;
            String result = g.connectToApi(api, null, "GET");
            runOnUiThread(() -> {
                hideLoadingImage();
                mainLayout.setVisibility(View.VISIBLE);
                setLayout.setVisibility(View.VISIBLE);
                updataJson(result);
                setTitleAndPriceAndNotes();
                initFragmentImages();
                initPanorama();
                initRatingPart();
                initFragmentRating();
                //changeimageTimer();
                if(isRatingLayout)
                //changeRatingTimer();
                createDetailsViews();
                setMapImage();
                setDate_Calender_Button();
if (favouriteNumber == 0)
    UnCheckHeart();
else
    checkHeart();

                response = result ;
            });
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void setTitleAndPriceAndNotes() {
        //set title
        TextView Title, ShowPrice;
        String headerTitle = chalet_title + " - " + title + "\n" + city + " - " + area;
        Title = findViewById(R.id.titleText);
        Title.setText(headerTitle);
        //set price
        ShowPrice = findViewById(R.id.showPrice);
        ShowPrice.setText(g.getPrice(price));
        // set notes for family
        String s = chalet_for_text + "\n" + "الدخول " + check_in_hour + "\n" + "المغادرة " + check_out_hour;
        TextView ForFamily = findViewById(R.id.forFamily);
        ForFamily.setText(s);
    }

    private ImageView autoColorImageView(ImageView imageView, int colorResourceId) {
        imageView.setColorFilter(ContextCompat.getColor(getBaseContext(), colorResourceId), PorterDuff.Mode.SRC_IN);
        return imageView;
    }

    private LinearLayout addRow(String text, int colorId, int checkIntForNullOrZero) {
        if (!text.isEmpty() && checkIntForNullOrZero > 0) {//&& checkIntForNullOrZero > 0
            int rowImage = R.drawable.baseline_keyboard_arrow_left_white_18;
            LinearLayout mHorLayout = new LinearLayout(this);
            mHorLayout.setOrientation(LinearLayout.HORIZONTAL);
            mHorLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            MyTextView myTextView = new MyTextView(this);
            myTextView.setText(text);
            myTextView.setTextSize(10f);
            myTextView.setTextColor(getResources().getColor(colorId));
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(rowImage);
            mHorLayout.addView(autoColorImageView(imageView, colorId));
            mHorLayout.addView(myTextView);
            return mHorLayout;
        } else {
            return new LinearLayout(this);
        }
    }

    private LinearLayout.LayoutParams getParam() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, g.getScreenDpi(20), 0);
        return params;
    }

    private void initFragmentImages() {
        // set fragment
        pager = findViewById(R.id.viewPager);
        final ArrayList<TabsFragment> tabList = new ArrayList<>();
        for (int i = 0; i < galleryUrlString.size(); i++) {
            Bundle bundle = new Bundle();
            bundle.putString("codeNumber", code);
            bundle.putString("coverPhotoUrl", galleryUrlString.get(i));
            bundle.putStringArrayList("galleryUrlString", galleryUrlString);// forward ArrayList to imageHouse Activity
            TabsFragment tabsFragment = new TabsFragment();
            tabsFragment.setArguments(bundle);
            tabList.add(tabsFragment);
        }
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), tabList);
        pager.setAdapter(adapter);
    }

    private void initFragmentRating() {
        // set fragment
        ratingPager = findViewById(R.id.ratingPager);
        final ArrayList<RatingFragment> tabList = new ArrayList<>();
        for (int i = 0; i < reviewsList.size(); i++) {
            Bundle bundle = new Bundle();
            RatingPage.ReviewsClass reviewsClass = reviewsList.get(i);
            bundle.putString("user_name", reviewsClass.getUser_name());
            bundle.putString("unit_name", reviewsClass.getUnit_name());
            bundle.putString("comment", reviewsClass.getComment());
            bundle.putString("date", reviewsClass.getDate());
            bundle.putString("scoreText", reviewsClass.getScore_text());
            bundle.putDouble("scoreDouble", reviewsClass.getScore());
            RatingFragment ratingFragment = new RatingFragment();
            ratingFragment.setArguments(bundle);
            tabList.add(ratingFragment);
        }
        PagerRatingAdapter adapter = new PagerRatingAdapter(getSupportFragmentManager(), tabList);
        ratingPager.setAdapter(adapter);
        ratingPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                setViewsAsChecked(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

    }

    private void initPanorama() {
        // banner 360 show
        LinearLayout panoramaLayout = findViewById(R.id.panoramaLayout);
        if (isPanorama) panoramaLayout.setVisibility(View.VISIBLE);
        else panoramaLayout.setVisibility(View.GONE);
        ArrayList<String> panoramaImgUrl = new ArrayList<>();
        ArrayList<String> panoramaIframeUrl = new ArrayList<>();

        for (int i = 0; i < panoramaObjectList.size(); i++) {
            try {
                panoramaImgUrl.add(panoramaObjectList.get(i).getString("thumb"));
                panoramaIframeUrl.add(panoramaObjectList.get(i).getString("mobileSrc"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        final ArrayList<BannerClass> imghouseList = new ArrayList<>();
        for (int i = 0; i < panoramaImgUrl.size(); i++) {
            imghouseList.add(new BannerClass(panoramaImgUrl.get(i), g.getScreenDpi(100), g.getScreenDpi(100)));
        }
        LinearLayoutManager imglinearLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        BannerAdapter imgAdapter = new BannerAdapter(imghouseList, this);
        RecyclerView imgRecyclerView = findViewById(R.id.imgRecyclerView);
        imgRecyclerView.setLayoutManager(imglinearLayoutManager);
        imgRecyclerView.setAdapter(imgAdapter);
        imgAdapter.setmOnEntryClickListener(new BannerAdapter.OnEntryClickListener() {
            @Override
            public void onEntryClick(View view, int position) {
                String ifarameUrl = panoramaIframeUrl.get(position);
                Intent open = new Intent(getBaseContext(), MyPanorama.class);
                open.putExtra("URL", ifarameUrl);
                startActivity(open);
            }
        });
    }

    private void initRatingPart() {
        // show or hide rating part
        LinearLayout ratingLinearLayout = findViewById(R.id.ratingLinearLayout);
        if (isRatingLayout) ratingLinearLayout.setVisibility(View.VISIBLE);
        else ratingLinearLayout.setVisibility(View.GONE);

//        View roundViewLite = findViewById(R.id.roundViewLite);
//        roundViewLite.setBackground(g.shape(R.color.viewLiteGray, 60, 0, 0));
        String pointString = String.valueOf(points);
        String s1 = pointString + "\n" + text;
        SpannableString ss1 = new SpannableString(s1);
        ss1.setSpan(new RelativeSizeSpan(2f), 0, pointString.length(), 0);
        ss1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.mywhite)), 0, pointString.length(), 0);
        TextView rating, readMore;
        rating = findViewById(R.id.rating);
        readMore = findViewById(R.id.readMore);
        rating.setText(ss1);
        SpannableString ss2 = new SpannableString("ضيوف حقيقين وآراء حقيقية.اقرأ المزيد");
        ss2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 25, 36, 0);
        readMore.setText(ss2);
        // set main rating
        String z = clean_label +" "+clean_text ;
        cleanTextView.setText(z);
        String z1 = staff_label + " "+staff_text ;
        stuffTextView.setText(z1);
        String z2 = value_label +" "+value_text ;
        chaletStatusTextView.setText(z2);

        setRatingProgress(cleanPoint ,R.id.cleanHorLayout);
        setRatingProgress(staff_point ,R.id.stuffHorLayout);
        setRatingProgress(staff_point ,R.id.statuesHorLayout);
    }

    private void updataJson(String serverResponse) {
        try {
            JSONObject mainObject = new JSONObject(serverResponse);
            JSONObject chalet = mainObject.optJSONObject("chalet");
            JSONObject totalReview = chalet.optJSONObject("totalReview");
            JSONObject coverphoto = mainObject.optJSONObject("coverphoto");
            JSONArray galleryArray = mainObject.optJSONArray("gallery");
            JSONObject address = mainObject.optJSONObject("address");
            JSONArray reviews = mainObject.optJSONArray("reviews");
            JSONObject reviewsOverview = mainObject.optJSONObject("reviewsOverview");
            JSONObject data = reviewsOverview.optJSONObject("data");
            JSONObject clean = data.optJSONObject("clean");
            JSONObject staff = data.optJSONObject("staff");
            JSONObject value = data.optJSONObject("value");
            JSONObject favourite = mainObject.optJSONObject("favourite");
            if(favourite != null)
            favouriteNumber = favourite .optInt("favourite");

            // clean_label ,clean_text ,staff_label ,staff_text ,value_label ,value_text ;
            clean_label = clean.optString("label");
            cleanPoint = clean.optDouble("points");
            clean_text = clean.optString("text");

            staff_label = staff.optString("label");
            staff_point = staff.optDouble("points");
            staff_text = staff.optString("text");

            value_label = value.optString("label");
            value_point = value.optDouble("points");
            value_text = value.optString("text");

            for (int i = 0; i < (reviews.length() <= 3 ? reviews.length() : 3); i++) {

                unit_name = reviews.optJSONObject(i).optString("unit_name");
                user_name = reviews.optJSONObject(i).optString("user_name");
                score_text = reviews.optJSONObject(i).optString("score_text");
                date = reviews.optJSONObject(i).optString("date");
                comment = reviews.optJSONObject(i).optString("comment");
                score = reviews.optJSONObject(i).optDouble("score");

                reviewsList.add(new RatingPage.ReviewsClass(unit_name, user_name, score_text, date, comment, score));
            }
            JSONArray panorama = mainObject.optJSONArray("panorama");
            JSONArray amenitiesArray = mainObject.optJSONArray("amenities");
            for (int i = 0; i < amenitiesArray.length(); i++) {
                amenitiesStringList.add(amenitiesArray.optJSONObject(i).optString("title"));
            }
            isPanorama = panorama.length() != 0;
            for (int i = 0; i < panorama.length(); i++) {
                panoramaObjectList.add(panorama.optJSONObject(i));
            }
            JSONObject options = mainObject.optJSONObject("options");
            JSONObject main = options.optJSONObject("main");
            JSONArray pools = options.optJSONArray("pools");
            JSONObject toilets = options.optJSONObject("toilets");
            if (toilets != null) checkToilet(toilets);
            for (int i = 0; i < pools.length(); i++) {
                poolsObjectList.add(pools.optJSONObject(i));
            }

            //int depth_from, int depth_to, int area_w, int area_h, String typeText, String styleText

            for (int i = 0; i < poolsObjectList.size(); i++) {
                JSONObject ob = poolsObjectList.get(i);
                myPoolsArrayList.add(new MyPools(ob.optInt("depth"), ob.optInt("depth_from"), ob.optInt("depth_to")
                        , ob.optInt("area_w"), ob.optInt("area_h"), ob.optString("typeText"), ob.optString("styleText")));
            }
            for (int i = 0; i < myPoolsArrayList.size(); i++) {
                MyPools myPools = myPoolsArrayList.get(i);
                poolsList.add(myPools.getPool());
            }
            JSONArray kitchen = options.optJSONArray("kitchen");
            if (kitchen != null)
                for (int i = 0; i < kitchen.length(); i++) {
                    kitchenStringList.add(kitchen.optJSONObject(i).optString("name"));
                }
            for (int i = 0; i < galleryArray.length(); i++) {
                galleryUrlString.add(galleryArray.optJSONObject(i).optString("full"));
            }
            //chalet object
            check_in_hour = chalet.optString("check_in_hour");
            check_out_hour = chalet.optString("check_out_hour");
            chalet_for_text = chalet.optString("chalet_for_text"); // مخصص للعوائل
            cancelPolicy = chalet.optString("cancelPolicy");
            reserveConditions = chalet.optJSONArray("reserveConditions").optString(0);
            map_image = chalet.optString("map_image");
            lat = chalet.optString("lat");
            lng = chalet.optString("lng");
            //
            city = address.optString("city");
            area = address.optString("area");
            photoUrl = coverphoto.optString("full");
            chalet_id = mainObject.optString("chalet_id");
            title = mainObject.optString("title");// used in submit booking
            chalet_title = mainObject.optString("chalet_title");// used in submit booking
            code = mainObject.optString("code");// used in submit booking
            capacity = mainObject.optInt("capacity");
            capacityByArea = mainObject.optInt("area");
            price = mainObject.optString("price");// used in submit booking
            views_count = mainObject.optString("views_count");
            isUnitAvailable = mainObject.optBoolean("isUnitAvailable");
            starCount = mainObject.optInt("starCount");


            general_persons_capacity = main.optInt("general_persons_capacity");//بشكل عام تسع ل 00 شخص
            main_hall_capacity = main.optInt("main_hall_capacity");// مجلس رئيسي يتسع ل 00 شخص
            extra_hall_capacity = main.optInt("extra_hall_capacity");// مجلس إضافي يتسع ل 00 شخص
            outer_hall_capacity = main.optInt("outer_hall_capacity");// جلسة خارجية تتسع ل 00 شخص
            outer_molhq_capacity = main.optInt("outer_molhq_capacity");// ملحق خارجي يتسع ل 00 شخص
            food_table_capacity = main.optInt("food_table_capacity");// طاولة طعام تتسع ل 00 شخص

            toilet_count = main.optInt("toilet_count");// عدد دورات المياة
            bedroom_single_count = main.optInt("bedroom_single_count");// غرف نوم فردي
            bedroom_double_count = main.optInt("bedroom_double_count");// غرف نوم دبل
            bedroom_master_count = main.optInt("bedroom_master_count");// غرف نومم ماستر
            points = totalReview.optDouble("points");
            if (points == 0)
                isRatingLayout = false;
            else isRatingLayout = true;
            text = totalReview.optString("text");
          //  Toast.makeText(getBaseContext(), "chalet_id = "+chalet_id +"unit id ="+id, Toast.LENGTH_LONG).show();


        } catch (JSONException e) {
            e.printStackTrace();
                    Toast.makeText(getBaseContext(), "e = " +e, Toast.LENGTH_LONG).show();

        }
    }

    private void createDetailsViews() {
        int NO_INT_VALUE = 1;
        // set sub title
        String s = chalet_title + " - " + title;
        TextView subTitle = findViewById(R.id.subTitle);
        subTitle.setText(s);
        //المجالس والجلسات
        sets = findViewById(R.id.sets);
        sets.addView(addRow("بشكل عام تسع ل " + general_persons_capacity + "شخص", R.color.myorange, general_persons_capacity));
        sets.addView(addRow("مجلس رئيسي يتسع ل " + main_hall_capacity + "شخص", R.color.myorange, main_hall_capacity));
        sets.addView(addRow("مجلس إضافي يتسع ل " + extra_hall_capacity + "شخص", R.color.myorange, extra_hall_capacity));
        sets.addView(addRow("مجلس خارجي يتسع ل" + outer_hall_capacity + "شخص", R.color.myorange, outer_hall_capacity));
        sets.addView(addRow("ملحق خارجي يسع ل " + outer_molhq_capacity + "شخص", R.color.myorange, outer_molhq_capacity));
        sets.addView(addRow("طاولة طعام تتسع ل " + food_table_capacity + "شخص", R.color.myorange, food_table_capacity));
        //المسابح Array
        poolLayout = findViewById(R.id.poolLayout);
        if (poolsList.size() > 0) {
            for (int i = 0; i < poolsList.size(); i++) {
                poolLayout.addView(addRow(poolsList.get(i), R.color.myliteblue, NO_INT_VALUE));
            }
        } else poolLayout.setVisibility(View.GONE);
        //دورات المياة
        wcLayout = findViewById(R.id.wcLayout);
        wcLayout.addView(addRow(String.valueOf(toilet_count) + "دورات مياة", R.color.myblue, toilet_count));
        //المرافق Array
        elmarafeqlayout = findViewById(R.id.elmarafeqlayout);
        if (amenitiesStringList.size() > 0) {
            for (int i = 0; i < amenitiesStringList.size(); i++) {
                elmarafeqlayout.addView(addRow(amenitiesStringList.get(i), R.color.mygreen, NO_INT_VALUE));
            }
//            LinearLayout hor1Layout = (LinearLayout) elmarafeqlayout.getChildAt(1);
//            LinearLayout hor2Layout = addRow("ركن شواء", R.color.mygreen, NO_INT_VALUE);
//            hor2Layout.setLayoutParams(getParam());
//            hor1Layout.addView(hor2Layout);
        } else elmarafeqlayout.setVisibility(View.GONE);
        //مرافق دورات المياة ArrayList
        marafeqElWC = findViewById(R.id.marafeqElWC);
        for (int i = 0; i < toiletStringList.size(); i++) {
            marafeqElWC.addView(addRow(toiletStringList.get(i), R.color.myblue, NO_INT_VALUE));
        }
        //غرف النوم
        bedRoomsLayout = findViewById(R.id.bedRoomsLayout);
        bedRoomsLayout.addView(addRow(String.valueOf(bedroom_master_count) + "غرفة نوم ماستر", R.color.myblue, bedroom_master_count));
        bedRoomsLayout.addView(addRow(String.valueOf(bedroom_double_count) + "غرفة نوم دبل", R.color.myblue, bedroom_double_count));
        bedRoomsLayout.addView(addRow(String.valueOf(bedroom_single_count) + "غرفة نوم مفردة", R.color.myblue, bedroom_single_count));

        //مرافق المطبخ Array
        marafeqElmatbakhLayout = findViewById(R.id.marafeqElmatbakhLayout);
        if (kitchenStringList.size() > 0) {
            for (int i = 0; i < kitchenStringList.size(); i++) {
                marafeqElmatbakhLayout.addView(addRow(kitchenStringList.get(i), R.color.myblue, NO_INT_VALUE));
            }
        } else marafeqElmatbakhLayout.setVisibility(View.GONE);
        //المساحة
        allCapacity = findViewById(R.id.allCapacity);
        int x = capacity * capacityByArea;
        String trueCapacity = "المساحة" + x + "م";
        allCapacity.setText(trueCapacity);
    }

    private void getId() {
        id = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("ID")).toString();
   }

    private class MyPools {
        int depth_from, depth_to, area_w, area_h, depth;
        String typeText, styleText;

        public MyPools(int depth, int depth_from, int depth_to, int area_w, int area_h, String typeText, String styleText) {
            this.depth_from = depth_from;
            this.depth_to = depth_to;
            this.area_w = area_w;
            this.area_h = area_h;
            this.typeText = typeText;
            this.styleText = styleText;
            this.depth = depth;
        }

        public String getPool() {
            return typeText + "\n"
                    + styleText + s1() + "\n"
                    + "ابعاد المسبح " + "(" + area_w + "*" + area_h + ")" + " امتار" + "\n"
                    + s2();
        }

        private String s1() {
            return depth_from != 0 && depth_to != 0 ? " (من " + depth_from + " الي " + depth_to + ") سم" : "";
        }

        private String s2() {
            return depth != 0 ? "عمق المسبح " + depth + " سم " : "";
        }
    }

    private void checkToilet(JSONObject toilets) {
        String[] index = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        for (int i = 0; i < index.length; i++) {
            if (toilets.has(index[i])) {
                try {
                    toiletStringList.add(toilets.getJSONObject(index[i]).optString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
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

    private void setMapImage() {
        ImageView openMapImage = findViewById(R.id.openMapImage);
        Picasso.with(this).load(map_image).into(openMapImage);

    }

    private class PagerRatingAdapter extends FragmentPagerAdapter {

        public ArrayList<RatingFragment> fragmentsList;

        public PagerRatingAdapter(android.support.v4.app.FragmentManager fm, ArrayList<RatingFragment> fragmentsList) {
            super(fm);
            this.fragmentsList = fragmentsList;
        }

        @Override
        public Fragment getItem(int position) {

            return fragmentsList.get(position);

        }

        @Override
        public int getCount() {
            return fragmentsList.size();
        }

    }

    private void setViewsAsChecked(int postion) {
        View v1 = findViewById(R.id.round1);
        View v2 = findViewById(R.id.round2);
        View v3 = findViewById(R.id.round3);
        Drawable checked = g.shapeColorString("#808080", g.getScreenDpi(60));
        Drawable unChecked = g.shapeColorString("#F3F3F3", g.getScreenDpi(60));
        switch (postion) {
            case 0:
                v1.setBackground(checked);
                v2.setBackground(unChecked);
                v3.setBackground(unChecked);

                break;
            case 1:
                v1.setBackground(unChecked);
                v2.setBackground(checked);
                v3.setBackground(unChecked);
                break;
            case 2:
                v1.setBackground(unChecked);
                v2.setBackground(unChecked);
                v3.setBackground(checked);
                break;

        }

    }

//    private void changeRatingTimer() {
//        int pageCount = Objects.requireNonNull(ratingPager.getAdapter()).getCount();
//        timerRating = new CountDownTimer(pageCount * 2000, 2000) {
//            int page = 0;
//
//            @Override
//            public void onTick(long millisUntilFinished) {
//                ratingPager.setCurrentItem(page);
//                if (page < pageCount)
//                    page++;
//                else page = 0;
//            }
//
//            @Override
//            public void onFinish() {
//                timerRating.cancel();
//                timerRating.start();
//                freeMemory();
//
//            }
//        }.start();
//
//    }

//    private void changeimageTimer() {
//        int pageCount = Objects.requireNonNull(pager.getAdapter()).getCount();
//
//        timerImage = new CountDownTimer(pageCount * 2000, 2000) {
//            int page = 0;
//
//            @Override
//            public void onTick(long millisUntilFinished) {
//                pager.setCurrentItem(page);
//                if (page < pageCount)
//                    page++;
//                else page = 0;
//            }
//
//            @Override
//            public void onFinish() {
////                timerImage.cancel();
//                timerImage.start();
//                freeMemory();
//
//            }
//        }.start();
//
//    }

    public void freeMemory() {
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }
    private void openWhatsApp(){
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
        if(launchIntent !=null)
            startActivity(launchIntent);
        else
            Toast.makeText(getBaseContext(), "لا يوجد تطبيق واتساب علي هذا الهاتف", Toast.LENGTH_LONG).show();

    }
    private void openTiwtter(){
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.twitter.android");
        if(launchIntent !=null)
            startActivity(launchIntent);
        else
                   Toast.makeText(getBaseContext(), "لا يوجد تطبيق تويتر علي هذا الهاتف", Toast.LENGTH_LONG).show();
    }
    private void setRatingProgress(double rate ,int LayoutId){
        LinearLayout cleanLayout = findViewById(LayoutId);

        for(int i = 0 ; i<10;i++){
            View view = new View(this);
            if(i <rate)
            view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.weight =1f ;
            view.setLayoutParams(params);
            cleanLayout.addView(view);
        }
    }
}
