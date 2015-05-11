package com.example.btcontroller;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DefaultFragment extends DialogFragment {
	
	/*
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// construct the RelativeLayout
		ListView v = (ListView)findViewById(R.id.list);
		String[] items = getResources().getStringArray(R.array.main_control_items);
		ArrayAdapter<String> colorAdapter = new ArrayAdapter<String>(getActivity(), 
				android.R.layout.simple_list_item_1, android.R.id.text1, items);
		setListAdapter(colorAdapter);
		return (ListView)v;
	}
	*/
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.text, null);
	}
}
