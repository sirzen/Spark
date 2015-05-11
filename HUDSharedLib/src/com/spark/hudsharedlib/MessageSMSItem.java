package com.spark.hudsharedlib;

import java.io.Serializable;

public class MessageSMSItem extends MessageBaseItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7661707144142173441L;
	
	private String msg_from;
	private String msg_date;
	private String msg_content;
	
	public MessageSMSItem () {
		super(TYPE_SMS);
	}
	
	public MessageSMSItem (String from, String date, String content) {
		super(TYPE_SMS);
		this.msg_from = from;
		this.msg_date = date;
		this.msg_content = content;
	}
	
	public String getFrom() {
		return msg_from;
	}
	
	public String getDate() {
		return msg_date;
	}
	
	public String getContent () {
		return msg_content;
	}
}
