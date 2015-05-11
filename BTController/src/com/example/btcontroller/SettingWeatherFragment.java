package com.example.btcontroller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParserException;

import com.spark.hudsharedlib.MessageWeatherItem;
import com.spark.hudsharedlib.MessageWeatherItemGroup;
import com.spark.hudsharedlib.SettingItem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
public class SettingWeatherFragment extends Fragment  {
	ArrayList<String> mCities;
	BTAction		  mBTAction;
	SettingWeather 	  sw;
	
	public SettingWeatherFragment(BTAction bta) {
		mBTAction = bta;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		/*
		 * initialize and insert one sample element into mCities.
		 */
		sw = (SettingWeather) ((FragmentChangeActivity)getActivity()).getSettingItem(SettingItem.Type.WEATHER);
		mCities = sw.getCitiesList(getActivity());
		
		if (mCities == null) {
			mCities = new ArrayList<String> ();
		}
		return inflater.inflate(R.layout.setting_menu_weather, null);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		SettingWeather sw = (SettingWeather) ((FragmentChangeActivity)getActivity()).getSettingItem(SettingItem.Type.WEATHER);
		//sw.setCitiesList(getActivity(), mCities);
		/*
		 * Should implement an interface to send new settings to display.
		 */
		//fetchWeatherDataAndUpdate(mCities);
		//mBTAction.doStartService();
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		final SettingWeatherAdapter adapter = new SettingWeatherAdapter(getActivity());
		adapter.addGroup(mCities);
		ListView lv = (ListView) getActivity().findViewById(R.id.setting_menu_weather_city_listview);
		lv.setAdapter(adapter);
		lv.setClickable(true);
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> p, View child, final int pos, long id){
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setMessage("你确定删除此城市吗？");
				builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						mCities.remove(pos);
						adapter.addGroup(mCities);
						sw.setCitiesList(getActivity(), mCities);
						mBTAction.doStartService();
					}
				});
				builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {

					}
				});
			    AlertDialog ad = builder.create();
			    ad.show();
			}
		});

		Button b = (Button)getActivity().findViewById(R.id.setting_menu_weather_city_add_city);
		b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
			    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			    LayoutInflater inflater = getActivity().getLayoutInflater();
			    View dialogView = inflater.inflate(R.layout.setting_weather_city_add_dialog, null);
                final EditText et = (EditText) dialogView.findViewById(R.id.setting_weather_city_add_dialog_text);

			    // Inflate and set the layout for the dialog
			    // Pass null as the parent view because its going in the dialog layout
			    builder.setView(dialogView)
			    	   .setTitle(R.string.setting_weather_city_add_dialog_title)
			    	   // Add action buttons
			           .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
			               @Override
			               public void onClick(DialogInterface dialog, int id) {
			                   mCities.add(et.getText().toString());
			                   adapter.addGroup(mCities);
			                   sw.setCitiesList(getActivity(), mCities);
			                   mBTAction.doStartService();
			               }
			           })
			           .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			               public void onClick(DialogInterface dialog, int id) {
			               }
			           });
			    AlertDialog ad = builder.create();
			    ad.show();
			}
		});
	}
	
	private class SettingWeatherItem {
		public String tag;
		public SettingWeatherItem(String tag) {
			this.tag = tag; 
		}
	}
	
	public List<String> getCitiesList() {
		return mCities;
	}

	public class SettingWeatherAdapter extends ArrayAdapter<SettingWeatherItem> {
		public SettingWeatherAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.setting_weather_city_list, null);
			}
			TextView title = (TextView) convertView.findViewById(R.id.setting_weather_city_name);
			title.setText(getItem(position).tag);

			return convertView;
		}
		
		public void addGroup(List<String> mCities) {
			this.clear();
			int sz = mCities.size();
			for (int i = 0; i < sz; i++) {
				this.add(new SettingWeatherItem(mCities.get(i)));
			}
			this.notifyDataSetChanged();
		}
	}
	

}
