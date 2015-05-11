package com.spark.hudsharedlib;

/*
 * This is the base class for all Bluetooth packages between ControlApp and DisplayApp.
 */

public class MessageBaseItem implements Cloneable {
	public static int TYPE_UNINITIALIZED = 0;
	public static int TYPE_WEATHER_INFO  = 1;
	public static int TYPE_WEATHER_GROUP = 100;
	public static int TYPE_MAP_INFO = 2;
	public static int TYPE_DRIVE_MODE = 3;
	public static int TYPE_MAP_MODE = 4;
	public static int TYPE_WEIBO_ITEM = 5;
	public static int TYPE_WEIBO_ITEMLIST = 6;
	public static int TYPE_SMS		 = 7;
	public static int TYPE_SMS_GROUP = 700;
	public static int TYPE_CALL      = 8;
	
	public static String UUID_String = "00001101-0000-1000-8000-00805F9B34FB";
	
	private int type;
	
	public MessageBaseItem(int t) {
		this.type = t;
	}
	
	public MessageBaseItem() {
		this(TYPE_UNINITIALIZED);
	}
	
	public int getType () {
		return this.type;
	}
	
	/*
	 * This shitty designed Clone interface causes EACH subclass NEEDS to explicitly
	 * implement this interface, ugh.
	 * @see java.lang.Object#clone()
	 */
	public MessageBaseItem clone() {
		return new MessageBaseItem(this.type);
	}
}
