package com.tr.ui.demand;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.common.constvalue.EnumConst.ModuleType;
import com.tr.App;
import com.tr.R;
import com.tr.api.DemandReqUtil;
import com.tr.api.UserReqUtil;
import com.tr.model.api.DataBox;
import com.tr.model.demand.ASSOData;
import com.tr.model.demand.ASSORPOK;
import com.tr.model.demand.AmountData;
import com.tr.model.demand.ChoiceObj;
import com.tr.model.demand.CustomData;
import com.tr.model.demand.DemandASSO;
import com.tr.model.demand.DemandASSOData;
import com.tr.model.demand.DemandData;
import com.tr.model.demand.DemandDetailsData;
import com.tr.model.demand.LableData;
import com.tr.model.demand.LeData;
import com.tr.model.demand.Metadata;
import com.tr.model.demand.NoteData;
import com.tr.model.demand.PermissionsData;
import com.tr.model.demand.RelationData;
import com.tr.model.demand.TemplateData;
import com.tr.model.demand.TitleData;
import com.tr.model.demand.ValueData;
import com.tr.model.joint.AffairNode;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.joint.KnowledgeNode;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.knowledge.UserCategory;
import com.tr.model.obj.AffairsMini;
import com.tr.model.obj.Connections;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.adapter.demand.AffairResourceAdapter;
import com.tr.ui.adapter.demand.ConnectionResourceAdapter;
import com.tr.ui.adapter.demand.KnowledgeResourceAdapter;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.JointResourceFragment.ResourceType;
import com.tr.ui.demand.util.ChooseDataUtil;
import com.tr.ui.demand.util.DemandAction;
import com.tr.ui.demand.util.TextStrUtil;
import com.tr.ui.knowledge.CreateKnowledgeActivity;
import com.tr.ui.knowledge.SelectConnectionsActivity;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.people.cread.CustomActivity;
import com.tr.ui.widgets.BasicListView2;
import com.tr.ui.widgets.MessageDialog;
import com.tr.ui.widgets.MessageDialog.OnDialogFinishListener;
import com.utils.common.EConsts;
import com.utils.common.TaskIDMaker;
import com.utils.common.ViewHolder;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.log.ToastUtil;
import com.utils.time.Util;

/**
 * 创建新需求界面：可保存创建模版界面，和需求详情信息
 * 
 * @author Administrator
 * 
 */
