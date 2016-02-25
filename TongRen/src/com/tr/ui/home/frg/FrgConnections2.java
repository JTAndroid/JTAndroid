package com.tr.ui.home.frg;

import java.util.ArrayList;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.common.constvalue.EnumConst.ModuleType;
import com.tr.App;
import com.tr.R;
import com.tr.api.ConnectionsReqUtil;
import com.tr.db.ConnectionsCacheData;
import com.tr.db.ConnectionsDBManager;
import com.tr.db.DBHelper;
import com.tr.model.connections.FriendRequest;
import com.tr.model.im.ChatDetail;
import com.tr.model.knowledge.UserCategory;
import com.tr.model.obj.Connections;
import com.tr.model.obj.MobilePhone;
import com.tr.navigate.ENavigate;
import com.tr.service.GetConnectionsListService;
import com.tr.service.GetConnectionsListService.RequestType;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.conference.square.SquareActivity;
import com.tr.ui.demand.RedactLabelActivity;
import com.tr.ui.home.InviteFriendByQRCodeActivity;
import com.tr.ui.relationship.MyFriendAllActivity;
import com.tr.ui.widgets.CircleImageView;
import com.tr.ui.widgets.ConnsCallAndSendSmsDialog;
import com.tr.ui.widgets.ConnsListDelDialog;
import com.tr.ui.widgets.ConnsListDelDialog.OnSelectListener;
import com.tr.ui.widgets.KnoCategoryAlertDialog.OperType;
import com.tr.ui.widgets.SideBar;
import com.utils.common.EConsts;
import com.utils.common.Util;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.image.LoadImage;
import com.utils.string.StringUtils;
import com.utils.time.TimeUtil;
import com.zxing.android.CaptureActivity;

/**
 * @ClassName: FrgConnections2.java
 * @Description: 通讯录 在原关系列表里改的 里面的列表数据是 我的好友和组织好友
 * @author hanqi
 * @version V1.0
 * @Date 2015-1-15
 */
public class FrgConnections2 extends JBaseFragment implements OnScrollListener {
	public static final String TAG = "FrgConnections2";

	/** 类型 */
	private int type = -1;

	/** 跳全局目录activity请求码 */
	public static final int REQUEST_CODE_CATEGORY_ACTIVITY = 1001;
	/** 跳全局标签activity请求码 */
	public static final int REQUEST_CODE_TAG_ACTIVITY = 1002;

	// 新关系消息和组
	/** all datas */
	public static ConnectionsDBManager connectionsDBManager = null;
	public static ConnectionsCacheData cnsCacheData;
	private ArrayList<Connections> mOrgConnections = new ArrayList<Connections>();
	private Bundle bundle;
	/** 我加入的机构 是否屏开状态 */
	private boolean IsJoinOrgListViewVisibility = true;
	/** 要删除的当前用户或人脉的 id */
	private String delPeopleId;
	/** 当前刷新状态 */
	private boolean isRefreshing = false;

	/** 我的个人好友列表 是否伸展开 */
	private boolean isMyFeopleFriendExtend = true;
	/** 新关系数 */
	private int newConnectionsCount = 0;

	boolean isFirst = true;
	public char[] letters = new char[] { '#', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
			'U', 'V', 'W', 'X', 'Y', 'Z', '*' };

	private XListView lvContact;// 内容list
	private RelativeLayout loadConnectionsWaitView;
	public static ContactAdapter contactAdapter = null;
	private SideBar indexBar;// 拖动的字母bar
	private WindowManager mWindowManager;

	public TextView cnsSizeTagNew;
	public TextView cnsSizeTagGroup;
	/** 搜索 layout */
	private LinearLayout searchLl;
	/** 搜索 输入框 */
	private EditText keywordEt;
	/** 联系人操作对话框 */
	private ConnsListDelDialog connsListDelDialog;
	/** 底部 标签目录 根布局 */
	private LinearLayout footLl;
	/** 目录 textview */
	private TextView categoryTv;
	/** 标签 textview */
	private TextView tagTv;

	// *********** menu start ********/
	/** 所有的MenuItem */
	private MenuItem iAllMenuItem;
	/** 我创建的MenuItem */
	private MenuItem iCreateMenuItem;
	/** 我收藏的MenuItem */
	private MenuItem iCollectMenuItem;
	/** 我保存的MenuItem */
	private MenuItem iSaveMenuItem;
	// *********** menu start ********/

	// ******************* 控件 end ******************* /

	Handler newMsgTagHandler = new Handler();
	Runnable newMsgTagRunnable = new Runnable() {
		@Override
		public void run() {
			ConnectionsReqUtil.getNewConnectionsCount(getActivity(), iBindData, new JSONObject(), null);
			newMsgTagHandler.postDelayed(newMsgTagRunnable, 120000);
		}
	};


	private TextView titleTv;

	private ImageView titleIv;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		String MSG = "onCreate()";
		Log.i(TAG, MSG);
		super.onCreate(savedInstanceState);
		// 注册 获取联系人列表 完成 广播
		registerGetConnectionsListBroadcastReceiver();
	}

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_frg_connections_list2, container, false);
		tagTv = (TextView) view.findViewById(R.id.tagTv);
		mWindowManager = (WindowManager) this.getActivity().getSystemService(Context.WINDOW_SERVICE);

		initView(view);

		return view;
	}

	private PopupWindow pop;
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		String MSG = "onViewCreated()";

		bundle = getArguments();
		type = bundle.getInt(EConsts.Key.TYPE);
