package com.jsloves.election.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import memo.activity.BoardActivity;

/**
 * Created by wrjeong on 2015. 8. 31..
 * 구역정보, 기관정보, 주요사업, 게시판, 정보수정이 표시되는 홈화면.
 */
public class ElectionHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ElectionHomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // 한글은 bold 적용이 안되기 때문에 소스상에서 처리.
        TextView tv = (TextView)findViewById(R.id.title_home);
        tv.setPaintFlags(tv.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);

        ArrayList<ImageView> arrImageView;
        arrImageView = new ArrayList<>();
        arrImageView.add((ImageView) findViewById(R.id.guyeok));
        arrImageView.add((ImageView) findViewById(R.id.juyo));
        arrImageView.add((ImageView) findViewById(R.id.board));
        arrImageView.add((ImageView) findViewById(R.id.gighan));
        findViewById(R.id.modify_myinfo).setOnClickListener(this);

        for (ImageView i : arrImageView) {
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
            case R.id.gighan:
                intent.putExtra("startPosition", 1);
                startActivity(intent);
                break;
            case R.id.juyo:
                intent.putExtra("startPosition", 2);
                startActivity(intent);
                break;
            case R.id.board:
                intent = new Intent(ElectionHomeActivity.this,BoardActivity.class);
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
