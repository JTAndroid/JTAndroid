package com.tr.ui.demand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.App;
import com.tr.R;
import com.tr.api.DemandReqUtil;
import com.tr.api.KnowledgeReqUtil;
import com.tr.api.PeopleReqUtil;
import com.tr.api.OrganizationReqUtil;
import com.tr.model.demand.LableData;
import com.tr.model.knowledge.UserTag;
import com.tr.navigate.ENavConsts;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.demand.MyView.CustomView;
import com.tr.ui.knowledge.GlobalKnowledgeTagActivity;
import com.tr.ui.knowledge.GlobalKnowledgeTagActivity.TagComparator;
import com.tr.ui.widgets.DoubleTextViewTagLayout;
import com.tr.ui.widgets.KnoTagGroupView;
import com.utils.common.EConsts;
import com.utils.http.EAPIConsts;
import com.utils.http.EAPIConsts.KnoReqType;
import com.utils.http.EAPIConsts.PeopleRequestType;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;

/**
 * @ClassName: checkLabelActivity.java
 * @author ZCS
 * @Date 2015年3月13日 上午11:37:36
 * @Description: 标签_筛选查看和管理
 */

public class CreateLabelActivity extends JBaseActivity implements
		OnClickListener, IBindData {

	private enum TypeCv {
		select, me, defaule

	}
	private String s;
	public enum ModulesType {
		/** 知识模块 */
		KnowledgeModules,
		/** 组织/客户模块 */
		OrgAndCustomModules,
		/** 人脉模块 */
		PeopleModules,
		/**需求模块*/
		DemandModules
	}


	ArrayList<LableData> selectData;// 选中的对象
	ArrayList<LableData> meData = new ArrayList<LableData>();// 选中的对象
	ArrayList<LableData> defauleData = new ArrayList<LableData>();// 选中的对象
	private KnoTagGroupView selectedCv;
	private KnoTagGroupView myLabelCv;
	private KnoTagGroupView defaultLabelCv;
	private MyOnClickItem onClick;
	private TagMeOnClickItem meOnClick;
	private TagDefaultOnClickItem defaultOnClick;
	private Dialog dialog;
	private ArrayList<String> listTag = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demand_label_check_create);
		onClick = new MyOnClickItem();
		meOnClick = new TagMeOnClickItem();
		defaultOnClick = new TagDefaultOnClickItem();
		selectedCv = (KnoTagGroupView) findViewById(R.id.selectedCv);
		myLabelCv = (KnoTagGroupView) findViewById(R.id.myLabelCv);
		defaultLabelCv = (KnoTagGroupView) findViewById(R.id.defaultLabelCv);
		getParam();// 已选择的标签
		getIntentLabel();// 金铜推荐标签
		getMeLabel();// 读取我的标签
		updateMyLabel();
		updateDefault();
		updateSelect();
	}

	/**
	 * 从上面页面获得的标签
	 */
	@SuppressWarnings("unchecked")
	private void getParam() {
		Intent intent = getIntent();
		selectData = selectData == null ? selectData = new ArrayList<LableData>()
				: selectData;
		if (intent != null) {
			ArrayList<LableData> data = (ArrayList<LableData>) intent
					.getSerializableExtra(ENavConsts.DEMAND_LABEL_DATA);// 代表传进来的
			if (data != null) {
				for (LableData lableData : data) {
					addSelected(lableData);
				}
			}
			mModulesType = (ModulesType) getIntent().getSerializableExtra(EConsts.Key.MODULES_TYPE);
		}
	}

	/**
	 * 金铜推荐标签
	 */
	private void getIntentLabel() {
		switch (mModulesType) {
		case DemandModules:
			DemandReqUtil.getTagList(CreateLabelActivity.this,CreateLabelActivity.this, null);
			break;
		case OrgAndCustomModules:
			OrganizationReqUtil.getTagList(CreateLabelActivity.this,CreateLabelActivity.this, null);
			break;
		case PeopleModules:
			PeopleReqUtil.getTagList(CreateLabelActivity.this,CreateLabelActivity.this, null);
			break;
		case KnowledgeModules:
			DemandReqUtil.getTagList(CreateLabelActivity.this,CreateLabelActivity.this, null);
			break;
		}
	}

	/**
	 * 获取用户个人标签信息
	 */
	private void getMeLabel() {
		switch (mModulesType) {
		case DemandModules:
			DemandReqUtil.queryMyTag(CreateLabelActivity.this,
					CreateLabelActivity.this, 0, 0, null);
			break;
		case OrgAndCustomModules:
			OrganizationReqUtil.getOrgAndCustTag(this,this, null);//获取组织客户的标签
			break;
		case PeopleModules:
			PeopleReqUtil.queryMyTag(CreateLabelActivity.this,CreateLabelActivity.this, 0, 0, null);
			break;
		case KnowledgeModules:
			KnowledgeReqUtil.doGetKnowledgeTagList(CreateLabelActivity.this, this, App.getUserID(), null);
			break;
		}
	}

	/**
	 * 向指定的框内增加一个view,不能直接调用，updateSelect即可
	 * 
	 * @param cv
	 * @param str
	 * @param isSelect
	 *            是否选择中了
	 * @return
	 */
	private boolean addLable(TypeCv cv, LableData data) {
		boolean b = isExist(selectData, data);
		switch (cv) {
		case select:// 添加到我的选择
			selectedCv.addTagView(data, onClick, null, false, b);
			break;
		case me:// 添加到我的标签
			myLabelCv.addTagView(data, meOnClick, null, false, b);
			break;
		case defaule:// 添到默认
			defaultLabelCv.addTagView(data, defaultOnClick, null, false, b);
			break;
		}
		return true;
	}

	private boolean isExist(ArrayList<LableData> dataList, LableData data) {
		if (dataList!=null) {
			for (LableData lableData : dataList) {
				if (lableData!=null&&data!=null) {
					if (lableData.tag!=null&&data.tag!=null) {
						if (lableData.tag.equals(data.tag)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * 添加到 “已选择”
	 * 
	 * @param lableDate
	 */
	private void addSelected(LableData lableDate) {
		if (isExist(selectData, lableDate)) {
			return;
		}
		selectData.add(lableDate);
		addMe(lableDate);
		// System.out.println("添加selectData");

	}

	/**
	 * 添加到 "我的标签"
	 * 
	 * @param lableDate
	 */
	private void addMe(LableData lableDate) {
		if (isExist(meData, lableDate)) {
			return;
		}
		meData.add(lableDate);
	}

	/**
	 * 点击后添加/删除
	 * 
	 * @param v
	 */
	private void addOrDelete(TypeCv cv, View view) {
		DoubleTextViewTagLayout v = (DoubleTextViewTagLayout) view;
		LableData data = (LableData) v.getTag();
		if (isExist(selectData, data)) {
			v.changeBackground(false);
			deleteItem(selectData, data);
		} else {
			if (selectData.size() >= 10) {
				Toast.makeText(getApplicationContext(), "最多10个",
						Toast.LENGTH_SHORT).show();
				return;
			}
			// 如果点击的是推荐标签，而当前推荐标签为没有选中的状态的时候
			if (cv == TypeCv.defaule) {
				for (LableData i : defauleData) {
					if (i.tag!=null&&i.tag.equals(v.getContentText())) {
						if (isExist(selectData, i)) {
							v.changeBackground(false);
							deleteItem(selectData, i);
						} else {
							v.changeBackground(true);
							addSelected(i);
						}
						updateSelect();
						updateMyLabel();
//						 updateDefault();
						return;
					}
				}
				switch (mModulesType) {
				case DemandModules:
					DemandReqUtil.saveTag(CreateLabelActivity.this,
							CreateLabelActivity.this, v.getContentText(), 0, null);
					break;
				case OrgAndCustomModules:
					OrganizationReqUtil.addTag(CreateLabelActivity.this, CreateLabelActivity.this, v.getContentText(), null);
					break;
				case PeopleModules:
					PeopleReqUtil.addTag(CreateLabelActivity.this,CreateLabelActivity.this, v.getContentText(), null);
					break;
				case KnowledgeModules:
					listTag .add(v.getContentText());
					KnowledgeReqUtil.doEditUserKnowledgeTag(CreateLabelActivity.this, CreateLabelActivity.this, App.getUserID(), listTag, null);
					break;
				}
				return;
			}
			v.setSelected(true);
			addSelected(data);
			// selectData.add(data);
		}
		updateSelect();
		updateMyLabel();
		updateDefault();
	}

	private boolean deleteItem(ArrayList<LableData> lableDataL, LableData data) {

		for (LableData lableData : lableDataL) {
			if (lableData.tag.equals(data.tag)) {
				lableDataL.remove(lableData);
				return true;
			}
		}
		return false;
	}

	private EditText labelEt;
	private ModulesType mModulesType;
	private ArrayList<Integer> listCount;
	private ArrayList<String> listTagName;
	private ArrayList<LableData> userTagList;

	/**
	 * 更新已选择的
	 */
	private void updateSelect() {
		selectedCv.removeAllViews();
		for (LableData data : selectData) {
			addLable(TypeCv.select, data);// 判断我的选择中已存在
		}
		View v = View.inflate(this, R.layout.demand_create_label_tv, null);
		v.findViewById(R.id.labelTv).setVisibility(View.GONE);
		// 添加按钮
		View vTv = v.findViewById(R.id.addLabelTv);
		vTv.setVisibility(View.VISIBLE);
		vTv.setOnClickListener(new OnClickListener() {
			

			@Override
			public void onClick(View v) {
				if (selectData.size() >= 10) {
					Toast.makeText(getApplicationContext(), "最多10个",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (v.getId() == R.id.cancelTv) {
					dialog.dismiss();
				} else if (v.getId() == R.id.confirmTv) {
					if (labelEt != null) {
						s = labelEt.getText().toString().trim();
						if (!TextUtils.isEmpty(s)) {
							switch (mModulesType) {
							case DemandModules:
								DemandReqUtil.saveTag(CreateLabelActivity.this,
										CreateLabelActivity.this, s, 0, null);
								break;
							case OrgAndCustomModules:
								OrganizationReqUtil.addTag(CreateLabelActivity.this, CreateLabelActivity.this, s, null);
								break;
							case PeopleModules:
								PeopleReqUtil.addTag(CreateLabelActivity.this, CreateLabelActivity.this, s, null);
								break;
							case KnowledgeModules:
								listTag .add(s);
								KnowledgeReqUtil.doEditUserKnowledgeTag(CreateLabelActivity.this, CreateLabelActivity.this, App.getUserID(), listTag, null);
								break;
							}
						}
					}
					dialog.dismiss();
				} else {
					View view = View.inflate(CreateLabelActivity.this,
							R.layout.demand_label_create_dialog, null);
					view.findViewById(R.id.cancelTv).setOnClickListener(this);
					view.findViewById(R.id.confirmTv).setOnClickListener(this);
					labelEt = (EditText) view.findViewById(R.id.labelEt);
					labelEt.addTextChangedListener(new TextWatcher() {

						@Override
						public void onTextChanged(CharSequence s, int start,
								int before, int count) {
							if (s.length() > 10) {
								showToast("最多输入10个字符");
								labelEt.setText(s.subSequence(0, 10));
								labelEt.setSelection(labelEt.getText().toString().length());
							}

						}

						@Override
						public void beforeTextChanged(CharSequence s,
								int start, int count, int after) {
							// TODO Auto-generated method stub

						}

						@Override
						public void afterTextChanged(Editable s) {

						}
					});
					showDialog(view);
				}
				// selectList.add(object)

			}
		});
		selectedCv.addView(v);
	}

	/**
	 * 删除已选择的
	 * 
	 * @param str
	 */
	private void deleteSelected(View v) {
		LableData data = (LableData) v.getTag();
		// deleteItem(meData,data);
		deleteItem(selectData, data);
		updateMyLabel();
		updateDefault();
		updateSelect();
	}

	/**
	 * 更新我label状态
	 */
	private void updateMyLabel() {
		myLabelCv.removeAllViews();
		for (LableData data : meData) {
			addLable(TypeCv.me, data);// 判断我的选择中已存在
		}
	}

	/**
	 * 更新默认的状态
	 */
	private void updateDefault() {
		defaultLabelCv.removeAllViews();
		for (LableData data : defauleData) {
			addLable(TypeCv.defaule, data);// 判断我的选择中已存在
		}
	}

	private void showDialog(View view) {
		dialog = new Dialog(CreateLabelActivity.this, R.style.MyDialog);
		// dialog.setCancelable(false);//是否允许返回
		dialog.addContentView(view, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		dialog.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		}

	}

	class MyOnClickItem implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			deleteSelected(v);

		}

	}
	
	class TagMeOnClickItem implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			addOrDelete(TypeCv.me, v);
		}

	}
	
	class TagDefaultOnClickItem implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			addOrDelete(TypeCv.defaule, v);
		}

	}

	@Override
	public void bindData(int tag, Object object) {
		// dismissLoadingDialog();
		if (object == null) {
			return;
		}
		if (tag == EAPIConsts.demandReqType.demand_tag_list || tag == EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_TAG_LIST_GINTONG ||tag == EAPIConsts.PeopleRequestType.PEOPLE_REQ_TAG_LIST) {// 推荐标签
			ArrayList<LableData> recommend = (ArrayList<LableData>) object;
			for (LableData lableData : recommend) {
				defauleData.add(lableData);
			}

		} else if (tag == EAPIConsts.demandReqType.demand_QueryTag || tag == EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_TAG_QUERY || tag ==EAPIConsts.PeopleRequestType.PEOPLE_REQ_TAG_MY) {// 我的标签

			ArrayList<LableData> meLable = (ArrayList<LableData>) object;
			for (LableData lableData : meLable) {
				addMe(lableData);
			}

		} else if (tag == EAPIConsts.demandReqType.demand_saveTag) {// 创建新的标签
			if (object instanceof String) {
				showToast((String) object);
				// 打印创建的错误信息
			}
			if (object instanceof LableData) {
				// 添加到我的标签
				LableData addLable = (LableData) object;
				addSelected(addLable);
			}
		}else if (tag == EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_SAVE_TAG) {//添加标签(针对组织客户)
			if (object !=null) {
				HashMap<String, Object> hashMap = (HashMap<String, Object>) object;
				boolean success = (Boolean) hashMap.get("success");
				LableData lableData = (LableData) hashMap.get("obj");
				if (success&&lableData!=null) {
					addSelected(lableData);
				}else {
					Toast.makeText(CreateLabelActivity.this, "添加失败", 0);
				}
			}
			
		}else if (tag == EAPIConsts.PeopleRequestType.PEOPLE_REQ_TAG_SAVE) {//人脉添加标签
			ArrayList<LableData> lableDatas = new ArrayList<LableData>();
			if (object !=null) {
				HashMap<String, Object> hashMap = (HashMap<String, Object>) object;
				boolean success = (Boolean) hashMap.get("success");
				lableDatas = (ArrayList<LableData>) hashMap.get("obj");
				for (LableData lableData2 : lableDatas) {
					if (success&&lableData2!=null) {
						addSelected(lableData2);
					}else {
						Toast.makeText(CreateLabelActivity.this, "添加失败", 0);
					}
				}
			}
		}// 获取知识标签
		else if (tag == KnoReqType.GetKnowledgeTagList){
			String MSG = "bindData()--case KnoReqType.GetKnowledgeTagList:() ";

			if (object != null) {
				Map<String, Object> dataMap = (Map<String, Object>) object;
				listCount = (ArrayList<Integer>) dataMap.get("listCount");
				listTagName = (ArrayList<String>) dataMap.get("listTag");
				userTagList = new ArrayList<LableData>();
				for (int i = 0; i < listCount.size(); i++) {
					String tagName = listTagName.get(i);
					LableData lableData = new LableData();
					lableData.id = listCount.get(i);
					lableData.tag = tagName;
					userTagList.add(lableData);
					listTag.add(tagName);
				}
				for (LableData lableData : userTagList) {
					addMe(lableData);
				}
			}

		}else if (tag == KnoReqType.EditUserKnowledgeTag)

			if (object != null) {
				Map<String, Object> dataMap = (Map<String, Object>) object;

				boolean b = (Boolean) dataMap.get("success");
				if (b) {
					listCount = (ArrayList<Integer>) dataMap.get("listCount");
					listTagName = (ArrayList<String>) dataMap.get("listTag");

					userTagList = new ArrayList<LableData>();
					for (int i = 0; i < listCount.size(); i++) {
						LableData lableData = new LableData();
						lableData.tag = listTagName.get(i) + "" ; 
						lableData.id = listCount.get(i) ;
						userTagList.add(lableData);
					}
					for (LableData lableData2 : userTagList) {
						if (b&&lableData2!=null) {
							if (s.equals(lableData2.tag)) {
								addSelected(lableData2);
							}
						}else {
							Toast.makeText(CreateLabelActivity.this, "添加失败", 0);
						}
					}
				}
			}
		// TODO 需要添加新标签成功后
		updateDefault();
		updateMyLabel();
		updateSelect();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.demand_create_label_ok, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.save_ok:
			// selectList 所有已选择的标签
			Intent data = new Intent();
			data.putExtra(ENavConsts.DEMAND_LABEL_DATA, selectData);
			setResult(Activity.RESULT_OK, data);
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void initJabActionBar() {
		ActionBar mActionBar = jabGetActionBar();
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(true);
		View mCustomView = getLayoutInflater().inflate(
				R.layout.demand_actionbar_title, null);
		mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView
				.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK
				| Gravity.CENTER_HORIZONTAL;
		mActionBar.setCustomView(mCustomView, mP);
		mActionBar.setTitle(" ");
		TextView myTitle = (TextView) mCustomView.findViewById(R.id.titleTv);
		myTitle.setText("标签");
		mCustomView.findViewById(R.id.titleIv).setVisibility(View.GONE);

	}
}
