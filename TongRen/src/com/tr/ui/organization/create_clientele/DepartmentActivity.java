package com.tr.ui.organization.create_clientele;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tr.R;
import com.tr.ui.organization.model.Relation;
import com.tr.ui.organization.model.government.DepartMentsInfo;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.view.MyEditTextView;
/**
 * 主要职能部门-部门
 * @author Wxh07151732
 *
 */
public class DepartmentActivity extends BaseActivity {
	private RelativeLayout quit_department_Rl;
	private MyEditTextView org_department_address_Mv;
	private MyEditTextView org_department_url_Mv;
	private MyEditTextView org_department_phone_Mv;
	private MyEditTextView org_department_fox_Mv;
	private MyEditTextView org_department_leader_Mv;
	private ArrayList<String> list;
	private String department_Text;
	private TextView org_department_Tv;
	private List<Relation> leader_list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_department);
		list = new ArrayList<String>();
		department_Text = this.getIntent().getStringExtra("Department_Activity");
		leader_list = new ArrayList<Relation>();
		init();
		initData();
	}
	public void finish(View v){
		if (!TextUtils.isEmpty(org_department_address_Mv.getText())) {
			list.add(org_department_address_Mv.getTextLabel()+"_"+org_department_address_Mv.getText());
		}
		if (!TextUtils.isEmpty(org_department_url_Mv.getText())) {
			list.add(org_department_url_Mv.getTextLabel()+"_"+org_department_url_Mv.getText());
		}
		if (!TextUtils.isEmpty(org_department_phone_Mv.getText())) {
			list.add(org_department_phone_Mv.getTextLabel()+"_"+org_department_phone_Mv.getText());
		}
		if (!TextUtils.isEmpty(org_department_fox_Mv.getText())) {
			list.add(org_department_fox_Mv.getTextLabel()+"_"+org_department_fox_Mv.getText());
		}
		if (!TextUtils.isEmpty(org_department_leader_Mv.getText())) {
			list.add(org_department_leader_Mv.getTextLabel()+"_"+org_department_leader_Mv.getText());
		}
		DepartMentsInfo departMentsInfo = new DepartMentsInfo();
		departMentsInfo.departName = department_Text;
		departMentsInfo.address = org_department_address_Mv.getText();
		departMentsInfo.website= org_department_url_Mv.getText();
		departMentsInfo.phone = org_department_phone_Mv.getText();
		departMentsInfo.fax = org_department_fox_Mv.getText();
		Relation relation = new Relation();
		relation.relation = org_department_leader_Mv.getText();
		leader_list.add(relation);
		departMentsInfo.list = leader_list;
		Bundle bundle = new Bundle();
		bundle.putSerializable("Department_Bean", departMentsInfo);
		if (!list.isEmpty()) {
			Intent intent = new Intent();
			intent.putStringArrayListExtra("Department_Activity", list);
			intent.putExtras(bundle);
			setResult(6001, intent);
		}
		finish();
	}
	private void init() {
		
		quit_department_Rl = (RelativeLayout) findViewById(R.id.quit_department_Rl);
		org_department_Tv = (TextView) findViewById(R.id.org_department_Tv);
		org_department_address_Mv = (MyEditTextView) findViewById(R.id.org_department_address_Mv);
		org_department_url_Mv = (MyEditTextView) findViewById(R.id.org_department_url_Mv);
		org_department_phone_Mv = (MyEditTextView) findViewById(R.id.org_department_phone_Mv);
		org_department_fox_Mv = (MyEditTextView) findViewById(R.id.org_department_fox_Mv);
		org_department_leader_Mv = (MyEditTextView) findViewById(R.id.org_department_leader_Mv);
		quit_department_Rl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		DepartMentsInfo departMentsInfo = (DepartMentsInfo) this.getIntent().getSerializableExtra("Department_Bean");
		if (departMentsInfo!=null) {
			if (departMentsInfo.departName!=null) {
			org_department_address_Mv.setText(departMentsInfo.address);
			 org_department_url_Mv.setText(departMentsInfo.website);
			 org_department_phone_Mv.setText(departMentsInfo.phone);
			 org_department_fox_Mv.setText(departMentsInfo.fax);
			org_department_leader_Mv.setText(departMentsInfo.list.get(0).relation);
			}
		}
	}

	private void initData() {
		if (department_Text!=null) {
			org_department_Tv.setText(department_Text);
		}
	}
}
