package com.tongmeng.alliance.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

//import com.amap.api.location.AMapLocation;
//import com.amap.api.location.AMapLocationClient;
//import com.amap.api.location.AMapLocationClientOption;
//import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
//import com.amap.api.maps.AMap;
//import com.amap.api.maps.CameraUpdateFactory;
//import com.amap.api.maps.MapView;
//import com.amap.api.maps.LocationSource.OnLocationChangedListener;
//import com.amap.api.maps.model.BitmapDescriptorFactory;
//import com.amap.api.maps.model.CameraPosition;
//import com.amap.api.maps.model.LatLng;
//import com.amap.api.maps.model.Marker;
//import com.amap.api.maps.model.MarkerOptions;
//import com.amap.api.services.core.LatLonPoint;
//import com.amap.api.services.geocoder.GeocodeAddress;
//import com.amap.api.services.geocoder.GeocodeQuery;
//import com.amap.api.services.geocoder.GeocodeResult;
//import com.amap.api.services.geocoder.GeocodeSearch;
//import com.amap.api.services.geocoder.RegeocodeQuery;
//import com.amap.api.services.geocoder.RegeocodeResult;
//import com.lidroid.xutils.ViewUtils;
//import com.tongmeng.alliance.util.AMapUtil;
import com.tr.R;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;

