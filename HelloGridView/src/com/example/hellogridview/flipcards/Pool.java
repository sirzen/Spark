package com.example.hellogridview.flipcards;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;
import android.annotation.SuppressLint;
import android.app.Fragment;

/*
 * The Pool is the centralized place for receiving and displaying fragments.
 * Each fragment to be displayed should extend a ContentPoolItem type and implement
 * its convertToFragment() interface.
 * The DisplayPool stores all fragments to be displayed.
 * The ContentPool stores all elements that could be displayed - it is DisplayPool's
 * responsibility to query for ContentPool and fetch whatever it wishes to display.
 */
@SuppressLint("UseSparseArrays")
public class Pool {
	private class DisplayPool {
		LinkedList<DisplayPoolItem> mDispItems;
		ReentrantLock mFragLock;
		int threshold;

		public DisplayPool() {
			mDispItems = new LinkedList<DisplayPoolItem>();
			mFragLock = new ReentrantLock();
			threshold = 2;
		}

		public void addDItemToFront(DisplayPoolItem dItem) {
			// This lock is causing a deadlock - fix it.
			mFragLock.lock();
			mDispItems.addFirst(dItem);
			mFragLock.unlock();
		}

		/*
		 * This function returns head of the linked list. Should grab the linked
		 * list's lock and do not block on this call. If the list is empty,
		 * return null.
		 */
		public DisplayPoolItem poll() {
			mFragLock.lock();
			/*
			 * Query contentPool for more Fragments, if current dPool's fragment
			 * number is below threshold.
			 */
			// if (mDispItems.size() <= threshold) {
			/*
			 * WTF? JDK does not support constant time concatenation of linked
			 * list???
			 */
			// LinkedList<DisplayPoolItem> newList = cPool.getFragList();
			// for (DisplayPoolItem f2 : newList) {
			// mDispItems.add(f2);
			// }
			// }

			if (mDispItems.peek() == null) {
				mFragLock.unlock();
				return null;
			}

			DisplayPoolItem dItem = mDispItems.pollFirst(); // pop and remove
															// the first element

			mFragLock.unlock();

			return dItem;
		}

		public void insertItem(LinkedList<DisplayPoolItem> fList) {
			// This lock is causing a deadlock - fix it.
			mFragLock.lock();
			for (DisplayPoolItem f2 : fList) {
				mDispItems.add(f2);
			}
			mFragLock.unlock();
		}
	}

	private class ContentPool {
		Map<Integer, ContentPoolItem> mMap;

		public ContentPool() {
			//mMap = new HashMap<Integer, ContentPoolItem>();
			/*
			 * Create empty ContentPoolItem for each supported PoolItem.
			 * Currently, the SMS needs initialization, since every time we
			 * receive a single SMS message from ControlApp. We need an empty
			 * container to receive SMS.
			 */
			//mMap.put(ContentPoolItem.POOL_ID_SMS, new SMSItem());
			// mMap.put(ContentPoolItem.POOL_ID_WEIBO, new WeiboItem());
		}

		public void insertItem(ContentPoolItem i) {
			// -mMap.put(i.getId(), i);
			dPool.insertItem(i.convertToDispItem());
		}

		/*
		 * public LinkedList<DisplayPoolItem> getFragList() {
		 * LinkedList<DisplayPoolItem> retList = new
		 * LinkedList<DisplayPoolItem>(); LinkedList<DisplayPoolItem> lFrag =
		 * new LinkedList<DisplayPoolItem>(); Iterator<Entry<Integer,
		 * ContentPoolItem>> it = mMap.entrySet().iterator(); while
		 * (it.hasNext()) { //-Map.Entry pairs = (Map.Entry)it.next(); Map.Entry
		 * pairs = (Map.Entry)it.next(); lFrag =
		 * ((ContentPoolItem)pairs.getValue()).convertToDispItem(); if (lFrag !=
		 * null) { for (DisplayPoolItem dItem : lFrag) { retList.add(dItem); } }
		 * }
		 * 
		 * return retList; }
		 */
		public ContentPoolItem getItem(Integer i) {
			return mMap.get(i);
		}
	}

	private ContentPool cPool;
	private DisplayPool dPool;

	public Pool() {
		cPool = new ContentPool();
		dPool = new DisplayPool();
	}

	public void insertContentPoolItem(ContentPoolItem i) {
		/*
		 * This operation will replace the older item and this IS expected.
		 */
		// change not to use contentPool to store objects

		// -cPool.insertItem(i);

		/*
		 * Additional handing if this item has a bPriority set - in this case,
		 * construct fragments from this item and put it into displayPool
		 * immediately.
		 */
		if (i.getPriority()) {
			LinkedList<DisplayPoolItem> fList = i.convertToPriorityDispItem();
			if (fList != null) {
				for (DisplayPoolItem f : fList) {
					dPool.addDItemToFront(f);
				}
			}
		} else {
			dPool.insertItem(i.convertToDispItem());
		}
	}

	/*
	 * The input integer is the key defined in ContentPoolItem.java
	 */
	public ContentPoolItem getContentPoolItem(Integer i) {
		return cPool.getItem(i);
	}

	/*
	 * public Fragment getDispFragment () { return dPool.poll(); }
	 * 
	 * public int peekNextDelay() { return dPool.peekNextDelay(); }
	 */

	public DisplayPoolItem getNextDispItem() {
		return dPool.poll();
	}

}
