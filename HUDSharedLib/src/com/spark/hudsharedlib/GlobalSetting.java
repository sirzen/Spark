package com.spark.hudsharedlib;

import java.io.Serializable;
import java.util.HashMap;
import java.util.TreeMap;
/*
 * Setting file. Controls all global settings.
 * 
 * Each Setting option will implement a SettingItem class.
 * 
 * Currently, support only floating pad's setting.
 * Floating pad will support:
 * 1. Weather - local weather
 * 2. Stock
 * --- Following will be added later.
 * 3. System Unread Message
 * 4. System Unread Email
 * 5. Unanswered Call 
 * 
 * The original implement will expose user with multi-select checkbox.
 * Will set TRUE if selected and display it on floating pad area.
 */
public class GlobalSetting extends BTObject implements Serializable {
	/**
	 * Generated Serial ID.
	 */
	private static final long serialVersionUID = -3006291639603182399L;
	public final static String UUID_String = "00001101-0000-1000-8000-00805F9B34FB";
	/*
	 * Once weather is selected, query Weather server for a list of available cities, use 
	 * a new activity to handle it.
	 * Same applies to Stock.
	 */
	private TreeMap<SettingItem.Type, SettingItem> items;
	
	public GlobalSetting() {
		items = new TreeMap<SettingItem.Type, SettingItem>();
		super.mObjectType = OBJECT_TYPE.SETTING;
	}
	
	public void insertItem(SettingItem mi) {
		if (items.get(mi.getType()) != null) {
			// Trigger action when duplicated entry is inserted
			// By default, will replace the old entry - old entry will be available for garbage collection.
		}
		items.put(mi.getType(), mi);
	}
	
	public void setItems(TreeMap<SettingItem.Type, SettingItem> hm) {
		items = hm;
	}
	
	public TreeMap<SettingItem.Type, SettingItem> getItems() {
		return items;
	}
	
	public SettingItem getItemByType(SettingItem.Type t) {
		return items.get(t);
	}
	
	public int size() {
		return items.size();
	}
}
