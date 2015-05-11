package com.example.btcontroller;

import com.spark.hudsharedlib.SettingItem;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SettingSMS extends SettingItem {
	private static final long serialVersionUID = 1611472112432344505L;
	private static String enable_key = "ENABLE"; 
    SharedPreferences pref ; 
    
	public SettingSMS(Context context, String s, boolean bEnabled) {
		super(SettingItem.Type.MESSAGING, s, bEnabled);
	    pref = context.getSharedPreferences(this.getName(), Context.MODE_PRIVATE);
	}

	public void setEnabled (Boolean b) {
		Editor editor = pref.edit();
        editor.clear();
	    editor.putBoolean(enable_key, b);
	    
        editor.commit();		 		
	}
	
	public Boolean getEnabled() {
		Boolean benabled = pref.getBoolean(enable_key, false);
        return benabled;
	}
    
}

