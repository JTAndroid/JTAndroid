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
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tr.R;
import com.tr.api.PeopleReqUtil;
import com.tr.navigate.ENavConsts;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.people.model.Education;
import com.tr.ui.people.model.PeopleDetails;
import com.tr.ui.people.model.PeopleRequest;
import com.tr.ui.people.model.PermIds;
import com.tr.ui.people.model.Person;
import com.tr.ui.people.model.PersonTagRelation;
import com.tr.ui.people.model.WorkExperience;
import com.tr.ui.work.WorkDatePickerDialog;
import com.tr.ui.work.WorkDatePickerDialog.OnDayChangeListener;
import com.utils.http.EAPIConsts.PeopleRequestType;
import com.utils.http.IBindData;

/**
 * 教育经历
 * 
 */
public class MyEducationExperienceActivity extends JBaseActivity implements OnClickListener, IBindData {
	private static final int DEGREEACTIVITY_REQUESTCODE = 1000;

	@ViewInject(R.id.text_school)
	private EditText text_school;// 学校名称

	@ViewInject(R.id.text_specialty)
	private EditText text_specialty;// 专业

	@ViewInject(R.id.text_start_time)
	private TextView text_start_time;// 开始时间
	@ViewInject(R.id.item_start_time)
	private LinearLayout item_start_time;// 开始时间item

	@ViewInject(R.id.text_close_time)
	private TextView text_close_time;// 结束时间
	@ViewInject(R.id.item_close_time)
	private LinearLayout item_close_time;// 结束时间item

	@ViewInject(R.id.text_degree)
	private TextView text_degree;// 学历

	@ViewInject(R.id.item_degree)
	private LinearLayout item_degree;// 学历
	
	@ViewInject(R.id.text_education_des_edit)
	private EditText text_education_des_edit;// 教育描述

	@ViewInject(R.id.text_education_action)
	private TextView text_education_action;// 动作保存/删除

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
	/**
	 * 教育经历
	 */
	private Education education = new Education();
	private int type = 0;// 0add 1 edit  2 删除
	private int position = -1;// 编辑的位置
	private PeopleDetails people_details = new PeopleDetails();// 人脉详情对象

	/** 向后台提交的人脉对象 */
	private PeopleRequest people_request;
	private ArrayList<Long> categoryList = new ArrayList<Long>(); // 目录
	private ArrayList<Long> tid = new ArrayList<Long>(); // 标签
	private PermIds permIds; // 权限
	private boolean isMeetTheConditions = false;

	private String degree;

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "教育经历", false, null, false, true);
		setContentView(R.layout.activity_myeducation_experience);
		getBundle();
		ViewUtils.inject(this);
		initView();
	}

	private void getBundle() {
		people_details = (PeopleDetails) this.getIntent().getSerializableExtra(ENavConsts.datas);
		type = this.getIntent().getIntExtra("type", 0);
		if (type == 1) {
			position = this.getIntent().getIntExtra("position", -1);
			education = (Education) this.getIntent().getSerializableExtra("EducationExperience");
		}
	}

	private void initView() {
		if (type == 1 && education != null) {
			text_education_action.setVisibility(View.VISIBLE);
			text_school.setText(education.school);// 学校
			text_specialty.setText(education.specialty);// 专业
			text_start_time.setText(String.valueOf(education.stime));
			text_close_time.setText(String.valueOf(education.etime));
			text_degree.setText(education.educationalBackgroundType);// 学位
			text_education_des_edit.setText(education.desc);
			text_education_action.setOnClickListener(this);
		} else {
			text_education_action.setVisibility(View.GONE);

		}
		item_start_time.setOnClickListener(this);
		item_close_time.setOnClickListener(this);
		item_degree.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		WorkDatePickerDialog datePicKDialog = new WorkDatePickerDialog(MyEducationExperienceActivity.this, null);
		InputMethodManager inputmanger = (InputMethodManager) getSystemService(MyEducationExperienceActivity.this.INPUT_METHOD_SERVICE);
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
		case R.id.item_degree :
			Intent degreeIntent = new Intent(this,DegreeActivity.class);
			degreeIntent.putExtra("degree", degree);
			startActivityForResult(degreeIntent, DEGREEACTIVITY_REQUESTCODE);
			break;
		case R.id.text_education_action:
			if (type == 1){
				type = 2 ;
				delete();
			}
			break;
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
			if (isMeetTheConditions){
				onSave();
			}
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void isMeetTheConditions() {
		if (TextUtils.isEmpty(text_school.getText().toString().trim())) {
			showToast("学校不能为空");
			isMeetTheConditions = false;
		} else if (TextUtils.isEmpty(text_specialty.getText().toString().trim())) {
			showToast("专业不能为空");
			isMeetTheConditions = false;
		} else if (TextUtils.isEmpty(text_start_time.getText().toString().trim())) {
			showToast("开始时间不能为空");
			isMeetTheConditions = false;
		} else if (TextUtils.isEmpty(text_close_time.getText().toString().trim())) {
			showToast("结束时间不能为空");
			isMeetTheConditions = false;
		} else if (TextUtils.isEmpty(text_degree.getText().toString().trim())) {
			showToast("学历不能为空");
			isMeetTheConditions = false;
		} else {
			isMeetTheConditions = true;
		}
	}

	/**
	 * 保持动作
	 */
	private void onSave() {
		education.school = text_school.getText().toString().trim();
		education.specialty = text_specialty.getText().toString().trim();
		String starttime = text_start_time.getText().toString().trim();
		if (!TextUtils.isEmpty(starttime)) {
			starttime = starttime.replace(".", "");
			education.stime = starttime;
		}
		String endtime = text_close_time.getText().toString().trim();
		if (!TextUtils.isEmpty(endtime)) {
			endtime = endtime.replace(".", "");
			education.etime = endtime;
		}
		education.educationalBackgroundType = text_degree.getText().toString().trim();
		education.desc = text_education_des_edit.getText().toString().trim();

		people_request = new PeopleRequest(); // 向后台传入的对象
		people_request.opType = "5";// 编辑个人资料
		Person person = people_details.people;
		List<Education> educationList = person.getEducationList();
		if (type == 0)
			educationList.add(education);
		else {
			educationList.remove(position);
			educationList.add(position, education);
		}
		person.setEducationList(educationList);
		people_request.people = person;
		doRequst();

	}

	private void delete() {
		people_request = new PeopleRequest(); // 向后台传入的对象
		people_request.opType = "5";// 编辑个人资料
		Person person = people_details.people;
		List<Education> educationList = person.getEducationList();
		educationList.remove(position);
		person.setEducationList(educationList);
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
			if (type == 0||type ==1){
				setResultAdd();
			}
			else{
				setResultDelete();
			}
				
			break;

		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data!=null) {
			if (requestCode == DEGREEACTIVITY_REQUESTCODE) {
				degree = data.getStringExtra("degree");
				text_degree.setText(degree);
			}
		}
	}
	private void setResultDelete() {
		// 数据是使用Intent返回
		Intent intent = new Intent();
		// 把返回数据存入Intent
		if (type == 2) {
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
		intent.putExtra("result", education);
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
