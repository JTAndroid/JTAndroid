package com.tr.ui.people.cread;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.http.client.RetryHandler;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tr.R;
import com.tr.api.PeopleReqUtil;
import com.tr.navigate.ENavConsts;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.people.model.PeopleDetails;
import com.tr.ui.people.model.PeopleRequest;
import com.tr.ui.people.model.PermIds;
import com.tr.ui.people.model.Person;
import com.tr.ui.people.model.PersonTagRelation;
import com.tr.ui.people.model.WorkExperience;
import com.tr.ui.work.WorkDatePickerDialog;
import com.tr.ui.work.WorkDatePickerDialog.OnDayChangeListener;
import com.utils.http.IBindData;
import com.utils.http.EAPIConsts.PeopleRequestType;

/**
 * 工作经历
 * 
 */
public class MyWorkExperienceActivity extends JBaseActivity implements OnClickListener, IBindData {
	@ViewInject(R.id.text_company)
	private EditText text_company;// 公司名称

	@ViewInject(R.id.text_department)
	private EditText text_department;// 部门

	@ViewInject(R.id.text_position)
	private EditText text_position;// 职位

	@ViewInject(R.id.text_start_time)
	private TextView text_start_time;// 开始时间
	@ViewInject(R.id.item_start_time)
	private LinearLayout item_start_time;// 开始时间item

	@ViewInject(R.id.text_close_time)
	private TextView text_close_time;// 结束时间
	@ViewInject(R.id.item_close_time)
	private LinearLayout item_close_time;// 结束时间item

	@ViewInject(R.id.edit_description_edit)
	private EditText edit_description_edit;// 经历描述

	@ViewInject(R.id.text_action)
	private TextView text_action;// 动作删除

