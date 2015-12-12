package com.jsloves.election.application;

import android.app.Application;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by juhyukkim on 2015. 8. 14..
 */
public class ElectionManagerApp extends Application {
    private static ElectionManagerApp instance;
    private String mFileName[];
    private String selectItems= null;
    private String selectItemsCode = null;
    private String defaultAdm_Cd = null;

    public String[] getFileName() {
        return mFileName;
    }

    public void setFileName(String[] fileName) {
        this.mFileName = fileName;
    }

    public void setDefaultAdm_Cd(String adm_cd) {
        defaultAdm_Cd = adm_cd;
    }

    public String getDefaultAdm_Cd() {
        return defaultAdm_Cd;
    }

    public void setSelectItems(String s) { selectItems= new String(s); }
    public void setSelectItemsCode(String s) { selectItemsCode= new String(s); }
    public JSONObject getSelectItemsObject() {
        JSONObject jo = null;
        try {
            JSONParser par = new JSONParser();
            jo = (JSONObject) par.parse(selectItems);
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return jo;
    }
    public JSONObject getSelectItemsCodeObject() {
        JSONObject jo = null;
        try {
            JSONParser par = new JSONParser();
            jo = (JSONObject) par.parse(selectItemsCode);
        } catch(ParseException e) {
            e.printStackTrace();
        }
        return jo;
    }

    public String getTupyoguCode(String[] array) {
        String sigunguText = array[0];
        String haengjoungdongText = array[1];
        String tupyoguText = array[2];

        JSONObject selectItemsCodeObject = getSelectItemsCodeObject();
        JSONObject selectItemsObject = getSelectItemsObject();

        JSONArray sigunguCodeArray = (JSONArray)selectItemsCodeObject.get("SIGUNGU");
        JSONArray sigunguTextArray = (JSONArray)selectItemsObject.get("SIGUNGU");
        int sigunguIndex = getIndex(sigunguTextArray,sigunguText);
        String sigunguCode = (String)sigunguCodeArray.get(sigunguIndex);

        JSONArray haengjoungdongCodeArray = (JSONArray)(((JSONObject)selectItemsCodeObject.get("HAENGJOUNGDONG")).get(sigunguCode));
        JSONArray haengjoungdongTextArray = (JSONArray)(((JSONObject)selectItemsObject.get("HAENGJOUNGDONG")).get(sigunguText));
        int haengjoungdongIndex = getIndex(haengjoungdongTextArray,haengjoungdongText);
        String haengjoungCode = (String)haengjoungdongCodeArray.get(haengjoungdongIndex);

        JSONArray tupyoguCodeArray = (JSONArray)(((JSONObject)selectItemsCodeObject.get("TUPYOGU")).get(haengjoungCode));
        JSONArray tupyoguTextArray = (JSONArray)(((JSONObject)selectItemsObject.get("TUPYOGU")).get(haengjoungdongText));
        int tupyoguIndex = getIndex(tupyoguTextArray,tupyoguText);
        String tupyoguCode = (String)tupyoguCodeArray.get(tupyoguIndex);

        return tupyoguCode;
    }

    public static int getIndex(JSONArray array, Object value) {
        int index = -1;

        for(int i = 0; i < array.size(); i++) {
            if(value.equals(array.get(i))) {
                index = i;
                break;
            }
        }
        return index;
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
