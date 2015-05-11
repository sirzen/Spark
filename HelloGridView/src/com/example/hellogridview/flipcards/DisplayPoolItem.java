package com.example.hellogridview.flipcards;

import android.app.Fragment;

public class DisplayPoolItem {
	public Fragment mFragment;
	public int dispTime;

	public DisplayPoolItem(Fragment f) {
		this.mFragment = f;
		this.dispTime = 5;
	}

	public DisplayPoolItem(Fragment f, int t) {
		this.mFragment = f;
		this.dispTime = t;
	}
}