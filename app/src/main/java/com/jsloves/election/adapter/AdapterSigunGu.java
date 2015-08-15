package com.jsloves.election.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jsloves.election.activity.R;

import java.util.List;

/**
 * Created by wrjeong on 2015. 8. 14..
 */
public class AdapterSigunGu extends ArrayAdapter<List<String>> {

    private List<String> items;

    public AdapterSigunGu(Context context, int resource, List objects) {
        super(context, resource, objects);
        items = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v==null) {

            LayoutInflater lif = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = lif.inflate(R.layout.navigation_drawer_item,null);
        }
        String item = items.get(position);

        if(item != null) {
            TextView tv_name = (TextView)v.findViewById(R.id.tv_name);
            if(tv_name != null) {
                tv_name.setText(item);
            }
        }

        return v;
    }


}
