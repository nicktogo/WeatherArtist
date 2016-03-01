package com.baelight.weatherartist.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class WeatherPagerAdapter extends FragmentStatePagerAdapter {
    
    private List<Fragment> fragments;

    public WeatherPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
	super(fm);
	this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
	// TODO Auto-generated method stub
	return fragments.get(position);
    }

    @Override
    public int getCount() {
	// TODO Auto-generated method stub
	return fragments.size();
    }

}
