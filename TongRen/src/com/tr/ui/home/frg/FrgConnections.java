package com.tr.ui.home.frg;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.common.constvalue.EnumConst.ModuleType;
import com.tr.App;
import com.tr.R;
import com.tr.api.ConnectionsReqUtil;
import com.tr.db.ConnectionsCacheData;
import com.tr.db.ConnectionsDBManager;
import com.tr.model.connections.FriendRequest;
import com.tr.model.im.ChatDetail;
import com.tr.model.knowledge.UserCategory;
import com.tr.model.obj.Connections;
import com.tr.model.obj.MobilePhone;
import com.tr.navigate.ENavigate;
import com.tr.service.GetConnectionsListService;
import com.tr.service.GetConnectionsListService.RequestType;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.common.view.MyXListView;
import com.tr.ui.common.view.MyXListView.IXListViewListener;
import com.tr.ui.communities.home.GroupMembersManagementActivity;
import com.tr.ui.demand.RedactLabelActivity;
import com.tr.ui.home.HomePageActivity;
import com.tr.ui.relationship.MyFriendAllActivity;
import com.tr.ui.widgets.CircleImageView;
import com.tr.ui.widgets.ConnsCallAndSendSmsDialog;
import com.tr.ui.widgets.ConnsListDelDialog;
import com.tr.ui.widgets.ConnsListDelDialog.OnSelectListener;
import com.tr.ui.widgets.KnoCategoryAlertDialog.OperType;
import com.utils.common.EConsts;
import com.utils.common.Util;
import com.utils.display.DisplayUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;
import com.utils.time.TimeUtil;

/**
 * @ClassName: FrgConnections.java
 * @Description: 好友/人脉
 * @author zhongshan
 * @version V1.0
 * @Date 2015-9-8
 */
public class FrgConnections extends JBaseFragment implements OnScrollListener {
	public static final String TAG = "FrgConnections";

	/** 类型 */
	private int type = -1;

	/** 跳全局目录activity请求码 */
	public static final int REQUEST_CODE_CATEGORY_ACTIVITY = 1001;
	/** 跳全局标签activity请求码 */
	public static final int REQUEST_CODE_TAG_ACTIVITY = 1002;
	/** 跳好友/人脉详情页面请求码 */
	public static final int REQUEST_CODE_ITEM_ACTIVITY = 1003;
	// 新关系消息和组
	/** all datas */
	public static ConnectionsDBManager connectionsDBManager = null;
	public static ConnectionsCacheData cnsCacheData;
	private Bundle bundle;
	/** 要删除的当前用户或人脉的 id */
	private String delPeopleId;

	boolean isFirst = true;

	public MyXListView lvContact;// 内容list
	public static ContactAdapter contactAdapter;

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

	// /** 所有的MenuItem */
	// private MenuItem iAllMenuItem;
	// /** 我创建的MenuItem */
	// private MenuItem iCreateMenuItem;
	// /** 我收藏的MenuItem */
	// private MenuItem iCollectMenuItem;
	// /** 我保存的MenuItem */
	// private MenuItem iSaveMenuItem;
	private Context mContext;
	/** 当前刷新状态 */
	private boolean isRefreshing = false;
	private boolean isLoadMore = false;

	/** 当前列表所加载的人脉集合 */
	ArrayList<Connections> listConnections = new ArrayList<Connections>();
	private int total;

	private TextView titleTv;

