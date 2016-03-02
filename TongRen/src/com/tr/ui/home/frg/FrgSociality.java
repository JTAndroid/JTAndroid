package com.tr.ui.home.frg;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.lidroid.xutils.ViewUtils;
import com.tr.App;
import com.tr.R;
import com.tr.api.CommunityReqUtil;
import com.tr.api.ConferenceReqUtil;
import com.tr.api.ConnectionsReqUtil;
import com.tr.api.IMReqUtil;
import com.tr.db.SocialityDBManager;
import com.tr.model.CommunityStateResult;
import com.tr.model.SimpleResult;
import com.tr.model.conference.MListSociality;
import com.tr.model.conference.MSociality;
import com.tr.model.im.ChatDetail;
import com.tr.model.obj.IMBaseMessage;
import com.tr.model.page.JTPage;
import com.tr.navigate.ENavigate;
import com.tr.ui.adapter.MListSocialityAdapter;
import com.tr.ui.adapter.MListSocialityAdapter.Holder;
import com.tr.ui.base.JBaseFragment;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.communities.home.CommumitiesNotificationActivity;
import com.tr.ui.communities.model.CommunityNotify;
import com.tr.ui.conference.home.MeetingNoticeActivity2;
import com.tr.ui.flow.FlowActivity;
import com.tr.ui.flow.frg.FrgFlow;
import com.tr.ui.home.MainActivity;
import com.tr.ui.home.PushMessageCallBack;
import com.tr.ui.im.MeetListActivity;
import com.tr.ui.relationship.NewConnectionActivity;
import com.utils.common.GlobalVariable;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * FrgSociality 社交列表
 * 
 */
