package com.jsloves.election.layout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.jsloves.election.common.CommonValuesManager;
import com.jsloves.election.fragment.BusinessListFragment;
import com.jsloves.election.fragment.FragmentTab;
import com.jsloves.election.fragment.SampleFragment;
import com.jsloves.election.fragment.SearchFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter implements CommonValuesManager {
    private String titles[] ;
    private SearchFragment mSearchFragment;
    private ViewPager mPager;

    public ViewPagerAdapter(FragmentManager fm, String[] titles2, ViewPager pager) {
        super(fm);
        titles=titles2;
        mPager=pager;
    }

    public SearchFragment getmSearchFragment() {
        return mSearchFragment;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("ViewPagerAdapater", " position : " + position);
        switch (position) {
            // 상단 메뉴 및 좌측메뉴.
            // Open FragmentTab1.java
            case 0:
                //return SampleFragment.newInstance(position);
                Log.d("ViewPagerAdapter","onCreateView mSearchFramgent : "+SearchFragment.newInstance("aa","bb"));
                mSearchFragment=SearchFragment.newInstance("aa", "bb");
                return mSearchFragment;
            case 1:
                // 기관 정보
                return FragmentTab.newInstance("aa","bb");
            case 2:
                return BusinessListFragment.newInstance();
            case 3:
                //return SampleFragment.newInstance(position);
                //return OrganIntroFragment.newInstance("aaa", "bbb");
                return SampleFragment.newInstance(position);
            case 4:
                return SampleFragment.newInstance(position);
            case 5:
                return SampleFragment.newInstance(position);

        }
        return null;
    }

//    @Override
//    public int getItemPosition(Object object) {
//        SearchFragment fragment = (SearchFragment) object;
//        String title = fragment.getTitle();
//        int position = titles.indexOf(title);
//
//        if (position >= 0) {
//            return position;
//        } else {
//            return POSITION_NONE;
//        }
//
//
//        return POSITION_NONE;
//    }

    @Override
    public int getItemPosition(Object item) {
        Fragment fragment = (Fragment) item;

        if (fragment instanceof SearchFragment) {
            return POSITION_UNCHANGED;
        } else {
            // after this, onCreateView() of Fragment is called.
            return POSITION_NONE;   // notifyDataSetChanged
        }
    }

    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return CommonValuesManager.PAGE_COUNT;
    }

}