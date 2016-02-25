package com.tr.ui.adapter.conference;

import java.util.ArrayList;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.tr.R;
import com.tr.ui.conference.initiatorhy.LocationActivity;
import com.tr.ui.flow.CreateLocationActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//create by d.c at 2014/11/10

public class ListLocationAdapter extends BaseAdapter implements OnGetGeoCoderResultListener {
	private Context mContext;
	private LayoutInflater mInflater;
	private GeoCoder mSearch;

	private ArrayList<PoiInfo> mLocationList = null;
	private int mItemIndex = -1;

	public ListLocationAdapter(Context context) {
		mContext = context;
		mLocationList = new ArrayList<PoiInfo>();
		mSearch = GeoCoder.newInstance();
		mSearch.setOnGetGeoCodeResultListener(this);
	}

	public void updateList(ArrayList<PoiInfo> arrayList) {

		if (null == arrayList) {
			return;
		}

		int iCount = arrayList.size();
		if (0 == iCount) {
			return;
		}
		mLocationList.clear();
		mItemIndex = -1;
		mLocationList.addAll(arrayList);
		if (mLocationList.size() > 0) {
			notifyDataSetChanged();
		}
	}

	public void clear() {
		mLocationList.clear();
	}

	@Override
	public int getCount() {
		if (null != mLocationList) {
			return mLocationList.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		if (null != mLocationList) {
			if (position >= 0 && position < mLocationList.size()) {
				return mLocationList.get(position);
			}
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		viewHolder holder;
		if (null == mInflater) {
			mInflater = LayoutInflater.from(mContext);
		}
		if (convertView == null) {
			holder = new viewHolder();
			convertView = mInflater.inflate(R.layout.hy_list_item_location, null);
			holder.tvName = (TextView) convertView.findViewById(R.id.hy_tv_name);
			holder.tvAddr = (TextView) convertView.findViewById(R.id.hy_tv_addr);
			holder.ivCheck = (ImageView) convertView.findViewById(R.id.hy_iv_check);
			convertView.setTag(holder);
		} else {
			holder = (viewHolder) convertView.getTag();
		}
		if (position >= 0 && position < mLocationList.size()) {
			PoiInfo aInfo = mLocationList.get(position);
			if (null != aInfo) {
				holder.tvName.setText(aInfo.name);
				holder.tvAddr.setText(aInfo.address);
				if (mItemIndex == position) {
					holder.ivCheck.setVisibility(View.VISIBLE);
				} else {
					holder.ivCheck.setVisibility(View.GONE);
				}
			}
		}
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mItemIndex = position;
				notifyDataSetChanged();
				PoiInfo aInfo = mLocationList.get(position);
				if (null != aInfo) {
					if ("暂时无法获取您的地址".equals(aInfo.name)) {
						Intent intent = new Intent(mContext, CreateLocationActivity.class);
						mContext.startActivity(intent);
						Activity activity = (Activity) mContext;
						activity.finish();
					}else{
						mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(aInfo.location));
					}
				}
			}
		});

		return convertView;
	}

	private class viewHolder {
		private TextView tvName;
		private TextView tvAddr;
		private ImageView ivCheck;
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {
	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
		if (arg0 == null || arg0.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(mContext, "无法获取地址信息", Toast.LENGTH_SHORT).show();
		}
		if (null == arg0.getAddressDetail().province && null == arg0.getAddressDetail().city && null == arg0.getAddressDetail().district && null == arg0.getAddressDetail().street && null == arg0.getAddressDetail().streetNumber) {
			Toast.makeText(mContext, "无法获取地址信息", Toast.LENGTH_SHORT).show();
		}
		if (TextUtils.isEmpty(arg0.getAddressDetail().province) && TextUtils.isEmpty(arg0.getAddressDetail().city) && TextUtils.isEmpty(arg0.getAddressDetail().district) && TextUtils.isEmpty(arg0.getAddressDetail().street) && TextUtils.isEmpty(arg0.getAddressDetail().streetNumber)) {
			Toast.makeText(mContext, "无法获取地址信息", Toast.LENGTH_SHORT).show();
		}
		LocationActivity aAct = (LocationActivity) mContext;
		if (null != aAct && mItemIndex >= 0 && mItemIndex < mLocationList.size()) {
			PoiInfo aInfo = mLocationList.get(mItemIndex);
			if (null != aInfo) {
				aAct.setPoiLocation(arg0, aInfo);
			}
		}
	}
}