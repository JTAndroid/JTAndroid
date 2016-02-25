package com.tr.ui.organization.create_clientele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

import com.tr.R;
import com.tr.api.OrganizationReqUtil;
import com.tr.ui.organization.firstpage.AccessControlActivity;
import com.tr.ui.organization.firstpage.BasicData;
import com.tr.ui.organization.firstpage.BusinessAnalysisActivity;
import com.tr.ui.organization.firstpage.EditBasicInfomationActivity;
import com.tr.ui.organization.firstpage.FinancialProductsActivity;
import com.tr.ui.organization.firstpage.OrganizationIntroductionActivity;
import com.tr.ui.organization.firstpage.SelectModuleAndColumnActivity;
import com.tr.ui.organization.model.Customer;
import com.tr.ui.organization.model.GetId;
import com.tr.ui.organization.model.JCustomer;
import com.tr.ui.organization.model.government.AreaInfo;
import com.tr.ui.organization.model.government.DepartMentsInfo;
import com.tr.ui.organization.model.industry.CustomerIndustry;
import com.tr.ui.organization.model.param.ClientDetailsParams;
import com.tr.ui.organization.model.param.ClientDetialsIncomParams;
import com.tr.ui.organization.model.profile.CustomerBranch;
import com.tr.ui.organization.model.profile.CustomerInfo;
import com.tr.ui.organization.model.profile.CustomerLinkMan;
import com.tr.ui.organization.model.profile.CustomerListing;
import com.tr.ui.organization.model.profile.CustomerPartner;
import com.tr.ui.organization.model.profile.CustomerPhone;
import com.tr.ui.organization.model.profile.CustomerRemark;
import com.tr.ui.organization.orgdetails.EditClientBasicInfomationActivity;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.CustomActivity;
import com.tr.ui.people.cread.utils.MakeListView;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.tr.ui.people.cread.view.MyitemView;
import com.utils.http.EAPIConsts.OrganizationReqType;
import com.utils.http.IBindData;

/**
 * 客户资料页面  
 * @author hzy
 *
 */

public class ClientDataActivity extends BaseActivity implements OnClickListener,IBindData{

