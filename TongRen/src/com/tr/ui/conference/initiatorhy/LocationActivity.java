package com.tr.ui.conference.initiatorhy;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionResult.SuggestionInfo;
import com.tr.App;
import com.tr.R;
import com.tr.baidumapsdk.BaiduLoc;
import com.tr.baidumapsdk.BaiduMapListener;
import com.tr.baidumapsdk.BaiduMapView;
import com.tr.baidumapsdk.BaiduMapView.MyMarker;
import com.tr.model.conference.MMapAddress;
import com.tr.model.obj.DynamicLocation;
import com.tr.ui.adapter.conference.ListLocationAdapter;
import com.tr.ui.conference.common.BaseActivity;
import com.tr.ui.flow.CreateFlowActivtiy;
import com.utils.time.Util;

/**
 * 地点
 * 
 * @author d.c 2014.11.09 22:00
 */

public class LocationActivity extends BaseActivity implements BaiduMapListener {
	// 标题栏
	private LinearLayout mIvBackButton = null;
	private TextView mTvTitle = null;
	private TextView mTvFinish = null;

	BaiduMapView mBaiduMapView = null;

	private EditText mEtSearch = null;
	private TextView mTvSearch = null;

	private ImageView mImageMine = null;
	private ImageView mImageZoomin = null;
	private ImageView mImageZoomout = null;
	private float mZoomLevel = 16;
	private LatLng myPosition = new LatLng(0.0, 0.0);
	private ReverseGeoCodeResult mRes = null;
	private PoiInfo mInfo = null;
	//private SuggestionSearch mSuggestionSearch = null;
	// 数据列表
	private ListView mLvLocations = null;
	private ListLocationAdapter mLocationListAdapter = null;
	//private ListSuggestAdapter mSuggestListAdapter = null;
	private boolean bCanSuggest = true;

	private MMapAddress mAddress = null;

	@Override
	public void initView() {
		setContentView(R.layout.hy_activity_location);
		mIvBackButton = (LinearLayout) findViewById(R.id.hy_layoutTitle_backBtn);
		mTvTitle = (TextView) findViewById(R.id.hy_layoutTitle_title);
		mTvFinish = (TextView) findViewById(R.id.hy_layoutTitle_rightTextBtn);

		mEtSearch = (EditText) findViewById(R.id.hy_et_search);
		mEtSearch.setSingleLine(true);
		mTvSearch = (TextView) findViewById(R.id.hy_tv_search);

		mBaiduMapView = new BaiduMapView(this, R.id.hy_ll_map, true, true);

		mImageMine = (ImageView) findViewById(R.id.img_mapsos_mine);
		mImageZoomin = (ImageView) findViewById(R.id.img_mapsos_zoomin);
		mImageZoomout = (ImageView) findViewById(R.id.img_mapsos_zoomout);

		mLvLocations = (ListView) findViewById(R.id.hy_lv_locations);
		mLocationListAdapter = new ListLocationAdapter(this);
		//mSuggestListAdapter = new ListSuggestAdapter(this);

		//mSuggestionSearch = SuggestionSearch.newInstance();
	}

