/**
 * Created by wooram.jeong on 2015-07-06.
 */
package com.jsloves.election.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jsloves.election.application.ElectionManagerApp;
import com.jsloves.election.fragment.AsyncFragment;
import com.jsloves.election.fragment.AsyncListener;
import com.jsloves.election.net.RestApiProvider;
import com.jsloves.election.util.NetworkStatus;
import com.jsloves.election.util.PhoneInfo;
import com.jsloves.election.util.PreferenceManager;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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

/**
 *  Lock 비밀번호 입력 화면.
 */
public class ElectionManagerActivity extends AppCompatActivity
        implements AsyncListener<Integer, String>
        , com.jsloves.election.view.KeyPadLayout.KeyPadListener {
    public static final String TAG = ElectionManagerActivity.class.getSimpleName();

    private static final int SIGNUP_AND_LOCKSCREEN = 1;
    private static final int RESET_PASSWORD = 2;
    private static final int MAIN = 3;
    private static final int MD5SUM_CHECK_FINISH = 4;

    private TextView mTvPass;
    public static final String ASYNC = "async";
    private ProgressDialog mDialog;
    private boolean mIsExistMacAdd = false;
    private String mClasscd;
    private String mPwd = "";
    private String lockPassword = "";
    private ImageView inputPassBox1;
    private ImageView inputPassBox2;
    private ImageView inputPassBox3;
    private ImageView inputPassBox4;

    // for pdf file download.
    private String mServerFileURL[];

    private NetworkStatus mNetConn;

    private boolean isCheckPassWord() {
        return lockPassword.equals(mPwd);
    }

    private File mFile[];
    private int mPdfCount;

    private String mFileName[];
    private boolean mUpdatePdfFile[];
    private PreferenceManager mPreferenceManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PhoneInfo phoneInfo = PhoneInfo.getInstance(this);
        mNetConn = new NetworkStatus(this);
        mPreferenceManager = PreferenceManager.getInstance(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Log.d("JS", "폰번호 : " + phoneInfo.getPhoneNumber() + " IMEI : " + phoneInfo.getImei() + " MacAddress : " + phoneInfo.getMacAddress());
        JSONObject json = new JSONObject();
        String md5chekSum = null;

//
//        json.put("existsPdfAtclient",mFile.exists());
        json.put("TYPE", "CHECK_MACADDRESS");
        json.put("IMEI", phoneInfo.getMacAddress());
        json.put("MD5SUM", md5chekSum);
        network_join(RestApiProvider.API_COMMON_URL, json.toString());
    }
    // ZZZ 는 승인되지 않은 초기 사용자 코드.
    private boolean isAuthorizedUser() {
        if(mClasscd!=null && !mClasscd.equals("ZZZ"))
            return true;
        else
            return false;
    }

    private String md5CheckSum(int index)
    {
        String result=null;
        FileInputStream fis=null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Log.d(TAG,"md5CheckSum() this.getFilesDir() : "+getFilesDir().getAbsolutePath()+" mFileName : "+mFileName);
            //fis = new FileInputStream(mSaveFolder+"/"+mFileName);
            fis = openFileInput(mFileName[index]);

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

    @Override
    protected void onResume() {
        super.onResume();

    }

    private class AsyncTaskForMd5kSumCheck extends AsyncTask<Void, Void, JSONArray> {
        private int mFileCount;

        public AsyncTaskForMd5kSumCheck(int fileCount) {
            mFileCount = fileCount;
        }

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "AsyncTaskForMd5kSumCheck onPreExecute");
            if (mDialog == null) {
                prepareProgressDialog();
            }
            mDialog.show();
        }

        @Override
        protected JSONArray doInBackground(Void... params) {
            Log.d(TAG, "AsyncTaskForMd5kSumCheck doinBackground");
            JSONArray jsonArry = new JSONArray();

            for(int i = 0 ; i < mFileCount; i++) {
                if(mFileName[i] != null && mFileName[i].length() > 0) {
                    JSONObject fileJson = new JSONObject();
                    String md5Sum = md5CheckSum(i);
                    fileJson.put("FILE_NAME",mFileName[i]);
                    fileJson.put("MD5SUM",md5Sum);
                    jsonArry.add(fileJson);
                }
            }
            return jsonArry;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            Message message = Message.obtain();
            message.obj = jsonArray;
            message.what = MD5SUM_CHECK_FINISH;
            mHandler.sendMessage(message);
        }
    }

    private class AsyncTaskForFileDownLoad extends AsyncTask {
        private Context mContext;

        public AsyncTaskForFileDownLoad(Context context) {
            mContext = context;
        }


        @Override
        protected void onPreExecute() {
            Log.d(TAG, "AsyncTaskForFileDownLoad onPreExecute");
            if (mDialog == null) {
                prepareProgressDialog();
            }

            if(!mDialog.isShowing()) {
                mDialog.show();
            }
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
                // 2015.11.14 rjeong
                // mSeverFileURL - macaddress가 등록되지 않은 기기에서 앱 실행시 DB에 PDFPATH 컬럼이 존재 하지 않기 때문에 null리턴.
                for(int i=0; i<mPdfCount; i++) {
                    Log.d(TAG, "doInBackground mServerFileURL["+i+"] :  "+ mServerFileURL[i]);
                    if ( mFile[i]!=null
                            && ( !mFile[i].exists() || mUpdatePdfFile[i])
                            && mServerFileURL[i]!=null) {
                        pdfUrl = new URL(mServerFileURL[i]);
                        conn = (HttpURLConnection) pdfUrl.openConnection();
                        int len = conn.getContentLength();
                        byte[] tmpByte = new byte[len];
                        is = conn.getInputStream();
                        File file = new File(mContext.getFilesDir(), mFileName[i]);
                        fos = new FileOutputStream(file);
                        fos = openFileOutput(mFileName[i],Context.MODE_PRIVATE);

                        for (; ; ) {
                            Read = is.read(tmpByte);
                            if (Read <= 0) {
                                break;
                            }
                            fos.write(tmpByte, 0, Read);
                        }
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

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case SIGNUP_AND_LOCKSCREEN :
                    Log.d(TAG, "exist macAdd : " + mIsExistMacAdd);
                    Log.d(TAG, "mClasscd : " + mClasscd);

                    if (mIsExistMacAdd) {
                        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                        Boolean no_question_pass = pref.getBoolean("no_question_pass",false);
                        Log.d(TAG,"no_question_pass : "+no_question_pass);

                        if(isAuthorizedUser()) {
                            if (no_question_pass) {
                                String fileNames = mPreferenceManager.getStringValue("FILE_NAME", "");
                                if(fileNames != null && fileNames.length() > 0) {
                                    md5SumCheck(fileNames);
                                } else {
                                    post_selectitems();
                                }
                                //network_join("http://192.168.0.6:8080/ElectionManager_server/MobileReq.jsp", json.toString());
                            } else {
                                setContentView(R.layout.layout_lock_screen_activity);
                                initView();
                            }
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(),"관리자의 승인이 필요합니다.\r\n승인 완료 후 접속해주세요",Toast.LENGTH_LONG);
                            toast.show();
                            finish();
                        }

                    } else {
                        Intent intent = new Intent(ElectionManagerActivity.this, JoinActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    break;

                case RESET_PASSWORD :
                    lockPassword = "";
                    setPinImage();
                    break;

                case MAIN:
                    Intent intent = new Intent(ElectionManagerActivity.this, ElectionHomeActivity.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                    break;

                case MD5SUM_CHECK_FINISH :
                    JSONArray jArray = (JSONArray)msg.obj;
                    post_selectitems(jArray);
                    break;

                default :
                    break;
            }
        }
    };

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

        if(!mDialog.isShowing()) {
            mDialog.show();
        }

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
                mIsExistMacAdd = (Boolean) re.get("RESULT");
                mPwd = (String) re.get("PWD");
                ElectionManagerApp.getInstance().setDefaultAdm_Cd((String) re.get("ADM_CD"));
                mClasscd = (String)re.get("CLASSCD");
                mHandler.sendEmptyMessageDelayed(SIGNUP_AND_LOCKSCREEN, 500);
            } else if (type.equals("SELECTITEMS")) {
                ElectionManagerApp.getInstance().setSelectItems(((JSONObject) re.get("SELECTITEMS")).toString());
                ElectionManagerApp.getInstance().setSelectItemsCode(((JSONObject) re.get("SELECTITEMS_CODE")).toString());
                JSONArray pdfInfoArrays;
                pdfInfoArrays=(JSONArray)re.get("PDFINFOARRAYS");
                Log.d(TAG,"onPostExecute SELECTITEMS pdfInfoArrays : "+pdfInfoArrays);
                mPdfCount = pdfInfoArrays.size();
                mServerFileURL = new String[mPdfCount];
                mFileName = new String[mPdfCount];
                mFile = new File[mPdfCount];
                mUpdatePdfFile = new boolean[mPdfCount];
                for(int i=0; i<mPdfCount; i++ ) {
                    JSONObject pdfFileInfo = (JSONObject)pdfInfoArrays.get(i);
                    String pdfpath = (String)pdfFileInfo.get("PDFPATH");
                    boolean updatePdfFile = (Boolean)pdfFileInfo.get("UPDATE_PDF_FILE");

                    mServerFileURL[i] = pdfpath;
                    int index = pdfpath.lastIndexOf("/");
                    pdfpath = pdfpath.substring(index+1);
                    mFileName[i] = pdfpath;
                    mFile[i] = new File(this.getFilesDir(), mFileName[i]);
                    mUpdatePdfFile[i] = updatePdfFile;
                    Log.d(TAG,"onPostExecute SELECTITEMS mFile["+i+"].exists() : "+mFile[i].exists());
                    Log.d(TAG,"onPostExecute SELECTITEMS updatePdfFile : "+updatePdfFile);
                    Log.d(TAG,"onPostExecute SELECTITEMS fileName : "+mFileName[i]);
                    if (mFile[i]!=null
                            && (!mFile[i].exists()
                                || updatePdfFile)
                    ) {
                        AsyncTaskForFileDownLoad task = new AsyncTaskForFileDownLoad(this);
                        task.execute();
                    }
                    Log.d(TAG,"pdfPath : "+pdfpath);
                }

                Gson gson = new Gson();
                String jsonData = gson.toJson(mFileName);
                mPreferenceManager.setStringValue("FILE_NAME",jsonData);

                ((ElectionManagerApp) getApplication()).setFileName(mFileName);
                mHandler.sendEmptyMessage(MAIN);
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
                    String fileNames = mPreferenceManager.getStringValue("FILE_NAME", "");
                    if(fileNames != null && fileNames.length() > 0) {
                        md5SumCheck(fileNames);
                    } else {
                        post_selectitems();
                    }
                    //network_join("http://192.168.0.6:8080/ElectionManager_server/MobileReq.jsp", json.toString());
                } else {
                    Vibrator vr = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    vr.vibrate(700);
                    mTvPass.setTextColor(Color.parseColor("#ff4444"));
                    mTvPass.setText("암호가 일치하지 않습니다.");
                    mHandler.sendEmptyMessageDelayed(RESET_PASSWORD,700);
                }
            }
        } else {
            if (lockPassword.length() != 0) {
                lockPassword = lockPassword.substring(0, lockPassword.length() - 1);
            }
        }
        setPinImage();
    }

    private void md5SumCheck(String fileName) {
        if(fileName == null || fileName.length() < 1) {
            return;
        }
        Gson gson = new Gson();
        String[] fileNameArray = gson.fromJson(fileName, String[].class);
        int fileCount = fileNameArray.length;

        mFileName = new String[fileCount];
        for(int i = 0 ; i < fileCount; i++) {
            File file = new File(getFilesDir(), fileNameArray[i]);
            if(file.exists()) {
                mFileName[i] = fileNameArray[i];
            }
        }
        new AsyncTaskForMd5kSumCheck(fileCount).execute();
    }

    private void post_selectitems(JSONArray jArray) {
        JSONObject json = new JSONObject();

        PhoneInfo phoneInfo = PhoneInfo.getInstance(getApplicationContext());
        json.put("TYPE", "SELECTITEMS");
        json.put("MACADDRESS", phoneInfo.getMacAddress());
        json.put("ADM_CD", ElectionManagerApp.getInstance().getDefaultAdm_Cd());
        json.put("CLASSCD", mClasscd);
        json.put("FILE_INFO",jArray);
        network_join(RestApiProvider.API_COMMON_URL, json.toString());
    }

    private void post_selectitems() {
        JSONObject json = new JSONObject();

        Log.d(TAG, "post_selectitems() mFile : "+mFile);
        PhoneInfo phoneInfo = PhoneInfo.getInstance(getApplicationContext());
        json.put("TYPE", "SELECTITEMS");
        json.put("MACADDRESS", phoneInfo.getMacAddress());
        json.put("ADM_CD", ElectionManagerApp.getInstance().getDefaultAdm_Cd());
        json.put("CLASSCD", mClasscd);
        network_join(RestApiProvider.API_COMMON_URL, json.toString());
    }

    private void network_join(String url,String params) {
        if (mNetConn != null && mNetConn.isNetworkAvailible()) {
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
        } else {
            mNetConn.networkErrPopup();
        }
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