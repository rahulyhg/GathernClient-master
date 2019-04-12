package com.max.gathernclient.gathernclient;
// require to open
/*
            expire_at = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("expire_at")).toString();
            reservation_id = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("reservation_id")).toString();
            chaletName = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("chaletName")).toString();
            unitName = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("unitName")).toString();
            check_in_label = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("check_in_label")).toString();
            day_price = Objects.requireNonNull(getIntent().getExtras()).getInt("day_price");
            couponDiscount = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("couponDiscount")).toString();
            total = Objects.requireNonNull(getIntent().getExtras()).getInt("total");
            insurance = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("insurance")).toString();
 */
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.Objects;

public class InfoBookingBankTransefere extends AppCompatActivity {
LinearLayout elBankElAhly , elBankElRaghy , bankAccountsList;
Globals g ;
ImageView imageInvoice , cancelImage;
TextView textOnImage  ,timeToPayTextView , reserve_id , reserve_number , chalet,unit , check_in
        , unitPrice ,discount , totalAmount , note ,whichBank ,confirmBooking;
EditText    howMuchPrice , accountHolderName , lastFourDigits ;
FrameLayout frameImageTransfer ;
boolean imageHasPhoto = false ;
String expire_at = "" , reservation_id , chaletName , unitName ,check_in_label , couponDiscount , insurance;
int day_price , total;
    String  st_img ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_booking_bank_transefere);
        g = new Globals(this);
        getId();
        initReferanceAndShape();
            //getJson ();
            setText ();
        addBankList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 && resultCode == RESULT_OK){

            Uri uri = data.getData();
            if (uri != null){
                try {
                    InputStream inputStream = getContentResolver().openInputStream(uri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    imageInvoice.setImageBitmap(bitmap);
                    textOnImage.setVisibility(View.GONE);
                    imageHasPhoto = true ;
                    cancelImage.setVisibility(View.VISIBLE);
                    // convert image to string
                    //   Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap,bitmap.getWidth(),bitmap.getHeight(),true);
                       ByteArrayOutputStream bos = new ByteArrayOutputStream();
                       bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                       byte[] img = bos.toByteArray();
                         st_img= Base64.encodeToString(img,Base64.DEFAULT);
                    //   Toast.makeText(getBaseContext(), "st_img =" + st_img, Toast.LENGTH_LONG).show();


//    bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, bos);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }


        }
    }
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageBack :
                finish();
                break;
            case R.id.frameImageTransfer :
                if(!imageHasPhoto) {
                    Intent photo = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                    startActivityForResult(photo, 1);
                }else{
                    imageInvoice.setImageBitmap(null);
                    imageHasPhoto = false ;
                    cancelImage.setVisibility(View.GONE);
                    textOnImage.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.confirmBooking :
                confirmReserve();
                break;
            case R.id.whichBank :
                CustomDialog popUp = new CustomDialog(this ,0);
                popUp.show();
                break;

        }
    }
    private void confirmReserve() {
        //  whichBank , howMuchPrice , accountHolderName , lastFourDigits ;
        String whBank = whichBank.getText().toString();
        String howPrice = howMuchPrice.getText().toString();
        String accountHolder = accountHolderName.getText().toString();
        String lastFourDigit = lastFourDigits.getText().toString();
        if (whBank.length() == 0 || howPrice.length() == 0 || accountHolder.length() == 0 || lastFourDigit.length() == 0){
            Toast.makeText(getBaseContext(), " يجب ملئ كافة الحقول" , Toast.LENGTH_LONG).show();
        }else {
            sendDataToServer(whBank , howPrice , accountHolder , lastFourDigit);
        }
    }
    private void sendDataToServer(String whBank, String howPrice, String accountHolder, String lastFourDigit) {
        showLoadingImage();
        disableSubmitButton();
            Runnable runnable = () -> {
                String api = "api/vb/client/reservation/confirm-wire";
                JSONObject Pparams = new JSONObject();
                try {
                    Pparams.put("method", "wire-transfer");
                    // Source , price ,lat ,lng , sleepover ,coupon_code
                    Pparams.put("amount", howPrice);//  ???
                    Pparams.put("reservation_id", reservation_id);
                    Pparams.put("to_account", whBank);
                    Pparams.put("from_name", accountHolder);
                    Pparams.put("from_account", lastFourDigit);
                    Pparams.put("image","image/png;base64,"+ st_img);

                } catch (JSONException e) {
                    e.printStackTrace();
                //    Toast.makeText(getBaseContext(), "JSONException = " + e, Toast.LENGTH_LONG).show();

                }
                String response = g.connectToApi(api, Pparams, "POST");
                runOnUiThread(() -> {
                    hideLoadingImage();
                    enableSubmitButton();
                    try {
                        JSONObject res = new JSONObject(response);
                        String msg = res.optString("msg");
                        boolean success = res.optBoolean("success");
                        if(success) {
                            Toast.makeText(getBaseContext(),msg, Toast.LENGTH_LONG).show();
                            Intent congrat =  new Intent(getBaseContext() , Congratulations.class);
                            congrat.putExtra("reservation_id", reservation_id);
                            startActivity(congrat);
                            finish();
                        }else{
                            JSONObject errors = res.optJSONObject("errors");
                            JSONArray amount = errors.optJSONArray("amount");
                            StringBuilder error =new StringBuilder();
                            for (int i = 0 ; i < amount.length(); i++){
                                error.append(amount.optString(i));
                            }
                            Toast.makeText(getBaseContext(),error, Toast.LENGTH_LONG).show();

                        }

                    } catch (JSONException | NullPointerException e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(),""+e, Toast.LENGTH_LONG).show();

                    }

                    //Toast.makeText(getBaseContext(), "response = " + response, Toast.LENGTH_LONG).show();
                });
            };
            Thread thread = new Thread(runnable);
            thread.start();

    }

    private void enableSubmitButton() {
        confirmBooking.setEnabled(true);
        confirmBooking.setBackground(g.shape(R.color.colorPrimary ,g.getScreenDpi(5),0,0));
        confirmBooking.setText("أكد الحجز");

    }

    private void disableSubmitButton() {
        confirmBooking.setEnabled(false);
        confirmBooking.setBackground(g.shape(R.color.transprimary ,g.getScreenDpi(5),0,0));
        confirmBooking.setText("");
    }

    private void getId() {
        try {
            expire_at = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("expire_at")).toString();
            reservation_id = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("reservation_id")).toString();
            chaletName = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("chaletName")).toString();
            unitName = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("unitName")).toString();
            check_in_label = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("check_in_label")).toString();
            day_price = Objects.requireNonNull(getIntent().getExtras()).getInt("day_price");
            couponDiscount = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("couponDiscount")).toString();
            total = Objects.requireNonNull(getIntent().getExtras()).getInt("total");
            insurance = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("insurance")).toString();
