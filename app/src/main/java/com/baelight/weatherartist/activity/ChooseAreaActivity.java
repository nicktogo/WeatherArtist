package com.baelight.weatherartist.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baelight.weatherartist.R;
import com.baelight.weatherartist.db.WeatherArtistDB;
import com.baelight.weatherartist.model.City;
import com.baelight.weatherartist.model.County;
import com.baelight.weatherartist.model.Province;
import com.baelight.weatherartist.util.HttpCallbackListener;
import com.baelight.weatherartist.util.HttpUtil;
import com.baelight.weatherartist.util.Utility;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;

public class ChooseAreaActivity extends Activity {
    
    //当前是在哪个ListView下（省市县）
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    
    private ProgressDialog progressDialog;
    private TextView titleText;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<String>();
    private WeatherArtistDB weatherArtistDB;//省、市、县数据库
    
    
    private List<Province> provinceList;
    
    
    private List<City> cityList;
    
    
    private List<County> countyList;
    
    private Province selectedProvince;
    
    private City selectedCity;
    
    private County selectedCounty;
    
    private int currentLevel;
    
    private boolean is_from_weather_activity;
    
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, getString(R.string.bmob_application_id));

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        is_from_weather_activity = getIntent().getBooleanExtra("from_weather_activity", false);
        
        if(prefs.getBoolean("city_selected", false) && !is_from_weather_activity){
            Intent intent = new Intent(this,WeatherActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);


        listView = (ListView) findViewById(R.id.list_view);
        titleText = (TextView) findViewById(R.id.title_text);
        
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,dataList);
        listView.setAdapter(adapter);
        weatherArtistDB = WeatherArtistDB.getInstance(this);
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int index,
                    long arg3) {
                if(currentLevel == LEVEL_PROVINCE){
                    selectedProvince = provinceList.get(index);
                    queryCities();
                } else if(currentLevel == LEVEL_CITY){
                    selectedCity = cityList.get(index);
                    queryCounties();
                } else if(currentLevel == LEVEL_COUNTY){
                    
                    //query from http://wthrcdn.etouch.cn/WeatherApi?city= , get xml-type data
                    //show the weather info for selected county
                    String countyName = countyList.get(index).getCountyName();
                    Log.i("MainActivity", "1. get from ChooseAreaActivity" + countyName);
                    Intent intent = new Intent(ChooseAreaActivity.this,WeatherActivity.class);
                    intent.putExtra("county_name", countyName);
                    startActivity(intent);
                    finish();
                    
                }
                
            }
	});
        
        queryProvinces();
        
    }
    
    
    private void queryProvinces(){
	provinceList = weatherArtistDB.loadProvince();
	
	if(provinceList.size() > 0){
	    dataList.clear();
	    for (Province province : provinceList) {
		dataList.add(province.getProvinceName());
	    }
	    
	    adapter.notifyDataSetChanged();
	    listView.setSelection(0);
	    titleText.setText("中国");
	    currentLevel = LEVEL_PROVINCE;
	} else{
	    queryFromServer(null, "province");
	}
    }
    
    private void queryCities(){
	cityList = weatherArtistDB.loadCity(selectedProvince.getId());
	if(cityList.size() > 0){
	    dataList.clear();
	    for (City city : cityList) {
		dataList.add(city.getCityName());
	    }
	    
	    adapter.notifyDataSetChanged();
	    listView.setSelection(0);
	    titleText.setText(selectedProvince.getProvinceName());
	    currentLevel = LEVEL_CITY;
	} else{
	    queryFromServer(selectedProvince.getProvinceCode(), "city");
	}
    }
    
    private void queryCounties(){
	countyList = weatherArtistDB.loadCounty(selectedCity.getId());
	if(countyList.size() > 0){
	    dataList.clear();
	    for (County county : countyList) {
		dataList.add(county.getCountyName());
	    } 
	    adapter.notifyDataSetChanged();
	    listView.setSelection(0);
	    titleText.setText(selectedCity.getCityName());
	    currentLevel = LEVEL_COUNTY;
	} else{
	    queryFromServer(selectedCity.getCityCode(), "county");
	}
    }
    
    
    private void queryFromServer(final String code, final String type){
	String address;
	if(!TextUtils.isEmpty(code)){
	    address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
	} else{
	    address = "http://www.weather.com.cn/data/list3/city.xml";
	}
	showProgressDialog();
	
	HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
	    
	    @Override
	    public void onFinish(String response) {
		boolean result = false;
		if("province".equals(type)){
		    result = Utility.handleProvincesResponse(weatherArtistDB, response);
		} else if("city".equals(type)){
		    result = Utility.handleCitiesResponse(weatherArtistDB, response, selectedProvince.getId());
		} else if("county".equals(type)){
		    result = Utility.handleCountiesResponse(weatherArtistDB, response, selectedCity.getId());
		}
		
		if(result){
		    runOnUiThread(new Runnable() {
		        
		        @Override
		        public void run() {
		            closeProgressDialog();
		            
		            if("province".equals(type)){
		        	queryProvinces();
		            } else if("city".equals(type)){
		        	queryCities();
		            } else if("county".equals(type)){
		        	queryCounties();
		            }
		        }
		    });
		}
		
	    }
	    
	    @Override
	    public void onError(Exception e) {
		runOnUiThread(new Runnable() {
		    
		    @Override
		    public void run() {
			closeProgressDialog();
			Toast.makeText(ChooseAreaActivity.this, "同步失败", Toast.LENGTH_SHORT).show();
		    }
		});
		
	    }
	});
	
	 
    }
    
    private void showProgressDialog(){
		if(progressDialog == null){
			progressDialog = new ProgressDialog(this);
			progressDialog.setMessage("同步中...");
			progressDialog.setCanceledOnTouchOutside(false);
		}

		progressDialog.show();
    }
    
    private void closeProgressDialog(){
	if(progressDialog != null)
	    progressDialog.dismiss();
    }
    
    
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
	if(currentLevel == LEVEL_COUNTY){
	    queryCities();
	} else if(currentLevel == LEVEL_CITY){
	    queryProvinces();
	} else{
	    if(is_from_weather_activity){
		Intent intent = new Intent(this,WeatherActivity.class);
		startActivity(intent);
	    }
	    finish();
	}
    }
}
