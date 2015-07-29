package com.jsloves.election.fragment;

import android.os.Bundle;

import com.jsloves.election.util.HttpConnection;

public class AsyncFragment extends PrimesFragment {

    @Override
    protected String doInBackground2(Object... args) {
        Bundle bundle = (Bundle)args[0];
        String url = bundle.getString("URL");
        String params = bundle.getString("PARAMS");
        return HttpConnection.PostData(url,params);
        //return HttpConnection.GetData(url+"?"+params);
    }
}