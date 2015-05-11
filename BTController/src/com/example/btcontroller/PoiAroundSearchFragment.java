package com.example.btcontroller;

import java.util.ArrayList;
import java.util.List;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnInfoWindowClickListener;
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
import com.amap.api.services.poisearch.Cinema;
import com.amap.api.services.poisearch.Dining;
import com.amap.api.services.poisearch.Hotel;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.SearchBound;
import com.amap.api.services.poisearch.Scenic;
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
import android.widget.AdapterView.OnItemSelectedListener;
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


/**
 * AMapV2地图中简单介绍poisearch搜索
 */
@SuppressLint({ "ValidFragment", "NewApi" })
public class PoiAroundSearchFragment extends Fragment implements OnMarkerClickListener, 
		InfoWindowAdapter, OnItemSelectedListener, OnPoiSearchListener, 
		OnMapClickListener, OnInfoWindowClickListener, OnClickListener {
	
	private BTAction mBTAction;
	private MapView mMapView;// 地图控件	
	private AMap aMap;
	private ProgressDialog progDialog = null;// 搜索时进度条
	private Spinner selectDeep;// 选择城市列表
	private String[] itemDeep = { "餐饮", "景区", "酒店", "影院" };
	private Spinner selectType;// 选择返回是否有团购，优惠
	private String[] itemTypes = { "所有poi", "有团购", "有优惠", "有团购或者优惠" };
	private String deepType = "";// poi搜索类型
	private int searchType = 0;// 搜索类型
	private int tsearchType = 0;// 当前选择搜索类型
	private PoiResult poiResult; // poi返回的结果
	private int currentPage = 0;// 当前页面，从0开始计数
	private PoiSearch.Query query;// Poi查询条件类
	private LatLonPoint lp = new LatLonPoint(39.908127, 116.375257);// 默认西单广场
//	private Marker locationMarker; // 选择的点
	private PoiSearch poiSearch;
	private PoiOverlay poiOverlay;// poi图层
	private List<PoiItem> poiItems;// poi数据
	private Marker detailMarker;// 显示Marker的详情
//	private Button nextButton;// 下一页
	private Marker locationMarker; // 选择的点
	
	//added for navi
	private Button destButton;
	private NaviLatLng mEndPoint = new NaviLatLng();
	private NaviLatLng mStartPoint = new NaviLatLng();;
	private NaviPoint mKsPoint = null;
	private String mCityName;
	private NaviPoint mNaviPoint;
	
	public PoiAroundSearchFragment(BTAction bta, NaviPoint ksPoint, String cityName) {
		mBTAction = bta;
		mKsPoint  = ksPoint;
		mCityName = cityName;
		mNaviPoint = mKsPoint;
	}
	
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.poiaroundsearch_activity, container, false);
		// 初始化所需资源、控件、事件监听
		init(savedInstanceState, rootView);
		//initListener();
		//initMapAndNavi();
	
		return rootView;
	    
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}
		

	/**
	 * 初始化AMap对象
	 */
	private void init(Bundle savedInstanceState, View rootView) {
		mMapView = (MapView) rootView.findViewById(R.id.map);
		mMapView.onCreate(savedInstanceState);
		if(aMap==null) {
			aMap = mMapView.getMap();
		}

		setUpMap(rootView);
		setSelectType(rootView);

		Button searchButton = (Button) rootView.findViewById(R.id.searchButton);
		searchButton.setOnClickListener(this);
			//nextButton = (Button) findViewById(R.id.nextButton);
			//nextButton.setOnClickListener(this);
			//nextButton.setClickable(false);// 默认下一页按钮不可点
		destButton = (Button) rootView.findViewById(R.id.gohere);
		destButton.setOnClickListener(this);
			//nextButton.setOnClickListener(this);
		locationMarker = aMap.addMarker(new MarkerOptions()
					.anchor(0.5f, 1)
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.point))
					.position(new LatLng(mKsPoint.getLatitude(), mKsPoint.getLongitute()))
					.title("中心点"));
		locationMarker.showInfoWindow();
	}

	/**
	 * 设置城市选择
	 */
	private void setUpMap(View rootView) {
		selectDeep = (Spinner) rootView.findViewById(R.id.spinnerdeep);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, itemDeep);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		selectDeep.setAdapter(adapter);
		selectDeep.setOnItemSelectedListener(this);// 添加spinner选择框监听事件
		aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
		aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件

		
	}

	/**
	 * 设置选择类型
	 */
	private void setSelectType(View rootView) {
		selectType = (Spinner) rootView.findViewById(R.id.searchType);// 搜索类型
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, itemTypes);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		selectType.setAdapter(adapter);
		selectType.setOnItemSelectedListener(this);// 添加spinner选择框监听事件
		aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
		aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
	}

	/**
	 * 注册监听
	 */
	private void registerListener() {
		aMap.setOnMapClickListener(this);
		aMap.setOnMarkerClickListener(this);
		aMap.setOnInfoWindowClickListener(this);
		aMap.setInfoWindowAdapter(this);
	}

	/**
	 * 显示进度框
	 */
	private void showProgressDialog() {
		if (progDialog == null)
			progDialog = new ProgressDialog(getActivity());
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(false);
		progDialog.setMessage("正在搜索中");
		progDialog.show();
	}

	/**
	 * 隐藏进度框
	 */
	private void dissmissProgressDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	/**
	 * 开始进行poi搜索
	 */
	protected void doSearchQuery() {
		showProgressDialog();// 显示进度框
		aMap.setOnMapClickListener(null);// 进行poi搜索时清除掉地图点击事件
		currentPage = 0;
		query = new PoiSearch.Query("", deepType, mCityName);// 第一个参数表示搜索字符串，第二个参数表示poi搜索类型，第三个参数表示poi搜索区域（空字符串代表全国）
		query.setPageSize(10);// 设置每页最多返回多少条poiitem
		query.setPageNum(currentPage);// 设置查第一页

		searchType = tsearchType;

		switch (searchType) {
		case 0: {// 所有poi
			query.setLimitDiscount(false);
			query.setLimitGroupbuy(false);
		}
			break;
		case 1: {// 有团购
			query.setLimitGroupbuy(true);
			query.setLimitDiscount(false);
		}
			break;
		case 2: {// 有优惠
			query.setLimitGroupbuy(false);
			query.setLimitDiscount(true);
		}
			break;
		case 3: {// 有团购或者优惠
			query.setLimitGroupbuy(true);
			query.setLimitDiscount(true);
		}
			break;
		}
		lp.setLatitude(mNaviPoint.getLatitude());
		lp.setLongitude(mNaviPoint.getLongitute());
		
		if (lp != null) {
			poiSearch = new PoiSearch(getActivity(), query);
			poiSearch.setOnPoiSearchListener(this);
			poiSearch.setBound(new SearchBound(lp, 2000, true));//
			// 设置搜索区域为以lp点为圆心，其周围2000米范围
			/*
			 * List<LatLonPoint> list = new ArrayList<LatLonPoint>();
			 * list.add(lp);
			 * list.add(AMapUtil.convertToLatLonPoint(Constants.BEIJING));
			 * poiSearch.setBound(new SearchBound(list));// 设置多边形poi搜索范围
			 */
			poiSearch.searchPOIAsyn();// 异步搜索
		}
	}

	/**
	 * 点击下一页poi搜索
	 */
	public void nextSearch() {
		if (query != null && poiSearch != null && poiResult != null) {
			if (poiResult.getPageCount() - 1 > currentPage) {
				currentPage++;

				query.setPageNum(currentPage);// 设置查后一页
				poiSearch.searchPOIAsyn();
			} else {
				ToastUtil
						.show(getActivity(), R.string.no_result);
			}
		}
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
		if (poiOverlay != null && poiItems != null && poiItems.size() > 0) {
			detailMarker = marker;
			
			doSearchPoiDetail(poiItems.get(poiOverlay.getPoiIndex(marker))
					.getPoiId());

			mNaviPoint.setTitle(marker.getTitle());
			mNaviPoint.setNaviPoint(marker.getPosition().latitude, marker.getPosition().longitude);
			
		}
		return false;
	}

	@Override
	public View getInfoContents(Marker marker) {
		return null;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		return null;
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
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent == selectDeep) {
			deepType = itemDeep[position];

		} else if (parent == selectType) {
			tsearchType = position;
		}
		//nextButton.setClickable(false);// 改变搜索条件，需重新搜索
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		if (parent == selectDeep) {
			deepType = "餐饮";
		} else if (parent == selectType) {
			tsearchType = 0;
		}
		//nextButton.setClickable(false);// 改变搜索条件，需重新搜索
	}

	/**
	 * POI详情回调
	 */
	@Override
	public void onPoiItemDetailSearched(PoiItemDetail result, int rCode) {
		dissmissProgressDialog();// 隐藏对话框
		if (rCode == 0) {
			if (result != null) {// 搜索poi的结果
				if (detailMarker != null) {
					StringBuffer sb = new StringBuffer(result.getSnippet());
					if ((result.getGroupbuys() != null && result.getGroupbuys()
							.size() > 0)
							|| (result.getDiscounts() != null && result
									.getDiscounts().size() > 0)) {

						if (result.getGroupbuys() != null
								&& result.getGroupbuys().size() > 0) {// 取第一条团购信息
							sb.append("\n团购："
									+ result.getGroupbuys().get(0).getDetail());
						}
						if (result.getDiscounts() != null
								&& result.getDiscounts().size() > 0) {// 取第一条优惠信息
							sb.append("\n优惠："
									+ result.getDiscounts().get(0).getDetail());
						}
					} else {
						sb = new StringBuffer("地址：" + result.getSnippet()
								+ "\n电话：" + result.getTel() + "\n类型："
								+ result.getTypeDes());
					}
					// 判断poi搜索是否有深度信息
					if (result.getDeepType() != null) {
						sb = getDeepInfo(result, sb);
						detailMarker.setSnippet(sb.toString());
					} else {
						ToastUtil.show(getActivity(),
								"此Poi点没有深度信息");
					}
				}

			} else {
				ToastUtil
						.show(getActivity(), R.string.no_result);
			}
		} else if (rCode == 27) {
			ToastUtil
					.show(getActivity(), R.string.error_network);
		} else if (rCode == 32) {
			ToastUtil.show(getActivity(), R.string.error_key);
		} else {
			ToastUtil.show(getActivity(),getString(R.string.error_other) + rCode);
		}
	}

	/**
	 * POI深度信息获取
	 */
	private StringBuffer getDeepInfo(PoiItemDetail result,
			StringBuffer sbuBuffer) {
		switch (result.getDeepType()) {
		// 餐饮深度信息
		case DINING:
			if (result.getDining() != null) {
				Dining dining = result.getDining();
				sbuBuffer
						.append("\n菜系：" + dining.getTag() + "\n特色："
								+ dining.getRecommend() + "\n来源："
								+ dining.getDeepsrc());
			}
			break;
		// 酒店深度信息
		case HOTEL:
			if (result.getHotel() != null) {
				Hotel hotel = result.getHotel();
				sbuBuffer.append("\n价位：" + hotel.getLowestPrice() + "\n卫生："
						+ hotel.getHealthRating() + "\n来源："
						+ hotel.getDeepsrc());
			}
			break;
		// 景区深度信息
		case SCENIC:
			if (result.getScenic() != null) {
				Scenic scenic = result.getScenic();
				sbuBuffer
						.append("\n价钱：" + scenic.getPrice() + "\n推荐："
								+ scenic.getRecommend() + "\n来源："
								+ scenic.getDeepsrc());
			}
			break;
		// 影院深度信息
		case CINEMA:
			if (result.getCinema() != null) {
				Cinema cinema = result.getCinema();
				sbuBuffer.append("\n停车：" + cinema.getParking() + "\n简介："
						+ cinema.getIntro() + "\n来源：" + cinema.getDeepsrc());
			}
			break;
		default:
			break;
		}
		return sbuBuffer;
	}

	/**
	 * POI搜索回调方法
	 */
	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		dissmissProgressDialog();// 隐藏对话框
		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {// 搜索poi的结果
				if (result.getQuery().equals(query)) {// 是否是同一条
					poiResult = result;
					poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
					List<SuggestionCity> suggestionCities = poiResult
							.getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
					if (poiItems != null && poiItems.size() > 0) {
						aMap.clear();// 清理之前的图标
						poiOverlay = new PoiOverlay(aMap, poiItems);
						poiOverlay.removeFromMap();
						poiOverlay.addToMap();
						poiOverlay.zoomToSpan();

						//nextButton.setClickable(true);// 设置下一页可点
					} else if (suggestionCities != null
							&& suggestionCities.size() > 0) {
						showSuggestCity(suggestionCities);
					} else {
						ToastUtil.show(getActivity(),
								R.string.no_result);
					}
				}
			} else {
				ToastUtil
						.show(getActivity(), R.string.no_result);
			}
		} else if (rCode == 27) {
			ToastUtil
					.show(getActivity(), R.string.error_network);
		} else if (rCode == 32) {
			ToastUtil.show(getActivity(), R.string.error_key);
		} else {
			ToastUtil.show(getActivity(),getString(R.string.error_other) + rCode);
		}
	}

	@Override
	public void onMapClick(LatLng latng) {
	/*	locationMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 1)
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.point))
				.position(latng).title("点击选择为中心点"));
		locationMarker.showInfoWindow();*/
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		/*locationMarker.hideInfoWindow();
		lp = new LatLonPoint(locationMarker.getPosition().latitude,
				locationMarker.getPosition().longitude);
		locationMarker.destroy();
	*/
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/**
		 * 点击标记按钮
		 */
		////case R.id.locationButton:
			//aMap.clear();// 清理所有marker
			//registerListener();
			//break;
		/**
		 * 点击搜索按钮
		 */
		case R.id.searchButton:
			doSearchQuery();
			break;
		/**
		 * 点击下一页按钮
		 */
		//case R.id.nextButton:
			//nextSearch();
			//break;
			
		case R.id.gohere:
			startNavi();
		
		
		default:
			break;
		}
	}
	
	public void startNavi() {
		if(mNaviPoint!=null) {
			mBTAction.sendMessage(mNaviPoint);	
		}			
	}

	
}

