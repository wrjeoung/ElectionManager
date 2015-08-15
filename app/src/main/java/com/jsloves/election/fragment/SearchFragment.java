package com.jsloves.election.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jsloves.election.activity.R;
import com.jsloves.election.application.ElectionManagerApp;
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
    private JSONArray array1 = new JSONArray();
    private JSONArray array2 = new JSONArray();
    private JSONArray array3 = new JSONArray();

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
	
	// GPS
    private Button gpsSearchBtn;
    private GpsInfo gps;
    private TextView resultGpsText;

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

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate");
        setRetainInstance(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Log.d(TAG," mParam1 : " + mParam1 + " mParam2 : " + mParam2);
        mTask = new SearchTask();
        JSONObject json1 = new JSONObject();
        json1.put("TYPE", "SELECTITEMS");
        json1.put("TARGET", "SIGUNGU");
        //excuteTask(getString(R.string.server_url), json1.toString());

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
            CookieSyncManager.getInstance().sync();

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG,"onCreateView");
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        myWebview = (WebView) view.findViewById(
                R.id.webView);
        myWebview.setWebViewClient(new MainWebViewClient());
        myWebview.setHorizontalScrollBarEnabled(true);
        myWebview.setVerticalScrollBarEnabled(true);
        WebSettings webSettings = myWebview.getSettings();
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setUseWideViewPort(false);
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowFileAccessFromFileURLs(true);
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        }
        webSettings.setAllowContentAccess(true);
        webSettings.setSaveFormData(true);

        sp1 = (Spinner) view.findViewById(R.id.spinner_1);
        sp2 = (Spinner) view.findViewById(R.id.spinner_2);
        sp3 = (Spinner) view.findViewById(R.id.spinner_3);

        setUpSpinner(sp1, ElectionManagerApp.getInstance().getSelectItemsObject().get("SIGUNGU").toString());
        myWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                //Log.d("kjh", consoleMessage.message() + '\n' + consoleMessage.messageLevel() + '\n' + consoleMessage.sourceId());
                return super.onConsoleMessage(consoleMessage);
            }
        });
        myWebview.loadUrl(getString(R.string.mapView_url));
        myWebview.setVisibility(View.GONE);

        final Button btn_search = (Button)view.findViewById(R.id.button_search);
        btn_search.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String sigungu = (String)sp1.getSelectedItem();
                String haengjoungdong = (String)sp2.getSelectedItem();
                String tupyoguStr = (String)sp3.getSelectedItem();
                tupyoguStr = tupyoguStr.replace("제","");
                tupyoguStr = tupyoguStr.replace("투표구","");

                JSONObject jo = new JSONObject();
                jo.put("TYPE","GEODATA");
                jo.put("SIGUNGUTEXT",sigungu);
                jo.put("HAENGTEXT",haengjoungdong);
                jo.put("TUPYOGU_NUM", Integer.parseInt(tupyoguStr));
                myWebview.loadUrl("javascript:drawMap('"+jo.toString()+"')");
                if(myWebview.getVisibility()!= View.VISIBLE)
                    myWebview.setVisibility(View.VISIBLE);
            }
        });
		
		gpsSearchBtn = (Button) view.findViewById(R.id.gpsSearch);
        gpsSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "gpsSearchBtn click");
                gps = new GpsInfo(getActivity());
                // GPS 사용유무 가져오기
                if(gps.isGetLocation()){
                    // 위도
                    double latitude  = gps.getLatitude();
                    // 경도
                    double longitude = gps.getLongitude();

                    String setText = "위도 : " + String.valueOf(latitude) + " 경도 : " + String.valueOf(longitude);
                    Log.d(TAG," result : " + setText);
                    String addr = gps.getAddress(latitude,longitude);
                    Log.d(TAG, "addr : " + addr);
                    Toast.makeText(getActivity().getApplicationContext(),"당신의 위치 - \n위도: " + latitude + "\n경도: " + longitude,Toast.LENGTH_SHORT).show();
                }else{
                    // GPS 를 사용할수 없으므로
                    gps.showSettingsAlert();
                }

            }
        });
		
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // TODO Auto-generated method stub

        switch (parent.getId()) {
            case R.id.spinner_1:
                String sigungu = (String)parent.getSelectedItem();
                JSONObject jo1 = (JSONObject)ElectionManagerApp.getInstance().getSelectItemsObject().get("HAENGJOUNGDONG");
                setUpSpinner(sp2, jo1.get(sigungu).toString());
                break;
            case R.id.spinner_2:
                String haengjoungdong = (String)parent.getSelectedItem();
                JSONObject jo2 = (JSONObject)ElectionManagerApp.getInstance().getSelectItemsObject().get("TUPYOGU");
                setUpSpinner(sp3, jo2.get(haengjoungdong).toString());
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
        Log.d(TAG,"onNothingSelected");

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
    private void setUpSpinner(Spinner spinner,String items) {
        System.out.println("items = "+items);
        Type type = new TypeToken<List<String>>(){}.getType();
        Gson converter = new Gson();
        List<String> list =  converter.fromJson(items, type);
        ArrayAdapter sp_Adapter = new ArrayAdapter(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item,list);
        sp_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(sp_Adapter);
        sp_Adapter.notifyDataSetChanged();
        spinner.setOnItemSelectedListener(SearchFragment.this);
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
            Bundle bundle = (Bundle)args[0];
            String url = bundle.getString("URL");
            String params = bundle.getString("PARAMS");
            return HttpConnection.PostData(url, params);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }

        @Override
        protected void onPostExecute(String resultData) {
            Log.d(TAG,"onPostExecute resultData : " + resultData);
            try {
                JSONObject re = null;
                JSONParser par = new JSONParser();
                System.out.println("resultData = "+resultData);
                re = (JSONObject) par.parse(resultData);
                String result = (String) re.get("RESULT");

                if (result.equals("SUCCESS")) {
                    JSONArray sigungu = (JSONArray) re.get("SIGUNGU");
                    sigungu.add("부천시 소사구");
                    sigungu.add("부천시 범박구");
                    setUpSpinner((Spinner) getView().findViewById(R.id.spinner_1), sigungu.toString());
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

    private void excuteTask(String url,String params) {
        Bundle bundle = new Bundle();
        bundle.putString("URL",url);
        bundle.putString("PARAMS", params);
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
        dialog.dismiss();
        dialog = null;
    }
}
