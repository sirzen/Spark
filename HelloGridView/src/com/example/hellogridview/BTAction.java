package com.example.hellogridview;

/*
 * This file contains all BlueTooth actions.
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;

import com.spark.hudsharedlib.GlobalSetting;

public class BTAction {
	private Context mContext;
	private LinkedBlockingQueue<Object> mReadQueue;

	private final int REQUEST_ENABLE_BT = 100;

	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothDevice mBluetoothDevice;

	private void displayBTUnsupported() {
	}

	public BTAction(Context context) {
		this.mContext = context;
		this.mReadQueue = new LinkedBlockingQueue<Object>();

		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			// create a dialog box indicating bluetooth is not supported.
			displayBTUnsupported();
			return;
		}

		if (!mBluetoothAdapter.isEnabled()) {
			// We need to define better UI handling here.
		}

		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter
				.getBondedDevices();
		if (pairedDevices.size() > 0) {
			// Loop through paired devices
			for (BluetoothDevice device : pairedDevices) {
				// Add the name and address to an array adapter to show in a
				// ListView
				mBluetoothDevice = device;
			}
		}

		AcceptThread aThread = new AcceptThread();
		/*
		 * Currently, this only accepts one connection - Need to support
		 * re-connect if a connection is lost.
		 */
		aThread.start();
	}

	private class AcceptThread extends Thread {
		private final BluetoothServerSocket mmServerSocket;
		UUID MY_UUID = UUID.fromString(GlobalSetting.UUID_String);

		public AcceptThread() {
			// Use a temporary object that is later assigned to mmServerSocket,
			// because mmServerSocket is final
			BluetoothServerSocket tmp = null;
			try {
				// MY_UUID is the app's UUID string, also used by the client
				// code
				tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(
						"ServerString", MY_UUID);
			} catch (IOException e) {
			}
			/*
			 * TODO: Need to check BlueTooth state here. Otherwise, this
			 * mmServerSocket will be NULL.
			 */
			mmServerSocket = tmp;
		}

		@Override
		public void run() {
			BluetoothSocket socket = null;
			// Keep listening until exception occurs or a socket is returned
			while (true) {
				try {
					socket = mmServerSocket.accept();
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}

				// If a connection was accepted
				if (socket != null) {
					// Do work to manage the connection (in a separate thread)
					try {
						manageConnectedSocket(socket);
					} catch (StreamCorruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}

		private void manageConnectedSocket(BluetoothSocket socket)
				throws StreamCorruptedException, IOException {
			ConnectedThread cThread = new ConnectedThread(socket);
			cThread.start();
		}

		private class ConnectedThread extends Thread {
			private final BluetoothSocket mmSocket;
			private InputStream mmInStream;
			private ObjectInputStream mmOIS;

			public ConnectedThread(BluetoothSocket socket) {
				mmSocket = socket;
			}

			@Override
			public void run() {
				InputStream tmpIn = null;

				// Get the input and output streams, using temp objects because
				// member streams are final
				try {
					tmpIn = mmSocket.getInputStream();
				} catch (IOException e) {
				}

				mmInStream = tmpIn;

				try {
					mmOIS = new ObjectInputStream(mmInStream);
				} catch (StreamCorruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				while (true) {
					try {
						// Read from the InputStream
						// Note, that client could tear down this connection. We
						// should return gracefully
						// once client disconnects.
						Object o = mmOIS.readObject();
						mReadQueue.add(o);
					} catch (IOException e) {
						// We probably could do something once client tear down
						// this socket
						// This should be acceptable. If this happens, simply
						// close this socket.
						e.printStackTrace();
						this.cancel();
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			public void cancel() {
				try {
					mmSocket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		/** Will cancel the listening socket, and cause the thread to finish */
		public void cancel() {
			try {
				mmServerSocket.close();
			} catch (IOException e) {
			}
		}
	}

	public Object readObject() {
		/*
		 * This will return null if queue is Empty.
		 */
		return mReadQueue.poll();
	}

	public Object readObjectBlocked() throws InterruptedException {
		// Let's block for 100 seconds.
		return mReadQueue.poll(1000, TimeUnit.SECONDS);
	}

}
