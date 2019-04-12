 package com.max.gathernclient.gathernclient;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import java.util.ArrayList;

 public class MyPagerAdapter extends FragmentPagerAdapter{

     public ArrayList<TabsFragment> fragmentsA;

     public MyPagerAdapter(android.support.v4.app.FragmentManager fm , ArrayList<TabsFragment> fragments ) {
         super(fm);
         this.fragmentsA = fragments ;
     }

     @Override
    public Fragment getItem(int position) {

         return fragmentsA.get(position);

    }

    @Override
    public int getCount() {
        return fragmentsA.size() ;
    }

}