//		sum = bundle.getInt(EConsts.Key.SUM);
		Log.i(TAG, MSG + " type = " + type);

		connectionsDBManager = ConnectionsDBManager.buildConnectionsDBManager(getActivity());
		cnsCacheData = new ConnectionsCacheData(connectionsDBManager);
		pop = new PopupWindow();
		filterConnectionsByType();
		initControl();
		initListener();

		initActionBar();
	}

	

	

	private void initActionBar() {
		final View mCustomView = getActivity().getActionBar().getCustomView();
		titleTv = (TextView) mCustomView.findViewById(R.id.titleTv);
		titleTv.setText("好友/人脉");
		titleIv = (ImageView) mCustomView.findViewById(R.id.titleIv);
		// 搜索
		titleTv.setOnClickListener(new OnClickListener() {
			

			@Override
			public void onClick(View v) {
				View view = View.inflate(getActivity(),
						R.layout.demand_me_need_item, null);
				
				TextView searchAllTv = (TextView) view.findViewById(R.id.searchAllTv);
				TextView searchMy = (TextView) view.findViewById(R.id.searchMy);
				TextView collectCollectTv = (TextView) view.findViewById(R.id.collectCollectTv);
				view.findViewById(R.id.searchSaveTv).setVisibility(View.GONE);
				searchAllTv.setOnClickListener(mOnClickListener);
				searchAllTv.setText("好友/人脉");
				searchMy.setOnClickListener(mOnClickListener);
				searchMy.setText("我创建的");
				collectCollectTv.setOnClickListener(mOnClickListener);
				collectCollectTv.setText("我收藏的");
				
				pop.setWidth(LayoutParams.WRAP_CONTENT);
				pop.setHeight(LayoutParams.WRAP_CONTENT);
				pop.setContentView(view);
				pop.setBackgroundDrawable(new BitmapDrawable());
				pop.setFocusable(true);
				pop.setOutsideTouchable(true);
				pop.setAnimationStyle(R.style.demand_me_need_popwin_anim_style);

				pop.showAsDropDown(mCustomView, -7, -2);

			}
		});
	}






	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		String MSG = "onStart()";
		Log.i(TAG, MSG);
		super.onStart();
	}

	/**
	 * 筛选内容
	 */
	private void filterConnectionsByType() {
		if (type != -1) {
			if (type == ConnectionsCacheData.FILTER_FRIEND_ALL) {
				cnsCacheData.setFilterType(ConnectionsCacheData.FILTER_FRIEND_PEOPLE);
			} else if (type == ConnectionsCacheData.FILTER_PEOPLE_ALL) {
				cnsCacheData.setFilterType(ConnectionsCacheData.FILTER_PEOPLE_ALL);
			}
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	/** 启动 获得联系人列表 服务 */
	private void startGetConnectionsListService() {
		try {
			if (type == ConnectionsCacheData.FILTER_FRIEND_ALL) {
				GetConnectionsListService.startGetConnectionsListService(getActivity(), RequestType.FriendAll);
			} else if (type == ConnectionsCacheData.FILTER_PEOPLE_ALL) {
				GetConnectionsListService.startGetConnectionsListService(getActivity(), RequestType.PeopleAll);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mFilterHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 2: {
				contactAdapter.dataChange();
			}
			case 1: {
				contactAdapter.notifyDataSetChanged();
			}
			default:
				break;
			}
		}
	};

	Runnable showComparator = new Runnable() {
		@Override
		public void run() {

			mFilterHandler.sendEmptyMessage(2);
		}
	};

	private MenuItem RichScan;

	private MenuItem InviteFriends;

	private MenuItem CreateConnections;

	

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (hidden) {

		} else {

		}
	}
		/** 请求 联系人列表 方法 */
	private void startGetConnections() {
		JSONObject jb = new JSONObject();
		try {
			jb.put("type", "0");
		}
		catch (JSONException e) {
			e.printStackTrace();
		}
		ConnectionsReqUtil.doGetConnectionsList(FrgConnections2.this.getActivity(), iBindData, jb, null);
	}

	private void initView(View view) {
		loadConnectionsWaitView = (RelativeLayout) view.findViewById(R.id.waitview);
		// 中间提示的拼音索引的 textview
		TextView mDialogText = (TextView) LayoutInflater.from(this.getActivity()).inflate(R.layout.im_relationcontactlist_position, null);
		mDialogText.setVisibility(View.INVISIBLE);
		indexBar = (SideBar) view.findViewById(R.id.sideBar);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
						| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
		mWindowManager.addView(mDialogText, lp);

		indexBar.setTextView(mDialogText);
		lvContact = (XListView) view.findViewById(R.id.lvContact);
		setXlistViewConfig();

		searchLl = (LinearLayout) view.findViewById(R.id.searchLl);
		keywordEt = (EditText) view.findViewById(R.id.keywordEt);

		footLl = (LinearLayout) view.findViewById(R.id.footLl);
		categoryTv = (TextView) view.findViewById(R.id.categoryTv);
		categoryTv.setOnClickListener(mOnClickListener);
		tagTv = (TextView) view.findViewById(R.id.tagTv);
		tagTv.setOnClickListener(mOnClickListener);
	}

	private void initListener() {
		keywordEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String MSG = "onTextChanged()";
				String keyword = keywordEt.getText().toString();
				cnsCacheData.setKeyword(keyword);
				contactAdapter.dataChange();
				Log.i(TAG, MSG + " keyword = " + keyword);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		connsListDelDialog = new ConnsListDelDialog(getActivity());
		connsListDelDialog.setOnSelectListener(new OnSelectListener() {

			@Override
			public void onSelect(OperType operType, Connections mAttachConnections) {

				if (OperType.Delete == operType) {

					boolean isOnline = mAttachConnections.isOnline();
					String id = mAttachConnections.getId();
					delPeopleId = id;

					// 这里做删除用户
					if (isOnline) {

						showLoadingDialog();
						ConnectionsReqUtil.dodeleteFriend(getActivity(), iBindData,
								ConnectionsReqUtil.getDeleteFriendJson(id, FriendRequest.type_persion), null);
					}
					// 这里做删除人脉
					else {
						showLoadingDialog();
						JSONObject jb = new JSONObject();
						try {
							jb.put("id", id);
						} catch (JSONException e) {
							Log.d(TAG, e.getMessage() + "");
						}
						ConnectionsReqUtil.getdelJtContact(getActivity(), iBindData, jb, null);

					}

				}
			}
		});
	}

	/** 设置XListView的参数 */
	private void setXlistViewConfig() {
		lvContact.showFooterView(false);
		// 设置xlistview可以加载、刷新
		lvContact.setPullRefreshEnable(true);
		lvContact.setPullLoadEnable(false);
		lvContact.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				 startGetData();
//				 startGetConnections();
//				lvContact.stopRefresh();
				
			}

			@Override
			public void onLoadMore() {
				// startGetData();
			}
		});
