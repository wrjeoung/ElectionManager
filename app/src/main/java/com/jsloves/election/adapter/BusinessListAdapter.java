package com.jsloves.election.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.jsloves.election.DTO.BusinessListDTO;
import com.jsloves.election.activity.R;

import java.util.ArrayList;

/**
 * Created by jhkim01 on 2015-11-30.
 */
public class BusinessListAdapter extends BaseAdapter {

    private LayoutInflater inflater = null;
    private ArrayList<BusinessListDTO> infoList = null;
    private ViewHolder viewHolder = null;
    private Context mContext = null;

    public BusinessListAdapter(Context c , ArrayList<BusinessListDTO> arrays){
        this.mContext = c;
        this.inflater = LayoutInflater.from(c);
        this.infoList = arrays;
    }

    // Adapter가 관리할 Data의 개수를 설정 합니다.
    @Override
    public int getCount() {
        return infoList.size();
    }

    // Adapter가 관리하는 Data의 Item 의 Position을 <객체> 형태로 얻어 옵니다.
    @Override
    public BusinessListDTO getItem(int position) {
        return infoList.get(position);
    }

    // Adapter가 관리하는 Data의 Item 의 position 값의 ID 를 얻어 옵니다.
    @Override
    public long getItemId(int position) {
        return position;
    }

    // ListView의 뿌려질 한줄의 Row를 설정 합니다.
    @Override
    public View getView(int position, View convertview, ViewGroup parent) {

        View v = convertview;

        if(v == null){
            viewHolder = new ViewHolder();
            v = inflater.inflate(R.layout.adapter_list, null);
            viewHolder.tv_seq = (TextView) v.findViewById(R.id.tv1);
            viewHolder.tv_kindStr = (TextView)v.findViewById(R.id.tv2);
            viewHolder.tv_title = (TextView)v.findViewById(R.id.tv3);
            v.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder)v.getTag();
        }

        viewHolder.tv_seq.setText(getItem(position).bnSeq + "");
        viewHolder.tv_kindStr.setText(getItem(position).kindStr);
        viewHolder.tv_title.setText(getItem(position).title);

        return v;
    }

    // Adapter가 관리하는 Data List를 교체 한다.
    // 교체 후 Adapter.notifyDataSetChanged() 메서드로 변경 사실을
    // Adapter에 알려 주어 ListView에 적용 되도록 한다.
    public void setArrayList(ArrayList<BusinessListDTO> arrays){
        this.infoList = arrays;
    }

    public ArrayList<BusinessListDTO> getArrayList(){
        return infoList;
    }

    /*
     * ViewHolder
     * getView의 속도 향상을 위해 쓴다.
     * 한번의 findViewByID 로 재사용 하기 위해 viewHolder를 사용 한다.
     */
    class ViewHolder{
        public TextView tv_seq = null;
        public TextView tv_kindStr = null;
        public TextView tv_title =  null;
    }

    @Override
    protected void finalize() throws Throwable {
        free();
        super.finalize();
    }

    private void free(){
        inflater = null;
        infoList = null;
        viewHolder = null;
        mContext = null;
    }
}
