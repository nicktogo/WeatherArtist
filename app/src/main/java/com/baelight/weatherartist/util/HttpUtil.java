


package com.baelight.weatherartist.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
	
	public static void sendHttpRequest(final String address, final HttpCallbackListener listener){
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				
				HttpURLConnection connection = null;
				
				try {
					
					URL url = new URL(address);
					Log.i("MainActivity", "url is " + url.toString());
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(10*1000);
					connection.setReadTimeout(10*1000);
					InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader(new InputStreamReader(in));
					
					StringBuilder response = new StringBuilder();
					String line;
					while((line = reader.readLine()) != null){
						response.append(line);
					}
					
					if(listener != null){
						listener.onFinish(response.toString());
					}
					
					
				} catch (Exception e) {
					// TODO: handle exception
					if(listener != null){
						listener.onError(e);
					}
				} finally{
					if(connection != null){
						connection.disconnect();
					}
				}
			}
		}).start();
		
	}
}
