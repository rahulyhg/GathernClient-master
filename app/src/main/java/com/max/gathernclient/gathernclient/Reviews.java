package com.max.gathernclient.gathernclient;


// require to open
//        openReview.putExtra("imageUrl",imageUrl);
//        openReview.putExtra("chaletName",chaletName);
//        openReview.putExtra("unitName",unitName);
//        openReview.putExtra("check_in_label",check_in_label);
//        openReview.putExtra("check_in",check_in);
//        openReview.putExtra("total",total);
//         openReview.putExtra("id",id);

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Reviews extends AppCompatActivity {
Globals g ;
LinearLayout mainLayout  , headerLayout , ratingPartLayout;
ImageView headImage ;
TextView headTitle  , textChaletAndUnit , textDay , sendBtn;
EditText comment ;
String imageUrl ,chaletName ,unitName ,check_in_label ,check_in  , comments;
int total , id ;
    // keys
    String count_clean ="",count_staff  ="",count_value  ="",count_reality  ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reviews);
        g = new Globals(this);
        getParams();
        initRefrance();
        createLayout("النظافة" ,"متسخ جداً" ,"نظيف جداً" ,"count_clean");
        createLayout("طاقم عمل الشاليه (الحارس - مديرالحجوزات)" ,"ضعيف جداً" ,"ممتاز" ,"count_staff");
        createLayout("حالة الشاليه" ,"قديم مستهلك" , "جديد مرمم" , "count_value");
        createLayout("خدمة موقع / تطبيق قاذر إن" ,"لن أستخدمه مرة اخرى" ,"طبعاً سأستخدمه و أنصح به" , "count_reality");
        setShapes();
        setHeaderData();
    }
    private void initRefrance(){
        sendBtn = findViewById(R.id.sendBtn);
        comment = findViewById(R.id.comment);
        textDay = findViewById(R.id.textDay);
        textChaletAndUnit = findViewById(R.id.textChaletAndUnit);
        headTitle = findViewById(R.id.headTitle);
        ratingPartLayout = findViewById(R.id.ratingPartLayout);
        headerLayout = findViewById(R.id.headerLayout);
        mainLayout = findViewById(R.id.mainLayout);
        headImage = findViewById(R.id.headImage);
    }
    private  void setShapes (){
        headerLayout.setBackground(g.shapeColorString("#F3F3F3" ,g.getScreenDpi(5)));
        headImage.setBackground(g.shapeColorString("#ffffff",g.getScreenDpi(10)));
        headImage.setClipToOutline(true);
    }
    private void setHeaderData (){
        String title = "تقيمات شاليهات " +chaletName ;
        headTitle.setText(title);
        String chalet = "ل " + chaletName +" " +unitName ;
        textChaletAndUnit.setText(chalet);
        String day = "يوم "+check_in_label +" "+check_in + "السعر" +total +" SAR ";
        textDay.setText(day);
        Picasso.with(this).load(imageUrl).fit().into(headImage);
    }
    private void createLayout (String kind , String low , String heigh , String tag){

        // main layout
        LinearLayout ver = new LinearLayout(this);
        ver.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams verParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        verParam.setMargins(0,0,0,g.getScreenDpi(8));
        ver.setLayoutParams(verParam);
        ver.setBackground(g.shape(0, g.getScreenDpi(5), 1, R.color.mydarkgray));
        ver.setPadding(g.getScreenDpi(4), g.getScreenDpi(4), g.getScreenDpi(4), g.getScreenDpi(4));
// text view
        MyTextView clean = new MyTextView(this);
        String s1 = kind +  " : ";
        clean.setText(s1);
        clean.setTextColor(Color.parseColor("#1B1B1B"));
        clean.setTextSize(TypedValue.COMPLEX_UNIT_SP ,12F);


        // hor layout
        LinearLayout hor = new LinearLayout(this);
        hor.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams horParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        horParam.setMargins(0,0,0,0);
        hor.setLayoutParams(horParam);
        hor.setPadding(g.getScreenDpi(4), g.getScreenDpi(4), g.getScreenDpi(4), g.getScreenDpi(4));


        LinearLayout.LayoutParams textParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        textParam.setMargins(g.getScreenDpi(1),g.getScreenDpi(1) , g.getScreenDpi(1) ,g.getScreenDpi(1));
        textParam.weight =1f ;

        MyTextView veryBad = new MyTextView(this);
        veryBad.setText(low);
        veryBad.setTextColor(Color.parseColor("#1B1B1B"));
        veryBad.setLayoutParams(textParam);
        veryBad.setTextSize(TypedValue.COMPLEX_UNIT_SP ,12F);


        MyTextView veryGood = new MyTextView(this);

        veryGood.setText(heigh);
        veryGood.setTextColor(Color.parseColor("#1B1B1B"));
         veryGood.setLayoutParams(textParam);
         veryGood.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
         veryGood.setTextSize(TypedValue.COMPLEX_UNIT_SP ,12F);


        hor.addView(veryBad);
        hor.addView(veryGood);
        ver.addView(clean);
        ver.addView(hor);
        // hor layout for btns
        LinearLayout horLayoutBtn = new LinearLayout(this);
        horLayoutBtn.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams horBtnparam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        horLayoutBtn.setLayoutParams(horBtnparam);
        // create 10 btns choices
         for (int i = 1 ; i <= 10 ; i++){
            MyTextView btn = new MyTextView(this);
            String points = ""+i ;
            btn.setText(points);
            btn.setTextColor(Color.parseColor("#1B1B1B"));
            btn.setBackground(g.shape(0, g.getScreenDpi(5), 1, R.color.mydarkgray));
            btn.setLayoutParams(textParam);
            btn.setPadding(g.getScreenDpi(2) , g.getScreenDpi(2) , g.getScreenDpi(2) , g.getScreenDpi(2));
           btn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            horLayoutBtn.addView(btn);
            btn.setTag(tag);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView currentText = (TextView) v;
                    String tag = v.getTag().toString();
                   // Toast.makeText(getBaseContext(), tag +currentText.getText().toString(), Toast.LENGTH_SHORT).show();
                    String cuNumber = currentText.getText().toString() ;
                    setBtnChecked(currentText);
                    // loop to remove checked
                    for(int i = 0 ; i<horLayoutBtn.getChildCount();i++) {
                  TextView random = (TextView) horLayoutBtn.getChildAt(i);
                  if(!random.getText().toString().equals(cuNumber)){
                      setBtnUnChecked(random);
                  }
                    }
                    switch (tag){
                        case "count_clean" :
                            count_clean = currentText.getText().toString() ;
                            break;
                        case "count_staff" :
                            count_staff = currentText.getText().toString() ;
                            break;
                        case "count_value" :
                            count_value = currentText.getText().toString() ;
                            break;
                        case "count_reality" :
                            count_reality = currentText.getText().toString() ;
                            break;
                    }

                }
            });
        }
        ver.addView(horLayoutBtn);
        ratingPartLayout.addView(ver);
    }
  private void  getParams (){
      imageUrl = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).getString("imageUrl"));
      chaletName = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).getString("chaletName"));
      unitName = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).getString("unitName"));
      check_in_label = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).getString("check_in_label"));
      check_in = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).getString("check_in"));
      total = Objects.requireNonNull(getIntent().getExtras()).getInt("total");
      id = Objects.requireNonNull(getIntent().getExtras()).getInt("id");
  }
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sendBtn :
                comments = comment.getText().toString();
                if(!count_clean.equals("")&&!count_reality.equals("")
                        &&!count_staff.equals("")&&!count_value.equals("")&&!comments.equals(""))
                sendReview();
                else
                    Toast.makeText(getBaseContext(), "يجب إختيار كافة الحقول", Toast.LENGTH_SHORT).show();

                break;
        }
    }
    public void sendReview (){
showLoadingImage();
        Runnable runnable = () ->{
            JSONObject param = new JSONObject();
            try {
                param.put("reservation_id",id);
                param.put("comment",comments);
                param.put("count_clean",count_clean);
                param.put("count_staff",count_staff);
                param.put("count_value",count_value);
                param.put("count_reality",count_reality);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            String result = g.connectToApi("api/vb/client/reviews/create",param , "POST");
            runOnUiThread(()->{
                hideLoadingImage();
                try {
                    JSONObject ob = new JSONObject(result);
                    boolean success = ob.optBoolean("success");
                    String msg = ob.optString("message");
                    if (success) {
                        startActivity(new Intent(Reviews.this, HomePage.class));
                        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //Toast.makeText(getBaseContext(), "result ="+result, Toast.LENGTH_SHORT).show();

            });
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
private void setBtnChecked (TextView btn){
    btn.setBackground(g.shape(R.color.colorPrimary, g.getScreenDpi(5), 0, 0));
    btn.setTextColor(Color.parseColor("#ffffff"));
}
    private void setBtnUnChecked (TextView btn){
        btn.setBackground(g.shape(0, g.getScreenDpi(5), 1, R.color.mydarkgray));
        btn.setTextColor(Color.parseColor("#1B1B1B"));

    }
    private void showLoadingImage() {
        ImageView loadingImage = findViewById(R.id.loading);
        loadingImage.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotation);
        animation.setInterpolator(new LinearInterpolator());
        loadingImage.setAnimation(animation);
        sendBtn .setBackgroundColor(getResources().getColor(R.color.transprimary));
        sendBtn.setEnabled(false);
    }

    private void hideLoadingImage() {
        ImageView loadingImage = findViewById(R.id.loading);
        loadingImage.setAnimation(null);
        loadingImage.setVisibility(View.GONE);
        sendBtn.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        sendBtn.setEnabled(true);
    }
}
