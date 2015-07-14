/**
 * Created by wooram.jeong on 2015-07-06.
 */
package com.jsloves.election.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import com.jsloves.election.util.PhoneInfo;

public class ElectionManagerActivity extends AppCompatActivity {
    private EditText mEtPass;
    private TextView mTvPass;

    private boolean isCheckPassWord() {
        return mEtPass.getText().toString().equals("0000");
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Handler handler = new Handler();
        PhoneInfo phoneInfo = PhoneInfo.getInstance(this);

        Log.d("JS", "폰번호 : " + phoneInfo.getPhoneNumber() + " IMEI : " + phoneInfo.getImei() + " MacAddress : " + phoneInfo.getMacAddress());

        if ( true){
            Log.d("JS", "isIMIEcheck");
            setContentView(R.layout.lockscreen);
            mEtPass = (EditText) findViewById(R.id.et_pass);
            
            mEtPass.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(mEtPass.getText().toString().length() == 4) {
                        mTvPass = (TextView) findViewById(R.id.tv_pass);
                        if(( isCheckPassWord())) {
                            Intent intent = new Intent(ElectionManagerActivity.this, ElectionMainActivity.class);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Vibrator vr = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                            vr.vibrate(700);
                            mEtPass.getText().clear();
                            mTvPass.setTextColor(Color.parseColor("#ff4444"));
                            mTvPass.setText("암호가 일치하지 않습니다.");

                        }
                    }
                }
            });
        } else {
            finish();
            Intent intent = new Intent(ElectionManagerActivity.this, ElectionSignUpActivity.class);
            startActivity(intent);
        }

        handler.postDelayed(mRunnable,1000);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            InputMethodManager input= (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            input.showSoftInput(mEtPass, 0);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

    }
}