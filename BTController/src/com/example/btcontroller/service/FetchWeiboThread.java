package com.example.btcontroller.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.widget.Toast;

import com.example.btcontroller.R;
import com.example.btcontroller.SettingWeiboFragment;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.sina.weibo.sdk.utils.LogUtil;
import com.spark.hudsharedlib.MessageWeiboItem;

public class FetchWeiboThread extends Thread {
	private static final String TAG = FetchWeiboThread.class.getName();	
	/** 当前 Token 信息 */
	private Oauth2AccessToken mAccessToken;
	/** 用于获取微博信息流等操作的API */
	private StatusesAPI mStatusesAPI;
	private Context mContext;
	private Boolean mEnabled;
	
	private long  since_id; //record id of the last weibo, initialized to 0
	
	public FetchWeiboThread(Context context, Boolean enabled, Oauth2AccessToken token) {
		mContext = context;
		mAccessToken = token;
		mEnabled  = enabled;
		since_id  = 0 ;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
        // 对statusAPI实例化
		
		while(mEnabled && (mAccessToken != null)) {
        	 if(mAccessToken.isSessionValid()) {
       	        mStatusesAPI = new StatusesAPI(mAccessToken);
        
	           //	mStatusesAPI.friendsTimeline(since_id, 0L, 1, 1, false, 0, false, mListener);
       	        mStatusesAPI.friendsTimeline(0L, 0L, 3, 1, false, 0, false, mListener);//for test
	            try {
					Thread.sleep(3600*20); //for test - wait for another 5 minutes to fetch again
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
					//Thread.currentThread().interrupt();
				}   
        	}
		}
        
	}

	public void settingUpdate(Boolean enabled, Oauth2AccessToken token) {
		mEnabled = enabled;
		mAccessToken = token;
	}
	
		/** 
	    * 格式 String s = "Thu Aug 16 09:46:53 +0800 2012"; 
	    *  
	    */  
	       private final String[] mE = { "Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep",  
	               "Oct", "Nov", "Dec" };  
	       private final String[] mC = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11" };  
	       private Calendar calendar = Calendar.getInstance();  
	       private SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");  
	     
