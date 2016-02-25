package com.tr.ui.work;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.tr.App;
import com.tr.R;
import com.tr.model.conference.MMapAddress;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.conference.home.MeetingAttendInfomationActivity;
import com.tr.ui.conference.initiatorhy.InitiatorHYActivity;
import com.tr.ui.conference.initiatorhy.LocationActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.time.Util;

/**
 * 地点
 * 
 * @author Administrator
 * 
 */
public class WorkNewLocationActivity extends JBaseActivity {

	String mLocationText;

	MapView mMapView;
	BaiduMap mBaiduMap;

	// 定位
	LocationClient mLocationClient = null;
	MyBDLocationListner mListner = null;
	BitmapDescriptor mCurrentMarker = null;

	// 设置第一次定位标志
	boolean isFirstLoc = true;

	// 地理编码
	GeoCoder mGeoCoder = null;

	// 位置列表
	XListView mListView;
	PlaceListAdapter mAdapter;
	List<PoiInfo> mInfoList;
	PoiInfo mCurentInfo;

	private String city;

	private final static int requestCode_address = 100;

	private MMapAddress mapAddress;


	private LinearLayout rootLl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.work_new_location_activity);
		mLocationText = getIntent().getStringExtra("Location");
		isWorkNewActivity = getIntent().getBooleanExtra("WorkNewActivity", false);
		rootLl = (LinearLayout) findViewById(R.id.rootLl);
		mMapView = new MapView(WorkNewLocationActivity.this);
		mBaiduMap = mMapView.getMap();

		// 初始化POI信息列表
		mInfoList = new ArrayList<PoiInfo>();

		// 定位
		mBaiduMap.setMyLocationEnabled(true);
		mLocationClient = new LocationClient(this);
		mListner = new MyBDLocationListner();
		mLocationClient.registerLocationListener(mListner);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
		mLocationClient.start();

		mGeoCoder = GeoCoder.newInstance();
		mGeoCoder.setOnGetGeoCodeResultListener(GeoListener);
		rootLl.setBackgroundResource(R.color.im_gray);
		initView();
		initData();
		showLoadingDialog();
		if (!isOPenGps(this)) {
			isFirstLoc = false;
			dismissLoadingDialog();
			mCurentInfo = new PoiInfo();
			mCurentInfo.address = "";
			mCurentInfo.name = "不显示位置";
			mLocationText = "不显示位置";
			mInfoList.add(mCurentInfo);
			mAdapter.mList = mInfoList;
			mAdapter.notifyDataSetChanged();
			rootLl.setBackgroundResource(R.drawable.location_empty);
		}
	}

	public boolean isOPenGps(Context context) {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
		boolean gps = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		// 获取系统服务
		ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		// 获取状态
		State wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		if (gps || wifi == State.CONNECTED || wifi == State.CONNECTING) {
			return true;
		}
		return false;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_new_main, menu);
		menu.findItem(R.id.home_new_menu_more).setVisible(false);