	@Override
	public void initData() {

		mTvTitle.setText("地点");
		mTvFinish.setText("完成");

		mBaiduMapView.setListener(this);
		BaiduLoc locater = App.getApp().getBaiduLoc();
		myPosition = new LatLng(locater.getLatitude(), locater.getLongitude());

		mAddress = (MMapAddress) getIntent().getSerializableExtra("address");
		if (null != mAddress) {
			if (null != mAddress.address) {
				if (false == mAddress.address.isEmpty()) {
					mBaiduMapView.clearPoi();
					mBaiduMapView.setCenter(mAddress.latitude, mAddress.longitude, R.drawable.hy_img_location);
					mEtSearch.setText(mAddress.address);
					mEtSearch.setSelection(mAddress.address.length());
				} else {
					mBaiduMapView.setCenter(locater.getLatitude(), locater.getLongitude(), R.drawable.hy_img_location);
				}
			} else {
				mBaiduMapView.setCenter(locater.getLatitude(), locater.getLongitude(), R.drawable.hy_img_location);
			}
		} else {
			mBaiduMapView.setCenter(locater.getLatitude(), locater.getLongitude(), R.drawable.hy_img_location);
		}

		mIvBackButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		mEtSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				bCanSuggest = true;
				mLvLocations.setVisibility(View.GONE);
			}
		});

		mEtSearch.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (KeyEvent.ACTION_UP == event.getAction() && KeyEvent.KEYCODE_ENTER == keyCode) {
					String sKeyword = mEtSearch.getEditableText().toString();
					BaiduLoc locater = App.getApp().getBaiduLoc();
					if (!sKeyword.isEmpty()) {
						mBaiduMapView.searchByString(sKeyword, locater.getLatitude(), locater.getLongitude());
					}
					try {
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						boolean isOpen = imm.isActive();
						if (isOpen) {
							imm.hideSoftInputFromWindow(LocationActivity.this.getCurrentFocus().getWindowToken(), 0);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
					
					return true;
				}
				return false;
			}
		});

		mEtSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
				if(false == bCanSuggest){
					return;
				}
				String sKeyword = mEtSearch.getEditableText().toString();
				BaiduLoc locater = App.getApp().getBaiduLoc();
				if (!sKeyword.isEmpty()) {
					mBaiduMapView.searchByString2(sKeyword, locater.getLatitude(), locater.getLongitude());
				}
				/*if (cs.length() <= 0 || false == bCanSuggest) {
					mLvLocations.setVisibility(View.GONE);
					return;
				}
				BaiduLoc locater = App.getApp().getBaiduLoc();
				if (null != locater) {
					String city = locater.getLocation().getCity();
					if (null != city) {
						if (false == city.isEmpty()) {
							mSuggestionSearch.requestSuggestion((new SuggestionSearchOption()).keyword(cs.toString()).city(city));
						}
					}
				}*/
			}
		});

		/*mSuggestionSearch.setOnGetSuggestionResultListener(new OnGetSuggestionResultListener() {
			@Override
			public void onGetSuggestionResult(SuggestionResult res) {
				if (res == null || res.getAllSuggestions() == null) {
					return;
				}
				List<SuggestionInfo> pList = res.getAllSuggestions();

				if (null != pList) {
					if (pList.size() > 0) {
						mLvLocations.setAdapter(mSuggestListAdapter);
						mSuggestListAdapter.updateList(pList);
						mLvLocations.setVisibility(View.VISIBLE);
					} else {
						mLvLocations.setVisibility(View.GONE);
					}
				}
			}
		});*/

		mTvFinish.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					boolean isOpen = imm.isActive();
					if (isOpen) {
						imm.hideSoftInputFromWindow(LocationActivity.this.getCurrentFocus().getWindowToken(), 0);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				if (null != mAddress) {
					if (!Util.isNull(mRes) && !Util.isNull(mInfo)) {
						if (null != mRes.getAddressDetail()) {
							MMapAddress ma = new MMapAddress();
							ma.address = mEtSearch.getEditableText().toString();
							ma.longitude = mInfo.location.longitude;
							ma.latitude = mInfo.location.latitude;
							ma.province = mRes.getAddressDetail().province;
							ma.city = mRes.getAddressDetail().city;
							ma.town = mRes.getAddressDetail().district;
							Bundle b = new Bundle();
							b.putSerializable(Util.IK_VALUE, ma);
							Message locationMes=  Message.obtain();
							locationMes.obj = mEtSearch.getEditableText().toString();;
							CreateFlowActivtiy.locationHand.sendMessage(locationMes);
							Util.activitySetResult(LocationActivity.this, InitiatorHYActivity.class, b);
							
							finish();
							return;
						}
					}
					Bundle b = new Bundle();
					b.putSerializable(Util.IK_VALUE, mAddress);
					Util.activitySetResult(LocationActivity.this, InitiatorHYActivity.class, b);
					finish();
				} else {
					if (Util.isNull(mRes) || Util.isNull(mInfo)) {
						Toast.makeText(LocationActivity.this, "没有选择地址", Toast.LENGTH_SHORT).show();
						return;
					}
					if (null == mRes.getAddressDetail()) {
						Toast.makeText(LocationActivity.this, "没有选择地址", Toast.LENGTH_SHORT).show();
						return;
					}
					MMapAddress ma = new MMapAddress();
					ma.address = mEtSearch.getEditableText().toString();
					ma.longitude = mInfo.location.longitude;
					ma.latitude = mInfo.location.latitude;
					ma.province = mRes.getAddressDetail().province;
					ma.city = mRes.getAddressDetail().city;
					ma.town = mRes.getAddressDetail().district;
					DynamicLocation location = new DynamicLocation();
					location.setName( mEtSearch.getEditableText().toString());
					location.setDimension( mInfo.location.latitude
					+"#"+mInfo.location.longitude);
					location.setSecondLevel(mRes.getAddressDetail().city);
					location.setDetailName( mRes.getAddressDetail().district);
					Bundle b = new Bundle();
					b.putSerializable(Util.IK_VALUE, ma);
					Message locationMes=  Message.obtain();
					locationMes.what = 1;
					locationMes.obj = location;
					App.getApp().addParam("location", mEtSearch.getEditableText().toString());
					CreateFlowActivtiy.locationHand.sendMessage(locationMes);
					Util.activitySetResult(LocationActivity.this, InitiatorHYActivity.class, b);
					finish();
				}
			}

		});

		mTvSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String sKeyword = mEtSearch.getEditableText().toString();
				BaiduLoc locater = App.getApp().getBaiduLoc();
				if (!sKeyword.isEmpty()) {
					mBaiduMapView.searchByString(sKeyword, locater.getLatitude(), locater.getLongitude());
				}
				try {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					boolean isOpen = imm.isActive();
					if (isOpen) {
						imm.hideSoftInputFromWindow(LocationActivity.this.getCurrentFocus().getWindowToken(), 0);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		});

		mImageMine.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				BaiduLoc locater = App.getApp().getBaiduLoc();
				myPosition = new LatLng(locater.getLatitude(), locater.getLongitude());
				mBaiduMapView.setCenterOnly(locater.getLatitude(), locater.getLongitude());
				mImageMine.setImageResource(R.drawable.hy_img_map_located);
			}
		});

		mImageZoomin = (ImageView) findViewById(R.id.img_mapsos_zoomin);
		mImageZoomin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mZoomLevel++;
				if (mZoomLevel > 19) {
					mZoomLevel = 19;
					mImageZoomin.setEnabled(false);
					Toast.makeText(getApplicationContext(), "地图已放到最大", Toast.LENGTH_SHORT).show();

				}
				mImageZoomout.setEnabled(true);
				mBaiduMapView.setZoomLevel(mZoomLevel);
			}
		});

		mImageZoomout = (ImageView) findViewById(R.id.img_mapsos_zoomout);
		mImageZoomout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mZoomLevel--;
				if (mZoomLevel < 3) {
					mZoomLevel = 3;
					mImageZoomout.setEnabled(false);
					Toast.makeText(getApplicationContext(), "地图已缩到最小", Toast.LENGTH_SHORT).show();
				}
				mImageZoomin.setEnabled(true);
				mBaiduMapView.setZoomLevel(mZoomLevel);
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		mBaiduMapView.onDestroy();
		//mSuggestionSearch.destroy();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		mBaiduMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onPause() {
		mBaiduMapView.onPause();
		super.onPause();
	}

	@Override
	public void onMapLoaded() {
	}

	@Override
	public void onSearched(PoiResult result) {
		ArrayList<PoiInfo> pList;
		if (result == null) {
			return;
		}
		if (result.error != SearchResult.ERRORNO.NO_ERROR) {
			mLvLocations.setVisibility(View.VISIBLE);
			PoiInfo info = new PoiInfo();
			info.name = "暂时无法获取您的地址";
			info.address = "请您创建地址";
			pList = new ArrayList<PoiInfo>();
			pList.add(info);
			mLvLocations.setAdapter(mLocationListAdapter);
			mLocationListAdapter.updateList(pList);
			return;
		}
		pList = (ArrayList<PoiInfo>) result.getAllPoi();
		if (null != pList) {
			if (pList.size() > 0) {
				mLvLocations.setAdapter(mLocationListAdapter);
				mLocationListAdapter.updateList(pList);
				mLvLocations.setVisibility(View.VISIBLE);
			} else {
				mLvLocations.setVisibility(View.VISIBLE);
				PoiInfo info = new PoiInfo();
				info.name = "暂时无法获取您的地址";
				info.address = "请您创建地址";
				pList.add(info);
				mLvLocations.setAdapter(mLocationListAdapter);
				mLocationListAdapter.updateList(pList);
			}
		}
	}

	@Override
	public void onSearchSuggestion(SuggestionResult result) {
	}

	@Override
	public void onMarkerClick(MyMarker mymarker) {
	}

	@Override
	public void onSnapshotReady(Bitmap snapshot) {
	}

	@Override
	public void OnMapChanged(MapStatus arg0) {
		if (mZoomLevel != arg0.zoom) {
			mZoomLevel = arg0.zoom;
			if (mZoomLevel > 19) {
				mZoomLevel = 19;
			}
			if (mZoomLevel < 3) {
				mZoomLevel = 3;
			}

			if (mZoomLevel == 19) {
				mImageZoomin.setEnabled(false);
				mImageZoomout.setEnabled(true);
				Toast.makeText(getApplicationContext(), "地图已放到最大", Toast.LENGTH_SHORT).show();
			} else {
				if (mZoomLevel == 3) {
					mImageZoomin.setEnabled(true);
					mImageZoomout.setEnabled(false);
					Toast.makeText(getApplicationContext(), "地图已缩到最小", Toast.LENGTH_SHORT).show();
				} else {
					mImageZoomin.setEnabled(true);
					mImageZoomout.setEnabled(true);
				}
			}
		}
		if (myPosition.latitude != arg0.target.latitude || myPosition.longitude != arg0.target.longitude) {
			if (null != mImageMine) {
				mImageMine.setImageResource(R.drawable.hy_selector_mapbar_mine);
			}
			myPosition = new LatLng(arg0.target.latitude, arg0.target.longitude);
		} else {
			if (mZoomLevel == 19) {
				Toast.makeText(getApplicationContext(), "地图已放到最大", Toast.LENGTH_SHORT).show();
			}
			if (mZoomLevel == 3) {
				Toast.makeText(getApplicationContext(), "地图已缩到最小", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void OnMapClick() {

	}

	public void setPoiLocation(ReverseGeoCodeResult res, PoiInfo info) {
		bCanSuggest = false;
		mLvLocations.setVisibility(View.GONE);

		mBaiduMapView.clearPoi();
		mBaiduMapView.setCenter(res.getLocation().latitude, res.getLocation().longitude, R.drawable.hy_img_location);
		mEtSearch.setText(info.name);
		mEtSearch.setSelection(info.name.length());
		mRes = res;
		mInfo = info;
	}

	public void setSugLocation(SuggestionInfo aInfo) {
		bCanSuggest = false;
		mLvLocations.setVisibility(View.GONE);

		mEtSearch.setText(aInfo.key);
		mEtSearch.setSelection(aInfo.key.length());
	}
}