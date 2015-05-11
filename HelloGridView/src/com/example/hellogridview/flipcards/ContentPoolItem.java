package com.example.hellogridview.flipcards;

import java.util.LinkedList;
import android.app.Fragment;

public class ContentPoolItem {
	private boolean bPriority;
	private int id;
	protected int dispTime;
	public static final int POOL_ID_WEATHER = 1;
	public static final int POOL_ID_WEIBO = 2;
	public static final int POOL_ID_SMS = 3;
	public static final int POOL_ID_CALL = 4;

	public ContentPoolItem(int id) {
		this.bPriority = false;
		this.id = id;
		this.dispTime = 5; // default dispTime
	}

	public ContentPoolItem(int id, int dispTime) {
		this.bPriority = false;
		this.id = id;
		this.dispTime = dispTime;
	}

	// Any subclass must implement this function to return the
	// desired fragments it wishes to display.
	public LinkedList<DisplayPoolItem> convertToDispItem() {
		return null;
	}

	// convert to priority fragment:
	public LinkedList<DisplayPoolItem> convertToPriorityDispItem() {
		return null;
	}

	public boolean getPriority() {
		return this.bPriority;
	}

	public void setPriority(boolean p) {
		this.bPriority = p;
	}

	public int getId() {
		return this.id;
	}
}
