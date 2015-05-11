package com.spark.hudsharedlib;

import java.io.Serializable;
import java.util.LinkedList;

public class MessageWeatherItem extends MessageBaseItem implements Serializable {
	/**
	 * 
	 */
	public static class WeatherDetail implements Serializable{
		public String date;
		public String weather;
		public String wind;
		public String temperature;
		public String dayPicUrl;
		public String nightPicUrl;
	}
	
	public static class IndexDetail implements Serializable {
		public String title;
		public String zs;
		public String tipt;
		public String des;
	}
	
	private static final long serialVersionUID = -4959577874200883166L;
	public String cityName;
	public String currentDate;
	public String pm25;
	public LinkedList<WeatherDetail> wDetail;
	public LinkedList<IndexDetail>   iDetail;
	
	public MessageWeatherItem() {
		super(MessageBaseItem.TYPE_WEATHER_INFO);
		wDetail = new LinkedList<WeatherDetail>();
		iDetail = new LinkedList<IndexDetail>();
	}
	
	/*
	public MessageWeatherItem(String cn, String cweather, String ct, String cd, String cwind, String wc) {
		cityName = cn;
		currentWeather = cweather;
		currentTemp = ct;
		currentDate = cd;
		currentWind = cwind;
		wearingCondition = wc;
	}
	*/
}
