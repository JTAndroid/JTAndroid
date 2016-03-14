package com.tr.ui.flow;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video.Thumbnails;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.tr.App;
import com.tr.R;
import com.tr.api.HomeReqUtil;
import com.tr.model.demand.ASSOData;
import com.tr.model.demand.ASSORPOK;
import com.tr.model.demand.DemandASSO;
import com.tr.model.demand.DemandASSOData;
import com.tr.model.joint.AffairNode;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.joint.KnowledgeNode;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.knowledge.UserCategory;
import com.tr.model.obj.AffairsMini;
import com.tr.model.obj.Connections;
import com.tr.model.obj.DynamicLocation;
import com.tr.model.obj.DynamicNews;
import com.tr.model.obj.DynamicPeopleRelation;
import com.tr.model.obj.DynamicPicturePath;
import com.tr.model.obj.ForwardDynamicNews;
import com.tr.model.obj.JTContactMini;
import com.tr.model.obj.JTFile;
import com.tr.model.user.OrganizationMini;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.JointResourceFragment.ResourceType;
import com.tr.ui.conference.initiatorhy.InitiatorDataCache;
import com.tr.ui.demand.BrowesPhotoVideo;
import com.tr.ui.demand.SelectPictureActivity;
import com.tr.ui.demand.MyView.DemandHorizontalListView;
import com.tr.ui.demand.util.FileUploader;
import com.tr.ui.demand.util.FileUploader.OnFileUploadListenerEx;
import com.tr.ui.flow.model.DynamicNewsRequest;
import com.tr.ui.flow.model.Topic;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.knowledge.CreateKnowledgeActivity;
import com.tr.ui.knowledge.KnowledgePermissionActivity;
import com.tr.ui.people.model.PersonPermDales;
import com.tr.ui.people.model.PersonPermXiaoles;
import com.tr.ui.people.model.PersonPermZhongles;
import com.tr.ui.widgets.BasicListView2;
import com.tr.ui.work.WorkNewLocationActivity;
import com.utils.common.EConsts;
import com.utils.common.TaskIDMaker;
import com.utils.common.ViewHolder;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.log.ToastUtil;

/**
 * 发布动态
 * 
 * */
