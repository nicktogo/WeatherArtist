

package com.baelight.weatherartist.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.baelight.weatherartist.db.WeatherArtistDB;
import com.baelight.weatherartist.model.City;
import com.baelight.weatherartist.model.County;
import com.baelight.weatherartist.model.Province;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.xml.parsers.SAXParserFactory;

public class Utility {
	
	
	
	public synchronized static boolean handleProvincesResponse(WeatherArtistDB weatherArtistDB, String response){
		
		if(!TextUtils.isEmpty(response)){
			String[] allProvinces = response.split(",");
			if(allProvinces != null && allProvinces.length > 0){
				for (String p : allProvinces) {
					String[] elements = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(elements[0]);
					province.setProvinceName(elements[1]);
					weatherArtistDB.saveProvince(province);
				}				
				return true;
			}
		}
		return false;
	}
	
	
	public synchronized static boolean handleCitiesResponse(WeatherArtistDB weatherArtistDB, String response, int provinceId){
		
		if(!TextUtils.isEmpty(response)){
			String[] allCities = response.split(",");
			if(allCities != null && allCities.length > 0){
				for (String c : allCities) {
					String[] elements = c.split("\\|");
					City city = new City();
					city.setCityCode(elements[0]);
					city.setCityName(elements[1]);
					city.setProvinceId(provinceId);
					
					weatherArtistDB.saveCity(city);
				}
				return true;
			}
		}
		
		return false;
	}
	
	
	public synchronized static boolean handleCountiesResponse(WeatherArtistDB weatherArtistDB, String response, int cityId){
		
		if(!TextUtils.isEmpty(response)){
			String[] allCounties = response.split(",");
			if(allCounties != null && allCounties.length > 0){
				for (String c : allCounties) {
					String[] elements = c.split("\\|");
					County county = new County();
					county.setCountyCode(elements[0]);
					county.setCountyName(elements[1]);
					county.setCityId(cityId);
					
					weatherArtistDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}
	
	
	
	public static void handleWeatherResponse(final Context context, final String response){
	    
	    Log.i("MainActivity", "handleWeatherResponse: " + response);
	    
	    try {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		XMLReader xmlReader = factory.newSAXParser().getXMLReader();
		xmlReader.setContentHandler(new DefaultHandler(){
		    
		    private String nodeName;
		    
		    private StringBuilder cityName;
		    private StringBuilder updatetime;
		    private StringBuilder wendu;
		    private StringBuilder type;
		    private StringBuilder high;
		    private StringBuilder low;

			/*---新增的天气信息---*/
			private StringBuilder wind;
			private StringBuilder humidity;
			private StringBuilder windDir;
            private StringBuffer date;

            private int forecastCount = 4;
		    boolean isFirstWeather = true;
		    
		    @Override
		    public void startDocument() throws SAXException {
				cityName = new StringBuilder();
		        updatetime = new StringBuilder();
		        wendu = new StringBuilder();
		        type = new StringBuilder();
		        high = new StringBuilder();
		        low = new StringBuilder();

				wind = new StringBuilder();
				humidity = new StringBuilder();
				windDir = new StringBuilder();
		        date = new StringBuffer();

		    }
		    
		    @Override
		    public void startElement(String uri, String localName,
		            String qName, Attributes attributes) throws SAXException {
		        nodeName = localName;
		    }
		    
		    @Override
		    public void characters(char[] ch, int start, int length)
		            throws SAXException {
		        
                if(isFirstWeather){
                    if("city".equals(nodeName)){
                        cityName.append(ch, start, length);
                    }else if("updatetime".equals(nodeName)){
                        updatetime.append(ch, start, length);
                    }else if("wendu".equals(nodeName)){
                        wendu.append(ch, start, length);
                    }else if("fengli".equals(nodeName)){
                        wind.append(ch,start,length);
                    }else if("shidu".equals(nodeName)){
                        humidity.append(ch, start, length);
                    }else if("fengxiang".equals(nodeName)){
                        windDir.append(ch, start, length);
                    }
                }

                if(forecastCount != 0){
                    if("date".equals(nodeName)){
                        date.setLength(0);
                        date.append(ch, start, length);
                    } else if("high".equals(nodeName)){
                        high.setLength(0);
                        high.append(ch, start, length);
                    } else if("low".equals(nodeName)){
                        low.setLength(0);
                        low.append(ch, start, length);
                    } else if("type".equals(nodeName)){
                        type.setLength(0);
                        type.append(ch, start, length);
                    }

                }
		    }
		    
		    @Override
		    public void endElement(String uri, String localName, String qName)
		            throws SAXException {
		        if("fengxiang".equals(localName) && isFirstWeather){
                    saveRegWeatherInfo(context,cityName.toString().trim(), wendu.toString().trim(), updatetime.toString().trim(),wind.toString().trim(),
                            humidity.toString().trim(),windDir.toString().trim());

		            isFirstWeather = false;
		        }

                if("weather".equals(localName) && forecastCount != 0){
                    saveForecast(context,cityName.toString().trim(),low.toString().trim(),high.toString().trim(),
                            type.toString().trim(),date.toString().trim(),4-forecastCount);

                    forecastCount--;
                }
		    }
		    
		    @Override
		    public void endDocument() throws SAXException {
			
		    }
		});
		xmlReader.parse(new InputSource(new StringReader(response)));
		
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}

    private static void saveRegWeatherInfo(Context context,String cityName,String wendu,String updatetime,
                                           String wind, String humidity, String windDir){
        SharedPreferences.Editor defaultEditor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        defaultEditor.putBoolean("city_selected", true);
        defaultEditor.apply();

        SharedPreferences.Editor editor = context.getSharedPreferences(cityName, 0).edit();
        editor.putString("city_name", cityName);
        Log.i("MainActivity", cityName);
        editor.putString("wendu", wendu);
        Log.i("MainActivity", wendu);
        editor.putString("updatetime", updatetime);
        Log.i("MainActivity", updatetime);
        editor.putString("wind", wind);
        editor.putString("humidity", humidity);
        editor.putString("windDir", windDir);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        editor.putString("current_date", sdf.format(new Date()));
        editor.apply();
    }

    private static void saveForecast(Context context,String cityName, String low, String high, String type,String date,int day){
        SharedPreferences.Editor editor = context.getSharedPreferences(cityName, 0).edit();
        editor.putString(day + "low", low.substring(low.length()-3));//only temp and symbol
        Log.i("MainActivity", low);
        editor.putString(day + "high", high.substring(high.length()-3));
        Log.i("MainActivity", high);
        editor.putString(day + "type", type);
        Log.i("MainActivity", type);

        editor.putString(day + "date",date.substring(date.length()-3));//desire for date with number

		int typeId = 0;
		switch (type){
			case "晴":
				typeId = 0;
				break;
			case "多云":
				typeId = 1;
				break;
			case "阴":
				typeId = 2;
				break;
			case "小雨":
				typeId = 3;
				break;
			case "中雨":
				typeId = 4;
				break;
			case "大雨":
				typeId = 5;
				break;
			case "阵雨":
				typeId = 6;
				break;
			case "暴雨":
				typeId = 7;
				break;
			case  "雷阵雨":
				typeId = 8;
				break;
			case "中到大雨":
				typeId = 9;
				break;
			default:
				break;
		}
        editor.putInt(day + "type_id", typeId);

        editor.apply();
    }
	
}
