package com.example.btcontroller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.LogoutAPI;
import com.sina.weibo.sdk.openapi.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.sina.weibo.sdk.utils.LogUtil;
import com.sina.weibo.sdk.widget.LoginButton;
import com.sina.weibo.sdk.widget.LoginoutButton;
import com.spark.hudsharedlib.MessageWeiboItem;
import com.spark.hudsharedlib.MessageWeiboItemList;
import com.spark.hudsharedlib.SettingItem;

@SuppressLint("ValidFragment")
public class SettingWeiboFragment extends Fragment {

	BTAction		  mBTAction;
	
	private static final String TAG = SettingWeiboFragment.class.getName();	
	private List<Map<String, Object>> mData;
	   /** 当前 Token 信息 */
	private Oauth2AccessToken mAccessToken;
	private SettingWeibo sweibo;
	/** 用于获取微博信息流等操作的API */
	private StatusesAPI mStatusesAPI;

	/** UI元素列表 */
    private TextView mTokenView;
    private LoginoutButton mLoginoutBtnSilver;
    
    /** 登陆认证对应的listener */
    private AuthListener mLoginListener = new AuthListener();
    /** 登出操作对应的listener */
    private LogOutRequestListener mLogoutListener = new LogOutRequestListener();

    String FILENAME = "token_file";
    
    /**
     * 该按钮用于记录当前点击的是哪一个 Button，用于在 {@link #onActivityResult}
     * 函数中进行区分。通常情况下，我们的应用中只需要一个合适的 {@link LoginButton} 
     * 或者 {@link LoginoutButton} 即可。
     */
    private Button mCurrentClickedButton;
    	
	public SettingWeiboFragment(BTAction bta) {
		mBTAction = bta;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.setting_menu_weibo, null);

		mTokenView = (TextView) rootView.findViewById(R.id.weibo_login_result);
        // 创建授权认证信息
        AuthInfo authInfo = new AuthInfo(getActivity(), Constants.APP_KEY, Constants.REDIRECT_URL, Constants.SCOPE);
		
	     // 登录/注销按钮（默认样式：蓝色）
        mLoginoutBtnSilver = (LoginoutButton)rootView.findViewById(R.id.login_out_button_silver);
        mLoginoutBtnSilver.setWeiboAuthInfo(authInfo, mLoginListener);
        mLoginoutBtnSilver.setLogoutListener(mLogoutListener);
        // 由于 LoginLogouButton 并不保存 Token 信息，因此，如果您想在初次
        // 进入该界面时就想让该按钮显示"注销"，请放开以下代码
        FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
		fca.setWeiboSetting(true);

		sweibo = (SettingWeibo) fca.getSettingItem(SettingItem.Type.WEIBO);
  
        // 获取当前已保存过的 Token
        mAccessToken = sweibo.readAccessToken();
        mLoginoutBtnSilver.setLogoutInfo(mAccessToken, mLogoutListener);
        
        
 		return rootView;
	}
	
    /**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     * 
     * @see {@link Activity#onActivityResult}
     */
    @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        mLoginoutBtnSilver.onActivityResult(requestCode, resultCode, data);
        
    }
	/**
     * 登入按钮的监听器，接收授权结果。
     */
    private class AuthListener implements WeiboAuthListener {
        @Override
        public void onComplete(Bundle values) {
            Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
            if (accessToken != null && accessToken.isSessionValid()) {
                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                        new java.util.Date(accessToken.getExpiresTime()));
                String format = getString(R.string.weibosdk_demo_token_to_string_format_1);
                mTokenView.setText(String.format(format, accessToken.getToken(), date));

                sweibo.writeAccessToken(getActivity(), accessToken);
                
                mBTAction.doStartService();
                
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(getActivity(), 
                    R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 登出按钮的监听器，接收登出处理结果。（API 请求结果的监听器）
     */
    private class LogOutRequestListener implements RequestListener {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String value = obj.getString("result");

                    if ("true".equalsIgnoreCase(value)) {
                        SettingWeibo.clear(getActivity());
                        mTokenView.setText(R.string.weibosdk_demo_logout_success);
                    }
                    
                    sweibo.writeAccessToken(getActivity(), null);
                    
                    mBTAction.doStartService();
                    
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }     

		@Override
		public void onWeiboException(WeiboException e) {
			mTokenView.setText(R.string.weibosdk_demo_logout_failed);
		}
    }
	
	@Override
	public void onPause() {
		super.onPause();
	
	}
    

}
