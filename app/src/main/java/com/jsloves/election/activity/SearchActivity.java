
package com.jsloves.election.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.simple.JSONArray;

import java.lang.reflect.Type;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    JSONArray array1 = new JSONArray();
    JSONArray array2 = new JSONArray();
    JSONArray array3 = new JSONArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_search);
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
        Spinner spinner_1 = (Spinner)findViewById(R.id.spinner_1);
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
        spinner_3.setAdapter(sp3_Adapter);

    }
}
