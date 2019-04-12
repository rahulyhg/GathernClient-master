package com.max.gathernclient.gathernclient;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Objects;

public class InfoBookingCOD extends AppCompatActivity {
TextView headText ;
    TextView textOnImage  , reserve_id , reserve_number , chalet,unit , check_in
            , unitPrice ,discount , totalAmount , note;
    String mainObject = "", timeToPay = "" , reservation_id , chaletName , unitName
            ,check_in_label , couponDiscount , insurance , downPayment;
    int day_price , total;
    Globals g ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_booking_cod);
        g = new Globals(this);
        getId();
        initReferanceAndShape();
        if(mainObject.length()>0){
            getJson ();
            setText ();
        }
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageBack :
                startActivity(new Intent(getBaseContext(), MyBooking.class));

                finish();
                break;
        }
    }
    private void getId() {
        try {
            mainObject = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("mainObject")).toString();
            downPayment = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("price")).toString();

            // price
        } catch (NullPointerException e) {
            Toast.makeText(getBaseContext(), "NullPointerException =" + e, Toast.LENGTH_LONG).show();
        }
//        if (mainObject.isEmpty())
//            Toast.makeText(getBaseContext(), "error get mainObject", Toast.LENGTH_LONG).show();
//        else Toast.makeText(getBaseContext(), "mainObject = " + mainObject, Toast.LENGTH_LONG).show();
    }
    private void initReferanceAndShape (){
        headText = findViewById(R.id.headText);
        note = findViewById(R.id.note);
        totalAmount = findViewById(R.id.total);
        discount = findViewById(R.id.discount);
        unitPrice = findViewById(R.id.unitPrice);
        check_in = findViewById(R.id.check_in);
        unit = findViewById(R.id.unit);
        chalet = findViewById(R.id.chalet);
        reserve_number = findViewById(R.id.reserve_number);
        reserve_id = findViewById(R.id.reserve_id);
       // timeToPayTextView = findViewById(R.id.timeToPayTextView);
        textOnImage = findViewById(R.id.textOnImage);
    }
    private void setText (){
//        String s1 ="وعندك مهلة "+timeToPay ;
//        timeToPayTextView.setText(s1); null text view
        String s2 = "تفاصيل الحجز رقم " +reservation_id ;
        reserve_id.setText(s2);
        reserve_number.setText(reservation_id);
        chalet.setText(chaletName);
        unit.setText(unitName);
        String s3 =check_in_label +" "+ g.getCheckInDateForApi() + "م" ;
        check_in.setText(s3);
        String s4 = "SAR " +day_price ;
        unitPrice.setText(s4);
        String s5 = "SAR " +couponDiscount ;
        discount.setText(s5);
        String s6 = "SAR " + total ;
        totalAmount.setText(s6);
        String s7 ="التأمين "+ insurance + " يدفع عند تسجيل الدخول ويسترجع عند المغادرة";
        note.setText(s7);
        String s = "حجزك اللآن مبدئي" + "\n" + "سيتواصل معك مندوبنا لتحصيل العربون " +"SAR"+ downPayment+ "\n" + "سيكون هناك رسوم إضافية وهي SAR 50 للتحصيل" + "\n" + "سيتم تأكيد حجزك فور إستلام المبلغ" ;
        SpannableString ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(1.2f), 0 ,16 ,0 );
        ss1.setSpan(new ForegroundColorSpan(Color.parseColor("#34A938")), 0,16 ,0);
        headText.setText(ss1);
    }
    private void getJson (){
        try {
            JSONObject ob = new JSONObject(mainObject);
            JSONObject data = ob.optJSONObject("data");
            JSONObject reservation = data.optJSONObject("reservation");
            JSONObject options = ob.optJSONObject("options");
            timeToPay = options.optString("line3");//"لتأكيد حجزك قبل الساعه 12:25"
            reservation_id = reservation.optString("id");
            chaletName = reservation.optString("chaletName");
            unitName = reservation.optString("unitName");
            check_in_label = reservation.optString("check_in_label");
            day_price = reservation.optInt("day_price");
            couponDiscount = reservation.optString("couponDiscount");
            total = reservation.optInt("total");
            insurance = reservation.optString("insurance");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
