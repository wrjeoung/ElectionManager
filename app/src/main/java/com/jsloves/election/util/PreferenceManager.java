package com.jsloves.election.util;

import java.util.Set;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

/**
 * Created by jhkim01 on 2015-12-14.
 */

public class PreferenceManager {

    private static PreferenceManager mInstance = null;
    private SharedPrefFacade mSharedFacade = null;

    /**
     * 생성자
     *
     * @param context
     *            안드로이드 컨텍스트
     */
    private PreferenceManager( Context context ) {
        mSharedFacade = new SharedPrefFacade(context.getApplicationContext());
    }

    /**
     * 싱글턴 인스턴스를 획득한다
     *
     * @param context
     *            안드로이드 컨텍스트
     * @return 싱글턴 인스턴스
     */
    public static PreferenceManager getInstance( Context context )
    {
        if( null == mInstance ) {
            synchronized(PreferenceManager.class) {
                if( null == mInstance ) {
                    mInstance = new PreferenceManager(
                            context.getApplicationContext());
                }
            }
        }
        return mInstance;
    }

    /**
     * Preference에 특정 키값이 정의되어 있는지 확인한다
     *
     * @param key
     *            확인할 키값
     * @return true이면 키값이 정의되어 있다
     */
    public boolean isContains( String key )
    {
        return mSharedFacade.isContains(key);
    }

    /**
     * Preference의 모든 데이터를 삭제한다
     */
    public void clearAll()
    {
        mSharedFacade.clearAll();
    }

    public String getPrefName()
    {
        return mSharedFacade.getPrefName();
    }

    public void registerOnSharedPreferenceChangeListener( OnSharedPreferenceChangeListener listener )
    {
        mSharedFacade.registerSPChangeListener(listener);
    }

    public void unregisterOnSharedPreferenceChangeListener( OnSharedPreferenceChangeListener listener )
    {
        mSharedFacade.unregisterSPChangeListener(listener);
    }

    /**********************************************************
     * 키값에 대한 getter/setter 함수 리스트
     **********************************************************/

    public void setParseKnownCharacteristics( String strKey, boolean b )
    {
        mSharedFacade.setBooleanValue(strKey, b);
    }

    public boolean isParseKnownCharacteristics( String strKey )
    {
        return mSharedFacade.getBooleanValue(strKey, false);
    }

    /**
     * Preference에 특정 Int 키값을 설정한다
     *
     * @param key
     *            설정할 Int 키값
     * @param value
     *            설정할 데이터
     */
    public void setIntValue( String key, int value )
    {
        mSharedFacade.setIntValue(key, value);
    }

    /**
     * Preference에 특정 String 키값을 설정한다
     *
     * @param key
     *            설정할 String 키값
     * @param value
     *            설정할 데이터
     */
    public void setStringValue( String key, String value )
    {
        mSharedFacade.setStringValue(key, value);
    }

    public void setStringSetValue( String key, Set<String> values )
    {
        mSharedFacade.setStringSetValue(key, values);
    }

    public Set<String> getStringSetValue( String key )
    {
        return mSharedFacade.getStringSetValues(key);
    }

    /**
     * Preference에 특정 boolean 키값을 설정한다
     *
     * @param key
     *            설정할 boolean 키값
     * @param value
     *            설정할 데이터
     */
    public void setBooleanValue( String key, boolean value )
    {
        mSharedFacade.setBooleanValue(key, value);
    }

    /**
     * Preference에 정의된 특정 Int 키값을 가져온다
     *
     * @param key
     *            확인할 Int 키값
     * @return 숫자 데이터로 키값이 없으면 0을 리턴함
     */
    public int getIntValue( String key )
    {
        return getIntValue(key, 0);
    }

    public int getIntValue( String key, int defaultValue )
    {
        return mSharedFacade.getIntValue(key, defaultValue);
    }

    /**
     * Preference에 정의된 특정 String 키값을 가져온다
     *
     * @param key
     *            확인할 String 키값
     * @return 문자열 데이터로 키값이 없으면 공백스트링을 리턴함
     */
    public String getStringValue( String key )
    {
        return getStringValue(key, "");
    }

    public String getStringValue( String key, String defaultValue )
    {
        return mSharedFacade.getStringValue(key, defaultValue);
    }

    /**
     * Preference에 정의된 특정 boolean 키값을 가져온다
     *
     * @param key
     *            확인할 boolean 키값
     * @return true or false
     */
    public boolean getBooleanValue( String key )
    {
        return getBooleanValue(key, false);
    }

