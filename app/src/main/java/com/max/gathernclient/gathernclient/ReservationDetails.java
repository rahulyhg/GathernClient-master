package com.max.gathernclient.gathernclient;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class ReservationDetails extends AppCompatActivity {
    /**
     * require to open reservation_id
     */
    TextView reserve_id, reserve_number, chalet , unit, check_inn, unitPrice , shareOnWhatsApp,
            discount, totalAmount, note, remainingView, paid , status , payNow;
    String reservation_id="", chaletName="", unitName="", check_in_label="", check_in="", couponDiscount="",
            downPayment="", remaining="", insurance="" , statusText = "" ,paymentMethodKey ="" , expire_at = "" , user_email ="";
    int day_price , total , statusNumber;
    Globals g ;
    LinearLayout mainLayout ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        g = new Globals(this);
        getReservation_id();
        setContentView(R.layout.reservation_details);
        initRefrance();
        setShapes();
        getDataFromServer();

    }


private void showButtons (){
        if(statusNumber == 1) {
            if(paymentMethodKey.equals("wire-transfer") ||paymentMethodKey.equals("credit-card") ||paymentMethodKey.equals("credit-mada")) {
                payNow.setVisibility(View.VISIBLE);
            }
        }
        else
            payNow.setVisibility(View.GONE);
    if(statusNumber == 3) {
        shareOnWhatsApp.setVisibility(View.VISIBLE);
    }else {
        shareOnWhatsApp.setVisibility(View.GONE);

    }


}
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageBack :
                finish();
                break;
            case R.id.shareOnWhatsApp :
                openWhatsApp();
                break;
            case R.id.payNow :
                payForward();
                break;
        }
    }
    private void creditCardPayment (){
        String price = String.valueOf(g.getNumberFromString(downPayment));
        Intent pay = new Intent(getBaseContext(), PayFortSdkSample.class);
        pay.putExtra("orderId" ,reservation_id);
        pay.putExtra("price" ,price);
        pay.putExtra("email" ,user_email);
        startActivity(pay);

//         Toast.makeText(getBaseContext() ,"reservation_id = " + reservation_id
//                 +"downPayment ="+downPayment
//                 +"user_email"+user_email
//                 ,Toast.LENGTH_LONG).show();


    }
    private void wireTransferIntent(){
        Intent pay = new Intent(getBaseContext(), InfoBookingBankTransefere.class);
        pay.putExtra("expire_at", expire_at);
        pay.putExtra("reservation_id", reservation_id);
        pay.putExtra("chaletName", chaletName);
        pay.putExtra("unitName", unitName);
        pay.putExtra("check_in_label", check_in_label);
        pay.putExtra("day_price", day_price);
        pay.putExtra("couponDiscount", couponDiscount);
        pay.putExtra("total", total);
        pay.putExtra("insurance", insurance);
        pay.putExtra("mainObject", "");// should be mainObject
        startActivity(pay);

//         Toast.makeText(getBaseContext() ,"expire_at = " +expire_at
//                 +"reservation_id" +reservation_id
//                 +"chaletName"+chaletName
//                 +"unitName" +unitName
//                 +"check_in_label" +check_in_label
//                 +"day_price" +day_price
//                 +"couponDiscount"+couponDiscount
//                 +"total"+total
//                 +"insurance"+insurance
//
//                 ,Toast.LENGTH_LONG).show();

    }
    private void payForward(){
        switch (paymentMethodKey){
            case "wire-transfer" :
                wireTransferIntent();
                break;
            case "credit-card" :
                creditCardPayment ();
                break;
            case "credit-mada" :
                creditCardPayment ();
                break;
        }

    }
    private void initRefrance (){
        // for invoice
        mainLayout  = findViewById(R.id.mainLayout);
        note = findViewById(R.id.note);
        totalAmount = findViewById(R.id.total);
        discount = findViewById(R.id.discount);
        unitPrice = findViewById(R.id.unitPrice);
        check_inn = findViewById(R.id.check_in);
        unit = findViewById(R.id.unit);
        chalet = findViewById(R.id.chalet);
        reserve_number = findViewById(R.id.reserve_number);
        reserve_id = findViewById(R.id.reserve_id);
        paid = findViewById(R.id.paid);
        remainingView = findViewById(R.id.remainingView);
        status = findViewById(R.id.status);
        shareOnWhatsApp = findViewById(R.id.shareOnWhatsApp);
        payNow = findViewById(R.id.payNow);
    }
    private void setText() {
        //  Picasso.with(this).load(chalet_map_image).into(openMapImage);
        String s2 = "تفاصيل الحجز رقم " + reservation_id;
        reserve_id.setText(s2);
        reserve_number.setText(reservation_id);
        chalet.setText(chaletName);
        unit.setText(unitName);
        String s3 = check_in_label + " " + g.getCheckInDateForApi() + "م";
        check_inn.setText(s3);
        unitPrice.setText(g.setSARText(""+day_price));
        // العربون
        int downPaymentInteger = g.getNumberFromString(downPayment);
        paid.setText(g.setSARText(""+downPaymentInteger));
        // المتبقي
        int remainingInteger = total - downPaymentInteger;
        remainingView.setText(g.setSARText(""+remainingInteger));
        discount.setText(g.setSARText(""+g.getNumberFromString(couponDiscount)));
        totalAmount.setText(g.setSARText(""+total));
        String s7 = "التأمين " + g.setSARText(""+insurance) + " يدفع عند تسجيل الدخول ويسترجع عند المغادرة";
        note.setText(s7);
        status.setText(statusText);
        if(statusNumber == 3)
            status.setTextColor(Color.parseColor("#34A938"));
        else if (statusNumber == 1)
            status.setTextColor(Color.parseColor("#749CF8"));
        else
            status.setTextColor(Color.parseColor("#F3AB4E"));


    }
    public void getDataFromServer (){
        showLoadingImage();

        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                String api = "api/va/client/reservation/view?id="+reservation_id+"&expand=chalet" ;
                String result = g.connectToApi(api , null ,"GET");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideLoadingImage();
                        getJson(result);
                        setText();
                        showButtons();
                        // Toast.makeText(getBaseContext() ,"result = " +result  ,Toast.LENGTH_LONG).show();
                    }
                });
            }};
        Thread thread= new Thread(runnable);
        thread.start();
    }
    private void getJson (String result){
        try {
            JSONObject ob = new JSONObject(result);
            chaletName = ob.optString("chaletName");
            unitName = ob.optString("unitName");
            check_in = ob.optString("check_in");
            day_price = ob.optInt("day_price");
            couponDiscount = ob.optString("couponDiscount");
            downPayment = ob.optString("downPayment");
            remaining = ob.optString("remaining");
            total = ob.optInt("total");
            insurance = ob.optString("insurance");
            statusText = ob.optString("statusText");
            paymentMethodKey = ob.optString("paymentMethodKey");
            statusNumber = ob.optInt("status");
            expire_at = ob.optString("expire_at");
            user_email = ob.optString("user_email");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
private void setShapes (){
    Drawable cornersGreen = g.shapeColorString("#34A938", g.getScreenDpi(5));
    shareOnWhatsApp.setBackground(cornersGreen);
    payNow.setBackground(cornersGreen);

}
    private void openWhatsApp() {
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.whatsapp");
        if(launchIntent !=null)
        startActivity(launchIntent);
    }
    private void getReservation_id() {
        reservation_id = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("reservation_id")).toString();
         }
    private void showLoadingImage() {
        mainLayout.setVisibility(View.GONE);
        ImageView loadingImage = findViewById(R.id.loading);
        loadingImage.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotation);
        animation.setInterpolator(new LinearInterpolator());
        loadingImage.setAnimation(animation);
    }

    private void hideLoadingImage() {
        mainLayout.setVisibility(View.VISIBLE);
        ImageView loadingImage = findViewById(R.id.loading);
        loadingImage.setAnimation(null);
        loadingImage.setVisibility(View.GONE);
    }

}
