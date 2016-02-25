package com.tr.ui.im;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.Editable;
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
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tr.App;
import com.tr.R;
import com.tr.api.CommunityReqUtil;
import com.tr.api.ConnectionsReqUtil;
import com.tr.api.IMReqUtil;
import com.tr.api.OrganizationReqUtil;
import com.tr.api.PeopleReqUtil;
import com.tr.db.ConnectionsCacheData;
import com.tr.db.ConnectionsDBManager;
import com.tr.model.api.DataBox;
import com.tr.model.im.ChatDetail;
import com.tr.model.im.MCreateMUC;
import com.tr.model.obj.ChatMessage;
import com.tr.model.obj.Connections;
import com.tr.model.obj.ConnectionsMini;
import com.tr.model.obj.IMBaseMessage;
import com.tr.model.obj.JTContact2;
import com.tr.model.obj.JTContactMini;
import com.tr.model.obj.JTFile;
import com.tr.model.obj.MUCDetail;
import com.tr.model.obj.MUCMessage;
import com.tr.model.user.JTMember;
import com.tr.model.user.OrganizationMini;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.service.GetConnectionsListService;
import com.tr.service.GetConnectionsListService.RequestType;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.communities.home.CommunitiesDetailsActivity;
import com.tr.ui.communities.model.CommunityNotify;
import com.tr.ui.communities.model.ImMucinfo;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.organization.model.CustomerProfileVo;
import com.tr.ui.organization.model.OrganizationUserDetailIncomingParameters;
import com.tr.ui.organization.model.param.CustomerOrganizatianParams;
import com.tr.ui.organization.model.parameters.OrganizationDetialsIncomingParameters;
import com.tr.ui.people.model.PeopleDetails;
import com.tr.ui.people.model.Person;
import com.tr.ui.people.model.params.PeopleDetialsIncomingParameters;
import com.tr.ui.share.SocialShareActivity;
import com.tr.ui.widgets.HorizontalListView;
import com.utils.common.CustomDlgClickListener;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.common.GlobalVariable;
import com.utils.common.Util;
import com.utils.http.EAPIConsts;
import com.utils.http.EAPIConsts.OrganizationReqType;
import com.utils.http.EAPIConsts.PeopleRequestType;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;

/**
 * 选择联系人界面 用于场景: 创建群聊 等 现在应包含组织好友
 * 
 * @author gushi
 * 
 */
