package com.max.gathernclient.gathernclient;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.renderscript.Sampler;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class SubmitBooking extends AppCompatActivity {//reserveConditions
    LinearLayout coponLayout ;
    TextView cancelPolicyView,notes, cashPayment , sleepOverPrice , sleepOverHours
            ,addSleepOver ,total , insurance , arbonTextView;
    TextView chaletPlsUnit, checkInDate, priceOneNight, remainToConfirm, note ,timeToCash;
    ImageView Done_1, Done_2, Done_3, Done_4, Done_5 , sleepOverCheck;
    EditText frstName, familyName, email , nationalId;
    int colorPrimary;
    int paymentMethod = 0;
    Drawable imageChecked, imageNotChecked;
    Globals g;
    String id = "";
    // String what comes from response
    String title, chalet_title, price, downPayment, sleepover_hour, sleepover_price, cancelPolicy, reserveConditions, unit_id = "", chalet_id = "", payment_method = "";
    boolean hasOffer, wireTransfer, creditCard, cashpay, installments, creditMada;
    int sleepover = 0 , couponType = 0 ;
    float couponAmount = 0f ;
    CustomDialog customDialog;
    String splitingPrice;
    LinearLayout sleepOverLayout , LinearLayoutBooking;
    boolean sleepCheck  = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.zooming, R.anim.zooming);
        setContentView(R.layout.submit_booking);
        g = new Globals(this);
        initRefrance();
        getId();
        getDataFromServer();
    }
    private void initRefrance (){
        LinearLayoutBooking = findViewById(R.id.LinearLayoutBooking);
        arbonTextView = findViewById(R.id.arbonTextView);
        insurance = findViewById(R.id.insurance);
        coponLayout = findViewById(R.id.coponLayout);
        sleepOverLayout = findViewById(R.id.sleepOverLayout);
        sleepOverCheck = findViewById(R.id.sleepOverCheck);
        sleepOverPrice = findViewById(R.id.sleepOverPrice);
        sleepOverHours = findViewById(R.id.sleepOverHours);
        timeToCash = findViewById(R.id.timeToCash);
        cancelPolicyView = findViewById(R.id.cancelPolicy);
        cashPayment = findViewById(R.id.cashPayment);
        Done_1 = findViewById(R.id.done_1);
        Done_2 = findViewById(R.id.done_2);
        Done_3 = findViewById(R.id.done_3);
        Done_4 = findViewById(R.id.done_4);
        Done_5 = findViewById(R.id.done_5);
        notes = findViewById(R.id.notes);
        nationalId = findViewById(R.id.nationalId);
        frstName = findViewById(R.id.firstName);
        familyName = findViewById(R.id.familyName);
        email = findViewById(R.id.email);
        note = findViewById(R.id.note);
        total = findViewById(R.id.total);
        addSleepOver= findViewById(R.id.addSleepOver);
        chaletPlsUnit = findViewById(R.id.chaletPlsUnit);
        checkInDate = findViewById(R.id.checkInDate);
        priceOneNight = findViewById(R.id.priceOneNight);
        remainToConfirm = findViewById(R.id.remainToConfirm);
        note = findViewById(R.id.note);

    }
    private void initPrimaryData(){



        if(sleepover == 0)
            sleepOverLayout.setVisibility(View.GONE);
        else {
            sleepOverLayout.setVisibility(View.GONE);
            String s = "إضافة مبيت إلي الساعة "+ sleepover_hour ;
            sleepOverHours.setText(s);
            String s2 = ""+g.getNumberFromString(sleepover_price) ;
            String s3 = "( "+s2+"ريال ) إضافية" ;
            sleepOverPrice.setText(s3);
        }


        colorPrimary = getResources().getColor(R.color.colorPrimary);
        imageChecked = getResources().getDrawable(R.drawable.shape_color_primary_radius60);
        imageNotChecked = getResources().getDrawable(R.drawable.shape_color_white_radius60_strok1_gray);


        SpannableString s4 = new SpannableString("كاش من خلال المندوب (50 ريال إضافية)");
        s4.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 19, 0);
        s4.setSpan(new RelativeSizeSpan(1.2f), 0, 19, 0);
        cashPayment.setText(s4);
    }
    private void initTerms(){
    notes.setText(cancelPolicy);

}
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageBack:
                finish();
                break;
