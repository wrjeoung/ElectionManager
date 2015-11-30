package com.jsloves.election.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jsloves.election.DTO.BusinessKindDTO;
import com.jsloves.election.DTO.BusinessListDTO;
import com.jsloves.election.DTO.OrganDAO;
import com.jsloves.election.activity.ElectionMainActivity;
import com.jsloves.election.activity.R;
import com.jsloves.election.adapter.BusinessListAdapter;
import com.jsloves.election.application.ElectionManagerApp;
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
 * Created by juhyukkim on 2015. 11. 29..
 */
public class BusinessListFragment extends Fragment implements AdapterView.OnItemSelectedListener  {

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
    private ArrayList<BusinessKindDTO> mBKList = null;
    private ArrayList<String> mBKNameList = null;
    private ArrayList<BusinessListDTO> mBusinessList = null;
    private ListView mListView = null;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText editSearch;
    private Spinner spHidden;
    private Spinner sp1;
    private Spinner sp2;

    private View view = null;

    private OnFragmentInteractionListener mListener;
    private Activity mActivity;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrganIntroFragment.
     */
    // TODO: Rename and change types and number of parameters

    public static BusinessListFragment newInstance(String param1, String param2) {
        System.out.println("param1:"+param1);
        System.out.println("param2:"+param2);
        BusinessListFragment fragment = new BusinessListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static BusinessListFragment newInstance() {
        BusinessListFragment fragment = new BusinessListFragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        ignoreUpdate = false;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initSpinner() {
        setUpSpinner(sp1,mBKNameList.toString());
        setUpSpinner(spHidden, ElectionManagerApp.getInstance().getSelectItemsObject().get("SIGUNGU").toString());
        String sigungu = (String) spHidden.getSelectedItem();
        JSONObject jo1 = (JSONObject)ElectionManagerApp.getInstance().getSelectItemsObject().get("HAENGJOUNGDONG");
        setUpSpinner(sp2, jo1.get(sigungu).toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_business_list, container, false);
        setLayout();

        editSearch = (EditText) view.findViewById(R.id.edit_search);
        spHidden = (Spinner) view.findViewById(R.id.sp_hidden);
        sp1 = (Spinner) view.findViewById(R.id.sp1);
        sp2 = (Spinner) view.findViewById(R.id.sp2);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position,
                                    long arg3) {

                TextView tv_seq = (TextView) view.findViewById(R.id.tv1);
                System.out.println("tv_seq:" + tv_seq.getText());
                String str_seq = tv_seq.getText().toString();

                FragmentManager fragmentManager = getFragmentManager();

                BusinessDetailFragment frament = new BusinessDetailFragment();
                frament.onDestroyView();
                Bundle bundle = new Bundle();
                bundle.putString("business_seq",str_seq);
                frament.setArguments(bundle);

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.business_list, frament);// Activity 레이아웃의 View ID
                fragmentTransaction.commit();

            }
        });

        final Button btn_search = (Button)view.findViewById(R.id.button_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editSearch.getText().toString();
                String kindName = (String)sp1.getSelectedItem();
                String sigungu = (String)spHidden.getSelectedItem();
                String haengjoungdong = (String)sp2.getSelectedItem();
                String tupyoguStr = "전체";
                requestBusinessList(title,getKindCode(kindName),sigungu,haengjoungdong,tupyoguStr);

            }
        });

        requestBKInfo();
        return view;
    }

    private String getKindCode(String kindName) {
        for(int i = 0; i<mBKList.size(); i++) {
            if(kindName.equals(mBKList.get(i).bkName)) {
                return mBKList.get(i).bkCode;
            }
        }
        return "";
    }

    private void requestBKInfo() {
        JSONObject json1 = new JSONObject();
        json1.put("TYPE", "BUSINESSKIND");
        excuteTask(getString(R.string.server_url), json1.toString());
        //excuteTask("http://10.11.1.164:8080/ElectionManager_server/MobileReq.jsp", json1.toString());
    }

    private void requestBusinessList(String title,String kindCode, String sigungu, String haengjoungdong, String tupyoguStr) {
        JSONObject json1 = new JSONObject();
        json1.put("TYPE", "BUSINESSLIST");
        json1.put("TITLE", title);
        json1.put("KIND", kindCode);

        String[] array = {sigungu, haengjoungdong, tupyoguStr};
        String adm_cd = ElectionManagerApp.getInstance().getTupyoguCode(array);
        json1.put("ADM_CD", adm_cd);
        excuteTask(getString(R.string.server_url), json1.toString());
        //excuteTask("http://10.11.1.164:8080/ElectionManager_server/MobileReq.jsp", json1.toString());
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
        mListener = null;
        mActivity = null;
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
        Type type = new TypeToken<List<String>>(){}.getType();
        Gson converter = new Gson();
        List<String> list =  converter.fromJson(items.toString(), type);
        ArrayAdapter sp_Adapter = new ArrayAdapter(getActivity().getApplicationContext(),R.layout.spinner_item,list);
        sp_Adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(sp_Adapter);
        spinner.setOnItemSelectedListener(BusinessListFragment.this);
    }

    private void setLayout(){
        mListView = (ListView) view.findViewById(R.id.lv_list);
    }

    public void cancel() {
        maTask.cancel(false);
    }

    class MyAsyncTask extends AsyncTask<Object, Integer, String> {

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
            String result = "";
            JSONObject re = null;
            JSONParser par = new JSONParser();
            Gson converter = new Gson();

            try{
                re = (JSONObject) par.parse(resultData.toString());
                String sType = (String) re.get("TYPE");
                Log.d("kjh","sType:"+sType);

                if(sType.equals("BUSINESSKIND")) {
                    mBKList = new ArrayList<BusinessKindDTO>();
                    mBKNameList = new ArrayList<String>();

                    Gson gson = new Gson();
                    JSONArray bkList = (JSONArray) re.get("BKINFO");

                    for(int i = 0; i<bkList.size();i++) {
                        BusinessKindDTO dto = gson.fromJson((String) bkList.get(i), BusinessKindDTO.class);
                        mBKList.add(dto);
                        mBKNameList.add(dto.bkName);
                    }
                    initSpinner();
                }else if(sType.equals("BUSINESSLIST")) {
                    mBusinessList = new ArrayList<BusinessListDTO>();
                    JSONArray businessList = (JSONArray) re.get("BUSINESSLIST");
                    Gson gson = new Gson();

                    for(int i = 0; i<businessList.size();i++) {
                        BusinessListDTO dto = gson.fromJson((String) businessList.get(i), BusinessListDTO.class);
                        mBusinessList.add(dto);
                    }
                    mListView.setAdapter(new BusinessListAdapter(getActivity().getApplicationContext(), mBusinessList));
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
            cleanUp();
        }

    }

    private void excuteTask(String url,String params) {
        Bundle bundle = new Bundle();
        bundle.putString("URL",url);
        bundle.putString("PARAMS", params);
        maTask = new MyAsyncTask();
        maTask.execute(bundle);
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
        Log.d("lcy", "cleanUp1");
        dialog.dismiss();
        dialog = null;

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
            case R.id.sp_hidden:
                String sigungu = (String)parent.getSelectedItem();
                String title = ((ElectionMainActivity) mActivity).getActionBarTitle();
                if(!sigungu.equals(title)) {
                    JSONArray jArray = (JSONArray)ElectionManagerApp.getInstance().getSelectItemsObject().get("SIGUNGU");
                    int index = ElectionManagerApp.getIndex(jArray,title);
                    spHidden.setSelection(index,true);
                    break;
                }
                JSONObject jo1 = (JSONObject)ElectionManagerApp.getInstance().getSelectItemsObject().get("HAENGJOUNGDONG");
                setUpSpinner(sp2, jo1.get(sigungu).toString());
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
