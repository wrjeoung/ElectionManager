package com.jsloves.election.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jsloves.election.activity.R;
import com.jsloves.election.application.ElectionManagerApp;

import org.json.simple.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by wrjeong on 2015. 8. 14..
 */
public class AdapterRootRightMenu extends BaseExpandableListAdapter {

    private List<String> mSigungu;
    private List<String> mHangjungdong;
    private List<String> mTuPyogu;
    private String SelectedSg;
    private String selectedHd;
    private String selectedTp;
    private Context mContext;
    private boolean mToggle = false;
    private int mLastExpandedPosition = -1;

    public AdapterRootRightMenu(Context context, List<String> list) {
        //super(context, resource, objects);
        //items = objects;
        this.mContext = context;
        this.mSigungu = list;
    }

    public void setmHangjungdong(List<String> mHangjungdong) {
        this.mHangjungdong = mHangjungdong;
    }

    @Override
    public int getGroupCount() {
        return mSigungu.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupPosition;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder = new ViewHolder();
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.first_level_item, null);
            holder.tv1 = (TextView) v.findViewById(R.id.tv_name);
            holder.iv1 = (ImageView) v.findViewById(R.id.indicator);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        String sigungu = mSigungu.get(groupPosition);
        Log.d("", "expandLog getGroupView signugu : " + sigungu);
        holder.tv1.setText(sigungu);
        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final CustExpListview SecondLevelexplv = new CustExpListview(mContext);
        SecondLevelexplv.setAdapter(new SecondLevelAdapter(mContext));
        SecondLevelexplv.setGroupIndicator(null);
        SecondLevelexplv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                ViewHolder holder = new ViewHolder();
                if (v == null) {

                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = inflater.inflate(R.layout.second_level_item, null);
                    holder.tv2 = (TextView) v.findViewById(R.id.tv_name2);
                    holder.iv2 = (ImageView) v.findViewById(R.id.indicator2);
                    v.setTag(holder);
                } else {
                    holder = (ViewHolder) v.getTag();
                }

                holder.iv2.setSelected(mToggle = !mToggle);
                String hangjungdong = holder.tv2.getText().toString();
                Toast.makeText(mContext.getApplicationContext(),"selected hangjungdong : "+hangjungdong,Toast.LENGTH_SHORT).show();

                JSONObject jo = (JSONObject) ElectionManagerApp.getInstance().getSelectItemsObject().get("TUPYOGU");
                String tupyogus = jo.get(hangjungdong).toString();
                Type type = new TypeToken<List<String>>() {
                }.getType();
                Gson converter = new Gson();
                List<String> tupyogulist = converter.fromJson(tupyogus, type);
                mTuPyogu = tupyogulist;
                return false;
            }
        });

        SecondLevelexplv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                ViewHolder holder = new ViewHolder();
//                if (v == null) {
//                    LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                    v = inflater.inflate(R.layout.third_level_item, null);
//                    holder.tv3 = (TextView) v.findViewById(R.id.tv_name3);
//                    v.setTag(holder);
//                } else {
//                    holder = (ViewHolder) v.getTag();
//                }
//
//                String tupyogu = holder.tv3.getText().toString();
//                Toast.makeText(mContext.getApplicationContext(),"selected tupyogu : "+tupyogu,Toast.LENGTH_SHORT).show();

                TextView tv = (TextView)v.findViewById(R.id.tv_name3);
                String text = tv.getText().toString();
                Toast.makeText(mContext.getApplicationContext(),"selected tupyogu : "+text,Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        SecondLevelexplv.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                //mToggle = true;
                if (mLastExpandedPosition != -1
                        && groupPosition != mLastExpandedPosition) {
                    SecondLevelexplv.collapseGroup(mLastExpandedPosition);
                }
                mLastExpandedPosition = groupPosition;
            }
        });

        SecondLevelexplv.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                //mToggle = false;
            }
        });
        return SecondLevelexplv;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class ViewHolder {
        TextView tv1;
        ImageView iv1;
        TextView tv2;
        ImageView iv2;
        TextView tv3;
    }


    public class CustExpListview extends ExpandableListView {

        public CustExpListview(Context context) {
            super(context);
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            //widthMeasureSpec = MeasureSpec.makeMeasureSpec(960, MeasureSpec.AT_MOST);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(3000, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        @Override
        protected void onDetachedFromWindow() {
            try {
                super.onDetachedFromWindow();
            } catch (IllegalArgumentException e) {
                // TODO: Workaround for http://code.google.com/p/android/issues/detail?id=22751
                e.printStackTrace();
            }
        }
    }

    public class SecondLevelAdapter extends BaseExpandableListAdapter {


        private Context context;


        public SecondLevelAdapter(Context context) {
            this.context = context;
        }


        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childPosition;
        }


        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }


        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View v = convertView;
            ViewHolder holder = new ViewHolder();
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.third_level_item, null);
                holder.tv3 = (TextView) v.findViewById(R.id.tv_name3);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

            Log.d("", "expandLog getGroupView tupyogu" + mTuPyogu.size() + "groupPosition : " + groupPosition);
            String tupyogu = mTuPyogu.get(childPosition);
            holder.tv3.setText(tupyogu);
            Log.d("", "expandLog getGroupView tupyogu : " + tupyogu);

            return v;
        }


        @Override
        public int getChildrenCount(int groupPosition) {
            return mTuPyogu.size();
        }


        @Override
        public Object getGroup(int groupPosition) {
            return groupPosition;
        }


        @Override
        public int getGroupCount() {
            return mHangjungdong.size();
        }


        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }


        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View v = convertView;
            ViewHolder holder = new ViewHolder();
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.second_level_item, null);
                holder.tv2 = (TextView) v.findViewById(R.id.tv_name2);
                holder.iv2 = (ImageView) v.findViewById(R.id.indicator2);
                v.setTag(holder);
            } else {
                holder = (ViewHolder) v.getTag();
            }

            String hangjungdong = mHangjungdong.get(groupPosition);
            Log.d("", "expandLog getGroupView hangjungdong : " + hangjungdong);
            holder.tv2.setText(hangjungdong);
            return v;
        }


        @Override
        public boolean hasStableIds() {
            return true;
        }


        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
