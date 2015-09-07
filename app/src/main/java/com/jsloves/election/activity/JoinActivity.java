package com.jsloves.election.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jsloves.election.util.HttpConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.jsloves.election.fragment.*;

public class JoinActivity extends AppCompatActivity implements AsyncListener<Integer, String> {

    public static final String ASYNC = "async";
    private ProgressDialog dialog;
    private Toolbar toolbar;

    public static final String TAG = JoinActivity.class.getSimpleName();
    private EditText user_id = null;
    private EditText passwords = null;
    private EditText name = null;
    private EditText password_conf = null;
    private EditText d_id = null;
    private Button login_but;
    private Button idcheck_but;
    private String id_check = "FAILED";
    private String imei = null;
    private String imsi = null;
    private String phoneNumber = null;
    private String mac = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        imei = telephonyManager.getDeviceId();
        imsi = telephonyManager.getSubscriberId();
        phoneNumber = telephonyManager.getLine1Number();

        WifiManager mWifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo mWifiInfo = mWifiManager.getConnectionInfo();
        mac = mWifiInfo.getMacAddress();

        setContentView(R.layout.join);

        user_id = (EditText) findViewById(R.id.login);
        name = (EditText) findViewById(R.id.name);
        passwords = (EditText) findViewById(R.id.password);
        password_conf = (EditText) findViewById(R.id.password_conf);
        d_id = (EditText) findViewById(R.id.d_id);
        d_id.setText(mac);
        d_id.setFocusableInTouchMode(false);
        login_but = (Button) findViewById(R.id.loginbutton);
        idcheck_but = (Button) findViewById(R.id.idcheck);

        System.out.println("imei:"+imei);
        System.out.println("imsi:"+ imsi);
        System.out.println("phoneNumber:"+phoneNumber);
        System.out.println("user:"+user_id.getText());
        System.out.println("name:"+ name.getText());
        System.out.println("password:"+passwords.getText());
        System.out.println("password_conf:"+password_conf.getText());
        System.out.println("d_id:"+d_id.getText());

        idcheck_but.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("onClick idcheck");

                if (user_id.getText().toString().equals(null) || user_id.getText().toString().equals("")) {
                    Toast.makeText(JoinActivity.this, "아이디를 입력하여 주세요.", Toast.LENGTH_SHORT).show();
                }else {

                    try {

                        System.out.println("user_id:" + user_id.getText());
                        JSONObject jo1 = new JSONObject();
                        jo1.put("TYPE", "IDCHECK");
                        jo1.put("USERID", user_id.getText().toString());

                        if(android.os.Build.VERSION.SDK_INT > 9) {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                        }

                        System.out.println("[IDCHECK] before http call");
                        setUp(getString(R.string.server_url), jo1.toString());
                        System.out.println("[IDCHECK] after http call");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        login_but.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                System.out.println("onClick JOIN");
                System.out.println("user_id:"+user_id.getText());
                System.out.println("name:"+ name.getText());
                System.out.println("password:"+passwords.getText());
                System.out.println("password_conf:"+password_conf.getText());
                System.out.println("d_id:"+imei);
                System.out.println("mac:"+mac);

                if(user_id.getText().toString().equals(null)||user_id.getText().toString().equals("")){
                    Toast.makeText(JoinActivity.this, "아이디를 입력하여 주세요.", Toast.LENGTH_SHORT).show();
                }else if(!id_check.equals("SUCCESS")) {
                    Toast.makeText(JoinActivity.this,"아이디 중복을 확인하여 주세요.", Toast.LENGTH_SHORT).show();
                }else if(name.getText().toString().equals(null)||name.getText().toString().equals("")) {
                    Toast.makeText(JoinActivity.this,"이름을 입력하여 주세요.", Toast.LENGTH_SHORT).show();
                }else if(passwords.getText().toString().equals(null)||passwords.getText().toString().equals("")) {
                    Toast.makeText(JoinActivity.this,"비밀번호을 입력하여 주세요.", Toast.LENGTH_SHORT).show();
                }else if(!passwords.getText().toString().equals(password_conf.getText().toString())){
                    System.out.println("password:"+passwords.getText() + ",password_conf:"+password_conf.getText());
                    Toast.makeText(JoinActivity.this, "비밀번호를 확인하여 주세요.", Toast.LENGTH_SHORT).show();
                }else if(passwords.getText().toString().length()!=4){
                    Toast.makeText(JoinActivity.this, "비밀번호는 4자리 입니다.", Toast.LENGTH_SHORT).show();
                }else {
                    System.out.println("password:" + passwords.getText() + ",password_conf:" + password_conf.getText());
                    JSONObject jo1 = new JSONObject();
                    JSONObject jo2 = new JSONObject();
                    JSONArray contents = new JSONArray();

                    try {

                        jo1.put("TYPE", "JOIN");
                        jo2.put("USERID", user_id.getText().toString());
                        jo2.put("USERNM",name.getText().toString());
                        jo2.put("PWD",passwords.getText().toString());
                        jo2.put("DEVICEID",imei);
                        jo2.put("MAC",mac);
                        jo2.put("PHONENUMBER", phoneNumber);
                        //jo2.put("CLASSCD",classcodes.getText().toString());
                        contents.add(jo2);
                        jo1.put("CONTENTS", contents);

                        if(android.os.Build.VERSION.SDK_INT > 9) {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                        }

                        System.out.println("[JOIN] before http call");
                        setUp(getString(R.string.server_url), jo1.toString());
                        System.out.println("[JOIN] after http call");

                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

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
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
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
        System.out.println("CleanUp");
        dialog.dismiss();
        dialog = null;
        FragmentManager fm = getSupportFragmentManager();
        AsyncFragment async = (AsyncFragment) fm.findFragmentByTag(ASYNC);
        fm.beginTransaction().remove(async).commit();
    }

    @Override
    public void onPreExecute() {
        if (dialog == null) {
            prepareProgressDialog();
        }
        dialog.show();
    }

    @Override
    public void onProgressUpdate(Integer... progress) {

    }

    @Override
    public void onPostExecute(String resultData) {

        try {

            System.out.println("resultData = "+resultData);
            String result = "";
            JSONObject re = null;
            JSONParser par = new JSONParser();
            re = (JSONObject) par.parse(resultData.toString());
            String sType = (String) re.get("TYPE");

            if(sType.equals("IDCHECK")) {

                result = (String) re.get("RESULT");

                if (result.equals("SUCCESS")) {
                    System.out.println("SUCCESS");
                    id_check = "SUCCESS";
                    Toast.makeText(JoinActivity.this, "사용가능한 아이디", Toast.LENGTH_SHORT).show();
                    passwords.requestFocus();
                } else {
                    System.out.println("FAILED");
                    Toast.makeText(JoinActivity.this, "중복된 아이디", Toast.LENGTH_SHORT).show();
                    user_id.setText("");
                }
            }else if(sType.equals("JOIN")){

                result = (String) re.get("RESULT");

                if(result.equals("SUCCESS")){
                    System.out.println("SUCCESS");
                    Toast.makeText(JoinActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(JoinActivity.this, ElectionManagerActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    System.out.println("FAILED");
                    Toast.makeText(JoinActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                }

            }

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e){
           e.printStackTrace();;
        } finally{
            cleanUp();
        }
    }

    @Override
    public void onCancelled(String s) {
        cleanUp();
    }
}
