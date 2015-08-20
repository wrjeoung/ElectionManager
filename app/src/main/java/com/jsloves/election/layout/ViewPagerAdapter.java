package com.jsloves.election.layout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.jsloves.election.common.CommonValuesManager;
import com.jsloves.election.fragment.SampleFragment;
import com.jsloves.election.fragment.SearchFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter implements CommonValuesManager {


    private String titles[] ;
    private static SearchFragment mSchFrt;


    public SearchFragment getmSchFrt() {
        return mSchFrt;
    }

    public ViewPagerAdapter(FragmentManager fm, String[] titles2) {
        super(fm);
        titles=titles2;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("ViewPagerAdapater", " position : " + position);
        switch (position) {
            // 상단 메뉴
            // Open FragmentTab1.java
            case 0:
                //return SampleFragment.newInstance(position);
                mSchFrt = SearchFragment.newInstance("aa","bb");
                return mSchFrt;
            case 1:
                return SampleFragment.newInstance(position);
            case 2:
                return SampleFragment.newInstance(position);
            case 3:
                return SampleFragment.newInstance(position);
            case 4:
                return SampleFragment.newInstance(position);
            case 5:
                return SampleFragment.newInstance(position);

        }
        return null;
    }

    @Override
    public int getItemPosition(Object object) {
//        SearchFragment fragment = (SearchFragment) object;
//        String title = fragment.getTitle();
//        int position = titles.indexOf(title);
//
//        if (position >= 0) {
//            return position;
//        } else {
//            return POSITION_NONE;
//        }


        return POSITION_NONE;
    }

    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return CommonValuesManager.PAGE_COUNT;
    }

}