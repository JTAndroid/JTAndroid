package com.tr.ui.flow;

import java.util.ArrayList;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.tr.App;
import com.tr.R;
import com.tr.model.demand.Metadata;
import com.tr.model.obj.DynamicLocation;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.demand.util.ChooseDataUtil;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.organization.model.Area;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.utils.log.ToastUtil;

public class CreateLocationActivity extends JBaseActivity implements OnClickListener{

	private MyEditTextView locationNameTv;
	private MyEditTextView locationAreaTv;
	private MyEditTextView locationAddressTv;
	private MyEditTextView locationTypeTv;
	private MyEditTextView locationPhoneTv;
	private ArrayList<Metadata> metadataArea;
	private Area area_result;
	private String secondLevel = "";
	@Override
	public void initJabActionBar() {
		ActionBar jabGetActionBar = jabGetActionBar();
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar, "创建位置", false, null, true, true);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createlocation);
		initView();
	}
	private void initView() {
		locationNameTv = (MyEditTextView)findViewById(R.id.locationNameTv);
		locationAreaTv = (MyEditTextView)findViewById(R.id.locationAreaTv);
		locationAddressTv = (MyEditTextView)findViewById(R.id.locationAddressTv);
		locationTypeTv = (MyEditTextView)findViewById(R.id.locationTypeTv);
		locationPhoneTv = (MyEditTextView)findViewById(R.id.locationPhoneTv);
		locationAreaTv.setOnClickListener(this);
		locationTypeTv.setOnClickListener(this);
		locationPhoneTv.setNumEdttext_inputtype();
	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.locationAreaTv:
			ENavigate.startChooseActivityForResult(this, false, "区域",
					ChooseDataUtil.CHOOSE_type_Area, null);
			break;
		case R.id.locationTypeTv:
			
			break;
		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			switch (requestCode) {
			case ENavConsts.ActivityReqCode.REQUEST_CHOOSE_SELECT:
				// 多级选择回调界面
				setChooseText((ArrayList<Metadata>) data
						.getSerializableExtra(ENavConsts.DEMAND_CHOOSE_DATA));
				break;
			default:
				break;
			}
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_createflow, menu);
		MenuItem finish = menu.findItem(R.id.flow_create);
		finish.setTitle("完成");
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.flow_create:
			if (TextUtils.isEmpty(locationNameTv.getText())) {
				ToastUtil.showToast(this, "位置地址为必填");
				return false;
			}
			if (TextUtils.isEmpty(locationAreaTv.getText())) {
				ToastUtil.showToast(this, "所在地区为必选");
				return false;
			}
			if (TextUtils.isEmpty(locationAddressTv.getText())) {
				ToastUtil.showToast(this, "详细地址为必填");
				return false;
			}
			
			DynamicLocation location = new DynamicLocation();
			location.setName( locationNameTv.getText());
			location.setSecondLevel(secondLevel);
			location.setDetailName(locationAddressTv.getText());
			location.setMobile(locationPhoneTv.getText());
			location.setType(locationTypeTv.getText());
			Message locationMes=  Message.obtain();
			locationMes.what = 2;
			locationMes.obj = location;
			App.getApp().addParam("location", locationNameTv.getText());
			CreateFlowActivtiy.locationHand.sendMessage(locationMes);
			finish();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	public void setChooseText(ArrayList<Metadata> data) {
		// 地区
		if (metadataArea != null) {
			metadataArea.clear();
		}
		metadataArea = data;
		area_result = ChooseDataUtil.getMetadataName(metadataArea);
		// area_Etv.setText((TextUtils.isEmpty(area_result.province) ? ""
		// : area_result.province)
		// + (TextUtils.isEmpty(area_result.city) ? "" : area_result.city)
		// + (TextUtils.isEmpty(area_result.county) ? ""
		// : area_result.county));
		// 去除直辖市名字重叠的问题
		locationAreaTv.setText(getAreaStr(area_result));
		// 将这些值赋值给创建人脉的对象，避免值的丢失
//		person.locationCountry = area_result.province;
//		person.locationCounty = area_result.county;
//		person.locationCity = area_result.city;
		if (metadataArea != null) {
			if (!metadataArea.isEmpty()) {
				for (int i = 0; i < metadataArea.size(); i++) {
//					person.regionId = Long.parseLong(metadataArea.get(i).id);

					for (Metadata metadata : metadataArea) {
						if (!metadata.childs.isEmpty()) {

							// 有二级
							for (Metadata data2 : metadata.childs) {
								// 有三级
								if (!data2.childs.isEmpty()) {

									for (Metadata data3 : data2.childs) {
										if (!data3.childs.isEmpty()) {
//											person.regionId = Long
//													.parseLong(data3.id);
										}
									}
//									person.regionId = Long.parseLong(data2.id);
									secondLevel =  data2.name;
								}
							}
						}
//						person.regionId = Long.parseLong(metadata.id);
					}

				}

			}
		}
	}
	/**
	 * 获取地区对象
	 * @param area_result
	 * @return
	 */
	public String getAreaStr(Area area_result) {
		String area = "";
		if (area_result != null) {
			String province = TextUtils.isEmpty(area_result.province) ? ""
					: area_result.province;
			String city = TextUtils.isEmpty(area_result.city) ? ""
					: area_result.city;
			String county = TextUtils.isEmpty(area_result.county) ? ""
					: area_result.county;
			if (city.equalsIgnoreCase(province)) {
				area = province + county;
			} else {
				area = province + city + county;
			}

		}
		return area;
	}
}
