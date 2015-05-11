package com.spark.hudsharedlib;

import java.io.Serializable;
import java.util.List;

public class MessageWeiboItem extends MessageBaseItem implements Serializable {
	
	private static final long serialVersionUID = -213213212321L;      
	
	public String  wbtext;
	public String  created_at;
	public String  wbuser;
	public byte[]  wbicon;
	public List<byte[]> wbpics;
	
	public String  ret_wbtext;
	public String  ret_wbuser;
	public List<byte[]> ret_wbpics;
	public boolean ret_stat = false;

	
	public MessageWeiboItem() {
		super(MessageBaseItem.TYPE_WEIBO_ITEM);
	}

	public void writeTime(String time) {
		created_at = time;
	}

	public String getTime() {
		return created_at;
	}
	
	public void writeText(String text) {
		wbtext = text;
	}
	
	public String getText() {
		return wbtext;
	}
	
		
	public void writeUser(String user) {
		wbuser = user;
	}      
	
	public String getUser() {
		return wbuser;
	}      
	
	public void setPics(List<byte[]> pic) {
		wbpics = pic;
	}
	public List<byte[]> getPics() {
		return wbpics;
	}
	public void setIcon(byte[] icon) {
		wbicon = icon;
	}
	public byte[] getIcon() {
		return wbicon;
	}
	
	
	
	public void writeRetText(String text) {
		ret_wbtext = text;
	}
	
	public void writeRetUser(String user) {
		ret_wbuser = user;
	}      
	      
	public String getRetText() {
		return ret_wbtext;
	}
	
	public String getRetUser() {
		return ret_wbuser;
	}      
	public void setRetPics(List<byte[]> pic) {
		ret_wbpics = pic;
	}
	public List<byte[]> getRetPics() {
		return ret_wbpics;
	}
	
	public void setRetStat() {
		ret_stat = true;
	}
	public boolean getRetStat() {
		return ret_stat;
	}
	
}
