package com.tr.baidumapsdk;

import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

//定位接口通知事件类
public interface BaiduLocListener {

	// 位置改变通知
	public void onLocationChanged(boolean result, ReverseGeoCodeResult geoResult);

}
