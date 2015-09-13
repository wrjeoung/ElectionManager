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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsloves.election.application.ElectionManagerApp;
import com.jsloves.election.fragment.AsyncFragment;
import com.jsloves.election.fragment.AsyncListener;
import com.jsloves.election.util.PhoneInfo;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ElectionManagerActivity extends AppCompatActivity
        implements AsyncListener<Integer, String>
        , com.jsloves.election.view.KeyPadLayout.keyPadListener{
    public static final String TAG = ElectionManagerActivity.class.getSimpleName();

    private EditText mEtPass;
    private TextView mTvPass;
    public static final String ASYNC = "async";
    private ProgressDialog mDialog;
    private boolean mIsImeiExist = false;
    private Handler mHandler = new Handler();
    private String mPwd = "";
    private String lockPassword = "";
    private ImageView inputPassBox1;
    private ImageView inputPassBox2;
    private ImageView inputPassBox3;
    private ImageView inputPassBox4;

    private boolean isCheckPassWord() {
        return lockPassword.equals(mPwd);
    }

    private Runnable r,mMainCallrunnable = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhoneInfo phoneInfo = PhoneInfo.getInstance(this);

        Log.d("JS", "폰번호 : " + phoneInfo.getPhoneNumber() + " IMEI : " + phoneInfo.getImei() + " MacAddress : " + phoneInfo.getMacAddress());
        JSONObject json = new JSONObject();
        json.put("TYPE", "CHECK_MACADDRESS");
        json.put("IMEI", phoneInfo.getMacAddress());
        setUp(getString(R.string.server_url), json.toString());
        r = new Runnable() {
            @Override
            public boolean equals(Object o) {
                return super.equals(o);
            }

            @Override
            public void run() {
                if (mIsImeiExist) {
                    Log.d("JS", "isIMIEcheck");
                    setContentView(R.layout.layout_lock_screen_activity);
                    initView();

//                    mHandler.postDelayed(mRunnable, 1000);


//                    mEtPass.addTextChangedListener(new TextWatcher() {
//                        @Override
//                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                        }
//
//                        @Override
//                        public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                        }
//
//                        @Override
//                        public void afterTextChanged(Editable s) {
//                            if(mEtPass.getText().toString().length() == 4) {
//                                mTvPass = (TextView) findViewById(R.id.tv_pass);
//                                if(( isCheckPassWord())) {
//                                    Intent intent = new Intent(ElectionManagerActivity.this, ElectionMainActivity.class);
//                                    //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                    startActivity(intent);
//                                    finish();
//                                }
//                                else {
//                                    Vibrator vr = (Vibrator)getSystemService(VIBRATOR_SERVICE);
//                                    vr.vibrate(700);
//                                    mEtPass.getText().clear();
//                                    mTvPass.setTextColor(Color.parseColor("#ff4444"));
//                                    mTvPass.setText("암호가 일치하지 않습니다.");
//
//                                }
//                            }
//                        }
//                    });
//                    mEtPass = (EditText) findViewById(R.id.et_pass);
                } else {
                    Intent intent = new Intent(ElectionManagerActivity.this, JoinActivity.class);
                    //Intent intent = new Intent(ElectionManagerActivity.this, SearchActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        mMainCallrunnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ElectionManagerActivity.this, ElectionHomeActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        };



       /* r = new Runnable() {
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
        };*/


    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            lockPassword="";
            setPinImage();
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
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
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
            String type = (String)re.get("TYPE");
            if(type.equals("CHECK_MACADDRESS")) {
                mIsImeiExist = (Boolean) re.get("RESULT");
                mPwd = (String) re.get("PWD");
                mHandler.post(r);
            } else if(type.equals("SELECTITEMS2")) {
                ElectionManagerApp.getInstance().setSelectItems(((JSONObject) re.get("SELECTITEMS2")).toString());
                mHandler.post(mMainCallrunnable);
            } else if(type.equals("SELECTITEMS")) {
                ElectionManagerApp.getInstance().setSelectItems(((JSONObject) re.get("SELECTITEMS")).toString());
                ElectionManagerApp.getInstance().setSelectItemsCode(((JSONObject) re.get("SELECTITEMS_CODE")).toString());
                mHandler.post(mMainCallrunnable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        cleanUp();
    }

    @Override
    public void onCancelled(String s) {
        cleanUp();
    }

    @Override
    public void keypadClicked(View paramView) {
        Log.d(TAG,"onClick()");
        int i = Integer.parseInt(paramView.getTag().toString());

        if(i>=0 && i<=9) {
            lockPassword = lockPassword.concat(String.valueOf(i));
            if( lockPassword.length()==4) {
                if( isCheckPassWord()) {
                    /*Intent intent = new Intent(ElectionManagerActivity.this, ElectionMainActivity.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();*/
                    JSONObject json = new JSONObject();
                    //json.put("TYPE", "SELECTITEMS2");
                    json.put("TYPE", "SELECTITEMS");
                    setUp(getString(R.string.server_url), json.toString());
                    //setUp("http://192.168.0.52:8080/Woori/MobileReq.jsp", json.toString());
                }
                else {
                    Vibrator vr = (Vibrator)getSystemService(VIBRATOR_SERVICE);
                    vr.vibrate(700);
                    mTvPass.setTextColor(Color.parseColor("#ff4444"));
                    mTvPass.setText("암호가 일치하지 않습니다.");
                    mHandler.postDelayed(mRunnable,700);

                }
            }
        }
        else {
            if(lockPassword.length() != 0) {
                lockPassword = lockPassword.substring(0,lockPassword.length()-1);
            }
        }
        setPinImage();
    }

    private void initView() {
        inputPassBox1 = (ImageView)findViewById(R.id.lock_input_first);
        inputPassBox2 = (ImageView)findViewById(R.id.lock_input_second);
        inputPassBox3 = (ImageView)findViewById(R.id.lock_input_third);
        inputPassBox4 = (ImageView)findViewById(R.id.lock_input_fourth);
        mTvPass = (TextView)findViewById(R.id.passcode_input_field_desc);
        setPinImage();
    }



    private void setPinImage()
    {
        if(lockPassword.length() == 0)
        {
            inputPassBox1.setEnabled(false);
            inputPassBox2.setEnabled(false);
            inputPassBox3.setEnabled(false);
            inputPassBox4.setEnabled(false);
        }
        else
        {
            if(lockPassword.length() == 1)
            {
                inputPassBox1.setEnabled(true);
                inputPassBox2.setEnabled(false);
                inputPassBox3.setEnabled(false);
                inputPassBox4.setEnabled(false);
                return;
            }
            if(lockPassword.length() == 2)
            {
                inputPassBox1.setEnabled(true);
                inputPassBox2.setEnabled(true);
                inputPassBox3.setEnabled(false);
                inputPassBox4.setEnabled(false);
                return;
            }
            if(lockPassword.length() == 3)
            {
                inputPassBox1.setEnabled(true);
                inputPassBox2.setEnabled(true);
                inputPassBox3.setEnabled(true);
                inputPassBox4.setEnabled(false);
                return;
            }
            if(lockPassword.length() == 4)
            {
                inputPassBox1.setEnabled(true);
                inputPassBox2.setEnabled(true);
                inputPassBox3.setEnabled(true);
                inputPassBox4.setEnabled(true);
                return;
            }
        }
    }
}