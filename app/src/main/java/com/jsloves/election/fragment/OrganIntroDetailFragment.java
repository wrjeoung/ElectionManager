package com.jsloves.election.fragment;

import android.app.Activity;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.jsloves.election.activity.ElectionMainActivity;
import com.jsloves.election.activity.R;
import com.jsloves.election.common.OnBackPressedListener;
import com.jsloves.election.policy.AppPolicy;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrganIntroDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrganIntroDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrganIntroDetailFragment extends Fragment implements OnBackPressedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String TAG = OrganIntroDetailFragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view = null;
    private WebView myWebview;

    private OnFragmentInteractionListener mListener;

    private String mOrgan_tap;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrganIntroDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrganIntroDetailFragment newInstance(String param1, String param2) {
        OrganIntroDetailFragment fragment = new OrganIntroDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public OrganIntroDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("lcy", "onCreate OrganIntroDetailFragment");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d("lcy", "onCreateView OrganIntroDetailFragment");

        ElectionMainActivity ema;
        ema = (ElectionMainActivity)getActivity();
        if(ema != null) {
            ema.setBackKeyListnerbyFragment(this);
        }

        mOrgan_tap = getArguments().getString("organ_tap");
        String organ_seq = getArguments().getString("organ_seq");
        String organ_gb = getArguments().getString("organ_gb");
        Log.d("lcy","mOrgan_tap:"+mOrgan_tap + ", organ_seq:" + organ_seq + ",organ_gb:"+ organ_gb);

        view = inflater.inflate(R.layout.fragment_organ_intro_detail, container, false);

        myWebview = (WebView) view.findViewById(
                R.id.organ_webView);
        myWebview.setWebViewClient(new MainWebViewClient());
        myWebview.setHorizontalScrollBarEnabled(true);
        myWebview.setVerticalScrollBarEnabled(true);
        WebSettings webSettings = myWebview.getSettings();
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
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

        myWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("lcy", consoleMessage.message() + '\n' + consoleMessage.messageLevel() + '\n' + consoleMessage.sourceId());
                return super.onConsoleMessage(consoleMessage);
            }
        });

        //myWebview.loadUrl("http://222.122.149.161:7070/ElectionManager_server/OrganIntroDetail.jsp?organ_seq="+organ_seq);
        //myWebview.loadUrl("http://192.168.42.189:8080/ElectionManager_server/OrganIntroDetail.jsp?organ_seq="+organ_seq);
        Log.d("lcy", "call WebView before");
        myWebview.loadUrl(AppPolicy.URI_SERVER+"/OrganIntroDetail.jsp?organ_seq=" + organ_seq + "&organ_gb=" + organ_gb);
        //myWebview.loadUrl("http://192.168.42.189:8080/ElectionManager_server/OrganIntroDetail.jsp?organ_seq=" + organ_seq + "&organ_gb="+organ_gb);
        Log.d("lcy", "call WebView after");

        return view;
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
        Log.d("lcy", "onAttach OrganIntroDetailFragment");
        /**
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }**/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onBackPressed() {
        if(mOrgan_tap.equals("organ1")) {

            OrganIntroFragment frament = new OrganIntroFragment();
            frament.onDestroyView();
            Bundle bundle = new Bundle();
            frament.setArguments(bundle);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.organ_detail, frament);// Activity 레이아웃의 View ID
            fragmentTransaction.commit();
        }else if(mOrgan_tap.equals("organ2")){
            OrganIntroFragment2 frament = new OrganIntroFragment2();
            frament.onDestroyView();
            Bundle bundle = new Bundle();
            frament.setArguments(bundle);

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.organ_detail, frament);// Activity 레이아웃의 View ID
            fragmentTransaction.commit();
        }
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

    private class MainWebViewClient extends WebViewClient {
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
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

}
