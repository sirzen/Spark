package com.example.btcontroller;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.example.btcontroller.service.mainService;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.spark.hudsharedlib.MessageBaseItem;
import com.spark.hudsharedlib.SettingItem;

public class BTAction {
	private Context mContext;
	public mainService mService;
	private ServiceConnection mServConn =  new ServiceConnection () {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = ((mainService.LocalBinder)service).getService();		
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
		};
	};
	
	private boolean mIsBound;

	public BTAction(Context context) {
		// Get binder to mainService and
		// mService = ((mainService.LocalBinder)service).getService();
		mContext = context;
		mIsBound = false;
		doBindService();
	}
	
	public void doBindService () {
		Intent it = setBundle();
		mContext.bindService(it, mServConn, Context.BIND_AUTO_CREATE);
		mIsBound = true;
	}
	
	void doUnBindService () {
		if (mIsBound) {
			mContext.unbindService(mServConn);
			mIsBound = false;
		}
	}
	
	public void doStartService () {
		Intent it = setBundle();
		mContext.startService(it);
		mIsBound = true;
	}
	
	public void sendMessage(MessageBaseItem msg) {
		mService.sendMessage(msg);
	}

	private Intent setBundle() {
		Intent it = new Intent(mContext, mainService.class);
	
		Bundle bundleSetting = new Bundle();
		SettingBT     mSettingBT  = (SettingBT)((FragmentChangeActivity)mContext).getSettingItem(SettingItem.Type.BTSETTING);
		SettingWeibo  mSettingWeibo = (SettingWeibo)((FragmentChangeActivity)mContext).getSettingItem(SettingItem.Type.WEIBO);
		SettingWeather mSettingWeather = (SettingWeather)((FragmentChangeActivity)mContext).getSettingItem(SettingItem.Type.WEATHER);
		SettingSMS mSettingSMS = (SettingSMS)((FragmentChangeActivity)mContext).getSettingItem(SettingItem.Type.MESSAGING);
		
		bundleSetting.putSerializable("btsetting", mSettingBT);
		bundleSetting.putSerializable("weibosetting", mSettingWeibo);
		bundleSetting.putStringArrayList("citylistsetting", mSettingWeather.getCitiesList(mContext));
		bundleSetting.putBoolean("weatherEnabled", mSettingWeather.getEnabled());
		bundleSetting.putBoolean("smsEnabled", mSettingSMS.getEnabled());
		
		it.putExtras(bundleSetting);
		
		return it;
	}
	
}
