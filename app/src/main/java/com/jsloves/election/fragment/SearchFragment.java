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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jsloves.election.activity.PDFViewActivity;
import com.jsloves.election.activity.R;
import com.jsloves.election.application.ElectionManagerApp;
import com.jsloves.election.util.GeoPoint;
import com.jsloves.election.util.GeoTrans;
import com.jsloves.election.util.GpsInfo;
import com.jsloves.election.util.HttpConnection;

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
    private ImageButton person;

    private StringBuffer mTest = new StringBuffer();

    private String mSigungu;
    private String mHangjungdong;
    private String mTupyogu;
    private Handler mHandler = new Handler();


	// GPS
    private Button gpsSearchBtn;
    private GpsInfo gps;

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
            //CookieSyncManager.getInstance().sync();

        }

    }

    private boolean isAllLevelClick(String sigungu, String hangjungdong, String tupyogu) {

        if(sigungu != null
                && hangjungdong != null
                && tupyogu != null) {
            return true;
        } else {
            return false;
        }
    }

    private void showMap(String sigungu, String hangjungdong, String tupyogu) {
        //tupyogu = tupyogu.replace("제","");
        //tupyogu = tupyogu.replace("투표구","");
        Log.d(TAG,"showMap mTest : "+mTest);
        JSONObject jo = new JSONObject();
        //jo.put("TYPE","GEODATA_TEST");
        jo.put("TYPE","TEST");
        jo.put("SIGUNGUTEXT", sigungu);
        jo.put("HAENGTEXT", hangjungdong);
        jo.put("TUPYOGU_NUM", tupyogu);
        jo.put("COX", 0.0);
        jo.put("COY", 0.0);
        Log.d(TAG, "showMap jo : " + jo);
        myWebview.loadUrl("javascript:drawMap('" + jo.toString() + "')");
        if(myWebview.getVisibility()!= View.VISIBLE)
            myWebview.setVisibility(View.VISIBLE);
    }

    private void showMap(String sigungu, String hangjungdong, String tupyogu, double cox, double coy) {
        //tupyogu = tupyogu.replace("제","");
        //tupyogu = tupyogu.replace("투표구","");
        Log.d(TAG,"showMap mTest : "+mTest);
        JSONObject jo = new JSONObject();
        //jo.put("TYPE","GEODATA_TEST");
        jo.put("TYPE","TEST");
        jo.put("SIGUNGUTEXT", sigungu);
        jo.put("HAENGTEXT", hangjungdong);
        jo.put("TUPYOGU_NUM", tupyogu);
        jo.put("COX", cox);
        jo.put("COY", coy);
        Log.d(TAG, "showMap jo : " + jo);
        myWebview.loadUrl("javascript:drawMap('" + jo.toString() + "')");
        if(myWebview.getVisibility()!= View.VISIBLE)
            myWebview.setVisibility(View.VISIBLE);
    }


    public void tupyoguClickByRightMenu(String sigungu, String hanjungdong, String tupyogu) {
        Log.d(TAG, "tupyoguClickByRightMenu sigungu : " + sigungu + "  hanjungdong : " + hanjungdong + "   tupyogu : " + tupyogu);
        if(isAllLevelClick(sigungu, hanjungdong, tupyogu)) {
            mSigungu = sigungu;
            mHangjungdong = hanjungdong;
            mTupyogu = tupyogu;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showMap(mSigungu, mHangjungdong, mTupyogu);
                }
            },1000);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        Log.d(TAG, "onCreateView");
        mTest.append("Hahahahahahaha");
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
        String url = "http://192.168.0.6:8080/Woori/areaMap.jsp";
        myWebview.loadUrl(getString(R.string.mapView_url));
        //myWebview.loadUrl(url);
        myWebview.setVisibility(View.GONE);
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

        final Button btn_search = (Button)view.findViewById(R.id.button_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sigungu = (String) sp1.getSelectedItem();
                String haengjoungdong = (String) sp2.getSelectedItem();
                String tupyoguStr = (String) sp3.getSelectedItem();

                showMap(sigungu, haengjoungdong, tupyoguStr);
            }
        });
        person = (ImageButton)view.findViewById(R.id.person);
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),PDFViewActivity.class);
                startActivity(intent);
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
                    if(gps.getLocation() != null){
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
                        String convert = "변환X: " + out_pt.getX()+ " ,변환Y: " + out_pt.getY();
                        Log.d(TAG, "convert : " + convert);
                        Toast.makeText(getActivity().getApplicationContext(), "당신의 위치 - \n위도: " + latitude + "\n경도: " + longitude+ "\n변환X: " + out_pt.getX()+ "\n변환Y: " + out_pt.getY(), Toast.LENGTH_SHORT).show();

                        // 구역이동 Spiner Value Setting
                        setAreaFieldValue(addr,out_pt);
                    }
                }else{
                    // GPS 를 사용할수 없으므로
                    gps.showSettingsAlert();
                }

            }
        });
		
        return view;
    }

    public void setAreaFieldValue(String addr,GeoPoint point){
        Log.d(TAG, "setAreaFieldValue addr : " + addr);
        String address[] = addr.split(" ");
        String sigungu      = "";
        String doroBubjoung = "";
        String gunmulBunji        = "";
        String gunmul       = "";
        String sigungutext  = "";

        if(address[2] == null || address[2].equals("null")) address[2] = "";

        sigungu      += address[1] + address[2];
        sigungutext  += address[1] + " " + address[2];
        doroBubjoung += address[3];
        gunmulBunji  += address[4];
        Log.d(TAG, "sigungu : " + sigungu + " doroBubjoung : " + doroBubjoung + " gunmulBunji : " + gunmulBunji);
        String url = "http://192.168.0.7:8080/Woori/MobileReq.jsp";
        JSONObject json = new JSONObject();
        //json.put("TYPE", "GPS");
        json.put("TYPE", "GPSTEST");
        json.put("SIGUNGU", sigungu);
        json.put("SIGUNGUTEXT", sigungutext);
        json.put("DOROBUBJOUNG", doroBubjoung);
        json.put("GUNMULBUNJI", gunmulBunji);
        json.put("COX", point.getX());
        json.put("COY", point.getY());
        excuteTask(getString(R.string.server_url), json.toString());
        //excuteTask(url, json.toString());
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {
        // TODO Auto-generated method stub
        if(ignoreUpdate) {
            ignoreUpdate = false;
            return;
        }

        switch (parent.getId()) {
            case R.id.spinner_1:
                String sigungu = (String)parent.getSelectedItem();
                JSONObject jo1 = (JSONObject)ElectionManagerApp.getInstance().getSelectItemsObject().get("HAENGJOUNGDONG");
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
        //sp_Adapter.notifyDataSetChanged();
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
            Log.d(TAG, "url : " + url + " params : " + params);
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
                String sType = (String) re.get("TYPE");
                String result = (String) re.get("RESULT");
                if(sType.equals("GPS") || sType.equals("GPSTEST")) {
                    /*if (result.equals("SUCCESS")) {
                        Log.d(TAG, "GPS SUCCESS");


                        ElectionManagerApp.getInstance().setSelectItems(((JSONObject) re.get("SELECTITEMS2")).toString());

                        Log.d(TAG, ElectionManagerApp.getInstance().getSelectItemsObject().get("SIGUNGU").toString());
                        Log.d(TAG, ElectionManagerApp.getInstance().getSelectItemsObject().get("HAENGJOUNGDONG").toString());
                        Log.d(TAG, ElectionManagerApp.getInstance().getSelectItemsObject().get("TUPYOGU").toString());

                        if(ElectionManagerApp.getInstance().getSelectItemsObject().get("SIGUNGU").toString().length() == 2
                                || ElectionManagerApp.getInstance().getSelectItemsObject().get("HAENGJOUNGDONG").toString().length() == 2
                                || ElectionManagerApp.getInstance().getSelectItemsObject().get("TUPYOGU").toString().length() == 2){
                            Log.d(TAG,"매칭 데이터 없음");
                            Toast.makeText(getActivity().getApplicationContext(),"현재 위치에 맞는 정보가 없습니다.",Toast.LENGTH_SHORT).show();
                        }else{
                            Log.d(TAG,"매칭 데이터 있음");
                            setUpSpinner(sp1, ElectionManagerApp.getInstance().getSelectItemsObject().get("SIGUNGU").toString());
                        }
                    }*/
                    if (result.equals("SUCCESS")) {
                        Log.d(TAG, "매칭 데이터 있음");
                        JSONObject resData = (JSONObject)re.get("RESDATA");
                        String sigungu = (String)resData.get("SIGUNGU");
                        String haengjoungdong = (String)resData.get("HAENGJOUNGDONG");
                        String tupyogu = (String)resData.get("TUPYOGU");
                        double cox = (Double)resData.get("COX");
                        double coy = (Double)resData.get("COY");

                        ignoreUpdate = true;
                        sp1.setSelection(getPosition(sp1, sigungu));
                        JSONObject jo1 = (JSONObject)ElectionManagerApp.getInstance().getSelectItemsObject().get("HAENGJOUNGDONG");
                        setUpSpinner(sp2, jo1.get(sigungu).toString());
                        sp2.setSelection(getPosition(sp2, haengjoungdong));
                        JSONObject jo2 = (JSONObject)ElectionManagerApp.getInstance().getSelectItemsObject().get("TUPYOGU");
                        setUpSpinner(sp3, jo2.get(haengjoungdong).toString());
                        sp3.setSelection(getPosition(sp3, tupyogu));
                        //showMap(sigungu,haengjoungdong,tupyogu);
                        showMap(sigungu,haengjoungdong,tupyogu,cox,coy);

                    } else if(result.equals("FAILED")) {
                        Log.d(TAG,"매칭 데이터 없음");
                        Toast.makeText(getActivity().getApplicationContext(),"현재 위치에 맞는 정보가 없습니다.",Toast.LENGTH_SHORT).show();
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

    private void excuteTask(String url,String params) {
        Bundle bundle = new Bundle();
        bundle.putString("URL",url);
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
        dialog.dismiss();
        dialog = null;
    }

    private int getPosition(Spinner sp, String value) {
        int position = -1;
        if (value != null) {
            position = ((ArrayAdapter)sp.getAdapter()).getPosition(value);
        }
        return position;
    }
}
