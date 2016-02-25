package com.tr.ui.organization.create_clientele;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.tr.R;
import com.tr.ui.organization.model.Relation;
import com.tr.ui.organization.model.profile.CustomerInfo;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.view.MyEditTextView;
/**
 * 机构简历
 * @author Wxh07151732
 *
 */
public class InstitutionsResumeActivity extends BaseActivity{
	private MyEditTextView government_address_Etv;
	private MyEditTextView government_development_area_Etv;
	private MyEditTextView government_station_Etv;
	private MyEditTextView government_phone_Etv;
	private MyEditTextView government_url_Etv;
	private MyEditTextView government_leader_Etv;
	private RelativeLayout quit_resume_Rl;
	private ArrayList<String> list;
	private List<Relation> expertList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_institutions_resume);
		list = new ArrayList<String>();
		expertList = new ArrayList<Relation>();
		init();
		initData();
	}

	private void initData() {
		
	}
	public void finish(View v){
		if (!TextUtils.isEmpty(government_address_Etv.getText())) {
			list.add(government_address_Etv.getTextLabel()+"_"+government_address_Etv.getText());
		}
		if (!TextUtils.isEmpty(government_development_area_Etv.getText())) {
			list.add(government_development_area_Etv.getTextLabel()+"_"+government_development_area_Etv.getText());
		}
		if (!TextUtils.isEmpty(government_station_Etv.getText())) {
			list.add(government_station_Etv.getTextLabel()+"_"+government_station_Etv.getText());
		}
		if (!TextUtils.isEmpty(government_url_Etv.getText())) {
			list.add(government_url_Etv.getTextLabel()+"_"+government_url_Etv.getText());
		}
		if (!TextUtils.isEmpty(government_phone_Etv.getText())) {
			list.add(government_phone_Etv.getTextLabel()+"_"+government_phone_Etv.getText());
		}
		if (!TextUtils.isEmpty(government_leader_Etv.getText())) {
			list.add(government_leader_Etv.getTextLabel()+"_"+government_leader_Etv.getText());
		}
		CustomerInfo customerInfo = new CustomerInfo();
		customerInfo.childArea = government_address_Etv.getText();
		customerInfo.devArea = government_development_area_Etv.getText();
		customerInfo.address = government_station_Etv.getText();
		customerInfo.website = government_url_Etv.getText();
		customerInfo.phone = government_phone_Etv.getText();
		Relation expert = new Relation();
		expert.relation = government_leader_Etv.getText();
		expertList.add(expert);
		customerInfo.expertList = expertList;
		Bundle bundle = new Bundle();
		bundle.putSerializable("Institutions_Bean", customerInfo);
		if (!list.isEmpty()) {
			Intent intent = new Intent();
			intent.putStringArrayListExtra("Institutions_resume_Activity", list);
			intent.putExtras(bundle);
			setResult(7006, intent);
		}
		finish();
	}
	private void init() {
		government_address_Etv = (MyEditTextView) findViewById(R.id.government_address_Etv);
		government_development_area_Etv = (MyEditTextView) findViewById(R.id.government_development_area_Etv);
		government_station_Etv = (MyEditTextView) findViewById(R.id.government_station_Etv);
		government_url_Etv = (MyEditTextView) findViewById(R.id.government_url_Etv);
		government_phone_Etv = (MyEditTextView) findViewById(R.id.government_phone_Etv);
		government_leader_Etv = (MyEditTextView) findViewById(R.id.government_leader_Etv);
		quit_resume_Rl = (RelativeLayout) findViewById(R.id.quit_resume_Rl);
		quit_resume_Rl.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		CustomerInfo customerInfo  =(CustomerInfo) this.getIntent().getSerializableExtra("Institutions_Bean");
		if (customerInfo!=null) {
			government_address_Etv.setText(customerInfo.childArea);
			government_development_area_Etv.setText(customerInfo.devArea );
			government_station_Etv.setText(customerInfo.address);
			government_url_Etv.setText(customerInfo.website);
			government_phone_Etv.setText(customerInfo.phone);
			government_leader_Etv.setText(customerInfo.expertList.get(0).relation );
		}
	
	}
}
