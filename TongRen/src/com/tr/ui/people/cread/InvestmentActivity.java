package com.tr.ui.people.cread;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.tr.R;
import com.tr.model.demand.Metadata;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.demand.util.ChooseDataUtil;
import com.tr.ui.organization.model.Area;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.organization.model.resource.CustomerDemandCommon;
import com.tr.ui.people.cread.view.MyDeleteView;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.tr.ui.people.cread.view.MyLineraLayout;

/**
 * 投资意向(子模块)
 * 
 * @author Wxh07151732
 * 
 */
public class InvestmentActivity extends BaseActivity implements
		OnClickListener {
	private TextView finish_investment_Tv;
	private MyEditTextView area_investment_Etv;
	private MyEditTextView industry_investment_Etv;
	private MyEditTextView type_investment_Etv;
	private MyEditTextView custom_investment_Etv;
	private ArrayList<String> list;
	private Intent intent;
	private LinearLayout continueadd_Ll;
	private LinearLayout investment_Ll;
	private LinearLayout delete_Ll;
	private RelativeLayout quit_investment_Rl;
	private ArrayList<String> list1;
	private ArrayList<MyEditTextView> list2;
	private static ArrayList<MyEditTextView> continueAdd;
	private ArrayList<MyLineraLayout> layouts;
	private int count = 0;
	private LinearLayout investment_main_Ll;
	private int continueAdd2;
	
	private CustomerDemandCommon customer;
	private boolean isNull;
	private int dataIndex;
	private ArrayList<Metadata> metadataType;
	private ArrayList<Metadata> metadataIndustry;
	private ArrayList<Metadata> metadataArea;
	private Area area;
	private ArrayList<CustomerPersonalLine> arrayList;
	private ArrayList<CustomerPersonalLine> arrayList2;
	private ArrayList<MyEditTextView> editTextViews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_investment);
		area = new Area();
		init();
		editTextViews = new ArrayList<MyEditTextView>();
		list = new ArrayList<String>();
		list1 = new ArrayList<String>();
		list2 = new ArrayList<MyEditTextView>();
		layouts = new ArrayList<MyLineraLayout>();
	}

	private void init() {
		
		customer = (CustomerDemandCommon) getIntent().getSerializableExtra("Customer");
		
		quit_investment_Rl = (RelativeLayout) findViewById(R.id.quit_investment_Rl);
		finish_investment_Tv = (TextView) findViewById(R.id.finish_investment_Tv);
		
		area_investment_Etv = (MyEditTextView) findViewById(R.id.area_investment_Etv);
		industry_investment_Etv = (MyEditTextView) findViewById(R.id.industry_investment_Etv);
		type_investment_Etv = (MyEditTextView) findViewById(R.id.type_investment_Etv);
		custom_investment_Etv = (MyEditTextView) findViewById(R.id.custom_investment_Etv);
		
		if(customer != null){
			
			area_investment_Etv.setText(customer.getAddress().getAddress());
			
			industry_investment_Etv.setText(customer.getIndustryNames());
			
			type_investment_Etv.setText(customer.getTypeNames());
			
			custom_investment_Etv.setTextLabel(customer.getPersonalLineList().get(0).getName());
			
			custom_investment_Etv.setText(customer.getPersonalLineList().get(0).getContent());
		}
		
		continueadd_Ll = (LinearLayout) findViewById(R.id.continueadd_Ll);
		investment_Ll = (LinearLayout) findViewById(R.id.investment_Ll);
		investment_main_Ll = (LinearLayout) findViewById(R.id.investment_main_Ll);
		delete_Ll = (LinearLayout) findViewById(R.id.delete_Ll);
		TextView continueadd_Tv = (TextView) findViewById(R.id.continueadd_Tv);
		continueadd_Tv.setText("新增投资意向");
		quit_investment_Rl.setOnClickListener(this);
		delete_Ll.setOnClickListener(this);
		continueadd_Ll.setOnClickListener(this);
		finish_investment_Tv.setOnClickListener(this);
		area_investment_Etv.setOnClickListener(this);
		industry_investment_Etv.setOnClickListener(this);
		type_investment_Etv.setOnClickListener(this);
		custom_investment_Etv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.finish_investment_Tv:
			if (!TextUtils.isEmpty(area_investment_Etv.getText())) {// 获取标题
				list.add(area_investment_Etv.getTextLabel() + "_"
						+ area_investment_Etv.getText());

			}
			if (!TextUtils.isEmpty(industry_investment_Etv.getText())) {//
				list.add(industry_investment_Etv.getTextLabel() + "_"
						+ industry_investment_Etv.getText());
			}

			if (!TextUtils.isEmpty(type_investment_Etv.getText())) {
				list.add(type_investment_Etv.getTextLabel() + "_"
						+ type_investment_Etv.getText());

			}
			if (!TextUtils.isEmpty(custom_investment_Etv.getText())) {
				list.add(custom_investment_Etv.getTextLabel() + "_"
						+ custom_investment_Etv.getText());

			}
			if (!list2.isEmpty()) {
				for (int i = 0; i < list2.size(); i++) {
					if (!TextUtils.isEmpty(list2.get(i).getText())) {
						list.add(list2.get(i).getTextLabel() + "_"
								+ list2.get(i).getText());
					}
				}
			}
			if (!"[]".equals(list.toString())) {
				intent = new Intent();
				intent.putStringArrayListExtra("Investment", list);
				setResult(2, intent);
			}
			finish();
			break;
		case R.id.area_investment_Etv:
			dataIndex = 3;
			ENavigate.startChooseActivityForResult(this, false, "区域",
					ChooseDataUtil.CHOOSE_type_Area, null);
			break;
		case R.id.industry_investment_Etv:
			dataIndex = 2;
			ENavigate.startChooseActivityForResult(this, true, "行业",
					ChooseDataUtil.CHOOSE_type_Trade, null);
			break;
		case R.id.type_investment_Etv:
			dataIndex = 1;
			 ENavigate.startChooseActivityForResult(this, true, "类型",
						 ChooseDataUtil.CHOOSE_type_OutInvestType,
						 null);
			break;
		case R.id.custom_investment_Etv:
			intent = new Intent(this, CustomActivity.class);
			intent.putExtra("fengxing", true);
			if (arrayList!=null) {
				if (!isNull) {
					Bundle bundle = new Bundle();
					bundle.putSerializable("Customer_Bean", arrayList);
					intent.putExtras(bundle);
				}
				
			}
			startActivityForResult(intent, 999);
			break;
		case R.id.quit_investment_Rl:
			finish();
			break;

		case R.id.delete_Ll:
			if (!(continueAdd2 == 0)) {
				investment_Ll.removeAllViews();
			} else {
				finish();
			}
			count--;
			break;
		case R.id.continueadd_Ll:
			list1.add(area_investment_Etv.getTextLabel());
			list1.add(industry_investment_Etv.getTextLabel());
			list1.add(type_investment_Etv.getTextLabel());
			list1.add(custom_investment_Etv.getTextLabel());
			ContinueAdd(list1, investment_main_Ll, list2, layouts);
			count++;
			break;
		default:
			break;
		}
	}
	public void setChooseText(ArrayList<Metadata> data,MyEditTextView type,MyEditTextView industry,MyEditTextView area_Me) {
		switch (dataIndex) {
		case 1:
			// 类型
			if (metadataType != null) {
				metadataType.clear();
			}
			metadataType = data;
			type
					.setText(ChooseDataUtil.getMetadataName(metadataType, 9));
			break;
		case 2:
			// 行业
			if (metadataIndustry != null) {
				metadataIndustry.clear();
			}
			metadataIndustry = data;
			industry.setText(ChooseDataUtil.getMetadataName(
					metadataIndustry,9));
			break;
		case 3:
			// 地区
			if (metadataArea != null) {
				metadataArea.clear();
			}
			metadataArea = data;
			Area area_result = ChooseDataUtil
					.getMetadataName(metadataArea);
			area_Me.setText((TextUtils.isEmpty(area_result.province) ? ""
					: area_result.province)
					+ (TextUtils.isEmpty(area_result.city) ? "" : area_result.city)
					+ (TextUtils.isEmpty(area_result.county) ? ""
							: area_result.county));
			break;
		}
	}
	public void ContinueAdd(ArrayList<String> list2,
			final LinearLayout partent, final ArrayList<MyEditTextView> list,
			final ArrayList<MyLineraLayout> layouts) {
		final MyLineraLayout lineraLayout = new MyLineraLayout(context);
		for (int i = 0; i < list2.size(); i++) {
			final MyEditTextView MyEditTextView = new MyEditTextView(context);
			String text = list2.get(i);
			MyEditTextView.setChoose(true);
			lineraLayout.setOrientation(LinearLayout.VERTICAL);
			MyEditTextView.setTextLabel(text);
			lineraLayout.addView(MyEditTextView, new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			if (i == list2.size() - 1) {
				MyEditTextView.setAddMore_hint(true);
			}
			if (i == 0) {
				MyEditTextView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
//						Intent intent = new Intent(context, Area_Activity.class);
//						intent.putExtra("Area_count",
//								MyEditTextView.getMyEdit_Id());
//						startActivityForResult(intent, 1);
						dataIndex = 3;
						ENavigate.startChooseActivityForResult(InvestmentActivity.this, false, "区域",
								ChooseDataUtil.CHOOSE_type_Area, null,MyEditTextView.getMyEdit_Id());
						mArea.put(MyEditTextView.MyEdit_Id, MyEditTextView);
						
					}
				});
			}
			if (i == 1) {
				MyEditTextView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
//						Intent intent = new Intent(context,
//								Industry_Activity.class);
//						intent.putExtra("Industry_count",
//								MyEditTextView.getMyEdit_Id());
//						startActivityForResult(intent, 1);
						dataIndex = 2;
						mInvestment.put(MyEditTextView.MyEdit_Id, MyEditTextView);
						ENavigate.startChooseActivityForResult(InvestmentActivity.this, true, "行业",
								ChooseDataUtil.CHOOSE_type_Trade, null,MyEditTextView.getMyEdit_Id());
					}
				});
			}
			if (i == 2) {
				MyEditTextView.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
//						Intent intent = new Intent(context, Type_Activity.class);
//						intent.putExtra("Type_count",
//								MyEditTextView.getMyEdit_Id());
						
//						startActivityForResult(intent, 1);
						dataIndex = 1;
						mType.put(MyEditTextView.MyEdit_Id, MyEditTextView);
						 ENavigate.startChooseActivityForResult(InvestmentActivity.this, true, "类型",
									 ChooseDataUtil.CHOOSE_type_InInvestType,
									 null,MyEditTextView.getMyEdit_Id());
					}
				});
			}
			if (i == 3) {
				MyEditTextView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(context,
								CustomActivity.class);
						intent.putExtra("Custom_count",
								MyEditTextView.getMyEdit_Id());
						intent.putExtra("fengxing", true);
						if (arrayList2!=null) {
							if (!isNull) {
								Bundle bundle = new Bundle();
								bundle.putSerializable("Customer_Bean", arrayList2);
								intent.putExtras(bundle);
							}
							
						}
						mDictionary.put(MyEditTextView.MyEdit_Id, MyEditTextView);
						startActivityForResult(intent, 1);
					}
				});
			}
			mLineraDictionary.put(MyEditTextView.getMyEdit_Id(), lineraLayout);
			
			list.add(MyEditTextView);
			layouts.add(lineraLayout);
		}
		MyDeleteView deleteView = new MyDeleteView(context);
		deleteView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				partent.removeView(lineraLayout);
				layouts.remove(lineraLayout);
			}
		});
		lineraLayout.addView(deleteView);
		partent.addView(lineraLayout, 2, new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		list2.removeAll(list2);
		list2 = null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			if (requestCode == 999) {
				switch (resultCode) {
			
				case 999:
					arrayList = custom2(data, custom_investment_Etv, investment_Ll, isNull,editTextViews);
					break;

				default:
					break;
				}
			}
			if(requestCode== ENavConsts.ActivityReqCode.REQUEST_CHOOSE_SELECT){
				// 多级选择回调界面
				setChooseText((ArrayList<Metadata>) data
						.getSerializableExtra(ENavConsts.DEMAND_CHOOSE_DATA),type_investment_Etv,industry_investment_Etv,area_investment_Etv);
			}
			if (requestCode == 1) {
				String custom_count = data.getStringExtra("custom_count");
				String industry_count = data.getStringExtra("industry_count");
				String type_count = data.getStringExtra("type_count");
				String area_count = data.getStringExtra("area_count");
				String people_id = data.getStringExtra(ENavConsts.PEOPLE_ID);
				// 多级选择回调界面
				setChooseText((ArrayList<Metadata>) data
						.getSerializableExtra(ENavConsts.DEMAND_CHOOSE_DATA),mType.get(people_id),mInvestment.get(people_id),mArea.get(people_id));
				switch (resultCode) {
				
				case 999:
					arrayList2 = custom2(data, mDictionary.get(custom_count),
							mLineraDictionary.get(custom_count), isNull,editTextViews);
					break;
				default:
					break;
				}
			}

		}

	}
}
