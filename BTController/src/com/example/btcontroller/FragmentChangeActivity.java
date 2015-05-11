package com.example.btcontroller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;






import com.example.btcontroller.service.CallListener;
import com.example.btcontroller.service.SmsListener;
import com.example.btcontroller.service.mainService;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.spark.hudsharedlib.GlobalSetting;
import com.spark.hudsharedlib.MessageBaseItem;
import com.spark.hudsharedlib.SettingItem;
import com.spark.hudsharedlib.SettingItem.Type;

public class FragmentChangeActivity extends BaseActivity {
	private Fragment mContent;
	private BTAction mBTAction;
	private Fragment mLeftFragment;
	private GlobalSetting mGs;
	
	public boolean mWeiboSetting = false;
	
	/*
	 * Probably we could use another SlidingMenu here to contain all
	 * sub-set sliding menu settings, instead of using the getSlidingMenu()
	 * returned settings. This is definitely a cleaner way to handle things.
	 */	
	public FragmentChangeActivity() {
		super(R.string.changing_fragments);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*
		 * Keep in mind, that in future the activity will have no info about bluetooth
		 * connection info. So, at this point, need to check if service is running, and
		 * if not, initialize service.
		 * Service should be responsible to register callback.
		 */
		mGs = new GlobalSetting();
		SettingBT sbt = new SettingBT(this, getResources().getString(R.string.setting_menu_bluetooth), true);
		SettingWeather sw = new SettingWeather(this, getResources().getString(R.string.setting_menu_weather), true);
		SettingWeibo sweibo = new SettingWeibo(this, getResources().getString(R.string.setting_menu_weibo), true);
		SettingSMS  ssms = new SettingSMS(this, getResources().getString(R.string.setting_menu_messaging), true);
		
		mGs.insertItem(sbt);
		mGs.insertItem(sw);
		mGs.insertItem(sweibo);
		mGs.insertItem(ssms);
		
		mBTAction = new BTAction(this);
		
		//record the setting parameters in mSettingParams
		//SettingWeibo  mSettingWeibo = (SettingWeibo)this.getSettingItem(SettingItem.Type.WEIBO);
		//Boolean weibo_enabled = ;
		//Oauth2AccessToken weibo_token = mSettingWeibo.readAccessToken(mContext);
	    //String weibo_token_uid    = weibo_token.getUid();
	    //String weibo_token_value  = weibo_token.getToken();
	    //long weibo_token_expire = weibo_token.getExpiresTime();

		//mSettingParams.SetWeiboBundle(enabled, uid, value, expire);
		
		/*Intent it = new Intent(this, mainService.class);
		Bundle bundleSetting = new Bundle();
		bundleSetting.putSerializable("setting", mSettingWeibo);
		it.putExtras(bundleSetting);
		*/
		//mBTAction.doBindService();

		// set the Above View
		if (savedInstanceState != null)
			mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
		if (mContent == null)
			mContent = new SettingsFragment(mBTAction);

		setContentView(R.layout.null_relative_layout);
		setBehindContentView(R.layout.menu_frame);
		mLeftFragment = new LeftPanelFragment(mBTAction);

		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.null_relative_layout, mContent)
		.replace(R.id.menu_frame, mLeftFragment)
		.commit();

		// customize the SlidingMenu
		getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

		// Start the mainService
		// Intent i = new Intent(FragmentChangeActivity.this, mainService.class);
		// startService(i);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}
	
	public void switchContent(Fragment fragment) {
		/*
		 * Give a frame, and switch to it, using it as above view.
		 */
		mContent = fragment;
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.null_relative_layout, fragment)
		.commit();
		getSlidingMenu().showContent();
	}
	
	public void switchContent(Fragment startFragment, Fragment endFragment) {
		// 1. push current fragment into fragment stack
		//    currently, let's not consider this.
		// 2. configure SlidingMenu() to a. set menu fragment, b. set content fragment
		// 3. trigger the animation.
		getSupportFragmentManager()
		.beginTransaction()
		/*
		 * how to do this? What's the difference between mLeftFragment and startFragment here.
		 * Do a comparison between mContent and startFragment here.
		 */
		// .replace(R.id.menu_frame, startFragment)
		.replace(R.id.null_relative_layout, endFragment)
		.commit();

		getSlidingMenu().showContent();
	}
	
	public SettingItem getSettingItem (Type t) {
		return mGs.getItemByType(t);
	}
	
	public void insertSettingItem(SettingItem si) {
		mGs.insertItem(si);
	}
	
	public GlobalSetting getGlobalSetting() {
		return mGs;
	}
	
	public void sendMessage(MessageBaseItem msg) {
		this.mBTAction.sendMessage(msg);
	}
	
	public boolean setWeiboSetting(boolean value) {
		return mWeiboSetting = value;
	}
	
	@SuppressLint("NewApi")
	@Override 
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
		super.onActivityResult(requestCode, resultCode, data); 
		
		if(mWeiboSetting) {
			Fragment frag_weibo = getSupportFragmentManager().findFragmentByTag("weibo");
			
			frag_weibo.onActivityResult(requestCode, resultCode, data);
		}
	}
	
}