//            case R.id.cancelPolicy:
//                cancelPolicyView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//                cancelPolicyView.setTextColor(getResources().getColor(R.color.mywhite));
//                notes.setText(cancelPolicy);//reserveConditions
//                break;

            case R.id.madaLayout:
                Done_1.setBackground(imageChecked);
                Done_2.setBackground(imageNotChecked);
                Done_3.setBackground(imageNotChecked);
                Done_4.setBackground(imageNotChecked);
                Done_5.setBackground(imageNotChecked);
                paymentMethod = 1;
                payment_method = "credit-mada";
                break;
            case R.id.visaLayout:
                Done_2.setBackground(imageChecked);
                Done_1.setBackground(imageNotChecked);
                Done_3.setBackground(imageNotChecked);
                Done_4.setBackground(imageNotChecked);
                Done_5.setBackground(imageNotChecked);
                paymentMethod = 2;
                payment_method = "credit-card";
                break;
            case R.id.aqsatLayout:
                Done_3.setBackground(imageChecked);
                Done_2.setBackground(imageNotChecked);
                Done_1.setBackground(imageNotChecked);
                Done_4.setBackground(imageNotChecked);
                Done_5.setBackground(imageNotChecked);
                paymentMethod = 3;
                payment_method = "installments";
                break;
            case R.id.wireTransfereLayout:
                Done_4.setBackground(imageChecked);
                Done_2.setBackground(imageNotChecked);
                Done_3.setBackground(imageNotChecked);
                Done_1.setBackground(imageNotChecked);
                Done_5.setBackground(imageNotChecked);
                paymentMethod = 4;
                payment_method = "wire-transfer";
                break;
            case R.id.cashLayout:
                Done_4.setBackground(imageNotChecked);
                Done_2.setBackground(imageNotChecked);
                Done_3.setBackground(imageNotChecked);
                Done_1.setBackground(imageNotChecked);
                Done_5.setBackground(imageChecked);
                paymentMethod = 5;
                payment_method = "cash-payment";
                break;
            case R.id.sleepOverLayout :
                if(sleepCheck) {
                    sleepOverCheck.setBackground(imageNotChecked);
                    sleepCheck = false ;
                    removeSleep();
                }
                else {
                    sleepOverCheck.setBackground(imageChecked);
                    sleepCheck = true ;
                    addSleep();
                }
                break;
            case R.id.submitBooking:
                customDialog = new CustomDialog(this, paymentMethod);
                customDialog.show();
                break;
            case R.id.showAllBank:
                Intent openDialog_6 = new Intent(getBaseContext(), TransparentActivity.class);
                openDialog_6.putExtra("ID", "showAllBank");
                String s6 = "البنوك المشاركة في خدمة الأقساط";
                openDialog_6.putExtra("title", s6);
                startActivity(openDialog_6);
                break;
            case R.id.applyDiscount:
                TextView discountCode = findViewById(R.id.discountCode);
                checkCopon(discountCode.getText().toString());
                //   int i =    getNumberFromString(discountCode.getText().toString());
                //  Toast.makeText(getBaseContext(), "number =" + i, Toast.LENGTH_LONG).show();

                break;
        }
    }

    private void checkCopon(String copon) {
        showLoadingCopon();
        disableCoponButton();
        Runnable runnable = () -> {
            String api = "api/va/client/coupon/check2";
            JSONObject params = new JSONObject();
            try {
                params.put("coupon_code",copon);
                params.put("chalet_id",chalet_id);
                params.put("unit_id",unit_id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String response = g.connectToApi(api, params, "POST");
            runOnUiThread(() -> {
                hideLoadingCopon();
                enableCoponButton();
                try {
                    JSONObject ob = new JSONObject(response);
                    boolean success = ob.optBoolean("success");

                    if (success && ob.has("coupon"))
                    {
                      JSONObject coupon = ob.optJSONObject("coupon");
                     couponType = coupon.optInt("type"); // 1= percent & 2 = fixed amount
                       couponAmount = coupon.optInt("amount");
                      initInvoice();
                    }
                    String message = ob.optString("message");
                    Toast.makeText(getBaseContext(),message, Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void getUserData() {

        String firstName, lastName, emailText;

        firstName = g.getUserData("firstName");
        lastName = g.getUserData("lastName");
        emailText = g.getUserData("email");

        frstName.setText(firstName);
        familyName.setText(lastName);
        email.setText(emailText);


    }

    private void initInvoice() {
// set calendar views
        LinearLayoutBooking
                .setBackground(g.shapeStringColor("#D6D5D5",g.getScreenDpi(5),1,"#b9b9b9"));
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
        // end part
        //اسم الشالية واسم الوحدة
        String x = chalet_title + "-" + title;
        chaletPlsUnit.setText(x);
        //التاريخ
        String date = g.getCheckInDateForApi();
        checkInDate.setText(date);

        // سعر الليلة الواحدة
        String p = " ريال " ;
        SpannableString s1 = new SpannableString( price + p);
        s1.setSpan(new RelativeSizeSpan(1.5f), 0,  price.length() ,0);
        s1.setSpan(new StyleSpan(Typeface.BOLD),  0,  price.length(), 0);
        s1.setSpan(new ForegroundColorSpan(Color.BLACK), 0,  price.length(), 0);
        priceOneNight.setText(s1);

        // المستحق دفعة لتأكيد الحجز (العربون)
        String z = "العربون (المستحق دفعه لتأكيد الحجز)";
        SpannableString z1 = new SpannableString(z);
        z1.setSpan(new ForegroundColorSpan(Color.BLACK),  0,  7, 0);
        arbonTextView.setText(z1);

        if (downPayment.contains(".")) {
            splitingPrice = downPayment.substring(0, downPayment.indexOf("."));
        } else {
            splitingPrice = downPayment;
        }
        SpannableString s2 = new SpannableString(  splitingPrice + p);
        s2.setSpan(new StyleSpan(Typeface.BOLD),  0,   splitingPrice.length() , 0);
        s2.setSpan(new ForegroundColorSpan(Color.BLACK),  0,  splitingPrice.length(), 0);
        s2.setSpan(new RelativeSizeSpan(1.5f),  0,  splitingPrice.length(), 0);
        remainToConfirm.setText(s2);


        int price_Int = g.getNumberFromString(price);

// الاجمالي
        int totaal = price_Int +g.getNumberFromString(sleepover_price);
        int finalTotal = totaal ;
        // check if coupon
      switch (couponType){
          case 1 :
              finalTotal = (int) (totaal - (couponAmount *totaal/100));
              break;
          case 2 :
              finalTotal = (int) (totaal - couponAmount);
              break;
      }
        String total_st = String.valueOf(finalTotal);
        String s ="SAR " + total_st;
        SpannableString s5 = new SpannableString(s);
        s5.setSpan(new StyleSpan(Typeface.BOLD), 4, 4 + total_st.length(), 0);
        s5.setSpan(new ForegroundColorSpan(Color.BLACK), 4, 4 + total_st.length(), 0);
        s5.setSpan(new RelativeSizeSpan(1.2f), 4, 4 + total_st.length(), 0);
        total.setText(s5);
        // باقي المبلغ
        int downpa = g.getNumberFromString(downPayment);
        int remainAmoun = finalTotal - downpa +g.getNumberFromString(sleepover_price);
        String remainAmount = String.valueOf(remainAmoun);
        String stamp = "SAR";
        SpannableString s4 = new SpannableString("باقي المبلغ " + remainAmount + " يدفع عند تسجيل الدخول للشالية");
        s4.setSpan(new StyleSpan(Typeface.BOLD), 12, 12 + remainAmount.length(), 0);
        s4.setSpan(new ForegroundColorSpan(Color.BLACK), 12, 12 + remainAmount.length(), 0);
        s4.setSpan(new RelativeSizeSpan(1.2f), 12, 12 + remainAmount.length(), 0);
        note.setText(s4);
        insurance.setText(reserveConditions);
    }

    private void getId() {
        try {
            id = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("ID")).toString();
        } catch (NullPointerException e) {
            Toast.makeText(getBaseContext(), "NullPointerException =" + e, Toast.LENGTH_LONG).show();
        }
//        if (response.isEmpty())
//            Toast.makeText(getBaseContext(), "error get response", Toast.LENGTH_LONG).show();
//        else Toast.makeText(getBaseContext(), "response = " + response, Toast.LENGTH_LONG).show();
    }

    private void getJson(String result) {// result from chalet/main/unit?id=
        try {
            JSONObject mainObject = new JSONObject(result);

            title = mainObject.optString("title");// used in submit booking
            chalet_title = mainObject.optString("chalet_title");// used in submit booking
            price = mainObject.optString("price");// used in submit booking
            downPayment = mainObject.optString("downPayment");
            hasOffer = mainObject.optBoolean("hasOffer");
            if (hasOffer)
                coponLayout.setVisibility(View.GONE);
            else
                coponLayout.setVisibility(View.GONE);
            //
            sleepover = mainObject.optInt("sleepover");// t/f 0/1 value
            sleepover_hour = mainObject.optString("sleepover_hour");
            sleepover_price = mainObject.optString("sleepover_price");
            //
            JSONObject chalet = mainObject.optJSONObject("chalet");
            cancelPolicy = chalet.optString("cancelPolicy");
            reserveConditions = chalet.optJSONArray("reserveConditions").optString(0);
            JSONArray paymentsMethods = chalet.optJSONArray("paymentsMethods");
            //  wireTransfer , creditCard ,cashpay , installments , creditMada;
            wireTransfer = paymentsMethods.optString(0).matches("wire-transfer");
            creditCard = paymentsMethods.optString(1).matches("credit-card");
            cashpay = paymentsMethods.optString(2).matches("cash-payment");
            installments = paymentsMethods.optString(3).matches("installments");
            creditMada = paymentsMethods.optString(4).matches("credit-mada");
            chalet_id = mainObject.optString("chalet_id");
            unit_id = mainObject.optString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void addRemovePaymentMethod() {
        LinearLayout madaLayout, visaLayout, aqsatLayout, wireTransfereLayout, cashLayout;
        madaLayout = findViewById(R.id.madaLayout);
        visaLayout = findViewById(R.id.visaLayout);
        aqsatLayout = findViewById(R.id.aqsatLayout);
        wireTransfereLayout = findViewById(R.id.wireTransfereLayout);
        cashLayout = findViewById(R.id.cashLayout);

        if (wireTransfer)
            wireTransfereLayout.setVisibility(View.VISIBLE);
        if (creditCard)
            visaLayout.setVisibility(View.VISIBLE);
        if (cashpay)
            cashLayout.setVisibility(View.VISIBLE);
//        if (installments)
//            aqsatLayout.setVisibility(View.VISIBLE);
        if (creditMada)
            madaLayout.setVisibility(View.VISIBLE);
    }

    private void tempReserve(int paymentMethod) {
         // nationalId
        showLoadingImage();
        disableSubmitButton();
        Runnable runnable = () -> {
            String check_in = g.getCheckInDateForApi();
            String api = "api/va/client/reservation/reserve";
            JSONObject Pparams = new JSONObject();
            try {
                Pparams.put("check_in", check_in);
                Pparams.put("user_name", frstName.getText().toString() + " " +familyName.getText().toString());
                Pparams.put("user_mobile", g.getUserData("mobile"));
                Pparams.put("user_email", email.getText().toString());
                Pparams.put("nationalId", nationalId.getText().toString());
                Pparams.put("unit_id", unit_id);
                Pparams.put("chalet_id", chalet_id);
                Pparams.put("payment_method", payment_method);
                // Source , price ,lat ,lng , sleepover ,coupon_code
                Pparams.put("Source", "Android Native");
                Pparams.put("price", price);//  ???
                Pparams.put("lat", g.getLoc("lat"));
                Pparams.put("lng", g.getLoc("lon"));
               if(sleepCheck)
               Pparams.put("sleepover", sleepover_price);//only if exist

//                Pparams.put("coupon_code", "");//only if exist

            } catch (JSONException e) {
                e.printStackTrace();
            }
            String response = g.connectToApi(api, Pparams, "POST");
            runOnUiThread(() -> {
                hideLoadingImage();
                enableSubmitButton();
                try {
                    JSONObject mainObject = new JSONObject(response); // response from reservation/reserve
                    boolean success = mainObject.optBoolean("success");
                    if (success) {
                        switch (paymentMethod){
                            case 1 ://mada
                                creditIntent(mainObject.toString());
                                break;
                            case 2 :// credit
                                creditIntent(mainObject.toString());
                                break;
                            case 4:// wire
                                wireTransferIntent(mainObject.toString());
                                break;
                            case 5 :// cash
                                cashIntent(mainObject.toString());
                                break;
                        }
                    } else
                        Toast.makeText(getBaseContext(), "لايمكن الحجز في هذا التاريخ جرب تاريخ اخر", Toast.LENGTH_LONG).show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            });
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }//
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
    private void showLoadingCopon() {
        ImageView loadingImage = findViewById(R.id.loadingCopon);
        loadingImage.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotation);
        animation.setInterpolator(new LinearInterpolator());
        loadingImage.setAnimation(animation);
    }
    private void disableCoponButton() {
        TextView coponBtn = findViewById(R.id.applyDiscount);
        coponBtn.setEnabled(false);
        coponBtn.setBackground(g.shape(R.color.transprimary ,g.getScreenDpi(5),0,0));
        coponBtn.setText("");
    }

    private void enableCoponButton() {
        TextView coponBtn = findViewById(R.id.applyDiscount);
        coponBtn.setEnabled(true);
        coponBtn.setBackground(g.shape(R.color.colorPrimary ,g.getScreenDpi(5),0,0));

        coponBtn.setText("تطبيق الكوبون");
    }
    private void hideLoadingCopon() {
        ImageView loadingImage = findViewById(R.id.loadingCopon);
        loadingImage.setAnimation(null);
        loadingImage.setVisibility(View.GONE);
    }
    private void showTopLoadingImage() {
        ImageView loadingImage = findViewById(R.id.topLoadingImage);
        loadingImage.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotation);
        animation.setInterpolator(new LinearInterpolator());
        loadingImage.setAnimation(animation);
        ScrollView mainScrollView = findViewById(R.id.mainScrollView);
        mainScrollView.setVisibility(View.GONE);
    }

    private void hideTopLoadingImage() {
        ImageView loadingImage = findViewById(R.id.topLoadingImage);
        loadingImage.setAnimation(null);
        loadingImage.setVisibility(View.GONE);
        ScrollView mainScrollView = findViewById(R.id.mainScrollView);
        mainScrollView.setVisibility(View.VISIBLE);
    }

    private void disableSubmitButton() {
        TextView submitBooking = findViewById(R.id.submitBooking);
        submitBooking.setEnabled(false);
        submitBooking.setText("");
    }

    private void enableSubmitButton() {
        TextView submitBooking = findViewById(R.id.submitBooking);
        submitBooking.setEnabled(true);
        submitBooking.setText("أكمل الحجز");
    }

    private class CustomDialog extends Dialog implements View.OnClickListener {
        TextView positiveButton, negativeButton, title;
        int dialogType;

        public CustomDialog(Context context, int dialogType) {
            super(context);
            this.dialogType = dialogType;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.reserve_dialog);
            Objects.requireNonNull(getWindow()).setLayout(g.getScreenDpi(240), LinearLayout.LayoutParams.WRAP_CONTENT);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            positiveButton = findViewById(R.id.positiveButton);
            negativeButton = findViewById(R.id.negativeButton);
            title = findViewById(R.id.title);
            setTitle();
            negativeButton.setOnClickListener(this);
            positiveButton.setOnClickListener(this);
        }

        private void setTitle() {

            switch (paymentMethod) {
                case 0:
                    String s0 = "يجب إختيار طريقة الدفع";
                    title.setText(s0);
                    negativeButton.setVisibility(View.GONE);
                    break;
                case 1:// mada
                    String s1 = "هل انت متأكد من تاريخ الحجز" + "\n" + g.getCheckInDateForApi() + "\n" + "يوم " +g.getBookingDate("dayText")+ "\n" + "سوف يتم تحويل لنموذج الدفع لإدخال بيانات البطاقة والدفع وسيتم تأكيد حجزك بشكل مباشر";
                    title.setText(s1);
                    negativeButton.setVisibility(View.VISIBLE);

                    break;
                case 2:// credit card
                    String s2 = "هل انت متأكد من تاريخ الحجز" + "\n" + g.getCheckInDateForApi() + "\n" + "يوم " +g.getBookingDate("dayText")+ "\n" + "سوف يتم تحويل لنموذج الدفع لإدخال بيانات البطاقة والدفع وسيتم تأكيد حجزك بشكل مباشر";
                    title.setText(s2);
                    negativeButton.setVisibility(View.VISIBLE);

                    break;
                case 3:// aqsat
                    String s3 = "هل انت متأكد من تاريخ الحجز" + "\n" + g.getCheckInDateForApi() + "\n" + "يوم "+g.getBookingDate("dayText") + "\n" + "سوف يتم تحويلك لنموذج الدفع لإدخال بيانات البطاقة والدفع وسيتم تأكيد حجزك بشكل مباشر";
                    title.setText(s3);
                    negativeButton.setVisibility(View.VISIBLE);

                    break;
                case 4:// bankTransfer
                    String s4 = "هل انت متأكد من تاريخ الحجز" + "\n" + g.getCheckInDateForApi() + "\n" + "يوم " +g.getBookingDate("dayText")+ "\n" + "مهله السداد 60 دقيقه" + "\n" + "ارجو تحويل المبلغ وارفاق الايصال وبعدها سيتم تأكيد حجزك تلقائي" + "\n" + "وعند انتهاء المهله اعد الحجز مره اخرى";
                    title.setText(s4);
                    negativeButton.setVisibility(View.VISIBLE);

                    break;
                case 5:// cashPayment
                    String s5 = "هل انت متأكد من تاريخ الحجز" + "\n" + g.getCheckInDateForApi() + "\n" + "يوم "+g.getBookingDate("dayText") + "\n" + "مهله السداد 60 دقيقه" + "\n" + "وسيتواصل معك مندوبنا لأخذ عنوانكم" + "\n" + "وعند انتهاء المهله اعد الحجز مره اخرى";
                    title.setText(s5);
                    negativeButton.setVisibility(View.VISIBLE);

                    break;

            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.positiveButton:
                    switch (dialogType) {
                        case 0:// has no selection
                            cancel();
                            break;
                        case 1:// mada
                            tempReserve(paymentMethod);
                            cancel();
                            break;
                        case 2:// credit card
                            tempReserve(paymentMethod);
                            cancel();
                            break;
                        case 3://aqsat
                            Toast.makeText(getBaseContext(), "aqsat", Toast.LENGTH_SHORT).show();
                            cancel();
                            break;
                        case 4: //BankTransfere
                            tempReserve(paymentMethod);
                            cancel();
                            break;
                        case 5://COD
                            tempReserve(paymentMethod);
                            cancel();
                            break;
                    }

                    break;
                case R.id.negativeButton:
                    cancel();
                    break;
            }
        }
    }
    public void getDataFromServer() {
        showTopLoadingImage();
        Runnable runnable = () -> {
            String check_in = g.getCheckInDateForApi();
            String expand = "gallery,panorama,options,futureBusyDays,amenities,chalet,reviews,reviewsOverview";//
            String api = "api/va/chalet/main/unit?id=" + id + "&check_in=" + check_in + "&expand=" + expand;
            String result = g.connectToApi(api, null, "GET");
            runOnUiThread(() -> {
                hideTopLoadingImage();
                if(result.length() >0) {
                    getJson(result);
                    addRemovePaymentMethod();
                    getUserData();
                    initInvoice();
                    initPrimaryData();
                    initTerms();
                    removeSleep();// to start page with sleep = 0 and bold
                    cashTime();
                }else
                     Toast.makeText(getBaseContext(), "no response" , Toast.LENGTH_LONG).show();

            });
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void creditIntent(String mainObject){
           int orderId = 0 ;
            try {
            JSONObject ob = new JSONObject(mainObject);
            JSONObject data = ob.optJSONObject("data");
            JSONObject reservation = data.optJSONObject("reservation");
            orderId =reservation.optInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent pay = new Intent(getBaseContext(), PayFortSdkSample.class);
        pay.putExtra("price", splitingPrice);
        pay.putExtra("orderId", orderId);
        pay.putExtra("email", email.getText().toString());

        startActivity(pay);
    }

    private void wireTransferIntent(String mainObject){
        String expire_at ="", reservation_id="", chaletName ="", unitName ="", check_in_label ="", couponDiscount=""
                , insurance ="";
        int day_price =0 , total = 0 ;
        try {
            JSONObject ob = new JSONObject(mainObject);
            JSONObject data = ob.optJSONObject("data");
            JSONObject reservation = data.optJSONObject("reservation");
            JSONObject options = ob.optJSONObject("options");
            expire_at = reservation.optString("expire_at");//"لتأكيد حجزك قبل الساعه 12:25"
              reservation_id = reservation.optString("id");
              chaletName = reservation.optString("chaletName");
              unitName = reservation.optString("unitName");
              check_in_label = reservation.optString("check_in_label");
                 day_price = reservation.optInt("day_price");
              couponDiscount = reservation.optString("couponDiscount");
                 total = reservation.optInt("total");
              insurance = reservation.optString("insurance");
        } catch (JSONException |NullPointerException e) {
            e.printStackTrace();
        }
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
        pay.putExtra("mainObject", mainObject);

        startActivity(pay);
    }
    private void cashIntent(String mainObject){
        Intent pay = new Intent(getBaseContext(), InfoBookingCOD.class);
        pay.putExtra("mainObject", mainObject);
        pay.putExtra("price", splitingPrice);

        startActivity(pay);
    }
    private void addSleep(){
        String x1 = String.valueOf(g.getNumberFromString(sleepover_price));
        if(x1.length() > 0 ) {
            SpannableString s3 = new SpannableString("SAR " + x1);
            s3.setSpan(new RelativeSizeSpan(1.2f), 4,4+x1.length() ,0);
            s3.setSpan(new StyleSpan(Typeface.BOLD), 4, 4+x1.length(), 0);
            s3.setSpan(new ForegroundColorSpan(Color.BLACK), 4, 4+x1.length(), 0);
            addSleepOver.setText(s3);

            // الاجمالي
            int price_Int = g.getNumberFromString(price);
            int totaal = price_Int +g.getNumberFromString(sleepover_price);
            String total_st = String.valueOf(totaal);
            String s ="SAR " + total_st;
            SpannableString s5 = new SpannableString(s);
            s5.setSpan(new StyleSpan(Typeface.BOLD), 4, 4 + total_st.length(), 0);
            s5.setSpan(new ForegroundColorSpan(Color.BLACK), 4, 4 + total_st.length(), 0);
            s5.setSpan(new RelativeSizeSpan(1.2f), 4, 4 + total_st.length(), 0);
            total.setText(s5);

            // باقي المبلغ
            int downpa = g.getNumberFromString(downPayment);
            int remainAmoun = price_Int - downpa +g.getNumberFromString(sleepover_price);
            String remainAmount = String.valueOf(remainAmoun);
            String stamp = "SAR";
            SpannableString s4 = new SpannableString("باقي المبلغ " + remainAmount + " يدفع عند تسجيل الدخول للشالية");
            s4.setSpan(new StyleSpan(Typeface.BOLD), 12, 12 + remainAmount.length(), 0);
            s4.setSpan(new ForegroundColorSpan(Color.BLACK), 12, 12 + remainAmount.length(), 0);
            s4.setSpan(new RelativeSizeSpan(1.2f), 12, 12 + remainAmount.length(), 0);
            note.setText(s4);
        }
    }
    private void removeSleep (){
        String x1 = String.valueOf(0);
            SpannableString s3 = new SpannableString("SAR " + x1);
            s3.setSpan(new RelativeSizeSpan(1.2f), 4, 4 + x1.length(), 0);
            s3.setSpan(new StyleSpan(Typeface.BOLD), 4, 4 + x1.length(), 0);
            s3.setSpan(new ForegroundColorSpan(Color.BLACK), 4, 4 + x1.length(), 0);
            addSleepOver.setText(s3);

        // الاجمالي
        int price_Int = g.getNumberFromString(price);
        String total_st = String.valueOf(price_Int);
        String s ="SAR " + total_st;
        SpannableString s5 = new SpannableString(s);
        s5.setSpan(new StyleSpan(Typeface.BOLD), 4, 4 + total_st.length(), 0);
        s5.setSpan(new ForegroundColorSpan(Color.BLACK), 4, 4 + total_st.length(), 0);
        s5.setSpan(new RelativeSizeSpan(1.2f), 4, 4 + total_st.length(), 0);
        total.setText(s5);

        // باقي المبلغ
        int downpa = g.getNumberFromString(downPayment);
        int remainAmoun = price_Int - downpa ;
        String remainAmount = String.valueOf(remainAmoun);
        String stamp = "SAR";
        SpannableString s4 = new SpannableString("باقي المبلغ " + remainAmount + " يدفع عند تسجيل الدخول للشالية");
        s4.setSpan(new StyleSpan(Typeface.BOLD), 12, 12 + remainAmount.length(), 0);
        s4.setSpan(new ForegroundColorSpan(Color.BLACK), 12, 12 + remainAmount.length(), 0);
        s4.setSpan(new RelativeSizeSpan(1.2f), 12, 12 + remainAmount.length(), 0);
        note.setText(s4);

    }
private void cashTime (){
        String alldata = g.getAppData("allData");
    String from = "";
    String to ="";

    try {
        JSONObject ob = new JSONObject(alldata);
        JSONObject cash_hours = ob.optJSONObject("cash_hours");
        JSONObject sat = cash_hours.optJSONObject("sat");
        JSONObject sun = cash_hours.optJSONObject("sun");
        JSONObject mon = cash_hours.optJSONObject("mon");
        JSONObject tue = cash_hours.optJSONObject("tue");
        JSONObject wed = cash_hours.optJSONObject("wed");
        JSONObject thu = cash_hours.optJSONObject("thu");
        JSONObject fri = cash_hours.optJSONObject("fri");
        String currentDay = g.getBookingDate("dayText");

        switch (currentDay){
            case "السبت":
                from = sat.optString("from");
                to = sat.optString("to");
                break;
            case "الأحد":
                from = sun.optString("from");
                to = sun.optString("to");
                break;
            case "الإثنين":
                from = mon.optString("from");
                to = mon.optString("to");
                break;
            case "الثلاثاء":
                from = tue.optString("from");
                to = tue.optString("to");
                break;
            case "الأربعاء":
                from = wed.optString("from");
                to = wed.optString("to");
                break;
            case "الخميس":
                from = thu.optString("from");
                to = thu.optString("to");
                break;
             case "الجمعة":
                 from = fri.optString("from");
                 to = fri.optString("to");
                break;

        }

    } catch (JSONException e) {
        e.printStackTrace();
    }


        String s =  "أوقات عمل المندوب من الساعة " + from+":00" +" الي الساعة " +to+":00";
    timeToCash.setText(s);
}
}
