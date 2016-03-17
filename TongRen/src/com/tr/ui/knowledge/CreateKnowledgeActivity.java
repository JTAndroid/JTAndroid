package com.tr.ui.knowledge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.R;
import com.tr.api.CommonReqUtil;
import com.tr.api.KnowledgeReqUtil;
import com.tr.image.ImageLoader;
import com.tr.image.LogUtils;
import com.tr.model.demand.LableData;
import com.tr.model.joint.AffairNode;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.joint.KnowledgeNode;
import com.tr.model.knowledge.Knowledge2;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.knowledge.UserCategory;
import com.tr.model.obj.AffairsMini;
import com.tr.model.obj.Connections;
import com.tr.model.obj.JTFile;
import com.tr.model.obj.RequirementMini;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.JointResourceFragment.ResourceType;
import com.tr.ui.demand.CreateLabelActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.knowledge.utils.ActivityCollector;
import com.tr.ui.widgets.BaseEditTextWithPaseteCallBack;
import com.tr.ui.widgets.BaseEditTextWithPaseteCallBack.OnClipboardListener;
import com.tr.ui.widgets.BasicListView2;
import com.tr.ui.widgets.ExpandableHeightGridView;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.http.EAPIConsts.CommonReqType;
import com.utils.http.EAPIConsts.KnoReqType;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;

/**
 * 创建知识
 * @author gintong
 *
 */
public class CreateKnowledgeActivity extends JBaseActivity implements OnItemClickListener, IBindData{
	
	private final static String TAG = "CreateKnowledgeActivity";
	
	public enum OperateType{
		Create,
		Save,
		Update
	}
	
	private OperateType mOperateType = OperateType.Create;
	
	public static final int REQUEST_CODE_KNOWLEDGE_CONTENT_ACTIVITY = 1001;
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
	
	private Knowledge2 knowledge2;
//	private EditText knowledgeTitleEt;
	private BaseEditTextWithPaseteCallBack knowledgeTitleEt;
	//内容
	private LinearLayout knowledgeContentLl;
	private TextView knowledgeContentTv;
	private LinearLayout contentImageLl;
	private ExpandableHeightGridView contentImageGv;
	private ContentImageGvAdapter contentImageGvAdapter;
	
	private LinearLayout knowledgeRelatedLl;
	private LinearLayout relatedDetailsLl;
	
	private boolean isCreate;
	private boolean backtype=false;// false 表示点击创建知识页面actionBar返回键到知识详情页面 true 关闭页面  默认为false
	
	private ArrayList<JTFile> listJtFile;
	private ArrayList<String> contentImageUrlList;
	
//	private LinkedHashMap<String, ArrayList<Connections>> relatedConnectionsGroupLhm;
//	private ArrayList<HashMap<String, ArrayList<Connections>>> relatedConnectionsGroupList;
	
	private ArrayList<Connections> relatedPeopleList;
	private ArrayList<Connections> relatedOrganizationList;
	
	private Context context;
	private String testImagePath = "/storage/emulated/0/DCIM/Camera/IMG_20140815_143630.jpg";
	private static String decollatorStr = "、";
	private ArrayList<HashMap<String, ArrayList<Connections>>> relatedPeopleGroupList;
	private ArrayList<HashMap<String, ArrayList<Connections>>> relatedOrganizationGroupList;
	private ArrayList<HashMap<String, ArrayList<KnowledgeMini2>>> relatedKnowledgeGroupList;
	private ArrayList<HashMap<String, ArrayList<RequirementMini>>> relatedRequirementGroupList;
	private ArrayList<UserCategory> categoryList;
	private String tagListStr;
	
	// 大乐权限人脉对象列表
	private ArrayList<Connections> listHightPermission;
	// 中乐权限人脉对象列表
	private ArrayList<Connections> listMiddlePermission;
	// 小乐权限人脉对象列表
	private ArrayList<Connections> listLowPermission;
	//编辑知识时知识ID
	private long updateID;
	//编辑知识时知识类型
	private int updateType;

	private int requestCode = 0;

	private BasicListView2 people;

	private BasicListView2 organization;

	private BasicListView2 knowledge;

	private BasicListView2 requirement;

	private LinearLayout people_Ll;

	private LinearLayout organization_Ll;

	private LinearLayout knowledge_Ll;

	private LinearLayout requirement_Ll;

	private View knowledgeTreeIC;

	private View knowledgeLabelIC;

	private TextView view_Tree_edit;

	private TextView view_Label_edit;

	private ConnectionsGroupAdapter peopleGroupAdapter;

	private ConnectionsGroupAdapter organizationGroupAdapter;

	private KnowledgeGroupAdapter knowledgeGroupAdapter;

	private RequirementGroupAdapter requirementGroupAdapter;

	private TextView relevance_knowledge;

	private TextView catalogue_knowledge;

	private TextView label_knowledge;

	private TextView jurisdiction_knowledge;

	private View person_Line;

	private View organization_Line;

	private View knowledge_Line;

	private View requirement_Line;
	
	//粘贴知识
	private boolean isPaste = false;
	
	
	private ArrayList<LableData> lableDatas = new ArrayList<LableData>();
	@Override
	public void initJabActionBar() {
		if (isCreate) {
			HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "创建知识", false, null, false, true);
			jabGetActionBar().setDisplayShowHomeEnabled(true);
		} else {
			HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "编辑知识", false, null, false, true);
			jabGetActionBar().setDisplayShowHomeEnabled(true);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_knowledge);
		context = this;
		initVars();
		initComponent();
