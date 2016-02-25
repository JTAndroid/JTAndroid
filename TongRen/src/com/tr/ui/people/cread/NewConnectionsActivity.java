package com.tr.ui.people.cread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.constvalue.EnumConst.ModuleType;
import com.tr.App;
import com.tr.R;
import com.tr.api.PeopleReqUtil;
import com.tr.image.ImageLoader;
import com.tr.model.demand.ASSOData;
import com.tr.model.demand.ASSORPOK;
import com.tr.model.demand.DemandASSO;
import com.tr.model.demand.DemandASSOData;
import com.tr.model.demand.LableData;
import com.tr.model.demand.Metadata;
import com.tr.model.joint.AffairNode;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.joint.KnowledgeNode;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.knowledge.UserCategory;
import com.tr.model.obj.AffairsMini;
import com.tr.model.obj.Connections;
import com.tr.model.obj.JTFile;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavConsts.PeopleModuleReqCode;
import com.tr.navigate.ENavigate;
import com.tr.ui.common.JointResourceFragment.ResourceType;
import com.tr.ui.demand.CreateLabelActivity;
import com.tr.ui.demand.util.ChooseDataUtil;
import com.tr.ui.demand.util.TextStrUtil;
import com.tr.ui.knowledge.CreateKnowledgeActivity;
import com.tr.ui.knowledge.PermissionActivity;
import com.tr.ui.organization.model.Area;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.people.contactsdetails.ContactsDetailsActivity;
import com.tr.ui.people.cread.utils.MakeListView;
import com.tr.ui.people.cread.view.MyEditTextView;
import com.tr.ui.people.cread.view.MyitemView;
import com.tr.ui.people.model.BaseResult;
import com.tr.ui.people.model.Basic;
import com.tr.ui.people.model.Category;
import com.tr.ui.people.model.PeopleDetails;
import com.tr.ui.people.model.PeopleRequest;
import com.tr.ui.people.model.PermIds;
import com.tr.ui.people.model.Person;
import com.tr.ui.people.model.PersonName;
import com.tr.ui.people.model.PersonPermDales;
import com.tr.ui.people.model.PersonPermXiaoles;
import com.tr.ui.people.model.PersonPermZhongles;
import com.tr.ui.people.model.PersonTagRelation;
import com.tr.ui.people.model.WorkExperience;
import com.tr.ui.widgets.BasicListView2;
import com.tr.ui.widgets.CircleImageView;
import com.utils.common.EConsts;
import com.utils.common.OrganizationPictureUploader;
import com.utils.common.OrganizationPictureUploader.OnOrganizationPictureUploadListener;
import com.utils.http.EAPIConsts.PeopleRequestType;
import com.utils.http.IBindData;
import com.utils.log.KeelLog;
import com.utils.log.ToastUtil;
import com.utils.string.StringUtils;

/**
 *   编辑用户、创建人脉
 *   
 * 启动方法：ENavigate.startNewConnectionsActivity
 * 
 * @param fromActivityType
 *            1-创建；2-编辑；3-编辑"我" 4 保存人脉 5 转为人脉
 * @param jtContact
 *            创建传null，编辑传PeopleDetails类型对象
 * @param requestCode
 *            创建传非0值
 * @author Wxh07151732
 * 
 */
