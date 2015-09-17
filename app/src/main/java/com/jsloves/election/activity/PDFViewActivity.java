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
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.joanzapata.pdfview.PDFView;


public class PDFViewActivity extends Activity implements View.OnClickListener {
    private PDFView pdfView;

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

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        pdfView = (PDFView) findViewById(R.id.pdfview);
        pdfView.fromAsset("150_L.pdf")

                .pages(0,2, 1, 3, 3,3)
                .defaultPage(1)
                .showMinimap(false)
                .enableSwipe(true)
                        //.onDraw(onDrawListener)
                        //.onLoad(onLoadCompleteListener)
                        //.onPageChange(onPageChangeListener)
                .load();
    }

    @Override
    public void onClick(View v) {
        try {

            Log.d("PDFViewActivity", "getCurrentPage : " + pdfView.getCurrentPage());
            Log.d("PDFViewActivity", "getPageCount : " + pdfView.getPageCount());

            switch (v.getId()) {

                case R.id.left:
                    if (pdfView.getCurrentPage() != 0) {
                        Log.d("PDFViewActivity","left!!!");
                        pdfView.jumpTo(pdfView.getCurrentPage());
                    }
                    break;
                case R.id.right:
                    if (pdfView.getCurrentPage() != pdfView.getPageCount()-1) {
                        Log.d("PDFViewActivity", "right!!!");
                        pdfView.jumpTo(pdfView.getCurrentPage() + 2);
                    }
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