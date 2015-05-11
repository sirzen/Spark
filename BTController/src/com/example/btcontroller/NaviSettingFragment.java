package com.example.btcontroller;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.example.btcontroller.SettingWeatherFragment.SettingWeatherAdapter;
import com.spark.hudsharedlib.MessageDriveMode;
import com.spark.hudsharedlib.SettingItem;

@SuppressLint("ValidFragment")
public class NaviSettingFragment  extends Fragment implements OnClickListener,
				OnCheckedChangeListener {
	BTAction		  mBTAction;
	
	private ImageView mBackView;//返回按钮
	private RadioGroup mNaviSettingGroup;//黑夜模式白天模式
	private Button mSendButton;//返回按钮

	private boolean mSpeedFlag = true;
	private boolean mDistanceFlag = false;
	private boolean mTrafficFlag = false;
	private boolean mHighwayFlag = false;
	private boolean mFeeFlag = false;
	private boolean mHighwayFeeFlag = false;
	private boolean mHighwayTrafficFlag = false;
	private boolean mFeeTrafficFlag = false;
	private boolean mHighwayFeeTrafficFlag = false;

	private int mThemeStyle;
	protected MessageDriveMode driveMode;
	
	
	public NaviSettingFragment(BTAction bta) {
		mBTAction = bta;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.activity_navisetting, container, false);
		
		mBackView = (ImageView) rootView.findViewById(R.id.setting_back_image);
		mNaviSettingGroup = (RadioGroup) rootView.findViewById(R.id.navisetting);
		mSendButton = (Button) rootView.findViewById(R.id.sendnavisetting);
		initListener();
	
		if(driveMode==null) {
			driveMode = new MessageDriveMode();
			driveMode.setMode(0);
		}
		
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	/**
	 * 初始化监听事件
	 */
	private void initListener() {
		mBackView.setOnClickListener(this);
		mSendButton.setOnClickListener(this);		
		mNaviSettingGroup.setOnCheckedChangeListener(this);
				
	}


	// ------------------------------生命周期重写方法---------------------------

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		mSpeedFlag = false;
		mDistanceFlag = false;
		mTrafficFlag = false;
		mHighwayFlag = false;
		mFeeFlag = false;
		mHighwayFeeFlag = false;
		mHighwayTrafficFlag = false;
		mFeeTrafficFlag = false;
		mHighwayFeeTrafficFlag = false;
	
		
		switch (checkedId) {
		// 昼夜模式
		case R.id.speedfast:
			mSpeedFlag = true;
			driveMode.setMode(0);			
			break;
		case R.id.feeless:
			mFeeFlag = true;
			driveMode.setMode(1);
			break;
		case R.id.distanceshort:
			mDistanceFlag = true;
			driveMode.setMode(2);
			break;
		// 拥堵重算
		case R.id.nohighway:
			mHighwayFlag = true;
			driveMode.setMode(3);
			break;
			// 偏航重算
		case R.id.trafficbetter:
			mTrafficFlag = true;
			driveMode.setMode(4);
			break;
		case R.id.no_highway_fee:
			mHighwayFeeFlag = true;
			driveMode.setMode(5);
			break;
		// 交通播报
		case R.id.no_highway_traffic:
			mHighwayTrafficFlag = true;
			driveMode.setMode(6);
			break;
		case R.id.no_fee_traffic:
			mFeeTrafficFlag = true;
			driveMode.setMode(7);
			break;
		// 摄像头播报
		case R.id.no_highway_fee_traffic:
			mHighwayFeeTrafficFlag = true;
			driveMode.setMode(8);
		}
		
	}


	// 事件处理方法
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.setting_back_image:
			FragmentManager ft = getActivity().getSupportFragmentManager();
			ft
			.beginTransaction()
			.addToBackStack(null)
			.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
			.replace(R.id.null_relative_layout, new MapFragment(mBTAction))
			.commit();
			break;
		case R.id.sendnavisetting:
			mBTAction.sendMessage(driveMode);
			break;
		}

		}

		
	
}
