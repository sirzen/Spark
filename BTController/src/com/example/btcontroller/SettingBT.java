package com.example.btcontroller;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.spark.hudsharedlib.SettingItem;
import com.spark.hudsharedlib.SettingItem.Type;

public class SettingBT extends SettingItem {
	private String btAddress;
	private String btName;
	private static final long serialVersionUID = 5779254445528237559L;
	private static SharedPreferences pref ; 
	
	public SettingBT(Context context, String s, boolean bEnabled) {
		super(SettingItem.Type.BTSETTING, s, bEnabled);
		btAddress = null;
		btName = null;
		pref = context.getSharedPreferences(this.getName(), Context.MODE_PRIVATE);
	}
	
	public void setBTAddress(String s) {
		//btAddress = s;
		Editor editor = pref.edit();
        editor.clear();
        editor.putString("btAddress", s);
        editor.commit();		 				
	}
	
	public String getBTAddress() {
		
		String btAddress = pref.getString("btAddress", null);
	
		return btAddress;
	}

	public void setBTName(String name) {
		btName = name;
	}

	public String getBTName() {
		return btName;
	}
	

}
