package com.jsloves.election.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by wooram.jeong on 2015-11-30.
 */
public class NetworkConnection {
    private volatile static NetworkConnection mNwCheck;
    private Context mContext;
    private final String ASYNC = "async";


    private NetworkConnection(Context context) {
            mContext = context;
    }

    public boolean isNetworkAvailible() {

        ConnectivityManager connMgr = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static NetworkConnection getInstance(Context context) {
        if (mNwCheck == null) {
            synchronized (NetworkConnection.class) {
                if (mNwCheck == null) {
                    mNwCheck = new NetworkConnection(context);
                }
            }
        }
        return mNwCheck;
    }
}
