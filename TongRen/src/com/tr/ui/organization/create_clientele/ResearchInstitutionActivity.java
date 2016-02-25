package com.tr.ui.organization.create_clientele;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tr.R;
import com.tr.ui.organization.model.Relation;
import com.tr.ui.organization.model.profile.CustomerInfo;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.CustomActivity;
import com.tr.ui.people.cread.view.MyEditTextView;
/**
 * 研究机构
 * @author Wxh07151732
 *
 */
public class ResearchInstitutionActivity extends BaseActivity {
	private RelativeLayout quit_research_institution_Rl;
	private MyEditTextView research_institution_principal_Etv;
	private MyEditTextView research_institution_specialist_Etv;
	private MyEditTextView research_institution_competent_Etv;
	private MyEditTextView research_institution_custom_Etv;
	private MyEditTextView research_institution_history_Etv;
	private ArrayList<String> list;
	private List<Relation> expertList;
	private List<CustomerPersonalLine> propertyList;
	private LinearLayout research_institution_Ll;
	private ArrayList<CustomerPersonalLine> custom;
	private boolean isNull;
	private ArrayList<MyEditTextView> editTextViews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_research_institution);
		list = new ArrayList<String>();
		editTextViews = new ArrayList<MyEditTextView>();
		expertList = new ArrayList<Relation>();
		propertyList = new ArrayList<CustomerPersonalLine>();
		init();
		initData();
	}

	private void initData() {
		
	}
	public void finish(View v){
		if (!TextUtils.isEmpty(research_institution_principal_Etv.getText())) {
			list.add(research_institution_principal_Etv.getTextLabel()+"_"+research_institution_principal_Etv.getText());
		}
		if (!TextUtils.isEmpty(research_institution_specialist_Etv.getText())) {
			list.add(research_institution_specialist_Etv.getTextLabel()+"_"+research_institution_specialist_Etv.getText());
		}
		if (!TextUtils.isEmpty(research_institution_competent_Etv.getText())) {
			list.add(research_institution_competent_Etv.getTextLabel()+"_"+research_institution_competent_Etv.getText());
		}
		if (!TextUtils.isEmpty(research_institution_custom_Etv.getText())) {
			list.add(research_institution_custom_Etv.getTextLabel()+"_"+research_institution_custom_Etv.getText());
		}
		if (!TextUtils.isEmpty(research_institution_history_Etv.getText())) {
			list.add(research_institution_history_Etv.getTextLabel()+"_"+research_institution_history_Etv.getText());
		}
		CustomerInfo customerInfo = new CustomerInfo();
		Relation leader = new Relation();
		leader.relation = research_institution_principal_Etv.getText();
		customerInfo.leader = leader ;
		Relation relation = new Relation();
		relation.relation = research_institution_specialist_Etv.getText();
		expertList.add(relation);
		customerInfo.expertList = expertList;
		Relation parentOrg = new Relation();
		parentOrg.relation = research_institution_competent_Etv.getText();
		customerInfo.parentOrg = parentOrg;
		CustomerPersonalLine customerPersonalLine = new CustomerPersonalLine();
		customerPersonalLine.name = research_institution_custom_Etv.getTextLabel();
		customerPersonalLine.content = research_institution_custom_Etv.getText();
		customerPersonalLine.type = "1";
//		if (custom!=null) {
//			CustomerPersonalLine customerPersonalLine1 = new CustomerPersonalLine();
//			customerPersonalLine1.name = custom.getTextLabel();
//			customerPersonalLine1.content = custom.getText();
//			customerPersonalLine1.type = "2";
//			propertyList.add(customerPersonalLine1);
//		}
		propertyList.add(customerPersonalLine);
		customerInfo.propertyList = propertyList;
		customerInfo.history = research_institution_history_Etv.getText();
		Bundle bundle = new Bundle();
		bundle.putSerializable("Research_institution_Bean", customerInfo);
		if (!list.isEmpty()) {
			Intent intent = new Intent();
			intent.putStringArrayListExtra("Research_institution_Activity", list);
			intent.putExtras(bundle);
			setResult(7009, intent);
		}
		finish();
	}
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg2!=null) {
			switch (arg1) {
			case 999:
				custom = custom2(arg2, research_institution_custom_Etv, research_institution_Ll, isNull,editTextViews);
				break;

			default:
				break;
			}
		}
	
	}
	private void init() {
		quit_research_institution_Rl = (RelativeLayout) findViewById(R.id.quit_research_institution_Rl);
		research_institution_principal_Etv = (MyEditTextView) findViewById(R.id.research_institution_principal_Etv);
		research_institution_Ll = (LinearLayout) findViewById(R.id.research_institution_Ll);
		research_institution_specialist_Etv = (MyEditTextView) findViewById(R.id.research_institution_specialist_Etv);
		research_institution_competent_Etv = (MyEditTextView) findViewById(R.id.research_institution_competent_Etv);
		research_institution_custom_Etv = (MyEditTextView) findViewById(R.id.research_institution_custom_Etv);
		research_institution_history_Etv = (MyEditTextView) findViewById(R.id.research_institution_history_Etv);
		quit_research_institution_Rl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		research_institution_custom_Etv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent= new Intent(ResearchInstitutionActivity.this, CustomActivity.class);
				if (custom!=null) {
					if (!isNull) {
						Bundle bundle = new Bundle();
						bundle.putSerializable("Customer_Bean", custom);
						intent.putExtras(bundle);
					}
					
				}
				startActivityForResult(intent, 0);
			}
		});
		CustomerInfo  customerInfo = (CustomerInfo) this.getIntent().getSerializableExtra("Research_institution_Bean");
		if (customerInfo!=null) {
		research_institution_principal_Etv.setText(customerInfo.leader.relation);
		research_institution_specialist_Etv.setText(customerInfo.expertList.get(0).relation);
		  research_institution_competent_Etv.setText(customerInfo.parentOrg.relation);
		  if (!customerInfo.propertyList.isEmpty()) {
			
		  for (int i = 0; i < customerInfo.propertyList.size(); i++) {
			  CustomerPersonalLine customerPersonalLine = customerInfo.propertyList.get(i);
			  if ("1".equals(customerPersonalLine.type)) {
				  research_institution_custom_Etv.setTextLabel(customerPersonalLine.name);
				  research_institution_custom_Etv.setText(customerPersonalLine.content );
			}else{
				MyEditTextView editTextView = new MyEditTextView(context);
				editTextView.setCustom_Text(true);
				editTextView.setTextLabel(customerPersonalLine.name);		
				editTextView.setText(customerPersonalLine.content )	;
			}
		  }
		}
		research_institution_history_Etv.setText(customerInfo.history);
	}
	}
}