public class NewConnectionsActivity extends BaseActivity implements
		OnClickListener, IBindData, OnItemClickListener,
		OnOrganizationPictureUploadListener {
	private static String decollatorStr = "、";
	private MyEditTextView address_Etv; // 详细地址
	private MyEditTextView area_Etv; // 所在地区
	private MyEditTextView classify_Etv; // 分类
	private MyEditTextView company_Etv; // 公司
	private MyEditTextView email_Etv;// 邮箱
	private MyEditTextView gender_Etv;// 性别
	private MyEditTextView name_Etv;// 名
	private MyEditTextView phone_Etv;// 电话
	private MyEditTextView surname_Etv;// 姓
	private MyEditTextView post_Etv;// 职位
	private TextView creadContacts_Tv;// 标题
	private ImageView businessCard_Tv;// 拍摄名片
	private TextView finish_Tv;// 完成创建
	private RelativeLayout addmore_Rl;// 添加其他项目
	private ArrayList<String> module_list;
	public static final String[] modules = { "基本信息", "联系方式", "个人情况", "投资意向",
			"融资意向", "专家需求", "专家身份", "教育经历", "工作经历", "社会活动", "会面情况", "备注",
			"自定义模块" }; //模块名称
	private MyitemView module; // 模块控件
	private MyEditTextView custom_Text_Etv; // 自定义控件
	private MyEditTextView custom_field_Etv; // 备注
	private static final int NewConnectionsActivity = 0; //从NewConnectionsActivity开启其他Activity的请求码
	private MyEditTextView First_Etv; //First_Name控件
	private MyEditTextView Last_Etv;  //Last_Name控件
	private ArrayList<Basic> contact_information;  //联系方式
	private HashMap<String, MyitemView> module_Map;  //子控件与模块名称集合，作用是取到与模块名称相对应的子控件。
	private RelativeLayout quit_Rl;// 退出创建人脉
	boolean isNull;  //自定义返回数据是否为空
	private AlertDialog create;  //自定义标题对话框
	
	/**
	 * 判断关联数据是否点击子条目中的类型
	 */
	public int currentRequestCode = 0;
	/**
	 * 判断关联数据是否点击子条目中的人脉
	 */
	public static final int REQUEST_CODE_RELATED_RESOURCE_PEOPLE = 2001;
	/**
	 * 判断关联数据是否点击子条目中的组织
	 */
	public static final int REQUEST_CODE_RELATED_RESOURCE_ORGANIZATION = 2002;
	/**
	 * 判断关联数据是否点击子条目中的知识
	 */
	public static final int REQUEST_CODE_RELATED_RESOURCE_KNOWLEDGE = 2003;
	/**
	 * 判断关联数据是否点击子条目中的事务
	 */
	public static final int REQUEST_CODE_RELATED_RESOURCE_AFFAIR = 2004;
	/**判断关联是否增加新的条目 */
	public static final int STATE_ADD = 0;
	/**判断关联是否编辑旧的条目 */
	public static final int STATE_EDIT = 1;
	public int currentRequestState = STATE_ADD;
	public int currentRequestEditPosition = -1; 
	private static final String TAG = "NewConnections_Activity";
	/**自定义控件的集合 */
	private static ArrayList<MyEditTextView> Custom_list = null;

	private ArrayList<ConnectionNode> connectionNodeList; //四大组件中的人脉数据集合
	private ArrayList<ConnectionNode> connectionNodeList2;//四大组件中的组织数据集合
	private ArrayList<KnowledgeNode> knowledgeNodeList;//四大组件中的知识数据集合
	private ArrayList<AffairNode> affairNodeList;//四大组件中的事务数据集合
	private BasicListView2 people; //四大组件中的人脉ListView
	private BasicListView2 organization;//四大组件中的组织ListView
	private BasicListView2 knowledge;//四大组件中的知识ListView
	private BasicListView2 requirement;//四大组件中的事务ListView
	private LinearLayout people_Ll; //四大组件中的人脉LinearLayout
	private LinearLayout organization_Ll;//四大组件中的组织LinearLayout
	private LinearLayout knowledge_Ll;//四大组件中的知识LinearLayout
	private LinearLayout requirement_Ll;//四大组件中的事务LinearLayout
	private ConnectionsGroupAdapter peopleGroupAdapter; //四大组件中的人脉Adapter
	private ConnectionsGroupAdapter organizationGroupAdapter;//四大组件中的组织Adapter
	private KnowledgeGroupAdapter knowledgeGroupAdapter;//四大组件中的知识Adapter
	private RequirementGroupAdapter requirementGroupAdapter;//四大组件中的事务Adapter
	private LinearLayout catalogue_people; //底部栏的目录
	private LinearLayout label_people;//底部栏的标签
	private LinearLayout jurisdiction_people;//底部栏的权限
	private LinearLayout relevance_people;//底部栏的关联
	private ArrayList<Metadata> metadataArea; // 区域
	private Area area_result; //所在地区对象
	private ArrayList<JTFile> picture; // 选择相片返回的值
	private CircleImageView picture_Iv;//相片控件
	private List<PersonName> peopleNameList;//姓名对象集合
	private int eFromActivityType; // 1-创建；2-编辑；3-编辑"我"
	private PeopleDetails people_details;;// 传递的对象
	private List<Basic> customTagList; //自定义对象集合
	private String remark; //备注返回的值
	private ArrayList<LableData> lableData = new ArrayList<LableData>();// 标签对象数据
	
	public boolean isAdd = true; // first-last是否添加过
	private ArrayList<Long> categoryList; // 目录
	private PermIds permIds; // 权限
	private ArrayList<Long> tid; // 标签

	private String avatarUrl = null;// 头像URL
	private TextView asso_Tv;
	private LinearLayout sidazujian_Ll;//底部栏父级LinearLayout
	/** 人脉目录对象集合*/
	private ArrayList<UserCategory> listCategory = new ArrayList<UserCategory>();
	/** 人脉主对象*/
	private Person person; 
	/** 向后台提交的人脉对象*/
	private PeopleRequest people_request;  
	/**
	 * 权限的大中小乐对象集合
	 */
	private ArrayList<Connections> listHightPermission = new ArrayList<Connections>();
	private ArrayList<Connections> listMiddlePermission = new ArrayList<Connections>();
	private ArrayList<Connections> listLowPermission = new ArrayList<Connections>();
	private boolean noPermission; //是否独乐
	/** 当前页面的状态*/
	public int type = 0;
	/** 可以请求*/
	private int Request = 0;
	/** 正在请求*/
	private int onRequesting = 1;
	/** 请求完毕*/
	private int Finish = 2;
	
	
	private String removeID;
	/**子模块控件的父类LinearLayout*/
	private LinearLayout module_Ll;
	/**返回的子模块名称集合*/
	private ArrayList<String> resource_list;
	private View peopleTreeIC; //目录父级View
	private TextView view_Tree_edit; //目录值
	private View peopleLabelIC; //标签父级View
	private TextView view_Label_edit;//标签值 
	
	private LinearLayout activityRootView; //最底部的父类LinearLayout
	private LinearLayout information_ll; //基本资料的父类LinearLayout
	private LinearLayout picture_Ll; //头像的父类LinearLayout
	private TextView alertdialog_Tv; //自定义对话框标题
	private TextView alertdialog_Yes; //自定义对话框的确认按钮
	private TextView alertdialog_No; //自定义对话框的取消按钮
	private EditText alertdialog_Et; //自定义对话框的输入框
	private ArrayList<String> work_experience; //工作经历集合
	private ArrayList<WorkExperience> workExperience_bean; //工作经历对象集合
	private View person_Line; //
	private View organization_Line;
	private View knowledge_Line;
	private View requirement_Line;
	

	private String[] newFiled = new String[] { "工作经验", "教育经历", "资源需求" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.people_activity_cread);
		initialize();
		init();
		initView();
		initData();
		initRequstData();

	}
	private void initialize() {
		people_request = new PeopleRequest(); // 向后台传入的对象
		// 自定义控件的集合
		Custom_list = new ArrayList<MyEditTextView>();
		peopleNameList = new ArrayList<PersonName>();
		categoryList = new ArrayList<Long>();
		tid = new ArrayList<Long>();
		permIds = new PermIds();
		customTagList = new ArrayList<Basic>();
		person = new Person();
		connectionNodeList = new ArrayList<ConnectionNode>();
		connectionNodeList2 = new ArrayList<ConnectionNode>();
		knowledgeNodeList = new ArrayList<KnowledgeNode>();
		affairNodeList = new ArrayList<AffairNode>();
	}
	/**
	 * 初始化请求数据
	 */
	private void initRequstData() {
		// 获取fromActivity类型进行不同的操作
				eFromActivityType = getIntent().getIntExtra(
						ENavConsts.EFromActivityType, -1);
				removeID = getIntent().getStringExtra(EConsts.Key.RELATE_REMOVE_ID);
		
		if (eFromActivityType == 1) {
			creadContacts_Tv.setText("创建人脉");
			people_request.opType = "1";
			
//			//新增的三个字段 工作经验，教育经历，资源需求
//			for (int i = 0; i < newFiled.length; i++) {
//				final MyEditTextView editTextView = new MyEditTextView(context);
//				editTextView.setTextLabel(newFiled[i]);
//				editTextView.setDelete(true);
//				information_ll.addView(editTextView,information_ll.indexOfChild(custom_Text_Etv)-1);
//				editTextView.getAddMore_Iv().setOnClickListener(
//						new OnClickListener() {
//							private AlertDialog delete_dialog;
//							@Override
//							public void onClick(View v) {
//								if (!TextUtils.isEmpty(editTextView.getText())) {
//									AlertDialog.Builder delete = new Builder(
//											NewConnectionsActivity.this);
//									View delete_view = View.inflate(NewConnectionsActivity.this,
//											R.layout.people_delete_alertdialog, null);
//									delete.setView(delete_view);
//									delete_dialog = delete.create();
//									delete_dialog.show();
//									alertdialog_Tv = (TextView) delete_view
//											.findViewById(R.id.alertdialog_delete_Tv);
//									alertdialog_Tv.setText("字段填写的数据也将一同被删除\n确认删除此字段？");
//									alertdialog_Yes = (TextView) delete_view
//											.findViewById(R.id.alertdialog_delete_Yes);
//									alertdialog_No = (TextView) delete_view
//											.findViewById(R.id.alertdialog_delete_No);
//									alertdialog_Yes.setOnClickListener(new OnClickListener() {
//										
//										@Override
//										public void onClick(View v) {
//											information_ll.removeView(editTextView);
//											Custom_list.remove(editTextView);
//											delete_dialog.dismiss();
//										}
//									});
//									alertdialog_No.setOnClickListener(new OnClickListener() {
//										
//										@Override
//										public void onClick(View v) {
//											delete_dialog.dismiss();												
//										}
//									});
//								}else{
//									information_ll.removeView(editTextView);
//									Custom_list.remove(editTextView);
//								}
//								
//								
//							}
//						});
//				Custom_list.add(editTextView);
//				
//		}
			
		} else if (eFromActivityType == 2) {
			creadContacts_Tv.setText("编辑人脉");
			people_request.opType = "4";
		} else if (eFromActivityType == 3) {
			creadContacts_Tv.setText("编辑个人资料");
			classify_Etv.setVisibility(View.GONE);
			sidazujian_Ll.setVisibility(View.GONE);
			people_request.opType = "5";
		} else if (eFromActivityType == 4) {
			creadContacts_Tv.setText("保存人脉");
			people_request.opType = "2";
		} else if (eFromActivityType == 5) {
			creadContacts_Tv.setText("转为人脉");
			people_request.opType = "3";
		}
		// 获取初始化数据
		people_details = (PeopleDetails) this.getIntent().getSerializableExtra(
				ENavConsts.datas);
		// 对初始化数据进行一一对应
		initListViewData();
		if (people_details != null) {// 编辑
			// 设置姓名
			if (people_details.people.peopleNameList != null
					&& people_details.people.peopleNameList.size() > 0) {
				// 中文姓名
				surname_Etv
						.setText(people_details.people.peopleNameList.get(0).lastname);
				name_Etv.setText(people_details.people.peopleNameList.get(0).firstname);
				// 英文名
				if (people_details.people.peopleNameList.size() > 1) {
					for (int i = 1; i < people_details.people.peopleNameList
							.size(); i++) {
						if (i == 1) {
							First_Etv.setVisibility(View.VISIBLE);
							Last_Etv.setVisibility(View.VISIBLE);
							surname_Etv.RotateUp();
							First_Etv
									.setText(people_details.people.peopleNameList
											.get(1).firstname);
							Last_Etv.setText(people_details.people.peopleNameList
									.get(1).lastname);
						} else {
							MyEditTextView first = new MyEditTextView(context);
							first.setTextLabel("First");
							first.setText(people_details.people.peopleNameList
									.get(i).firstname);
							MyEditTextView last = new MyEditTextView(context);
							last.setTextLabel("Last");
							last.setText(people_details.people.peopleNameList
									.get(i).lastname);
						}

					}
				}
			}
			// 设置头像，头像为从展示页面传递到编辑页的绝对路径
			if (!TextUtils.isEmpty(people_details.people.portrait)) {
				ImageLoader.load(picture_Iv, people_details.people.portrait);
			}
			if (people_details.people.customTagList != null) {
				if (!people_details.people.customTagList.isEmpty()) {
					for (int i = 0; i < people_details.people.customTagList
							.size(); i++) {
						Basic basic = people_details.people.customTagList
								.get(i);
						final MyEditTextView editTextView = new MyEditTextView(
								context);
						editTextView.setCustom(true);
						editTextView.setDelete(true);
						editTextView.setTextLabel(basic.name);
						editTextView.setText(basic.content);
						information_ll
								.addView(editTextView, information_ll
										.indexOfChild(custom_Text_Etv) - 1);
						Custom_list.add(editTextView);
						editTextView.getAddMore_Iv().setOnClickListener(
								new OnClickListener() {

									@Override
									public void onClick(View v) {
										Custom_list.remove(editTextView);
										information_ll.removeView(editTextView);
									}
								});
					}
				}
			}

			remark = people_details.people.remark;
			custom_field_Etv.setText(people_details.people.remark);
			post_Etv.setText(people_details.people.position);
			if (people_details.people.gender == 1) {
				gender_Etv.setText("男");
			} else if (people_details.people.gender == 2) {
				gender_Etv.setText("女");
			} else if (people_details.people.gender == 3) {
				gender_Etv.setText("未知");
			}

			// 10-娱乐人物、11-政治人物、12-体育人物、13-历史人物、14-文化人物、15-科学家、
			// 16-虚拟人物、17-行业人物、18-话题人物、99-其他人物
			classify_Etv.setText(people_details.people.peopleType);

			phone_Etv.setText(people_details.people.telephone);
			email_Etv.setText(people_details.people.email);
			company_Etv.setText(people_details.people.company);
			address_Etv.setText(people_details.people.address);

			// if (picture != null) {
			// Bitmap bitmap = BitmapFactory.decodeFile(person.portrait);
			// picture_Iv.setImageBitmap(bitmap);
			// }

			String locationCountry = TextUtils
					.isEmpty(people_details.people.locationCountry) ? ""
					: people_details.people.locationCountry;
			String locationCity = TextUtils
					.isEmpty(people_details.people.locationCity) ? ""
					: people_details.people.locationCity;
			String locationCounty = TextUtils
					.isEmpty(people_details.people.locationCounty) ? ""
					: people_details.people.locationCounty;
			area_Etv.setText(locationCountry + locationCity + locationCounty);
			// 关联
			if (people_details.people.asso != null && eFromActivityType != 4) {
				createKnowNewASSO(people_details.people.asso);
				updateAllUI();

				// 更新
				if (!people_details.people.asso.p.isEmpty()) {
					people_Ll.setVisibility(View.VISIBLE);
					asso_Tv.setVisibility(View.VISIBLE);
					if (connectionNodeList.size() == 1) {
						person_Line.setVisibility(View.GONE);
					} else {
						person_Line.setVisibility(View.VISIBLE);
					}

				}
				if (!people_details.people.asso.r.isEmpty()) {
					requirement_Ll.setVisibility(View.VISIBLE);
					asso_Tv.setVisibility(View.VISIBLE);
					if (people_details.people.asso.r.size() == 1) {
						requirement_Line.setVisibility(View.GONE);
					} else {
						requirement_Line.setVisibility(View.VISIBLE);
					}

				}
				if (!people_details.people.asso.o.isEmpty()) {
					organization_Ll.setVisibility(View.VISIBLE);
					asso_Tv.setVisibility(View.VISIBLE);
					if (people_details.people.asso.o.size() == 1) {
						organization_Line.setVisibility(View.GONE);
					} else {
						organization_Line.setVisibility(View.VISIBLE);
					}

				}
				if (!people_details.people.asso.k.isEmpty()) {
					knowledge_Ll.setVisibility(View.VISIBLE);
					asso_Tv.setVisibility(View.VISIBLE);
					if (people_details.people.asso.k.size() == 1) {
						knowledge_Line.setVisibility(View.GONE);
					} else {
						knowledge_Line.setVisibility(View.VISIBLE);
					}

				}
			}

			if (people_details.categoryList != null && eFromActivityType != 4) {
				if (!people_details.categoryList.isEmpty()) {

					ArrayList<String> strings = new ArrayList<String>();
					for (int i = 0; i < people_details.categoryList.size(); i++) {
						Long categoryId = people_details.categoryList.get(i).id;
						String categoryName = people_details.categoryList
								.get(i).name;
						UserCategory category = new UserCategory();
						category.setId(categoryId);
						category.setCategoryname(categoryName);
						listCategory.add(category);
						peopleTreeIC.setVisibility(View.VISIBLE); // 显示
						strings.add(TextStrUtil.checkCategoryname(listCategory
								.get(i)));
						categoryList.add(categoryId);
						view_Tree_edit.setText(TextStrUtil.getStringSize(9,
								strings));
					}
				}
			}

			if (people_details.tid != null && eFromActivityType != 4) {
				if (!people_details.tid.isEmpty()) {

					for (int i = 0; i < people_details.tid.size(); i++) {
						PersonTagRelation personTagRelation = people_details.tid
								.get(i);
						LableData data = new LableData();
						data.id = personTagRelation.tagId.intValue();
						data.tag = personTagRelation.tagName;
						lableData.add(data);
						tid.add(personTagRelation.tagId);
					}
					peopleLabelIC.setVisibility(View.VISIBLE);
					view_Label_edit.setText(TextStrUtil.getLableDataSize(9,
							lableData));
				}
			}
			if (people_details.people.permIds != null && eFromActivityType != 4) {
				if (!people_details.people.permIds.dales.isEmpty()) {
					for (int i = 0; i < people_details.people.permIds.dales
							.size(); i++) {
						PersonPermDales personPermDales = people_details.people.permIds.dales
								.get(i);
						Connections connections = new Connections();
						connections.setType(connections.type_persion + "");
						connections.jtContactMini
								.setId(personPermDales.id + "");
						connections.setName(personPermDales.name);
						listHightPermission.add(connections);
					}
				}

				if (!people_details.people.permIds.zhongles.isEmpty()
						&& eFromActivityType != 4) {
					for (int i = 0; i < people_details.people.permIds.zhongles
							.size(); i++) {
						PersonPermZhongles permZhongles = people_details.people.permIds.zhongles
								.get(i);
						Connections connections = new Connections();
						connections.setType(connections.type_persion + "");
						connections.jtContactMini.setId(permZhongles.id + "");
						connections.setName(permZhongles.name);
						listMiddlePermission.add(connections);
					}
				}
				if (!people_details.people.permIds.xiaoles.isEmpty()
						&& eFromActivityType != 4) {
					for (int i = 0; i < people_details.people.permIds.xiaoles
							.size(); i++) {
						PersonPermXiaoles permXiaoles = people_details.people.permIds.xiaoles
								.get(i);
						Connections connections = new Connections();
						connections.setType(connections.type_persion + "");
						connections.jtContactMini.setId(permXiaoles.id + "");
						connections.setName(permXiaoles.name);
						listLowPermission.add(connections);
					}
				}
			}
			if (people_details.people.permIds != null && eFromActivityType != 4) {
				permIds = people_details.people.permIds;
			}

			// 将这些值赋值给创建人脉的对象，避免值的丢失
			person.locationCountry = people_details.people.locationCountry;
			person.locationCounty = people_details.people.locationCounty;
			person.locationCity = people_details.people.locationCity;
			if (people_details.categoryList != null
					&& !people_details.categoryList.isEmpty()) {
				for (int i = 0; i < people_details.categoryList.size(); i++) {
					Category category = people_details.categoryList.get(i);
					categoryList.add(category.id);
				}
			}

			if (area_result != null) {
				person.locationCountry = area_result.province;
				person.locationCounty = area_result.county;
				person.locationCity = area_result.city;
			}
			if (people_details.tid != null && !people_details.tid.isEmpty()) {
				for (int i = 0; i < people_details.tid.size(); i++) {
					PersonTagRelation personTagRelation = people_details.tid
							.get(i);
					tid.add(personTagRelation.tagId);
				}
			}
		}
	}
	/**
	 * 将知识返回的关联对象转换为ASSORPOK对象
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
					if (obj.jtContactMini.isOnline()) {
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
					if (obj.organizationMini.isOnline()) {
						assoData.type = 4;
					} else {
						assoData.type = 5;
					}

					assoData.id = obj.getId();
					assoData.name = obj.getName();
					assoData.ownerid = App.getUserID();
					assoData.ownername = App.getNick();
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
	 * 将 ASSORPOK对象转换为知识需要的关联对象
	 * @return
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
					Pobj.setName(conn2.get(j).name);
					Pobj.setCareer(conn2.get(j).career);
					Pobj.setCompany(conn2.get(j).company);
					if(conn2.get(j).type==2)//type=2人脉
						Pobj.jtContactMini.isOnline=false;
					if(conn2.get(j).type==3)//type=3好友
						Pobj.jtContactMini.isOnline=true;
					Pobj.jtContactMini.image = conn2.get(j).picPath;
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
					Oobj.setID(conn2.get(j).id);
					Oobj.setName(conn2.get(j).name);
					Oobj.setCareer(conn2.get(j).career);
					Oobj.setCompany(conn2.get(j).company);
					Oobj.organizationMini.logo = conn2.get(j).picPath;
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

	// 四大组件ListView设置
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

	public void updateAllUI() {
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

	private void initData() {

	}
	//设置点击事件
	private void initView() {
		picture_Ll.setOnClickListener(this);
		gender_Etv.setOnClickListener(this);
		classify_Etv.setOnClickListener(this);
		area_Etv.setOnClickListener(this);
		custom_Text_Etv.setOnClickListener(this);
	}
	/**
	 * 人脉，组织适配器
	 * @author John
	 *
	 */
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
	/**
	 * 知识适配器
	 * */
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
	/**
	 * 
	 * 
	 * 事务适配器
	 * */
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

	/**
	 * 点击完成，进入人脉详情页面
	 * 
	 * @param view
	 */

	public void finishs(View view) {
		showLoadingDialogHy();
		if (!StringUtils.isEmpty(email_Etv.getText())
				&& !StringUtils.checkEmail(email_Etv.getText())) {
			Toast.makeText(this, "请输入有效的邮箱", 1).show();
			return;
		}

		if (people_details != null) {// 编辑
			if (eFromActivityType == 4) { // 保存成人脉
				person.fromPersonId = people_details.people.id;
			} else if(eFromActivityType == 3){
				if (avatarUrl != null) {
					person.portrait = avatarUrl;// 表示重新上传了一张头像
					Log.v("TOUXIANG", "上传的头像相对路径----" + person.portrait);
					App.getUser().setImage(avatarUrl);
					App.getUser().setNick(surname_Etv.getText()+name_Etv.getText());
				} else {// 表示直接点击完成，没有对头像做再选择操作则直接把传递过来的头像绝对路径进行截取返回
					person.portrait = people_details.people.portrait;// 人脉的头像要区别与组织和客户，是不同的后台进行开发的
				}
				person.id = people_details.people.id;
				person.fromUserId = people_details.people.fromUserId;
				if (!categoryList.isEmpty()) {
					people_request.categoryList = categoryList;
				}
				if (tid != null && !tid.isEmpty()) {
					people_request.tid = tid;
				}
				// 关联信息获取
				if (createNewASSO() != null) {
					people_request.asso = createNewASSO();
				}
				if (permIds.dales.isEmpty() && permIds.zhongles.isEmpty()
						&& permIds.xiaoles.isEmpty()) {
					permIds.dule = true;
				} else {
					permIds.dule = false;
				}

				if (permIds != null) {
					people_request.permIds = permIds;
				}
			}else if (eFromActivityType == 5) { // 转为人脉
				person.fromUserId = people_details.people.createUserId;
				if (!categoryList.isEmpty()) {
					people_request.categoryList = categoryList;
				}
				if (tid != null && !tid.isEmpty()) {
					people_request.tid = tid;
				}
				// 关联信息获取
				if (createNewASSO() != null) {
					people_request.asso = createNewASSO();
				}
				if (permIds.dales.isEmpty() && permIds.zhongles.isEmpty()
						&& permIds.xiaoles.isEmpty()) {
					permIds.dule = true;
				} else {
					permIds.dule = false;
				}

				if (permIds != null) {
					people_request.permIds = permIds;
				}
			} else {
				person.id = people_details.people.id;
				person.fromUserId = people_details.people.fromUserId;
				if (!categoryList.isEmpty()) {
					people_request.categoryList = categoryList;
				}
				if (tid != null && !tid.isEmpty()) {
					people_request.tid = tid;
				}
				// 关联信息获取
				if (createNewASSO() != null) {
					people_request.asso = createNewASSO();
				}
				if (permIds.dales.isEmpty() && permIds.zhongles.isEmpty()
						&& permIds.xiaoles.isEmpty()) {
					permIds.dule = true;
				} else {
					permIds.dule = false;
				}

				if (permIds != null) {
					people_request.permIds = permIds;
				}
			}
			if (avatarUrl != null) {
				person.portrait = avatarUrl;// 表示重新上传了一张头像
				Log.v("TOUXIANG", "上传的头像相对路径----" + person.portrait);
				
			} else {// 表示直接点击完成，没有对头像做再选择操作则直接把传递过来的头像绝对路径进行截取返回
				person.portrait = people_details.people.portrait;// 人脉的头像要区别与组织和客户，是不同的后台进行开发的
			}
		} else {// 创建
			// 只有编辑和创建的时候才有四大组件。
			if (!categoryList.isEmpty()) {
				people_request.categoryList = categoryList;
			}
			if (tid != null && !tid.isEmpty()) {
				people_request.tid = tid;
			}
			// 关联信息获取
			if (createNewASSO() != null) {
				people_request.asso = createNewASSO();
			}
			if (permIds.dales.isEmpty() && permIds.zhongles.isEmpty()
					&& permIds.xiaoles.isEmpty()) {
				permIds.dule = true;
			} else {
				permIds.dule = false;
			}

			if (permIds != null) {
				people_request.permIds = permIds;
			}
			if (avatarUrl != null) {
				person.portrait = avatarUrl;// 表示重新上传了一张头像
				Log.v("TOUXIANG", "上传的头像相对路径----" + person.portrait);
			}
		}
		person.createUserId = Long.parseLong(App.getUserID());
		if (!TextUtils.isEmpty(surname_Etv.getText())) {

			person.position = post_Etv.getText();

			if ("男".equals(gender_Etv.getText())) {
				person.gender = 1;
			} else if ("女".equals(gender_Etv.getText())) {
				person.gender = 2;
			} else if ("未知".equals(gender_Etv.getText())) {
				person.gender = 3;
			}
			person.peopleType = classify_Etv.getText();
			if ("娱乐人物".equals(classify_Etv.getText())) {
				person.typeId = 99;
			} else if ("政治人物".equals(classify_Etv.getText())) {
				person.typeId = 100;
			} else if ("体育人物".equals(classify_Etv.getText())) {
				person.typeId = 101;
			} else if ("历史人物".equals(classify_Etv.getText())) {
				person.typeId = 102;
			} else if ("文化人物".equals(classify_Etv.getText())) {
				person.typeId = 103;
			} else if ("科学家".equals(classify_Etv.getText())) {
				person.typeId = 104;
			} else if ("虚拟人物".equals(classify_Etv.getText())) {
				person.typeId = 105;
			} else if ("行业人物".equals(classify_Etv.getText())) {
				person.typeId = 106;
			} else if ("话题人物".equals(classify_Etv.getText())) {
				person.typeId = 107;
			} else if ("其他人物".equals(classify_Etv.getText())) {
				person.typeId = 108;
			}
			person.telephone = phone_Etv.getText();
			person.email = email_Etv.getText();
			contact_information = new ArrayList<Basic>();
			Basic contactInfoPhone = new Basic();
			contactInfoPhone.content = phone_Etv.getText().toString();
			contactInfoPhone.name = "电话";
			contactInfoPhone.type = "1";
			contactInfoPhone.subtype = "1";
			contact_information.add(contactInfoPhone);
			Basic contactInfoEmail = new Basic();
			contactInfoEmail.content = email_Etv.getText().toString();
			contactInfoEmail.name = "邮箱";
			contactInfoEmail.type = "4";
			contactInfoEmail.subtype = "1";
			contact_information.add(contactInfoEmail);
			person.contactInformationList = contact_information;
			person.company = company_Etv.getText();
			person.address = address_Etv.getText();
			if (Custom_list != null && !Custom_list.isEmpty()) {
				for (int i = 0; i < Custom_list.size(); i++) {
					MyEditTextView myEditTextView = Custom_list.get(i);
					Basic basic = new Basic();
					basic.content = myEditTextView.getText();
					basic.name = myEditTextView.getTextLabel();
					basic.type = "N";
					customTagList.add(basic);
				}
				person.customTagList = customTagList;
			}

			// 中文名
			PersonName zhongwenName = new PersonName();
			zhongwenName.lastname = surname_Etv.getText();
			zhongwenName.firstname = name_Etv.getText();

			peopleNameList.add(zhongwenName);
			if (!TextUtils.isEmpty(First_Etv.getText())
					|| !TextUtils.isEmpty(Last_Etv.getText())) {
				// // 英文名
				PersonName yinwenName = new PersonName();
				yinwenName.firstname = First_Etv.getText();
				yinwenName.lastname = Last_Etv.getText();
				peopleNameList.add(yinwenName);
			}
			person.peopleNameList = peopleNameList;

			// 地区

			// 备注
			person.remark = custom_field_Etv.getText();
			KeelLog.d("==>>", person.remark);

			people_request.people = person;

			if (type == Request) {
				if (people_request != null) {
					PeopleReqUtil.doRequestWebAPI(context, this,
							people_request, null,
							PeopleRequestType.PEOPLE_REQ_CREATE);
				}
				type = onRequesting;
			} else {
				ToastUtil.showToast(context, "已经在努力中..");
			}
		} else {
			Toast.makeText(context, "姓名不能为空，请填写后完成", 0).show();
			dismissLoadingDialogHy();
			return;
		}
	}

	/**
	 * 去除焦点
	 */
	@Override
	protected void onStart() {
		super.onStart();
		surname_Etv.getEditText().clearFocus();
		name_Etv.getEditText().clearFocus();
		First_Etv.getEditText().clearFocus();
		Last_Etv.getEditText().clearFocus();
		post_Etv.getEditText().clearFocus();
		phone_Etv.getEditText().clearFocus();
		email_Etv.getEditText().clearFocus();
		address_Etv.getEditText().clearFocus();
		company_Etv.getEditText().clearFocus();
	}
	/**
	 * 初始化控件
	 */
	private void init() {
		activityRootView = (LinearLayout) findViewById(R.id.People_activityRootView); // activtiy最外层Layout
		picture_Iv = (CircleImageView) findViewById(R.id.picture_Iv);
		asso_Tv = (TextView) findViewById(R.id.asso_Tv);
		quit_Rl = (RelativeLayout) findViewById(R.id.quit_Rl);
		creadContacts_Tv = (TextView) findViewById(R.id.creadContacts_Tv);
		businessCard_Tv = (ImageView) findViewById(R.id.businessCard_Tv);
		finish_Tv = (TextView) findViewById(R.id.finishs_Tv);
		address_Etv = (MyEditTextView) findViewById(R.id.address_Etv);
		area_Etv = (MyEditTextView) findViewById(R.id.area_Etv);
		classify_Etv = (MyEditTextView) findViewById(R.id.classify_Etv);
		company_Etv = (MyEditTextView) findViewById(R.id.company_Etv);
		email_Etv = (MyEditTextView) findViewById(R.id.email_Etv);
		gender_Etv = (MyEditTextView) findViewById(R.id.gender_Etv);
		name_Etv = (MyEditTextView) findViewById(R.id.name_Etv);
		phone_Etv = (MyEditTextView) findViewById(R.id.phone_Etv);
		surname_Etv = (MyEditTextView) findViewById(R.id.surname_Etv);
		post_Etv = (MyEditTextView) findViewById(R.id.post_Etv);
		custom_Text_Etv = (MyEditTextView) findViewById(R.id.custom_Text_Etv);
		custom_field_Etv = (MyEditTextView) findViewById(R.id.custom_field_Etv);
		addmore_Rl = (RelativeLayout) findViewById(R.id.Addmore_Rl);
		First_Etv = (MyEditTextView) findViewById(R.id.First_Etv);
		Last_Etv = (MyEditTextView) findViewById(R.id.Last_Etv);
		module_Map = new HashMap<String, MyitemView>();
		sidazujian_Ll = (LinearLayout) findViewById(R.id.sidazujian_Ll);
		picture_Ll = (LinearLayout) findViewById(R.id.picture_Ll);
		module_Ll = (LinearLayout) findViewById(R.id.module_Ll);
		
		people = (BasicListView2) findViewById(R.id.people_conn);
		organization = (BasicListView2) findViewById(R.id.organization_conn);
		knowledge = (BasicListView2) findViewById(R.id.knowledge_conn);
		requirement = (BasicListView2) findViewById(R.id.requirement_conn);
		people_Ll = (LinearLayout) findViewById(R.id.people_person_Ll);
		organization_Ll = (LinearLayout) findViewById(R.id.organization_person_Ll);
		knowledge_Ll = (LinearLayout) findViewById(R.id.knowledge_person_Ll);
		requirement_Ll = (LinearLayout) findViewById(R.id.requirement_person_Ll);
		person_Line = findViewById(R.id.person_Line);
		organization_Line =  findViewById(R.id.organization_Line);
		knowledge_Line = findViewById(R.id.knowledge_Line);
		requirement_Line =  findViewById(R.id.requirement_Line);
		
		information_ll = (LinearLayout) findViewById(R.id.information_ll);

		phone_Etv.setNumEdttext_inputtype();

		// 目录展示
		peopleTreeIC = findViewById(R.id.peopleTreeIC);
		peopleTreeIC.setOnClickListener(this);
		view_Tree_edit = (TextView) peopleTreeIC
				.findViewById(R.id.view_et_edit);
		TextView view_Tree_name = (TextView) peopleTreeIC
				.findViewById(R.id.view_tv_name);
		view_Tree_name.setText("目录");
		// 标签展示
		peopleLabelIC = findViewById(R.id.peopleLabelIC);
		view_Label_edit = (TextView) peopleLabelIC
				.findViewById(R.id.view_et_edit);
		TextView view_Label_name = (TextView) peopleLabelIC
				.findViewById(R.id.view_tv_name);
		view_Label_name.setText("标签");

		relevance_people = (LinearLayout) findViewById(R.id.relevance_people);
		catalogue_people = (LinearLayout) findViewById(R.id.catalogue_people);
		label_people = (LinearLayout) findViewById(R.id.label_people);
		jurisdiction_people = (LinearLayout) findViewById(R.id.jurisdiction_people);
		// Custom_Activity.setChange(true);
		float density = this.getResources().getDisplayMetrics().density;
		System.out.println("比例：" + density);
		// Listener();
		quit_Rl.setOnClickListener(this);

		custom_Text_Etv.setOnClickListener(this);
		custom_field_Etv.setOnClickListener(this);
		// 四大组件
		relevance_people.setOnClickListener(this);
		catalogue_people.setOnClickListener(this);
		label_people.setOnClickListener(this);
		jurisdiction_people.setOnClickListener(this);
		
		activityRootView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
					 InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
					    //强制隐藏键盘  
				return imm.hideSoftInputFromWindow(activityRootView.getWindowToken(), 0);
			}
		});
		
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		address_Etv.NoSingleLine();
		// 添加layout大小发生改变监听器
		// activityRootView.addOnLayoutChangeListener(this);
		/*surname_Etv.getMyedittext_Addmore_Rl().setOnClickListener(new  OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (First_Etv.isShown()) {
					First_Etv.setVisibility(View.GONE);
					Last_Etv.setVisibility(View.GONE);
					surname_Etv.RotateDown();
				}else{
					First_Etv.setVisibility(View.VISIBLE);
					Last_Etv.setVisibility(View.GONE);
					surname_Etv.RotateUp();
				}
				
			}
		});*/
		surname_Etv.getMyedittext_Addmore_Rl().setVisibility(View.GONE);
		
		
		}

	/**
	 * 将模块选择动态的添加到布局中
	 * 
	 * @param module_list2
	 */
	private void AddMore(ArrayList<String> module_list2) {
		module = null;

		LinearLayout layout = new LinearLayout(this);
		System.out.println("控件数据集合" + module_list2);
		module_Ll.removeAllViews();
		if (module_list2 != null) {
			for (final String string : module_list2) {
				module = new MyitemView(this);
				module.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
				layout.setOrientation(LinearLayout.VERTICAL);
				module.setText_label(string);
				System.out.println("控件数据" + string);
				layout.addView(module, new LinearLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				module.setOnClickListener(new OnClickListener() {
					private Intent intent;

					@Override
					// 根据标题来判断进入那个Activity
					public void onClick(View v) {
						if ("基本信息".equals(string)) {
							intent = new Intent(NewConnectionsActivity.this,
									NewConnectionsActivity.class);
							startActivityForResult(intent, 0);
						} else if ("联系方式".equals(string)) {
							intent = new Intent(NewConnectionsActivity.this,
									ContactInformationActivity.class);
							intent.putExtra("Contact_information",
									contact_information);

							startActivityForResult(intent, 0);
						} else if ("个人情况".equals(string)) {
							intent = new Intent(NewConnectionsActivity.this,
									PersonalInformationActivity.class);
							startActivityForResult(intent, 0);
						} else if ("投资意向".equals(string)) {
							intent = new Intent(NewConnectionsActivity.this,
									InvestmentActivity.class);
							startActivityForResult(intent, 0);
						} else if ("融资意向".equals(string)) {
							intent = new Intent(NewConnectionsActivity.this,
									FinancingActivity.class);
							startActivityForResult(intent, 0);
						} else if ("专家需求".equals(string)) {
							intent = new Intent(NewConnectionsActivity.this,
									SpecialistNeedsActivity.class);
							startActivityForResult(intent, 0);
						} else if ("教育经历".equals(string)) {
							intent = new Intent(NewConnectionsActivity.this,
									EducationActivity.class);
							startActivityForResult(intent, 0);
						} else if ("专家身份".equals(string)) {
							intent = new Intent(NewConnectionsActivity.this,
									SpecialistIdentityActivity.class);
							startActivityForResult(intent, 0);
						} else if ("工作经历".equals(string)) {
							intent = new Intent(NewConnectionsActivity.this,
									WorkExperienceActivity.class);
							Bundle bundle = new Bundle();
							bundle.putSerializable("WorkExperience_Bean",
									workExperience_bean);
							intent.putExtras(bundle);
							startActivityForResult(
									intent,
									PeopleModuleReqCode.WORK_EXPERIENCE_REQ_CODE);
						} else if ("社会活动".equals(string)) {
							intent = new Intent(NewConnectionsActivity.this,
									SocietyActivity.class);
							startActivityForResult(intent, 0);
						} else if ("会面情况".equals(string)) {
							intent = new Intent(NewConnectionsActivity.this,
									MeetingActivity.class);
							startActivityForResult(intent, 0);
						} else if ("备注".equals(string)) {
							intent = new Intent(NewConnectionsActivity.this,
									RemarkActivity.class);
							startActivity(intent);
						} else if ("自定义模块".equals(string)) {
							intent = new Intent(NewConnectionsActivity.this,
									CustomActivity.class);
							startActivityForResult(intent, 0);
						}
					}
				});
				module_Map.put(string, module);
			}

			module_Ll.addView(layout);
		}
	}
	/**
	 * 计算裁剪尺寸
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
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
	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			switch (requestCode) {
			case ENavConsts.ActivityReqCode.REQUEST_CHOOSE_SELECT:
				// 多级选择回调界面
				setChooseText((ArrayList<Metadata>) data
						.getSerializableExtra(ENavConsts.DEMAND_CHOOSE_DATA));
				break;
			default:
				break;
			}

			switch (resultCode) {
			case 999:
				isNull = data.getBooleanExtra("isNull", false);
				break;
			case 99:
				module_list = data.getStringArrayListExtra("module");
				resource_list = data.getStringArrayListExtra("resource_list");
				if (resource_list != null && !resource_list.isEmpty()) {
					module_list.addAll(resource_list);
				}
				AddMore(module_list);
				break;
			case 11:
				String classify = data.getStringExtra("classify");
				classify_Etv.setText(classify);
				break;
			case 22:
				String gender = data.getStringExtra("gender");
				gender_Etv.setText(gender);
				break;
			case 0:
				contact_information = (ArrayList<Basic>) data
						.getSerializableExtra("Contact_information");
				person.contactInformationList = contact_information;
				break;
			case 1:
				ArrayList<String> Personal_information = data
						.getStringArrayListExtra("Personal_information");
				if (Personal_information != null) {
					AdaptiveListView(MakeListView.ToListviewAdapter(this,
							Personal_information, module_Map.get("个人情况")
									.getMyitemview_Lv()), displayMetrics);
				}
				break;
			case 2:
				ArrayList<String> Investment = data
						.getStringArrayListExtra("Investment");
				if (Investment != null) {
					AdaptiveListView(MakeListView.ToListviewAdapter(this,
							Investment, module_Map.get("投资意向")
									.getMyitemview_Lv()), displayMetrics);
				}
				break;
			case 3:
				ArrayList<String> Financing = data
						.getStringArrayListExtra("Financing");
				if (Financing != null) {
					AdaptiveListView(MakeListView.ToListviewAdapter(this,
							Financing, module_Map.get("融资意向")
									.getMyitemview_Lv()), displayMetrics);
				}

				break;
			case 4:
				ArrayList<String> Specialist_identity = data
						.getStringArrayListExtra("Specialist_identity");
				if (Specialist_identity != null) {
					AdaptiveListView(MakeListView.ToListviewAdapter(this,
							Specialist_identity, module_Map.get("专家身份")
									.getMyitemview_Lv()), displayMetrics);

				}
				break;
			case 5:
				ArrayList<String> Specialist_needs = data
						.getStringArrayListExtra("Specialist_needs");
				if (Specialist_needs != null) {
					AdaptiveListView(MakeListView.ToListviewAdapter(this,
							Specialist_needs, module_Map.get("专家需求")
									.getMyitemview_Lv()), displayMetrics);

				}
				break;
			case 6:
				ArrayList<String> Education_Activity = data
						.getStringArrayListExtra("Education");
				if (Education_Activity != null) {
					AdaptiveListView(MakeListView.ToListviewAdapter(this,
							Education_Activity, module_Map.get("教育经历")
									.getMyitemview_Lv()), displayMetrics);
				}
				break;

			case PeopleModuleReqCode.WORK_EXPERIENCE_REQ_CODE:
				Bundle bundle = data.getExtras();
				bundle.setClassLoader(getClassLoader());
				workExperience_bean = (ArrayList<WorkExperience>) bundle
						.getSerializable("WorkExperience_Bean");
				person.workExperienceList = workExperience_bean;
				work_experience = data
						.getStringArrayListExtra(ENavConsts.WORK_EXPERIENCE);
				if (work_experience != null) {
				}
				break;
			case 8:
				ArrayList<String> Society = data
						.getStringArrayListExtra("Society");
				if (Society != null) {
					AdaptiveListView(
							MakeListView.ToListviewAdapter(this, Society,
									module_Map.get("社会活动").getMyitemview_Lv()),
							displayMetrics);
				}
				break;
			case 9:
				ArrayList<String> Meeting = data
						.getStringArrayListExtra("Meeting");
				if (Meeting != null) {
					AdaptiveListView(
							MakeListView.ToListviewAdapter(this, Meeting,
									module_Map.get("会面情况").getMyitemview_Lv()),
							displayMetrics);
				}
				break;
			}
			
			if (1006 == requestCode) {
				remark = data.getStringExtra("Remark_Activity");
				custom_field_Etv.setText(remark);
			}
			
			if (9 == requestCode) {
				picture = (ArrayList<JTFile>) data
						.getSerializableExtra(ENavConsts.DEMAND_INTENT_SELECTED_PICTURE);
				System.out.println("上传头像---picture:--" + picture);
				if (picture != null && !picture.isEmpty() && picture.size() > 0) {
					BitmapFactory.Options options = new BitmapFactory.Options(); 
					options.inJustDecodeBounds = true;  
				    BitmapFactory.decodeFile(picture.get(0).mLocalFilePath, options);  
				  
				    // Calculate inSampleSize  
				    options.inSampleSize = calculateInSampleSize(options, 480, 800);  
				  
				    // Decode bitmap with inSampleSize set  
				    options.inJustDecodeBounds = false;  
				      
				    Bitmap bm = BitmapFactory.decodeFile(picture.get(0).mLocalFilePath, options); 
				    picture_Iv.setImageBitmap(bm);
					// 调上传头像接口
					OrganizationPictureUploader uploader = new OrganizationPictureUploader(
							this);
					JTFile jtFile = new JTFile();
					jtFile.setId(String.valueOf(picture.get(0).mCreateTime));
					jtFile.mLocalFilePath = picture.get(0).mLocalFilePath;
					jtFile.mType = 4;
					uploader.startNewUploadTask(jtFile);
				}
			}

		}

		if (101 == requestCode) { //
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
						if (connectionNodeList.size() == 1) {
							person_Line.setVisibility(View.GONE);
						} else {
							person_Line.setVisibility(View.VISIBLE);
						}
						people_Ll.setVisibility(View.VISIBLE);
						peopleGroupAdapter.notifyDataSetChanged();
						asso_Tv.setVisibility(View.VISIBLE);
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
						if (connectionNodeList2.size() == 1) {
							organization_Line.setVisibility(View.GONE);
						} else {
							organization_Line.setVisibility(View.VISIBLE);
						}
						organization_Ll.setVisibility(View.VISIBLE);
						organizationGroupAdapter.notifyDataSetChanged();
						asso_Tv.setVisibility(View.VISIBLE);
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
						if (knowledgeNodeList.size() == 1) {
							knowledge_Line.setVisibility(View.GONE);
						} else {
							knowledge_Line.setVisibility(View.VISIBLE);
						}
						knowledge_Ll.setVisibility(View.VISIBLE);
						knowledgeGroupAdapter.notifyDataSetChanged();
						asso_Tv.setVisibility(View.VISIBLE);
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
						if (affairNodeList.size() == 1) {
							requirement_Line.setVisibility(View.GONE);
						} else {
							requirement_Line.setVisibility(View.VISIBLE);
						}
						requirement_Ll.setVisibility(View.VISIBLE);
						requirementGroupAdapter.notifyDataSetChanged();
						asso_Tv.setVisibility(View.VISIBLE);
					}
				}

			}
		}

		else if (102 == requestCode) { // 目录
			if (resultCode == Activity.RESULT_OK) {

				listCategory = (ArrayList<UserCategory>) data.getSerializableExtra(EConsts.Key.KNOWLEDGE_CATEGORY_LIST);
				boolean isSelect = data.getBooleanExtra(EConsts.Key.KNOWLEDGE_CATEGORY_GROUP, false);
				if (listCategory.size() == 1 && "未分组".equals(listCategory.get(0).getCategoryname())) {
					if (isSelect) {
						ArrayList<UserCategory> tmpArrayList = new ArrayList<UserCategory>();
						tmpArrayList.add(listCategory.get(listCategory.size()-1));
						updatePeopleCategoryUi(tmpArrayList);
						tmpArrayList.clear();
					} else {
						updatePeopleCategoryUi(null);
					}
				} else {
					updatePeopleCategoryUi(listCategory);
				}
			}
		}

		// 标签
		else if (103 == requestCode) {

			String MSG = "";

			if (resultCode == Activity.RESULT_OK) {
				lableData = (ArrayList<LableData>) data
						.getSerializableExtra(ENavConsts.DEMAND_LABEL_DATA);
				tid.clear();
				ArrayList<String> tagStringList = new ArrayList<String>();
				if (lableData != null && !lableData.isEmpty()) {
					for (LableData userTag : lableData) {
						tagStringList.add(userTag.tag);
						tid.add((long) userTag.id);
					}

					peopleLabelIC.setVisibility(View.VISIBLE);
					view_Label_edit.setText(TextStrUtil.getLableDataSize(9,
							lableData));
				} else {
					peopleLabelIC.setVisibility(View.GONE);
				}
			}

		}

		// 权限
		else if (104 == requestCode) {
			String MSG = "";
			if (resultCode == Activity.RESULT_OK) {
				listHightPermission = new ArrayList<Connections>();
				listMiddlePermission = new ArrayList<Connections>();
				listLowPermission = new ArrayList<Connections>();
				permIds.dales.clear();
				permIds.zhongles.clear();
				permIds.xiaoles.clear();
				noPermission = data.getBooleanExtra("noPermission", false);

				Log.i(TAG, MSG + " noPermission = " + noPermission);
				listHightPermission = (ArrayList<Connections>) data
						.getSerializableExtra("listHightPermission");
				listMiddlePermission = (ArrayList<Connections>) data
						.getSerializableExtra("listMiddlePermission");
				listLowPermission = (ArrayList<Connections>) data
						.getSerializableExtra("listLowPermission");
				if (listHightPermission != null) {
					if (!listHightPermission.isEmpty()) {

						for (int i = 0; i < listHightPermission.size(); i++) {
							PersonPermDales dales = new PersonPermDales();
							if (!TextUtils.isEmpty(listHightPermission.get(i)
									.getId())) {
								dales.id = Long.parseLong(listHightPermission
										.get(i).getId());
							}

							dales.name = listHightPermission.get(i).getName();
							permIds.dales.add(dales);
						}
					}
				}
				if (listMiddlePermission != null) {
					if (!listMiddlePermission.isEmpty()) {

						for (int i = 0; i < listMiddlePermission.size(); i++) {
							PersonPermZhongles zhongles = new PersonPermZhongles();
							if (!TextUtils.isEmpty(listMiddlePermission.get(i)
									.getId())) {

								zhongles.id = Long
										.parseLong(listMiddlePermission.get(i)
												.getId());
							}
							zhongles.name = listMiddlePermission.get(i)
									.getName();
							permIds.zhongles.add(zhongles);
						}
					}
				}
				if (listLowPermission != null) {
					if (!listLowPermission.isEmpty()) {
						for (int i = 0; i < listLowPermission.size(); i++) {
							PersonPermXiaoles xiaoles = new PersonPermXiaoles();
							if (!TextUtils.isEmpty(listLowPermission.get(i)
									.getId())) {
								xiaoles.id = Long.parseLong(listLowPermission
										.get(i).getId());
							}
							xiaoles.name = listLowPermission.get(i).getName();
							permIds.xiaoles.add(xiaoles);
						}
					}

				}

			}
		}
		System.out.println("onActivityResult");
	}

	private void updatePeopleCategoryUi(ArrayList<UserCategory> tmpArrayList) {
		categoryList.clear();
		if (tmpArrayList != null && !tmpArrayList.isEmpty()) {
			peopleTreeIC.setVisibility(View.VISIBLE); // 显示
			ArrayList<String> list = new ArrayList<String>();
			for (int i = 0; i < tmpArrayList.size(); i++) {
				list.add(TextStrUtil.checkCategoryname(tmpArrayList.get(i)));
				categoryList.add(tmpArrayList.get(i).recursiveGetCategoryId());
			}
			view_Tree_edit.setText(TextStrUtil.getStringSize(9, list));
		} else {
			peopleTreeIC.setVisibility(View.GONE); // 隐藏
		}
	}
	public void setChooseText(ArrayList<Metadata> data) {
		// 地区
		if (metadataArea != null) {
			metadataArea.clear();
		}
		metadataArea = data;
		area_result = ChooseDataUtil.getMetadataName(metadataArea);
		// area_Etv.setText((TextUtils.isEmpty(area_result.province) ? ""
		// : area_result.province)
		// + (TextUtils.isEmpty(area_result.city) ? "" : area_result.city)
		// + (TextUtils.isEmpty(area_result.county) ? ""
		// : area_result.county));
		// 去除直辖市名字重叠的问题
		area_Etv.setText(getAreaStr(area_result));
		// 将这些值赋值给创建人脉的对象，避免值的丢失
		person.locationCountry = area_result.province;
		person.locationCounty = area_result.county;
		person.locationCity = area_result.city;
		if (metadataArea != null) {
			if (!metadataArea.isEmpty()) {
				for (int i = 0; i < metadataArea.size(); i++) {
					person.regionId = Long.parseLong(metadataArea.get(i).id);

					for (Metadata metadata : metadataArea) {
						if (!metadata.childs.isEmpty()) {

							// 有二级
							for (Metadata data2 : metadata.childs) {
								// 有三级
								if (!data2.childs.isEmpty()) {

									for (Metadata data3 : data2.childs) {
										if (!data3.childs.isEmpty()) {
											person.regionId = Long
													.parseLong(data3.id);
										}
									}
									person.regionId = Long.parseLong(data2.id);
								}
							}
						}
						person.regionId = Long.parseLong(metadata.id);
					}

				}

			}
		}
	}
	/**
	 * 获取地区对象
	 * @param area_result
	 * @return
	 */
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

		}
		return area;
	}

	/**
	 * 去模块选择页面
	 * 
	 * @param view
	 */
	public void ToModule(View view) {
		Intent intent = new Intent(NewConnectionsActivity.this,
				MoreModuleActivity.class);
		if (module_list != null) {
			intent.putExtra("Module_List", module_list);
		}
		if (resource_list != null) {
			intent.putExtra("resource_list", resource_list);
		}
		startActivityForResult(intent, 0);
	}

	/**
	 * 控件的点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.gender_Etv:
			Intent intent3 = new Intent(this, GenderActivity.class);
			startActivityForResult(intent3, NewConnectionsActivity);
			break;
		case R.id.classify_Etv:
			Intent intent4 = new Intent(this, ClassifyActivity.class);
			startActivityForResult(intent4, NewConnectionsActivity);
			break;
		case R.id.custom_field_Etv:
			Intent intent = new Intent(context, RemarkActivity.class);
			if (!TextUtils.isEmpty(remark)) {
				intent.putExtra("Remark_Activity", remark);
			}
			startActivityForResult(intent, 1006);
			break;
		case R.id.custom_Text_Etv:
			AlertDialog.Builder builder = new Builder(
					NewConnectionsActivity.this);
			View view2 = View.inflate(NewConnectionsActivity.this,
					R.layout.people_alertdialog_module, null);
			builder.setView(view2);
			create = builder.create();
			alertdialog_Tv = (TextView) view2
					.findViewById(R.id.alertdialog_module_Tv);
			alertdialog_Yes = (TextView) view2
					.findViewById(R.id.alertdialog_module_Yes);
			alertdialog_No = (TextView) view2
					.findViewById(R.id.alertdialog_module_No);
			alertdialog_Et = (EditText) view2
					.findViewById(R.id.alertdialog_module_Et);
			alertdialog_Et.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					if (s.length() >= 6) {
						Toast.makeText(context, "温馨提示：自定义只能输入6个字符", 0).show();
					}
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {

				}

				@Override
				public void afterTextChanged(Editable arg0) {

				}
			});
			alertdialog_Tv.setText("添加自定义字段");
			alertdialog_Yes.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String text = alertdialog_Et.getText().toString().trim();
					final MyEditTextView editTextView = new MyEditTextView(
							context);
					editTextView.setDelete(true);
					
					editTextView.getAddMore_Iv().setOnClickListener(
							new OnClickListener() {
								private AlertDialog delete_dialog;

								@Override
								public void onClick(View v) {
									if (!TextUtils.isEmpty(editTextView
											.getText())) {
										AlertDialog.Builder delete = new Builder(
												NewConnectionsActivity.this);
										View delete_view = View
												.inflate(
														NewConnectionsActivity.this,
														R.layout.people_delete_alertdialog,
														null);
										delete.setView(delete_view);
										delete_dialog = delete.create();
										delete_dialog.show();
										alertdialog_Tv = (TextView) delete_view
												.findViewById(R.id.alertdialog_delete_Tv);
										alertdialog_Tv
												.setText("字段填写的数据也将一同被删除\n确认删除此字段？");
										alertdialog_Yes = (TextView) delete_view
												.findViewById(R.id.alertdialog_delete_Yes);
										alertdialog_No = (TextView) delete_view
												.findViewById(R.id.alertdialog_delete_No);
										alertdialog_Yes
												.setOnClickListener(new OnClickListener() {

													@Override
													public void onClick(View v) {
														information_ll
																.removeView(editTextView);
														Custom_list
																.remove(editTextView);
														delete_dialog.dismiss();
													}
												});
										alertdialog_No
												.setOnClickListener(new OnClickListener() {

													@Override
													public void onClick(View v) {
														delete_dialog.dismiss();
													}
												});
									} else {
										information_ll.removeView(editTextView);
										Custom_list.remove(editTextView);
									}

								}
							});
					
//					if ("工作经验".equals(text)||"教育经历".equals(text)||"资源需求".equals(text)) {
//						ToastUtil.showToast(context, "此字段已存在");
//					}else{
						information_ll.addView(editTextView,
								information_ll.indexOfChild(custom_Text_Etv) - 1);
						Custom_list.add(editTextView);
						if (!TextUtils.isEmpty(text)) {
							editTextView.setTextLabel(text);
						} else {
							editTextView.setTextLabel("自定义");
						}
//					}
				
					create.dismiss();
				}
			});
			alertdialog_No.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					create.dismiss();
				}
			});
			create.show();

			break;
		case R.id.quit_Rl:
			finish();
			break;
		case R.id.picture_Ll:
			ENavigate.startSelectPictureActivityforSingleSelection(
					NewConnectionsActivity.this, 9, picture, true);
			break;

		case R.id.area_Etv:
			ENavigate.startChooseActivityForResult(this, false, "区域",
					ChooseDataUtil.CHOOSE_type_Area, null);
			break;
		case R.id.relevance_people://2015/10/20 取消关联时姓名与关联绑定
//			if (!TextUtils.isEmpty(surname_Etv.getText() + name_Etv.getText())) {
				currentRequestCode = 0;
				ENavigate.startRelatedResourceActivityForResult(
						NewConnectionsActivity.this, 101, /*surname_Etv.getText()
								+ name_Etv.getText()*/null, ResourceType.People,
						null, null);