//			lvContact.startRefresh();

	}

	/**
	 * 获取页数据
	 */
	public void startGetData() {
		if (!isRefreshing) {
			isRefreshing = true;
			initListDataOver = false;
			
			lvContact.setAdapter(null);
			
			// startGetConnections();
			startGetConnectionsListService();
		}
	}

	/**
	 * 创建menu
	 */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		if (type != -1) {
			if (type == ConnectionsCacheData.FILTER_FRIEND_ALL) {

			} else if (type == ConnectionsCacheData.FILTER_PEOPLE_ALL) {
				inflater.inflate(R.menu.menu_my_connections_list, menu);
//				iAllMenuItem = menu.findItem(R.id.iAllMenuItem);
//				iCreateMenuItem = menu.findItem(R.id.iCreateMenuItem);
//				iCollectMenuItem = menu.findItem(R.id.iCollectMenuItem);
//				iSaveMenuItem = menu.findItem(R.id.iSaveMenuItem);
				 RichScan = menu.findItem(R.id.RichScan);
				 InviteFriends = menu.findItem(R.id.InviteFriends);
				 CreateConnections = menu.findItem(R.id.CreateConnections);
				 
			}
		}

		super.onCreateOptionsMenu(menu, inflater);
	};

	/**
	 * menu被选中
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		String MSG = "onOptionsItemSelected";
		if (type != -1) {
			if (type == ConnectionsCacheData.FILTER_FRIEND_ALL) {

			} else if (type == ConnectionsCacheData.FILTER_PEOPLE_ALL && RichScan!=null) {
//				
//				if(item.getItemId() == iAllMenuItem.getItemId() ){
//					MSG = "allItem";
//					getActivity().getActionBar().setTitle("好友/人脉 ");
//					cnsCacheData.setFilterType(ConnectionsCacheData.FILTER_PEOPLE_ALL);
////					type = ConnectionsCacheData.FILTER_PEOPLE_ALL;
//				}
//				else if(item.getItemId() == iCreateMenuItem.getItemId() ){
//					MSG = "iCreateItem";
//					getActivity().getActionBar().setTitle("我创建的人脉");
//					cnsCacheData.setFilterType(ConnectionsCacheData.FILTER_I_CREATE_OFFLINE_PEOPLE);
////					contactsSubType = ConnectionsCacheData.FILTER_I_CREATE_OFFLINE_PEOPLE;
//				}
//				else if(item.getItemId() == iCollectMenuItem.getItemId() ){
//					MSG = "iCollectItem";
//					getActivity().getActionBar().setTitle("我收藏的人脉");
//					cnsCacheData.setFilterType(ConnectionsCacheData.FILTER_I_COLLECT_OFFLINE_PEOPLE);
////					contactsSubType = ConnectionsCacheData.FILTER_I_COLLECT_OFFLINE_PEOPLE;
//				}
//				else if(item.getItemId() == iSaveMenuItem.getItemId() ){
//					MSG = "iSaveItem";
//					getActivity().getActionBar().setTitle("我保存的人脉");
//					cnsCacheData.setFilterType(ConnectionsCacheData.FILTER_I_SAVE_OFFLINE_PEOPLE);
////					contactsSubType = ConnectionsCacheData.FILTER_I_SAVE_OFFLINE_PEOPLE;
//				}
				if(item.getItemId() == RichScan.getItemId()){  //扫一扫
					startActivityForResult(new Intent(getActivity(), CaptureActivity.class), 1000);
				}else if(item.getItemId() == InviteFriends.getItemId()){  //邀请好友
					ENavigate.startMeetingInviteFriendsActivity(getActivity());
				}else if(item.getItemId() == CreateConnections.getItemId()){  //创建人脉
					ENavigate.startNewConnectionsActivity(getActivity(), 1, null, 9);
				}
				
			}
		}
		
//		Log.i(TAG, MSG + " clicked ");
		return super.onOptionsItemSelected(item);
	}

	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			if (v == categoryTv) {
				ENavigate.startKnowledgeCategoryActivityForResult(getActivity(), FrgConnections2.this, null, null, ModuleType.PEOPLE, false,titleTv.getText().toString());
			} else if (v == tagTv) {
				ENavigate.startRedactLabelActivity(getActivity(), RedactLabelActivity.ModulesType.PeopleModules, true); // 进入标签界面
			}else if(v.getId() == R.id.searchAllTv){
				titleTv.setText("好友/人脉 ");
				cnsCacheData.setFilterType(ConnectionsCacheData.FILTER_PEOPLE_ALL);
				contactAdapter.dataChange();
				pop.dismiss();
			}
			else if(v.getId() == R.id.searchMy){
				titleTv.setText("我创建的人脉");
				cnsCacheData.setFilterType(ConnectionsCacheData.FILTER_I_CREATE_OFFLINE_PEOPLE);
				contactAdapter.dataChange();
				pop.dismiss();
			}else if(v.getId() == R.id.collectCollectTv){
				titleTv.setText("我收藏的人脉");
				cnsCacheData.setFilterType(ConnectionsCacheData.FILTER_I_COLLECT_OFFLINE_PEOPLE);
				contactAdapter.dataChange();
				pop.dismiss();
			}
		}
	};

	private OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			if (TimeUtil.isFastDoubleClick()) {
				return;
			}

			if (arg2 != 0) {
//				Connections connections = (Connections) arg1.getTag();
				Connections connections = cnsCacheData.get(arg2-1);
				if (connections != null) {
					/** 个人好友和机构好友 FILTER_FRIEND_ALL = 5 */
					if (ConnectionsCacheData.FILTER_FRIEND_ALL == type) {
						// 是人的时候
						if (connections.type == Connections.type_persion) {
							// 发起聊天
							ChatDetail chatDetail = new ChatDetail();
							chatDetail.setThatID(connections.getId());
							chatDetail.setThatImage(connections.getImage());
							chatDetail.setThatName(connections.getName());
							ENavigate.startIMActivity(FrgConnections2.this.getActivity(), chatDetail);
						}
						// 是组织的时候(现在不是混排)
						else if (connections.type == Connections.type_org) {
							//
							// ENavigate.startOrgDetailsActivity(FrgConnections2.this.getActivity(),connections.getId(),
							// connections.isOnline(), 0);

						}
					}
					/** 个人好友和人脉 FILTER_PEOPLE_ALL = 6 */
					else if (ConnectionsCacheData.FILTER_PEOPLE_ALL == type) {
						// 是人的时候 去详情页
						if (connections.type == Connections.type_persion) {

							// 用户的候 去新详情页
							if (connections.isOnline()) {
								// ENavigate.startRelationHomeActivity(getActivity(),
								// connections.getId(), true,
								// ENavConsts.type_details_other);
								// 新 用户详情跳转
								ENavigate.startRelationHomeActivity(getActivity(), connections.getId());
							}
							// 人脉的时候
							else {
								// ENavigate.startUserDetailsActivity(getActivity(),
								// connections.getId(), connections.isOnline(),
								// 0);
								// 新 人脉详情跳转
								ENavigate.startContactsDetailsActivity(getActivity(), 2, Long.valueOf(connections.getId()), 0);
							}
						}
						// 是组织的时候
						else if (connections.type == Connections.type_org) {

						}

					}
				}

			}
		}
	};

	/** 关系列表adapter 长按点击事件 监听 */
	private OnItemLongClickListener mOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

			if (position != 0) {

//				Connections connections = (Connections) view.getTag();
				Connections connections = cnsCacheData.get(position-1);
				/** 个人好友和机构好友 FILTER_FRIEND_ALL = 5 */
				if (ConnectionsCacheData.FILTER_FRIEND_ALL == type) {
					// 是人的时候
					if (connections.type == Connections.type_persion) {

					}
					// 是组织的时候
					else if (connections.type == Connections.type_org) {

					}
				}
				/** 个人好友和人脉 FILTER_PEOPLE_ALL = 6 */
				else if (ConnectionsCacheData.FILTER_PEOPLE_ALL == type) {
					// 是人的时候 去详情页
					if (connections.type == Connections.type_persion) {
						// 用户的时候 弹出删除对话框
						if (connections.isOnline()) {
							// Toast.makeText(getActivity(),
							// "你长按了 用户 position =  " + position, 0).show();
						}
						// 人脉的时候 弹出删除对话框
						else {
							// Toast.makeText(getActivity(),
							// "你长按了 人脉 position =  " + position, 0).show();
						}

						connsListDelDialog.setAttachViewAndData(view, connections);
						connsListDelDialog.show();

					}
					// 是组织的时候
					else if (connections.type == Connections.type_org) {

					}
				}

			}

			return true;
		}

	};

	/**
	 * 初始化变量
	 */
	private void initControl() {
		// 设置适配器的操作
		//如果数据库写入操作完成，那么就初始化列表数据，否则，就在广播接收到的时候初始化
		if (App.connectionDataBaseWriteOver&&!initListDataOver) {
			//将页面已经进行初始化，置为true;
//			initListDataOver = true;
//			initListViewData();
			showLoadingDialog();
			//从新请求
			startGetConnectionsListService();
		}else {
			showLoadingDialog();
		}
		
		lvContact.setOnItemClickListener(mOnItemClickListener);
		lvContact.setOnItemLongClickListener(mOnItemLongClickListener);

		if (ConnectionsCacheData.FILTER_FRIEND_ALL == type) {
			searchLl.setVisibility(View.GONE);
			footLl.setVisibility(View.GONE);
		} else if (ConnectionsCacheData.FILTER_PEOPLE_ALL == type) {

		}

	}

	private void initListViewData() {
		ArrayList<Character> letterList = new ArrayList<Character>();
		for (int i = 0; i < letters.length; i++) {
			int position = cnsCacheData.getCharAt(letters[i]);
			if (position != -1) {
				Character c = letters[i];
				letterList.add(c);
			}
		}
		indexBar.setListView(lvContact, letterList, cnsCacheData);
		contactAdapter = new ContactAdapter(getActivity(), cnsCacheData);
		contactAdapter.dataChange();
		lvContact.setAdapter(contactAdapter);
	}

	/**
	 * 主关系列表 Adapter
	 * 
	 * @author gushi
	 * 
	 */
	public class ContactAdapter extends BaseAdapter {
		private Context mContext;
		private ConnectionsCacheData connectionsCacheData;

		@SuppressWarnings("unchecked")
		public ContactAdapter(Context mContext, ConnectionsCacheData cnsCacheData) {
			this.mContext = mContext;
			connectionsCacheData = cnsCacheData;
		}

		public void setConnectionsCacheData(ConnectionsCacheData connectionsCacheData) {
			this.connectionsCacheData = connectionsCacheData;
			notifyDataSetChanged();
		}

		public void dataChange() {
			// indexBar.init();
			// 组织关系 ——————————//根本没用
			/*new AsyncTask<Void, Void, ArrayList<Connections>>() {

				@Override
				protected ArrayList<Connections> doInBackground(Void... params) {
					mOrgConnections = connectionsDBManager.queryFriend(0, 10000, Connections.type_persion);
					if (mOrgConnections == null) {
						mOrgConnections = new ArrayList<Connections>();
					}
					return mOrgConnections;
				}

				protected void onPostExecute(java.util.ArrayList<Connections> result) {
					connectionsCacheData.init();
					notifyDataSetChanged();
				};
			}.execute();*/
			connectionsCacheData.init();
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return isMyFeopleFriendExtend ? connectionsCacheData.size(): 1;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		public ConnectionsCacheData getConnectionsCacheData() {
			return connectionsCacheData;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			String mNicks = "";
			Connections connections = connectionsCacheData.get(position);
			mNicks = connections.getName();
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.im_relationcontactmain_item_20150203, null);
				viewHolder = new ViewHolder();
				viewHolder.tag = (TextView) convertView.findViewById(R.id.contactitem_catalog);
				viewHolder.imcontactName = (TextView) convertView.findViewById(R.id.imcontactname);
				viewHolder.imcontactComeFrom = (TextView) convertView.findViewById(R.id.imcontactcomefrom);
				viewHolder.companyTv = (TextView) convertView.findViewById(R.id.companyTv);
				viewHolder.careerTv = (TextView) convertView.findViewById(R.id.careerTv);
				viewHolder.icon = (CircleImageView) convertView.findViewById(R.id.contactitem_avatar_iv);
				viewHolder.icontag = (ImageView) convertView.findViewById(R.id.contactitem_icontag);
				convertView.setTag(viewHolder);
			}
			viewHolder = (ViewHolder) convertView.getTag();
			String lastCatalog = null;
			String catalog = "";
			if (position == 0 && !StringUtils.isEmpty(mNicks)) {
				viewHolder.tag.setVisibility(View.VISIBLE);
				catalog = connections.getCharName() + "";
				viewHolder.tag.setText(catalog);
			} else if (position != 0 && !StringUtils.isEmpty(mNicks)) {
				catalog = connections.getCharName() + "";
				//上一个首字母
				lastCatalog = connectionsCacheData.get(position - 1).getCharName() + "";

				if (catalog.equals(lastCatalog)) {
					viewHolder.tag.setVisibility(View.GONE);
				} else {
					viewHolder.tag.setVisibility(View.VISIBLE);
					viewHolder.tag.setText(catalog);
				}
			} else {
				viewHolder.tag.setVisibility(View.GONE);
			}

			viewHolder.imcontactName.setText(connections.getName());
			viewHolder.imcontactComeFrom.setText(connections.sourceFrom);
			viewHolder.companyTv.setText(connections.getCompany());
			viewHolder.careerTv.setText(connections.getCareer());

			final ArrayList<MobilePhone> mobilePhoneList = connections.getMobilePhoneList();
			final ArrayList<MobilePhone> fixedPhoneList = connections.getFixedPhoneList();

			final ImageView callIv = (ImageView) convertView.findViewById(R.id.callIv);
			final ImageView sendSmsIv = (ImageView) convertView.findViewById(R.id.sendSmsIv);

			OnClickListener mOnClickListener = new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (callIv == v) {
						new ConnsCallAndSendSmsDialog(getActivity(), ConnsCallAndSendSmsDialog.TYPE_CALL, mobilePhoneList, fixedPhoneList).show();
					} else if (sendSmsIv == v) {
						new ConnsCallAndSendSmsDialog(getActivity(), ConnsCallAndSendSmsDialog.TYPE_SEND_SMS, mobilePhoneList, null).show();
					}
				}
			};

			callIv.setOnClickListener(mOnClickListener);
			sendSmsIv.setOnClickListener(mOnClickListener);

			// 如果有手机号列表
			if (mobilePhoneList.size() > 0 || (fixedPhoneList.size() > 0) && (!TextUtils.isEmpty(fixedPhoneList.get(0).getMobile()))) {
				sendSmsIv.setVisibility(View.VISIBLE);
				callIv.setVisibility(View.VISIBLE);
			} else {
				sendSmsIv.setVisibility(View.GONE);
				callIv.setVisibility(View.GONE);

			}
			if (!connections.isOnline()) {
				viewHolder.icontag.setVisibility(View.VISIBLE);
			} else {
				viewHolder.icontag.setVisibility(View.GONE);
			}
			if (connections.type == Connections.type_persion) {
				Util.initAvatarImage(mContext, viewHolder.icon, "", connections.getImage(), 0, 1);
			} else if (connections.type == Connections.type_org) {
				Util.initAvatarImage(mContext, viewHolder.icon, "", connections.getImage(), 0, 2);
			}
