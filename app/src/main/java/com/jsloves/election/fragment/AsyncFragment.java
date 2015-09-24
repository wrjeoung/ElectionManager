package com.jsloves.election.fragment;

import android.os.Bundle;
import android.util.Log;

import com.jsloves.election.util.HttpConnection;

public class AsyncFragment extends PrimesFragment {
    private static final String TAG = AsyncFragment.class.getSimpleName();
    @Override
    protected String doInBackground2(Object... args) {
        Log.d(TAG, "doInBackground2");
        Bundle bundle = (Bundle)args[0];
        String url = bundle.getString("URL");
        String params = bundle.getString("PARAMS");
        return HttpConnection.PostData(url,params);
        //return HttpConnection.GetData(url+"?"+params);
    }
}