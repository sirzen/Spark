package com.example.hellogridview;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.example.hellogridview.flipcards.CallItem;
import com.example.hellogridview.flipcards.ContentPoolItem;
import com.example.hellogridview.flipcards.DisplayPoolItem;
import com.example.hellogridview.flipcards.Pool;
import com.example.hellogridview.flipcards.SMSItem;
import com.example.hellogridview.flipcards.WeatherCardFragment;
import com.example.hellogridview.flipcards.WeatherItem;
import com.example.hellogridview.flipcards.WeiboItem;
import com.example.hellogridview.service.AckService;
import com.spark.hudsharedlib.MessageBaseItem;
import com.spark.hudsharedlib.MessageCallItem;
import com.spark.hudsharedlib.MessageDriveMode;
import com.spark.hudsharedlib.MessageSMSItem;
import com.spark.hudsharedlib.MessageSMSItemGroup;
import com.spark.hudsharedlib.MessageWeatherItem;
import com.spark.hudsharedlib.MessageWeatherItemGroup;
import com.spark.hudsharedlib.MessageWeiboItem;
import com.spark.hudsharedlib.MessageWeiboItemList;
import com.spark.hudsharedlib.NaviPoint;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.WindowManager;

public class FlipCardActivity extends Activity {
	private BTAction mBtAction;
	private boolean mMapStarted = false;
	private int mDriveMode;
	private Pool mPool;
	private TimerThread mTimerThread;

