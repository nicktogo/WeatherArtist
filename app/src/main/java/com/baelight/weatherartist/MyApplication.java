package com.baelight.weatherartist;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
    private static Context context;
    
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
    
    public static Context getContext(){
	return context;
    }

    public static int[] getWeatherImages(){
        //天气图片
        int[] weatherImages = new int[] {R.drawable.sunny, R.drawable.cloudy,
                R.drawable.overcast, R.drawable.rain_light,
                R.drawable.rain_medium,R.drawable.rain_heavy,
                R.drawable.shower,R.drawable.storm,R.drawable.thunder_storm,R.drawable.rain_medium_heavy};

        return weatherImages;
    }
}
