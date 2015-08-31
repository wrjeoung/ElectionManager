package com.jsloves.election.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by wrjeong on 2015. 8. 31..
 */
public class ElectionHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ElectionHomeActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);

        ArrayList<LinearLayout> listLiLayout;
        listLiLayout = new ArrayList<>();

        listLiLayout.add((LinearLayout)findViewById(R.id.guyeok));
        listLiLayout.add((LinearLayout)findViewById(R.id.jungchi));
        listLiLayout.add((LinearLayout)findViewById(R.id.social));
        listLiLayout.add((LinearLayout)findViewById(R.id.juyo));
        listLiLayout.add((LinearLayout)findViewById(R.id.board));
        listLiLayout.add( (LinearLayout)findViewById(R.id.gighan));

        for(LinearLayout i:listLiLayout) {
            i.setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(ElectionHomeActivity.this, ElectionMainActivity.class);

        switch(v.getId()) {
            case R.id.guyeok:
                intent.putExtra("startPosition", 0);
                startActivity(intent);
                break;
            case R.id.jungchi:
                intent.putExtra("startPosition", 1);
                startActivity(intent);
                break;
            case R.id.social:
                intent.putExtra("startPosition", 2);
                startActivity(intent);
                break;
            case R.id.gighan:
                intent.putExtra("startPosition", 3);
                startActivity(intent);
                break;
            case R.id.juyo:
                intent.putExtra("startPosition", 4);
                startActivity(intent);
                break;
            case R.id.board:
                intent.putExtra("startPosition", 5);
                startActivity(intent);
                break;
            default:
                Log.e(TAG, "no selected item");
                break;
        }
    }
}
