package com.jsloves.election.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import com.jsloves.election.common.OnBackPressedListener;
import com.jsloves.election.fragment.HomeMenuOnCLickListner;
import com.jsloves.election.layout.SlidingTabLayout;
import com.jsloves.election.layout.ViewPagerAdapter;
import com.jsloves.election.util.NetworkStatus;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import memo.activity.BoardActivity;

/* fragment 의 메인이 되는 화면. */
public class ElectionMainActivity extends AppCompatActivity implements CommonValuesManager, HomeMenuOnCLickListner {

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
    private ActionBar mActionbar;
    private NetworkStatus mNetConn;
    private OnBackPressedListener mOnBackPressedListner;
    private String mFileName[];

    public void setBackKeyListnerbyFragment(OnBackPressedListener obpl) {
        mOnBackPressedListner = obpl;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNetConn!=null && !mNetConn.isNetworkAvailible()) {
            Log.e(TAG,"onResume network error!!!");
            mNetConn.networkErrPopup();
        }
    }

    @Override
    public void onCLickLinstnerByHome(int id) {
        Log.d(TAG, "onClickListnerByHome id : " + id);
        if (pager == null)
            return;

        switch(id) {
            case R.id.guyeok:
                pager.setCurrentItem(1);
                break;
            case R.id.gighan:
                pager.setCurrentItem(2);
                break;
            case R.id.juyo:
                pager.setCurrentItem(3);
                break;
            case R.id.board:
                pager.setCurrentItem(4);
                break;
        }
    }

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
    public void onBackPressed() {
        Log.d(TAG,"onBackPressed");
        if(mOnBackPressedListner!=null) {
            mOnBackPressedListner.onBackPressed();
            mOnBackPressedListner=null;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        mNetConn = new NetworkStatus(this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        titles[0] = getString(R.string.area_info);
        titles[1] = getString(R.string.gigwan_info);
        titles[2] = getString(R.string.jooyo_saup);
        //titles[3] = getString(R.string.board);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navdrawer);
        mDrawerMenuRight = (ExpandableListView) findViewById(R.id.navdrawer_right);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            mActionbar = getSupportActionBar();
            setActionBarTitle(getSigunguTextFromAdm_cd(ElectionManagerApp.getInstance().getDefaultAdm_Cd()));
            toolbar.setNavigationIcon(R.drawable.ic_ab_drawer);
            toolbar.setTitleTextColor(Color.WHITE);
        }
        pager = (ViewPager) findViewById(R.id.viewpager);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        mFragmentManager=getSupportFragmentManager();
        mVpageAdapter = new ViewPagerAdapter(mFragmentManager, titles, pager);
        pager.setAdapter(mVpageAdapter);
        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });

        int startPosition = (int)getIntent().getExtras().get("startPosition");
        pager.setCurrentItem(startPosition);

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
                getString(R.string.home),
                getString(R.string.area_info),
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
                if(mNetConn!=null && mNetConn.isNetworkAvailible()) {
                    // 좌측 메뉴
                    switch (position) {
                        case 0: // 홈
                            Intent intent = new Intent(ElectionMainActivity.this, ElectionHomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            break;
                        case 1: // 구역정보
                            //mDrawerList.setBackgroundColor(getResources().getColor(R.color.blue));
                            //toolbar.setBackgroundColor(getResources().getColor(R.color.blue));
                            //slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.blue));
                            pager.setCurrentItem(position - 1);
                            mDrawerLayout.closeDrawer(GravityCompat.START);
                            break;
                        case 2: // 기관정보
                            //mDrawerList.setBackgroundColor(getResources().getColor(R.color.material_blue_grey_800));
                            //toolbar.setBackgroundColor(getResources().getColor(R.color.material_blue_grey_800));
                            //slidingTabLayout.setBackgroundColor(getResources().getColor(R.color.material_blue_grey_800));
                            pager.setCurrentItem(position - 1);
                            mDrawerLayout.closeDrawer(GravityCompat.START);
                            break;
                        case 3: // 주요사업
                            pager.setCurrentItem(position - 1);
                            mDrawerLayout.closeDrawer(GravityCompat.START);
                            break;
                        case 4: // 게시판
                            //pager.setCurrentItem(position-1);
                            //mDrawerLayout.closeDrawer(GravityCompat.START);
                            Intent boardIntent = new Intent(ElectionMainActivity.this, BoardActivity.class);
                            startActivity(boardIntent);
                            break;
                    }
                } else {
                    mNetConn.networkErrPopup();
                }
            }
        });

        String sigungus = ElectionManagerApp.getInstance().getSelectItemsObject().get("SIGUNGU").toString();
        List<String> sigunguList = convertFromJson(sigungus);
        Log.d(TAG, "expandLog list : " + sigunguList);

        final AdapterRootRightMenu adapter2 = new AdapterRootRightMenu(this, sigunguList, pager);
        mDrawerMenuRight.setAdapter(adapter2);
        mDrawerMenuRight.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Log.d(TAG,"onGroupClick ringht menu 1depth");
                if(mNetConn!=null && mNetConn.isNetworkAvailible()) {
                    parent.getChildAt(groupPosition).findViewById(R.id.indicator).setSelected(mToggle = !mToggle);
                    String sigungu = ((TextView) v.findViewById(R.id.tv_name)).getText().toString();
                    adapter2.setSelectedSg(sigungu);
                    JSONObject jo1 = (JSONObject) ElectionManagerApp.getInstance().getSelectItemsObject().get("HAENGJOUNGDONG");
                    String hangjungdongs = jo1.get(sigungu).toString();
                    List<String> hangjungdongList = convertFromJson(hangjungdongs);
                    adapter2.setmHangjungdong(hangjungdongList);
                } else {
                    mNetConn.networkErrPopup();
                }
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

    public ViewPagerAdapter getmVpageAdapter() {
        return mVpageAdapter;
    }

    public DrawerLayout getmDrawerLayout() {
        return mDrawerLayout;
    }

    private List<String> convertFromJson(String item) {
        List<String> itemList;
        Type type = new TypeToken<List<String>>(){}.getType();
        Gson converter = new Gson();
        itemList =  converter.fromJson(item, type);
        return itemList;
    }

    public void setActionBarTitle(String title) {
        mActionbar.setTitle(title);
    }

    public String getActionBarTitle() {
        return (String)mActionbar.getTitle();
    }

    private String getSigunguTextFromAdm_cd(String adm_cd) {
        String sigunguCode = adm_cd.substring(0,5);
        final JSONArray sigunguTextArray = (JSONArray)ElectionManagerApp.getInstance().getSelectItemsObject().get("SIGUNGU");
        final JSONArray sigunguCodeArray = (JSONArray)ElectionManagerApp.getInstance().getSelectItemsCodeObject().get("SIGUNGU");

        int sigunguIndex = ElectionManagerApp.getIndex(sigunguCodeArray, sigunguCode);

        return (String)sigunguTextArray.get(sigunguIndex);
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
