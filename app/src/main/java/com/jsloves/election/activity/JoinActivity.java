package com.jsloves.election.activity;

import com.jsloves.election.util.HttpConnection;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.telephony.TelephonyManager;
import android.widget.Spinner;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class JoinActivity extends Activity {

        EditText user_id = null;
	    EditText passwords = null;
        EditText name = null;
        EditText password_conf = null;
        EditText d_id = null;
	    Button login_but;
        Button idcheck_but;
        Spinner spinner1=null;
        Spinner spinner2=null;

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();
            String imsi = telephonyManager.getSubscriberId();

            setContentView(R.layout.join);

            spinner1= (Spinner) findViewById(R.id.spinner1);
            ArrayAdapter adapter1 = ArrayAdapter.createFromResource(this, R.array.group, android.R.layout.simple_spinner_dropdown_item);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner1.setAdapter(adapter1);

            spinner2= (Spinner) findViewById(R.id.spinner2);
            ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this, R.array.classcd, android.R.layout.simple_spinner_dropdown_item);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner2.setAdapter(adapter2);

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

                            System.out.println("[idcheck] before http call");

                            String resultData = "";
                            resultData = HttpConnection.PostData("http://172.30.84.239:8080/Woori/MobileReq.jsp", "CMD=" + jo1.toString());
                            System.out.println("resultData:" + resultData);
                            System.out.println("[idcheck] after http call");

                            JSONObject re = null;
                            JSONParser par = new JSONParser();
                            re = (JSONObject) par.parse(resultData.toString());
                            String result = (String) re.get("RESULT");

                            if (result.equals("SUCCESS")) {
                                System.out.println("SUCCESS");
                                Toast.makeText(JoinActivity.this, "사용가능한 아이디", Toast.LENGTH_SHORT).show();
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
                        System.out.println("d_id:"+d_id.getText());

                        if(user_id.getText().toString().equals(null)||user_id.getText().toString().equals("")){
                            Toast.makeText(JoinActivity.this,"아이디를 입력하여 주세요.", Toast.LENGTH_SHORT).show();
                        }else if(name.getText().toString().equals(null)||name.getText().toString().equals("")) {
                            Toast.makeText(JoinActivity.this,"이름을 입력하여 주세요.", Toast.LENGTH_SHORT).show();
                        }else if(passwords.getText().toString().equals(null)||passwords.getText().toString().equals("")) {
                            Toast.makeText(JoinActivity.this,"비밀번호을 입력하여 주세요.", Toast.LENGTH_SHORT).show();
                        }else if(!passwords.getText().toString().equals(password_conf.getText().toString())){
                            System.out.println("password:"+passwords.getText() + ",password_conf:"+password_conf.getText());
                            Toast.makeText(JoinActivity.this,"비밀번호를 확인하여 주세요.", Toast.LENGTH_SHORT).show();
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
                                jo2.put("DEVICEID",d_id.getText().toString());
                                //jo2.put("CLASSCD",classcodes.getText().toString());
                                contents.add(jo2);
                                jo1.put("CONTENTS", contents);

                                System.out.println("before http call");

                                String resultData = "";
                                resultData = HttpConnection.PostData("http://172.30.84.239:8080/Woori/MobileReq.jsp", "CMD=" + jo1.toString());
                                System.out.println("resultData:"+resultData);
                                System.out.println("after http call");

                                JSONObject re=null;
                                JSONParser par = new JSONParser();
                                re = (JSONObject) par.parse(resultData.toString());
                                String result = (String) re.get("RESULT");

                                if(result.equals("SUCCESS")){
                                    System.out.println("SUCCESS");
                                    Toast.makeText(JoinActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();

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
