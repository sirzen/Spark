package com.example.hellogridview.flipcards;

import java.util.LinkedList;
import java.util.Map;

import com.spark.hudsharedlib.MessageSMSItem;
import com.spark.hudsharedlib.MessageWeatherItem;
import com.spark.hudsharedlib.MessageWeatherItemGroup;
import com.spark.hudsharedlib.MessageWeiboItem;
import com.spark.hudsharedlib.MessageWeiboItemList;

import android.app.Fragment;

public class WeiboItem extends ContentPoolItem {
	// private LinkedList<MessageWeiboItem> mList;
	MessageWeiboItem msgWeiboItem;

	public WeiboItem(MessageWeiboItem item) {
		super(POOL_ID_WEIBO);
		msgWeiboItem = item;
		// mList = new LinkedList<MessageWeiboItem>();
	}

	/*
	 * public void insertItem(MessageWeiboItem weiboItem) {
	 * mList.add(weiboItem); }
	 * 
	 * public void clearItems() { mList.clear(); }
	 * 
	 * @Override public LinkedList<DisplayPoolItem> convertToDispItem () {
	 * LinkedList<DisplayPoolItem> fList = new LinkedList<DisplayPoolItem>();
	 * int position = 1; int total_num; LinkedList<MessageWeiboItem> tmpList =
	 * (LinkedList<MessageWeiboItem>)mList.clone(); total_num = tmpList.size();
	 * MessageWeiboItem item = tmpList.poll(); while (item != null) {
	 * WeiboCardFragment wcFrag = new WeiboCardFragment(item); fList.add(new
	 * DisplayPoolItem(wcFrag)); item = tmpList.poll(); } return fList; }
	 */

	@Override
	public LinkedList<DisplayPoolItem> convertToDispItem() {
		LinkedList<DisplayPoolItem> fList = new LinkedList<DisplayPoolItem>();
		WeiboCardFragment wcFrag = new WeiboCardFragment(msgWeiboItem);
		fList.add(new DisplayPoolItem(wcFrag));

		return fList;
	}

}
