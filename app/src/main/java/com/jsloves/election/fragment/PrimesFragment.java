package com.jsloves.election.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

public class PrimesFragment extends Fragment {

    private static final String TAG = PrimesFragment.class.getSimpleName();
    private AsyncListener<Integer,String> listener;
    private PrimesTask task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Bundle bundle = getArguments();
        if(bundle != null) {
            task = new PrimesTask();
            task.execute(bundle);
        }
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

    protected String doInBackground2(Object... args) {
        Log.d(TAG, "doInBackground2");
        return "return result";
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
            /* int primeToFind = 2000;
            for (int i=0; i<primeToFind; i++) {
                int percentComplete = (int)((i * 100f)/primeToFind);
                publishProgress(percentComplete);

                if (isCancelled())
                    break;
            }*/
            return doInBackground2(args);
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
