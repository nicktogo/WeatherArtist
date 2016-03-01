package com.baelight.weatherartist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherArtistOpenHelper extends SQLiteOpenHelper {
	
	
	//Province建表
	public static final String CREATE_PROVINCE = "create table Province(" +
			"id integer primary key autoincrement, " +
			"province_name text, " +
			"province_code text" +
			")";
	
	//City建表
	public static final String CREATE_CITY = "create table City(" +
			"id integer primary key autoincrement, " +
			"city_name text, " +
			"city_code text, " +
			"province_id integer" +
			")";
	
	//County建表
	public static final String CREATE_COUNTY = "create table County(" +
			"id integer primary key autoincrement, " +
			"county_name text, " +
			"county_code, " +
			"city_id integer" +
			")";
	
	//County新表
	public static final String NEW_CREATE_COUNTY = "create table County(" +
		"id integer primary key autoincrement, " +
		"county_name text, " +
		"county_code text, " +
		"city_id integer, " +
		"county_select integer default 0" +//默认为0,未被选中
		")";
	

	public WeatherArtistOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_PROVINCE);
		db.execSQL(CREATE_CITY);
		db.execSQL(NEW_CREATE_COUNTY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	    switch(oldVersion){
	    case 1:
		db.execSQL("alter table County add column county_select integer default 0");
		break;
	    default:
		break;
	    }
	}

}
