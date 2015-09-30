/**
 * Copyright 2014 Joan Zapata
 * <p/>
 * This file is part of Android-pdfview.
 * <p/>
 * Android-pdfview is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * Android-pdfview is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with Android-pdfview.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jsloves.election.activity;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.joanzapata.pdfview.PDFView;

import java.io.File;
import java.io.FileNotFoundException;


public class PDFViewActivity extends Activity implements View.OnClickListener {
    private static final String TAG = PDFViewActivity.class.getSimpleName();
    private PDFView pdfView;
    private String mSavedFilePath = "/sdcard/final.pdf";
    private int mStartPageNum;
    private int mEndPageNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        Button bt_l = new Button(this);
        Button bt_r = new Button(this);
        Button bt_e = new Button(this);

        setContentView(R.layout.main_menu);
        bt_l = (Button) findViewById(R.id.left);
        bt_r = (Button) findViewById(R.id.right);
        bt_e = (Button) findViewById(R.id.exit);


        bt_l.setOnClickListener(this);
        bt_r.setOnClickListener(this);
        bt_e.setOnClickListener(this);
        mStartPageNum = getIntent().getIntExtra("pdfStartPageNum", 1);
        mEndPageNum = getIntent().getIntExtra("pdfEndPageNum", 1);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        pdfView = (PDFView) findViewById(R.id.pdfview);
        pdfView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
        Log.d(TAG, "absolutepath : " + Environment.getExternalStorageDirectory().getAbsolutePath());
        try {
            if(new File(mSavedFilePath).exists()) {
                pdfView.fromFile(new File(mSavedFilePath))
                        // pdfView.fromAsset("final.pdf")
                        //.pages(0, 2, 1, 3, 3, 3)
                        .defaultPage(mStartPageNum)
                        .showMinimap(false)
                        .enableSwipe(true)
                                //.onDraw(onDrawListener)
                                //.onLoad(onLoadCompleteListener)
                                //.onPageChange(onPageChangeListener)
                        .load();            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    public void onClick(View v) {
        try {

            Log.d("PDFViewActivity", "onclick() getCurrentPage : " + pdfView.getCurrentPage());
            Log.d("PDFViewActivity", "onclick() getPageCount : " + pdfView.getPageCount());
            Log.d("PDFViewActivity", "onclick() mStartPageNum : " + mStartPageNum);
            Log.d("PDFViewActivity", "onclick() mEndPageNum : " + mEndPageNum);

            switch (v.getId()) {

                case R.id.left:
                    if (pdfView.getCurrentPage() <= mStartPageNum - 1)
                        break;

                    Log.d("PDFViewActivity", "left!!!");
                    pdfView.jumpTo(pdfView.getCurrentPage());

                    break;
                case R.id.right:
                    if (pdfView.getCurrentPage() >= mEndPageNum - 1)
                        break;

                    Log.d("PDFViewActivity", "right!!!");
                    pdfView.jumpTo(pdfView.getCurrentPage() + 2);

                    break;
                case R.id.exit:
                    finish();
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}