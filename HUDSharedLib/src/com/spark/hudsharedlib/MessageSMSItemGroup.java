package com.spark.hudsharedlib;

import java.io.Serializable;
import java.util.LinkedList;

public class MessageSMSItemGroup extends MessageBaseItem implements Serializable, Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5169364339018452278L;
	LinkedList<MessageSMSItem> mList;
	
	public MessageSMSItemGroup () {
		super(MessageBaseItem.TYPE_SMS_GROUP);
		mList = new LinkedList<MessageSMSItem> ();
	}
	
	public void pushback(MessageSMSItem item) {
		mList.addLast(item);
	}
	
	public MessageSMSItem poll () {
		return mList.pollFirst();
	}
	
	@SuppressWarnings("unchecked")
	private MessageSMSItemGroup(LinkedList<MessageSMSItem> i) {
		super(MessageBaseItem.TYPE_SMS_GROUP);
		this.mList = (LinkedList<MessageSMSItem>) i.clone();
	}
	
	public MessageSMSItemGroup clone() {
		MessageSMSItemGroup grp = new MessageSMSItemGroup(mList);
		return grp;
	}
}