public class FrgSociality extends JBaseFragment implements IBindData,
		PushMessageCallBack {

	private XListView mListView;
	private JTPage mPage;
	private MListSocialityAdapter adapter;
	private PopupWindow window;
	private MListSociality conferenceAndChat;
	private MListSociality conference;
	private List<MSociality> listSocialMeet;
	private int conferenceUnreadMsg = 0;
	private int measuredHeigh;
	private int measuredWidth;
	private int linmitHeight;
	private static View navigateView;
	private boolean isFirstStart = true;
	public boolean isRunningTask = false;
	/* 是否是新关系长按点击事件 */
	public boolean iscnsNewLongClick = false;
	public int deleteClickPosition = -1;
	private int socialityListMaxCount = 3;
	/* 社交列表存储数据集合 */
	private List<MSociality> listSocial;
	// 是否有邀请函的标识
	private boolean hasInvitation = false;
	private View deleteView;
	private TextView deleteTv;
	private TextView save;
	private TextView navigateNumTv;// 底部导航社交未读消息数目
	private View frgSocialityMenuView;
	// private ImageView redIv;
	private int newConnectionsCount;
	private FrameLayout launchChatingFL;
//	private boolean isFirstLoadData = true;
	private SharedPreferences social_sp;
	private SharedPreferences.Editor social_edtior;
	
	private final int PUSH_NEW = 5;
	public final int REQ_CHAT = 100;
	public final int REQ_MUC = 101;
	
	//增加社群item
	private MSociality community;
	private ArrayList<CommunityNotify> communityNotifylist;

	@Override
	public void onResume() {
		super.onResume();
//		if (social_sp.getBoolean(GlobalVariable.SOCIAL_ISFISTLOAD, false)) {
			startGetData();
//		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				listSocial = (List<MSociality>) msg.obj;
				((MainActivity) getActivity()).updateNavigateNum(listSocial);
				init();
				mListView.stopRefresh();
				dismissLoadingDialog();
				isFirstStart = false;
				isRunningTask = false;
				break;
			case 2:
				listSocial = (List<MSociality>) msg.obj;
				((MainActivity) getActivity()).updateNavigateNum(listSocial);
				adapter.notifyDataSetChanged();
				mListView.stopRefresh();
				isRunningTask = false;
				break;
			// 更新下部未读消息角标
			case 3:
				int pushNum = msg.arg1;
				// navigateNumTv.setText((pushNum > 99 ? 99 : pushNum) + "");
				updateNavigateNum(pushNum);
				break;
			case 4:
				if (adapter != null) {
					adapter.notifyDataSetChanged();
				}
				break;
			case PUSH_NEW:
				startGetData();
				break;
			}
		};
	};

	/**
	 * 更新主页导航社交图标未读消失图标
	 * 
	 * @param pushNum
	 */
	public void updateNavigateNum(int pushNum) {
		if (pushNum <= 0) {
			navigateView.setVisibility(View.GONE);
		} else {
			navigateView.setVisibility(View.VISIBLE);
			navigateNumTv.setText((pushNum > 99 ? 99 : pushNum) + "");
		}
	}

	public FrgSociality() {
		super();
	}

	public FrgSociality(View view) {
		super();
		this.navigateView = view;
		navigateNumTv = (TextView) navigateView
				.findViewById(R.id.inlet_gam_remind_num_tv);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// actionBar
		View view = inflater.inflate(R.layout.frg_sociality, null);
		ViewUtils.inject(this, view);
		mListView = (XListView) view
				.findViewById(R.id.home_frg_sociatity_listview);

		mListView.showFooterView(false);
		mListView.setPullLoadEnable(false);
		mListView.setPullRefreshEnable(true);

		deleteView = View.inflate(getActivity(),
				R.layout.layout_sociality_delete, null);
		deleteTv = (TextView) deleteView.findViewById(R.id.delete);
		save = (TextView) deleteView.findViewById(R.id.save);

		setHasOptionsMenu(true);
		// showLoadingDialog();
		mListView.setXListViewListener(new IXListViewListener() {
			@Override
			public void onRefresh() {
				// // 下拉刷新
				startGetData();
			}

			@Override
			public void onLoadMore() {
				// 加载更多
				startGetData();
			}
		});
		ViewUtils.inject(view);
		social_sp = getActivity().getSharedPreferences(
				GlobalVariable.SHARED_PREFERENCES_SOCIAL_ISFISTLOAD,
				getActivity().MODE_PRIVATE);
		social_edtior = social_sp.edit();
		return view;
	}

	/**
	 * 加载数据 CJJ
	 * ---------------------------------------该函数保证实时刷新数据+setUserVisibleHint
	 * -------------------------------------------
	 */
	public void startGetData() {
		int nowIndex = 0;
		if (mPage != null) {
			nowIndex = mPage.getIndex() + 1;
		} else {
			// showLoadingDialog();
		}
		/*
		 * ConferenceReqUtil.doGetSocialList(getActivity(), FrgSociality.this,
		 * null, nowIndex, socialityListMaxCount);
		 */
		if (isFirstStart) {
//			showLoadingDialog();
		}

		ConferenceReqUtil.doGetSocialList(getActivity(), FrgSociality.this,
				null);

		ConnectionsReqUtil.getNewConnectionsCount(getActivity(),
				FrgSociality.this, new JSONObject(), null);
		
		ConferenceReqUtil.getCommunityState(getActivity(), this, null, App.getUserID());
	}

	private boolean mIsVisibleToUser;

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (window != null && window.isShowing()) {
			window.dismiss();
		}
		mIsVisibleToUser = isVisibleToUser;

		if (getActivity() != null) {
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {

					SharedPreferences sharedPreferences = getActivity()
							.getSharedPreferences(
									GlobalVariable.SHARED_PREFERENCES_MEETING_NEW_COUNT,
									getActivity().MODE_PRIVATE);
					int meetingCount = sharedPreferences.getInt(
							GlobalVariable.MEETING_NEW_COUNT_KEY, 0);
					sharedPreferences = getActivity()
							.getSharedPreferences(
									GlobalVariable.SHARED_PREFERENCES_NOTICE_NEW_COUNT,
									getActivity().MODE_PRIVATE);
					int noticeCount =  sharedPreferences.getInt(GlobalVariable.NOTICE_NEW_COUNT_KEY, 0);
					for (int i = 0; listSocial != null && i < listSocial.size(); i++) {
						if (MSociality.isMeeting(listSocial.get(i).getType())) {
							listSocial.get(i).setNewCount(meetingCount);
						} else if (listSocial.get(i).getType() == MSociality.NOTICE) {
							listSocial.get(i).setNewCount(noticeCount);
						}
					}

					if (adapter != null && listSocial != null) {
						adapter.notifyDataSetChanged();
						((MainActivity) getActivity())
								.updateNavigateNum(listSocial);
					}
				}
			});
		}

	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		deleteView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
		measuredWidth = deleteView.getMeasuredWidth();
		measuredHeigh = deleteView.getMeasuredHeight();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//		inflater.inflate(R.menu.sociaty_menu, menu);
		menu.findItem(R.id.home_new_menu_more).setVisible(false);
