/*
 * create by roffee 
 */
package com.tr.ui.conference.initiatorhy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.App;
import com.tr.R;
import com.tr.api.ConnectionsReqUtil;
import com.tr.db.ConnectionsDBManager;
import com.tr.db.DBHelper;
import com.tr.model.conference.MExpFriendContact;
import com.tr.model.conference.MMeetingQuery;
import com.tr.model.obj.Connections;
import com.tr.model.obj.JTContact2;
import com.tr.model.obj.JTContactMini;
import com.tr.model.obj.JTFile;
import com.tr.model.user.JTMember;
import com.tr.model.user.OrganizationMini;
import com.tr.navigate.ENavConsts;
import com.tr.ui.adapter.conference.ExpListviewInviteFriendAdapter;
import com.tr.ui.adapter.conference.ListViewSearchAdapter;
import com.tr.ui.common.ContactFriendListAdapter;
import com.tr.ui.common.view.XListView;
import com.tr.ui.conference.common.BaseActivity;
import com.tr.ui.conference.home.MeetingInviteOtherActivity;
import com.tr.ui.conference.im.MeetingChatActivity;
import com.tr.ui.conference.initiatorhy.search.SearchEditWatcher;
import com.tr.ui.conference.initiatorhy.search.SearchEditWatcher.OnSearchListener;
import com.tr.ui.conference.initiatorhy.search.SearchUtil;
import com.tr.ui.conference.square.MRoadShowCacheFiles;
import com.tr.ui.connections.revision20150122.detail.ForwardToFriendActivity;
import com.tr.ui.flow.CreateFlowActivtiy;
import com.tr.ui.im.IMRelationSelectActivity.SelectAdapter;
import com.tr.ui.widgets.CircleImageView;
import com.tr.ui.widgets.HorizontalListView;
import com.tr.ui.widgets.hy.SlideLetterView;
import com.tr.ui.widgets.hy.SlideLetterView.OnTouchingLetterChangedListener;
import com.utils.common.EConsts;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;
import com.utils.time.Util;

/**
 * 邀请参会人
 * 
 * @author leon
 */
public class InviteFriendActivity extends BaseActivity implements IBindData,
		OnTouchingLetterChangedListener, OnSearchListener {
	public final static int TYPE_EXTERNAL_USE = 0;
	public final static int TYPE_INVITE_SPEAKER_FRIEND = 1;
	public final static int TYPE_INVITE_ATTEND_FRIEND = 2;
	public final static int TYPE_INVITE_OTHER_FRIEND = 3;
	public final static int TYPE_SHARE_TO_MEETING = 4; // 分享到会议
	public final static int TYPE_INVITE_AT_FRIEND = 5; // @好友
	public final static int TYPE_INVITE_AT_FLOW = 6; // 动态
	public final static int requestCode_speakerEdit = 0;

	private int inviteType = 0;
	private LinearLayout friListLayout;
	private ExpandableListView friExpLv;
	private TextView tv_myfriend_char, tv_myorg_char;
	/* 我的组织显示 */
	// private ExpandableListView orgExpLv;
	private XListView orgXListView;
	// private ExpListviewInviteFriendOrgAdapter orgExpLvAdp;
	private ContactFriendListAdapter orgFriendLVAdapter;
	private ExpListviewInviteFriendAdapter friExpLvAdp;
	private SlideLetterView slideLetterView;
	private TextView showSelLetterView;
	private EditText searchEdit;
	private LinearLayout avatarGalleryLayout;
	private HorizontalListView horizontalListView;
	private List<Object> choosedata = new ArrayList<Object>();
	private SearchEditWatcher searchEditWatcher;
	private ListView searchLv;
	private ListViewSearchAdapter searchLvAdp;
	private boolean isSelectedInSearchList;
	// private List<JTContactMini> selectedList = new
	// ArrayList<JTContactMini>();
	public Map<String, JTContactMini> selectedMap = InitiatorDataCache
			.getInstance().costomFriselectedMap;// new
												// HashMap<String,
												// JTContactMini>();

	// add by leon
	private String fromActivity;
	public Map<String, JTContactMini> inviteSpeakerSelectedMapBak = new HashMap<String, JTContactMini>();
//	public Map<String, JTContactMini> inviteSelectedMapBak = new HashMap<String, JTContactMini>();
	public LinkedHashMap<String, JTContactMini> inviteAttendSelectedMap = new LinkedHashMap<String, JTContactMini>();
	// 人脉数据库管理器
	private ConnectionsDBManager connsDBManager;
	private ConnectionsUpdateReceiver connsUpdateReceiver;
	private ImageView allCheck;

	boolean selected = false;
	private FrameLayout allFL;
	private ImageView hyCheckAll;
	private ImageView orgCheckAll;
	boolean showOrgXListViewBool = true;
	boolean showPeopleXListViewBool = true;

	private ArrayList<Connections> mOrgConnections = new ArrayList<Connections>();
//	private ImageView peopleListViewIv;
//	private ImageView orgListViewIvImageView;
	
	/** 是否初始化 自己  （邀请主讲人页面要初始化自己）	*/
	private boolean isInitMyself = false;
	/**其他邀请方式*/
	private FrameLayout otherInviteFl;
	/**是否显示其他邀请方式*/
	private boolean showOtherInvite;
	private MMeetingQuery meetingQuery;
	private TextView rightTextBtn;
	private Map<String, JTContactMini> dataMap;
	private SelectAdapter selectAdapter;

	private int friendSize = 0;
	private int orgSize = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hy_activity_invite_friend);
		// add by leon
		initVars();
		findAndInitViews();
		startGetConnections();
	}

	// add by leon
	// 初始化变量
	private void initVars() {
		fromActivity = getIntent().getStringExtra(ENavConsts.EFromActivityName);
		temporaryVariable();
		Iterator<Entry<String, JTContactMini>> speakerInterator = InitiatorDataCache
				.getInstance().inviteAttendSelectedMap.entrySet().iterator();
		inviteAttendSelectedMap.clear();
		while (speakerInterator.hasNext()) {
			Map.Entry<String, JTContactMini> entry = speakerInterator.next();
			inviteAttendSelectedMap.put(entry.getKey(), entry.getValue());
		}
		// 判断需要加载的数据库
		SharedPreferences sp = getSharedPreferences(
				EConsts.share_firstLoginGetConnections, Activity.MODE_PRIVATE);
		String tableName = sp.getString(EConsts.share_itemUserTableName, "");
		if (TextUtils.isEmpty(tableName)) {
			connsDBManager = new ConnectionsDBManager(this,
					DBHelper.TABLE_APP_CONNECTIONS);
		} else if (tableName.equals(DBHelper.TABLE_APP_CONNECTIONS)) {
			connsDBManager = new ConnectionsDBManager(this,
					DBHelper.TABLE_APP_CONNECTIONS);
		} else if (tableName.equals(DBHelper.TABLE_APP_CONNECTIONS_BACK)) {
			connsDBManager = new ConnectionsDBManager(this,
					DBHelper.TABLE_APP_CONNECTIONS_BACK);
		} else {
			connsDBManager = new ConnectionsDBManager(this,
					DBHelper.TABLE_APP_CONNECTIONS);
		}
		// 广播接收器
		connsUpdateReceiver = new ConnectionsUpdateReceiver();
		int firstLogin = sp.getInt(EConsts.share_itemFirstLogin, 0);
		// 第一次登陆 (firstLogin == 0 为第一次登录)
		if (firstLogin == 0) {
			showLoadingDialog();
		}
		if(InitiatorDataCache.getInstance().forwardingAndSharingOrgMap.size() <= 0){
			mOrgConnections.clear();
			InitiatorDataCache.getInstance().friendOrgCheckAll = false;
		}
	}

	/**
	 * 初始化临时存储
	 */
	private void temporaryVariable() {
		Iterator<Entry<String, JTContactMini>> speakerInterator = InitiatorDataCache
				.getInstance().inviteSpeakerSelectedMap.entrySet().iterator();
		inviteSpeakerSelectedMapBak.clear();
		while (speakerInterator.hasNext()) {
			Map.Entry<String, JTContactMini> entry = speakerInterator.next();
			inviteSpeakerSelectedMapBak.put(entry.getKey(), entry.getValue());
		}
	}

	private void findAndInitViews() {
		Bundle b = getIntent().getExtras();
		if (b != null) {
			inviteType = b.getInt(Util.IK_VALUE);
			showOtherInvite = b.getBoolean("showOtherInvite");
			if (showOtherInvite) {
				meetingQuery = (MMeetingQuery) b.getSerializable(ENavConsts.EMeetingDetail);
			}
		}
//		orgListViewIvImageView = (ImageView) findViewById(R.id.org_listview_Iv);
//		peopleListViewIv = (ImageView) findViewById(R.id.people_listView_Iv);
		otherInviteFl = (FrameLayout) findViewById(R.id.otherInviteFl);
		if (showOtherInvite&& (inviteType==TYPE_INVITE_ATTEND_FRIEND || inviteType == TYPE_INVITE_AT_FRIEND||inviteType==TYPE_INVITE_AT_FLOW)) {
			otherInviteFl.setVisibility(View.VISIBLE);
		}else {
			otherInviteFl.setVisibility(View.GONE);
		}
		otherInviteFl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra(ENavConsts.EMeetingDetail, meetingQuery);
				intent.setClass(InviteFriendActivity.this, MeetingInviteOtherActivity.class);
				startActivity(intent);
			}
		});
		orgXListView = (XListView) findViewById(R.id.hy_actInviteFriend_orgelistview);
