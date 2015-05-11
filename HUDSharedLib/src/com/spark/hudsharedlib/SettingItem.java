package com.spark.hudsharedlib;

import java.io.Serializable;

public class SettingItem implements Serializable {
	/**
	 * Serial ID
	 */
	private static final long serialVersionUID = -5271069048333033887L;

	public enum Type {
		WEATHER,
		STOCK,
		WEIBO,
		MESSAGING,
		BTSETTING,
	};
	
	private Type type;
	private Boolean bEnabled;
	private String name;
	
	public SettingItem(Type t, String name) {
		this.type = t;
		this.bEnabled = false;
		this.name = name;
	}
	
	public SettingItem(Type t, String name, Boolean bEnabled) {
		this.type = t;
		this.bEnabled = bEnabled;
		this.name = name;
	}
	
	public void setEnabled (Boolean b) {
		this.bEnabled = b;
	}
	
	public Boolean getEnabled () {
		return bEnabled;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public String getName() {
		return this.name;
	}
}
