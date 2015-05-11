package com.example.btcontroller.service;

import com.example.btcontroller.BTService;
import com.example.btcontroller.R;
import com.example.btcontroller.SettingBT;
import com.example.btcontroller.SettingWeather;
import com.example.btcontroller.SettingWeibo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.spark.hudsharedlib.GlobalSetting;
import com.spark.hudsharedlib.MessageBaseItem;

import java.util.ArrayList;
import java.util.Set;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;
import android.bluetooth.BluetoothDevice;

public class mainService extends Service {
	Handler mHandler;
	BTService mBTService;
	private SettingBT mSettingBT = null;
	private SettingWeibo mSettingWeibo = null; 
	private Boolean  mWeatherEnabled = false;
	private Boolean  mSMSEnabled = false;
	
	private ArrayList<String> mCityList = null;
	FetchWeiboThread mWeiboThread = null;
	FetchWeatherThread mWeatherThread = null;
	SmsListener smsListener = null;
	Boolean	mBtConnected;
	
	private final IBinder mBinder = new LocalBinder();
	
    public class LocalBinder extends Binder {
        public mainService getService() {
            return mainService.this;
        }
    }
	
	@Override
	public void onCreate() {
	    super.onCreate();
	    
	    mHandler = new Handler();
	    mBTService = new BTService(this);
        Toast.makeText(this, R.string.main_service_started, Toast.LENGTH_SHORT).show();
        /*mWeiboThread = null;
        mWeatherThread = null;
        smsListener = null;
        */
        mBtConnected = false;
        
        startListener();
	}

	private void startListener() {
		// Start SMS BroadcastListener
		/*	
	 	SmsListener smsListener = new SmsListener(mBTService);
		this.registerReceiver(smsListener, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
		Toast.makeText(this, getResources().getString(R.string.starting_sms_listener), Toast.LENGTH_SHORT)
	    .show();*/
		
		CallListener callListener = new CallListener(mBTService);
		this.registerReceiver(callListener, new IntentFilter("android.intent.action.PHONE_STATE"));
		Toast.makeText(this, getResources().getString(R.string.starting_call_listener), Toast.LENGTH_SHORT)
	    .show();
	}

	
	@Override 
	public int onStartCommand(Intent intent, int flags, int startId) {
	    // Toast.makeText(this, "mainService Starting", Toast.LENGTH_SHORT).show();
	    super.onStartCommand(intent,flags,startId);
	    getBundle(intent);	
	    
	    return START_STICKY;
	}

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mBTService.destroy();

        // Show toast to user to indicate we have terminated.
        // TODO: Remove all toast messages while displaying all status in
        // centralized control panel.
        Toast.makeText(this, R.string.main_service_stopped, Toast.LENGTH_SHORT).show();
    }
	
    
    
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		getBundle(intent);
		
		return mBinder;
	}
	
	public void sendMessage(MessageBaseItem msg) {
		mBTService.sendMessage(msg);
	}
	
	public Set<BluetoothDevice> getBluetoothDevices() {
		return mBTService.getPairedDevices();
	}
	
	public void setBTDevByMAC(String s) {
		mBtConnected = mBTService.pairSelectedDevice(s);
		//when bluetooth device is connected, start to fetch data
		if(mBtConnected==true) {
			startFetchData();
		}
	}

    private void getBundle(Intent intent) {
	    Bundle bundle = intent.getExtras();  
	    if(bundle != null) {
	    	mSettingBT = (SettingBT) bundle.getSerializable("btsetting");
	    	
	    	mSMSEnabled  =  bundle.getBoolean("smsenabled");
	    	
	    	mSettingWeibo = (SettingWeibo) bundle.getSerializable("weibosetting");
	    	mWeatherEnabled = bundle.getBoolean("weatherEnabled", false);
	    	mCityList = bundle.getStringArrayList("citylistsetting");
	    	mSMSEnabled = bundle.getBoolean("smsEnabled", false);
	    	
	    	if(mSettingBT.getBTAddress()!=null) {
	    		if(mBtConnected==false) {
	    			setBTDevByMAC(mSettingBT.getBTAddress());
	    		} else {
	    			startFetchData();
	    		}
	    	}
	    }
    }
	
	private void startFetchData() {
		if(smsListener==null) {
			smsListener = new SmsListener(mBTService);
			smsListener.setEnable(mSMSEnabled);
			this.registerReceiver(smsListener, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
			Toast.makeText(this, getResources().getString(R.string.starting_call_listener), Toast.LENGTH_SHORT)
		    .show();
		} else {
			smsListener.setEnable(mSMSEnabled);
		}		
		
    	if(mSettingWeibo!=null) { 
    		Oauth2AccessToken token = mSettingWeibo.readAccessToken();
    		if(mWeiboThread == null ) {
    			if(mSettingWeibo.getEnabled()==true && token != null) {
            		mWeiboThread = new FetchWeiboThread(this, true, token);
        			mWeiboThread.start();
    			}
    		} else {
    			mWeiboThread.settingUpdate(mSettingWeibo.getEnabled(), token);
    			if(mSettingWeibo.getEnabled()==false || token == null) {
    				mWeiboThread.interrupt();
    				mWeiboThread = null;
    			}
    		}
    	}

    	if(mWeatherEnabled==true && mCityList.isEmpty()==false) {
    		if(mWeatherThread==null) {	
    			mWeatherThread = new FetchWeatherThread(this, true, mCityList);
    			mWeatherThread.start();
    		} else {
    			mWeatherThread.settingUpdate(mWeatherEnabled, mCityList);
    		}
    	} else {
    		if(mWeatherThread!=null) {
	    		mWeatherThread.settingUpdate(mWeatherEnabled, mCityList);
	    		mWeatherThread.interrupt();
	    		mWeatherThread = null;
    		}
    	}
    	
	}
	
}
