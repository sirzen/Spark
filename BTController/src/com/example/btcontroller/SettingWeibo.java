/*
 * Copyright (C) 2010-2013 The SINA WEIBO Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.btcontroller;

import java.util.ArrayList;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.spark.hudsharedlib.SettingItem;

/**
 * 该类定义了微博授权时所需要的参数。
 * 
 * @author SINA
 * @since 2013-10-07
 */
public class SettingWeibo extends SettingItem {
	private static final long serialVersionUID = 16114724324446767L;
    private static final String PREFERENCES_NAME = "com_weibo_sdk_android";
    private static SharedPreferences pref ;
    private static String enable_key = "ENABLE"; 

    private static final String KEY_UID           = "uid";
    private static final String KEY_ACCESS_TOKEN  = "access_token";
    private static final String KEY_EXPIRES_IN    = "expires_in";
 
	public SettingWeibo(Context context, String s, boolean bEnabled) {
		super(SettingItem.Type.WEIBO, s, bEnabled);
		pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
	}

	@SuppressLint("NewApi")
	public void setEnabled (Boolean b) {
		Boolean tokenValid = pref.getBoolean("tokenValid", false);
        String uid   = pref.getString(KEY_UID, "");
        String token = pref.getString(KEY_ACCESS_TOKEN, "");
        Long expire_time = pref.getLong(KEY_EXPIRES_IN, 0);
      
		Editor editor = pref.edit();
        editor.clear();
        editor.putBoolean("tokenValid", tokenValid);
        editor.putString(KEY_UID, uid);
        editor.putString(KEY_ACCESS_TOKEN, token);
        editor.putLong(KEY_EXPIRES_IN, expire_time);
    
        editor.putBoolean(enable_key, b);
	    
        editor.commit();		 		
	}
	
	public Boolean getEnabled() {
		Boolean benabled = pref.getBoolean(enable_key, false);
        return benabled;
	}
	
    /**
     * 保存 Token 对象到 SharedPreferences。
     * 
     * @param context 应用程序上下文环境
     * @param token   Token 对象
     */
    public static void writeAccessToken(Context context, Oauth2AccessToken token) {
        if (null == context) {
            return;
        }
        
        Boolean benabled = pref.getBoolean(enable_key, false);

        Editor editor = pref.edit();
        editor.clear();
        
        if(null == token) {
        	editor.putBoolean("tokenValid", false);
        } else {
        	editor.putBoolean("tokenValid", true);
        	editor.putString(KEY_UID, token.getUid());
            editor.putString(KEY_ACCESS_TOKEN, token.getToken());
            editor.putLong(KEY_EXPIRES_IN, token.getExpiresTime());
    	    editor.putBoolean(enable_key, benabled);
        }
	    
        editor.commit();
    }

    /**
     * 从 SharedPreferences 读取 Token 信息。
     * 
     * @param context 应用程序上下文环境
     * 
     * @return 返回 Token 对象
     */
    public static Oauth2AccessToken readAccessToken() {
        //if (null == context) {
          //  return null;
        //}
        
        Oauth2AccessToken token = new Oauth2AccessToken();
        if(pref.getBoolean("tokenValid", false) ==true) {
	        token.setUid(pref.getString(KEY_UID, ""));
	        token.setToken(pref.getString(KEY_ACCESS_TOKEN, ""));
	        token.setExpiresTime(pref.getLong(KEY_EXPIRES_IN, 0));
        } else {
        	token = null;
        }
        
	    return token;
    }

    /**
     * 清空 SharedPreferences 中 Token信息。
     * 
     * @param context 应用程序上下文环境
     */
    public static void clear(Context context) {
        if (null == context) {
            return;
        }
        
        Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}
