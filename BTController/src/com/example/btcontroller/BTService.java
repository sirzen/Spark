package com.example.btcontroller;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.spark.hudsharedlib.MessageBaseItem;

public class BTService {
	private Context mContext;
	private BluetoothSocket mConnectedSocket;
    private ObjectOutputStream mmOOS = null;
	
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothDevice  mBluetoothDevice;
	private Handler    mHandler;
	
	private void displayBTUnsupported () {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(mContext, R.string.setting_menu_bt_not_supported, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void displayBTDisabled () {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(mContext, R.string.setting_menu_bt_not_supported, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void displayNotConnected () {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(mContext, R.string.setting_menu_bt_not_connected, Toast.LENGTH_SHORT).show();
			}
		});
	}
	public BTService(Context context) {
		
		this.mContext = context;
		mHandler = new Handler();
		
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			// create a dialog box indicating bluetooth is not supported.
			displayBTUnsupported();
			return;
		}
		
		if (!mBluetoothAdapter.isEnabled()) {
			displayBTDisabled();
			return;
		}
		
		/*
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		if (pairedDevices.size() > 0) {
		    // Loop through paired devices
		    for (BluetoothDevice device : pairedDevices) {
		        // Add the name and address to an array adapter to show in a ListView
		        mBluetoothDevice = device;
		    }
		}
		*/
	}
	
	public Set<BluetoothDevice> getPairedDevices () {
		if (mBluetoothAdapter == null) {
			displayBTUnsupported();
			return null;
		}
		
		if (!mBluetoothAdapter.isEnabled()) {
			displayBTDisabled();
			return null;
		}
		
		return mBluetoothAdapter.getBondedDevices();
	}
	
	/**
	 * Try to instantiate a bluetooth connection with device specificed by macAddr.
	 * @param macAddr
	 * @return
	 */
	public Boolean pairSelectedDevice(String macAddr) {
		/*
		 * TODO: We also need to save this mac address into GlobalSetting.
		 */
		
		BluetoothDevice bDevice = null;

		try {
			bDevice = mBluetoothAdapter.getRemoteDevice(macAddr);
		} catch (IllegalArgumentException e) {
			Toast.makeText(mContext, "Wrong MAC address passed in", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		mBluetoothDevice = bDevice;
		
		ConnectBTSocketThread cThread = new ConnectBTSocketThread(mBluetoothDevice);
		cThread.start();
		return true;
	}

	public void sendMessage(MessageBaseItem msg) {
        // Get the input and output streams, using temp objects because
        // member streams are final
        if (mConnectedSocket == null || (mmOOS == null)) {
        	displayNotConnected();
        	return;
        }

        try {
			mmOOS.writeObject(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class ConnectBTSocketThread extends Thread {
	    private UUID MY_UUID;
	    public ConnectBTSocketThread(BluetoothDevice device) {
	        MY_UUID = UUID.fromString(MessageBaseItem.UUID_String);
		   	 
	        // Get a BluetoothSocket to connect with the given BluetoothDevice
	        try {
	            // MY_UUID is the app's UUID string, also used by the server code
	            mConnectedSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
	        } catch (IOException e) { 
	        	e.printStackTrace();
	        }
	    }
	    
	    // We need to wait for the following thread to complete for any further action.
	    // Find a way to block UI until this thread is called.
	    // Why the hell this runnable is never scheduled to run?
		@Override
		public void run() {
			mBluetoothAdapter.cancelDiscovery();
	        try {
	            // Connect the device through the socket. This will block
	            // until it succeeds or throws an exception
	        	// TODO: If the server does not start, then we will be blocked on the following step
	        	// Therefore, must block all other operations on mmOOS on the next step -
	        	// Design a UI to block it.
	        	mConnectedSocket.connect();
	        	OutputStream tmpOut = mConnectedSocket.getOutputStream();
	        	mmOOS = new ObjectOutputStream(tmpOut);
	        } catch (IOException e) {
	            // Unable to connect; close the socket and get out
	            try {
	            	e.printStackTrace();
	            	mConnectedSocket.close();
	            } catch (IOException closeException) { }
	            return;
	        }
		}
	}

	public void destroy() {
		// Tear down all pre-allocated Bluetooth resources. 
		// TODO.
		
	}
}
