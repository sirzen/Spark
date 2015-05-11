package com.spark.hudsharedlib;

import java.io.Serializable;

public class MessageCallItem extends MessageBaseItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7661707144142173441L;
	
	private String call_from;
	private String call_date;
	private String call_state;
	
	public MessageCallItem () {
		super(TYPE_CALL);
	}
	
	public MessageCallItem (String from, String date, String state) {
		super(TYPE_CALL);
		this.call_from = from;
		this.call_date = date;
		this.call_state = state;
	}
	
	public String getFrom() {
		return call_from;
	}
	
	public String getDate() {
		return call_date;
	}
	
	public String getState() {
		return call_state;
	}
	
}