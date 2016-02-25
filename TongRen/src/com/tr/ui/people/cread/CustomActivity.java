package com.tr.ui.people.cread;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tr.R;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.organization.model.profile.CustomerRemark;
import com.tr.ui.people.cread.view.MyEditTextView;

/**
 * 自定义模块（目前需求在使用，但是跟产品沟通过，马上会废弃）
 * 
 * @author Wxh07151732
 * 
 */
public class CustomActivity extends BaseActivity implements OnClickListener {
	private static MyEditTextView custom_Text_Ttv;
	private MyEditTextView custom_Ttv;
	private LinearLayout custom_Ll;
	private ArrayList<String> list;
	private static boolean isChange;
	private RelativeLayout quit_custom_Rl;
	private ArrayList<MyEditTextView> list1;
	private static ArrayList<String> initialize_Custom;
	
	private String custom;
	private TextView custom_Tv;
	private String custom_count;
	private String Work_Custom_ID;
	private String Education_Custom_ID;
	private String Meeting_Custom_ID;
	private String Society_Custom_ID;
	private List<CustomerPersonalLine> propertyList;
	private List<CustomerRemark> sponsorCustomerList;
	private ArrayList<CustomerPersonalLine> sponsorList;
	private String Partner_ID;
	private Bundle bundle;
	private String Edit_competition_ID;
	private LinearLayout customtv_Ll;
	private ArrayList<MyEditTextView> text_list;
	private Intent intent;
	private boolean fengxing;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_custom);
		custom = this.getIntent().getStringExtra("Custom");
		custom_count = this.getIntent().getStringExtra("Custom_count");
		Edit_competition_ID = this.getIntent().getStringExtra(
				"Edit_competition_ID");
		Work_Custom_ID = this.getIntent().getStringExtra("Work_Custom_ID");
		Partner_ID = this.getIntent().getStringExtra("Partner_ID");
		Education_Custom_ID = this.getIntent().getStringExtra(
				"Education_Custom_ID");
		Meeting_Custom_ID = this.getIntent()
				.getStringExtra("Meeting_Custom_ID");
		Society_Custom_ID = this.getIntent()
				.getStringExtra("Society_Custom_ID");
		fengxing = this.getIntent().getBooleanExtra("fengxing", false);
		list1 = new ArrayList<MyEditTextView>();
		text_list = new ArrayList<MyEditTextView>();
		propertyList = new ArrayList<CustomerPersonalLine>();
		sponsorCustomerList = new ArrayList<CustomerRemark>();
		sponsorList = new ArrayList<CustomerPersonalLine>();
		intent = new Intent();
		init();
		initData();
		list = new ArrayList<String>();

	}

	private void initData() {
		if (custom != null) {
			custom_Tv.setText(custom);
		}
		custom_Text_Ttv.MakeAddMoreMethod(custom_Text_Ttv,
				CustomActivity.this,0, custom_Ll, "自定义文本", text_list);
		custom_Ttv.MakeAddMoreMethod(custom_Ttv, CustomActivity.this,0,
				customtv_Ll, "", list1);
	}

	private void init() {
		customtv_Ll = (LinearLayout) findViewById(R.id.customtv_Ll);
		custom_Tv = (TextView) findViewById(R.id.custom_Tv);
		custom_Text_Ttv = (MyEditTextView) findViewById(R.id.custom_Text_Ttv);
		custom_Text_Ttv.setReadOnly(true);
		quit_custom_Rl = (RelativeLayout) findViewById(R.id.quit_custom_Rl);
		custom_Ttv = (MyEditTextView) findViewById(R.id.custom_Ttv);
		custom_Ll = (LinearLayout) findViewById(R.id.custom_Ll);
		custom_Ttv.setOnClickListener(this);
		custom_Text_Ttv.setOnClickListener(this);
		quit_custom_Rl.setOnClickListener(this);
		if ("企业刊物".equals(custom)) {
			CustomerRemark customerRemark = (CustomerRemark) this.getIntent()
					.getSerializableExtra("publication_Bean");
			if (customerRemark != null) {
				if (!customerRemark.propertyList.isEmpty()) {
					for (int i = 0; i < customerRemark.propertyList.size(); i++) {
						CustomerPersonalLine customerPersonalLine = customerRemark.propertyList
								.get(i);
						if ("1".equals(customerPersonalLine.type)) {
							custom_Ttv.setTextLabel(customerPersonalLine.name);
							custom_Ttv.setText(customerPersonalLine.content);
						} else {
							custom_Text_Ttv
									.setTextLabel(customerPersonalLine.name);
							custom_Text_Ttv
									.setText(customerPersonalLine.content);
						}
						if (customerRemark.propertyList.size()>=3) {
							if ("1".equals(customerPersonalLine.type)) {
								final MyEditTextView editTextView = new MyEditTextView(
										context);
								editTextView.setCustom(true);
								editTextView.setTextLabel(customerPersonalLine.name);
								editTextView.setText(customerPersonalLine.content);
								editTextView.setDelete(true);
								custom_Ll.addView(editTextView);
								editTextView.getMyedittext_Addmore_Rl().setOnClickListener(
										new OnClickListener() {
											@Override
											public void onClick(View v) {
												custom_Ll.removeView(editTextView);
												list1.remove(editTextView);
											}
										});
								
								list1.add(editTextView);
							}else{
								final MyEditTextView editTextView = new MyEditTextView(
										context);
								editTextView.setCustom_Text(true);
								editTextView.setTextLabel(customerPersonalLine.name);
								editTextView.setText(customerPersonalLine.content);
								editTextView.setDelete(true);
								custom_Ll.addView(editTextView);
								editTextView.getMyedittext_Addmore_Rl().setOnClickListener(
										new OnClickListener() {
											@Override
											public void onClick(View v) {
												custom_Ll.removeView(editTextView);
												list1.remove(editTextView);
											}
										});
								
								list1.add(editTextView);
							}
						}
					}
				}
			}

		} else if ("主要客户".equals(custom)) {

			CustomerRemark customerRemark = (CustomerRemark) this.getIntent()
					.getSerializableExtra("sponsorCustomer_Bean");
			if (customerRemark != null) {
				if (!customerRemark.propertyList.isEmpty()) {

					for (int i = 0; i < customerRemark.propertyList.size(); i++) {
						CustomerPersonalLine customerPersonalLine = customerRemark.propertyList
								.get(i);
						if ("1".equals(customerPersonalLine.type)) {
							custom_Ttv.setTextLabel(customerPersonalLine.name);
							custom_Ttv.setText(customerPersonalLine.content);
						} else {
							custom_Text_Ttv
									.setTextLabel(customerPersonalLine.name);
							custom_Text_Ttv
									.setText(customerPersonalLine.content);
						}
						
						if (customerRemark.propertyList.size()>=3) {
							if ("1".equals(customerPersonalLine.type)) {
								final MyEditTextView editTextView = new MyEditTextView(
										context);
								editTextView.setCustom(true);
								editTextView.setTextLabel(customerPersonalLine.name);
								editTextView.setText(customerPersonalLine.content);
								editTextView.setDelete(true);
								custom_Ll.addView(editTextView);
								editTextView.getMyedittext_Addmore_Rl().setOnClickListener(
										new OnClickListener() {
											@Override
											public void onClick(View v) {
												custom_Ll.removeView(editTextView);
												list1.remove(editTextView);
											}
										});
								
								list1.add(editTextView);
							}else{
								final MyEditTextView editTextView = new MyEditTextView(
										context);
								editTextView.setCustom_Text(true);
								editTextView.setTextLabel(customerPersonalLine.name);
								editTextView.setText(customerPersonalLine.content);
								editTextView.setDelete(true);
								custom_Ll.addView(editTextView);
								editTextView.getMyedittext_Addmore_Rl().setOnClickListener(
										new OnClickListener() {
											@Override
											public void onClick(View v) {
												custom_Ll.removeView(editTextView);
												list1.remove(editTextView);
											}
										});
								
								list1.add(editTextView);
							}
						}
					}
				}
			}
		}
		// 初始化数据
		if (fengxing) {
			custom_Text_Ttv.setVisibility(View.GONE);
			final ArrayList<CustomerPersonalLine> custom = (ArrayList<CustomerPersonalLine>) this.getIntent()
					.getSerializableExtra("Customer_Bean");
			if (custom != null) {
				for (int i = 0; i < custom.size(); i++) {
					final CustomerPersonalLine customerPersonalLine = custom.get(i);
				
					if ("1".equals(customerPersonalLine.type)) {
						final MyEditTextView editTextView = new MyEditTextView(
								context);
						editTextView.setCustom(true);
						editTextView.setTextLabel(customerPersonalLine.name);
						editTextView.setText(customerPersonalLine.content);
						
						editTextView.setDelete(true);
						custom_Ll.addView(editTextView);
						list1.add(editTextView);
						editTextView.getMyedittext_Addmore_Rl().setOnClickListener(
								new OnClickListener() {
									@Override
									public void onClick(View v) {
										custom_Ll.removeView(editTextView);
										list1.remove(editTextView);
										custom.remove(customerPersonalLine);
									}
								});
						
					}
				}
			}
		}
	}

	public void finish(View v) {
		if (TextUtils.isEmpty(custom_Ttv.getTextLabel()) && list1.isEmpty()&&TextUtils.isEmpty(custom_Ttv.getText())) {
			Intent intent = new Intent();
			intent.putExtra("isNull", true);
			setResult(999, intent);
			finish();
			return;
		}
		
		if ("企业刊物".equals(custom)) {
			CustomerRemark publication = new CustomerRemark();
			if (!TextUtils.isEmpty(custom_Ttv.getTextLabel())) {
				CustomerPersonalLine customerPersonalLine = new CustomerPersonalLine();
				customerPersonalLine.name = custom_Ttv.getTextLabel();
				customerPersonalLine.content = custom_Ttv.getText();
				customerPersonalLine.type = "1";
				propertyList.add(customerPersonalLine);
			}
			if (!TextUtils.isEmpty(custom_Text_Ttv.getTextLabel())){
				CustomerPersonalLine customerPersonalLine1 = new CustomerPersonalLine();
				customerPersonalLine1.name = custom_Text_Ttv.getTextLabel();
				customerPersonalLine1.content = custom_Text_Ttv.getText();
				customerPersonalLine1.type = "2";
				propertyList.add(customerPersonalLine1);
			}
			
			
			if (!list1.isEmpty()) {
				for (int i = 0; i < list1.size(); i++) {
					MyEditTextView myEditTextView = list1.get(i);
					if (!TextUtils.isEmpty(myEditTextView.getText())) {
						CustomerPersonalLine custom_more = new CustomerPersonalLine();
						custom_more.name = myEditTextView.getTextLabel();
						custom_more.content = myEditTextView.getText();
						custom_more.type = "1";
						propertyList.add(custom_more);
					}
	
				}
			}
			if (!text_list.isEmpty()) {
				for (int i = 0; i < text_list.size(); i++) {
					MyEditTextView myEditTextView = text_list.get(i);
					if (!TextUtils.isEmpty(myEditTextView.getText())) {
						CustomerPersonalLine custom1_more = new CustomerPersonalLine();
						custom1_more.name = myEditTextView.getTextLabel();
						custom1_more.content = myEditTextView.getText();
						custom1_more.type = "2";
						propertyList.add(custom1_more);
					}
				}
			}
			bundle = new Bundle();
			publication.propertyList = propertyList;
			if (!propertyList.isEmpty()) {
				bundle = new Bundle();
				bundle.putSerializable("publication_Bean", publication);
			}
		} else if ("主要客户".equals(custom)) {
			CustomerRemark sponsorCustomer = new CustomerRemark();
			if (!TextUtils.isEmpty(custom_Ttv.getTextLabel())) {
				CustomerPersonalLine sponsor = new CustomerPersonalLine();
				sponsor.name = custom_Ttv.getTextLabel();
				sponsor.content = custom_Ttv.getText();
				sponsor.type = "1";
				sponsorList.add(sponsor);
			}
			if (!TextUtils.isEmpty(custom_Ttv.getTextLabel())) {
				CustomerPersonalLine sponsor1 = new CustomerPersonalLine();
				sponsor1.name = custom_Text_Ttv.getTextLabel();
				sponsor1.content = custom_Text_Ttv.getText();
				sponsor1.type = "2";
				sponsorList.add(sponsor1);
			}
			if (!list1.isEmpty()) {
			for (int i = 0; i < list1.size(); i++) {
				MyEditTextView myEditTextView = list1.get(i);
				if (!TextUtils.isEmpty(myEditTextView.getText())) {
					CustomerPersonalLine custom_more = new CustomerPersonalLine();
					custom_more.name = myEditTextView.getTextLabel();
					custom_more.content = myEditTextView.getText();
					custom_more.type = "1";
					sponsorList.add(custom_more);
				}

			}
			}
			if (!text_list.isEmpty()) {
				for (int i = 0; i < text_list.size(); i++) {
					MyEditTextView myEditTextView = text_list.get(i);
					if (!TextUtils.isEmpty(myEditTextView.getText())) {
						CustomerPersonalLine custom1_more = new CustomerPersonalLine();
						custom1_more.name = myEditTextView.getTextLabel();
						custom1_more.content = myEditTextView.getText();
						custom1_more.type = "2";
						sponsorList.add(custom1_more);
					}
				}
				sponsorCustomer.propertyList = sponsorList;
				sponsorCustomerList.add(sponsorCustomer);
				if (!sponsorList.isEmpty()) {
					bundle = new Bundle();
					bundle.putSerializable("sponsorCustomer_Bean", sponsorCustomer);
				}
				
			}
		} else if (fengxing) {
			CustomerPersonalLine custom = new CustomerPersonalLine();
			if (!TextUtils.isEmpty(custom_Ttv.getTextLabel())) {
				custom.name = custom_Ttv.getTextLabel();
				custom.content = custom_Ttv.getText();
				custom.type = "1";
				sponsorList.add(custom);
			}
			
			if (!list1.isEmpty()) {
				for (int i = 0; i < list1.size(); i++) {
					MyEditTextView myEditTextView = list1.get(i);
					myEditTextView.setCustom(true);
					if (!TextUtils.isEmpty(myEditTextView.getTextLabel())) {
						CustomerPersonalLine custom_more = new CustomerPersonalLine();
						custom_more.name = myEditTextView.getTextLabel();
						custom_more.content = myEditTextView.getText();
						custom_more.type = "1";
						sponsorList.add(custom_more);
					}else if(TextUtils.isEmpty(myEditTextView.getTextLabel())&&TextUtils.isEmpty(myEditTextView.getText())){
						continue;
					}

				}

			}
			if (!sponsorList.isEmpty()) {
				bundle = new Bundle();
				bundle.putSerializable("Customer_Bean", sponsorList);
			}
			
		} else {
			if (!TextUtils.isEmpty( custom_Ttv.getTextLabel())) {
				CustomerPersonalLine custom = new CustomerPersonalLine();
				custom.name = custom_Ttv.getTextLabel();
				custom.content = custom_Ttv.getText();
				custom.type = "1";
				sponsorList.add(custom);
			}
			if (!TextUtils.isEmpty( custom_Text_Ttv.getTextLabel())) {
				CustomerPersonalLine custom1 = new CustomerPersonalLine();
				custom1.name = custom_Text_Ttv.getTextLabel();
				custom1.content = custom_Text_Ttv.getText();
				custom1.type = "2";
				sponsorList.add(custom1);
			}
			

			for (int i = 0; i < list1.size(); i++) {
				MyEditTextView myEditTextView = list1.get(i);
				if (!TextUtils.isEmpty(myEditTextView.getText())) {
					CustomerPersonalLine custom_more = new CustomerPersonalLine();
					custom_more.name = myEditTextView.getTextLabel();
					custom_more.content = myEditTextView.getText();
					custom_more.type = "1";
					sponsorList.add(custom_more);
				}

			}
			if (!text_list.isEmpty()) {
				for (int i = 0; i < text_list.size(); i++) {
					MyEditTextView myEditTextView = text_list.get(i);
					if (!TextUtils.isEmpty(myEditTextView.getText())) {
						CustomerPersonalLine custom1_more = new CustomerPersonalLine();
						custom1_more.name = myEditTextView.getTextLabel();
						custom1_more.content = myEditTextView.getText();
						custom1_more.type = "2";
						sponsorList.add(custom1_more);
					}
				}
			}
			if (!sponsorList.isEmpty()) {
				bundle = new Bundle();
				bundle.putSerializable("Customer_Bean", sponsorList);
			}
			
		}

		if (custom_count != null) {
			intent.putExtra("custom_count", custom_count);
		} else if (Work_Custom_ID != null) {
			intent.putExtra("Work_Custom_ID", Work_Custom_ID);
		} else if (Education_Custom_ID != null) {
			intent.putExtra("Education_Custom_ID", Education_Custom_ID);
		} else if (Meeting_Custom_ID != null) {
			intent.putExtra("Meeting_Custom_ID", Meeting_Custom_ID);
		} else if (Society_Custom_ID != null) {
			intent.putExtra("Society_Custom_ID", Society_Custom_ID);
		} else if (Partner_ID != null) {
			intent.putExtra("Partner_ID", Partner_ID);
		} else if (Edit_competition_ID != null) {
			intent.putExtra("Edit_competition_ID", Edit_competition_ID);
		}
		if (bundle != null) {
			intent.putExtras(bundle);
			setResult(999, intent);
		}
		
		
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.quit_custom_Rl:
			finish();
			break;
		default:
			break;
		}
	}
}
