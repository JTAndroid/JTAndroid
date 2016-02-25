package com.tr.ui.im;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tr.R;
import com.tr.api.ConnectionsReqUtil;
import com.tr.api.IMReqUtil;
import com.tr.model.conference.MMeetingQuery;
import com.tr.model.im.ChatDetail;
import com.tr.model.model.PeopleForm;
import com.tr.model.obj.ConnectionsMini;
import com.tr.model.obj.IMBaseMessage;
import com.tr.model.obj.IMGroupCategory;
import com.tr.model.obj.MUCDetail;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.widgets.CateaoryGrid;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * 人脉分享权限选择页面
 * @author leon
 */
public class IMCreateGroupCategoryActivity1 extends JBaseFragmentActivity
		implements IBindData {
	public static final int type_group = 1;// 多个人
	public static final int type_meeting = 3; // 会议
	public static final int type_one = 2;// 一个人
	private int type = 0;
	private boolean result = false;// 是否需要返回

	CateaoryGrid investGrid, tradeGrid, areaGrid;
	ImageAdapter investImageAdapter, tradeImageAdapter, areaImageAdapter;
	ImageView investEnd, tradeEnd, areaEnd;
	View investTv, tradeTv, areaTv;// 分类
	ArrayList<ValueObj> selectedGroupCategorys = new ArrayList<ValueObj>();// 保存选中数据
	String tradeStr[] = { "手机", "传真", "固话", "邮箱", "地址", "主页", "通讯", "性别", "民族",
			"籍贯", "重要日期", "生活习惯", "爱好", "信仰", "民族", "社会关系", "身体状况" };
	String investStr[] = { "头像", "备注" };
	String areaStr[] = { "教育经历", "工作经历", "社会活动", "会面情况", "需求资源", "附件" };

	MUCDetail mucDetail;
	String thatMucID;
	ChatDetail chatDetail;
	MMeetingQuery meetingDetail;
	IMBaseMessage im;

	boolean isOnline = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getParam();
		this.setContentView(R.layout.im_creategroup_choose1);

		investGrid = (CateaoryGrid) findViewById(R.id.investGrid);
		investGrid.mIsSingleSelection = false;
		initGridAdapter(ValueObj.type_invest, investGrid);
		investGrid.setOnItemClickListener(gridOnItemClickListener);
		investGrid.setNumColumns(3);

		tradeGrid = (CateaoryGrid) findViewById(R.id.tradeGrid);
		tradeGrid.mIsSingleSelection = false;
		initGridAdapter(ValueObj.type_trade, tradeGrid);
		tradeGrid.setOnItemClickListener(gridOnItemClickListener);
		tradeGrid.setNumColumns(3);

		areaGrid = (CateaoryGrid) findViewById(R.id.areaGrid);
		areaGrid.mIsSingleSelection = false;
		initGridAdapter(ValueObj.type_area, areaGrid);
		areaGrid.setOnItemClickListener(gridOnItemClickListener);
		areaGrid.setNumColumns(3);

		investTv = findViewById(R.id.investtv);
		investTv.setOnClickListener(onclick);
		tradeTv = findViewById(R.id.tradetv);
		tradeTv.setOnClickListener(onclick);
		areaTv = findViewById(R.id.areatv);
		areaTv.setOnClickListener(onclick);

		investEnd = (ImageView) findViewById(R.id.investend);
		tradeEnd = (ImageView) findViewById(R.id.tradeend);
		areaEnd = (ImageView) findViewById(R.id.areaend);

		this.findViewById(R.id.clearAll).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						selectedGroupCategorys.clear();
						ArrayList<ValueObj> arr = null;
						arr = ((ImageAdapter) investGrid.getAdapter())
								.getList();
						for (ValueObj valueObj : arr) {
							valueObj.setFocuse(false);
						}

						arr = ((ImageAdapter) tradeGrid.getAdapter()).getList();
						for (ValueObj valueObj : arr) {
							valueObj.setFocuse(false);
						}
						arr = ((ImageAdapter) areaGrid.getAdapter()).getList();
						for (ValueObj valueObj : arr) {
							valueObj.setFocuse(false);
						}
						investImageAdapter.notifyDataSetChanged();
						tradeImageAdapter.notifyDataSetChanged();
						areaImageAdapter.notifyDataSetChanged();
					}
				});

		this.findViewById(R.id.im_group_invest_all).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						ArrayList<ValueObj> arr = null;
						arr = ((ImageAdapter) investGrid.getAdapter())
								.getList();
						for (ValueObj valueObj : arr) {
							valueObj.setFocuse(true);
							boolean isHas = false;
							for (int i = 0; i < selectedGroupCategorys.size(); i++) {
								if (valueObj.getName()
										.equals(selectedGroupCategorys.get(i)
												.getName())) {
									isHas = true;
									break;
								}
							}
							if (!isHas) {
								selectedGroupCategorys.add(valueObj);
							}
						}
						investImageAdapter.notifyDataSetChanged();
					}
				});

		this.findViewById(R.id.im_group_area_all).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						ArrayList<ValueObj> arr = null;
						arr = ((ImageAdapter) areaGrid.getAdapter()).getList();
						for (ValueObj valueObj : arr) {
							valueObj.setFocuse(true);
							boolean isHas = false;
							for (int i = 0; i < selectedGroupCategorys.size(); i++) {
								if (valueObj.getName()
										.equals(selectedGroupCategorys.get(i)
												.getName())) {
									isHas = true;
									break;
								}
							}
							if (!isHas) {
								selectedGroupCategorys.add(valueObj);
							}
						}
						areaImageAdapter.notifyDataSetChanged();
					}
				});

		this.findViewById(R.id.im_group_trade_all).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						ArrayList<ValueObj> arr = null;
						arr = ((ImageAdapter) tradeGrid.getAdapter()).getList();
						for (ValueObj valueObj : arr) {
							valueObj.setFocuse(true);
							boolean isHas = false;
							for (int i = 0; i < selectedGroupCategorys.size(); i++) {
								if (valueObj.getName()
										.equals(selectedGroupCategorys.get(i)
												.getName())) {
									isHas = true;
									break;
								}
							}
							if (!isHas) {
								selectedGroupCategorys.add(valueObj);
							}
						}
						tradeImageAdapter.notifyDataSetChanged();
					}
				});

	}

	// 读取传入的参数
	public void getParam() {
		Intent intent = getIntent();
		type = intent.getIntExtra(ENavConsts.EFromActivityType, 0);
		im = (IMBaseMessage) intent
				.getSerializableExtra(ENavConsts.EIMBaseMessage);
		if (type == type_group) { // 群聊
			mucDetail = (MUCDetail) intent
					.getSerializableExtra(ENavConsts.EMucDetail);
			thatMucID = intent.getStringExtra(ENavConsts.EMucID);
			if ((thatMucID == null) && (mucDetail == null)) {
				finish();
			}
			if (thatMucID != null) {
				getMucDetail(thatMucID);
			} else {
				thatMucID = mucDetail.getId() + "";
			}
		}
		else if(type == type_meeting){ // 会议
			meetingDetail = (MMeetingQuery) intent.getSerializableExtra(ENavConsts.EMeetingDetail);
		}
		else { // 私聊
			chatDetail = (ChatDetail) intent
					.getSerializableExtra(ENavConsts.EChatDetail);
		}

		result = intent.getBooleanExtra(ENavConsts.EFromActivityResult, false);
	}

	public String[] getIDs() {
		String strs[] = null;
		if (type == type_group) {
			List<ConnectionsMini> connectionsMiniList = mucDetail
					.getListConnectionsMini();
			if (mucDetail != null && connectionsMiniList != null
					&& connectionsMiniList.size() > 0) {
				strs = new String[connectionsMiniList.size()];
				for (int i = 0; i < connectionsMiniList.size(); i++) {
					strs[i] = connectionsMiniList.get(i).getId();
				}
			}
		}
		else if(type == type_meeting){
			strs = new String[meetingDetail.getListMeetingMember().size()];
			for (int i = 0; i < strs.length; i++) {
				strs[i] = meetingDetail.getListMeetingMember().get(i).getId() + "";
			}
		}
		else {
			strs = new String[1];
			strs[0] = chatDetail.getThatID();
		}
		if (strs == null) {
			finish();
		}
		return strs;
	}

	public void sharePart() {
		String[] ids = getIDs();
		PeopleForm peopleForm = doFinish();

		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.serializeNulls().create();
		HashMap hashJson = new HashMap();
		hashJson.put("ids", ids);
		hashJson.put("peopleForm", peopleForm);
		if (im != null && im.getJtFile() != null
				&& im.getJtFile().mTaskId != null) {
			hashJson.put("id", im.getJtFile().mTaskId);
		} 
		else {
			finish();
		}
		String json = gson.toJson(hashJson);
		ConnectionsReqUtil.getSharePart(this, this, json, null);
		showLoadingDialog();
	}

	// 通过mucid获取mucDetail
	public void getMucDetail(String mucID) {
		if (false != IMReqUtil.getMUCDetail(this, this, null, mucID)) {
			// 发送失败
			showLoadingDialog();
		} else {
			showToast("获取畅聊信息失败");
			finish();
		}
	}

	public void initGridAdapter(int type, CateaoryGrid cateaoryGrid) {
		ArrayList<ValueObj> arr = new ArrayList<ValueObj>();
		ImageAdapter imageAdapter = null;
		if (type == ValueObj.type_invest) {
			for (int i = 0; i < investStr.length; i++) {
				ValueObj ValueObj = new ValueObj(investStr[i]);
				arr.add(ValueObj);
			}
			imageAdapter = new ImageAdapter(
					IMCreateGroupCategoryActivity1.this, arr);
			investImageAdapter = imageAdapter;
		} else if (type == ValueObj.type_trade) {
			for (int i = 0; i < tradeStr.length; i++) {
				ValueObj ValueObj = new ValueObj(tradeStr[i]);
				arr.add(ValueObj);
			}
			imageAdapter = new ImageAdapter(
					IMCreateGroupCategoryActivity1.this, arr);
			tradeImageAdapter = imageAdapter;
		} else if (type == ValueObj.type_area) {
			for (int i = 0; i < areaStr.length; i++) {
				ValueObj ValueObj = new ValueObj(areaStr[i]);
				arr.add(ValueObj);
			}
			imageAdapter = new ImageAdapter(
					IMCreateGroupCategoryActivity1.this, arr);
			areaImageAdapter = imageAdapter;
		}

		if (selectedGroupCategorys != null && arr != null) {
			for (int i = 0; i < selectedGroupCategorys.size(); i++) {
				String str = selectedGroupCategorys.get(i).getName();
				for (int j = 0; j < arr.size(); j++) {
					if (arr.get(j).getName().equals(str)) {
						arr.get(j).setFocuse(true);
						break;
					}
				}
			}
		}
		cateaoryGrid.setAdapter(imageAdapter);
	}

	public void init(PeopleForm pf) {
		ArrayList<ValueObj> arr = new ArrayList<ValueObj>();
		ValueObj item = null;
		if (pf.portrait != null && pf.portrait.equals("头像")) {
			item = new ValueObj("头像");
			arr.add(item);
		}
		if (pf.remark != null && pf.remark.equals("备注")) {
			item = new ValueObj("备注");
			arr.add(item);
		}
		if (pf.educationList != null && pf.educationList.equals("教育经历")) {
			item = new ValueObj("教育经历");
			arr.add(item);
		}
		if (pf.workExperienceList != null
				&& pf.workExperienceList.equals("工作经历")) {
			item = new ValueObj("工作经历");
			arr.add(item);
		}
		if (pf.socialActivityList != null
				&& pf.socialActivityList.equals("社会活动")) {
			item = new ValueObj("社会活动");
			arr.add(item);
		}
		if (pf.meetingList != null && pf.meetingList.equals("会面情况")) {
			item = new ValueObj("会面情况");
			arr.add(item);
		}
		if (pf.demand != null && pf.demand.equals("需求资源")) {// /
			item = new ValueObj("需求资源");
			arr.add(item);
		}
		if (pf.fileIndexs != null && pf.fileIndexs.equals("附件")) {// /
			item = new ValueObj("附件");
			arr.add(item);
		}
		if (pf.contactMobileList != null && pf.contactMobileList.equals("手机")) {
			item = new ValueObj("手机");
			arr.add(item);
		}
		if (pf.contactFaxList != null && pf.contactFaxList.equals("传真")) {
			item = new ValueObj("传真");
			arr.add(item);
		}
		if (pf.contactFixedList != null && pf.contactFixedList.equals("固话")) {
			item = new ValueObj("固话");
			arr.add(item);
		}
		if (pf.contactMailList != null && pf.contactMailList.equals("邮箱")) {
			item = new ValueObj("邮箱");
			arr.add(item);
		}
		if (pf.contactAddressList != null && pf.contactAddressList.equals("地址")) {
			item = new ValueObj("地址");
			arr.add(item);
		}
		if (pf.contactHomeList != null && pf.contactHomeList.equals("主页")) {
			item = new ValueObj("主页");
			arr.add(item);
		}
		if (pf.contactCommunicationList != null
				&& pf.contactCommunicationList.equals("通讯")) {
			item = new ValueObj("通讯");
			arr.add(item);
		}

		if (isOnline) {
			if (pf.gender != null && pf.gender.equals("性别")) {
				item = new ValueObj("性别");
				arr.add(item);
			}
		} else {
			if (pf.genderName != null && pf.genderName.equals("性别")) {
				item = new ValueObj("性别");
				arr.add(item);
			}
		}
		if (pf.raceName != null && pf.raceName.equals("民族")) {
			item = new ValueObj("民族");
			arr.add(item);
		}
		if (pf.birthPlaceAddress != null && pf.birthPlaceAddress.equals("籍贯")) {
			item = new ValueObj("籍贯");
			arr.add(item);
		}
		if (pf.importantDateList != null && pf.importantDateList.equals("重要日期")) {
			item = new ValueObj("重要日期");
			arr.add(item);
		}
		if (pf.habit != null && pf.habit.equals("生活习惯")) {
			item = new ValueObj("生活习惯");
			arr.add(item);
		}
		if (pf.hobby != null && pf.hobby.equals("爱好")) {
			item = new ValueObj("爱好");
			arr.add(item);
		}
		if (pf.faithName != null && pf.faithName.equals("信仰")) {
			item = new ValueObj("信仰");
			arr.add(item);
		}
		if (pf.communityRelationshipList != null
				&& pf.communityRelationshipList.equals("社会关系")) {
			item = new ValueObj("社会关系");
			arr.add(item);
		}

		if (pf.bodySituation != null && pf.bodySituation.equals("身体状况")) {
			item = new ValueObj("身体状况");
			arr.add(item);
		}

	}

	public PeopleForm doFinish() {
		PeopleForm pf = new PeopleForm();
		for (ValueObj valueObj : selectedGroupCategorys) {
			if (valueObj.getName().equals("头像")) {
				pf.portrait = valueObj.getName();
			} else if (valueObj.getName().equals("备注")) {
				pf.remark = valueObj.getName();
			} else if (valueObj.getName().equals("教育经历")) {
				pf.educationList = valueObj.getName();
			} else if (valueObj.getName().equals("工作经历")) {
				pf.workExperienceList = valueObj.getName();
			} else if (valueObj.getName().equals("社会活动")) {
				pf.socialActivityList = valueObj.getName();
			} else if (valueObj.getName().equals("会面情况")) {
				pf.meetingList = valueObj.getName();
			} else if (valueObj.getName().equals("需求资源")) {// /、、、、、、、、、、
				pf.demand = valueObj.getName();
			} else if (valueObj.getName().equals("附件")) {
				pf.fileIndexs = valueObj.getName(); // //////////////
			} else if (valueObj.getName().equals("手机")) {
				pf.contactMobileList = valueObj.getName();
			} else if (valueObj.getName().equals("传真")) {
				pf.contactFaxList = valueObj.getName();
			} else if (valueObj.getName().equals("固话")) {
				pf.contactFixedList = valueObj.getName();
			} else if (valueObj.getName().equals("邮箱")) {
				pf.contactMailList = valueObj.getName();
			} else if (valueObj.getName().equals("地址")) {
				pf.contactAddressList = valueObj.getName();
			} else if (valueObj.getName().equals("主页")) {
				pf.contactHomeList = valueObj.getName();
			} else if (valueObj.getName().equals("通讯")) {
				pf.contactCommunicationList = valueObj.getName();
			} else if (valueObj.getName().equals("性别")) {
				if (isOnline) {
					pf.gender = valueObj.getName();
				} else {
					pf.genderName = valueObj.getName();
				}
			} else if (valueObj.getName().equals("民族")) {
				pf.raceName = valueObj.getName();
			} else if (valueObj.getName().equals("籍贯")) {
				pf.birthPlaceAddress = valueObj.getName();
			} else if (valueObj.getName().equals("重要日期")) {
				pf.importantDateList = valueObj.getName();
			} else if (valueObj.getName().equals("生活习惯")) {
				pf.habit = valueObj.getName();
			} else if (valueObj.getName().equals("爱好")) {
				pf.hobby = valueObj.getName();
			} else if (valueObj.getName().equals("信仰")) {
				pf.faithName = valueObj.getName();
			} else if (valueObj.getName().equals("社会关系")) {
				pf.communityRelationshipList = valueObj.getName();
			} else if (valueObj.getName().equals("身体状况")) {
				pf.bodySituation = valueObj.getName();
			}
		}
		// Intent it = new Intent();
		// it.putExtra(ENavConsts.redatas, pf );
		// setResult(Activity.RESULT_OK, it);
		return pf;
	}

	/**
	 * actionbar 中菜单点击事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.conference_create_next:
			sharePart();
			// send();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void send(String id) {
		JSONObject jb = new JSONObject();
		try {
			jb.put("jtContactID", id);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ConnectionsReqUtil.getShareDetail(this, this, jb, null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.im_chatmenu, menu);
		return true;
	}

	OnItemClickListener gridOnItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			CateaoryGrid cateaoryGrid = (CateaoryGrid) arg0;
			ImageAdapter adapter = (ImageAdapter) cateaoryGrid.getAdapter();

			ValueObj valueObj = (ValueObj) arg1.getTag();
			int type = valueObj.getType();

			valueObj.setFocuse(!valueObj.isFocuse());
			if (valueObj.isFocuse()) {
				arg1.setBackgroundResource(valueObj.getResFocuse());
				// 增加
				selectedGroupCategorys.add((ValueObj) arg1.getTag());
			} else {
				arg1.setBackgroundResource(valueObj.getResdefault());
				for (int i = 0; i < selectedGroupCategorys.size(); i++) {
					if (selectedGroupCategorys.get(i).getName()
							.equals(valueObj.getName())) {
						selectedGroupCategorys.remove(i);
						break;
					}
				}
			}
			adapter.notifyDataSetChanged();
		}
	};

	View.OnClickListener onclick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (investTv == v) {
				if (investGrid.getVisibility() == View.VISIBLE) {
					investGrid.setVisibility(View.GONE);
					investEnd.setVisibility(View.VISIBLE);
					((ImageView) findViewById(R.id.investtv_tag))
							.setImageResource(R.drawable.zhankai);
				} else {
					investGrid.setVisibility(View.VISIBLE);
					investEnd.setVisibility(View.GONE);
					((ImageView) findViewById(R.id.investtv_tag))
							.setImageResource(R.drawable.shouqi);
				}
			} else if (tradeTv == v) {
				if (tradeGrid.getVisibility() == View.VISIBLE) {
					tradeGrid.setVisibility(View.GONE);
					tradeEnd.setVisibility(View.VISIBLE);
					((ImageView) findViewById(R.id.tradetv_tag))
							.setImageResource(R.drawable.zhankai);
				} else {
					tradeGrid.setVisibility(View.VISIBLE);
					tradeEnd.setVisibility(View.GONE);
					((ImageView) findViewById(R.id.tradetv_tag))
							.setImageResource(R.drawable.shouqi);
				}
			} else if (areaTv == v) {
				if (areaGrid.getVisibility() == View.VISIBLE) {
					areaGrid.setVisibility(View.GONE);
					areaEnd.setVisibility(View.VISIBLE);
					((ImageView) findViewById(R.id.areatv_tag))
							.setImageResource(R.drawable.zhankai);
				} else {
					areaGrid.setVisibility(View.VISIBLE);
					areaEnd.setVisibility(View.GONE);
					((ImageView) findViewById(R.id.areatv_tag))
							.setImageResource(R.drawable.shouqi);
				}
			}
		}
	};

	public static class ImageAdapter extends BaseAdapter {
		Context mContext = null;
		boolean removeState = false;
		private ArrayList<ValueObj> baseData;

		public ImageAdapter(Context c, ArrayList<ValueObj> data) {
			mContext = c;
			baseData = data;
		}

		public ArrayList<ValueObj> getList() {
			return baseData;
		}

		public int getCount() {
			return baseData.size();
		}

		public Object getItem(int position) {
			return baseData.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ValueObj ValueObj = baseData.get(position);
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(
						R.layout.im_creategroupcategory_grid_item, null);
			}
			convertView.setTag(ValueObj);
			setFocus(ValueObj, convertView);
			((TextView) convertView.findViewById(R.id.text)).setText(ValueObj
					.getName());
			return convertView;
		}

		public void setFocus(ValueObj ValueObj, View view) {
			if (ValueObj.isFocuse()) {
				if (ValueObj.getResFocuse() != 0) {
					view.setBackgroundResource(ValueObj.getResFocuse());
				}
			} else {
				if (ValueObj.getResdefault() != 0) {
					view.setBackgroundResource(ValueObj.getResdefault());
				}
			}
		}
	}

	class ValueObj extends IMGroupCategory {
		String data = null;

		public ValueObj(String str) {
			data = str;
		}

		@Override
		public void initWithJson(JSONObject jsonObject) throws JSONException,
				MalformedURLException, ParseException {
		}

		@Override
		public JSONObject toJSONObject() throws JSONException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return data;
		}
	}

	public ActionBar actionbar = null;

	@Override
	public void initJabActionBar() {
		actionbar = getActionBar();
		actionbar.setDisplayShowTitleEnabled(true);
		actionbar.setTitle("分享选择");
	}

	@Override
	public void bindData(int tag, Object object) {
		dismissLoadingDialog();
		if (hasDestroy()) {
			return;
		}
		if (tag == EAPIConsts.IMReqType.IM_REQ_GET_MUC_DETAIL) {

			if (object == null) {
				finish();
				return;
			}
			// 获取群聊详情返回
			mucDetail = (MUCDetail) object;
			return;
		} else if (tag == EAPIConsts.concReqType.getSharePart) {
			if (object == null) {
				finish();
				return;
			}
			String id = (String) object;
			if (im != null && im.getJtFile() != null) {
				im.getJtFile().mTaskId = id;
			}
			if (result) {
				Intent it = new Intent();
				it.putExtra(ENavConsts.redatas, im.getJtFile());
				setResult(Activity.RESULT_OK, it);
				finish();
			} 
			else {
				if (type == type_group) {
					ENavigate.startIMGroupActivity(
							IMCreateGroupCategoryActivity1.this, mucDetail, im);
				} 
				else if(type == type_one){
					ENavigate.startIMActivity(
							IMCreateGroupCategoryActivity1.this, chatDetail, im);
				}
				else{
					
				}
			}
		}

	}
}
