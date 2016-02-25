package com.tr.ui.organization.create_clientele;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tr.R;
import com.tr.ui.organization.model.Relation;
import com.tr.ui.organization.model.profile.CustomerPartner;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.organization.model.profile.CustomerProfile;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.CustomActivity;
import com.tr.ui.people.cread.IndustryActivity;
import com.tr.ui.people.cread.view.MyAddMordView;
import com.tr.ui.people.cread.view.MyDeleteView;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.tr.ui.people.cread.view.MyLineraLayout;
/**
 * 合伙人
 * @author Wxh07151732
 *
 */
public class PartnerActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout quit_partner_Rl;
	private LinearLayout partner_Ll;
	private MyEditTextView partner_name_Etv;
	private MyEditTextView partner_ctsuom_Etv;
	private MyEditTextView partner_scale_Etv;
	private MyEditTextView partner_office_Etv;
	private MyEditTextView partner_territory_Etv;
	private MyEditTextView partner_duty_Etv;
	private Intent intent;
	private ArrayList<String> list;
	private LinearLayout partner_main_Ll;
	private MyDeleteView partner_delete_Mdv;
	private MyAddMordView partner_MAMV;
	private ArrayList<String> list1;
	private ArrayList<MyEditTextView> mEditViewlist;
	private ArrayList<MyLineraLayout> mLineralist;
	private ArrayList<CustomerPartner> partnerList;
	private List<CustomerPersonalLine> partnercustom_List;
	private ArrayList<CustomerPersonalLine> myEditTextView;
	private List<CustomerPersonalLine> partnercustom_more_List;
	private ArrayList<CustomerPersonalLine> myEditTextView_more;
	private ArrayList<CustomerPartner> partner_list;
	private boolean isNull;
	private ArrayList<MyEditTextView> editTextViews;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_partner);
		list = new ArrayList<String>();
		list1 = new ArrayList<String>();
		editTextViews = new ArrayList<MyEditTextView>();
		mEditViewlist = new ArrayList<MyEditTextView>();
		mLineralist = new ArrayList<MyLineraLayout>();
		partnerList = new ArrayList<CustomerPartner>();
		partnercustom_List = new ArrayList<CustomerPersonalLine>();
		partnercustom_more_List = new ArrayList<CustomerPersonalLine>();
		init();
		initData();
	}

	private void initData() {
		quit_partner_Rl.setOnClickListener(this);
		partner_ctsuom_Etv.setOnClickListener(this);
		partner_MAMV.setOnClickListener(this);
		partner_delete_Mdv.setOnClickListener(this);
	}
	public void finish(View v){
		if (!TextUtils.isEmpty(partner_name_Etv.getText())) {
			list.add(partner_name_Etv.getTextLabel()+"_"+partner_name_Etv.getText());
		}
		if (!TextUtils.isEmpty(partner_duty_Etv.getText())) {
			list.add(partner_duty_Etv.getTextLabel()+"_"+partner_duty_Etv.getText());
		}
		if (!TextUtils.isEmpty(partner_territory_Etv.getText())) {
			list.add(partner_territory_Etv.getTextLabel()+"_"+partner_territory_Etv.getText());
		}
		if (!TextUtils.isEmpty(partner_office_Etv.getText())) {
			list.add(partner_office_Etv.getTextLabel()+"_"+partner_office_Etv.getText());
		}
		
		if (!TextUtils.isEmpty(partner_scale_Etv.getText())) {
			list.add(partner_scale_Etv.getTextLabel()+"_"+partner_scale_Etv.getText());
		}
		if (!TextUtils.isEmpty(partner_ctsuom_Etv.getText())) {
			list.add(partner_ctsuom_Etv.getTextLabel()+"_"+partner_ctsuom_Etv.getText());
		}
		Parcel parcel =  Parcel.obtain();
		CustomerPartner partner = new CustomerPartner(parcel);
		Relation relation = new Relation();
		relation.relation=partner_name_Etv.getText();
		partner.name = relation;
		partner.companyJob = partner_duty_Etv.getText();
		partner.expertise = partner_territory_Etv.getText();
		partner.address = partner_office_Etv.getText();
		partner.percent = partner_scale_Etv.getText();
		CustomerPersonalLine customerPersonalLine = new CustomerPersonalLine();
		CustomerPersonalLine customerPersonalLine1 = new CustomerPersonalLine();
		customerPersonalLine.name=partner_ctsuom_Etv.getTextLabel();
		customerPersonalLine.content = partner_ctsuom_Etv.getText();
		customerPersonalLine.type = "1";
//		if (myEditTextView!=null) {
//			customerPersonalLine1.name=myEditTextView.getTextLabel();
//			customerPersonalLine1.content = myEditTextView.getText();
//			customerPersonalLine1.type = "2";
//		}
		partnercustom_List.add(customerPersonalLine);
		partnercustom_List.add(customerPersonalLine1);
		partner.propertyList = partnercustom_List;
		if (!mLineralist.isEmpty()) {
			for (int i = 0; i < mLineralist.size(); i++) {
				CustomerPartner partner_more = new CustomerPartner(parcel);
				MyEditTextView name_Etv = (MyEditTextView) mLineralist.get(i).getChildAt(1);
				MyEditTextView duty_Etv = (MyEditTextView) mLineralist.get(i).getChildAt(2);
				MyEditTextView territory_Etv = (MyEditTextView) mLineralist.get(i).getChildAt(3);
				MyEditTextView office_Etv = (MyEditTextView) mLineralist.get(i).getChildAt(4);
				MyEditTextView scale_Etv = (MyEditTextView) mLineralist.get(i).getChildAt(5);
				MyEditTextView ctsuom_Etv = (MyEditTextView) mLineralist.get(i).getChildAt(6);
				Relation relation_more = new Relation();
				relation_more.relation = name_Etv.getText();
				partner_more.name = relation_more;
				partner_more .companyJob = duty_Etv.getText();
				partner_more.expertise = territory_Etv.getText();
				partner_more.address = office_Etv.getText();
				partner_more.percent = scale_Etv.getText();
				CustomerPersonalLine line_more = new CustomerPersonalLine();
				line_more.content =ctsuom_Etv .getText();
				line_more.name = ctsuom_Etv.getTextLabel();
				line_more.type = "1";
				CustomerPersonalLine line_text_more = new CustomerPersonalLine();
//				if (myEditTextView_more!=null) {
//					line_text_more.content =myEditTextView_more .getText();
//					line_text_more.name = myEditTextView_more.getTextLabel();
//					line_text_more.type = "2";
//				}
				
				partnercustom_more_List.add(line_more);
				partnercustom_more_List.add(line_text_more);
				partner_more.propertyList = partnercustom_more_List;
				partnerList.add(partner_more);
			}
		}
		partnerList.add(partner);
		Bundle bundle = new Bundle();
		bundle.putParcelableArrayList("Partner_Bean", partnerList);
		
		if (!list.isEmpty()) {
			intent = new Intent();
			intent.putStringArrayListExtra("Partner_Activity", list);
			intent.putExtras(bundle);
			setResult(7005, intent);
		}
		finish();
	}
	private void init() {
		partner_MAMV = (MyAddMordView) findViewById(R.id.partner_MAMV);
		partner_delete_Mdv = (MyDeleteView) findViewById(R.id.partner_delete_Mdv);
		quit_partner_Rl = (RelativeLayout) findViewById(R.id.quit_partner_Rl);
		partner_Ll = (LinearLayout) findViewById(R.id.partner_Ll);
		partner_main_Ll = (LinearLayout) findViewById(R.id.partner_main_Ll);
		partner_name_Etv = (MyEditTextView) findViewById(R.id.partner_name_Etv);
		partner_duty_Etv = (MyEditTextView) findViewById(R.id.partner_duty_Etv);
		partner_territory_Etv = (MyEditTextView) findViewById(R.id.partner_territory_Etv);
		partner_office_Etv = (MyEditTextView) findViewById(R.id.partner_office_Etv);
		partner_scale_Etv = (MyEditTextView) findViewById(R.id.partner_scale_Etv);
		partner_ctsuom_Etv = (MyEditTextView) findViewById(R.id.partner_ctsuom_Etv);
		
		partner_list = this.getIntent().getParcelableArrayListExtra("Partner_Bean");
		if (partner_list!=null) {
			for (int i = 0; i < partner_list.size(); i++) {
				CustomerPartner customerPartner = partner_list.get(i);
				if (partner_list.size()==1) {
					partner_name_Etv.setText(customerPartner.name.relation);
					 partner_duty_Etv.setText(customerPartner.companyJob);
					partner_territory_Etv.setText(customerPartner.expertise );
					  partner_office_Etv.setText(customerPartner.address);
					  partner_scale_Etv.setText(customerPartner.percent);
					if (!customerPartner.propertyList.isEmpty()) {
						for (int j = 0; j < customerPartner.propertyList.size(); j++) {
							CustomerPersonalLine personalLine = customerPartner.propertyList.get(j);
							if ("1".equals(customerPartner.propertyList.get(j).type)) {
								partner_ctsuom_Etv.setTextLabel(personalLine.name);
								 partner_ctsuom_Etv.setText(personalLine.content);
							}else{
								MyEditTextView editTextView = new MyEditTextView(context);
								editTextView.setJustLabel(true);
								editTextView.setText(personalLine.content);
								editTextView.setTextLabel(personalLine.name);
								partner_Ll.addView(editTextView,partner_Ll.indexOfChild(editTextView)+1);
							}
						}
					}
					
				}else{
					partner_name_Etv.setText(partner_list.get(0).name.relation);
					 partner_duty_Etv.setText(partner_list.get(0).companyJob);
					partner_territory_Etv.setText(partner_list.get(0).expertise );
					  partner_office_Etv.setText(partner_list.get(0).address);
					  partner_scale_Etv.setText(partner_list.get(0).percent);
					if (!customerPartner.propertyList.isEmpty()) {
						for (int j = 0; j < customerPartner.propertyList.size(); j++) {
							CustomerPersonalLine personalLine = partner_list.get(0).propertyList.get(j);
							if ("1".equals(customerPartner.propertyList.get(j).type)) {
								partner_ctsuom_Etv.setTextLabel(personalLine.name);
								 partner_ctsuom_Etv.setText(personalLine.content);
							}
//							else{
//								MyEditTextView editTextView = new MyEditTextView(context);
//								editTextView.setJustLabel(true);
//								editTextView.setText(personalLine.content);
//								editTextView.setTextLabel(personalLine.name);
//								partner_Ll.addView(editTextView,partner_Ll.indexOfChild(editTextView)+1);
//							}
						}
					}
					list1.add(partner_name_Etv.getTextLabel());
					list1.add(partner_duty_Etv.getTextLabel());
					list1.add(partner_territory_Etv.getTextLabel());
					list1.add(partner_office_Etv.getTextLabel());
					list1.add(partner_scale_Etv.getTextLabel());
					list1.add(partner_ctsuom_Etv.getTextLabel());
					ArrayList<MyEditTextView> more = more();
					if (more!=null) {
						more.get(i*list1.size()-list1.size()).setText(customerPartner.name.relation);
						more.get(i*list1.size()-list1.size()+1).setText(customerPartner.companyJob);
						more.get(i*list1.size()-list1.size()+2).setText(customerPartner.expertise);
						more.get(i*list1.size()-list1.size()+3).setText(customerPartner.address);
						more.get(i*list1.size()-list1.size()+4).setText(customerPartner.percent);
						more.get(i*list1.size()-list1.size()+5).setText(customerPartner.propertyList.get(i).content);
						if (customerPartner.propertyList.get(i).name!=null) {
							more.get(i*list1.size()-list1.size()+5).setTextLabel(customerPartner.propertyList.get(i).name);
						}else{
							more.get(i*list1.size()-list1.size()+5).setTextLabel("自定义");
						}
					}
				}
			}
			
		}
	}
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		if (arg2!=null) {
			switch (arg1) {
			case 999:
				myEditTextView = custom2(arg2, partner_ctsuom_Etv, partner_Ll, isNull,editTextViews);
				break;

			default:
				break;
			}
			if (arg0==8001) {
				String Partner_ID = arg2.getStringExtra("Partner_ID");
				switch (arg1) {
				case 999:
					myEditTextView_more = custom2(arg2, mDictionary.get(Partner_ID), mLineraDictionary.get(Partner_ID), isNull,editTextViews);
					break;

				default:
					break;
				}
				
			}
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.quit_partner_Rl:
			finish();
			break;
		case R.id.partner_ctsuom_Etv:
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
		case R.id.partner_MAMV:
			list1.add(partner_name_Etv.getTextLabel());
			list1.add(partner_duty_Etv.getTextLabel());
			list1.add(partner_territory_Etv.getTextLabel());
			list1.add(partner_office_Etv.getTextLabel());
			list1.add(partner_scale_Etv.getTextLabel());
			list1.add(partner_ctsuom_Etv.getTextLabel());
			more();
			break;
		case R.id.partner_delete_Mdv:
			if (mLineralist.isEmpty()) {
				finish();
			}else{
				partner_Ll.removeAllViews();
			}
			break;
		default:
			break;
		}
	}

	private ArrayList<MyEditTextView> more() {
		final MyLineraLayout lineraLayout = new MyLineraLayout(context);
		lineraLayout.setOrientation(LinearLayout.VERTICAL);
		MyDeleteView deleteView = new MyDeleteView(context);
		for (int i = 0; i < list1.size(); i++) {
			final MyEditTextView editTextView = new MyEditTextView(context);
			if (i==2) {
				editTextView.setChoose(true);
				editTextView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(PartnerActivity.this, IndustryActivity.class);
						startActivityForResult(intent, 8001);
					}
				});
			}
			if (i==list1.size()-1) {
				editTextView.setChoose(true);
				editTextView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(PartnerActivity.this, CustomActivity.class);
						intent.putExtra("fengxing", true);
						intent.putExtra("Partner_ID", editTextView.MyEdit_Id);
						if (myEditTextView_more!=null) {
							if (!isNull) {
								Bundle bundle = new Bundle();
								bundle.putSerializable("Customer_Bean", myEditTextView_more);
								intent.putExtras(bundle);
							}
							
						}
						startActivityForResult(intent, 8001);
					}
				});
			}
			
			editTextView.setTextLabel(list1.get(i));
			lineraLayout.addView(editTextView);
			mEditViewlist.add(editTextView);
		}
		mLineralist.add(lineraLayout);
		lineraLayout.addView(deleteView);
		deleteView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				lineraLayout.removeAllViews();
				mLineralist.remove(lineraLayout);
			}
		});
		partner_main_Ll.addView(lineraLayout,1);
		list1.removeAll(list1);
		return mEditViewlist;
	}
}
