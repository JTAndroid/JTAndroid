package com.tr.ui.organization.firstpage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.R;
import com.tr.api.OrganizationReqUtil;
import com.tr.ui.organization.create_clientele.AffiliatedEnterpriseActivity;
import com.tr.ui.organization.create_clientele.AreaSurveyActivity;
import com.tr.ui.organization.create_clientele.EmbranchmentActivity;
import com.tr.ui.organization.create_clientele.EnterpriseActivity;
import com.tr.ui.organization.create_clientele.FinancialAnalysisActivity;
import com.tr.ui.organization.create_clientele.FunctionalDepartmentActivity;
import com.tr.ui.organization.create_clientele.HorizontalCompetitionActivity;
import com.tr.ui.organization.create_clientele.IndustryTrendsActivity;
import com.tr.ui.organization.create_clientele.InstitutionsResumeActivity;
import com.tr.ui.organization.create_clientele.LinkmanInformationActivity;
import com.tr.ui.organization.create_clientele.MarkeyInformationActivity;
import com.tr.ui.organization.create_clientele.PartnerActivity;
import com.tr.ui.organization.create_clientele.ProfessionalTeamActivity;
import com.tr.ui.organization.create_clientele.RelevantPartiesActivity;
import com.tr.ui.organization.create_clientele.ResearchInstitutionActivity;
import com.tr.ui.organization.create_clientele.ResourceNeedsActivity;
import com.tr.ui.organization.create_clientele.SeniorManagementActivity;
import com.tr.ui.organization.create_clientele.ShareholderActivity;
import com.tr.ui.organization.create_clientele.TelecastActivity;
import com.tr.ui.organization.model.BasicInfo;
import com.tr.ui.organization.model.Customer;
import com.tr.ui.organization.model.CustomerProfileVo;
import com.tr.ui.organization.model.GetId;
import com.tr.ui.organization.model.government.AreaInfo;
import com.tr.ui.organization.model.government.DepartMentsInfo;
import com.tr.ui.organization.model.profile.CustomerBranch;
import com.tr.ui.organization.model.profile.CustomerInfo;
import com.tr.ui.organization.model.profile.CustomerLinkMan;
import com.tr.ui.organization.model.profile.CustomerListing;
import com.tr.ui.organization.model.profile.CustomerPartner;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.organization.model.profile.CustomerPersonalPlate;
import com.tr.ui.organization.model.profile.CustomerRemark;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.CustomActivity;
import com.tr.ui.people.cread.utils.MakeListView;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.tr.ui.people.cread.view.MyitemView;
import com.utils.http.EAPIConsts.OrganizationReqType;
import com.utils.http.IBindData;
import com.utils.log.KeelLog;

/**
 * 组织资料页面  展示状态
 * @author John
 *
 */
