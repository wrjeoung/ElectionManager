package com.jsloves.election.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.nfc.Tag;
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
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jsloves.election.activity.R;
import com.jsloves.election.application.ElectionManagerApp;
import com.jsloves.election.layout.CustomBaseAdapter;
import com.jsloves.election.layout.DataClass;
import com.jsloves.election.util.HttpConnection;
import android.widget.AdapterView.OnItemSelectedListener;
import com.jsloves.election.DTO.OrganDAO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.nio.BufferUnderflowException;
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
public class OrganIntroFragment extends Fragment implements OnItemSelectedListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public static final String TAG = OrganIntroFragment.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String organ_gb = "A"; //기관

    private static JSONArray array1 = null;
    private static JSONArray array2 = null;

    private ProgressDialog dialog;
    private MyAsyncTask maTask;

    private int iCnt = 0;

    private boolean ignoreUpdate;

    private static boolean iFlag = false;

    private WeakReference<MyAsyncTask> asyncTaskWeakRef;
    public static final String ASYNC = "async";

    private ArrayList<DataClass> mCareList = null;
    private ListView mListView = null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Spinner sp4;
    private Spinner sp5;
    private Spinner sp6;

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

    public static OrganIntroFragment newInstance(String param1, String param2) {
        System.out.println("param1:"+param1);
        System.out.println("param2:"+param2);
        OrganIntroFragment fragment = new OrganIntroFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public OrganIntroFragment(){
        Log.d("lcy", "OrganIntroFragment");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("lcy", "onCreate");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        ignoreUpdate = false;
        //JSONObject json1 = new JSONObject();
        //json1.put("TYPE", "SELECTORGAN1");

        //excuteTask("http://192.168.0.2:8080/ElectionManager_server/MobileReq.jsp", json1.toString());
        //excuteTask(getString(R.string.server_url), json1.toString());
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

        view = inflater.inflate(R.layout.fragment_organ_intro, container, false);
        setLayout();

        sp4 = (Spinner) view.findViewById(R.id.spinner_4);
        sp5 = (Spinner) view.findViewById(R.id.spinner_5);
        sp6 = (Spinner) view.findViewById(R.id.spinner_6);

        Log.d("lcy", ElectionManagerApp.getInstance().getSelectItemsObject().get("SIGUNGU").toString());

        setUpSpinner(sp4, ElectionManagerApp.getInstance().getSelectItemsObject().get("SIGUNGU").toString());

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


                TextView tv_seq = null;

                tv_seq = (TextView) view.findViewById(R.id.tv_seq);
                System.out.println("tv_seq:"+tv_seq.getText());
                String  str_seq = tv_seq.getText().toString();

                FragmentManager fragmentManager = getFragmentManager();

                OrganIntroDetailFragment frament = new OrganIntroDetailFragment();
                frament.onDestroyView();
                Bundle bundle = new Bundle();
                bundle.putString("organ_tap","organ1");
                bundle.putString("organ_seq",str_seq);
                bundle.putString("organ_gb",organ_gb);
                frament.setArguments(bundle);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.organ_intro1, frament);// Activity 레이아웃의 View ID
                fragmentTransaction.commit();

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
                json1.put("ORGAN_GB", organ_gb);

                String sigungu = (String) sp4.getSelectedItem();
                String haengjoungdong = (String) sp5.getSelectedItem();
                String tupyoguStr = (String) sp6.getSelectedItem();
                String[] array = {sigungu,haengjoungdong,tupyoguStr};
                String adm_cd = ElectionManagerApp.getInstance().getTupyoguCode(array);
                json1.put("ADM_CD", adm_cd);
                Log.d("lcy",adm_cd);

                //excuteTask("http://192.168.42.189:8080/ElectionManager_server/MobileReq.jsp", json1.toString());
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
        Log.d("lcy", "items = " + items);
        Type type = new TypeToken<List<String>>(){}.getType();
        Gson converter = new Gson();
        List<String> list =  converter.fromJson(items.toString(), type);
        ArrayAdapter sp_Adapter = new ArrayAdapter(getActivity().getApplicationContext(),android.R.layout.simple_spinner_item,list);
        sp_Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(sp_Adapter);
        spinner.setOnItemSelectedListener(OrganIntroFragment.this);
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

                if(sType.equals("SELECTORGAN1")) {
                    Log.d("lcy", "SELECTORGAN1");

                    ignoreUpdate = true;

                }else {
                    if (sType.equals("SEARCHORGAN")) {
                        Log.d("lcy", "SEARCHORGAN");

                        mCareList = new ArrayList<DataClass>();
                        String adm_cd = (String) re.get("ADM_CD");
                        JSONArray organlist = (JSONArray) re.get("ORGANLIST");
                        System.out.println("organlist:" + organlist.size());

                        Gson gson = new Gson();

                        OrganDAO obj = new OrganDAO();

                        for (int i = 0; i < organlist.size(); i++) {
                            obj = gson.fromJson((String) organlist.get(i), OrganDAO.class);
                            Log.d("lcy","getOrgan_Seq():" + obj.getOrgan_Seq() + ", getHaengtext():"+obj.getHaengtext() + ", getOrgan_Name(),:"+obj.getOrgan_Name());
                            mCareList.add(new DataClass(obj.getOrgan_Seq(), obj.getHaengtext(), obj.getOrgan_Name()));
                        }

                        //mCareList.add(new DataClass(adm_cd, organ_name));
                        mListView.setAdapter(new CustomBaseAdapter(getActivity().getApplicationContext(), mCareList));
                    }
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
        Log.d("lcy", "cleanUp1");
        dialog.dismiss();
        dialog = null;

    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {

        Log.d("lcy", "onItemSelected:"+ignoreUpdate);
        // TODO Auto-generated method stub
        if(ignoreUpdate) {
            ignoreUpdate = false;
            return;
        }

        switch (parent.getId()) {
            case R.id.spinner_4:
                String sigungu = (String)parent.getSelectedItem();
                JSONObject jo1 = (JSONObject)ElectionManagerApp.getInstance().getSelectItemsObject().get("HAENGJOUNGDONG");
                setUpSpinner(sp5, jo1.get(sigungu).toString());
                break;
            case R.id.spinner_5:
                String haengjoungdong = (String) parent.getSelectedItem();
                JSONObject jo2 = (JSONObject) ElectionManagerApp.getInstance().getSelectItemsObject().get("TUPYOGU");
                setUpSpinner(sp6, jo2.get(haengjoungdong).toString());
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
