package com.example.hellogridview.flipcards;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hellogridview.R;
import com.spark.hudsharedlib.MessageCallItem;

public class CallCardFragment extends Fragment {
	MessageCallItem mItem;
	int position;
	int total_num;

	public CallCardFragment(MessageCallItem msgCall) {
		mItem = msgCall;
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.flipcard_call_item, null);

		TextView tv = (TextView) v.findViewById(R.id.flipcard_call_item_text);
		// tv.setText(getResources().getString(R.string.message_new) + ": (" +
		// position + "/" + total_num + ")");
		tv.setText(getResources().getString(R.string.call_from) + ": "
				+ mItem.getFrom());
		return v;
	}
}
