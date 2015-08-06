
package com.jsloves.election.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jsloves.election.fragment.AsyncFragment;
import com.jsloves.election.fragment.AsyncListener;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.lang.reflect.Type;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements AsyncListener<Integer, String>,OnItemSelectedListener {
    public static final String ASYNC = "async";
    private ProgressDialog dialog;
    private Toolbar toolbar;
    JSONArray array1 = new JSONArray();
    JSONArray array2 = new JSONArray();
    JSONArray array3 = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_search);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.area_info));
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        try {
            JSONObject json1 = new JSONObject();
            json1.put("TYPE", "SELECTITEMS");
            json1.put("TARGET", "SIGUNGU");
            setUp(getString(R.string.server_url),json1.toString());
            //setUp(getString(R.string.server_url_test),"mode=haengjoungdong");
        } catch(Exception e) {
            e.printStackTrace();
        }

        array1.add("소사구");
        array1.add("오정구");
        array2.add("괴안동");
        array2.add("범박동");
        array2.add("여월");
        array3.add("제1투표구");
        array3.add("제2투표구");
        array3.add("제3투표구");

        // Converting JSON String array to Java String array
        //String jsonStringArray = "[\"JSON\",\"To\",\"Java\"]";

        //creating Gson instance to convert JSON array to Java array
        Gson converter = new Gson();

        Type type = new TypeToken<List<String>>(){}.getType();
        List<String> list1 =  converter.fromJson(array1.toString(), type);
        List<String> list2 =  converter.fromJson(array2.toString(), type);
        List<String> list3 =  converter.fromJson(array3.toString(), type);
        //convert List to Array in Java
        //String [] strArray = list.toArray(new String[0]);



        // Spinner
        /*Spinner spinner_1 = (Spinner)findViewById(R.id.spinner_1);
        ArrayAdapter sp1_Adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,list1);
        sp1_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_1.setAdapter(sp1_Adapter);

        Spinner spinner_2 = (Spinner)findViewById(R.id.spinner_2);
        ArrayAdapter sp2_Adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,list2);
        sp2_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_2.setAdapter(sp2_Adapter);

        Spinner spinner_3 = (Spinner)findViewById(R.id.spinner_3);
        ArrayAdapter sp3_Adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,list3);
        sp2_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_3.setAdapter(sp3_Adapter);*/

    }

    private void setUpSpinner(Spinner spinner,String items) {
        Type type = new TypeToken<List<String>>(){}.getType();
        Gson converter = new Gson();
        List<String> list =  converter.fromJson(items.toString(), type);
        ArrayAdapter sp_Adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,list);
        sp_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(sp_Adapter);
        spinner.setOnItemSelectedListener(SearchActivity.this);
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

    // OnItemSelectedListener interface의 abstract 메소드.
    // Spinner를 통해 item의 select되었을때 호출되는 callback.
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("kjh","id = "+id);
    }

    // OnItemSelectedListener interface의 Abstract 메소드.
    // Item list에서 아무것도 선택 하지 않았을때 발생하는 event를 위한 callback.
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Log.d("kjh","onNothingSelected");
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
            JSONObject re = null;
            JSONParser par = new JSONParser();
            System.out.println("resultData = "+resultData);
            re = (JSONObject) par.parse(resultData);
            String result = (String) re.get("RESULT");

            if (result.equals("SUCCESS")) {
                JSONArray sigungu = (JSONArray) re.get("SIGUNGU");
                sigungu.add("부천시 소사구");
                setUpSpinner((Spinner)findViewById(R.id.spinner_1),sigungu.toString());
            } else { // Test code
                JSONArray sigungu = (JSONArray) par.parse(resultData);
                Type type = new TypeToken<List<JSONObject>>() {
                }.getType();
                Gson converter = new Gson();
                List<JSONObject> list1 = converter.fromJson(sigungu.toString(), type);
                Spinner spinner_1 = (Spinner) findViewById(R.id.spinner_1);
                ArrayAdapter sp1_Adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list1);
                sp1_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_1.setAdapter(sp1_Adapter);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cleanUp();
        }
    }

    @Override
    public void onCancelled(String s) {
        cleanUp();
    }
}
