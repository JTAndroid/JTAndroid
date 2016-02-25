package com.tr.ui.organization.orgdetails.orgfragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.R;
import com.tr.api.OrganizationReqUtil;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.knowledge.Knowledge2;
import com.tr.model.obj.Connections;
import com.tr.navigate.ENavigate;
import com.tr.ui.common.JointResourceFragment.ResourceType;
import com.tr.ui.common.RelatedResourceActivity;
import com.tr.ui.connections.viewfrg.BaseViewPagerFragment;
import com.tr.ui.knowledge.MyKnowledgeActivity;
import com.tr.ui.organization.adapter.LatestAnnouncementAdapter;
import com.tr.ui.organization.adapter.LatestInformationAdapter;
import com.tr.ui.organization.model.CustomerProfileVo;
import com.tr.ui.organization.model.PushKnowledge;
import com.tr.ui.organization.model.RelatedContacts;
import com.tr.ui.organization.model.RelatedContents;
import com.tr.ui.organization.model.RelatedEvent;
import com.tr.ui.organization.model.RelatedInformation;
import com.tr.ui.organization.model.RelatedKEveContents;
import com.tr.ui.organization.model.RelatedKnoContents;
import com.tr.ui.organization.model.RelatedKnowledge;
import com.tr.ui.organization.model.RelatedOrgContents;
import com.tr.ui.organization.model.RelatedOrganization;
import com.tr.ui.organization.model.finance.CustomerFinanceDetail;
import com.tr.ui.organization.model.hight.CustomerHightInfo;
import com.tr.ui.organization.model.industry.CustomerOrgIndustry;
import com.tr.ui.organization.model.notice.CustomerNotice;
import com.tr.ui.organization.model.param.CustomerOrganizatianParams;
import com.tr.ui.organization.model.parameters.OrganizationDetialsIncomingParameters;
import com.tr.ui.organization.model.peer.CustomerPeerInfo;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.organization.model.profile.CustomerPersonalPlate;
import com.tr.ui.organization.model.profile.CustomerPhone;
import com.tr.ui.organization.model.resource.CustomerDemandCommon;
import com.tr.ui.organization.model.resource.CustomerResource;
import com.tr.ui.organization.model.stock.CustomerStock;
import com.tr.ui.organization.model.stock.CustomerStockList;
import com.tr.ui.organization.model.stock.CustomerTenStock;
import com.tr.ui.widgets.BasicListView2;
import com.tr.ui.widgets.viewpagerheaderscroll.delegate.ScrollViewDelegate;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.http.EAPIConsts.KnoReqType;
import com.utils.http.EAPIConsts.OrganizationReqType;
import com.utils.http.IBindData;

/**
 * 组织详情中的我的主页 资料模块
 * 
 * @author User OrgMyHomePageDataFragment
 * 
 */
