package com.baelight.weatherartist.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.baelight.weatherartist.MyApplication;
import com.baelight.weatherartist.R;
import com.baelight.weatherartist.db.WeatherArtistDB;
import com.baelight.weatherartist.fragment.AboutFragement;
import com.baelight.weatherartist.fragment.ChooseWidgetFragment;
import com.baelight.weatherartist.fragment.UpdateFragment;
import com.baelight.weatherartist.fragment.WeatherFragment;
import com.baelight.weatherartist.fragment.WeatherPagerAdapter;
import com.baelight.weatherartist.model.County;
import com.baelight.weatherartist.util.HttpCallbackListener;
import com.baelight.weatherartist.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.listener.BmobUpdateListener;
import cn.bmob.v3.update.BmobUpdateAgent;
import cn.bmob.v3.update.UpdateResponse;
import cn.bmob.v3.update.UpdateStatus;

public class WeatherActivity extends AppCompatActivity {

    private ViewPager weatherViewPager;
    private FragmentStatePagerAdapter adapter;
    private List<Fragment> fragments;
    
    private WeatherArtistDB weatherArtistDB;

    /*Toolbar and Navigation Drawer*/
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private ListView leftDrawerList;
    private ArrayAdapter<String> navigationDrawerListAdapter;
    private String[] leftSlideData = {"Weather", "Artist", "About"};
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_for_weather_activity_with_fragment);

//        BmobUpdateAgent.initAppVersion(this);
        BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {
            @Override
            public void onUpdateReturned(int updateStatus, UpdateResponse updateResponse) {
                if (updateStatus == UpdateStatus.Yes) {
                    android.app.DialogFragment updateFragment = new UpdateFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(getString(R.string.new_update_url), updateResponse.path);
                    bundle.putString(getString(R.string.new_update_msg), updateResponse.updateLog);
                    updateFragment.setArguments(bundle);
                    updateFragment.show(getFragmentManager(), "");
                }
            }
        });
        BmobUpdateAgent.update(MyApplication.getContext());

        initView();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name).substring(0, 7));
        initDrawer();

        weatherArtistDB = WeatherArtistDB.getInstance(this);
        //若新增城市，则先更新数据库
        String newCounty = getIntent().getStringExtra("county_name");
        if(!TextUtils.isEmpty(newCounty)){
            weatherArtistDB.updateCounty(newCounty);
            SharedPreferences.Editor defaultEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
            defaultEditor.putString("selected_county_name", newCounty);
            defaultEditor.commit();
        }


        //取出被选城市
        getFragment();

        weatherViewPager = (ViewPager) findViewById(R.id.view_pager);
        adapter = new WeatherPagerAdapter(getSupportFragmentManager(),fragments);
        weatherViewPager.setAdapter(adapter);

        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String selectedCountyName = defaultSharedPreferences.getString("selected_county_name","");

        //找出上次最后一个被查看的County
        for (Fragment fragment : fragments) {
            String countyName = fragment.getArguments().getString("county_name");
            Log.d("TAG", selectedCountyName + " " + countyName);

            if(countyName.equals(selectedCountyName)){
                weatherViewPager.setCurrentItem(fragments.indexOf(fragment), false);
                break;
            }
        }


        //更改当前城市
        weatherViewPager.addOnPageChangeListener(new OnPageChangeListener() {


            @Override
            public void onPageSelected(int position) {
                Fragment fragment = fragments.get(position);
                String countyName = fragment.getArguments().getString("county_name");
                SharedPreferences.Editor defaultEditor = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext()).edit();
                defaultEditor.putString("selected_county_name", countyName);
                defaultEditor.commit();

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });


    }


    private void initView() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        //侧拉菜单
        leftDrawerList = (ListView) findViewById(R.id.left_drawer);
        navigationDrawerListAdapter =new ArrayAdapter<>( this, android.R.layout.simple_list_item_1, leftSlideData);
        leftDrawerList.setAdapter(navigationDrawerListAdapter);

        leftDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {

                    //点击首项，若在Artist界面，则返回Weather界面
                    case 0:
                        if (findViewById(R.id.choose_widget) != null) {
                            onBackPressed();
                        }
                        drawerLayout.closeDrawers();
                        toolbar.setTitle(leftSlideData[position]);
                        break;

                    case 1:
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        Fragment chooseWidgetFragment = new ChooseWidgetFragment();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                        fragmentTransaction.replace(R.id.view_pager_layout, chooseWidgetFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        drawerLayout.closeDrawers();
                        toolbar.setTitle(leftSlideData[position]);
                        Log.d("DRAWER", "Clicked!");
                        break;

                    case 2:
                        DialogFragment dialog = new AboutFragement();
                        dialog.show(getSupportFragmentManager(), "About");
                        drawerLayout.closeDrawers();
                        break;


                    default:
                        break;
                }
            }
        });
    }

    private void initDrawer() {

        //Drawer触发
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                toolbar.setTitle(getResources().getString(R.string.app_name));

            }
        };

        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar.setTitle("Weather");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //菜单里的选项被点击
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    //从SharedPreferences new Fragment
    private void getFragment(){
        List<County> counties = weatherArtistDB.loadSelectedCounty();
        fragments = new ArrayList<Fragment>();
        for (County county : counties) {
                Fragment fragment = new WeatherFragment();

                Bundle bundle = new Bundle();
                bundle.putString("county_name", county.getCountyName());
                fragment.setArguments(bundle);

                fragments.add(fragment);
            }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(findViewById(R.id.choose_widget) == null){
            toolbar.setTitle(getResources().getString(R.string.app_name).substring(0,7));
        }
    }

    //check for update
    public void checkUpdate(){
        //show a progressdialog

        ProgressDialog checkingDialog = new ProgressDialog(WeatherActivity.this);
        checkingDialog.setMessage("Checking...");
        checkingDialog.show();

        String url = null;

        HttpUtil.sendHttpRequest(url, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                //解析返回的xml
            }

            @Override
            public void onError(Exception e) {

            }
        });

        //get xml data from server by AsyncTask

        //new version pop up decide dialog

        //no pop up a snackbar


    }
}

