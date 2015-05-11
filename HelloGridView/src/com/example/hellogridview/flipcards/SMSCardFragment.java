package com.example.hellogridview.flipcards;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hellogridview.R;
import com.spark.hudsharedlib.MessageSMSItem;

public class SMSCardFragment extends Fragment {
	MessageSMSItem mItem;
	int position;
	int total_num;

	public SMSCardFragment(MessageSMSItem msgWeather, int p, int t) {
		mItem = msgWeather;
		position = p;
		total_num = t;
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.flipcard_sms_item, null);

		TextView tv = (TextView) v.findViewById(R.id.flipcard_sms_item_title);
		tv.setText(getResources().getString(R.string.message_new) + ": ("
				+ position + "/" + total_num + ")");
		tv = (TextView) v.findViewById(R.id.flipcard_sms_item_from);
		tv.setText(getResources().getString(R.string.message_from) + ": "
				+ mItem.getFrom());
		tv = (TextView) v.findViewById(R.id.flipcard_sms_item_date);
		tv.setText(getResources().getString(R.string.message_received_at)
				+ ": " + mItem.getDate());
		tv = (TextView) v.findViewById(R.id.flipcard_sms_item_msgbody);
		tv.setText(mItem.getContent());

		return v;
	}
}