public class NewDemandActivity extends JBaseActivity implements
		OnClickListener, OnItemClickListener, IBindData {
	public static final int REQUEST_CODE_CUSTOM_ACTIVITY = 1001;// 启动自定义组件的回调
	public static final int REQUEST_CODE_RELATED_RESOURCE_ACTIVITY = 1002; // 启动关联的回调
	public static final int REQUEST_CODE_GLOBAL_KNOWLEDGE_CATEGORY_ACTIVITY = 1003; // 启动目录的回调
	public static final int REQUEST_CODE_LABLE_ACTIVITY = 1004;// 启动标签的回调
	public static final int REQUEST_CODE_KNOWLEDGE_PERMISSION_ACTIVITY = 1005;// 启动权限控制的回调
	public static final int REQUEST_CODE_INTRODUCE_ACTIVITY = 1006;// 启动介绍回调
	/** 确定 请求时 人脉，知识，组织，事件等信息 */
	public static final int REQUEST_CODE_RELATED_RESOURCE_PEOPLE = 2001;
	public static final int REQUEST_CODE_RELATED_RESOURCE_ORGANIZATION = 2002;
	public static final int REQUEST_CODE_RELATED_RESOURCE_KNOWLEDGE = 2003;
	public static final int REQUEST_CODE_RELATED_RESOURCE_AFFAIR = 2004;

	/** 权限控制器 */
	public static final int REQUEST_CODE_HIGHT_PERMISSION = 3001;
	public static final int REQUEST_CODE_MIDDLE_PERMISSION = 3002;
	public static final int REQUEST_CODE_LOW_PERMISSION = 3003;
	private TextView titleTv;
	/** 基本属性 */
	private EditText demandTitel;// 标签
	private TextView demandIntro;// 介绍 //介绍内容文本输入框
	private TextView demandType;// 类型
	private TextView demandIndustry;// 行业
	private TextView demandArea;// 地区
	private TextView demandAmount;// 金额

	/** 自定义拓展字段 */
	private LinearLayout demandCustom;// 自定义字段
	private ArrayList<CustomData> customData = new ArrayList<CustomData>();// 自定义字段的信息
	// private CustomAdapter customAdapter;
	/** 联系人信息 */
	private EditText demandUser;// 联系人
	private EditText demandPhone;// 电话号码
	private EditText demandCord;// 验证码
	private TextView demandCordBt;// 获取验证码
	/** 四大组件 人脉，组织，知识，事件 */
	private TextView associatedTv;
	private View demandContacts;// 人脉
	private BasicListView2 demandContactsLl;// 添加人脉信息
	private View contactsIv;
	private View demandOrganization;// 组织
	private BasicListView2 demandOrganizationLl;// 添加组织信息
	private View organizationIv;
	private View demandKnowledge;// 知识
	private BasicListView2 demandKnowledgeLl;// 添加知识信息
	private View knowledgeIv;
	private View demandEvent;// 事件
	private BasicListView2 demandEventLl;// 添加事件信息
	private View eventIv;
	private ScrollView demandScrollView;// 自动滑动到底部
	/**
	 * 标签信息， 目录信息，权限控制
	 */
	// 编辑的时候 获取的标签和目录信息
	private ArrayList<LableData> lableList;// 标签
	private ArrayList<UserCategory> categoryList;// 目录
	private View viewTree;
	private View viewLabel;
	private View viewPlace;
	/** 目录和标签显示的文本 */
	private TextView treeTv;
	private TextView LabelTv;

	private ArrayList<LableData> lableData = new ArrayList<LableData>();// 标签对象数据
	private ArrayList<UserCategory> treeData = new ArrayList<UserCategory>();// 目录对象数据

	/**
	 * 保存信息
	 */
	private ArrayList<ConnectionNode> contactsNode = new ArrayList<ConnectionNode>();// 人脉信息
	private ArrayList<ConnectionNode> organizationNode = new ArrayList<ConnectionNode>();// 组织信息
	private ArrayList<KnowledgeNode> knowledgeNode = new ArrayList<KnowledgeNode>();// 知识信息
	private ArrayList<AffairNode> eventNode = new ArrayList<AffairNode>();// 事件信息
	private int ResourceNode = 0;
	/** 关联信息的 数据适配 */
	private ConnectionResourceAdapter contactsAdapter;
	private ConnectionResourceAdapter organizationAdapter;
	private KnowledgeResourceAdapter knowledgeAdapter;
	private AffairResourceAdapter eventAdapter;

	/**
	 * 界面信息数据
	 */
	private ArrayList<Metadata> metadataType;// 类型
	private ArrayList<Metadata> metadataIndustry;// 行业
	private ArrayList<Metadata> metadataArea;// 区域
	private int dataIndex = 0; // 信息id 确定当前 点击后的是 行业还是类型 还是地区
	private int type;
	private TemplateData bean; // 模版基本信息
	private List<String> categoryIdsList = new ArrayList<String>();// 选中的目录id

	private AmountData money;// 金额信息

	private int currentRequestEditPosition;// 当前选择编辑的item项
	private int currentRequestCode; // 当前请求返回值

	/**
	 * 保存模版时的提示框
	 */
	private Dialog dialog;
	private EditText editTemple; // 输入框
	/**
	 * 编辑关联信息的枚举信息
	 */
	private ResourceNodeEnum presourceEnum = ResourceNodeEnum.Default;

	/** 四大组件信息回调状态控制器 */
	private enum ResourceNodeEnum {
		Default, Add, Edit
	}

	/**
	 * 获取验证码
	 */
	private final String TIP_EMPTY_VCODE = "请输入验证码";
	private final String TIP_REGET_VERIFY_CODE = "重获验证码";
	private final String TIP_GET_VERIFY_CODE = "获取验证码";

	private final int EXPIRED_TIME = 60; // 验证码超时时间,60s
	private final int COUNTDOWN_INTERVAL = 1000; // 倒计时时间间隔
	private final int MSG_BASE = 100;
	private final int MSG_COUNT_DOWN = MSG_BASE + 1; // 倒计时的消息标识
	private int mCountdownLeft; // 倒计时剩余时间
	private Timer mTimer; //

	/** 权限控制基本信息属性 */
	private CheckBox noPermissionSwitchCb;
	private RelativeLayout lowPermissionRl;
	private TextView lowPermissionContentTv;
	private RelativeLayout middlePermissionRl;
	private TextView middlePermissionContentTv;
	private RelativeLayout highPermissionRl;
	private TextView highPermissionContentTv;
	private ImageView lowPermissionRightArrowsIv;
	private ImageView middlePermissionRightArrowsIv;
	private ImageView highPermissionRightArrowsIv;
	// 大乐权限人脉对象列表
	private ArrayList<Connections> listHightPermission = new ArrayList<Connections>();
	// 中乐权限人脉对象列表
	private ArrayList<Connections> listMiddlePermission = new ArrayList<Connections>();
	// 小乐权限人脉对象列表
	private ArrayList<Connections> listLowPermission = new ArrayList<Connections>();

	private HashMap<String, String> LimitsMap = new HashMap<String, String>();
	private boolean noPermission = true;

	// 数据上传封装对象
	private String cord;// 输入的验证码
	private TitleData title = new TitleData(true);// 标题
	private ValueData user = new ValueData(true); // 联系人
	private ValueData phone = new ValueData(true);// 手机号
	private PermissionsData permission = new PermissionsData();// 权限控制
	private NoteData noteData = new NoteData(true); // 介绍内容
	/**
	 * 编辑对象
	 */
	private DemandDetailsData detailsEdit;

	/**
	 * 需求创建的枚举对象
	 * 
	 * @author Administrator
	 *
	 */
	public enum DemandEnum {
		Default, // 正常的创建
		Add, // 通过详情保存的状态
		Edit, // 通过详情 编辑的状态
		Callback // 通过四大组件 回调的状态， 不跳转到详情
	}

	DemandEnum demandEnum = DemandEnum.Default;
	private String phoneEdit="";// 手机号

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demand_new);
		getParam();
		initView();
		mTimer = new Timer();
		mCountdownLeft = EXPIRED_TIME; // 60s倒计时时间
		initComponent();
		initLimitsMap();
		// 如果不是空的 就代表了编辑对象
		if (detailsEdit != null) {
			titleTv.setText("编辑需求");
			LimitsMap.clear();
			toDetailsEdit();
			if (demandEnum == DemandEnum.Add) {
				initLimitsMap();
			}
		}
		resetDemandCustom();
	}

	/**
	 * "0介绍","2行业","1类型","3区域","4金额","5关联"
	 */
	public static String[] limits = { "介绍", "类型", "行业", "区域", "金额", "关联" };

	/**
	 * 创建时的选中效果
	 */
	private void initLimitsMap() {
		for (String str : limits) {
			LimitsMap.put(str, str);
		}
	}

	/**
	 * 获取上级页面传递的数据
	 */
	private void getParam() {
		Intent intent = this.getIntent();
		demandEnum = (DemandEnum) intent
				.getSerializableExtra(ENavConsts.DEMAND_FOR_RESULT);
		if (demandEnum == null) { // true
			demandEnum = DemandEnum.Default;
		}
		bean = (TemplateData) intent
				.getSerializableExtra(ENavConsts.DEMAND_NEW);
		if (bean != null) {
			if (bean.listProperty != null) {
				CustomData(bean.listProperty);
			}
		}
		detailsEdit = (DemandDetailsData) intent
				.getSerializableExtra(ENavConsts.DEMAND_EDIT);// 编辑
		lableList = (ArrayList<LableData>) intent
				.getSerializableExtra(ENavConsts.DEMAND_LABEL_DATA);// 标签
		categoryList = (ArrayList<UserCategory>) intent
				.getSerializableExtra(ENavConsts.DEMAND_CATEGORY_DATA);// 目录
		type = intent.getIntExtra(ENavConsts.DEMAND_TYPE,
				ChooseDataUtil.CHOOSE_TYPE_DEFAULT);
		if (type == ChooseDataUtil.CHOOSE_type_OutInvestType) {
			titleTv.setText("投资需求");
		}
		if (type == ChooseDataUtil.CHOOSE_type_InInvestType) {
			titleTv.setText("融资需求");
		}
	}

	/**
	 * 创建需求的时候 目录还原
	 * 
	 * @param listProperty
	 */
	private void CustomData(ArrayList<String> listProperty) {
		for (String proper : listProperty) {
			customData.add(new CustomData(proper, true));
			LimitsMap.put(proper, proper);
		}
	}

	/**
	 * 重置自定义布局
	 */
	private void resetDemandCustom() {
		demandCustom.removeAllViews();
		int point = 0;
		for (CustomData custom : customData) {
			View convertView = View.inflate(NewDemandActivity.this,
					R.layout.demand_include_view1, null);
			TextView tagTv = ViewHolder.get(convertView, R.id.view_tv_name);
			tagTv.setText(custom.key);
			final EditText tvContent = ViewHolder.get(convertView,
					R.id.view_et_edit);
			tvContent.setText(custom.value);
			final int editPoint = point;
			tvContent.addTextChangedListener(new TextWatcher() {

				@Override
				public void afterTextChanged(Editable s) {

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {

				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					CustomData customEditTemp = customData.get(editPoint);
					String value = s.toString();
					// if (value.length() > 10) {
					// value = value.substring(0, 10);
					// Toast.makeText(NewDemandActivity.this, "最多只能输入10个字",
					// Toast.LENGTH_SHORT).show();
					// tvContent.setText(value);
					// tvContent.setSelection(value.length());
					// }
					customEditTemp.value = value;

				}

			});
			demandCustom.addView(convertView);
			point++;
		}
	}

	/**
	 * 编辑状态下的数据获取
	 */
	private void toDetailsEdit() {
		title = detailsEdit.title;// 必须有的值
		if (title == null) {
			title = new TitleData("", true);
		}
		user = detailsEdit.contact;// 联系人信息
		if (user == null) {
			user = new ValueData();
			user.isVisable = true;
			user.value = "";
		}
		phone = detailsEdit.phone;// 联系电话信息
		if (phone == null) {
			phone = new ValueData();
			phone.isVisable = true;
			phone.value = "";
		}
		phoneEdit = phone.value;
		money = detailsEdit.amount;// 金额信息
		noteData = detailsEdit.note;// 介绍内容信息
		if (noteData!=null&&!TextUtils.isEmpty(noteData.value)) {
			demandIntro.setHint("点击查看具体内容");
		}
		detailsEdit.tags = "";
		demandTitel.setText(detailsEdit.title.value);// 编辑
		// 标题
		if (detailsEdit.type != null) { // 获取类型 并显示
			demandType.setText(TextStrUtil.getDemandDataSize(13,
					detailsEdit.type.list));
			metadataType = (ArrayList<Metadata>) ChoiceObj
					.toMetadataList(detailsEdit.typeObj);
			if (detailsEdit.type.isVisable) {
				// 添加项目
				LimitsMap.put(limits[1], limits[1]);// 类型 可见
			}
		} else {
			detailsEdit.type = new RelationData();
			detailsEdit.type.isVisable = true;
		}
		if (detailsEdit.area != null) { // 区域
			demandArea.setText(TextStrUtil.getDemandDataAreaSize(10,
					detailsEdit.area.list));
			metadataArea = (ArrayList<Metadata>) ChoiceObj
					.toMetadataList(detailsEdit.areaObj);
			if (detailsEdit.area.isVisable) {
				// 添加项目
				LimitsMap.put(limits[3], limits[3]);// 可见
			}
		} else {
			detailsEdit.area = new RelationData();
			detailsEdit.area.isVisable = true;
		}
		if (detailsEdit.industry != null) { // 行业
			demandIndustry.setText(TextStrUtil.getDemandDataSize(10,
					detailsEdit.industry.list));
			metadataIndustry = (ArrayList<Metadata>) ChoiceObj
					.toMetadataList(detailsEdit.industryObj);
			if (detailsEdit.industry.isVisable) {
				// 添加项目
				LimitsMap.put(limits[2], limits[2]);// 可见
			}
		} else {
			detailsEdit.industry = new RelationData();
			detailsEdit.industry.isVisable = true;
		}
		demandUser.setText(user.value);
		demandPhone.setText(phone.value); // 手机号
		// 金额信息处理
		if (money != null) {
			if (!TextUtils.isEmpty(money.unit))
				demandAmount.setText(money.unit + "-" + money.getAmountData());// 金额
			if (money.isVisable) {
				// 添加项目
				LimitsMap.put(limits[4], limits[4]);// 类型 可见
			}
		}
		if (noteData != null) {
			if (noteData.isVisable) {
				// 添加项目
				LimitsMap.put(limits[0], limits[0]);// 类型 可见
			}
		} else {
			noteData = new NoteData();
			noteData.isVisable = true;
		}
		// 获取自定义信息
		customData = (ArrayList<CustomData>) detailsEdit.customList;
		if (customData == null) {
			customData = new ArrayList<CustomData>();
		} else {
			demandCustom.removeAllViews();
			for (CustomData data : customData) {
				if (data.isVisable) {
					LimitsMap.put(data.value, data.value);// 添加编辑
				}
			}
			resetDemandCustom();
		}

		// 获取关联关系
		if (detailsEdit.asso != null) {
			if (detailsEdit.asso.isVisable) {
				LimitsMap.put(limits[5], limits[5]);
			}
			assoToNode(detailsEdit.asso);
		}

		// 获取标签
		if (lableList != null && lableList.size() > 0) {
			// 显示标签
			viewLabel.setVisibility(View.VISIBLE);
			LabelTv.setText(TextStrUtil.getLableDataSize(9, lableList));
			lableData.addAll(lableList);
			lableList.clear();// 清除数据
		}
		// 获取目录
		if (categoryList != null && categoryList.size() > 0) {
			// 显示目录
			viewTree.setVisibility(View.VISIBLE);
			treeData.clear();
			treeData.addAll(categoryList);
			treeTv.setText(TextStrUtil.getCategoryDataSize(9, treeData));
			for(UserCategory category : categoryList){
				categoryIdsList.add(category.id+"");
			}
			categoryList = null;
		}
		listLowPermission(detailsEdit.permIds);
		updateAllPermissionContentTv();
		viewPlace.setVisibility(View.VISIBLE);
	}

	private void initView() {
		demandScrollView = (ScrollView) this
				.findViewById(R.id.demandScrollView);// 滑动块
		View viewTitle = this.findViewById(R.id.demandTitleIC);
		View viewIntro = this.findViewById(R.id.demandIntroIC);// 介绍
		View viewType = this.findViewById(R.id.demandTypeIC);// 类型
		View viewIndustry = this.findViewById(R.id.demandIndustryIC);// 行业
		View viewArea = this.findViewById(R.id.demandAreaIC);// 地区
		View viewAmount = this.findViewById(R.id.demandAmountIC);// 金额
		View viewCustom = this.findViewById(R.id.demandCustomIC);// 自定义
		View viewUser = this.findViewById(R.id.demandUserIC);
		View viewPhone = this.findViewById(R.id.demandPhoneIC);
		View viewCord = this.findViewById(R.id.demandCodeIC);

		associatedTv = (TextView) this.findViewById(R.id.demand_associatedTv);
		viewPlace = this.findViewById(R.id.placeLl);
		viewTree = this.findViewById(R.id.demandTreeIC);// 目录信息
		viewLabel = this.findViewById(R.id.demandLabelIC);// 标签信息
		treeTv = (TextView) viewTree.findViewById(R.id.view_et_edit);
		LabelTv = (TextView) viewLabel.findViewById(R.id.view_et_edit);

		demandTitel = (EditText) viewTitle.findViewById(R.id.view_et_edit);
		demandIntro = (TextView) viewIntro.findViewById(R.id.view_et_edit);
		demandType = (TextView) viewType.findViewById(R.id.view_et_edit);
		demandIndustry = (TextView) viewIndustry
				.findViewById(R.id.view_et_edit);
		demandArea = (TextView) viewArea.findViewById(R.id.view_et_edit);
		demandAmount = (TextView) viewAmount.findViewById(R.id.view_et_edit);

		demandUser = (EditText) viewUser.findViewById(R.id.view_et_edit);
		demandPhone = (EditText) viewPhone.findViewById(R.id.view_et_edit);
		demandCord = (EditText) viewCord.findViewById(R.id.code_et_edit);
		demandCordBt = (TextView) viewCord.findViewById(R.id.code_tv_btn);
		demandCordBt.setText(TIP_GET_VERIFY_CODE);
		demandCordBt.setOnClickListener(this);
		/** 四大组件 */
		demandContacts = this.findViewById(R.id.demandContactsIc);
		demandContactsLl = (BasicListView2) demandContacts
				.findViewById(R.id.associatedLv);
		demandContactsLl.setOnItemClickListener(this);
		contactsAdapter = new ConnectionResourceAdapter(this, contactsNode,new OnClickListener() {

					@Override
					public void onClick(View v) {
						contactsNode.remove(v.getTag());
						contactsAdapter.notifyDataSetInvalidated();
						if (contactsNode.size() == 0) {
							ResourceNode--;
							demandContacts.setVisibility(View.GONE);
						}
						if (ResourceNode == 0) {
							associatedTv.setVisibility(View.GONE);
						}
					}
				});
		demandContactsLl.setAdapter(contactsAdapter);
		contactsIv = demandContacts.findViewById(R.id.associatedline);
		demandOrganization = this.findViewById(R.id.demandOrganizationIc);
		organizationAdapter = new ConnectionResourceAdapter(this,
				organizationNode, new OnClickListener() {

					@Override
					public void onClick(View v) {
						organizationNode.remove(v.getTag());
						organizationAdapter.notifyDataSetInvalidated();
						if (organizationNode.size() == 0) {
							ResourceNode--;
							demandOrganization.setVisibility(View.GONE);
						}
						if (ResourceNode == 0) {
							associatedTv.setVisibility(View.GONE);
						}
					}
				});
		demandOrganizationLl = (BasicListView2) demandOrganization
				.findViewById(R.id.associatedLv);
		demandOrganizationLl.setOnItemClickListener(this);
		demandOrganizationLl.setAdapter(organizationAdapter);
		organizationIv = demandOrganization.findViewById(R.id.associatedline);
		demandKnowledge = this.findViewById(R.id.demandKnowledgeIc);
		knowledgeAdapter = new KnowledgeResourceAdapter(this, knowledgeNode,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						knowledgeNode.remove(v.getTag());
						knowledgeAdapter.notifyDataSetInvalidated();
						if (knowledgeNode.size() == 0) {
							demandKnowledge.setVisibility(View.GONE);
							ResourceNode--;
						}
						if (ResourceNode == 0) {
							associatedTv.setVisibility(View.GONE);
						}
					}
				});
		demandKnowledgeLl = (BasicListView2) demandKnowledge
				.findViewById(R.id.associatedLv);
		demandKnowledgeLl.setOnItemClickListener(this);
		demandKnowledgeLl.setAdapter(knowledgeAdapter);
		knowledgeIv = demandKnowledge.findViewById(R.id.associatedline);
		demandEvent = this.findViewById(R.id.demandEventIc);
		eventAdapter = new AffairResourceAdapter(this, eventNode,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						eventNode.remove(v.getTag());
						eventAdapter.notifyDataSetInvalidated();
						if (eventNode.size() == 0) {
							ResourceNode--;
							demandEvent.setVisibility(View.GONE);
						}
						if (ResourceNode == 0) {
							associatedTv.setVisibility(View.GONE);
						}
					}
				});
		demandEventLl = (BasicListView2) demandEvent
				.findViewById(R.id.associatedLv);
		demandEventLl.setOnItemClickListener(this);
		eventIv = demandEvent.findViewById(R.id.associatedline);
		demandEventLl.setAdapter(eventAdapter);
		/**
		 * 自定义信息
		 */
		demandCustom = (LinearLayout) this.findViewById(R.id.demandCustomBLv);
		// customAdapter = new CustomAdapter();
		// demandCustom.setAdapter(customAdapter);
		TextView text = (TextView) viewCustom.findViewById(R.id.view_et_edit);
		text.setText("点击填写更多");
		text.setTextColor(getResources().getColor(R.color.demand_text_gray));
		text.setGravity(Gravity.RIGHT);
		treeTv.setTextColor(getResources().getColor(R.color.demand_text_gray));
		treeTv.setGravity(Gravity.RIGHT);
		LabelTv.setTextColor(getResources().getColor(R.color.demand_text_gray));
		LabelTv.setGravity(Gravity.RIGHT);
		demandTitel.setHint("请输入需求标题");
		demandTitel
				.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
						120) });
		demandIntro.setHint("点击填写介绍内容");
		demandUser.setHint("请输入联系人");
		demandPhone.setHint("请输入手机号");
		demandCord.setHint("请输入验证码");
		setItemText(demandContacts, "人脉");
		setItemText(demandOrganization, "组织");
		setItemText(demandKnowledge, "知识");
		setItemText(demandEvent, "事件");
		demandPhone.setKeyListener(new NumberKeyListener() {
			@Override
			protected char[] getAcceptedChars() {
				return new char[] { '1', '2', '3', '4', '5', '6', '7', '8',
						'9', '0' };
			}

			@Override
			public int getInputType() {
				// TODO Auto-generated method stub
				return android.text.InputType.TYPE_CLASS_PHONE;
			}
		});
		setItemText(viewTitle, "标题");
		setItemText(viewIntro, "介绍");
		setItemText(viewType, "类型");
		setItemText(viewIndustry, "行业");
		setItemText(viewArea, "区域");
		setItemText(viewAmount, "金额");
		setItemText(viewCustom, "自定义");
		setItemText(viewUser, "联系人");
		setItemText(viewPhone, "手机号");
		setItemText(viewCord, "验证码");

		setItemText(viewTree, "目录");
		setItemText(viewLabel, "标签");
		/* 给项 添加点击事件 */
		setOnItemClick(viewIntro);
		setOnItemClick(viewType);
		setOnItemClick(viewIndustry);
		setOnItemClick(viewArea);
		setOnItemClick(viewAmount);
		setOnItemClick(viewCustom);
		setOnItemClick(viewTree);
		setOnItemClick(viewLabel);
		/** 底部四个标签 */
		setOnItemClick(this.findViewById(R.id.relevanceLl)); // 关联按钮
		setOnItemClick(this.findViewById(R.id.treeLl)); // 目录按钮
		setOnItemClick(this.findViewById(R.id.labelLl)); // 标签按钮
		setOnItemClick(this.findViewById(R.id.jurisdictionLl)); // 权限控制按钮
	}

	/**
	 * 设置View的点击
	 * 
	 * @param view
	 */
	private void setOnItemClick(View view) {
		view.setOnClickListener(this);
	}

	/**
	 * 设置item 左侧名称
	 * 
	 * @param view
	 * @param itemtitle
	 */
	private void setItemText(View view, String itemtitle) {
		setItemText(view, itemtitle, R.id.view_tv_name);
	}

	private void setItemText(View view, String itemTitle, int titleid) {
		TextView title = (TextView) view.findViewById(titleid);
		title.setText(itemTitle);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.demandIntroIC:
			// 介绍页面
			ENavigate.startIntroduceActivity(this, noteData,
					REQUEST_CODE_INTRODUCE_ACTIVITY);
			break;
		case R.id.demandTypeIC:
			// 类型
			dataIndex = 1;
			ENavigate.startChooseActivityForResult(this, true, "类型", type,
					metadataType);
			break;
		case R.id.demandAmountIC:
			// 金额
			ENavigate.startAmountOfMoneyActivity(this,
					ENavConsts.ActivityReqCode.REQUEST_MOENY_SELECT, money);
			break;
		case R.id.demandIndustryIC:
			// 行业
			dataIndex = 2;
			ENavigate.startChooseActivityForResult(this, true, "行业",
					ChooseDataUtil.CHOOSE_type_Trade, metadataIndustry);
			break;
		case R.id.demandAreaIC:
			// 地区
			dataIndex = 3;
			ENavigate.startChooseActivityForResult(this, true, "区域",
					ChooseDataUtil.CHOOSE_type_Area, metadataArea);
			break;
		case R.id.demandCustomIC:
			// 自定义
			stapCustom();
			break;
		case R.id.code_tv_btn:
			// 获取验证码
			if (TextUtils.isEmpty(demandPhone.getText().toString())) {
				showToast("您未输入手机号");
				return;

			}
			// if (!EUtil.isMobileNO(demandPhone.getText().toString())) {
			// showToast("手机号格式不正确");
			// return;
			// }
			getCord();
			break;
		case R.id.relevanceLl:
			// 关联按钮
			presourceEnum = ResourceNodeEnum.Add;
			ENavigate.startRelatedResourceActivityForResult(this,
					REQUEST_CODE_RELATED_RESOURCE_ACTIVITY, "",
					ResourceType.People, null);
			break;
		case R.id.treeLl: // 底部目录菜单
		case R.id.demandTreeIC: // 目录项
			ENavigate.startKnowledgeCategoryActivityForResult(this,
					REQUEST_CODE_GLOBAL_KNOWLEDGE_CATEGORY_ACTIVITY, treeData,
					ModuleType.DEMAND, true,"创建需求");
			break;
		case R.id.labelLl:
		case R.id.demandLabelIC:// 标签项
			// 标签按钮
			ENavigate.startCheckLabelActivity(this,
					REQUEST_CODE_LABLE_ACTIVITY, lableData,CreateLabelActivity.ModulesType.DemandModules);
			break;
		case R.id.jurisdictionLl:
			// 权限控制按钮
			// 检查情况
			ArrayList<String> ss = new ArrayList<String>();
			for (CustomData custom : customData) {
				ss.add(custom.key);
			}
			// 设置选择
			ENavigate.startLimitsActivity(this,
					REQUEST_CODE_KNOWLEDGE_PERMISSION_ACTIVITY, LimitsMap, ss);
			break;
		case R.id.cancelTv:// 取消保存
			dialog.dismiss();
			break;
		case R.id.confirmTv: // 保存模版按钮
			if (editTemple != null
					&& !TextUtils.isEmpty(editTemple.getText().toString())) {
				showLoadingDialog();
				String s = editTemple.getText().toString();
				if (!TextUtils.isEmpty(s)) {
					TemplateData template = new TemplateData();// 保存信息
					template.name = s;
					template.demandType = type;
					for (CustomData custom : customData) {
						template.listProperty.add(custom.key);
					}
					DemandReqUtil.saveTemplatelist(this, this, template, null);
					dialog.dismiss();
				}
			} else {
				showToast("请输入模板名称");
			}
			break;
		}
	}

	/**
	 * 打开自定义组件信息界面
	 */
	private void stapCustom() {
		Intent intent = new Intent(this, CustomActivity.class);
		intent.putExtra("fengxing", true);
		ArrayList<CustomerPersonalLine> custom = new ArrayList<CustomerPersonalLine>();
		for (CustomData data : customData) {
			custom.add(CustomData.toCustomerPersonalLine(data));
		}
		Bundle bundle = new Bundle();
		bundle.putSerializable("Customer_Bean", custom);
		intent.putExtras(bundle);
//		intent.putParcelableArrayListExtra("Customer_Bean", custom);
		this.startActivityForResult(intent, REQUEST_CODE_CUSTOM_ACTIVITY);
	}

	/**
	 * 获取验证码
	 */
	private void getCord() {
		if (demandCordBt.getText().equals(TIP_GET_VERIFY_CODE)) { // 获取验证码

			// 弹出提示
			MessageDialog messageDialog = new MessageDialog(this);
			messageDialog.setTitle("确认手机号码");
			messageDialog.setContent("我们将发送验证码到这个号码：" + demandPhone.getText());
			messageDialog.setOnDialogFinishListener(new OnDialogFinishListener() {
				
				@Override
				public void onFinish(String content) {
					
					// 显示加载框
					showLoadingDialog();
					// 发送获取验证码请求
					UserReqUtil.doGetVerifyCode(
							NewDemandActivity.this,
							NewDemandActivity.this,
							UserReqUtil
									.getDoGetVerifyCodeParams(
											3, "+86",
											demandPhone
													.getText()
													.toString()),
							null);
					
				}

				@Override
				public void onCancel(String content) {
					// TODO Auto-generated method stub
					
				}
			});
			messageDialog.show();
		} else if (demandCordBt.getText().equals(TIP_REGET_VERIFY_CODE)) { // 重获验证码
			// 不再需要提示，直接显示加载框
			showLoadingDialog();
			// 发送获取验证码请求
			UserReqUtil.doGetVerifyCode(NewDemandActivity.this,
					NewDemandActivity.this, UserReqUtil
							.getDoGetVerifyCodeParams(3, "+86", demandPhone
									.getText().toString()), null);
		}
	}

	public void setChooseText(ArrayList<Metadata> data) {
		switch (dataIndex) {
		case 1:
			// 类型
			if (metadataType != null) {
				metadataType.clear();
			}
			metadataType = data;
			demandType.setText(ChooseDataUtil.getMetadataName(metadataType, 9));
			break;
		case 2:
			// 行业
			if (metadataIndustry != null) {
				metadataIndustry.clear();
			}
			metadataIndustry = data;
			demandIndustry.setText(ChooseDataUtil.getMetadataName(
					metadataIndustry, 9));
			break;
		case 3:
			// 地区
			if (metadataArea != null) {
				metadataArea.clear();
			}
			metadataArea = data;
			demandArea.setText(ChooseDataUtil.getMetadataName(metadataArea, 9));
			break;
		}
	}

	@Override
	public void initJabActionBar() {
		ActionBar mActionBar = jabGetActionBar();
		mActionBar.setDisplayShowCustomEnabled(true);
		mActionBar.setDisplayShowHomeEnabled(false);// 不显示应用图标
		mActionBar.setDisplayShowTitleEnabled(true);
		View mCustomView = getLayoutInflater().inflate(
				R.layout.demand_actionbar, null);
		mActionBar.setCustomView(mCustomView, new ActionBar.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		ActionBar.LayoutParams mP = (ActionBar.LayoutParams) mCustomView
				.getLayoutParams();
		mP.gravity = mP.gravity & ~Gravity.HORIZONTAL_GRAVITY_MASK
				| Gravity.CENTER_HORIZONTAL;
		mActionBar.setCustomView(mCustomView, mP);
		mActionBar.setTitle(" ");
		titleTv = (TextView) mCustomView.findViewById(R.id.titleTv);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.demand_newcreate, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.save_temple:
			if (dialog != null) {
				dialog.dismiss();
			}
			View view = View.inflate(this,
					R.layout.demand_temple_create_dialog, null);
			view.findViewById(R.id.cancelTv).setOnClickListener(this);
			view.findViewById(R.id.confirmTv).setOnClickListener(this);
			editTemple = (EditText) view.findViewById(R.id.labelEt);
			showDialog(view);
			break;
		case R.id.save_demand:
			if (isEdit()) {
				DemandDetailsData details = createNewDemand();
				DemandASSO asso = createNewASSO();
				if (demandEnum == DemandEnum.Edit) {
					showLoadingDialog("正在修改需求");
					details.id = detailsEdit.id;
					details.createrName = detailsEdit.createrName;
					details.createTime = detailsEdit.createTime;
					DemandReqUtil.updateDemand(this, this, cord, details, asso,
							null);
				} else {
					showLoadingDialog("正在创建需求");
					DemandReqUtil.createDemand(this, this, cord, details, asso,
							null);
				}
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 验证输入信息
	 * 
	 * @return
	 */
	public boolean isEdit() {
		// 获取标题输入
		title.value = demandTitel.getText().toString().trim();
		if (TextUtils.isEmpty(title.value)) {
			Toast.makeText(this, "请输入标题", Toast.LENGTH_SHORT).show();
			demandTitel.setText("");
			return false;
		}
		// 获取验证码
		phone.value = demandPhone.getText().toString().trim();
		user.value = demandUser.getText().toString().trim();
		cord = demandCord.getText().toString();
		if (!TextUtils.isEmpty(user.value)) {//输入了用户名
			if (TextUtils.isEmpty(phone.value)) {
				showToast("请输入手机号");
				return false;
			}
			if (TextUtils.isEmpty(cord)) {
				if (demandEnum == DemandEnum.Edit) {//如果是编辑 
					if(!phone.value.equals(phoneEdit)){ //判断手机号是否有变化
						//就不用验证
						showToast("请输入验证码"); //
						return false;
					}
				}else{
					showToast("请输入验证码");
					return false;
				}
			}
		}
		if (!TextUtils.isEmpty(phone.value)) { // 输入了手机号
			if (TextUtils.isEmpty(user.value)) {
				ToastUtil.showToast(this, "请输入联系人");
				return false;
			}
			if (TextUtils.isEmpty(cord)) {
				if (demandEnum == DemandEnum.Edit) {//如果是编辑 
					if(!phone.value.equals(phoneEdit)){ //判断手机号是否有变化
						//就不用验证
						showToast("请输入验证码"); //
						return false;
					}
				}else{
					showToast("请输入验证码");
					return false;
				}
			}
		}
		if (!TextUtils.isEmpty(cord)) { // 输入了验证码
			if (TextUtils.isEmpty(user.value)) {
				showToast("请输入用户名");
				return false;
			}
			if (TextUtils.isEmpty(phone.value)) {
				showToast("请输入手机号");
				return false;
			}
		}

		if (noteData == null) {
			noteData = new NoteData(TaskIDMaker.getTaskId(App.getUserName()),
					"", true);
		}
		if (TextUtils.isEmpty(noteData.value)) {
			noteData.value = "";
		}
		if (TextUtils.isEmpty(noteData.taskId)) {
			noteData.taskId = TaskIDMaker.getTaskId(App.getUserName());// 创建一个
		}
		return true;
	}

	/**
	 * 判断 当前 字段是否可见
	 * 
	 * @param name
	 * @return
	 */
	private boolean isLimits(String name) {
		if (LimitsMap != null) {
			return LimitsMap.get(name) != null ? true : false;
		}
		return false;
	}

	/**
	 * 创建需求
	 */
	public DemandDetailsData createNewDemand() {
		DemandDetailsData demandDetail = new DemandDetailsData();
		demandDetail.title = title;
		noteData.isVisable = isLimits(limits[0]);
		demandDetail.note = noteData;// 是否显示 由他定义
		demandDetail.demandType = type;
		demandDetail.type = getListType(metadataType, isLimits(limits[1]));
		demandDetail.industry = getListType(metadataIndustry,
				isLimits(limits[2]));
		demandDetail.area = getListArea(metadataArea, isLimits(limits[3]));
		if (money == null) {
			money = new AmountData();
		}
		money.isVisable = isLimits(limits[4]);
		demandDetail.amount = money;
		user.value = demandUser.getText().toString().trim();// 保存用户信息
		user.isVisable = true;// 用户名肯定显示
		demandDetail.contact = user; // 创建需求的时候 没有保存用户信息
		demandDetail.phone = phone;
		demandDetail.demandType = type;// 需求类型
		// 标签信息对象的处理
		if (lableData != null) {
			List<String> list = new ArrayList<String>();
			for (LableData lable : lableData) {
				list.add(new String(lable.id + ""));
			}
			demandDetail.tags = TextStrUtil.getStringAppend(list, ",");// 标签字段
		}
		demandDetail.customList = customData;
		demandDetail.categoryIds = TextStrUtil.getStringAppend(categoryIdsList,
				",");// 目录id 字段
		demandDetail.asso = null;
		demandDetail.permIds = permission;

		if (metadataArea != null) {
			demandDetail.areaObj = ChoiceObj.toChoiceObjList(metadataArea);
		}
		if (metadataIndustry != null) {
			demandDetail.industryObj = ChoiceObj
					.toChoiceObjList(metadataIndustry);
		}
		if (metadataType != null) {
			demandDetail.typeObj = ChoiceObj.toChoiceObjList(metadataType);
		}
		return demandDetail;
	}

	/**
	 * 需求类型,行业，地区
	 * 
	 * @return
	 */
	public RelationData getListType(List<Metadata> list, boolean isVisable) {
		RelationData listType = new RelationData();
		listType.isVisable = isVisable;// 显示
		if (list != null) {
			List<Metadata> metadata = ChooseDataUtil.getSelectList(list);
			if (metadata != null) {
				listType.setList(new ArrayList<DemandData>());
				for (Metadata mm : metadata) {
					listType.getList().add(
							new DemandData(mm.getNumber(), mm.name));
				}
			}
		}
		return listType;
	}

	/**
	 * 地区
	 * 
	 * @param list
	 * @param isVisable
	 * @return
	 */
	public RelationData getListArea(List<Metadata> list, boolean isVisable) {
		RelationData listType = new RelationData();
		listType.isVisable = isVisable;// 显示
		if (list != null) {
			List<Metadata> metadata = ChooseDataUtil.getSelectList(list);
			if (metadata != null) {
				listType.setList(new ArrayList<DemandData>());
				for (Metadata mm : metadata) {
					listType.getList().add(
							new DemandData(mm.id, mm.name));
				}
			}
//			listType.setList(metadataListByMetadataList(list, null));
		}
		return listType;
	}

	public List<DemandData> metadataListByMetadataList(List<Metadata> list,
			DemandData model) {
		if (null == model) {
			model = new DemandData(null, null);
		}
		List<DemandData> outList = new ArrayList<DemandData>();
		if (null != list && list.size() > 0) {
			for (Metadata one : list) {
				if (null != one) {
					DemandData clone = model.cloneMe();
					clone.id = one.id;
					if (null == clone.name) {
						if (!"3418".equals(one.id)) {
							clone.name = "国内" + "-" + one.name;
						} else {
							clone.name = one.name;
						}

					} else {
						clone.name = clone.name + "-" + one.name;
					}
					List<DemandData> outListTemp = metadataListByMetadataList(
							one.childs, clone);
					outList.addAll(outListTemp);
				}
			}
		} else {
			outList.add(model);
		}
		return outList;
	}

	/**
	 * 将asso对象数据 转换成Node对象
	 */
	private void assoToNode(DemandASSO asso) {
		ASSORPOK rpok = asso.value;
		if (rpok == null) {
			return;
		}
		// 获取人脉信息
		if (rpok.p != null && rpok.p.size() > 0) {
			associatedTv.setVisibility(View.VISIBLE);
			for (ASSOData data : rpok.p) {
				ConnectionNode assoData = new ConnectionNode();// 人脉信息
				assoData.setMemo(data.tag);
				for (DemandASSOData obj : data.conn) {
					Connections conn = new Connections();
					if (obj.type == 2) {
						conn.setType(Connections.type_org + "");
					}
					if (obj.type == 3) {
						conn.setType(Connections.type_persion + "");
					}
					conn.setID(obj.id);
					conn.setName(obj.name);
					conn.setCareer(obj.career);
					conn.setCompany(obj.company);
					if(obj.type==2)//type=2人脉
						conn.jtContactMini.isOnline=false;
					if(obj.type==3)//type=3好友
						conn.jtContactMini.isOnline=true;
					assoData.getListConnections().add(conn);
				}
				ResourceNode++;
				contactsNode.add(assoData);
			}
			demandContacts.setVisibility(View.VISIBLE);
			//contactsAdapter.setData(contactsNode);
			contactsAdapter.notifyDataSetInvalidated();
		}
		// 获取知识信息

		if (rpok.k != null && rpok.k.size() > 0) {
			associatedTv.setVisibility(View.VISIBLE);
			for (ASSOData data : rpok.k) {
				KnowledgeNode node = new KnowledgeNode();
				node.setMemo(data.tag);
				for (DemandASSOData obj : data.conn) {
					KnowledgeMini2 knowledgeMini2 = new KnowledgeMini2();
					knowledgeMini2 = obj.toKnowledgeMini2();
					node.getListKnowledgeMini2().add(knowledgeMini2);
				}
				ResourceNode++;
				knowledgeNode.add(node);
			}
			demandKnowledge.setVisibility(View.VISIBLE);
			//knowledgeAdapter.setData(knowledgeNode);
			knowledgeAdapter.notifyDataSetInvalidated();
		}
		// 获取组织信息
		if (rpok.o != null && rpok.o.size() > 0) {
			associatedTv.setVisibility(View.VISIBLE);
			for (ASSOData data : rpok.o) {
				ConnectionNode assoData = new ConnectionNode();// 人脉信息
				assoData.setMemo(data.tag);
				for (DemandASSOData obj : data.conn) {
					Connections conn = obj.toConnection();
					if (obj.type == 4) {
						conn.setType(Connections.type_org + "");
					}
					if (obj.type == 5) {
						conn.setType(Connections.type_persion + "");
					}
					conn.setID(obj.id);
					conn.setName(obj.name);
					conn.setCareer(obj.career);
					conn.setCompany(obj.company);
					assoData.getListConnections().add(conn);
					//assoData.getListConnections().add(conn);
				}
				ResourceNode++;
				organizationNode.add(assoData);
			}
			demandOrganization.setVisibility(View.VISIBLE);
			//organizationAdapter.setData(organizationNode);
			organizationAdapter.notifyDataSetInvalidated();
		}
		// 获取事件信息
		if (rpok.r != null && rpok.r.size() > 0) {
			associatedTv.setVisibility(View.VISIBLE);
			for (ASSOData data : rpok.r) {
				AffairNode assoData = new AffairNode();// 人脉信息
				assoData.setMemo(data.tag);
				for (DemandASSOData obj : data.conn) {
					AffairsMini conn = obj.toAffaitrsMini();

					assoData.getListAffairMini().add(conn);
				}
				ResourceNode++;
				eventNode.add(assoData);
			}
			demandEvent.setVisibility(View.VISIBLE);
			//eventAdapter.setData(eventNode);
			eventAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * 创建ASSO 关联关系信息
	 * 
	 * @return
	 */
	public DemandASSO createNewASSO() {
		DemandASSO asso = new DemandASSO();
		asso.isVisable = isLimits(limits[5]);
		List<ASSOData> p = new ArrayList<ASSOData>();
		// 人脉信息
		if (contactsNode != null) {
			for (ConnectionNode node : contactsNode) {
				List<DemandASSOData> conn = new ArrayList<DemandASSOData>();
				for (Connections obj : node.getListConnections()) {
					DemandASSOData assoData = new DemandASSOData();
					assoData.type = 2; //
					if (obj.isOnline()) {
						assoData.type = 3;

					} else {
						assoData.type = 2;
					}

					assoData.id = obj.getId();
					assoData.name = obj.getName();
					assoData.ownerid = App.getUserID();
					assoData.ownername = App.getNick();
					assoData.career = obj.getCareer();
					assoData.company = obj.getCompany();
					assoData.picPath = obj.getImage();
					conn.add(assoData);
				}
				p.add(new ASSOData(node.getMemo(), conn));
			}
		}

		List<ASSOData> o = new ArrayList<ASSOData>();
		// 组织信息
		if (organizationNode != null) {
			for (ConnectionNode node : organizationNode) {
				List<DemandASSOData> conn = new ArrayList<DemandASSOData>();
				for (Connections obj : node.getListConnections()) {
					DemandASSOData assoData = new DemandASSOData();
					if (obj.isOnline()) {
						assoData.type = 4;

					} else {
						assoData.type = 5;
					}

					assoData.id = obj.getId();
					assoData.name = obj.getName();
					assoData.ownerid = App.getUserID();
					assoData.ownername = App.getNick();
					// 当前组织没有行业和地址
					// assoData.setAddress(obj.getAttribute());
					// assoData.setHy(obj.getOrganizationMini());
					conn.add(assoData);
					assoData.picPath = obj.getImage();
				}
				o.add(new ASSOData(node.getMemo(), conn));
			}
		}
		// 知识
		List<ASSOData> k = new ArrayList<ASSOData>();
		if (knowledgeNode != null) {
			for (KnowledgeNode node : knowledgeNode) {
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
		if (eventNode != null) {
			for (AffairNode node : eventNode) {
				List<DemandASSOData> conn = new ArrayList<DemandASSOData>();
				for (AffairsMini obj : node.getListAffairMini()) {
					DemandASSOData assoData = new DemandASSOData();
					assoData.type = 1; //
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
		return asso;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {// 正确返回
			switch (requestCode) {
			case ENavConsts.ActivityReqCode.REQUEST_CHOOSE_SELECT:
				// 多级选择回调界面
				setChooseText((ArrayList<Metadata>) data
						.getSerializableExtra(ENavConsts.DEMAND_CHOOSE_DATA));
				break;
			case ENavConsts.ActivityReqCode.REQUEST_MOENY_SELECT:
				// 选择金额
				money = (AmountData) data
						.getSerializableExtra(ENavConsts.DEMAND_CHOOSE_TYEP);
				if (money != null) {
					demandAmount.setText(money.unit + "-"
							+ money.getAmountData());
				}
				break;
			case REQUEST_CODE_RELATED_RESOURCE_ACTIVITY:
				// 关联信息
				associatedTv.setVisibility(View.VISIBLE);
				if (data.hasExtra(EConsts.Key.RELATED_PEOPLE_NODE)) { // 人脉信息
					ConnectionNode connectionNode = (ConnectionNode) data
							.getSerializableExtra(EConsts.Key.RELATED_PEOPLE_NODE);
					if (presourceEnum == ResourceNodeEnum.Edit
							&& currentRequestCode == REQUEST_CODE_RELATED_RESOURCE_PEOPLE) {
						// 编辑后的人脉
						contactsNode.set(currentRequestEditPosition,
								connectionNode);
					} else {
						ResourceNode++;
						contactsNode.add(connectionNode);// 添加新的人脉信息
					}
					demandContacts.setVisibility(View.VISIBLE);
					contactsAdapter.notifyDataSetInvalidated();
				}
				if (data.hasExtra(EConsts.Key.RELATED_ORGANIZATION_NODE)) { // 组织信息
					ConnectionNode connectionNode = (ConnectionNode) data
							.getSerializableExtra(EConsts.Key.RELATED_ORGANIZATION_NODE);
					if (presourceEnum == ResourceNodeEnum.Edit
							&& currentRequestCode == REQUEST_CODE_RELATED_RESOURCE_ORGANIZATION) {
						// 编辑后的人脉
						organizationNode.set(currentRequestEditPosition,
								connectionNode);
					} else {
						ResourceNode++;
						organizationNode.add(connectionNode);// 添加组织信息
					}
					demandOrganization.setVisibility(View.VISIBLE);
					organizationAdapter.notifyDataSetInvalidated();
				}
				if (data.hasExtra(EConsts.Key.RELATED_KNOWLEDGE_NODE)) { // 知识信息
					KnowledgeNode ResourNode = (KnowledgeNode) data
							.getSerializableExtra(EConsts.Key.RELATED_KNOWLEDGE_NODE);
					if (presourceEnum == ResourceNodeEnum.Edit
							&& currentRequestCode == REQUEST_CODE_RELATED_RESOURCE_KNOWLEDGE) {
						// 编辑后的人脉
						knowledgeNode.set(currentRequestEditPosition,
								ResourNode);
					} else {
						ResourceNode++;
						knowledgeNode.add(ResourNode);// 添加新的知识信息
					}
					demandKnowledge.setVisibility(View.VISIBLE);
					knowledgeAdapter.notifyDataSetInvalidated();
				}
				if (data.hasExtra(EConsts.Key.RELATED_AFFAIR_NODE)) {// 事件信息
					AffairNode affairNode = (AffairNode) data
							.getSerializableExtra(EConsts.Key.RELATED_AFFAIR_NODE);
					if (presourceEnum == ResourceNodeEnum.Edit
					// 编辑后的人脉
							&& currentRequestCode == REQUEST_CODE_RELATED_RESOURCE_AFFAIR) {
						eventNode.set(currentRequestEditPosition, affairNode);
					} else {
						ResourceNode++;
						eventNode.add(affairNode);// 添加新的事件
					}
					demandEvent.setVisibility(View.VISIBLE);
					eventAdapter.notifyDataSetInvalidated();
				}
				break;
			case REQUEST_CODE_GLOBAL_KNOWLEDGE_CATEGORY_ACTIVITY:// 启动目录的回调信息
				treeData = (ArrayList<UserCategory>) data.getSerializableExtra(EConsts.Key.KNOWLEDGE_CATEGORY_LIST);
				boolean isSelect = data.getBooleanExtra(EConsts.Key.KNOWLEDGE_CATEGORY_GROUP, false);
				if (treeData.size() == 1 && "未分组".equals(treeData.get(0).getCategoryname())) {
					if (isSelect) {
						ArrayList<UserCategory> tmpArrayList = new ArrayList<UserCategory>();
						tmpArrayList.add(treeData.get(treeData.size()-1));
						updateDemandCategoryUi(tmpArrayList);
						tmpArrayList.clear();
					} else {
						updateDemandCategoryUi(null);
					}
				} else {
					updateDemandCategoryUi(treeData);
				}
				break;
			case REQUEST_CODE_KNOWLEDGE_PERMISSION_ACTIVITY:// 启动权限控制的回调
				viewPlace.setVisibility(View.VISIBLE);
				LimitsMap = (HashMap<String, String>) data
						.getSerializableExtra(ENavConsts.DEMAND_PERMISSION_DATA);
				if (LimitsMap == null) {
					LimitsMap = new HashMap<String, String>();
				}
				scollViewDown();
				break;
			case REQUEST_CODE_LABLE_ACTIVITY:
				// 标签页面的回调信息
				lableData = (ArrayList<LableData>) data
						.getSerializableExtra(ENavConsts.DEMAND_LABEL_DATA);
				if (lableData != null && lableData.size() > 0) {
					viewLabel.setVisibility(View.VISIBLE);
					LabelTv.setText(TextStrUtil.getLableDataSize(9, lableData));
				} else {
					viewLabel.setVisibility(View.GONE);
				}
				break;
			// 小乐
			case REQUEST_CODE_LOW_PERMISSION:
				listLowPermission = (ArrayList<Connections>) data
						.getSerializableExtra("listConnections");
				Util.removeDuplicatesConnections(listLowPermission,
						listMiddlePermission, listHightPermission);
				updateAllPermissionContentTv();
				break;
			// 中乐
			case REQUEST_CODE_MIDDLE_PERMISSION:
				listMiddlePermission = (ArrayList<Connections>) data
						.getSerializableExtra("listConnections");
				Util.removeDuplicatesConnections(listMiddlePermission,
						listLowPermission, listHightPermission);
				updateAllPermissionContentTv();
				break;
			// 大乐
			case REQUEST_CODE_HIGHT_PERMISSION:
				listHightPermission = (ArrayList<Connections>) data
						.getSerializableExtra("listConnections");
				Util.removeDuplicatesConnections(listHightPermission,
						listLowPermission, listMiddlePermission);
				updateAllPermissionContentTv();
				break;
			case REQUEST_CODE_INTRODUCE_ACTIVITY:// 介绍
				noteData = (NoteData) data
						.getSerializableExtra(ENavConsts.DEMAND_NOTE_DATA);
				if (noteData!=null&&!TextUtils.isEmpty(noteData.value)) {
					demandIntro.setHint("点击查看具体内容");
				}else{
					demandIntro.setHint("点击填写介绍内容");
				}
				break;
			}
		}
		// 自定义组件回调样式
		if (resultCode == 999 && requestCode == REQUEST_CODE_CUSTOM_ACTIVITY) {
			// 自定义组件

			// 类型转换错误
			// ArrayList<CustomerPersonalLine> ss = data
//					.getParcelableArrayListExtra("Customer_Bean");
			ArrayList<CustomerPersonalLine> ss = (ArrayList<CustomerPersonalLine>) data .getSerializableExtra("Customer_Bean");
			boolean isNull = data.getBooleanExtra("isNull", false);

			demandCustom.removeAllViews();
			customData.clear();// 清空数据
			if (isNull)
				return;
			if (ss != null) {
				for (CustomerPersonalLine s : ss) {
					CustomData custom = CustomData.toCustomData(s);
					custom.isVisable = isLimits(custom.key);// 判断当前图片是否有
					customData.add(custom);
				}
			}

			resetDemandCustom();
		}
	}

	private void updateDemandCategoryUi(ArrayList<UserCategory> treeData) {
		if (treeData != null) {
			categoryIdsList.clear();
			viewTree.setVisibility(View.VISIBLE); // 显示
			List<String> list = new ArrayList<String>();
			for (UserCategory lable : treeData) {
				list.add(TextStrUtil.checkCategoryname(lable));
				categoryIdsList.add(TextStrUtil.checkCategoryId(lable));
			}
			treeTv.setText(TextStrUtil.getStringSize(9, list));
		} else {
			treeTv.setText("");
			viewTree.setVisibility(View.GONE); // 隐藏
		}
		
	}

	/**
	 * 设置滑动滚动到最底部
	 */
	private void scollViewDown() {
		Handler handler = new Handler();
		handler.post(new Runnable() {
			@Override
			public void run() {
				demandScrollView.fullScroll(ScrollView.FOCUS_DOWN);
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		currentRequestEditPosition = position;
		presourceEnum = ResourceNodeEnum.Edit;
		if (parent == demandContactsLl) { // 编辑人脉
			currentRequestCode = REQUEST_CODE_RELATED_RESOURCE_PEOPLE;
			ENavigate.startRelatedResourceActivityForResult(this,
					REQUEST_CODE_RELATED_RESOURCE_ACTIVITY, "",
					ResourceType.People, contactsAdapter.getItem(position));
		} else if (parent == demandOrganizationLl) { // 编辑组织
			currentRequestCode = REQUEST_CODE_RELATED_RESOURCE_ORGANIZATION;
			ENavigate.startRelatedResourceActivityForResult(this,
					REQUEST_CODE_RELATED_RESOURCE_ACTIVITY, "",
					ResourceType.Organization,
					organizationAdapter.getItem(position));
		} else if (parent == demandKnowledgeLl) { // 编辑知识
			currentRequestCode = REQUEST_CODE_RELATED_RESOURCE_KNOWLEDGE;
			ENavigate.startRelatedResourceActivityForResult(this,
					REQUEST_CODE_RELATED_RESOURCE_ACTIVITY, "",
					ResourceType.Knowledge, knowledgeAdapter.getItem(position));
		} else if (parent == demandEventLl) { // 编辑事件
			currentRequestCode = REQUEST_CODE_RELATED_RESOURCE_AFFAIR;
			ENavigate.startRelatedResourceActivityForResult(this,
					REQUEST_CODE_RELATED_RESOURCE_ACTIVITY, "",
					ResourceType.Affair, eventAdapter.getItem(position));
		}
	}

	// class CustomAdapter extends BaseAdapter {
	//
	// @Override
	// public int getCount() {
	// return customData != null ? customData.size() : 0;
	// }
	//
	// @Override
	// public CustomData getItem(int position) {
	// // TODO Auto-generated method stub
	// return customData.get(position);
	// }
	//
	// @Override
	// public long getItemId(int position) {
	// // TODO Auto-generated method stub
	// return position;
	// }
	//
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// // TODO Auto-generated method stub
	// if (convertView == null) {
	// convertView = View.inflate(NewDemandActivity.this,
	// R.layout.demand_include_custom, null);
	// }
	// TextView tagTv = ViewHolder.get(convertView, R.id.view_tv_name);
	// CustomData custom = getItem(position);
	// tagTv.setText(custom.getKey());
	// TextView tvContent = ViewHolder.get(convertView, R.id.view_et_edit);
	// tvContent.setText(custom.getValue());
	// return convertView;
	// }
	// }

	/**
	 * 权限控制器
	 */
	private void initComponent() {
		noPermissionSwitchCb = (CheckBox) findViewById(R.id.noPermissionSwitchCb);
		noPermissionSwitchCb.setOnClickListener(mOnClickListener);
		noPermissionSwitchCb
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							lowPermissionRightArrowsIv.setVisibility(View.GONE);
							middlePermissionRightArrowsIv
									.setVisibility(View.GONE);
							highPermissionRightArrowsIv
									.setVisibility(View.GONE);
							// 清理数据保存的值
							listLowPermission.clear();
							listMiddlePermission.clear();
							listHightPermission.clear();
							updateAllPermissionContentTv();
							permission.dule = true;
							permission.clearList();
						} else {
							permission.dule = false;
							lowPermissionRightArrowsIv
									.setVisibility(View.VISIBLE);
							middlePermissionRightArrowsIv
									.setVisibility(View.VISIBLE);
							highPermissionRightArrowsIv
									.setVisibility(View.VISIBLE);
						}

					}
				});

		lowPermissionRl = (RelativeLayout) findViewById(R.id.lowPermissionRl);
		lowPermissionRl.setOnClickListener(mOnClickListener);
		lowPermissionContentTv = (TextView) findViewById(R.id.lowPermissionContentTv);
		lowPermissionRightArrowsIv = (ImageView) findViewById(R.id.lowPermissionRightArrowsIv);

		middlePermissionRl = (RelativeLayout) findViewById(R.id.middlePermissionRl);
		middlePermissionRl.setOnClickListener(mOnClickListener);
		middlePermissionContentTv = (TextView) findViewById(R.id.middlePermissionContentTv);
		middlePermissionRightArrowsIv = (ImageView) findViewById(R.id.middlePermissionRightArrowsIv);

		highPermissionRl = (RelativeLayout) findViewById(R.id.highPermissionRl);
		highPermissionRl.setOnClickListener(mOnClickListener);
		highPermissionContentTv = (TextView) findViewById(R.id.highPermissionContentTv);
		highPermissionRightArrowsIv = (ImageView) findViewById(R.id.highPermissionRightArrowsIv);

		noPermissionSwitchCb.setChecked(noPermission);
		if (listHightPermission.size() > 0 || listMiddlePermission.size() > 0
				|| listLowPermission.size() > 0) {
			noPermission = false;
			noPermissionSwitchCb.setChecked(noPermission);
		}
		updateAllPermissionContentTv();

	}

	/**
	 * 传递
	 */
	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String MSG = "onClick()";
			Intent intent = new Intent(NewDemandActivity.this,
					SelectConnectionsActivity.class);
			// intent.putExtra(EConsts.Key.FROM_ACTIVITY,
			// NewDemandActivity.class.getSimpleName());
			if (noPermissionSwitchCb == v) {
				noPermission = noPermission ? false : true;
			} else if (noPermission == true
					&& (lowPermissionRl == v || middlePermissionRl == v || highPermissionRl == v)) {
				Toast.makeText(NewDemandActivity.this, "关闭独乐，方可选择大、中、小乐哦", 0)
						.show();
			} else if (lowPermissionRl == v) {
				intent.putExtra("listConnections", listLowPermission);
				startActivityForResult(intent, REQUEST_CODE_LOW_PERMISSION);
			} else if (middlePermissionRl == v) {
				intent.putExtra("listConnections", listMiddlePermission);
				startActivityForResult(intent, REQUEST_CODE_MIDDLE_PERMISSION);
			} else if (highPermissionRl == v) {
				intent.putExtra("listConnections", listHightPermission);
				startActivityForResult(intent, REQUEST_CODE_HIGHT_PERMISSION);
			}
		}
	};

	private void listLowPermission(PermissionsData data) {
		if (data != null) {
			if (data.dule == true) {
				permission.dule = true;
				noPermission = true;
				listLowPermission.clear();
				listMiddlePermission.clear();
				listHightPermission.clear();
			} else {
				permission.dule = false;
				noPermission = false;
				List<LeData> xiaole = data.xiaoles;
				List<LeData> zhongle = data.zhongles;
				List<LeData> dale = data.dales;

				if (xiaole != null) {// 小乐
					Connections conne;
					for (LeData le : xiaole) {
						conne = new Connections();
						conne.setID(le.id);
						conne.setName(le.name);
						listLowPermission.add(conne);
					}
				}
				if (zhongle != null) {// 中乐
					Connections conne;
					for (LeData le : zhongle) {
						conne = new Connections();
						conne.setID(le.id);
						conne.setName(le.name);
						listMiddlePermission.add(conne);
					}
				}
				if (dale != null) {// 大乐
					Connections conne;
					for (LeData le : dale) {
						conne = new Connections();
						conne.setID(le.id);
						conne.setName(le.name);
						listHightPermission.add(conne);
					}
				}
			}
		} else {
			permission.dule = true;
		}
		noPermissionSwitchCb.setChecked(noPermission);
	}

	/**
	 * 将数据还原并显示
	 */
	private void updateAllPermissionContentTv() {
		if (listLowPermission == null || listMiddlePermission == null
				|| listHightPermission == null) {
			return;
		}
		if (listLowPermission.size() > 0) {
			// 获取小乐 信息
			ArrayList<LeData> xiaole = new ArrayList<LeData>();
			for (Connections connection : listLowPermission) {
				xiaole.add(new LeData(connection.getId(), connection.getName()));
			}
			permission.xiaoles = xiaole;
			lowPermissionContentTv.setText(CreateKnowledgeActivity
					.listPermission2Str(listLowPermission));
		} else if (listLowPermission.size() <= 0) {
			lowPermissionContentTv.setText("（不可见、可对接、不可分享）");
			permission.xiaoles = new ArrayList<LeData>();
		}

		if (listMiddlePermission.size() > 0) {
			// 获取中乐 信息
			ArrayList<LeData> zhongle = new ArrayList<LeData>();
			for (Connections connection : listMiddlePermission) {
				zhongle.add(new LeData(connection.getId(), connection.getName()));
			}
			permission.zhongles = zhongle;
			middlePermissionContentTv.setText(CreateKnowledgeActivity
					.listPermission2Str(listMiddlePermission));
		} else if (listMiddlePermission.size() <= 0) {
			middlePermissionContentTv.setText("（可见、可对接、不可分享）");
			permission.zhongles = new ArrayList<LeData>();
		}

		if (listHightPermission.size() > 0) {
			// 获取小乐 信息
			ArrayList<LeData> dale = new ArrayList<LeData>();
			for (Connections connection : listHightPermission) {
				dale.add(new LeData(connection.getId(), connection.getName()));
			}
			permission.dales = dale;
			highPermissionContentTv.setText(CreateKnowledgeActivity
					.listPermission2Str(listHightPermission));
		} else if (listHightPermission.size() <= 0) {
			highPermissionContentTv.setText("（可见、可对接、可分享）");
			permission.dales = new ArrayList<LeData>();
		}
		if (listHightPermission.size() > 0 || listMiddlePermission.size() > 0
				|| listLowPermission.size() > 0) {
			noPermission = false;
			noPermissionSwitchCb.setChecked(noPermission);
		}
	}

	private void showDialog(View view) {
		dialog = new Dialog(this, R.style.MyDialog);
		// dialog.setCancelable(false);//是否允许返回
		dialog.addContentView(view, new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		dialog.show();
	}

	@Override
	public void bindData(int tag, Object object) {
		this.dismissLoadingDialog();
		if (object == null) {
			return;
		}
		if (tag == EAPIConsts.ReqType.GET_VERIFY_CODE) { // 获取验证码
			// 是否获取成功
			DataBox dataBox = (DataBox) object;
			if (dataBox.mIsSuccess) { // 获取验证码成功，开始倒计时
				// 重置倒计时Timer
				if (mTimer != null) {
					mTimer.cancel();
					mTimer = null;
				}
				mTimer = new Timer();
				mTimer.schedule(new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						mCountdownLeft--;
						mHandler.sendEmptyMessage(MSG_COUNT_DOWN);
					}
				}, 0, COUNTDOWN_INTERVAL);
				// 设置倒计时时间
				mCountdownLeft = EXPIRED_TIME;
				// 设置验证按钮状态
				demandCordBt.setEnabled(false);
			}
		} else if (tag == EAPIConsts.demandReqType.demand_saveTemplate) {
			// 保存信息
			Map<Integer, Object> template = (Map<Integer, Object>) object;
			if ((Boolean) template.get(1)) {
				showToast("模版保存成功");
				// 发送广播进行刷新消息
				NewDemandActivity.this.sendBroadcast(new Intent(
						DemandAction.DEMAND_TEMPLATE_ACYTION));
			} else {
				String s = (String) template.get(-1);
				showToast(TextUtils.isEmpty(s) ? "操作失败" : s);// 没有数据时 操作失败
			}
		} else if (tag == EAPIConsts.demandReqType.demand_createDemand
				|| tag == EAPIConsts.demandReqType.demand_updateDemand) {
			Map<Integer, String> msp = (Map<Integer, String>) object;
			if (msp.get(1) != null) {
				String demandId = msp.get(1);
				// 创建和修改成功后
				switch (demandEnum) {
				case Default:
					showToast("需求创建成功");
					ENavigate.startNeedDetailsActivity(this, demandId, 1);
					break;
				case Callback:
					showToast("需求创建成功");
					setResult(RESULT_OK);
					ENavigate.startNeedDetailsActivity(this, demandId, 1);
					break;
				case Add:
					showToast("需求保存成功");
					Intent intent1 = new Intent();
					intent1.putExtra(ENavConsts.DEMAND_DETAILS_ID, demandId);
					setResult(RESULT_OK, intent1);
					break;
				case Edit:
					showToast("需求修改成功");
					Intent intent = new Intent();
					intent.putExtra(ENavConsts.DEMAND_DETAILS_ID, demandId);
					setResult(RESULT_OK, intent);
					break;
				default:
					break;
				}
				this.finish();
			} else {
				String error = msp.get(2) != null ? msp.get(2) : "操作失败";
				showToast(error);
			}
		}
	}

	// 消息处理器
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_COUNT_DOWN: // 倒计时消息
				if (mCountdownLeft <= 0) { // 倒计时结束
					// 取消倒计时任务
					if (mTimer != null) {
						mTimer.cancel();
						mTimer = null;
					}
					// 更改态验证码按钮状态和文字
					demandCordBt.setText(TIP_REGET_VERIFY_CODE);
					demandCordBt.setEnabled(true);
				} else { // 倒计时仍在进行
					demandCordBt
							.setText(String.format("剩余%d秒", mCountdownLeft));
				}
				break;
			}
		}
	};
}
