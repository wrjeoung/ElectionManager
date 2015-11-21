package com.jsloves.election.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by juhyukkim on 2015. 11. 21..
 */
public class ElectionIntroActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_intro_activity);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ElectionIntroActivity.this,ElectionManagerActivity.class);
                startActivity(intent);
                finish();
            }
        },1000); // 1초뒤 실행
    }
}
