/**
 * Created by wooram.jeong on 2015-07-06.
 */
package com.jsloves.election.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ElectionManagerActivity extends AppCompatActivity
        implements AsyncListener<Integer, String>
        , com.jsloves.election.view.KeyPadLayout.KeyPadListener {
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

    // for pdf file download.
    private String mFileName = "final.pdf";
    private String mSaveFolder = "/sdcard";
    private String mServerFileURL = null;

    private boolean isCheckPassWord() {
        return lockPassword.equals(mPwd);
    }

    private Runnable r, mMainCallrunnable = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhoneInfo phoneInfo = PhoneInfo.getInstance(this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Log.d("JS", "폰번호 : " + phoneInfo.getPhoneNumber() + " IMEI : " + phoneInfo.getImei() + " MacAddress : " + phoneInfo.getMacAddress());
        JSONObject json = new JSONObject();
        json.put("TYPE", "CHECK_MACADDRESS");
        json.put("IMEI", phoneInfo.getMacAddress());
        json.put("MD5SUM",md5CheckSum());
        setUp(getString(R.string.server_url), json.toString());

        r = new Runnable() {
            @Override
            public boolean equals(Object o) {
                return super.equals(o);
            }

            @Override
            public void run() {
                Log.d(TAG,"exist imei : "+mIsImeiExist);
                if (mIsImeiExist) {
                    SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                    Boolean no_question_pass = pref.getBoolean("no_question_pass",false);
                    Log.d(TAG,"no_question_pass : "+no_question_pass);

                    if(no_question_pass ) {
                        JSONObject json = new JSONObject();
                        json.put("TYPE", "SELECTITEMS");
                        json.put("ADM_CD", ElectionManagerApp.getInstance().getDefaultAdm_Cd());
                        setUp(getString(R.string.server_url), json.toString());
                    } else {
                        setContentView(R.layout.layout_lock_screen_activity);
                        initView();
                    }

                } else {
                    Intent intent = new Intent(ElectionManagerActivity.this, JoinActivity.class);
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
    }

    private String md5CheckSum()
    {
        String result=null;
        FileInputStream fis=null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(mSaveFolder+"/"+mFileName);

            byte[] dataBytes = new byte[1024];

            int nread = 0;
            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            };

            byte[] mdbytes = md.digest();

            //convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < mdbytes.length; i++) {
                sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
            }

            Log.d(TAG,"Md5CheckSum() Digest(in hex format) 1. : " + sb.toString());
            //convert the byte to hex format method 2
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < mdbytes.length; i++) {
                String hex = Integer.toHexString(0xff & mdbytes[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            result = hexString.toString();
            Log.d(TAG,"Md5CheckSum() Digest(in hex format) 2. : " + hexString.toString());
        }catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG,"Md5CheckSum() pdf file not found exception!!");
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.e(TAG, "Md5CheckSum() no such algorithm exception!!");
        }catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Md5CheckSum() IO exception!!");
        }finally {
            if(fis!=null) { try{fis.close();}catch (IOException e){}}
        }
        return result;
    }


    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            lockPassword = "";
            setPinImage();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void setUp(String url, String params) {
        AsyncFragment async = (AsyncFragment)
                getSupportFragmentManager().findFragmentByTag(ASYNC);

        if (async == null) {
            async = new AsyncFragment();
            Bundle bundle = new Bundle();
            bundle.putString("URL", url);
            bundle.putString("PARAMS", params);
            async.setArguments(bundle);
            FragmentTransaction transaction =
                    getSupportFragmentManager().beginTransaction();
            transaction.add(async, ASYNC);
            transaction.commit();
        }
    }

    private class AsyncTaskForFileDownLoad extends AsyncTask {

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "AsyncTaskForFileDownLoad onPreExecute");
            if (mDialog == null) {
                prepareProgressDialog();
            }
            mDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            Log.d(TAG, "AsyncTaskForFileDownLoad doinBackground");

            URL pdfUrl;
            int Read;
            InputStream is = null;
            FileOutputStream fos = null;
            HttpURLConnection conn = null;

            try {
                File dir = new File(mSaveFolder);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                if (new File(mSaveFolder + "/" + mFileName).exists() == false) {
                    pdfUrl = new URL(mServerFileURL);
                    conn = (HttpURLConnection) pdfUrl.openConnection();
                    int len = conn.getContentLength();
                    byte[] tmpByte = new byte[len];
                    is = conn.getInputStream();
                    File file = new File(mSaveFolder + "/" + mFileName);
                    fos = new FileOutputStream(file);
                    for (; ; ) {
                        Read = is.read(tmpByte);
                        if (Read <= 0) {
                            break;
                        }
                        fos.write(tmpByte, 0, Read);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("ERROR", e.getMessage());
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                    if (conn != null) {
                        conn.disconnect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            Log.d(TAG, "AsyncTaskForFileDownLoad onPostExecute");
            super.onPostExecute(o);
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
            System.out.println("resultData = " + resultData);
            re = (JSONObject) par.parse(resultData);
            String type = (String) re.get("TYPE");
            Log.d(TAG,"onPostExecute re1 : "+re);
            Log.d(TAG,"onPostExecute re2 : "+re);

            if (type.equals("CHECK_MACADDRESS")) {
                mIsImeiExist = (Boolean) re.get("RESULT");
                mPwd = (String) re.get("PWD");
                Log.d(TAG,"pdfpath : "+(String) re.get("PDFPATH"));
                mServerFileURL = (String) re.get("PDFPATH");
                ElectionManagerApp.getInstance().setDefaultAdm_Cd((String) re.get("ADM_CD"));
                mHandler.postDelayed(r,500);
            } else if (type.equals("SELECTITEMS2")) {
                ElectionManagerApp.getInstance().setSelectItems(((JSONObject) re.get("SELECTITEMS2")).toString());
                mHandler.post(mMainCallrunnable);
            } else if (type.equals("SELECTITEMS")) {
                ElectionManagerApp.getInstance().setSelectItems(((JSONObject) re.get("SELECTITEMS")).toString());
                ElectionManagerApp.getInstance().setSelectItemsCode(((JSONObject) re.get("SELECTITEMS_CODE")).toString());
                mHandler.post(mMainCallrunnable);
            }
            boolean updatePdfFile = (boolean) re.get("updatePdfFile");
            Log.d(TAG,"updatePdfFile : "+updatePdfFile);
            if(updatePdfFile
                    || !(new File(mSaveFolder + "/" + mFileName).exists())) {
                AsyncTaskForFileDownLoad task = new AsyncTaskForFileDownLoad();
                task.execute();
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
        Log.d(TAG, "onClick()");
        int i = Integer.parseInt(paramView.getTag().toString());

        if (i >= 0 && i <= 9) {
            lockPassword = lockPassword.concat(String.valueOf(i));
            if (lockPassword.length() == 4) {
                if (isCheckPassWord()) {
                    /*Intent intent = new Intent(ElectionManagerActivity.this, ElectionMainActivity.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();*/
                    JSONObject json = new JSONObject();
                    //json.put("TYPE", "SELECTITEMS2");
                    json.put("TYPE", "SELECTITEMS");
                    json.put("ADM_CD", ElectionManagerApp.getInstance().getDefaultAdm_Cd());
                    setUp(getString(R.string.server_url), json.toString());
                } else {
                    Vibrator vr = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    vr.vibrate(700);
                    mTvPass.setTextColor(Color.parseColor("#ff4444"));
                    mTvPass.setText("암호가 일치하지 않습니다.");
                    mHandler.postDelayed(mRunnable, 700);
                }
            }
        } else {
            if (lockPassword.length() != 0) {
                lockPassword = lockPassword.substring(0, lockPassword.length() - 1);
            }
        }
        setPinImage();
    }

    private void initView() {
        inputPassBox1 = (ImageView) findViewById(R.id.lock_input_first);
        inputPassBox2 = (ImageView) findViewById(R.id.lock_input_second);
        inputPassBox3 = (ImageView) findViewById(R.id.lock_input_third);
        inputPassBox4 = (ImageView) findViewById(R.id.lock_input_fourth);
        mTvPass = (TextView) findViewById(R.id.passcode_input_field_desc);
        setPinImage();
    }


    private void setPinImage() {
        if (lockPassword.length() == 0) {
            inputPassBox1.setEnabled(false);
            inputPassBox2.setEnabled(false);
            inputPassBox3.setEnabled(false);
            inputPassBox4.setEnabled(false);
        } else {
            if (lockPassword.length() == 1) {
                inputPassBox1.setEnabled(true);
                inputPassBox2.setEnabled(false);
                inputPassBox3.setEnabled(false);
                inputPassBox4.setEnabled(false);
                return;
            }
            if (lockPassword.length() == 2) {
                inputPassBox1.setEnabled(true);
                inputPassBox2.setEnabled(true);
                inputPassBox3.setEnabled(false);
                inputPassBox4.setEnabled(false);
                return;
            }
            if (lockPassword.length() == 3) {
                inputPassBox1.setEnabled(true);
                inputPassBox2.setEnabled(true);
                inputPassBox3.setEnabled(true);
                inputPassBox4.setEnabled(false);
                return;
            }
            if (lockPassword.length() == 4) {
                inputPassBox1.setEnabled(true);
                inputPassBox2.setEnabled(true);
                inputPassBox3.setEnabled(true);
                inputPassBox4.setEnabled(true);
                return;
            }
        }
    }
}