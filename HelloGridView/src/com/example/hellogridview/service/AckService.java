package com.example.hellogridview.service;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

public class AckService extends IntentService {

	public AckService() {
		super("AckService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Toast.makeText(this, "I have captured a HandleIntent call!",
				Toast.LENGTH_LONG).show();
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "AckService Starting", Toast.LENGTH_SHORT).show();
		return super.onStartCommand(intent, flags, startId);
	}
}
