package com.jsloves.election.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        mTabHost.addTab(mTabHost.newTabSpec("simple").setIndicator("1"),
                OrganIntroFragment.class, b1);

        mTabHost.addTab(mTabHost.newTabSpec("contacts").setIndicator("2"),
                OrganIntroFragment2.class, b2);

        return mTabHost;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTabHost = null;
    }
}