//		menu.findItem(R.id.aff_create).setVisible(false);
//		menu.findItem(R.id.affairs_new_menu_list).setVisible(false);
//		frgSocialityMenuView = menu.findItem(R.id.phone_call_notebook)
//				.getActionView();
//		launchChatingFL = (FrameLayout) frgSocialityMenuView
//				.findViewById(R.id.activity_inlet_gam_fl);
//		launchChatingFL.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// TODO 发起畅聊
//				ENavigate.startIMRelationSelectActivity(getActivity(), null,
//						null, 0, null, null);
//			}
//		});
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	private void controlXListBottom() {
		int totalPages;
		if (mPage == null) {
			mListView.setPullLoadEnable(false);
			return;
		}
		if ((mPage.getTotal() % socialityListMaxCount) == 0) {
			totalPages = mPage.getTotal() / socialityListMaxCount;
		} else {
			totalPages = mPage.getTotal() / socialityListMaxCount + 1;
		}
		if ((totalPages == 0) || (mPage.getIndex() >= totalPages)) {
			mListView.setPullLoadEnable(false);
		} else {
			mListView.setPullLoadEnable(true);
		}
	}

	/**
	 * 重新登录环信
	 */
	private void loginEasemob() {
		EMChatManager.getInstance().login(
				App.getApp().getAppData().getUserID(),
				App.getApp().getAppData().getUserID(), new EMCallBack() {// 回调
					@Override
					public void onSuccess() {
						// 注册一个监听连接状态的listener
						EMChatManager.getInstance().addConnectionListener(
								new com.tr.imservice.MyConnectionListener(getActivity()));

						getActivity().runOnUiThread(new Runnable() {
							public void run() {
								EMGroupManager.getInstance().loadAllGroups();
								EMChatManager.getInstance()
										.loadAllConversations();
								Log.d("main", "登陆聊天服务器成功！");
							}
						});
					}

					@Override
					public void onProgress(int progress, String status) {

					}

					@Override
					public void onError(int code, String message) {
						Log.d("main", "登陆聊天服务器失败！");
					}
				});
	}

	@Override
	public void bindData(int tag, Object object) {
		try {
			// dismissLoadingDialog();
			if (mListView != null) {
				mListView.stopLoadMore();
				mListView.stopRefresh();
			}
			if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GETSOCIALLIST
					&& object != null) {
				loginEasemob();
				conferenceAndChat = (MListSociality) object;
				listSocial = conferenceAndChat.getListSocial();
				
				//增加社群item
				if(community == null){
					community = new MSociality();
				}
				community.setType(MSociality.COMMUNITY);
				listSocial.add(1, community);

				if (isFirstStart) {
					isFirstStart = false;
					init();
					dismissLoadingDialog();
				}
				// controlXListBottom();
				((MainActivity) getActivity())
						.updateNavigateNum(conferenceAndChat.getCount());
				/**
				 * 更新关系数
				 */
				newConnectionsCount = conferenceAndChat.getCount();
				adapter.setNewConnectionsCount(newConnectionsCount);
				int meetingNum = 0;
				int noticeNum = 0;
				for (int i = 0; listSocial != null && i < listSocial.size(); i++) {
					if (MSociality.isMeeting(listSocial.get(i).getType())) {
						meetingNum = listSocial.get(i).getNewCount();
					} else if (listSocial.get(i).getType() == MSociality.NOTICE) {
						noticeNum = listSocial.get(i).getNewCount();
					}
				}
				// 写入数
				SharedPreferences meetingSharedPreferences = getActivity()
						.getSharedPreferences(
								GlobalVariable.SHARED_PREFERENCES_MEETING_NEW_COUNT,
								getActivity().MODE_PRIVATE);
				SharedPreferences.Editor editor = meetingSharedPreferences.edit();
				editor.putInt(GlobalVariable.MEETING_NEW_COUNT_KEY, meetingNum);
				editor.commit();
				
				// 写入数
				SharedPreferences noticeSharedPreferences = getActivity()
						.getSharedPreferences(
								GlobalVariable.SHARED_PREFERENCES_NOTICE_NEW_COUNT,
								getActivity().MODE_PRIVATE);
				SharedPreferences.Editor noticeEditor = noticeSharedPreferences.edit();
				noticeEditor.putInt(GlobalVariable.NOTICE_NEW_COUNT_KEY, noticeNum);
				noticeEditor.commit();

				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (adapter != null) {
							adapter.setListSocial(listSocial);
							adapter.notifyDataSetChanged();
						}
					}
				});

			}

			if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_CHANGEMYMEMBERMEETSTATUS
					&& object != null) {
				SimpleResult result = (SimpleResult) object;
				if (result.isSucceed()) {
					Toast.makeText(getActivity(), "保存会议成功", 0).show();
				} else {
					Toast.makeText(getActivity(), "保存会议失败", 0).show();
				}
			}
			/*
			 * if (tag ==
			 * EAPIConsts.IMReqType.IM_REQ_CLEAR_UNREAD_MESSAGENUMBER) { if
			 * (object != null) { int responseCode = (Integer) object; // 删除失败
			 * if (responseCode == -1) { showToast("删除失败"); } // 删除成功 else if
			 * (responseCode == 0) { if (deleteClickPosition != -1) {
			 * listSocial.get(deleteClickPosition - 1).setDelete( true);
			 * listSocial.get(deleteClickPosition - 1) .setNewCount(0);
			 * adapter.notifyDataSetChanged(); ((MainActivity) getActivity())
			 * .updateNavigateNum(listSocial); } } } deleteClickPosition = -1; }
			 */
			if (tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_DELETE_SOCIAL) {
				if (object != null) {
					boolean is_remove = (Boolean) object;
					if (!is_remove) {
						showToast("删除失败");
					} else {
						listSocial.remove(deleteClickPosition - 1);
						adapter.setListSocial(listSocial);
						adapter.notifyDataSetChanged();
						((MainActivity) getActivity())
								.updateNavigateNum(listSocial);
					}
				}
				deleteClickPosition = -1;
			}
			if(tag == EAPIConsts.ConferenceReqType.CONFERENCE_REQ_COMMUNITY_STATE){
				if (object != null) {
					if(community == null){
						community = new MSociality();
					}
					int newcount = 0;
					ArrayList<CommunityStateResult> mCommunityStateResultList = (ArrayList<CommunityStateResult>) object;
					if (mCommunityStateResultList != null
							&& mCommunityStateResultList.size() > 0) {
						for (CommunityStateResult iterable_element : mCommunityStateResultList) {
							if (iterable_element.getNewMessageRemind() == 0
									&& iterable_element.getNewCount() > 0) {
								// 显示红点
								newcount += iterable_element.getNewCount();
							}
						}
						community.setNewCount(newcount);
					} else {
						community.setNewCount(0);
					}
				}
			}
