package com.max.gathernclient.gathernclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class RatingPage extends AppCompatActivity {
Globals g ;
ArrayList<ReviewsClass> reviewsList = new ArrayList<>();
String id = "" , text = "";
double points = 0.0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating_page);
        g = new Globals(this);
//        initRatingHeader();
//        setRatingProgress(5 ,"مقبول" , R.id.cleanLayout,R.id.cleanText);
//        setRatingProgress(8 ,"رائع" , R.id.stuffLayout,R.id.stuffText);
//        setRatingProgress(10 ,"ممتاز" , R.id.chaletLayout,R.id.chaletText);
       // comments ();
        getId();
        getDataFromServer();
    }
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageBack :
                finish();
                break;
        }
    }
    private void initRatingHeader(){
        LinearLayout ratingHeader = findViewById(R.id.ratingHeader);
        ratingHeader.setBackground(g.shape(R.color.mygray, g.getScreenDpi(5),0,0 ));
        String s1 = "9.3" + "\n" + "رائع";
        SpannableString ss1 = new SpannableString(s1);
        ss1.setSpan(new RelativeSizeSpan(2f), 0, 3, 0);
        ss1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.mywhite)), 0, 3, 0);
        MyTextView rating = findViewById(R.id.rating);
        rating.setText(ss1);
    }
    private void setRatingProgress(int rate , String textDegree ,int LayoutId , int textId){
        LinearLayout cleanLayout = findViewById(LayoutId);
        MyTextView textView = findViewById(textId);
        String currentText = textView.getText().toString();
        String s = currentText + textDegree +" " + rate ;
        textView.setText(s);
        for(int i = 0 ; i<rate;i++){
            View view = new View(this);
            view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(g.getScreenDpi(15 ), LinearLayout.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(params);
            cleanLayout.addView(view);
        }
    }

    public void getDataFromServer() {
        showLoadingImage();
        Runnable runnable = () -> {
            // String id = "714";
            String check_in = g.getCheckInDateForApi();
          //  http://gathern.co/api/va/chalet/main/view?id=395&expand=reviews
            String expand = "reviews,reviewsOverview";//
            String api = "api/va/chalet/main/view?id=" + id+ "&check_in=" + check_in + "&expand=" + expand;
            String result = g.connectToApi(api, null, "GET");
            runOnUiThread(() -> {
                hideLoadingImage();
                updateJson(result);
                initRecyclerView();

            });
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
    private void updateJson(String result){
        String unit_name , user_name , score_text , date , comment ;
        String clean_label ,clean_text , staff_label ,staff_text ,value_label , value_text ; // for main reviews bar
        double cleanPoint , staff_point , value_point ;// for main reviews bar
        double score ;
        try {
            JSONObject mainObject = new JSONObject(result);
            JSONArray reviews = mainObject.optJSONArray("reviews");
            JSONObject totalReview = mainObject.optJSONObject("totalReview");
            text = totalReview.optString("text");
            points = totalReview.optDouble("points");

            JSONObject reviewsOverview = mainObject.optJSONObject("reviewsOverview");
            JSONObject data = reviewsOverview.optJSONObject("data");
            JSONObject clean = data.optJSONObject("clean");
            JSONObject staff = data.optJSONObject("staff");
            JSONObject value = data.optJSONObject("value");

            clean_label = clean.optString("label");
            cleanPoint = clean.optDouble("points");
            clean_text = clean.optString("text");

            staff_label = staff.optString("label");
            staff_point = staff.optDouble("points");
            staff_text = staff.optString("text");

            value_label = value.optString("label");
            value_point = value.optDouble("points");
            value_text = value.optString("text");

            for(int i =0 ; i<reviews.length();i++){
                unit_name = reviews.optJSONObject(i).optString("unit_name");
                user_name = reviews.optJSONObject(i).optString("user_name");
                comment = reviews.optJSONObject(i).optString("comment");
                score = reviews.optJSONObject(i).optDouble("score");
                score_text = reviews.optJSONObject(i).optString("score_text");
                date = reviews.optJSONObject(i).optString("date");
                reviewsList.add(new ReviewsClass(unit_name ,user_name ,score_text , date , comment , score));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public static class ReviewsClass {
        String unit_name , user_name , score_text , date  ,comment;
        double score ;
        public ReviewsClass (String unit_name ,String user_name , String score_text ,String date , String comment, double score){
            this.user_name = user_name ;
            this.unit_name = unit_name ;
            this.score_text = score_text ;
            this.date = date ;
            this.score = score ;
            this.comment = comment ;
        }

        public String getComment() {
            return comment;
        }

        public double getScore() {
            return score;
        }

        public String getDate() {
            return date;
        }

        public String getScore_text() {
            return score_text;
        }

        public String getUnit_name() {
            return unit_name;
        }

        public String getUser_name() {
            return user_name;
        }
    }

private void initRecyclerView(){
    RecyclerView ratingRecyclerView = findViewById(R.id.ratingRecyclerView);
    ReviewsAdapter adapter = new ReviewsAdapter(reviewsList , this , String.valueOf(points) , text);
    LinearLayoutManager linearLayoutManager =
            new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    ratingRecyclerView.setAdapter(adapter);
    ratingRecyclerView.setLayoutManager(linearLayoutManager);


}
    private void getId() {
        id = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("ID")).toString();
//        if (id.isEmpty())
//            Toast.makeText(getBaseContext(), "error get ID", Toast.LENGTH_LONG).show();
//        else Toast.makeText(getBaseContext(), "ID = " + id, Toast.LENGTH_LONG).show();
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
}
