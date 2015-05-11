package com.example.btcontroller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.spark.hudsharedlib.SettingItem;

public class SettingWeather extends SettingItem {
	private static final long serialVersionUID = 1611476629775304505L;
	private ArrayList<String> mCitiesList;
	private static String enable_key = "ENABLE"; 
    SharedPreferences pref ; 
	
	public SettingWeather(Context context, String s, boolean bEnabled) {
		super(SettingItem.Type.WEATHER, s, bEnabled);
		mCitiesList = new ArrayList<String>();
	    pref = context.getSharedPreferences(this.getName(), Context.MODE_PRIVATE);
	}


	@SuppressLint("NewApi")
	public void setEnabled (Boolean b) {
        Set<String> set = pref.getStringSet(this.getName(), null);
		
		Editor editor = pref.edit();
        editor.clear();
	    editor.putStringSet(this.getName(), set);
	    editor.putBoolean(enable_key, b);
	    
        editor.commit();		 		
	}
	
	public Boolean getEnabled() {
		Boolean benabled = pref.getBoolean(enable_key, false);
        return benabled;
	}
        
	@SuppressLint("NewApi")
	public ArrayList<String> getCitiesList(Context context) {
        if (null == context) {
            return null;
        }
        
        Set<String> set = pref.getStringSet(this.getName(), null);
        if(set==null) {
        	mCitiesList = null;
        } else {
        	mCitiesList = new ArrayList<String>(set);
        }
        return mCitiesList;		
		
	}
	
	@SuppressLint("NewApi")
	public void setCitiesList(Context context, ArrayList<String> citiesList) {
        if (null == context || null == citiesList) {
            return;
        }
        Boolean benabled = pref.getBoolean(enable_key, false);
        
        Editor editor = pref.edit();
        editor.clear();
	    //Set the values
	    Set<String> set = new HashSet<String>();
	    set.addAll(citiesList);
	    editor.putStringSet(this.getName(), set);
	    editor.putBoolean(enable_key, benabled);
	    
        editor.commit();		
	}
  	
}
