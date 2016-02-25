package com.tr.baidumapsdk;

import java.util.ArrayList;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.LinearLayout;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMapLoadedCallback;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMap.SnapshotReadyCallback;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

public class BaiduMapView implements OnGetPoiSearchResultListener, OnMapStatusChangeListener, OnMapClickListener, OnGetSuggestionResultListener {

	private MapView mMapView = null;
	private BaiduMap mBaiduMap = null;

	private MyMarker mMyself = null;

	private ArrayList<MyMarker> mMarkers = null;

	private BaiduMapListener mListener = null;

	private LinearLayout mLayout = null;

	private PoiSearch mPoiSearch = null;

	private SuggestionSearch mSuggestionSearch = null;

	private Activity mAct = null;

	public BaiduMapView(Activity myActivity, int resId, boolean canMove, boolean canZoom) {
		try {

			BaiduMapOptions bo = new BaiduMapOptions().compassEnabled(false).overlookingGesturesEnabled(false).rotateGesturesEnabled(false)
					.scaleControlEnabled(false).scrollGesturesEnabled(canMove).zoomControlsEnabled(false).zoomGesturesEnabled(canZoom);
			mMapView = new MapView(myActivity, bo);
			mLayout = (LinearLayout) myActivity.findViewById(resId);
			mLayout.setVisibility(View.VISIBLE);
			mLayout.addView(mMapView);

			mBaiduMap = mMapView.getMap();
			MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(16.0f);
			mBaiduMap.setMapStatus(msu);

			mBaiduMap.setOnMapLoadedCallback(new OnMapLoadedCallback() {

				public void onMapLoaded() {
					if (null != mListener) {
						mListener.onMapLoaded();
					}
				}
			});
			mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

				public boolean onMarkerClick(Marker marker) {
					if (null != mMyself) {
						if (marker == mMyself.mMarker) {
							if (null != mListener) {
								mListener.onMarkerClick(mMyself);

							}
						}
					}
					int count = mMarkers.size();
					for (int i = 0; i < count; i++) {
						MyMarker mymarker = mMarkers.get(i);
						if (marker == mymarker.mMarker) {
							if (null != mListener) {
								mListener.onMarkerClick(mymarker);

							}
						}
					}

					return true;
				}
			});
			mBaiduMap.setOnMapClickListener(this);
			mBaiduMap.setOnMapStatusChangeListener(this);

			mPoiSearch = PoiSearch.newInstance();
			mPoiSearch.setOnGetPoiSearchResultListener(this);

			mSuggestionSearch = SuggestionSearch.newInstance();
			mSuggestionSearch.setOnGetSuggestionResultListener(this);

			mMarkers = new ArrayList<MyMarker>();

			mAct = myActivity;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public LinearLayout getLayout() {
		return mLayout;
	}

	// 地图缩放级别 3~19
	public void setZoomLevel(float level) {
		mBaiduMap = mMapView.getMap();
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(level);
		mBaiduMap.setMapStatus(msu);
	}

