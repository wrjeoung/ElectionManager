package com.jsloves.election.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ArrayList<LinearLayout> listLiLayout;
        listLiLayout = new ArrayList<>();

        listLiLayout.add((LinearLayout) findViewById(R.id.guyeok));
        listLiLayout.add((LinearLayout) findViewById(R.id.jungchi));
        listLiLayout.add((LinearLayout) findViewById(R.id.social));
        listLiLayout.add((LinearLayout) findViewById(R.id.juyo));
        listLiLayout.add((LinearLayout) findViewById(R.id.board));
        listLiLayout.add((LinearLayout) findViewById(R.id.gighan));
        findViewById(R.id.modify_myinfo).setOnClickListener(this);

        for (LinearLayout i : listLiLayout) {
            i.setOnClickListener(this);
        }
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed");
        //  super.onBackPressed();
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(this);
        builder.setTitle("종료확인")
                .setMessage("종료하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        //dialog.dismiss();
                        finish();
                    }
                })
                .setNegativeButton("아니요", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        dialog.cancel();
                    }
                })
                .show();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(ElectionHomeActivity.this, ElectionMainActivity.class);
        Log.d(TAG,"onClick() v.getId() : "+v.getId());
        switch (v.getId()) {
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
            case R.id.modify_myinfo:
                intent = new Intent(ElectionHomeActivity.this,JoinActivity.class);
                intent.putExtra("whereFrom","modify_myinfo");
                startActivity(intent);
            default:
                Log.e(TAG, "no selected item");
                break;
        }
    }
}
