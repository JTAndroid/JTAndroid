package com.tr.baidumapsdk;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

public class BaiduLoc implements OnGetGeoCoderResultListener {

	private LocationClient mLocationClient;
	private MyLocationListener mMyLocationListener;
	private BDLocation mLocation;
	private BaiduLocListener mBaiduLocListener;
	private GeoCoder mSearch;
	private String mAddress;
	private List<PoiInfo> mPois = null;

	public BaiduLoc(Context context) {
		try {
			SDKInitializer.initialize(context);

			mLocationClient = new LocationClient(context);
			mMyLocationListener = new MyLocationListener();
			mLocationClient.registerLocationListener(mMyLocationListener);

			LocationClientOption option = new LocationClientOption();
			option.setLocationMode(LocationMode.Battery_Saving);
			option.setCoorType("bd09ll");
			option.setScanSpan(3000);
			option.setIsNeedAddress(true);
			mLocationClient.setLocOption(option);

			mSearch = GeoCoder.newInstance();
			mSearch.setOnGetGeoCodeResultListener(this);

			startLocation();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void startLocation() {
		try {
			if (null != mLocationClient) {
				mLocationClient.start();
				mLocationClient.requestLocation();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stopLocation() {
		try {
			if (null != mLocationClient) {
				mLocationClient.stop();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getAddress() {

		if (null != mAddress) {
			return mAddress;
		} else {
			return "";
		}

	}

	public List<PoiInfo> getPois() {
		if (null != mPois) {
			return mPois;
		}
		return null;
	}

	public double getLatitude() {
		try {
			if (null != mLocation) {
				return mLocation.getLatitude();
			} else {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public double getLongitude() {
		try {
			if (null != mLocation) {
				return mLocation.getLongitude();
			} else {
				return 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void setBaiduLocListener(BaiduLocListener baiduLocListener) {
		mBaiduLocListener = baiduLocListener;

	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {

			mLocation = location;
			if (isLocationValid(getLatitude(), getLongitude()) && null != mSearch) {
				mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(mLocation.getLatitude(), mLocation.getLongitude())));
			} else {
				startLocation();
			}
		}

	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {

	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
		if (arg0 == null || arg0.error != SearchResult.ERRORNO.NO_ERROR) {
			if (null != mBaiduLocListener) {
				mBaiduLocListener.onLocationChanged(false, null);
			}
			return;
		}

		if (null == arg0.getAddressDetail().province && null == arg0.getAddressDetail().city && null == arg0.getAddressDetail().district
				&& null == arg0.getAddressDetail().street && null == arg0.getAddressDetail().streetNumber) {
			if (null != mBaiduLocListener) {
				mBaiduLocListener.onLocationChanged(false, null);
			}
			return;
		}

		if (TextUtils.isEmpty(arg0.getAddressDetail().province) && TextUtils.isEmpty(arg0.getAddressDetail().city)
				&& TextUtils.isEmpty(arg0.getAddressDetail().district) && TextUtils.isEmpty(arg0.getAddressDetail().street)
				&& TextUtils.isEmpty(arg0.getAddressDetail().streetNumber)) {
			if (null != mBaiduLocListener) {
				mBaiduLocListener.onLocationChanged(false, null);
			}
			return;
		}

		mAddress = arg0.getAddressDetail().province;
		mAddress += arg0.getAddressDetail().city;
		mAddress += arg0.getAddressDetail().district;
		mAddress += arg0.getAddressDetail().street;
		mAddress += arg0.getAddressDetail().streetNumber;

		if (mAddress.endsWith("0") || mAddress.endsWith("1") || mAddress.endsWith("2") || mAddress.endsWith("3") || mAddress.endsWith("4")
				|| mAddress.endsWith("5") || mAddress.endsWith("6") || mAddress.endsWith("7") || mAddress.endsWith("8") || mAddress.endsWith("9")) {
			mAddress += "号";

		}

		mPois = arg0.getPoiList();

		if (null != mBaiduLocListener) {
			mBaiduLocListener.onLocationChanged(true, arg0);
		}
		stopLocation();
	}

	public boolean isLocationValid(double lng, double lat) {
		if (lng == 4.9E-324 || lat == 4.9E-324) {
			return false;
		}
		if (lng == 0 || lat == 0) {
			return false;
		}
		return true;
	}

	public BDLocation getLocation() {
		return mLocation;
	}
}