//		initSimulateData();
		bindComponent();
	}
	
	private void initVars(){
		String MSG = "initVars()";
		
		knowledge2 = new Knowledge2();
//		setDefaultPermission();
		Intent intent = getIntent();
		Knowledge2 knowledge = (Knowledge2) intent.getSerializableExtra(EConsts.Key.KNOWLEDGE2);
		OperateType operateType = (OperateType) intent.getSerializableExtra(EConsts.Key.OPERATE_TYPE);
		isCreate = intent.getBooleanExtra(EConsts.Key.KNOWLEDGE_IS_CREATE, true);
		backtype = intent.getBooleanExtra("backtype", false);
		initJabActionBar();
		// 请求码
		requestCode = getIntent().getIntExtra(EConsts.Key.REQUEST_CODE, 0);
		if(operateType != null){
			mOperateType = operateType;
		}
		
		if( knowledge != null ){
			knowledge2 = knowledge; 
			if(OperateType.Save == mOperateType){
//				setDefaultPermission();
				knowledge2.setId(0);
				knowledge2.setType(1);
				knowledge2.setSaved(true);
				knowledge2.setTaskId("");
			}
			if(OperateType.Update == mOperateType){
				updateID = knowledge.getId();
				updateType = knowledge.getType();
				knowledge2.setId(updateID);
				knowledge2.setType(updateType);
				knowledge2.setSaved(true);
				knowledge2.setTaskId("");
			}
			
		}
		
		// 请求码
		requestCode = getIntent().getIntExtra(EConsts.Key.REQUEST_CODE, 0);
	}
	/*//设置知识默认权限为大乐
	private void setDefaultPermission() {
		Connections connections = new Connections();
		connections.type = Connections.type_org;
		connections.setID(String.valueOf(-1));
		connections.setName("全平台");
		connections.setmSourceFrom("全金桐网");
		ArrayList<Connections> mlistHightPermission =new ArrayList<Connections>();
		mlistHightPermission.add(connections);
		knowledge2.setListHighPermission(mlistHightPermission);//知识默认权限为大乐
	}*/

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem item = menu.add(0, 101, 0, "完成");
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String MSG = "onOptionsItemSelected()";
		Log.i(TAG, knowledge2.toString());
		//发布知识
		if( 101 == item.getItemId() ){
			knowledge2.setTitle(knowledgeTitleEt.getText().toString());
			
			if(StringUtils.isEmpty(knowledge2.getTitle())){
				Toast.makeText(context, "标题不能为空", 0).show();
			}
			else {
				if(knowledge2.getTaskId() == null){
					knowledge2.setTaskId("");
				}
				
				ArrayList<UserCategory> listUserCategory = knowledge2.getListUserCategory();
				if(listUserCategory != null && listUserCategory.size() > 0){
					for (int i = 0; i < listUserCategory.size(); i++) {
						listUserCategory.set(i, listUserCategory.get(i).getLastLevelCategory(listUserCategory.get(i)));
					}
				}
				
				switch (mOperateType) {
				case Create:
				case Save:
					KnowledgeReqUtil.doCreateKnowledge(context, this, knowledge2, null);
					break;
					
				case Update:
					KnowledgeReqUtil.doUpdateKnowledge(context, this, knowledge2, null);
					break;
				}
				this.showLoadingDialog("", false, null);
//				ENavigate.startKnowledgeOfDetailActivity(CreateKnowledgeActivity.this, knowledge2.getId(), knowledge2.getType());
			}
		}else
		{
			if (knowledge2.getId() != 0 && knowledge2.getType() !=0&&!backtype) {
				ENavigate.startKnowledgeOfDetailActivity(CreateKnowledgeActivity.this, knowledge2.getId(), knowledge2.getType());
			}
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	private void initComponent(){
		String MSG = "initComponent()";
		
		//标题
		knowledgeTitleEt = (BaseEditTextWithPaseteCallBack) findViewById(R.id.knowledgeTitleEt);
//		knowledgeTitleEt.addTextChangedListener(new MaxLengthWatcher(context, 200, "字符数长度不能超过200", knowledgeTitleEt));
		knowledgeTitleEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (start == 0 && !StringUtils.isEmpty(s.toString())) {
					if(s.toString().contains("http://") || s.toString().contains("https://")){
						knowledgeTitleEt.setText("");
						CommonReqUtil.doFetchExternalKnowledgeUrl(context, CreateKnowledgeActivity.this, s.toString(), true , null);
						showLoadingDialog("", false, null);
					}
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		knowledgeTitleEt.setOnClipboardListener(new OnClipboardListener() {
			@Override
			public void onPaste(String clipboardContent) {
				String MSG = "onPaste()";
				
				String lastTitleText = knowledgeTitleEt.getText().toString().trim();
				Log.i(TAG, MSG + " lastTitleText = " + lastTitleText);
				Log.i(TAG, MSG + " clipboardContent = " + clipboardContent);
				if(StringUtils.isEmpty(lastTitleText)){
					if(clipboardContent.contains("http://") || clipboardContent.contains("https://")){
						Log.i(TAG, MSG + "  clipboardContent.contains  (http://)  ");
						CommonReqUtil.doFetchExternalKnowledgeUrl(context, CreateKnowledgeActivity.this, clipboardContent, true , null);
						showLoadingDialog("", false, null);
					}
				}
			}
		});
		
		//关联内容
		knowledgeContentLl = (LinearLayout) findViewById(R.id.knowledgeContentLl);
		knowledgeContentLl.setOnClickListener(mOnclickListener);
		knowledgeContentTv = (TextView) findViewById(R.id.knowledgeContentTv);
		if(!TextUtils.isEmpty(knowledge2.getContent())){
			knowledgeContentTv.setText(EUtil.filterHtml(knowledge2.getContent()));
		}
		
		contentImageLl = (LinearLayout) findViewById(R.id.contentImageLl);
		contentImageLl.setOnClickListener(mOnclickListener);
		
		contentImageGv = (ExpandableHeightGridView) findViewById(R.id.contentImageGv);  
		
		contentImageGvAdapter = new ContentImageGvAdapter(context, knowledge2.getListJtFile());
		contentImageGv.setAdapter(contentImageGvAdapter);
		
		
		person_Line = findViewById(R.id.person_Line);
		organization_Line =  findViewById(R.id.organization_Line);
		knowledge_Line = findViewById(R.id.knowledge_Line);
		requirement_Line =  findViewById(R.id.requirement_Line);
		knowledge_asso_Tv = (TextView) findViewById(R.id.knowledge_asso_Tv);
		people = (BasicListView2) findViewById(R.id.people_knowledge_conn);
		organization = (BasicListView2) findViewById(R.id.organization_knowledge_conn);
		knowledge = (BasicListView2) findViewById(R.id.knowledge_knowledge_conn);
		requirement = (BasicListView2) findViewById(R.id.requirement_knowledge_conn);
		people_Ll = (LinearLayout) findViewById(R.id.people_knowledge_Ll);
		organization_Ll = (LinearLayout) findViewById(R.id.organization_knowledge_Ll);
		knowledge_Ll = (LinearLayout) findViewById(R.id.knowledge_knowledge_Ll);
		requirement_Ll = (LinearLayout) findViewById(R.id.requirement_knowledge_Ll);
		peopleGroupAdapter = new ConnectionsGroupAdapter(context, knowledge2.getListRelatedConnectionsNode());
		organizationGroupAdapter = new ConnectionsGroupAdapter(context, knowledge2.getListRelatedOrganizationNode());
		knowledgeGroupAdapter = new KnowledgeGroupAdapter(context, knowledge2.getListRelatedKnowledgeNode());
		requirementGroupAdapter = new RequirementGroupAdapter(context, knowledge2.getListRelatedAffairNode());
		people.setAdapter(peopleGroupAdapter);
		organization.setAdapter(organizationGroupAdapter);
		knowledge.setAdapter(knowledgeGroupAdapter);
		requirement.setAdapter(requirementGroupAdapter);
		// 目录展示
		knowledgeTreeIC = findViewById(R.id.knowledgeTreeIC);
		view_Tree_edit = (TextView) knowledgeTreeIC
				.findViewById(R.id.view_et_edit);
		TextView view_Tree_name = (TextView) knowledgeTreeIC
				.findViewById(R.id.view_tv_name);
		view_Tree_name.setText("目录");
		// 标签展示
		knowledgeLabelIC = findViewById(R.id.knowledgeLabelIC);
		view_Label_edit = (TextView) knowledgeLabelIC
				.findViewById(R.id.view_et_edit);
		TextView view_Label_name = (TextView) knowledgeLabelIC
				.findViewById(R.id.view_tv_name);
		view_Label_name.setText("标签");
		knowledgeTreeIC.setOnClickListener(mOnclickListener);
		knowledgeLabelIC.setOnClickListener(mOnclickListener);
		people.setOnItemClickListener(this);
		organization.setOnItemClickListener(this);
		knowledge.setOnItemClickListener(this);
		requirement.setOnItemClickListener(this);
		relevance_knowledge = (TextView) findViewById(R.id.relevance_knowledge);
		catalogue_knowledge = (TextView) findViewById(R.id.catalogue_knowledge);
		label_knowledge = (TextView) findViewById(R.id.label_knowledge);
		jurisdiction_knowledge = (TextView) findViewById(R.id.jurisdiction_knowledge);
		// 四大组件
				relevance_knowledge.setOnClickListener(mOnclickListener);
				catalogue_knowledge.setOnClickListener(mOnclickListener);
				label_knowledge.setOnClickListener(mOnclickListener);
				jurisdiction_knowledge.setOnClickListener(mOnclickListener);
				getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		
//		knowledgeRelatedLl = (LinearLayout) findViewById(R.id.knowledgeRelatedLl);
//		knowledgeRelatedLl.setOnClickListener(mOnclickListener);
		
//		relatedDetailsLl = (LinearLayout) findViewById(R.id.relatedDetailsLl);
		
//		// 关联人
//		addRelatedPeopleLl = (LinearLayout) findViewById(R.id.addRelatedPeopleLl);
//		addRelatedPeopleLl.setOnClickListener(mOnclickListener);
//		addRelatedPeopleIv = (ImageView) findViewById(R.id.addRelatedPeopleIv);
//		addRelatedPeopleIv.setOnClickListener(mOnclickListener);
//		relatedPeopleLl = (LinearLayout) findViewById(R.id.relatedPeopleLl);
//		// 关联人列表
//		relatedPeopleBlv = (BasicListView2) findViewById(R.id.relatedPeopleBlv);
//		relatedPeopleBlv.setOnItemClickListener(this);
//		relatedPeopleBlv.setAdapter(peopleGroupAdapter);
//		
//		// 关联组织
//		addRelatedOrganizationLl = (LinearLayout) findViewById(R.id.addRelatedOrganizationLl);
//		addRelatedOrganizationLl.setOnClickListener(mOnclickListener);
//		addRelatedOrganizationIv = (ImageView) findViewById(R.id.addRelatedOrganizationIv);
//		addRelatedOrganizationIv.setOnClickListener(mOnclickListener);
//		relatedOrganizationLl = (LinearLayout) findViewById(R.id.relatedOrganizationLl);
//		// 关联组织列表
//		relatedOrganizationBlv = (BasicListView2) findViewById(R.id.relatedOrganizationBlv);
//		relatedOrganizationBlv.setOnItemClickListener(this);
//		relatedOrganizationBlv.setAdapter(organizationGroupAdapter);
//		
//		// 关联知识
//		addRelatedKnowledgeLl = (LinearLayout) findViewById(R.id.addRelatedKnowledgeLl);
//		addRelatedKnowledgeLl.setOnClickListener(mOnclickListener);
//		addRelatedKnowledgeIv = (ImageView) findViewById(R.id.addRelatedKnowledgeIv);
//		addRelatedKnowledgeIv.setOnClickListener(mOnclickListener);
//		relatedKnowledgeLl = (LinearLayout) findViewById(R.id.relatedKnowledgeLl);
//		// 关联知识列表
//		relatedKnowledgeBlv = (BasicListView2) findViewById(R.id.relatedKnowledgeBlv);
//		relatedKnowledgeBlv.setOnItemClickListener(this);
//		relatedKnowledgeBlv.setAdapter(knowledgeGroupAdapter);
//		
//		
//		// 关联需求(事件)
//		addRelatedRequirementLl = (LinearLayout) findViewById(R.id.addRelatedRequirementLl);
//		addRelatedRequirementLl.setOnClickListener(mOnclickListener);
//		addRelatedRequirementIv = (ImageView) findViewById(R.id.addRelatedRequirementIv);
//		addRelatedRequirementIv.setOnClickListener(mOnclickListener);
//		relatedRequirementLl = (LinearLayout) findViewById(R.id.relatedRequirementLl);
//		// 关联需求列表
//		relatedRequirementBlv = (BasicListView2) findViewById(R.id.relatedRequirementBlv);
//		relatedRequirementBlv.setOnItemClickListener(this);
//		relatedRequirementBlv.setAdapter(requirementGroupAdapter);
//		
//		//目录
//		knowledgeCategoryLl = (LinearLayout) findViewById(R.id.knowledgeCategoryLl);
//		knowledgeCategoryLl.setOnClickListener(mOnclickListener);
//		categoryTv = (TextView) findViewById(R.id.categoryTv);
//		
//		knowledgeTagLl = (LinearLayout) findViewById(R.id.knowledgeTagLl);
//		knowledgeTagLl.setOnClickListener(mOnclickListener);
//		tagTv = (TextView) findViewById(R.id.tagTv);
//		
//		knowledgePermissionLl = (LinearLayout) findViewById(R.id.knowledgePermissionLl);
//		knowledgePermissionLl.setOnClickListener(mOnclickListener);
//		noPermissionTv = (TextView) findViewById(R.id.noPermissionTv);
//		permissionLl = (LinearLayout) findViewById(R.id.permissionLl);
//		hightPermissionLl = (LinearLayout) findViewById(R.id.hightPermissionLl);
//		middlePermissionLl = (LinearLayout) findViewById(R.id.middlePermissionLl);
//		lowPermissionLl = (LinearLayout) findViewById(R.id.lowPermissionLl);
//		hightPermissionTv = (TextView) findViewById(R.id.hightPermissionTv);
//		middlePermissionTv = (TextView) findViewById(R.id.middlePermissionTv);
//		lowPermissionTv = (TextView) findViewById(R.id.lowPermissionTv);
		
		
		//初始化标题
		knowledgeTitleEt.setText( knowledge2!=null ? knowledge2.getTitle() : "");
		//从外面传进来的url
		String  externalUrl = (String) getIntent().getStringExtra(EConsts.Key.EXTERNAL_URL);
		if(!StringUtils.isEmpty(externalUrl)){
			Log.i(TAG, MSG + " externalUrl = " + externalUrl );
			CommonReqUtil.doFetchExternalKnowledgeUrl(context, CreateKnowledgeActivity.this, externalUrl,true, null);
			showLoadingDialog("", false, null);
		}
		
		updateAllUI();
	}
	
	private void initSimulateData() {
		
		//内容
		contentImageLl.setVisibility(View.VISIBLE);
		
  	    contentImageGvAdapter.setListJtFile(listJtFile);
		contentImageGvAdapter.notifyDataSetChanged();
		
		//关联人
		relatedPeopleGroupList = new ArrayList<HashMap<String,ArrayList<Connections>>>();
		for (int i = 0; i < 5; i++) {
			HashMap<String, ArrayList<Connections>> hm = new HashMap<String, ArrayList<Connections>>();
			for (int j = 0; j < 1; j++) {
				ArrayList<Connections> connectionsList = new ArrayList<Connections>();
				for (int k = 0; k < 5; k++) {
					Connections connections = new Connections();
					connections.type = Connections.type_persion;
					connections.setName("用户名" + i);

					connectionsList.add(connections);
				}

				hm.put("专家组" + i, connectionsList);
			}

			relatedPeopleGroupList.add(hm);
		}
		
//		peopleGroupAdapter.setConnectionsGroupList(relatedPeopleGroupList);
//		peopleGroupAdapter.notifyDataSetChanged();
		
		//关联组
		relatedOrganizationGroupList = new ArrayList<HashMap<String,ArrayList<Connections>>>();
		for (int i = 0; i < 5; i++) {
			HashMap<String, ArrayList<Connections>> hm = new HashMap<String, ArrayList<Connections>>();
			for (int j = 0; j < 1; j++) {
				ArrayList<Connections> connectionsList = new ArrayList<Connections>();
				for (int k = 0; k < 5; k++) {
					Connections connections = new Connections();
					connections.type = Connections.type_org;
					connections.setName("组织名" + i);
					
					connectionsList.add(connections);
				}
				
				hm.put("组织组" + i, connectionsList);
			}
			
			relatedOrganizationGroupList.add(hm);
		}
		
//		organizationGroupAdapter.setConnectionsGroupList(relatedOrganizationGroupList);
//		organizationGroupAdapter.notifyDataSetChanged();
		
		//关联知识
		relatedKnowledgeGroupList = new ArrayList<HashMap<String,ArrayList<KnowledgeMini2>>>();
		for (int i = 0; i < 5; i++) {
			HashMap<String, ArrayList<KnowledgeMini2>> hm = new HashMap<String, ArrayList<KnowledgeMini2>>();
			for (int j = 0; j < 1; j++) {
				ArrayList<KnowledgeMini2> knowledgeList = new ArrayList<KnowledgeMini2>();
				for (int k = 0; k < 5; k++) {
					KnowledgeMini2 knowledgeMini = new KnowledgeMini2();
					knowledgeMini.title="长长长长长长长长长长长的知识题目";
					knowledgeList.add(knowledgeMini);
				}
				
				hm.put("知识组" + i, knowledgeList);
			}
			
			relatedKnowledgeGroupList.add(hm);
		}
		
//		knowledgeGroupAdapter.setKnowledgeGroupList(relatedKnowledgeGroupList);
//		knowledgeGroupAdapter.notifyDataSetChanged();
		
		
		//关联需求(事件)
		relatedRequirementGroupList = new ArrayList<HashMap<String,ArrayList<RequirementMini>>>();
		for (int i = 0; i < 5; i++) {
			HashMap<String, ArrayList<RequirementMini>> hm = new HashMap<String, ArrayList<RequirementMini>>();
			for (int j = 0; j < 1; j++) {
				ArrayList<RequirementMini> requirementList = new ArrayList<RequirementMini>();
				for (int k = 0; k < 5; k++) {
					RequirementMini requirementMini = new RequirementMini();
					requirementMini.mTitle = "长长长长长长长长长长长的需求题目";
					requirementList.add(requirementMini);
				}
				
				hm.put("事件组" + i, requirementList);
			}
			
			relatedRequirementGroupList.add(hm);
		}
		
//		requirementGroupAdapter.setRequirementGroupList(relatedRequirementGroupList);
//		requirementGroupAdapter.notifyDataSetChanged();
		
		
		//目录
		categoryList = new ArrayList<UserCategory>();
		for (int i = 0; i < 3; i++) {
			UserCategory category = new UserCategory();
			category.setCategoryname( "选中目录名" + i);
			category.setPathName("目录" + i + "/目录" + i + "/目录" + i );
			categoryList.add(category);
		}
		
		StringBuilder categorySb = new StringBuilder(); ;
		
		for (int i = 0; i < categoryList.size(); i++) {
			UserCategory category = categoryList.get(i);
			categorySb.append(category.getPathName());
			categorySb.append("/");
			categorySb.append(category.getCategoryname());
			
			if( i != categoryList.size() -1 ){
				categorySb.append("\n");
			}
			
		}
		
		view_Tree_edit.setText(categorySb.toString());
		
		
		tagListStr = "互联网、投资项目、金融联系、歌手、演员、昨天、今天、明天";
		view_Label_edit.setText(tagListStr);
		
		//权限
		listHightPermission = new ArrayList<Connections>();
		listMiddlePermission = new ArrayList<Connections>();
		listLowPermission = new ArrayList<Connections>();
		
		for (int i = 0; i < 5; i++) {
			Connections connections = new Connections();
			if( i % 2 == 0){
				connections.type = Connections.type_persion;
				connections.setName("人名" + i);
			}
			else {
				connections.type = Connections.type_org;
				connections.setName("组名" + i);
			}
			
			listHightPermission.add(connections);
			listMiddlePermission.add(connections);
			listLowPermission.add(connections);
		}
		
//		hightPermissionTv.setText(listPermission2Str(listHightPermission));
//		middlePermissionTv.setText(listPermission2Str(listMiddlePermission));
//		lowPermissionTv.setText(listPermission2Str(listLowPermission));
		
	}
	
	private OnClipboardListener mOnClipboardListener = new OnClipboardListener() {
		
		@Override
		public void onPaste(String clipboardContent) {
			String MSG = "onPaste()";
			
			String lastTitleText = knowledgeTitleEt.getText().toString().trim();
			Log.i(TAG, MSG + " lastTitleText = " + lastTitleText);
			Log.i(TAG, MSG + " clipboardContent = " + clipboardContent);
			if(StringUtils.isEmpty(lastTitleText)){
				if(clipboardContent.contains("http://") || clipboardContent.contains("https://")){
					Log.i(TAG, MSG + "  clipboardContent.contains  (http://)  ");
					CommonReqUtil.doFetchExternalKnowledgeUrl(context, CreateKnowledgeActivity.this, clipboardContent, true , null);
					showLoadingDialog("", false, null);
				}
			}
			
			
		}
	};
	
	
	public static  String listPermission2Str(ArrayList<Connections> listPermission){
		
		String MSG = "";
		
		if(listPermission == null){
			Log.i(TAG, MSG);
			
			return "";
		}

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < listPermission.size(); i++) {
			Connections connections = listPermission.get(i);
			sb.append(connections.getName());

			if (i != listPermission.size() - 1 ) {
				sb.append(decollatorStr);
			}
/*			if("-1".equals(connections.getId())){
				sb.delete(0, sb.length());
				sb.append("全平台");
				return sb.toString();
			}*/
		}

		return sb.toString();
	}
	
	private void bindComponent() {
		
		
	}
	
	private View.OnClickListener mOnclickListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			try {
			// 在这里启动到知识 内容编辑 页
			if(v == knowledgeContentLl || v == contentImageLl){
				ENavigate.startKnowledgeContentActivityForResult(
						CreateKnowledgeActivity.this, REQUEST_CODE_KNOWLEDGE_CONTENT_ACTIVITY,
						knowledge2.getTaskId(), knowledge2.getContent(), knowledge2.getListJtFile(), knowledge2.getPic());
			}
			//在这里启动到知识 目录 页
			else if ( v == knowledgeTreeIC){
				ENavigate.startKnowledgeCategoryActivityForResult(
						CreateKnowledgeActivity.this,
						REQUEST_CODE_GLOBAL_KNOWLEDGE_CATEGORY_ACTIVITY,
						knowledge2.getListUserCategory(),true,"创建知识");
			}
			//在这里启动到知识 标签 页
			else if ( v == knowledgeLabelIC){
/*				Intent intent = new Intent(CreateKnowledgeActivity.this, GlobalKnowledgeTagActivity.class);
				intent.putExtra(EConsts.Key.FROM_ACTIVITY, CreateKnowledgeActivity.class.getSimpleName());
				intent.putExtra("listTag", knowledge2.getListTag());
				intent.putExtra(EConsts.Key.OPERATE_TYPE, GlobalKnowledgeTagActivity.OperateType.MultipleChoice);
				startActivityForResult(intent, REQUEST_CODE_GLOBAL_KNOWLEDGE_TAG_ACTIVITY);*/
				lableDatas.clear();
				if (knowledge2!=null&&knowledge2.getListTag()!=null&&!knowledge2.getListTag().isEmpty()) {
					for (int i = 0; i < knowledge2.getListTag().size(); i++) {
						LableData data = new LableData();
						data.tag = knowledge2.getListTag().get(i);
						lableDatas.add(data);
					}
				}
				
				ENavigate.startCheckLabelActivity(CreateKnowledgeActivity.this,
						REQUEST_CODE_GLOBAL_KNOWLEDGE_TAG_ACTIVITY,  lableDatas,
						CreateLabelActivity.ModulesType.KnowledgeModules);
				
			}
//			//在这里启动到知识 权限 页
//			else if ( v == knowledgePermissionLl){
//				Intent intent = new Intent(CreateKnowledgeActivity.this, KnowledgePermissionActivity.class);
//				intent.putExtra(EConsts.Key.FROM_ACTIVITY, CreateKnowledgeActivity.class.getSimpleName());
//				intent.putExtra("listHightPermission", knowledge2.getListHighPermission());
//				intent.putExtra("listMiddlePermission", knowledge2.getListMiddlePermission());
//				intent.putExtra("listLowPermission", knowledge2.getListLowPermission());
//				startActivityForResult(intent, REQUEST_CODE_KNOWLEDGE_PERMISSION_ACTIVITY);
//				
//			}
//			
			else if( v == relevance_knowledge || v == catalogue_knowledge || v == jurisdiction_knowledge || v == label_knowledge ){
				
//				String keyword = knowledgeTitleEt.getText().toString().trim();
				
//				if(StringUtils.isEmpty(keyword)){
//					Toast.makeText(context, "标题不能为空", 0).show();
//					return;
//				}
				
				if ( v == relevance_knowledge ){ // 关联 2015/10/20 取消关标题作为关键字传到关联资源页面及与关联/目录/标签/权限的绑定
//					currentRequestCode = REQUEST_CODE_RELATED_RESOURCE_PEOPLE;
					currentRequestState = STATE_ADD;
					ENavigate.startRelatedResourceActivityForResult(
							CreateKnowledgeActivity.this, REQUEST_CODE_RELATED_RESOURCE_ACTIVITY,
							/*keyword*/null,
							ResourceType.People, null);
//					ENavigate.startRelatedResourceActivityForResult(CreateKnowledgeActivity.this, REQUEST_CODE_RELATED_RESOURCE_ACTIVITY, keyword, ResourceType.People, null);
				}
				else if(v == catalogue_knowledge){ // 目录
//					currentRequestCode = REQUEST_CODE_RELATED_RESOURCE_ORGANIZATION;
//					currentRequestState = STATE_ADD;
//					ENavigate.startRelatedResourceActivityForResult(CreateKnowledgeActivity.this, REQUEST_CODE_RELATED_RESOURCE_ACTIVITY, keyword, ResourceType.Organization, null);
					ENavigate.startKnowledgeCategoryActivityForResult(
							CreateKnowledgeActivity.this,
							REQUEST_CODE_GLOBAL_KNOWLEDGE_CATEGORY_ACTIVITY,
							knowledge2.getListUserCategory(),true,"创建知识");
				}
				else if(v == label_knowledge){ // 标签
//					currentRequestCode = REQUEST_CODE_RELATED_RESOURCE_KNOWLEDGE;
//					currentRequestState = STATE_ADD;
//					ENavigate.startRelatedResourceActivityForResult(CreateKnowledgeActivity.this, REQUEST_CODE_RELATED_RESOURCE_ACTIVITY, keyword, ResourceType.Knowledge, null);
				/*	Intent intent = new Intent(CreateKnowledgeActivity.this, GlobalKnowledgeTagActivity.class);
					intent.putExtra(EConsts.Key.FROM_ACTIVITY, CreateKnowledgeActivity.class.getSimpleName());
					intent.putExtra("listTag", knowledge2.getListTag());
					intent.putExtra(EConsts.Key.OPERATE_TYPE, GlobalKnowledgeTagActivity.OperateType.MultipleChoice);
					startActivityForResult(intent, REQUEST_CODE_GLOBAL_KNOWLEDGE_TAG_ACTIVITY);*/
					lableDatas.clear();
					if (knowledge2!=null&&knowledge2.getListTag()!=null&&!knowledge2.getListTag().isEmpty()) {
						for (int i = 0; i < knowledge2.getListTag().size(); i++) {
							LableData data = new LableData();
							data.tag = knowledge2.getListTag().get(i);
							lableDatas.add(data);
						}
					}
					
					
					ENavigate.startCheckLabelActivity(CreateKnowledgeActivity.this,
							REQUEST_CODE_GLOBAL_KNOWLEDGE_TAG_ACTIVITY,  lableDatas,
							CreateLabelActivity.ModulesType.KnowledgeModules);
					
				}
				else if(v == jurisdiction_knowledge){ // 权限
//					currentRequestCode = REQUEST_CODE_RELATED_RESOURCE_AFFAIR;
//					currentRequestState = STATE_ADD;
//					ENavigate.startRelatedResourceActivityForResult(CreateKnowledgeActivity.this, REQUEST_CODE_RELATED_RESOURCE_ACTIVITY, keyword,  ResourceType.Affair, null);
					Intent intent = new Intent(CreateKnowledgeActivity.this, KnowledgePermissionActivity.class);
					intent.putExtra(EConsts.Key.FROM_ACTIVITY, CreateKnowledgeActivity.class.getSimpleName());
					intent.putExtra("listHightPermission", knowledge2.getListHighPermission());
					intent.putExtra("listMiddlePermission", knowledge2.getListMiddlePermission());
					intent.putExtra("listLowPermission", knowledge2.getListLowPermission());
					startActivityForResult(intent, REQUEST_CODE_KNOWLEDGE_PERMISSION_ACTIVITY);
				}
				
			}
			} catch (Exception ex) {
				LogUtils.e(ex.toString());
			}
		}
	};

	public class ContentImageGvAdapter extends BaseAdapter {
		
		private Context context;
		private ArrayList<JTFile> listJtFile;
		
		public ContentImageGvAdapter() {
			super();
		}

		public ContentImageGvAdapter(Context context, ArrayList<JTFile> listJtFile) {
			super();
			this.context = context;
			if( listJtFile != null ){
				this.listJtFile = listJtFile;
			}
			else {
				this.listJtFile = new ArrayList<JTFile>();;
			}
		}

		public ArrayList<JTFile> getListJtFile() {
			return listJtFile;
		}

		public void setListJtFile(ArrayList<JTFile> listJtFile) {
			if(listJtFile != null){
				this.listJtFile = listJtFile;
			}
		}

		@Override
		public int getCount() {
//			return listJtFile.size() >= 4 ? 4 : listJtFile.size() ;
			return listJtFile.size();
		}

		@Override
		public  JTFile getItem(int position) {
			return listJtFile.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			
			if( convertView == null ){
				holder = new ViewHolder();
				
				LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.item_single_image_view, null);
				holder.iv = (ImageView) convertView.findViewById(R.id.iv);
				
				convertView.setTag(holder);
			}
			else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			JTFile jtFile = listJtFile.get(position);
			
//			if( position < listJtFile.size() - 1 ){
//				Bitmap bitmap = KnowledgeContentActivity.getSmallBitmap(jtFile.mLocalFilePath);
//				holder.iv.setImageBitmap(bitmap);
				ImageLoader.load(holder.iv, jtFile.url, R.drawable.frgflow_img_null);
//			}
//			else {
//				holder.iv.setImageResource(R.drawable.kno_add_pic);
//			}
			
			return convertView;
		}
		
		public final class ViewHolder {
			public ImageView iv;
		}
		
	}
	
	public class ConnectionsGroupAdapter extends BaseAdapter {
		
		private Context context;
		private ArrayList<ConnectionNode> listRelatedConnectionsNode; 
		
		public ConnectionsGroupAdapter() {
			super();
		}

		public ConnectionsGroupAdapter(Context context, ArrayList<ConnectionNode> listRelatedConnectionsNode) {
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
		
		public void setListRelatedConnectionsNode(ArrayList<ConnectionNode> listRelatedConnectionsNode) {
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
		public View getView(final int position, View convertView, final ViewGroup parent) {

			ViewHolder viewHolder;

			if (convertView == null) {
				viewHolder = new ViewHolder();

				convertView = LayoutInflater.from(context).inflate(R.layout.list_item_connections_group, null);
				viewHolder.keyTv = (TextView) convertView.findViewById(R.id.keyTv);
				viewHolder.valueTv = (TextView) convertView.findViewById(R.id.valueTv);
				viewHolder.deleteIv = (ImageView) convertView.findViewById(R.id.deleteIv);

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

			ConnectionNode connectionNode = listRelatedConnectionsNode.get(position);
			String key = connectionNode.getMemo();
			viewHolder.keyTv.setText(key + " : ");
			ArrayList<Connections> listConnections = connectionNode.getListConnections();
			StringBuilder valueSb = new StringBuilder();
			for (int i = 0; i < listConnections.size(); i++) {
				if (listConnections.get(i) != null) {
					if (listConnections.get(i).getJtContactMini() !=null) {
						valueSb.append(listConnections.get(i).getJtContactMini().name);
					}
				}
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
		
		public KnowledgeGroupAdapter(Context context, ArrayList<KnowledgeNode> listRelatedKnowledgeNode) {
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
		
		public void setListRelatedKnowledgeNode(ArrayList<KnowledgeNode> listRelatedKnowledgeNode) {
			if( listRelatedKnowledgeNode != null){
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
		public View getView(final int position, View convertView, final ViewGroup parent) {
			
			ViewHolder viewHolder;
			
			if(convertView == null ){
				viewHolder = new ViewHolder();
				
				convertView = LayoutInflater.from(context).inflate(R.layout.list_item_connections_group, null);
				viewHolder.keyTv = (TextView) convertView.findViewById(R.id.keyTv);
				viewHolder.valueTv = (TextView) convertView.findViewById(R.id.valueTv);
				viewHolder.deleteIv = (ImageView) convertView.findViewById(R.id.deleteIv);
				
				convertView.setTag(viewHolder);
			}
			else {
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
			
			KnowledgeNode knowledgeNode = listRelatedKnowledgeNode.get(position);
			String key = knowledgeNode.getMemo();
			viewHolder.keyTv.setText(key + " : ");
			ArrayList<KnowledgeMini2> listKnowledgeMini2 = knowledgeNode.getListKnowledgeMini2();
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
		
		public RequirementGroupAdapter(Context context, ArrayList<AffairNode> listRelatedAffairNode) {
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
		
		public void setListRelatedAffairNode(ArrayList<AffairNode> listRelatedAffairNode) {
			if(listRelatedAffairNode != null){
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
		public View getView(final int position, View convertView, final ViewGroup parent) {
			
			ViewHolder viewHolder;
			
			if(convertView == null ){
				viewHolder = new ViewHolder();
				
				convertView = LayoutInflater.from(context).inflate(R.layout.list_item_connections_group, null);
				viewHolder.keyTv = (TextView) convertView.findViewById(R.id.keyTv);
				viewHolder.valueTv = (TextView) convertView.findViewById(R.id.valueTv);
				viewHolder.deleteIv = (ImageView) convertView.findViewById(R.id.deleteIv);
				
				convertView.setTag(viewHolder);
			}
			else {
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
			ArrayList<AffairsMini> listaAffairsMini = affairNode.getListAffairMini();
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
		
		// 各种activity界面回调方法
		if(REQUEST_CODE_KNOWLEDGE_CONTENT_ACTIVITY == requestCode ){ // 知识内容
			String MSG = "onActivityResult() -- REQUEST_CODE_KNOWLEDGE_CONTENT_ACTIVITY  ";
			
			if(resultCode == Activity.RESULT_OK){
				String content = data.getStringExtra(EConsts.Key.KNOWLEDGE_CONTENT);
				@SuppressWarnings("unchecked")
				ArrayList<JTFile> listJtFile = (ArrayList<JTFile>) data.getSerializableExtra(EConsts.Key.KNOWLEDGE_LIST_JTFILE);
				String coverUrl = data.getStringExtra("coverUrl");
				Log.i(TAG, MSG  + " coverUrl = " + coverUrl);
				
				knowledge2.setPic(coverUrl);
				knowledge2.setContent(content);
				knowledge2.setListJtFile(listJtFile);
				
				updataKnowledgeContentUi();
			}
		}
		else if( REQUEST_CODE_RELATED_RESOURCE_ACTIVITY == requestCode ){ // 关联资源
			if( resultCode == Activity.RESULT_OK ) {
				
				if (data.hasExtra(EConsts.Key.RELATED_PEOPLE_NODE)) {
					// 数据去重
					ConnectionNode connectionNode = (ConnectionNode) data.getSerializableExtra(EConsts.Key.RELATED_PEOPLE_NODE);
					ArrayList<ConnectionNode>  connectionNodeList = knowledge2.getListRelatedConnectionsNode();
					
					if (currentRequestState == STATE_EDIT ) {
						connectionNodeList.set(currentRequestEditPosition, connectionNode);
					} 
					else {
						connectionNodeList.add(connectionNode);
					}
					peopleGroupAdapter.setListRelatedConnectionsNode(knowledge2.getListRelatedConnectionsNode());
					peopleGroupAdapter.notifyDataSetChanged();
					knowledge_asso_Tv.setVisibility(View.VISIBLE);
					if (connectionNodeList.size()==1) {
						person_Line.setVisibility(View.GONE);
					}else{
						person_Line.setVisibility(View.VISIBLE);
					}
					people_Ll.setVisibility(View.VISIBLE);
				}
				
				//相关资源
				if(data.hasExtra(EConsts.Key.RELATED_ORGANIZATION_NODE)){
					// 数据去重
					ConnectionNode connectionNode = (ConnectionNode) data.getSerializableExtra(EConsts.Key.RELATED_ORGANIZATION_NODE);
					ArrayList<ConnectionNode>  connectionNodeList = knowledge2.getListRelatedOrganizationNode();
					
					if (currentRequestState == STATE_EDIT  ) {
						connectionNodeList.set(currentRequestEditPosition, connectionNode);
					}
					else {
						// 加入列表
						connectionNodeList.add(connectionNode	);
					}
					organizationGroupAdapter.setListRelatedConnectionsNode(knowledge2.getListRelatedOrganizationNode());
					organizationGroupAdapter.notifyDataSetChanged();
					knowledge_asso_Tv.setVisibility(View.VISIBLE);
					if (connectionNodeList.size()==1) {
						organization_Line.setVisibility(View.GONE);
					}else{
						organization_Line.setVisibility(View.VISIBLE);
					}
					organization_Ll.setVisibility(View.VISIBLE);
				}
				
				if(data.hasExtra(EConsts.Key.RELATED_KNOWLEDGE_NODE)){
					// 数据去重
					KnowledgeNode knowledgeNode = (KnowledgeNode) data.getSerializableExtra(EConsts.Key.RELATED_KNOWLEDGE_NODE);
					ArrayList<KnowledgeNode> knowledgeNodeList= knowledge2.getListRelatedKnowledgeNode();
					
					if (currentRequestState == STATE_EDIT  ) {
						knowledgeNodeList.set(currentRequestEditPosition, knowledgeNode);
					}
					else {
						knowledgeNodeList.add(knowledgeNode);
					}
					knowledgeGroupAdapter.setListRelatedKnowledgeNode(knowledge2.getListRelatedKnowledgeNode());
					knowledgeGroupAdapter.notifyDataSetChanged();
					knowledge_asso_Tv.setVisibility(View.VISIBLE);
					if (knowledgeNodeList.size()==1) {
						knowledge_Line.setVisibility(View.GONE);
					}else{
						knowledge_Line.setVisibility(View.VISIBLE);
					}
					knowledge_Ll.setVisibility(View.VISIBLE);
					
				}
				
				if(data.hasExtra(EConsts.Key.RELATED_AFFAIR_NODE)){
					// 数据去重\
					
					AffairNode affairNode = (AffairNode) data.getSerializableExtra(EConsts.Key.RELATED_AFFAIR_NODE);
					ArrayList<AffairNode> affairNodeList = knowledge2.getListRelatedAffairNode();
					
					if (currentRequestState == STATE_EDIT ) {
						affairNodeList.set(currentRequestEditPosition, affairNode);
					}
					else {
						affairNodeList.add(affairNode);
					}
					requirementGroupAdapter.setListRelatedAffairNode(knowledge2.getListRelatedAffairNode());
					requirementGroupAdapter.notifyDataSetChanged();
					if (affairNodeList.size()==1) {
						requirement_Line.setVisibility(View.GONE);
					}else{
						requirement_Line.setVisibility(View.VISIBLE);
					}
					knowledge_asso_Tv.setVisibility(View.VISIBLE);
					requirement_Ll.setVisibility(View.VISIBLE);
				}
				
				// 更新相应控件
			}
		}
		
		else if( REQUEST_CODE_GLOBAL_KNOWLEDGE_CATEGORY_ACTIVITY == requestCode ){ // 知识目录
			if(resultCode == Activity.RESULT_OK) {
				ArrayList<UserCategory> listCategory = (ArrayList<UserCategory>) data.getSerializableExtra(EConsts.Key.KNOWLEDGE_CATEGORY_LIST);
				boolean isSelect = data.getBooleanExtra(EConsts.Key.KNOWLEDGE_CATEGORY_GROUP, false);
				if(listCategory != null){
					knowledge2.setListUserCategory(listCategory);
					// 更新相关控件
					if (listCategory.size() == 1 && "未分组".equals(listCategory.get(0).getCategoryname())) {
						if (isSelect) {
							ArrayList<UserCategory> tmpArrayList = new ArrayList<UserCategory>();
							tmpArrayList.add(listCategory.get(listCategory.size()-1));
							updateKnowledgeCategoryUi(tmpArrayList);
							tmpArrayList.clear();
						} else {
							updateKnowledgeCategoryUi(null);
						}
					}else {
						updateKnowledgeCategoryUi(knowledge2.getListUserCategory());
					}
					
				}
			}
		}
		
		// 知识标签
		else if( REQUEST_CODE_GLOBAL_KNOWLEDGE_TAG_ACTIVITY == requestCode ){ 
			String MSG = "";
			
			if(resultCode == Activity.RESULT_OK) {
				ArrayList<LableData> userTaglist = (ArrayList<LableData>) data.getSerializableExtra(ENavConsts.DEMAND_LABEL_DATA);
				ArrayList<String> tagStringList = new ArrayList<String>();
				if (userTaglist != null) {
					for (LableData userTag : userTaglist) {
						tagStringList.add(userTag.tag);
					}
					knowledge2.setListTag(tagStringList);

					updateKnowledgeTagUi();
				}
			}
		}
		
		//权限
		else if( REQUEST_CODE_KNOWLEDGE_PERMISSION_ACTIVITY == requestCode ){ 
			String MSG = "";
			
			if(resultCode == Activity.RESULT_OK) {
				
				boolean noPermission  = data.getBooleanExtra("noPermission", false);
				Log.i(TAG, MSG + " noPermission = " + noPermission );
				ArrayList<Connections> listHightPermission = (ArrayList<Connections>) data.getSerializableExtra("listHightPermission");
				ArrayList<Connections> listMiddlePermission = (ArrayList<Connections>) data.getSerializableExtra("listMiddlePermission");
				ArrayList<Connections> listLowPermission = (ArrayList<Connections>) data.getSerializableExtra("listLowPermission");
				if(listHightPermission == null){
					listHightPermission = new ArrayList<Connections>();
				}
				if(listMiddlePermission == null){
					listMiddlePermission = new ArrayList<Connections>();
				}
				if(listLowPermission == null){
					listLowPermission = new ArrayList<Connections>();
				}
				
				knowledge2.setListHighPermission(listHightPermission);
				knowledge2.setListLowPermission(listLowPermission);
				knowledge2.setListMiddlePermission(listMiddlePermission);
				
				
			}
		}
		
	}

	private void updateKnowledgeTagUi() {
		if(knowledge2.getListTag() != null){
			view_Label_edit.setText(listString2String(knowledge2.getListTag()));
			knowledgeLabelIC.setVisibility(View.VISIBLE);
		}
	}

	private void updateKnowledgeCategoryUi(ArrayList<UserCategory> categoryList) {
//		ArrayList<UserCategory> categoryList = knowledge2.getListUserCategory();
		if (categoryList != null) {
			StringBuilder categorySb = new StringBuilder();

			for (int i = 0; i < categoryList.size(); i++) {
				UserCategory category = categoryList.get(i);
				categorySb.append(category.getAllCategoryname(category));
				if (i != categoryList.size() - 1) {
					categorySb.append("\n");
				}
			}
			knowledgeTreeIC.setVisibility(View.VISIBLE);
			view_Tree_edit.setText(categorySb.toString());
		}else {
			knowledgeTreeIC.setVisibility(View.GONE);
		}
	}

	private void updataKnowledgeContentUi() {
		// 显示到相应的控件
		if(!TextUtils.isEmpty(knowledge2.getContent())){
			knowledgeContentTv.setText(EUtil.filterHtml(knowledge2.getContent()));
		}
		if(knowledge2.getListJtFile() != null){
			contentImageGvAdapter.setListJtFile(knowledge2.getListJtFile());
			contentImageGvAdapter.notifyDataSetChanged();
		}
		if(knowledge2.getListJtFile() != null){
			if(knowledge2.getListJtFile().size() >0){
				contentImageLl.setVisibility(View.VISIBLE);
				knowledge2.setTaskId(knowledge2.getListJtFile().get(0).mTaskId);
			}
			else if (knowledge2.getListJtFile().size() == 0){
				contentImageLl.setVisibility(View.GONE);
				knowledge2.setTaskId("");
			}
		}
	}
	
	
	private String listString2String(ArrayList<String> strList){
		StringBuilder sb = new  StringBuilder() ;
		for (int i = 0; i < strList.size(); i++) {
			sb.append(strList.get(i));
			if( i != strList.size()-1 )
			sb.append("、");
		}
		return sb.toString();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		String keyword = knowledgeTitleEt.getText().toString().trim();
		if(StringUtils.isEmpty(keyword)){
			Toast.makeText(context, "标题不能为空", 0).show();
			return;
		}
		
		currentRequestEditPosition = position;
		currentRequestState = STATE_EDIT;
		
		if(parent == people){ // 编辑人脉
			currentRequestCode = REQUEST_CODE_RELATED_RESOURCE_PEOPLE;
			ENavigate.startRelatedResourceActivityForResult(this, REQUEST_CODE_RELATED_RESOURCE_ACTIVITY, keyword, ResourceType.People, 
					knowledge2.getListRelatedConnectionsNode().get(position));
		}
		else if(parent == organization){ // 编辑组织
			currentRequestCode = REQUEST_CODE_RELATED_RESOURCE_ORGANIZATION;
			ENavigate.startRelatedResourceActivityForResult(this, REQUEST_CODE_RELATED_RESOURCE_ACTIVITY, keyword, ResourceType.Organization, 
					knowledge2.getListRelatedOrganizationNode().get(position));
		}
		else if(parent == knowledge){ // 编辑知识
			currentRequestCode = REQUEST_CODE_RELATED_RESOURCE_KNOWLEDGE;
			ENavigate.startRelatedResourceActivityForResult(this, REQUEST_CODE_RELATED_RESOURCE_ACTIVITY, keyword, ResourceType.Knowledge, 
					knowledge2.getListRelatedKnowledgeNode().get(position));
		}
		else if(parent == requirement){ // 编辑事件
			currentRequestCode = REQUEST_CODE_RELATED_RESOURCE_AFFAIR;
			ENavigate.startRelatedResourceActivityForResult(this, REQUEST_CODE_RELATED_RESOURCE_ACTIVITY, keyword, ResourceType.Affair,
					knowledge2.getListRelatedAffairNode().get(position));
		}
		
	}
	
	public void updateAllUI(){
		if(knowledge2 == null){
			return;
		}
		knowledgeTitleEt.setText(knowledge2.getTitle());
		Selection.setSelection(knowledgeTitleEt.getText(),knowledgeTitleEt.getText().toString().length());
		updataKnowledgeContentUi();
		if (knowledge2.getListRelatedConnectionsNode()!=null&&!knowledge2.getListRelatedConnectionsNode().isEmpty()) {
			people_Ll.setVisibility(View.VISIBLE);
			knowledge_asso_Tv.setVisibility(View.VISIBLE);
			if (knowledge2.getListRelatedConnectionsNode().size()==1) {
				person_Line.setVisibility(View.GONE);
			}else{
				person_Line.setVisibility(View.VISIBLE);
			}
			
		}
		if (knowledge2.getListRelatedOrganizationNode()!=null&&!knowledge2.getListRelatedOrganizationNode().isEmpty()) {
			organization_Ll.setVisibility(View.VISIBLE);
			knowledge_asso_Tv.setVisibility(View.VISIBLE);
			if (knowledge2.getListRelatedOrganizationNode().size()==1) {
				organization_Line.setVisibility(View.GONE);
			}else{
				organization_Line.setVisibility(View.VISIBLE);
			}
		}
		if (knowledge2.getListRelatedKnowledgeNode()!=null&&!knowledge2.getListRelatedKnowledgeNode().isEmpty()) {
			knowledge_Ll.setVisibility(View.VISIBLE);
			knowledge_asso_Tv.setVisibility(View.VISIBLE);
			if (knowledge2.getListRelatedKnowledgeNode().size()==1) {
				knowledge_Line.setVisibility(View.GONE);
			}else{
				knowledge_Line.setVisibility(View.VISIBLE);
			}
		}
		if (knowledge2.getListRelatedAffairNode()!=null&&!knowledge2.getListRelatedAffairNode().isEmpty()) {
			requirement_Ll.setVisibility(View.VISIBLE);
			knowledge_asso_Tv.setVisibility(View.VISIBLE);
			if (knowledge2.getListRelatedAffairNode().size()==1) {
				requirement_Line.setVisibility(View.GONE);
			}else{
				requirement_Line.setVisibility(View.VISIBLE);
			}
		}
		//更新 
		peopleGroupAdapter.setListRelatedConnectionsNode(knowledge2.getListRelatedConnectionsNode());
		peopleGroupAdapter.notifyDataSetChanged();
		organizationGroupAdapter.setListRelatedConnectionsNode(knowledge2.getListRelatedOrganizationNode());
		organizationGroupAdapter.notifyDataSetChanged();
		organizationGroupAdapter.setListRelatedConnectionsNode(knowledge2.getListRelatedOrganizationNode());
		organizationGroupAdapter.notifyDataSetChanged();
		knowledgeGroupAdapter.setListRelatedKnowledgeNode(knowledge2.getListRelatedKnowledgeNode());
		knowledgeGroupAdapter.notifyDataSetChanged();
		requirementGroupAdapter.setListRelatedAffairNode(knowledge2.getListRelatedAffairNode());
		requirementGroupAdapter.notifyDataSetChanged();
		
		updateKnowledgeCategoryUi(knowledge2.getListUserCategory());
		updateKnowledgeTagUi();
		updateKnowledgePermission();
		
	}
	
	boolean noPermission =false;

	private TextView knowledge_asso_Tv;
	
	private void updateKnowledgePermission() {

			ArrayList<Connections> listHightPermission = knowledge2.getListHighPermission();
			ArrayList<Connections> listMiddlePermission = knowledge2.getListMiddlePermission();
			ArrayList<Connections> listLowPermission = knowledge2.getListLowPermission();
			if((listHightPermission == null || listHightPermission.size() <=0 )&&
					(listMiddlePermission == null || listMiddlePermission.size() <=0 ) &&
							(listLowPermission == null || listLowPermission.size() <=0) ){
				noPermission = true;
			}
			if(listHightPermission == null){
				listHightPermission = new ArrayList<Connections>();
			}
			if(listMiddlePermission == null){
				listMiddlePermission = new ArrayList<Connections>();
			}
			if(listLowPermission == null){
				listLowPermission = new ArrayList<Connections>();
			}
			
			knowledge2.setListHighPermission(listHightPermission);
			knowledge2.setListLowPermission(listLowPermission);
			knowledge2.setListMiddlePermission(listMiddlePermission);
			
//			if( noPermission || ( listHightPermission.size() == 0 & listMiddlePermission.size() == 0 & listLowPermission.size() == 0 ) ) {
//				noPermissionTv.setVisibility(View.VISIBLE);
//				permissionLl.setVisibility(View.GONE);
//			}
//			else {
//				if (listHightPermission != null && listHightPermission.size() > 0) {
//					hightPermissionLl.setVisibility(View.VISIBLE);
//					hightPermissionTv.setText(listPermission2Str(knowledge2.getListHighPermission()));
//				}
//				else {
//					hightPermissionLl.setVisibility(View.GONE);
//				}
//				
//				if (listMiddlePermission != null && listMiddlePermission.size() > 0 ) {
//					middlePermissionLl.setVisibility(View.VISIBLE);
//					middlePermissionTv.setText(listPermission2Str(knowledge2.getListMiddlePermission()));
//				}
//				else {
//					middlePermissionLl.setVisibility(View.GONE);
//				}
//				if (listLowPermission != null  && listLowPermission.size() > 0) {
//					lowPermissionLl.setVisibility(View.VISIBLE);
//					lowPermissionTv.setText(listPermission2Str(knowledge2.getListLowPermission()));
//				}
//				else {
//					lowPermissionLl.setVisibility(View.GONE);
//				}
//				
//				noPermissionTv.setVisibility(View.GONE);
//				permissionLl.setVisibility(View.VISIBLE);
//			}
	}
	@Override
	public void bindData(int tag, Object object) {
		// 隐藏加载框
		dismissLoadingDialog();
		
		switch (tag) {
		
		// 修改知识
		case KnoReqType.updateKnowledge:
		{
			if( object == null ) {
				return;
			}
			@SuppressWarnings("unchecked")
			Map<String, Object> dataHm = (Map<String, Object>) object;
			Knowledge2 knowledge = (Knowledge2) dataHm.get("knowledge2");
			if( knowledge != null){
//				if(requestCode == MyKnowledgeActivity.REQUESTCODE_CREATE_KNOWLEDGE_ACTIVITY){
//					ENavigate.startKnowledgeOfDetailActivityForResult(CreateKnowledgeActivity.this, knowledge, "MyKnowledgeActivity", MyKnowledgeActivity.REQUESTCODE_CREATE_KNOWLEDGE_ACTIVITY);
//				}
//				else{
					ENavigate.startKnowledgeOfDetailActivity(CreateKnowledgeActivity.this, knowledge);
					ActivityCollector.finishAll();
					ActivityCollector.activities.clear();
//				}
				finish();
			}
		}
		
		 // 发布知识
		case KnoReqType.CreateKnowledge:{
			if( object == null ) {
				return;
			}
			
				@SuppressWarnings("unchecked")
				Map<String, Object> dataHm = (Map<String, Object>) object;
				Knowledge2 knowledge = (Knowledge2) dataHm.get("knowledge2");
				if( knowledge != null){
//					if(requestCode == MyKnowledgeActivity.REQUESTCODE_CREATE_KNOWLEDGE_ACTIVITY){
//						ENavigate.startKnowledgeOfDetailActivityForResult(CreateKnowledgeActivity.this, knowledge, "MyKnowledgeActivity", MyKnowledgeActivity.REQUESTCODE_CREATE_KNOWLEDGE_ACTIVITY);
//					}
//					else{
						ENavigate.startKnowledgeOfDetailActivity(CreateKnowledgeActivity.this, knowledge);
						ActivityCollector.finishAll();
						ActivityCollector.activities.clear();
//					}
				}
				finish();
		}
			break;
			
		// 解析Url类型的知识
		case  CommonReqType.FetchExternalKnowledgeUrl: {
			String MSG = " case  CommonReqType.FetchExternalKnowledgeUrl ";
			Log.i(TAG, MSG);
			
			if( object == null ) {
				Toast.makeText(context, "解析失败", 0).show();
				Log.i(TAG, MSG + " object == null ");
				return;
			}
			
			Map<String, Object> dataHm = (Map<String, Object>) object;
			Knowledge2 knowledge = (Knowledge2) dataHm.get("knowledge2");
			if( knowledge != null){
				knowledge2 = knowledge;
//				setDefaultPermission();
				knowledge2.setTaskId("");
				updateAllUI();
			}
		}
			break;
			
			
		default:
			break;
		}
	}
}