public class OrganizationDataActivity extends BaseActivity implements
		OnClickListener, IBindData {
	private ImageView orgdata_rl_iv;
	private TextView org_tv_finish;
	private ImageView org_iv_edit;
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
	private CustomerProfileVo customer;
	private TextView name;
	private TextView industry;
	private TextView email;
	private TextView dial;
	private TextView money;
	private BasicData basicData;
	private GetId getId2;
	private Long orgId;
	private Long type2;
	private long createId;
	private ImageView org_line1_iv;
	private boolean isNull;
	private ArrayList<MyEditTextView> editTextViews;

	// private ArrayList<CustomerPersonalLine> custom; // 自定义
	private LinearLayout data_main_LLL;
	private TextView org_Intro_content;
	private List<CustomerPersonalLine> propertyList; // 自定义
	private RelativeLayout org_money_RL;
	private TextView org_line1_tv;
	private String urlToSql;

	private List<CustomerPersonalLine> propertyLists;

	private Customer myCustomer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.organization_data);
		propertyLists = new ArrayList<CustomerPersonalLine>();
		editTextViews = new ArrayList<MyEditTextView>();
		customer = (CustomerProfileVo) getIntent().getSerializableExtra(
				"customer");
		createId = getIntent().getLongExtra("customerId", 0);

		myCustomer = new Customer();
		init();
		initListener();
		if (customer != null) {
			initData(customer);
		}
	}

	// 从我的主页带来的数据
	private void initData(CustomerProfileVo customer) {
		// 设置组织资料的Logo
		if (customer.picLogo != null) {
			ImageLoader.getInstance().displayImage(customer.picLogo,org_line1_iv);
			myCustomer.picLogo = customer.picLogo.substring(customer.picLogo
					.indexOf("m", 0) + 1);
		} else {
			org_line1_iv.setVisibility(View.VISIBLE);
		}
		// 组织简称
		if (!TextUtils.isEmpty(customer.shotName)) {
			name.setText(customer.shotName);
		} else {
			org_line1_tv.setText("组织全称");
			name.setText(customer.name);
		}
		// 行业
		List<String> lists = customer.industrys;
		String str = "";
		if (lists != null && lists.size() > 0) {
			for (int i = 0; i < lists.size(); i++) {
				if (i == lists.size() - 1) {
					str += lists.get(i);
				} else {
					str += lists.get(i) + "";
				}
			}
		}
		industry.setText(str);
		// 组织邮箱
		if (customer.linkEmail != null) {
			email.setText(customer.linkEmail);
		} else {
			email.setText("");
		}
		// 联系电话
		if (customer.linkMobile != null) {
			dial.setText(customer.linkMobile);
		} else {
			dial.setText("");
		}
		// 证券代码
		if (!"3".equals(customer.type)) {
			if (customer.stockNum != null) {
				money.setText(customer.stockNum);
			} else {
				money.setText("");
			}
		}else{
			org_money_RL.setVisibility(View.GONE);
		}
		
		// 组织简介
		if (customer.discribe != null) {
			org_Intro_content.setText(customer.discribe);
		} else {
			org_Intro_content.setText("");
		}

		// 政府机构
		if ("3".equals(customer.type)) {
			org_money_RL.setVisibility(View.GONE);
		}
		// 非上市
		if ("0".equals(customer.isListing)) {
			org_money_RL.setVisibility(View.GONE);
		}

		// 展示自定义模块
		propertyLists = customer.propertyList;
		if (propertyLists != null && propertyLists.size() > 0) {
			data_main_LLL.removeAllViews();
			for (int i = 0; i < propertyLists.size(); i++) {
				CustomerPersonalLine line = propertyLists.get(i);
				if ("1".equals(line.type)) {
					if (!line.name.equals("")) {
						MyEditTextView myEditTextView_Text = new MyEditTextView(
								context);
						myEditTextView_Text.setText(line.content);
						myEditTextView_Text.setTextLabel(line.name);
						data_main_LLL.addView(myEditTextView_Text);
					}
				}
			}
		}

	}

	private void initListener() {
		org_iv_edit.setOnClickListener(this);
		orgdata_rl_iv.setOnClickListener(this);
		org_tv_finish.setOnClickListener(this);
	}

	private void init() {
		getId2 = new GetId();
		orgdata_rl_iv = (ImageView) findViewById(R.id.orgdata_rl_iv);
		org_tv_finish = (TextView) findViewById(R.id.org_tv_finish);
		org_iv_edit = (ImageView) findViewById(R.id.org_iv_edit);
		data_main_Ll = (LinearLayout) findViewById(R.id.data_main_Ll);
		data_Rl = (RelativeLayout) findViewById(R.id.data_Rl);
		module_Map = new HashMap<String, MyitemView>();
		org_Addmore_Rl = (RelativeLayout) findViewById(R.id.org_Addmore_Rl);
		name = (TextView) findViewById(R.id.name);
		industry = (TextView) findViewById(R.id.industry);
		email = (TextView) findViewById(R.id.email);
		dial = (TextView) findViewById(R.id.dial);
		money = (TextView) findViewById(R.id.money);
		org_line1_iv = (ImageView) findViewById(R.id.org_line1_iv);
		data_main_LLL = (LinearLayout) findViewById(R.id.data_main_LLL);
		org_Intro_content = (TextView) findViewById(R.id.org_Intro_content);
		orgdata_rl_iv = (ImageView) findViewById(R.id.orgdata_rl_iv);
		org_money_RL = (RelativeLayout) findViewById(R.id.org_money_RL);
		org_line1_tv = (TextView) findViewById(R.id.org_line1_tv);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.orgdata_rl_iv:// 后退上一页
			finish();
			break;
		case R.id.org_tv_finish:// 点击完成
			getData();
			break;
		case R.id.org_iv_edit:// 点击编辑
			Intent intent_edit = new Intent(this,
					EditBasicInfomationActivity.class);
			// BasicInfo basicInfo = new BasicInfo();
			// basicInfo.setPicLogo(customer.picLogo);
			// basicInfo.setName(name.getText().toString());
			// basicInfo.setIndustrys(industry.getText().toString());
			// basicInfo.setEmail(email.getText().toString());
			// basicInfo.setLinkedMobile(dial.getText().toString());
			// basicInfo.setDiscribe(org_Intro_content.getText().toString());
			if (customer != null) {
				intent_edit.putExtra("edit", customer);
			}
			startActivityForResult(intent_edit, 333);
			break;
		// case R.id.org_Addmore_Rl:// 点击选择模块及常用栏目
		// Intent intent_select = new Intent(this, Org_Module_Activity.class);
		// CustomerColumn_re column_re2 = new CustomerColumn_re();
		// orgId = Long.parseLong(createId);
		// column_re2.orgId = orgId;
		// type2 = Long.parseLong(customer.type);
		// column_re2.templateId = type2;
		// Bundle bundle = new Bundle();
		// bundle.putSerializable("Column_Bean", column_re2);
		// System.out.println("Column_Bean+Column_Bean+Column_Bean"
		// + column_re2.toString());
		// intent_select.putExtras(bundle);
		// startActivityForResult(intent_select, 0);
		// break;
		// case R.id.access:// 点击权限，跳到权限页面
		// Intent intent_access = new Intent(this, AccessControlActivity.class);
		// startActivity(intent_access);
		// break;
		}
	}

	private void getData() {
		// 用于保存资料信息，点完成掉接口时用

		String nameString = name.getText().toString().trim();// 组织名称
		String industryString = industry.getText().toString().trim();// 行业
		String emailString = email.getText().toString().trim();// 邮箱
		String dialString = dial.getText().toString().trim();// 联系电话
		String moneyString = money.getText().toString().trim();// 证劵代码
		String discribe = org_Intro_content.getText().toString().trim();// 描述信息

		myCustomer.shotName = nameString;// 添加简称

		if (industryString != null) {// 添加行业
			String[] arr = industryString.split(",");
			List<String> lists = new ArrayList<String>();
			for (String str : arr) {
				lists.add(str);
			}
			myCustomer.industrys = lists;
		}
		List<String> industryIds = new ArrayList<String>();
		myCustomer.industryIds = industryIds;
		myCustomer.email = emailString;// 添加邮箱
		myCustomer.linkMobile = dialString;// 关联电话
		myCustomer.stockNum = moneyString;// 证卷号码
		myCustomer.discribe = discribe;// 简介

		// if (propertyList != null) {
		// for (int i = 0; i < propertyList.size(); i++) {
		// CustomerPersonalLine line = propertyList.get(i);
		// if ("1".equals(line.type)) {
		// if (!line.name.equals("")) {
		// propertyList.add(line);
		// System.out.println("这段走了几次");
		// }
		// }
		// }
		// }
		//
		// if(propertyLists != null){
		// for (int i = 0; i < propertyLists.size(); i++) {
		// CustomerPersonalLine line = propertyLists.get(i);
		// if ("1".equals(line.type)) {
		// if (!line.name.equals("")) {
		// propertyList.add(line);
		// System.out.println("第二段段走了几次");
		// }
		// }
		// }
		// }
		// // customer.propertyList = propertyList;
		// // customer.id = Long.valueOf(createId);
		// // customer.id = createId;

		if (propertyLists != null) {
			myCustomer.propertyList = propertyLists;
		}
		myCustomer.id = createId;
		// 将封装的javaBean请
		OrganizationReqUtil.doRequestWebAPI(this, this, myCustomer, null,
				OrganizationReqType.ORGANIZATION_REQ_ORG_SAVECUSPROFILE);
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
								intent = new Intent(context,
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
			data_main_Ll.addView(layout,
					data_main_Ll.indexOfChild(org_Addmore_Rl) + 1);

		}
	}

	@Override
	protected void onActivityResult(int resultCode, int requestCode, Intent data) {
		super.onActivityResult(resultCode, requestCode, data);
		if (data != null) {
			switch (requestCode) {
			case 300:
				String text = data.getStringExtra("introduction");
				System.out.println("组织资料界面====" + text);
				break;
			case 6001:
				customerInfo = (CustomerInfo) data
						.getSerializableExtra("Enterprise_Bean");

				customerProfile.info = customerInfo;
				customer.type = "2";
				listview_item = data.getStringArrayListExtra("Enterprise");
				if (listview_item != null) {
					// 动态的将联系方式的数据填充到listview中
					AdaptiveListView(MakeListView.ToListviewAdapter(this,
							listview_item, module_Map.get("一般企业")
									.getMyitemview_Lv()), displayMetrics);
				}
				break;
			case 6002:
				customerInfo = (CustomerInfo) data
						.getSerializableExtra("Enterprise_Bean");
				customerProfile.info = customerInfo;
				customer.type = "4";
				listview_item = data.getStringArrayListExtra("Enterprise");
				if (listview_item != null) {
					// 动态的将联系方式的数据填充到listview中
					AdaptiveListView(MakeListView.ToListviewAdapter(this,
							listview_item, module_Map.get("中介机构")
									.getMyitemview_Lv()), displayMetrics);
				}
				break;
			case 6007:
				customerInfo = (CustomerInfo) data
						.getSerializableExtra("Enterprise_Bean");
				customerProfile.info = customerInfo;
				customer.type = "9";
				listview_item = data.getStringArrayListExtra("Enterprise");
				if (listview_item != null) {
					// 动态的将联系方式的数据填充到listview中
					AdaptiveListView(MakeListView.ToListviewAdapter(this,
							listview_item, module_Map.get("互联网媒体")
									.getMyitemview_Lv()), displayMetrics);
				}
				break;
			case 6008:
				customerInfo = (CustomerInfo) data
						.getSerializableExtra("Enterprise_Bean");
				customerProfile.info = customerInfo;
				customer.type = "6";
				listview_item = data.getStringArrayListExtra("Enterprise");
				if (listview_item != null) {
					// 动态的将联系方式的数据填充到listview中
					AdaptiveListView(MakeListView.ToListviewAdapter(this,
							listview_item, module_Map.get("报纸期刊")
									.getMyitemview_Lv()), displayMetrics);
				}
				break;
			case 6010:
				customerInfo = (CustomerInfo) data
						.getSerializableExtra("Enterprise_Bean");
				customerProfile.info = customerInfo;
				customer.type = "1";
				listview_item = data.getStringArrayListExtra("Enterprise");
				if (listview_item != null) {
					// 动态的将联系方式的数据填充到listview中
					AdaptiveListView(MakeListView.ToListviewAdapter(this,
							listview_item, module_Map.get("金融机构")
									.getMyitemview_Lv()), displayMetrics);
				}
				break;
			case 6011:
				customerInfo = (CustomerInfo) data
						.getSerializableExtra("Telecast_Bean");
				customerProfile.info = customerInfo;
				customer.type = "8";
				listview_item = data
						.getStringArrayListExtra("Telecast_Activity");
				if (listview_item != null) {
					// 动态的将联系方式的数据填充到listview中
					AdaptiveListView(MakeListView.ToListviewAdapter(this,
							listview_item, module_Map.get("电视广播")
									.getMyitemview_Lv()), displayMetrics);
				}
				break;
			case 6006:
				customerInfo = (CustomerInfo) data
						.getSerializableExtra("Institutions_Bean");
				customerProfile.info = customerInfo;
				customer.type = "3";
				listview_item = data
						.getStringArrayListExtra("Institutions_resume_Activity");
				if (listview_item != null) {
					// 动态的将联系方式的数据填充到listview中
					AdaptiveListView(MakeListView.ToListviewAdapter(this,
							listview_item, module_Map.get("机构简介")
									.getMyitemview_Lv()), displayMetrics);
				}
				break;
			case 6009:
				customerInfo = (CustomerInfo) data
						.getSerializableExtra("Research_institution_Bean");
				customerProfile.info = customerInfo;
				customer.type = "7";
				listview_item = data
						.getStringArrayListExtra("Research_institution_Activity");
				if (listview_item != null) {
					// 动态的将联系方式的数据填充到listview中
					AdaptiveListView(MakeListView.ToListviewAdapter(this,
							listview_item, module_Map.get("研究机构")
									.getMyitemview_Lv()), displayMetrics);
				}
				break;
			case 6003:
				publication = (CustomerRemark) data
						.getSerializableExtra("publication_Bean");
				customerProfile.publication = publication;
				listview_item = data.getStringArrayListExtra("Custom");
				if (listview_item != null) {
					// 动态的将联系方式的数据填充到listview中
					AdaptiveListView(MakeListView.ToListviewAdapter(this,
							listview_item, module_Map.get("企业刊物")
									.getMyitemview_Lv()), displayMetrics);
				}
				break;
			case 6004:
				sponsorCustomer_Bean = (CustomerRemark) data
						.getSerializableExtra("sponsorCustomer_Bean");
				sponsorCustomerList.add(sponsorCustomer_Bean);
				customerProfile.sponsorCustomerList = sponsorCustomerList;
				listview_item = data.getStringArrayListExtra("Custom");
				if (listview_item != null) {
					// 动态的将联系方式的数据填充到listview中
					AdaptiveListView(MakeListView.ToListviewAdapter(this,
							listview_item, module_Map.get("主要客户")
									.getMyitemview_Lv()), displayMetrics);
				}
				break;
			case 6012:
				Bundle bundle = data.getExtras();
				bundle.setClassLoader(getClassLoader());
				Linkman_information = bundle
						.getParcelableArrayList("Linkman_information_Bean");
				customerProfile.linkMans = Linkman_information;
				listview_item = data
						.getStringArrayListExtra("Linkman_information");
				if (listview_item != null) {
					// 动态的将联系方式的数据填充到listview中
					AdaptiveListView(MakeListView.ToListviewAdapter(this,
							listview_item, module_Map.get("联系人资料")
									.getMyitemview_Lv()), displayMetrics);
				}
				break;
			case 6013:
				markey_information = (CustomerListing) data
						.getSerializableExtra("Markey_information_Bean");
				customerProfile.listing = markey_information;
				listview_item = data
						.getStringArrayListExtra("Markey_information");
				if (listview_item != null) {
					// 动态的将联系方式的数据填充到listview中
					AdaptiveListView(MakeListView.ToListviewAdapter(this,
							listview_item, module_Map.get("上市信息")
									.getMyitemview_Lv()), displayMetrics);
				}
				break;
			case 6005:
				partner = data.getParcelableArrayListExtra("Partner_Bean");
				customerProfile.partnerList = partner;
				listview_item = data
						.getStringArrayListExtra("Partner_Activity");
				if (listview_item != null) {
					// 动态的将联系方式的数据填充到listview中
					AdaptiveListView(MakeListView.ToListviewAdapter(this,
							listview_item, module_Map.get("合伙人")
									.getMyitemview_Lv()), displayMetrics);
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
				listview_item = data
						.getStringArrayListExtra("Relevant_parties_Activity_institution");
				listview_item_bank = data
						.getStringArrayListExtra("Relevant_parties_Activity_bank_list");
				if (listview_item != null) {
					// 动态的将联系方式的数据填充到listview中
					AdaptiveListView(MakeListView.ToListviewAdapter(this,
							listview_item, module_Map.get("相关当事人")
									.getMyitemview_Lv()), displayMetrics);
				}
				break;
			case 6029:
				// 地区
				AreaInfo areaInfo = (AreaInfo) data
						.getSerializableExtra("Area_Survey_Input_Bean");
				listview_item = data
						.getStringArrayListExtra("Area_survey_Activity");
				if (listview_item != null) {
					// 动态的将联系方式的数据填充到listview中
					AdaptiveListView(MakeListView.ToListviewAdapter(this,
							listview_item, module_Map.get("地区概况")
									.getMyitemview_Lv()), displayMetrics);
				}
				break;
			case 6030:
				// 主要职能部门
				DepartMentsInfo departMentsInfo = (DepartMentsInfo) data
						.getSerializableExtra("Functional_department_Bean");
				listview_item = data
						.getStringArrayListExtra("Functional_department_Activity");
				if (listview_item != null) {
					// 动态的将联系方式的数据填充到listview中
					AdaptiveListView(MakeListView.ToListviewAdapter(this,
							listview_item, module_Map.get("主要职能部门")
									.getMyitemview_Lv()), displayMetrics);
				}
				break;
			case 333:
				// BasicData basicData = (BasicData) data
				// .getSerializableExtra("basic");
				CustomerProfileVo customer = (CustomerProfileVo) data
						.getSerializableExtra("customer");
				if (customer != null) {
					initData(customer);
				}

				// industry.setText(basicData.getIndustry());
				// email.setText(basicData.getEmail());
				// dial.setText(basicData.getDial());
				// org_Intro_content.setText(basicData.getIntrodution());
				//
				// urlToSql = basicData.getUrlToSql();
				// String urlComplete = basicData.getAvatar_urlComplete();
				// System.out.println("从编辑页返回的头像rul----" + urlToSql + "---绝对路径："
				// + urlComplete);
				//
				// if (urlComplete != null && urlToSql != null) {
				// ImageLoader.getInstance().displayImage(urlComplete,
				// org_line1_iv);
				// customer.picLogo = urlToSql;
				// myCustomer.picLogo = urlToSql;//
				// }
				//
				// propertyList = (ArrayList<CustomerPersonalLine>) data
				// .getSerializableExtra("custom");
				// if (propertyList != null) {
				// for (int i = 0; i < propertyList.size(); i++) {
				// CustomerPersonalLine line = propertyList.get(i);
				// if ("1".equals(line.type)) {
				// if (!line.name.equals("")) {
				// MyEditTextView myEditTextView_Text = new MyEditTextView(
				// context);
				// myEditTextView_Text.setText(line.content);
				// myEditTextView_Text.setTextLabel(line.name);
				// data_main_LLL.addView(myEditTextView_Text);
				//
				// }
				// }
				// }
				// }
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
		case OrganizationReqType.ORGANIZATION_REQ_ORG_SAVECUSPROFILE:
			if (object == null) {
				return;
			}
			Map<String, Object> dataMap = (Map<String, Object>) object;
			String msg = (String) dataMap.get("responseData");
			if ("操作成功".equals(msg)) {
				Toast.makeText(this, "保存成功", 0).show();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						finish();
					}
				}, 2000);
			} else {
				Toast.makeText(this, "保存失败，请重试", 0).show();
			}
			break;
		}

	}
}
