package com.tr.ui.organization.create_clientele;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.constvalue.EnumConst.ModuleType;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.App;
import com.tr.R;
import com.tr.api.OrganizationReqUtil;
import com.tr.model.demand.ASSOData;
import com.tr.model.demand.ASSORPOK;
import com.tr.model.demand.DemandASSO;
import com.tr.model.demand.DemandASSOData;
import com.tr.model.demand.ImageItem;
import com.tr.model.demand.LableData;
import com.tr.model.demand.Metadata;
import com.tr.model.demand.NoteData;
import com.tr.model.joint.AffairNode;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.joint.KnowledgeNode;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.knowledge.UserCategory;
import com.tr.model.obj.AffairsMini;
import com.tr.model.obj.Connections;
import com.tr.model.obj.JTFile;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.common.JointResourceFragment.ResourceType;
import com.tr.ui.demand.CreateLabelActivity;
import com.tr.ui.demand.util.ChooseDataUtil;
import com.tr.ui.demand.util.TextStrUtil;
import com.tr.ui.knowledge.CreateKnowledgeActivity;
import com.tr.ui.knowledge.PermissionActivity;
import com.tr.ui.organization.firstpage.BusinessAnalysisActivity;
import com.tr.ui.organization.firstpage.FinancialProductsActivity;
import com.tr.ui.organization.model.Area;
import com.tr.ui.organization.model.CustomerTag;
import com.tr.ui.organization.model.GetId;
import com.tr.ui.organization.model.government.AreaInfo;
import com.tr.ui.organization.model.government.DepartMents;
import com.tr.ui.organization.model.param.CustomerClientParams;
import com.tr.ui.organization.model.param.CustomerOrganizatianParams;
import com.tr.ui.organization.model.permission.CustomerPermission_Bean;
import com.tr.ui.organization.model.permission.Permission;
import com.tr.ui.organization.model.profile.CustomerBranch;
import com.tr.ui.organization.model.profile.CustomerInfo;
import com.tr.ui.organization.model.profile.CustomerLinkMan;
import com.tr.ui.organization.model.profile.CustomerListing;
import com.tr.ui.organization.model.profile.CustomerPartner;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.organization.model.profile.CustomerPhone;
import com.tr.ui.organization.model.profile.CustomerRemark;
import com.tr.ui.organization.model.template.CustomerColumn_re;
import com.tr.ui.people.cread.BaseActivity;
import com.tr.ui.people.cread.CustomActivity;
import com.tr.ui.people.cread.RemarkActivity;
import com.tr.ui.people.cread.utils.MakeListView;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.tr.ui.people.cread.view.MyitemView;
import com.tr.ui.widgets.BasicListView2;
import com.utils.common.EConsts;
import com.utils.common.OrganizationPictureUploader;
import com.utils.common.OrganizationPictureUploader.OnOrganizationPictureUploadListener;
import com.utils.http.EAPIConsts.OrganizationReqType;
import com.utils.http.IBindData;
import com.utils.log.KeelLog;
import com.utils.log.ToastUtil;
import com.utils.string.StringUtils;

/**
 * 创建客户
 *  启动方法：ENavigate.startCreateClienteleActivity
 * formActicityType : 1 创建 2 其他
 * customer :　客户对象
 * id　：客户ID
 * @author Wxh07151732
 * 
 */
