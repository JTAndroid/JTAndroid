package com.tr.ui.organization.create_clientele;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tr.R;
import com.tr.model.demand.Metadata;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.demand.util.ChooseDataUtil;
import com.tr.ui.organization.model.Area;
import com.tr.ui.organization.model.Relation;
import com.tr.ui.organization.model.government.AreaInfo;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.CustomActivity;
import com.tr.ui.people.cread.view.MyEditTextView;

/**
 * 地区概况(子模块)填写状态
 * @author Wxh07151732
 *
 */
public class AreaSurveyInput_Activity extends BaseActivity implements OnClickListener {
	private MyEditTextView org_area_input_name_Etv;
	private MyEditTextView org_area_input_othername_Etv;
	private MyEditTextView org_area_input_acreage_Etv;
	private MyEditTextView org_area_input_population_Etv;
	private MyEditTextView org_area_input_administrative_Etv;
	private MyEditTextView org_area_input_superior_Etv;
	private MyEditTextView org_area_input_GDP_Etv;
	private MyEditTextView org_area_input_resource_Etv;
	private MyEditTextView org_area_input_airport_Etv;
	private MyEditTextView org_area_input_train_station_Etv;
	private MyEditTextView org_area_input_enterprise_Etv;
	private MyEditTextView org_area_input_college_Etv;
	private MyEditTextView org_area_input_celebrity_Etv;
	private MyEditTextView org_area_input_synopsis_Etv;
	private MyEditTextView orge_area_input_custom_Etv;
	private RelativeLayout quit_area_input_Rl;
	private Intent intent;
	private ArrayList<String> list;
	private List<Relation> famousList;
	private List<CustomerPersonalLine> custom;
	private LinearLayout org_area_input_Ll;
	private ArrayList<CustomerPersonalLine> myEditTextView;
	private int dataIndex;
	private ArrayList<Metadata> metadataArea;
	private Area area;
	private String area_de;
	Boolean isNull;
	private ArrayList<MyEditTextView> editTextViews;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_area_survey_input);
		editTextViews = new ArrayList<MyEditTextView>();
		list = new ArrayList<String>();
		area = new Area();
		famousList = new ArrayList<Relation>();
		custom=  new ArrayList<CustomerPersonalLine>();
		init();
		initData();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.quit_area_input_Rl:
			finish();
			break;
		case R.id.org_area_input_administrative_Etv:
			dataIndex = 3;
			ENavigate.startChooseActivityForResult(this, false, "区域",
					ChooseDataUtil.CHOOSE_type_Area, null);
			break;
		case R.id.orge_area_input_custom_Etv:
			intent = new Intent(this, CustomActivity.class);
			intent.putExtra("fengxing", true);
			if (myEditTextView!=null) {
				if (!isNull) {
					Bundle bundle = new Bundle();
					bundle.putSerializable("Customer_Bean", myEditTextView);
					intent.putExtras(bundle);
				}
				
			}
			startActivityForResult(intent, 0);
			break;
		default:
			break;
		}
	}
	private void initData() {
		quit_area_input_Rl.setOnClickListener(this);
		org_area_input_administrative_Etv.setOnClickListener(this);
		orge_area_input_custom_Etv.setOnClickListener(this);
	}
	public void finish(View v){
		if (!TextUtils.isEmpty(org_area_input_name_Etv.getText())) {
			list.add(org_area_input_name_Etv.getTextLabel()+"_"+org_area_input_name_Etv.getText());
		}
		if (!TextUtils.isEmpty(org_area_input_othername_Etv.getText())) {
			list.add(org_area_input_othername_Etv.getTextLabel()+"_"+org_area_input_othername_Etv.getText());
		}
		if (!TextUtils.isEmpty(org_area_input_acreage_Etv.getText())) {
			list.add(org_area_input_acreage_Etv.getTextLabel()+"_"+org_area_input_acreage_Etv.getText()+""+org_area_input_acreage_Etv.getHintLabel());
		}
		if (!TextUtils.isEmpty(org_area_input_population_Etv.getText())) {
			list.add(org_area_input_population_Etv.getTextLabel()+"_"+org_area_input_population_Etv.getText()+""+org_area_input_population_Etv.getHintLabel());
		}
		if (!TextUtils.isEmpty(org_area_input_administrative_Etv.getText())) {
			list.add(org_area_input_administrative_Etv.getTextLabel()+"_"+org_area_input_administrative_Etv.getText());
		}
		if (!TextUtils.isEmpty(org_area_input_superior_Etv.getText())) {
			list.add(org_area_input_superior_Etv.getTextLabel()+"_"+org_area_input_superior_Etv.getText());
		}
		if (!TextUtils.isEmpty(org_area_input_GDP_Etv.getText())) {
			list.add(org_area_input_GDP_Etv.getTextLabel()+"_"+org_area_input_GDP_Etv.getText()+""+org_area_input_GDP_Etv.getHintLabel());
		}
		if (!TextUtils.isEmpty(org_area_input_resource_Etv.getText())) {
			list.add(org_area_input_resource_Etv.getTextLabel()+"_"+org_area_input_resource_Etv.getText());
		}
		if (!TextUtils.isEmpty(org_area_input_airport_Etv.getText())) {
			list.add(org_area_input_airport_Etv.getTextLabel()+"_"+org_area_input_airport_Etv.getText());
		}
		if (!TextUtils.isEmpty(org_area_input_train_station_Etv.getText())) {
			list.add(org_area_input_train_station_Etv.getTextLabel()+"_"+org_area_input_train_station_Etv.getText());
		}
		if (!TextUtils.isEmpty(org_area_input_enterprise_Etv.getText())) {
			list.add(org_area_input_enterprise_Etv.getTextLabel()+"_"+org_area_input_enterprise_Etv.getText());
		}
		if (!TextUtils.isEmpty(org_area_input_college_Etv.getText())) {
			list.add(org_area_input_college_Etv.getTextLabel()+"_"+org_area_input_college_Etv.getText());
		}
		if (!TextUtils.isEmpty(org_area_input_celebrity_Etv.getText())) {
			list.add(org_area_input_celebrity_Etv.getTextLabel()+"_"+org_area_input_celebrity_Etv.getText());
		}
		if (!TextUtils.isEmpty(org_area_input_synopsis_Etv.getText())) {
			list.add(org_area_input_synopsis_Etv.getTextLabel()+"_"+org_area_input_synopsis_Etv.getText());
		}
		AreaInfo areaInfo = new AreaInfo();
		areaInfo.name = org_area_input_name_Etv.getText();
		areaInfo.shotName =org_area_input_othername_Etv.getText();
		areaInfo.area = org_area_input_acreage_Etv.getText();
		areaInfo.type = org_area_input_administrative_Etv.getText();
		areaInfo.population = org_area_input_population_Etv.getText();
		areaInfo.parentArea = org_area_input_superior_Etv.getText();
		areaInfo.GDP = org_area_input_GDP_Etv.getText();
		areaInfo.resource = org_area_input_resource_Etv.getText();
		areaInfo.airport = org_area_input_airport_Etv.getText();
		areaInfo.train = org_area_input_train_station_Etv.getText();
		Relation mainCom = new Relation();
		mainCom.relation = org_area_input_enterprise_Etv.getText();
		areaInfo.mainCom =mainCom;
		areaInfo.mainColleges = org_area_input_college_Etv.getText();
		Relation famous = new Relation();
		famous.relation = org_area_input_celebrity_Etv.getText();
		famousList.add(famous);
		areaInfo.famousList = famousList ;
		areaInfo.remark = org_area_input_synopsis_Etv.getText();
		CustomerPersonalLine customerPersonalLine = new  CustomerPersonalLine();
		CustomerPersonalLine customerPersonalLine1 = new  CustomerPersonalLine();
		customerPersonalLine.name=orge_area_input_custom_Etv.getTextLabel();
		customerPersonalLine.content = orge_area_input_custom_Etv.getText();
		customerPersonalLine.type = "1";
		
//		if (myEditTextView!=null) {
//			customerPersonalLine1.name=myEditTextView.getTextLabel();
//			customerPersonalLine1.content=myEditTextView.getText();
//			customerPersonalLine1.type = "2";
//		}
		custom.add(customerPersonalLine);
		custom.add(customerPersonalLine1);
		areaInfo.propertyList = custom;
		if (area.province!=null) {
			area_de = "1";
		}else if(area.city!=null){
			area_de = "2";
		}else if(area.county!=null){
			area_de = "3";
		}
		Message message = Message.obtain();
		message.obj = area_de;
		FunctionalDepartmentActivity.handler.sendMessage(message);
		
		Bundle bundle = new Bundle();
		bundle.putSerializable("Area_Survey_Input_Bean", areaInfo);
		if (!list.isEmpty()) {
			Intent intent = new Intent();
			intent.putStringArrayListExtra("Area_Survey_Input_Activity", list);
			intent.putExtras(bundle);
			setResult(7001, intent);
		}
		finish();
	}
	private void init() {
		quit_area_input_Rl = (RelativeLayout) findViewById(R.id.quit_area_input_Rl);
		org_area_input_name_Etv = (MyEditTextView) findViewById(R.id.org_area_input_name_Etv);
		org_area_input_othername_Etv = (MyEditTextView) findViewById(R.id.org_area_input_othername_Etv);
		org_area_input_acreage_Etv = (MyEditTextView) findViewById(R.id.org_area_input_acreage_Etv);
		org_area_input_population_Etv = (MyEditTextView) findViewById(R.id.org_area_input_population_Etv);
		org_area_input_administrative_Etv = (MyEditTextView) findViewById(R.id.org_area_input_administrative_Etv);
		org_area_input_superior_Etv = (MyEditTextView) findViewById(R.id.org_area_input_superior_Etv);
		org_area_input_GDP_Etv = (MyEditTextView) findViewById(R.id.org_area_input_GDP_Etv);
		org_area_input_resource_Etv = (MyEditTextView) findViewById(R.id.org_area_input_resource_Etv);
		org_area_input_airport_Etv = (MyEditTextView) findViewById(R.id.org_area_input_airport_Etv);
		org_area_input_train_station_Etv = (MyEditTextView) findViewById(R.id.org_area_input_train_station_Etv);
		org_area_input_enterprise_Etv = (MyEditTextView) findViewById(R.id.org_area_input_enterprise_Etv);
		org_area_input_college_Etv = (MyEditTextView) findViewById(R.id.org_area_input_college_Etv);
		org_area_input_celebrity_Etv = (MyEditTextView) findViewById(R.id.org_area_input_celebrity_Etv);
		org_area_input_synopsis_Etv = (MyEditTextView) findViewById(R.id.org_area_input_synopsis_Etv);
		orge_area_input_custom_Etv = (MyEditTextView) findViewById(R.id.orge_area_input_custom_Etv);
		org_area_input_Ll = (LinearLayout) findViewById(R.id.org_area_input_Ll);
		
		AreaInfo areaInfo  =(AreaInfo) this.getIntent().getSerializableExtra("Area_Survey_Input_Bean");
		if (areaInfo!=null) {
			if (areaInfo.name!=null) {
				
			org_area_input_name_Etv.setText(areaInfo.name );
			 org_area_input_othername_Etv.setText(areaInfo.shotName);
			 org_area_input_acreage_Etv.setText(areaInfo.area );
			 org_area_input_administrative_Etv.setText(areaInfo.type);
			  org_area_input_population_Etv.setText(areaInfo.population);
			  org_area_input_superior_Etv.setText(areaInfo.parentArea);
			 org_area_input_GDP_Etv.setText(areaInfo.GDP);
			 org_area_input_resource_Etv.setText(areaInfo.resource);
			 org_area_input_airport_Etv.setText(	areaInfo.airport);
			org_area_input_train_station_Etv.setText(areaInfo.train );
			Relation r =  new Relation();
			r =areaInfo.mainCom;
			org_area_input_enterprise_Etv.setText(r.relation);
			 org_area_input_college_Etv.setText(areaInfo.mainColleges);
			 org_area_input_celebrity_Etv.setText(areaInfo.famousList.get(0).relation);
			  org_area_input_synopsis_Etv.setText(areaInfo.remark);
			  
			  if (customer.propertyList!=null) {
					if(!customer.propertyList.isEmpty()){
						for (int i = 0; i < customer.propertyList.size(); i++) {
							
							if ("1".equals(customer.propertyList.get(i).type)) {
								orge_area_input_custom_Etv.setText(customer.propertyList.get(0).content);
								orge_area_input_custom_Etv.setTextLabel(customer.propertyList.get(0).name);
							}
							else {
								MyEditTextView  editTextView = new MyEditTextView(context);
								editTextView.setCustom_Text(true);
								editTextView.setText(customer.propertyList.get(1).content);
								editTextView.setTextLabel(customer.propertyList.get(1).name);
								org_area_input_Ll.addView(editTextView,org_area_input_Ll.indexOfChild(orge_area_input_custom_Etv)+1);
							}
						}
						
					}
				}
		}
		}
	}
	public void setChooseText(ArrayList<Metadata> data) {
		switch (dataIndex) {
		case 1:
//			// 类型
//			if (metadataType != null) {
//				metadataType.clear();
//			}
//			metadataType = data;
//			demandType
//					.setText(ChooseDataUtil.getMetadataName(metadataType, 9));
			break;
		case 2:
//			// 行业
//			if (metadataIndustry != null) {
//				metadataIndustry.clear();
//			}
//			metadataIndustry = data;
//			org_industry_Etv.setText(ChooseDataUtil.getMetadataName(
//					metadataIndustry,9));
			break;
		case 3:
			// 地区
			if (metadataArea != null) {
				metadataArea.clear();
			}
			metadataArea = data;
			
			Area area_result = ChooseDataUtil
					.getMetadataName(metadataArea);
			org_area_input_administrative_Etv.setText((TextUtils.isEmpty(area_result.province) ? ""
					: area_result.province)
					+ (TextUtils.isEmpty(area_result.city) ? "" : area_result.city)
					+ (TextUtils.isEmpty(area_result.county) ? ""
							: area_result.county));
			break;
		}
	}
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg2!=null) {
			switch (arg1) {
			case 999:
				
				myEditTextView = custom2(arg2, orge_area_input_custom_Etv, org_area_input_Ll, isNull,editTextViews);
				break;
			
				
			default:
				break;
			}
			switch (arg0) {
			case ENavConsts.ActivityReqCode.REQUEST_CHOOSE_SELECT:
				// 多级选择回调界面
				setChooseText((ArrayList<Metadata>) arg2
						.getSerializableExtra(ENavConsts.DEMAND_CHOOSE_DATA));
				break;
			default:
				break;
			}
		}
		
	}
	
}
