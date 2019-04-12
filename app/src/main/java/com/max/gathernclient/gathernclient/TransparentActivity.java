package com.max.gathernclient.gathernclient;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TransparentActivity extends AppCompatActivity {
RecyclerView recyclerView ;
TextView TitleText , positiveButton , negativeButton ;
    String ID = "";
LinearLayout linearLayout , bottomLayout , layoutBankImages;
Globals globals ;
String kindOfDialog = "";
String priceOrCity = "" , itemMask = "";
View topLine , bottomLine ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transparent);
        globals = new Globals(this);
        recyclerView = findViewById(R.id.dialogRecyclerView);
        TitleText = findViewById(R.id.titleImage);
        linearLayout = findViewById(R.id.linearLayout);
        bottomLayout = findViewById(R.id.bottomLayout);
        positiveButton = findViewById(R.id.positiveButton);
        negativeButton = findViewById(R.id.negativeButton);
        topLine = findViewById(R.id.topLine);
        bottomLine = findViewById(R.id.bottomLine);
        layoutBankImages = findViewById(R.id.layoutBankImages);
        int colorBlack = getResources().getColor(R.color.myblack);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(globals.getScreenDpi(15));
        drawable.setColor(Color.parseColor("#F7F7F7"));
        final ArrayList<DialogClass> dialogList = new ArrayList<>();
        ID =  getStrings("ID");


          switch (ID) {
              case "0":
                  recyclerView.setVisibility(View.VISIBLE);
                  dialogList.add(new DialogClass(getStrings("allCity"),"", false));
                  dialogList.add(new DialogClass(getStrings("city_2"),"1", false));
                  dialogList.add(new DialogClass(getStrings("city_3"),"5", false));
                  dialogList.add(new DialogClass(getStrings("city_4"),"6", false));
                  dialogList.add(new DialogClass(getStrings("city_5"),"7", false));
                  dialogList.add(new DialogClass(getStrings("city_6"),"8", false));
                  dialogList.add(new DialogClass(getStrings("city_7"),"9", false));
                  dialogList.add(new DialogClass(getStrings("city_8"),"10", false));
                  dialogList.add(new DialogClass(getStrings("city_9"),"11", false));
                  kindOfDialog = "city";
                  SpannableString ss1 = new SpannableString("حدد المدينة");
                  ss1.setSpan(new StyleSpan(Typeface.BOLD) ,0,ss1.length(),0);
                  TitleText.setText(ss1);
                  break;
              case "1":
                  recyclerView.setVisibility(View.VISIBLE);
                  dialogList.add(new DialogClass(getStrings("allPrice"),"", false));
                  dialogList.add(new DialogClass(getStrings("price_1"),"0-800", false));
                  dialogList.add(new DialogClass(getStrings("price_2"),"810-1200", false));
                  dialogList.add(new DialogClass(getStrings("price_3"),"1201-2000", false));
                  dialogList.add(new DialogClass(getStrings("price_4"),"2001-100000", false));
                  kindOfDialog = "price";
                  SpannableString ss2 = new SpannableString("حدد المدينة");
                  ss2.setSpan(new StyleSpan(Typeface.BOLD) ,0,ss2.length(),0);
                  TitleText.setText(ss2);
                  break;
              case "bottomDialog" :
                  linearLayout.setVisibility(View.GONE);
                  bottomLayout.setVisibility(View.VISIBLE);
                  break;
              case "mustChosePaymentMethod" :
                  TitleText.setText(getStrings("title"));
                  TitleText.setTextColor(colorBlack);
                  negativeButton.setVisibility(View.GONE);
                  positiveButton.setText("نعم");
                  positiveButton.setTextColor(colorBlack);
                  topLine.setVisibility(View.GONE);
                  break;
              case "madaCard" :
                  TitleText.setText(getStrings("title"));
                  TitleText.setTextColor(colorBlack);
                  positiveButton.setText("نعم");
                  positiveButton.setTextColor(colorBlack);
                  negativeButton.setText("لا");
                  negativeButton.setTextColor(colorBlack);
                  topLine.setVisibility(View.GONE);
                  kindOfDialog = "madaCard";
                  break;
              case "masterCard" :
                  TitleText.setText(getStrings("title"));
                  TitleText.setTextColor(colorBlack);
                  positiveButton.setText("نعم");
                  positiveButton.setTextColor(colorBlack);
                  negativeButton.setText("لا");
                  negativeButton.setTextColor(colorBlack);
                  topLine.setVisibility(View.GONE);
                  kindOfDialog = "masterCard";
                  break;
              case "aqsat" :
                  TitleText.setText(getStrings("title"));
                  TitleText.setTextColor(colorBlack);
                  positiveButton.setText("نعم");
                  positiveButton.setTextColor(colorBlack);
                  negativeButton.setText("لا");
                  negativeButton.setTextColor(colorBlack);
                  topLine.setVisibility(View.GONE);
                  kindOfDialog = "aqsat";
                  break;
              case "bankTransfer" :
                  TitleText.setText(getStrings("title"));
                  TitleText.setTextColor(colorBlack);
                  positiveButton.setText("نعم");
                  positiveButton.setTextColor(colorBlack);
                  negativeButton.setText("لا");
                  negativeButton.setTextColor(colorBlack);
                  topLine.setVisibility(View.GONE);
                  kindOfDialog = "bankTransfer";
                  break;
              case "cashPayment" :
                  TitleText.setText(getStrings("title"));
                  TitleText.setTextColor(colorBlack);
                  positiveButton.setText("نعم");
                  positiveButton.setTextColor(colorBlack);
                  negativeButton.setText("لا");
                  negativeButton.setTextColor(colorBlack);
                  topLine.setVisibility(View.GONE);
                  kindOfDialog = "cashPayment";
                  break;
              case "showAllBank" :
                  SpannableString ss3 = new SpannableString(getStrings("title"));
                  ss3.setSpan(new StyleSpan(Typeface.BOLD) ,0,ss3.length(),0);
                  TitleText.setText(ss3);
                 linearLayout.setBackground(drawable);
                  negativeButton.setText("إغلاق");
                  positiveButton.setVisibility(View.GONE);
                  topLine.setVisibility(View.GONE);
                  layoutBankImages.setVisibility(View.VISIBLE);
                  break;

          }


          final LinearLayoutManager linearLayoutManager =
                  new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
          final DialogAdapter adapter = new DialogAdapter(dialogList , this);
          recyclerView.setLayoutManager(linearLayoutManager);
          recyclerView.setAdapter(adapter);

          adapter.setmOnEntryClickListener((view, position) -> {
              DialogClass Dialog = dialogList.get(position);
              Dialog.setImageChecked(true);
              dialogList.set(position, Dialog);
              priceOrCity = Dialog.getItemName();
              itemMask = Dialog.getItemMask();
              for (int i = 0; i < dialogList.size(); i++) {
                  if (i != position) {
                      Dialog = dialogList.get(i);
                      Dialog.setImageChecked(false);
                      dialogList.set(i, Dialog);
                  }
              }
              adapter.notifyDataSetChanged();
              // Toast.makeText(getBaseContext(),""+position,Toast.LENGTH_SHORT).show();

          });




    }