    /**
     * Preference에 정의된 특정 boolean 키값을 가져온다
     *
     * @param key
     *            확인할 boolean 키값
     * @return true or false
     */
    public boolean getBooleanValue( String key, boolean defaultValue )
    {
        return mSharedFacade.getBooleanValue(key, defaultValue);
    }

    /**
     * SharedPreference를 효율적으로 접근하기 위한 메소드들을 제공하는 클래스로 FWPreferenceManager에서 사용한다.
     */
    private class SharedPrefFacade
    {
        private SharedPreferences        mSharedPref = null;
        private SharedPreferences.Editor mEditor     = null;

        private final static String      PREF_NAME    = "pref_env";

        /**
         * 생성자
         *
         * @param context
         *            안드로이드 컨텍스트
         */
        public SharedPrefFacade( Context context ) {
            mSharedPref = context.getSharedPreferences(PREF_NAME,
                    Context.MODE_PRIVATE);
            mEditor = mSharedPref.edit();
            mEditor.commit();
        }

        public String getPrefName()
        {
            return PREF_NAME;
        }

        /**
         * Preference에 특정 Int 키값을 설정한다
         *
         * @param key
         *            설정할 Int 키값
         * @param value
         *            설정할 데이터
         */
        public void setIntValue( String key, int value )
        {
            mEditor.putInt(key, value);
            mEditor.commit();
        }

        /**
         * Preference에 특정 String 키값을 설정한다
         *
         * @param key
         *            설정할 String 키값
         * @param value
         *            설정할 데이터
         */
        public void setStringValue( String key, String value )
        {
            mEditor.putString(key, value);
            mEditor.commit();
        }

        /**
         * Preference에 특정 String 키값을 설정한다
         *
         * @param key
         *            설정할 String 키값
         * @param value
         *            설정할 데이터
         */
        public void setStringSetValue( String key, Set<String> values )
        {
            mEditor.putStringSet(key, values);
            mEditor.commit();
        }

        public Set<String> getStringSetValues( String key )
        {
            return mSharedPref.getStringSet(key, null);
        }

        /**
         * Preference에 특정 boolean 키값을 설정한다
         *
         * @param key
         *            설정할 boolean 키값
         * @param value
         *            설정할 데이터
         */
        public void setBooleanValue( String key, boolean value )
        {
            mEditor.putBoolean(key, value);
            mEditor.commit();
        }

        /**
         * Preference에 정의된 특정 Int 키값을 가져온다
         *
         * @param key
         *            확인할 Int 키값
         * @return 숫자 데이터로 키값이 없으면 0을 리턴함
         */
        public int getIntValue( String key )
        {
            return getIntValue(key, 0);
        }

        public int getIntValue( String key, int defaultValue )
        {
            return mSharedPref.getInt(key, defaultValue);
        }

        /**
         * Preference에 정의된 특정 String 키값을 가져온다
         *
         * @param key
         *            확인할 String 키값
         * @return 문자열 데이터로 키값이 없으면 공백스트링을 리턴함
         */
        public String getStringValue( String key )
        {
            return getStringValue(key, "");
        }

        public String getStringValue( String key, String defaultValue )
        {
            return mSharedPref.getString(key, defaultValue);
        }

        /**
         * Preference에 정의된 특정 boolean 키값을 가져온다
         *
         * @param key
         *            확인할 boolean 키값
         * @return true or false
         */
        public boolean getBooleanValue( String key )
        {
            return getBooleanValue(key, false);
        }

        /**
         * Preference에 정의된 특정 boolean 키값을 가져온다
         *
         * @param key
         *            확인할 boolean 키값
         * @return true or false
         */
        public boolean getBooleanValue( String key, boolean defaultValue )
        {
            return mSharedPref.getBoolean(key, defaultValue);
        }

        /**
         * Preference에 특정 키값이 정의되어 있는지 확인한다
         *
         * @param key
         *            확인할 키값
         * @return true이면 키값이 정의되어 있다
         */
        public boolean isContains( String key )
        {
            return(mSharedPref == null ? false : mSharedPref.contains(key));
        }

        /**
         * Preference의 모든 데이터를 삭제한다
         */
        public void clearAll()
        {
            SharedPreferences.Editor editor = mSharedPref.edit();
            editor.clear();
            editor.commit();
        }

        public void registerSPChangeListener(
                OnSharedPreferenceChangeListener listener )
        {
            mSharedPref.registerOnSharedPreferenceChangeListener(listener);
        }

        public void unregisterSPChangeListener(
                OnSharedPreferenceChangeListener listener )
        {
            mSharedPref.unregisterOnSharedPreferenceChangeListener(listener);
        }
    }
}

