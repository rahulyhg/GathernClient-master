package com.max.gathernclient.gathernclient;
// require to open
//        orderId = Objects.requireNonNull(getIntent().getExtras()).getInt("orderId");
//        price = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("price")).toString();
//        user_email = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("email")).toString();


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.payfort.fort.android.sdk.base.FortSdk;
import com.payfort.fort.android.sdk.base.callbacks.FortCallBackManager;
import com.payfort.fort.android.sdk.base.callbacks.FortCallback;
import com.payfort.sdk.android.dependancies.base.FortInterfaces;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.payfort.sdk.android.dependancies.models.FortRequest ;
import com.shamanland.fonticon.FontIconTypefaceHolder;
import com.shamanland.fonticon.FontIconView;

import org.json.JSONException;
import org.json.JSONObject;

public class PayFortSdkSample extends AppCompatActivity {
    private FortCallBackManager fortCallback = null;
    Globals g ;
    String deviceId = "", sdkToken = "";
    FortRequest fortrequest = new FortRequest();
    String SHA_REQUEST_PHRASE = "TESTSHAIN";
   // public final static String SHA_RESPONSE_PHRASE = "TESTSHAOUT";
     String SHA_TYPE = "SHA-256";
    String MERCHANT_IDENTIFIRE ="oGoVJQXK";
     String ACCESS_CODE ="26qD1Kjvo0vdxVRLFmCE";
     String CURRENCY ="SAR";
    String LANGUAGE ="en";
     String api = "https://sbpaymentservices.payfort.com/FortAPI/paymentApi" ;
    String price = "" ,user_email ="" , orderId ="", mResponseMap ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FontIconTypefaceHolder.init(getAssets() , "icons.ttf");
        setContentView(R.layout.prepaid_layout);
        g = new Globals(this);
        getParams();
        deviceId = g.getDeviceId();
        // create Fort callback instance
        fortCallback = FortCallback.Factory.create();
        // Generating deviceId
        deviceId = FortSdk.getDeviceId(PayFortSdkSample.this);
        if(orderId.length() > 0 && price.length() > 0 &&user_email.length() > 0)
        getToken();
        else {
            Toast.makeText(getBaseContext() , "حدث خطأ في الحجز .. يرجي إعادة المحاولة"  ,Toast.LENGTH_LONG).show();
            startActivity( new Intent(getBaseContext() , SubmitBooking.class));
        }
    }
