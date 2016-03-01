

package com.baelight.weatherartist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.baelight.weatherartist.model.City;
import com.baelight.weatherartist.model.County;
import com.baelight.weatherartist.model.Province;

import java.util.ArrayList;
import java.util.List;

public class WeatherArtistDB {
	
	
	public static final String DB_NAME = "weather_artist";
	
	
	public static final int VERSION = 2;
	
	private static WeatherArtistDB weatherArtistDB;
	
	private SQLiteDatabase db;
	
	
	private WeatherArtistDB(Context context) {
		WeatherArtistOpenHelper dbHelper = new WeatherArtistOpenHelper(context, DB_NAME, null, VERSION);
		
		db = dbHelper.getWritableDatabase();
	}
	
	
	public synchronized static WeatherArtistDB getInstance(Context context){
		if(weatherArtistDB == null){
			weatherArtistDB = new WeatherArtistDB(context);
		}
		
		return weatherArtistDB;
	}
	
	
	public void saveProvince(Province province){
		if(province != null){
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code",province.getProvinceCode());
			db.insert("Province", null, values);
		}
	}
	
	
	public List<Province> loadProvince(){
		List<Province> list = new ArrayList<Province>();
		
		Cursor cursor = db.query("Province", null, null, null, null, null, null);
		
		if(cursor.moveToFirst()){
			do{
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				
				list.add(province);
			} while(cursor.moveToNext());
		}
		
		return list;
	}
	
	
	public void saveCity(City city){
		if(city != null){
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			
			db.insert("City", null, values);
		}
	}
	
	
	public List<City> loadCity(int provinceId){
		List<City> list = new ArrayList<City>();
		
		Cursor cursor = db.query("City", null, "province_id = ?", new String[] {String.valueOf(provinceId)},
				null, null, null);
		
		if(cursor.moveToFirst()){
			do {
				
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
				
				list.add(city);
				
			} while (cursor.moveToNext());
				
			
		}
		
		return list;
	}
	
	
	public void saveCounty(County county){
		
		if(county != null){
			ContentValues values = new ContentValues();
			values.put("county_name", county.getCountyName());
			values.put("county_code", county.getCountyCode());
			values.put("city_id", county.getCityId());
			
			db.insert("County", null, values);
		}
	}
	
	
	public List<County> loadCounty(int cityId){
		List<County> list = new ArrayList<County>();
		
		Cursor cursor = db.query("County", null, "city_id = ?", new String[] {String.valueOf(cityId)},
				null, null, null);
		
		if(cursor.moveToFirst()){
			do {
				
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
				
				list.add(county);
				
			} while (cursor.moveToNext());
		}
		
		return list;
	}
	
	//更新被选情况
	public void updateCounty(String countyName){
	    ContentValues values = new ContentValues();
	    values.put("county_select", 1);
	    db.update("County", values, "county_name = ?", new String[] {countyName});
	}
	
	//返回被选过的County集合（county_select 为 1）
	public List<County> loadSelectedCounty(){
	    List<County> list = new ArrayList<County>();
	    
	    Cursor cursor = db.query("County", null, "county_select = ?", new String[] {String.valueOf(1)},
		    null, null, null);
	    
	    if(cursor.moveToFirst()){
		do{
		    County county = new County();
		    county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
		    
		    list.add(county);
		} while(cursor.moveToNext());
	    }
	    
	    return list;
	}
}
