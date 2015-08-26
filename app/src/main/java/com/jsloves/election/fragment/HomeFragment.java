package com.jsloves.election.fragment;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jsloves.election.activity.R;

/**
 * Created by wrjeong on 2015. 8. 23..
 */
public class HomeFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = HomeFragment.class.getSimpleName();
    private static final String ARG_POSITION = "position";
    private static ViewPager mPager;
    private LinearLayout mGuyok;
    private LinearLayout mJungchi;
    private LinearLayout mSocioal;
    private LinearLayout mJuyo;
    private LinearLayout mBoard;
    private LinearLayout mGighan;

    private ImageView mImGuyok;
    private ImageView mImJungchi;
    private ImageView mImSocioal;
    private ImageView mImJuyo;
    private ImageView mImBoard;
    private ImageView mImGighan;

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
        Log.d(TAG,"onCreateview mPosition : "+mPosition);
        mPosition=getArguments().getInt(ARG_POSITION);
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mGuyok = (LinearLayout)rootView.findViewById(R.id.guyeok);
        mJungchi = (LinearLayout)rootView.findViewById(R.id.jungchi);
        mSocioal = (LinearLayout)rootView.findViewById(R.id.social);
        mJuyo = (LinearLayout)rootView.findViewById(R.id.juyo);
        mBoard = (LinearLayout)rootView.findViewById(R.id.board);
        mGighan = (LinearLayout)rootView.findViewById(R.id.gighan);
        mImGuyok = (ImageView)rootView.findViewById(R.id.img1);

        mGuyok.setOnClickListener(this);
        mJungchi.setOnClickListener(this);
        mSocioal.setOnClickListener(this);
        mJuyo.setOnClickListener(this);
        mBoard.setOnClickListener(this);
        mGighan.setOnClickListener(this);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.guyeok);

        mImGuyok.setImageBitmap(bitmap);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.guyeok:
                mPager.setCurrentItem(1);
                break;
            case R.id.jungchi:
                mPager.setCurrentItem(2);
                break;
            case R.id.social:
                mPager.setCurrentItem(3);
                break;
            case R.id.juyo:
                mPager.setCurrentItem(4);
                break;
            case R.id.board:
                mPager.setCurrentItem(5);
                break;
            case R.id.gighan:
                mPager.setCurrentItem(6);
                break;
        }
    }
}
