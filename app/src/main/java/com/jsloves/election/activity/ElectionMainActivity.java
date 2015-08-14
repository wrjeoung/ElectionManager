package com.jsloves.election.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.ListView;

import com.jsloves.election.application.ElectionManagerApp;
import com.jsloves.election.common.CommonValuesManager;
import com.jsloves.election.fragment.AsyncFragment;
import com.jsloves.election.fragment.AsyncListener;
import com.jsloves.election.layout.SlidingTabLayout;
import com.jsloves.election.layout.ViewPagerAdapter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class ElectionMainActivity extends AppCompatActivity implements CommonValuesManager,AsyncListener<Integer, String> {

    private static final String TAG = ElectionMainActivity.class.getSimpleName();
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ListView mDrawerListRight;
    private ViewPager pager;
    private String titles[] = new String[CommonValuesManager.PAGE_COUNT];
    private Toolbar toolbar;
    private MenuItem mRmIcon;
    private ElectionDrawerListner mDrawLisner;
    private SlidingTabLayout slidingTabLayout;
    private final String ASYNC = "async";
    private ProgressDialog dialog;


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
        mDrawerListRight = (ListView) findViewById(R.id.navdrawer_right);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_ab_drawer);
        }
        pager = (ViewPager) findViewById(R.id.viewpager);
        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), titles));

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
        try {
            JSONObject json1 = new JSONObject();
            json1.put("TYPE", "SELECTITEMS2");
            setUp(getString(R.string.server_url),json1.toString());
        } catch(Exception e) {
            e.printStackTrace();
        }
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

    private void setUp(String url,String params) {
        AsyncFragment async = (AsyncFragment)
                getSupportFragmentManager().findFragmentByTag(ASYNC);

        if (async == null) {
            async = new AsyncFragment();
            Bundle bundle = new Bundle();
            bundle.putString("URL",url);
            bundle.putString("PARAMS",params);
            async.setArguments(bundle);
            FragmentTransaction transaction =
                    getSupportFragmentManager().beginTransaction();
            transaction.add(async, ASYNC);
            transaction.commit();
        }
    }

    private void prepareProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                AsyncFragment async = (AsyncFragment)
                        getSupportFragmentManager().findFragmentByTag(ASYNC);
                async.cancel();
            }
        });
        //dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    }
    private void cleanUp() {
        dialog.dismiss();
        dialog = null;
        FragmentManager fm = getSupportFragmentManager();
        AsyncFragment async = (AsyncFragment) fm.findFragmentByTag(ASYNC);
        fm.beginTransaction().remove(async).commit();
    }

    @Override
    public void onPreExecute() {
        if (dialog == null) {
            prepareProgressDialog();
        }
        dialog.show();
    }

    @Override
    public void onProgressUpdate(Integer... progress) {

    }

    @Override
    public void onPostExecute(String resultData) {
        try {
            JSONObject re = null;
            JSONParser par = new JSONParser();
            re = (JSONObject) par.parse(resultData);
            String result = (String) re.get("RESULT");

            if (result.equals("SUCCESS")) {
                ElectionManagerApp.getInstance().setSelectItems(((JSONObject)re.get("SELECTITEMS2")).toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cleanUp();
        }
    }

    @Override
    public void onCancelled(String s) {
        cleanUp();
    }

}
