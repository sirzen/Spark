package com.example.btcontroller;

//import com.example.hellogridview.R;
import com.spark.hudsharedlib.GlobalSetting;
import com.spark.hudsharedlib.SettingItem;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class SettingsFragment extends Fragment {
	private BTAction mBTAction;
	FragmentChangeActivity mActivity;
	SettingWeibo  mSettingWeibo ; 
	SettingWeather mSettingWeather;
	SettingSMS  mSettingSMS;
	
	
	public SettingsFragment(BTAction bta) {mBTAction = bta;}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.setting_content_frame, container, false);
		mActivity = ((FragmentChangeActivity)getActivity());
		mSettingWeibo = (SettingWeibo)(mActivity.getSettingItem(SettingItem.Type.WEIBO));
		mSettingWeather = (SettingWeather)(mActivity.getSettingItem(SettingItem.Type.WEATHER));
		mSettingSMS = (SettingSMS)(mActivity.getSettingItem(SettingItem.Type.MESSAGING));
		
		boolean weather_enabled = mSettingWeather.getEnabled();
		final CheckBox cb_weather = (CheckBox)v.findViewById(R.id.setting_checkbox_weather);
		cb_weather.setChecked(mSettingWeather.getEnabled());
		
		cb_weather.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean bEnabled = !(mSettingWeather.getEnabled());
				mSettingWeather.setEnabled(bEnabled);
				cb_weather.setChecked(bEnabled);
				//mSettingWeather.setEnabled(bEnabled);
				mBTAction.doStartService();
			}
			
		});
/*
		boolean stock_enabled = mActivity.getSettingItem(SettingItem.Type.STOCK).getEnabled();
		final CheckBox cb_stock = (CheckBox)v.findViewById(R.id.setting_checkbox_weibo);
		cb_stock.setChecked(mGs.getItemByType(SettingItem.Type.STOCK).getEnabled());
		
		cb_stock.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean bEnabled = !(mActivity.getSettingItem(SettingItem.Type.STOCK).getEnabled());
				mGs.getItemByType(SettingItem.Type.STOCK).setEnabled(bEnabled);
				cb_stock.setChecked(bEnabled);
			}
		});
	
*/
		boolean msg_enabled = mSettingSMS.getEnabled();
		final CheckBox cb_msg = (CheckBox)v.findViewById(R.id.setting_checkbox_messaging);
		cb_msg.setChecked(mSettingSMS.getEnabled());
		
		cb_msg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean bEnabled = !(mSettingSMS.getEnabled());
				mSettingSMS.setEnabled(bEnabled);
				cb_msg.setChecked(bEnabled);
			}
		});
	
		boolean weibo_enabled = mSettingWeibo.getEnabled();
		final CheckBox cb_weibo = (CheckBox)v.findViewById(R.id.setting_checkbox_weibo);
		cb_weibo.setChecked(mSettingWeibo.getEnabled());
		
		cb_weibo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean bEnabled = !(mSettingWeibo.getEnabled());
				cb_weibo.setChecked(bEnabled);
				mSettingWeibo.setEnabled(bEnabled);
				mBTAction.doStartService();
			}
		});		
		
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		/*
		 * Test: hook up a onClick() to the first i.e. weather, to do a transition for updating current fragment -
		 * just use this same fragment.
		 */
		
		
		TextView tv = (TextView)getActivity().findViewById(R.id.setting_menu_text_weather);
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager ft = getActivity().getSupportFragmentManager();
				ft
				.beginTransaction()
				.addToBackStack(null)
				.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
				.replace(R.id.null_relative_layout, new SettingWeatherFragment(mBTAction))
				.commit();
			}
		});
		
		tv = (TextView)getActivity().findViewById(R.id.setting_menu_text_stock);
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager ft = getActivity().getSupportFragmentManager();
				ft
				.beginTransaction()
				.addToBackStack(null)
				.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
				.replace(R.id.null_relative_layout, new DefaultFragment())
				.commit();
			}
		});

		tv = (TextView)getActivity().findViewById(R.id.setting_menu_text_messaging);
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FragmentManager ft = getActivity().getSupportFragmentManager();
				ft
				.beginTransaction()
				.addToBackStack(null)
				.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
				.replace(R.id.null_relative_layout, new DefaultFragment())
				.commit();
			}
		});
		
		tv = (TextView)getActivity().findViewById(R.id.setting_menu_text_weibo);
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				FragmentManager ft = getActivity().getSupportFragmentManager();
				ft
				.beginTransaction()
				.addToBackStack(null)
				.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
				.replace(R.id.null_relative_layout, new SettingWeiboFragment(mBTAction), "weibo")
				.commit();
			}
		});
		
		tv = (TextView)getActivity().findViewById(R.id.setting_menu_text_bluetooth);
		tv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				FragmentManager ft = getActivity().getSupportFragmentManager();
				ft
				.beginTransaction()
				.addToBackStack(null)
				.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
				.replace(R.id.null_relative_layout, new SettingBTFragment(getActivity()))
				.commit();
			}
		});
	}
}
