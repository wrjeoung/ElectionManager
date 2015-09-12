/**
 * Copyright 2014 Joan Zapata
 *
 * This file is part of Android-pdfview.
 *
 * Android-pdfview is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Android-pdfview is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Android-pdfview.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jsloves.election.activity;


import android.app.Activity;
import android.os.Bundle;

import com.joanzapata.pdfview.PDFView;

public class PDFViewActivity extends Activity {
    private PDFView pdfView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.main_menu);
        pdfView = (PDFView)findViewById(R.id.pdfview);
        pdfView.fromAsset("exam.pdf")
                //.pages(79,80, 81, 82, 83)
                .defaultPage(80)
                .showMinimap(false)
                .enableSwipe(true)
                        //.onDraw(onDrawListener)
                //.onLoad(onLoadCompleteListener)
                //.onPageChange(onPageChangeListener)
                .load();
    }

}