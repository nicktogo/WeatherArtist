package com.baelight.weatherartist.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

import com.baelight.weatherartist.receiver.AutoUpdateReceiver;
import com.baelight.weatherartist.util.HttpCallbackListener;
import com.baelight.weatherartist.util.HttpUtil;
import com.baelight.weatherartist.util.Utility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class AutoUpdateService extends Service {

    @Override
    public IBinder onBind(Intent arg0) {
	// TODO Auto-generated method stub
	return null;
    }

    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub

	new Thread(new Runnable() {
	    
	    @Override
	    public void run() {
		updateWeather();
		
	    }
	}).start();
	
	
	int eightHour = 8*60*60*1000;//八小时
	long triggerAtTime = SystemClock.elapsedRealtime() + eightHour;
	
	Intent i = new Intent(this,AutoUpdateReceiver.class);
	PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
	
	AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
	manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
	
        return super.onStartCommand(intent, flags, startId);
    }
    
    private void updateWeather(){
	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	
	String countyName = prefs.getString("city_name", "");
	
	String query = null;
	try {
	    query = URLEncoder.encode(countyName, "utf-8");
	} catch (UnsupportedEncodingException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}
	
	String address = "http://wthrcdn.etouch.cn/WeatherApi?city=" + query;
	
	HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
	    
	    @Override
	    public void onFinish(String response) {
		// TODO Auto-generated method stub
		Utility.handleWeatherResponse(AutoUpdateService.this, response);
	    }
	    
	    @Override
	    public void onError(Exception e) {
		// TODO Auto-generated method stub
		e.printStackTrace();
	    }
	});
	
	
    }
}
