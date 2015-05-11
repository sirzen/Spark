package com.spark.hudsharedlib;

import java.io.Serializable;

public class MessageDriveMode extends MessageBaseItem implements Serializable {   
		private static final long serialVersionUID = 1122112L;      
		
		
	    public int mDriveMode;	   
	    
	    public MessageDriveMode() {
	    	super(MessageBaseItem.TYPE_DRIVE_MODE);
	    }
	    
	    public void setMode(int mode) {
	    	mDriveMode = mode;
	    }
	    
	    public int getMode() {
	    	return mDriveMode;
	    }
}
