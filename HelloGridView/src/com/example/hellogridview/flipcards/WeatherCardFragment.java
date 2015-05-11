package com.example.hellogridview.flipcards;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hellogridview.R;
import com.spark.hudsharedlib.MessageWeatherItem;
import com.spark.hudsharedlib.MessageWeatherItem.IndexDetail;
import com.spark.hudsharedlib.MessageWeatherItem.WeatherDetail;

@SuppressLint("ViewHolder")
public class WeatherCardFragment extends Fragment {
	MessageWeatherItem mItem;

	public WeatherCardFragment(MessageWeatherItem msgWeather) {
		mItem = msgWeather;
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		/*
		 * initialize and insert one sample element into mCities.
		 */
		View v = inflater.inflate(R.layout.flipcard_weather_item2, null);
		String date = parseDate(mItem.wDetail.peek().date);
		String realTimeTemp = parseRealTimeTemp(mItem.wDetail.peek().date);

		TextView tv = (TextView) v
				.findViewById(R.id.flipcard_weather_item_city_name);
		tv.setText(mItem.cityName);
		tv = (TextView) v.findViewById(R.id.flipcard_weather_item_date);
		tv.setText(date);
		tv = (TextView) v
				.findViewById(R.id.flipcard_weather_item_realtime_temp);
		tv.setText(realTimeTemp);
		tv = (TextView) v.findViewById(R.id.flipcard_weather_item_current_pm25);
		tv.setText("PM2.5值: " + mItem.pm25);
		tv = (TextView) v.findViewById(R.id.flipcard_weather_item_current_wind);
		tv.setText(mItem.wDetail.peek().wind);

		tv = (TextView) v
				.findViewById(R.id.flipcard_weather_item_current_weather);
		tv.setText(mItem.wDetail.peek().weather);
		tv = (TextView) v.findViewById(R.id.flipcard_weather_item_current_temp);
		tv.setText(mItem.wDetail.peek().temperature);

		ImageView iv = (ImageView) v
				.findViewById(R.id.flipcard_weather_item_weatherpic);
		iv.setImageResource(translateWeather(mItem.wDetail.peek().weather));
		// iv.setImageDrawable(getResources().getDrawable(R.drawable.clear_day));

		weatherItemAdapter wiAdapter = new weatherItemAdapter(getActivity(),
				R.layout.flipcard_weather_item_left_panel_item_layout,
				mItem.wDetail);
		GridView gv = (GridView) v
				.findViewById(R.id.flipcard_weather_item_left_forecast_item);
		gv.setAdapter(wiAdapter);

		indexItemAdapter iiAdapter = new indexItemAdapter(getActivity(),
				R.layout.flipcard_weather_item_right_panel_item_layout,
				mItem.iDetail);
		ListView lv = (ListView) v
				.findViewById(R.id.flipcard_weather_item_right_panel_item);
		lv.setAdapter(iiAdapter);
		return v;
	}

	// Wait till next year's Jan, see if format is 01月 or 1 月.
	private String parseDate(String in) {
		int idxOfMonth = in.indexOf('月');
		String date;
		idxOfMonth -= 2;
		if (in.charAt(idxOfMonth) == '1') {
			date = in.substring(idxOfMonth, idxOfMonth + 6);
		} else {
			date = in.substring(idxOfMonth + 1, idxOfMonth + 6);
		}

		return date;
	}

	// Weather: how about negative and single digit weather?
	private String parseRealTimeTemp(String in) {
		int idxOfShi = in.indexOf('实');
		if (idxOfShi == -1)
			return null;
		return in.substring(idxOfShi + 3, idxOfShi + 6);
	}

	// translates weather string to the matching image resource ID.
	private int translateWeather(String w) {
		if (w.length() >= 1) {
			if (w.charAt(0) == '晴') {
				return R.drawable.clear_day;
			} else if (w.charAt(0) == '阴') {
				return R.drawable.yin;
			}
		}

		if (w.length() >= 2) {
			if (w.charAt(1) == '雨') {
				return R.drawable.shower;
			} else if (w.substring(0, 2).equals("多云")) {
				return R.drawable.cloudy;
			}
		}

		return R.drawable.clear_day;
	}

	private class weatherItemAdapter extends
			ArrayAdapter<MessageWeatherItem.WeatherDetail> {
		Context mContext;
		List<WeatherDetail> mList;
		int mResource;

		public weatherItemAdapter(Context context, int resource,
				List<WeatherDetail> objects) {
			super(context, resource, objects);
			mList = objects;
			mResource = resource;
			mContext = context;
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			/*
			 * Try not inflate every time - can we just use convertView and use
			 * findViewById() from convertView() and return convertView
			 */
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflater.inflate(
					R.layout.flipcard_weather_item_left_panel_item_layout,
					parent, false);
			TextView tv = (TextView) v
					.findViewById(R.id.flipcard_weather_item_left_panel_item_weekday);
			tv.setText(mList.get(position).date.substring(0, 2));

			tv = (TextView) v
					.findViewById(R.id.flipcard_weather_item_left_panel_item_weather);
			tv.setText(mList.get(position).weather);
			tv = (TextView) v
					.findViewById(R.id.flipcard_weather_item_left_panel_item_temp);
			tv.setText(mList.get(position).temperature);
			tv = (TextView) v
					.findViewById(R.id.flipcard_weather_item_left_panel_item_wind);
			tv.setText(mList.get(position).wind);

			ImageView iv = (ImageView) v
					.findViewById(R.id.flipcard_weather_item_left_panel_item_weatherpic);
			iv.setImageResource(translateWeather(mList.get(position).weather));
			return v;
		}
	}

	private class indexItemAdapter extends
			ArrayAdapter<MessageWeatherItem.IndexDetail> {
		Context mContext;
		List<IndexDetail> mList;
		int mResource;

		public indexItemAdapter(Context context, int resource,
				List<IndexDetail> objects) {
			super(context, resource, objects);

			mContext = context;
			mList = objects;
			mResource = resource;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			/*
			 * Try not inflate every time - can we just use convertView and use
			 * findViewById() from convertView() and return convertView
			 */
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View v = inflater.inflate(
					R.layout.flipcard_weather_item_right_panel_item_layout,
					parent, false);

			TextView tv = (TextView) v
					.findViewById(R.id.flipcard_weather_item_right_panel_item_tipt);
			tv.setText(mList.get(position).tipt);
			tv = (TextView) v
					.findViewById(R.id.flipcard_weather_item_right_panel_item_des);
			tv.setText(mList.get(position).des);

			return v;
		}
	}
}
