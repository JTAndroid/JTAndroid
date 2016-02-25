package com.tr.ui.people.cread;

import java.util.ArrayList;

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

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * 融资意向(子模块)
 * 
 * @author Wxh07151732
 * 
 */
public class FinancingActivity extends BaseActivity implements OnClickListener {
	private TextView financing_Tv;
	private TextView finish_financing_Tv;
	private MyEditTextView area_financing_Etv;
	private MyEditTextView industry_financing_Etv;
	private MyEditTextView type_financing_Etv;
	private MyEditTextView custom_financing_Etv;
	private ArrayList<String> list;
	private LinearLayout continueadd_Ll;
	private LinearLayout delete_Ll;
	private LinearLayout financing_Ll;
	private RelativeLayout quit_financing_Rl;
	private ArrayList<String> list1;
	private ArrayList<MyEditTextView> list2;
	private ArrayList<MyEditTextView> continueAdd;
	private MyEditTextView editTextView;
	private ArrayList<MyLineraLayout> layouts;
	private int count;
	private LinearLayout financing_main_Ll;
	private int continueAdd2;
	private CustomerDemandCommon customer;
	private boolean isNull;
	private int dataIndex;
	private ArrayList<CustomerPersonalLine> arrayList;
	private ArrayList<Metadata> metadataType;
	private ArrayList<Metadata> metadataIndustry;
	private ArrayList<Metadata> metadataArea;
	private Area area;
	private ArrayList<CustomerPersonalLine> arrayList2;
	private ArrayList<MyEditTextView> editTextViews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_financing);
		area =new Area();
		init();
		editTextViews = new ArrayList<MyEditTextView>();
		list = new ArrayList<String>();
		list1 = new ArrayList<String>();
		list2 = new ArrayList<MyEditTextView>();
		layouts = new ArrayList<MyLineraLayout>();
	}

	private void init() {
		quit_financing_Rl = (RelativeLayout) findViewById(R.id.quit_financing_Rl);
		financing_Tv = (TextView) findViewById(R.id.financing_Tv);
		finish_financing_Tv = (TextView) findViewById(R.id.finish_financing_Tv);

		area_financing_Etv = (MyEditTextView) findViewById(R.id.area_financing_Etv);
		industry_financing_Etv = (MyEditTextView) findViewById(R.id.industry_financing_Etv);
		type_financing_Etv = (MyEditTextView) findViewById(R.id.type_financing_Etv);
		custom_financing_Etv = (MyEditTextView) findViewById(R.id.custom_financing_Etv);
		
		
		customer = (CustomerDemandCommon) getIntent().getSerializableExtra("Customer");
		if (customer != null) {

			area_financing_Etv.setText(customer.getAddress().getAddress());

			industry_financing_Etv.setText(customer.getIndustryNames());

			type_financing_Etv.setText(customer.getTypeNames());

			custom_financing_Etv.setTextLabel(customer.getPersonalLineList()
					.get(0).getName());

			custom_financing_Etv.setText(customer.getPersonalLineList().get(0)
					.getContent());
		}

		continueadd_Ll = (LinearLayout) findViewById(R.id.continueadd_Ll);
		financing_Ll = (LinearLayout) findViewById(R.id.financing_Ll);
		financing_main_Ll = (LinearLayout) findViewById(R.id.financing_main_Ll);
		delete_Ll = (LinearLayout) findViewById(R.id.delete_Ll);
		TextView continueadd_Tv = (TextView) findViewById(R.id.continueadd_Tv);
		continueadd_Tv.setText("新增融资意向");
		quit_financing_Rl.setOnClickListener(this);
		delete_Ll.setOnClickListener(this);
		continueadd_Ll.setOnClickListener(this);
		finish_financing_Tv.setOnClickListener(this);
		area_financing_Etv.setOnClickListener(this);
		industry_financing_Etv.setOnClickListener(this);
		type_financing_Etv.setOnClickListener(this);
		custom_financing_Etv.setOnClickListener(this);
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
	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		// 完成
		case R.id.finish_financing_Tv:
			if (!TextUtils.isEmpty(area_financing_Etv.getText())) {
				list.add(area_financing_Etv.getTextLabel() + "_"
						+ area_financing_Etv.getText());

			}
			if (!TextUtils.isEmpty(industry_financing_Etv.getText())) {
				list.add(industry_financing_Etv.getTextLabel() + "_"
						+ industry_financing_Etv.getText());

			}
			if (!TextUtils.isEmpty(type_financing_Etv.getText())) {
				list.add(type_financing_Etv.getTextLabel() + "_"
						+ type_financing_Etv.getText());

			}
			if (!TextUtils.isEmpty(custom_financing_Etv.getText())) {
				list.add(custom_financing_Etv.getTextLabel() + "_"
						+ custom_financing_Etv.getText());

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
				intent.putStringArrayListExtra("Financing", list);
				setResult(3, intent);
			}
			finish();
			break;
		case R.id.area_financing_Etv:
			dataIndex = 3;
			ENavigate.startChooseActivityForResult(this, false, "区域",
					ChooseDataUtil.CHOOSE_type_Area, null);
			break;
		case R.id.industry_financing_Etv:
			dataIndex = 2;
			ENavigate.startChooseActivityForResult(this, true, "行业",
					ChooseDataUtil.CHOOSE_type_Trade, null);
			break;
		case R.id.type_financing_Etv:
			dataIndex = 1;
			 ENavigate.startChooseActivityForResult(this, true, "类型",
						 ChooseDataUtil.CHOOSE_type_InInvestType,
						 null);
			break;
		case R.id.custom_financing_Etv:
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
		case R.id.quit_financing_Rl:
			finish();
			break;
		case R.id.delete_Ll:
			if (!(continueAdd2 == 0)) {
				financing_Ll.removeAllViews();
			} else {
				finish();
			}
			count--;
			break;
		case R.id.continueadd_Ll:
			list1.add(area_financing_Etv.getTextLabel());

			list1.add(industry_financing_Etv.getTextLabel());

			list1.add(type_financing_Etv.getTextLabel());

			list1.add(custom_financing_Etv.getTextLabel());

			ContinueAdd(list1, financing_main_Ll, list2, layouts);
			count++;
			break;
		default:
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
						mArea.put(MyEditTextView.MyEdit_Id, MyEditTextView);
						ENavigate.startChooseActivityForResult(FinancingActivity.this, false, "区域",
								ChooseDataUtil.CHOOSE_type_Area, null,MyEditTextView.getMyEdit_Id());
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
						ENavigate.startChooseActivityForResult(FinancingActivity.this, true, "行业",
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
						mType.put(MyEditTextView.MyEdit_Id, MyEditTextView);
						dataIndex = 1;
						 ENavigate.startChooseActivityForResult(FinancingActivity.this, true, "类型",
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
						mDictionary.put(MyEditTextView.getMyEdit_Id(), MyEditTextView);
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
				case 1:
					String area = data.getStringExtra("area");
					area_financing_Etv.setText(area);
					break;
				case 2:
					String industry = data.getStringExtra("industry");
					industry_financing_Etv.setText(industry);
					break;
				case 3:
					String type = data.getStringExtra("type");
					type_financing_Etv.setText(type);
					break;
				case 999:
					arrayList = custom2(data, custom_financing_Etv, financing_Ll, isNull,editTextViews);

					break;

				default:
					break;
				}
			}
			if(requestCode== ENavConsts.ActivityReqCode.REQUEST_CHOOSE_SELECT){
				// 多级选择回调界面
				setChooseText((ArrayList<Metadata>) data
						.getSerializableExtra(ENavConsts.DEMAND_CHOOSE_DATA),type_financing_Etv,industry_financing_Etv,area_financing_Etv);
			}
			if (requestCode == ENavConsts.ActivityReqCode.REQUEST) {
				String custom_count = data.getStringExtra("custom_count");
				String industry_count = data.getStringExtra("industry_count");
				String type_count = data.getStringExtra("type_count");
				String area_count = data.getStringExtra("area_count");
				String people_id = data.getStringExtra(ENavConsts.PEOPLE_ID);
				if(mType != null && mInvestment != null 
						&& mArea != null ){
					// 多级选择回调界面
					setChooseText((ArrayList<Metadata>) data
							.getSerializableExtra(ENavConsts.DEMAND_CHOOSE_DATA),mType
							.get(people_id),mInvestment
							.get(people_id),mArea
							.get(people_id));
				}
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