// mainObject to forward cong page
            //mainObject = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("mainObject")).toString();

        } catch (NullPointerException e) {
        //    Toast.makeText(getBaseContext(), "NullPointerException =" + e, Toast.LENGTH_LONG).show();
        }
//        if (mainObject.isEmpty())
//            Toast.makeText(getBaseContext(), "error get mainObject", Toast.LENGTH_LONG).show();
//        else Toast.makeText(getBaseContext(), "mainObject = " + mainObject, Toast.LENGTH_LONG).show();
    }
    private void initReferanceAndShape (){
        bankAccountsList = findViewById(R.id.bankAccountsList);
        confirmBooking = findViewById(R.id.confirmBooking);
        note = findViewById(R.id.note);
        totalAmount = findViewById(R.id.total);
        discount = findViewById(R.id.discount);
        unitPrice = findViewById(R.id.unitPrice);
        check_in = findViewById(R.id.check_in);
        unit = findViewById(R.id.unit);
        chalet = findViewById(R.id.chalet);
        reserve_number = findViewById(R.id.reserve_number);
        reserve_id = findViewById(R.id.reserve_id);
        timeToPayTextView = findViewById(R.id.timeToPayTextView);
        elBankElAhly = findViewById(R.id.elBankElAhly);
        elBankElRaghy = findViewById(R.id.elBankElRaghy);
        imageInvoice = findViewById(R.id.imageInvoice);
        textOnImage = findViewById(R.id.textOnImage);
        cancelImage = findViewById(R.id.cancelImage);
        whichBank = findViewById(R.id.whichBank);
        howMuchPrice = findViewById(R.id.howMuchPrice);
        accountHolderName = findViewById(R.id.accountHolderName);
        lastFourDigits = findViewById(R.id.lastFourDigits);
        frameImageTransfer = findViewById(R.id.frameImageTransfer);

        elBankElAhly.setBackground(g.shape(0,0,1,R.color.mydarkgray));
        elBankElRaghy.setBackground(g.shape(0,0,1,R.color.mydarkgray));
        whichBank.setBackground(g.shape(R.color.mywhite , 5 ,1 ,R.color.mydarkgray));
        howMuchPrice.setBackground(g.shape(R.color.mywhite , 5 ,1 ,R.color.mydarkgray));
        accountHolderName.setBackground(g.shape(R.color.mywhite , 5 ,1 ,R.color.mydarkgray));
        lastFourDigits.setBackground(g.shape(R.color.mywhite , 5 ,1 ,R.color.mydarkgray));
        frameImageTransfer.setBackground(g.shape(R.color.mywhite , 5 ,1 ,R.color.mydarkgray));
    }
    private void setText (){
        String hourToPay = expire_at.substring(11,16);
        String s1 ="وعندك مهلة لتأكيد حجزك قبل الساعة "+hourToPay ;
        timeToPayTextView.setText(s1);
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
        confirmBooking.setBackground(g.shape(R.color.colorPrimary ,g.getScreenDpi(5),0,0));
    }

