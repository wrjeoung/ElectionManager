package com.jsloves.election.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jsloves.election.util.HttpConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JoinActivity extends Activity {

    private EditText user_id = null;
    private EditText passwords = null;
    private EditText name = null;
    private EditText password_conf = null;
    private EditText d_id = null;
    private Button login_but;
    private Button idcheck_but;
    private String id_check = null;
    private String imei = null;
    private String imsi = null;
    private String phoneNumber = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        imei = telephonyManager.getDeviceId();
        imsi = telephonyManager.getSubscriberId();
        phoneNumber = telephonyManager.getLine1Number();

        setContentView(R.layout.join);

        user_id = (EditText) findViewById(R.id.login);
        name = (EditText) findViewById(R.id.name);
        passwords = (EditText) findViewById(R.id.password);
        password_conf = (EditText) findViewById(R.id.password_conf);
        d_id = (EditText) findViewById(R.id.d_id);
        d_id.setText(imei);
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
                System.out.println("onClick idcheck ssss");

                if (user_id.getText().toString().equals(null) || user_id.getText().toString().equals("")) {
                    Toast.makeText(JoinActivity.this, "아이디를 입력하여 주세요.", Toast.LENGTH_SHORT).show();
                }else {

                    try {

                        System.out.println("user_id:" + user_id.getText());
                        JSONObject jo1 = new JSONObject();
                        jo1.put("TYPE", "IDCHECK");
                        jo1.put("USERID", user_id.getText().toString());

                        System.out.println("[IDCHECK] before http call");

                        if(android.os.Build.VERSION.SDK_INT > 9) {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                        }

                        String resultData = "";
                        resultData = HttpConnection.PostData("http://222.122.149.161:7070/Woori/MobileReq.jsp", jo1.toString());
                        System.out.println("resultData:" + resultData);
                        System.out.println("[IDCHECK] after http call");

                        JSONObject re = null;
                        JSONParser par = new JSONParser();
                        re = (JSONObject) par.parse(resultData.toString());
                        String result = (String) re.get("RESULT");

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
                        jo2.put("PHONENUMBER",phoneNumber);
                        //jo2.put("CLASSCD",classcodes.getText().toString());
                        contents.add(jo2);
                        jo1.put("CONTENTS", contents);

                        System.out.println("[JOIN] before http call");

                        if(android.os.Build.VERSION.SDK_INT > 9) {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                        }

                        String resultData = "";
                        //resultData = HttpConnection.PostData("http://222.122.149.161:7070/Woori/MobileReq.jsp", "CMD=" + jo1.toString());
                        resultData = HttpConnection.PostData("http://222.122.149.161:7070/Woori/MobileReq.jsp", jo1.toString());
                        System.out.println("resultData:"+resultData);
                        System.out.println("after http call");

                        JSONObject re=null;
                        JSONParser par = new JSONParser();
                        re = (JSONObject) par.parse(resultData.toString());
                        String result = (String) re.get("RESULT");

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

                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}
