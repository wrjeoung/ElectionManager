package com.jsloves.election.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by wooram.jeong on 2015-07-07.
 */
public class ElectionMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        /*Button btn_area_search = (Button) findViewById(R.id.btn_area_search);
        // Listening to News Feed button click
        btn_area_search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Launching SearchActivity
                Intent i = new Intent(getApplicationContext(), SearchActivity.class);
                startActivity(i);
            }
        });*/
    }
}