//		orgListViewIvImageView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if(!showOrgXListViewBool){
//					orgXListView.setVisibility(View.VISIBLE);
//					orgListViewIvImageView.setImageResource(R.drawable.common_arrow_down);
//				}else {
//					orgXListView.setVisibility(View.GONE);
//					orgListViewIvImageView.setImageResource(R.drawable.common_arrow_up);
//				}
//				showOrgXListViewBool = !showOrgXListViewBool;
//			}
//		});
//		peopleListViewIv.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if(!showPeopleXListViewBool){
//					friExpLv.setVisibility(View.VISIBLE);
//					peopleListViewIv.setImageResource(R.drawable.common_arrow_down);
//				}else {
//					friExpLv.setVisibility(View.GONE);
//					peopleListViewIv.setImageResource(R.drawable.common_arrow_up);
//				}
//				showPeopleXListViewBool = !showPeopleXListViewBool;
//			}
//		});
		
		findAndInitTitleViews();
		/* 显示组织内容 */
		if (inviteType != TYPE_INVITE_SPEAKER_FRIEND && inviteType !=TYPE_INVITE_ATTEND_FRIEND &&inviteType !=TYPE_INVITE_AT_FLOW) {
			findAndInitOrgListViews();
		}
		findAndInitSearchViews();
		findAndInitSlideLetterViews();
		findAndInitFriendListViews();
		updateAvatarGallery();
		if (selected) {
			CircleImageView image = (CircleImageView) new CircleImageView(
					InviteFriendActivity.this);
			image.setImageResource(R.drawable.gintong_smart_brain);
			avatarGalleryLayout.addView(image);
		}
		
		// 更新列表
		// friExpLvAdp.update(InitiatorDataCache.getInstance().friendList);
		// refreshFriExpLv();
		if (inviteType == TYPE_INVITE_SPEAKER_FRIEND) {
			isInitMyself = true;
		}

		checkAll();// 全选功能
	}

	/**
	 * 个人好友和组织好友全选功能
	 */
	private void checkAll() {
		/* 我的好友全选 */
		hyCheckAll = (ImageView) findViewById(R.id.hy_check_all);
		/* 我的组织好友全选 */
		orgCheckAll = (ImageView) findViewById(R.id.org_check_all);

		if (inviteType == TYPE_INVITE_ATTEND_FRIEND||inviteType==TYPE_INVITE_AT_FLOW) {
			hyCheckAll.setVisibility(View.VISIBLE);
			orgCheckAll.setVisibility(View.VISIBLE);
		} else {
			hyCheckAll.setVisibility(View.GONE);
			orgCheckAll.setVisibility(View.GONE);
		}
		/*我的个人好友显示UI*/
		if (InitiatorDataCache.getInstance().friendCheckAll) {
			hyCheckAll.setImageResource(R.drawable.demand_me_need_checkbox_activated);
		} else {
			hyCheckAll.setImageResource(R.drawable.demand_me_need_checkbox_default);
		}
		/*我的组织好友显示UI*/
		if (InitiatorDataCache.getInstance().friendOrgCheckAll) {
			orgCheckAll.setImageResource(R.drawable.demand_me_need_checkbox_activated);
		} else {
			orgCheckAll.setImageResource(R.drawable.demand_me_need_checkbox_default);
		}
		/*我的个人好友全选功能*/
		hyCheckAll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (InitiatorDataCache.getInstance() != null && !InitiatorDataCache.getInstance().friendCheckAll) {// 全选好友
					for (MExpFriendContact mExpFriendContact : InitiatorDataCache
							.getInstance().friendList) {
						for (JTContactMini mJTContactMini : mExpFriendContact.contactList) {
							InitiatorDataCache.getInstance().inviteAttendSelectedMap
									.put(mJTContactMini.id, mJTContactMini);
							choosedata.add(mJTContactMini);
						}
					}
					updateAvatarGallery();
					isSelectedInSearchList = true;
					hyCheckAll.setImageResource(R.drawable.demand_me_need_checkbox_activated);
					friExpLvAdp.notifyDataSetChanged();
				} else {// 取消全选好友
					boolean hasGinTongObj = false;
					/*如果已经包含则添加金桐*/
					if(InitiatorDataCache.getInstance().inviteAttendSelectedMap.containsKey(getGinTongObj().id)){
						hasGinTongObj = true;
					}
					InitiatorDataCache.getInstance().inviteAttendSelectedMap
							.clear();
					if(hasGinTongObj){
						InitiatorDataCache.getInstance().inviteAttendSelectedMap.put(getGinTongObj().id, getGinTongObj());
					}
					
					for (int i = 0, len = choosedata.size(); i < len; ++i) {
						if (choosedata.get(i) instanceof JTContactMini) {
							JTContactMini jtcm = (JTContactMini) choosedata.get(i);
							if(!jtcm.getId().equals("0")){
								choosedata.remove(i);
								--len;// 减少一个
								--i;// 多谢deny_guoshou指正，如果不加会出现评论1楼所说的情况。
							}
						}
					}
					
					updateAvatarGallery();
					isSelectedInSearchList = true;
					friExpLvAdp.notifyDataSetChanged();
					hyCheckAll.setImageResource(R.drawable.demand_me_need_checkbox_default);
				}
				selectAdapter.notifyChange();
				setRightText(choosedata.size());
				InitiatorDataCache.getInstance().friendCheckAll = !InitiatorDataCache
						.getInstance().friendCheckAll;
			}
		});
		
		/*我的组织好友全选功能*/
		orgCheckAll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!InitiatorDataCache.getInstance().friendOrgCheckAll){//全选好友
					for (Connections orgItem : mOrgConnections) {
						InitiatorDataCache.getInstance().forwardingAndSharingOrgMap
						.put(orgItem.organizationMini.id, orgItem.organizationMini);
						choosedata.add(orgItem.organizationMini);
					}
					orgCheckAll.setImageResource(R.drawable.demand_me_need_checkbox_activated);
				}else {
					for (int i = 0, len = choosedata.size(); i < len; ++i) {
						if (choosedata.get(i) instanceof OrganizationMini) {
							choosedata.remove(i);
							--len;// 减少一个
							--i;// 多谢deny_guoshou指正，如果不加会出现评论1楼所说的情况。
						}
					}
					InitiatorDataCache.getInstance().forwardingAndSharingOrgMap.clear();
					orgCheckAll.setImageResource(R.drawable.demand_me_need_checkbox_default);
				}
				updateAvatarGallery();
				selectAdapter.notifyChange();
				setRightText(choosedata.size());
				isSelectedInSearchList = true;
				InitiatorDataCache.getInstance().friendOrgCheckAll = !InitiatorDataCache
						.getInstance().friendOrgCheckAll;
				if(orgFriendLVAdapter!=null){
					orgFriendLVAdapter.notifyDataSetChanged();
				}
			}
		});
	}

	private void findAndInitTitleViews() {
		LinearLayout backBtn = (LinearLayout) findViewById(R.id.hy_layoutTitle_backBtn);
		TextView title = (TextView) findViewById(R.id.hy_layoutTitle_title);
		rightTextBtn = (TextView) findViewById(R.id.hy_layoutTitle_rightTextBtn);
		allFL = (FrameLayout) findViewById(R.id.allFL);
		allCheck = (ImageView) findViewById(R.id.hy_itemInvitefriend_checkbox);
		if(inviteType == TYPE_INVITE_AT_FRIEND){
			allCheck.setVisibility(View.GONE);
		}
		// 从转发到好友页面跳转来
		if (ForwardToFriendActivity.class.getSimpleName().equals(
				getIntent().getStringExtra(ENavConsts.EFromActivityName))) {
			// title.setText("选择好友");
			allFL.setVisibility(View.VISIBLE);
			/* 选择好友则不能选择全平台 */
			/*
			 * selected =
			 * InitiatorDataCache.getInstance().inviteAttendSelectedMap
			 * .size()>=1?false:true; if (selected) {
			 * allCheck.setImageResource(R.drawable.demand_me_need_checkbox_activated); } else {
			 * allCheck.setImageResource(R.drawable.demand_me_need_checkbox_default); }
			 */
			if (InitiatorDataCache.getInstance().inviteAttendSelectedMap
					.containsKey("0")) {
				allCheck.setImageResource(R.drawable.demand_me_need_checkbox_activated);
			} else {
				allCheck.setImageResource(R.drawable.demand_me_need_checkbox_default);
			}
			FrameLayout OrgFriendFl = (FrameLayout) findViewById(R.id.OrgFriendFl);
			OrgFriendFl.setVisibility(View.GONE);
		} else {
			// title.setText("邀请好友");
			allFL.setVisibility(View.GONE);
			orgXListView.setVisibility(View.GONE);
			FrameLayout OrgFriendFl = (FrameLayout) findViewById(R.id.OrgFriendFl);
			OrgFriendFl.setVisibility(View.GONE);
		}
		title.setText("选择");
		if (inviteType > TYPE_INVITE_SPEAKER_FRIEND) {
			rightTextBtn.setText("确定");
		} else {
			rightTextBtn.setText("确定");
		}
		if(inviteType == TYPE_INVITE_AT_FRIEND){
			rightTextBtn.setVisibility(View.GONE);
		}
		backBtn.setOnClickListener(new MyOnClickListener());
		rightTextBtn.setOnClickListener(new MyOnClickListener());
	}

	/**
	 * 显示组织
	 */
	private void findAndInitOrgListViews() {
		mOrgConnections = connsDBManager.queryFriend(0, 10000,
				Connections.type_org);
		if (mOrgConnections == null) {
			mOrgConnections = new ArrayList<Connections>();
		}else {
			for (int i = 0; i < mOrgConnections.size(); i++) {
				OrganizationMini mOrganizationMini = mOrgConnections.get(i).getOrganizationMini();
				if("10040".equals(mOrganizationMini.getId()) && mOrganizationMini.fullName.equals("金桐")){
					mOrgConnections.remove(mOrgConnections.get(i));
				}
			}
		}
		orgSize = mOrgConnections.size();

		orgFriendLVAdapter = new ContactFriendListAdapter(this, mOrgConnections, inviteType);
		orgXListView.setAdapter(orgFriendLVAdapter);
		orgXListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(inviteType != TYPE_INVITE_AT_FRIEND){
					ImageView contactListsOorgGroupCheckBoxIV = (ImageView) view
							.findViewById(R.id.contactLists_orgGroup_checkBox_IV);
					int arrIndex = position - 1;
					if (mOrgConnections.size() < position || arrIndex < 0) {
						return ;
					}
					OrganizationMini mOrganizationMini = mOrgConnections
							.get(arrIndex).organizationMini;
					String idString = mOrganizationMini.mID;
					if (InitiatorDataCache.getInstance().forwardingAndSharingOrgMap
							.containsKey(idString)) {
						InitiatorDataCache.getInstance().forwardingAndSharingOrgMap
								.remove(idString);
						contactListsOorgGroupCheckBoxIV
								.setImageResource(R.drawable.demand_me_need_checkbox_default);
						choosedata.remove(mOrganizationMini);
						selectAdapter.notifyChange();
					} else {
						InitiatorDataCache.getInstance().forwardingAndSharingOrgMap
								.put(idString, mOrganizationMini);
						contactListsOorgGroupCheckBoxIV
								.setImageResource(R.drawable.demand_me_need_checkbox_activated);
						choosedata.add(mOrganizationMini);
						selectAdapter.notifyChange();
					}
					// 更新
					updateAvatarGallery();
					isSelectedInSearchList = true;
					if(InitiatorDataCache.getInstance().forwardingAndSharingOrgMap.size() <
							mOrgConnections.size()){
						orgCheckAll.setImageResource(R.drawable.demand_me_need_checkbox_default);
						InitiatorDataCache.getInstance().friendOrgCheckAll = false;
					}else {
						orgCheckAll.setImageResource(R.drawable.demand_me_need_checkbox_activated);
						InitiatorDataCache.getInstance().friendOrgCheckAll = true;
					}
				}else{
					Intent data = new Intent();
					data.putExtra("at_name", mOrgConnections
							.get(position-1).organizationMini.getFullName());
					setResult(200, data);
					finishActivity();
				}
			}
		});
	}

	private void findAndInitFriendListViews() {
		tv_myfriend_char = (TextView) findViewById(R.id.tv_myfriend_char);
		tv_myorg_char = (TextView) findViewById(R.id.tv_myorg_char);
		friListLayout = (LinearLayout) findViewById(R.id.hy_actInviteFriend_FriListLayout);
		friExpLv = (ExpandableListView) findViewById(R.id.hy_actInviteFriend_friExpandlistview);
		friExpLvAdp = new ExpListviewInviteFriendAdapter(this, inviteType,
				InitiatorDataCache.getInstance().friendList);
		friExpLv.setAdapter(friExpLvAdp);
		friExpLv.setVerticalScrollBarEnabled(true);
		friExpLv.setGroupIndicator(null);
		friExpLv.setOnChildClickListener(new MyExpFriLvOnChildClickListener());
		forbitGroupClicked();
		friExpLv.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				slideLetterView
						.setPaintColor(selectTopListViewitems(firstVisibleItem));
			}
		});
		allFL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(inviteType == TYPE_INVITE_AT_FRIEND){
					Intent data = new Intent();
					data.putExtra("at_name", "金桐脑");
					setResult(200, data);
					finishActivity();
				}else{
					if (selected) {// 选中--取消
						allCheck.setImageResource(R.drawable.demand_me_need_checkbox_default);
//						avatarGalleryLayout.removeAllViews();
						selected = false;
						InitiatorDataCache.getInstance().inviteAttendSelectedMap
								.remove(getGinTongObj().id);
						for(Object obj:choosedata){
							if(obj instanceof JTContactMini){
								if(((JTContactMini) obj).getId().equals("0")){
									choosedata.remove(obj);
									break;
								}
							}
						}
					} else {
						// InitiatorDataCache.getInstance().inviteAttendSelectedMap.clear();
						friExpLvAdp.notifyDataSetChanged();
						// avatarGalleryLayout.removeAllViews();
						/*
						 * ImageView image = (ImageView) new
						 * ImageView(InviteFriendActivity.this);
						 * image.setImageResource(R.drawable.all_platforms);
						 * avatarGalleryLayout.addView(image);
						 */
						allCheck.setImageResource(R.drawable.demand_me_need_checkbox_activated);
						selected = true;
						// 添加金桐脑到集合
						InitiatorDataCache.getInstance().inviteAttendSelectedMap
								.put(getGinTongObj().id, getGinTongObj());
						choosedata.add(getGinTongObj());
					}
					selectAdapter.notifyChange();
					setRightText(choosedata.size());
					updateAvatarGallery();
					isSelectedInSearchList = true;
				}
			}
		});
	}

	/**
	 * 获取金桐脑对象
	 */
	private JTContactMini getGinTongObj() {
		JTContactMini jtContactMini = new JTContactMini();
		jtContactMini.setId("0");
		jtContactMini.setName("金桐脑");
		return jtContactMini;
	}

	private void findAndInitSlideLetterViews() {
		slideLetterView = (SlideLetterView) findViewById(R.id.hy_actInviteFriend_slideLetterView);
		showSelLetterView = (TextView) findViewById(R.id.hy_actInviteFriend_showSelLetter);
		slideLetterView.setOnTouchingLetterChangedListener(this);
		showSelLetterView.setVisibility(View.INVISIBLE);
	}

	private void findAndInitSearchViews() {
		searchEdit = (EditText) findViewById(R.id.hy_layoutSearch_edit);
//		avatarGalleryLayout = (LinearLayout) findViewById(R.id.hy_layoutSearch_galleryAvatarLayout);
		horizontalListView = (HorizontalListView) findViewById(R.id.choosedata);
		selectAdapter = new SelectAdapter(this);
		horizontalListView.setAdapter(selectAdapter);
//		horizontalListView.setOnItemClickListener(new OnItemClickListener() {
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//				choosedata.remove(arg1.getTag());
//				selectAdapter.notifyChange();
//			}
//		});
		
		searchLv = (ListView) findViewById(R.id.hy_actInviteFriend_searchListLayout);
		if (inviteType == TYPE_INVITE_ATTEND_FRIEND) {
			searchEditWatcher = new SearchEditWatcher(this,
					SearchEditWatcher.SEARCH_INVITE_ATTEND_FRIEND);
			searchLvAdp = new ListViewSearchAdapter(this,
					ListViewSearchAdapter.TYPE_INVITE_ATTEND_FRIEND);
		}else if (inviteType == TYPE_INVITE_AT_FLOW){
			searchEditWatcher = new SearchEditWatcher(this,
					SearchEditWatcher.SEARCH_SHARE_FLOW);
			searchLvAdp = new ListViewSearchAdapter(this,
					ListViewSearchAdapter.TYPE_INVITE_AT_FLOW);
		}else if(inviteType == TYPE_INVITE_AT_FRIEND){
			searchEditWatcher = new SearchEditWatcher(this,
					SearchEditWatcher.SEARCH_INVITE_ATTEND_FRIEND);
			searchLvAdp = new ListViewSearchAdapter(this,
					ListViewSearchAdapter.TYPE_INVITE_AT_FRIEND);
		} else {
			searchEditWatcher = new SearchEditWatcher(this,
					SearchEditWatcher.SEARCH_INVITE_SPEAKER_FRIEND);
			searchLvAdp = new ListViewSearchAdapter(this,
					ListViewSearchAdapter.TYPE_INVITE_SPEAKER_FRIEND);
		}
		searchEditWatcher.setOnSearchListener(this);
		searchEdit.addTextChangedListener(searchEditWatcher);
		searchLv.setAdapter(searchLvAdp);
		searchLv.setOnItemClickListener(new MyOnItemClickListener());
	}

	private void updateAvatarGallery() {
//		avatarGalleryLayout.removeAllViews();
////		Map<String, JTContactMini> dataMap;
//		if (inviteType == TYPE_INVITE_ATTEND_FRIEND || inviteType == TYPE_INVITE_AT_FRIEND) {
//			dataMap = InitiatorDataCache.getInstance().inviteAttendSelectedMap;
//		} else if (inviteType == TYPE_INVITE_SPEAKER_FRIEND) {
//			dataMap = InitiatorDataCache.getInstance().inviteSpeakerSelectedMap;
//		} else {
//			dataMap = selectedMap;
//		}
//
//		if (Util.isNull(dataMap)) {
//			return;
//		}
//		Iterator<Entry<String, JTContactMini>> iterIAM = dataMap.entrySet()
//				.iterator();
//		while (iterIAM.hasNext()) {
//			Map.Entry entry = (Map.Entry) iterIAM.next();
//			JTContactMini item = (JTContactMini) entry.getValue();
//			if ("0".equals(item.id)) {
//				CircleImageView image = (CircleImageView) new CircleImageView(
//						InviteFriendActivity.this);
//				image.setImageResource(R.drawable.gintong_smart_brain);
//				avatarGalleryLayout.addView(image);
//			} else {
//				addAvatar(item.image);
//			}
//		}
//		/* 添加组织 */
//		if (InitiatorDataCache.getInstance().forwardingAndSharingOrgMap != null) {
//			Iterator<Entry<String, OrganizationMini>> orgIAM = InitiatorDataCache
//					.getInstance().forwardingAndSharingOrgMap.entrySet()
//					.iterator();
//			while (orgIAM.hasNext()) {
//				Map.Entry entry = (Map.Entry) orgIAM.next();
//				OrganizationMini item = (OrganizationMini) entry.getValue();
//				addAvatar(item.logo);
//			}
//		}
	}

	private void addAvatar(String image) {
		View itemV = LayoutInflater.from(this).inflate(
				R.layout.hy_item_search_gallery_avatar, null);
		ImageView avatar = (ImageView) itemV
				.findViewById(R.id.hy_itemSearchGallery_avatar);
		avatar.setImageResource(R.drawable.hy_ic_default_friend_avatar);
		if (!Util.isNull(image) && ImageLoader.getInstance() != null) {
			ImageLoader.getInstance().displayImage(image, avatar);
		}
		avatarGalleryLayout.addView(itemV);
	}

	private void expFriLvUnfold() {
		for (int i = 0; i < friExpLvAdp.getGroupCount(); i++) {
			friExpLv.expandGroup(i);
		}
		;
	}

	private void forbitGroupClicked() {
		friExpLv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				if (true) {

				}
				return true;
			}
		});
	}

	private void startGetConnections() {

		/*
		 * JSONObject jb = new JSONObject(); try { jb.put("type", "1"); } catch
		 * (JSONException e) { e.printStackTrace(); } showLoadingDialog();
		 * ConnectionsReqUtil.doGetConnectionsList(this, this, jb, null);
		 */

		// 执行后台任务
		// showLoadingDialog();
		new GetConnectionsTask().execute();
	}

	/***
	 * 判断滚动到顶部条目内容所对应的字母
	 * 
	 * @param index
	 *            条目索引
	 * @return
	 */
	private String selectTopListViewitems(int index) {
		int count = 0;
		String str = "";
		List<MExpFriendContact> datalist = friExpLvAdp.getExpFriendContact();
		if (Util.isNull(datalist)) {
			return str;
		}
		for (int i = 0; i < datalist.size(); i++) {
			if (index == count + i) {
				str = datalist.get(i).nameCh;
				return str;
			} else {
				count = count + datalist.get(i).contactList.size();
				if (count + i >= index) {
					str = datalist.get(i).nameCh;
					return str;
				}
			}
		}
		return str;
	}

	public void finishActivity() {
		this.finish();
	}

	public void refreshFriExpLv() {
		friExpLvAdp.notifyDataSetChanged();
		expFriLvUnfold();
	}

	private class MyOnClickListener implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			try {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				boolean isOpen = imm.isActive();
				if (isOpen) {
					imm.hideSoftInputFromWindow(InviteFriendActivity.this
							.getCurrentFocus().getWindowToken(), 0);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			int clickId = v.getId();
			switch (clickId) {
			case R.id.hy_layoutTitle_backBtn: {
				InitiatorDataCache.getInstance().inviteSpeakerSelectedMap = inviteSpeakerSelectedMapBak;
				MRoadShowCacheFiles.getInstance().releaseAll();
				InitiatorDataCache.getInstance().inviteAttendSelectedMap.clear();
				InitiatorDataCache.getInstance().inviteAttendSelectedMap = inviteAttendSelectedMap;
				finishActivity();
			}
				break;
			case R.id.hy_layoutTitle_rightTextBtn: {
				if (inviteType == TYPE_INVITE_ATTEND_FRIEND||inviteType==TYPE_INVITE_AT_FLOW) {
					if (!TextUtils.isEmpty(fromActivity)
							&& fromActivity.equals(CreateFlowActivtiy.class
									.getSimpleName())) {
						if (InitiatorDataCache.getInstance().inviteAttendSelectedMap
							.size()>10) {
							Toast.makeText(InviteFriendActivity.this, "提醒的人数不得超过10人", 0).show();
							return;
						}
					}
					if (InitiatorDataCache.getInstance().inviteAttendSelectedMap
							.size() < 1) {
						InitiatorDataCache.getInstance().inviteAttendSelectedMap
								.clear();
						/*
						 * Toast.makeText(InviteFriendActivity.this, "没有选择好友",
						 * Toast.LENGTH_SHORT).show();
						 */
					}/* else{ */
					Bundle b = new Bundle();
					b.putBoolean(Util.IK_VALUE, true);
					Util.activitySetResult(InviteFriendActivity.this,
							InitiatorHYActivity.class, b);
					finishActivity();
					/* } */
				}
				/** 邀请主讲人 */
				else if (inviteType == TYPE_INVITE_SPEAKER_FRIEND) {
					if (InitiatorDataCache.getInstance().inviteSpeakerSelectedMap
							.size() < 1) {
						Toast.makeText(InviteFriendActivity.this, "没有选择好友",
								Toast.LENGTH_SHORT).show();
					} else {
						Util.forwardTargetActivityForResult(
								InviteFriendActivity.this,
								MeetingSpeakerListActivity.class,
//								InviteSpeakerEditActivity.class,
								requestCode_speakerEdit);
					}
				} else if (inviteType == TYPE_INVITE_OTHER_FRIEND) {
					if (Util.isNull(selectedMap)) {
						Toast.makeText(InviteFriendActivity.this, "没有选择好友",
								Toast.LENGTH_SHORT).show();
					} else {
						List<JTContactMini> aList = new ArrayList();
						Iterator<Entry<String, JTContactMini>> iterIAM = selectedMap
								.entrySet().iterator();
						while (iterIAM.hasNext()) {
							Entry entry = iterIAM.next();
							if (null != entry) {
								aList.add((JTContactMini) entry.getValue());
							}
						}

						Intent intent = new Intent();
						Bundle b = new Bundle();
						b.putSerializable("value", (Serializable) aList);
						intent.putExtras(b);
						setResult(Activity.RESULT_OK, intent);
						finish();
					}
				} else if (inviteType == TYPE_SHARE_TO_MEETING) { // add by
																	// leon,分享到畅聊
					if (Util.isNull(selectedMap)) {
						Toast.makeText(InviteFriendActivity.this, "没有选择好友",
								Toast.LENGTH_SHORT).show();
					} else {
						// 请求用户或人脉的详情
						showLoadingDialog();
						JTContactMini con = null;// = selectedList.get(0);
						Iterator<Entry<String, JTContactMini>> iterIAM = selectedMap
								.entrySet().iterator();
						while (iterIAM.hasNext()) {
							Map.Entry entry = (Map.Entry) iterIAM.next();
							con = (JTContactMini) entry.getValue();
							break;
						}
						requestConnectionDetail(con.id, con.isOnline,
								Connections.type_persion,
								InviteFriendActivity.this);
					}
				}
			}
				break;
			}
		}
	}

	// add by leon
	// 获取人脉或用户详情，以便获取分享所需要的信息
	public void requestConnectionDetail(String id, boolean isonline, int type,
			Activity a) {
		if (type == Connections.type_org) { // 机构
			showLoadingDialog();
			JSONObject jb = ConnectionsReqUtil.getOrganizationDetailJson(id,
					isonline);
			ConnectionsReqUtil.dogetOrganizationDetail(a, this, jb, null);
		} else { // 个人
			showLoadingDialog();
			JSONObject jb = ConnectionsReqUtil.getContactDetailJson(id,
					isonline);
			ConnectionsReqUtil.doContactDetail(a, this, jb, null);
		}
	}

	private class MyExpFriLvOnChildClickListener implements
			OnChildClickListener {
		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			List<MExpFriendContact> datalist = friExpLvAdp
					.getExpFriendContact();
			JTContactMini item = datalist.get(groupPosition).contactList
					.get(childPosition);

			ImageView cb = (ImageView) v
					.findViewById(R.id.hy_itemInvitefriend_checkbox);
			if (inviteType == TYPE_INVITE_ATTEND_FRIEND||inviteType==TYPE_INVITE_AT_FLOW) {
				if (InitiatorDataCache.getInstance().inviteAttendSelectedMap
						.containsKey(item.id)) {
					cb.setImageResource(R.drawable.demand_me_need_checkbox_default);
					InitiatorDataCache.getInstance().inviteAttendSelectedMap
							.remove(item.id);
					choosedata.remove(item);
					selectAdapter.notifyChange();
				} else {
					if (!TextUtils.isEmpty(fromActivity)
							&& fromActivity.equals(CreateFlowActivtiy.class
									.getSimpleName())) {
						if (InitiatorDataCache.getInstance().inviteAttendSelectedMap
							.size()>=10) {
							Toast.makeText(InviteFriendActivity.this, "提醒的人数不得超过10人", 0).show();
							return true;
						}
					}
					cb.setImageResource(R.drawable.demand_me_need_checkbox_activated);
					InitiatorDataCache.getInstance().inviteAttendSelectedMap
							.put(item.id, item);
					choosedata.add(item);
					selectAdapter.notifyChange();
					/*
					 * //从转发跳转来 if
					 * (selected&&ForwardToFriendActivity.class.getSimpleName
					 * ().equals
					 * (getIntent().getStringExtra(ENavConsts.EFromActivityName
					 * ))) {//选中全平台--取消 selected = false;
					 * allCheck.setImageResource(R.drawable.demand_me_need_checkbox_default); }
					 */
				}
			}else if(inviteType == TYPE_INVITE_AT_FRIEND){
				Intent data = new Intent();
				data.putExtra("at_name", item.getName());
				setResult(200, data);
				finishActivity();
			} else if (inviteType == TYPE_INVITE_SPEAKER_FRIEND) {
				if (InitiatorDataCache.getInstance().inviteSpeakerSelectedMap
						.containsKey(item.id)) {
					cb.setImageResource(R.drawable.demand_me_need_checkbox_default);
					InitiatorDataCache.getInstance().inviteSpeakerSelectedMap
							.remove(item.id);
					temporaryVariable();

					choosedata.remove(item);
					selectAdapter.notifyChange();
				} else {
					cb.setImageResource(R.drawable.demand_me_need_checkbox_activated);
					item.inviteSpeakerTopicList.clear();
					item.lisMeetingTopicQuery.clear();
					InitiatorDataCache.getInstance().inviteSpeakerSelectedMap
							.put(item.id, item);

					choosedata.add(item);
					selectAdapter.notifyChange();
					
				}
			} else if (inviteType == TYPE_SHARE_TO_MEETING) { // add by
																// leon,分享到畅聊
				// 单选限制
				if (!TextUtils.isEmpty(fromActivity)
						&& fromActivity.equals(MeetingChatActivity.class
								.getSimpleName())) {
					if (selectedMap.containsKey(item.id)) {
						cb.setImageResource(R.drawable.demand_me_need_checkbox_default);
						selectedMap.remove(item.id);

						choosedata.remove(item);
						selectAdapter.notifyChange();
					} else {
						if (selectedMap.isEmpty()) {
							cb.setImageResource(R.drawable.demand_me_need_checkbox_activated);
							selectedMap.put(item.id, item);
						} else {
							selectedMap.clear();
							cb.setImageResource(R.drawable.demand_me_need_checkbox_activated);
							selectedMap.put(item.id, item);
							friExpLvAdp.notifyDataSetChanged();
						}

						choosedata.add(item);
						selectAdapter.notifyChange();
					}
				}
			} else {
				if (selectedMap.containsKey(item.id)) {
					cb.setImageResource(R.drawable.demand_me_need_checkbox_default);
					selectedMap.remove(item.id);

					choosedata.remove(item);
					selectAdapter.notifyChange();
				} else {
					cb.setImageResource(R.drawable.demand_me_need_checkbox_activated);
					selectedMap.put(item.id, item);

					choosedata.add(item);
					selectAdapter.notifyChange();
				}
			}
			updateAvatarGallery();
			setRightText(choosedata.size());
			if(getSizeByType("JTContactMini") < friendSize){
				hyCheckAll.setImageResource(R.drawable.demand_me_need_checkbox_default);
				InitiatorDataCache.getInstance().friendCheckAll = false;
			}else{
				hyCheckAll.setImageResource(R.drawable.demand_me_need_checkbox_activated);
				InitiatorDataCache.getInstance().friendCheckAll = true;
			}
			
			if(getSizeByType("OrganizationMini") < orgSize){
				orgCheckAll.setImageResource(R.drawable.demand_me_need_checkbox_default);
				InitiatorDataCache.getInstance().friendOrgCheckAll = false;
			}else{
				orgCheckAll.setImageResource(R.drawable.demand_me_need_checkbox_activated);
				InitiatorDataCache.getInstance().friendOrgCheckAll = true;
			}
			return true;
		}

	}

	private class MyOnItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			JTContactMini item = ((JTContactMini) searchLvAdp.getDataList()
					.get(arg2));
			ImageView cb = (ImageView) arg1
					.findViewById(R.id.hy_itemInvitefriend_checkbox);
			if (inviteType == TYPE_INVITE_ATTEND_FRIEND||inviteType==TYPE_INVITE_AT_FLOW) {
				if (InitiatorDataCache.getInstance().inviteAttendSelectedMap
						.containsKey(item.id)) {
					cb.setImageResource(R.drawable.demand_me_need_checkbox_default);
					InitiatorDataCache.getInstance().inviteAttendSelectedMap
							.remove(item.id);
					choosedata.remove(item);
					selectAdapter.notifyChange();
				} else {
					if (!TextUtils.isEmpty(fromActivity)
							&& fromActivity.equals(CreateFlowActivtiy.class
									.getSimpleName())) {
						if (InitiatorDataCache.getInstance().inviteAttendSelectedMap
							.size()>=10) {
							Toast.makeText(InviteFriendActivity.this, "提醒的人数不得超过10人", 0).show();
							return ;
						}
					}
					choosedata.add(item);
					cb.setImageResource(R.drawable.demand_me_need_checkbox_activated);
					InitiatorDataCache.getInstance().inviteAttendSelectedMap
							.put(item.id, item);
					selectAdapter.notifyChange();
				}
			}else if(inviteType == TYPE_INVITE_AT_FRIEND){
				Intent data = new Intent();
				data.putExtra("at_name", item.getName());
				setResult(200, data);
				finishActivity();
			} else if (inviteType == TYPE_INVITE_SPEAKER_FRIEND) {
				if (InitiatorDataCache.getInstance().inviteSpeakerSelectedMap
						.containsKey(item.id)) {
					cb.setImageResource(R.drawable.demand_me_need_checkbox_default);
					InitiatorDataCache.getInstance().inviteSpeakerSelectedMap
							.remove(item.id);
					choosedata.remove(item);
					selectAdapter.notifyChange();
				} else {
					cb.setImageResource(R.drawable.demand_me_need_checkbox_activated);
					InitiatorDataCache.getInstance().inviteSpeakerSelectedMap
							.put(item.id, item);
					choosedata.add(item);
					selectAdapter.notifyChange();
				}
			} else {
				if (selectedMap.containsKey(item.id)) {
					cb.setImageResource(R.drawable.demand_me_need_checkbox_default);
					selectedMap.remove(item.id);
					choosedata.remove(item);
					selectAdapter.notifyChange();
				} else {
					cb.setImageResource(R.drawable.demand_me_need_checkbox_activated);
					selectedMap.put(item.id, item);
					choosedata.add(item);
					selectAdapter.notifyChange();
				}
			}
			setRightText(choosedata.size());
			updateAvatarGallery();
			isSelectedInSearchList = true;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case requestCode_speakerEdit: {
			Bundle b = new Bundle();
			b.putBoolean(Util.IK_VALUE, true);
			Util.activitySetResult(InviteFriendActivity.this,
					InitiatorHYActivity.class, b);
			finishActivity();
		}
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void bindData(int tag, Object object) {
		dismissLoadingDialog();
		try {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			boolean isOpen = imm.isActive();
			if (isOpen) {
				imm.hideSoftInputFromWindow(InviteFriendActivity.this
						.getCurrentFocus().getWindowToken(), 0);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		if (tag == EAPIConsts.concReqType.CONNECTIONSLIST) {
			if (object != null) {
				/*
				 * InitiatorDataCache.getInstance().connectionsList =
				 * (List<Connections>) object;
				 * InitiatorDataCache.getInstance().friendList =
				 * IniviteUtil.sortExpFriendContact
				 * (InitiatorDataCache.getInstance().connectionsList, true);
				 * friExpLvAdp
				 * .update(InitiatorDataCache.getInstance().friendList);
				 * refreshFriExpLv();
				 */
				// 启动后台任务
				new GetConnectionsTask().execute();
			} else {
				Toast.makeText(this, "没有获取到的好友", Toast.LENGTH_SHORT).show();
			}
		}
		// add by leon
		else if (tag == EAPIConsts.concReqType.ContactDetail) { // 获取人脉详情
			if (object == null) {
				Toast.makeText(this, "选择人脉失败", Toast.LENGTH_SHORT).show();
				return;
			}
			JTFile jtFile = null;
			ArrayList returnData = (ArrayList) object;
			JTContact2 onlineTContact = null;
			JTContact2 offlineTContact = null;
			for (int i = 0; returnData.size() > i; i = i + 2) {
				int type = (Integer) returnData.get(i);
				if (type == 0) {
					onlineTContact = (JTContact2) returnData.get(i + 1);
				} else {
					offlineTContact = (JTContact2) returnData.get(i + 1);
				}
			}
			if (onlineTContact != null) { // 用户或线上机构
				jtFile = onlineTContact.toJTfile();
				jtFile.mType = JTFile.TYPE_JTCONTACT_ONLINE;
				// 返回值
				Intent intent = new Intent();
				intent.putExtra(ENavConsts.redatas, jtFile);
				setResult(Activity.RESULT_OK, intent);
				finish();
			} else if (offlineTContact != null) { // 人脉或线下机构
				jtFile = offlineTContact.toJTfile();
				jtFile.mType = JTFile.TYPE_JTCONTACT_OFFLINE;
				// 返回值
				Intent intent = new Intent();
				intent.putExtra(ENavConsts.redatas, jtFile);
				setResult(Activity.RESULT_OK, intent);
				finish();
			} else {
				Toast.makeText(this, "选择人脉失败", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		searchEditWatcher.resetSearch();
		searchLvAdp.release();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// if (searchLv.getVisibility() == View.VISIBLE){
			// searchLv.setVisibility(View.GONE);
			// searchLvAdp.getDataList().clear();
			// friListLayout.setVisibility(View.VISIBLE);
			// if(isSelectedInSearchList){
			// isSelectedInSearchList = false;
			// refreshFriExpLv();
			// }
			// return false;
			// }else{
			// return super.onKeyDown(keyCode, event);
			// }
			MRoadShowCacheFiles.getInstance().releaseAll();
			if (!SearchUtil.isEmpty(searchEdit.getText().toString())) {
				searchEdit.setText("");
				return false;
			} else {
				return super.onKeyDown(keyCode, event);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initData() {
		// startGetConnections();

	}

	@Override
	public void onTouchingLetterChanged(String s, boolean fal) {
		// TODO Auto-generated method stub
		if (fal) {
			showSelLetterView.setVisibility(View.GONE);
		} else {
			int count = 0;
			showSelLetterView.setText(s);
			showSelLetterView.setVisibility(View.VISIBLE);
			List<MExpFriendContact> datalist = friExpLvAdp
					.getExpFriendContact();
			if (Util.isNull(datalist)) {
				return;
			}
			for (int i = 0; i < datalist.size(); i++) {
				if (datalist.get(i).nameCh.equals(s)) {
					friExpLv.setSelection(i + count);
					break;
				}
				count = count + datalist.get(i).contactList.size();
			}
		}
	}

	@Override
	public void onSearchListener(int searchType, ArrayList<Object> resultList) {
		// TODO Auto-generated method stub
		if (SearchUtil.isEmpty(searchEdit.getText().toString())) {
			searchLv.setVisibility(View.GONE);
			searchLvAdp.getDataList().clear();
			friListLayout.setVisibility(View.VISIBLE);
			if (isSelectedInSearchList) {
				isSelectedInSearchList = false;
				refreshFriExpLv();
			}
			return;
		}
		isSelectedInSearchList = false;
		friListLayout.setVisibility(View.GONE);
		searchLv.setVisibility(View.VISIBLE);
		searchLvAdp.update(resultList);
		searchEditWatcher.resetSearch();
	}

	@Override
	public void onBackPressed() {
		InitiatorDataCache.getInstance().inviteSpeakerSelectedMap = inviteSpeakerSelectedMapBak;
		InitiatorDataCache.getInstance().inviteAttendSelectedMap.clear();
		InitiatorDataCache.getInstance().inviteAttendSelectedMap = inviteAttendSelectedMap;
		super.onBackPressed();
	}

	/**
	 * 获取好友列表后台任务
	 * 
	 * @author leon
	 */
	private class GetConnectionsTask extends
			AsyncTask<Void, Integer, ArrayList<Connections>> {

		public GetConnectionsTask() {

		}

		@Override
		protected ArrayList<Connections> doInBackground(Void... params) {
			int perFriendSize = connsDBManager
					.queryFriendSize(Connections.type_persion);
			int orgFriendSize = connsDBManager
					.queryFriendSize(Connections.type_org);
			ArrayList<Connections> listConns = new ArrayList<Connections>();
			ArrayList<Connections> listPerFriends = connsDBManager.queryFriend(
					0, perFriendSize, Connections.type_persion);
			if (listPerFriends != null) {
				listConns.addAll(listPerFriends);
			}
			ArrayList<Connections> listOrgFriends = connsDBManager.queryFriend(
					0, orgFriendSize, Connections.type_org);
			if (listOrgFriends != null) {
				listConns.addAll(listOrgFriends);
			}
			return listConns;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
			// 进度更新
		}

		@Override
		protected void onPostExecute(ArrayList<Connections> result) {
			InitiatorDataCache.getInstance().connectionsList = result;
			if (isInitMyself) {//需要初始化自己，就把自己加进来
				Connections mySelfConnections = addMySelfToList();
				if (mySelfConnections!=null) {
					InitiatorDataCache.getInstance().connectionsList.add(mySelfConnections);
				}
			}
			InitiatorDataCache.getInstance().friendList = IniviteUtil
					.sortExpFriendContact(
							InitiatorDataCache.getInstance().connectionsList,
							true);

			List<MExpFriendContact> friendlist = InitiatorDataCache.getInstance().friendList;
			if(!friendlist.isEmpty()){
				for (MExpFriendContact mfc : friendlist) {
					
				

				tv_myfriend_char.setVisibility(View.VISIBLE);
				tv_myfriend_char.setText(mfc.nameCh);
				for(JTContactMini jtc : mfc.contactList){
					friendSize++;
					if(InitiatorDataCache.getInstance().inviteAttendSelectedMap.containsKey(jtc.id)){
						choosedata.add(jtc);
						
					}
				}
				
				}
				selectAdapter.notifyChange();
				setRightText(choosedata.size());
			}
			friExpLvAdp.update(InitiatorDataCache.getInstance().friendList);
			refreshFriExpLv();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		registerReceiver(connsUpdateReceiver, new IntentFilter(
				EConsts.Action.ACTION_GET_CONNECTIONS_LIST_FINISH)); // 注册接收器
	}

	@Override
	public void onPause() {
		super.onPause();
		try {
			unregisterReceiver(connsUpdateReceiver); // 取消接收器
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 人脉更新广播接收器
	 * 
	 * @author leon
	 */
	private class ConnectionsUpdateReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			dismissLoadingDialog();
			new GetConnectionsTask().execute();
		}
	}
	/**
	 * 将自己加入列表
	 */
	private Connections addMySelfToList(){
		Connections myselfConnections = null;
			myselfConnections = new Connections();
			myselfConnections.setOnline(true);
			if (App.getApp().getAppData().getUser().getmUserType() == JTMember.UT_PERSON) {
				myselfConnections.type = Connections.type_persion;
				JTContactMini jtContactMini = myselfConnections.jtContactMini;
				jtContactMini.setId(App.getUser().mID);
				
				if (!StringUtils.isEmpty(App.getUser().getmNick())) {
					jtContactMini.setName(App.getUser().getmNick());
				} else if (!StringUtils.isEmpty(App.getUser().getmUserName())) {
					jtContactMini.setName(App.getUser().getmUserName());
				} else {
					jtContactMini.setName("");
				}
				jtContactMini.nameChar = '#';
				jtContactMini.image = App.getUser().getImage();
			}
			else {
				myselfConnections.type = Connections.type_org;
				OrganizationMini organizationMini = myselfConnections.organizationMini;
				organizationMini.setId(App.getUserID());
				organizationMini.fullName = App.getUser().getmOrganizationInfo().mFullName;
				organizationMini.shortName = App.getUser().mNick;
				organizationMini.mLogo = App.getUser().getImage();
				organizationMini.nameChar = '#';
			}
			return myselfConnections;
	}
	
	/** 选中的显示类 */
	public class SelectAdapter extends BaseAdapter {
		Context mContext = null;

		public SelectAdapter(Context context) {
			mContext = context;
			// iMGroupCategorys.add(new IMGroupCategory("334", "String 1"));
		}
		
		public void notifyChange(){
			this.notifyDataSetChanged();
			if(choosedata.size()>0){
				horizontalListView.setSelection(choosedata.size() - 1);
			}
		}

		@Override
		public int getCount() {
			return choosedata.size();

			// TODO Auto-generated method stub
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final Object obj = choosedata.get(position);
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.im_relationcontactselect_itemed, null);
				convertView.setTag(obj);
			} else {
				convertView.setTag(obj);
			}
			ImageView avatar_iv =  (ImageView) convertView.findViewById(R.id.avatar_iv);
			OrganizationMini mOrganizationMini = null;
			JTContactMini jtcontactmini = null;
			if(obj instanceof OrganizationMini){
				mOrganizationMini = (OrganizationMini) obj;
				com.utils.common.Util.initAvatarImage(mContext, avatar_iv, mOrganizationMini.getShortName(), mOrganizationMini.getLogo(), 1, 1);
			}else if(obj instanceof JTContactMini){
				jtcontactmini = (JTContactMini) obj;
				if(jtcontactmini.getId().equals("0")){
					avatar_iv.setImageResource(R.drawable.gintong_smart_brain);
				}else{
					com.utils.common.Util.initAvatarImage(mContext, avatar_iv, jtcontactmini.getName(), jtcontactmini.getImage(), 1, 1);
				}
			}
			return convertView;
		}
	}
	
	public void setRightText(int count){
		rightTextBtn.setText("确定("+count+")"); //初始化选定数量
	}
	
	public int getSizeByType(String type){
		int size = 0;
		if(type.equals("JTContactMini")){
			for(Object obj:choosedata){
				if(obj instanceof JTContactMini){
					if(!((JTContactMini) obj).getId().equals("0")){
						size++;
					}
				}
			}
		}else if(type.equals("OrganizationMini")){
			for(Object obj:choosedata){
				if(obj instanceof OrganizationMini){
					size++;
				}
			}
		}
		return size;
	}
}
