package com.spark.hudsharedlib;

import java.io.Serializable;

import com.spark.hudsharedlib.BTObject.OBJECT_TYPE;

public class NaviPoint extends MessageBaseItem implements Serializable {   
	private static final long serialVersionUID = 11110L;      
	
	public String pointTitle;
    public double mEndPointx;
    public double mEndPointy;

    public NaviPoint() {
    	super(MessageBaseItem.TYPE_MAP_INFO);
    }
    
    public void setTitle(String title) {
    	pointTitle = title;
    }
    
    public void setNaviPoint(double pointItemx, double pointItemy) {
    	mEndPointx = pointItemx;
    	mEndPointy = pointItemy;
    }

    public String getTitle() {
    	return pointTitle;
    }
    
    public double getLatitude() {
    	return mEndPointx;
    }
    
    public double getLongitute() {
    	return mEndPointy;
    }
    

    
}   
