package com.spark.hudsharedlib;

import java.io.Serializable;
import java.util.LinkedList;

public class MessageWeatherItemGroup extends MessageBaseItem implements Serializable, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4495366822431573039L;
	LinkedList<MessageWeatherItem> mList;
	
	public MessageWeatherItemGroup () {
		super(MessageBaseItem.TYPE_WEATHER_GROUP);
		mList = new LinkedList<MessageWeatherItem> ();
	}
	
	public void pushback(MessageWeatherItem item) {
		mList.addLast(item);
	}
	
	public MessageWeatherItem poll () {
		return mList.pollFirst();
	}
	
	@SuppressWarnings("unchecked")
	private MessageWeatherItemGroup(LinkedList<MessageWeatherItem> i) {
		super(MessageBaseItem.TYPE_WEATHER_GROUP);
		this.mList = (LinkedList<MessageWeatherItem>) i.clone();
	}
	
	public MessageWeatherItemGroup clone() {
		MessageWeatherItemGroup grp = new MessageWeatherItemGroup(mList);
		return grp;
	}
}
