package com.jsloves.election.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

/**
 * Created by wooram.jeong on 2015-07-07.
 */
public class PhoneInfo {

    private volatile static PhoneInfo phoneInfo;
    private TelephonyManager mMgr;
    private Context mContext;

    private PhoneInfo(Context context) {
        mContext = context;
        mMgr = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public static PhoneInfo getInstance(Context context) {
        if( phoneInfo == null) {
            synchronized (PhoneInfo.class) {
                if( phoneInfo == null) {
                    phoneInfo = new PhoneInfo(context);
                }
            }
        }
        return phoneInfo;
    }

    public String getPhoneNumber() {
        String phoneNumber = mMgr.getLine1Number();
        return phoneNumber;
    }

    public String getImei () {
        String imei = mMgr.getDeviceId();
        return  imei;
    }

    public String getMacAddress() {
        WifiManager wifi = (WifiManager)mContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }
}