public class CreateFlowActivtiy extends JBaseActivity implements
		OnClickListener, OnItemClickListener, IBindData, OnFileUploadListenerEx {

	private ActionBar jabGetActionBar;
	private ImageView photoVideoIb;
	private EditText editflowEt;
	private LinearLayout locationLl;
	private LinearLayout topicLl;
	private LinearLayout relevanceLl;
	private LinearLayout WhoCanLl;
	private LinearLayout remindLl;
	private static TextView locationTv;
	private TextView remindTv;
	// 存放勾选图片/视频的路径集合
	private ArrayList<JTFile> selectedPictureSDAndNet = new ArrayList<JTFile>();// 本地加网络
	private ArrayList<FileUploader> mListPicUpLoader = new ArrayList<FileUploader>();
	private DemandHorizontalListView showBackPicVidHlv;
	private boolean tag;
	private ImageLoader loader;
	private DisplayImageOptions options;
	private PictureAdapter mPictureAdapter;
	public static final int REQUEST_CODE_ADD_ACTIVITY = 1002; // 启动选择相片的回调
	public static final int REQUEST_CODE_BROWERS_ACTIVITY = 1001; // 启动预览图片的回调
	private static String decollatorStr = "、";
	private Bitmap netBitmap;
	ArrayList<JTFile> uploadedPicture = new ArrayList<JTFile>();
	private Handler handlerNetVideo = new Handler() {
		public void handleMessage(Message msg) {
			((ImageView) msg.obj).setImageBitmap(netBitmap);
		};
	};
	private Bitmap extractMiniThumb;
	private Handler handlerVideo = new Handler() {
		public void handleMessage(Message msg) {
			((ImageView) msg.obj).setImageBitmap(extractMiniThumb);
		};
	};
	public static Handler locationHand = new Handler() {
		public void handleMessage(Message msg) {
			location = (DynamicLocation) msg.obj;
			switch (msg.what) {
			case 1:// 搜索
				if (locationTv != null && location != null) {
					locationTv.setText(location.getName());
				}
				break;
			case 2:// 创建
				if (locationTv != null && location != null) {
					locationTv.setText(location.getName());
				}
				break;
			default:
				break;
			}

		};
	};
	private ArrayList<ConnectionNode> connectionNodeList = new ArrayList<ConnectionNode>(); // 四大组件中的人脉数据集合
	private ArrayList<ConnectionNode> connectionNodeList2 = new ArrayList<ConnectionNode>();// 四大组件中的组织数据集合
	private ArrayList<KnowledgeNode> knowledgeNodeList = new ArrayList<KnowledgeNode>();// 四大组件中的知识数据集合
	private ArrayList<AffairNode> affairNodeList = new ArrayList<AffairNode>();// 四大组件中的事务数据集合
	private BasicListView2 people; // 四大组件中的人脉ListView
	private BasicListView2 organization;// 四大组件中的组织ListView
	private BasicListView2 knowledge;// 四大组件中的知识ListView
	private BasicListView2 requirement;// 四大组件中的事务ListView
	private LinearLayout people_Ll; // 四大组件中的人脉LinearLayout
	private LinearLayout organization_Ll;// 四大组件中的组织LinearLayout
	private LinearLayout knowledge_Ll;// 四大组件中的知识LinearLayout
	private LinearLayout requirement_Ll;// 四大组件中的事务LinearLayout
	private ConnectionsGroupAdapter peopleGroupAdapter; // 四大组件中的人脉Adapter
	private ConnectionsGroupAdapter organizationGroupAdapter;// 四大组件中的组织Adapter
	private KnowledgeGroupAdapter knowledgeGroupAdapter;// 四大组件中的知识Adapter
	private RequirementGroupAdapter requirementGroupAdapter;// 四大组件中的事务Adapter
	private ArrayList<DynamicPeopleRelation> relationUserInfos = new ArrayList<DynamicPeopleRelation>();
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
	/** 判断关联是否增加新的条目 */
	public static final int STATE_ADD = 0;
	/** 判断关联是否编辑旧的条目 */
	public static final int STATE_EDIT = 1;
	public int currentRequestState = STATE_ADD;
	public int currentRequestEditPosition = -1;
	private View person_Line;
	private View organization_Line;
	private View knowledge_Line;
	private View requirement_Line;
	private ArrayList<UserCategory> listCategory = new ArrayList<UserCategory>();
	private ArrayList<Long> categoryList = new ArrayList<Long>(); // 话题ID
	private ArrayList<String> names = new ArrayList<String>();
	private String userid;
	private String locationvStr = "";
	private ArrayList<Connections> listHightPermission = new ArrayList<Connections>();
	private ArrayList<Connections> listMiddlePermission = new ArrayList<Connections>();
	private ArrayList<Connections> listLowPermission = new ArrayList<Connections>();
	private boolean noPermission;
	/**
	 * “peopleCount”:关联人脉数量， “orgCount”:关联组织数量, “knowledgeCount”:关联知识数量，
	 * “demandCount”:关联需求数量。
	 */
	private int peopleCount;
	private int orgCount;
	private int knowledgeCount;
	private int demandCount;
	private static DynamicLocation location = new DynamicLocation();
	public int scope = 0;// “scope”：0，所有好友，1，部分好友，2,私密
	private ArrayList<DynamicPicturePath> picturePaths = new ArrayList<DynamicPicturePath>();
	private ArrayList<Topic> topic = new ArrayList<Topic>();
	private ArrayList<Long> receiverIds = new ArrayList<Long>();
	private String mTaskId;
	private ForwardDynamicNews forwardDynamicNews;
	DynamicNews dynamicNews = new DynamicNews();
	private TextView nameTv;
	private TextView contentTv;
	private ImageView ImageIv;
	private RelativeLayout shareLl;
	private int picSuccessSum;
	private View viewFactory;
	private boolean canClick = false;

	@Override
	public void initJabActionBar() {
		jabGetActionBar = jabGetActionBar();
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar, "发动态",
				false, null, true, true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_createflow);
		mTaskId = TaskIDMaker.getTaskId(App.getUserName());// 根据用户名生成TaskId
		userid = getIntent().getStringExtra("Userid");
		initView();
		initData();
	}

	private void initData() {
		forwardDynamicNews = new ForwardDynamicNews();
		Intent intent = getIntent();
		JTFile jtFile = (JTFile) intent.getSerializableExtra("JTFile");
		// forwardDynamicNews.setContent(jtContact.getUserJob());
		// forwardDynamicNews.createrIdApp.getUserID());
		// forwardDynamicNews.imgPath = jtContact.getIconUrl());
		// forwardDynamicNews.setTargetId(jtContact.getUser().getId());
		// forwardDynamicNews.setTitle(jtContact.getName());
		if (jtFile != null) {

			forwardDynamicNews.createrId = App.getUserID();// 创建者id
			forwardDynamicNews.imgPath = jtFile.getmUrl();// 图片
			forwardDynamicNews.targetId = jtFile.mTaskId;// 转发源id

			switch (jtFile.getmType()) {
			case 5:
			case 6: // 人脉
				forwardDynamicNews.title = jtFile.fileName;
				forwardDynamicNews.type = DynamicNews.TYPE_FORWARDING_CONTACTS
						+ "";// 转发类型
				forwardDynamicNews.lowType = "0";// 源类型
				forwardDynamicNews.content = jtFile.reserved2
						+ jtFile.reserved1;// 内容:公司+职位
				HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar,
						"分享人脉", false, null, true, true);

				break;
			case 9: // 组织
			case JTFile.TYPE_ORGANIZATION:
				// imageIv.setBackgroundResource(R.drawable.default_portrait116);
				forwardDynamicNews.title = !TextUtils.isEmpty(jtFile.mFileName) ? jtFile.mFileName
						: jtFile.getmSuffixName();// 标题:姓名
				forwardDynamicNews.type = DynamicNews.TYPE_FORWARDING_ORGANIZATION
						+ "";// 转发类型
				forwardDynamicNews.lowType = jtFile.mModuleType + "";// 源类型
				forwardDynamicNews.content = jtFile.reserved1;// 内容:职位
				HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar,
						"分享组织", false, null, true, true);

				break;
			case JTFile.TYPE_CLIENT: // 客户
				// imageIv.setBackgroundResource(R.drawable.default_portrait116);
				forwardDynamicNews.title = !TextUtils.isEmpty(jtFile.mFileName) ? jtFile.mFileName
						: jtFile.getmSuffixName();// 标题:姓名
				// forwardDynamicNews.type = DynamicNews.TYPE_CUSTOMER_CARD +
				// "";// 转发类型
				forwardDynamicNews.type = DynamicNews.TYPE_FORWARDING_CUSTOM
						+ "";// 转发类型
				forwardDynamicNews.lowType = jtFile.mModuleType + "";// 源类型
				forwardDynamicNews.content = jtFile.reserved1;// 内容:职位
				HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar,
						"分享客户", false, null, true, true);

				break;
			case 10:// 用户
				forwardDynamicNews.title = jtFile.mFileName;// 标题:姓名
				forwardDynamicNews.type = DynamicNews.TYPE_USER_CARD + "";// 转发类型
				forwardDynamicNews.lowType = "0";// 源类型
				forwardDynamicNews.content = jtFile.reserved1;// 内容:职位
				HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar,
						"分享用户", false, null, true, true);

				break;
			case 13:// 知识
				forwardDynamicNews.title = jtFile.reserved2;// 标题:知识title
				forwardDynamicNews.type = DynamicNews.TYPE_FORWARDING_KNOWLEDGE
						+ "";// 转发类型
				forwardDynamicNews.lowType = jtFile.reserved1 + "";// 源类型
				forwardDynamicNews.content = jtFile.mSuffixName;// 内容：知识desc
				HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar,
						"分享知识", false, null, true, true);

				break;
			case 14:// 会议
				forwardDynamicNews.title = jtFile.mSuffixName;// 标题:知识title
				forwardDynamicNews.type = DynamicNews.TYPE_FORWARDING_MEETING
						+ "";// 转发类型
				forwardDynamicNews.lowType = jtFile.mModuleType + "";// 源类型
				forwardDynamicNews.content = jtFile.reserved1;//
				HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar,
						"分享会议", false, null, true, true);

				break;
			case JTFile.TYPE_DEMAND:// 需求转发
				forwardDynamicNews.createrId = jtFile.reserved3;// 转发需求的时候
																// 传入创建者id
				forwardDynamicNews.title = jtFile.mFileName;// 标题:需求title
				forwardDynamicNews.type = DynamicNews.TYPE_FORWARDING_REQUIREMENT
						+ "";// 转发类型
				forwardDynamicNews.lowType = jtFile.reserved2 + "";// 源类型
																	// 融资和投资类型
				forwardDynamicNews.content = jtFile.mSuffixName;// 内容：需求介绍内容
				HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar,
						"分享需求", false, null, true, true);

				break;
			default:
				break;
			}
			shareLl.setVisibility(View.VISIBLE);
			if (TextUtils.isEmpty(forwardDynamicNews.imgPath)) {
				ImageIv.setVisibility(View.GONE);
			} else {
				ImageLoader.getInstance().displayImage(
						forwardDynamicNews.imgPath, ImageIv);
				dynamicNews.setImgPath(forwardDynamicNews.imgPath);
			}
			dynamicNews
					.setLowType(Integer.parseInt(forwardDynamicNews.lowType));
			nameTv.setText(forwardDynamicNews.title);
			dynamicNews.setTitle(forwardDynamicNews.title);
			contentTv.setText(forwardDynamicNews.content);
			dynamicNews.setClearContent(forwardDynamicNews.content);
			HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar,
					"到动态", false, null, true, true);
			photoVideoIb.setVisibility(View.GONE);
		} else {
			shareLl.setVisibility(View.GONE);
			HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar,
					"发动态", false, null, true, true);
			photoVideoIb.setVisibility(View.VISIBLE);
		}

	}

	private void initView() {
		editflowEt = (EditText) findViewById(R.id.editflowEt);
		photoVideoIb = (ImageView) findViewById(R.id.photoVideoIb);
		locationLl = (LinearLayout) findViewById(R.id.locationLl);
		shareLl = (RelativeLayout) findViewById(R.id.shareLl);
		topicLl = (LinearLayout) findViewById(R.id.topicLl);
		relevanceLl = (LinearLayout) findViewById(R.id.relevanceLl);
		WhoCanLl = (LinearLayout) findViewById(R.id.WhoCanLl);
		remindLl = (LinearLayout) findViewById(R.id.remindLl);
		locationTv = (TextView) findViewById(R.id.locationTv);
		remindTv = (TextView) findViewById(R.id.remindTv);
		nameTv = (TextView) findViewById(R.id.nameTv);
		contentTv = (TextView) findViewById(R.id.contentTv);
		ImageIv = (ImageView) findViewById(R.id.ImageIv);
		showBackPicVidHlv = (DemandHorizontalListView) findViewById(R.id.flowshowBackPicVidHlv);
		showBackPicVidHlv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position < selectedPictureSDAndNet.size()) {// 浏览图片
					Intent intent = new Intent(CreateFlowActivtiy.this,
							BrowesPhotoVideo.class);
					intent.putExtra("index", position);
					intent.putExtra(ENavConsts.DEMAND_INTENT_SELECTED_PICTURE,
							selectedPictureSDAndNet);
					intent.putExtra(ENavConsts.DEMAND_BROWER_DELETE, true);
					CreateFlowActivtiy.this.startActivityForResult(intent,
							REQUEST_CODE_BROWERS_ACTIVITY);
				} else {// 选择图片
					boolean b = false;
					for (JTFile imageItem : selectedPictureSDAndNet) {
						if (imageItem.mType == 2) {
							b = true;
							break;
						}
					}
					Intent intent = new Intent(CreateFlowActivtiy.this,
							SelectPictureActivity.class);
					intent.putExtra(ENavConsts.DEMAND_INTENT_SELECTED_PICTURE,
							selectedPictureSDAndNet);
					intent.putExtra("video", b);
					intent.putExtra("isFlow", true);
					// ENavigate.startSelectPictureActivity(CreateFlowActivtiy.this,
					// REQUEST_CODE_ADD_ACTIVITY, selectedPictureSDAndNet, b);
					startActivityForResult(intent, REQUEST_CODE_ADD_ACTIVITY);
				}
			}
		});
		photoVideoIb.setOnClickListener(this);
		locationLl.setOnClickListener(this);
		topicLl.setOnClickListener(this);
		relevanceLl.setOnClickListener(this);
		WhoCanLl.setOnClickListener(this);
		remindLl.setOnClickListener(this);
		loader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
		// 设置图片在加载中显示的图片
				.showImageOnLoading(R.drawable.demand_defaultimg)
				// 设置图片Uri为空或是错误的时候显示的图片
				.showImageForEmptyUri(R.drawable.demand_defaultimg)
				// 设置图片加载/解码过程中错误时候显示的图片
				.showImageOnFail(R.drawable.demand_defaultimg)
				// 设置下载的图片是否缓存在内存中
				.cacheInMemory(true)
				// 设置下载的图片是否缓存在SD卡中
				// .cacheOnDisk(true)
				// 保留Exif信息
				.considerExifParams(false)
				// 设置图片以如何的编码方式显示
				.imageScaleType(ImageScaleType.EXACTLY)
				// 设置图片的解码类型
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		mPictureAdapter = new PictureAdapter();
		showBackPicVidHlv.setAdapter(mPictureAdapter);
		// 关联
		people = (BasicListView2) findViewById(R.id.people_conn);
		organization = (BasicListView2) findViewById(R.id.organization_conn);
		knowledge = (BasicListView2) findViewById(R.id.knowledge_conn);
		requirement = (BasicListView2) findViewById(R.id.requirement_conn);
		people_Ll = (LinearLayout) findViewById(R.id.people_flow_Ll);
		organization_Ll = (LinearLayout) findViewById(R.id.organization_flow_Ll);
		knowledge_Ll = (LinearLayout) findViewById(R.id.knowledge_flow_Ll);
		requirement_Ll = (LinearLayout) findViewById(R.id.requirement_flow_Ll);
		person_Line = findViewById(R.id.person_Line);
		organization_Line = findViewById(R.id.organization_Line);
		knowledge_Line = findViewById(R.id.knowledge_Line);
		requirement_Line = findViewById(R.id.requirement_Line);
		initListViewData();

		editflowEt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(final Editable s) {
				if (TextUtils.isEmpty(s.toString().trim())
						&& viewFactory != null) {
					((TextView) viewFactory).setTextColor(Color.GRAY);
					canClick = false;
				} else if (viewFactory != null) {
					((TextView) viewFactory).setTextColor(Color.BLACK);
					canClick = true;
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.menu_createflow, menu);
		getLayoutInflater().setFactory(new Factory() {
			@Override
			public View onCreateView(String name, Context context,
					AttributeSet attrs) {
				System.out.println(name);
				if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")
						|| name.equalsIgnoreCase("com.android.internal.view.menu.ActionMenuItemView")) {
					try {
						LayoutInflater f = getLayoutInflater();
						viewFactory = f.createView(name, null, attrs);
						System.out.println((viewFactory instanceof TextView));
						if (viewFactory instanceof TextView) {
							((TextView) viewFactory).setTextColor(Color.GRAY);
							return viewFactory;
						}
					} catch (InflateException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
				return null;

			}
		});
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.flow_create:
			if (canClick) {
				showLoadingDialog();
				if(!selectedPictureSDAndNet.isEmpty()){
					for (JTFile imgFile : selectedPictureSDAndNet) {
						if (!new File(imgFile.mFileName).isFile()
								|| !TextUtils.isEmpty(imgFile.mTaskId)) {// 如果是本地的
							continue;
						}
						imgFile.mTaskId = mTaskId;
						imgFile.mModuleType = JTFile.TYPE_DEMAND;
						FileUploader uploader = new FileUploader(imgFile,
								CreateFlowActivtiy.this);
						uploader.WEB_URL = EAPIConsts.FILE_DEMAND_URL;
						mListPicUpLoader.add(uploader);
						uploader.start();
					}
				}else{
					createFlow();
				}
			}
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void createFlow(){
		if (!TextUtils.isEmpty(editflowEt.getText().toString().trim())
				|| !TextUtils.isEmpty(forwardDynamicNews.targetId)
				|| picSuccessSum != 0) {
			DynamicNewsRequest dynamicNewsRequest = new DynamicNewsRequest();
			dynamicNews.setLocation(location);
			dynamicNews.setCreaterId(Long.parseLong(userid));
			dynamicNews.setCreaterName(App.getUser().getmNick());
			dynamicNews.setPicPath(App.getUserAvatar());
			if (forwardDynamicNews != null
					&& !TextUtils
							.isEmpty(forwardDynamicNews.targetId)
					&& !TextUtils
							.isEmpty(forwardDynamicNews.type)) {
				dynamicNews.setType(Integer
						.parseInt(forwardDynamicNews.type));
				dynamicNews
						.setTargetId(Long
								.parseLong(forwardDynamicNews.targetId));
			} else {
				dynamicNews.setType(DynamicNews.TYPE_DYNAMIC);
			}

			dynamicNews.setContent(editflowEt.getText()
					.toString());

			dynamicNews.peopleRelation = (relationUserInfos);
			dynamicNews.setCtime(System.currentTimeMillis()
					+ "");
			// dynamicNews.setPicturePaths(picturePaths);
			dynamicNews.asso = createNewASSO();
			dynamicNews.setPeopleCount(peopleCount);
			dynamicNews.setOrgCount(orgCount);
			dynamicNews.setKnowledgeCount(knowledgeCount);
			dynamicNews.setDemandCount(demandCount);
			dynamicNews.picturePaths = picturePaths;
			 
			dynamicNews.createType = 1;
			// String topicText = topicEt.getText().toString();
			// String[] splitTopic = topicText.split(",");
			// for (int i = 0; i < splitTopic.length; i++) {
			// Topic top = new Topic();
			// }

			dynamicNews.topic = topic;
			dynamicNewsRequest.scope = scope;
			dynamicNewsRequest.receiverIds = receiverIds;
			dynamicNewsRequest.dynamicNews = dynamicNews;
			HomeReqUtil.addFlow(CreateFlowActivtiy.this,
					CreateFlowActivtiy.this,
					dynamicNewsRequest, null);
			InitiatorDataCache.getInstance().inviteAttendSelectedMap
					.clear();
		} else if(TextUtils.isEmpty(editflowEt.getText().toString().trim())) {
			dismissLoadingDialog();
			ToastUtil.showToast(CreateFlowActivtiy.this,
					"请添加动态内容");
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.photoVideoIb:// 选择图片/视频
			boolean b = false;
			for (JTFile imageItem : selectedPictureSDAndNet) {
				if (imageItem.mType == 2) {
					b = true;
					break;
				}
			}
			Intent intent = new Intent(this, SelectPictureActivity.class);
			intent.putExtra(ENavConsts.DEMAND_INTENT_SELECTED_PICTURE,
					selectedPictureSDAndNet);
			intent.putExtra("video", b);
			intent.putExtra("isFlow", true);
			startActivityForResult(intent, REQUEST_CODE_ADD_ACTIVITY);
			// ENavigate.startSelectPictureActivity(this,
			// REQUEST_CODE_ADD_ACTIVITY, selectedPictureSDAndNet, b);
			break;
		case R.id.locationLl:// 所在位置
			intent = new Intent(this, WorkNewLocationActivity.class);
			intent.putExtra("Location", locationvStr);
			startActivityForResult(intent, 111);
			break;
		case R.id.remindLl: // 提醒谁看
			ENavigate.startInviteFriendActivity(this,
					CreateFlowActivtiy.class.getSimpleName(), 2, 100);
			break;
		case R.id.relevanceLl:// 关联
			currentRequestCode = 0;
			ENavigate.startRelatedResourceActivityForResult(this, 101, /*
																		 * surname_Etv
																		 * .
																		 * getText
																		 * () +
																		 * name_Etv
																		 * .
																		 * getText
																		 * ()
																		 */
					null, ResourceType.People, null, null);
			break;
		case R.id.topicLl:// 话题

			break;
		case R.id.WhoCanLl:// 谁可以看
			if (!noPermission && listMiddlePermission != null
					&& listLowPermission != null
					&& listMiddlePermission.isEmpty()
					&& listLowPermission.isEmpty()) {
				setDefaultPermission();
			}
			intent = new Intent(this, KnowledgePermissionActivity.class);
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

	// 设置知识默认权限为大乐
	private void setDefaultPermission() {
		if (listHightPermission.size() > 0) {
			return;
		}
		Connections connections = new Connections();
		connections.type = Connections.type_org;
		connections.setID(String.valueOf(-1));
		connections.setName("全平台");
		connections.setmSourceFrom("全金桐网");
		listHightPermission.add(connections);// 知识默认权限为大乐
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {

			if (requestCode == REQUEST_CODE_BROWERS_ACTIVITY) {// 查看图片
				selectedPictureSDAndNet.clear();
				selectedPictureSDAndNet = (ArrayList<JTFile>) data
						.getSerializableExtra(ENavConsts.DEMAND_INTENT_SELECTED_PICTURE);
				showImage();

			}// 谁可以看
			else if (104 == requestCode) {
				String MSG = "";
				if (resultCode == Activity.RESULT_OK) {
					listHightPermission = new ArrayList<Connections>();
					listMiddlePermission = new ArrayList<Connections>();
					listLowPermission = new ArrayList<Connections>();
					noPermission = data.getBooleanExtra("noPermission", false);
					if (noPermission) {
						scope = 2;
					}
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
								if (!TextUtils.isEmpty(listHightPermission.get(
										i).getId())) {
									dales.id = Long
											.parseLong(listHightPermission.get(
													i).getId());
								}

								dales.name = listHightPermission.get(i)
										.getName();
							}
							scope = 0;
						}
					}
					if (listMiddlePermission != null) {
						if (!listMiddlePermission.isEmpty()) {

							for (int i = 0; i < listMiddlePermission.size(); i++) {
								PersonPermZhongles zhongles = new PersonPermZhongles();
								if (!TextUtils.isEmpty(listMiddlePermission
										.get(i).getId())) {

									zhongles.id = Long
											.parseLong(listMiddlePermission
													.get(i).getId());
									receiverIds.add(Long
											.parseLong(listMiddlePermission
													.get(i).getId()));
								}
								zhongles.name = listMiddlePermission.get(i)
										.getName();
							}
							scope = 1;
						}

					}
					if (listLowPermission != null) {
						if (!listLowPermission.isEmpty()) {
							for (int i = 0; i < listLowPermission.size(); i++) {
								PersonPermXiaoles xiaoles = new PersonPermXiaoles();
								if (!TextUtils.isEmpty(listLowPermission.get(i)
										.getId())) {
									xiaoles.id = Long
											.parseLong(listLowPermission.get(i)
													.getId());
									receiverIds.add(Long
											.parseLong(listLowPermission.get(i)
													.getId()));
								}
								xiaoles.name = listLowPermission.get(i)
										.getName();
							}
							scope = 1;
						}

					}

				}
			} else if (requestCode == REQUEST_CODE_ADD_ACTIVITY) {// 添加图片
				// 获取选中图片的集合
				selectedPictureSDAndNet.clear();
				showLoadingDialog();
				selectedPictureSDAndNet = (ArrayList<JTFile>) data
						.getSerializableExtra(ENavConsts.DEMAND_INTENT_SELECTED_PICTURE);

				showImage();
				dismissLoadingDialog();
			} else if (requestCode == 111 && resultCode == 1000) { // 所在地址
				locationvStr = data.getStringExtra("Location");
				double latitude = data.getDoubleExtra("latitude", 0);
				double longitude = data.getDoubleExtra("longitude", 0);
				String Uid = data.getStringExtra("longitudeId");
				String detailName = data.getStringExtra("detailName");
				String secondLevel = data.getStringExtra("secondLevel");
				if (!locationvStr.equals("不显示位置")) {
					location = new DynamicLocation();
					location.setName(locationvStr);
					location.setSecondLevel(secondLevel);
					location.setDimension(latitude + "#" + longitude);
					location.setDetailName(detailName);
				}
				locationTv.setText(locationvStr);

			} else if (100 == requestCode) { // 提醒谁看
				Iterator<Entry<String, JTContactMini>> attendSelMap = InitiatorDataCache
						.getInstance().inviteAttendSelectedMap.entrySet()
						.iterator();
				names.clear();
				relationUserInfos.clear();
				while (attendSelMap.hasNext()) {
					DynamicPeopleRelation relationUserInfo = new DynamicPeopleRelation();
					Map.Entry entry = (Map.Entry) attendSelMap.next();
					JTContactMini item = (JTContactMini) entry.getValue();
					relationUserInfo.setUserId(Long.parseLong(item.getId()));
					relationUserInfo.setUserName(item.getName());
					relationUserInfos.add(relationUserInfo);
					names.add(item.getName());
				}

				if (InitiatorDataCache.getInstance().forwardingAndSharingOrgMap != null
						&& InitiatorDataCache.getInstance().forwardingAndSharingOrgMap
								.size() >= 1) {
					/* 添加组织业务逻辑 */
					Iterator<Entry<String, OrganizationMini>> orgMap = InitiatorDataCache
							.getInstance().forwardingAndSharingOrgMap
							.entrySet().iterator();
					while (orgMap.hasNext()) {
						Map.Entry entry = (Map.Entry) orgMap.next();
						OrganizationMini item = (OrganizationMini) entry
								.getValue();
						names.add(item.fullName);
					}
				}
				// (mOrganizationMinis != null && mOrganizationMinis.size() >=
				// 1) || (jtContactMinis != null && jtContactMinis.size() >= 1)
				// ||
				if (!names.isEmpty()) {
					String name = names.toString();
					String replace = name.replace("[", "");
					String string = replace.replace("]", "");
					remindTv.setText(string);
				} else {
					remindTv.setText("");
				}
			}
			// 关联资源
			else if (resultCode == Activity.RESULT_OK) {

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
					}
				}

			}
		}

	}
	
	private Handler upLoadHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			// 上传成功
			case 1:
				if (msg.obj != null) {
					String fileUrl = (String) msg.obj;
					DynamicPicturePath picture = new DynamicPicturePath();
					picture.setSourcePath(fileUrl);
					picturePaths.add(picture);
				}
				if(picSuccessSum == selectedPictureSDAndNet.size()){
					createFlow();
				}
				break;
			case 2:
				showToast("图片上传失败,请检查网络连接");
				dismissLoadingDialog();
				for (FileUploader uploader : mListPicUpLoader) {
					uploader.cancel();
				}
				break;
			}
		}
	};

	@Override
	public void onPrepared(long uid) {
		
	}

	@Override
	public void onSuccessForJTFile(JTFile jtFile) {

	}

	@Override
	public void onStarted(long uid) {
		
	}

	@Override
	public void onUpdate(long uid, int progress) {
	}

	@Override
	public void onCanceled(long uid) {

	}

	@Override
	public void onSuccess(long uid, String fileUrl) {
		Log.v(TAG, "上传成功" + fileUrl);
		Message msg = upLoadHandler.obtainMessage();
		picSuccessSum++;
		msg.what = 1; // 代表上传成功
		msg.obj = fileUrl;
		upLoadHandler.sendMessage(msg);
	}

	@Override
	public void onError(long uid, int code, String message, String filePath) {
		Log.e(TAG, message);
		Log.v(TAG, "上传失败" + filePath);
		
		Message msg = upLoadHandler.obtainMessage();
		msg.what = 2; // 代表上传失败
		upLoadHandler.sendMessage(msg);
	}

	private void showImage() {//
		if (selectedPictureSDAndNet != null
				&& selectedPictureSDAndNet.size() > 0) {
			showBackPicVidHlv.setVisibility(View.VISIBLE);
		} else {
			showBackPicVidHlv.setVisibility(View.GONE);
		}

		photoVideoIb.setVisibility(View.GONE);
		if (selectedPictureSDAndNet.size() < 4
				&& selectedPictureSDAndNet.size() != 0) {
			tag = true;
		} else {
			tag = false;
			photoVideoIb.setVisibility(View.VISIBLE);
		}
		if (mPictureAdapter != null) {
			showBackPicVidHlv.setAdapter(mPictureAdapter);
			// mPictureAdapter.notifyDataSetChanged();
		}
	}

	public class PictureAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return tag ? selectedPictureSDAndNet.size() + 1
					: selectedPictureSDAndNet.size();
		}

		@Override
		public JTFile getItem(int position) {
			return selectedPictureSDAndNet.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(CreateFlowActivtiy.this,
						R.layout.demand_item_showback_local, null);// 带有播放按钮的图片view(播放按钮默认隐藏)
			}
			ImageView localPhotoVideoIv = ViewHolder.get(convertView,
					R.id.localPhotoVideoIv);// 显示图片和视频的缩略图

			ImageView localVideoPlayIv = ViewHolder.get(convertView,
					R.id.localVideoPlayIv);// 视频的播放按钮默认隐藏

			LinearLayout localPhotoVideoIv2 = ViewHolder.get(convertView,
					R.id.localPhotoVideoIv2);// 添加图片按钮

			if (position < selectedPictureSDAndNet.size()) {
				JTFile jtFile = selectedPictureSDAndNet.get(position);
				localPhotoVideoIv.setVisibility(View.VISIBLE);
				localPhotoVideoIv2.setVisibility(View.GONE);

				String imgPath = jtFile.mScreenshotFilePath;
				if (TextUtils.isEmpty(imgPath)) {// 缩略图为空
					imgPath = jtFile.mUrl;
				}
				if (TextUtils.isEmpty(imgPath)) {// 网络地址为空
					imgPath = jtFile.mLocalFilePath;
				}
				/*
				 * if(!(new File(imageItem.path).isFile())){ String fullUrl =
				 * DemandReqUtil.getFullUrl(imageItem.path); imageItem.path =
				 * fullUrl; }
				 */

				if (!(jtFile.mType == 2)) {// 如果是图片
					if (!(new File(imgPath).isFile())) {
						// imageItem.path =
						// "http://192.168.101.22:81/web1/0032900961";
						loader.displayImage(imgPath, localPhotoVideoIv, options);
					} else {
						loader.displayImage("file://" + imgPath,
								localPhotoVideoIv, options);
					}

					// 隐藏播放按钮
					localVideoPlayIv.setVisibility(View.GONE);
				} else {
					if (!(new File(imgPath).isFile())) {
						// 异步获取网络视频的第一帧
						videoNetThread(localPhotoVideoIv, imgPath, 150, 150);
					} else {
						// 异步获取本地视频的第一帧
						videoThread(localPhotoVideoIv, imgPath);
					}
					localVideoPlayIv.setVisibility(View.VISIBLE);
				}
			} else {
				localPhotoVideoIv2.setVisibility(View.VISIBLE);
				localPhotoVideoIv.setVisibility(View.GONE);
				localVideoPlayIv.setVisibility(View.GONE);
			}

			return convertView;
		}

		private void videoNetThread(final ImageView localPhotoVideoIv,
				final String imgPath, final int width, final int height) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					netBitmap = null;
					MediaMetadataRetriever retriever = new MediaMetadataRetriever();
					int kind = MediaStore.Video.Thumbnails.MINI_KIND;
					try {
						if (Build.VERSION.SDK_INT >= 14) {
							retriever.setDataSource(imgPath,
									new HashMap<String, String>());
						} else {
							retriever.setDataSource(imgPath);
						}
						netBitmap = retriever.getFrameAtTime();
					} catch (IllegalArgumentException ex) {
						// Assume this is a corrupt video file
					} catch (RuntimeException ex) {
						// Assume this is a corrupt video file.
					} finally {
						try {
							retriever.release();
						} catch (RuntimeException ex) {
							// Ignore failures while cleaning up.
						}
					}
					if (kind == Images.Thumbnails.MICRO_KIND
							&& netBitmap != null) {
						netBitmap = ThumbnailUtils.extractThumbnail(netBitmap,
								width, height,
								ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
					}
					Message msg = handlerNetVideo.obtainMessage();
					msg.obj = localPhotoVideoIv;
					handlerNetVideo.sendMessage(msg);

				}
			}).start();
		}

		private void videoThread(final ImageView image, final String imagePath) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					Bitmap bm = ThumbnailUtils.createVideoThumbnail(imagePath,
							Thumbnails.MINI_KIND);
					extractMiniThumb = ThumbnailUtils.extractThumbnail(bm, 200,
							200);
					Message msg = handlerVideo.obtainMessage();
					msg.obj = image;
					handlerVideo.sendMessage(msg);
				}
			}).start();
		}

		@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
		private Bitmap createVideoThumbnail(String url, int width, int height) {
			Bitmap bitmap = null;
			MediaMetadataRetriever retriever = new MediaMetadataRetriever();
			int kind = MediaStore.Video.Thumbnails.MINI_KIND;
			try {
				if (Build.VERSION.SDK_INT >= 14) {
					retriever.setDataSource(url, new HashMap<String, String>());
				} else {
					retriever.setDataSource(url);
				}
				bitmap = retriever.getFrameAtTime();
			} catch (IllegalArgumentException ex) {
				// Assume this is a corrupt video file
			} catch (RuntimeException ex) {
				// Assume this is a corrupt video file.
			} finally {
				try {
					retriever.release();
				} catch (RuntimeException ex) {
					// Ignore failures while cleaning up.
				}
			}
			if (kind == Images.Thumbnails.MICRO_KIND && bitmap != null) {
				bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
						ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
			}
			return bitmap;
		}
	}

	/**
	 * 将知识返回的关联对象转换为ASSORPOK对象
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
					if (obj.jtContactMini.isOnline()) {
						assoData.type = 3;
					} else {
						assoData.type = 2;
					}

					assoData.picPath = obj.jtContactMini.image;
					conn.add(assoData);
				}

				p.add(new ASSOData(node.getMemo(), conn));
				peopleCount = conn.size();
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
				orgCount = conn.size();
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
				knowledgeCount = conn.size();
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
				demandCount = conn.size();
			}
		}

		asso.value = new ASSORPOK(r, p, o, k);
		return asso.value;

	}

	/**
	 * 将 ASSORPOK对象转换为知识需要的关联对象
	 * 
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
					if (conn2.get(j).type == 2)// type=2人脉
						Pobj.jtContactMini.isOnline = false;
					if (conn2.get(j).type == 3)// type=3好友
						Pobj.jtContactMini.isOnline = true;
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
		peopleGroupAdapter = new ConnectionsGroupAdapter(this,
				connectionNodeList);
		people.setAdapter(peopleGroupAdapter);
		organization.setOnItemClickListener(this);
		organizationGroupAdapter = new ConnectionsGroupAdapter(this,
				connectionNodeList2);
		organization.setAdapter(organizationGroupAdapter);
		knowledge.setOnItemClickListener(this);
		knowledgeGroupAdapter = new KnowledgeGroupAdapter(this,
				knowledgeNodeList);
		knowledge.setAdapter(knowledgeGroupAdapter);
		requirement.setOnItemClickListener(this);
		requirementGroupAdapter = new RequirementGroupAdapter(this,
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

	/**
	 * 人脉，组织适配器
	 * 
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
	protected void onDestroy() {
		location = new DynamicLocation();
		super.onDestroy();
	}

	@Override
	public void bindData(int tag, Object object) {
		if (tag == EAPIConsts.HomeReqType.HOME_REQ_ADD_FLOW) {
			dismissLoadingDialog();
			if (object != null) {
				Long dynamicId = (Long) object;
				if (dynamicId != 0) {

					dynamicNews.setId(dynamicId);
					Intent intent = new Intent();
					if (scope != 2) {
						intent.putExtra("CreateDynamicNews", dynamicNews);
					}
					setResult(999, intent);
					finish();

				} else {
					ToastUtil.showToast(this, "发布动态失败");
				}
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {// 如果是返回
			for (FileUploader item : mListPicUpLoader) {
				item.cancel();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
