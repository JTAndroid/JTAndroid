package com.tr.ui.communities.home;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.tr.App;
import com.tr.R;
import com.tr.api.CommunityReqUtil;
import com.tr.navigate.ENavConsts;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.communities.model.CommunityLabels;
import com.tr.ui.communities.model.Label;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.widgets.DoubleTextViewTagLayout;
import com.tr.ui.widgets.KnoTagGroupView;
import com.utils.common.GlobalVariable;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * 社群标签页
 */
public class CommunityLabelsActivity extends JBaseActivity implements IBindData {
	private enum TypeCv {
		select, me, defaule

	}

	private long communityId = -1;// 创建成功返回的社群id；
	private List<Label> selectData = new ArrayList<Label>();// 选中的对象
	private List<Label> meData = new ArrayList<Label>();// 自己自定义的标签
	private List<Label> defauleData = new ArrayList<Label>();// 推荐的热门标签
	private KnoTagGroupView selectedCv;
	private KnoTagGroupView myLabelCv;
	private KnoTagGroupView defaultLabelCv;
	private Dialog dialog;
	private MyOnClickItem onClick;
	private TagMeOnClickItem meOnClick;
	private TagDefaultOnClickItem defaultOnClick;

	private EditText labelEt;
	private String s;

	@Override
	public void initJabActionBar() {
		getBundle();
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "标签",
				false, null, true, true);
		setContentView(R.layout.activity_demand_label_check_create);
		initView();
		initSelected();// 乙选择的标签
		getData();
	}

	private void initView() {
		onClick = new MyOnClickItem();
		meOnClick = new TagMeOnClickItem();
		defaultOnClick = new TagDefaultOnClickItem();
		selectedCv = (KnoTagGroupView) findViewById(R.id.selectedCv);
		myLabelCv = (KnoTagGroupView) findViewById(R.id.myLabelCv);
		defaultLabelCv = (KnoTagGroupView) findViewById(R.id.defaultLabelCv);
	}

	private void getData() {
		showLoadingDialog();
		CommunityReqUtil.getCommunityTagList(CommunityLabelsActivity.this,
				this, null);
	}

	private void initSelected() {
		if (selectData != null) {
			for (Label lableData : selectData) {
				addSelected(lableData);
			}
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

	/**
	 * 删除已选择的
	 * 
	 * @param str
	 */
	private void deleteSelected(View v) {
		Label data = (Label) v.getTag();
		// deleteItem(meData,data);
		deleteItem(selectData, data);
		updateMyLabel();
		updateDefault();
		updateSelect();
	}

	/**
	 * 更新已选择的
	 */
	private void updateSelect() {
		selectedCv.removeAllViews();
		for (Label data : selectData) {
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
							showLoadingDialog();
							CommunityReqUtil.createCommunityTag(
									CommunityLabelsActivity.this,
									CommunityLabelsActivity.this, s,
									Long.parseLong(App.getApp().getUserID()),
									null);
						}
					}
					dialog.dismiss();
				} else {
					View view = View.inflate(CommunityLabelsActivity.this,
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
								labelEt.setSelection(labelEt.getText()
										.toString().length());
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

	private void showDialog(View view) {
		dialog = new Dialog(CommunityLabelsActivity.this, R.style.MyDialog);
		// dialog.setCancelable(false);//是否允许返回
		dialog.addContentView(view, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		dialog.show();
	}

	private boolean deleteItem(List<Label> lableDataL, Label data) {

		for (Label lableData : lableDataL) {
			if (lableData.getName().equals(data.getName())) {
				lableDataL.remove(lableData);
				return true;
			}
		}
		return false;
	}

	/**
	 * 更新我label状态
	 */
	private void updateMyLabel() {
		myLabelCv.removeAllViews();
		for (Label data : meData) {
			addLable(TypeCv.me, data);// 判断我的选择中已存在
		}
	}

	/**
	 * 更新默认的状态
	 */
	private void updateDefault() {
		defaultLabelCv.removeAllViews();
		for (Label data : defauleData) {
			addLable(TypeCv.defaule, data);// 判断我的选择中已存在
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
	private boolean addLable(TypeCv cv, Label data) {
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

	/**
	 * 点击后添加/删除
	 * 
	 * @param v
	 */
	private void addOrDelete(TypeCv cv, View view) {
		DoubleTextViewTagLayout v = (DoubleTextViewTagLayout) view;
		Label data = (Label) v.getTag();
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
				for (Label i : defauleData) {
					if (i.getName() != null && i.getName().equals(v.getContentText())) {
						if (isExist(selectData, i)) {
							v.changeBackground(false);
							deleteItem(selectData, i);
						} else {
							v.changeBackground(true);
							addSelected(i);
						}
						updateSelect();
						updateMyLabel();
						return;
					}
				}
				return;
			}
			v.changeBackground(true);
			addSelected(data);
		}
		updateSelect();
		updateMyLabel();
		updateDefault();
	}

	/**
	 * 添加到 “已选择”
	 * 
	 * @param lableDate
	 */
	private void addSelected(Label lableDate) {
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
	private void addMe(Label lableDate) {
		if (isExist(meData, lableDate)) {
			return;
		}
		meData.add(lableDate);
	}

	private boolean isExist(List<Label> dataList, Label data) {
		if (dataList != null) {
			for (Label lableData : dataList) {
				if (lableData != null && data != null) {
					if (lableData.getName() != null && data.getName() != null) {
						if (lableData.getName().equals(data.getName())) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private void getBundle() {
		Intent intent = getIntent();
		selectData = selectData == null ? selectData = new ArrayList<Label>()
				: selectData;
		communityId = getIntent().getLongExtra(GlobalVariable.COMMUNITY_ID, -1);
		if (intent != null) {
			selectData = (ArrayList<Label>) intent
					.getSerializableExtra(ENavConsts.DEMAND_LABEL_DATA);// 代表传进来的
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_createflow, menu);
		menu.findItem(R.id.flow_create).setTitle("完成");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.flow_create) {
			String id = String.valueOf(communityId);
			if (!id.contains("-1")) {
				doModify();
			} else {
				doFinish();
			}

		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 更改社群表签信息
	 */
	private void doModify() {
		showLoadingDialog();
		List<String> labelIds = new ArrayList<String>();
		if (null != selectData) {
			for (int i = 0; i < selectData.size(); i++) {
				String id = String.valueOf(selectData.get(i).getId());
				labelIds.add(id);
			}
		}
		CommunityReqUtil.doModifyCommunityLabels(this, this,
				String.valueOf(communityId), labelIds, null);
	}

	@Override
	public void bindData(int tag, Object object) {
		HashMap<String, Object> dataMap = (HashMap<String, Object>) object;
		switch (tag) {
		case EAPIConsts.CommunityReqType.TYPE_GET_COMMUNITY_LABELS:// 获取标签详情
			if (null != dataMap) {
				CommunityLabels communityLabels = (CommunityLabels) dataMap
						.get("result");
				meData = communityLabels.getUserDefinedLabel();
				defauleData = communityLabels.getHotLabel();
				defauleData = removeDuplicate(defauleData);
				meData = removeDuplicate(meData);
			}
			break;
		case EAPIConsts.CommunityReqType.TYPE_CREATE_LABEL:// 创建标签
			if (null != dataMap) {
				Long labelId = (Long) dataMap.get("labelId");
				// 添加到我的标签
				Label lable = new Label();
				lable.setId(labelId);
				lable.setName(s);
				addSelected(lable);
			}
			break;
		case EAPIConsts.CommunityReqType.TYPE_MODIFY_COMMUNITYLABELS:
			if (null != dataMap) {
				String notifCode = (String) dataMap.get("notifCode");
				if (notifCode.contains("1"))
					doFinish();
			} else {
				Toast.makeText(CommunityLabelsActivity.this, "修改标签失败",
						Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
		updateMyLabel();
		updateSelect();
		updateDefault();
		dismissLoadingDialog();
	}

	/**
	 * 去掉标签重复
	 * @param tempList
	 */
	private List<Label> removeDuplicate(List<Label> tempList) {
		for (int i = 0; i < tempList.size(); i++) // 外循环是循环的次数
		{
			for (int j = tempList.size() - 1; j > i; j--) // 内循环是
																// 外循环一次比较的次数
			{

				if (tempList.get(i).getName()
						.equals(tempList.get(j).getName())) {
					tempList.remove(j);
				}

			}
		}
		return tempList;
	}

	private void doFinish() {
		// selectList 所有已选择的标签
		Intent data = new Intent();
		data.putExtra(ENavConsts.DEMAND_LABEL_DATA, (Serializable) selectData);
		setResult(Activity.RESULT_OK, data);
		finish();
	}
}
