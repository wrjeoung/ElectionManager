package com.jsloves.election.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.jsloves.election.fragment.AsyncListener;
import com.jsloves.election.fragment.PrimesFragment;

import java.math.BigInteger;

public class Example5Activity extends FragmentActivity
implements AsyncListener<Integer, String> {

    public static final String PRIMES = "primes";
    private ProgressDialog dialog;
    private TextView resultView;
    private Button goButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch2_example_layout);
        ((TextView)findViewById(R.id.title)).setText("Chapter2 Example5");
        ((TextView)findViewById(R.id.description)).setText("Retained Headless Fragment to continue AsyncTask processing across activity restarts");

        resultView = (TextView)findViewById(R.id.result);
        goButton = (Button)findViewById(R.id.go);
        goButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                PrimesFragment primes = (PrimesFragment)
                    getSupportFragmentManager().findFragmentByTag(PRIMES);

                if (primes == null) {
                    primes = new PrimesFragment();
                    FragmentTransaction transaction =
                        getSupportFragmentManager().beginTransaction();
                    transaction.add(primes, PRIMES);
                    transaction.commit();
                }
            }
        });
    }

    public void onPreExecute() {
        onProgressUpdate(0);
    }

    public void onProgressUpdate(Integer... progress) {
        if (dialog == null) {
            prepareProgressDialog();
        }
        dialog.setProgress(progress[0]);
    }

    public void onPostExecute(String result) {
        resultView.setText(result);
        cleanUp();
    }

    public void onCancelled(String result) {
        resultView.setText("cancelled at " + result);
        cleanUp();
    }

    private void prepareProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.setTitle("Calculating");
        dialog.setCancelable(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                PrimesFragment primes = (PrimesFragment)
                        getSupportFragmentManager().findFragmentByTag(PRIMES);
                primes.cancel();
            }
        });
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setProgress(0);
        dialog.setMax(100);
        dialog.show();
    }

    private void cleanUp() {
        dialog.dismiss();
        dialog = null;
        FragmentManager fm = getSupportFragmentManager();
        PrimesFragment primes = (PrimesFragment) fm.findFragmentByTag(PRIMES);
        fm.beginTransaction().remove(primes).commit();
    }
}
