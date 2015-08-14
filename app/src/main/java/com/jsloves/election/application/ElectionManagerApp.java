package com.jsloves.election.application;

import android.app.Application;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by juhyukkim on 2015. 8. 14..
 */
public class ElectionManagerApp extends Application {
    private static ElectionManagerApp instance;
    private static String selectItems= null;
    public static void setSelectItems(String s) { selectItems= new String(s); }
    public static JSONObject getSelectItemsObject() throws ParseException {
        JSONParser par = new JSONParser();
        return (JSONObject) par.parse(selectItems);
    }

    public static ElectionManagerApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