//		if(isWorkNewActivity){
//			menu.findItem(R.id.home_new_menu_search).setVisible(false);
//		}else {
//			menu.findItem(R.id.home_new_menu_search).setVisible(true);
//		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.home_new_menu_search:// 搜索地址
			Intent intent = new Intent(this, LocationActivity.class);
			startActivity(intent);
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void initView() {
		mListView = (XListView) findViewById(R.id.locationLv);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			switch (requestCode) {

			default:
				break;
			}
		}
	}

	public void initData() {
		mListView.setOnItemClickListener(itemClickListener);
		mAdapter = new PlaceListAdapter(getLayoutInflater(), mInfoList);
		mListView.setAdapter(mAdapter);
		mListView.setPullRefreshEnable(false);
		mListView.setPullLoadEnable(false);

		mListView.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
			}

			@Override
			public void onLoadMore() {
				mGeoCoder.reverseGeoCode((new ReverseGeoCodeOption())
						.location(mInfoList.get(mInfoList.size() - 1).location));
			}
		});
	}

	OnItemClickListener itemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Intent intent = getIntent();
			if (position == 1) {
				intent.putExtra("Location", "");
			} else {
				PoiInfo info = (PoiInfo) mAdapter.getItem(position - 1);
				intent.putExtra("Location", info.name);
				// <<<<<<< HEAD
				// }
				// setResult(1000,intent);
				// finish();
				// }
				// =======
				intent.putExtra("latitude", info.location.latitude);
				intent.putExtra("longitude", info.location.longitude);
				intent.putExtra("longitudeId", info.uid);
				intent.putExtra("detailName", info.address);
				intent.putExtra("secondLevel", city);
				
			}
			setResult(1000, intent);
			finish();
		}
	};

	@Override
	public void initJabActionBar() {// ActionBar的设置
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar(), "地点",
				false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	// 地理编码监听器
	OnGetGeoCoderResultListener GeoListener = new OnGetGeoCoderResultListener() {
		public void onGetGeoCodeResult(GeoCodeResult result) {
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				dismissLoadingDialog();
				Toast.makeText(WorkNewLocationActivity.this,
						"暂时无法获取您的地理位置，请搜索地点，或开启定位服务", Toast.LENGTH_SHORT)
						.show();// 没有检索到结果
			} else {
				Toast.makeText(WorkNewLocationActivity.this, "获取地理编码结果  ",
						Toast.LENGTH_SHORT).show();
			}
			// 获取地理编码结果
		}

		@Override
		public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
			dismissLoadingDialog();
			mListView.stopLoadMore();
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.makeText(WorkNewLocationActivity.this,
						"暂时无法获取您的地理位置，请搜索地点，或开启定位服务", Toast.LENGTH_SHORT)
						.show(); // 没有找到检索结果
			}
			// 获取反向地理编码结果
			else {
				// 将周边信息加入表
				if (result.getPoiList() != null) {
					mInfoList.addAll(result.getPoiList());
					removeDuplicate(mInfoList);
					// 通知适配数据已改变
					mAdapter.notifyDataSetChanged();
					mListView.setPullLoadEnable(true);
				} else {
					mListView.setPullLoadEnable(false);
				}

			}
		}
	};

	private boolean isWorkNewActivity;

	// 定位监听器
	private class MyBDLocationListner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// 当前经纬度
			double mLantitude = location.getLatitude();
			double mLongtitude = location.getLongitude();
			LatLng mLoactionLatLng = new LatLng(mLantitude, mLongtitude);
			String city = location.getCity();

			// 是否第一次定位
			if (isFirstLoc) {
				isFirstLoc = false;

				mCurentInfo = new PoiInfo();
				mCurentInfo.address = "";
				mCurentInfo.location = mLoactionLatLng;
				mCurentInfo.name = "不显示位置";
				if (TextUtils.isEmpty(mLocationText)) {
					mLocationText = "不显示位置";
				}
				mInfoList.add(mCurentInfo);
				mGeoCoder.reverseGeoCode((new ReverseGeoCodeOption())
						.location(mLoactionLatLng));
				return;
			}

		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mLocationClient.stop();
		mGeoCoder.destroy();
	}

	public class PlaceListAdapter extends BaseAdapter {

		List<PoiInfo> mList;
		LayoutInflater mInflater;

		private class MyViewHolder {
			TextView placeName;
			TextView placeAddree;
			ImageView imageview;
		}

		public PlaceListAdapter(LayoutInflater mInflater, List<PoiInfo> mList) {
			super();
			this.mList = mList;
			this.mInflater = mInflater;
		}

		public void clear() {
			this.mList.clear();
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			MyViewHolder holder;
			if (convertView == null) {
				System.out.println("----aa-");
				convertView = mInflater.inflate(
						R.layout.work_new_location_item, parent, false);
				holder = new MyViewHolder();
				holder.placeName = (TextView) convertView
						.findViewById(R.id.nameTv);
				holder.placeAddree = (TextView) convertView
						.findViewById(R.id.addressTv);
				holder.imageview = (ImageView) convertView
						.findViewById(R.id.ImageView);
				convertView.setTag(holder);
			} else {
				holder = (MyViewHolder) convertView.getTag();
			}

			holder.placeName.setText(mList.get(position).name);
			holder.placeAddree.setText(mList.get(position).address);
			if (mLocationText != null) {

				if (mLocationText.equals(mList.get(position).name)) {
					holder.imageview.setVisibility(View.VISIBLE);
				} else {
					holder.imageview.setVisibility(View.GONE);
				}
			}
			return convertView;
		}
	}

	public void removeDuplicate(List<PoiInfo> list) {
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = list.size() - 1; j > i; j--) {
				if (list.get(j).name.equals(list.get(i).name)) {
					list.remove(j);
				}
			}
		}
	}
}
