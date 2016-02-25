package com.tr.ui.demand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.App;
import com.tr.R;
import com.tr.api.DemandReqUtil;
import com.tr.api.KnowledgeReqUtil;
import com.tr.api.OrganizationReqUtil;
import com.tr.api.PeopleReqUtil;
import com.tr.model.demand.LableData;
import com.tr.model.knowledge.UserTag;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.demand.MyView.CustomView;
import com.tr.ui.demand.util.DemandAction;
import com.tr.ui.demand.util.TextStrUtil;
import com.tr.ui.knowledge.GlobalKnowledgeTagActivity;
import com.tr.ui.knowledge.MyKnowledgeActivity;
import com.tr.ui.knowledge.GlobalKnowledgeTagActivity.TagComparator;
import com.tr.ui.widgets.KnoTagGroupView;
import com.utils.common.EConsts;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * @ClassName: CreateLabelActivity.java
 * @author zcs
 * @Date 2015年3月14日 下午2:02:28
 * @Description: 修改标签
 */
public class RedactLabelActivity extends JBaseActivity implements
		OnClickListener, IBindData {

	private ArrayList<String> listString;
	private ArrayList<LableData> mustList = new ArrayList<LableData>();

	private KnoTagGroupView customLabelCv;
	private boolean isDelete = false;
	// private View headerVi;
	private EditText labelEt;
	private boolean isEdit = false;
	// private MyPopupWindow popu;
	private Dialog dialog;
	private boolean isType;
	private MenuItem findItem;
	private LableData removeLableData;

	ArrayList<LableData> lds = new ArrayList<LableData>();
	private MenuItem findItem2;
	/**区分哪个模块*/
	private ModulesType mModulesType;

	private String knowTag;
	
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demand_label_redact);
		listString = new ArrayList<String>();
		customLabelCv = (KnoTagGroupView) findViewById(R.id.meLabelCv);
		findViewById(R.id.createLabelTv).setOnClickListener(this);
		// headerVi = findViewById(R.id.headerVi);
		initData();// 初始化数据
		initBroadcast();
	}

	public void initBroadcast() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(DemandAction.DEMAND_LABLE_ACTIVITY);
		this.registerReceiver(lableBroadcast, filter);
	}

	/**
	 * 刷新列表的
	 */
	private BroadcastReceiver lableBroadcast = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (DemandAction.DEMAND_LABLE_ACTIVITY.equals(intent.getAction())) {
				customLabelCv.removeAllViews();// 清除所有数据
				listString.clear();
				showLoadingDialog();
				switch (mModulesType) {
				case KnowledgeModules:
					
					break;
				case OrgAndCustomModules:
					OrganizationReqUtil.getOrgAndCustTag(RedactLabelActivity.this,RedactLabelActivity.this, null);
					break;
				case PeopleModules:
					PeopleReqUtil.getPeopleTag(RedactLabelActivity.this,RedactLabelActivity.this, null);
					break;
				case DemandModules:
					DemandReqUtil.getDemandTag(RedactLabelActivity.this,RedactLabelActivity.this, null);
					break;
				}
			}
		}
	};

	private void initData() {
		// 清空一下多选的时候用的集合
		mustList.clear();
		// 拿到上一页返回的值 true的话不显示完成按钮，否则显示完成按钮
		isType = getIntent().getBooleanExtra(ENavConsts.DEMAND_LABEL_TYPE,
				false);
		mModulesType = (ModulesType) getIntent().getSerializableExtra(EConsts.Key.MODULES_TYPE);
		// 请求网络获取数据
		showLoadingDialog();
		switch (mModulesType) {
		case KnowledgeModules:
//			KnowledgeReqUtil.getKnowledgeTag(this, this, null);
			KnowledgeReqUtil.doGetKnowledgeTagList(this, this, App.getUserID(), null);
			break;
		case OrgAndCustomModules:
			OrganizationReqUtil.getOrgAndCustTag(this, this, null);
			break;
		case PeopleModules:
			PeopleReqUtil.getPeopleTag(RedactLabelActivity.this,RedactLabelActivity.this, null);
			break;
		case DemandModules:
			DemandReqUtil.getDemandTag(this, this, null);
			break;
		}
	}

	/**
	 * 设置可编辑状态
	 * 
	 * @param isDelete
	 */
	private void updateData(boolean isDelete) {
		// TODO 状态为删除 且 可删除
		int childCount = customLabelCv.getChildCount();
		this.isDelete = isDelete;
		for (int i = 0; i < childCount; i++) {
			View v = customLabelCv.getChildAt(i);
			v.findViewById(R.id.deleteIv).setVisibility(
					isDelete ? View.VISIBLE : View.GONE);
		}
	}

	// 长按
	class MyOnLongClick implements View.OnLongClickListener {

		@Override
		public boolean onLongClick(View v) {
			// updateData(true);
			LableData lableData = (LableData) v.getTag();
			switch (mModulesType) {
			case DemandModules:
				ENavigate.startDemandLableActivity(RedactLabelActivity.this,
						lableData, DemandLableActivity.ModulesType.DemandModules,true);
				break;
			case PeopleModules:
//				ENavigate.startDemandLableActivity(RedactLabelActivity.this,lableData, DemandLableActivity.ModulesType.PeopleModules,true);
				break;
			case OrgAndCustomModules:
//				ENavigate.startDemandLableActivity(RedactLabelActivity.this,lableData, DemandLableActivity.ModulesType.OrgAndCustomModules,true);
				break;
			case KnowledgeModules:
				
				break;
			}
			return true;
		}
	}

	class MyOnClick implements OnClickListener {

		@Override
		public void onClick(View v) {

			if (!isDelete) {
				if (isType) {// 点击一个标签调回上一个界面，并传递当前点击view的对象
					LableData lableData = (LableData) v.getTag();
					switch (mModulesType) {
					case DemandModules:
						ENavigate.startDemandLableActivity(
								RedactLabelActivity.this, lableData, DemandLableActivity.ModulesType.DemandModules,false);
						break;
					case PeopleModules:
						ENavigate.startDemandLableActivity(RedactLabelActivity.this,
								lableData, DemandLableActivity.ModulesType.PeopleModules,false);
						break;
					case OrgAndCustomModules:
						ENavigate.startDemandLableActivity(RedactLabelActivity.this,
								lableData, DemandLableActivity.ModulesType.OrgAndCustomModules,false);
						break;
					case KnowledgeModules:
						String name= lableData.tag;
						Long TagId = (long) lableData.id;
						ENavigate.startKnowledgeCategoryLabelActivity(RedactLabelActivity.this, null,null, TagId , name);
						break;
					}
				} else {
					if (v.isSelected()) {
						v.setSelected(false);
						mustList.remove((LableData) v.getTag());
					} else {
						v.setSelected(true);
						mustList.add((LableData) v.getTag());
					}
					if (mustList.size() == 0) {
						findItem.setTitle("完成");
					} else {
						findItem.setTitle("完成(" + mustList.size() + ")");
					}

				}
			} else {
				if (isDelete) {// 删除
					LableData lableData = (LableData) v.getTag();
					// 调删除接口
					switch (mModulesType) {
					case KnowledgeModules:
						ArrayList<String> listTag = new ArrayList<String>();
						for(LableData lbd:lds){
							if(lbd.tag.equals(lableData.tag)){
								lds.remove(lbd);
								break;
							}
						}
						for(LableData lbd:lds){
							listTag.add(lbd.tag);
						}
						
						knowTag ="编辑";
						KnowledgeReqUtil.doEditUserKnowledgeTag(RedactLabelActivity.this, RedactLabelActivity.this, App.getUserID(), listTag, null);
						break;
					case OrgAndCustomModules:
						OrganizationReqUtil.deleteTag(RedactLabelActivity.this,RedactLabelActivity.this, lableData.id, null);
						break;
					case PeopleModules:
						PeopleReqUtil.deleteTag(RedactLabelActivity.this,RedactLabelActivity.this, lableData.id, null);
						break;
					case DemandModules:
						DemandReqUtil.deleteTag(RedactLabelActivity.this,
								RedactLabelActivity.this, lableData.id, null);
						break;
					}
					removeLableData = lableData;
					String tag= lableData.tag;
					// 从本地集合里面删除
					removeLabelViewCv(v,tag);
				}
			}
		}
	}

	/**
	 * 移除一个
	 * 
	 * @param s
	 * @param view
	 */
	private void removeLabelViewCv(View view,String tag) {
//		TextView tv = (TextView) view.findViewById(R.id.labelTv);
		listString.remove(tag);
		customLabelCv.removeView(view);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.demand_create_label_ok, menu);
		inflater.inflate(R.menu.demand_create_label_edit, menu);

		findItem2 = menu.findItem(R.id.edit_ok);
		findItem = menu.findItem(R.id.save_ok);
		findItem.setVisible(false);
		findItem2.setVisible(false);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		if (isType) {
			findItem2.setVisible(true);
			findItem.setVisible(false);
		} else {
			findItem2.setVisible(false);
			findItem.setVisible(true);
		}
		return super.onPrepareOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.save_ok:
			if (mustList.size() < 1) {
				Toast.makeText(RedactLabelActivity.this, "标签不能为空",
						Toast.LENGTH_SHORT).show();
			} else {
				Intent data = new Intent();
				data.putExtra(ENavConsts.DEMAND_LABEL_DATA, mustList);
				setResult(RESULT_OK, data);
				finish();
			}
			break;
		case R.id.edit_ok:
			if (isEdit) {
				isEdit = false;
				updateData(false);
				findItem2.setTitle("编辑");
			} else {
				isEdit = true;
				updateData(true);
				findItem2.setTitle("取消");
			}

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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.createLabelTv:// 添加一个新标签
			View view = View.inflate(RedactLabelActivity.this,
					R.layout.demand_label_create_dialog, null);
			/*
			 * popu = new MyPopupWindow(RedactLabelActivity.this, view);
			 * popu.showAsDropDown(headerVi);
			 */
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
			break;
		case R.id.cancelTv:
			if (labelEt != null) {
				InputMethodManager m1 = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				m1.hideSoftInputFromWindow(labelEt.getApplicationWindowToken(),
						0);
			}
			dialog.dismiss();
			break;
		case R.id.confirmTv:
			String label = labelEt.getText().toString().trim();
			if (null == label || "".equals(label)) {
				Toast.makeText(RedactLabelActivity.this, "请输入标签名",
						Toast.LENGTH_SHORT).show();
			} else if (!TextStrUtil.maxLength(label, 20)) {
				Toast.makeText(RedactLabelActivity.this, "只能输入10个中文或者20个字符串",
						Toast.LENGTH_SHORT).show();
			} else {
				if (labelEt != null) {
					InputMethodManager m1 = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
					m1.hideSoftInputFromWindow(labelEt.getApplicationWindowToken(),
							0);
				}
				boolean bool = true;
				for (LableData lableData : lds) {
					if (lableData.tag.equals(label)) {
						bool = false;
						break;
					}
				}
				if (bool) {
					// 请求网络添加一个标签
					requstAddLabel(label);
					dialog.dismiss();
				}else {
					Toast.makeText(this, "标签已存在", 0).show();
				}
			}

			break;
		}
	}

	private void requstAddLabel(String tag) {
		switch (mModulesType) {
		case KnowledgeModules:
			ArrayList<String> listTag = new ArrayList<String>();
			for(LableData lbd:lds){
				listTag.add(lbd.tag);
			}
			listTag.add(tag);
			knowTag = "添加";
			KnowledgeReqUtil.doEditUserKnowledgeTag(this, this, App.getUserID(), listTag, null);
			break;
		case OrgAndCustomModules:
			OrganizationReqUtil.addTag(this, this, tag, null);
			break;
		case PeopleModules:
			PeopleReqUtil.addTag(this, this, tag, null);
			break;
		case DemandModules:
			DemandReqUtil.saveTag(RedactLabelActivity.this,
					RedactLabelActivity.this, tag, 0, null);
			break;
		}
	}

	private void showDialog(View view) {
		dialog = new Dialog(RedactLabelActivity.this, R.style.MyDialog);
		// dialog.setCancelable(false);//是否允许返回
		dialog.addContentView(view, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		dialog.show();
	}
	@Override
	protected void onStop() {
		InputMethodManager	imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
			if (imm != null) {
				imm.hideSoftInputFromWindow(this.getWindow().getDecorView()
						.getWindowToken(), 0);
				// imm.showSoftInputFromInputMethod(me.getWindow().getDecorView().getWindowToken(),0);
				// imm.hideSoftInputFromWindow(me.getWindow().getDecorView().getWindowToken(),InputMethodManager.HIDE_IMPLICIT_ONLY);
			}
			
		super.onStop();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && isDelete) {
			updateData(false);
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void bindData(int tag, Object object) {
		dismissLoadingDialog();
		if (object == null) {
			return;
		}
		if (tag == EAPIConsts.demandReqType.demand_saveTag) { // 创建标签---需求
			
			if (object instanceof String) {
				// object的实例是一个String 的话那么这就是错误信息打印出来
				Toast.makeText(RedactLabelActivity.this, (String) object,
						Toast.LENGTH_SHORT).show();
			} else {
				// 正常返回解析成功，把object变成一个对象，并添加上去
				lds.add(((LableData) object));
				customLabelCv.addTagView((LableData) object, new MyOnClick(), new MyOnLongClick(), true, false);
				updateData(isEdit);
			}

		} else if (tag == EAPIConsts.demandReqType.demand_getTagQuery || tag == EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_TAG_QUERY || tag == EAPIConsts.PeopleRequestType.PEOPLE_REQ_TAG_QUERY
				) { // 拿到标签信息
			// 把object转成一个集合
			lds = (ArrayList<LableData>) object;
			customLabelCv.addTagViews(lds, new MyOnClick(), new MyOnLongClick(), true, false);
		} else if (tag == EAPIConsts.demandReqType.demand_deleteTag || tag == EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_DELETE_TAG || tag == EAPIConsts.PeopleRequestType.PEOPLE_REQ_TAG_DELETE) {// 删除标签
			if (object instanceof Boolean) {
				if ((Boolean) object) {
					showToast("删除成功");
					lds.remove(removeLableData);
				} else {
					showToast("删除失败");
				}
			}
		}else if (tag == EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_SAVE_TAG) {//组织添加标签
			if (object !=null) {
				HashMap<String, Object> hashMap = (HashMap<String, Object>) object;
				boolean success = (Boolean) hashMap.get("success");
				LableData lableData = (LableData) hashMap.get("obj");
				if (success&&lableData!=null) {
					lds.add(lableData);
					customLabelCv.addTagView(lableData, new MyOnClick(), new MyOnLongClick(), true, false);
					updateData(isEdit);
				}else {
					Toast.makeText(RedactLabelActivity.this, "添加失败", 0);
				}
			}
			
		}else if (tag == EAPIConsts.PeopleRequestType.PEOPLE_REQ_TAG_SAVE) {//人脉添加标签
			if (object !=null) {
				HashMap<String, Object> hashMap = (HashMap<String, Object>) object;
				boolean success = (Boolean) hashMap.get("success");
				ArrayList<LableData> lableDatas = (ArrayList<LableData>) hashMap.get("obj");
				if(lableDatas!=null && lableDatas.size()>0){
					customLabelCv.addTagViews(lableDatas, new MyOnClick(), new MyOnLongClick(), true, false);
					updateData(isEdit);
					lds.addAll(lableDatas);
				}else{
					Toast.makeText(RedactLabelActivity.this, "添加失败", 0);
				}
			}
		}else if(tag == EAPIConsts.KnoReqType.GetKnowledgeTagList){
			Map<String, Object> dataMap = (Map<String, Object>) object;

			ArrayList<Integer> listCount = (ArrayList<Integer>) dataMap.get("listCount");
			ArrayList<String> listTag = (ArrayList<String>) dataMap.get("listTag");
			
			for (int i = 0; i < listCount.size(); i++) {
				LableData userTag = new LableData(0, listCount.get(i), listTag.get(i));
				lds.add(userTag);
			}
			customLabelCv.addTagViews(lds, new MyOnClick(), new MyOnLongClick(), true, false);
		}else if(tag == EAPIConsts.KnoReqType.EditUserKnowledgeTag){
			Map<String, Object> dataMap = (Map<String, Object>) object;
			lds.clear();
			customLabelCv.removeAllViews();
			boolean b = (Boolean) dataMap.get("success");
			if (b) {
				ArrayList<Integer> listCount = (ArrayList<Integer>) dataMap.get("listCount");
				ArrayList<String> listTag = (ArrayList<String>) dataMap.get("listTag");
				
				for (int i = 0; i < listCount.size(); i++) {
					LableData userTag = new LableData(0, listCount.get(i), listTag.get(i));
					lds.add(userTag);
				}
				customLabelCv.addTagViews(lds, new MyOnClick(), new MyOnLongClick(), true, false);
				updateData(isEdit);
				Toast.makeText(RedactLabelActivity.this, knowTag+"成功！", 0).show();
			} else {
				Toast.makeText(RedactLabelActivity.this, knowTag+"失败!", 0).show();
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (lableBroadcast != null) {
			unregisterReceiver(lableBroadcast);
		}
		super.onDestroy();
	}

	@Override
	public void onLoadingDialogCancel() {

	}
}
