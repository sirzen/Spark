package com.example.btcontroller.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.btcontroller.BTService;
import com.spark.hudsharedlib.MessageCallItem;
import com.spark.hudsharedlib.MessageSMSItem;


public class CallListener extends BroadcastReceiver{
    private BTService		  mBTService;

    public CallListener (BTService b) {
    	this.mBTService = b;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String          call_from = new String();
        String			call_date = new String();
        String 			call_state = new String();
    	
        DateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        call_date = formatter.format(calendar.getTime());

        call_state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        call_from = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        
        /* Action to perform on Controller side when an incoming call is ringing.
        if (TelephonyManager.EXTRA_STATE_RINGING.equals(call_state))
        {
        }
        // Action to perform when incoming ringing call is ended.
        if (TelephonyManager.EXTRA_STATE_RINGING.equals(call_state))
        {
        }
        */
        
        String call_contactName = getContactDisplayNameByNumber(context, call_from);
        if (!call_contactName.equals("")) {
        	call_from = call_contactName + " (" + call_from + ")"; 
        }
        MessageCallItem cItem = new MessageCallItem(call_from, call_date, call_state);
        mBTService.sendMessage(cItem);
    }
    
    private String getContactDisplayNameByNumber(Context c, String number) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String name = "";

        ContentResolver contentResolver = c.getContentResolver();
        Cursor contactLookup = contentResolver.query(uri, new String[] {BaseColumns._ID,
                ContactsContract.PhoneLookup.DISPLAY_NAME }, null, null, null);

        try {
            if (contactLookup != null && contactLookup.getCount() > 0) {
                contactLookup.moveToNext();
                name = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
                //String contactId = contactLookup.getString(contactLookup.getColumnIndex(BaseColumns._ID));
            }
        } finally {
            if (contactLookup != null) {
                contactLookup.close();
            }
        }

        return name;
    }
}