	public void setCenterOnly(double latitude, double longitude) {
		mBaiduMap = mMapView.getMap();
		LatLng location = new LatLng(latitude, longitude);
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(location));
	}

	public void setCenter(double latitude, double longitude, int resid) {
		try {
			if (null != mBaiduMap && 0 != resid) {
				LatLng location = new LatLng(latitude, longitude);
				mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(location));

				mMyself = new MyMarker();
				mMyself.mName = "";
				mMyself.mBitmap = BitmapDescriptorFactory.fromResource(resid);

				OverlayOptions oop = new MarkerOptions().position(new LatLng(latitude, longitude)).icon(mMyself.mBitmap);
				mMyself.mMarker = (Marker) mBaiduMap.addOverlay(oop);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setCenter(double latitude, double longitude, Bitmap bitmap) {
		try {
			if (null != mBaiduMap && null != bitmap) {
				LatLng location = new LatLng(latitude, longitude);
				mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(location));

				mMyself = new MyMarker();
				mMyself.mName = "";
				mMyself.mBitmap = BitmapDescriptorFactory.fromBitmap(bitmap);

				OverlayOptions oop = new MarkerOptions().position(new LatLng(latitude, longitude)).icon(mMyself.mBitmap);
				mMyself.mMarker = (Marker) mBaiduMap.addOverlay(oop);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setCenter(double latitude, double longitude, View popview) {
		try {
			if (null != mBaiduMap && null != popview) {
				LatLng location = new LatLng(latitude, longitude);
				mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(location));

				mMyself = new MyMarker();
				mMyself.mName = "";
				mMyself.mBitmap = BitmapDescriptorFactory.fromView(popview);

				OverlayOptions oop = new MarkerOptions().position(new LatLng(latitude, longitude)).icon(mMyself.mBitmap);
				mMyself.mMarker = (Marker) mBaiduMap.addOverlay(oop);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void focusMaker(int id, double latitude, double longitude) {
		for (int i = 0; i < mMarkers.size(); i++) {
			MyMarker mm = mMarkers.get(i);
			if (null != mm) {
				if (mm.mTag == id) {
					if (null != mm.mMarker) {
						mm.mMarker.setZIndex(mMarkers.size());
					}
				} else {
					if (null != mm.mMarker) {
						mm.mMarker.setZIndex(i);
					}
				}
			}
		}
	}

	public void focusAndMoveMarker(int id, double latitude, double longitude) {
		LatLng location = new LatLng(latitude, longitude);
		for (int i = 0; i < mMarkers.size(); i++) {
			MyMarker mm = mMarkers.get(i);
			if (null != mm) {
				if (mm.mTag == id) {
					if (null != mm.mMarker) {
						mm.mMarker.setZIndex(mMarkers.size());
						mm.mMarker.setPosition(location);
					}
				} else {
					if (null != mm.mMarker) {
						mm.mMarker.setZIndex(i);
					}
				}
			}
		}
	}

	public void setListener(BaiduMapListener listenter) {
		mListener = listenter;
	}

	public void clearPoi() {
		try {
			if (null != mBaiduMap) {
				mBaiduMap.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addPeoplePoi(int id, double latitude, double longitude, View popview) {
		try {
			if (null != mBaiduMap) {
				MyMarker newMarker = new MyMarker();
				newMarker.mName = "";
				newMarker.mBitmap = BitmapDescriptorFactory.fromView(popview);
				newMarker.mTag = id;

				OverlayOptions oop = new MarkerOptions().position(new LatLng(latitude, longitude)).icon(newMarker.mBitmap);
				newMarker.mMarker = (Marker) mBaiduMap.addOverlay(oop);

				if (null != mMarkers) {
					mMarkers.add(newMarker);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addPeoplePoi(int id, double latitude, double longitude, Bitmap bitmap) {
		try {
			if (null != mBaiduMap) {
				MyMarker newMarker = new MyMarker();
				newMarker.mName = "";
				newMarker.mBitmap = BitmapDescriptorFactory.fromBitmap(bitmap);
				newMarker.mTag = id;

				OverlayOptions oop = new MarkerOptions().position(new LatLng(latitude, longitude)).icon(newMarker.mBitmap);
				newMarker.mMarker = (Marker) mBaiduMap.addOverlay(oop);

				if (null != mMarkers) {
					mMarkers.add(newMarker);
				}
			}
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int iSearchKind = 1;
	private PoiNearbySearchOption opt = null;
	private PoiCitySearchOption opt2 = null;

	public void searchByString(String keyword, double latitude, double longitude) {
		try {
			opt = new PoiNearbySearchOption();
			opt.location(new LatLng(latitude, longitude));
			opt.keyword(keyword);
			opt.radius(2000);
			opt.pageCapacity(100);			

			opt2 = new PoiCitySearchOption();
			opt2.city("");
			opt2.keyword(keyword);
			opt2.pageCapacity(100);
			opt2.pageNum(0);

			iSearchKind = 1;

			if (null != mPoiSearch) {
				mPoiSearch.searchInCity(opt2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void searchByString2(String keyword, double latitude, double longitude) {
		try {
			opt = new PoiNearbySearchOption();
			opt.location(new LatLng(latitude, longitude));
			opt.keyword(keyword);
			opt.radius(2000);
			opt.pageCapacity(100);
			opt.pageNum(0);
			
			iSearchKind = 2;

			if (null != mPoiSearch) {
				mPoiSearch.searchNearby(opt);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void reSearch() {
		iSearchKind = 2;
		if (null != mPoiSearch) {
			mPoiSearch.searchNearby(opt);
		}
	}

	public void searchSuggestion(String keyword) {
		if (null != mSuggestionSearch) {
			mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(keyword));
		}
	}

	public void addSearchPoi(int id, PoiInfo info, int resid) {
		try {
			if (null != mBaiduMap) {
				MyMarker newMarker = new MyMarker();
				newMarker.mName = info.name;
				newMarker.mCity = info.city;
				newMarker.mAddr = info.address;
				newMarker.mPhone = info.phoneNum;

				newMarker.mBitmap = BitmapDescriptorFactory.fromResource(resid);
				newMarker.mTag = id;

				OverlayOptions oop = new MarkerOptions().position(info.location).icon(newMarker.mBitmap);
				newMarker.mMarker = (Marker) mBaiduMap.addOverlay(oop);

				if (null != mMarkers) {
					mMarkers.add(newMarker);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void snapshot() {
		try {
			if (null != mBaiduMap) {
				mBaiduMap.snapshot(new SnapshotReadyCallback() {

					public void onSnapshotReady(Bitmap snapshot) {
						if (null != mListener) {
							mListener.onSnapshotReady(snapshot);
						}
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onPause() {
		if (null != mMapView) {
			mMapView.onPause();
		}
	}

	public void onResume() {
		if (null != mMapView) {
			mMapView.onResume();
		}
	}

	public void onDestroy() {
		if (null != mPoiSearch) {
			mPoiSearch.destroy();
		}

		if (null != mSuggestionSearch) {
			mSuggestionSearch.destroy();
		}

		if (null != mBaiduMap) {
			mBaiduMap.clear();
		}

		if (null != mMyself) {
			if (null != mMyself.mBitmap) {
				mMyself.mBitmap.recycle();
			}
		}
		if (null != mMarkers) {
			int count = mMarkers.size();
			for (int i = 0; i < count; i++) {
				MyMarker tmp = mMarkers.get(i);
				if (null != tmp) {
					if (null != tmp.mBitmap) {
						tmp.mBitmap.recycle();
					}
				}
			}
			mMarkers.clear();
		}

	}

	public class MyMarker {
		public Marker mMarker;
		public BitmapDescriptor mBitmap;
		public String mName;
		public String mCity;
		public String mAddr;
		public String mPhone;
		public int mTag;
	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult result) {

		if (result.error != SearchResult.ERRORNO.NO_ERROR) {

		} else {

		}
	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			if (1 == iSearchKind) {
				reSearch();
			} else if (null != mListener) {
				mListener.onSearched(result);
			}
			return;
		}

		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			if (null == result.getAllPoi()) {
				if (1 == iSearchKind) {
					reSearch();
				} else if (null != mListener) {
					mListener.onSearched(result);
				}
				return;
			}
			if (0 == result.getAllPoi().size()) {
				if (1 == iSearchKind) {
					reSearch();
				} else if (null != mListener) {
					mListener.onSearched(result);
				}
				return;
			}
			if (null != mListener) {
				mListener.onSearched(result);
			}
			return;
		}

		if (null == result.getAllPoi()) {
			if (1 == iSearchKind) {
				reSearch();
			}
			return;
		}
		if (0 == result.getAllPoi().size()) {
			if (1 == iSearchKind) {
				reSearch();
			}
			return;
		}
	}

	@Override
	public void onGetSuggestionResult(SuggestionResult arg0) {
		if (arg0 == null || arg0.getAllSuggestions() == null) {
			return;
		}
		if (null != mListener) {
			mListener.onSearchSuggestion(arg0);
		}
	}

	@Override
	public void onMapStatusChange(MapStatus arg0) {
	}

	@Override
	public void onMapStatusChangeFinish(MapStatus arg0) {
		if (null != mListener) {
			mListener.OnMapChanged(arg0);
		}
	}

	@Override
	public void onMapStatusChangeStart(MapStatus arg0) {

	}

	@Override
	public void onMapClick(LatLng arg0) {
		if (null != mListener) {
			mListener.OnMapClick();
		}
	}

	@Override
	public boolean onMapPoiClick(MapPoi arg0) {
		return false;
	}
}