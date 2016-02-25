package com.tr.ui.flow;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.tr.R;
import com.tr.model.obj.DynamicLocation;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.home.utils.HomeCommonUtils;

public class FlowMapActivity extends JBaseFragmentActivity {

	private DynamicLocation mylocation;
	private TextView company;
	private TextView address;
	private MapView mapView;
	BaiduMap mBaiduMap;

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "地图",
				false, null, false, true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_flow_location_map);

		company = (TextView) findViewById(R.id.company);
		address = (TextView) findViewById(R.id.address);
		mapView = (MapView) findViewById(R.id.mapView);
		mylocation = (DynamicLocation) getIntent().getSerializableExtra(
				"location");

		company.setText(mylocation.getName());
		address.setText(mylocation.getDetailName());

		mBaiduMap = mapView.getMap();

		if(!TextUtils.isEmpty(mylocation.getDimension())){
			// 定义Maker坐标点
			LatLng point = new LatLng(Double.valueOf(mylocation.getDimension()
					.split("#")[0]), Double.valueOf(mylocation.getDimension()
					.split("#")[1]));
			// 构建Marker图标
			BitmapDescriptor bitmap = BitmapDescriptorFactory
					.fromResource(R.drawable.icon_level3);
			// 构建MarkerOption，用于在地图上添加Marker
			OverlayOptions option = new MarkerOptions().position(point)
					.icon(bitmap);
			// 在地图上添加Marker，并显示
			mBaiduMap.addOverlay(option);
		}
	}

}
