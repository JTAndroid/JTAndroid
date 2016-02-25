package com.tr.ui.communities.home;

import java.util.HashMap;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tr.App;
import com.tr.R;
import com.tr.api.CommunityReqUtil;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.communities.model.CreateSet;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.utils.common.GlobalVariable;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * 社群权限页面
 * 
 */
public class CommumitiesPermissionActivity extends JBaseActivity implements IBindData {
	@ViewInject(R.id.group_jion_permisson)
	private RadioGroup group_jion_permisson;// 加入群权限

	@ViewInject(R.id.group_menber)
	private CheckBox group_menber;// 群成员
	@ViewInject(R.id.group_people)
	private CheckBox group_people;// 群人脉
	@ViewInject(R.id.group_company)
	private CheckBox group_company;// 群公司
	@ViewInject(R.id.group_know)
	private CheckBox group_know;// 群知识
	@ViewInject(R.id.group_demand)
	private CheckBox group_demand;// 群需求

	@ViewInject(R.id.group_state)
	private RadioGroup group_state;// 隐身状态
	private CreateSet createSet = new CreateSet();
	private long communityId = -1;// 创建成功返回的社群id；

	private int applayType = 1;// 设置加入社群权限:1是所有人 2申请加入. 默认传1.
	private int memberShowType = 1;// 群成员对外是否可见：1可见 2是不可见. 默认传1.
	private int peopleShowType = 1;// 群人脉显示 1可见 2是不可见. 默认传1.
	private int companyShowType = 1;// 群企业对外是否可见：1可见 2是不可见. 默认传1.
	private int knowledgeShowType = 1;// 群知识对外是否可见：1可见 2是不可见. 默认传1.
	private int demandShowType = 1;// 群需求对外是否可见：1可见 2是不可见. 默认传1.
	private int communityShowType = 1;// 隐身状态：1不隐身 2是隐身. 默认传1.

