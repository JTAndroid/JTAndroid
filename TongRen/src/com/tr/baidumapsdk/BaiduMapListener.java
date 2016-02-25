package com.tr.baidumapsdk;

//import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.tr.baidumapsdk.BaiduMapView.MyMarker;
//import com.tr.baidumapsdk.BaiduMapView.MyMarker;

import android.graphics.Bitmap;

//地图接口通知事件类
public interface BaiduMapListener {

	// 地图加载完毕
	public void onMapLoaded();

	// 搜索结果回调
	public void onSearched(PoiResult result);
	
	// 搜索结果回调
	public void onSearchSuggestion(SuggestionResult result);	

	// 点击覆盖物
	public void onMarkerClick(MyMarker mymarker);

	// 截图完成
	public void onSnapshotReady(Bitmap snapshot);
	
	//地图被移动
	public void OnMapChanged(MapStatus arg0);
	
	//地图点击
	public void OnMapClick();
}