	private ImageView orgdata_rl_iv;
    private TextView org_tv_finish;
    private ImageView org_iv_edit;
    private MyEditTextView org_Custom;
    private RelativeLayout org_Addmore_Rl;
    private TextView access;
    private Intent intent;
	private MyitemView module;
	private HashMap<String, MyitemView> module_Map;
	private CustomerInfo customerInfo;
	private ArrayList<String> listview_item;
	private CustomerRemark publication;
	private CustomerRemark sponsorCustomer_Bean;
	private ArrayList<CustomerRemark> sponsorCustomerList;
	private ArrayList<CustomerLinkMan> Linkman_information;
	private CustomerListing markey_information;
	private ArrayList<CustomerPartner> partner;
	private List<CustomerBranch> sponsorBranchList;
	private CustomerBranch sponsorBank;
	private CustomerBranch lawFirm;
	private ArrayList<String> listview_item_bank;
	private String media_Type;
	private LinearLayout data_main_Ll;
	private RelativeLayout data_Rl;
	private ClientDetailsParams customer;
	private TextView name;
	private TextView industry;
	private TextView email;
	private TextView dial;
	private TextView money;
	private TextView org_Intro;
	private BasicData basicData;
	private long organ_Id;
	private GetId getId;
	private TextView client_type;
	private TextView client_msg;
	public String[] isListings = { "非上市公司", "上市公司" };// 是否上市 是 否
	private TextView client_all_name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.client_data);
		customer = (ClientDetailsParams)getIntent().getSerializableExtra("mCustomer");
		basicData = (BasicData) getIntent().getSerializableExtra("basic");
		organ_Id = getIntent().getLongExtra("organ_Id", 1);
		
		init();
		initListener();
		initData();
		initDataBasic();
	}
	
	private void initDataBasic(){//用于填充编辑页面返回的数据
		if(basicData != null){
			if(basicData.getName()!= null){//客户简称
				name.setText(basicData.getName());
			}
			else{
				name.setText("");
			}
			if(basicData.getType()!=null){//客户类型 client_type
				client_type.setText(basicData.getType());
			}else {
				client_type.setText("");
			}
			
			if(basicData.getIndustry()!= null){//行业类型
				industry.setText(basicData.getIndustry());
			}
			else{
				industry.setText("");
			}
			if(basicData.getEmail() != null){//邮箱
				email.setText(basicData.getEmail());
			}
			else{
				email.setText("");
			}
			if(basicData.getDial() != null){//联系电话
				dial.setText(basicData.getDial());
			}
			else{
				dial.setText("");
			}
			if(basicData.getIntrodution() != null){//客户描述
				org_Intro.setText(basicData.getIntrodution());
			}
			else{
				org_Intro.setText("");
			}
			if(basicData.getType() != null){//客户类型
				client_type.setText(basicData.getType());
			}
			else{
				client_type.setText("");
			}
			if(basicData.getOrgmsg() != null){//上市信息
				client_msg.setText(basicData.getOrgmsg() );
			}
			else{
				client_msg.setText("");
			}
			if(basicData.getOrgnum() != null){//证劵代码
				money.setText(basicData.getOrgnum() );
			}
			else{
				money.setText("");
			}
			
		}
		
	}

	private void initData(){
		//客户全称
		if(customer.name !=null){
			client_all_name.setText(customer.name);
		}else{
			client_all_name.setText("");
		}
		
		//客户简称
		if(customer.shotName != null){
			name.setText(customer.shotName);			
		}
		//行业
		List<String> lists = customer.industrys;
		System.out.println(lists.size());
		String str = "";
		if(lists!=null && lists.size()>0){
			for(int i = 0;i<lists.size();i++){
				if(i == lists.size()-1){
					str += lists.get(i);
				}else{
					str += lists.get(i) +",";
				}
			}
		}
		industry.setText(str);
		
		
		if (!customer.isListing.equals("")) {

			switch (Integer.parseInt(customer.isListing)) {

			case 0:

				client_msg.setText(isListings[0]);

				break;

			case 1:

				client_msg.setText(isListings[1]);

				break;

			}

		}
		
		//客户类型
		if(customer.type !=null){
			client_type.setText(customer.type);
		}else{
			client_type.setText("");
		}
		
		//客户邮箱
		if(customer.linkEmail != null){
			email.setText(customer.linkEmail);
		}
		else{
			email.setText("");
		}
//		//联系电话
//		if(customer.linkMobile != null){
//			dial.setText(customer.linkMobile);
//		}
//		else{
//			dial.setText("");
//		}
		
		//联系电话
		 ArrayList<CustomerPhone> phoneList = (ArrayList<CustomerPhone>) customer.phoneList;
		 String phone = "";
		 if(phoneList != null && phoneList.size()>0){
			 System.out.println("联系电话:" +phoneList.size());
		 for (int i = 0; i < phoneList.size(); i++) {
		 CustomerPhone customerPhone = phoneList.get(i);
		 phone = customerPhone.phone;
		   }
		 }
		 dial.setText(phone);
		
		
		
		
		//证券代码
		if(customer.stockNum != null){
			money.setText(customer.stockNum);
		}
		else{
			money.setText("");
		}
		//客户简介
		if(customer.discribe != null){
			org_Intro.setText(customer.discribe);
		}
		else{
			org_Intro.setText("");
		}
	}

	private void initListener() {
		org_Custom.setOnClickListener(this);
		org_iv_edit.setOnClickListener(this);
		org_Addmore_Rl.setOnClickListener(this);
		access.setOnClickListener(this);
//		org_Intro.setOnClickListener(this);
		orgdata_rl_iv.setOnClickListener(this);
		org_tv_finish.setOnClickListener(this);
	}


	private void init() {
		orgdata_rl_iv = (ImageView) findViewById(R.id.clientdata_rl_iv);
		org_tv_finish = (TextView) findViewById(R.id.client_tv_finish);
		org_iv_edit = (ImageView) findViewById(R.id.client_iv_edit);
		org_Custom = (MyEditTextView) findViewById(R.id.client_Custom);
		access = (TextView) findViewById(R.id.client_access);
		data_main_Ll = (LinearLayout) findViewById(R.id.client_data_main_Ll);
		data_Rl = (RelativeLayout) findViewById(R.id.client_data_Rl);
		module_Map = new HashMap<String, MyitemView>();
		org_Addmore_Rl = (RelativeLayout) findViewById(R.id.client_Addmore_Rl);
		name = (TextView) findViewById(R.id.client_name);
		industry = (TextView) findViewById(R.id.client_industry);
		email = (TextView) findViewById(R.id.client_email);
		dial = (TextView) findViewById(R.id.client_dial);
		money = (TextView) findViewById(R.id.client_money);
		org_Intro = (TextView) findViewById(R.id.show_client_Intro);
		client_type = (TextView) findViewById(R.id.client_type);
		client_msg = (TextView) findViewById(R.id.client_msg);
		client_all_name = (TextView) findViewById(R.id.client_all_name);
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.clientdata_rl_iv://后退上一页
			finish();
			break;
		case R.id.client_tv_finish://点击完成 跳到我的客户页面
			
			initData();
			
//			ClientDetialsIncomParams params = new ClientDetialsIncomParams();	
//			params.orgId = organ_Id;
//			Log.e("MSG", "客户资料参数"+params.orgId);
			
//			long orgId = Long.parseLong(getId.orgId );
//			customer.id = orgId;
			OrganizationReqUtil.doRequestWebAPI(ClientDataActivity.this,this, customer, null,OrganizationReqType.ORGANIZATION_REQ_CUSTOMER_SAVECUSPROFILE);//添加客户详情接口
			Intent in = new Intent(this, ClientDetailsActivity.class);
//			intent.putExtra("Organ_Id", orgId);		
			startActivity(in);			
			break;
		case R.id.client_iv_edit://点击编辑 基本信息
			Intent intent_edit = new Intent(this,EditClientBasicInfomationActivity.class);
			intent_edit.putExtra("edit", customer);
			startActivityForResult(intent_edit, 333);
			break;
		case R.id.client_Custom://点击自定义
			Intent intent_custom = new Intent(this,CustomActivity.class);
			startActivity(intent_custom);		
			break;

		case R.id.client_Addmore_Rl://点击选择模块及常用栏目
			Intent intent_select = new Intent(this,SelectModuleAndColumnActivity.class);
			startActivityForResult(intent_select, 0);
			break;
		case R.id.client_access://点击权限，跳到权限页面
			Intent intent_access = new Intent(this,AccessControlActivity.class);
			startActivity(intent_access);
			break;
		}
	}
	/**
	 * 将模块选择动态的添加到布局中
	 * 
	 * @param module_list
	 */
	private void AddMore(ArrayList<String> module_list) {
		module = null;
		LinearLayout layout = new LinearLayout(this);
		System.out.println("控件数据集合" + module_list);
		if (module_list != null) {
			for (final String string : module_list) {
				if ("地区概况".equals(string)) {
					MyEditTextView editTextView = new MyEditTextView(this);
					editTextView.setChoose(true);
					editTextView.setTextLabel("地区概况");
					editTextView.setOverline(true);
					editTextView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							intent = new Intent(context,
									AreaSurveyActivity.class);
							startActivityForResult(intent, 6029);
						}
					});
					layout.addView(editTextView, new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT));
				}
				if ("主要职能部门".equals(string)) {
					MyEditTextView editTextView = new MyEditTextView(this);
					editTextView.setChoose(true);
					editTextView.setOverline(true);
					editTextView.setTextLabel("主要职能部门");
					editTextView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							intent = new Intent(context,
									FunctionalDepartmentActivity.class);
							startActivityForResult(intent, 6030);
						}
					});
					layout.addView(editTextView, new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT));
				}
				if (!"主要职能部门".equals(string) && !"地区概况".equals(string)) {
					module = new MyitemView(this);
					layout.setOrientation(LinearLayout.VERTICAL);
					module.setText_label(string);
					System.out.println("控件数据" + string);
					layout.addView(module, new LinearLayout.LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT));
					module.setOnClickListener(new OnClickListener() {
						private Intent intent;

						@Override
						// 根据标题来判断进入那个Activity
						public void onClick(View v) {
							if ("一般企业".equals(string)) {
								intent = new Intent(context,
										EnterpriseActivity.class);
								intent.putExtra("Enterprise", "一般企业");
								Bundle bundle = new Bundle();
								bundle.putSerializable("Enterprise_Bean",
										customerInfo);
								intent.putExtras(bundle);
								startActivityForResult(intent, 6001);
							} else if ("地区概况".equals(string)) {

							} else if ("主要职能部门".equals(string)) {

							} else if ("中介机构".equals(string)) {
								intent = new Intent(context,
										EnterpriseActivity.class);
								intent.putExtra("Enterprise", "中介机构");
								Bundle bundle = new Bundle();
								bundle.putSerializable("Enterprise_Bean",
										customerInfo);
								intent.putExtras(bundle);
								startActivityForResult(intent, 6002);
							} else if ("企业刊物".equals(string)) {
								intent = new Intent(context,
										CustomActivity.class);
								intent.putExtra("Custom", "企业刊物");
								Bundle bundle = new Bundle();
								bundle.putSerializable("publication_Bean",
										publication);
								intent.putExtras(bundle);
								startActivityForResult(intent, 6003);
							} else if ("主要客户".equals(string)) {
								intent = new Intent(context,
										CustomActivity.class);
								intent.putExtra("Custom", "主要客户");
								Bundle bundle = new Bundle();
								bundle.putSerializable("sponsorCustomer_Bean",
										sponsorCustomer_Bean);
								intent.putExtras(bundle);
								startActivityForResult(intent, 6004);
							} else if ("合伙人".equals(string)) {
								intent = new Intent(context,
										PartnerActivity.class);
								Bundle bundle = new Bundle();
								bundle.putParcelableArrayList("Partner_Bean",
										partner);
								intent.putExtras(bundle);
								startActivityForResult(intent, 6005);
							} else if ("机构简介".equals(string)) {
								intent = new Intent(context,
										InstitutionsResumeActivity.class);
								Bundle bundle = new Bundle();
								bundle.putSerializable("Institutions_Bean",
										customerInfo);
								intent.putExtras(bundle);
								startActivityForResult(intent, 6006);
							} else if ("互联网媒体".equals(string)) {
								intent = new Intent(context,
										EnterpriseActivity.class);
								intent.putExtra("Enterprise", "互联网媒体");
								Bundle bundle = new Bundle();
								bundle.putSerializable("Enterprise_Bean",
										customerInfo);
								intent.putExtras(bundle);
								startActivityForResult(intent, 6007);
							} else if ("报纸期刊".equals(string)) {
								intent = new Intent(context,
										EnterpriseActivity.class);
								Bundle bundle = new Bundle();
								bundle.putSerializable("Enterprise_Bean",
										customerInfo);
								intent.putExtras(bundle);
								intent.putExtra("Enterprise", "报纸期刊");
								startActivityForResult(intent, 6008);
							} else if ("研究机构".equals(string)) {
								intent = new Intent(context,
										ResearchInstitutionActivity.class);
								Bundle bundle = new Bundle();
								bundle.putSerializable(
										"Research_institution_Bean",
										customerInfo);
								intent.putExtras(bundle);
								startActivityForResult(intent, 6009);
							} else if ("金融机构".equals(string)) {
								intent = new Intent(context,
										EnterpriseActivity.class);
								intent.putExtra("Enterprise", "金融机构");
								Bundle bundle = new Bundle();
								bundle.putSerializable("Enterprise_Bean",
										customerInfo);
								intent.putExtras(bundle);
								startActivityForResult(intent, 6010);
							} else if ("电视广播".equals(string)) {
								intent = new Intent(context,
										TelecastActivity.class);
								Bundle bundle = new Bundle();
								bundle.putSerializable("Telecast_Bean",
										customerInfo);
								intent.putExtras(bundle);
								startActivityForResult(intent, 6011);
							} else if ("联系人资料".equals(string)) {
								intent = new Intent(context,
										LinkmanInformationActivity.class);
								Bundle bundle = new Bundle();
								bundle.putParcelableArrayList(
										"Linkman_information_Bean",
										Linkman_information);
								intent.putExtras(bundle);
								startActivityForResult(intent, 6012);
							} else if ("上市信息".equals(string)) {
								intent = new Intent(context,
										MarkeyInformationActivity.class);
								Bundle bundle = new Bundle();
								bundle.putSerializable(
										"Markey_information_Bean",
										markey_information);
								intent.putExtras(bundle);
								startActivityForResult(intent, 6013);
							} else if ("相关当事人".equals(string)) {
								intent = new Intent(context,
										RelevantPartiesActivity.class);
								startActivityForResult(intent, 6014);
							} else if ("执业资质".equals(string)) {
								intent = new Intent(
										context,
										CustomActivity.class);
								intent.putExtra("Custom", "执业资质");
								startActivityForResult(intent, 6016);
							} else if ("分支机构".equals(string)) {
								intent = new Intent(context,
										EmbranchmentActivity.class);
								startActivityForResult(intent, 6020);
							} else if ("业务分析".equals(string)) {
								intent = new Intent(context,
										BusinessAnalysisActivity.class);
								startActivityForResult(intent, 6019);
							} else if ("关联企业".equals(string)) {
								intent = new Intent(context,
										AffiliatedEnterpriseActivity.class);
								startActivityForResult(intent, 6017);
							} else if ("金融产品".equals(string)) {
								intent = new Intent(context,
										FinancialProductsActivity.class);
								startActivityForResult(intent, 6018);
							} else if ("专业团队".equals(string)) {
								intent = new Intent(context,
										ProfessionalTeamActivity.class);
								startActivityForResult(intent, 6015);
							} else if ("资源需求".equals(string)) {
								intent = new Intent(context,
										ResourceNeedsActivity.class);
								startActivityForResult(intent, 6031);
							} else if ("财务分析".equals(string)) {
								intent = new Intent(context,
										FinancialAnalysisActivity.class);
								startActivityForResult(intent, 6025);
							} else if ("高层治理".equals(string)) {
								intent = new Intent(context,
										SeniorManagementActivity.class);
								startActivityForResult(intent, 6023);
							} else if ("股东研究".equals(string)) {
								intent = new Intent(context,
										ShareholderActivity.class);
								startActivityForResult(intent, 6024);
							} else if ("同业竞争".equals(string)) {
								intent = new Intent(context,
										HorizontalCompetitionActivity.class);
								startActivityForResult(intent, 6026);
							} else if ("行业动态".equals(string)) {
								intent = new Intent(context,
										IndustryTrendsActivity.class);
								startActivityForResult(intent, 6027);
							} else if ("研究报告".equals(string)) {
								intent = new Intent(context,
										CustomActivity.class);
								intent.putExtra("Custom", "研究报告");
								startActivityForResult(intent, 6028);
							}
						}
					});
					module_Map.put(string, module);
				}
			}
			data_main_Ll.addView(layout,data_main_Ll.indexOfChild(org_Addmore_Rl)+1);

		}
	}

	@Override
	protected void onActivityResult(int resultCode, int requestCode, Intent data) {
		super.onActivityResult(resultCode, requestCode, data);
		if (data != null) {
			switch (resultCode) {
			case 6001:
				customerInfo = (CustomerInfo) data
						.getSerializableExtra("Enterprise_Bean");
				
				customerProfile.info = customerInfo;
				customer.type = "2";
				listview_item = data.getStringArrayListExtra("Enterprise");
				if (listview_item!=null) {
					//动态的将联系方式的数据填充到listview中
					AdaptiveListView(MakeListView.ToListviewAdapter(this,listview_item,module_Map.get("一般企业").getMyitemview_Lv()),displayMetrics);
				}
				break;
			case 6002:
				customerInfo = (CustomerInfo) data
						.getSerializableExtra("Enterprise_Bean");
				customerProfile.info = customerInfo;
				customer.type = "4";
				listview_item = data.getStringArrayListExtra("Enterprise");
				if (listview_item!=null) {
					//动态的将联系方式的数据填充到listview中
					AdaptiveListView(MakeListView.ToListviewAdapter(this,listview_item,module_Map.get("中介机构").getMyitemview_Lv()),displayMetrics);
				}
				break;
			case 6007:
				customerInfo = (CustomerInfo) data
						.getSerializableExtra("Enterprise_Bean");
				customerProfile.info = customerInfo;
				customer.type = "9";
				listview_item = data.getStringArrayListExtra("Enterprise");
				if (listview_item!=null) {
					//动态的将联系方式的数据填充到listview中
					AdaptiveListView(MakeListView.ToListviewAdapter(this,listview_item,module_Map.get("互联网媒体").getMyitemview_Lv()),displayMetrics);
				}
				break;
			case 6008:
				customerInfo = (CustomerInfo) data
						.getSerializableExtra("Enterprise_Bean");
				customerProfile.info = customerInfo;
				customer.type = "6";
				listview_item = data.getStringArrayListExtra("Enterprise");
				if (listview_item!=null) {
					//动态的将联系方式的数据填充到listview中
					AdaptiveListView(MakeListView.ToListviewAdapter(this,listview_item,module_Map.get("报纸期刊").getMyitemview_Lv()),displayMetrics);
				}
				break;
			case 6010:
				customerInfo = (CustomerInfo) data
						.getSerializableExtra("Enterprise_Bean");
				customerProfile.info = customerInfo;
				customer.type = "1";
				listview_item = data.getStringArrayListExtra("Enterprise");
				if (listview_item!=null) {
					//动态的将联系方式的数据填充到listview中
					AdaptiveListView(MakeListView.ToListviewAdapter(this,listview_item,module_Map.get("金融机构").getMyitemview_Lv()),displayMetrics);
				}
				break;
			case 6011:
				customerInfo = (CustomerInfo) data
						.getSerializableExtra("Telecast_Bean");
				customerProfile.info = customerInfo;
				customer.type = "8";
				listview_item = data.getStringArrayListExtra("Telecast_Activity");
				if (listview_item!=null) {
					//动态的将联系方式的数据填充到listview中
					AdaptiveListView(MakeListView.ToListviewAdapter(this,listview_item,module_Map.get("电视广播").getMyitemview_Lv()),displayMetrics);
				}
				break;
			case 6006:
				customerInfo = (CustomerInfo) data
						.getSerializableExtra("Institutions_Bean");
				customerProfile.info = customerInfo;
				customer.type = "3";
				listview_item = data.getStringArrayListExtra("Institutions_resume_Activity");
				if (listview_item!=null) {
					//动态的将联系方式的数据填充到listview中
					AdaptiveListView(MakeListView.ToListviewAdapter(this,listview_item,module_Map.get("机构简介").getMyitemview_Lv()),displayMetrics);
				}
				break;
			case 6009:
				customerInfo = (CustomerInfo) data
						.getSerializableExtra("Research_institution_Bean");
				customerProfile.info = customerInfo;
				customer.type = "7";
				listview_item = data.getStringArrayListExtra("Research_institution_Activity");
				if (listview_item!=null) {
					//动态的将联系方式的数据填充到listview中
					AdaptiveListView(MakeListView.ToListviewAdapter(this,listview_item,module_Map.get("研究机构").getMyitemview_Lv()),displayMetrics);
				}
				break;
			case 6003:
				publication = (CustomerRemark) data
						.getSerializableExtra("publication_Bean");
				customerProfile.publication = publication;
				listview_item = data.getStringArrayListExtra("Custom");
				if (listview_item!=null) {
					//动态的将联系方式的数据填充到listview中
					AdaptiveListView(MakeListView.ToListviewAdapter(this,listview_item,module_Map.get("企业刊物").getMyitemview_Lv()),displayMetrics);
				}
				break;
			case 6004:
				sponsorCustomer_Bean = (CustomerRemark) data
						.getSerializableExtra("sponsorCustomer_Bean");
				sponsorCustomerList.add(sponsorCustomer_Bean);
				customerProfile.sponsorCustomerList = sponsorCustomerList;
				listview_item = data.getStringArrayListExtra("Custom");
				if (listview_item!=null) {
					//动态的将联系方式的数据填充到listview中
					AdaptiveListView(MakeListView.ToListviewAdapter(this,listview_item,module_Map.get("主要客户").getMyitemview_Lv()),displayMetrics);
				}
				break;
			case 6012:
				Bundle bundle = data.getExtras();
				bundle.setClassLoader(getClassLoader());
				Linkman_information = bundle
						.getParcelableArrayList("Linkman_information_Bean");
				customerProfile.linkMans = Linkman_information;
				listview_item = data.getStringArrayListExtra("Linkman_information");
				if (listview_item!=null) {
					//动态的将联系方式的数据填充到listview中
					AdaptiveListView(MakeListView.ToListviewAdapter(this,listview_item,module_Map.get("联系人资料").getMyitemview_Lv()),displayMetrics);
				}
				break;
			case 6013:
				markey_information = (CustomerListing) data
						.getSerializableExtra("Markey_information_Bean");
				customerProfile.listing = markey_information;
				listview_item = data.getStringArrayListExtra("Markey_information");
				if (listview_item!=null) {
					//动态的将联系方式的数据填充到listview中
					AdaptiveListView(MakeListView.ToListviewAdapter(this,listview_item,module_Map.get("上市信息").getMyitemview_Lv()),displayMetrics);
				}
				break;
			case 6005:
				partner = data.getParcelableArrayListExtra("Partner_Bean");
				customerProfile.partnerList = partner;
				listview_item = data.getStringArrayListExtra("Partner_Activity");
				if (listview_item!=null) {
					//动态的将联系方式的数据填充到listview中
					AdaptiveListView(MakeListView.ToListviewAdapter(this,listview_item,module_Map.get("合伙人").getMyitemview_Lv()),displayMetrics);
				}
				break;
			case 6014:
				sponsorBranchList = data
						.getParcelableArrayListExtra("sponsorBranchList");
				sponsorBank = data.getParcelableExtra("sponsorBank");
				lawFirm = data.getParcelableExtra("lawFirm");
				customerProfile.sponsorBranchList = sponsorBranchList;
				customerProfile.sponsorBank = sponsorBank;
				customerProfile.lawFirm = lawFirm;
				listview_item = data.getStringArrayListExtra("Relevant_parties_Activity_institution");
				listview_item_bank = data.getStringArrayListExtra("Relevant_parties_Activity_bank_list");
				if (listview_item!=null) {
					//动态的将联系方式的数据填充到listview中
					AdaptiveListView(MakeListView.ToListviewAdapter(this,listview_item,module_Map.get("相关当事人").getMyitemview_Lv()),displayMetrics);
				}
				break;
			case 6029:
				// 地区
				AreaInfo areaInfo = (AreaInfo) data
						.getSerializableExtra("Area_Survey_Input_Bean");
				listview_item = data.getStringArrayListExtra("Area_survey_Activity");
				if (listview_item!=null) {
					//动态的将联系方式的数据填充到listview中
					AdaptiveListView(MakeListView.ToListviewAdapter(this,listview_item,module_Map.get("地区概况").getMyitemview_Lv()),displayMetrics);
				}
				break;
			case 6030:
				// 主要职能部门
				DepartMentsInfo departMentsInfo = (DepartMentsInfo) data
						.getSerializableExtra("Functional_department_Bean");
				listview_item = data.getStringArrayListExtra("Functional_department_Activity");
				if (listview_item!=null) {
					//动态的将联系方式的数据填充到listview中
					AdaptiveListView(MakeListView.ToListviewAdapter(this,listview_item,module_Map.get("主要职能部门").getMyitemview_Lv()),displayMetrics);
				}
				break;
			case 333:
				BasicData basicData = (BasicData) data.getSerializableExtra("basic");
				name.setText(basicData.getName());
				industry.setText(basicData.getIndustry());
				email.setText(basicData.getEmail());
				dial.setText(basicData.getDial());
				org_Intro.setText(basicData.getIntrodution());
				client_type.setText(basicData.getType());
				break;
			default:
				break;
			}
			if (resultCode == 0) {

				switch (requestCode) {

				case 999:
					// myEditTextView =
					// custom(data,org_custom_Etv,org_main1_Ll,list);
					break;
			
				case 8001:
					media_Type = data.getStringExtra("Media_Type");
					break;
				
				case 23:
					ArrayList<String> Module = data
							.getStringArrayListExtra("Module");
					if (!Module.isEmpty()) {
						AddMore(Module);
					}
					break;
				default:
					break;
				}
			}
		}
	}

	@Override
	public void bindData(int tag, Object object) {
		
		switch (tag) {
     case OrganizationReqType.ACCESS_TO_THE_PRIMARY_KEY: //获取客户主键ID
			
			Map<String, Object> dataHm = (Map<String, Object>) object;
			
			getId = (GetId) dataHm.get("getId");
			
           default:
			break;			
		}

		
	}		
}
