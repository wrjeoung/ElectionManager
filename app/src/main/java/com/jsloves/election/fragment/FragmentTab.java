package com.jsloves.election.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.jsloves.election.activity.R;

public class FragmentTab extends Fragment {
    private FragmentTabHost mTabHost;


    // TODO: Rename and change types and number of parameters
    public static FragmentTab newInstance(String param1, String param2) {
        System.out.println("param1:"+param1);
        System.out.println("param2:"+param2);
        FragmentTab fragment = new FragmentTab();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        mTabHost = new FragmentTabHost(getActivity());
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.content);

        Bundle b1 = new Bundle();
        b1.putString("b1", "b1");

        Bundle b2 = new Bundle();
        b2.putString("b2", "b2");

        //TabHost.TabSpec tabSpec1 = mTabHost.newTabSpec("tab1").setIndicator("s");
        //TabHost.TabSpec tabSpec2 = mTabHost.newTabSpec("tab2").setIndicator("s");
        //mTabHost.addTab(tabSpec1, OrganIntroFragment.class, b1);
        //mTabHost.addTab(tabSpec2, OrganIntroFragment2.class, b2);


        mTabHost.addTab(mTabHost.newTabSpec("simple").setIndicator(getString(R.string.str_org1), null),
                OrganIntroFragment.class, b1);

        mTabHost.addTab(mTabHost.newTabSpec("contacts").setIndicator(getString(R.string.str_org2), null),
                OrganIntroFragment2.class, b2);

        int currentPos = mTabHost.getCurrentTab();
        final TabWidget widget = mTabHost.getTabWidget();
        final int childCount = widget.getChildCount();

        for(int i = 0; i < childCount; i++) {
            View tabView = widget.getChildTabViewAt(i);
            TextView tv = (TextView)tabView.findViewById(android.R.id.title);
            if (i == currentPos) {
                tabView.setBackgroundColor(Color.parseColor("#AAAAAA"));
                tv.setTextColor(Color.parseColor("#FFFFFF"));
            } else {
                tabView.setBackgroundColor(Color.parseColor("#BDBDBD"));
                tv.setTextColor(Color.parseColor("#000000"));
            }
        }

        mTabHost.getTabWidget().getChildTabViewAt(0).setBackgroundColor(Color.parseColor("#AAAAAA"));
        mTabHost.getTabWidget().getChildTabViewAt(1).setBackgroundColor(Color.parseColor("#BDBDBD"));

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                int currentPos = mTabHost.getCurrentTab();

                for (int i = 0; i < childCount; i++) {
                    View tabView = widget.getChildTabViewAt(i);
                    TextView tv = (TextView)tabView.findViewById(android.R.id.title);
                    if (i == currentPos) {
                        tabView.setBackgroundColor(Color.parseColor("#AAAAAA"));
                        tv.setTextColor(Color.parseColor("#FFFFFF"));
                    } else {
                        tabView.setBackgroundColor(Color.parseColor("#BDBDBD"));
                        tv.setTextColor(Color.parseColor("#000000"));
                    }

                }
            }
        });

        return mTabHost;
    }

        @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTabHost = null;
    }
}