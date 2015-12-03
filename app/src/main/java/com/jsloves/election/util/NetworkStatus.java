package com.jsloves.election.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by wooram.jeong on 2015-11-30.
 */
public class NetworkStatus {
    private Context mContext;

    public NetworkStatus(Context context) {
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

    public void networkErrPopup() {
        try {
            if(mContext!=null && mContext instanceof Activity) {
                Toast toast = Toast.makeText(mContext,"네트워크 상태가 불안정 합니다.\r\n다시 접속해주세요",Toast.LENGTH_LONG);
                toast.show();
                ((Activity)mContext).finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