	/** 向后台提交的人脉对象 */
	private PeopleRequest people_request;
	private ArrayList<Long> categoryList = new ArrayList<Long>(); // 目录
	private ArrayList<Long> tid = new ArrayList<Long>(); // 标签
	private PermIds permIds; // 权限

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
	private boolean isMeetTheConditions=false;
	private boolean isdelete=false;
	/**
	 * 工作经历
	 */
	private WorkExperience workExperience = new WorkExperience();
	private int type = 0;// 0add 1 edit
	private int position = -1;// 编辑的位置
	private PeopleDetails people_details = new PeopleDetails();// 人脉详情对象

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "工作经历", false, null, false, true);
		setContentView(R.layout.activity_mywork_experience);
		getBundle();
		ViewUtils.inject(this);
		initView();
	}

	private void getBundle() {
		people_details = (PeopleDetails) this.getIntent().getSerializableExtra(ENavConsts.datas);
		type = this.getIntent().getIntExtra("type", 0);
		if (type == 1) {
			position = this.getIntent().getIntExtra("position", -1);
			workExperience = (WorkExperience) this.getIntent().getSerializableExtra("WorkExperience");
		}
	}

	private void initView() {
		if (type == 1 && workExperience != null) {
			text_action.setVisibility(View.VISIBLE);
			text_company.setText(workExperience.company);
			text_department.setText(workExperience.department);
			text_position.setText(workExperience.position);
			text_start_time.setText(workExperience.stime);
			text_close_time.setText(workExperience.etime);
			edit_description_edit.setText(workExperience.desc);
			text_action.setText("删除");
			text_action.setOnClickListener(this);
		} else {
			text_action.setVisibility(View.GONE);

		}
		item_start_time.setOnClickListener(this);
		item_close_time.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		WorkDatePickerDialog datePicKDialog = new WorkDatePickerDialog(MyWorkExperienceActivity.this, null);
		InputMethodManager inputmanger = (InputMethodManager) getSystemService(MyWorkExperienceActivity.this.INPUT_METHOD_SERVICE);
		switch (v.getId()) {
		case R.id.item_start_time:// 开始时间

			datePicKDialog.setSimpleDateFormat(dateFormat);
			datePicKDialog.dateTimePicKDialog(0);

			datePicKDialog.setDayChangeListener(mStartTime);

			if (inputmanger.isActive()) {
				inputmanger.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);// 隐藏输入法
			}

			break;
		case R.id.item_close_time:// 结束时间
			datePicKDialog.setSimpleDateFormat(dateFormat);
			datePicKDialog.dateTimePicKDialog(0);

			datePicKDialog.setDayChangeListener(mEndTime);

			if (inputmanger.isActive()) {
				inputmanger.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);// 隐藏输入法
			}
			break;
		case R.id.text_action:
			if (type == 1)
				delete();

		default:
			break;
		}
	}

	public OnDayChangeListener mStartTime = new OnDayChangeListener() {

		@Override
		public void onDayChagne(String outDay) {
			text_start_time.setText(outDay);
		}
	};
	public OnDayChangeListener mEndTime = new OnDayChangeListener() {

		@Override
		public void onDayChagne(String outDay) {
			text_close_time.setText(outDay);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_new_main, menu);
		menu.findItem(R.id.home_new_menu_search).setVisible(false);
		menu.findItem(R.id.home_new_menu_more).setVisible(false);
		menu.findItem(R.id.menu_save).setVisible(true);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_save:
			isMeetTheConditions();
			if(isMeetTheConditions)
			onSave();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void isMeetTheConditions() {
		if (TextUtils.isEmpty(text_company.getText().toString().trim())) {
			showToast("公司不能为空");
			isMeetTheConditions=false;
		}/*else
		if (TextUtils.isEmpty(text_department.getText().toString().trim())) {
			showToast("部门不能为空");
			isMeetTheConditions=false;
		}*/else
		if (TextUtils.isEmpty(text_position.getText().toString().trim())) {
			showToast("职位不能为空");
			isMeetTheConditions=false;
		}else
		if (TextUtils.isEmpty(text_start_time.getText().toString().trim())) {
			showToast("开始时间不能为空");
			isMeetTheConditions=false;
		}else
		if (TextUtils.isEmpty(text_close_time.getText().toString().trim())) {
			showToast("结束时间不能为空");
			isMeetTheConditions=false;
		}else{
			isMeetTheConditions=true;
		}
	}

	/**
	 * 保持动作
	 */
	private void onSave() {
		isdelete=false;
		workExperience.setCompany(text_company.getText().toString().trim());
		workExperience.setDepartment(text_department.getText().toString().trim());
		workExperience.setPosition(text_position.getText().toString().trim());
		workExperience.setsTime(text_start_time.getText().toString().trim());
		workExperience.seteTime(text_close_time.getText().toString().trim());
		workExperience.setDesc(edit_description_edit.getText().toString().trim());

		people_request = new PeopleRequest(); // 向后台传入的对象
		people_request.opType = "5";// 编辑个人资料
		Person person = people_details.people;
		List<WorkExperience> workExperienceList = person.getWorkExperienceList();
		if (type == 0)
			workExperienceList.add(workExperience);
		else {
			workExperienceList.remove(position);
			workExperienceList.add(position, workExperience);
		}

		person.setWorkExperienceList(workExperienceList);
		people_request.people = person;
		doRequst();

	}

	private void delete() {
		isdelete=true;
		people_request = new PeopleRequest(); // 向后台传入的对象
		people_request.opType = "5";// 编辑个人资料
		Person person = people_details.people;
		List<WorkExperience> workExperienceList = person.getWorkExperienceList();
		workExperienceList.remove(position);
		person.setWorkExperienceList(workExperienceList);
		people_request.people = person;

		doRequst();

	}

	private void doRequst() {
		for (int i = 0; people_details.tid != null && i < people_details.tid.size(); i++) {
			PersonTagRelation personTagRelation = people_details.tid.get(i);
			tid.add(personTagRelation.tagId);
		}
		people_request.tid = tid;

		for (int i = 0; people_details.categoryList != null && i < people_details.categoryList.size(); i++) {
			Long categoryId = people_details.categoryList.get(i).id;
			categoryList.add(categoryId);
		}
		people_request.categoryList = categoryList;
		if (people_details.people.permIds != null) {
			permIds = people_details.people.permIds;
		}
		people_request.permIds = permIds;
		people_request.asso = people_details.asso;

		PeopleReqUtil.doRequestWebAPI(this, this, people_request, null, PeopleRequestType.PEOPLE_REQ_CREATE);
	}

	@Override
	public void bindData(int tag, Object object) {
		switch (tag) {
		case PeopleRequestType.PEOPLE_REQ_CREATE:
			if (!isdelete)
				setResultAdd();
			else
				setResultDelete();
			break;

		default:
			break;
		}
	}

	private void setResultDelete() {
		// 数据是使用Intent返回
		Intent intent = new Intent();
		// 把返回数据存入Intent
		if (type == 1) {
			intent.putExtra("position", position);
		}
		intent.putExtra("people", people_request.people);
		// 设置返回数据
		this.setResult(RESULT_OK, intent);
		// 关闭Activity
		this.finish();
	}

	private void setResultAdd() {
		// 数据是使用Intent返回
		Intent intent = new Intent();
		// 把返回数据存入Intent
		intent.putExtra("result", workExperience);
		if (type == 1) {
			intent.putExtra("position", position);
		}
		intent.putExtra("people", people_request.people);
		// 设置返回数据
		this.setResult(RESULT_OK, intent);
		// 关闭Activity
		this.finish();
	}
}