public class CreateClienteleActivity extends BaseActivity implements
		OnClickListener, IBindData, OnItemClickListener,
		OnOrganizationPictureUploadListener {
	/** 左箭头退出*/
	private RelativeLayout quit_org_Rl;
	/** 拍摄名片*/
	private ImageView org_businessCard_Tv; 
	/** 头像*/
	private ImageView picture_org_Iv;
	/** 联系号码*/
	private MyEditTextView org_contact_Etv;
	/** 邮件*/
	private MyEditTextView org_email_Etv;
	/** 上市信息*/
	private MyEditTextView org_market_Etv;
	/** 备注描述*/
	private MyEditTextView org_describe_Etv;
	/** 客户类型*/
	private static MyEditTextView org_type_Etv;
	/** 地区*/
	private MyEditTextView org_address_Etv;
	/** 行业*/
	private MyEditTextView org_industry_Etv;
	/** 详细地址*/
	private MyEditTextView org_detail_address_Etv;
	/** 自定义*/
	private MyEditTextView org_custom_Etv;
	private Intent intent;
	/** 父类Layout*/
	private LinearLayout org_main1_Ll;
	private ArrayList<MyEditTextView> contact_list; // 联系人控件集合
	private MyitemView module; // 模块控件
	private HashMap<String, MyitemView> module_Map; // 模块控件集合，与模块名称相绑定，根据模块名称来确定跳转那个模块
	private RelativeLayout org_Addmore_Rl;
	private LinearLayout org_main_Ll; // 所有控件的父类，但不是基类
	private List<CustomerPhone> contact;
	private ArrayList<String> industry;
	private CustomerInfo customerInfo; // 公司概况对象
	private List<CustomerRemark> sponsorCustomerList;// 自定义对象
	private CustomerRemark publication;// 自定义对象
	private CustomerRemark sponsorCustomer_Bean; // 自定义对象
	private ArrayList<CustomerLinkMan> Linkman_information; // 联系人对象集合
	private CustomerListing markey_information; // 上市信息对象
	private ArrayList<CustomerPartner> partner; // 合伙人对象
	private ArrayList<CustomerBranch> sponsorBranchList; // 分支机构对象
	private CustomerBranch sponsorBank; // 主办银行
	private CustomerBranch lawFirm; // 律师事务所
	private ArrayList<String> listview_item;
	private ArrayList<String> listview_item_bank;
	/** 四大组件 - 关联*/
	private TextView relevance_Tv;
	/** 四大组件 - 目录*/
	private TextView catalogue_Tv;
	/** 四大组件 - 标签*/
	private TextView label_Tv;
	/** 四大组件 - 权限*/
	private TextView jurisdiction_Tv;
	public static final int REQUEST_CODE_RELATED_RESOURCE_ACTIVITY = 1002;
	public static final int REQUEST_CODE_GLOBAL_KNOWLEDGE_CATEGORY_ACTIVITY = 1003;
	public static final int REQUEST_CODE_GLOBAL_KNOWLEDGE_TAG_ACTIVITY = 1004;
	public static final int REQUEST_CODE_KNOWLEDGE_PERMISSION_ACTIVITY = 1005;

	public static final int REQUEST_CODE_RELATED_RESOURCE_PEOPLE = 2001;
	public static final int REQUEST_CODE_RELATED_RESOURCE_ORGANIZATION = 2002;
	public static final int REQUEST_CODE_RELATED_RESOURCE_KNOWLEDGE = 2003;
	public static final int REQUEST_CODE_RELATED_RESOURCE_AFFAIR = 2004;

	public static final int STATE_ADD = 0;
	public static final int STATE_EDIT = 1;

	public int currentRequestCode = 0;
	public int currentRequestState = STATE_ADD;
	public int currentRequestEditPosition = -1;

	private int Type = 0;
	/**
	 * 视频/图片
	 */
	public static final int REQUEST_CODE_BROWERS_ACTIVITY = 1001; // 启动关联的回调
	private static final String TAG = "Create_Clientele_Activity";
	// 存放勾选图片/视频的路径集合
	private ArrayList<ImageItem> selectedPictureSD = new ArrayList<ImageItem>();// 本地
	private ArrayList<ImageItem> selectedPictureSDAndNet = new ArrayList<ImageItem>();// 本地加网络
	private ArrayList<CustomerPersonalLine> propertyList; // 自定义
	private ConnectionsGroupAdapter peopleGroupAdapter;// 展示关联信息的人脉Listview的适配器
	private ConnectionsGroupAdapter organizationGroupAdapter;// 展示关联信息的组织Listview的适配器
	private KnowledgeGroupAdapter knowledgeGroupAdapter;// 展示关联信息的知识Listview的适配器
	private RequirementGroupAdapter requirementGroupAdapter;// 展示关联信息的事件Listview的适配器
	private static String decollatorStr = "、";
	private BasicListView2 people;// 展示关联信息的人脉Listview
	private BasicListView2 organization;// 展示关联信息的组织Listview
	private BasicListView2 knowledge;// 展示关联信息的知识Listview
	private BasicListView2 requirement;// 展示关联信息的事件Listview
	private LinearLayout people_Ll; // 展示关联信息的人脉
	private LinearLayout organization_Ll;// 展示关联信息的组织
	private LinearLayout knowledge_Ll;// 展示关联信息的知识
	private LinearLayout requirement_Ll;// 展示关联信息的事件
	private String keyword; // 四大组件-关联关键字
	private boolean isNull;
	private ArrayList<ConnectionNode> connectionNodeList; // 四大组件框架需要的对象集合
	private ArrayList<ConnectionNode> connectionNodeList2;
	private ArrayList<KnowledgeNode> knowledgeNodeList;
	private ArrayList<AffairNode> affairNodeList;
	private Bundle bundle;
	private ArrayList<String> directory; // 目录集合
	private ArrayList<CustomerTag> lableList; // 标签集合
	private CustomerPermission_Bean customerPermissions; // 权限对象
	private ArrayList<Permission> xiaoles;
	private ArrayList<Permission> dales;
	private ArrayList<Permission> zhongles;
	private boolean CustomnoPermission; // 是否独乐
	private ArrayList<String> modelType;
	private int dataIndex = 0;
	private ArrayList<Metadata> metadataIndustry;// 行业
	private ArrayList<Metadata> metadataArea;// 地区
	private NoteData noteData;
	private ArrayList<String> listview_item_law;
	private ArrayList<String> relevant; // 相关当事人的返回值
	private MyEditTextView org_shortname_Etv;
	private MyEditTextView org_name_Etv;
	private int label = 0;
	private MyEditTextView org_security_Etv;
	private String avatarUrlToSql = null; // 图片上传后的相对路劲
	private String avatarUrlToSqlEditClient = null; // 图片上传后的绝对路劲
	private int marketlabel = 0;
	private CustomerClientParams customer_params; // //从客户详情传递进来的对象
	private List<String> industryIds;// 行业id集合；
	private View organizationTreeIC;// include的目录布局
	private View organizationLabelIC; // include的标签布局
	private LinearLayout org_information_Ll; // 基本信息的父类
	private TextView org_asso_Tv; // 关联信息
	private CustomerOrganizatianParams organization_params; // 从组织详情传递进来的对象
	private static String type3; // 客户类型的返回值
	public static final int REQUEST_CODE_INTRODUCE_ACTIVITY = 1006;// 启动介绍回调
	private DepartMents departMentsInfo;// 职能部门对象
	private AreaInfo areaInfo;// 地区概况对象
	private Area area;
	private HashSet<String> set;
	private Long orgId; // 客户id;
	private Long type2; // 客户类型所代表的id
	private GetId getId; // 获取主键id返回的对象
	private ArrayList<JTFile> picture; // 图片返回的对象集合，只有一个值
	private ArrayList<MyEditTextView> editTextViews; // 自定义的控件集合，以便于完成时取值，和进行自定义值的增删改查
	private String remark; // 备注

	/**
	 * 1 创建 2 编辑 3 保存 4 转为客户 //区分Activity的来源，和作用
	 */
	private int activity_type;
	private long details_id; // 从别处传递进来的id ，此有值时，不需要去访问获取主键ID接口
	private TextView cread_clientele_Tv; // 标题
	private Area area_result = new Area(); // 地址对象
	private ArrayList<LableData> selectTagList = new ArrayList<LableData>(); // 四大组件标签的返回值
	private ArrayList<UserCategory> listCategory = new ArrayList<UserCategory>(); // 四大组件目录的返回值
	private ArrayList<Connections> listHightPermission = new ArrayList<Connections>(); // 四大组件权限的大乐返回值
	private ArrayList<Connections> listMiddlePermission = new ArrayList<Connections>(); // 四大组件权限的中乐返回值
	private ArrayList<Connections> listLowPermission = new ArrayList<Connections>(); // 四大组件权限的小乐返回值
	private TextView view_Label_edit; // 展示标签的Textview
	private TextView view_Tree_edit; // 展示目录的Textview
	private boolean commit;
	private LinearLayout picture_org_Ll;
	/**
	 * 接受客户类型的返回值
	 */
	public static Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			type3 = (String) msg.obj;
			if (type3 != null) {
				org_type_Etv.setText(type3);
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.organization_create_clientele);
		editTextViews = new ArrayList<MyEditTextView>();

		customerPermissions = new CustomerPermission_Bean();
		// 四大组件-权限
		xiaoles = new ArrayList<Permission>();
		zhongles = new ArrayList<Permission>();
		dales = new ArrayList<Permission>();

		industryIds = new ArrayList<String>();
		modelType = new ArrayList<String>();
		// 行业
		metadataIndustry = new ArrayList<Metadata>();
		// 地区
		metadataArea = new ArrayList<Metadata>();
		// 联系人控件的集合
		contact_list = new ArrayList<MyEditTextView>();
		// 相关当事人
		relevant = new ArrayList<String>();
		// 地区
		area = new Area();
		bundle = new Bundle();
		module_Map = new HashMap<String, MyitemView>();
		contact = new ArrayList<CustomerPhone>();
		industry = new ArrayList<String>();
		set = new HashSet<String>();
		directory = new ArrayList<String>();
		lableList = new ArrayList<CustomerTag>();
		sponsorCustomerList = new ArrayList<CustomerRemark>();

		// 目录展示
		organizationTreeIC = findViewById(R.id.organizationTreeIC);
		organizationTreeIC.setOnClickListener(this);
		view_Tree_edit = (TextView) organizationTreeIC
				.findViewById(R.id.view_et_edit);
		TextView view_Tree_name = (TextView) organizationTreeIC
				.findViewById(R.id.view_tv_name);
		view_Tree_name.setText("目录");
		// 标签展示
		organizationLabelIC = findViewById(R.id.organizationLabelIC);
		view_Label_edit = (TextView) organizationLabelIC
				.findViewById(R.id.view_et_edit);
		TextView view_Label_name = (TextView) organizationLabelIC
				.findViewById(R.id.view_tv_name);
		view_Label_name.setText("标签");

		connectionNodeList = new ArrayList<ConnectionNode>();
		connectionNodeList2 = new ArrayList<ConnectionNode>();
		knowledgeNodeList = new ArrayList<KnowledgeNode>();
		affairNodeList = new ArrayList<AffairNode>();

		propertyList = new ArrayList<CustomerPersonalLine>(); // 自定义值的集合

		init();
		initListViewData();
		initData();

	}

	// 四大组件
	private void initListViewData() {
		people.setOnItemClickListener(this);
		peopleGroupAdapter = new ConnectionsGroupAdapter(context,
				connectionNodeList);
		people.setAdapter(peopleGroupAdapter);
		organization.setOnItemClickListener(this);
		organizationGroupAdapter = new ConnectionsGroupAdapter(context,
				connectionNodeList2);
		organization.setAdapter(organizationGroupAdapter);
		knowledge.setOnItemClickListener(this);
		knowledgeGroupAdapter = new KnowledgeGroupAdapter(context,
				knowledgeNodeList);
		knowledge.setAdapter(knowledgeGroupAdapter);
		requirement.setOnItemClickListener(this);
		requirementGroupAdapter = new RequirementGroupAdapter(context,
				affairNodeList);
		requirement.setAdapter(requirementGroupAdapter);
	}
	/**
	 * 更新四大组件中的关联数据
	 */
	public void updateAllUI() {

		// 更新
		peopleGroupAdapter.setListRelatedConnectionsNode(connectionNodeList);
		peopleGroupAdapter.notifyDataSetChanged();
		organizationGroupAdapter
				.setListRelatedConnectionsNode(connectionNodeList2);
		organizationGroupAdapter.notifyDataSetChanged();
		knowledgeGroupAdapter.setListRelatedKnowledgeNode(knowledgeNodeList);
		knowledgeGroupAdapter.notifyDataSetChanged();
		requirementGroupAdapter.setListRelatedAffairNode(affairNodeList);
		requirementGroupAdapter.notifyDataSetChanged();

	}
	/**
	 * 初始化数据
	 */
	private void initData() {

		// 获取详情页传过来的数据

		details_id = this.getIntent().getLongExtra(ENavConsts.EClient_Id, -1); // 传递进来的ID；

		activity_type = this.getIntent().getIntExtra(
				ENavConsts.EFromActivityName, 1);

		if (activity_type == 1 || activity_type == 3 || activity_type == 4) { // 创建，才需要获取主键ID，非创建的话是使用传递进来的ID
			OrganizationReqUtil.doRequestWebAPI(this, this, customer, null,
					OrganizationReqType.ACCESS_TO_THE_PRIMARY_KEY);
		}
		if (activity_type != 1) {
			if (activity_type == 2 || activity_type == 3) {

				customer_params = (CustomerClientParams) this.getIntent()
						.getSerializableExtra(ENavConsts.EClient_Data);
				if (customer_params != null) {
					if (customer_params.customer != null) {
						// 加载头像
						ImageLoader.getInstance().displayImage(customer_params.customer.picLogo,
								picture_org_Iv);
						customer.picLogo = customer_params.customer.picLogo;
						// 客户类型
						if (customer_params.customer.type.equals("1")) {
							org_type_Etv.setText("金融机构");
						} else if (customer_params.customer.type.equals("2")) {
							org_type_Etv.setText("一般企业");
						} else if (customer_params.customer.type.equals("3")) {
							org_type_Etv.setText("政府组织");
						} else if (customer_params.customer.type.equals("4")) {
							org_type_Etv.setText("中介机构");
						} else if (customer_params.customer.type.equals("5")) {
							org_type_Etv.setText("专业媒体");
						} else if (customer_params.customer.type.equals("6")) {
							org_type_Etv.setText("期刊报纸");
						} else if (customer_params.customer.type.equals("7")) {
							org_type_Etv.setText("研究机构");
						} else if (customer_params.customer.type.equals("8")) {
							org_type_Etv.setText("电视广播");
						} else if (customer_params.customer.type.equals("9")) {
							org_type_Etv.setText("互联网媒体");
						}

						remark = customer_params.customer.discribe;
						// 客户描述
						org_describe_Etv
								.setText(remark);

						if (customer_params.customer.phoneList != null
								&& !customer_params.customer.phoneList
										.isEmpty()) { // 联系电话
							for (int i = 0; i < customer_params.customer.phoneList
									.size(); i++) {
								org_contact_Etv
										.setText(customer_params.customer.phoneList
												.get(i).phone);
							}
							customer.phoneList = customer_params.customer.phoneList;
						}

						org_email_Etv
								.setText(customer_params.customer.linkEmail);// 邮箱

						if (customer_params.customer.isListing.equals("1")) {
							org_market_Etv.setText("上市公司");
							org_security_Etv
									.setText(customer_params.customer.stockNum);

						} else if (customer_params.customer.isListing
								.equals("0")) {
							org_market_Etv.setText("非上市公司");
						}

						// org_type_Etv.setText(customer_params.customer.type);

						if (!TextUtils.isEmpty(customer_params.customer.name)) {
							org_name_Etv.setText(customer_params.customer.name);// 客户名称
						}
						// 客户地址
						if (customer_params.customer.area != null) {
							org_detail_address_Etv
									.setText(customer_params.customer.area.address);
							org_address_Etv
									.setText(getAreaStr(customer_params.customer.area));
							area_result = customer_params.customer.area;
						}
						if (customer_params.customer.industrys != null
								&& customer_params.customer.industrys.size() > 0) {
							org_industry_Etv.setText(customer_params.customer.industrys.toString());// 客户行业
							customer.industryIds = customer_params.customer.industryIds;
							customer.industrys =  customer_params.customer.industrys;
						}

						if (customer_params.customer.propertyList != null
								&& !customer_params.customer.propertyList
										.isEmpty()) {
							for (int i = 0; i < customer_params.customer.propertyList
									.size(); i++) {
								CustomerPersonalLine custom = customer_params.customer.propertyList
										.get(i);
								final MyEditTextView editTextView = new MyEditTextView(
										context);
								editTextView.setCustom(true);
								editTextView.setDelete(true);
								editTextView.setTextLabel(custom.name);
								editTextView.setText(custom.content);
								org_information_Ll
										.addView(
												editTextView,
												org_information_Ll
														.indexOfChild(org_custom_Etv) - 1);
								editTextViews.add(editTextView);// 将初始化的自定义控件添加到自定义控件集合中，以便于完成的时候去取值
								editTextView.getAddMore_Iv()
										.setOnClickListener(
												new OnClickListener() {

													@Override
													public void onClick(View v) {
														org_information_Ll
																.removeView(editTextView);
														editTextViews
																.remove(editTextView);
													}
												});
							}
						}

						if (!TextUtils
								.isEmpty(customer_params.customer.shotName)) {
							org_shortname_Etv
									.setText(customer_params.customer.shotName);// 客户简称
						}

					}

				}
				if (activity_type == 2) {  //编辑客户时有四大组件的值！
					cread_clientele_Tv.setText("编辑客户信息");
					if (customer_params.relevance != null) {
						createKnowNewASSO(customer_params.relevance);

						if (!customer_params.relevance.p.isEmpty()) {
							//TODO
							org_asso_Tv.setVisibility(View.VISIBLE);
							people_Ll.setVisibility(View.VISIBLE);
						}
						if (!customer_params.relevance.r.isEmpty()) {
							org_asso_Tv.setVisibility(View.VISIBLE);
							requirement_Ll.setVisibility(View.VISIBLE);
						}
						if (!customer_params.relevance.o.isEmpty()) {
							org_asso_Tv.setVisibility(View.VISIBLE);
							organization_Ll.setVisibility(View.VISIBLE);
						}
						if (!customer_params.relevance.k.isEmpty()) {
							org_asso_Tv.setVisibility(View.VISIBLE);
							knowledge_Ll.setVisibility(View.VISIBLE);
						}
						updateAllUI();
					}

					if (customer_params.customerPermissions != null) {
						if (customer_params.customerPermissions.dales != null
								&& !customer_params.customerPermissions.dales
										.isEmpty()) {
							for (int i = 0; i < customer_params.customerPermissions.dales
									.size(); i++) {
								Permission personPermDales = customer_params.customerPermissions.dales
										.get(i);
								Connections connections = new Connections();
								connections.setType(connections.type_org + "");
								connections.jtContactMini
										.setId(personPermDales.id + "");
								connections.setName(personPermDales.name);
								listHightPermission.add(connections);
							}
						}

						if (customer_params.customerPermissions.zhongles != null
								&& !customer_params.customerPermissions.zhongles
										.isEmpty()) {
							for (int i = 0; i < customer_params.customerPermissions.zhongles
									.size(); i++) {
								Permission permZhongles = customer_params.customerPermissions.zhongles
										.get(i);
								Connections connections = new Connections();
								connections.setType(connections.type_org + "");
								connections.jtContactMini.setId(permZhongles.id
										+ "");
								connections.setName(permZhongles.name);
								listMiddlePermission.add(connections);
							}
						}
						if (customer_params.customerPermissions.xiaoles != null
								&& !customer_params.customerPermissions.xiaoles
										.isEmpty()) {
							for (int i = 0; i < customer_params.customerPermissions.xiaoles
									.size(); i++) {
								Permission permXiaoles = customer_params.customerPermissions.xiaoles
										.get(i);
								Connections connections = new Connections();
								connections.setType(connections.type_org + "");
								connections.jtContactMini.setId(permXiaoles.id
										+ "");
								connections.setName(permXiaoles.name);
								listLowPermission.add(connections);
							}
						}

						customer.customerPermissions = customer_params.customerPermissions;

					}
					if (customer_params.directory != null) {
						if (!customer_params.directory.isEmpty()) {

							ArrayList<String> strings = new ArrayList<String>();
							for (int i = 0; i < customer_params.directory
									.size(); i++) {
								Long categoryId = customer_params.directory
										.get(i).id;
								String categoryName = customer_params.directory
										.get(i).name;
								UserCategory category = new UserCategory();
								category.setId(categoryId);
								category.setCategoryname(categoryName);
								listCategory.add(category);
								strings.add(TextStrUtil
										.checkCategoryname(listCategory.get(i)));
							}
							organizationTreeIC.setVisibility(View.VISIBLE); // 显示
							view_Tree_edit.setText(TextStrUtil.getStringSize(9,
									strings));
							for (int i = 0; i < customer_params.directory
									.size(); i++) {
								directory
										.add(customer_params.directory.get(i).id
												+ "");
							}
							customer.directory = directory;
						}

					}

					if (customer_params.customer.lableList != null) {
						if (!customer_params.customer.lableList.isEmpty()) {
							for (int i = 0; i < customer_params.customer.lableList
									.size(); i++) {
								CustomerTag customerTag = customer_params.customer.lableList
										.get(i);
								LableData data = new LableData();
								data.id = customerTag.tagId;
								data.tag = customerTag.tagName;
								selectTagList.add(data);
							}
							organizationLabelIC.setVisibility(View.VISIBLE);
							view_Label_edit.setText(TextStrUtil
									.getLableDataSize(9, selectTagList));
							customer.lableList = customer_params.customer.lableList;
						}

					}
				} else if (activity_type == 3) {
					cread_clientele_Tv.setText("保存客户信息");
				}
			} else if (activity_type == 4) {
				cread_clientele_Tv.setText("转为客户信息");
				organization_params = (CustomerOrganizatianParams) this
						.getIntent().getSerializableExtra(
								ENavConsts.EClient_Data);
				if (organization_params != null) {
					if (organization_params.customer != null) {
						// 加载头像
						ImageLoader.getInstance().displayImage(organization_params.customer.picLogo,picture_org_Iv
								);
						customer.picLogo = organization_params.customer.picLogo;
						// 客户类型
						if (organization_params.customer.type.equals("1")) {
							org_type_Etv.setText("金融机构");
						} else if (organization_params.customer.type
								.equals("2")) {
							org_type_Etv.setText("一般企业");
						} else if (organization_params.customer.type
								.equals("3")) {
							org_type_Etv.setText("政府组织");
						} else if (organization_params.customer.type
								.equals("4")) {
							org_type_Etv.setText("中介机构");
						} else if (organization_params.customer.type
								.equals("5")) {
							org_type_Etv.setText("专业媒体");
						} else if (organization_params.customer.type
								.equals("6")) {
							org_type_Etv.setText("期刊报纸");
						} else if (organization_params.customer.type
								.equals("7")) {
							org_type_Etv.setText("研究机构");
						} else if (organization_params.customer.type
								.equals("8")) {
							org_type_Etv.setText("电视广播");
						} else if (organization_params.customer.type
								.equals("9")) {
							org_type_Etv.setText("互联网媒体");
						}

						// 客户描述
						org_describe_Etv
								.setText(organization_params.customer.discribe);

						// if (organization_params.customer.phoneList != null
						// && !organization_params.customer.phoneList.isEmpty())
						// { // 联系电话
						// for (int i = 0; i <
						// organization_params.customer.phoneList
						// .size(); i++) {
						// org_contact_Etv
						// .setText(organization_params.customer.phoneList
						// .get(i).phone);
						// }
						// customer.phoneList =
						// organization_params.customer.phoneList;
						// }
						org_contact_Etv
								.setText(organization_params.customer.linkMobile);
						org_email_Etv
								.setText(organization_params.customer.linkEmail);// 邮箱

						if (organization_params.customer.isListing.equals("1")) {
							org_market_Etv.setText("上市公司");
							org_security_Etv
									.setText(organization_params.customer.stockNum);

						} else if (organization_params.customer.isListing
								.equals("0")) {
							org_market_Etv.setText("非上市公司");
						}

						// org_type_Etv.setText(customer_params.customer.type);

						if (!TextUtils
								.isEmpty(organization_params.customer.name)) {
							org_name_Etv
									.setText(organization_params.customer.name);// 客户名称
						}
						// 客户地址
						if (organization_params.customer.area != null) {
							org_detail_address_Etv
									.setText(organization_params.customer.area.address);
							org_address_Etv
									.setText(getAreaStr(organization_params.customer.area));
							area_result = organization_params.customer.area;
						}
						if (organization_params.customer.industrys != null
								&& organization_params.customer.industrys
										.size() > 0) {
							org_industry_Etv.setText(
									organization_params.customer.industrys.toString());// 客户行业
//							customer.industryIds = organization_params.customer.industryIds;
							customer.industrys =  organization_params.customer.industrys;
						}

						if (organization_params.customer.propertyList != null
								&& !organization_params.customer.propertyList
										.isEmpty()) {
							for (int i = 0; i < organization_params.customer.propertyList
									.size(); i++) {
								CustomerPersonalLine custom = organization_params.customer.propertyList
										.get(i);
								final MyEditTextView editTextView = new MyEditTextView(
										context);
								editTextView.setCustom(true);
								editTextView.setDelete(true);
								editTextView.setTextLabel(custom.name);
								editTextView.setText(custom.content);
								org_information_Ll
										.addView(
												editTextView,
												org_information_Ll
														.indexOfChild(org_custom_Etv) - 1);
								editTextViews.add(editTextView);// 将初始化的自定义控件添加到自定义控件集合中，以便于完成的时候去取值
								editTextView.getAddMore_Iv()
										.setOnClickListener(
												new OnClickListener() {

													@Override
													public void onClick(View v) {
														org_information_Ll
																.removeView(editTextView);
														editTextViews
																.remove(editTextView);
													}
												});
							}
						}
						if (!TextUtils
								.isEmpty(organization_params.customer.shotName)) {
							org_shortname_Etv
									.setText(organization_params.customer.shotName);// 客户简称
						}
					}
				}
			}
		}

	}
	/**
	 * 初始化控件
	 */
	private void init() {
		quit_org_Rl = (RelativeLayout) findViewById(R.id.quit_org_Rl);
		org_Addmore_Rl = (RelativeLayout) findViewById(R.id.org_Addmore_Rl);
		cread_clientele_Tv = (TextView) findViewById(R.id.cread_clientele_Tv);
		org_main1_Ll = (LinearLayout) findViewById(R.id.org_main1_Ll);
		org_main_Ll = (LinearLayout) findViewById(R.id.org_main_Ll);
		org_information_Ll = (LinearLayout) findViewById(R.id.org_information_Ll);
		org_businessCard_Tv = (ImageView) findViewById(R.id.org_businessCard_Tv);
		picture_org_Iv = (ImageView) findViewById(R.id.picture_org_Iv);
		picture_org_Ll = (LinearLayout) findViewById(R.id.picture_org_Ll);
		org_contact_Etv = (MyEditTextView) findViewById(R.id.org_contact_Etv);
		org_email_Etv = (MyEditTextView) findViewById(R.id.org_email_Etv);
		org_market_Etv = (MyEditTextView) findViewById(R.id.org_market_Etv);
		org_describe_Etv = (MyEditTextView) findViewById(R.id.org_describe_Etv);
		org_type_Etv = (MyEditTextView) findViewById(R.id.org_type_Etv);
		org_industry_Etv = (MyEditTextView) findViewById(R.id.org_industry_Etv);
		org_address_Etv = (MyEditTextView) findViewById(R.id.org_address_Etv);
		org_detail_address_Etv = (MyEditTextView) findViewById(R.id.org_detail_address_Etv);
		org_custom_Etv = (MyEditTextView) findViewById(R.id.org_custom_Etv);
		org_shortname_Etv = (MyEditTextView) findViewById(R.id.org_shortname_Etv);
		org_name_Etv = (MyEditTextView) findViewById(R.id.org_name_Etv);
		org_security_Etv = (MyEditTextView) findViewById(R.id.org_security_Etv);

		people = (BasicListView2) findViewById(R.id.people);
		organization = (BasicListView2) findViewById(R.id.organization);
		knowledge = (BasicListView2) findViewById(R.id.knowledge);
		requirement = (BasicListView2) findViewById(R.id.requirement);
		people_Ll = (LinearLayout) findViewById(R.id.people_Ll);
		organization_Ll = (LinearLayout) findViewById(R.id.organization_Ll);
		knowledge_Ll = (LinearLayout) findViewById(R.id.knowledge_Ll);
		requirement_Ll = (LinearLayout) findViewById(R.id.requirement_Ll);

		relevance_Tv = (TextView) findViewById(R.id.relevance_Tv);
		catalogue_Tv = (TextView) findViewById(R.id.catalogue_Tv);
		label_Tv = (TextView) findViewById(R.id.label_Tv);
		jurisdiction_Tv = (TextView) findViewById(R.id.jurisdiction_Tv);
		org_asso_Tv = (TextView) findViewById(R.id.org_asso_Tv);

		onclicklistener();
		org_email_Etv.getEditText().clearFocus();
		org_name_Etv.getEditText().clearFocus();
		org_shortname_Etv.getEditText().clearFocus();
		org_contact_Etv.getEditText().clearFocus();
		org_detail_address_Etv.getEditText().clearFocus();
		org_security_Etv.getEditText().clearFocus();

		org_contact_Etv.setNumEdttext_inputtype(); // 将联系电话设置为输入类型为PHONE
		org_security_Etv.setNumEdttext_inputtype(); // 将证券号码设置为输入类型为PHONE
		// 让键盘覆盖布局。
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		org_custom_Etv.getAddMore_Iv().setOnClickListener(
				new OnClickListener() { // 让自定义的那个加号也能添加！

					@Override
					public void onClick(View v) {
						final MyEditTextView editTextView = new MyEditTextView(
								context);
						editTextView.setDelete(true);
						editTextView.setCustom(true);
						org_information_Ll
								.addView(editTextView, org_information_Ll
										.indexOfChild(org_custom_Etv) - 1);

						editTextView.getAddMore_Iv().setBackgroundResource(
								R.drawable.people_column_delete);
						editTextView.getAddMore_Iv().setOnClickListener(
								new OnClickListener() {
									@Override
									public void onClick(View v) {
										org_information_Ll
												.removeView(editTextView);
										editTextViews.remove(editTextView);
									}
								});
						editTextViews.add(editTextView);
					}
				});
	}

	/**
	 * 将由框架关联返回值，转换成ASSORPOK对象
	 * 
	 * @return
	 */
	public ASSORPOK createNewASSO() {
		DemandASSO asso = new DemandASSO();
		List<com.tr.model.demand.ASSOData> p = new ArrayList<com.tr.model.demand.ASSOData>();
		// 人脉信息
		if (connectionNodeList != null) {
			for (ConnectionNode node : connectionNodeList) {
				List<DemandASSOData> conn = new ArrayList<DemandASSOData>();
				for (Connections obj : node.getListConnections()) {
					DemandASSOData assoData = new DemandASSOData();
					assoData.id = obj.getId();
					assoData.name = obj.getName();
					assoData.ownerid = App.getUserID();
					assoData.ownername = App.getNick();
					assoData.career = obj.getCareer();
					assoData.company = obj.getCompany();
					if (obj.isOnline()) {
						assoData.type = 3;
					} else {
						assoData.type = 2;
					}
					assoData.picPath = obj.jtContactMini.image;
					conn.add(assoData);
				}
				p.add(new ASSOData(node.getMemo(), conn));
			}
		}
		List<ASSOData> o = new ArrayList<ASSOData>();
		// 组织信息
		if (connectionNodeList2 != null) {
			for (ConnectionNode node : connectionNodeList2) {
				List<DemandASSOData> conn = new ArrayList<DemandASSOData>();
				for (Connections obj : node.getListConnections()) {
					DemandASSOData assoData = new DemandASSOData();
					if (obj.isOnline()) {
						assoData.type = 4;
					} else {
						assoData.type = 5;
					}
					assoData.id = obj.getOrganizationMini().id;//obj.getId();2015/10/20 关联客户id==assoData.id用OrganizationMini().id
					assoData.name = obj.getName();
					assoData.ownerid = obj.getOrganizationMini().ownerid;//App.getUserID();
					assoData.ownername = obj.getOrganizationMini().ownername;//App.getNick();
					assoData.picPath = obj.organizationMini.mLogo;
					// 当前组织没有行业和地址
					// assoData.setAddress(obj.getAttribute());
					// assoData.setHy(obj.getOrganizationMini());
					conn.add(assoData);
				}
				o.add(new ASSOData(node.getMemo(), conn));
			}
		}
		// 知识
		List<ASSOData> k = new ArrayList<ASSOData>();
		if (knowledgeNodeList != null) {
			for (KnowledgeNode node : knowledgeNodeList) {
				List<DemandASSOData> conn = new ArrayList<DemandASSOData>();
				for (KnowledgeMini2 obj : node.getListKnowledgeMini2()) {
					DemandASSOData assoData = new DemandASSOData();
					assoData.type = 6; //
					assoData.id = obj.id + "";
					assoData.title = obj.title;
					// assoData.setOwnerid(App.getUserID()); //创建id 没有
					// assoData.setOwnername(App.getNick());// 创建人 没有
					assoData.columnpath = obj.columnpath;
					assoData.columntype = obj.type + "";//
					conn.add(assoData);
				}
				k.add(new ASSOData(node.getMemo(), conn));
			}
		}
		// 事件 （需求）
		List<ASSOData> r = new ArrayList<ASSOData>();
		if (affairNodeList != null) {
			for (AffairNode node : affairNodeList) {
				List<DemandASSOData> conn = new ArrayList<DemandASSOData>();
				for (AffairsMini obj : node.getListAffairMini()) {
					DemandASSOData assoData = new DemandASSOData();
					if ("1".equals(obj.requirementType)) {
						assoData.type = 0; //
					} else if ("2".equals(obj.requirementType)) {
						assoData.type = 1;
					}
					assoData.id = obj.id + "";
					assoData.title = obj.title;
					assoData.name = obj.name;
					assoData.ownerid = App.getUserID();
					assoData.ownername = App.getNick();
					assoData.requirementtype = obj.reserve; // 事件类型
					conn.add(assoData);
				}
				r.add(new ASSOData(node.getMemo(), conn));
			}
		}
		asso.value = new ASSORPOK(r, p, o, k);
		return asso.value;

	}

	/**
	 * 将ASSORPOK对象的值转换成框架的值
	 * 
	 * @param relatedInformation2
	 */
	public void createKnowNewASSO(ASSORPOK relatedInformation2) {
		if (!relatedInformation2.p.isEmpty()) {

			List<ASSOData> Pass = relatedInformation2.p;
			for (ASSOData assoData : Pass) {
				ConnectionNode Pnode = new ConnectionNode();
				Pnode.setMemo(assoData.tag);
				Connections Pobj = null;
				List<DemandASSOData> conn2 = assoData.conn;
				for (int j = 0; j < conn2.size(); j++) {
					Pobj = new Connections();
					Pobj.setType(Connections.type_persion + "");
					Pobj.setID(conn2.get(j).id);
					if(3==conn2.get(j).type){
						Pobj.setOnline(true);
					}else{
						Pobj.setOnline(false);
					}
					Pobj.setName(conn2.get(j).name);
					Pobj.setCareer(conn2.get(j).career);
					Pobj.setCompany(conn2.get(j).company);
					Pobj.setImage(conn2.get(j).picPath);
					Pnode.getListConnections().add(Pobj);
				}
				connectionNodeList.add(Pnode);
			}
			peopleGroupAdapter.notifyDataSetChanged();
		}
		if (!relatedInformation2.o.isEmpty()) {

			List<ASSOData> Oass = relatedInformation2.o;
			for (ASSOData assoData : Oass) {
				ConnectionNode Onode = new ConnectionNode();
				List<DemandASSOData> conn2 = assoData.conn;
				Onode.setMemo(assoData.tag);
				Connections Oobj = null;
				for (int j = 0; j < conn2.size(); j++) {
					Oobj = new Connections();
					Oobj.setType(Connections.type_org + "");
					
					if(4==conn2.get(j).type){
						Oobj.setOnline(true);
					}else{
						Oobj.setOnline(false);
					}
					Oobj.setID(conn2.get(j).id);
					Oobj.setName(conn2.get(j).name);
					Oobj.setCareer(conn2.get(j).career);
					Oobj.setCompany(conn2.get(j).company);
					Oobj.setImage(conn2.get(j).picPath);
					Onode.getListConnections().add(Oobj);
				}
				connectionNodeList2.add(Onode);
			}
			organizationGroupAdapter.notifyDataSetChanged();
		}
		if (!relatedInformation2.k.isEmpty()) {

			List<ASSOData> Kass = relatedInformation2.k;
			for (ASSOData assoData : Kass) {
				KnowledgeNode Knode = new KnowledgeNode();
				List<DemandASSOData> conn2 = assoData.conn;
				Knode.setMemo(assoData.tag);
				KnowledgeMini2 Kobj = null;
				for (int j = 0; j < conn2.size(); j++) {
					Kobj = new KnowledgeMini2();
					String name = conn2.get(j).title;
					Kobj.id = Long.parseLong(conn2.get(j).id);
					Kobj.title = name;
					Knode.getListKnowledgeMini2().add(Kobj);
				}
				knowledgeNodeList.add(Knode);
			}
			knowledgeGroupAdapter.notifyDataSetChanged();

		}
		if (!relatedInformation2.r.isEmpty()) {

			List<ASSOData> Aass = relatedInformation2.r;
			for (ASSOData assoData : Aass) {
				AffairNode Anode = new AffairNode();
				AffairsMini Aobj = null;
				List<DemandASSOData> conn2 = assoData.conn;
				Anode.setMemo(assoData.tag);
				for (int j = 0; j < conn2.size(); j++) {
					Aobj = new AffairsMini();
					String name = conn2.get(j).title;
					Aobj.id = Integer.parseInt(conn2.get(j).id);
					Aobj.title = name;
					Anode.getListAffairMini().add(Aobj);
				}
				affairNodeList.add(Anode);
			}
			requirementGroupAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 保存
	 * 
	 * @param v
	 */
	public void finishs(View v) {
		if (!commit) {
			if (!"".equals(org_name_Etv.getText().toString())) {
				customer.email = org_email_Etv.getText().toString();// 邮箱
				if (!StringUtils.isEmpty(org_email_Etv.getText())
						&& !StringUtils.checkEmail(org_email_Etv.getText())) {
					ToastUtil.showToast(this, "请输入有效的邮箱");
					return;
				}
				if (org_type_Etv.getText().equals("金融机构")) {
					customer.type = "1";
				} else if (org_type_Etv.getText().equals("一般企业")) {
					customer.type = "2";
				} else if (org_type_Etv.getText().equals("政府组织")) {
					customer.type = "3";
				} else if (org_type_Etv.getText().equals("中介机构")) {
					customer.type = "4";
				} else if (org_type_Etv.getText().equals("专业媒体")) {
					customer.type = "5";
				} else if (org_type_Etv.getText().equals("期刊报纸")) {
					customer.type = "6";
				} else if (org_type_Etv.getText().equals("研究机构")) {
					customer.type = "7";
				} else if (org_type_Etv.getText().equals("电视广播")) {
					customer.type = "8";
				} else if (org_type_Etv.getText().equals("互联网媒体")) {
					customer.type = "9";
				} else {
					customer.type = "10";
				}
				customer.discribe = org_describe_Etv.getText().toString();// 客户描述
				
				CustomerPhone customerPhone = new CustomerPhone();
				if (contact_list != null && !contact_list.isEmpty()) {
					for (int i = 0; i < contact_list.size(); i++) {
						CustomerPhone customerPhone_more = new CustomerPhone();
						customerPhone_more.phone = contact_list.get(i)
								.getText();
						customerPhone_more.type = "联系电话";
						contact.add(customerPhone_more);
					}
				}
				customerPhone.phone = org_contact_Etv.getText();
				customerPhone.type = "联系电话";
				contact.add(customerPhone);
				customer.phoneList = contact;

				Log.e("TAG", "联系电话" + customerPhone.phone);

				if ("上市公司".equals(org_market_Etv.getText().toString())) {
					customer.isListing = "1";
					customer.stockNum = org_security_Etv.getText().toString();
					Log.e("TAG", "证劵号--------->：" + customer.stockNum);

				} else if ("非上市公司".equals(org_market_Etv.getText().toString())) {
					customer.isListing = "0";
				}
				customer.virtual = "0"; // 是否是组织 0 客户 1 用户注册组织 2 未注册的组织
				customer.name = org_name_Etv.getText().toString().trim();// 客户名称

				if (area_result != null) {
					area_result.address = org_detail_address_Etv.getText();// 客户地址
					customer.area = area_result;
					Log.e("LOG", "编辑客户地址保存：" + area_result);
				}

				if (editTextViews != null && !editTextViews.isEmpty()) { // 自定义赋值
					for (int i = 0; i < editTextViews.size(); i++) {
						MyEditTextView view = editTextViews.get(i);
							CustomerPersonalLine customerPersonalLine = new CustomerPersonalLine();
							customerPersonalLine.content = view.getText();
							customerPersonalLine.name = view.getTextLabel();
							customerPersonalLine.type = 1 + "";
							propertyList.add(customerPersonalLine);
					}
					customer.propertyList = propertyList;
				}
				
				customer.name = org_name_Etv.getText().toString().trim();
				customer.shotName = org_shortname_Etv.getText();// 客户简称

				if (activity_type == 3) {// 保存
					customer.isOrgChange = 2 + "";
				} else if (activity_type == 4) {// 转为
					customer.isOrgChange = 1 + "";
					customer.comeId = details_id + "";
				} else if (activity_type == 1) {// 创建
					customer.isOrgChange = 0 + "";
				}

				// 关联信息
				if (createNewASSO() != null) {
					customer.relevance = createNewASSO();
				}
				if (!directory.isEmpty()) {
					customer.directory = directory;
				}
				if (!lableList.isEmpty()) {
					customer.lableList = lableList;
				}
				if (xiaoles.isEmpty() && zhongles.isEmpty()
						&& dales.isEmpty()) {
					CustomnoPermission = true;
				} else {
					CustomnoPermission = false;
				}

				customerPermissions.dule = CustomnoPermission;
				customerPermissions.xiaoles = xiaoles;
				customerPermissions.zhongles = zhongles;
				customerPermissions.dales = dales;
				customerPermissions.modelType = modelType;
				customerPermissions.mento = "";
				customer.customerPermissions = customerPermissions;
				if (avatarUrlToSql != null) {
					customer.picLogo = avatarUrlToSql;
					System.out.println("创建客户要传递的头像相对路径 avatarUrlToSql--->"
							+ avatarUrlToSql);
				}

				if (getId != null && !TextUtils.isEmpty(getId.orgId)) {
					orgId = Long.parseLong(getId.orgId);
					Log.e("orgId", "orgId" + orgId);
					customer.id = orgId;
				} else {
					orgId = details_id;
					Log.e("details_id", "details_id" + details_id);
					customer.id = orgId;
				}

				customer.profile = customerProfile; // 公司概况
				if (customer != null) {
					OrganizationReqUtil
							.doRequestWebAPI(
									this,
									this,
									customer,
									null,
									OrganizationReqType.ORGANIZATION_REQ_CUSTOMER_SAVECUSPROFILE);

				}

			} else {
				ToastUtil.showToast(context, "客户名称必填");
				return;
			}
			commit = true;
		} else {
			ToastUtil.showToast(context, "已经在努力中");
		}
	}

	private void onclicklistener() {
		quit_org_Rl.setOnClickListener(this);
		org_businessCard_Tv.setOnClickListener(this);
		picture_org_Ll.setOnClickListener(this);
		org_market_Etv.setOnClickListener(this);
		org_describe_Etv.setOnClickListener(this);
		org_type_Etv.setOnClickListener(this);
		org_industry_Etv.setOnClickListener(this);
		org_address_Etv.setOnClickListener(this);
		org_custom_Etv.setOnClickListener(this);
		org_Addmore_Rl.setOnClickListener(this);
		relevance_Tv.setOnClickListener(this);
		catalogue_Tv.setOnClickListener(this);
		label_Tv.setOnClickListener(this);
		jurisdiction_Tv.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 四大组件-关联
		case R.id.relevance_Tv://2015/10/20改为名称不是必填项 keyword为关联资源页面里的搜索关键字才用到
//			keyword = org_name_Etv.getText().toString().trim();
//			if (StringUtils.isEmpty(keyword)) {
//				ToastUtil.showToast(context, "客户名称不能为空");
//				return;
//			}
			currentRequestCode = 0;
			ENavigate.startRelatedResourceActivityForResult(
					CreateClienteleActivity.this, 10001, keyword,
					ResourceType.Organization, null);
			break;
		// 四大组件-目录
		case R.id.organizationTreeIC:
		case R.id.catalogue_Tv:
			// ENavigate.startKnowledgeCategoryActivityForResult(
			// Create_Clientele_Activity.this, 10002, null, true);
			ENavigate.startKnowledgeCategoryActivityForResult(
					CreateClienteleActivity.this, 10002, listCategory,
					ModuleType.ORG, true,"创建客户");
			break;
		// 四大组件-标签
		case R.id.label_Tv:
			ENavigate.startCheckLabelActivity(this, 10003, selectTagList,
					CreateLabelActivity.ModulesType.OrgAndCustomModules);
			break;
		// 四大组件-权限
		case R.id.jurisdiction_Tv:
			intent = new Intent(CreateClienteleActivity.this,
					PermissionActivity.class);
			intent.putExtra(EConsts.Key.FROM_ACTIVITY,
					CreateKnowledgeActivity.class.getSimpleName());
			intent.putExtra("listHightPermission", listHightPermission);
			intent.putExtra("listMiddlePermission", listMiddlePermission);
			intent.putExtra("listLowPermission", listLowPermission);
			startActivityForResult(intent, 10004);
			break;
		case R.id.quit_org_Rl:
			finish();
			break;
		case R.id.picture_org_Ll:// 上传图片
			ENavigate.startSelectPictureActivityforSingleSelection(
					CreateClienteleActivity.this,
					REQUEST_CODE_BROWERS_ACTIVITY, picture, true);
			break;
		case R.id.org_market_Etv:// 上市信息
			intent = new Intent(this, MarketActivity.class);
			intent.putExtra("marketlabel", marketlabel);
			startActivityForResult(intent, 0);
			break;
		case R.id.org_describe_Etv:// 客户描述
			Intent intent = new Intent(context, RemarkActivity.class);
			if (!TextUtils.isEmpty(remark)) {
				intent.putExtra("Remark_Activity", remark);
			}
			intent.putExtra("remark", "客户描述");
			startActivityForResult(intent, 1006);
			break;
		case R.id.org_type_Etv:// 客户类型
			// ENavigate.startChooseActivityForResult(this, true,
			// "类型",ChooseDataUtil.CHOOSE_type_OutInvestType, null);
			intent = new Intent(this, OrgTypeActivity.class);
			intent.putExtra("Type", Type);
			intent.putExtra("client", true);
			Log.e("MSG", "客户Type:" + Type);
			startActivityForResult(intent, 0);
			break;
		case R.id.org_industry_Etv:// 客户行业
			dataIndex = 2;
			ENavigate.startChooseActivityForResult(this, true, "行业",
					ChooseDataUtil.CHOOSE_type_Trade, null);
			break;
		case R.id.org_address_Etv:
			dataIndex = 3;
			ENavigate.startChooseActivityForResult(this, false, "区域",
					ChooseDataUtil.CHOOSE_type_Area, null);
			break;
		case R.id.org_custom_Etv:
			// intent = new Intent(this, Custom_Activity.class);
			// intent.putExtra("fengxing", true);
			// if (propertyList != null) {
			// if (!isNull) {
			// Bundle bundle = new Bundle();
			// bundle.putSerializable("Customer_Bean", propertyList);
			// intent.putExtras(bundle);
			// }
			//
			// }
			// startActivityForResult(intent, 0);
			final MyEditTextView editTextView = new MyEditTextView(context);
			editTextView.setDelete(true);
			editTextView.setCustom(true);
			org_information_Ll.addView(editTextView,
					org_information_Ll.indexOfChild(org_custom_Etv) - 1);

			editTextView.getAddMore_Iv().setBackgroundResource(
					R.drawable.people_column_delete);
			editTextView.getAddMore_Iv().setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							org_information_Ll.removeView(editTextView);
							editTextViews.remove(editTextView);
						}
					});
			editTextViews.add(editTextView);
			break;
		case R.id.org_Addmore_Rl:
			intent = new Intent(this, OrgModuleActivity.class);
			if (org_type_Etv.getText().equals("金融机构")) {
				customer.type = "1";
			} else if (org_type_Etv.getText().equals("一般企业")) {
				customer.type = "2";
			} else if (org_type_Etv.getText().equals("政府组织")) {
				customer.type = "3";
			} else if (org_type_Etv.getText().equals("中介机构")) {
				customer.type = "4";
			} else if (org_type_Etv.getText().equals("专业媒体")) {
				customer.type = "5";
			} else if (org_type_Etv.getText().equals("期刊报纸")) {
				customer.type = "6";
			} else if (org_type_Etv.getText().equals("研究机构")) {
				customer.type = "7";
			} else if (org_type_Etv.getText().equals("电视广播")) {
				customer.type = "8";
			} else if (org_type_Etv.getText().equals("互联网媒体")) {
				customer.type = "9";
			} else {
				customer.type = "10";
			}
			CustomerColumn_re column_re2 = new CustomerColumn_re();
			orgId = Long.parseLong(getId.orgId);
			column_re2.orgId = orgId;
			type2 = Long.parseLong(customer.type);
			column_re2.templateId = type2;
			OrganizationReqUtil.doRequestWebAPI(this, this, column_re2, null,
					OrganizationReqType.ORGANIZATION_CUSTOMER_COLUMNLIST);
			Bundle bundle = new Bundle();
			bundle.putSerializable("Column_Bean", column_re2);
			intent.putExtras(bundle);
			// intent.putExtra("Create_Clientele", org_type_Etv.getText());
			startActivityForResult(intent, 0);
			break;
		default:
			break;
		}
	}

	/**
	 * 将模块选择动态的添加到布局中
	 * 
	 * @param set
	 */
	private void AddMore(HashSet<String> set) {
		module = null;
		LinearLayout layout = new LinearLayout(this);
		// layout.removeAllViews();
		// layout.removeAllViewsInLayout();
		// System.out.println("--------------------------------------");
		System.out.println("控件数据集合" + set);
		if (set != null) {
			for (final String string : set) {
				if (!"组织概况".equals(string)) {

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
								if (areaInfo != null) {
									bundle.putSerializable("Area_survey_Bean",
											areaInfo);
									intent.putExtras(bundle);
								}

								startActivityForResult(intent, 6029);
							}
						});
						layout.addView(editTextView,
								new LinearLayout.LayoutParams(
										LayoutParams.MATCH_PARENT,
										LayoutParams.WRAP_CONTENT));
					} else if ("主要职能部门".equals(string)) {
						MyEditTextView editTextView = new MyEditTextView(this);
						editTextView.setChoose(true);
						editTextView.setOverline(true);
						editTextView.setTextLabel("主要职能部门");
						editTextView.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								intent = new Intent(context,
										FunctionalDepartmentActivity.class);
								if (departMentsInfo != null) {
									bundle.putSerializable(
											"Functional_department_Bean",
											departMentsInfo);
									intent.putExtras(bundle);
								}

								startActivityForResult(intent, 6030);
							}
						});
						layout.addView(editTextView,
								new LinearLayout.LayoutParams(
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
									intent.putExtra("fengxing", true);
									intent.putExtra("Custom", "企业刊物");

									Bundle bundle = new Bundle();
									bundle.putSerializable("publication_Bean",
											publication);
									intent.putExtras(bundle);
									startActivityForResult(intent, 6003);
								} else if ("主要客户".equals(string)) {
									intent = new Intent(context,
											CustomActivity.class);
									intent.putExtra("fengxing", true);
									intent.putExtra("Custom", "主要客户");
									Bundle bundle = new Bundle();

									bundle.putSerializable(
											"sponsorCustomer_Bean",
											sponsorCustomer_Bean);
									intent.putExtras(bundle);
									startActivityForResult(intent, 6004);
								} else if ("合伙人".equals(string)) {
									intent = new Intent(context,
											PartnerActivity.class);
									Bundle bundle = new Bundle();
									bundle.putParcelableArrayList(
											"Partner_Bean", partner);
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
									intent.putExtra("fengxing", true);
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
									intent.putExtra("fengxing", true);
									// Bundle bundle = new Bundle();
									//
									// bundle.putSerializable("sponsorCustomer_Bean",
									// sponsorCustomer_Bean);
									// intent.putExtras(bundle);
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
									intent = new Intent(
											context,
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
									intent = new Intent(
											context,
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
								} else {
									intent = new Intent(context,
											CustomActivity.class);
									startActivityForResult(intent, 6031);
								}
							}
						});
						module_Map.put(string, module);
					}
				}
				int size = org_main_Ll.getChildCount();
				int indexof = org_main_Ll.indexOfChild(org_Addmore_Rl);
				// System.out.println("----------size="+size);
				// System.out.println("----------indexOfChild="+indexof);
				for (int i = 1; i <= size - 12;) {
					i += indexof;
					org_main_Ll.removeViewAt(i); // delete already exist tag
				}
				org_main_Ll.addView(layout,
						org_main_Ll.indexOfChild(org_Addmore_Rl) + 1);
			}
		}
	}

	public class ConnectionsGroupAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<ConnectionNode> listRelatedConnectionsNode;

		public ConnectionsGroupAdapter() {
			super();
		}

		public ConnectionsGroupAdapter(Context context,
				ArrayList<ConnectionNode> listRelatedConnectionsNode) {
			super();
			this.context = context;
			if (listRelatedConnectionsNode != null) {
				this.listRelatedConnectionsNode = listRelatedConnectionsNode;
			} else {
				this.listRelatedConnectionsNode = new ArrayList<ConnectionNode>();
			}
		}

		public ArrayList<ConnectionNode> getListRelatedConnectionsNode() {
			return listRelatedConnectionsNode;
		}

		public void setListRelatedConnectionsNode(
				ArrayList<ConnectionNode> listRelatedConnectionsNode) {
			if (listRelatedConnectionsNode != null) {
				this.listRelatedConnectionsNode = listRelatedConnectionsNode;
			}
		}

		@Override
		public int getCount() {
			return listRelatedConnectionsNode.size();
		}

		@Override
		public Object getItem(int position) {
			return listRelatedConnectionsNode.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				final ViewGroup parent) {

			ViewHolder viewHolder;

			if (convertView == null) {
				viewHolder = new ViewHolder();

				convertView = LayoutInflater.from(context).inflate(
						R.layout.list_item_connections_group, null);
				viewHolder.keyTv = (TextView) convertView
						.findViewById(R.id.keyTv);
				viewHolder.valueTv = (TextView) convertView
						.findViewById(R.id.valueTv);
				viewHolder.deleteIv = (ImageView) convertView
						.findViewById(R.id.deleteIv);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.deleteIv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					listRelatedConnectionsNode.remove(position);
					notifyDataSetChanged();
					if (listRelatedConnectionsNode.isEmpty()) {
						LinearLayout layout = (LinearLayout) parent.getParent();
						layout.setVisibility(View.GONE);
					}
				}
			});

			ConnectionNode connectionNode = listRelatedConnectionsNode
					.get(position);
			String key = connectionNode.getMemo();
			viewHolder.keyTv.setText(key + " : ");
			ArrayList<Connections> listConnections = connectionNode
					.getListConnections();
			StringBuilder valueSb = new StringBuilder();
			for (int i = 0; i < listConnections.size(); i++) {
				valueSb.append(listConnections.get(i).getName());
				if (i != listConnections.size() - 1) {
					valueSb.append(decollatorStr);
				}
			}

			viewHolder.valueTv.setText(valueSb.toString());

			return convertView;
		}

		class ViewHolder {
			TextView keyTv;
			TextView valueTv;
			ImageView deleteIv;
		}

	}

	public class KnowledgeGroupAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<KnowledgeNode> listRelatedKnowledgeNode;

		public KnowledgeGroupAdapter() {
			super();
		}

		public KnowledgeGroupAdapter(Context context,
				ArrayList<KnowledgeNode> listRelatedKnowledgeNode) {
			super();
			this.context = context;
			if (listRelatedKnowledgeNode != null) {
				this.listRelatedKnowledgeNode = listRelatedKnowledgeNode;
			} else {
				this.listRelatedKnowledgeNode = new ArrayList<KnowledgeNode>();
			}
		}

		public ArrayList<KnowledgeNode> getListRelatedKnowledgeNode() {
			return listRelatedKnowledgeNode;
		}

		public void setListRelatedKnowledgeNode(
				ArrayList<KnowledgeNode> listRelatedKnowledgeNode) {
			if (listRelatedKnowledgeNode != null) {
				this.listRelatedKnowledgeNode = listRelatedKnowledgeNode;
			}
		}

		@Override
		public int getCount() {
			return listRelatedKnowledgeNode.size();
		}

		@Override
		public Object getItem(int position) {
			return listRelatedKnowledgeNode.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				final ViewGroup parent) {

			ViewHolder viewHolder;

			if (convertView == null) {
				viewHolder = new ViewHolder();

				convertView = LayoutInflater.from(context).inflate(
						R.layout.list_item_connections_group, null);
				viewHolder.keyTv = (TextView) convertView
						.findViewById(R.id.keyTv);
				viewHolder.valueTv = (TextView) convertView
						.findViewById(R.id.valueTv);
				viewHolder.deleteIv = (ImageView) convertView
						.findViewById(R.id.deleteIv);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.deleteIv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					listRelatedKnowledgeNode.remove(position);
					notifyDataSetChanged();
					if (listRelatedKnowledgeNode.isEmpty()) {
						LinearLayout layout = (LinearLayout) parent.getParent();
						layout.setVisibility(View.GONE);
					}
				}
			});

			KnowledgeNode knowledgeNode = listRelatedKnowledgeNode
					.get(position);
			String key = knowledgeNode.getMemo();
			viewHolder.keyTv.setText(key + " : ");
			ArrayList<KnowledgeMini2> listKnowledgeMini2 = knowledgeNode
					.getListKnowledgeMini2();
			StringBuilder valueSb = new StringBuilder();
			for (int i = 0; i < listKnowledgeMini2.size(); i++) {
				valueSb.append(listKnowledgeMini2.get(i).title);
				if (i != listKnowledgeMini2.size() - 1) {
					valueSb.append(decollatorStr);
				}
			}

			viewHolder.valueTv.setText(valueSb.toString());

			return convertView;
		}

		class ViewHolder {
			TextView keyTv;
			TextView valueTv;
			ImageView deleteIv;
		}

	}

	public class RequirementGroupAdapter extends BaseAdapter {

		private Context context;
		private ArrayList<AffairNode> listRelatedAffairNode;

		public RequirementGroupAdapter() {
			super();
		}

		public RequirementGroupAdapter(Context context,
				ArrayList<AffairNode> listRelatedAffairNode) {
			super();
			this.context = context;
			if (listRelatedAffairNode != null) {
				this.listRelatedAffairNode = listRelatedAffairNode;
			} else {
				this.listRelatedAffairNode = new ArrayList<AffairNode>();
			}
		}

		public ArrayList<AffairNode> getListRelatedAffairNode() {
			return listRelatedAffairNode;
		}

		public void setListRelatedAffairNode(
				ArrayList<AffairNode> listRelatedAffairNode) {
			if (listRelatedAffairNode != null) {
				this.listRelatedAffairNode = listRelatedAffairNode;
			}
		}

		@Override
		public int getCount() {
			return listRelatedAffairNode.size();
		}

		@Override
		public Object getItem(int position) {
			return listRelatedAffairNode.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				final ViewGroup parent) {

			ViewHolder viewHolder;

			if (convertView == null) {
				viewHolder = new ViewHolder();

				convertView = LayoutInflater.from(context).inflate(
						R.layout.list_item_connections_group, null);
				viewHolder.keyTv = (TextView) convertView
						.findViewById(R.id.keyTv);
				viewHolder.valueTv = (TextView) convertView
						.findViewById(R.id.valueTv);
				viewHolder.deleteIv = (ImageView) convertView
						.findViewById(R.id.deleteIv);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.deleteIv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					listRelatedAffairNode.remove(position);
					notifyDataSetChanged();
					if (listRelatedAffairNode.isEmpty()) {
						LinearLayout layout = (LinearLayout) parent.getParent();
						layout.setVisibility(View.GONE);
					}
				}
			});

			AffairNode affairNode = listRelatedAffairNode.get(position);
			String key = affairNode.getMemo();
			viewHolder.keyTv.setText(key + " : ");
			ArrayList<AffairsMini> listaAffairsMini = affairNode
					.getListAffairMini();
			StringBuilder valueSb = new StringBuilder();
			for (int i = 0; i < listaAffairsMini.size(); i++) {
				valueSb.append(listaAffairsMini.get(i).title);
				if (i != listaAffairsMini.size() - 1) {
					valueSb.append(decollatorStr);
				}
			}
			viewHolder.valueTv.setText(valueSb.toString());

			return convertView;
		}

		class ViewHolder {
			TextView keyTv;
			TextView valueTv;
			ImageView deleteIv;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			switch (requestCode) {
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
				listview_item_law = data
						.getStringArrayListExtra("Relevant_parties_Activity_law_office");
				relevant.addAll(listview_item);
				relevant.addAll(listview_item_bank);
				relevant.addAll(listview_item_law);
				if (!relevant.isEmpty()) {
					// 动态的将联系方式的数据填充到listview中
					AdaptiveListView(MakeListView.ToListviewAdapter(this,
							relevant, module_Map.get("相关当事人")
									.getMyitemview_Lv()), displayMetrics);
				}
				break;
			case 6029:
				areaInfo = (AreaInfo) data
						.getSerializableExtra("Area_Survey_Input_Bean");
				listview_item = data
						.getStringArrayListExtra("Area_survey_Activity");
				if (listview_item != null) {
					// // 动态的将联系方式的数据填充到listview中
					// AdaptiveListView(MakeListView.ToListviewAdapter(this,
					// listview_item, module_Map.get("地区概况")
					// .getMyitemview_Lv()), displayMetrics);
				}
				if (areaInfo != null) {
					areaInfo.id = orgId;
					OrganizationReqUtil
							.doRequestWebAPI(
									this,
									this,
									areaInfo,
									null,
									OrganizationReqType.ORGANIZATION_REQ_ADD_GOVERNMENTAREASURVEY);
				}
				break;
			case 6030:
				departMentsInfo = (DepartMents) data
						.getSerializableExtra("Functional_department_Bean");
				listview_item = data
						.getStringArrayListExtra("Functional_department_Activity");
				if (listview_item != null) {
					// 动态的将联系方式的数据填充到listview中
					// AdaptiveListView(MakeListView.ToListviewAdapter(this,
					// listview_item, module_Map.get("主要职能部门")
					// .getMyitemview_Lv()), displayMetrics);
				}

				if (departMentsInfo != null) {
					departMentsInfo.id = orgId;
					OrganizationReqUtil
							.doRequestWebAPI(
									this,
									this,
									departMentsInfo,
									null,
									OrganizationReqType.ORGANIZATION_REQ_ADD_MAIN_DEPARTMENT);
				}
				break;
			case ENavConsts.ActivityReqCode.REQUEST_CHOOSE_SELECT:
				// 多级选择回调界面
				setChooseText((ArrayList<Metadata>) data
						.getSerializableExtra(ENavConsts.DEMAND_CHOOSE_DATA));
				break;
			case REQUEST_CODE_INTRODUCE_ACTIVITY:// 介绍
				noteData = (NoteData) data
						.getSerializableExtra(ENavConsts.DEMAND_NOTE_DATA);
				// org_describe_Etv.setHint("点击查看具体内容");
				break;
			default:
				break;
			}
			// 获取选中图片的集合
			if (REQUEST_CODE_BROWERS_ACTIVITY == requestCode) {
				picture = (ArrayList<JTFile>) data
						.getSerializableExtra(ENavConsts.DEMAND_INTENT_SELECTED_PICTURE);
				selectedPictureSD.clear();
				selectedPictureSDAndNet.clear();
				if (picture != null && picture.size() > 0) {
					BitmapFactory.Options options = new BitmapFactory.Options(); 
					options.inJustDecodeBounds = true;  
				    BitmapFactory.decodeFile(picture.get(0).mLocalFilePath, options);  
				  
				    // Calculate inSampleSize  
				    options.inSampleSize = calculateInSampleSize(options, 480, 800);  
				  
				    // Decode bitmap with inSampleSize set  
				    options.inJustDecodeBounds = false;  
				      
				    Bitmap bm = BitmapFactory.decodeFile(picture.get(0).mLocalFilePath, options); 
				    picture_org_Iv.setImageBitmap(bm);
					
					// 调上传头像接口
					OrganizationPictureUploader uploader = new OrganizationPictureUploader(
							this);
					JTFile jtFile = new JTFile();
					// jtFile.setId(String.valueOf(picture.get(0).mCreateTime));
					jtFile.setId(String.valueOf(picture.get(0).mCreateTime));
					jtFile.mLocalFilePath = picture.get(0).mLocalFilePath;
					jtFile.mType = 4;

					uploader.startNewUploadTask(jtFile);
				} else {
					System.out.println("上传头像为空---");
				}
			}

			if (requestCode == 0) {

				switch (resultCode) {

				case 999:
					isNull = data.getBooleanExtra("isNull", false);
					propertyList = custom2(data, org_custom_Etv, org_main1_Ll,
							isNull, editTextViews);

					break;
				case 20:
					String market = data.getStringExtra("market");
					org_market_Etv.setText(market);
					if ("上市公司".equals(market)) {
						org_security_Etv.setVisibility(View.VISIBLE);
					} else {
						org_security_Etv.setVisibility(View.GONE);
						org_security_Etv.setText("");
					}
					break;
				case 21:
					String type = data.getStringExtra("type");
					org_type_Etv.setText(type);
					if ("政府组织".equals(type)) {
						org_market_Etv.setVisibility(View.GONE);
						org_security_Etv.setVisibility(View.GONE);
						org_security_Etv.setText("");
						org_market_Etv.setText("");
					} else {
						org_market_Etv.setVisibility(View.VISIBLE);
						if ("上市公司".equals(org_market_Etv.getText())) {
							org_security_Etv.setVisibility(View.VISIBLE);
						}

					}
					break;
				case 23:
					ArrayList<String> Module = data
							.getStringArrayListExtra("Module");
					System.out.println(data);
					if (!Module.isEmpty()) {
						for (String string : Module) {

							if (set.contains(string)) {
								continue;
							}
							set.add(string);
						}
						AddMore(set);
					}
					set.removeAll(set);
					break;
				case 24:
					String media_type = data.getStringExtra("media_type");
					org_type_Etv.setText(media_type);
					if ("政府组织".equals(media_type)) {
						org_market_Etv.setVisibility(View.GONE);
						org_security_Etv.setVisibility(View.GONE);

					} else {
						org_market_Etv.setVisibility(View.VISIBLE);
						org_security_Etv.setVisibility(View.VISIBLE);
					}

					break;
				default:
					break;
				}
			}
			if (10001 == requestCode) { //
				// 关联资源
				if (resultCode == Activity.RESULT_OK) {

					if (data.hasExtra(EConsts.Key.RELATED_PEOPLE_NODE)) {
						// 数据去重
						ConnectionNode connectionNode = (ConnectionNode) data
								.getSerializableExtra(EConsts.Key.RELATED_PEOPLE_NODE);

						if (currentRequestState == STATE_EDIT
								& currentRequestCode == REQUEST_CODE_RELATED_RESOURCE_PEOPLE) {
							connectionNodeList.set(currentRequestEditPosition,
									connectionNode);
						} else {
							connectionNodeList.add(connectionNode);
						}
						if (connectionNodeList != null
								&& !connectionNodeList.isEmpty()) {
							people_Ll.setVisibility(View.VISIBLE);
							peopleGroupAdapter.notifyDataSetChanged();
							org_asso_Tv.setVisibility(View.VISIBLE);
						}

					}
					// 相关资源
					if (data.hasExtra(EConsts.Key.RELATED_ORGANIZATION_NODE)) {
						// 数据去重
						ConnectionNode connectionNode = (ConnectionNode) data
								.getSerializableExtra(EConsts.Key.RELATED_ORGANIZATION_NODE);

						if (currentRequestState == STATE_EDIT
								& currentRequestCode == REQUEST_CODE_RELATED_RESOURCE_ORGANIZATION) {
							connectionNodeList2.set(currentRequestEditPosition,
									connectionNode);
						} else {
							// 加入列表
							connectionNodeList2.add(connectionNode);
						}
						if (connectionNodeList2 != null
								&& !connectionNodeList2.isEmpty()) {
							organization_Ll.setVisibility(View.VISIBLE);
							organizationGroupAdapter.notifyDataSetChanged();
							org_asso_Tv.setVisibility(View.VISIBLE);
						}
					}

					if (data.hasExtra(EConsts.Key.RELATED_KNOWLEDGE_NODE)) {
						// 数据去重
						KnowledgeNode knowledgeNode = (KnowledgeNode) data
								.getSerializableExtra(EConsts.Key.RELATED_KNOWLEDGE_NODE);

						if (currentRequestState == STATE_EDIT
								& currentRequestCode == REQUEST_CODE_RELATED_RESOURCE_KNOWLEDGE) {
							knowledgeNodeList.set(currentRequestEditPosition,
									knowledgeNode);
						} else {
							knowledgeNodeList.add(knowledgeNode);
						}
						if (knowledgeNodeList != null
								&& !knowledgeNodeList.isEmpty()) {
							knowledge_Ll.setVisibility(View.VISIBLE);
							knowledgeGroupAdapter.notifyDataSetChanged();
							org_asso_Tv.setVisibility(View.VISIBLE);
						}
					}

					if (data.hasExtra(EConsts.Key.RELATED_AFFAIR_NODE)) {
						// 数据去重\
						AffairNode affairNode = (AffairNode) data
								.getSerializableExtra(EConsts.Key.RELATED_AFFAIR_NODE);
						if (currentRequestState == STATE_EDIT
								& currentRequestCode == REQUEST_CODE_RELATED_RESOURCE_AFFAIR) {
							affairNodeList.set(currentRequestEditPosition,
									affairNode);
						} else {
							affairNodeList.add(affairNode);
						}
						if (affairNodeList != null && !affairNodeList.isEmpty()) {
							requirement_Ll.setVisibility(View.VISIBLE);
							requirementGroupAdapter.notifyDataSetChanged();
							org_asso_Tv.setVisibility(View.VISIBLE);
						}
					}

				}
			}

			else if (10002 == requestCode) { // 目录
				if (resultCode == Activity.RESULT_OK) {
					if (resultCode == Activity.RESULT_OK) {
						directory.clear();
						listCategory = (ArrayList<UserCategory>) data.getSerializableExtra(EConsts.Key.KNOWLEDGE_CATEGORY_LIST);
						boolean isSelect = data.getBooleanExtra(EConsts.Key.KNOWLEDGE_CATEGORY_GROUP, false);
						if (listCategory.size() == 1 && "未分组".equals(listCategory.get(0).getCategoryname())) {
							if (isSelect) {
								ArrayList<UserCategory> tmpArrayList = new ArrayList<UserCategory>();
								tmpArrayList.add(listCategory.get(listCategory.size()-1));
								updateClientCategoryUi(tmpArrayList);
								tmpArrayList.clear();
							} else {
								updateClientCategoryUi(null);
							}
						} else {
							if (listCategory.size() == 0) {
								updateClientCategoryUi(null);
							} else {
								updateClientCategoryUi(listCategory);
							}
						}
						
					}

				}
			}
			// 标签
			else if (10003 == requestCode) {

				if (resultCode == Activity.RESULT_OK) {
					selectTagList = (ArrayList<LableData>) data
							.getSerializableExtra(ENavConsts.DEMAND_LABEL_DATA);

					ArrayList<String> tagStringList = new ArrayList<String>();
					if (selectTagList != null && !selectTagList.isEmpty()) {
						for (LableData userTag : selectTagList) {
							tagStringList.add(userTag.tag);
							CustomerTag tag = null;
								tag = new CustomerTag();
								tag.tagId = userTag.id;
								tag.tagName = userTag.tag;
								lableList.add(tag);
							
						}
						organizationLabelIC.setVisibility(View.VISIBLE);
						view_Label_edit.setText(TextStrUtil.getLableDataSize(9,
								selectTagList));

						

					} else {
						organizationLabelIC.setVisibility(View.GONE);
					}
				}

			}

			// 权限
			else if (10004 == requestCode) {
				String MSG = "";
				if (resultCode == Activity.RESULT_OK) {
					listHightPermission = new ArrayList<Connections>();
					listMiddlePermission = new ArrayList<Connections>();
					listLowPermission = new ArrayList<Connections>();
					xiaoles.clear();
					zhongles.clear();
					dales.clear();
					boolean noPermission = data.getBooleanExtra("noPermission",
							false);
					Log.i(TAG, MSG + " noPermission = " + noPermission);
					listHightPermission = (ArrayList<Connections>) data
							.getSerializableExtra("listHightPermission");
					listMiddlePermission = (ArrayList<Connections>) data
							.getSerializableExtra("listMiddlePermission");
					listLowPermission = (ArrayList<Connections>) data
							.getSerializableExtra("listLowPermission");
					if (listHightPermission != null) {

						for (int i = 0; i < listHightPermission.size(); i++) {
							Permission permission = new Permission();
							permission.id = listHightPermission.get(i).getId();
							permission.name = listHightPermission.get(i)
									.getName();
							dales.add(permission);
						}
					}
					if (listMiddlePermission != null) {
						for (int i = 0; i < listMiddlePermission.size(); i++) {
							Permission permission = new Permission();
							permission.id = listMiddlePermission.get(i).getId();
							permission.name = listMiddlePermission.get(i)
									.getName();
							zhongles.add(permission);
						}
					}
					if (listLowPermission != null) {
						for (int i = 0; i < listLowPermission.size(); i++) {
							Permission permission = new Permission();
							permission.id = listLowPermission.get(i).getId();
							permission.name = listLowPermission.get(i)
									.getName();
							xiaoles.add(permission);
						}
					}

				
				}
			}
			// 获取选中图片的集合
			if (1006 == requestCode) {
				remark = data.getStringExtra("Remark_Activity");
				org_describe_Etv.setText(remark);
			}
		}
	}

	private void updateClientCategoryUi(ArrayList<UserCategory> listCategory) {
		if (listCategory != null && !listCategory.isEmpty()) {
			organizationTreeIC.setVisibility(View.VISIBLE); // 显示
			ArrayList<String> strings = new ArrayList<String>();
			for (int i = 0; i < listCategory.size(); i++) {
				strings.add(TextStrUtil.checkCategoryname(listCategory.get(i)));
				directory.add(listCategory.get(i).getId() + "");
			}
			view_Tree_edit.setText(TextStrUtil.getStringSize(9,strings));
		} else {
			directory.clear();
			organizationTreeIC.setVisibility(View.GONE); // 隐藏
		}
		
	}

	@Override
	public void bindData(int tag, Object object) {

		switch (tag) {

		case OrganizationReqType.ACCESS_TO_THE_PRIMARY_KEY: // 获取客户主键ID
			if (object != null) {
				Map<String, Object> dataHm = (Map<String, Object>) object;
				getId = (GetId) dataHm.get("getId");
				Boolean isSuccess = getId.success;
				if (isSuccess) {
					// Toast.makeText(context, "获取客户主键ID成功", 0).show();
				} else {
					ToastUtil.showToast(context, getId.msg);
				}
				Log.e("MSG", "客户主键ID" + getId);

			}

			break;
		case OrganizationReqType.ORGANIZATION_REQ_CUSTOMER_SAVECUSPROFILE:
			Boolean success = (Boolean) object;
			if (success != null && success) {

				if (success == true) {
					// Log.e("MSG", "保存客户信息成功"+isSuccess);
					if (activity_type == 1) {
						ToastUtil.showToast(context, "创建成功");
					} else if (activity_type == 2) {
						ToastUtil.showToast(context, "编辑成功");
					} else if (activity_type == 3) {
						ToastUtil.showToast(context, "保存成功");
					} else if (activity_type == 4) {
						ToastUtil.showToast(context, "转为成功");
					}

					if (activity_type == 1 || activity_type == 3
							|| activity_type == 4) {

						Intent intent = new Intent(this,
								ClientDetailsActivity.class);
						intent.putExtra("Organ_Id", orgId);
						intent.putExtra("label", label);
						startActivityForResult(intent, 10000);
						finish();
					} else {
						finish();
					}
				} else if (success == false) {
					ToastUtil.showToast(context, "保存失败");
					// Log.e("MSG", "保存客户信息失败"+isSuccess);
					return;
				}
			}
			break;
		default:
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String keyword = org_name_Etv.getText().toString().trim();
		if (StringUtils.isEmpty(keyword)) {
			ToastUtil.showToast(context, "客户名称不能为空");
			return;
		}
		currentRequestEditPosition = position;
		currentRequestState = STATE_EDIT;

		if (parent == people) { // 编辑人脉
			currentRequestCode = REQUEST_CODE_RELATED_RESOURCE_PEOPLE;
			ENavigate.startRelatedResourceActivityForResult(this, 10001,
					keyword, ResourceType.People,
					connectionNodeList.get(position));
		} else if (parent == organization) { // 编辑组织
			currentRequestCode = REQUEST_CODE_RELATED_RESOURCE_ORGANIZATION;
			ENavigate.startRelatedResourceActivityForResult(this, 10001,
					keyword, ResourceType.Organization,
					connectionNodeList2.get(position));
		} else if (parent == knowledge) { // 编辑知识
			currentRequestCode = REQUEST_CODE_RELATED_RESOURCE_KNOWLEDGE;
			ENavigate.startRelatedResourceActivityForResult(this, 10001,
					keyword, ResourceType.Knowledge,
					knowledgeNodeList.get(position));
		} else if (parent == requirement) { // 编辑事件
			currentRequestCode = REQUEST_CODE_RELATED_RESOURCE_AFFAIR;
			ENavigate.startRelatedResourceActivityForResult(this, 10001,
					keyword, ResourceType.Affair, affairNodeList.get(position));
		}

	}

	public void setChooseText(ArrayList<Metadata> data) {
		switch (dataIndex) {
		case 1:
			// // 类型
			// if (metadataType != null) {
			// metadataType.clear();
			// }
			// metadataType = data;
			// demandType
			// .setText(ChooseDataUtil.getMetadataName(metadataType, 9));
			break;
		case 2:
			// 行业
			if (metadataIndustry != null) {
				metadataIndustry.clear();
				industry.clear();
				industryIds.clear();
			}
			metadataIndustry = data;
			org_industry_Etv.setText(ChooseDataUtil.getMetadataName(
					metadataIndustry, 9));
//			industry.add(org_industry_Etv.getText().toString());// 客户行业

			if (metadataIndustry != null) {
				if (!metadataIndustry.isEmpty()) {
					for (Metadata Industrydata : metadataIndustry) {

						// 有二级
						for (Metadata data2 : Industrydata.childs) {
							// 有三级
							for (Metadata data3 : data2.childs) {
								industryIds.add(data3.id);
								industry.add(data3.name);
							}
							industryIds.add(data2.id);
							industry.add(data2.name);
						}
						industryIds.add(Industrydata.id);
						industry.add(Industrydata.name);
					}
					customer.industrys = industry;
					customer.industryIds = industryIds;
				}

			}
			break;
		case 3:
			// 地区
			if (metadataArea != null) {
				metadataArea.clear();
			}
			metadataArea = data;

			area_result = ChooseDataUtil.getMetadataName(metadataArea);
			org_address_Etv.setText(getAreaStr(area_result));
			break;
		}

	}

	public String getAreaStr(Area area_result) {
		String area = "";
		if (area_result != null) {
			String province = TextUtils.isEmpty(area_result.province) ? ""
					: area_result.province;
			String city = TextUtils.isEmpty(area_result.city) ? ""
					: area_result.city;
			String county = TextUtils.isEmpty(area_result.county) ? ""
					: area_result.county;
			if (city.equalsIgnoreCase(province)) {
				area = province + county;
			} else {
				area = province + city + county;
			}

			// KeelLog.e("==>>province" ,province);
			// KeelLog.e("==>>city" ,city);
			// KeelLog.e("==>>county" ,county);
			// KeelLog.e("==>>area" ,area);
		}
		return area;
	}

	/**
	 * 上传图片
	 */
	@Override
	public void onPrepared(String id) {

	}

	@Override
	public void onStarted(String id) {

	}

	@Override
	public void onUpdate(String id, int progress) {

	}

	@Override
	public void onCanceled(String id) {

	}

	@Override
	public void onSuccess(String id, Map<String, String> result) {
		//
		// "url":"全路径",
		// "urlToSql":"相对路径，需传到后台的",

		final String url = result.get("url"); //
		avatarUrlToSql = result.get("urlToSql");
		if (activity_type == 2) {
			avatarUrlToSqlEditClient = result.get("urlToSql");
		}
		KeelLog.d("===>>onSuccess", url);
		if (!TextUtils.isEmpty(url)) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					ToastUtil.showToast(CreateClienteleActivity.this, "上传成功");
				}
			});

		}
	}
	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
		}

		return inSampleSize;
	}
	@Override
	public void onError(String id, int code, final String message) {
		KeelLog.d("===>>OnError", message);
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				ToastUtil.showToast(context, message);
			}
		});

	}

}
