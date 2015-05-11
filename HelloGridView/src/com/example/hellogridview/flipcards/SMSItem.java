package com.example.hellogridview.flipcards;

import java.util.LinkedList;

import com.spark.hudsharedlib.MessageSMSItem;

public class SMSItem extends ContentPoolItem {
//	private LinkedList<MessageSMSItem> msgList;
	private MessageSMSItem mSMSItem; 
	private int position = 1;
	
/*	public SMSItem() {
		super(POOL_ID_SMS);
		msgList = new LinkedList<MessageSMSItem>();
	}
*/
	public SMSItem(MessageSMSItem smsItem) {
		super(POOL_ID_SMS);
		mSMSItem = smsItem;
	}
	
	public LinkedList<DisplayPoolItem> convertToDispItem() {
		LinkedList<DisplayPoolItem> fList = new LinkedList<DisplayPoolItem>();
		SMSCardFragment sFrag = new SMSCardFragment(mSMSItem, position++, position);
		fList.add(new DisplayPoolItem(sFrag));
		return fList;
	}
	
	/*
	public void insertItem(MessageSMSItem smsItem) {
		msgList.add(smsItem);
	}

	public void clearItems() {
		msgList.clear();
	}

	@Override
	public LinkedList<DisplayPoolItem> convertToDispItem() {
		LinkedList<DisplayPoolItem> fList = new LinkedList<DisplayPoolItem>();
		int position = 1;
		int total_num;
		LinkedList<MessageSMSItem> tmpList = (LinkedList<MessageSMSItem>) msgList
				.clone();
		total_num = tmpList.size();
		MessageSMSItem item = tmpList.poll();
		while (item != null) {
			SMSCardFragment sFrag = new SMSCardFragment(item, position++,
					total_num);
			fList.add(new DisplayPoolItem(sFrag));
			item = tmpList.poll();
		}
		return fList;
	}*/
	
	
}