//			isFirstLoadData = false;
			if(social_edtior != null){
				social_edtior.putBoolean(GlobalVariable.SOCIAL_ISFISTLOAD, false);
				social_edtior.commit();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	class ComparatorNotify implements Comparator{

		@Override
		public int compare(Object lhs, Object rhs) {
			CommunityNotify communityNotify1 = (CommunityNotify) lhs;
			CommunityNotify communityNotify2 = (CommunityNotify) rhs;
			int flag=communityNotify2.getUpdatedTime().compareTo(communityNotify1.getUpdatedTime());
			if (flag == 0) {
				return communityNotify2.getCreatedTime().compareTo(communityNotify1.getCreatedTime());
			} else {
				return flag;
			}
		}
		
	}

	private void init() {
		if (adapter == null) {
			adapter = new MListSocialityAdapter(getActivity(), listSocial);
		}
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				if (position == 0) {
					return;
				}
				// "type":"1-私聊，2-群聊，3-进行中的会议，4-未开始，5-已结束的会议，6-通知，7-邀请函 8-新关系",
				// 点击后自动将提示数据清空 并插入到数据库
				// mSociality.getNewCount(0);
				Holder holder = (Holder) view.getTag();
				MSociality mSociality = listSocial.get(position - 1);
				if (mSociality.getType() == MSociality.NEW_RELATIONSHIP) {
					if (!iscnsNewLongClick) {
						mSociality.setNewCount(0);
						listSocial.remove(position - 1);
						listSocial.add(position - 1, mSociality);
						adapter.setListSocial(listSocial);
						Intent intent = new Intent(getActivity(),
								NewConnectionActivity.class);
						getActivity().startActivity(intent);
						holder.cnsSizeTvNew.setVisibility(View.GONE);
					}
					iscnsNewLongClick = false;
				}else if(mSociality.getType() == MSociality.COMMUNITY){
					Intent intent = new Intent(getActivity(), CommumitiesNotificationActivity.class);
					intent.putExtra("communityNotifylist", communityNotifylist);
					getActivity().startActivity(intent);
				} else if (MSociality.isMeeting(mSociality.getType())) {// 会议//
																		// 3-进行中的会议，4-未开始，5-已结束的会议
																		// 0-没有会议
					Intent intent = new Intent(getActivity(),
							MeetListActivity.class);
					getActivity().startActivity(intent);
				} else if (mSociality.getType() == MSociality.INVITATION) {// 邀请函
					mSociality.setNewCount(0);
					listSocial.remove(position - 1);
					listSocial.add(position - 1, mSociality);
					adapter.setListSocial(listSocial);
					// 进入新邀请函列表
					ENavigate.startMeetingInvitationActivity(getActivity());
				} else if (mSociality.getType() == MSociality.NOTICE) {// 通知
					Intent intent = new Intent();
					intent.setClass(getActivity(), MeetingNoticeActivity2.class);
					startActivity(intent);
				} else {
					switch (mSociality.getType()) {
					// 单聊
					case 1:
						holder.chat_push_data_num_control
								.setVisibility(View.GONE);
						ChatDetail chatDetail = new ChatDetail();
						chatDetail.setThatID(mSociality.getId() + "");
						chatDetail.setThatName(mSociality.getTitle());
						chatDetail.setType(mSociality.getUserType());
						if (mSociality.getSocialDetail().getListImageUrl() != null
								&& mSociality.getSocialDetail()
										.getListImageUrl().size() > 0) {
							chatDetail
									.setThatImage(mSociality.getSocialDetail()
											.getListImageUrl().get(0));
						}
						ENavigate.startIMActivity(getActivity(), chatDetail,REQ_CHAT);

						break;
					// 群聊
					case 2:
						holder.chat_push_data_num_gv_control
								.setVisibility(View.GONE);
						ENavigate.startIMGroupActivity(getActivity(),REQ_MUC,
								mSociality.getId() + "");
						break;
					}

					mSociality.setAtVisible(false);
					mSociality.setNewCount(0);
					listSocial.remove(position - 1);
					listSocial.add(position - 1, mSociality);
					adapter.setListSocial(listSocial);
				}
			}
		});

		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int scrollY = getScrollY();
				if (scrollY >= 100) {
					dismissPopwindow();
				}
			}

			/*
			 * //** 计算出当前ListView的滚动高度
			 * 
			 * @return
			 */
			public int getScrollY() {
				View c = mListView.getChildAt(0);
				if (c == null) {
					return 0;
				}
				int firstVisiblePosition = mListView.getFirstVisiblePosition();
				int top = c.getTop();
				return -top + firstVisiblePosition * c.getHeight();
			}
		});

		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long arg3) {
				MSociality mSociality = listSocial.get(position - 1);
				dismissPopwindow();
				if (mSociality.getType() == MSociality.NEW_RELATIONSHIP) {// 8
																			// 新关系
					iscnsNewLongClick = true;
					return false;
				}
				if (MSociality.isMeeting(mSociality.getType())
						|| mSociality.getType() == MSociality.NOTICE
						|| mSociality.getType() == MSociality.INVITATION) {
					return false;
				}
				Rect frame = new Rect();
				getActivity().getWindow().getDecorView()
						.getWindowVisibleDisplayFrame(frame);
				int statusBarHeight = frame.top;
				int actionBarHeight = getActivity().getActionBar().getHeight();
				linmitHeight = statusBarHeight + actionBarHeight;
				/*
				 * if (mSociality.getType() == 3 || mSociality.getType() == 4 ||
				 * mSociality.getType() == 5) {
				 * save.setVisibility(View.VISIBLE); } else {
				 */
				save.setVisibility(View.GONE);
				/* } */
				deleteTv.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						deleteClickPosition = position;
						MSociality mSociality = listSocial.get(position - 1);
						int type = mSociality.getType();
						// 1单聊 2畅聊 3会议 4邀请函 5通知 :
						// 1-私聊，2-群聊，3-进行中的会议，4-未开始，5-已结束的会议，6-通知，7-邀请函 8-新关系
						// 0-没有会议
						int parametersType = 0;
						long userId2 = 0;
						long mucId = 0;
						if (type == MSociality.PRIVATE_CHAT) {
							userId2 = mSociality.getId();
							parametersType = 1;
						} else if (type == MSociality.GROUP_CHAT
								|| type == MSociality.MEETING
								|| type == MSociality.NOT_BEGINNING_MEETING
								|| type == MSociality.ENDED_MEETING) {
							mucId = mSociality.getId();
							parametersType = (MSociality.isMeeting(type)) ? 3
									: 2;
						} else if (type == MSociality.NOTICE) {
							parametersType = 4;
						} else if (type == MSociality.INVITATION) {
							parametersType = 5;
						}
						SimpleDateFormat format = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						// 调删除社交列表中数据的接口
						ConferenceReqUtil.doRemoveSocial(getActivity(),
								FrgSociality.this, mSociality.getId(), 0, type,
								Long.valueOf(App.getUserID()),
								format.format(new Date()), mHandler);
						
						IMReqUtil.doclearUnreadMessageNumber(getActivity(),
								FrgSociality.this,
								Long.valueOf(App.getUserID()), userId2, mucId,
								parametersType, null, false);
						dismissPopwindow();
					}
				});
				save.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						ConferenceReqUtil.doChangeMyMemberMeetStatus(
								getActivity(), FrgSociality.this, listSocial
										.get(position - 1).getId(), Long
										.valueOf(App.getUserID()), 0, null);
						dismissPopwindow();
					}
				});

				window = new PopupWindow(deleteView, LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				window.setBackgroundDrawable(new ColorDrawable(-000000));
				window.setOutsideTouchable(true);
				int[] location = new int[2];
				view.getLocationInWindow(location);
				int width = getActivity().getWindowManager()
						.getDefaultDisplay().getWidth();
				if (linmitHeight >= location[1]) {
					window.showAtLocation(parent, Gravity.LEFT + Gravity.TOP,
							width / 2 - (measuredWidth / 2), linmitHeight
									- measuredHeigh + 10);
				} else {
					window.showAtLocation(parent, Gravity.LEFT + Gravity.TOP,
							width / 2 - (measuredWidth / 2), location[1]
									- measuredHeigh);
				}
				return true;
			}
		});
	}

	/** 转发到 */
	private void showTransmitDialog() {
		final Dialog dialog = new Dialog(getActivity(),
				R.style.transmeeting_dialog);
		View dialogBg = View.inflate(getActivity(),
				R.layout.hy_dialog_meeting_regist, null);

		dialog.setContentView(dialogBg);
		// 转发到会议
		((TextView) dialogBg.findViewById(R.id.transmit_to_meeting_tv))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						dialog.dismiss();
					}
				});

		// 转发到畅聊
		((TextView) dialogBg.findViewById(R.id.transmit_to_chat_tv))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {

						dialog.dismiss();
					}
				});

		dialog.show();
	}

	private void dismissPopwindow() {
		if (window != null && window.isShowing()) {
			window.dismiss();
			window = null;
		}
	}

	@Override
	public void onPushMessage(String userId, MSociality mSociality) {
		listSocial.clear();
		List<MSociality> listSocialTemp = adapter.setListAddSocial(mSociality);
		if(adapter.isPushNew){
			adapter.isPushNew = false;
			handler.sendEmptyMessage(PUSH_NEW);
		}else{
			listSocial.addAll(listSocialTemp);
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (adapter != null) {
						adapter.notifyDataSetChanged();
						((MainActivity) getActivity())
								.updateNavigateNum(listSocial);
					}
				}
			});
		}
		// handler.sendEmptyMessage(4);
	}

	public void updateNewContactsTip() {
		ConnectionsReqUtil.getNewConnectionsCount(getActivity(),
				FrgSociality.this, new JSONObject(), null);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public List<MSociality> getListSocial() {
		return listSocial;
	}

	public void setListSocial(List<MSociality> listSocial) {
		this.listSocial = listSocial;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode!=getActivity().RESULT_OK){
			return;
		}
		switch(requestCode){
		case REQ_CHAT:
			IMBaseMessage chat_msg = (IMBaseMessage) data.getSerializableExtra("imbasemsg");
			if(chat_msg!=null){
				for(MSociality social:listSocial){
					if((social.getId()+"").equals(chat_msg.getSenderID()) || (social.getId()+"").equals(chat_msg.getRecvID())){
						if(!TextUtils.isEmpty(chat_msg.getSenderName())){
							social.getSocialDetail().setContent(chat_msg.getSenderName()+":"+chat_msg.getContent().replace(chat_msg.getSenderName(), ""));
						}
						social.setTime(chat_msg.getTime());
						adapter.notifyDataSetChanged();
						break;
					}
				}
			}
			break;
		case REQ_MUC:
			IMBaseMessage muc_msg = (IMBaseMessage) data.getSerializableExtra("imbasemsg");
			if(muc_msg!=null){
				for(MSociality social:listSocial){
					if((social.getId()+"").equals(muc_msg.getRecvID())){
						if(!TextUtils.isEmpty(muc_msg.getSenderName())){
							social.getSocialDetail().setContent(muc_msg.getSenderName()+":"+muc_msg.getContent().replace(muc_msg.getSenderName(), ""));
						}
						social.setTime(muc_msg.getTime());
						adapter.notifyDataSetChanged();
						break;
					}
				}
			}
			break;
		}
	}
	
}