//			convertView.setTag(connections);
			return convertView;
		}

	}

	class ViewHolder {
		TextView tag;// 标头
		TextView imcontactName;// 名字
		TextView imcontactComeFrom;// 来自
		TextView companyTv; // 公司
		TextView careerTv; // 职位
		CircleImageView icon;// 现在圆角的头像
		ImageView icontag;// 头像
	}

	/**
	 * 这个方法只是个工具方法 被 别的类调用了 有时间 应该抽个工具类里
	 * 
	 * @param chines
	 * @return
	 */
	public static String converterToFirstSpell(String chines) {
		String pinyinName = "";
		chines = chines.toUpperCase();
		char[] nameChar = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {
				try {
					// pinyinName += PinyinHelper.toHanyuPinyinStringArray(
					// nameChar[i], defaultFormat)[0].charAt(0);
					pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[0], defaultFormat)[0].charAt(0);
				} catch (Exception e) {
					e.printStackTrace();
					pinyinName = "#";
				}
			} else {
				pinyinName += nameChar[i];
			}
			break;
		}

		return pinyinName;
	}

	/** 接收 获得联系人列表后 写入数据完成后的 广播 */
	private BroadcastReceiver getConnectionsListBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String MSG = "onReceive()";
			dismissLoadingDialog();
			lvContact.stopRefresh();
			String action = intent.getAction();
			Log.i(TAG, MSG + " action = " + action);

			if (EConsts.Action.ACTION_GET_CONNECTIONS_LIST_FINISH.equals(action)) {

				boolean success = intent.getBooleanExtra(EConsts.Key.SUCCESS, false);

				if (success) {
					String tableName = intent.getStringExtra(EConsts.Key.TABLE_NAME);

					Log.i(TAG, MSG + " tableName = " + tableName);
					if (tableName != null) {
						cnsCacheData.setTableName(tableName);
//						contactAdapter.dataChange();
					}

					SharedPreferences sp = context.getSharedPreferences(EConsts.share_firstLoginGetConnections, Activity.MODE_PRIVATE);
					Editor editor = sp.edit();
					editor.putString(EConsts.share_itemUserTableName, tableName);
					editor.commit();
					if (getActivity() != null) {
						((MyFriendAllActivity) getActivity()).dismissLoadingDialog();
					}

					/*contactAdapter.setConnectionsCacheData(cnsCacheData);
					contactAdapter.dataChange();*/
					if (!initListDataOver) {
						//标识 页面已经初始化
						initListDataOver = true;
						initListViewData();
					}
				} else {
					Toast.makeText(getActivity(), "请求失败 请重试.", 0).show();
				}
				isRefreshing = false;
			}

		}
	};

	private String meetingId;

	 
	 
	 
	 
	 /**
	     * 注册 获取联系人列表 完成 广播
	     */
	    private void registerGetConnectionsListBroadcastReceiver() {
	        IntentFilter filter = new IntentFilter();
	        filter.addAction(EConsts.Action.ACTION_GET_CONNECTIONS_LIST_FINISH);
	        getActivity().registerReceiver(getConnectionsListBroadcastReceiver, filter);
	    }

	    /**
	     * 注销  获取联系人列表 完成 广播
	     */
	    private void unregisterGetConnectionsListBroadcastReceiver() {
	       getActivity().unregisterReceiver(getConnectionsListBroadcastReceiver);
	    }
	
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterGetConnectionsListBroadcastReceiver();
		// 取消 新关系数 请求
		newMsgTagHandler.removeCallbacks(newMsgTagRunnable);
	}

	/*
	 * 发邀请短信
	 */
	private void sendSMS(String smsnumber) {
		String str = App.getApp().getAppData().mInviteJoinGinTongInfo;
		Uri smsToUri = Uri.parse("smsto:" + smsnumber);
		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
		intent.putExtra("sms_body", str);
		startActivity(intent);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	// public void onRefresh() {
	// TODO Auto-generated method stubif(!isRefreshing){
	// if (!isRefreshing) {
	// isRefreshing = true;
	// startGetConnections();
	// startGetConnectionsListService();
	// }
	// }

	/**
	 * 用于外界通知刷新界面
	 * 
	 * @param connections
	 */
	public void updateUI(Connections connections) {
		// onRefresh();
		startGetConnectionsListService();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String MSG = "onActivityResult()";
		Log.i(TAG, MSG );
		if (requestCode == 1000) {
			if (resultCode == Activity.RESULT_OK) {
				meetingId = data.getStringExtra("result");
				if (null == meetingId) {
					Toast.makeText(getActivity(), "无效的二维码", Toast.LENGTH_SHORT).show();
					return;
				} else if (meetingId.isEmpty()) {
					Toast.makeText(getActivity(), "无效的二维码", Toast.LENGTH_SHORT).show();
					return;
				}
				// TODO 这里用二维码访问网页 好有人脉
				else if (meetingId.contains("/invitation/")) {
					String substr = meetingId.substring(0, meetingId.length() - 1);
					substr = substr.substring(substr.lastIndexOf("/") + 1, substr.length());
					ENavigate.startInviteFriendByQRCodeActivity(getActivity(), substr, InviteFriendByQRCodeActivity.PeopleFriend);
					return;
				}
				// 组织
				else if (meetingId.contains("customerId")) {
					String substr = meetingId.substring(meetingId.lastIndexOf("=") + 1, meetingId.length());
					if (StringUtils.isEmpty(substr) || StringUtils.isEmpty(App.getUserID())) {
						Toast.makeText(getActivity(), "无效的二维码", 0).show();
						return;
					}
					ENavigate.startInviteFriendByQRCodeActivity(getActivity(), substr, InviteFriendByQRCodeActivity.OrgFriend);
					return;

				}else if (meetingId.contains("communityId")) {
					String communityId = meetingId.substring(meetingId.indexOf("=")+1, meetingId.indexOf("&"));
					if (StringUtils.isEmpty(communityId) || StringUtils.isEmpty(App.getUserID())) {
						Toast.makeText(getActivity(), "无效的二维码", 0).show();
						return;
					}
					ENavigate.startCommunityDetailsActivity(getActivity(),Long.parseLong(communityId), false);
					return;

				}
				try {
					// 启动
					long a = 0;
					a = Long.valueOf(meetingId).longValue();
					if (0 == a) {
						// Toast.makeText(this, "无效的二维码",
						// Toast.LENGTH_SHORT)
						// .show();
						Uri uri = Uri.parse(meetingId);
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						startActivity(intent);
						return;
					}
					Intent intent = new Intent(getActivity(), SquareActivity.class);
					intent.putExtra("meeting_id", Long.valueOf(meetingId));
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (REQUEST_CODE_CATEGORY_ACTIVITY == requestCode) {
			if (Activity.RESULT_OK == resultCode) {
				// 获取相应的目录
				// type = TYPE_CATEGORY;
				UserCategory category = (UserCategory) data.getSerializableExtra(EConsts.Key.KNOWLEDGE_CATEGORY);

				// clearResponseData();

				// reSetActionBarTitle();
				// reSetLayout();
				// onRefresh();
			}
			// else {
			// type = TYPE_ALL;
			// // clearResponseData();
			// // reSetActionBarTitle();
			// // reSetLayout();
			// // onRefresh();
			// }

		}

		else if (REQUEST_CODE_TAG_ACTIVITY == requestCode) {
			if (Activity.RESULT_OK == resultCode) {
				// type = TYPE_TAG;
				String tagName = data.getStringExtra("tag");
				Log.i(TAG, MSG + " tagName = " + tagName);
				// if (knoTagName != null) {
				// org_Tv.setText(knoTagName);
				// }
				// clearResponseData();
				// onRefresh();
			} else {
				// type = TYPE_ALL;
				// clearResponseData();
				// onRefresh();
			}

		}

	}

	/**
	 * 数据请求回调函数
	 */
	IBindData iBindData = new IBindData() {
		@Override
		public void bindData(int tag, Object object) {
			dismissLoadingDialog();
			if (!isResumed()) {
				return;
			}
			// 邀请加入金铜网
			if (tag == EAPIConsts.concReqType.im_inviteJoinGinTong) {
				if (getActivity() != null) {
					((MyFriendAllActivity) getActivity()).dismissLoadingDialog();
				}
				if (object != null) {
					String sur = (String) object;
					if (sur.equals("true")) {
						// addfriend.setVisibility(View.INVISIBLE);
						showToast("邀请邮件已发送");
						return;
					}
				}
				showToast("邀请邮件以发送失败");

			}
			// 请求加好友
			else if (tag == EAPIConsts.concReqType.im_addFriend) {
				if (getActivity() != null) {
					((MyFriendAllActivity) getActivity()).dismissLoadingDialog();
				}
				if (object != null) {
					String sur = (String) object;
					if (sur.equals("true")) {
						// addfriend.setVisibility(View.INVISIBLE);
						showToast("请求对方为好友，发送成功");
						return;
					}
				}
				showToast("请求对方为好友，发送失败");

			}
			// 获取联系人列表
			else if (tag == EAPIConsts.concReqType.CONNECTIONSLIST) {

				if (object != null) {
					final ArrayList<Connections> connArr = (ArrayList<Connections>) object;
					new AsyncTask<Void, Void, Void>() {

						@Override
						protected Void doInBackground(Void... params) {
							synchronized (connectionsDBManager) {
								// 确定tablename
								String tableName;
								Context context = connectionsDBManager.getContext();
								ConnectionsDBManager tempConnectionsDBManager = null;
								if (connectionsDBManager.getTableName().equals(DBHelper.TABLE_APP_CONNECTIONS)) {
									tempConnectionsDBManager = new ConnectionsDBManager(connectionsDBManager.getContext(),
											DBHelper.TABLE_APP_CONNECTIONS_BACK);
									tableName = DBHelper.TABLE_APP_CONNECTIONS_BACK;
								} else {
									tempConnectionsDBManager = new ConnectionsDBManager(context, DBHelper.TABLE_APP_CONNECTIONS);
									tableName = DBHelper.TABLE_APP_CONNECTIONS;
								}
								ConnectionsCacheData tempConnectionsCacheData = new ConnectionsCacheData(tempConnectionsDBManager);
								tempConnectionsCacheData.clearData();
								ArrayList<Connections> insertData = new ArrayList<Connections>();
								for (int i = 0; i < connArr.size(); i++) {
									insertData.add(connArr.get(i));
									if (insertData.size() >= 100) {
										tempConnectionsCacheData.insert(insertData);
										insertData.clear();
									}
								}
								if (insertData.size() != 0) {
									tempConnectionsCacheData.insert(insertData);
									insertData.clear();
								}

								cnsCacheData.setTableName(tableName);
								// FrgConnections.contactAdapter.dataChange();
								SharedPreferences sp = context.getSharedPreferences(EConsts.share_firstLoginGetConnections, Activity.MODE_PRIVATE);
								Editor editor = sp.edit();
								editor.putString(EConsts.share_itemUserTableName, tableName);
								editor.commit();
							}
							return null;
						}

						protected void onPostExecute(Void result) {
							contactAdapter.dataChange();
							lvContact.stopRefresh();
							isRefreshing = false;
						};
					}.execute();

					loadConnectionsWaitView.setVisibility(View.GONE);
					// swipeLayout.setRefreshing(false);
					if (getActivity() != null) {
						((MyFriendAllActivity) getActivity()).dismissLoadingDialog();
					}

				}

			}
			// else if (tag == EAPIConsts.concReqType.CONNECTIONSLIST) {
			//
			// if (object != null) {
			// ArrayList<Connections> connArr = (ArrayList<Connections>) object;
			//
			// synchronized (connectionsDBManager) {
			// // 确定tablename
			// String tableName;
			// Context context = connectionsDBManager.getContext();
			// ConnectionsDBManager tempConnectionsDBManager = null;
			// if
			// (connectionsDBManager.getTableName().equals(DBHelper.TABLE_APP_CONNECTIONS))
			// {
			// tempConnectionsDBManager = new
			// ConnectionsDBManager(connectionsDBManager.getContext(),
			// DBHelper.TABLE_APP_CONNECTIONS_BACK);
			// tableName = DBHelper.TABLE_APP_CONNECTIONS_BACK;
			// }
			// else {
			// tempConnectionsDBManager = new ConnectionsDBManager(context,
			// DBHelper.TABLE_APP_CONNECTIONS);
			// tableName = DBHelper.TABLE_APP_CONNECTIONS;
			// }
			// ConnectionsCacheData tempConnectionsCacheData = new
			// ConnectionsCacheData(tempConnectionsDBManager);
			// tempConnectionsCacheData.clearData();
			// ArrayList<Connections> insertData = new ArrayList<Connections>();
			// for (int i = 0; i < connArr.size(); i++) {
			// insertData.add(connArr.get(i));
			// if (insertData.size() >= 100) {
			// tempConnectionsCacheData.insert(insertData);
			// insertData.clear();
			// }
			// }
			// if (insertData.size() != 0) {
			// tempConnectionsCacheData.insert(insertData);
			// insertData.clear();
			// }
			//
			// cnsCacheData.setTableName(tableName);
			// // FrgConnections.contactAdapter.dataChange();
			// SharedPreferences sp =
			// context.getSharedPreferences(App.share_firstLoginGetConnections,
			// Activity.MODE_PRIVATE);
			// Editor editor = sp.edit();
			// editor.putString(App.share_itemUserTableName, tableName);
			// editor.commit();
			// }
			//
			// loadConnectionsWaitView.setVisibility(View.GONE);
			// // swipeLayout.setRefreshing(false);
			// if (getActivity() != null) {
			// ((MyFriendAllActivity) getActivity()).dismissLoadingDialog();
			// }
			//
			// if (object != null) {
			// contactAdapter.dataChange();
			// isRefreshing = false;
			// }
			// else {
			// isRefreshing = false;
			// }
			// isRefreshing = false;
			// }
			//
			// }
			else if (tag == EAPIConsts.concReqType.im_delJtContact) { // 删除人脉
				dismissLoadingDialog();

				if (object != null) {
					String sur = (String) object;
					if (sur.equals("true")) {
						/*
						 * if (onlineTContact != null) { // 删除用户
						 * FrgConnections.cnsCacheData.delete(
						 * onlineTContact.getID(), Connections.type_persion,
						 * onlineTContact.isOnline); }
						 */
						if (delPeopleId != null) { // 删除人脉
							cnsCacheData.delete(delPeopleId, Connections.type_persion, false);
						}
						contactAdapter.dataChange();
						showToast("删除成功");
					}
				} else {
					showToast("删除失败，请重试");
				}
			} else if (tag == EAPIConsts.concReqType.im_deleteFriend) { // 删除好友
				dismissLoadingDialog();

				if (object != null) {
					String sur = (String) object;
					if (sur.equals("true")) {
						cnsCacheData.delete(delPeopleId, Connections.type_persion, true);
						contactAdapter.dataChange();
						showToast("删除成功");
					}
				} else {
					showToast("删除失败");
				}
			}

			// 获得新关系数
			else if (tag == EAPIConsts.concReqType.im_getNewConnectionsCount) {
				String MSG = "EAPIConsts.concReqType.im_getNewConnectionsCount";

				if (object != null) {
					newConnectionsCount = (Integer) object;
					Log.i(TAG, MSG + " newConnectionsCount = " + newConnectionsCount);
					// newConnectionsCount=89;
					// int size = newGroupCount + newConnectionsCount;
					if (newConnectionsCount == 0) {
						if (cnsSizeTagNew != null) {
							cnsSizeTagNew.setVisibility(View.INVISIBLE);
						}
					}
					// 下面是群的处理
					else {
						if (cnsSizeTagNew != null) {
							cnsSizeTagNew.setText("" + newConnectionsCount);
							cnsSizeTagNew.setVisibility(View.VISIBLE);
						}
					}
				}
			}
			// 根据目录标签查人脉列表
			else if (tag == EAPIConsts.PeopleRequestType.PEOPLE_REQ_PEOPLELIST) {
				String MSG = "EAPIConsts.concReqType.im_getNewConnectionsCount";

				if (object != null) {
					newConnectionsCount = (Integer) object;
					Log.i(TAG, MSG + " newConnectionsCount = " + newConnectionsCount);
					// newConnectionsCount=89;
					// int size = newGroupCount + newConnectionsCount;
					if (newConnectionsCount == 0) {
						if (cnsSizeTagNew != null) {
							cnsSizeTagNew.setVisibility(View.INVISIBLE);
						}
					}
					// 下面是群的处理
					else {
						if (cnsSizeTagNew != null) {
							cnsSizeTagNew.setText("" + newConnectionsCount);
							cnsSizeTagNew.setVisibility(View.VISIBLE);
						}
					}
				}
			}

		}
	};

	private boolean initListDataOver;

}
