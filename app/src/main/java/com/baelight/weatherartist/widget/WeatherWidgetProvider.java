package com.baelight.weatherartist.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.baelight.weatherartist.MyApplication;
import com.baelight.weatherartist.R;

public class WeatherWidgetProvider extends AppWidgetProvider {

    private TextView currentCounty;
    private TextView temp1;
    private TextView publishText;

    
    //第一次建立Widget时会被调用
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
        // TODO Auto-generated method stub

            int appWidgetId = appWidgetIds[appWidgetIds.length-1];

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
            String selectedCountyName = sharedPreferences.getString("selected_county_name", "");
            String widgetName = sharedPreferences.getString("widget", "");

            SharedPreferences prefs = MyApplication.getContext().getSharedPreferences(selectedCountyName, 0);
            RemoteViews views = null;

            switch (widgetName){
                case "high_low":
                    views = new RemoteViews(context.getPackageName(),R.layout.widget_high_low_background);
                    views.setTextViewText(R.id.temp1, prefs.getString(0 + "high", "").substring(3));
                    views.setTextViewText(R.id.temp2, prefs.getString(0 + "low", "").substring(3));
                    break;

                case "current":
                    views = new RemoteViews(context.getPackageName(),R.layout.widget_current_background);
                    break;
                case "big":
                    views = new RemoteViews(context.getPackageName(),R.layout.widget_big_background);
                    views.setTextViewText(R.id.wind, "风力" + prefs.getString("wind", ""));
                    views.setTextViewText(R.id.humidity, "湿度" + prefs.getString("humidity", ""));
                    break;
//                case "plain":
//                    views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
//                    views.setTextViewText(R.id.city_name,prefs.getString("city_name",""));
//                    views.setTextViewText(R.id.weather_desc_widget, prefs.getString(0 + "type", ""));
//                    Log.d("WIDGET", prefs.getString("type", ""));
//                    views.setTextViewText(R.id.publish_time_widget, prefs.getString("updatetime", ""));
//                    views.setTextViewText(R.id.temp1_widget, prefs.getString(0 + "low", ""));
//                    views.setTextViewText(R.id.temp2_widget, prefs.getString(0 + "high", "test"));
//                    break;
                case "newWidget":
                    views = new RemoteViews(context.getPackageName(),R.layout.widget_created_background);
//                    currentCounty = (TextView) views.findViewById(R.id.current_county);
//                    currentCounty.setTextColor(defaultPrefs.getInt("name_color",getResources().getColor(R.color.font_default)));
//
//                    temp1 = (TextView) views.findViewById(R.id.temp1);
//                    temp1.setTextColor(defaultPrefs.getInt("temp_color",getResources().getColor(R.color.font_default)));
//
//
//                    publishText = (TextView) views.findViewById(R.id.publish_text);
//                    publishText.setTextColor(defaultPrefs.getInt("time_color",getResources().getColor(R.color.font_default)));

                    views.setTextColor(R.id.current_county,sharedPreferences.getInt
                            ("name_color",MyApplication.getContext().getResources().getColor(R.color.font_default)));

                    views.setTextColor(R.id.temp1,sharedPreferences.getInt
                            ("temp_color",MyApplication.getContext().getResources().getColor(R.color.font_default)));

                    views.setTextColor(R.id.publish_text,sharedPreferences.getInt
                            ("time_color",MyApplication.getContext().getResources().getColor(R.color.font_default)));

                    break;
                default:
                    views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                    views.setTextViewText(R.id.city_name,prefs.getString("city_name",""));
                    views.setTextViewText(R.id.weather_desc_widget, prefs.getString(0 + "type", ""));
                    Log.d("WIDGET", prefs.getString("type", ""));
                    views.setTextViewText(R.id.publish_time_widget, prefs.getString("updatetime", ""));
                    views.setTextViewText(R.id.temp1_widget, prefs.getString(0 + "low", ""));
                    views.setTextViewText(R.id.temp2_widget, prefs.getString(0 + "high", "test"));
                    break;
            }

            views.setTextViewText(R.id.current_county, prefs.getString("city_name", ""));
            if(!widgetName.equals("newWidget")){
                views.setTextViewText(R.id.weather_desc, prefs.getString(0 + "type", ""));
            }
            views.setImageViewResource(R.id.widget_image, MyApplication.getWeatherImages()[prefs.getInt(0 + "type_id", 0)]);

            if(widgetName.equals("high_low") || widgetName.equals("current") || widgetName.equals("newWidget")){
                views.setTextViewText(R.id.publish_text, prefs.getString("updatetime", ""));
            }

            if(widgetName.equals("current") || widgetName.equals("big") || widgetName.equals("newWidget")){
                views.setTextViewText(R.id.temp1, prefs.getString("wendu", "") + "\u00B0");
            }

            appWidgetManager.updateAppWidget(appWidgetId, views);

    }
}
