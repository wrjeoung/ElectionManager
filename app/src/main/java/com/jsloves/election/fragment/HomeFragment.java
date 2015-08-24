package com.jsloves.election.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jsloves.election.activity.R;

/**
 * Created by wrjeong on 2015. 8. 23..
 */
public class HomeFragment extends Fragment {

    private static final String ARG_POSITION = "position";


    public static HomeFragment newInstance(int position) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        return rootView;
    }
}