private void getKeys(){
    String allData = g.getAppData("allData");
    try {
        JSONObject ob = new JSONObject(allData);
        JSONObject keys = ob.optJSONObject("keys");
        JSONObject payfort = keys.optJSONObject("keys");
        SHA_REQUEST_PHRASE =payfort.getString("request_phrase");
        SHA_TYPE = payfort.getString("sha_type");
        MERCHANT_IDENTIFIRE =payfort.getString("merchant_identifier");
        ACCESS_CODE = payfort.getString("access_code");
        CURRENCY = payfort.getString("currency");
        LANGUAGE =  payfort.getString("language");
        api =payfort.getString("host");
    } catch (JSONException e) {
        e.printStackTrace();
    }
}
    private Map<String, Object> collectRequestMap(String sdkToken) {
        Map<String, Object> requestMap = new HashMap<>();
        //Mandatory
        requestMap.put("command", "PURCHASE");
        requestMap.put("merchant_reference",String.valueOf(orderId));
        requestMap.put("amount", price + "00");//price
        requestMap.put("currency", CURRENCY);
        requestMap.put("language", "en");
        requestMap.put("customer_email",user_email );//user_email
        requestMap.put("sdk_token", sdkToken);
        //optional
//        requestMap.put("customer_name", "SHERIF");
//        requestMap.put("customer_ip", g.getNormalIpAddress());
//       // requestMap.put("payment_option", "VISA");
//        requestMap.put("eci", "ECOMMERCE");
//        requestMap.put("order_description", "DESCRIPTION");
        return requestMap;
    }

    private void callSdk(FortRequest fortrequest) {
        try {
            FortSdk.getInstance().registerCallback(PayFortSdkSample.this, fortrequest,
                    FortSdk.ENVIRONMENT.TEST, 5, fortCallback, new FortInterfaces.OnTnxProcessed() {
                        @Override
                        public void onCancel(Map<String, Object> requestParamsMap, Map<String,
                                Object> responseMap) {
                            //TODO: handle me
                            deleteReservation(String.valueOf(orderId));
                            finish();
                             }

                        @Override
                        public void onSuccess(Map<String, Object> requestParamsMap, Map<String, Object> fortResponseMap) {
                         //  Toast.makeText(getBaseContext() ,"onSuccess = " + fortResponseMap ,Toast.LENGTH_LONG).show();
                            mResponseMap = fortResponseMap.toString();
                            confirmReservation(mResponseMap);

                        }


                        @Override
                        public void onFailure(Map<String, Object> requestParamsMap, Map<String, Object> fortResponseMap) {
                            mResponseMap = fortResponseMap.toString();
                            deleteReservation(String.valueOf(orderId));
                            finish();
                             //  Toast.makeText(getBaseContext() , "onFailure = " + fortResponseMap ,Toast.LENGTH_LONG).show();
                        }
                    });
        } catch (Exception e) {
            Log.e("execute Payment", "call FortSdk", e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fortCallback.onActivityResult(requestCode, resultCode, data);
     //   Toast.makeText(getBaseContext(), "resultCode =" + resultCode, Toast.LENGTH_LONG).show();

    }

    public void onBackPressed(View view) {
    }

//    public void onPayPressed(View view) {
//       getToken();
//    }
private void getToken (){
    String signature = getSignatureSHA256(initSignatureString ());

    JSONObject tokenParams = new JSONObject();
    try {
        tokenParams.put("service_command","SDK_TOKEN");
        tokenParams.put("access_code",ACCESS_CODE);
        tokenParams.put("merchant_identifier",MERCHANT_IDENTIFIRE);
        tokenParams.put("language",LANGUAGE);
        tokenParams.put("device_id",deviceId);
        tokenParams.put("signature",signature);
    } catch (JSONException e) {
        e.printStackTrace();
    }
    Runnable runnable = () -> {
        String response =   g.connectPayFortApiToken(tokenParams ,api);

        runOnUiThread(() -> {
            try {
                JSONObject res_object = new JSONObject(response);
                sdkToken = res_object.optString("sdk_token");
                //Toast.makeText(getBaseContext(), "sdkToken =" + sdkToken, Toast.LENGTH_LONG).show();
                if(sdkToken.length() > 0) {
                    fortrequest.setRequestMap(collectRequestMap(sdkToken));
                    fortrequest.setShowResponsePage(true); // to [display/use] the SDK response page
                    callSdk(fortrequest);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

    });
};
    Thread thread = new Thread(runnable);
        thread.start();
}
    private String getSignatureSHA256(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance(SHA_TYPE);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            return String.format("%0" + (messageDigest.length * 2) + 'x', new BigInteger(1, messageDigest));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    private String initSignatureString (){
        return SHA_REQUEST_PHRASE +
                "access_code" + "=" + ACCESS_CODE +
                "device_id" + "=" + deviceId +
                "language" + "=" + "en" +
                "merchant_identifier" + "=" + MERCHANT_IDENTIFIRE +
                "service_command" + "=" + "SDK_TOKEN" +
                SHA_REQUEST_PHRASE;
    }

   private void getParams(){//orderId
       orderId = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("orderId")).toString();
    price = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("price")).toString();
    user_email = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).get("email")).toString();
}
private void deleteReservation(String id){
    Runnable runnable = () -> {
        String api = "api/vb/client/reservation/delete";
        JSONObject params = new JSONObject();
        try {
            params.put("id",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String response = g.connectToApi(api, params, "POST");
        runOnUiThread(() -> {
            Log.i("response = ",response);
            try {
                JSONObject res = new JSONObject(response);
                String msg = res.optString("msg");
                Toast.makeText(getBaseContext(),  msg, Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Toast.makeText(getBaseContext(), "response = " + response, Toast.LENGTH_LONG).show();
        });
    };
    Thread thread = new Thread(runnable);
    thread.start();
}
    private void confirmReservation (String param){
        // param is response map from success to string
        Runnable runnable = () -> {
            String api = "api/vb/client/reservation/confirm-credit";
            JSONObject params = null;
            try {
                params = new JSONObject(param);
                String reservationId = params.optString("merchant_reference");
                params.put("reservation_id" , reservationId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String response = g.connectToApi(api, params, "POST");
            runOnUiThread(() -> {
                try {
                    JSONObject res = new JSONObject(response);
                    boolean success = res.optBoolean("success");
                    String msg = res.optString("msg");
                    if(success){
                        Intent congrat =  new Intent(getBaseContext() , Congratulations.class);
                        congrat.putExtra("reservation_id", orderId);
                        startActivity(congrat);
                        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
                        finish();
                    }else {
                        Toast.makeText(getBaseContext(), response, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            });
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}

