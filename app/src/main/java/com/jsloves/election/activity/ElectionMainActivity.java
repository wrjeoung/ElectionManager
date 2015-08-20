package com.jsloves.election.activity;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jsloves.election.adapter.AdapterRootRightMenu;
import com.jsloves.election.application.ElectionManagerApp;
import com.jsloves.election.common.CommonValuesManager;
import com.jsloves.election.fragment.SearchFragment;
import com.jsloves.election.layout.SlidingTabLayout;
import com.jsloves.election.layout.ViewPagerAdapter;

import org.json.simple.JSONObject;

import java.lang.reflect.Type;
import java.util.List;


public class ElectionMainActivity extends AppCompatActivity implements CommonValuesManager{

    private static final String TAG = ElectionMainActivity.class.getSimpleName();
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ExpandableListView mDrawerMenuRight;
    private ViewPager pager;
    private String titles[] = new String[CommonValuesManager.PAGE_COUNT];
    private Toolbar toolbar;
    private MenuItem mRmIcon;
    private ElectionDrawerListner mDrawLisner;
    private SlidingTabLayout slidingTabLayout;
    private boolean mToggle=false;
    private int mLastExpandedPosition=-1;
    private FragmentManager mFragmentManager;
    private ViewPagerAdapter mVpageAdapter;
    private SearchFragment mSearchFragment;

    class ElectionDrawerListner extends ActionBarDrawerToggle {

        public ElectionDrawerListner(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, @StringRes int openDrawerContentDescRes, @StringRes int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
            mRmIcon.setIcon(R.drawable.swipe_left_100);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            mRmIcon.setIcon(R.drawable.swipe_right_100);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        titles[0] = getString(R.string.area_info);
        titles[1] = getString(R.string.jungchi_hwangyong);
        titles[2] = getString(R.string.social_hwangyong);
        titles[3] = getString(R.string.gigwan_info);
        titles[4] = getString(R.string.jooyo_saup);
        titles[5] = getString(R.string.board);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navdrawer);
        mDrawerMenuRight = (ExpandableListView) findViewById(R.id.navdrawer_right);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_ab_drawer);
        }
        pager = (ViewPager) findViewById(R.id.viewpager);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mFragmentManager=getSupportFragmentManager();
        mVpageAdapter = new ViewPagerAdapter(mFragmentManager, titles);
        mSearchFragment=mVpageAdapter.getmSchFrt();
        pager.setAdapter(mVpageAdapter);

        slidingTabLayout.setViewPager(pager);
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return Color.WHITE;
            }
        });

        mDrawLisner = new ElectionDrawerListner(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawLisner);
        String[] values = new String[]{
                getString(R.string.area_info),
                getString(R.string.jungchi_hwangyong),
                getString(R.string.social_hwangyong),
                getString(R.string.gigwan_info),
                getString(R.string.jooyo_saup),
                getString(R.string.board)
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 좌측 메뉴
                switch (position) {
                    case 0:
                        mDrawerList.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                        toolbar.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                        slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                        pager.setCurrentItem(position);
                        mDrawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 1:
                        //mDrawerList.setBackgroundColor(getResources().getColor(R.color.red));
                        //toolbar.setBackgroundColor(getResources().getColor(R.color.red));
                        //slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.red));
                        pager.setCurrentItem(position);
                        mDrawerLayout.closeDrawer(GravityCompat.START);

                        break;
                    case 2:
                        //mDrawerList.setBackgroundColor(getResources().getColor(R.color.blue));
                        //toolbar.setBackgroundColor(getResources().getColor(R.color.blue));
                        //slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.blue));
                        pager.setCurrentItem(position);
                        mDrawerLayout.closeDrawer(GravityCompat.START);

                        break;
                    case 3:
                        //mDrawerList.setBackgroundColor(getResources().getColor(R.color.material_blue_grey_800));
                        //toolbar.setBackgroundColor(getResources().getColor(R.color.material_blue_grey_800));
                        //slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.material_blue_grey_800));
                        pager.setCurrentItem(position);
                        mDrawerLayout.closeDrawer(GravityCompat.START);

                        break;
                    case 4:
                        pager.setCurrentItem(position);
                        mDrawerLayout.closeDrawer(GravityCompat.START);

                        break;

                    case 5:
                        pager.setCurrentItem(position);
                        mDrawerLayout.closeDrawer(GravityCompat.START);

                        break;
                }
            }
        });

        String sigungus = ElectionManagerApp.getInstance().getSelectItemsObject().get("SIGUNGU").toString();
        List<String> sigunguList = convertFromJson(sigungus);
        Log.d(TAG, "expandLog list : " + sigunguList);

        final AdapterRootRightMenu adapter2 = new AdapterRootRightMenu(this,sigunguList,pager);
        adapter2.setmFragmentManager(mFragmentManager);
        adapter2.setmSearchFragment(mSearchFragment);
        mDrawerMenuRight.setAdapter( adapter2);
        mDrawerMenuRight.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                parent.getChildAt(groupPosition).findViewById(R.id.indicator).setSelected(mToggle = !mToggle);
                String sigungu = ((TextView) v.findViewById(R.id.tv_name)).getText().toString();
                adapter2.setSelectedSg(sigungu);
                Log.d(TAG, "expandLog onGroupClick groupPostion : " + groupPosition + " item : " + sigungu);

                JSONObject jo1 = (JSONObject) ElectionManagerApp.getInstance().getSelectItemsObject().get("HAENGJOUNGDONG");
                String hangjungdongs = jo1.get(sigungu).toString();
                List<String> hangjungdongList = convertFromJson(hangjungdongs);
                adapter2.setmHangjungdong(hangjungdongList);

                return false;
            }
        });

        mDrawerMenuRight.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (mLastExpandedPosition != -1
                        && groupPosition != mLastExpandedPosition) {
                    mDrawerMenuRight.collapseGroup(mLastExpandedPosition);
                }
                mLastExpandedPosition = groupPosition;
            }
        });

    }

    private List<String> convertFromJson(String item) {
        List<String> itemList;
        Type type = new TypeToken<List<String>>(){}.getType();
        Gson converter = new Gson();
        itemList =  converter.fromJson(item, type);
        return itemList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawLisner.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_right_menu:
                mDrawerLayout.openDrawer(GravityCompat.END);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    private void setRightMenuIcon() {
//        if(mDrawerLayout.isDrawerOpen(GravityCompat.END))
//            item.setIcon(R.drawable.swipe_right_100);
//        else
//            item.setIcon(R.drawable.swipe_left_100);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.toolbar_menu_items, menu);
        mRmIcon = menu.findItem(R.id.action_right_menu);
        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawLisner.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "onConfigurationChanged");
        super.onConfigurationChanged(newConfig);
        mDrawLisner.onConfigurationChanged(newConfig);
    }
}
