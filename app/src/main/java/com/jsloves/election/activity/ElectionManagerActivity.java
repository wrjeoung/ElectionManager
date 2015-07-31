/**
 * Created by wooram.jeong on 2015-07-06.
 */
package com.jsloves.election.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.jsloves.election.fragment.AsyncFragment;
import com.jsloves.election.fragment.AsyncListener;
import com.jsloves.election.util.PhoneInfo;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ElectionManagerActivity extends AppCompatActivity implements AsyncListener<Integer, String> {
    private EditText mEtPass;
    private TextView mTvPass;
    public static final String ASYNC = "async";
    private ProgressDialog mDialog;
    private boolean mIsImeiExist = false;
    private Handler mHandler = new Handler();
    private String mPwd = "";

    private boolean isCheckPassWord() {
        return mEtPass.getText().toString().equals(mPwd);
    }

    private Runnable r = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhoneInfo phoneInfo = PhoneInfo.getInstance(this);

        Log.d("JS", "폰번호 : " + phoneInfo.getPhoneNumber() + " IMEI : " + phoneInfo.getImei() + " MacAddress : " + phoneInfo.getMacAddress());
        JSONObject json = new JSONObject();
        json.put("TYPE", "IMEICHECK");
        json.put("IMEI", phoneInfo.getImei());
        setUp(getString(R.string.server_url),json.toString());
        r = new Runnable() {
            @Override
            public void run() {
                if (mIsImeiExist){
                    Log.d("JS", "isIMIEcheck");
                    setContentView(R.layout.lockscreen);
                    mHandler.postDelayed(mRunnable, 1000);

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
                    Intent intent = new Intent(ElectionManagerActivity.this, JoinActivity.class);
                    //Intent intent = new Intent(ElectionManagerActivity.this, SearchActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        /*if (true){
            Log.d("JS", "isIMIEcheck");
            setContentView(R.layout.lockscreen);
            handler.postDelayed(mRunnable, 1000);

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
            //Intent intent = new Intent(ElectionManagerActivity.this, JoinActivity.class);
            Intent intent = new Intent(ElectionManagerActivity.this, SearchActivity.class);
            startActivity(intent);
        }*/
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

    private void setUp(String url,String params) {
        AsyncFragment async = (AsyncFragment)
                getSupportFragmentManager().findFragmentByTag(ASYNC);

        if (async == null) {
            async = new AsyncFragment();
            Bundle bundle = new Bundle();
            bundle.putString("URL",url);
            bundle.putString("PARAMS",params);
            async.setArguments(bundle);
            FragmentTransaction transaction =
                    getSupportFragmentManager().beginTransaction();
            transaction.add(async, ASYNC);
            transaction.commit();
        }
    }

    private void prepareProgressDialog() {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("Please wait...");
        mDialog.setCancelable(true);
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                AsyncFragment async = (AsyncFragment)
                        getSupportFragmentManager().findFragmentByTag(ASYNC);
                async.cancel();
            }
        });
        //dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }
    private void cleanUp() {
        mDialog.dismiss();
        mDialog = null;
        FragmentManager fm = getSupportFragmentManager();
        AsyncFragment async = (AsyncFragment) fm.findFragmentByTag(ASYNC);
        fm.beginTransaction().remove(async).commit();
    }

    @Override
    public void onPreExecute() {
        if (mDialog == null) {
            prepareProgressDialog();
        }
        mDialog.show();
    }

    @Override
    public void onProgressUpdate(Integer... progress) {

    }

    @Override
    public void onPostExecute(String resultData) {
        try {
            JSONObject re = null;
            JSONParser par = new JSONParser();
            System.out.println("resultData = "+resultData);
            re = (JSONObject) par.parse(resultData);
            mIsImeiExist = (Boolean) re.get("RESULT");
            mPwd = (String)re.get("PWD");
        } catch (Exception e) {
            e.printStackTrace();
        }
        cleanUp();
        mHandler.post(r);
    }

    @Override
    public void onCancelled(String s) {
        cleanUp();
    }
}