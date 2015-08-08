package com.jsloves.election.layout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jsloves.election.common.CommonValuesManager;
import com.jsloves.election.fragment.SampleFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter implements CommonValuesManager {


    private String titles[] ;

    public ViewPagerAdapter(FragmentManager fm, String[] titles2) {
        super(fm);
        titles=titles2;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            // 상단 메뉴
            // Open FragmentTab1.java
            case 0:
                return SampleFragment.newInstance(position);
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

    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return CommonValuesManager.PAGE_COUNT;
    }

}