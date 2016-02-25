package com.tr.ui.people.cread;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tr.R;
import com.tr.navigate.ENavConsts;
import com.tr.ui.adapter.WorkExperienceAdapter;
import com.tr.ui.adapter.WorkExperienceAdapter.OnEditExperience;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.people.model.PeopleDetails;
import com.tr.ui.people.model.Person;
import com.tr.ui.people.model.WorkExperience;

/**
 * 编辑工作经历列表
 * 
 */
public class EditWorkExperienceActivity extends JBaseActivity implements OnClickListener, OnEditExperience {
	@ViewInject(R.id.root_Ll)
	private LinearLayout root_Ll;
	@ViewInject(R.id.text_add_experience)
	private LinearLayout text_add_experience;
	@ViewInject(R.id.text_add_experience_Tv)
	private TextView text_add_experience_Tv;
	@ViewInject(R.id.list_edit_experience)
	private ListView list_edit_experience;
	/**
	 * 工作经历
	 */
	private List<WorkExperience> workExperienceList = new ArrayList<WorkExperience>();
	private WorkExperienceAdapter workExperienceAdapter;
	private static final int ADD = 0;
	private static final int EDIT = 1;
	private PeopleDetails people_details = new PeopleDetails();// 人脉详情对象
	private Person person;

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "编辑工作经历", false, null, false, true);
		setContentView(R.layout.activity_edit_experience);
		getBundle();
		ViewUtils.inject(this);
		initView();
	}

	private void getBundle() {
		people_details = (PeopleDetails) this.getIntent().getSerializableExtra(ENavConsts.datas);
		person = people_details.getPeople();
		workExperienceList = people_details.getPeople().getWorkExperienceList();
		
	}

	private void initView() {
		text_add_experience_Tv.setText("添加工作经历");
		workExperienceAdapter = new WorkExperienceAdapter(this, workExperienceList);
		workExperienceAdapter.isEdit(true);
		list_edit_experience.setAdapter(workExperienceAdapter);
		workExperienceAdapter.setOnEditExperience(this);
		text_add_experience.setOnClickListener(this);
//		if (workExperienceList.isEmpty()) {
//			root_Ll.setBackgroundResource(R.drawable.empty);
//		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.text_add_experience:
			Intent intent = new Intent(this, MyWorkExperienceActivity.class);
			intent.putExtra(ENavConsts.datas, people_details);
			intent.putExtra("type", ADD);
			startActivityForResult(intent, ADD);
			break;

		default:
			break;
		}
	}

	@Override
	public void onCompleted(int position, Object object) {
		//
		Intent intent = new Intent(this, MyWorkExperienceActivity.class);
		intent.putExtra(ENavConsts.datas, people_details);
		intent.putExtra("WorkExperience", workExperienceList.get(position));
		intent.putExtra("position", position);
		intent.putExtra("type", EDIT);
		startActivityForResult(intent, EDIT);
	}

	private void handlerActivityResult(int requestCode, Intent data) {
		WorkExperience experience;
		switch (requestCode) {
		case ADD:// 增加
			person = (Person) data.getSerializableExtra("people");
			people_details.setPeople(person) ;
			experience = (WorkExperience) data.getSerializableExtra("result");
			root_Ll.setBackgroundResource(R.color.project_bg);
			workExperienceList.add(experience);
			// workExperienceAdapter.add(experience);
			workExperienceAdapter.notifyDataSetChanged();
			break;
		case EDIT:// 编辑
			person = (Person) data.getSerializableExtra("people");
			people_details.setPeople(person) ;
			int position = data.getIntExtra("position", -1);
			experience = (WorkExperience) data.getSerializableExtra("result");
			if (null != experience) {
				this.workExperienceList.remove(position);
				this.workExperienceList.add(position, experience);
				// workExperienceAdapter.update(experience, position);
				workExperienceAdapter.notifyDataSetChanged();
			} else {

				this.workExperienceList.remove(position);
				// workExperienceAdapter.delete(position);
				workExperienceAdapter.notifyDataSetChanged();
			}
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (null != data) {
			handlerActivityResult(requestCode, data);
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// do something...
			onSave();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onSave();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}

	}

	private void onSave() {
		// 数据是使用Intent返回
		Intent intent = new Intent();
		// 把返回数据存入Intent
		intent.putExtra("people", person);
		// 设置返回数据
		this.setResult(RESULT_OK, intent);
		// 关闭Activity
		this.finish();
	}
}