public class IMRelationSelectActivity extends JBaseActivity implements
		IBindData {
	public static final String TAG = "IMRelationSelectActivity";

	private ExpandableListView lvContact;
	ContactAdapter contactAdapter;
	private WindowManager mWindowManager;
	HorizontalListView horizontalListView;// 选中横向list
	ArrayList<Connections> selectGroupCategorys = new ArrayList<Connections>();// 选中数据
	ArrayList<Connections> reselectGroupCategorys = new ArrayList<Connections>();// 传入的选中数据
	ArrayList<Connections> iMGroupCategorys = new ArrayList<Connections>();// 列表数据
	ArrayList<ArrayList<Connections>> iMGroupCategoryslist = new ArrayList<ArrayList<Connections>>();// 群组数据
	EditText editText = null;// 输入框

	private String fromActivityName;
	private MUCDetail mucDetail;// 不为空，表示从 创建会议，群聊设置录像过来
	private ChatDetail chatDetail;// 不为空，表示从私聊-私聊设置页路线过来的

	/** 是否单选 */
	private boolean isSingle = false;
	/** 是否初始化删除传进来的数据 */
	private boolean isInitInDataDel = true;
	/** 是否初始化 自己 */
	private boolean isInitMyself = false;
	/** 是否必需选择联系人 */
	private boolean isIndispensableSelect = true;

	// IM进来，只需要显示用户的好友，其他的都不要显示， 没事业部搜索
	/** IM类型 显示 个人好友和组织好友混排 */
	public final static int FT_IM = 1;
	public final static int FT_org = 3;// 机构
	public final static int FT_OTHER = 100;// 其他类型， 具体各自用到的找huangliang加
	public final static int FT_SIM = 101;// sim卡联系人
	public final static int FT_recommend = 102;// 存储
	public final static int FT_relevantPeopleAndCustomer = 103;// 相关机构和联系人
	public final static int FT_atChatGroupFriends = 104;// @好友
	private int mFilterType = FT_OTHER;

	private JTFile mShareInfo; // 分享的知识
	private ArrayList<JTFile> mShareInfoList; // 分享的知识列表
	private String mMessage; // 用户附加消息
	// private IMBaseMessage mShareMsg; // 包含知识的消息
	private MUCDetail mMucDetail; // 群聊信息
	private ChatDetail mChatDetail; // 私聊细心
	private RelativeLayout selecteGroupView = null;

	public ConnectionsDBManager connectionsDBManager = null;
	public ConnectionsCacheData cnsCacheData;

	private boolean furtherHandle = false;

	/** 创建畅聊后立即分享的Jtfile集合 */
	private ArrayList<JTFile> listJtFile;
	private boolean isShareFile;

	// 初始化关系
	ArrayList<Connections> datas = null;
	ArrayList<Connections> orgdatas = null;

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.im_relationcontactselect_list);
		registerGetConnectionsListBroadcastReceiver();
		GetConnectionsListService.startGetConnectionsListService(this,
				RequestType.FriendAll);
		selecteGroupView = (RelativeLayout) this
				.findViewById(R.id.selecteGroup);
		getParam();
		getShareParam(); // 获取分享的参数
		mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		findView();
		editText = (EditText) findViewById(R.id.edit);
		editText.addTextChangedListener(new Watcher());

		horizontalListView = (HorizontalListView) findViewById(R.id.choosedata);
		horizontalListView.setAdapter(new SelectAdapter(this));
		horizontalListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				SelectAdapter selectAdapter = (SelectAdapter) horizontalListView
						.getAdapter();
				Connections connections = (Connections) arg1.getTag();
				selectGroupCategorys.remove(connections);
				contactAdapter.notifyDataSetChanged();
				selectAdapter.notifyDataSetChanged();
			}
		});
		// 初始化数据库对象
		connectionsDBManager = ConnectionsDBManager
				.buildConnectionsDBManager(this);
		if (mFilterType == FT_IM || mFilterType == FT_OTHER
				|| mFilterType == FT_recommend) {
			if (connectionsDBManager.queryTotalSize("") > 0) {
				ArrayList<Connections> IMDatas = new ArrayList<Connections>();
				if (mFilterType == FT_IM || mFilterType == FT_recommend) { // 创建群聊
					// 初使化自己
					Connections myselfConnections = null;
					if (isInitMyself) {
						myselfConnections = new Connections();
						if (App.getApp().getAppData().getUser().getmUserType() == JTMember.UT_PERSON) {
							myselfConnections.type = Connections.type_persion;
							JTContactMini jtContactMini = myselfConnections.jtContactMini;
							jtContactMini.setId(App.getUser().mID);

							if (!StringUtils.isEmpty(App.getUser().getmNick())) {
								jtContactMini.setName(App.getUser().getmNick());
							} else if (!StringUtils.isEmpty(App.getUser()
									.getmUserName())) {
								jtContactMini.setName(App.getUser()
										.getmUserName());
							} else {
								jtContactMini.setName("");
							}
							jtContactMini.nameChar = '#';
							jtContactMini.image = App.getUser().getImage();
						} else {
							myselfConnections.type = Connections.type_org;
							OrganizationMini organizationMini = myselfConnections.organizationMini;
							// organizationMini.setId(App.getUser().mID);
							organizationMini.setId(App.getUserID());
							organizationMini.fullName = App.getUser()
									.getmOrganizationInfo().mFullName;
							organizationMini.shortName = App.getUser().mNick;
							organizationMini.mLogo = App.getUser().getImage();
							organizationMini.nameChar = '#';
						}
					}
					// 初始化关系
					// ArrayList<Connections> datas=null;
					// ArrayList<Connections> orgdatas = null;
					if (mFilterType == FT_IM) {
						// datas = connectionsDBManager.queryFriend(0, 100000);
						// datas = connectionsDBManager.queryFriendAll(0,
						// 100000);
						datas = connectionsDBManager.queryUserFriend(0, 100000);
						// orgdatas = connectionsDBManager.queryJoinOrg();
					} else {
						datas = connectionsDBManager.queryFriend(0, 100000);
					}
					// 把自己 加到 关系列表第一位
					if (myselfConnections != null) {
						datas.add(0, myselfConnections);
					}

					// if (orgdatas != null && orgdatas.size() > 0) {
					// iMGroupCategoryslist.add(orgdatas);
					// }
					// if (datas != null && datas.size() > 0) {
					// iMGroupCategoryslist.add(datas);
					// }
				} else {
					connectionsDBManager = new ConnectionsDBManager(
							IMRelationSelectActivity.this,
							connectionsDBManager.getTableName());
					cnsCacheData = new ConnectionsCacheData(
							connectionsDBManager);
					cnsCacheData.setFilterType(ConnectionsCacheData.FILTER_ALL);
				}
				if (mFilterType == FT_IM || mFilterType == FT_recommend) {
					showLoadingDialog();
					ConnectionsReqUtil.doGetOrgRelations(getApplication(),
							IMRelationSelectActivity.this, RequestType.All,
							null);
				} else {
					Connections connections = new Connections();
					Connections orgconnections = new Connections();
					OrganizationMini organizationMini = new OrganizationMini();
					if (mFilterType == FT_IM || mFilterType == FT_recommend) {
						connections.jtContactMini.name = "我的好友";
						organizationMini.fullName = "我的组织";
						connections.type = Connections.type_persion;
						orgconnections.type = Connections.type_org;
						orgconnections.organizationMini = organizationMini;
						iMGroupCategorys.add(orgconnections);
						iMGroupCategorys.add(connections);
					} else {
						organizationMini.fullName = "所有关系";
						orgconnections.type = Connections.type_org;
						orgconnections.organizationMini = organizationMini;
						iMGroupCategorys.add(orgconnections);
					}

					if (FT_OTHER == mFilterType) {// 如果是全部数据
						if (cnsCacheData == null || cnsCacheData.size() == 0) {
							showLongToast("数据为空");
						} else {
							if (iMGroupCategorys != null
									&& iMGroupCategorys.size() == 1
									&& lvContact != null) {
								lvContact.expandGroup(0);
							}
							initInData();
						}
					} else {
						if (iMGroupCategoryslist == null
								|| iMGroupCategoryslist.size() == 0
								|| iMGroupCategoryslist.get(0) == null
								|| iMGroupCategoryslist.get(0).size() == 0) {
							showLongToast("数据为空");
						} else {
							if (iMGroupCategorys != null
									&& iMGroupCategorys.size() != 0
									&& lvContact != null) {
								int size = iMGroupCategorys.size();
								for (int i = 0; i < size; i++) {
									lvContact.expandGroup(i);
								}
							}
						}
						if (mFilterType == FT_IM || mFilterType == FT_recommend) {
							if (isInitInDataDel) {
								initInDataDel();
							} else {
								initInData();
							}
						} else {
							initInData();
						}
						if (fromActivityName
								.equalsIgnoreCase(ENavConsts.EForwardToFriendActivity)) {
							initInData();
						}
					}
					// // 如果传进来的数据大于0 就复选
					// if (reselectGroupCategorys != null &&
					// reselectGroupCategorys.size() > 0){
					// initInData();
					// }
					contactAdapter.setDatas(iMGroupCategoryslist,
							iMGroupCategorys);
					contactAdapter.notifyDataSetChanged();
				}
			}
		} else if (mFilterType == FT_org) {
			JSONObject json = new JSONObject();
			JSONArray jsonA = new JSONArray();
			try {
				// for(int i=0;i<datasArry.length;i++){
				// jsonA.put(datasArry[i]);
				// }
				json.put("listOrganiationID", jsonA);
				json.put("type", 3);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ConnectionsReqUtil.dogetWorkmate(IMRelationSelectActivity.this,
					this, json, mHandler);
			showLoadingDialog();
			// show
		} else if (mFilterType == FT_SIM) {
			isSingle = true;
			showLoadingDialog();
			new Thread(simrun).start();
		} else if (mFilterType == FT_relevantPeopleAndCustomer) {
			JSONObject json = new JSONObject();
			ConnectionsReqUtil.getRelevantPeopleAndCustomer(
					IMRelationSelectActivity.this, this, json, mHandler);
			showLoadingDialog();
		}

	}

	/** 接收 获得联系人列表后 写入数据完成后的 广播 */
	private BroadcastReceiver getConnectionsListBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String MSG = "onReceive()";
			// dismissLoadingDialog();
			String action = intent.getAction();
			Log.i(TAG, MSG + " action = " + action);

			if (EConsts.Action.ACTION_GET_CONNECTIONS_LIST_FINISH
					.equals(action)) {

				boolean success = intent.getBooleanExtra(EConsts.Key.SUCCESS,
						false);

				if (success) {
					String tableName = intent
							.getStringExtra(EConsts.Key.TABLE_NAME);

					Log.i(TAG, MSG + " tableName = " + tableName);
					if (tableName != null) {
						if (cnsCacheData != null) {
							cnsCacheData.setTableName(tableName);
						} else {
							cnsCacheData = new ConnectionsCacheData(
									connectionsDBManager);
							cnsCacheData.setTableName(tableName);
						}

						contactAdapter.notifyDataSetChanged();
					}

					SharedPreferences sp = context.getSharedPreferences(
							EConsts.share_firstLoginGetConnections,
							Activity.MODE_PRIVATE);
					Editor editor = sp.edit();
					editor.putString(EConsts.share_itemUserTableName, tableName);
					editor.commit();

					// loadConnectionsWaitView.setVisibility(View.GONE);

					contactAdapter.notifyDataSetChanged();

				} else {
					Toast.makeText(IMRelationSelectActivity.this, "请求失败 请重试.",
							0).show();
				}
			}

		}
	};

	/**
	 * 注册 获取联系人列表 完成 广播
	 */
	private void registerGetConnectionsListBroadcastReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(EConsts.Action.ACTION_GET_CONNECTIONS_LIST_FINISH);
		registerReceiver(getConnectionsListBroadcastReceiver, filter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterGetConnectionsListBroadcastReceiver();
	}

	/**
	 * 注销 获取联系人列表 完成 广播
	 */
	private void unregisterGetConnectionsListBroadcastReceiver() {
		unregisterReceiver(getConnectionsListBroadcastReceiver);
	}

	private Cursor getPhoneByType(String contactId, int type) {
		Cursor phone = IMRelationSelectActivity.this.getContentResolver()
				.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = " + contactId + " AND "
								+ ContactsContract.CommonDataKinds.Phone.TYPE
								+ "=" + type, null, null);
		return phone;
	}

	Runnable simrun = new Runnable() {
		public void run() {
			ArrayList<Connections> connectionsArray = new ArrayList<Connections>();
			getSimContacts(connectionsArray);
			iMGroupCategoryslist.add(connectionsArray);
			simHandler.sendEmptyMessage(0);
		}
	};

	protected Handler simHandler = new Handler() {
		public void handleMessage(Message msg) {

			Connections connections = new Connections();
			OrganizationMini organizationMini = new OrganizationMini();
			organizationMini.fullName = "所有关系";
			connections.type = Connections.type_org;
			connections.organizationMini = organizationMini;
			iMGroupCategorys.add(connections);

			contactAdapter.setDatas(iMGroupCategoryslist, iMGroupCategorys);
			contactAdapter.notifyDataSetChanged();
			if (iMGroupCategoryslist.get(0).size() == 0) {
				showLongToast("数据为空");
			} else {
				if (iMGroupCategorys != null && iMGroupCategorys.size() == 1
						&& lvContact != null) {
					lvContact.expandGroup(0);
				}
			}
			dismissLoadingDialog();
		}
	};

	// 传入数据初始化
	public void initInData() {
		boolean isSinglebreak = false;
		boolean isFindOkBreak = false;
		if (reselectGroupCategorys != null
				&& reselectGroupCategorys.size() != 0) {// 穿入数据不为空
			if (iMGroupCategoryslist != null
					&& iMGroupCategoryslist.size() != 0) {// 本地数组不为空
				for (Connections selectconnections : reselectGroupCategorys) {
					isFindOkBreak = false;
					for (ArrayList<Connections> groups : iMGroupCategoryslist) {// 二级数组便利
						for (Connections connectionone : groups) {
							if (selectconnections.type == connectionone.type
									&& selectconnections.getId().equals(
											connectionone.getId())) {
								selectGroupCategorys.add(connectionone);
								if (isSingle) {
									isSinglebreak = true;
									break;
								}
								isFindOkBreak = true;
								break;
							}
						}
						if (isSinglebreak) {
							break;
						}
						if (isFindOkBreak) {
							break;
						}
					}
					if (isSinglebreak) {
						break;
					}
				}
			}
		}
		SelectAdapter selectAdapter = (SelectAdapter) horizontalListView
				.getAdapter();
		selectAdapter.notifyDataSetChanged();
	}

	// 传入数据初始化
	public void initInDataDel() {
		boolean isSinglebreak = false;
		boolean isFindOkBreak = false;
		if (reselectGroupCategorys != null
				&& reselectGroupCategorys.size() != 0) {// 穿入数据不为空
			if (iMGroupCategoryslist != null
					&& iMGroupCategoryslist.size() != 0) {// 本地数组不为空
				for (Connections selectconnections : reselectGroupCategorys) {
					isFindOkBreak = false;
					for (ArrayList<Connections> groups : iMGroupCategoryslist) {// 二级数组便利
						for (Connections connectionone : groups) {
							if (selectconnections.jtContactMini.id == connectionone
									.getId()
									|| selectconnections.organizationMini.mID == connectionone
											.getId()) {
								groups.remove(connectionone);
								if (isSingle) {
									isSinglebreak = true;
									break;
								}
								isFindOkBreak = true;
								break;
							}
						}
						if (isSinglebreak) {
							break;
						}
						if (isFindOkBreak) {
							break;
						}
					}
					if (isSinglebreak) {
						break;
					}
				}
			}
		}
		SelectAdapter selectAdapter = (SelectAdapter) horizontalListView
				.getAdapter();
		selectAdapter.notifyDataSetChanged();
	}

	public void doFinish() {
		Intent it = new Intent();
		it.putExtra(ENavConsts.redatas, selectGroupCategorys);
		setResult(Activity.RESULT_OK, it);
		finish();
	}

	public void doFinish(JTFile file) {
		Intent it = new Intent();
		it.putExtra(ENavConsts.redatas, file);
		setResult(Activity.RESULT_OK, it);
		finish();
	}

	String datasArry[] = null;

	// 读取传入的参数
	public void getParam() {
		Intent intent = getIntent();
		fromActivityName = intent.getStringExtra(ENavConsts.EFromActivityName);
		mFilterType = intent
				.getIntExtra(ENavConsts.EFromActivityType, FT_OTHER);
		if (intent.hasExtra(ENavConsts.IsSingleSelection)) {
			isSingle = intent.getBooleanExtra(ENavConsts.IsSingleSelection,
					false);
		}
		if (intent.hasExtra(ENavConsts.IsInitInDataDel)) {
			isInitInDataDel = intent.getBooleanExtra(
					ENavConsts.IsInitInDataDel, false);
		}
		if (intent.hasExtra(ENavConsts.IsInitMyself)) {
			isInitMyself = intent.getBooleanExtra(ENavConsts.IsInitMyself,
					false);
		}

		if (intent.hasExtra(ENavConsts.IsInitMyself)) {
			isIndispensableSelect = intent.getBooleanExtra(
					ENavConsts.isIndispensableSelect, true);
		}
		// if(mFilterType==FT_org){
		// datasArry=intent.getStringArrayExtra(ENavConsts.datas);
		// datasArry=new String[]{"1","36","37"};
		// }
		if (!StringUtils.isEmpty(fromActivityName)) {
			mucDetail = (MUCDetail) intent
					.getSerializableExtra(ENavConsts.EMucDetail);
			chatDetail = (ChatDetail) intent
					.getSerializableExtra(ENavConsts.EChatDetail);

			if (fromActivityName
					.equalsIgnoreCase(ENavConsts.EIMCreateGroupActivity)
					|| fromActivityName
							.equalsIgnoreCase(ENavConsts.EForwardToFriendActivity)) {
				// 创建会议-会议主题设置-选择 联系人路线
				mFilterType = FT_IM;
			} else if (fromActivityName
					.equalsIgnoreCase(ENavConsts.EMainActivity)) {
				// 首页直接创建群聊
				mFilterType = FT_IM;
			} else if (fromActivityName
					.equalsIgnoreCase(ENavConsts.EShareActivity)) {
				// 分享直接创建群聊
				mFilterType = FT_IM;
				selecteGroupView.setVisibility(View.VISIBLE);
			} else if (fromActivityName
					.equalsIgnoreCase(ENavConsts.EIMEditMemberActivity)) {
				// 从群聊设置页面过来
				mFilterType = FT_IM;
			} else if (fromActivityName
					.equalsIgnoreCase(ENavConsts.EShareCnsSelectActivity)) {
				isSingle = true;
			} else if (fromActivityName
					.equalsIgnoreCase(SocialShareActivity.class.getSimpleName())) {
				// 从社交分享页面过来
				mFilterType = FT_IM;
			} else if (fromActivityName
					.equalsIgnoreCase(ENavConsts.ECommunityChatSettingActivity)) {
				// 从社群设置过来
				mFilterType = FT_IM;
			}
		}
		if (intent.hasExtra(ENavConsts.datas)) {
			Object obj = intent.getSerializableExtra(ENavConsts.datas);
			if (obj instanceof ArrayList) {
				reselectGroupCategorys = (ArrayList<Connections>) obj;
				// for(int i=reselectGroupCategorys.size()-1;i>=0;i--){
				// if(reselectGroupCategorys.get(i).getId()==0){
				// reselectGroupCategorys.remove(i);
				// }
				// }
				// for (int i = 0; i < reselectGroupCategorys.size(); i++) {
				// selectGroupCategorys.add(reselectGroupCategorys.get(i));
				// }
				// initInData();

				// isSingle = false;
			} else if (obj instanceof Connections) {
				Connections connections = (Connections) obj;
				reselectGroupCategorys = new ArrayList<Connections>();
				// if(connections.getId()!=0){
				reselectGroupCategorys.add(connections);
				// }
				isSingle = true;
			}
		}
		// 传过来在创建新畅聊后立即分享的Jtfile集合
		if (intent.hasExtra(ENavConsts.EListJTFile)) {
			listJtFile = (ArrayList<JTFile>) intent
					.getSerializableExtra(ENavConsts.EListJTFile);
			if (listJtFile != null && listJtFile.size() > 0) {
				isShareFile = true;
			}
		}
	}

	// 获取分享的参数
	@SuppressWarnings("unchecked")
	protected void getShareParam() {

		mMessage = "";
		if (getIntent().hasExtra(ENavConsts.EShareParam)) { // 分享单个项目
			mShareInfo = (JTFile) getIntent().getSerializableExtra(
					ENavConsts.EShareParam);
			/*
			 * if(mShareInfo != null && mShareInfo.mType ==
			 * JTFile.TYPE_KNOWLEDGE &&
			 * TextUtils.isEmpty(mShareInfo.mSuffixName)){ showLoadingDialog();
			 * UserReqUtil.doGetTreatedHtml(this, this,
			 * UserReqUtil.getTreatedHtmlParams(mShareInfo.mUrl), null); }
			 */
		} else if (getIntent().hasExtra(ENavConsts.EShareParamList)) { // 分享多个项目
			mShareInfoList = (ArrayList<JTFile>) getIntent()
					.getSerializableExtra(ENavConsts.EShareParamList);
		}
	}

	MenuItem confirm;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.im_chatmenu, menu);
		confirm = menu.findItem(R.id.conference_create_next);
		if (isSingle) {
			confirm.setTitle("确定");
		} else {
			confirm.setTitle("确定(0)");
		}
		return true;
	}

	/**
	 * actionbar 中菜单点击事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// doFinish();

			// Intent it = new Intent();
			// it.putExtra(ENavConsts.redatas, selectGroupCategorys);
			// setResult(Activity.RESULT_CANCELED, it);
			finish();
			break;
		case R.id.conference_create_next: {
			if (isIndispensableSelect) {
				if (selectGroupCategorys.size() <= 0) {
					showToast("请选择联系人");
					return super.onOptionsItemSelected(item);
				}
			}
			if (fromActivityName
					.equalsIgnoreCase(ENavConsts.EIMCreateGroupActivity)) { // 创建一个新的会议
				List<ConnectionsMini> listMini = new ArrayList<ConnectionsMini>();
				for (Connections cons : selectGroupCategorys) {
					ConnectionsMini mini = new ConnectionsMini();
					mini.setId("" + cons.getId());
					mini.setImage(null);
					mini.setName(cons.getName());
					mini.setType(cons.getmType());
					listMini.add(mini);
				}
				mucDetail.setListConnectionsMini(listMini);
				createConference();
			} else if (fromActivityName
					.equalsIgnoreCase(ENavConsts.EMainActivity)) { // 创建一个新的群聊
				if (selectGroupCategorys.size() > 1) { // 创建群聊
					createMUC();
				} else { // 创建私聊
					Connections con = this.selectGroupCategorys.get(0);
					ChatDetail chatDetail = new ChatDetail();
					chatDetail.setThatID(con.getId() + "");
					chatDetail.setThatImage(con.getImage());
					chatDetail.setThatName(con.getName());
					chatDetail.setType(con.type);
					if (isShareFile) {
						ENavigate.startIMActivity(this, chatDetail, listJtFile);
					} else {
						ENavigate.startIMActivity(this, chatDetail);
					}
					finish();
				}
			} else if (fromActivityName
					.equalsIgnoreCase(ENavConsts.EIMEditMemberActivity)) { // 从群聊设置页过来，可能是从私聊创建一个新的群聊，也可能是在已有群聊中加人
				if (mucDetail != null) { // 在已有群聊中加人
					invite2MUC();
				} else { // 私聊进来，创建一个新的群聊
					ConnectionsMini mini = new ConnectionsMini();
					mini.setName(chatDetail.getThatName());
					mini.setId(chatDetail.getThatID());
					mini.setType(chatDetail.getType());
					createMUC(mini);
				}
			} else if (fromActivityName
					.equalsIgnoreCase(ENavConsts.ESelectConnection)) { // 选择人脉
				Intent intent = new Intent();
				intent.putExtra(ENavConsts.datas, selectGroupCategorys);
				setResult(Activity.RESULT_OK, intent);
			} else if (fromActivityName
					.equalsIgnoreCase(ENavConsts.ESelectOrgman)) { // 选择组织
				Intent intent = new Intent();
				intent.putExtra(ENavConsts.datas, selectGroupCategorys);
				setResult(Activity.RESULT_OK, intent);
			} else if (fromActivityName
					.equalsIgnoreCase(com.tr.ui.share.ShareActivity.class
							.getSimpleName())
					|| fromActivityName
							.equalsIgnoreCase(SocialShareActivity.class
									.getSimpleName())) { // 分享页面过来
				if (selectGroupCategorys.size() > 1) { // 创建群聊
					createMUC();
				} else { // 只选择一个人，创建私聊
					Connections con = this.selectGroupCategorys.get(0);
					final ChatDetail chatDetail = new ChatDetail();
					chatDetail.setThatID(con.getId() + "");
					chatDetail.setThatImage(con.getImage());
					chatDetail.setThatName(con.getName());

					if (mShareInfo != null) { // 分享单个项目
						if (mShareInfo.mType == JTFile.TYPE_KNOWLEDGE) { // 知识
							EUtil.showShareDialog(this, mShareInfo.mSuffixName,
									new CustomDlgClickListener() {
										@Override
										public void onPositiveClick(
												String message) {
											mShareInfo.mFileName = message;
											mChatDetail = chatDetail;
											// 分享到私聊
											ENavigate
													.startIMActivity(
															IMRelationSelectActivity.this,
															chatDetail,
															mShareInfo);
											finish();
										}
									});
						} else if (mShareInfo.mType == JTFile.TYPE_KNOWLEDGE2) { // 新知识
							EUtil.showShareDialog(this, mShareInfo.reserved2,
									new CustomDlgClickListener() {
										@Override
										public void onPositiveClick(
												String message) {
											mShareInfo.mFileName = message;
											// 分享到私聊
											ENavigate
													.startIMActivity(
															IMRelationSelectActivity.this,
															chatDetail,
															mShareInfo);
											finish();
										}
									});
						} else if (mShareInfo.mType == JTFile.TYPE_REQUIREMENT) { // 需求
							EUtil.showShareDialog(this, mShareInfo.mFileName,
									new CustomDlgClickListener() {
										@Override
										public void onPositiveClick(
												String message) {
											// 分享到私聊
											ENavigate
													.startIMActivity(
															IMRelationSelectActivity.this,
															chatDetail,
															mShareInfo);
											finish();
										}
									});
						} else if (mShareInfo.mType == JTFile.TYPE_CONFERENCE) { // 会议

							// 分享到私聊
							IMBaseMessage shareMsg = new ChatMessage("[会议]");
							shareMsg.setJtFile(mShareInfo);
							shareMsg.setType(IMBaseMessage.TYPE_CONFERENCE);
							ENavigate.startIMActivity(
									IMRelationSelectActivity.this, chatDetail,
									mShareInfo);
							finish();
						} else if (mShareInfo.mType == JTFile.TYPE_JTCONTACT_OFFLINE
								|| mShareInfo.mType == JTFile.TYPE_JTCONTACT_ONLINE
								|| mShareInfo.mType == JTFile.TYPE_ORG_ONLINE
								|| mShareInfo.mType == JTFile.TYPE_ORG_OFFLINE) {
							// 分享到私聊
							IMBaseMessage shareMsg = new ChatMessage(mMessage);
							shareMsg.setJtFile(mShareInfo);
							if (mShareInfo.mType == JTFile.TYPE_JTCONTACT_OFFLINE) {
								ENavigate.startIMCreateGroupCategoryActivity1(
										this, chatDetail, shareMsg);
							} else {
								ENavigate.startIMActivity(this, chatDetail,
										mShareInfo);
							}
							finish();
						} else if (mShareInfo.mType == JTFile.TYPE_TEXT) { // 分享文本
							ENavigate.startIMActivity(
									IMRelationSelectActivity.this, chatDetail,
									mShareInfo);
							finish();
						} else if (mShareInfo.mType == JTFile.TYPE_IMAGE
								|| mShareInfo.mType == JTFile.TYPE_VIDEO
								|| mShareInfo.mType == JTFile.TYPE_FILE
								|| mShareInfo.mType == JTFile.TYPE_AUDIO) { // 分享图片、视频、文件、语音
							ENavigate.startIMActivity(
									IMRelationSelectActivity.this, chatDetail,
									mShareInfo);
							finish();
						}
					} else if (mShareInfoList != null) { // 分享多个项目
						ENavigate.startIMActivity(
								IMRelationSelectActivity.this, chatDetail,
								mShareInfoList);
						finish();
					} else {
						if (isShareFile) {
							ENavigate.startIMActivity(this, chatDetail,
									listJtFile);
						} else {
							ENavigate.startIMActivity(this, chatDetail);
						}
						finish();
					}
				}
			}
			// 从群聊分享关系过来
			else if (fromActivityName
					.equalsIgnoreCase(ENavConsts.EShareCnsSelectActivity)) {
				Connections con = this.selectGroupCategorys.get(0);
				requestConnectionDetail(con.getId(), con.isOnline(), con.type,
						IMRelationSelectActivity.this);
			} else if (fromActivityName
					.equalsIgnoreCase(ENavConsts.ECommunityChatSettingActivity)) {
				if (mucDetail != null) { // 在已有群聊中加人
					int applyType = getIntent().getIntExtra("applyType", 1);
					if(applyType == 2){
						invite2CommunityMUC();
					}else{
						List<Long> listID = new ArrayList<Long>();
						for (Connections con : this.selectGroupCategorys) {
							listID.add(Long.valueOf(con.getId()));
						}
						showLoadingDialog();
						CommunityReqUtil.doInvite2Muc(this, mucDetail.getId(), listID, this, null);
					}
				}
			} else {
				doFinish();
			}
		}
			break;

		}
		return true;
	}

	public void requestConnectionDetail(String id, boolean isonline, int type,
			Activity a) {
		showLoadingDialog();
		if (type == Connections.type_org) {
			// JSONObject jb = ConnectionsReqUtil.getOrganizationDetailJson(id,
			// isonline);
			// ConnectionsReqUtil.dogetOrganizationDetail(a, this, jb, null);
			requestOrgDetail(id, isonline);
		} else {
			// JSONObject jb = ConnectionsReqUtil.getContactDetailJson(id,
			// isonline);
			// ConnectionsReqUtil.doContactDetail(a, this, jb, null);
			PeopleDetialsIncomingParameters parameters = new PeopleDetialsIncomingParameters();
			if (!StringUtils.isEmpty(id)) {
				parameters.id = Long.parseLong(id);
			}
			if (isonline) {
				parameters.personType = 1;
			} else {
				parameters.personType = 2;
			}
			parameters.view = 0;
			PeopleReqUtil.doRequestWebAPI(IMRelationSelectActivity.this, this,
					parameters, null, PeopleRequestType.PEOPLE_REQ_GETPEOPLE);
		}
	}

	/**
	 * org detail
	 */
	public void requestOrgDetail(String userId, boolean isonline) {
		// 参数二：IbindData的实现类
		// 组织详情页面4.4 组织详情 /org/orgAndProInfo
		// 组织好友 userId查询
		if (isonline) {
			//
			OrganizationUserDetailIncomingParameters orgParameters = new OrganizationUserDetailIncomingParameters();
			// orgParameters.orgId = 2763;
			// orgParameters.orgId = 24;
			if (!StringUtils.isEmpty(userId)) {
				orgParameters.userId = Long.parseLong(userId);
			}
			OrganizationReqUtil.doRequestWebAPI(this, this, orgParameters,
					null, OrganizationReqType.ORGANIZATION_REQ_ORGANDPROINFO);
		}
		// 客户
		else {
			OrganizationDetialsIncomingParameters orgParameters = new OrganizationDetialsIncomingParameters();
			if (!StringUtils.isEmpty(userId)) {
				orgParameters.orgId = Long.parseLong(userId);
			}
			OrganizationReqUtil.doRequestWebAPI(this, this, orgParameters,
					null, OrganizationReqType.ORGANIZATION_REQ_ORGANDPROINFO);
		}
	}

	// 邀请人进入群聊
	public void invite2MUC() {
		List<String> arr = new ArrayList<String>();
		for (Connections con : this.selectGroupCategorys) {
			arr.add("" + con.getId());
		}
		boolean ret = IMReqUtil.invite2MUC(
				App.getApp().getApplicationContext(), this, null, arr,
				mucDetail.getId());
		if (ret) {
			this.showLoadingDialog("正在邀请,请稍候...");
		} else {
			showToast("加人失败");
		}
	}

	// 邀请人进入群聊
	public void invite2CommunityMUC() {
		ImMucinfo community = (ImMucinfo) getIntent().getSerializableExtra("community");
		List<CommunityNotify> notifys = new ArrayList<CommunityNotify>();
		for (Connections con : this.selectGroupCategorys) {
			CommunityNotify notify = new CommunityNotify();
			notify.setCommunityId(community.getId());
			notify.setCommunityLogo(community.getPicPath());
			notify.setCommunityName(community.getTitle());
			notify.setApplicantId(Long.valueOf(con.getId()));
			notify.setApplicantName(con.getName());
			notify.setUserLogo(con.getImage());
			notify.setAttendType(0);
			notify.setAcceptStatus(0);
			notify.setApplyReason("邀请加入" + mucDetail.getTitle());
			notify.setNoticeType(0);
			notify.setCreatedUserId(Long.valueOf(App.getUserID()));
			notify.setCreatedUserLogo(App.getUserAvatar());
			notify.setCreatedUserName(App.getNick());
			notify.setCreatedTime(new Date().getTime());
			notify.setApplicantReadStatus(0);
			notify.setOwnerReadStatus(1);
			notifys.add(notify);
		}
		boolean ret = CommunityReqUtil.createNotices(this, this, notifys, null);
		if (ret) {
			this.showLoadingDialog("正在发送邀请,请稍候...");
		} else {
			showToast("加人失败");
		}

	}

	/**
	 * 创建群聊
	 * 
	 * @param param
	 */
	public void createMUC(ConnectionsMini param) {
		List<ConnectionsMini> arr = new ArrayList<ConnectionsMini>();
		arr.add(param);
		for (Connections con : selectGroupCategorys) {
			ConnectionsMini mini = new ConnectionsMini();
			mini.setId("" + con.getId());
			mini.setName(con.getName());
			mini.setType(con.getmType());
			mini.setImage(con.getImage());
			arr.add(mini);
		}
		boolean ret = IMReqUtil.createMUC(this, this, null, arr);
		if (ret) {
			showLoadingDialog();
		} else {
			showToast("创建畅聊失败");
		}
	}

	// 创建群聊
	public void createMUC() {
		List<ConnectionsMini> arr = new ArrayList<ConnectionsMini>();
		for (Connections con : this.selectGroupCategorys) {
			ConnectionsMini mini = new ConnectionsMini();
			mini.setId("" + con.getId());
			mini.setName(con.getName());
			mini.setType(con.getmType());
			mini.setImage(con.getImage());
			arr.add(mini);
		}
		boolean ret = IMReqUtil.createMUC(App.getApp().getApplicationContext(),
				this, null, arr);
		if (ret) {
			showLoadingDialog("正在创建畅聊,请稍候...");
		} else {
			showToast("创建畅聊失败");
		}
	}

	// 创建会议
	public void createConference() {
		boolean ret = IMReqUtil.createConference(App.getApp()
				.getApplicationContext(), this, null, mucDetail);
		if (ret) {
			this.showLoadingDialog("正在创建畅聊,请稍候...");
		} else {
			showToast("创建畅聊失败");
		}
	}

	private void findView() {
		lvContact = (ExpandableListView) this.findViewById(R.id.lvContact);
		lvContact.setGroupIndicator(null);
		contactAdapter = new ContactAdapter(this);
		lvContact.setAdapter(contactAdapter);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
						| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);

		lvContact.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {

				Connections connectionsTag = (Connections) v.getTag();
				CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkbox);
				setCheckBox(connectionsTag, checkBox);
				
				if (fromActivityName.equalsIgnoreCase(ENavConsts.EShareCnsSelectActivity)) {
					Connections con = selectGroupCategorys.get(0);
					requestConnectionDetail(con.getId(), con.isOnline(), con.type,
							IMRelationSelectActivity.this);
				}
				return false;
			}
		});
		// 选择已有群
		selecteGroupView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				if (mShareInfo != null) {
					ENavigate.startGroupListActivity(
							IMRelationSelectActivity.this,
							ENavConsts.EShareActivity, mShareInfo);
				} else if (mShareInfoList != null) {
					ENavigate.startGroupListActivity(
							IMRelationSelectActivity.this,
							ENavConsts.EShareActivity, mShareInfoList);
				}
			}
		});
	}

	public class Watcher implements TextWatcher {
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
			if (mFilterType == FT_OTHER) {
				if (cnsCacheData != null) {
					cnsCacheData.setKeyword(editText.getText().toString());
					if (cnsCacheData.size() == 0) {
						showToast("数据为空");
					}
					cnsCacheData.init();
					contactAdapter.notifyDataSetChanged();
				}
			} else {
				if (iMGroupCategoryslist == null
						|| iMGroupCategoryslist.size() == 0) {
					showToast("数据为空");
				} else {
					Filterable contactAdapter = (Filterable) lvContact
							.getAdapter();
					contactAdapter.getFilter().filter(
							editText.getText().toString());
				}
			}

		}
	}

	//
	// public void initdata(ContactAdapter contactAdapter){
	// ArrayList<ArrayList<Connections>> iMGroupCategoryslist=new
	// ArrayList<ArrayList<Connections>>();
	// ArrayList<Connections> iMGrouplist=new ArrayList<Connections>();
	// for(int i=0;i<5;i++){
	// ArrayList<Connections> arrs=new ArrayList<Connections>();
	// for(int j=0;j<5;j++){
	// Connections con=new Connections();
	// con.mID=1;
	// con.jtContactMini.mName="abc"+j;
	// con.mType=Connections.type_persion;
	// con.mSourceFrom="ok le";
	// if(i==0&&j==0){
	// con.jtContactMini.mName="ad"+j;
	// }else if(i==0&&j==1){
	// con.jtContactMini.mName="1eee"+j;
	// }
	// arrs.add(con);
	// }
	// iMGroupCategoryslist.add(arrs);
	// }
	//
	// for(int i=0;i<5;i++){
	// Connections con=new Connections();
	// con.mID=1;
	// con.mType=Connections.type_org;
	// con.organizationMini.mFullName="组织"+i;
	// con.mSourceFrom="ok le";
	// iMGrouplist.add(con);
	// }
	// contactAdapter.setDatas(iMGroupCategoryslist, iMGrouplist);
	// }
	//

	private void setCheckBox(Connections connections, CheckBox checkBox) {
		if (isSingle) {
			if (selectGroupCategorys.contains(connections)) {
				selectGroupCategorys.remove(connections);
				checkBox.setChecked(false);
			} else {
				if (selectGroupCategorys.size() > 0) {
					selectGroupCategorys.clear();
				}
				selectGroupCategorys.add(connections);
				checkBox.setChecked(true);
			}
			contactAdapter.notifyDataSetChanged();
			((SelectAdapter) horizontalListView.getAdapter())
					.notifyDataSetChanged();
			if (selectGroupCategorys.size() == 0) {
				horizontalListView.setVisibility(View.VISIBLE);
			} else {
				horizontalListView
						.setSelection(selectGroupCategorys.size() - 1);
				horizontalListView.setVisibility(View.VISIBLE);
			}
		} else {
			if (selectGroupCategorys.contains(connections)) {
				selectGroupCategorys.remove(connections);
				checkBox.setChecked(false);
			} else {
				selectGroupCategorys.add(connections);
				checkBox.setChecked(true);
			}
			contactAdapter.notifyDataSetChanged();
			((SelectAdapter) horizontalListView.getAdapter())
					.notifyDataSetChanged();
			if (selectGroupCategorys.size() == 0) {
				horizontalListView.setVisibility(View.VISIBLE);
			} else {
				horizontalListView
						.setSelection(selectGroupCategorys.size() - 1);
				horizontalListView.setVisibility(View.VISIBLE);
			}
			confirm.setTitle("确定(" + selectGroupCategorys.size() + ")");
		}
	}

	/** 选中的显示类 */
	public class SelectAdapter extends BaseAdapter {
		Context mContext = null;

		public SelectAdapter(Context context) {
			mContext = context;
			// iMGroupCategorys.add(new IMGroupCategory("334", "String 1"));
		}

		@Override
		public int getCount() {
			return selectGroupCategorys.size();

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
			final Connections connections = selectGroupCategorys.get(position);
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.im_relationcontactselect_itemed, null);
				convertView.setTag(connections);
			} else {
				convertView.setTag(connections);
			}
			ImageView avatar_iv = (ImageView) convertView
					.findViewById(R.id.avatar_iv);
			Util.initAvatarImage(mContext, avatar_iv, connections.getName(),
					connections.getImage(), 0,
					Integer.valueOf(connections.getType()));
			return convertView;
		}
	}

	class ContactAdapter extends BaseExpandableListAdapter implements
			Filterable {
		private Context mContext;
		ArrayList<ArrayList<Connections>> iMGroupCategoryslist;
		ArrayList<Connections> iMGrouplist;

		ArrayList<ArrayList<Connections>> showiMGroupCategoryslist = new ArrayList<ArrayList<Connections>>();
		ArrayList<Connections> showiMGrouplist;

		public ContactAdapter(Context mContext) {
			this.mContext = mContext;
		}

		public void cloneData() {
			showiMGroupCategoryslist = new ArrayList<ArrayList<Connections>>();
			if (iMGroupCategoryslist != null) {
				for (int i = 0; i < iMGroupCategoryslist.size(); i++) {
					ArrayList<Connections> datas = iMGroupCategoryslist.get(i);
					ArrayList<Connections> arrs = new ArrayList<Connections>();
					if (datas != null) {
						for (Connections connections : datas) {
							arrs.add(connections);
						}
					}
					showiMGroupCategoryslist.add(arrs);
				}
			}

			showiMGrouplist = new ArrayList<Connections>();
			if (iMGrouplist != null) {
				for (int i = 0; i < iMGrouplist.size(); i++) {
					showiMGrouplist.add(iMGrouplist.get(i));
				}
			}
		}

		public void setDatas(
				ArrayList<ArrayList<Connections>> iMGroupCategoryslist,
				ArrayList<Connections> iMGrouplist) {
			this.iMGroupCategoryslist = iMGroupCategoryslist;
			this.iMGrouplist = iMGrouplist;
			if (iMGrouplist != null && iMGrouplist.size() != 0
					&& lvContact != null) {
				int size = iMGrouplist.size();
				for (int i = 0; i < size; i++) {
					lvContact.expandGroup(i);
				}
			}
			cloneData();
		}

		public boolean isContainer(Connections connections) {
			if (selectGroupCategorys == null
					|| selectGroupCategorys.size() == 0) {
				return false;
			}
			Connections tempConnections = null;
			for (int i = 0; i < selectGroupCategorys.size(); i++) {
				tempConnections = selectGroupCategorys.get(i);
				if (tempConnections.type == connections.type
						&& tempConnections.getId().equals(connections.getId())
						&& tempConnections.isOnline() == connections.isOnline()) {
					return true;
				}
			}
			return false;
		}

		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			String MSG = "getChildView()";
			// Log.i(TAG, MSG);

			Connections tempConnections = null;

			if (mFilterType == FT_OTHER) {
				tempConnections = cnsCacheData.get(childPosition);
			} else {
				tempConnections = showiMGroupCategoryslist.get(groupPosition)
						.get(childPosition);
			}

			final Connections connections = tempConnections;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.im_relationcontactselect_item, null);
				convertView.setTag(connections);
			} else {
				convertView.setTag(connections);
			}

			TextView catalogTv = (TextView) convertView
					.findViewById(R.id.contactitem_catalog);// 拼音索引目录标识
			LinearLayout catalogll = (LinearLayout) convertView
					.findViewById(R.id.catalog_ll);// 拼音索引目录标识

			ImageView ivAvatar = (ImageView) convertView
					.findViewById(R.id.contactitem_avatar_iv);// 头像
			TextView tvNick = (TextView) convertView
					.findViewById(R.id.imcontactname);// name
			TextView tvContent = (TextView) convertView
					.findViewById(R.id.imcontactcomefrom);// 内容
			final CheckBox checkBox = (CheckBox) convertView
					.findViewById(R.id.checkbox);// 内容
			View top_line = convertView.findViewById(R.id.top_line);
			View bottom_line = convertView.findViewById(R.id.bottom_line);
			if (isSingle)
				checkBox.setVisibility(View.GONE);

			/***** 拼音索引目录标识 start *******/
			String lastCatalog = null;
			String catalog = "";
			if (childPosition == 0) {
				catalogll.setVisibility(View.GONE);
				top_line.setVisibility(View.GONE);
				bottom_line.setVisibility(View.GONE);
			} else if (childPosition != 0
					&& !StringUtils.isEmpty(connections.getName())) {
				if (connections.type == Connections.type_org) {
					catalogll.setVisibility(View.GONE);
					bottom_line.setVisibility(View.GONE);
				} else {
					catalogll.setVisibility(View.VISIBLE);
				}
				catalog = connections.getCharName() + "";
				// lastCatalog = connectionsCacheData.get(arg1 -
				// 2).getCharName() + "";

				if (mFilterType == FT_OTHER) {
					lastCatalog = cnsCacheData.get(childPosition - 1)
							.getCharName() + "";
				} else {
					lastCatalog = showiMGroupCategoryslist.get(groupPosition)
							.get(childPosition - 1).getCharName()
							+ "";
				}

				if (catalog.equals(lastCatalog)) {
					catalogll.setVisibility(View.GONE);
					top_line.setVisibility(View.VISIBLE);
				} else {
					catalogll.setVisibility(View.VISIBLE);
					top_line.setVisibility(View.GONE);
					catalogTv.setText(catalog);
				}

			} else {
				catalogTv.setVisibility(View.GONE);
			}
			/***** 拼音索引目录标识 end *******/

			// if(selectGroupCategorys.contains(connections)){
			if (isContainer(connections)) {
				checkBox.setChecked(true);
			} else {
				checkBox.setChecked(false);
			}

			checkBox.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					setCheckBox(connections, checkBox);
				}
			});

			// ivAvatar.setImageResource(R.drawable.im_relationcontactlist_dficon);
			tvNick.setText(connections.getName());
			tvContent.setText(connections.sourceFrom);
			Util.initAvatarImage(mContext, ivAvatar, connections.getName(),
					connections.getImage(), 0,
					Integer.valueOf(connections.getType()));
			return convertView;
		}

		@Override
		public Object getChild(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getChildId(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int getChildrenCount(int arg0) {
			if (mFilterType == FT_OTHER) {
				if (cnsCacheData != null) {
					return cnsCacheData.size();
				}
			} else {
				if (showiMGroupCategoryslist != null
						&& showiMGroupCategoryslist.size() != 0) {
					return showiMGroupCategoryslist.get(arg0).size();
				}
			}
			return 0;
		}

		@Override
		public Object getGroup(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			if (showiMGrouplist != null && showiMGrouplist.size() != 0) {
				return showiMGrouplist.size();
			}
			return 0;
			// return showiMGroupCategoryslist.size();
		}

		@Override
		public long getGroupId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getGroupView(final int groupPosition, boolean isExpanded,
				View convertView, ViewGroup arg3) {
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.im_relationcontactselect_group, null);
			}
			final Connections conn = showiMGrouplist.get(groupPosition);
			String groupName = conn.getName();

			if (!StringUtils.isEmpty(groupName)
					&& showiMGroupCategoryslist.size() > 0) {
				if (showiMGroupCategoryslist.get(groupPosition) != null
						&& showiMGroupCategoryslist.get(groupPosition).size() > 0
						&& showiMGroupCategoryslist.get(groupPosition).get(0) != null) {
					if (groupName.equals("我的好友")) {
						groupName = groupName
								+ " "
								+ showiMGroupCategoryslist.get(groupPosition)
										.get(0).getCharName();
					}
				}
			}
			((TextView) convertView.findViewById(R.id.text)).setText(groupName);
			final CheckBox cb = (CheckBox) convertView
					.findViewById(R.id.all_check);
			int count = 0;
			for (Connections temp : selectGroupCategorys) {
				if (conn.getType().endsWith(temp.getType())) {
					count++;
				}
			}

			if (isSingle) {
				cb.setVisibility(View.GONE);
			} else {
				cb.setVisibility(View.VISIBLE);
			}
			if (showiMGroupCategoryslist.size() > 0) {
				if (count == showiMGroupCategoryslist.get(groupPosition).size()) {
					cb.setChecked(true);
				} else {
					cb.setChecked(false);
				}
			}

			cb.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!cb.isChecked()) {
						if (conn.getType().equals(Connections.type_org + "")) {
							for (int i = 0, len = selectGroupCategorys.size(); i < len; ++i) {
								if (selectGroupCategorys.get(i).getType()
										.equals(Connections.type_org + "")) {
									selectGroupCategorys.remove(i);
									--len;// 减少一个
									--i;// 多谢deny_guoshou指正，如果不加会出现评论1楼所说的情况。
								}
							}
						} else if (conn.getType().equals(
								Connections.type_persion + "")) {
							for (int i = 0, len = selectGroupCategorys.size(); i < len; ++i) {
								if (selectGroupCategorys.get(i).getType()
										.equals(Connections.type_persion + "")) {
									selectGroupCategorys.remove(i);
									--len;// 减少一个
									--i;// 多谢deny_guoshou指正，如果不加会出现评论1楼所说的情况。
								}
							}
						}
					} else {
						for (Connections conn : showiMGroupCategoryslist
								.get(groupPosition)) {
							if (!selectGroupCategorys.contains(conn)) {
								selectGroupCategorys.add(conn);
							}
						}
					}
					confirm.setTitle("确定(" + selectGroupCategorys.size() + ")");
					notifyDataSetChanged();
					((SelectAdapter) horizontalListView.getAdapter())
							.notifyDataSetChanged();
					horizontalListView.setVisibility(View.VISIBLE);
					if (selectGroupCategorys.size() >= 0) {
						horizontalListView.setSelection(selectGroupCategorys
								.size() - 1);
					}
				}
			});

			if (mFilterType == FT_OTHER) {
				if (isExpanded) {
					if (cnsCacheData != null) {
						if (cnsCacheData.size() != 0) {
							convertView.findViewById(R.id.end).setVisibility(
									View.GONE);
						}
					}
				} else {
					convertView.findViewById(R.id.end).setVisibility(
							View.VISIBLE);
				}
			} else {
				if (isExpanded) {
					if (showiMGroupCategoryslist != null
							&& showiMGroupCategoryslist.size() != 0
							&& showiMGroupCategoryslist.get(groupPosition)
									.size() != 0) {
						convertView.findViewById(R.id.end).setVisibility(
								View.GONE);
					}
				} else {
					convertView.findViewById(R.id.end).setVisibility(
							View.VISIBLE);
				}
			}
			convertView.setClickable(true);
			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isChildSelectable(int arg0, int arg1) {
			// TODO Auto-generated method stub
			return true;
		}

		// boolean isExpandGroup = flase;
		boolean isExpandGroup = true;
		Filter newFilter;

		@Override
		public Filter getFilter() {
			String MSG = "getFilter()";
			// Log.i(TAG, MSG);

			if (newFilter != null) {
				return newFilter;
			}
			newFilter = new Filter() {
				@Override
				protected void publishResults(CharSequence constraint,
						FilterResults results) {
					String MSG = "publishResults()";
					Log.i(TAG, MSG);
					lvContact.collapseGroup(0);
					lvContact.collapseGroup(1);

					ArrayList arrayList = (ArrayList) results.values;
					showiMGroupCategoryslist = (ArrayList<ArrayList<Connections>>) arrayList
							.get(0);
					showiMGrouplist = (ArrayList<Connections>) arrayList.get(1);
					notifyDataSetChanged();
					if (isExpandGroup) {
						if (getGroupCount() > 1) {
							lvContact.expandGroup(0);
							lvContact.expandGroup(1);
						} else {
							lvContact.expandGroup(0);
						}
					}
					isExpandGroup = false;
					// isExpandGroup = true;
				}

				@Override
				protected FilterResults performFiltering(CharSequence prefix) {
					String MSG = "performFiltering()";
					// Log.i(TAG, MSG);

					FilterResults results = new FilterResults();
					// if(showiMGrouplist!=null&showiMGrouplist.size()!=0){
					// for(int i=0;i<showiMGrouplist.size();i++){
					// lvContact.collapseGroup(i);
					// }
					// }

					if (prefix != null && prefix.toString().length() > 0) {
						ArrayList<ArrayList<Connections>> tempShowiMGroupCategoryslist = new ArrayList<ArrayList<Connections>>();
						ArrayList<Connections> tempShowiMGrouplist = new ArrayList<Connections>();

						for (Connections connections : iMGrouplist) {
							tempShowiMGrouplist.add(connections);
						}

						String strData = null;
						ArrayList<Integer> deleteIndex = new ArrayList<Integer>();
						for (int i = 0; i < iMGroupCategoryslist.size(); i++) {
							ArrayList<Connections> datas = iMGroupCategoryslist
									.get(i);
							ArrayList<Connections> arr = new ArrayList<Connections>();
							for (Connections connections : datas) {
								strData = connections.getName();
								if (strData.contains(prefix)) {
									arr.add(connections);
								}
							}
							if (arr.size() == 0) {
								deleteIndex.add(i);
								// tempShowiMGrouplist.remove(i);
							} else {
								tempShowiMGroupCategoryslist.add(arr);
							}
						}
						for (int i = deleteIndex.size() - 1; i >= 0; i--) {
							int tempInt = deleteIndex.get(i);
							tempShowiMGrouplist.remove(tempInt);
						}

						ArrayList resultlist = new ArrayList();
						resultlist.add(tempShowiMGroupCategoryslist);
						resultlist.add(tempShowiMGrouplist);
						isExpandGroup = true;
						results.values = resultlist;
						results.count = 2;
					} else {
						synchronized (iMGroupCategoryslist) {
							// isExpandGroup = false;
							isExpandGroup = true;
							ArrayList<ArrayList<Connections>> tempShowiMGroupCategoryslist = new ArrayList<ArrayList<Connections>>();
							ArrayList<Connections> tempShowiMGrouplist = new ArrayList<Connections>();
							for (Connections connections : iMGrouplist) {
								tempShowiMGrouplist.add(connections);
							}
							for (int i = 0; i < iMGroupCategoryslist.size(); i++) {
								ArrayList<Connections> datas = iMGroupCategoryslist
										.get(i);
								ArrayList<Connections> arr = new ArrayList<Connections>();
								for (Connections connections : datas) {
									arr.add(connections);
								}
								tempShowiMGroupCategoryslist.add(arr);
							}
							ArrayList resultlist = new ArrayList();
							resultlist.add(tempShowiMGroupCategoryslist);
							resultlist.add(tempShowiMGrouplist);
							results.values = resultlist;
							results.count = 2;
						}
					}
					return results;
				}

			};
			return newFilter;
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void bindData(int tag, Object object) {
		if (!isLoadingDialogShowing()) {
			return;
		}
		if (tag != EAPIConsts.ReqType.GET_TREATED_HTML) {
			dismissLoadingDialog();
		}
		if (tag == EAPIConsts.IMReqType.IM_REQ_CREATE_MUC) { // 创建群聊
			if (null == object) {
				return;
			}
			final MCreateMUC muc = (MCreateMUC) object;
			if (muc.getMucDetail() != null) {
				if (fromActivityName
						.equalsIgnoreCase(ENavConsts.EIMCreateGroupActivity)) {
					// 创建一个新的会议
					// showToast("会议创建成功");
					Intent intent = new Intent();
					intent.putExtra(ENavConsts.EMucDetail, muc.getMucDetail());
					setResult(RESULT_OK, intent);
					finish();
				} else if (fromActivityName
						.equalsIgnoreCase(ENavConsts.EMainActivity)) {
					// 创建一个新的群聊
					finish();
					ENavigate.startIMGroupActivity(this, muc.getMucDetail());
				} else if (fromActivityName
						.equalsIgnoreCase(ENavConsts.EIMEditMemberActivity)) {
					// 从私聊-畅聊设置页-创建一个新的群聊，成功后返回畅谈设置页
					Intent intent = new Intent();
					intent.putExtra(ENavConsts.EMucDetail, muc.getMucDetail());
					setResult(RESULT_OK, intent);
					finish();
				} else if (fromActivityName
						.equalsIgnoreCase(com.tr.ui.share.ShareActivity.class
								.getSimpleName())
						|| fromActivityName
								.equalsIgnoreCase(SocialShareActivity.class
										.getSimpleName())) { // 分享

					if (mShareInfo != null) {
						if (mShareInfo.mType == JTFile.TYPE_JTCONTACT_OFFLINE
								|| mShareInfo.mType == JTFile.TYPE_JTCONTACT_ONLINE
								|| mShareInfo.mType == JTFile.TYPE_ORG_ONLINE
								|| mShareInfo.mType == JTFile.TYPE_ORG_OFFLINE) { // 关系
							IMBaseMessage shareMsg = new MUCMessage(mMessage);
							shareMsg.setJtFile(mShareInfo);
							if (mShareInfo.mType == JTFile.TYPE_JTCONTACT_OFFLINE) {
								ENavigate.startIMCreateGroupCategoryActivity1(
										this, muc.getMucDetail().getId() + "",
										shareMsg);
							} else {
								ENavigate.startIMGroupActivity(this,
										muc.getMucDetail(), shareMsg);
							}
							finish();
						} else if (mShareInfo.mType == JTFile.TYPE_REQUIREMENT) { // 需求
							EUtil.showShareDialog(this, mShareInfo.mFileName,
									new CustomDlgClickListener() {
										@Override
										public void onPositiveClick(
												String message) {
											IMBaseMessage shareMsg = new MUCMessage(
													mMessage);
											shareMsg.setContent(message);
											shareMsg.setJtFile(mShareInfo);
											shareMsg.setType(IMBaseMessage.TYPE_REQUIREMENT);
											ENavigate
													.startIMGroupActivity(
															IMRelationSelectActivity.this,
															muc.getMucDetail(),
															shareMsg);
											finish();
										}
									});
						} else if (mShareInfo.mType == JTFile.TYPE_KNOWLEDGE) { // 知识
							EUtil.showShareDialog(this, mShareInfo.mSuffixName,
									new CustomDlgClickListener() {
										@Override
										public void onPositiveClick(
												String message) {
											mShareInfo.mFileName = message;
											mMucDetail = muc.getMucDetail();
											showLoadingDialog();
											// 直接分享到群聊
											IMBaseMessage shareMsg = new MUCMessage(
													message);
											shareMsg.setJtFile(mShareInfo);
											shareMsg.setType(IMBaseMessage.TYPE_KNOWLEDGE);
											ENavigate
													.startIMGroupActivity(
															IMRelationSelectActivity.this,
															muc.getMucDetail(),
															shareMsg);
											finish();

										}
									});
						} else if (mShareInfo.mType == JTFile.TYPE_TEXT) { // 文本
							IMBaseMessage shareMsg = new MUCMessage(
									mShareInfo.mFileName);
							shareMsg.setJtFile(mShareInfo);
							ENavigate.startIMGroupActivity(
									IMRelationSelectActivity.this,
									muc.getMucDetail(), shareMsg);
							finish();
						} else if (mShareInfo.mType == JTFile.TYPE_KNOWLEDGE2) { // 新知识
							EUtil.showShareDialog(this, mShareInfo.reserved2,
									new CustomDlgClickListener() {
										@Override
										public void onPositiveClick(
												String message) {
											mShareInfo.mFileName = message;
											mMucDetail = muc.getMucDetail();
											showLoadingDialog();
											// 分享到群聊
											IMBaseMessage shareMsg = new MUCMessage(
													message);
											shareMsg.setJtFile(mShareInfo);
											shareMsg.setType(IMBaseMessage.TYPE_KNOWLEDGE2);
											ENavigate
													.startIMGroupActivity(
															IMRelationSelectActivity.this,
															muc.getMucDetail(),
															shareMsg);
											finish();
										}
									});
						} else if (mShareInfo.mType == JTFile.TYPE_CONFERENCE) { // 会议

							// 分享到群聊
							IMBaseMessage shareMsg = new MUCMessage("[会议]");
							shareMsg.setJtFile(mShareInfo);
							shareMsg.setType(IMBaseMessage.TYPE_CONFERENCE);
							ENavigate.startIMGroupActivity(
									IMRelationSelectActivity.this,
									muc.getMucDetail(), shareMsg);
							finish();
						} else if (mShareInfo.mType == JTFile.TYPE_IMAGE
								|| mShareInfo.mType == JTFile.TYPE_FILE
								|| mShareInfo.mType == JTFile.TYPE_AUDIO
								|| mShareInfo.mType == JTFile.TYPE_VIDEO) { // 分析图片、视频、语音、文件

							IMBaseMessage shareMsg = new MUCMessage("");
							shareMsg.setJtFile(mShareInfo);
							shareMsg.setType(mShareInfo);
							ENavigate.startIMGroupActivity(
									IMRelationSelectActivity.this,
									muc.getMucDetail(), shareMsg);
							finish();
						}
					} else if (mShareInfoList != null) { // 分享多个项目

						ArrayList<IMBaseMessage> shareMsgList = new ArrayList<IMBaseMessage>();
						for (JTFile jtFile : mShareInfoList) {
							IMBaseMessage shareMsg = new MUCMessage("");
							shareMsg.setJtFile(jtFile);
							shareMsg.setType(jtFile);
							shareMsgList.add(shareMsg);
						}
						// ENavigate.startIMGroupActivity(IMRelationSelectActivity.this,
						// muc.getMucDetail(), shareMsgList);
						finish();
					} else {
						if (isShareFile) {
							ENavigate.startIMGroupActivity(this, muc
									.getMucDetail().getId() + "", listJtFile);
						} else {
							ENavigate.startIMGroupActivity(this,
									muc.getMucDetail());
						}
						finish();
					}
				}
			}
		} else if (tag == EAPIConsts.IMReqType.IM_REQ_INVITE2MUC) {
			if (null == object) {
				return;
			}
			mucDetail = (MUCDetail) object;
			if (fromActivityName
					.equalsIgnoreCase(ENavConsts.EIMEditMemberActivity)
					|| fromActivityName
							.equalsIgnoreCase(ENavConsts.ECommunityChatSettingActivity)) { // 在已有会议中邀请人
				Intent intent = new Intent();
				intent.putExtra(ENavConsts.EMucDetail, mucDetail);
				setResult(RESULT_OK, intent);
				finish();
			}
		} else if (tag == EAPIConsts.ReqType.GET_TREATED_HTML) { // 大数据解析

			if (object != null) {
				DataBox dataBox = (DataBox) object;
				if (dataBox.mTreatedHtml != null) {
					mShareInfo.mSuffixName = dataBox.mTreatedHtml.getTitle();
				}
				if (furtherHandle) {
					if (selectGroupCategorys.size() > 1) { // 分享到群聊
						IMBaseMessage shareMsg = new MUCMessage(
								mShareInfo.mFileName);
						shareMsg.setJtFile(mShareInfo);
						shareMsg.setType(IMBaseMessage.TYPE_KNOWLEDGE);
						ENavigate.startIMGroupActivity(this, mMucDetail,
								shareMsg);
						finish();
					} else { // 分享到私聊
						IMBaseMessage shareMsg = new ChatMessage(
								mShareInfo.mFileName);
						shareMsg.setJtFile(mShareInfo);
						shareMsg.setType(IMBaseMessage.TYPE_KNOWLEDGE);
						// ENavigate.startIMActivity(this, mChatDetail,
						// shareMsg);
						finish();
					}
				} else {
					dismissLoadingDialog();
				}
			} else {
				dismissLoadingDialog();
			}
		} else if (EAPIConsts.concReqType.im_getWorkmate == tag
				|| EAPIConsts.concReqType.im_relevantPeopleAndCustomer == tag) {
			if (object != null) {
				ArrayList arr1 = (ArrayList) object;
				ArrayList arr2 = (ArrayList) arr1.get(0);
				ArrayList arr3 = (ArrayList) arr1.get(1);
				iMGroupCategorys = (ArrayList<Connections>) arr2;
				iMGroupCategoryslist = (ArrayList<ArrayList<Connections>>) arr3;
				initInData();
				contactAdapter.setDatas(iMGroupCategoryslist, iMGroupCategorys);
				contactAdapter.notifyDataSetChanged();
			} else {
				showToast("获取数据错误");
			}
		} else if (tag == OrganizationReqType.ORGANIZATION_REQ_ORGANDPROINFO) {
			if (object == null) {
				showToast("选择人脉失败");
				return;
			}
			Map<String, Object> map = (Map<String, Object>) object;
			// 组织用户的类
			CustomerOrganizatianParams cop = (CustomerOrganizatianParams) map
					.get("customer_organization_params");
			CustomerProfileVo customer = null;
			if (cop != null) {
				customer = cop.customer;
			}

			JTFile jTFile = new JTFile();
			if (customer != null) {
				jTFile.setmUrl(customer.picLogo);
				jTFile.mFileName = customer.shotName;
				jTFile.setmSuffixName(customer.name);
				if (customer.industrys.size() != 0) {
					jTFile.setReserved1(customer.industrys.get(0)
							+ ","
							+ customer.industrys.get(customer.industrys.size() - 1));
				}
				jTFile.mModuleType = 10;
				jTFile.mFileSize = 0;
				// ;//是否是组织 0 客户 1 用户注册组织 2 未注册的组织
				if (customer.virtual.equals("0")) {
					jTFile.setmType(jTFile.TYPE_CLIENT);
					jTFile.mTaskId = customer.customerId + "";
				} else {
					jTFile.setmType(jTFile.TYPE_ORGANIZATION);
					jTFile.mTaskId = customer.userId + "";
				}
				doFinish(jTFile);
			} else {
				Toast.makeText(IMRelationSelectActivity.this, "获取数据失败", 0)
						.show();
			}
		} else if (tag == PeopleRequestType.PEOPLE_REQ_GETPEOPLE) {
			if (object == null) {
				showToast("选择人脉失败");
				return;
			}
			PeopleDetails peopleDetails = (PeopleDetails) object;
			Person person = peopleDetails.people;
			JTFile jtFile = new JTFile();
			// 用户
			if (peopleDetails.type >= 7 && peopleDetails.type <= 10) {
				jtFile.mType = JTFile.TYPE_JTCONTACT_ONLINE;
				jtFile.mTaskId = person.createUserId + "";
			} else {
				jtFile.mTaskId = person.id + "";
				jtFile.mType = JTFile.TYPE_JTCONTACT_OFFLINE;
			}

			if(TextUtils.isEmpty(person.peopleNameList.get(0).lastname)){
				jtFile.fileName = person.peopleNameList.get(0).firstname;
			}else {
				jtFile.fileName = person.peopleNameList.get(0).lastname;
			}
//			jtFile.fileName = person.peopleNameList.get(0).lastname
//					+ person.peopleNameList.get(0).firstname;
			jtFile.mSuffixName = person.company;
			jtFile.mUrl = person.portrait;
			jtFile.reserved1 = person.position;
			// ArrayList returndata = (ArrayList) object;
			// JTContact2 onlineTContact = null;
			// JTContact2 offlineTContact = null;
			// for (int i = 0; returndata.size() > i; i = i + 2) {
			// int type = (Integer) returndata.get(i);
			// if (type == 0) {
			// onlineTContact = (JTContact2) returndata.get(i + 1);
			// } else {
			// offlineTContact = (JTContact2) returndata.get(i + 1);
			// }
			// }
			// if (onlineTContact != null) {
			// jtFile = onlineTContact.toJTfile();
			// jtFile.mType = JTFile.TYPE_JTCONTACT_ONLINE;
			// } else if (offlineTContact != null) {
			// jtFile = offlineTContact.toJTfile();
			// jtFile.mType = JTFile.TYPE_JTCONTACT_OFFLINE;
			// } else {
			// showToast("选择人脉失败");
			// }
			doFinish(jtFile);
		} else if (tag == EAPIConsts.concReqType.getAllRelations) {
			orgdatas = (ArrayList<Connections>) object;
			iMGroupCategoryslist.add(orgdatas);
			iMGroupCategoryslist.add(datas);

			if (chatDetail != null) {// 单聊进来移除对方，为解决ANDROID-809
				if (datas.size() > 0) {
					for (Connections conn : datas) {
						if (conn.jtContactMini.getId().equals(
								chatDetail.getThatID())) {
							datas.remove(conn);
							break;
						}
					}
				}
				if (orgdatas.size() > 0) {
					for (Connections conn : orgdatas) {
						if (conn.organizationMini.mID.equals(chatDetail
								.getThatID())) {
							orgdatas.remove(conn);
							break;
						}
					}
				}
			}

			Connections connections = new Connections();
			Connections orgconnections = new Connections();
			OrganizationMini organizationMini = new OrganizationMini();
			if (mFilterType == FT_IM || mFilterType == FT_recommend) {
				connections.jtContactMini.name = "我的好友";
				organizationMini.fullName = "我的组织";
			} else {
				organizationMini.fullName = "所有关系";
			}

			connections.type = Connections.type_persion;
			orgconnections.type = Connections.type_org;
			orgconnections.organizationMini = organizationMini;
			iMGroupCategorys.add(orgconnections);
			iMGroupCategorys.add(connections);

			if (FT_OTHER == mFilterType) {// 如果是全部数据
				if (cnsCacheData == null || cnsCacheData.size() == 0) {
					showLongToast("数据为空");
				} else {
					if (iMGroupCategorys != null
							&& iMGroupCategorys.size() == 1
							&& lvContact != null) {
						lvContact.expandGroup(0);
					}
					initInData();
				}
			} else {
				if (iMGroupCategoryslist == null
						|| iMGroupCategoryslist.size() == 0
						|| iMGroupCategoryslist.get(0) == null
						|| iMGroupCategoryslist.get(0).size() == 0) {
					showLongToast("数据为空");
				} else {
					if (iMGroupCategorys != null
							&& iMGroupCategorys.size() != 0
							&& lvContact != null) {
						int size = iMGroupCategorys.size();
						for (int i = 0; i < size; i++) {
							lvContact.expandGroup(i);
						}
					}
				}
				if (mFilterType == FT_IM || mFilterType == FT_recommend) {
					if (isInitInDataDel) {
						initInDataDel();
					} else {
						initInData();
					}
				} else {
					initInData();
				}
				if (fromActivityName
						.equalsIgnoreCase(ENavConsts.EForwardToFriendActivity)) {
					initInData();
				}
			}
			// // 如果传进来的数据大于0 就复选
			// if (reselectGroupCategorys != null &&
			// reselectGroupCategorys.size() > 0){
			// initInData();
			// }
			contactAdapter.setDatas(iMGroupCategoryslist, iMGroupCategorys);
			contactAdapter.notifyDataSetChanged();
		} else if (tag == EAPIConsts.CommunityReqType.TYPE_CREATE_BATCH_COMMUNITY_NOTICES) {
			if (null != object) {
				HashMap<String, Object> dataMap = (HashMap<String, Object>) object;
				String notifCode = (String) dataMap.get("notifCode");
				if (notifCode.contains("1")) {
					showToast("邀请通知已发送");
					finish();
				} else {
					showToast("邀请失败，请重新邀请");
				}
			} else
				showToast("邀请失败，请重新邀请");
		} else if(tag == EAPIConsts.CommunityReqType.TYPE_INVITE2MUC){
			HashMap<String, Object> dataMap = (HashMap<String, Object>) object;
			if (null != dataMap) {
				MUCDetail mucDetail = (MUCDetail) dataMap.get("mucDetail");
				Intent intent = new Intent();
				intent.putExtra(ENavConsts.EMucDetail, mucDetail);
				intent.putExtra(ENavConsts.redatas, selectGroupCategorys);
				setResult(RESULT_OK, intent);
				finish();
			} else {
				showToast("邀请失败");
			}
		}
	}

	public ActionBar actionbar = null;

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar(), "人脉/好友",
				false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	public void getSimContacts(ArrayList<Connections> connectionsArray) {
		// 获得所有的联系人
		Cursor cur = getContentResolver().query(
				ContactsContract.Contacts.CONTENT_URI,
				null,
				null,
				null,
				ContactsContract.Contacts.DISPLAY_NAME
						+ " COLLATE LOCALIZED ASC");
		// 循环遍历
		if (cur.moveToFirst()) {
			int idColumn = cur.getColumnIndex(ContactsContract.Contacts._ID);
			int displayNameColumn = cur
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
			do {
				String contactId = cur.getString(idColumn);
				String disPlayName = cur.getString(displayNameColumn);

				Connections consact = new Connections();
				consact.type = Connections.type_persion;
				consact.jtContactMini = new JTContactMini();
				consact.jtContactMini.name = disPlayName;

				int phoneCount = cur
						.getInt(cur
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
				if (phoneCount > 0) {
					// 获得联系人的电话号码
					Cursor phones = getContentResolver().query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = " + contactId, null, null);
					if (phones.moveToFirst()) {
						String phoneNumber = null;
						// 各种电话
						Cursor phone = getPhoneByType(
								contactId,
								ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);
						while (phone.moveToNext()) {
							phoneNumber = phone
									.getString(phone
											.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							consact.sourceFrom = phoneNumber;
							break;
						}
						phone.close();
					}
					phones.close();
				}

				connectionsArray.add(consact);
			} while (cur.moveToNext());
		}
	}

	/**
	 * 刷新界面
	 * 
	 * @param connections
	 */
	public void updateUI(Connections connections) {
		String MSG = "updateUI()";
		Log.i(TAG, MSG);

	}

}
