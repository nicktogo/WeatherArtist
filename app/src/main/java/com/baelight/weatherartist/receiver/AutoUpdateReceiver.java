package com.baelight.weatherartist.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.baelight.weatherartist.service.AutoUpdateService;

public class AutoUpdateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
	// TODO Auto-generated method stub
	Intent intent = new Intent(arg0,AutoUpdateService.class);
	arg0.startService(intent);
    }

}