	private PopupWindow pop;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		// 注册 获取联系人列表 完成 广播
		registerGetConnectionsListBroadcastReceiver();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_frg_connections_list,
				container, false);
		tagTv = (TextView) view.findViewById(R.id.tagTv);
		initView(view);
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		pop = new PopupWindow();
		String MSG = "onViewCreated()";
		bundle = getArguments();
		type = bundle.getInt(EConsts.Key.TYPE);
		Log.i(TAG, MSG + " type = " + type);
		/** 和数据库交互 **/
		connectionsDBManager = ConnectionsDBManager
				.buildConnectionsDBManager(getActivity());
		cnsCacheData = new ConnectionsCacheData(connectionsDBManager);
		filterConnectionsByType();
		initControl();
		initListener();
		initActionBar();

	}

	private void initActionBar() {
		getActivity().getActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.action_bar_bg2));
		final View mCustomView = getActivity().getActionBar().getCustomView();
		titleTv = (TextView) mCustomView.findViewById(R.id.titleTv);
		titleTv.setText("好友/人脉");
		// 搜索
		titleTv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				View view = View.inflate(getActivity(),
						R.layout.demand_me_need_item, null);

				TextView searchAllTv = (TextView) view
						.findViewById(R.id.searchAllTv);
				TextView searchMy = (TextView) view.findViewById(R.id.searchMy);
				TextView collectCollectTv = (TextView) view
						.findViewById(R.id.collectCollectTv);
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

	/**
	 * 筛选内容
	 */
	private void filterConnectionsByType() {
		if (type != -1) {
			if (type == ConnectionsCacheData.FILTER_FRIEND_ALL) {
				cnsCacheData
						.setFilterType(ConnectionsCacheData.FILTER_FRIEND_PEOPLE);
			} else if (type == ConnectionsCacheData.FILTER_PEOPLE_ALL) {
				cnsCacheData
						.setFilterType(ConnectionsCacheData.FILTER_PEOPLE_ALL);
			}
		}
	}

	/** 启动 获得联系人列表 服务 */
	private void startGetConnectionsListService() {
		try {
			if (type == ConnectionsCacheData.FILTER_FRIEND_ALL) {
				GetConnectionsListService.startGetConnectionsListService(
						getActivity(), RequestType.FriendAll);
			} else if (type == ConnectionsCacheData.FILTER_PEOPLE_ALL) {
				GetConnectionsListService.startGetConnectionsListService(
						getActivity(), RequestType.PeopleAll);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (hidden) {

		} else {

		}
	}

	private void initView(View view) {
		TextView mDialogText = (TextView) LayoutInflater.from(
				this.getActivity()).inflate(
				R.layout.im_relationcontactlist_position, null);
		mDialogText.setVisibility(View.INVISIBLE);
		lvContact = (MyXListView) view.findViewById(R.id.lvContact);
		setXlistViewConfig();
		searchLl = (LinearLayout) view.findViewById(R.id.searchLl);
		keywordEt = (EditText) view.findViewById(R.id.keywordEt);
		footLl = (LinearLayout) view.findViewById(R.id.footLl);
		lvContact.setBottomBar(footLl);
		categoryTv = (TextView) view.findViewById(R.id.categoryTv);
		categoryTv.setOnClickListener(mOnClickListener);
		tagTv = (TextView) view.findViewById(R.id.tagTv);
		tagTv.setOnClickListener(mOnClickListener);
	}

	private void initListener() {
		keywordEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String keyword = keywordEt.getText().toString();
				int length = keyword.length();
				if (length == 1 && !StringUtils.isEmpty(keyword)) {// 如果为一个字
					char charAt = keyword.charAt(0);
					// 如果是英文
					if ((charAt < 'z' && charAt > 'a')
							|| (charAt < 'Z' && charAt > 'A')) {
						keyword = charAt + "";
						cnsCacheData.setQurryByFistChar(true);
						keyword = keyword.toUpperCase();
					}
					// 汉字
					else {
						// String pingYin = PingYinUtil.getPingYin(keyword);
						// keyword = pingYin.charAt(0) + "";
						// cnsCacheData.setQurryByFistChar(true);
						cnsCacheData.setQurryByFistChar(false);
					}
				} else {
					cnsCacheData.setQurryByFistChar(false);
				}
				cnsCacheData.setKeyword(keyword);
				reFreshData();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		connsListDelDialog = new ConnsListDelDialog(getActivity());
		connsListDelDialog.setOnSelectListener(new OnSelectListener() {

			@Override
			public void onSelect(OperType operType,
					Connections mAttachConnections) {

				if (OperType.Delete == operType) {

					boolean isOnline = mAttachConnections.isOnline();
					String id = mAttachConnections.getId();
					delPeopleId = id;

					// 这里做删除用户
					if (isOnline) {

						showLoadingDialog();
						ConnectionsReqUtil.dodeleteFriend(getActivity(),
								iBindData, ConnectionsReqUtil
										.getDeleteFriendJson(id,
												FriendRequest.type_persion),
								null);
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
						ConnectionsReqUtil.getdelJtContact(getActivity(),
								iBindData, jb, null);
					}

				}
			}
		});
	}

	/** 设置XListView的参数 */
	private void setXlistViewConfig() {
		lvContact.showFooterView(false);
		lvContact.setPullRefreshEnable(false);
		lvContact.setPullLoadEnable(false);
		lvContact.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {

				startGetData();
			}

			@Override
			public void onLoadMore() {
				loadMoreData();
			}
		});
		if (IsChange) {
			lvContact.startRefresh();
		}
	}

	/**
	 * 获取页数据
	 */
	public void startGetData() {
		if (!isRefreshing) {
			isRefreshing = true;
			initListDataOver = false;
			startGetConnectionsListService();
		}
	}

	/**
	 * 刷新数据
	 */
	public void reFreshData() {
		total = 0;
		listConnections.clear();
		ArrayList<Connections> connections = cnsCacheData.getDate(0, 20);
		total += connections.size();
		listConnections.addAll(connections);
		contactAdapter.setListConnections(listConnections);
		lvContact.stopRefresh();
	}

	/**
	 * 加载更多
	 */
	private void loadMoreData() {
		synchronized (new Object()) {
			if (!isLoadMore) {
				isLoadMore = true;
				// 起始位置
				int startIndex = 0;
				startIndex = total;
				if (total == 0) {
					startIndex = 0;
					listConnections.clear();
				}
				// 起始位置 为当前查询到的总数-1；
				ArrayList<Connections> connections = cnsCacheData.getDate(
						startIndex, 20);
				// 当前查询到的总数
				total += connections.size();
				listConnections.addAll(connections);
				contactAdapter.setListConnections(listConnections);
				lvContact.stopLoadMore();
				isLoadMore = false;
			}
		}
	}

	/**
	 * 点击事件
	 */
	private OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			if (v == categoryTv) {
				ENavigate.startKnowledgeCategoryActivityForResult(
						getActivity(), FrgConnections.this, null, null,
						ModuleType.PEOPLE, false, titleTv.getText()
								.toString());
			} else if (v == tagTv) {
				ENavigate.startRedactLabelActivity(getActivity(),
						RedactLabelActivity.ModulesType.PeopleModules, true); // 进入标签界面
			}

			else if (v.getId() == R.id.searchAllTv) {
				titleTv.setText("好友/人脉 ");
				cnsCacheData
						.setFilterType(ConnectionsCacheData.FILTER_PEOPLE_ALL);
				reFreshData();
				pop.dismiss();
			} else if (v.getId() == R.id.searchMy) {
				titleTv.setText("我创建的人脉");
				cnsCacheData
						.setFilterType(ConnectionsCacheData.FILTER_I_CREATE_OFFLINE_PEOPLE);
				reFreshData();
				pop.dismiss();
			} else if (v.getId() == R.id.collectCollectTv) {
				titleTv.setText("我收藏的人脉");
				cnsCacheData
						.setFilterType(ConnectionsCacheData.FILTER_I_COLLECT_OFFLINE_PEOPLE);
				reFreshData();
				pop.dismiss();
			}
		}
	};

	private OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			if (TimeUtil.isFastDoubleClick()) {
				return;
			}

			if (arg2 != 0) {
				Connections connections = listConnections.get(arg2 - 1);
				if (connections != null) {
					/** 个人好友和机构好友 FILTER_FRIEND_ALL = 5 */
					if (ConnectionsCacheData.FILTER_FRIEND_ALL == type) {
						// 是人的时候
						if (connections.type == Connections.type_persion) {
							// 发起聊天
							ChatDetail chatDetail = new ChatDetail();
							chatDetail.setThatID(connections.getJtContactMini().getId());
							chatDetail.setThatImage(connections.getImage());
							chatDetail.setThatName(connections.getName());
							ENavigate.startIMActivity(
									FrgConnections.this.getActivity(),
									chatDetail);
						}
						// 是组织的时候(现在不是混排)
						else if (connections.type == Connections.type_org) {

						}
					}
					/** 个人好友和人脉 FILTER_PEOPLE_ALL = 6 */
					else if (ConnectionsCacheData.FILTER_PEOPLE_ALL == type) {
						// 是人的时候 去详情页
						if (connections.type == Connections.type_persion) {
							/**
							 * 用户的候 去新详情页
							 */
							if (connections.isOnline()) {
//								ENavigate.startHomePageActivity(getActivity(), connections.getJtContactMini().getId());
								ENavigate.startRelationHomeActivity(getActivity(), connections.getJtContactMini().getId(),true,1);
							} else {
								// 新 人脉详情跳转
//								ENavigate.startHomePageActivity(getActivity(), connections.getJtContactMini().getId());
								ENavigate.startRelationHomeActivity(getActivity(), connections.getJtContactMini().getId(),false,2);
							}
						}
						// 是组织的时候
						else if (connections.type == Connections.type_org) {
//							ENavigate.startHomePageActivity(getActivity(), connections.getOrganizationMini().getId());
							ENavigate.startRelationHomeActivity(getActivity(), connections.getOrganizationMini().getId(),true,1);
						}

					}
				}

			}
		}
	};

	/** 关系列表adapter 长按点击事件 监听 */
	private OnItemLongClickListener mOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {

			if (position != 0) {

				Connections connections = cnsCacheData.get(position - 1);
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
						if (connections.isOnline()) {
						}
						// 人脉的时候 弹出删除对话框
						else {
							connsListDelDialog.setAttachViewAndData(view,
									connections);
							connsListDelDialog.show();
						}

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
		// 如果数据库写入操作完成，那么就初始化列表数据，否则，就在广播接收到的时候初始化
		if (App.connectionDataBaseWriteOver && !initListDataOver) {
			// 将页面已经进行初始化，置为true;
			initListDataOver = true;
			initListViewData();
		} else {
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
		lvContact.setPullRefreshEnable(true);
		lvContact.setPullLoadEnable(true);
		listConnections.clear();
		contactAdapter = new ContactAdapter(listConnections);
		// lvContact.startRefresh();
		reFreshData();
		lvContact.setAdapter(contactAdapter);
		startGetData();// 从服务器更新下好友人脉
	}

	/**
	 * 主关系列表 Adapter
	 * 
	 * @author gushi
	 * 
	 */
	public class ContactAdapter extends BaseAdapter {
		private ArrayList<Connections> listConnections = new ArrayList<Connections>();

		public ArrayList<Connections> getListConnections() {
			return listConnections;
		}

		public void setListConnections(ArrayList<Connections> listConnections) {
			this.listConnections = listConnections;
			notifyDataSetChanged();
		}

		public ContactAdapter(ArrayList<Connections> listConnections) {
			super();
			this.listConnections = listConnections;
		}

		@Override
		public int getCount() {
			return listConnections.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Connections connections = listConnections.get(position);
			ViewHolder viewHolder;
			if (convertView == null) {
				convertView = View.inflate(mContext,
						R.layout.im_relationcontactmain_item, null);
				viewHolder = new ViewHolder();
				viewHolder.contactNameTv = (TextView) convertView
						.findViewById(R.id.contactNameTv);
				viewHolder.contactCompanyOfferTv = (TextView) convertView
						.findViewById(R.id.contactCompanyOfferTv);
				viewHolder.contactAvatarIv = (CircleImageView) convertView
						.findViewById(R.id.contactAvatarIv);
				viewHolder.sendSmsIv = (ImageView) convertView
						.findViewById(R.id.sendSmsIv);
				viewHolder.callIv = (ImageView) convertView
						.findViewById(R.id.callIv);
				convertView.setTag(viewHolder);
			}
			viewHolder = (ViewHolder) convertView.getTag();
			if (viewHolder != null) {
				// 用户/人脉时样式设置
				Util.initAvatarImage(mParentActivity,
						viewHolder.contactAvatarIv, connections.getName(),
						connections.getImage(), connections.getJtContactMini()
								.getGender(), 1);
				Drawable drawable = null;
				if (connections != null && connections.isOnline()) {
					drawable = getResources().getDrawable(
							R.drawable.contactfriendtag);
				} else {
					drawable = getResources().getDrawable(
							R.drawable.contactpeopletag);
				}
				if (drawable != null) {
					drawable.setBounds(0, 0, drawable.getMinimumWidth(),
							drawable.getMinimumHeight());
					if (viewHolder.contactNameTv != null) {
						viewHolder.contactNameTv.setCompoundDrawables(drawable,
								null, null, null);
						viewHolder.contactNameTv
								.setCompoundDrawablePadding(DisplayUtil.dip2px(
										mContext, 10));
					}
				}
				if (viewHolder.contactNameTv != null) {
					viewHolder.contactNameTv.setText(connections.getName());
				}
				if (viewHolder.contactCompanyOfferTv != null) {
					viewHolder.contactCompanyOfferTv.setText(connections
							.getCompany() + connections.getCareer());
				}

				final ArrayList<MobilePhone> mobilePhoneList = new ArrayList<MobilePhone>();
				for (MobilePhone mobilePhone : connections.getMobilePhoneList()) {
					if (!StringUtils.isEmpty(mobilePhone.mobile)
							&& !StringUtils.isEmpty(mobilePhone.name)) {
						mobilePhoneList.add(mobilePhone);
					}
				}
				final ArrayList<MobilePhone> fixedPhoneList = new ArrayList<MobilePhone>();
				for (MobilePhone mobilePhone : connections.getFixedPhoneList()) {
					if (!StringUtils.isEmpty(mobilePhone.mobile)
							&& !StringUtils.isEmpty(mobilePhone.name)) {
						fixedPhoneList.add(mobilePhone);
					}
				}
				if (viewHolder.sendSmsIv != null && viewHolder.callIv != null) {
					// 如果有手机号列表
					if (mobilePhoneList.size() > 0
							|| (fixedPhoneList.size() > 0)
							&& (!TextUtils.isEmpty(fixedPhoneList.get(0)
									.getMobile()))) {
						viewHolder.sendSmsIv.setVisibility(View.VISIBLE);
						viewHolder.callIv.setVisibility(View.VISIBLE);
					} else {
						viewHolder.sendSmsIv.setVisibility(View.GONE);
						viewHolder.callIv.setVisibility(View.GONE);
					}
					OnClickListener mOnClickListener = new OnClickListener() {

						@Override
						public void onClick(View v) {

							switch (v.getId()) {
							case R.id.sendSmsIv:
								new ConnsCallAndSendSmsDialog(
										getActivity(),
										ConnsCallAndSendSmsDialog.TYPE_SEND_SMS,
										mobilePhoneList, null).show();
								break;
							case R.id.callIv:
								new ConnsCallAndSendSmsDialog(getActivity(),
										ConnsCallAndSendSmsDialog.TYPE_CALL,
										mobilePhoneList, fixedPhoneList).show();
								break;
							}
						}
					};
					viewHolder.callIv.setOnClickListener(mOnClickListener);
					viewHolder.sendSmsIv.setOnClickListener(mOnClickListener);
				}
			}
			return convertView;
		}

	}

	class ViewHolder {
		TextView contactNameTv;// 类型+名字
		TextView avatarText;// 头像上面的字
		TextView contactCompanyOfferTv;// 公司+职位
		CircleImageView contactAvatarIv;// 头像
		ImageView sendSmsIv;// 发短信
		ImageView callIv;// 打电话
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

			if (EConsts.Action.ACTION_GET_CONNECTIONS_LIST_FINISH
					.equals(action)) {

				boolean success = intent.getBooleanExtra(EConsts.Key.SUCCESS,
						false);

				if (success) {
					String tableName = intent
							.getStringExtra(EConsts.Key.TABLE_NAME);

					Log.i(TAG, MSG + " tableName = " + tableName);
					if (tableName != null) {
						cnsCacheData.setTableName(tableName);
					}

					SharedPreferences sp = context.getSharedPreferences(
							EConsts.share_firstLoginGetConnections,
							Activity.MODE_PRIVATE);
					Editor editor = sp.edit();
					editor.putString(EConsts.share_itemUserTableName, tableName);
					editor.commit();
					if (getActivity() != null) {
						((MyFriendAllActivity) getActivity())
								.dismissLoadingDialog();
					}

					if (!initListDataOver) {
						// 标识 页面已经初始化
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

	/**
	 * 注册 获取联系人列表 完成 广播
	 */
	private void registerGetConnectionsListBroadcastReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(EConsts.Action.ACTION_GET_CONNECTIONS_LIST_FINISH);
		getActivity().registerReceiver(getConnectionsListBroadcastReceiver,
				filter);
	}

	/**
	 * 注销 获取联系人列表 完成 广播
	 */
	private void unregisterGetConnectionsListBroadcastReceiver() {
		getActivity().unregisterReceiver(getConnectionsListBroadcastReceiver);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterGetConnectionsListBroadcastReceiver();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

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
		Log.i(TAG, MSG);

		if (REQUEST_CODE_CATEGORY_ACTIVITY == requestCode) {
			if (Activity.RESULT_OK == resultCode) {
				// 获取相应的目录
				UserCategory category = (UserCategory) data
						.getSerializableExtra(EConsts.Key.KNOWLEDGE_CATEGORY);
			}
		}

		else if (REQUEST_CODE_TAG_ACTIVITY == requestCode) {
			if (Activity.RESULT_OK == resultCode) {
				String tagName = data.getStringExtra("tag");
				Log.i(TAG, MSG + " tagName = " + tagName);
			} else {
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
					((MyFriendAllActivity) getActivity())
							.dismissLoadingDialog();
				}
				if (object != null) {
					String sur = (String) object;
					if (sur.equals("true")) {
						showToast("邀请邮件已发送");
						return;
					}
				}
				showToast("邀请邮件以发送失败");

			}
			// 请求加好友
			else if (tag == EAPIConsts.concReqType.im_addFriend) {
				if (getActivity() != null) {
					((MyFriendAllActivity) getActivity())
							.dismissLoadingDialog();
				}
				if (object != null) {
					String sur = (String) object;
					if (sur.equals("true")) {
						showToast("请求对方为好友，发送成功");
						return;
					}
				}
				showToast("请求对方为好友，发送失败");

			} else if (tag == EAPIConsts.concReqType.im_delJtContact) { // 删除人脉
				dismissLoadingDialog();

				if (object != null) {
					String sur = (String) object;
					if (sur.equals("true")) {
						if (delPeopleId != null) { // 删除人脉
							cnsCacheData.delete(delPeopleId,
									Connections.type_persion, false);
						}
						reFreshData();
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
						cnsCacheData.delete(delPeopleId,
								Connections.type_persion, true);
						reFreshData();
						showToast("删除成功");
					}
				} else {
					showToast("删除失败");
				}
			}
		}
	};

	private boolean initListDataOver;

	public boolean IsChange;

}