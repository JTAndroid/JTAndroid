package com.tr.ui.organization.orgdetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tr.R;
import com.tr.api.OrganizationReqUtil;
import com.tr.ui.organization.model.hight.CustomerHight;
import com.tr.ui.organization.model.hight.CustomerHightInfo;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.organization.model.resource.CustomerAddress;
import com.tr.ui.organization.model.resource.CustomerDemandCommon;
import com.tr.ui.organization.orgdetails.orgadapter.AddModulesAdapter;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.FinancingActivity;
import com.tr.ui.people.cread.InvestmentActivity;
import com.tr.ui.people.cread.SpecialistIdentityActivity;
import com.tr.ui.people.cread.SpecialistNeedsActivity;
import com.tr.ui.people.cread.utils.MakeListView;
import com.tr.ui.people.cread.view.MyitemView;
import com.utils.http.EAPIConsts.OrganizationReqType;
import com.utils.http.IBindData;

public class OrgFinancingActivity extends BaseActivity implements
		OnClickListener, IBindData {

	private ImageView finacing_back_btn;

	private Intent intent;

	private String moduleName;

	private TextView titleName, myitemview_Tv;

	private ListView myitemview_Lv;

	private AddModulesAdapter addModulesAdapter;

	private ArrayList<String> dataList;

	private LinearLayout addLinearLayout;

	private MyitemView myItemView;

	private ImageView addmore_myitemview_iv;

	private List<CustomerDemandCommon> customerList;

	private CustomerDemandCommon customer;

	private List<CustomerPersonalLine> personalLineList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.org_activity_finacing_view);

		initUI();


	}


	public void initUI() {

		titleName = (TextView) findViewById(R.id.titleName);

		moduleName = getIntent().getStringExtra("ModuleName");

		if (moduleName.equals("投资意向")) {

			titleName.setText(moduleName);

		} else if (moduleName.equals("融资意向")) {

			titleName.setText(moduleName);

		} else if (moduleName.equals("专家需求")) {

			titleName.setText(moduleName);

		} else if (moduleName.equals("专家身份")) {

			titleName.setText(moduleName);

		}

		finacing_back_btn = (ImageView) findViewById(R.id.finacing_back_btn);
		finacing_back_btn.setOnClickListener(this);

		addLinearLayout = (LinearLayout) findViewById(R.id.addLinearLayout);

		myItemView = new MyitemView(this);

		addLinearLayout.addView(myItemView);

		myitemview_Tv = (TextView) myItemView.findViewById(R.id.myitemview_Tv);

		myitemview_Tv.setText("互联网金融");

		addmore_myitemview_iv = (ImageView) myItemView
				.findViewById(R.id.myitemview_Iv);

		addmore_myitemview_iv.setOnClickListener(this);

		myitemview_Lv = myItemView.getMyitemview_Lv();

		customerList = new ArrayList<CustomerDemandCommon>();

		customer = new CustomerDemandCommon();

		personalLineList = new ArrayList<CustomerPersonalLine>();

		// /** 主键 */
		// public Long id;
		// /** 所属模块： 1-投资，2-融资，3-专家需求，4-专家身份 */

		for (int i = 0; i < 2; i++) {

			customer.setId(Long.decode("1"));

			CustomerAddress custAddress = new CustomerAddress();

			custAddress.setCountyName("地区");

			custAddress.setAddress("北京");

			customer.setAddress(custAddress);

			customer.setIndustryIds("行业");

			customer.setIndustryNames("投资");

			customer.setTypeIds("类型");

			customer.setTypeNames("金融");

			CustomerPersonalLine customerPerson = new CustomerPersonalLine();

			customerPerson.setName("自定义名称");

			customerPerson.setContent("自定义内容");

			customerPerson.setType("1");

			personalLineList.add(customerPerson);

			customer.setPersonalLineList(personalLineList);

			customerList.add(customer);
		}

		addModulesAdapter = new AddModulesAdapter(this, customerList);

		myitemview_Lv.setAdapter(addModulesAdapter);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.finacing_back_btn:

			if (moduleName.equals("投资意向")) {

				intent = new Intent(OrgFinancingActivity.this,
						OrgMyHomePageActivity.class);

				setResult(1001, intent);

				finish();

			} else if (moduleName.equals("融资意向")) {

				intent = new Intent(OrgFinancingActivity.this,
						OrgMyHomePageActivity.class);

				setResult(1002, intent);

				finish();

			} else if (moduleName.equals("专家需求")) {

				intent = new Intent(OrgFinancingActivity.this,
						OrgMyHomePageActivity.class);

				setResult(1003, intent);

				finish();

			} else if (moduleName.equals("专家身份")) {

				intent = new Intent(OrgFinancingActivity.this,
						OrgMyHomePageActivity.class);

				setResult(1004, intent);

				finish();

			}

			break;

		case R.id.myitemview_Iv:

			if (moduleName.equals("投资意向")) {

				intent = new Intent(OrgFinancingActivity.this,
						InvestmentActivity.class);

				getCustomerDemandCommon();

				startActivityForResult(intent, 2001);

			} else if (moduleName.equals("融资意向")) {

				intent = new Intent(OrgFinancingActivity.this,
						FinancingActivity.class);

				getCustomerDemandCommon();

				startActivityForResult(intent, 2002);

			} else if (moduleName.equals("专家需求")) {
				intent = new Intent(OrgFinancingActivity.this,
						SpecialistNeedsActivity.class);

				getCustomerDemandCommon();

				startActivityForResult(intent, 2003);

			} else if (moduleName.equals("专家身份")) {
				intent = new Intent(OrgFinancingActivity.this,
						SpecialistIdentityActivity.class);

				getCustomerDemandCommon();

				startActivityForResult(intent, 2004);

			}

			break;

		}

	}

	public CustomerDemandCommon getCustomerDemandCommon() {
		for (int i = 0; i < customerList.size(); i++) {

			intent.putExtra("Customer", customerList.get(i));

		}
		return customer;

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {

		switch (resultCode) {

		case 2:

			dataList = intent.getStringArrayListExtra("Investment");

			MakeListView.ToListviewAdapter(this, dataList, myitemview_Lv);

			break;
		case 3:

			dataList = intent.getStringArrayListExtra("Financing");

			MakeListView.ToListviewAdapter(this, dataList, myitemview_Lv);

			break;
		case 4:

			dataList = intent.getStringArrayListExtra("Specialist_identity");

			MakeListView.ToListviewAdapter(this, dataList, myitemview_Lv);

			break;
		case 5:

			dataList = intent.getStringArrayListExtra("Specialist_needs");

			MakeListView.ToListviewAdapter(this, dataList, myitemview_Lv);

			break;
		}

		super.onActivityResult(requestCode, resultCode, intent);
	}

	// 网络请求的数据回掉接口

	@Override
	public void bindData(int tag, Object object) {
		
	}

}
