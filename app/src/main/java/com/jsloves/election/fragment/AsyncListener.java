package com.jsloves.election.fragment;

public interface AsyncListener<Progress, Result> {
    void onPreExecute();
    void onProgressUpdate(Progress... progress);
    void onPostExecute(Result result);
    void onCancelled(Result result);
}
