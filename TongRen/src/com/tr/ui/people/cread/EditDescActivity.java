package com.tr.ui.people.cread;

import java.util.ArrayList;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tr.R;
import com.tr.api.PeopleReqUtil;
import com.tr.navigate.ENavConsts;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.people.model.BaseResult;
import com.tr.ui.people.model.PeopleDetails;
import com.tr.ui.people.model.PeopleRequest;
import com.tr.ui.people.model.PermIds;
import com.tr.ui.people.model.PersonTagRelation;
import com.utils.http.EAPIConsts.PeopleRequestType;
import com.utils.http.IBindData;

/**
 * 编辑个人描述页面
 * 
 */
public class EditDescActivity extends JBaseActivity implements IBindData {

	@ViewInject(R.id.edit_remark_description)
	private EditText edit_remark_description;// 出生日期
	private String description;
	private PeopleDetails people_details = new PeopleDetails();// 人脉详情对象
	/** 向后台提交的人脉对象 */
	private PeopleRequest people_request;
	private ArrayList<Long> categoryList; // 目录
	private ArrayList<Long> tid; // 标签
	private PermIds permIds; // 权限
	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "个人描述", false, null, false, true);
		setContentView(R.layout.activity_edit_des);
		getBundle();
		ViewUtils.inject(this);
		initView();
	}

	private void getBundle() {
		people_details = (PeopleDetails) this.getIntent().getSerializableExtra(ENavConsts.datas);//
		description=people_details.getPeople().remark;

	}

	private void initView() {
		if (!TextUtils.isEmpty(description)) {
			edit_remark_description.setText(description);
		}
	}


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
			onSave();
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void onSave() {
		showLoadingDialog();
		people_request = new PeopleRequest(); // 向后台传入的对象
		people_request.opType = "5";// 编辑个人资料
		people_details.getPeople().remark=edit_remark_description.getText().toString();
		people_request.people = people_details.getPeople();
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
		if (people_details.getPeople().permIds != null) {
			permIds = people_details.getPeople().permIds;
		}
		people_request.permIds = permIds;
		people_request.asso = people_details.asso;

		PeopleReqUtil.doRequestWebAPI(EditDescActivity.this, this, people_request, null, PeopleRequestType.PEOPLE_REQ_CREATE);
	
	}

	@Override
	public void bindData(int tag, Object object) {
		switch (tag) {
		case PeopleRequestType.PEOPLE_REQ_CREATE:
			dismissLoadingDialog();
			if (object != null) {
				BaseResult result = (BaseResult) object;
				if (result.success){
					// 数据是使用Intent返回
					Intent intent = new Intent();
					// 把返回数据存入Intent
					intent.putExtra("people", people_request.people);
					// intent.putExtra("birthdate", birthdate);
					// intent.putExtra("birthCity", birthCity);
					// intent.putExtra("goodAt", goodAt);
					// intent.putExtra("hobby", hobby);
					// 设置返回数据
					this.setResult(RESULT_OK, intent);
					// 关闭Activity
					this.finish();
				}
				else
					Toast.makeText(EditDescActivity.this, "保存失败！", 0).show();
			} else {
				Toast.makeText(EditDescActivity.this, "保存失败！", 0).show();
			}
			break;

		default:
			break;
		}
		
	}
}
