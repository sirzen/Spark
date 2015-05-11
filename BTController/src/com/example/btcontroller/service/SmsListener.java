package com.example.btcontroller.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.DateFormat;

import com.example.btcontroller.BTAction;
import com.example.btcontroller.BTService;
import com.spark.hudsharedlib.MessageSMSItem;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.telephony.SmsMessage;
import android.widget.Toast;


public class SmsListener extends BroadcastReceiver{
    private BTService		  mBTService;
    private Boolean mEnabled;
    
    public SmsListener (BTService b) {
    	this.mBTService = b;
    	mEnabled = true;
    }
    
    public void setEnable(Boolean enable) {
    	mEnabled = enable;
    }
    
    @Override
    public void onReceive(Context context, Intent intent) {
        String          msg_from = new String();
        String			msg_date = new String();
        String 			msg_content = new String();
        String			msg_contactName = "";
        if(mEnabled == false) {
        	return;
        }
        
        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            if (bundle != null){
                //---retrieve the SMS message received---
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i < msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        msg_contactName = getContactDisplayNameByNumber(context, msg_from);
                        String msgBody = msgs[i].getMessageBody();
                        msg_content += msgBody;
                    }
                }catch(Exception e){
                	// Generally, in future we should provide Toast to report errors.
                	// Toast.makeText(context, "Caught an exception inside SMS handler", Toast.LENGTH_SHORT).show();
                	e.printStackTrace();
                }
            }
        }
        
        if (!msg_contactName.equals("")) {
        	msg_from = msg_contactName + " (" + msg_from + ")";
        }
        
        DateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
        Calendar calendar = Calendar.getInstance();
        msg_date = formatter.format(calendar.getTime());

        MessageSMSItem smsItem = new MessageSMSItem(msg_from, msg_date, msg_content);
        mBTService.sendMessage(smsItem);
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