//    private void getJson (){
//        try {
//            JSONObject ob = new JSONObject(mainObject);
//            JSONObject data = ob.optJSONObject("data");
//            JSONObject reservation = data.optJSONObject("reservation");
//            expire_at = reservation.optString("expire_at");//"لتأكيد حجزك قبل الساعه 12:25"
//            reservation_id = reservation.optString("id");
//            chaletName = reservation.optString("chaletName");
//            unitName = reservation.optString("unitName");
//            check_in_label = reservation.optString("check_in_label");
//            day_price = reservation.optInt("day_price");
//            couponDiscount = reservation.optString("couponDiscount");
//            total = reservation.optInt("total");
//            insurance = reservation.optString("insurance");
//        } catch (JSONException |NullPointerException e) {
//            e.printStackTrace();
//        }
//    }
    public Bitmap get_bitmap(String img_source){
        byte[] s1 = Base64.decode(img_source, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(s1, 0, s1.length);
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
    private class CustomDialog extends Dialog implements View.OnClickListener {
        TextView txt1, txt2, txt3;
        int dialogType;

        public CustomDialog(Context context, int dialogType) {
            super(context);
            this.dialogType = dialogType;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.wire_transefer_dialog);
            Objects.requireNonNull(getWindow()).setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
               WindowManager.LayoutParams wlp = getWindow().getAttributes();
               wlp.gravity = Gravity.BOTTOM;
            getWindow().setAttributes(wlp);

            txt1 = findViewById(R.id.txt1);
            txt2 = findViewById(R.id.txt2);
            txt3 = findViewById(R.id.txt3);

            txt1.setOnClickListener(this);
            txt2.setOnClickListener(this);
            txt3.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.txt1:
                    whichBank.setText("الأهلي");
                    cancel();
                    break;
                case R.id.txt2:
                    whichBank.setText("الراجحي");
                    cancel();
                    break;
                case R.id.txt3:
                    cancel();
                    break;
            }
        }
    }
    private void addBankList(){
        String allData = g.getAppData("allData");
        JSONArray banks = new JSONArray();
        try {
            JSONObject ob = new JSONObject(allData);
             banks = ob.optJSONArray("banks");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        createBankLayout(banks);

    }
private void createBankLayout(JSONArray banks){

    String id = "" ,number= "" ,iban = "",owner = "",bankName= "" ,bankLogo = "";
    for (int i = 0 ; i<banks.length() ; i++) {
        number = banks.optJSONObject(i).optString("number");
        iban = banks.optJSONObject(i).optString("iban");
        owner = banks.optJSONObject(i).optString("owner");
        bankName = banks.optJSONObject(i).optString("bankName");
        bankLogo = banks.optJSONObject(i).optString("bankLogo");

        LinearLayout hor = new LinearLayout(this);
        hor.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams horParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        horParam.setMargins(0,0,0,g.getScreenDpi(8 ));
        hor.setLayoutParams(horParam);
        hor.setBackground(g.shape(0, 0, 1, R.color.mydarkgray));
        hor.setPadding(g.getScreenDpi(4), g.getScreenDpi(4), g.getScreenDpi(4), g.getScreenDpi(4));

        ImageView bankImage = new ImageView(this);
        Picasso.with(this).load(bankLogo).fit().into(bankImage);
        LinearLayout.LayoutParams imgParam = new LinearLayout.LayoutParams(g.getScreenDpi(50),
                g.getScreenDpi(50));
        imgParam.setMargins(g.getScreenDpi(4), g.getScreenDpi(4), g.getScreenDpi(4), g.getScreenDpi(4));
        bankImage.setLayoutParams(imgParam);


        LinearLayout ver = new LinearLayout(this);
        ver.setOrientation(LinearLayout.VERTICAL);

        MyTextView bankNameView = new MyTextView(this);
        String s1 = "اسم البنك : " + bankName;
        bankNameView.setText(s1);
        bankNameView.setTextColor(Color.parseColor("#1B1B1B"));

        MyTextView accountNumber = new MyTextView(this);
        String s = "رقم الحساب :" + number;
        accountNumber.setText(s);
        accountNumber.setTextColor(Color.parseColor("#1B1B1B"));

        MyTextView ibanView = new MyTextView(this);
        String s2 = "الايبان : " + iban;
        ibanView.setText(s2);
        ibanView.setTextColor(Color.parseColor("#1B1B1B"));

        MyTextView ownerName = new MyTextView(this);
        String s3 = "اسم صاحب الحساب : " + owner;
        ownerName.setText(s3);

        ownerName.setTextColor(Color.parseColor("#1B1B1B"));
        ver.addView(bankNameView);
        ver.addView(accountNumber);
        ver.addView(ibanView);
        ver.addView(ownerName);
        hor.addView(bankImage);
        hor.addView(ver);
        bankAccountsList.addView(hor);
    }
}
}
