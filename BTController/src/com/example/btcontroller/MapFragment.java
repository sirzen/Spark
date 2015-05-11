package com.example.btcontroller;

import java.util.ArrayList;
import java.util.List;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.AMap.InfoWindowAdapter;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.LocationSource.OnLocationChangedListener;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.overlay.PoiOverlay;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Tip;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.example.btcontroller.AMapUtil;
import com.example.btcontroller.R;
import com.example.btcontroller.RouteSearchPoiDialog;
import com.example.btcontroller.TTSController;
import com.example.btcontroller.ToastUtil;
import com.example.btcontroller.RouteSearchPoiDialog.OnListItemClick;

import com.spark.hudsharedlib.NaviPoint;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;


@SuppressLint("ValidFragment")
public class MapFragment extends Fragment implements OnClickListener,
	LocationSource, AMapLocationListener, OnMarkerClickListener, 
	InfoWindowAdapter, TextWatcher, OnPoiSearchListener {


	private BTAction mBTAction;
	// --------------View基本控件---------------------
	private MapView mMapView;// 地图控件	
	private RadioGroup mNaviMethodGroup;// 步行驾车选择控件
	private AutoCompleteTextView mStartPointText;// 起点输入
	private EditText mWayPointText;// 途经点输入
	private EditText mEndPointText;// 终点输入
	//private AutoCompleteTextView mStrategyText;// 行车策略输入
	private Button mRouteButton;// 路径规划按钮
	private Button mNaviButton;// 模拟导航按钮
	private ProgressDialog mProgressDialog;// 路径规划过程显示状态
	private ProgressDialog mGPSProgressDialog;// GPS过程显示状态
	private ImageView mStartImage;// 起点下拉按钮
	private ImageView mWayImage;// 途经点点击按钮
	private ImageView mEndImage;// 终点点击按钮
	
	private ImageView settingFlag;
	//private ImageView mStrategyImage;// 行车策略点击按钮
	// 地图和导航核心逻辑类
	private AMap mAmap;
	private AMapNavi mAmapNavi;
	// ------定位相关-------
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	private Marker marker;// 定位雷达小图标
	private RadioGroup mGPSModeGroup;

	// 关键字搜索相关
	private AutoCompleteTextView searchText;// 输入搜索关键字
	private String keyWord = "";// 要输入的poi搜索关键字
	private ProgressDialog progDialog = null;// 搜索时进度条
	private EditText editCity;// 要输入的城市名字或者城市区号
	private PoiResult poiResult; // poi返回的结果
	private PoiSearch.Query query;// Poi查询条件类
	private int currentPage = 0;// 当前页面，从0开始计数
	private PoiSearch poiSearch;// POI搜索
	private int isNavi = 1;
	
	// 周边搜索
	private int gotoAsPoint = 0;
	private Spinner selectDeep;// 选择城市列表
	private String[] itemDeep = { "餐饮", "景区", "酒店", "影院" };
	private Spinner selectType;// 选择返回是否有团购，优惠
	private String[] itemTypes = { "所有poi", "有团购", "有优惠", "有团购或者优惠" };
	private String deepType = "";// poi搜索类型
	private int searchType = 0;// 搜索类型
	private int tsearchType = 0;// 当前选择搜索类型
	// private PoiResult poiResult; // poi返回的结果
	// private PoiSearch.Query query;// Poi查询条件类
	private LatLonPoint lp = new LatLonPoint(39.908127, 116.375257);// 默认西单广场
	private Marker locationMarker; // 选择的点
	// private PoiSearch poiSearch;
	private PoiOverlay poiOverlay;// poi图层
	private List<PoiItem> poiItems;// poi数据
	private Marker detailMarker;// 显示Marker的详情

	// ---------------------变量---------------------
	private String[] mStrategyMethods;// 记录行车策略的数组
	private String[] mPositionMethods;// 记录起点我的位置、地图点选数组
	// 驾车路径规划起点，途经点，终点的list
	private List<NaviLatLng> mStartPoints = new ArrayList<NaviLatLng>();
	private List<NaviLatLng> mWayPoints = new ArrayList<NaviLatLng>();
	private List<NaviLatLng> mEndPoints = new ArrayList<NaviLatLng>();
	// 记录起点、终点、途经点位置
	private NaviLatLng mStartPoint = new NaviLatLng();
	private NaviLatLng mEndPoint = new NaviLatLng();
	private NaviLatLng mWayPoint = new NaviLatLng();
	// 记录起点、终点、途经点在地图上添加的Marker
	private Marker mStartMarker;
	private Marker mWayMarker;
	private Marker mEndMarker;
	private Marker mGPSMarker;
	private boolean mIsGetGPS = false;// 记录GPS定位是否成功
	private boolean mIsStart = false;// 记录是否已我的位置发起路径规划

	private ArrayAdapter<String> mPositionAdapter;

	private AMapNaviListener mAmapNaviListener;

	// 记录地图点击事件相应情况，根据选择不同，地图响应不同
	private int mMapClickMode = MAP_CLICK_NO;
	private static final int MAP_CLICK_NO = 0;// 地图不接受点击事件
	private static final int MAP_CLICK_START = 1;// 地图点击设置起点
	private static final int MAP_CLICK_WAY = 2;// 地图点击设置途经点
	private static final int MAP_CLICK_END = 3;// 地图点击设置终点
	private static final int MAP_CLICK_KS = 4;// 地图点击设置终点
	private static final int MAP_CLICK_AS = 5;// 地图点击设置终点

	// 记录导航种类，用于记录当前选择是驾车还是步行
	private int mTravelMethod = DRIVER_NAVI_METHOD;
	private static final int DRIVER_NAVI_METHOD = 0;// 驾车导航
	private static final int WALK_NAVI_METHOD = 1;// 步行导航

	private int mNaviMethod;
	private static final int NAVI_METHOD = 0;// 执行模拟导航操作
	private static final int ROUTE_METHOD = 1;// 执行计算线路操作

	private int mStartPointMethod = BY_MY_POSITION;
	private static final int BY_MY_POSITION = 0;// 以我的位置作为起点
	private static final int BY_MAP_POSITION = 1;// 以地图点选的点为起点
	// 计算路的状态
	private final static int GPSNO = 0;// 使用我的位置进行计算、GPS定位还未成功状态
	private final static int CALCULATEERROR = 1;// 启动路径计算失败状态
	private final static int CALCULATESUCCESS = 2;// 启动路径计算成功状态

	private boolean mNaviPointObtained = false;
	protected NaviPoint mNaviPoint = null;
	protected String mCityName;
	
	public MapFragment(BTAction bta) {
		mBTAction = bta;
	}
	
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_navistart, container, false);
		// 初始化所需资源、控件、事件监听
		initResources();
	
		initView(savedInstanceState, rootView);
		initListener();
		initMapAndNavi();
		
		
		return rootView;
	    
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}
	
	// -----------------初始化-------------------
	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap(View rootView) {
		ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point1));
		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point2));
		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point3));
		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point4));
		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point5));
		giflist.add(BitmapDescriptorFactory.fromResource(R.drawable.point6));
		marker = mAmap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
				.icons(giflist).period(50));
		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
		myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
		myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
		// myLocationStyle.anchor(int,int)//设置小蓝点的锚点
		myLocationStyle.strokeWidth(0.1f);// 设置圆形的边框粗细
		mAmap.setMyLocationStyle(myLocationStyle);
		mAmap.setMyLocationRotateAngle(180);
		mAmap.setLocationSource(this);// 设置定位监听
		mAmap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
		mAmap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
		mAmap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
		mAmap.getMyLocation();
		// keyword search
		Button searKsButton = (Button) rootView.findViewById(R.id.searchKsButton);
		searKsButton.setOnClickListener(this);
		// Button nextButton = (Button) findViewById(R.id.nextButton);
		// nextButton.setOnClickListener(this);
		searchText = (AutoCompleteTextView) rootView.findViewById(R.id.keyWord);
		searchText.addTextChangedListener(this);// 添加文本输入框监听事件

		// mAmap.setOnMarkerClickListener(this);// 添加点击marker监听事件
		// mAmap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件

		// around search
		Button searchButton = (Button) rootView.findViewById(R.id.aroundSearchButton);
		searchButton.setOnClickListener(this);
		
		
		/*locationMarker = mAmap.addMarker(new MarkerOptions().anchor(0.5f, 1)
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.point))
				.position(new LatLng(lp.getLatitude(), lp.getLongitude()))
				.title("西单为中心点，查其周边"));
		locationMarker.showInfoWindow();*/

	}
	/**
	 * 初始化资源文件，主要是字符串
	 */
	private void initResources() {
		mCityName = "";
		Resources res = getResources();
		mStrategyMethods = new String[] {
				res.getString(R.string.navi_strategy_speed),
				res.getString(R.string.navi_strategy_cost),
				res.getString(R.string.navi_strategy_distance),
				res.getString(R.string.navi_strategy_nohighway),
				res.getString(R.string.navi_strategy_timenojam),
				res.getString(R.string.navi_strategy_costnojam) };
		mPositionMethods = new String[] { res.getString(R.string.mypoistion),
				res.getString(R.string.mappoistion) };

	}

	/**
	 * 初始化界面所需View控件
	 * 
	 * @param savedInstanceState
	 */
	private void initView(Bundle savedInstanceState, View rootView) {
		mMapView = (MapView) rootView.findViewById(R.id.map);
		mMapView.onCreate(savedInstanceState);
		if(mAmap==null) {
			mAmap = mMapView.getMap();
		}

		setUpMap(rootView);

		mRouteButton = (Button) rootView.findViewById(R.id.navi_route_button);
		
		settingFlag = (ImageView) rootView.findViewById(R.id.strategysetting);
		//settingFlag.getBackground().setAlpha(0);
		//settingFlag.setAlpha(50);
		
		ImageView naviFlag = (ImageView) rootView.findViewById(R.id.naviflag);
		ImageView aroundFlag = (ImageView) rootView.findViewById(R.id.aroundflag);
		ImageView zoomFlag = (ImageView) rootView.findViewById(R.id.zoomflag);
		
		
	}


	/**
	 * 初始化所需监听
	 */
	private void initListener() {
		// 控件点击事件
		mRouteButton.setOnClickListener(this);
		// mNaviMethodGroup.setOnCheckedChangeListener(this);
		// 设置地图点击事件
		//mAmap.setOnMapClickListener(this);
		mAmap.setOnMarkerClickListener(this);// 添加点击marker监听事件
		mAmap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
		
		mAMapLocationManager = LocationManagerProxy.getInstance(getActivity());
		  
	        //此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
	        //注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
	        //在定位结束后，在合适的生命周期调用destroy()方法     
	        //其中如果间隔时间为-1，则定位只定一次
		mAMapLocationManager.requestLocationUpdates(
	                LocationProviderProxy.AMapNetwork, 60*1000, 15, this);
	 
		mAMapLocationManager.setGpsEnable(false);
		
		settingFlag.setOnClickListener(this);		

	}

	/**
	 * 初始化地图和导航相关内容
	 */
	private void initMapAndNavi() {

		mTravelMethod = DRIVER_NAVI_METHOD;
		// 初始语音播报资源
		getActivity().setVolumeControlStream(AudioManager.STREAM_MUSIC);// 设置声音控制
		TTSController ttsManager = TTSController.getInstance(getActivity());// 初始化语音模块
		ttsManager.init();
		mAmapNavi = AMapNavi.getInstance(getActivity());// 初始化导航引擎
		mAmapNavi.setAMapNaviListener(ttsManager);// 设置语音模块播报
		// 初始化Marker添加到地图
		// mStartMarker = mAmap.addMarker(new MarkerOptions()
		// .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
		// .decodeResource(getResources(), R.drawable.start))));
		mEndMarker = mAmap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
						.decodeResource(getResources(), R.drawable.end))));
		mGPSMarker = mAmap.addMarker(new MarkerOptions()
				.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
						.decodeResource(getResources(),
								R.drawable.location_marker))));
		
		
	}

	
	// ----------具体处理方法--------------
	/**
	 * 算路的方法，根据选择可以进行行车和步行两种方式进行路径规划,仅选择驾车模式
	 */
	private void calculateRoute() {
		// 驾车导航
		int driverIndex = calculateDriverRoute();
		if (driverIndex == CALCULATEERROR) {
			showToast("路线计算失败,检查参数情况");
			return;
		} else if (driverIndex == GPSNO) {
			return;
		}
		// 显示路径规划的窗体
		showProgressDialog();
	}

	/**
	 * 对行车路线进行规划
	 */
	private int calculateDriverRoute() {
		int driveMode = 0;
		int code = CALCULATEERROR;

		if (!mIsGetGPS) {
			
			// we add for debug
			if(gotoAsPoint==0) {
				mStartPoint.setLatitude(mAmap.getMyLocation().getLatitude());
				mStartPoint.setLongitude(mAmap.getMyLocation().getLongitude());
				mStartPoints.add(mStartPoint);	
			}
			
			showGPSProgressDialog();
			code = GPSNO;
			// just remove for debug - mIsStart = true;

			// 我的位置-add for debug
			if (mAmapNavi.calculateDriveRoute(mStartPoints, mEndPoints,
					mWayPoints, driveMode)) {
				code = CALCULATESUCCESS;
			} else {

				code = CALCULATEERROR;
			}

		} else {

			if (mAmapNavi
					.calculateDriveRoute(mEndPoints, mWayPoints, driveMode)) {
				code = CALCULATESUCCESS;
			} else {

				code = CALCULATEERROR;
			}

		}
		// 支持多个终点，终点列表的首点为导航终点，终点列表按车行方向排列，带有方向信息，可有效避免算路到马路的另一侧；
		return code;
	}

	
	public void OnNaviSetting() {
		FragmentManager ft = getActivity().getSupportFragmentManager();
		ft
		.beginTransaction()
		.addToBackStack(null)
		.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
		.replace(R.id.null_relative_layout, new NaviSettingFragment(mBTAction))
		.commit();

	}
	
	
    void startNavi() {
    	mBTAction.sendMessage(mNaviPoint);
    }
	// ----------------------事件处理---------------------------

	/**
	 * 控件点击事件监听
	 * */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 路径规划按钮处理事件
		case R.id.navi_route_button:
			mStartPointMethod = BY_MY_POSITION;
			mNaviMethod = ROUTE_METHOD;
			if (mNaviPointObtained==false) {
				ToastUtil.show(getActivity(), "请选择目的地");
				return;
			} else {
				startNavi();
				ToastUtil.show(getActivity(), "已发出导航指令");
			}
			
			break;

		/**
		 * 点击搜索按钮
		 */
		case R.id.searchKsButton:
			mMapClickMode = MAP_CLICK_KS;
			isNavi = 0;
			searchButton();
			break;
		/**
		 * 点击下一页按钮
		 */
		// case R.id.nextButton:
		// nextButton();
		// break;

    	/**
		 * 点击搜索按钮
		 */
		case R.id.aroundSearchButton:
			mMapClickMode = MAP_CLICK_AS;
			startAsSearch();
			break;

		case R.id.strategysetting:
			OnNaviSetting();
			break;
			
		default:
			break;

		}
	}

	public void startAsSearch() {
		mEndPoint.setLatitude(mAmap.getMyLocation().getLatitude());
		mEndPoint.setLongitude(mAmap.getMyLocation().getLongitude());
		mEndPoints.add(mEndPoint);	
		if(mNaviPoint==null) {
			mNaviPoint = new NaviPoint();
			mNaviPoint.setTitle("");
			mNaviPoint.setNaviPoint(mStartPoint.getLatitude(), mStartPoint.getLongitude());
		}

		
		FragmentManager ftmap = getActivity().getSupportFragmentManager();
		ftmap
		.beginTransaction()
		.addToBackStack(null)
		.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
		.replace(R.id.null_relative_layout, new PoiAroundSearchFragment(mBTAction, mNaviPoint, mCityName))
		.commit();		
	}	
	// the meat of switching the above fragment
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof FragmentChangeActivity) {
			FragmentChangeActivity fca = (FragmentChangeActivity) getActivity();
			fca.switchContent(fragment);
		}
	}	


	/**
	 * 起点下拉框点击事件监听
	 * */
	private OnItemClickListener getOnItemClickListener() {
		return new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int index,
					long arg3) {
			}

		};
	}

	/**
	 * 导航回调函数
	 * 
	 * @return
	 */
	private AMapNaviListener getAMapNaviListener() {
		if (mAmapNaviListener == null) {

			mAmapNaviListener = new AMapNaviListener() {

				@Override
				public void onTrafficStatusUpdate() {
					// TODO Auto-generated method stub

				}

				@Override
				public void onStartNavi(int arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onReCalculateRouteForYaw() {
					// TODO Auto-generated method stub

				}

				@Override
				public void onReCalculateRouteForTrafficJam() {
					// TODO Auto-generated method stub

				}

				@Override
				public void onLocationChange(AMapNaviLocation location) {
					// GPS位置更新回调函数
					mIsGetGPS = true;
					NaviLatLng naviLatLang = location.getCoord();
					mGPSMarker.setPosition(new LatLng(
							naviLatLang.getLatitude(), naviLatLang
									.getLongitude()));
					dissmissGPSProgressDialog();
					if (mIsStart) {
						calculateRoute();
						mIsStart = false;
					}
				}

				@Override
				public void onInitNaviSuccess() {
					// TODO Auto-generated method stub

				}

				@Override
				public void onInitNaviFailure() {
					// TODO Auto-generated method stub

				}

				@Override
				public void onGetNavigationText(int arg0, String arg1) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onEndEmulatorNavi() {
					// TODO Auto-generated method stub

				}

				@Override
				public void onCalculateRouteSuccess() {
					dissmissProgressDialog();
					//Intent intent = new Intent(NaviStartActivity.this,
						//	NaviCustomActivity.class);
					//intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					//startActivity(intent);
				}

				@Override
				public void onCalculateRouteFailure(int arg0) {
					dissmissProgressDialog();
					showToast("路径规划出错");
				}

				@Override
				public void onArrivedWayPoint(int arg0) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onArriveDestination() {
					// TODO Auto-generated method stub

				}

				@Override
				public void onGpsOpenStatus(boolean arg0) {
					// TODO Auto-generated method stub

				}
			};
		}
		return mAmapNaviListener;
	}

	/**
	 * 返回键处理事件
	 * */
	
	// ---------------UI操作----------------
	private void showToast(String message) {
		Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
	}

	private void setTextDescription(TextView view, String description) {
		view.setText(description);
	}

	/**
	 * 显示进度框
	 */
	private void showProgressDialog() {
		if (mProgressDialog == null)
			mProgressDialog = new ProgressDialog(getActivity());
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setMessage("线路规划中");
		mProgressDialog.show();
	}

	/**
	 * 隐藏进度框
	 */
	private void dissmissProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}

	/**
	 * 显示GPS进度框
	 */
	private void showGPSProgressDialog() {
		if (mGPSProgressDialog == null)
			mGPSProgressDialog = new ProgressDialog(getActivity());
		mGPSProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mGPSProgressDialog.setIndeterminate(false);
		mGPSProgressDialog.setCancelable(true);
		mGPSProgressDialog.setMessage("GPS定位中");
		mGPSProgressDialog.show();
	}

	/**
	 * 隐藏进度框
	 */
	private void dissmissGPSProgressDialog() {
		if (mGPSProgressDialog != null) {
			mGPSProgressDialog.dismiss();
		}
	}
	
	// -------------生命周期必须重写方法----------------
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);
	}

		
	@Override
	public void onResume() {
		super.onResume();
		mMapView.onResume();

		// 以上两句必须重写
		// 以下两句逻辑是为了保证进入首页开启定位和加入导航回调
		AMapNavi.getInstance(getActivity()).setAMapNaviListener(getAMapNaviListener());
		mAmapNavi.startGPS();
	}

	@Override
	public void onPause() {
		super.onPause();
		mMapView.onPause();
		// 以上两句必须重写
		// 下边逻辑是移除监听
		AMapNavi.getInstance(getActivity())
				.removeAMapNaviListener(getAMapNaviListener());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mMapView.onDestroy();
		// 以上逻辑是必须重写的
		// 这是最后退出页，所以销毁导航和播报资源
		AMapNavi.getInstance(getActivity()).destroy();// 销毁导航
		TTSController.getInstance(getActivity()).stopSpeaking();// 停止播报
		TTSController.getInstance(getActivity()).destroy();// 销毁播报模块

	}

	/**
	 * 此方法已经废弃
	 */
	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		if (mListener != null && aLocation != null) {
			mListener.onLocationChanged(aLocation);// 显示系统小蓝点
			marker.setPosition(new LatLng(aLocation.getLatitude(), aLocation
					.getLongitude()));// 定位雷达小图标
			float bearing = mAmap.getCameraPosition().bearing;
			mAmap.setMyLocationRotateAngle(bearing);// 设置小蓝点旋转角度
			
			mCityName = aLocation.getCity();
			mStartPoint.setLatitude(aLocation.getLatitude());
			mStartPoint.setLongitude(aLocation.getLongitude());
			
		}
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(getActivity());
			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
			mAMapLocationManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, 60*1000, 15, this);
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
	}

	/**
	 * 点击搜索按钮
	 */
	public void searchButton() {
		keyWord = AMapUtil.checkEditText(searchText);
		if ("".equals(keyWord)) {
			ToastUtil.show(getActivity(), "请输入搜索关键字");
			return;
		} else {
			mNaviPointObtained = false;
			doSearchQuery();
		}
	}

	/**
	 * 显示进度框
	 */
	private void showKSProgressDialog() {
		if (progDialog == null)
			progDialog = new ProgressDialog(getActivity());
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(false);
		progDialog.setMessage("正在搜索:\n" + keyWord);
		progDialog.show();
	}

	/**
	 * 隐藏进度框
	 */
	private void dissmissKSProgressDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	/**
	 * 开始进行poi搜索
	 */
	protected void doSearchQuery() {
		showKSProgressDialog();// 显示进度框

		currentPage = 0;
		query = new PoiSearch.Query(keyWord, "", mCityName);// editCity.getText().toString());//
														// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
		query.setPageSize(10);// 设置每页最多返回多少条poiitem
		query.setPageNum(currentPage);// 设置查第一页

		poiSearch = new PoiSearch(getActivity(), query);
		poiSearch.setOnPoiSearchListener(this);
		poiSearch.searchPOIAsyn();
	}



	@Override
	public View getInfoContents(Marker marker) {
		return null;
	}

	@Override
	public View getInfoWindow(final Marker marker) {
		// add for an end point
		mEndPoint.setLatitude(marker.getPosition().latitude);
		mEndPoint.setLongitude(marker.getPosition().longitude);
		mEndPoints.add(mEndPoint);

		View view = getActivity().getLayoutInflater().inflate(R.layout.poikeywordsearch_uri,
				null);
		TextView title = (TextView) view.findViewById(R.id.title);
		title.setText(marker.getTitle());

		TextView snippet = (TextView) view.findViewById(R.id.snippet);
		snippet.setText(marker.getSnippet());
		ImageButton button = (ImageButton) view
				.findViewById(R.id.start_amap_app);
		// 调起高德地图app
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				startURI(marker);
			}
		});
		return view;
	}

	/**
	 * 通过URI方式调起高德地图app
	 */
	public void startURI(Marker marker) {
		if (getAppIn()) {
			Intent intent = new Intent(
					"android.intent.action.VIEW",
					android.net.Uri
							.parse("androidamap://viewMap?sourceApplication="
									+ getApplicationName() + "&poiname="
									+ marker.getTitle() + "&lat="
									+ marker.getPosition().latitude + "&lon="
									+ marker.getPosition().longitude + "&dev=0"));
			intent.setPackage("com.autonavi.minimap");
			startActivity(intent);
		} else {
			String url = "http://mo.amap.com/?dev=0&q="
					+ marker.getPosition().latitude + ","
					+ marker.getPosition().longitude + "&name="
					+ marker.getTitle();
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(url));
			startActivity(intent);
		}

	}

	/**
	 * 判断高德地图app是否已经安装
	 */
	public boolean getAppIn() {
		PackageInfo packageInfo = null;
		try {
			packageInfo = getActivity().getPackageManager().getPackageInfo(
					"com.autonavi.minimap", 0);
		} catch (NameNotFoundException e) {
			packageInfo = null;
			e.printStackTrace();
		}
		// 本手机没有安装高德地图app
		if (packageInfo != null) {
			return true;
		}
		// 本手机成功安装有高德地图app
		else {
			return false;
		}
	}

	/**
	 * 获取当前app的应用名字
	 */
	public String getApplicationName() {
		PackageManager packageManager = null;
		ApplicationInfo applicationInfo = null;
		try {
			packageManager = getActivity().getApplicationContext().getPackageManager();
			applicationInfo = packageManager.getApplicationInfo(
					getActivity().getPackageName(), 0);
		} catch (PackageManager.NameNotFoundException e) {
			applicationInfo = null;
		}
		String applicationName = (String) packageManager
				.getApplicationLabel(applicationInfo);
		return applicationName;
	}

	/**
	 * poi没有搜索到数据，返回一些推荐城市的信息
	 */
	private void showSuggestCity(List<SuggestionCity> cities) {
		String infomation = "推荐城市\n";
		for (int i = 0; i < cities.size(); i++) {
			infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
					+ cities.get(i).getCityCode() + "城市编码:"
					+ cities.get(i).getAdCode() + "\n";
		}
		ToastUtil.show(getActivity(), infomation);

	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		String newText = s.toString().trim();
		Inputtips inputTips = new Inputtips(getActivity(),
				new InputtipsListener() {

					@Override
					public void onGetInputtips(List<Tip> tipList, int rCode) {
						if (rCode == 0) {// 正确返回
							List<String> listString = new ArrayList<String>();
							for (int i = 0; i < tipList.size(); i++) {
								listString.add(tipList.get(i).getName());
							}
							ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
									getActivity().getApplicationContext(),
									R.layout.route_inputs, listString);
							searchText.setAdapter(aAdapter);
							aAdapter.notifyDataSetChanged();
						}
					}
				});
		try {
			inputTips.requestInputtips(newText, "");// 第一个参数表示提示关键字，第二个参数默认代表全国，也可以为城市区号

		} catch (AMapException e) {
			e.printStackTrace();
		}
	}

	/**
	 * POI详情查询回调方法
	 */
	@Override
	public void onPoiItemDetailSearched(PoiItemDetail arg0, int rCode) {

	}



	/**
	 * POI信息查询回调方法
	 */
	@Override
	public void onPoiSearched(PoiResult result, int rCode) {

		dissmissKSProgressDialog();// 隐藏对话框
		dissmissProgressDialog();// 隐藏对话框

		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {// 搜索poi的结果
				if (result.getQuery().equals(query)) {// 是否是同一条
					poiResult = result;
					// 取得搜索到的poiitems有多少页
					List<PoiItem> poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
					List<SuggestionCity> suggestionCities = poiResult
							.getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息

					if (poiItems != null && poiItems.size() > 0) {
						mAmap.clear();// 清理之前的图标
						PoiOverlay poiOverlay = new PoiOverlay(mAmap, poiItems);
						poiOverlay.removeFromMap();
						poiOverlay.addToMap();
						poiOverlay.zoomToSpan();
							
						RouteSearchPoiDialog dialog = new RouteSearchPoiDialog(
									getActivity(), poiItems);
						dialog.setTitle("您要找的是:");
						dialog.show();
						dialog.setOnListClickListener(new OnListItemClick() {
							@Override
							public void onListItemClick(
								RouteSearchPoiDialog dialog, PoiItem endpoiItem) {
								
								mEndPoint.setLatitude(endpoiItem.getLatLonPoint().getLatitude());
								mEndPoint.setLongitude(endpoiItem.getLatLonPoint().getLongitude());
								mEndPoints.add(mEndPoint);
								
								endpoiItem.getTitle();
								
								if(mNaviPoint==null) {
									mNaviPoint = new NaviPoint();
								}
								mNaviPoint.setTitle(endpoiItem.getTitle());
								mNaviPoint.setNaviPoint(endpoiItem.getLatLonPoint().getLatitude(), endpoiItem.getLatLonPoint().getLongitude());
								mNaviPointObtained = true;
							}
						});
						
					} else if (suggestionCities != null
							&& suggestionCities.size() > 0) {
						showSuggestCity(suggestionCities);
					} else {
						ToastUtil.show(getActivity(),
								R.string.no_result);
					}
				}
			} else {
				ToastUtil.show(getActivity(), R.string.no_result);
			}
		} else if (rCode == 27) {
			ToastUtil.show(getActivity(), R.string.error_network);
		} else if (rCode == 32) {
			ToastUtil.show(getActivity(), R.string.error_key);
		} else {
			ToastUtil.show(getActivity(),
					getString(R.string.error_other) + rCode);
		}

	}

	/**
	 * 注册监听
	 */
	private void registerListener() {
		//mAmap.setOnMapClickListener(this);
		mAmap.setOnMarkerClickListener(this);
		mAmap.setInfoWindowAdapter(this);
	}

	
	/**
	 * 查单个poi详情
	 * 
	 * @param poiId
	 */
	public void doSearchPoiDetail(String poiId) {
		if (poiSearch != null && poiId != null) {
			poiSearch.searchPOIDetailAsyn(poiId);
		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		marker.showInfoWindow();
		
		// set default position
		mEndPoint.setLatitude(marker.getPosition().latitude);
		mEndPoint.setLongitude(marker.getPosition().longitude);
		mEndPoints.add(mEndPoint);
		
		if(mNaviPoint==null) {
			mNaviPoint = new NaviPoint();
		}
		mNaviPoint.setTitle(marker.getTitle());
		mNaviPoint.setNaviPoint(marker.getPosition().latitude, marker.getPosition().longitude);
		
		mNaviPointObtained = true;
		
		return false;
	}

	
}