package com.spark.hudsharedlib;

import java.io.Serializable;
import java.util.LinkedList;

public class MessageWeiboItemList extends MessageBaseItem implements Serializable,Cloneable {

	
	private static final long serialVersionUID = -2132112231244432132L;      

	LinkedList<MessageWeiboItem> mList;
	
    public MessageWeiboItemList() {
    	super(MessageBaseItem.TYPE_WEIBO_ITEMLIST);
    	mList = new LinkedList<MessageWeiboItem>();
    }

      
	public void pushback(MessageWeiboItem item) {
		//mMessageList.push(item);
		mList.addLast(item);
	}
	
	public MessageWeiboItem poll () {
		//return mMessageList.pop();
		
		return mList.pollFirst();
	}
	
	private MessageWeiboItemList(LinkedList<MessageWeiboItem> i) {
			super(MessageBaseItem.TYPE_WEIBO_ITEMLIST);
			this.mList = (LinkedList<MessageWeiboItem>) i.clone();
	}

	public MessageWeiboItemList clone() {
		MessageWeiboItemList mGrp = new MessageWeiboItemList(mList);
		return mGrp;
	}
	
	

    
}
