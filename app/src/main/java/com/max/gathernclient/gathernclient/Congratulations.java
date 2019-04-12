package com.max.gathernclient.gathernclient;
// require to open // reservation_id
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Objects;

public class Congratulations extends AppCompatActivity {
LinearLayout mainLayout ;

    String reservation_id = "", chaletName = "", unitName = "", check_in_label = "", check_in = "", couponDiscount = "",
            downPayment = "", remaining = "", insurance = "", chalet_map_image = "" , lat = "" , lng = "";
    int day_price =0, total = 0 ;
    Globals g;
    LinearLayout firstLayout, seconedLayout, thirdLayout;
    TextView goChalet, callSuperVisor, shareOnWhatsApp, cancelReservation;
    // textViews for invoice
    TextView reserve_id, reserve_number, chalet, unit, check_inn, unitPrice, discount, totalAmount, note, remainingView, paid;
    ImageView openMapImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.congratulations);
        g = new Globals(this);
        getReservationId();
        // handle confirmed reservation here
        initReferance();

        setShaps();
        getReservationDetails();

    }
    private void getReservationId() {
        try {
            reservation_id = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("reservation_id")).toString();
        } catch (NullPointerException e) {
          //  Toast.makeText(getBaseContext(), "NullPointerException =" + e, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
   }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getBaseContext(), HomePage.class));
    }


    private void initReferance() {
        mainLayout = findViewById(R.id.mainLayout);
        openMapImage = findViewById(R.id.openMapImage);
        paid = findViewById(R.id.paid);
        remainingView = findViewById(R.id.remainingView);
        cancelReservation = findViewById(R.id.cancelReservation);
        shareOnWhatsApp = findViewById(R.id.shareOnWhatsApp);
        firstLayout = findViewById(R.id.firstLayout);
        seconedLayout = findViewById(R.id.seconedLayout);
        thirdLayout = findViewById(R.id.thirdLayout);
        goChalet = findViewById(R.id.goChalet);
        callSuperVisor = findViewById(R.id.callSuperVisor);
        // for invoice
        note = findViewById(R.id.note);
        totalAmount = findViewById(R.id.total);
        discount = findViewById(R.id.discount);
        unitPrice = findViewById(R.id.unitPrice);
        check_inn = findViewById(R.id.check_in);
        unit = findViewById(R.id.unit);
        chalet = findViewById(R.id.chalet);
        reserve_number = findViewById(R.id.reserve_number);
        reserve_id = findViewById(R.id.reserve_id);

    }

    private void setShaps() {
        Drawable corners = g.shapeColorString("#ffffff", g.getScreenDpi(5));
        Drawable cornersBlue = g.shapeColorString("#134AD6", g.getScreenDpi(5));
        Drawable cornersGreen = g.shapeColorString("#34A938", g.getScreenDpi(5));
        Drawable cornersRed = g.shapeColorString("#CA0B0B", g.getScreenDpi(5));

        firstLayout.setBackground(corners);
        seconedLayout.setBackground(corners);

        goChalet.setBackground(cornersBlue);
        callSuperVisor.setBackground(cornersGreen);
        shareOnWhatsApp.setBackground(cornersGreen);
        cancelReservation.setBackground(cornersRed);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageBack:
                finish();
                break;
            case R.id.openMapImage:
                openMap();
                break;
            case R.id.goChalet:
                openMap();
                break;
            case R.id.shareOnWhatsApp:
                 openWhatsApp();
                //getReservationDetails();
                break;
            case R.id.callSuperVisor:
                callNow();
                break;
            case R.id.cancelReservation:
                cancelReservation(reservation_id);
                break;
        }

    }

    private void openMap() {
//        String lat = g.getLoc("lat");
//        String lon = g.getLoc("lon");
        Uri gmmIntentUri = Uri.parse("geo:" + lat + "," + lng);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    private void openWhatsApp() {
        String url = "https://api.whatsapp.com/send?phone=" + "+966112042335";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void callNow() {
        Intent call = new Intent(Intent.ACTION_DIAL);
        call.setData(Uri.parse("tel:" + "0555555551"));
        startActivity(call);
    }

    private void getJson(String response) {
        try {
            JSONObject ob = new JSONObject(response);
            JSONObject chalet_location = ob.optJSONObject("chalet_location");

            reservation_id = ob.optString("id");
            chaletName = ob.optString("chaletName");
            unitName = ob.optString("unitName");
            check_in_label = ob.optString("check_in_label");
            check_in = ob.optString("check_in");
            downPayment = ob.optString("downPayment");
            remaining = ob.optString("remaining");
            day_price = ob.optInt("day_price");
            couponDiscount = ob.optString("couponDiscount");
            total = ob.optInt("total");
            insurance = ob.optString("insurance");
            chalet_map_image = ob.optString("chalet_map_image");

            lat = chalet_location.optString("lat");
            lng = chalet_location.optString("lng");

        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        //   Toast.makeText(getBaseContext(), ""+ e , Toast.LENGTH_LONG).show();
        }
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
        String s4 = "SAR " + day_price;
        unitPrice.setText(s4);
        String s5 = "SAR " + g.getNumberFromString(couponDiscount);
        // العربون
        int downPaymentInteger = g.getNumberFromString(downPayment);
        String s9 = "SAR " + downPaymentInteger;
        paid.setText(s9);
        // المتبقي
        int remainingInteger = total - downPaymentInteger;
        String s8 = "SAR " + remainingInteger;
        remainingView.setText(s8);
        discount.setText(s5);
        String s6 = "SAR " + total;
        totalAmount.setText(s6);
        String s7 = "التأمين " + insurance + " يدفع عند تسجيل الدخول ويسترجع عند المغادرة";
        note.setText(s7);
    }

    private void cancelReservation(String reservationId) {
        showLoadingImage();
        disableSubmitButton();
        Runnable runnable = () -> {
            String api = "api/vb/client/reservation/cancel?id=" + reservationId;
            String response = g.connectToApi(api, null, "GET");
            runOnUiThread(() -> {
                hideLoadingImage();
                enableSubmitButton();
                try {
                    JSONObject res = new JSONObject(response);
                    String message = res.optString("message");
                    boolean success = res.optBoolean("success");
                    if (success) {
                        startActivity(new Intent(this, SubmitBooking.class));
                    }
                    Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        };
        Thread thread = new Thread(runnable);
        thread.start();

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

    private void disableSubmitButton() {
        TextView cancelReservation = findViewById(R.id.cancelReservation);
        cancelReservation.setEnabled(false);
        cancelReservation.setText("");
        Drawable cornersTransRed = g.shapeColorString("#7ECA0B0B", g.getScreenDpi(5));
        cancelReservation.setBackground(cornersTransRed);
    }

    private void enableSubmitButton() {
        TextView cancelReservation = findViewById(R.id.cancelReservation);
        cancelReservation.setEnabled(true);
        cancelReservation.setText("إلغاء الحجز");
        Drawable cornersRed = g.shapeColorString("#CA0B0B", g.getScreenDpi(5));
        cancelReservation.setBackground(cornersRed);
    }
    private void getReservationDetails(){
        showTopLoadingImage();
        Runnable runnable = () -> {
            String api = "api/vb/client/reservation/view?id=" + reservation_id;
            String response = g.connectToApi(api, null, "GET");
            runOnUiThread(() -> {
                if(response.length() > 0) {

                    hideTopLoadingImage();
                    getJson(response);
                    setText();
                    setMapImage();
                }

               // Toast.makeText(getBaseContext() ,"response = " +response,Toast.LENGTH_LONG).show();
            });
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void setMapImage() {
       // String url =  "https://maps.googleapis.com/maps/api/staticmap?zoom=16&size=400x200&key=AIzaSyABLYQ9SaAM_cfNZjqso4vEFJoGuMxda88&markers=icon:http://icons.iconarchive.com/icons/paomedia/small-n-flat/1024/sign-check-icon.png%7C24.75177803229138,46.7741800390994";
      //  String url2 = "https://gathern.s3.eu-central-1.amazonaws.com/1/9rP2SeG48B3aI0gYyZdl0zaxj_nhOeOL-600x500.jpg";
        String url3 = "https://maps.googleapis.com/maps/api/staticmap?center=24.868101000853386,46.82694866894508&size=400x200&maptype=roadmap&path=fillcolor:0xE85F0E43%7Ccolor:0x91A93A00%7Cenc:gfxvCk{x|GFw@Rq@^i@h@]n@Qp@An@Lj@Z`@d@Vp@Ht@Ct@Or@]j@g@`@m@Tq@Do@Im@Wc@e@[m@Mu@&zoom=16&sensor=false&key=AIzaSyBd-AzOOwfGAMT3_8JsQt6cKAjGEi5OlFU&signature=XZE05P26_GuZPAMG9Lx0CtmicbE=" ;
        Picasso.with(this).load(url3).into(openMapImage);
    }

    private void showTopLoadingImage() {
        ImageView loadingImage = findViewById(R.id.topLoadingImage);
        loadingImage.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotation);
        animation.setInterpolator(new LinearInterpolator());
        loadingImage.setAnimation(animation);
        mainLayout.setVisibility(View.GONE);
    }

    private void hideTopLoadingImage() {
        ImageView loadingImage = findViewById(R.id.topLoadingImage);
        loadingImage.setAnimation(null);
        loadingImage.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);

    }

}
