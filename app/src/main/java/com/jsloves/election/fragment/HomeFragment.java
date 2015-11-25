package com.jsloves.election.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jsloves.election.activity.R;

import java.util.ArrayList;

/**
 * Created by wrjeong on 2015. 8. 23..
 */
public class HomeFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final String ARG_POSITION = "position";
    private static ViewPager mPager;
    private ArrayList<ImageView> mArrImageView;
    private HomeMenuOnCLickListner mHomelistner;
    private int mPosition;

    public static HomeFragment newInstance(int position, ViewPager pager) {
        // [수정] 번들에 ViewPager를 담으려 했는데....방법없나?? static 맵버 사용하기 싫은데...
        mPager = pager;
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateview mPosition : " + mPosition);

        mPosition=getArguments().getInt(ARG_POSITION);
        mArrImageView = new ArrayList<>();
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mArrImageView.add((ImageView) rootView.findViewById(R.id.guyeok));
        mArrImageView.add((ImageView) rootView.findViewById(R.id.juyo));
        mArrImageView.add((ImageView) rootView.findViewById(R.id.board));
        mArrImageView.add( (ImageView)rootView.findViewById(R.id.gighan));

        for(ImageView i:mArrImageView) {
            i.setOnClickListener(this);
        }
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mHomelistner = (HomeMenuOnCLickListner)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mHomelistner = null;
    }

    @Override
    public void onClick(View v) {
        mHomelistner.onCLickLinstnerByHome(v.getId());

    }
}
