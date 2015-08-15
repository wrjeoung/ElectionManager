package com.jsloves.election.layout;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.jsloves.election.activity.R;

public class CustomBaseAdapter extends BaseAdapter{
	
	private LayoutInflater inflater = null;
	private ArrayList<DataClass> infoList = null;
	private ViewHolder viewHolder = null;
	private Context mContext = null;
	
	public CustomBaseAdapter(Context c , ArrayList<DataClass> arrays){
		System.out.println("array:" + arrays);
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
	public DataClass getItem(int position) {
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
			v = inflater.inflate(R.layout.intro_list, null);
			viewHolder.tv_organ = (TextView)v.findViewById(R.id.tv_organ);
			viewHolder.tv_region = (TextView)v.findViewById(R.id.tv_region);
			v.setTag(viewHolder);
			
		}else {
			viewHolder = (ViewHolder)v.getTag();
		}
		
		viewHolder.tv_organ.setText(getItem(position).organ);
		viewHolder.tv_region.setText(getItem(position).region);

		return v;
	}
	
	// Adapter가 관리하는 Data List를 교체 한다. 
	// 교체 후 Adapter.notifyDataSetChanged() 메서드로 변경 사실을
	// Adapter에 알려 주어 ListView에 적용 되도록 한다.
	public void setArrayList(ArrayList<DataClass> arrays){
		this.infoList = arrays;
	}
	
	public ArrayList<DataClass> getArrayList(){
		return infoList;
	}
	
	private View.OnClickListener buttonClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			
			// 기관명 클릭
			case R.id.tv_organ:
				Toast.makeText(
						mContext, 
						"기관명 Tag = " + v.getTag(),
						Toast.LENGTH_SHORT
						).show();
				break;

			// 지역 클릭
			case R.id.tv_region:
				Toast.makeText(
						mContext,
						"주소 Tag = " + v.getTag(),
						Toast.LENGTH_SHORT
						).show();
				break;

			default:
				break;
			}
		}
	};
	
	/*
	 * ViewHolder 
	 * getView의 속도 향상을 위해 쓴다.
	 * 한번의 findViewByID 로 재사용 하기 위해 viewHolder를 사용 한다.
	 */
	class ViewHolder{
		public TextView tv_organ = null;
		public TextView tv_region = null;
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