public String getStrings ( String key){
    String var ="";
    if (getIntent().getExtras() !=null) {
        try {
            var = getIntent().getExtras().get(key).toString();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }
        return var ;
}


    public void onClick(View view) {
        switch (view.getId()){
            case R.id.negativeButton :
                finish();
                break;
            case R.id.positiveButton :
//                if (kindOfDialog.equals("city")) {
//                    globals.setChosenPricAndCity("city", priceOrCity);
//                    globals.setChosenPricAndCity("cityMask", itemMask);
//
//                }else if (kindOfDialog.equals("price")){
//                    globals.setChosenPricAndCity("price", priceOrCity);
//                    globals.setChosenPricAndCity("priceMask", itemMask);
//
//                }else

                    if (kindOfDialog.equals("madaCard")){
                    Intent open = new Intent(getBaseContext() , PayFortSdkSample.class);
                    open.putExtra("ID","madaCard");
                    startActivity(open);
                }else if (kindOfDialog.equals("masterCard")){
                    Intent open = new Intent(getBaseContext() , PayFortSdkSample.class);
                    open.putExtra("ID","masterCard");
                    startActivity(open);
                }else if (kindOfDialog.equals("aqsat")){
                    Toast.makeText(getBaseContext() , "aqsat",Toast.LENGTH_SHORT ).show();
                }else if (kindOfDialog.equals("cashPayment")){
                    Intent open = new Intent(getBaseContext() , InfoBookingCOD.class);
                    startActivity(open);
                }else if (kindOfDialog.equals("bankTransfer")){
                    Intent open = new Intent(getBaseContext() , InfoBookingBankTransefere.class);
                    startActivity(open);
                }
                finish();
                break;
            case R.id.finish :
                finish();
                break;
            case R.id.startCall :
                Intent call = new Intent(Intent.ACTION_DIAL);
                call.setData(Uri.parse("tel:" + "920007858"));
                startActivity(call);
                finish();
                break;
            case R.id.whatsAppChat :
                openWhatsApp();
                finish();
                break;

        }
    }
    private void openWhatsApp() {
        String url = "https://api.whatsapp.com/send?phone=" + "+966112042335";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

}