	       /** 
	        *  
	        * @param s 
	        *            "Thu Aug 16 09:46:53 +0800 2012" 
	        * @return 1年前 or 08-15 10:50 or 几小时前 or 几分钟前 or 几秒前， otherwise "" 
	        */  
	       public String parseTime(String s) {  
	           String[] split = s.split(" ");  
	           String month = monthUtil(split[1]);  
	           calendar.set(Calendar.YEAR, Integer.valueOf(split[5]));  
	           calendar.set(Calendar.MONTH, Integer.valueOf(month));  
	           calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(split[2]));  
	           String[] hourSplit = split[3].split(":");  
	           calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hourSplit[0]));  
	           calendar.set(Calendar.MINUTE, Integer.valueOf(hourSplit[1]));  
	           calendar.set(Calendar.SECOND, Integer.valueOf(hourSplit[2]));  
	           Calendar currentCalendar = Calendar.getInstance();  
	           if (calendar.get(calendar.YEAR) < currentCalendar.get(currentCalendar.YEAR))  
	               return currentCalendar.get(currentCalendar.YEAR) - calendar.get(calendar.YEAR) + "年前";  
	           if (currentCalendar.get(currentCalendar.MONTH) - calendar.get(calendar.MONTH) > 0)   
	               return sdf.format(calendar.getTimeInMillis());  
	           if (currentCalendar.get(currentCalendar.DAY_OF_MONTH) - calendar.get(calendar.DAY_OF_MONTH) > 0)  
	               return sdf.format(calendar.getTimeInMillis());  
	           if (currentCalendar.get(currentCalendar.HOUR_OF_DAY) - calendar.get(calendar.HOUR_OF_DAY) > 0)  
	               return currentCalendar.get(currentCalendar.HOUR_OF_DAY) - calendar.get(calendar.HOUR_OF_DAY) + "小时前";  
	           if (currentCalendar.get(currentCalendar.MINUTE) - calendar.get(calendar.MINUTE) > 0)  
	               return currentCalendar.get(currentCalendar.MINUTE) - calendar.get(calendar.MINUTE) + "分钟前";  
	           if (currentCalendar.get(currentCalendar.SECOND) - calendar.get(calendar.SECOND) > 0)  
	               return currentCalendar.get(currentCalendar.SECOND) - calendar.get(calendar.SECOND) + "秒前";  
	           return "";  
	       }  
	     
	       private String monthUtil(String m) {  
	           for (int i = 0; i < mE.length; i++) {  
	               if (mE[i].equalsIgnoreCase(m))  
	                   return mC[i];  
	           }  
	           return "";//这个若返回""会报错，没处理  
	       }  		
	  
	  /**
	  * 微博 OpenAPI 回调接口。
	  */
	   private RequestListener mListener = new RequestListener() {
	        @Override
	        public void onComplete(String response) {
	        	
	            if (!TextUtils.isEmpty(response)) {
	                LogUtil.i(TAG, response);

	                if (response.startsWith("{\"statuses\"")) {
	                    // 调用 StatusList#parse 解析字符串成微博列表对象
	                    StatusList statuses = StatusList.parse(response);
	                    
	                    for (int ix = 0; ix < statuses.statusList.size(); ix++) {
	                    	Status  curStatus = statuses.statusList.get(ix);  
	                    	
	                    	MessageWeiboItem mMsg = new MessageWeiboItem();
	                    	
	                    	long since_id = Long.parseLong(curStatus.id);
	                    	
	                		String wtext = curStatus.text;
	                		String wuser = curStatus.user.screen_name;
	                		String iconUrl = curStatus.user.profile_image_url;
	                		ArrayList<String> pic_urls = curStatus.pic_urls;
	                		Status retStatus = curStatus.retweeted_status;
	                		mMsg.writeUser(wuser);
	                		String timeformatted = parseTime(curStatus.created_at);
	                		mMsg.writeTime(timeformatted);
	                		mMsg.writeText(wtext);
	                		ArrayList<String> ret_pic_urls;
	                		if(retStatus!=null) {
		                		ret_pic_urls = retStatus.pic_urls;
		                		if(retStatus.user!=null) {
		                			mMsg.writeRetUser(retStatus.user.screen_name);
		                		} else {
		                			mMsg.writeRetUser("");
		                		}
	                			mMsg.writeRetText(retStatus.text);
	                			mMsg.setRetStat();
	                		} else {
	                			ret_pic_urls=null;
	                		}
	                		

	                		DownloadImgThread imgThread = new DownloadImgThread(mContext, mMsg, iconUrl, pic_urls, ret_pic_urls);
	                		imgThread.start();
	                    }
	                    
	                    if (statuses != null && statuses.total_number > 0) {
	                    	//mBTAction.sendMessage(mMessageList);
	                   //     Toast.makeText(WeiBoHomeActivity.this, 
	                     //           "获取微博信息流成功, 条数: " + statuses.statusList.size(), 
	                       //         Toast.LENGTH_LONG).show();
	                    }
	                    	
	                    
	                    
	                } else {
	                    Toast.makeText(mContext, response, Toast.LENGTH_LONG).show();
	                }
	            } else {
	            	 Toast.makeText(mContext, "no response", Toast.LENGTH_LONG).show();
	            }
	            
	        }

	        @Override
	        public void onWeiboException(WeiboException e) {
	            LogUtil.e(TAG, e.getMessage());
	            ErrorInfo info = ErrorInfo.parse(e.getMessage());
	            Toast.makeText(mContext, "exception", Toast.LENGTH_LONG).show();
	        }
	   };	
	   
	   private class DownloadImgThread extends Thread {
		   	private Context mContext;
			MessageWeiboItem mItem;
			String m_iconUrl;
			List<String> m_Pic_Urls;
			List<String> m_ret_Pic_Urls;
			List<byte[]> imgList = new ArrayList<byte[]>();
			List<byte[]> retimgList = new ArrayList<byte[]>();
			
			public DownloadImgThread(Context context, MessageWeiboItem map, String iconUrl, ArrayList<String> pic_urls, ArrayList<String> ret_pic_urls) {
				mContext = context;
				m_iconUrl = iconUrl;
				m_Pic_Urls = pic_urls;
				m_ret_Pic_Urls = ret_pic_urls;
				mItem = map;
			}
			
			@Override
			public void run() {
				URL m;
				Bitmap bmap;
				ByteArrayOutputStream baos;
				InputStream img;				
				//for(int i=0; i<1; i++) {

			        try {
			        	
			            m = new URL(m_iconUrl);
			            img = (InputStream) m.getContent();
			            bmap = BitmapFactory.decodeStream(img);  
			            
			            baos = new ByteArrayOutputStream();  
			            bmap.compress(Bitmap.CompressFormat.PNG, 100, baos);  
			            mItem.setIcon(baos.toByteArray()); 	

			        	if(m_Pic_Urls!=null) {
			        		int downloadImgSize;
			        		if(m_Pic_Urls.size()>2) {
			        			downloadImgSize = 2; 
			        		} else {
			        			downloadImgSize = m_Pic_Urls.size();
			        		}
				        	for(int i=0; i < downloadImgSize; i++) {
					            m = new URL(m_Pic_Urls.get(i));
					            img = (InputStream) m.getContent();
					            bmap = BitmapFactory.decodeStream(img);  
					            
					            baos = new ByteArrayOutputStream();  
					            bmap.compress(Bitmap.CompressFormat.PNG, 100, baos);  
					            imgList.add(baos.toByteArray());  
				        	} 
			        	}
			        	if(m_ret_Pic_Urls!=null) {
			        		int downloadImgSize;
			        		if(m_ret_Pic_Urls.size()>2) {
			        			downloadImgSize = 2; 
			        		} else {
			        			downloadImgSize = m_ret_Pic_Urls.size();
			        		}

				        	for(int i=0; i < downloadImgSize; i++) {
					            m = new URL(m_ret_Pic_Urls.get(i));
					            img = (InputStream) m.getContent();
					            bmap = BitmapFactory.decodeStream(img);  
					            
					            baos = new ByteArrayOutputStream();  
					            bmap.compress(Bitmap.CompressFormat.PNG, 100, baos);  
					            retimgList.add(baos.toByteArray());  
				        	}
			        	}			        	
			        } catch (MalformedURLException e1) {
			            e1.printStackTrace();
			        } catch (IOException e) {
			            e.printStackTrace();
			        }
				//}
				
				mItem.setPics(imgList);
				mItem.setRetPics(retimgList);
			
				//if(m_ret_Pic_Urls!=null) {
				((mainService)mContext).sendMessage(mItem);
				//}
			}
		}	   
}
