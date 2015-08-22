package com.jsloves.election.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jsloves.election.activity.R;
import com.jsloves.election.layout.CustomBaseAdapter;
import com.jsloves.election.layout.DataClass;
import com.jsloves.election.util.HttpConnection;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrganIntroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrganIntroFragment2 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static JSONArray array1 = null;
    private static JSONArray array2 = null;

    private ProgressDialog dialog;
    private MyAsyncTask maTask;

    private int iCnt = 0;

    private static boolean iFlag = false;

    private WeakReference<MyAsyncTask> asyncTaskWeakRef;
    public static final String ASYNC = "async";

    private ArrayList<DataClass> mCareList = null;
    private ListView mListView = null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view = null;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrganIntroFragment.
     */
    // TODO: Rename and change types and number of parameters

    public static OrganIntroFragment2 newInstance(String param1, String param2) {
        System.out.println("param1:"+param1);
        System.out.println("param2:"+param2);
        OrganIntroFragment2 fragment = new OrganIntroFragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public OrganIntroFragment2(){
        Log.d("lcy", "OrganIntroFragment2");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("lcy", "onCreate");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        JSONObject json1 = new JSONObject();
        json1.put("TYPE", "SELECTORGAN2");

        //excuteTask("http://192.168.0.31:8080/Woori/MobileReq.jsp", json1.toString());
        excuteTask(getString(R.string.server_url), json1.toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("lcy", "onResume");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("lcy", "onCreateView");

        view = inflater.inflate(R.layout.fragment_organ_intro2, container, false);
        setLayout();

        if(iFlag==false){
            this.iFlag = true;
        }else {

            Gson converter = new Gson();

            try {
                //검색 스피너
                setUpSpinner((Spinner) view.findViewById(R.id.spinner_4), array1.toString());
                setUpSpinner((Spinner) view.findViewById(R.id.spinner_5), array2.toString());

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //cleanUp();
            }
        }

        // ArrayAdapter 연결
//        mListView.setAdapter(new CustomArrayAdapter(this, R.layout.intro_list, mCareList));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long arg3) {

                Toast.makeText(
                        getActivity().getApplicationContext(),
                        "ITEM CLICK = " + position,
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        final Button btn_search = (Button)view.findViewById(R.id.button_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(
                        getActivity().getApplicationContext(),
                        "검색 버튼 클릭",
                        Toast.LENGTH_SHORT
                ).show();

                JSONObject json1 = new JSONObject();
                json1.put("TYPE", "SEARCHORGAN");
                //excuteTask("http://192.168.0.31:8080/Woori/MobileReq.jsp", json1.toString());
                excuteTask(getString(R.string.server_url), json1.toString());

            }
        });

        // Inflate the layout for this fragment
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
        Log.d("lcy", "onAttach");
        /**try {
         mListener = (OnFragmentInteractionListener) activity;
         } catch (ClassCastException e) {
         throw new ClassCastException(activity.toString()
         + " must implement OnFragmentInteractionListener");
         }**/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("lcy", "onDetach");
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
        Log.d("lcy", "setUpSpinner");
        Type type = new TypeToken<List<String>>(){}.getType();
        Gson converter = new Gson();
        List<String> list =  converter.fromJson(items.toString(), type);
        ArrayAdapter sp_Adapter = new ArrayAdapter(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item,list);
        sp_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(sp_Adapter);
    }

    private void setLayout(){
        mListView = (ListView) view.findViewById(R.id.lv_list);
    }

    public void cancel() {
        Log.d("lcy", "cancel");
        maTask.cancel(false);
    }

    class MyAsyncTask extends AsyncTask<Object, Integer, String> {

        @Override
        protected void onPreExecute() {
            Log.d("lcy", "onPreExecute");
            if (dialog == null) {
                prepareProgressDialog();
            }
            dialog.show();
        }

        @Override
        protected String doInBackground(Object... args) {
            Log.d("lcy", "doInBackground");
            Bundle bundle = (Bundle)args[0];
            String url = bundle.getString("URL");
            String params = bundle.getString("PARAMS");
            return HttpConnection.PostData(url, params);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.d("lcy", "onProgressUpdate");
        }

        @Override
        protected void onPostExecute(String resultData) {
            Log.d("lcy", "onPostExecute:" + resultData);

            String result = "";
            JSONObject re = null;
            JSONParser par = new JSONParser();
            Gson converter = new Gson();

            try{
                re = (JSONObject) par.parse(resultData.toString());
                String sType = (String) re.get("TYPE");
                Log.d("lcy","sType:"+sType);

                if(sType.equals("SELECTORGAN2")) {
                    Log.d("lcy", "SELECTORGAN2");

                    array1 = new JSONArray();
                    array2 = new JSONArray();

                    array1 = (JSONArray) re.get("array1");
                    array2 = (JSONArray) re.get("array2");

                    setUpSpinner((Spinner) view.findViewById(R.id.spinner_4), array1.toString());
                    setUpSpinner((Spinner) view.findViewById(R.id.spinner_5), array2.toString());

                    result = (String) re.get("RESULT");

                }else if(sType.equals("SEARCHORGAN")){
                    Log.d("lcy", "SEARCHORGAN");
                    mCareList = new ArrayList<DataClass>();
                    mCareList.add(new DataClass("대구", "대구교도소"));
                    mCareList.add(new DataClass("부산", "부산교도소"));
                    mListView.setAdapter(new CustomBaseAdapter(getActivity().getApplicationContext(), mCareList));

                }

            }catch (ParseException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            } finally{
                if(dialog!=null) cleanUp();
            }

        }

        @Override
        protected void onCancelled(String result) {
            Log.d("lcy", "onCancelled");
            cleanUp();
        }

    }

    private void excuteTask(String url,String params) {
        Log.d("lcy", "excuteTask");
        Bundle bundle = new Bundle();
        bundle.putString("URL",url);
        bundle.putString("PARAMS", params);
        maTask = new MyAsyncTask();
        maTask.execute(bundle);
    }

    private void prepareProgressDialog() {
        Log.d("lcy", "prepareProgressDialog");
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
        Log.d("lcy", "cleanUp2");
        dialog.dismiss();
        dialog = null;

    }
}
