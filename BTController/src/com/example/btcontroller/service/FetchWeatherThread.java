package com.example.btcontroller.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;

import com.example.btcontroller.WeatherXmlParser;
import com.spark.hudsharedlib.MessageWeatherItem;
import com.spark.hudsharedlib.MessageWeatherItemGroup;

public class FetchWeatherThread extends Thread {
	private Context mContext;
	private Boolean mEnabled;
	private ArrayList<String> requestList;
	
	public FetchWeatherThread(Context context, Boolean enabled, ArrayList<String> cityList) {
		mContext = context;
		mEnabled = enabled;
		
		String weather_prefix = "http://api.map.baidu.com/telematics/v3/weather?location=";
		String weather_suffix = "&output=xml&ak=889c9b331927a095810c660c4c7058ea";
	   
		requestList = new ArrayList<String> ();
		for (String cityName : cityList) {
			String request = weather_prefix + cityName + weather_suffix;
			//SettingWeatherTask task = new SettingWeatherTask();
			//task.execute(request);
			requestList.add(request);
		}
	}
	
	@Override
	public void run() {
		while(mEnabled && (requestList.isEmpty()==false)) {
			SettingWeatherThread sThread = new SettingWeatherThread(requestList);
			sThread.start();
			
	        try {
					//Thread.sleep(21600); //one hour interval
	        		Thread.sleep(3600*15); // for test - wait for another  minutes to fetch
	        } catch (InterruptedException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
	        	//Thread.currentThread().interrupt();
			}   
		}
	}
	
	public void settingUpdate(Boolean enabled, ArrayList<String> cityList) {
		mEnabled = enabled;
		
		requestList.clear();
		
		String weather_prefix = "http://api.map.baidu.com/telematics/v3/weather?location=";
		String weather_suffix = "&output=xml&ak=889c9b331927a095810c660c4c7058ea";
		for (String cityName : cityList) {
			String request = weather_prefix + cityName + weather_suffix;
			//SettingWeatherTask task = new SettingWeatherTask();
			//task.execute(request);
			requestList.add(request);
		}		
		
	}
	/*
	 * Functional equivalent to the old SettingWeatherTask
	 */
	private class SettingWeatherThread extends Thread {
		ArrayList<String> uriArray;

		public SettingWeatherThread (ArrayList<String> a) {
			uriArray = a;
		}

		@Override
		public void run () {
			InputStream is;
			MessageWeatherItemGroup	weatherItemGroup = new MessageWeatherItemGroup();

			for (String uri : uriArray) {
				HttpGet get = new HttpGet(uri);
				DefaultHttpClient httpClient = new DefaultHttpClient();
				try {
					HttpResponse httpResponse = httpClient.execute(get);
	
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						HttpEntity entity = httpResponse.getEntity();
	
						is = entity.getContent();
						MessageWeatherItem weatherItem = WeatherXmlParser.parse(is);
						weatherItemGroup.pushback(weatherItem);
					}
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
				}
			}
          
			((mainService)mContext).sendMessage(weatherItemGroup);
		}
	}
}
