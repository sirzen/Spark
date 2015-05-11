/*
 * Parsing XML file from baidu weather server.
 * This class must be used outside of UI thread.
 */

package com.example.btcontroller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.spark.hudsharedlib.MessageWeatherItem;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Xml;

public class WeatherXmlParser {
	private static final String ns = null;
	
	public static MessageWeatherItem parse(InputStream is) throws XmlPullParserException, IOException {
		// MessageWeatherItem wItem = new MessageWeatherItem();
		try {
			XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);
            parser.nextTag();

            return readWeather(parser);
		} finally {
			is.close();
		}
	}

	private static MessageWeatherItem readWeather(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "CityWeatherResponse");
		
		MessageWeatherItem wItem = new MessageWeatherItem();

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			String name = parser.getName();
			if (name.equals("results")) {
				// return (readEntries(parser));
				// should parse results, and then return.
				readEntries(parser, wItem);
				return wItem;
			} else if (name.equals("date")) {
				wItem.currentDate = readText(parser);
			} else {
				skip(parser);
			}
		}
		return null;
	}
	
	/*
	 * This function will return current weather. Should implement a new way to fetch all
	 * future weather forecast.
	 */
	private static void readEntries(XmlPullParser parser, MessageWeatherItem wItem) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "results");

		String cityName = null;
	    while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }
	        
	        String name = parser.getName();
	        if (name.equals("currentCity")) {
	        	cityName = readText(parser);
	        	wItem.cityName = cityName;
	        } else if (name.equals("weather_data") && (cityName != null)) {
	        	readWeatherData(parser, wItem);
	        } else if (name.equals("index")) {
	        	readIndex(parser, wItem);
	        } else if (name.equals("pm25")) {
	        	wItem.pm25 = readText(parser);
	        } else {
	        	skip(parser);
	        }
	    }
	}
	
	private static void readWeatherData (XmlPullParser parser, MessageWeatherItem wItem) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, "weather_data");
		MessageWeatherItem.WeatherDetail wDetailItem = new MessageWeatherItem.WeatherDetail();

	    while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }

	        String name = parser.getName();
	        if (name.equals("date")) {
	        	/*
	        	 * New entry starts. Insert a new data into element.
	        	 * This loop is stupid. Fix it.
	        	 */
	        	wDetailItem = new MessageWeatherItem.WeatherDetail();
	        	wDetailItem.date = readText(parser);
	        } else if (name.equals("weather")) {
	        	wDetailItem.weather = readText(parser);
	        } else if (name.equals("wind")) {
	        	wDetailItem.wind= readText(parser);
	        } else if (name.equals("temperature")) {
	        	wDetailItem.temperature = readText(parser);
	        	wItem.wDetail.add(wDetailItem);
	        	// return entry;
	        } else if (name.equals("dayPictureUrl")) {
	        	wDetailItem.dayPicUrl = readText(parser);
	        } else if (name.equals("nightPictureUrl")) {
	        	wDetailItem.nightPicUrl = readText(parser);
	        } else {
	        	skip(parser);
	        }
	    }
	}
	
	private static void readIndex (XmlPullParser parser, MessageWeatherItem wItem) throws IOException, XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, "index");
		MessageWeatherItem.IndexDetail wIndexItem = new MessageWeatherItem.IndexDetail();

	    while (parser.next() != XmlPullParser.END_TAG) {
	        if (parser.getEventType() != XmlPullParser.START_TAG) {
	            continue;
	        }

	        String name = parser.getName();
	        if (name.equals("title")) {
	        	/*
	        	 * New entry starts. Insert a new data into element.
	        	 */
	        	wIndexItem = new MessageWeatherItem.IndexDetail();
	        	wIndexItem.title = readText(parser);
	        } else if (name.equals("zs")) {
	        	wIndexItem.zs = readText(parser);
	        } else if (name.equals("tipt")) {
	        	wIndexItem.tipt= readText(parser);
	        } else if (name.equals("des")) {
	        	wIndexItem.des = readText(parser);
	        	wItem.iDetail.add(wIndexItem);
	        	// return entry;
	        } else {
	        	skip(parser);
	        }
	    }
	}

	private static String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
	    String result = "";
	    if (parser.next() == XmlPullParser.TEXT) {
	        result = parser.getText();
	        parser.nextTag();
	    }
	    return result;
	}
	
	private static void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
	    if (parser.getEventType() != XmlPullParser.START_TAG) {
	        throw new IllegalStateException();
	    }
	    int depth = 1;
	    while (depth != 0) {
	        switch (parser.next()) {
	        case XmlPullParser.END_TAG:
	            depth--;
	            break;
	        case XmlPullParser.START_TAG:
	            depth++;
	            break;
	        }
	    }
	 }
}
