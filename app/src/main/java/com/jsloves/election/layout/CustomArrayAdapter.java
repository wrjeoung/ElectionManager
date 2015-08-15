package com.jsloves.election.layout;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.jsloves.election.activity.R;;

public class CustomArrayAdapter extends ArrayAdapter<DataClass>{

	private ViewHolder viewHolder = null;
	private LayoutInflater inflater = null;
	private ArrayList<DataClass> infoList = null;
	private Context mContext = null;

	public CustomArrayAdapter(Context c, int textViewResourceId, 
			ArrayList<DataClass> arrays) {
		super(c, textViewResourceId, arrays);
		this.inflater = LayoutInflater.from(c);
		this.mContext = c;
	}

	@Override
	public int getCount() {
		return super.getCount();
	}

	@Override
	public DataClass getItem(int position) {
		return super.getItem(position);
	}

	@Override
	public long getItemId(int position) {
		return super.getItemId(position);
	}

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
	
	public void setArrayList(ArrayList<DataClass> arrays){
		this.infoList  = arrays;
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
			
			// 주소 클릭
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
