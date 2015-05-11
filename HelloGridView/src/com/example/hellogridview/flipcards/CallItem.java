package com.example.hellogridview.flipcards;

import java.util.LinkedList;

import com.spark.hudsharedlib.MessageCallItem;
import com.example.hellogridview.flipcards.CallCardFragment;

public class CallItem extends ContentPoolItem {
	MessageCallItem mCallItem;

	public CallItem(MessageCallItem m) {
		super(POOL_ID_CALL);
		mCallItem = m;
	}

	@Override
	// convert to priority fragment:
	public LinkedList<DisplayPoolItem> convertToPriorityDispItem() {
		CallCardFragment cFrag = new CallCardFragment(mCallItem);
		LinkedList<DisplayPoolItem> lDisp = new LinkedList<DisplayPoolItem>();
		lDisp.add(new DisplayPoolItem(cFrag, 100));
		return lDisp;
	}
}