public class ActionMapActivity 
//	extends JBaseActivity implements
//		OnClickListener, OnMapLongClickListener, OnGeocodeSearchListener,
//		LocationSource, AMapLocationListener, OnCameraChangeListener 
{
//
//	MapView mapView;
//	private AMap aMap;
//	private LatLonPoint latLonPoint;
//	private GeocodeSearch geocoderSearch;
//	private OnLocationChangedListener mListener;
//	private AMapLocationClient mlocationClient;
//	private AMapLocationClientOption mLocationOption;
//	private Marker geoMarker;
//	private Marker regeoMarker;
//	Button searchBtn, sureBtn;
//	EditText edittext;
//	LinearLayout linearLayout;
//	TextView textview;
//
//	private String addressName, province, city, district, detailStr, positionX,
//			positionY;
//
//	@Override
//	public void initJabActionBar() {
//		// TODO Auto-generated method stub
//		setContentView(R.layout.mymap);
//		initTitle();
//		initView();
//	}
//
//	private void initView() {
//		// TODO Auto-generated method stub
//		edittext = (EditText) findViewById(R.id.mymap_edittext1);
//		linearLayout = (LinearLayout) findViewById(R.id.mymap_locaionlayout);
//		linearLayout.setOnClickListener(this);
//		sureBtn = (Button) findViewById(R.id.mymap_sureBtn);
//		sureBtn.setOnClickListener(this);
//		textview = (TextView) findViewById(R.id.mymap_showText);
//		edittext.setOnEditorActionListener(new OnEditorActionListener() {
//
//			@Override
//			public boolean onEditorAction(TextView v, int actionId,
//					KeyEvent event) {
//				// TODO Auto-generated method stub
//				Log.e("", "actionId:" + actionId);
//				if (actionId == EditorInfo.IME_ACTION_DONE) {
//					String path = edittext.getText().toString();
//					if (path == null || "".equals(path)) {
//						Toast.makeText(ActionMapActivity.this, "请输入您要查询的地址", 0)
//								.show();
//					} else {
//						GeocodeQuery query = new GeocodeQuery(path, null);// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
//						geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
//					}
//				}
//				return false;
//			}
//		});
//		// 地图
//		mapView = (MapView) findViewById(R.id.mymap_map);
//		mapView.onCreate(savedInstanceState);
//		if (aMap == null) {
//			aMap = mapView.getMap();
//			// aMap.setOnMapClickListener(this);// 对amap添加单击地图事件监听器
//			aMap.setOnMapLongClickListener(this);// 对amap添加长按地图事件监听器
//			aMap.setOnCameraChangeListener(this);// 对amap添加移动地图事件监听器
//			// aMap.setOnMapTouchListener(this);// 对amap添加触摸地图事件监听器
//
//			aMap.setLocationSource(this);// 设置定位监听
//			aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
//			aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
//			// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
//			aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
//
//			geoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
//					.icon(BitmapDescriptorFactory
//							.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
//			regeoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
//					.icon(BitmapDescriptorFactory
//							.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//		}
//		geocoderSearch = new GeocodeSearch(this);
//		geocoderSearch.setOnGeocodeSearchListener(this);
//	}
//
//	private void initTitle() {
//		// TODO Auto-generated method stub
//		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "选择活动地点",
//				false, null, false, true);
//	}
//
//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		switch (v.getId()) {
//		case R.id.mymap_locaionlayout:
//			// 获取自己坐标点，标出坐标
//			// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
//			aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
//			break;
//		case R.id.mymap_sureBtn:
//			Intent intent1 = new Intent();
//			if (TextUtils.equals("", textview.getText().toString())
//					|| TextUtils.equals(null, textview.getText().toString())) {
//				Toast.makeText(ActionMapActivity.this, "请输入或长按地图，获取您的活动地点", 0);
//			}
//			intent1.putExtra("addressName", textview.getText().toString());
//			intent1.putExtra("province", province);
//			intent1.putExtra("city", city);
//			intent1.putExtra("detailStr", detailStr);
//			intent1.putExtra("positionX", positionX);
//			intent1.putExtra("positionY", positionY);
//			Log.e("MyMapActivity::", "addressName:"
//					+ textview.getText().toString());
//			Log.e("MyMapActivity", "province::" + province);
//			Log.e("MyMapActivity", "city::" + city);
//			Log.e("MyMapActivity", "detailStr::" + detailStr);
//			Log.e("MyMapActivity", "positionX::" + positionX);
//			Log.e("MyMapActivity", "positionY::" + positionY);
//			setResult(RESULT_OK, intent1);
//			finish();
//			break;
//		}
//	}
//
//	/**
//	 * 方法必须重写
//	 */
//	@Override
//	protected void onResume() {
//		super.onResume();
//		mapView.onResume();
//	}
//
//	/**
//	 * 方法必须重写
//	 */
//	@Override
//	protected void onPause() {
//		super.onPause();
//		mapView.onPause();
//		deactivate();
//	}
//
//	/**
//	 * 方法必须重写
//	 */
//	@Override
//	protected void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//		mapView.onSaveInstanceState(outState);
//	}
//
//	/**
//	 * 方法必须重写
//	 */
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		mapView.onDestroy();
//		if (null != mlocationClient) {
//			mlocationClient.onDestroy();
//		}
//	}
//
//	/**
//	 * 定位成功后回调函数
//	 */
//	@Override
//	public void onLocationChanged(AMapLocation amapLocation) {
//		if (mListener != null && amapLocation != null) {
//			if (amapLocation != null && amapLocation.getErrorCode() == 0) {
//				Log.e("", "定位成功");
//				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
//			} else {
//				String errText = "定位失败," + amapLocation.getErrorCode() + ": "
//						+ amapLocation.getErrorInfo();
//				Log.e("AmapErr", errText);
//				// Toast.makeText(this, errText, 0).show();
//			}
//		}
//	}
//
//	/**
//	 * 激活定位
//	 */
//	@Override
//	public void activate(OnLocationChangedListener listener) {
//		// TODO Auto-generated method stub
//		mListener = listener;
//		if (mlocationClient == null) {
//			mlocationClient = new AMapLocationClient(this);
//			mLocationOption = new AMapLocationClientOption();
//			// 设置定位监听
//			mlocationClient.setLocationListener(this);
//			// 设置为高精度定位模式
//			mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
//			// 设置定位参数
//			mlocationClient.setLocationOption(mLocationOption);
//			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
//			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
//			// 在定位结束后，在合适的生命周期调用onDestroy()方法
//			// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//			mlocationClient.startLocation();
//		}
//	}
//
//	/**
//	 * 停止定位
//	 */
//	@Override
//	public void deactivate() {
//		mListener = null;
//		if (mlocationClient != null) {
//			mlocationClient.stopLocation();
//			mlocationClient.onDestroy();
//		}
//		mlocationClient = null;
//	}
//
//	@Override
//	public void onMapLongClick(LatLng point) {
//		// TODO Auto-generated method stub
//		positionX = point.latitude + "";
//		positionY = point.longitude + "";
//
//		Log.e("", "positionX:" + positionX);
//		Log.e("", "positionY:" + positionY);
//
//		latLonPoint = new LatLonPoint(point.latitude, point.longitude);
//		RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
//				GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
//		geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
//	}
//
//	@Override
//	public void onGeocodeSearched(GeocodeResult result, int rCode) {
//		// TODO Auto-generated method stub
//		if (rCode == 0) {
//			if (result != null && result.getGeocodeAddressList() != null
//					&& result.getGeocodeAddressList().size() > 0) {
//				GeocodeAddress address = result.getGeocodeAddressList().get(0);
//				aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//						AMapUtil.convertToLatLng(address.getLatLonPoint()), 15));
//				geoMarker.setPosition(AMapUtil.convertToLatLng(address
//						.getLatLonPoint()));
//				addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
//						+ address.getFormatAddress();
//				Log.e("", "addressName::" + addressName);
//				textview.setText(address.getFormatAddress());
//				// Toast.makeText(this, addressName, 0).show();
//			} else {
//				Toast.makeText(this, "对不起，没有搜索到相关数据！", 0).show();
//			}
//		} else if (rCode == 27) {
//			Toast.makeText(this, "搜索失败,请检查网络连接！", 0).show();
//		} else if (rCode == 32) {
//			Toast.makeText(this, "key验证无效！", 0).show();
//
//		} else {
//			Toast.makeText(this, "未知错误，请稍后重试!错误码为" + rCode, 0).show();
//		}
//	}
//
//	@Override
//	public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
//		// TODO Auto-generated method stub
//		if (rCode == 0) {
//			if (result != null && result.getRegeocodeAddress() != null
//					&& result.getRegeocodeAddress().getFormatAddress() != null) {
//				addressName = result.getRegeocodeAddress().getFormatAddress();
//
//				textview.setText(addressName);
//				province = result.getRegeocodeAddress().getProvince();
//				city = result.getRegeocodeAddress().getCity();
//				district = result.getRegeocodeAddress().getDistrict();
//				if (TextUtils.equals("", city) || TextUtils.equals(null, city)) {
//					city = district;
//				}
//				detailStr = addressName.substring(addressName.indexOf(city)
//						+ city.length(), addressName.length());
//				Toast.makeText(this, addressName, 0).show();
//				Log.e("", "addressName::" + addressName);
//			} else {
//				Toast.makeText(this, "对不起，没有搜索到相关数据！", 0).show();
//			}
//		} else if (rCode == 27) {
//			Toast.makeText(this, "搜索失败,请检查网络连接！", 0).show();
//
//		} else if (rCode == 32) {
//			Toast.makeText(this, "key验证无效！", 0).show();
//
//		} else {
//			Toast.makeText(this, "未知错误，请稍后重试!错误码为" + rCode, 0).show();
//		}
//	}
//
//	@Override
//	public void onCameraChange(CameraPosition cameraPosition) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onCameraChangeFinish(CameraPosition cameraPosition) {
//		// TODO Auto-generated method stub
//
//	}
}