public class OrgMyHomePageDataFragment extends BaseViewPagerFragment implements
		OnClickListener, IBindData {
	private static final String TAG = "OrgMyHomePageDataFragment";

	/** 适应框架 */
	private ScrollViewDelegate mScrollViewDelegate = new ScrollViewDelegate();
	private ScrollView mScrollView;

	private Intent intent;

	private RelativeLayout homepage_rl_orgRelativeLayout,
			homepage_newnoticeRelativeLayout, homepage_rl_newsRelativeLayout,
			homepage_investment_intentionsRelativeLayout,
			homepage_financing_intentRelativeLayout,
			homepage_experts_demandRelativeLayout, homepage_experts_identity,
			homepage_senior_managementRelativeLayout,
			homepage_shareholders_researchRelativeLayout,
			homepage_financial_analysisRelativeLayout,
			homepage_peer_competition, homepage_industryNewsRelativeLayout,
			homepage_ResearchreportRelativeLayout,
			homepage_area_overviewRelativeLayout,
			homepage_main_sectorsRelativeLayout
			;
	 private LinearLayout listingLinearLayout,stockLinearLayout;
	private View view,stock_line_view,listing_line_view;
	// 从组织详情传过来的数据
	private CustomerOrganizatianParams org_customer;
	private ArrayList<CustomerNotice> customerNoticeList;
	private ArrayList<PushKnowledge> pushKnowledgeNoticeList;
	private CustomerResource resource;
	private RelatedInformation relatedInformation;

	private TextView org_full_name,// 组织全称
			org_short_name,// 组织简称
			org_type,// 组织类型
			org_industry,// 组织行业
			listingInformationTv,// 上市信息
			stockNumTv,// 证卷代码
			contactPhoneTv,// 联系电话
			org_emial,// 组织邮箱
			brief_introduction_contant,//简介

			investment_areaTypeTv,// 投资:地区
			investment_industryNamesTv,// 投资:行业
			investment_typeNames,// 投资:类型
			cuinvestment_stomText,// 投资:自定义字段
			investment_customContentTv,// 投资:自定义内容
			investment_custom_largeTv,// 大文本的自定义字段
			investment_DescriptionTv,// 投资:描述内容

			financing_areaTypeTv,// 融资:地区
			financing_industryNamesTv,// 融资:行业
			financing_typeNames,// 融资:类型
			financing_cuinvestment_stomText,// 融资:自定义字段
			financing_customContentTv,// 融资:自定义内容
			financing_custom_largeTv,// 大文本的自定义字段
			financing_DescriptionTv,// 融资:描述内容

			experts_demand_areaTypeTv,// 专家需求:地区
			experts_demand_industryNamesTv,// 专家需求:行业
			experts_demand_typeNames,// 专家需求:类型
			experts_demand_cuinvestment_stomText,// 专家需求:自定义字段
			experts_demand_customContentTv,// 专家需求:自定义内容
			experts_demand_custom_largeTv,// 大文本的自定义字段
			experts_demand_DescriptionTv,// 专家需求:描述内容

			expert_status_areaTypeTv,// 专家身份:地区
			expert_status_industryNamesTv,// 专家身份:行业
			expert_status_typeNames,// 专家身份:类型
			expert_status_cuinvestment_stomText,// 专家身份:自定义字段
			expert_status_customContentTv,// 专家身份:自定义内容
			expert_status_custom_largeTv,// 大文本的自定义字段
			expert_status_DescriptionTv// 专家身份:描述内容
				;	
	private Map<String, Object> map;

	public String[] types = { "金融机构", "一般企业", "政府组织", "中介机构", "专业媒体", "期刊报纸",
			"研究机构", "电视广播", "互联网媒体", "通用类型" };// 客户类型 1.金融机构 2一般企业 3.政府组织 4.中介机构
												// 5.专业媒体 6.期刊报纸 7.研究机构 8.电视广播
												// 9.互联网媒体,10.通用类型

	public String[] isListings = { "非上市公司", "上市公司" };// 是否上市 是 否

	private ListView latestAnnouncementListView, latestInformationListView;

	private LatestAnnouncementAdapter announcementAdapter;// 最新公告adapter

	private LatestInformationAdapter informationAdapter;// 最新咨询adapter
	/** 投资意向 */
	private List<CustomerDemandCommon> investmentdemandList;
	/** 融资意向 */
	private List<CustomerDemandCommon> financingdemandList;
	/** 专家需求 */
	private List<CustomerDemandCommon> expertdemandList;
	/** 专家身份 */
	private List<CustomerDemandCommon> expertIdentitydemandList;

	public static final int REQUEST_CODE_KNOWLEDGE_CONTENT_ACTIVITY = 1001;
	public static final int REQUEST_CODE_RELATED_RESOURCE_ACTIVITY = 1002;
	public static final int REQUEST_CODE_GLOBAL_KNOWLEDGE_CATEGORY_ACTIVITY = 1003;
	public static final int REQUEST_CODE_GLOBAL_KNOWLEDGE_TAG_ACTIVITY = 1004;
	public static final int REQUEST_CODE_KNOWLEDGE_PERMISSION_ACTIVITY = 1005;

	public static final int REQUEST_CODE_RELATED_RESOURCE_PEOPLE = 2001;
	public static final int REQUEST_CODE_RELATED_RESOURCE_ORGANIZATION = 2002;
	public static final int REQUEST_CODE_RELATED_RESOURCE_KNOWLEDGE = 2003;
	public static final int REQUEST_CODE_RELATED_RESOURCE_AFFAIR = 2004;

	public static final int ORGANIZATION_DEATILS_CODE = 2011;

	public static final int STATE_ADD = 0;
	public static final int STATE_EDIT = 1;

	public int currentRequestCode = 0;
	public int currentRequestState = STATE_ADD;
	public int currentRequestEditPosition = -1;
	/**
	 * 视频/图片
	 */
	public static final int REQUEST_CODE_BROWERS_ACTIVITY = 1001; // 启动关联的回调
	private Knowledge2 knowledge2;
	private int requestCode = 0;
	private ArrayList<Connections> listHightPermission;
	private ArrayList<Connections> listMiddlePermission;
	private ArrayList<Connections> listLowPermission;
	private static String decollatorStr = "、";
	private LinearLayout customContainer;
	private long userId;
	
	// 组织：关联 人脉的集合
	private ArrayList<RelatedContacts> contactsList;
	// 组织：关联 组织的集合
	private ArrayList<RelatedOrganization> organizationList;
	// 组织：关联 知识的集合
	private ArrayList<RelatedKnowledge> knowledgeList;
	// 组织：关联 事件的集合
	private ArrayList<RelatedEvent> eventList;
	// 财务分析
	private CustomerFinanceDetail customerFinanceDetail;
	// 股东研究
	private CustomerStock customerStock;
	// 是大股东列表
	private ArrayList<CustomerTenStock> customerTenStockList;
	// 流通股东
	private ArrayList<CustomerStockList> customerLtStockList;
	// 查询同业竞争详情
	private ArrayList<CustomerPeerInfo> peerList;
	// 查询高层治理
	private ArrayList<CustomerHightInfo> dshList;
	private ArrayList<CustomerHightInfo> jshList;
	private ArrayList<CustomerHightInfo> ggList;
	private ArrayList<CustomerHightInfo> ggjzList;
	//4.16 查询行业动态
	private ArrayList<CustomerOrgIndustry> customerOrgIndustryList;
	//4.22查询研究报告
	private ArrayList<CustomerPersonalLine> customerPersonalLineList;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.org_myhomepage_data_fragment_view,
				container, false);
		
		customContainer = (LinearLayout) view.findViewById(R.id.customContainer);
		
		if (getArguments().containsKey("userId")) {
			userId = getArguments().getLong("userId", -1);
			Log.v("DATA", "用户的地址--->"+userId);
		} else if (getArguments().containsKey("customerId")) {
			userId = getArguments().getLong("customerId", -1);
			Log.v("DATA", "组织的地址--->"+userId);
		}
		
		knowledge2 = new Knowledge2();
		// 权限
		listHightPermission = new ArrayList<Connections>();
		listMiddlePermission = new ArrayList<Connections>();
		listLowPermission = new ArrayList<Connections>();

		initUI();

		return view;
	}
	
	//再次进入界面是会自动请求接口，刷新数据
	@Override
	public void onStart() {
		super.onStart();
		requestOrgJson();
	}


	public void initUI() {

		mScrollView = (ScrollView) view
				.findViewById(R.id.org_myhomepage_ScrollView);
		//证券代码LinearLayout
		stockLinearLayout = (LinearLayout) view.findViewById(R.id.stockLinearLayout);
		stock_line_view = view.findViewById(R.id.stock_line_view);
		//政府组织
		listingLinearLayout = (LinearLayout) view.findViewById(R.id.listingLinearLayout);
		listing_line_view = view.findViewById(R.id.stock_line_view);
		
		
		// 最新报告
//		homepage_newnoticeRelativeLayout = (RelativeLayout) view
//				.findViewById(R.id.homepage_newnoticeRelativeLayout);
//		homepage_newnoticeRelativeLayout.setOnClickListener(this);
//		
//		// 最新资讯
//		homepage_rl_newsRelativeLayout = (RelativeLayout) view
//				.findViewById(R.id.homepage_rl_newsRelativeLayout);
//		homepage_rl_newsRelativeLayout.setOnClickListener(this);
//		
//		// 投资意向
//		homepage_investment_intentionsRelativeLayout = (RelativeLayout) view
//				.findViewById(R.id.homepage_investment_intentionsRelativeLayout);

		// 融资意向
		// homepage_financing_intentRelativeLayout = (RelativeLayout) view
		// .findViewById(R.id.homepage_financing_intentRelativeLayout);
		// homepage_financing_intentRelativeLayout.setOnClickListener(this);
		// // 专家需求
		// homepage_experts_demandRelativeLayout = (RelativeLayout) view
		// .findViewById(R.id.homepage_experts_demandRelativeLayout);
		// homepage_experts_demandRelativeLayout.setOnClickListener(this);
		// // 专家身份
		// homepage_experts_identity = (RelativeLayout) view
		// .findViewById(R.id.homepage_experts_identity);
		// homepage_experts_identity.setOnClickListener(this);
		// // 高层治理
		// homepage_senior_managementRelativeLayout = (RelativeLayout) view
		// .findViewById(R.id.homepage_senior_managementRelativeLayout);
		// homepage_senior_managementRelativeLayout.setOnClickListener(this);
		// 股东研究
//		homepage_shareholders_researchRelativeLayout = (RelativeLayout) view
//				.findViewById(R.id.homepage_shareholders_researchRelativeLayout);
//		homepage_shareholders_researchRelativeLayout.setOnClickListener(this);
//		// 财务分析
//		homepage_financial_analysisRelativeLayout = (RelativeLayout) view
//				.findViewById(R.id.homepage_financial_analysisRelativeLayout);
//		homepage_financial_analysisRelativeLayout.setOnClickListener(this);
//		// 同行竞争
//		homepage_peer_competition = (RelativeLayout) view
//				.findViewById(R.id.homepage_peer_competition);
//
//		// 行业动态
//		homepage_industryNewsRelativeLayout = (RelativeLayout) view
//				.findViewById(R.id.homepage_industryNewsRelativeLayout);
//		homepage_industryNewsRelativeLayout.setOnClickListener(this);
//		// 研究报告
//		homepage_ResearchreportRelativeLayout = (RelativeLayout) view
//				.findViewById(R.id.homepage_ResearchreportRelativeLayout);
//		homepage_ResearchreportRelativeLayout.setOnClickListener(this);
//		// 地区概况
//		homepage_area_overviewRelativeLayout = (RelativeLayout) view
//				.findViewById(R.id.homepage_area_overviewRelativeLayout);
//		homepage_area_overviewRelativeLayout.setOnClickListener(this);
//		// 主要职能部门
//		homepage_main_sectorsRelativeLayout = (RelativeLayout) view
//				.findViewById(R.id.homepage_main_sectorsRelativeLayout);
//		homepage_main_sectorsRelativeLayout.setOnClickListener(this);
	}

	public static OrgMyHomePageDataFragment newInstance(int index) {
		OrgMyHomePageDataFragment fragment = new OrgMyHomePageDataFragment();
		Bundle args = new Bundle();
		args.putInt(BUNDLE_FRAGMENT_INDEX, index);
		fragment.setArguments(args);
		return fragment;
	}
	
	public void requestOrgJson() {
		// 参数二：IbindData的实现类
		OrganizationDetialsIncomingParameters orgParameters = new OrganizationDetialsIncomingParameters();
		
		if (getArguments().containsKey("userId")) {
			orgParameters.userId = userId;
		} else if (getArguments().containsKey("customerId")) {
			orgParameters.orgId = userId;
		}
		
		
		OrganizationReqUtil.doRequestWebAPI(getActivity(), this, orgParameters,
				null, OrganizationReqType.ORGANIZATION_REQ_ORGANDPROINFO);
		
		//子模块在迭代里面做，暂时隐藏	
		
//		// 组织详情页面4.11 按年份，报表类型,季度，获取财务分析详情
//		FinancialAnalysisParameters financialAnalysisParameters = new FinancialAnalysisParameters();
//		OrganizationReqUtil.doRequestWebAPI(getActivity(), this,
//				financialAnalysisParameters, null,
//				OrganizationReqType.ORGANIZATION_REQ_DETAILS);
//
//		// 4.12高层治理
//		CustomerHight_re customerHight_re = new CustomerHight_re();
//		OrganizationReqUtil.doRequestWebAPI(getActivity(), this,
//				customerHight_re, null,
//				OrganizationReqType.ORGANIZATION_REQ_FINDHEGHTONE);
//		
//		// 4.14查询股东研究
//		CustomerStockParameters customerStockParameters = new CustomerStockParameters();
//		OrganizationReqUtil.doRequestWebAPI(getActivity(), this,
//				customerStockParameters, null,
//				OrganizationReqType.ORGANIZATION_REQ_FINDSTOCKONE);
//		
//		//4.16查询行业动态
//		CustomerOrgIndustryParams customerOrgIndustryParams = new CustomerOrgIndustryParams();
////		customerOrgIndustryParams.customerId = userId;
//		customerOrgIndustryParams.customerId = "1";
//		OrganizationReqUtil.doRequestWebAPI(getActivity(), this,
//				customerOrgIndustryParams, null,
//				OrganizationReqType.ORGANIZATION_REQ_FINDINDUSTRY);
//		
//
//		// 4.19 查询同业竞争列表 customer/peer/findPeer.json
//		CustomerPeerInfoParameters customerPeerInfoParameters = new CustomerPeerInfoParameters();
//		customerPeerInfoParameters.customerId = 1 + "";
//		OrganizationReqUtil.doRequestWebAPI(getActivity(), this,
//				customerPeerInfoParameters, null,
//				OrganizationReqType.ORGANIZATION_REQ_FINDPER);
//		
//		//4.22 查询研究报告
//		CustomerPersonalLineParams customerPersonalLineParams = new CustomerPersonalLineParams();
//		customerPersonalLineParams.customerId = "1";
//		OrganizationReqUtil.doRequestWebAPI(getActivity(), this,
//				customerPersonalLineParams, null,
//				OrganizationReqType.ORGANIZATION_REQ_FINDREPORT);
		
	}

	@Override
	public boolean isViewBeingDragged(MotionEvent event) {
		return mScrollViewDelegate.isViewBeingDragged(event, mScrollView);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		
		// 最新报告
//		case R.id.homepage_newnoticeRelativeLayout:
//
//			intent = new Intent(getActivity(), NewNoticeActivity.class);
//
//			startActivityForResult(intent, 1002);
//
//			break;
//		// 最新资讯
//		case R.id.homepage_rl_newsRelativeLayout:
//
//			intent = new Intent(getActivity(), News_Activity.class);
//
//			startActivityForResult(intent, 1003);
//
//			break;
//		// 融资需求
//		case R.id.homepage_investment_intentionsRelativeLayout:
//
//			intent = new Intent(getActivity(), Resource_needs_Activity.class);
//
//			intent.putExtra("resource", resource);
//
//			startActivityForResult(intent, 1111);
//
//			break;
		// // 融资意向
		// case R.id.homepage_financing_intentRelativeLayout:
		//
		// intent = new Intent(getActivity(), OrgFinancingActivity.class);
		//
		// intent.putExtra("ModuleName", "融资意向");
		//
		// intent.putExtra("customer", customer);
		//
		// startActivityForResult(intent, 1005);
		//
		// break;
		// // 专家需求
		// case R.id.homepage_experts_demandRelativeLayout:
		//
		// intent = new Intent(getActivity(), OrgFinancingActivity.class);
		//
		// intent.putExtra("ModuleName", "专家需求");
		//
		// intent.putExtra("customer", customer);
		//
		// startActivityForResult(intent, 1006);
		//
		// break;
		// // 专家身份
		// case R.id.homepage_experts_identity:
		//
		// intent = new Intent(getActivity(), OrgFinancingActivity.class);
		//
		// intent.putExtra("ModuleName", "专家身份");
		//
		// startActivityForResult(intent, 1007);
		//
		// break;
		// 高层治理
//		case R.id.homepage_senior_managementRelativeLayout:
//
//			intent = new Intent(getActivity(),
//					Edit_Seniormanagement_Activity.class);
//
//			startActivityForResult(intent, 1008);
//			break;
//		// 股东研究
//		case R.id.homepage_shareholders_researchRelativeLayout:
//
//			intent = new Intent(getActivity(), Edit_Shareholder_Activity.class);
//
//			startActivityForResult(intent, 1009);
//
//			break;
//		// 财务分析
//		case R.id.homepage_financial_analysisRelativeLayout:
//
//			// intent = new Intent(getActivity(),
//			// Edit_Seniormanagement_Activity.class);
//			//
//			// startActivityForResult(intent, 1010);
//			break;
//		// 同行竞争
//		case R.id.homepage_peer_competition:
//
//			intent = new Intent(getActivity(), News_zheda_Activity.class);
//
//			startActivityForResult(intent, 1011);
//
//			break;
//		// 行业动态
//		case R.id.homepage_industryNewsRelativeLayout:
//
//			intent = new Intent(getActivity(), Hight_Subject_Activity.class);
//
//			startActivityForResult(intent, 1012);
//
//			break;
//		// 研究报告
//		case R.id.homepage_ResearchreportRelativeLayout:
//
//			intent = new Intent(getActivity(), Custom_Activity.class);
//
//			startActivityForResult(intent, 1013);
//
//			break;
//		// 地区概况
//		case R.id.homepage_area_overviewRelativeLayout:
//			break;
//		// 主要职能部门
//		case R.id.homepage_main_sectorsRelativeLayout:
//			break;

		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		Log.v(TAG, "接受从关联界面返回的数据1111");

		if (requestCode == ORGANIZATION_DEATILS_CODE) { //

			Log.v("TAG", "接受从关联界面返回的数据2222");

			// 关联资源
			if (resultCode == Activity.RESULT_OK) {

				Log.v("TAG", "接受从关联界面返回的数据3333");

				if (data.hasExtra(EConsts.Key.RELATED_PEOPLE_NODE)) {
					// 数据去重
					ConnectionNode connectionNode = (ConnectionNode) data
							.getSerializableExtra(EConsts.Key.RELATED_PEOPLE_NODE);
					// 知识返回的集合 start
					ArrayList<Connections> connectionsList = connectionNode
							.getListConnections();

					// 知识返回的集合 end

					// 组织的类
					// 知识返回的集合 start

					// 组织详情：关联人脉的最外层的集合

					RelatedContacts relatedContacts = new RelatedContacts();

					// List<RelatedContents> relatedContactsList =
					// relatedContacts.conn;
					ArrayList<RelatedContents> orgContentsList = new ArrayList<RelatedContents>();

					// 转换数据 start
					// 设置组织的标签

					relatedContacts.tag = connectionNode.getMemo();

					// 知识返回的集合 end
					for (int i = 0; i < connectionsList.size(); i++) {

						Connections connections = connectionsList.get(i);

						RelatedContents relatedContents = new RelatedContents();

						relatedContents.id = connections.getId();

						relatedContents.name = connections.getName();

						orgContentsList.add(relatedContents);

					}
					// 放到次层集合List<RelatedContents>
					relatedContacts.conn = orgContentsList;

					// 转换数据 end

					if (currentRequestState == STATE_EDIT
							& currentRequestCode == REQUEST_CODE_RELATED_RESOURCE_PEOPLE) {
						// 放到最外层的集合List<RelatedContacts>里面
						// Log.v("", "");
						// currentRequestEditPosition
						contactsList.set(currentRequestEditPosition,
								relatedContacts);

					} else {

						contactsList.add(relatedContacts);

					}


				}

				// 相关资源
				// if (data.hasExtra(EConsts.Key.RELATED_ORGANIZATION_NODE)) {
				// // 数据去重
				// ConnectionNode connectionNode = (ConnectionNode) data
				// .getSerializableExtra(EConsts.Key.RELATED_ORGANIZATION_NODE);
				// ArrayList<ConnectionNode> connectionNodeList = knowledge2
				// .getListRelatedOrganizationNode();
				//
				// if (currentRequestState == STATE_EDIT
				// & currentRequestCode ==
				// REQUEST_CODE_RELATED_RESOURCE_ORGANIZATION) {
				// connectionNodeList.set(currentRequestEditPosition,
				// connectionNode);
				// } else {
				// // 加入列表
				// connectionNodeList.add(connectionNode);
				// }
				// organization_Ll.setVisibility(View.VISIBLE);
				//
				// //
				// organizationGroupAdapter.setListRelatedConnectionsNode(knowledge2.getListRelatedOrganizationNode());
				//
				//
				// organizationGroupAdapter.notifyDataSetChanged();
				// }

				// if (data.hasExtra(EConsts.Key.RELATED_KNOWLEDGE_NODE)) {
				// // 数据去重
				// KnowledgeNode knowledgeNode = (KnowledgeNode) data
				// .getSerializableExtra(EConsts.Key.RELATED_KNOWLEDGE_NODE);
				// ArrayList<KnowledgeNode> knowledgeNodeList = knowledge2
				// .getListRelatedKnowledgeNode();
				//
				// if (currentRequestState == STATE_EDIT
				// & currentRequestCode ==
				// REQUEST_CODE_RELATED_RESOURCE_KNOWLEDGE) {
				// knowledgeNodeList.set(currentRequestEditPosition,
				// knowledgeNode);
				// } else {
				// knowledgeNodeList.add(knowledgeNode);
				// }
				// knowledge_Ll.setVisibility(View.VISIBLE);
				// // knowledgeGroupAdapter
				// // .setListRelatedKnowledgeNode(knowledge2
				// // .getListRelatedKnowledgeNode());
				// knowledgeGroupAdapter.notifyDataSetChanged();
				// }
				//
				// if (data.hasExtra(EConsts.Key.RELATED_AFFAIR_NODE)) {
				// // 数据去重\
				//
				// AffairNode affairNode = (AffairNode) data
				// .getSerializableExtra(EConsts.Key.RELATED_AFFAIR_NODE);
				// ArrayList<AffairNode> affairNodeList = knowledge2
				// .getListRelatedAffairNode();
				//
				// if (currentRequestState == STATE_EDIT
				// & currentRequestCode == REQUEST_CODE_RELATED_RESOURCE_AFFAIR)
				// {
				// affairNodeList.set(currentRequestEditPosition,
				// affairNode);
				// } else {
				// affairNodeList.add(affairNode);
				// }
				// requirement_Ll.setVisibility(View.VISIBLE);
				// requirementGroupAdapter
				// .setListRelatedAffairNode(knowledge2
				// .getListRelatedAffairNode());
				// requirementGroupAdapter.notifyDataSetChanged();
				// }

			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/**
	 * 添加自定义项
	 */
	private void showCustomItem(ArrayList<CustomerPersonalLine> propertyList) {
		try {
			int count = propertyList.size();
			customContainer.removeAllViews();
			for (int i = 0; i < count; i++) {
				View customTagView = LayoutInflater.from(getActivity()).inflate(
						R.layout.people_contactsdetail_custom_item_viewl, null);
				TextView nameLabel = (TextView) customTagView
						.findViewById(R.id.name_label);
				TextView contentView = (TextView) customTagView
						.findViewById(R.id.content);
				customContainer.addView(customTagView);

				CustomerPersonalLine customerPersonalLine = propertyList.get(i);
				if (!TextUtils.isEmpty(customerPersonalLine.name)) {
					nameLabel.setText(customerPersonalLine.name);
				}
				if (!TextUtils.isEmpty(customerPersonalLine.content)) {
					contentView.setText(customerPersonalLine.content);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	

	// 网络请求返回的数据都在bindData接口中
	@SuppressWarnings("unchecked")
	@Override
	public void bindData(int tag, Object object) {

		switch (tag) {

		case OrganizationReqType.ORGANIZATION_REQ_ORGANDPROINFO:// 查询组织详情4.4
			// Log.v("TAG", "进入1");

			if (object == null) {
				// Log.v("TAG", "进入2");
				return;
			}
			// Log.v("TAG", "进入3");

			map = (Map<String, Object>) object;

			org_customer = (CustomerOrganizatianParams) map.get("customer_organization_params");
			
			// 组织资料
			if (org_customer != null) {
				if (org_customer.customer!=null) {
					
				// Log.v("TAG", customer.toString());

				// 给组织详情添加数据
				// Log.v("TAG", "公司全称---->" + customer.name);
				org_full_name = (TextView) view
						.findViewById(R.id.org_full_name);
				org_full_name.setText(org_customer.customer.name);

				org_short_name = (TextView) view
						.findViewById(R.id.org_short_name);
				org_short_name.setText(org_customer.customer.shotName);

				org_type = (TextView) view.findViewById(R.id.org_type);
				Log.v("DATA", "customer.type--->"+org_customer.customer.type);
				//组织类型
				if (!TextUtils.isEmpty(org_customer.customer.type)) {
					if(org_customer.customer.type.equals("1")){
						org_type.setText(types[0]);
					}else if(org_customer.customer.type.equals("2")){
						org_type.setText(types[1]);
					}else if(org_customer.customer.type.equals("3")){
						org_type.setText(types[2]);
						listingLinearLayout.setVisibility(View.GONE);  
						listing_line_view.setVisibility(View.GONE);  
						stockLinearLayout.setVisibility(View.GONE);  
						stock_line_view.setVisibility(View.GONE); 
					}else if(org_customer.customer.type.equals("4")){
						org_type.setText(types[3]);
						
					}else if(org_customer.customer.type.equals("5")){
						org_type.setText(types[4]);
					}else if(org_customer.customer.type.equals("6")){
						org_type.setText(types[5]);
					}else if(org_customer.customer.type.equals("7")){
						org_type.setText(types[6]);
					}else if(org_customer.customer.type.equals("8")){
						org_type.setText(types[7]);
					}else if(org_customer.customer.type.equals("9")){
						org_type.setText(types[8]);
					}else if(org_customer.customer.type.equals("10")){
						org_type.setText(types[9]);
					}
				}else{
					org_type.setText(types[9]);
				}
				
				//组织行业
				org_industry = (TextView) view.findViewById(R.id.org_industry);
				if (!org_customer.customer.industrys.isEmpty()) {
					if(org_customer.customer.industrys.toString().equals("[(null)]")){
						org_industry.setText("");
					}else{
						
						String industry = org_customer.customer.industrys.toString();
						String industrys = industry.substring(industry.indexOf("[")+1,industry.indexOf("]"));
						org_industry.setText(industrys);
						
					}
					
				}
				
				listingInformationTv = (TextView) view
						.findViewById(R.id.listingInformationTv);
				if (!org_customer.customer.isListing.equals("")) {
					if ("0".equals(org_customer.customer.isListing)) {
						listingInformationTv.setText(isListings[0]);
						
						stockLinearLayout.setVisibility(View.GONE);  
						
						stock_line_view.setVisibility(View.GONE);  
					}else if("1".equals(org_customer.customer.isListing)){
						listingInformationTv.setText(isListings[1]);
					}
					
				}

				stockNumTv = (TextView) view.findViewById(R.id.stockNumTv);
				stockNumTv.setText(org_customer.customer.stockNum);

				contactPhoneTv = (TextView) view
						.findViewById(R.id.contactPhoneTv);
				if("2".equals(org_customer.customer.virtual)){ //0 客户 1 用户注册组织 2 未注册的组织(或者是大数据推送过来的组织（主页底部不显示任何操作）)
					if (org_customer.customer.phoneList != null) {
						ArrayList<CustomerPhone> phoneList = (ArrayList<CustomerPhone>) org_customer.customer.phoneList;
						StringBuilder phone = new StringBuilder("");
						if (phoneList != null && phoneList.size() > 0) {
							for (int i = 0; i < phoneList.size(); i++) {
								CustomerPhone customerPhone = phoneList.get(i);
								if(!TextUtils.isEmpty(customerPhone.areaCode)){
									phone.append(customerPhone.areaCode);
									phone.append(" - ");
								}
								phone.append(customerPhone.phone);
								if(!TextUtils.isEmpty(customerPhone.extension)){
									phone.append(" - ");
									phone.append(customerPhone.extension);
								}
								phone.append("\n");
							}
						}
						contactPhoneTv.setText(phone.toString());
					}
				}else {
					contactPhoneTv.setText(org_customer.customer.linkMobile);
				}

				org_emial = (TextView) view.findViewById(R.id.org_emial);
				org_emial.setText(org_customer.customer.linkEmail);

				brief_introduction_contant = (TextView) view
						.findViewById(R.id.brief_introduction_contant);
				brief_introduction_contant.setText(EUtil.filterHtml(org_customer.customer.discribe));
				
				//自定义文本
				if(org_customer.customer.propertyList != null && org_customer.customer.propertyList.size() > 0){
					Log.v("DATA", "customerPersonalPlateList.......");
					ArrayList<CustomerPersonalLine> propertyList = org_customer.customer.propertyList;
					showCustomItem(propertyList);
					
				}

			} 
			// 最新公告
			customerNoticeList = (ArrayList<CustomerNotice>) map
					.get("noticeList");
			// Log.v("TAG",
			// "customerNoticeList--->" + customerNoticeList.toString());

			if (customerNoticeList != null) {

				announcementAdapter = new LatestAnnouncementAdapter(
						customerNoticeList, getActivity());// 最新公告adapter
//				latestAnnouncementListView = (ListView) view
//						.findViewById(R.id.latestAnnouncementListView);
//				latestAnnouncementListView.setAdapter(announcementAdapter);
				// setListViewHeightBasedOnChildren(latestAnnouncementListView);
			}

			// 最新资讯
			pushKnowledgeNoticeList = (ArrayList<PushKnowledge>) map
					.get("orgNewList");
			// Log.v("TAG", pushKnowledgeNoticeList.toString());
			if (pushKnowledgeNoticeList != null) {

				informationAdapter = new LatestInformationAdapter(
						pushKnowledgeNoticeList, getActivity());// 最新资讯adapter
//				latestInformationListView = (ListView) view
//						.findViewById(R.id.latestInformationListView);
//				latestInformationListView.setAdapter(informationAdapter);
				// setListViewHeightBasedOnChildren(latestInformationListView);

			}

			// 投资/融资/专家需求/专家身份
			resource = (CustomerResource) map.get("result");
//			Log.v("TAG", "投资/融资/专家需求/专家身份---->" + resource);
			if (resource != null) {
//				homepage_investment_intentionsRelativeLayout
//						.setOnClickListener(this);
			}

			// 投资意向：地区
			// investment_areaTypeTv = (TextView) view
			// .findViewById(R.id.investment_areaTypeTv);
			// investment_industryNamesTv = (TextView) view
			// .findViewById(R.id.investment_industryNamesTv);// 投资:行业
			// investment_typeNames = (TextView) view
			// .findViewById(R.id.investment_typeNames);// 投资:类型
			// cuinvestment_stomText = (TextView) view
			// .findViewById(R.id.cuinvestment_stomText);// 投资:自定义字段
			// investment_customContentTv = (TextView) view
			// .findViewById(R.id.investment_customContentTv);// 投资:自定义内容
			// // investment_custom_largeTv,//大文本的自定义字段
			// investment_custom_largeTv = (TextView) view
			// .findViewById(R.id.investment_custom_largeTv);
			//
			// investment_DescriptionTv = (TextView) view
			// .findViewById(R.id.investment_DescriptionTv);// 投资:描述内容
			//
			// financing_areaTypeTv = (TextView) view
			// .findViewById(R.id.financing_areaTypeTv);
			// financing_industryNamesTv = (TextView) view
			// .findViewById(R.id.financing_industryNamesTv);// 融资:行业
			// financing_typeNames = (TextView) view
			// .findViewById(R.id.financing_typeNames);// 融资:类型
			// financing_cuinvestment_stomText = (TextView) view
			// .findViewById(R.id.financing_cuinvestment_stomText);// 融资:自定义字段
			// financing_customContentTv = (TextView) view
			// .findViewById(R.id.financing_customContentTv);// 融资:自定义内容
			// // investment_custom_largeTv,//大文本的自定义字段
			// financing_custom_largeTv = (TextView) view
			// .findViewById(R.id.financing_custom_largeTv);
			//
			// financing_DescriptionTv = (TextView) view
			// .findViewById(R.id.financing_DescriptionTv);// 融资:描述内容
			//
			// experts_demand_areaTypeTv = (TextView) view
			// .findViewById(R.id.experts_demand_areaTypeTv);
			// experts_demand_industryNamesTv = (TextView) view
			// .findViewById(R.id.experts_demand_industryNamesTv);// 专家需求:行业
			// experts_demand_typeNames = (TextView) view
			// .findViewById(R.id.experts_demand_typeNames);// 专家需求:类型
			// experts_demand_cuinvestment_stomText = (TextView) view
			// .findViewById(R.id.experts_demand_cuinvestment_stomText);//
			// 专家需求:自定义字段
			// experts_demand_customContentTv = (TextView) view
			// .findViewById(R.id.experts_demand_customContentTv);// 专家需求:自定义内容
			//
			// // investment_custom_largeTv,//大文本的自定义字段
			// experts_demand_custom_largeTv = (TextView) view
			// .findViewById(R.id.experts_demand_custom_largeTv);
			//
			// experts_demand_DescriptionTv = (TextView) view
			// .findViewById(R.id.experts_demand_DescriptionTv);// 专家需求:描述内容
			//
			// expert_status_areaTypeTv = (TextView) view
			// .findViewById(R.id.expert_status_areaTypeTv);
			// expert_status_industryNamesTv = (TextView) view
			// .findViewById(R.id.expert_status_industryNamesTv);// 专家身份:行业
			// expert_status_typeNames = (TextView) view
			// .findViewById(R.id.expert_status_typeNames);// 专家身份:类型
			// expert_status_cuinvestment_stomText = (TextView) view
			// .findViewById(R.id.expert_status_cuinvestment_stomText);//
			// 专家身份:自定义字段
			// expert_status_customContentTv = (TextView) view
			// .findViewById(R.id.expert_status_customContentTv);// 专家身份:自定义内容
			//
			// // investment_custom_largeTv,//大文本的自定义字段
			// expert_status_custom_largeTv = (TextView) view
			// .findViewById(R.id.expert_status_custom_largeTv);
			//
			// expert_status_DescriptionTv = (TextView) view
			// .findViewById(R.id.expert_status_DescriptionTv);// 专家身份:描述内容
			//
			// homepage_occupation_text = (TextView) view
			// .findViewById(R.id.homepage_occupation_text);
			// homepage_name_text = (TextView) view
			// .findViewById(R.id.homepage_name_text);
			// homepage_author_text = (TextView) view
			// .findViewById(R.id.homepage_author_text);
			// homepage_author_name_text = (TextView) view
			// .findViewById(R.id.homepage_author_name_text);
			// homepage_company_text = (TextView) view
			// .findViewById(R.id.homepage_company_text);
			// homepage_companyname_text = (TextView) view
			// .findViewById(R.id.homepage_companyname_text);
			// homepage_owledge_author_text = (TextView) view
			// .findViewById(R.id.homepage_owledge_author_text);
			// homepage_owledge_name_text = (TextView) view
			// .findViewById(R.id.homepage_owledge_name_text);
			// homepage_event_text = (TextView) view
			// .findViewById(R.id.homepage_event_text);
			// homepage_experts_text = (TextView) view
			// .findViewById(R.id.homepage_experts_text);
			//
			// if (resource != null) {
			// /** 投资意向 */
			// investmentdemandList = resource.investmentdemandList;
			//
			// if (investmentdemandList != null && investmentdemandList.size()
			// != 0) {
			//
			// homepage_investment_intentionsRelativeLayout
			// .setOnClickListener(this);
			//
			// for (int i = 0; i < investmentdemandList.size(); i++) {
			//
			// if (investmentdemandList.get(i).getAddress().getAddress() !=
			// null) {
			// investment_areaTypeTv.setText(investmentdemandList
			// .get(i).getAddress().getAddress());
			// }
			//
			// if (investmentdemandList.get(i).getIndustryNames() != null) {
			//
			// investment_industryNamesTv
			// .setText(investmentdemandList.get(i).getIndustryNames());
			//
			// }
			//
			// if (investmentdemandList.get(i).getTypeNames() != null) {
			//
			// Log.v("TAG",
			// "投资意向数据----->"+investmentdemandList.get(i).getTypeNames().toString());
			//
			// investment_typeNames.setText(investmentdemandList
			// .get(i).getTypeNames());
			// }
			//
			// if(investmentdemandList.get(i).getPersonalLineList() != null &&
			// investmentdemandList.get(i).getPersonalLineList().size() != 0){
			//
			// for(int j = 0;j <
			// investmentdemandList.get(i).getPersonalLineList().size();j++){
			//
			// if (investmentdemandList.get(i).getPersonalLineList().get(j)
			// .getType() != null) {
			//
			// if (investmentdemandList.get(i).getPersonalLineList()
			// .get(j).getType().equals("1")) {
			//
			// if (investmentdemandList.get(i).getPersonalLineList()
			// .get(j).getName() != null) {
			//
			// cuinvestment_stomText.setText(investmentdemandList.get(i).getPersonalLineList().get(j).getName());
			//
			// }
			//
			// if (investmentdemandList.get(i).getPersonalLineList()
			// .get(j).getContent() != null) {
			// investment_customContentTv.setText(investmentdemandList.get(i).getPersonalLineList().get(j).getContent());
			//
			// }
			//
			// } else if (investmentdemandList.get(i).getPersonalLineList()
			// .get(j).getType().equals("2")) {
			//
			// if (investmentdemandList.get(i).getPersonalLineList()
			// .get(j).getName() != null) {
			// investment_custom_largeTv
			// .setText(investmentdemandList
			// .get(i).getPersonalLineList()
			// .get(j).getName());
			//
			// }
			//
			// if (investmentdemandList.get(i).getPersonalLineList()
			// .get(j).getContent() != null) {
			// investment_DescriptionTv
			// .setText(investmentdemandList
			// .get(i).getPersonalLineList()
			// .get(j).getContent());
			//
			// }
			//
			// }
			//
			// }
			//
			// }
			//
			//
			// }
			//
			//
			//
			// }
			//
			// }
			//
			// /** 融资需求 */
			// financingdemandList = resource.financingdemandList;
			//
			// if (financingdemandList != null && financingdemandList.size() !=
			// 0) {
			//
			// homepage_financing_intentRelativeLayout
			// .setOnClickListener(this);
			//
			// for (int i = 0; i < financingdemandList.size(); i++) {
			//
			// if (financingdemandList.get(i).getAddress().getAddress() != null)
			// {
			// financing_areaTypeTv.setText(financingdemandList
			// .get(i).getAddress().getAddress());
			//
			// }
			//
			// if (financingdemandList.get(i).getIndustryNames() != null) {
			//
			// financing_industryNamesTv
			// .setText(financingdemandList.get(i).getIndustryNames());
			//
			// }
			//
			// if (financingdemandList.get(i).getTypeNames() != null) {
			//
			// financing_typeNames.setText(financingdemandList
			// .get(i).getTypeNames());
			//
			// }
			//
			// if(financingdemandList.get(i).getPersonalLineList() != null &&
			// financingdemandList.get(i).getPersonalLineList().size() != 0){
			//
			// for(int k = 0;k <
			// financingdemandList.get(i).getPersonalLineList().size();k++){
			//
			// if
			// (financingdemandList.get(i).getPersonalLineList().get(k).getType()
			// != null) {
			//
			// if
			// (financingdemandList.get(i).getPersonalLineList().get(k).getType().equals("1"))
			// {
			//
			//
			// if
			// (financingdemandList.get(i).getPersonalLineList().get(k).getName()
			// != null) {
			//
			//
			// financing_cuinvestment_stomText
			// .setText(financingdemandList.get(i).getPersonalLineList()
			// .get(k).getName());
			//
			// }
			//
			// if
			// (financingdemandList.get(i).getPersonalLineList().get(i).getContent()
			// != null) {
			//
			// financing_customContentTv
			// .setText(financingdemandList.get(i).getPersonalLineList()
			// .get(k).getContent());
			// }
			//
			// } else if
			// (financingdemandList.get(i).getPersonalLineList().get(k).getType().equals("2"))
			// {
			//
			//
			// if
			// (financingdemandList.get(i).getPersonalLineList().get(k).getName()
			// != null) {
			//
			// financing_custom_largeTv.setText(financingdemandList.get(i).getPersonalLineList().get(i).getName());
			//
			// }
			//
			// if
			// (financingdemandList.get(i).getPersonalLineList().get(k).getContent()
			// != null) {
			//
			// financing_DescriptionTv.setText(financingdemandList.get(i).getPersonalLineList().get(k).getContent());
			//
			// }
			//
			// }
			//
			// }
			//
			//
			//
			// }
			// }
			//
			//
			//
			// }
			//
			// }
			//
			// /** 专家需求 */
			// expertdemandList = resource.expertdemandList;
			// if (financingdemandList != null && financingdemandList.size() !=
			// 0) {
			//
			// homepage_experts_demandRelativeLayout
			// .setOnClickListener(this);
			//
			// for (int i = 0; i < financingdemandList.size(); i++) {
			//
			// if (expertdemandList.get(i).getAddress().getAddress()!= null) {
			//
			// experts_demand_areaTypeTv.setText(expertdemandList
			// .get(i).getAddress().getAddress());
			// }
			//
			// if (expertdemandList.get(i).getIndustryNames() != null) {
			// experts_demand_industryNamesTv
			// .setText(expertdemandList.get(i).getIndustryNames() );
			// }
			//
			// if (expertdemandList.get(i).getTypeNames() != null) {
			//
			// experts_demand_typeNames.setText(expertdemandList
			// .get(i).getTypeNames());
			// }
			//
			// if(expertdemandList.get(i).getPersonalLineList() != null &&
			// expertdemandList.get(i).getPersonalLineList().size() != 0){
			// if (expertdemandList.get(i).getPersonalLineList().get(i)
			// .getType() != null) {
			// if (expertdemandList.get(i).getPersonalLineList().get(i)
			// .getType().equals("1")) {
			//
			// if (expertdemandList.get(i).getPersonalLineList()
			// .get(i).getName() != null) {
			// experts_demand_cuinvestment_stomText
			// .setText(expertdemandList.get(i).getPersonalLineList()
			// .get(i).getName());
			// }
			//
			// if (expertdemandList.get(i).getPersonalLineList()
			// .get(i).getContent() != null) {
			// experts_demand_customContentTv
			// .setText(expertdemandList.get(i).getPersonalLineList()
			// .get(i).getContent());
			//
			// }
			//
			// } else if (expertdemandList.get(i).getPersonalLineList()
			// .get(i).getType().equals("2")) {
			//
			// if (expertdemandList.get(i).getPersonalLineList()
			// .get(i).getName() != null) {
			//
			// experts_demand_custom_largeTv
			// .setText(expertdemandList.get(i).getPersonalLineList()
			// .get(i).getName());
			//
			// }
			//
			// if (expertdemandList.get(i).getPersonalLineList()
			// .get(i).getContent() != null) {
			//
			// experts_demand_DescriptionTv
			// .setText(expertdemandList.get(i).getPersonalLineList()
			// .get(i).getContent());
			//
			// }
			//
			// }
			//
			// }
			// }
			//
			//
			//
			//
			// }
			//
			// }
			//
			// /** 专家身份 */
			// expertIdentitydemandList = resource.expertIdentitydemandList;
			// if (expertIdentitydemandList != null &&
			// expertIdentitydemandList.size() != 0) {
			//
			// homepage_experts_identity.setOnClickListener(this);
			//
			// for (int i = 0; i < expertIdentitydemandList.size(); i++) {
			//
			// if (expertIdentitydemandList.get(i).getAddress().getAddress() !=
			// null) {
			//
			// expert_status_areaTypeTv
			// .setText(expertIdentitydemandList.get(i).getAddress().getAddress());
			// }
			//
			// if (expertIdentitydemandList.get(i).getIndustryNames() != null) {
			// expert_status_industryNamesTv
			// .setText(expertIdentitydemandList.get(i).getIndustryNames());
			// }
			//
			// if (expertIdentitydemandList.get(i).getTypeNames() != null) {
			//
			// expert_status_typeNames
			// .setText(expertIdentitydemandList.get(i).getTypeNames());
			// }
			//
			// if(expertIdentitydemandList.get(i).getPersonalLineList() !=
			// null){
			//
			// if (expertIdentitydemandList.get(i).getPersonalLineList()
			// .get(i).getType() != null) {
			// if (expertIdentitydemandList.get(i).getPersonalLineList()
			// .get(i).getType().equals("1")) {
			//
			// if (expertIdentitydemandList.get(i).getPersonalLineList()
			// .get(i).getName() != null) {
			//
			// expert_status_cuinvestment_stomText
			// .setText(expertIdentitydemandList
			// .get(i).getPersonalLineList()
			// .get(i).getName());
			//
			// }
			//
			// if (expertIdentitydemandList.get(i).getPersonalLineList()
			// .get(i).getContent() != null) {
			// expert_status_customContentTv
			// .setText(expertIdentitydemandList
			// .get(i).getPersonalLineList()
			// .get(i).getContent());
			//
			// }
			//
			// } else if (expertIdentitydemandList.get(i).getPersonalLineList()
			// .get(i).getType().equals("2")) {
			//
			// if (expertIdentitydemandList.get(i).getPersonalLineList()
			// .get(i).getName() != null) {
			//
			// expert_status_custom_largeTv
			// .setText(expertIdentitydemandList
			// .get(i).getPersonalLineList()
			// .get(i).getName());
			//
			// }
			//
			// if (expertIdentitydemandList.get(i).getPersonalLineList()
			// .get(i).getContent() != null) {
			//
			// expert_status_DescriptionTv
			// .setText(expertIdentitydemandList
			// .get(i).getPersonalLineList()
			// .get(i).getContent());
			//
			// }
			//
			// }
			//
			// }
			// }
			//
			//
			// }
			//
			// }
			//
			// }

			//关联关系
			relatedInformation = (RelatedInformation) map.get("relevance");
//			Log.v("TAG", "关联信息数据---->" + relatedInformation.toString());

			// Log.v("TAG", resource.toString());
			//
			// Log.v("TAG", relatedInformation.toString());
			}
			break;

		case OrganizationReqType.ORGANIZATION_REQ_DETAILS:// 4.11按年份，报表类型,季度，获取财务分析详情

			// Log.v("TAG", "进入5");

			if (object == null) {
				// Log.v("TAG", "进入6");
				return;
			}
			// Log.v("TAG", "进入7");

			map = (Map<String, Object>) object;

			customerFinanceDetail = (CustomerFinanceDetail) map.get("listRow");

//			Log.v("TAG", "财务分析---->" + customerFinanceDetail.toString());

			break;

		case OrganizationReqType.ORGANIZATION_REQ_FINDHEGHTONE:// 4.12查询高层治理
			if (object == null) {
				return;
			}
			
			map = (Map<String, Object>) object;
			// 查询高层治理里面的4个数据集合
			dshList = (ArrayList<CustomerHightInfo>) map.get("dshList");
//			Log.v("TAG", "查询高层治理<----dshList---->" + dshList);
			jshList = (ArrayList<CustomerHightInfo>) map.get("jshList");
//			Log.v("TAG", "查询高层治理<----jshList---->" + dshList);
			ggList = (ArrayList<CustomerHightInfo>) map.get("ggList");
//			Log.v("TAG", "查询高层治理<----ggList---->" + dshList);
			ggjzList = (ArrayList<CustomerHightInfo>) map.get("ggjzList");
//			Log.v("TAG", "查询高层治理<----ggjzList---->" + dshList);

			break;

		case OrganizationReqType.ORGANIZATION_REQ_FINDSTOCKONE:// 4.14查询股东研究

			if (object == null) {
				return;
			}

			map = (Map<String, Object>) object;
			
			// 股东研究
			customerStock = (CustomerStock) map.get("stock");
//			Log.v("TAG", "股东研究---->" + customerStock.toString());

			// 流通股东
			customerTenStockList = (ArrayList<CustomerTenStock>) map
					.get("tenStockList");

//			Log.v("TAG", "流通股东---->" + customerTenStockList.toString());

			// 十大股东
			customerLtStockList = (ArrayList<CustomerStockList>) map
					.get("ltStockList");
//			Log.v("TAG", "十大股东---->" + customerLtStockList.toString());

			break;
			
		case OrganizationReqType.ORGANIZATION_REQ_FINDINDUSTRY://4.16 查询行业动态
			
			if (object == null) {
				return;
			}

			map = (Map<String, Object>) object;

			customerOrgIndustryList = (ArrayList<CustomerOrgIndustry>) map.get("peerList");

//			Log.v("TAG", "查询行业动态---->" + customerOrgIndustryList.toString());

			if (peerList != null) {
				// 点击行业动态跳转界面
				homepage_industryNewsRelativeLayout.setOnClickListener(this);

			}
			
			break;
			
		case OrganizationReqType.ORGANIZATION_REQ_FINDPER:// 4.19 查询同业竞争列表
															// customer/peer/findPeer.json
			if (object == null) {
				return;
			}

			map = (Map<String, Object>) object;

			peerList = (ArrayList<CustomerPeerInfo>) map.get("peerList");

//			Log.v("TAG", "同业竞争---->" + peerList.toString());

			if (peerList != null) {
				// 点击同业竞争跳转界面
				homepage_peer_competition.setOnClickListener(this);

			}

			break;
			
		case OrganizationReqType.ORGANIZATION_REQ_FINDREPORT://4.22 查询研究报告
			
			if (object == null) {
				return;
			}

			map = (Map<String, Object>) object;
			
			customerPersonalLineList = (ArrayList<CustomerPersonalLine>) map.get("personalLineList");
			
			
			break;

		// 发布知识
		case KnoReqType.CreateKnowledge: {
			if (object == null) {
				return;
			}

			Map<String, Object> dataHm = (Map<String, Object>) object;
			Knowledge2 knowledge = (Knowledge2) dataHm.get("knowledge2");
			if (knowledge != null) {
				if (requestCode == MyKnowledgeActivity.REQUESTCODE_CREATE_KNOWLEDGE_ACTIVITY) {
					ENavigate
							.startKnowledgeOfDetailActivityForResult(
									getActivity(),
									knowledge,
									"MyKnowledgeActivity",
									MyKnowledgeActivity.REQUESTCODE_CREATE_KNOWLEDGE_ACTIVITY);

				} else {
					ENavigate.startKnowledgeOfDetailActivity(getActivity(),
							knowledge);
				}
			}
		}
			break;

		}

		

	}


	public void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1))
				- 45;
		listView.getDividerHeight();
		listView.setLayoutParams(params);
	}

}
