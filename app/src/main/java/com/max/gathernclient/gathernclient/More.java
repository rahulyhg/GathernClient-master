package com.max.gathernclient.gathernclient;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.net.URI;

public class More extends AppCompatActivity {
LinearLayout MyAccountData , SignOutLayout , MyAccount , myFavorite , lineLayout;
Globals g ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more);
      //  overridePendingTransition(R.anim.zooming , R.anim.zooming);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        g = new Globals(this);
        initRefrance();
        setFragment();

        if (g.getSingedState() == 1){
            MyAccountData.setVisibility(View.VISIBLE);
            myFavorite.setVisibility(View.VISIBLE);
            lineLayout.setVisibility(View.VISIBLE);
            MyAccount.setVisibility(View.GONE);
            SignOutLayout.setVisibility(View.VISIBLE);
        }else {
            MyAccountData.setVisibility(View.GONE);
            lineLayout.setVisibility(View.GONE);
            MyAccount.setVisibility(View.VISIBLE);
            myFavorite.setVisibility(View.GONE);
            SignOutLayout.setVisibility(View.GONE);

        }


           // ImageViewCompat.setImageTintList(imageView , ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));

    }
    private void initRefrance (){
        lineLayout = findViewById(R.id.lineLayout);
        myFavorite = findViewById(R.id.myFavorite);
        MyAccountData = findViewById(R.id.myAccountData);
        SignOutLayout = findViewById(R.id.signOutLayout);
        MyAccount = findViewById(R.id.account);

    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.houses:
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                break;
            case R.id.parties:
                startActivity(new Intent(getBaseContext(), Parties.class));
                break;
            case R.id.offers:
                startActivity(new Intent(getBaseContext(), Offers.class));
                break;
            case R.id.booking:
                startActivity(new Intent(getBaseContext(), MyBooking.class));
                break;
            case R.id.account:
                Intent open = new Intent(getBaseContext() ,MyAccount.class);
                open.putExtra("ID","");
                startActivity(open);
                break;
            case R.id.signOutLayout :
                g.setSingedState(0);
                g.setUserData("firstName","");
                g.setUserData("lastName","");
                g.setUserData("email","");
                g.setUserData("yearOfBirth","");
                g.setUserData("access_token","");
                g.setUserData("mobile", "");
                startActivity(new Intent(getBaseContext(), More.class));
                break;
            case R.id.myAccountData :
                startActivity(new Intent(getBaseContext(), EditMyData.class));
                break;
            case R.id.myFavorite :
                startActivity(new Intent(getBaseContext(), Favorite.class));

                break;
            case R.id.faqLayout :
                startActivity(new Intent(getBaseContext(), Faq.class));
//                Intent open = new Intent(getBaseContext(), TextInfo.class);
//                open.putExtra("titleText","الأسئلة المتكررة");
//                startActivity(open);
                break;
            case R.id.contactUsLayout :
                Intent open2 = new Intent(getBaseContext(), ContactUs.class);
                open2.putExtra("titleText","تواصل معنا");
                startActivity(open2);
                break;
            case R.id.inviteFriendsLayout :
                String app_url="https://play.google.com/store/apps/details?";
                Intent share_app=new Intent(Intent.ACTION_SEND);
                share_app.setType("text/plain");
                share_app.putExtra(Intent.EXTRA_TEXT,app_url);
                startActivity(Intent.createChooser(share_app,"مشاركة علي "));
                break;
            case R.id.termsOfUsesLayout :
                startActivity(new Intent(getBaseContext(), TermsOfUses.class));

//                Intent open3 = new Intent(getBaseContext(), TextInfo.class);
//                open3.putExtra("titleText","شروط الإستخدام");
//                startActivity(open3);
                break;
            case R.id.privacyLayout :
                startActivity(new Intent(getBaseContext(), PrivacyPolicy.class));

//                Intent open4 = new Intent(getBaseContext(), TextInfo.class);
//                open4.putExtra("titleText","سياسة الخصوصية");
//                startActivity(open4);
                break;
            case R.id.owners :
                Intent openBrowser = new Intent(Intent.ACTION_VIEW , Uri.parse("https://gathern.co/business/auth"));
               startActivity(openBrowser);
                break;

        }
    }
    protected void setFragment() {
        Bundle bundle = new Bundle();
        bundle.putString("Activity","More");
        BottomBarFragment fragment = new BottomBarFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentBottomBar, fragment);
        fragmentTransaction.commit();
    }
    @Override
    public void onBackPressed() {
    }
}
