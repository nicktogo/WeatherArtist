package com.baelight.weatherartist.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.getbase.floatingactionbutton.AddFloatingActionButton;
import com.baelight.weatherartist.MyApplication;
import com.baelight.weatherartist.R;
import com.baelight.weatherartist.activity.ChooseAreaActivity;
import com.baelight.weatherartist.service.AutoUpdateService;
import com.baelight.weatherartist.util.HttpCallbackListener;
import com.baelight.weatherartist.util.HttpUtil;
import com.baelight.weatherartist.util.Utility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WeatherFragment extends Fragment implements OnClickListener {


	private int[] weatherImages;
    private ImageView weatherImage;

	private View otherInfo;
    
    private TextView cityNameText;
    private TextView publishText;
    private TextView currentDateText;
    private TextView weatherDespText;
    private TextView temp1Text;
    private TextView temp2Text;
	private TextView currentTemp;
    
    private RelativeLayout weatherInfoLayout;
    
    private AddFloatingActionButton switch_city;
    
    private SwipeRefreshLayout swipe;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View view;
		view = inflater.inflate(R.layout.weather_layout, container, false);

		//一系列控件
		cityNameText = (TextView) view.findViewById(R.id.current_county);
		publishText = (TextView) view.findViewById(R.id.publish_text);
		currentDateText = (TextView) view.findViewById(R.id.current_date);
		weatherDespText = (TextView) view.findViewById(R.id.weather_desc);
		temp1Text = (TextView) view.findViewById(R.id.temp1);
		temp2Text = (TextView) view.findViewById(R.id.temp2);
		currentTemp = (TextView) view.findViewById(R.id.main_layout_current_temp);
		otherInfo = view.findViewById(R.id.other_info);
        weatherImage = (ImageView) view.findViewById(R.id.weather_image);

		//天气图片
        weatherImages = MyApplication.getWeatherImages();

		//天气信息布局
		weatherInfoLayout = (RelativeLayout) view.findViewById(R.id.weather_info_layout);

		//下滑刷新
		swipe = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
		swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

		@Override
		public void onRefresh() {
		publishText.setText("同步中...");
				SharedPreferences prefs = MyApplication.getContext().getSharedPreferences(getArguments().getString("county_name"), 0);
				String countyName = prefs.getString("city_name", "");
				if(!TextUtils.isEmpty(countyName)){
				queryWeatherInfoFromServerDirectlyByName(countyName);

				}
			swipe.setRefreshing(false);
			}
		});



		swipe.setColorSchemeResources(R.color.orange,R.color.green,R.color.blue);

		switch_city = (AddFloatingActionButton) view.findViewById(R.id.switch_city);
		switch_city.setOnClickListener(this);

		String countyName = getArguments().getString("county_name");
		publishText.setText("同步中...");
		swipe.setRefreshing(true);
		weatherInfoLayout.setVisibility(View.INVISIBLE);
		cityNameText.setVisibility(View.INVISIBLE);
		if(!TextUtils.isEmpty(countyName)){
			queryWeatherInfoFromServerDirectlyByName(countyName);
			Log.i("MainActivity", "countyName 不为空");
			Log.i("MainActivity", "并获得了县名，第一次同步");
		}else{
			showWeather(countyName);
			Log.i("MainActivity", "countyName 为空，直接showweather()");
		}
		swipe.setRefreshing(false);

	return view;
    }
    
    @Override
    public void onClick(View v) {
	switch (v.getId()){
			case R.id.switch_city:
				Intent intent = new Intent(MyApplication.getContext(),ChooseAreaActivity.class);
				intent.putExtra("from_weather_activity", true);

				getActivity().startActivity(intent);
				getActivity().finish();
				break;


			default:
				break;
		}
    }
    
    private void queryWeatherInfoFromServerDirectlyByName(final String countyName){
	
	String query = null;
	
	try {
	    query = URLEncoder.encode(countyName, "utf-8");
	} catch (UnsupportedEncodingException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}
	
	String address;
	address = "http://wthrcdn.etouch.cn/WeatherApi?city=" + query;
	Log.i("MainActivity", "组装后地址 " + address);
	
	HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {

		@Override
		public void onFinish(String response) {
			// call Utility to parse xml data and save it to SharedPreferences
			Utility.handleWeatherResponse(MyApplication.getContext(), response);
			getActivity().runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// 显示天气
					showWeather(countyName);
				}
			});
		}

		@Override
		public void onError(Exception e) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					publishText.setText("同步失败");
				}
			});
		}
	});
    }
    
		private void showWeather(String countyName){
			SharedPreferences prefs = MyApplication.getContext().getSharedPreferences(countyName, 0);

			cityNameText.setText(prefs.getString("city_name", ""));
			publishText.setText(prefs.getString("updatetime", ""));
			currentDateText.setText(prefs.getString("current_date", ""));
			weatherDespText.setText(prefs.getString(0 + "type", ""));
            weatherImage.setImageResource(weatherImages[prefs.getInt(0 + "type_id",0)]);
			temp2Text.setText(prefs.getString(0+"low", ""));
			temp1Text.setText(prefs.getString(0+"high", ""));
			currentTemp.setText(prefs.getString("wendu", ""));

			((TextView)otherInfo.findViewById(R.id.wind)).setText(prefs.getString("wind", ""));
			((TextView)otherInfo.findViewById(R.id.humidity)).setText(prefs.getString("humidity", ""));
			((TextView)otherInfo.findViewById(R.id.wind_dir)).setText(prefs.getString("windDir", ""));


			weatherInfoLayout.setVisibility(View.VISIBLE);
			cityNameText.setVisibility(View.VISIBLE);


            //显示未来三天预报
			View v = null;
			for(int i = 1; i < 4; i++){
				switch (i){
					case 1:
						v = getView().findViewById(R.id.first_day);
						break;
					case 2:
						v = getView().findViewById(R.id.second_day);
						break;
					case 3:
						v = getView().findViewById(R.id.third_day);
						break;
					default:
						break;
				}

				if(v != null){
					((TextView)v.findViewById(R.id.date))
							.setText(prefs.getString(i + "date", ""));

                    ((ImageView)v.findViewById(R.id.forecast_weather_image))
                            .setImageResource(weatherImages[prefs.getInt(i + "type_id", 0)]);

					((TextView)v.findViewById(R.id.low))
							.setText(prefs.getString(i + "low", ""));

					((TextView)v.findViewById(R.id.high))
							.setText(prefs.getString(i + "high",""));
				}
			}


			Log.i("MainActivity", "showWeather");
			Intent intent = new Intent(MyApplication.getContext(),AutoUpdateService.class);
			MyApplication.getContext().startService(intent);
		}
}
