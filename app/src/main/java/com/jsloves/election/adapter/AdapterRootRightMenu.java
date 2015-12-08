package com.jsloves.election.adapter;

import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jsloves.election.activity.ElectionMainActivity;
import com.jsloves.election.activity.R;
import com.jsloves.election.application.ElectionManagerApp;
import com.jsloves.election.util.NetworkStatus;

import org.json.simple.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by wrjeong on 2015. 8. 14..
 */
public class AdapterRootRightMenu extends BaseExpandableListAdapter {
    private static final String TAG = AdapterRootRightMenu.class.getSimpleName();
    private List<String> mSigungu;
    private List<String> mHangjungdong;
    private List<String> mTuPyogu;
    private String selectedSg;
    private String selectedHd;
    private String selectedTg;
    private Context mContext;
    private boolean mToggle = false;
    private int mLastExpandedPosition = -1;
    private ViewPager mPager;
    private NetworkStatus mNetConn;

    public AdapterRootRightMenu(Context context, List<String> list, ViewPager pager) {
        //super(context, resource, objects);
        //items = objects;
        this.mContext = context;
        this.mSigungu = list;
        this.mPager = pager;
        mNetConn = new NetworkStatus(this.mContext);
    }

    public void setSelectedSg(String selectedSg) {
        this.selectedSg = selectedSg;
    }

    public void setSelectedHd(String selectedHd) {
        this.selectedHd = selectedHd;
    }

    public void setSelectedTg(String selectedTg) {
        this.selectedTg = selectedTg;
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
                Log.d(TAG,"onGroupClick ringht menu 2depth");
                if(mNetConn!=null && mNetConn.isNetworkAvailible()) {
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
                    setSelectedHd(hangjungdong);

                    JSONObject jo = (JSONObject) ElectionManagerApp.getInstance().getSelectItemsObject().get("TUPYOGU");
                    String tupyogus = jo.get(hangjungdong).toString();
                    Type type = new TypeToken<List<String>>() {
                    }.getType();
                    Gson converter = new Gson();
                    List<String> tupyogulist = converter.fromJson(tupyogus, type);
                    mTuPyogu = tupyogulist;
                } else {
                    mNetConn.networkErrPopup();
                }
                return false;
            }
        });

        SecondLevelexplv.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.d(TAG,"onGroupClick ringht menu 3depth");
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
                if(mNetConn!=null && mNetConn.isNetworkAvailible()) {
                    TextView tv = (TextView) v.findViewById(R.id.tv_name3);
                    String tupyogu = tv.getText().toString();
                    setSelectedTg(tupyogu);

                    //[수정] 왜 getItem(0)으로 얻어오는 SearchFragment는 myWebview멤버가 null 일까?? 생성한 SearchFragment 객체가 아닌듯한데!?...
                    //((SearchFragment)mViewPagerAdapter.getItem(0)).tupyoguClickByRightMenu(selectedSg, selectedHd, selectedTg);

                    ((ElectionMainActivity) mContext).getmVpageAdapter().getmSearchFragment().tupyoguClickByRightMenu(selectedSg, selectedHd, selectedTg);
                    ((ElectionMainActivity) mContext).getmDrawerLayout().closeDrawer(GravityCompat.END);
                    if (!((ElectionMainActivity) mContext).getActionBarTitle().equals(selectedSg)) {
                        ((ElectionMainActivity) mContext).setActionBarTitle(selectedSg);
                        ((ElectionMainActivity) mContext).getmVpageAdapter().notifyDataSetChanged();
                    }
                    mPager.setCurrentItem(0);
                } else {
                    mNetConn.networkErrPopup();
                }
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
            String tupyogu = mTuPyogu.get(childPosition);
            holder.tv3.setText(tupyogu);
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
