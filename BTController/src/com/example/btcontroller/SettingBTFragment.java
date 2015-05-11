package com.example.btcontroller;

import java.util.Set;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.btcontroller.service.mainService;
import com.spark.hudsharedlib.SettingItem;

@SuppressLint("NewApi")
public class SettingBTFragment extends Fragment {
	mainService mService;
	private boolean mIsBound;
	Context     mContext;
	String      btDevMACAddr = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		/*
		 * initialize and insert one sample element into mCities.
		 */
		
		final View v = inflater.inflate(R.layout.setting_menu_bt, null);
		SettingBT sbt = (SettingBT) ((FragmentChangeActivity)getActivity()).getSettingItem(SettingItem.Type.BTSETTING);
		if (sbt.getBTAddress() != null && sbt.getBTAddress()!="") {
            TextView tv = (TextView)v.findViewById(R.id.setting_menu_bt_display_name_text);
            tv.setText(sbt.getBTName());
            //for test - sbt.setBTAddress(null);
            mService.setBTDevByMAC(sbt.getBTAddress());
            tv = (TextView)v.findViewById(R.id.setting_menu_bt_display_status_text);
            tv.setText(getResources().getString(R.string.setting_menu_bt_display_status_connected));
		}
		else
		{
	         TextView tv = (TextView)v.findViewById(R.id.setting_menu_bt_display_status_text);
	         tv.setText(getResources().getString(R.string.setting_menu_bt_display_status_disconnected));
		}
		
		
		Set<BluetoothDevice> deviceSet = mService.getBluetoothDevices();
		if(deviceSet == null) {
			return v;
		}
		
		final BTDevAdapter		 mAdapter = new BTDevAdapter(mContext, deviceSet);
		ListView lv = (ListView)v.findViewById(R.id.setting_menu_bt_device_list);
		lv.setAdapter(mAdapter);
		
        lv.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> p, View child, final int pos, long id){
                 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                 builder.setMessage(getResources().getString(R.string.setting_menu_bt_pair_with_selected_device));
                 builder.setPositiveButton(R.string. confirm , new DialogInterface.OnClickListener() {
                         public void onClick(DialogInterface dialog, int id) {
                                btDevMACAddr = mAdapter.getItem(pos).address;
                                /*
                                 * Also need to refresh all UI elements displaying this.
                                 * We have 2 pending todo here:
                                 * 1. update globalsetting.btsetting upon clicked
                                 * 2. in instantiating this UI, load string from globalsetting.btsetting.
                                 * if it's not present, then display nothing here.
                                 */
                                SettingBT sbt2 = (SettingBT) ((FragmentChangeActivity)getActivity()).getSettingItem(SettingItem.Type.BTSETTING);
                                sbt2.setBTAddress(btDevMACAddr);
                                sbt2.setBTName(mAdapter.getItem(pos).name);
                                
                                mService.setBTDevByMAC(btDevMACAddr);
                                TextView tv = (TextView)v.findViewById(R.id.setting_menu_bt_display_name_text);
                                tv.setText(mAdapter.getItem(pos).name);
                                tv = (TextView)v.findViewById(R.id.setting_menu_bt_display_status_text);
                                tv.setText(getResources().getString(R.string.setting_menu_bt_display_status_connected));

                        }
                 });
                  builder.setNegativeButton(getResources().getString(R.string. cancel ), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                 });
               AlertDialog ad = builder.create();
               ad.show();
           }
    });

		/*
		 * Need a custom adapter to convert this set into ArrayAdapter.
		 */
		return v;
	}
	
	@Override
	public void onPause () {
		super.onPause();
		doUnBindService();
	}
	
	public SettingBTFragment (Context c) {
		mContext = c;
		doBindService();
		/*
		 * Initialize UI layout
		 */
	}
	
	private class BTDevStruct {
		public String address;
		public String name;
		public BTDevStruct (String a, String n) {
			address = a;
			name = n;
		}
	}
	
    private class BTDevAdapter extends ArrayAdapter<BTDevStruct> {
		Set<BluetoothDevice> mDevSet;
		public BTDevAdapter(Context context, Set<BluetoothDevice> devSet) {
			super(context, 0);
			mDevSet = devSet;
			
			for (BluetoothDevice bDev : mDevSet) {
				this.add(new BTDevStruct(bDev.getAddress(), bDev.getName()));
			}
			/*
			 * You need to add an array of <string> in the constructor - to display it.
			 */
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.setting_weather_city_list, null);
			}
			TextView tv = (TextView) convertView.findViewById(R.id.setting_weather_city_name);
			tv.setText(this.getItem(position).name);
			return convertView;
		}
	}
	
	private ServiceConnection mServConn =  new ServiceConnection () {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mService = ((mainService.LocalBinder)service).getService();				
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mService = null;
		};
	};
	
	void doBindService () {
		mContext.bindService(new Intent(mContext, mainService.class), mServConn, Context.BIND_AUTO_CREATE);
		mIsBound = true;
	}
	
	void doUnBindService () {
		if (mIsBound) {
			mContext.unbindService(mServConn);
			mIsBound = false;
		}
	}
}
