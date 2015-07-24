package com.jsloves.election.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.math.BigInteger;
import java.util.Map;

public class PrimesFragment extends Fragment {

    private AsyncListener<Integer,String> listener;
    private PrimesTask task;
    private String url;
    private Map<String, String> params;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        task = new PrimesTask();
        task.execute(url,params);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (AsyncListener<Integer,String>)activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void cancel() {
        task.cancel(false);
    }

    class PrimesTask extends AsyncTask<Object, Integer, String> {
        @Override
        protected void onPreExecute() {
           if (listener != null) listener.onPreExecute();
        }

        @Override
        protected String doInBackground(Object... args) {
            int primeToFind = 2000;
            String prime = "ab";
            for (int i=0; i<primeToFind; i++) {
                int percentComplete = (int)((i * 100f)/primeToFind);
                publishProgress(percentComplete);

                if (isCancelled())
                    break;
            }
            return prime;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (listener != null) listener.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            if (listener != null) listener.onPostExecute(result);
        }

        @Override
        protected void onCancelled(String result) {
            if (listener != null) listener.onCancelled(result);
        }
    }
}
