package com.spark.hudsharedlib;

import java.io.Serializable;


public class BTObject implements Serializable {   
	private static final long serialVersionUID = -121321L;   ;   

	public enum OBJECT_TYPE {
	    CONTROL,SETTING,WEIXIN,MAP,WEIBO,HELP;
	};

	protected OBJECT_TYPE mObjectType;
	
    public OBJECT_TYPE getObjectType() {
    	return mObjectType;
    }
    
    public void setObjectType(OBJECT_TYPE type) {
    	mObjectType = type;
    }
        
}   