//			} else {
//				Toast.makeText(context, "姓名不能为空，请填写后关联", 0).show();
//			}
			break;
		case R.id.catalogue_people:
			ENavigate.startKnowledgeCategoryActivityForResult(
					NewConnectionsActivity.this, 102, listCategory,
					ModuleType.PEOPLE, true,"创建人脉");
			break;
		case R.id.peopleTreeIC:
			ENavigate.startKnowledgeCategoryActivityForResult(
					NewConnectionsActivity.this, 102, listCategory,
					ModuleType.PEOPLE, true,"创建人脉");
			break;
		case R.id.label_people:
			ENavigate.startCheckLabelActivity(NewConnectionsActivity.this, 103,
					lableData, CreateLabelActivity.ModulesType.PeopleModules);
			break;
		case R.id.jurisdiction_people:
			intent = new Intent(this, PermissionActivity.class);
			intent.putExtra(EConsts.Key.FROM_ACTIVITY,
					CreateKnowledgeActivity.class.getSimpleName());
			intent.putExtra("listHightPermission", listHightPermission);
			intent.putExtra("listMiddlePermission", listMiddlePermission);
			intent.putExtra("listLowPermission", listLowPermission);
			startActivityForResult(intent, 104);
			break;
		default:
			break;
		}
	}

	@Override
	public void bindData(int tag, Object object) {
		switch (tag) {
		case PeopleRequestType.PEOPLE_REQ_CREATE:
			if (object != null) {
				BaseResult result = (BaseResult) object;
				Log.v("TOUXIANG", "编辑页点击返回的obj---->" + result.toString());
				if (result.success) {
					if (eFromActivityType == 1) {// 创建，跳到详情页
						/*Intent intent = new Intent(this,
								ContactsDetailsActivity.class);// 进入人脉详情页
						intent.putExtra(EConsts.Key.PERSON_ID,result.id );// 对象id
						intent.putExtra(ENavConsts.EFromActivityType, 1);// 详情入口标记
						startActivity(intent);*/
						// 新 人脉详情跳转
						ENavigate.startRelationHomeActivity(NewConnectionsActivity.this, result.id+"",false,2);
						setResult(ENavConsts.ActivityReqCode.REQUEST_CODE_UPDATE_FRIDENDS);
					} else if (eFromActivityType == 4) {
						/*Intent intent = new Intent(this,
								ContactsDetailsActivity.class);// 进入人脉详情页
						intent.putExtra(EConsts.Key.PERSON_ID, result.id);// 对象id
						intent.putExtra(ENavConsts.EFromActivityType, 4);// 详情入口标记
						startActivity(intent);*/
						// 新 人脉详情跳转
						ENavigate.startRelationHomeActivity(NewConnectionsActivity.this, result.id+"",false,2);
						setResult(ENavConsts.ActivityReqCode.REQUEST_CODE_UPDATE_FRIDENDS);
					} else if (eFromActivityType == 5) {
						/*Intent intent = new Intent(this,
								ContactsDetailsActivity.class);// 进入人脉详情页
						intent.putExtra(EConsts.Key.PERSON_ID, result.id);// 对象id
						intent.putExtra(ENavConsts.EFromActivityType, 5);// 详情入口标记
						startActivity(intent);*/
						// 新 人脉详情跳转
						ENavigate.startRelationHomeActivity(NewConnectionsActivity.this, result.id+"",false,2);
						setResult(ENavConsts.ActivityReqCode.REQUEST_CODE_UPDATE_FRIDENDS);
					} else {
						setResult(Activity.RESULT_OK);
					}
					finish();
					type = Finish;
				} else {
					Toast.makeText(context, "保存失败！", 0).show();
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
		currentRequestEditPosition = position;
		currentRequestState = STATE_EDIT;

		if (parent == people) { // 编辑关联人脉
			currentRequestCode = REQUEST_CODE_RELATED_RESOURCE_PEOPLE;
			ENavigate.startRelatedResourceActivityForResult(this, 101, null,
					ResourceType.People, connectionNodeList.get(position));
		} else if (parent == organization) { // 编辑关联组织
			currentRequestCode = REQUEST_CODE_RELATED_RESOURCE_ORGANIZATION;
			ENavigate.startRelatedResourceActivityForResult(this, 101, null,
					ResourceType.Organization,
					connectionNodeList2.get(position));
		} else if (parent == knowledge) { // 编辑关联知识
			currentRequestCode = REQUEST_CODE_RELATED_RESOURCE_KNOWLEDGE;
			ENavigate.startRelatedResourceActivityForResult(this, 101, null,
					ResourceType.Knowledge, knowledgeNodeList.get(position));
		} else if (parent == requirement) { // 编辑关联事件
			currentRequestCode = REQUEST_CODE_RELATED_RESOURCE_AFFAIR;
			ENavigate.startRelatedResourceActivityForResult(this, 101, null,
					ResourceType.Affair, affairNodeList.get(position));
		}

	}

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
	/**
	 * 图片上传成功回调
	 */
	@Override
	public void onSuccess(String id, Map<String, String> result) {

		final String url = result.get("url"); //
		KeelLog.d("===>>onSuccess", url);

		if (!TextUtils.isEmpty(url)) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					Toast.makeText(context, "上传成功", 0).show();
				}
			});

		}
		avatarUrl = result.get("urlToSql"); //
		KeelLog.d("===>>onSuccess", url);
	}

	@Override
	public void onError(String id, int code, final String message) {
		KeelLog.d("===>>OnError", message);
		KeelLog.d("===>>OnError", message);
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(NewConnectionsActivity.this, message,
						Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	

}