	@Override
	public void initJabActionBar() {
		getBundle();
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "社群权限", false, null, true, true);
		setContentView(R.layout.activity_commumitiespermission);
		ViewUtils.inject(this);
		updateUi();
		initView();
	}

	private void updateUi() {

		// 设置加入社群权限:1是所有人 2申请加入. 默认传1.
		switch (applayType) {
		case 1:
			group_jion_permisson.check(R.id.radio_all);
			break;
		case 2:
			group_jion_permisson.check(R.id.radio_apply);
			break;

		default:
			break;
		}
		// 群成员对外是否可见：1可见 2是不可见. 默认传1.
		if (memberShowType == 1)
			group_menber.setChecked(true);
		else
			group_menber.setChecked(false);
		// 群人脉显示 1可见 2是不可见. 默认传1.
		if (peopleShowType == 1)
			group_people.setChecked(true);
		else
			group_people.setChecked(false);
		// 群企业对外是否可见：1可见 2是不可见. 默认传1.
		if (companyShowType == 1)
			group_company.setChecked(true);
		else
			group_company.setChecked(false);
		// 群知识对外是否可见：1可见 2是不可见. 默认传1.
		if (knowledgeShowType == 1)
			group_know.setChecked(true);
		else
			group_know.setChecked(false);
		// 群需求对外是否可见：1可见 2是不可见. 默认传1.
		if (demandShowType == 1)
			group_demand.setChecked(true);
		else
			group_demand.setChecked(false);
		// 隐身状态：1不隐身 2是隐身. 默认传1.
		switch (communityShowType) {
		case 1:
			group_state.check(R.id.radio_show);
			break;
		case 2:
			group_state.check(R.id.radio_hiding);
			break;

		default:
			break;
		}
	}

	private void getBundle() {
		communityId = getIntent().getLongExtra(GlobalVariable.COMMUNITY_ID, -1);
		createSet = (CreateSet) getIntent().getSerializableExtra("permission");
		applayType = createSet.getApplayType();// 设置加入社群权限:1是所有人 2申请加入. 默认传1.
		memberShowType = createSet.getMemberShowType();// 群成员对外是否可见：1可见 2是不可见.
														// 默认传1.
		peopleShowType = createSet.getPeopleShowType();// 群人脉显示 1可见 2是不可见. 默认传1.
		companyShowType = createSet.getCompanyShowType();// 群企业对外是否可见：1可见 2是不可见.
															// 默认传1.
		knowledgeShowType = createSet.getKnowledgeShowType();// 群知识对外是否可见：1可见
																// 2是不可见. 默认传1.
		demandShowType = createSet.getDemandShowType();// 群需求对外是否可见：1可见 2是不可见.
														// 默认传1.
		communityShowType = createSet.getCommunityShowType();
	}

	private void initView() {
		group_menber.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked)
					memberShowType = 1;
				else
					memberShowType = 2;
			}

		});
		group_people.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked)
					peopleShowType = 1;
				else
					peopleShowType = 2;
			}

		});
		group_company.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked)
					companyShowType = 1;
				else
					companyShowType = 2;
			}

		});
		group_know.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked)
					knowledgeShowType = 1;
				else
					knowledgeShowType = 2;
			}

		});
		group_demand.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked)
					demandShowType = 1;
				else
					demandShowType = 2;
			}

		});

		group_jion_permisson.setOnCheckedChangeListener(member);
		group_state.setOnCheckedChangeListener(state);
	}

	private OnCheckedChangeListener member = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.radio_all:

				applayType = 1;
				break;
			case R.id.radio_apply:

				applayType = 2;
				break;

			default:
				break;
			}
		}
	};
	private OnCheckedChangeListener state = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.radio_show:

				communityShowType = 1;
				break;
			case R.id.radio_hiding:

				communityShowType = 2;
				break;

			default:
				break;
			}
		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// do something...
			if (String.valueOf(communityId).contains("-1"))
				onSave();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (String.valueOf(communityId).contains("-1")) 
				onSave();
			break;
		case R.id.flow_create:
			if (!(String.valueOf(communityId).contains("-1"))) {
				saveSet();
				doModify();
			}
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!(String.valueOf(communityId).contains("-1"))) {
			getMenuInflater().inflate(R.menu.menu_createflow, menu);
			menu.findItem(R.id.flow_create).setTitle("完成");
		}
		return super.onCreateOptionsMenu(menu);
	}

	private void doModify() {
		showLoadingDialog();
		CommunityReqUtil.doModifyCommunityPermission(this, this, createSet, null);
	}

	private void onSave() {
		saveSet();
		setResult();

	}

	private void setResult() {
		Intent intent = new Intent();
		intent.putExtra("permission", createSet);
		setResult(RESULT_OK, intent);
		finish();
	}

	private void saveSet() {
		createSet = new CreateSet();
		createSet.setCreatedUserId(Long.parseLong(String.valueOf(App.getApp().getUserID())));
		createSet.setApplayType(applayType);
		if (!(String.valueOf(communityId).contains("-1")))
		createSet.setCommunityId(communityId);
		createSet.setMemberShowType(memberShowType);
		createSet.setPeopleShowType(peopleShowType);
		createSet.setActivityShowType(1);
		createSet.setCompanyShowType(companyShowType);
		createSet.setKnowledgeShowType(knowledgeShowType);
		createSet.setDemandShowType(demandShowType);
		createSet.setCommunityShowType(communityShowType);
	}

	@Override
	public void bindData(int tag, Object object) {
		HashMap<String, Object> dataMap = (HashMap<String, Object>) object;
		switch (tag) {
		case EAPIConsts.CommunityReqType.TYPE_MODIFY_COMMUNITY_PERMISSION:
			if (null != dataMap) {
				String notifCode = (String) dataMap.get("notifCode");
				if (notifCode.contains("1"))
					setResult();
			}
			break;

		default:
			break;
		}
		dismissLoadingDialog();
	}

}
