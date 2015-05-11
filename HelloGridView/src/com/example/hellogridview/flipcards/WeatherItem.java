package com.example.hellogridview.flipcards;

import java.util.LinkedList;

import com.spark.hudsharedlib.MessageWeatherItem;
import com.spark.hudsharedlib.MessageWeatherItemGroup;

import android.app.Fragment;

public class WeatherItem extends ContentPoolItem {
	MessageWeatherItemGroup mMsgGrp;

	public WeatherItem(MessageWeatherItemGroup msgGrp) {
		super(ContentPoolItem.POOL_ID_WEATHER);
		this.mMsgGrp = msgGrp;
	}

	@Override
	public LinkedList<DisplayPoolItem> convertToDispItem() {
		LinkedList<DisplayPoolItem> fList = new LinkedList<DisplayPoolItem>();
		// MessageWeatherItemGroup tmpGrp =
		// (MessageWeatherItemGroup)mMsgGrp.clone();
		MessageWeatherItemGroup tmpGrp = mMsgGrp;
		MessageWeatherItem item = tmpGrp.poll();
		while (item != null) {
			WeatherCardFragment wcFrag = new WeatherCardFragment(item);
			fList.add(new DisplayPoolItem(wcFrag));
			item = tmpGrp.poll();
		}
		return fList;
	}

}