	/*
	 * This is the fragments to be displayed.
	 */
	private ConcurrentLinkedQueue<Fragment> mFragmentQueue;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.flipcard_loading);
		mDriveMode = 0;
		mBtAction = new BTAction(this);

		mPool = new Pool();

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		enterImmersive();

		/*
		 * Create a Thread running on background polling mBTAction's message
		 * queue, parsing for weather message for now - in future, should create
		 * a dispatch function to dispatch messages to their individual queue.
		 */
		DispatchThread dThread = new DispatchThread();
		dThread.start();

		/*
		 * Create a for loop checking if new items is available in the
		 * mFragmentQueue. If yes, delay for 5 seconds, and then replace current
		 * fragment.
		 */
		mTimerThread = new TimerThread();
		mTimerThread.start();
	}

	/*
	 * switches current fragment to next fragment designated by f.
	 */
	public void switchFragment(Fragment f) {
		FragmentManager ft = this.getFragmentManager();
		ft.beginTransaction()
				.setCustomAnimations(R.anim.enter_from_right,
						R.anim.exit_to_left, R.anim.enter_from_left,
						R.anim.exit_to_right)
				.replace(R.id.flipcard_relative_layout, f)
				.commitAllowingStateLoss();
	}

	private class DispatchThread extends Thread {
		@Override
		public void run() {
			MessageBaseItem item = null;
			/*
			 * Read mBTAction's queue, and construct Fragments into the
			 * mFragmentQueue. In the UI thread, will try to update to next
			 * fragment, if the top of queue is not empty i.e. poll() does not
			 * return NULL.
			 */
			while (true) {
				item = (MessageBaseItem) mBtAction.readObject();
				if (item != null) {
					if (item instanceof MessageWeatherItemGroup) {
						/*
						 * Yanhong, you shouldn't finish activity here. You
						 * should finish when it is going to display. Here it
						 * only puts fragment into queue but does not display
						 * it.
						 */
						if (mMapStarted) {
							finishActivity(2);
						}
						MessageWeatherItemGroup wGroup = (MessageWeatherItemGroup) item;
						WeatherItem weatherItem = new WeatherItem(wGroup);
						// weatherItem.setPriority(true);
						mPool.insertContentPoolItem(weatherItem);
					} else if (item instanceof NaviPoint) {
						startNavi((NaviPoint) item);
					} else if (item instanceof MessageDriveMode) {
						MessageDriveMode driveModeMessage = (MessageDriveMode) item;
						mDriveMode = driveModeMessage.getMode();
					} else if (item instanceof MessageWeiboItem) {
						MessageWeiboItem weiboMsg = (MessageWeiboItem) item;
						WeiboItem wbitem = new WeiboItem(weiboMsg);
						mPool.insertContentPoolItem(wbitem);

						// WeiboItem wbitem = (WeiboItem)
						// mPool.getContentPoolItem(ContentPoolItem.POOL_ID_WEIBO);
						// wbitem.insertItem(weiboMsg);
						// wbitem.setPriority(true);
						// mPool.insertContentPoolItem (wbitem);
					} else if (item instanceof MessageSMSItem) {
						MessageSMSItem msgSMSItem = (MessageSMSItem) item;
//						SMSItem smsItem = (SMSItem) mPool
	//							.getContentPoolItem(ContentPoolItem.POOL_ID_SMS);
		//				smsItem.insertItem(msgSMSItem);
						SMSItem smsItem = new SMSItem(msgSMSItem);
						mPool.insertContentPoolItem(smsItem);
						
					} else if (item instanceof MessageCallItem) {
						MessageCallItem msgCallItem = (MessageCallItem) item;
						/*
						 * To handle high priority item and trigger an instant
						 * display, need to send an interrupt to the Timer
						 * Thread to terminate it's current sleep and do a
						 * refresh instantly.
						 */
						if (TelephonyManager.EXTRA_STATE_RINGING
								.equals(msgCallItem.getState())) {
							/*
							 * New call is coming - construct a single call
							 * Fragment and set priority bit to TRUE and insert
							 * it.
							 */
							CallItem cItem = new CallItem(msgCallItem);
							cItem.setPriority(true);
							mPool.insertContentPoolItem(cItem);
						}
						mTimerThread.interrupt();
					}
				}
			}
		}
	}

	void startNavi(NaviPoint dstpoint) {
		Intent intent = new Intent("android.intent.action.VIEW",
				android.net.Uri.parse("androidamap://navi?sourceApplication="
						+ getApplicationName() + "&poiname="
						+ dstpoint.getTitle() + "&lat="
						+ dstpoint.getLatitude() + "&lon="
						+ dstpoint.getLongitute() + "&dev=1"
						+ "&style=mDriveMode"));
		intent.setPackage("com.autonavi.minimap");
		// startActivity(intent);
		// assign a request code of 2 to navigation activity
		startActivityForResult(intent, 2);
		mMapStarted = true;
	}

	/**
	 * 获取当前app的应用名字
	 */
	public String getApplicationName() {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = getApplicationContext().getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(
					getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName = (String) packageManager
				.getApplicationLabel(applicationInfo);
		return applicationName;
	}

	private class TimerThread extends Thread {
		DisplayPoolItem dItem = null;
		@Override
		public void run() {
			while (true) {
				dItem = mPool.getNextDispItem();
				if (dItem == null) {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}

				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (dItem != null) {
							switchFragment(dItem.mFragment);
						}
					}
				});

				try {
					// Thread.sleep(milliseconds);
					Thread.sleep(dItem.dispTime * 1000);
					/*
					 * Delay current Thread by current fragment's delayTime.
					 */
				} catch (InterruptedException e) {
					/*
					 * Here, we captured a Interrupt requested from upper level,
					 * meaning a higher priority message has been sent to the
					 * DisplayApp (for example, a MessageCallItem is received
					 * and need to display fragment asap. The caller is
					 * responsible to set next fragment ready in the Pool, so we
					 * just need to start a fresh run of this while loop;
					 */
					continue;
				}
			}
		}
	}

	/*
	 * Need to have a global enterImmersive() for all activities.
	 */
	@SuppressLint("InlinedApi")
	private void enterImmersive() {
		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_FULLSCREEN
						| View.SYSTEM_UI_FLAG_IMMERSIVE
						| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
	}

}
