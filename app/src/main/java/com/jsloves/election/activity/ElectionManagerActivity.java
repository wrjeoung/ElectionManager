/**
 * Created by wooram.jeong on 2015-07-06.
 */
package com.jsloves.election.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.jsloves.election.util.PhoneInfo;

public class ElectionManagerActivity extends Activity {
    private  EditText mEditText;

    private boolean isCheckPassWord() {
        return mEditText.getText().toString().equals("0000");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhoneInfo phoneInfo = PhoneInfo.getInstance(this);
        Log.d("JS","폰번호 : "+phoneInfo.getPhoneNumber() + " IMEI : "+phoneInfo.getImei()+" MacAddress : "+phoneInfo.getMacAddress());

        if ( true){
            Log.d("JS", "isIMIEcheck");
            setContentView(R.layout.lockscreen);
            mEditText = (EditText) findViewById(R.id.editText1);

            mEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(mEditText.getText().toString().length() == 4) {
                        if(( isCheckPassWord())) {
                            Intent intent = new Intent(ElectionManagerActivity.this, ElectionMainActivity.class);
                            //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            finish();
                        }
                        else {


                        }
                    }
                }
            });
        } else {
            finish();
            Intent intent = new Intent(ElectionManagerActivity.this, ElectionSignUpActivity.class);
            startActivity(intent);
        }
    }


}