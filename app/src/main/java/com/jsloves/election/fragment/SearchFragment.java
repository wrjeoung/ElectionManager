package com.jsloves.election.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jsloves.election.DTO.ElectDao;
import com.jsloves.election.DTO.FamilyDAO;
import com.jsloves.election.DTO.PdfDAO;
import com.jsloves.election.DTO.StatsDAO;
import com.jsloves.election.DTO.VoteDAO;
import com.jsloves.election.activity.PDFViewActivity;
import com.jsloves.election.activity.R;
import com.jsloves.election.application.ElectionManagerApp;
import com.jsloves.election.util.GeoPoint;
import com.jsloves.election.util.GeoTrans;
import com.jsloves.election.util.GpsInfo;
import com.jsloves.election.util.HttpConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.lang.reflect.Type;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements OnItemSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String TAG = SearchFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private SearchTask mTask;
    private ProgressDialog dialog;
    private WebView myWebview;
    private Spinner sp1;
    private Spinner sp2;
    private Spinner sp3;
    private boolean ignoreUpdate;

    private StringBuffer mTest = new StringBuffer();

    private String mSigungu;
    private String mHangjungdong;
    private String mTupyogu;
    private String mAdm_cd;
    private Handler mHandler = new Handler(Looper.getMainLooper());


    // GPS
    private Button gpsSearchBtn;
    private GpsInfo gps;

    // politics enviroment of AreaInfo.
    // voteRate
    // Button
    private ImageButton mVoterate_btn;

    // sub of voteRate
    // Button
    private ImageButton mVoteratesub_btnM;
    private ImageButton mVoteratesub_btnP;
    // header
    private TableRow mWrapper_voteratesub_header;
    // sungugu.
    private TableRow mWrapper_voteratesub_sungugu;
    private TextView mVoteratesub_sunggugu_avg;
    private TextView mVoteratesub_sunggugu_6th;
    private TextView mVoteratesub_sunggugu_19th;
    private TextView mVoteratesub_sunggugu_18th_1;
    private TextView mVoteratesub_sunggugu_18th_2;
    // haengjoungdong
    private TableRow mWrapper_voteratesub_haengjoung;
    private TextView mVoteratesub_haengjoung_avg;
    private TextView mVoteratesub_haengjoung_6th;
    private TextView mVoteratesub_haengjoung_19th;
    private TextView mVoteratesub_haengjoung_18th_1;
    private TextView mVoteratesub_haengjoung_18th_2;
    // tupyogu
    private TableRow mWrapper_voteratesub_tupyogu;
    private TextView mVoteratesub_tupyogu_avg;
    private TextView mVoteratesub_tupyogu_6th;
    private TextView mVoteratesub_tupyogu_19th;
    private TextView mVoteratesub_tupyogu_18th_1;
    private TextView mVoteratesub_tupyogu_18th_2;


    // social enviroment of AreaInfo.
    // ratio of voter age group.
    // header
    private TableRow mWrraper_voteratio_header;
    // sungugu.
    private TableRow mWrraper_sungugu;
    private TextView mSungugu_20th;
    private TextView mSungugu_30th;
    private TextView mSungugu_40th;
    private TextView mSungugu_40th_under;
    private TextView mSungugu_50th_over;
    private TextView mSungugu_50th;
    private TextView mSungugu_60th;
    // hangjungdong.
    private TableRow mWrraper_hangjung;
    private TextView mHangjung_20th;
    private TextView mHangjung_30th;
    private TextView mHangjung_40th;
    private TextView mHangjung_40th_under;
    private TextView mHangjung_50th_over;
    private TextView mHangjung_50th;
    private TextView mHangjung_60th;
    // tupyogu
    private TableRow mWrraper_typyogu;
    private TextView mtypyogu_20th;
    private TextView mtypyogu_30th;
    private TextView mtypyogu_40th;
    private TextView mtypyogu_40th_under;
    private TextView mtypyogu_50th_over;
    private TextView mtypyogu_50th;
    private TextView mtypyogu_60th;

    // statistics of population.
    // avarage age.
    private LinearLayout mLwrraper_header_population;
    private LinearLayout mLwrraper_age;
    private ImageButton mButton_age;
    private TextView mTtupyogu_age;
    private TextView mTsungugu_age;
    // wife ratio.
    private LinearLayout mLwrraper_wife;
    private ImageButton mButton_wife;
    private TextView mTtupyogu_wife;
    private TextView mTsungugu_wife;
    // graduate of the University
    private LinearLayout mLwrraper_univercity;
    private ImageButton mButton_univercity;
    private TextView mTtupyogu_univercity;
    private TextView mTsungugu_univercity;
    // Population density
    private LinearLayout mLwrraper_population;
    private ImageButton mButton_population;
    private TextView mTtupyogu_population;
    private TextView mTsungugu_population;
    // Aging
    private LinearLayout mLwrraper_ageing;
    private ImageButton mButton_ageing;
    private TextView mTtupyogu_ageing;
    private TextView mTsungugu_ageing;
    // 유년부양비
    private LinearLayout mLwrraper_dependency;
    private ImageButton mButton_dependency;
    private TextView mTtupyogu_dependency;
    private TextView mTsungugu_dependency;

    // statistics of memberOfHouseHold
    private LinearLayout mLwrraper_header_familly;
    private LinearLayout mLwrraper_one_man;
    private ImageButton mButton_one_man;
    private TextView mTtupyogu_one_man;
    private TextView mTsungugu_one_man;

    private LinearLayout mLwrraper_two_more;
    private ImageButton mButton_two_more;
    private TextView mTtupyogu_two_more;
    private TextView mTsungugu_two_more;

    private LinearLayout mLwrraper_myhouse;
    private ImageButton mButton_myhouse;
    private TextView mTtupyogu_myhouse;
    private TextView mTsungugu_myhouse;

    private LinearLayout mLwrraper_apt;
    private ImageButton mButton_apt;
    private TextView mTtupyogu_apt;
    private TextView mTsungugu_apt;

    private LinearLayout mLwrraper_40m_over;
    private ImageButton mButton_40m_over;
    private TextView mTtupyogu_40m_over;
    private TextView mTsungugu_40m_over;

    // 평균가구원.
    private LinearLayout mLwrraper_memberOfHouseHold;
    private ImageButton mButton_memberOfHouseHold;
    private TextView mTtupyogu_memberOfHouseHold;
    private TextView mTsungugu_memberOfHouseHold;

    private ImageButton mPerson;
    private boolean mPageLoadFinished;
    private JSONObject mMapdata;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private void initVoteRateSub(View view) {
        mVoteratesub_btnM = (ImageButton) view.findViewById(R.id.voteratesub_btnM);
        mVoteratesub_btnP = (ImageButton) view.findViewById(R.id.voteratesub_btnP);

        mWrapper_voteratesub_header = (TableRow) view.findViewById(R.id.wrapper_voteratesub_header);
        // sunggugu
        mWrapper_voteratesub_sungugu = (TableRow) view.findViewById(R.id.wrapper_voteratesub_sungugu);
        mVoteratesub_sunggugu_avg = (TextView) view.findViewById(R.id.voteratesub_sungugu_avg);
        mVoteratesub_sunggugu_6th = (TextView) view.findViewById(R.id.voteratesub_sungugu_6th);
        mVoteratesub_sunggugu_19th = (TextView) view.findViewById(R.id.voteratesub_sungugu_19th);
        mVoteratesub_sunggugu_18th_1 = (TextView) view.findViewById(R.id.voteratesub_sungugu_18th_1);
        mVoteratesub_sunggugu_18th_2 = (TextView) view.findViewById(R.id.voteratesub_sungugu_18th_2);
        // haengjoungdong
        mWrapper_voteratesub_haengjoung = (TableRow) view.findViewById(R.id.wrapper_voteratesub_haengjoung);
        mVoteratesub_haengjoung_avg = (TextView) view.findViewById(R.id.voteratesub_haengjoung_avg);
        mVoteratesub_haengjoung_6th = (TextView) view.findViewById(R.id.voteratesub_haengjoung_6th);
        mVoteratesub_haengjoung_19th = (TextView) view.findViewById(R.id.voteratesub_haengjoung_19th);
        mVoteratesub_haengjoung_18th_1 = (TextView) view.findViewById(R.id.voteratesub_haengjoung_18th_1);
        mVoteratesub_haengjoung_18th_2 = (TextView) view.findViewById(R.id.voteratesub_haengjoung_18th_2);
        // tupyogu
        mWrapper_voteratesub_tupyogu = (TableRow) view.findViewById(R.id.wrapper_voteratesub_tupyogu);
        mVoteratesub_tupyogu_avg = (TextView) view.findViewById(R.id.voteratesub_tupyogu_avg);
        mVoteratesub_tupyogu_6th = (TextView) view.findViewById(R.id.voteratesub_tupyogu_6th);
        mVoteratesub_tupyogu_19th = (TextView) view.findViewById(R.id.voteratesub_tupyogu_19th);
        mVoteratesub_tupyogu_18th_1 = (TextView) view.findViewById(R.id.voteratesub_tupyogu_18th_1);
        mVoteratesub_tupyogu_18th_2 = (TextView) view.findViewById(R.id.voteratesub_tupyogu_18th_2);
    }

    private void setVisivilityVoteRateSub(int size) {

        switch (size) {
            case 1:
                mWrapper_voteratesub_header.setVisibility(View.VISIBLE);
                mWrapper_voteratesub_sungugu.setVisibility(View.VISIBLE);
                mWrapper_voteratesub_haengjoung.setVisibility(View.GONE);
                mWrapper_voteratesub_tupyogu.setVisibility(View.GONE);
                break;
            case 2:
                mWrraper_voteratio_header.setVisibility(View.VISIBLE);
                mWrapper_voteratesub_sungugu.setVisibility(View.VISIBLE);
                mWrapper_voteratesub_haengjoung.setVisibility(View.VISIBLE);
                mWrapper_voteratesub_tupyogu.setVisibility(View.GONE);
                break;
            case 3:
                mWrraper_voteratio_header.setVisibility(View.VISIBLE);
                mWrapper_voteratesub_sungugu.setVisibility(View.VISIBLE);
                mWrapper_voteratesub_haengjoung.setVisibility(View.VISIBLE);
                mWrapper_voteratesub_tupyogu.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void setDataVoteRateSub(JSONArray data) {

        Gson gs = new Gson();
        Log.d(TAG, "setDataVoteRateSub data.toJSONString : " + data.toJSONString());
        Log.d(TAG, "setDataVoteRateSub data.size() : " + data.size());
        Log.d(TAG, "setDataVoteRateSub data.get(0) : " + data.get(0));

        for (int i = 0; i < data.size(); i++) {
            ElectDao ed = gs.fromJson((String) data.get(i), ElectDao.class);

            switch (i) {
                case 0:
                    mVoteratesub_sunggugu_avg.setText(String.valueOf(ed.getAvg()));
                    mVoteratesub_sunggugu_6th.setText(String.valueOf(ed.getF6th()));
                    mVoteratesub_sunggugu_19th.setText(String.valueOf(ed.getF19th()));
                    mVoteratesub_sunggugu_18th_1.setText(String.valueOf(ed.getF18th_1()));
                    mVoteratesub_sunggugu_18th_2.setText(String.valueOf(ed.getF18th_2()));
                    break;
                case 1:
                    mVoteratesub_haengjoung_avg.setText(String.valueOf(ed.getAvg()));
                    mVoteratesub_haengjoung_6th.setText(String.valueOf(ed.getF6th()));
                    mVoteratesub_haengjoung_19th.setText(String.valueOf(ed.getF19th()));
                    mVoteratesub_haengjoung_18th_1.setText(String.valueOf(ed.getF18th_1()));
                    mVoteratesub_haengjoung_18th_2.setText(String.valueOf(ed.getF18th_2()));
                    break;
                case 2:
                    mVoteratesub_tupyogu_avg.setText(String.valueOf(ed.getAvg()));
                    mVoteratesub_tupyogu_6th.setText(String.valueOf(ed.getF6th()));
                    mVoteratesub_tupyogu_19th.setText(String.valueOf(ed.getF19th()));
                    mVoteratesub_tupyogu_18th_1.setText(String.valueOf(ed.getF18th_1()));
                    mVoteratesub_tupyogu_18th_2.setText(String.valueOf(ed.getF18th_2()));
                    break;
                default:
                    break;
            }
        }
    }


    private void initVoterRatioOfAge(View view) {
        mWrraper_voteratio_header = (TableRow) view.findViewById(R.id.wrraper_voteratio_header);
        mWrraper_sungugu = (TableRow) view.findViewById(R.id.wrraper_sungugu);
        mSungugu_20th = (TextView) view.findViewById(R.id.sungugu_20th);
        mSungugu_30th = (TextView) view.findViewById(R.id.sungugu_30th);
        mSungugu_40th = (TextView) view.findViewById(R.id.sungugu_40th);
        mSungugu_40th_under = (TextView) view.findViewById(R.id.sungugu_40th_under);
        mSungugu_50th_over = (TextView) view.findViewById(R.id.sungugu_50th_over);
        mSungugu_50th = (TextView) view.findViewById(R.id.sungugu_50th);
        mSungugu_60th = (TextView) view.findViewById(R.id.sungugu_60th);

        mWrraper_hangjung = (TableRow) view.findViewById(R.id.wrraper_hangjung);
        mHangjung_20th = (TextView) view.findViewById(R.id.hangjung_20th);
        mHangjung_30th = (TextView) view.findViewById(R.id.hangjung_30th);
        mHangjung_40th = (TextView) view.findViewById(R.id.hangjung_40th);
        mHangjung_40th_under = (TextView) view.findViewById(R.id.hangjung_40th_under);
        mHangjung_50th_over = (TextView) view.findViewById(R.id.hangjung_50th_over);
        mHangjung_50th = (TextView) view.findViewById(R.id.hangjung_50th);
        mHangjung_60th = (TextView) view.findViewById(R.id.hangjung_60th);

        mWrraper_typyogu = (TableRow) view.findViewById(R.id.wrraper_tupyogu);
        mtypyogu_20th = (TextView) view.findViewById(R.id.tupyogu_20th);
        mtypyogu_30th = (TextView) view.findViewById(R.id.tupyogu_30th);
        mtypyogu_40th = (TextView) view.findViewById(R.id.tupyogu_40th);
        mtypyogu_40th_under = (TextView) view.findViewById(R.id.tupyogu_40th_under);
        mtypyogu_50th_over = (TextView) view.findViewById(R.id.tupyogu_50th_over);
        mtypyogu_50th = (TextView) view.findViewById(R.id.tupyogu_50th);
        mtypyogu_60th = (TextView) view.findViewById(R.id.tupyogu_60th);
    }

    private void setVisivilityVoterRatioOfAge(int size) {

        switch (size) {
            case 1:
                mWrraper_voteratio_header.setVisibility(View.VISIBLE);
                mWrraper_sungugu.setVisibility(View.VISIBLE);
                mWrraper_hangjung.setVisibility(View.GONE);
                mWrraper_typyogu.setVisibility(View.GONE);
                break;
            case 2:
                mWrraper_voteratio_header.setVisibility(View.VISIBLE);
                mWrraper_sungugu.setVisibility(View.VISIBLE);
                mWrraper_hangjung.setVisibility(View.VISIBLE);
                mWrraper_typyogu.setVisibility(View.GONE);
                break;
            case 3:
                mWrraper_voteratio_header.setVisibility(View.VISIBLE);
                mWrraper_sungugu.setVisibility(View.VISIBLE);
                mWrraper_hangjung.setVisibility(View.VISIBLE);
                mWrraper_typyogu.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private void setDataVoterRAtionOfAge(JSONArray data) {

        Gson gs = new Gson();
        Log.d(TAG, "setDataVoterRAtionOfAge data.toJSONString : " + data.toJSONString());
        Log.d(TAG, "setDataVoterRAtionOfAge data.size() : " + data.size());
        Log.d(TAG, "setDataVoterRAtionOfAge data.get(0) : " + data.get(0));

        for (int i = 0; i < data.size(); i++) {
            VoteDAO vd = gs.fromJson((String) data.get(i), VoteDAO.class);

            switch (i) {
                case 0:
                    mSungugu_20th.setText(String.valueOf(vd.getV20th()));
                    mSungugu_30th.setText(String.valueOf(vd.getV30th()));
                    mSungugu_40th.setText(String.valueOf(vd.getV40th()));
                    mSungugu_40th_under.setText(String.valueOf(vd.getV40th_under()));
                    mSungugu_50th_over.setText(String.valueOf(vd.getV50th_over()));
                    mSungugu_50th.setText(String.valueOf(vd.getV50th()));
                    mSungugu_60th.setText(String.valueOf(vd.getV60th_over()));
                    break;
                case 1:
                    mHangjung_20th.setText(String.valueOf(vd.getV20th()));
                    mHangjung_30th.setText(String.valueOf(vd.getV30th()));
                    mHangjung_40th.setText(String.valueOf(vd.getV40th()));
                    mHangjung_40th_under.setText(String.valueOf(vd.getV40th_under()));
                    mHangjung_50th_over.setText(String.valueOf(vd.getV50th_over()));
                    mHangjung_50th.setText(String.valueOf(vd.getV50th()));
                    mHangjung_60th.setText(String.valueOf(vd.getV60th_over()));
                    break;
                case 2:
                    mtypyogu_20th.setText(String.valueOf(vd.getV20th()));
                    mtypyogu_30th.setText(String.valueOf(vd.getV30th()));
                    mtypyogu_40th.setText(String.valueOf(vd.getV40th()));
                    mtypyogu_40th_under.setText(String.valueOf(vd.getV40th_under()));
                    mtypyogu_50th_over.setText(String.valueOf(vd.getV50th_over()));
                    mtypyogu_50th.setText(String.valueOf(vd.getV50th()));
                    mtypyogu_60th.setText(String.valueOf(vd.getV60th_over()));
                    break;
                default:
                    break;
            }
        }
    }

    private class ChromeClient extends WebChromeClient {
        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            Log.d(TAG, consoleMessage.message() + '\n' + consoleMessage.messageLevel() + '\n' + consoleMessage.sourceId());
            return super.onConsoleMessage(consoleMessage);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean dialog,
                                      boolean userGesture, Message resultMsg) {
            // TODO Auto-generated method stub
            return super.onCreateWindow(view, dialog, userGesture, resultMsg);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message,
                                   JsResult result) {
            Log.d(TAG, "[onJsConfirm]");

            final JsResult res = result;
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.app_name)
                    .setMessage(message)
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        public void onCancel(DialogInterface dialog) {
                            res.cancel();
                        }
                    })
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    res.confirm();
                                }
                            })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    res.cancel();
                                }
                            }).show();

            return true;
        }

        /** JavaScript Alert */
        @Override
        public boolean onJsAlert(WebView view, String url, String message,
                                 JsResult result) {
            Log.d(TAG,"[onJsAlert]");

            AlertDialog.Builder builder = new AlertDialog.Builder(
                    getActivity());
            builder.setMessage(message)
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        public void onCancel(DialogInterface dialog) {

                            dialog.dismiss();
                        }
                    })
                    .setNeutralButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
                                    arg0.dismiss();
                                }
                            }).show();
            result.cancel();

            return true;
        }

        @JavascriptInterface
        public void funcCallSearchByJSParam(final String adm_cd) {
            Log.d(TAG, "funcCallSearchByJSParam() >>>>>>>>>>> :" + adm_cd);
            mAdm_cd = adm_cd;
            showMap(mAdm_cd);
        }
    }

    private class PdfViewOnclickListner implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.d(TAG,"PdfViewOnclickListner v.getTag() : "+v.getTag());
            if(v.getTag() == null) {
                return;
            }

            Intent intent = new Intent(getActivity(), PDFViewActivity.class);
            String[] pages = v.getTag().toString().split(";");

            int startPageNum = Integer.parseInt(pages[0]);
            int endPageNum = pages.length > 1 ? Integer.parseInt(pages[pages.length-1]) : startPageNum;
            intent.putExtra("pdfStartPageNum",startPageNum);
            intent.putExtra("pdfEndPageNum",endPageNum);
            startActivity(intent);
        }
    }

    private void initStatisticsOfpopulation(View view) {
        mLwrraper_header_population = (LinearLayout) view.findViewById(R.id.wrraper_header_population);
        mLwrraper_age = (LinearLayout) view.findViewById(R.id.wrraper_age);
        mButton_age = (ImageButton) view.findViewById(R.id.button_age);
        mTtupyogu_age = (TextView) view.findViewById(R.id.tupyogu_age);
        mTsungugu_age = (TextView) view.findViewById(R.id.sungugu_age);

        mLwrraper_wife = (LinearLayout) view.findViewById(R.id.wrraper_wife);
        mButton_wife = (ImageButton) view.findViewById(R.id.button_wife);
        mTtupyogu_wife = (TextView) view.findViewById(R.id.tupyogu_wife);
        mTsungugu_wife = (TextView) view.findViewById(R.id.sungugu_wife);

        mLwrraper_univercity = (LinearLayout) view.findViewById(R.id.wrraper_univercity);
        mButton_univercity = (ImageButton) view.findViewById(R.id.button_univercity);
        mTtupyogu_univercity = (TextView) view.findViewById(R.id.tupyogu_univer);
        mTsungugu_univercity = (TextView) view.findViewById(R.id.sungugu_univer);

        mLwrraper_population = (LinearLayout) view.findViewById(R.id.wrraper_population);
        mButton_population = (ImageButton) view.findViewById(R.id.button_population);
        mTtupyogu_population = (TextView) view.findViewById(R.id.tupyogu_population);
        mTsungugu_population = (TextView) view.findViewById(R.id.sungugu_population);

        mLwrraper_ageing = (LinearLayout) view.findViewById(R.id.wrraper_ageing);
        mButton_ageing = (ImageButton) view.findViewById(R.id.button_ageing);
        mTtupyogu_ageing = (TextView) view.findViewById(R.id.tupyogu_ageing);
        mTsungugu_ageing = (TextView) view.findViewById(R.id.sungugu_ageing);

        mLwrraper_dependency = (LinearLayout) view.findViewById(R.id.wrraper_dependency);
        mButton_dependency = (ImageButton) view.findViewById(R.id.button_dependency);
        mTtupyogu_dependency = (TextView) view.findViewById(R.id.tupyogu_dependency);
        mTsungugu_dependency = (TextView) view.findViewById(R.id.sungugu_dependency);
    }

    private void setVisivilityStatisticsOfpopulation(int size) {

        if (size > 0) {
            mLwrraper_header_population.setVisibility(View.VISIBLE);
            mLwrraper_age.setVisibility(View.VISIBLE);
            mButton_age.setVisibility(View.VISIBLE);
            mTtupyogu_age.setVisibility(View.VISIBLE);
            mTsungugu_age.setVisibility(View.VISIBLE);

            mLwrraper_wife.setVisibility(View.VISIBLE);
            mButton_wife.setVisibility(View.VISIBLE);
            mTtupyogu_wife.setVisibility(View.VISIBLE);
            mTsungugu_wife.setVisibility(View.VISIBLE);

            mLwrraper_univercity.setVisibility(View.VISIBLE);
            mButton_univercity.setVisibility(View.VISIBLE);
            mTtupyogu_univercity.setVisibility(View.VISIBLE);
            mTsungugu_univercity.setVisibility(View.VISIBLE);

            mLwrraper_population.setVisibility(View.VISIBLE);
            mButton_population.setVisibility(View.VISIBLE);
            mTtupyogu_population.setVisibility(View.VISIBLE);
            mTsungugu_population.setVisibility(View.VISIBLE);

            mLwrraper_ageing.setVisibility(View.VISIBLE);
            mButton_ageing.setVisibility(View.VISIBLE);
            mTtupyogu_ageing.setVisibility(View.VISIBLE);
            mTsungugu_ageing.setVisibility(View.VISIBLE);

            mLwrraper_dependency.setVisibility(View.VISIBLE);
            mButton_dependency.setVisibility(View.VISIBLE);
            mTtupyogu_dependency.setVisibility(View.VISIBLE);
            mTsungugu_dependency.setVisibility(View.VISIBLE);
        }
    }

    private void setDataStatisticsOfpopulation(JSONArray data) {
        Gson gs = new Gson();
        Log.d(TAG, "setDataStatisticsOfpopulation data.toJSONString : " + data.toJSONString());
        Log.d(TAG, "setDataStatisticsOfpopulation data.size() : " + data.size());
        Log.d(TAG, "setDataStatisticsOfpopulation data.get(0) : " + data.get(0));

        for (int i = 0; i < data.size(); i++) {
            StatsDAO sd = gs.fromJson((String) data.get(i), StatsDAO.class);
            switch (i) {
                case 0:
                    mTsungugu_age.setText(String.valueOf(sd.getAge_avg()));
                    mTsungugu_wife.setText(String.valueOf(sd.getPartner_yn()));
                    mTsungugu_univercity.setText(String.valueOf(sd.getUniv_over()));
                    mTsungugu_population.setText(String.valueOf(sd.getPop_dnsity()));
                    mTsungugu_ageing.setText(String.valueOf(sd.getAged_child_ratio()));
                    mTsungugu_dependency.setText(String.valueOf(sd.getChildhood_alimony()));

                    Log.d(TAG, "setDataStatisticsOfpopulation sd.getAge_avg() : " + sd.getAge_avg());
                    Log.d(TAG, "setDataStatisticsOfpopulation sd.getAged_child_ratio() : " + sd.getAged_child_ratio());
                    //Log.d(TAG, "setDataStatisticsOfpopulation sd.getFamily_avg() : " + sd.getFamily_avg());
                    Log.d(TAG, "setDataStatisticsOfpopulation sd.getPop_dnsity() : " + sd.getPop_dnsity());
                    break;
                case 1:
                    mTtupyogu_age.setText(String.valueOf(sd.getAge_avg()));
                    mTtupyogu_wife.setText(String.valueOf(sd.getPartner_yn()));
                    mTtupyogu_univercity.setText(String.valueOf(sd.getUniv_over()));
                    mTtupyogu_population.setText(String.valueOf(sd.getPop_dnsity()));
                    mTtupyogu_ageing.setText(String.valueOf(sd.getChildhood_alimony()));
                    mTtupyogu_dependency.setText(String.valueOf(sd.getChildhood_alimony()));
                    break;
                default:
                    break;
            }
        }

        if (data.size() == 1) {
            mTtupyogu_age.setText("정보없음");
            mTtupyogu_wife.setText("정보없음");
            mTtupyogu_univercity.setText("정보없음");
            mTtupyogu_population.setText("정보없음");
            mTtupyogu_ageing.setText("정보없음");
            mTtupyogu_dependency.setText("정보없음");
        }

    }

    private void initStatisticsOfFamily(View view) {
        mLwrraper_header_familly = (LinearLayout) view.findViewById(R.id.wrraper_header_familly);
        mLwrraper_one_man = (LinearLayout) view.findViewById(R.id.wrraper_one_man);
        mButton_one_man = (ImageButton) view.findViewById(R.id.button_one_man);
        mTtupyogu_one_man = (TextView) view.findViewById(R.id.tupyogu_one_man);
        mTsungugu_one_man = (TextView) view.findViewById(R.id.sungugu_one_man);

        mLwrraper_two_more = (LinearLayout) view.findViewById(R.id.wrraper_two_more);
        mButton_two_more = (ImageButton) view.findViewById(R.id.button_two_more);
        mTtupyogu_two_more = (TextView) view.findViewById(R.id.tupyogu_two_more);
        mTsungugu_two_more = (TextView) view.findViewById(R.id.sungugu_two_more);

        mLwrraper_myhouse = (LinearLayout) view.findViewById(R.id.wrraper_myhouse);
        mButton_myhouse = (ImageButton) view.findViewById(R.id.button_myhouse);
        mTtupyogu_myhouse = (TextView) view.findViewById(R.id.tupyogu_myhouse);
        mTsungugu_myhouse = (TextView) view.findViewById(R.id.sungugu_myhouse);

        mLwrraper_apt = (LinearLayout) view.findViewById(R.id.wrraper_apt);
        mButton_apt = (ImageButton) view.findViewById(R.id.button_apt);
        mTtupyogu_apt = (TextView) view.findViewById(R.id.tupyogu_apt);
        mTsungugu_apt = (TextView) view.findViewById(R.id.sungugu_apt);

        mLwrraper_40m_over = (LinearLayout) view.findViewById(R.id.wrraper_40m_over);
        mButton_40m_over = (ImageButton) view.findViewById(R.id.button_40m_over);
        mTtupyogu_40m_over = (TextView) view.findViewById(R.id.tupyogu_40m_over);
        mTsungugu_40m_over = (TextView) view.findViewById(R.id.sungugu_40m_over);

        mLwrraper_memberOfHouseHold = (LinearLayout) view.findViewById(R.id.wrraper_memberOfHouseHold);
        mButton_memberOfHouseHold = (ImageButton) view.findViewById(R.id.button_memberOfHouseHold);
        mTtupyogu_memberOfHouseHold = (TextView) view.findViewById(R.id.tupyogu_memberOfHouseHold);
        mTsungugu_memberOfHouseHold = (TextView) view.findViewById(R.id.sungugu_memberOfHouseHold);
    }

    private void setVisivilityStaticsticsOfFamily(int size) {

        if(size > 0) {
            mLwrraper_header_familly.setVisibility(View.VISIBLE);
            mLwrraper_one_man.setVisibility(View.VISIBLE);
            mButton_one_man.setVisibility(View.VISIBLE);
            mTtupyogu_one_man.setVisibility(View.VISIBLE);
            mTsungugu_one_man.setVisibility(View.VISIBLE);

            mLwrraper_two_more.setVisibility(View.VISIBLE);
            mButton_two_more.setVisibility(View.VISIBLE);
            mTtupyogu_two_more.setVisibility(View.VISIBLE);
            mTsungugu_two_more.setVisibility(View.VISIBLE);

            mLwrraper_myhouse.setVisibility(View.VISIBLE);
            mButton_myhouse.setVisibility(View.VISIBLE);
            mTtupyogu_myhouse.setVisibility(View.VISIBLE);
            mTsungugu_myhouse.setVisibility(View.VISIBLE);

            mLwrraper_apt.setVisibility(View.VISIBLE);
            mButton_apt.setVisibility(View.VISIBLE);
            mTtupyogu_apt.setVisibility(View.VISIBLE);
            mTsungugu_apt.setVisibility(View.VISIBLE);

            mLwrraper_40m_over.setVisibility(View.VISIBLE);
            mButton_40m_over.setVisibility(View.VISIBLE);
            mTtupyogu_40m_over.setVisibility(View.VISIBLE);
            mTsungugu_40m_over.setVisibility(View.VISIBLE);

            mLwrraper_memberOfHouseHold.setVisibility(View.VISIBLE);
            mButton_memberOfHouseHold.setVisibility(View.VISIBLE);
            mTtupyogu_memberOfHouseHold.setVisibility(View.VISIBLE);
            mTsungugu_memberOfHouseHold.setVisibility(View.VISIBLE);
        }
    }

    private void setDataStatisticsOfFamily(JSONArray data) {
        Gson gs = new Gson();
        Log.d(TAG, "setDataStatisticsOfFamily data.toJSONString : " + data.toJSONString());
        Log.d(TAG, "setDataStatisticsOfFamily data.size() : " + data.size());
        Log.d(TAG, "setDataStatisticsOfFamily data.get(0) : " + data.get(0));

        for (int i = 0; i < data.size(); i++) {
            FamilyDAO fd = gs.fromJson((String) data.get(i), FamilyDAO.class);
            switch (i) {
                case 0:
                    mTsungugu_one_man.setText(String.valueOf(fd.getFamily_one()));
                    mTsungugu_two_more.setText(String.valueOf(fd.getFamily_two_over()));
                    mTsungugu_myhouse.setText(String.valueOf(fd.getMyhome_ratio()));
                    mTsungugu_apt.setText(String.valueOf(fd.getApt_ratio()));
                    mTsungugu_40m_over.setText(String.valueOf(fd.getF40m_over()));
                    mTsungugu_memberOfHouseHold.setText(String.valueOf(fd.getFamily_avg()));
                    break;
                case 1:
                    mTtupyogu_one_man.setText(String.valueOf(fd.getFamily_one()));
                    mTtupyogu_two_more.setText(String.valueOf(fd.getFamily_two_over()));
                    mTtupyogu_myhouse.setText(String.valueOf(fd.getMyhome_ratio()));
                    mTtupyogu_apt.setText(String.valueOf(fd.getApt_ratio()));
                    mTtupyogu_40m_over.setText(String.valueOf(fd.getF40m_over()));
                    mTtupyogu_memberOfHouseHold.setText(String.valueOf(fd.getFamily_avg()));
                    break;
                default:
                    break;
            }
        }

        if (data.size() == 1) {
            mTtupyogu_one_man.setText("정보없음");
            mTtupyogu_two_more.setText("정보없음");
            mTtupyogu_myhouse.setText("정보없음");
            mTtupyogu_apt.setText("정보없음");
            mTtupyogu_40m_over.setText("정보없음");
           mTtupyogu_memberOfHouseHold.setText("정보없음");
        }
    }

    private void setDataOfPdf(JSONArray data) {
        Gson gs = new Gson();
        Log.d(TAG, "setDataOfPdf data.toJSONString : " + data.toJSONString());
        Log.d(TAG, "setDataOfPdf data.size() : " + data.size());

        for (int i = 0; i < data.size(); i++) {
            PdfDAO pd = gs.fromJson((String) data.get(i), PdfDAO.class);

            String pdf_code = pd.getPdf_code();
            String pdf_page = pd.getPdf_page();
            String pdf_etc = pd.getPdf_etc();

            if(pdf_code.equals("B_11")) {
                if(pdf_etc.equalsIgnoreCase("p")) {
                    mVoterate_btn.setTag(pdf_page);
                }
            } else if(pdf_code.equals("B_21")) {
                if(pdf_etc.equalsIgnoreCase("p")) {
                    mVoteratesub_btnP.setTag(pdf_page);
                } else if(pdf_etc.equalsIgnoreCase("m")) {
                    mVoteratesub_btnM.setTag(pdf_page);
                }
            } else if(pdf_code.equals("A_11")) {
                if(pdf_etc.equalsIgnoreCase("m")) {
                    mPerson.setTag(pdf_page);
                }
            } else if(pdf_code.equals("A_25")) {
                if(pdf_etc.equalsIgnoreCase("p")) {
                    mButton_age.setTag(pdf_page);
                }
            } else if(pdf_code.equals("A_24")) {
                if(pdf_etc.equalsIgnoreCase("p")) {
                    mButton_wife.setTag(pdf_page);
                }
            } else if(pdf_code.equals("A_23")) {
                if(pdf_etc.equalsIgnoreCase("p")) {
                    mButton_univercity.setTag(pdf_page);
                }
            } else if(pdf_code.equals("A_26")) {
                if(pdf_etc.equalsIgnoreCase("p")) {
                    mButton_population.setTag(pdf_page);
                }
            } else if(pdf_code.equals("A_27")) {
                if(pdf_etc.equalsIgnoreCase("p")) {
                    mButton_ageing.setTag(pdf_page);
                }
            } else if(pdf_code.equals("A_28")) {
                if(pdf_etc.equalsIgnoreCase("p")) {
                    mButton_dependency.setTag(pdf_page);
                }
            } else if(pdf_code.equals("A_32")) {
                if(pdf_etc.equalsIgnoreCase("p")) {
                    mButton_one_man.setTag(pdf_page);
                }
            } else if(pdf_code.equals("A_33")) {
                if(pdf_etc.equalsIgnoreCase("p")) {
                    mButton_two_more.setTag(pdf_page);
                }
            } else if(pdf_code.equals("A_34")) {
                if(pdf_etc.equalsIgnoreCase("p")) {
                    mButton_myhouse.setTag(pdf_page);
                }
            } else if(pdf_code.equals("A_35")) {
                if(pdf_etc.equalsIgnoreCase("p")) {
                    mButton_apt.setTag(pdf_page);
                }
            } else if(pdf_code.equals("A_36")) {
                if(pdf_etc.equalsIgnoreCase("p")) {
                    mButton_40m_over.setTag(pdf_page);
                }
            } else if(pdf_code.equals("A_31")) {
                if(pdf_etc.equalsIgnoreCase("p")) {
                    mButton_memberOfHouseHold.setTag(pdf_page);
                }
            }

        }
    }

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setRetainInstance(true);
        ignoreUpdate = false;
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Log.d(TAG, " mParam1 : " + mParam1 + " mParam2 : " + mParam2);
    }

    private class MainWebViewClient extends WebViewClient {
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed(); // SSL 에러가 발생해도 계속 진행!
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "shouldOverrideUrlLoading url : " + url);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if(url.equals(getString(R.string.mapView_url))) {
                mPageLoadFinished = true;
            }

            if(mPageLoadFinished && mMapdata != null ) {
                myWebview.loadUrl("javascript:drawMap('" + mMapdata.toString() + "')");
                mMapdata = null;
            }
            //CookieSyncManager.getInstance().sync();
        }

    }

    private boolean isAllLevelClick(String sigungu, String hangjungdong, String tupyogu) {

        if (sigungu != null
                && hangjungdong != null
                && tupyogu != null) {
            return true;
        } else {
            return false;
        }
    }

    private void showMap(String adm_cd) {
        //tupyogu = tupyogu.replace("제","");
        //tupyogu = tupyogu.replace("투표구","");
        Log.d(TAG, "showMap mTest : " + mTest);
        JSONObject jo = new JSONObject();
        //jo.put("TYPE","GEODATA_TEST");
        jo.put("TYPE","TEST");
        jo.put("ADM_CD", adm_cd);
        jo.put("COX", 0.0);
        jo.put("COY", 0.0);
        Log.d(TAG, "showMap jo : " + jo);

        excuteTask(getString(R.string.server_url), jo.toString());
        //excuteTask("http://192.168.0.8:8080/Woori/MobileReq.jsp", jo.toString());
        /*myWebview.loadUrl("javascript:drawMap('" + jo.toString() + "')");
        if(myWebview.getVisibility()!= View.VISIBLE)
            myWebview.setVisibility(View.VISIBLE);
        */
    }

    private void showMap(String adm_cd, double cox, double coy) {
        //tupyogu = tupyogu.replace("제","");
        //tupyogu = tupyogu.replace("투표구","");
        Log.d(TAG, "showMap mTest : " + mTest);
        JSONObject jo = new JSONObject();
        //jo.put("TYPE","GEODATA_TEST");
        jo.put("TYPE","TEST");
        jo.put("ADM_CD", adm_cd);
        jo.put("COX", cox);
        jo.put("COY", coy);
        Log.d(TAG, "showMap jo : " + jo);


        excuteTask(getString(R.string.server_url), jo.toString());
        //excuteTask("http://192.168.25.4:8080/Woori/MobileReq.jsp", jo.toString());
        /*
        myWebview.loadUrl("javascript:drawMap('" + jo.toString() + "')");
        if(myWebview.getVisibility()!= View.VISIBLE)
            myWebview.setVisibility(View.VISIBLE);
            */
    }


    public void tupyoguClickByRightMenu(String sigungu, String hanjungdong, String tupyogu) {
        Log.d(TAG, "tupyoguClickByRightMenu sigungu : " + sigungu + "  hanjungdong : " + hanjungdong + "   tupyogu : " + tupyogu);
        if (isAllLevelClick(sigungu, hanjungdong, tupyogu)) {
            mSigungu = sigungu;
            mHangjungdong = hanjungdong;
            mTupyogu = tupyogu;
            String[] array = {mSigungu, mHangjungdong, mTupyogu};
            final String adm_cd = ElectionManagerApp.getInstance().getTupyoguCode(array);

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showMap(adm_cd);
                }
            }, 1000);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Log.d(TAG, "onCreateView");
        mTest.append("Hahahahahahaha");
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initVoteRateSub(view);
        initVoterRatioOfAge(view);
        initStatisticsOfpopulation(view);
        initStatisticsOfFamily(view);

        myWebview = (WebView) view.findViewById(
                R.id.webView);
        myWebview.setWebViewClient(new MainWebViewClient());
        myWebview.setHorizontalScrollBarEnabled(true);
        myWebview.setVerticalScrollBarEnabled(true);
        myWebview.addJavascriptInterface(new ChromeClient(), "ElectionManager");

        WebSettings webSettings = myWebview.getSettings();
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setUseWideViewPort(false);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowFileAccessFromFileURLs(true);
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        }
        webSettings.setAllowContentAccess(true);
        webSettings.setSaveFormData(true);

        sp1 = (Spinner) view.findViewById(R.id.spinner_1);
        sp2 = (Spinner) view.findViewById(R.id.spinner_2);
        sp3 = (Spinner) view.findViewById(R.id.spinner_3);
        setUpSpinner(sp1, ElectionManagerApp.getInstance().getSelectItemsObject().get("SIGUNGU").toString());
        //String url = "http://10.112.58.94:8080/Woori/areaMap.jsp";
        //myWebview.loadUrl(url);
        //myWebview.setVisibility(View.GONE);
        mPageLoadFinished = false;
        mMapdata = null;
        myWebview.loadUrl(getString(R.string.mapView_url));
        myWebview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

        final Button btn_search = (Button) view.findViewById(R.id.button_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sigungu = (String) sp1.getSelectedItem();
                String haengjoungdong = (String) sp2.getSelectedItem();
                String tupyoguStr = (String) sp3.getSelectedItem();
                String[] array = {sigungu, haengjoungdong, tupyoguStr};
                String adm_cd = ElectionManagerApp.getInstance().getTupyoguCode(array);


                Log.d(TAG, "tupyoguStr = " + tupyoguStr + " ,adm_cd = " + adm_cd);
                showMap(adm_cd);
            }
        });
        PdfViewOnclickListner pdfOnCLick = new PdfViewOnclickListner();

        mVoterate_btn = (ImageButton) view.findViewById(R.id.voterate_btn);
        mVoterate_btn.setOnClickListener(pdfOnCLick);
        mVoteratesub_btnM.setOnClickListener(pdfOnCLick);
        mVoteratesub_btnP.setOnClickListener(pdfOnCLick);

        mPerson = (ImageButton) view.findViewById(R.id.person);
        mPerson.setOnClickListener(pdfOnCLick);
        mButton_age.setOnClickListener(pdfOnCLick);
        mButton_wife.setOnClickListener(pdfOnCLick);
        mButton_univercity.setOnClickListener(pdfOnCLick);
        mButton_population.setOnClickListener(pdfOnCLick);
        mButton_ageing.setOnClickListener(pdfOnCLick);
        mButton_memberOfHouseHold.setOnClickListener(pdfOnCLick);
        mButton_dependency.setOnClickListener(pdfOnCLick);
        mButton_one_man.setOnClickListener(pdfOnCLick);
        mButton_two_more.setOnClickListener(pdfOnCLick);
        mButton_myhouse.setOnClickListener(pdfOnCLick);
        mButton_apt.setOnClickListener(pdfOnCLick);
        mButton_40m_over.setOnClickListener(pdfOnCLick);

        gpsSearchBtn = (Button) view.findViewById(R.id.gpsSearch);
        gpsSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "gpsSearchBtn click");
                gps = new GpsInfo(getActivity());
                // GPS 사용유무 가져오기
                if (gps.isGetLocation()) {
                    if (gps.getLocation() != null) {
                        // 위도
                        double latitude = gps.getLatitude();
                        // 경도
                        double longitude = gps.getLongitude();

                        //latitude = 37.5204504; longitude = 126.9149769;
                        //latitude = 37.4691758; longitude = 126.8978739;
                        GeoPoint in_pt = new GeoPoint(longitude, latitude);
                        GeoPoint out_pt = GeoTrans.convert(GeoTrans.GEO, GeoTrans.UTMK, in_pt);
                        String setText = "위도 : " + String.valueOf(latitude) + " 경도 : " + String.valueOf(longitude);
                        Log.d(TAG, " result : " + setText);
                        String addr = gps.getAddress(latitude, longitude);
                        Log.d(TAG, "addr : " + addr);
                        String convert = "변환X: " + out_pt.getX() + " ,변환Y: " + out_pt.getY();
                        Log.d(TAG, "convert : " + convert);
                        //Toast.makeText(getActivity().getApplicationContext(), "당신의 위치 - \n위도: " + latitude + "\n경도: " + longitude+ "\n변환X: " + out_pt.getX()+ "\n변환Y: " + out_pt.getY(), Toast.LENGTH_SHORT).show();

                        // 구역이동 Spiner Value Setting
                        setAreaFieldValue(addr, out_pt);
                    }
                } else {
                    // GPS 를 사용할수 없으므로
                    gps.showSettingsAlert();
                }

            }
        });
        mAdm_cd = "3105300-00";
        showMap(mAdm_cd);
        return view;
    }

    public void setAreaFieldValue(String addr, GeoPoint point) {
        Log.d(TAG, "setAreaFieldValue addr : " + addr);
        String address[] = addr.split(" ");
        String sigungu = "";
        String doroBubjoung = "";
        String gunmulBunji = "";
        String gunmul = "";
        String sigungutext = "";

        if (address[2] == null || address[2].equals("null")) address[2] = "";

        sigungu += address[1] + address[2];
        sigungutext += address[1] + " " + address[2];
        doroBubjoung += address[3];
        gunmulBunji += address[4];
        Log.d(TAG, "sigungu : " + sigungu + " doroBubjoung : " + doroBubjoung + " gunmulBunji : " + gunmulBunji);
        String url = "http://192.168.0.7:8080/Woori/MobileReq.jsp";
        JSONObject json = new JSONObject();
        //json.put("TYPE", "GPS");
        json.put("TYPE", "GPSTEST");
        json.put("SIGUNGU", sigungu);
        json.put("SIGUNGUTEXT", sigungutext);
        json.put("DOROBUBJOUNG", doroBubjoung);
        json.put("GUNMULBUNJI", gunmulBunji);
        //cox = 941434.0; coy = 1945787.0;
        //cox = 943731.0; coy = 1947957.0; // haengjoungdong
        json.put("COX", point.getX());
        json.put("COY", point.getY());
        excuteTask(getString(R.string.server_url), json.toString());

        //excuteTask(url, json.toString());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {

        final int viewId = parent.getId();
        // TODO Auto-generated method stub
        if (ignoreUpdate) {
            if(viewId != R.id.spinner_3) {
                return;
            }
            ignoreUpdate = false;
        }

        switch (viewId) {
            case R.id.spinner_1:
                String sigungu = (String) parent.getSelectedItem();
                JSONObject jo1 = (JSONObject) ElectionManagerApp.getInstance().getSelectItemsObject().get("HAENGJOUNGDONG");
                setUpSpinner(sp2, jo1.get(sigungu).toString());
                break;
            case R.id.spinner_2:
                String haengjoungdong = (String) parent.getSelectedItem();
                JSONObject jo2 = (JSONObject) ElectionManagerApp.getInstance().getSelectItemsObject().get("TUPYOGU");
                setUpSpinner(sp3, jo2.get(haengjoungdong).toString());
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
        Log.d(TAG, "onNothingSelected");

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /*try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private void setUpSpinner(Spinner spinner, String items) {
        System.out.println("items = " + items);
        Type type = new TypeToken<List<String>>() {
        }.getType();
        Gson converter = new Gson();
        List<String> list = converter.fromJson(items, type);
        ArrayAdapter sp_Adapter = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, list);
        sp_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(sp_Adapter);
        //sp_Adapter.notifyDataSetChanged();
        spinner.setOnItemSelectedListener(SearchFragment.this);
    }

    private void resetSpinnerFromAdmCd(String adm_cd) {
        ignoreUpdate = true;

        String haengCode = adm_cd.split("-")[0];
        String sigunguCode = adm_cd.substring(0,5);

        JSONObject joCode1 = (JSONObject)ElectionManagerApp.getInstance().getSelectItemsCodeObject();
        JSONObject joCode2 = (JSONObject)joCode1.get("HAENGJOUNGDONG");
        JSONObject joCode3 = (JSONObject)joCode1.get("TUPYOGU");

        JSONObject joText1 = (JSONObject)ElectionManagerApp.getInstance().getSelectItemsObject();
        JSONObject joText2 = (JSONObject)joText1.get("HAENGJOUNGDONG");
        JSONObject joText3 = (JSONObject)joText1.get("TUPYOGU");

        int sigunguIndex = ElectionManagerApp.getIndex((JSONArray) joCode1.get("SIGUNGU"), sigunguCode);
        int haengIndex = ElectionManagerApp.getIndex((JSONArray) joCode2.get(sigunguCode), haengCode);
        int tupyoguIndex = ElectionManagerApp.getIndex((JSONArray) joCode3.get(haengCode), adm_cd);

        String sigunguText = (String)((JSONArray)joText1.get("SIGUNGU")).get(sigunguIndex);
        String haengText = (String)((JSONArray)(joText2.get(sigunguText))).get(haengIndex);

        sp1.setSelection(sigunguIndex);
        setUpSpinner(sp2, joText2.get(sigunguText).toString());
        sp2.setSelection(haengIndex);
        setUpSpinner(sp3, joText3.get(haengText).toString());
        sp3.setSelection(tupyoguIndex);
    }


    public void cancel() {
        mTask.cancel(false);
    }

    class SearchTask extends AsyncTask<Object, Integer, String> {
        @Override
        protected void onPreExecute() {
            if (dialog == null) {
                prepareProgressDialog();
            }
            dialog.show();
        }

        @Override
        protected String doInBackground(Object... args) {
            Bundle bundle = (Bundle) args[0];
            String url = bundle.getString("URL");
            String params = bundle.getString("PARAMS");
            Log.d(TAG, "url : " + url + " params : " + params);
            return HttpConnection.PostData(url, params);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPostExecute(String resultData) {
            Log.d(TAG, "onPostExecute resultData : " + resultData);
            try {
                JSONObject re = null;
                JSONParser par = new JSONParser();
                System.out.println("resultData = " + resultData);
                re = (JSONObject) par.parse(resultData);
                String sType = (String) re.get("TYPE");
                String result = (String) re.get("RESULT");
                if(sType.equals("GPS") || sType.equals("GPSTEST")) {
                    if (result.equals("SUCCESS")) {
                        Log.d(TAG, "매칭 데이터 있음");
                        JSONArray alElectDao = new JSONArray();
                        JSONArray alVoteDao = new JSONArray();
                        JSONArray alStatsDAO = new JSONArray();
                        JSONArray alFamilyDAO = new JSONArray();
                        JSONArray alPdfDAO = new JSONArray();
                        alElectDao = (JSONArray) re.get("ELECT");
                        alVoteDao = (JSONArray) re.get("RATE");
                        alStatsDAO = (JSONArray) re.get("STATS");
                        alFamilyDAO = (JSONArray) re.get("FAMILY");
                        alPdfDAO = (JSONArray) re.get("PDF");

                        setVisivilityVoteRateSub(alElectDao.size());
                        setDataVoteRateSub(alElectDao);
                        setVisivilityVoterRatioOfAge(alVoteDao.size());
                        setDataVoterRAtionOfAge(alVoteDao);

                        setVisivilityStatisticsOfpopulation(alStatsDAO.size());
                        setDataStatisticsOfpopulation(alStatsDAO);

                        setVisivilityStaticsticsOfFamily(alFamilyDAO.size());
                        setDataStatisticsOfFamily(alFamilyDAO);
                        setDataOfPdf(alPdfDAO);

                        final JSONObject mapData =  (JSONObject)re.get("MAPDATA");
                        double cox = (Double)mapData.get("COX");
                        double coy = (Double)mapData.get("COY");
                        String adm_cd = (String)mapData.get("ADM_CD");

                        resetSpinnerFromAdmCd(adm_cd);

                        if(mPageLoadFinished) {
                            myWebview.loadUrl("javascript:drawMap('" + mapData.toString() + "')");
                        } else {
                            mMapdata = mapData;
                        }

                    } else if (result.equals("FAILED")) {
                        Log.d(TAG, "매칭 데이터 없음");
                        Toast.makeText(getActivity().getApplicationContext(), "현재 위치에 맞는 정보가 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else if (sType.equals("TEST")) {

                    JSONArray alElectDao = new JSONArray();
                    JSONArray alVoteDao = new JSONArray();
                    JSONArray alStatsDAO = new JSONArray();
                    JSONArray alFamilyDAO = new JSONArray();
                    JSONArray alPdfDAO = new JSONArray();
                    alElectDao = (JSONArray) re.get("ELECT");
                    alVoteDao = (JSONArray) re.get("RATE");
                    alStatsDAO = (JSONArray) re.get("STATS");
                    alFamilyDAO = (JSONArray) re.get("FAMILY");
                    alPdfDAO = (JSONArray) re.get("PDF");

                    setVisivilityVoteRateSub(alElectDao.size());
                    setDataVoteRateSub(alElectDao);

                    setVisivilityVoterRatioOfAge(alVoteDao.size());
                    setDataVoterRAtionOfAge(alVoteDao);

                    setVisivilityStatisticsOfpopulation(alStatsDAO.size());
                    setDataStatisticsOfpopulation(alStatsDAO);

                    setVisivilityStaticsticsOfFamily(alFamilyDAO.size());
                    setDataStatisticsOfFamily(alFamilyDAO);
                    setDataOfPdf(alPdfDAO);

                    if(mAdm_cd != null && mAdm_cd.length() > 1) {
                        resetSpinnerFromAdmCd(mAdm_cd);
                        mAdm_cd = null;
                    }

                    final JSONObject mapData =  (JSONObject)re.get("MAPDATA");

                    if(mPageLoadFinished) {
                        myWebview.loadUrl("javascript:drawMap('" + mapData.toString() + "')");
                    } else {
                        mMapdata = mapData;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                cleanUp();
            }
        }

        @Override
        protected void onCancelled(String result) {
            cleanUp();
        }
    }

    private void excuteTask(String url, String params) {
        Bundle bundle = new Bundle();
        bundle.putString("URL", url);
        bundle.putString("PARAMS", params);
        mTask = new SearchTask();
        mTask.execute(bundle);
    }

    private void prepareProgressDialog() {
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please wait...");
        dialog.setCancelable(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cancel();
            }
        });
        //dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }

    private void cleanUp() {
        if(dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    private int getPosition(Spinner sp, String value) {
        int position = -1;
        if (value != null) {
            position = ((ArrayAdapter) sp.getAdapter()).getPosition(value);
        }
        return position;
    }

}
