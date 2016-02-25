package com.tr.ui.communities.home;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tr.App;
import com.tr.R;
import com.tr.api.CommunityReqUtil;
import com.tr.model.im.FetchFriends;
import com.tr.model.im.Friend;
import com.tr.model.obj.ConnectionsMini;
import com.tr.model.obj.MUCDetail;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.communities.adapter.GroupMembersAdapter;
import com.tr.ui.communities.adapter.GroupMembersAdapter.GroupMembersBatchListener;
import com.tr.ui.communities.model.CommunityKickFromForBatch;
import com.tr.ui.communities.model.CommunityKickFromForBatchRequest;
import com.tr.ui.communities.model.CommunityNotify;
import com.tr.ui.communities.model.ImMucinfo;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.widgets.MessageDialog;
import com.tr.ui.widgets.MessageDialog.OnDialogFinishListener;
import com.tr.ui.widgets.NoScrollListview;
import com.tr.ui.widgets.title.menu.popupwindow.ActionItem;
import com.tr.ui.widgets.title.menu.popupwindow.TitlePopup;
import com.tr.ui.widgets.title.menu.popupwindow.TitlePopup.OnPopuItemOnClickListener;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.log.ToastUtil;
import com.utils.time.TimeUtil;

public class GroupMembersManagementActivity extends JBaseActivity implements IBindData, OnClickListener{
	@ViewInject(R.id.groupMembersKeywordEt)
	private EditText groupMembersKeywordEt ; 
	@ViewInject(R.id.groupMembersOwnerNslv)
	private NoScrollListview groupMembersOwnerNslv ;
	@ViewInject(R.id.groupMembersNslv)
	private NoScrollListview groupMembersNslv ;
	@ViewInject(R.id.text_transparent_line)
	private View headView ;
	private GroupMembersAdapter groupMembersAdapter;
	private MUCDetail mucDetail;
	private ConnectionsMini removeConnectionsMini;
	private GroupMembersAdapter groupMembersOwnerAdapter;
	private boolean isBatch;
	private TitlePopup titlePopup;
	@ViewInject(R.id.editRl)
	private LinearLayout editRl;
	@ViewInject(R.id.bannedToPostTv)
	private TextView bannedToPostTv;
	@ViewInject(R.id.relieveBannedToPostTv)
	private TextView relieveBannedToPostTv;
	@ViewInject(R.id.group_notice_all)
	private CheckBox group_notice_all;
	private String communityTitle;
	@ViewInject(R.id.deleteIv)
	private ImageView deleteIv;
	private ImMucinfo community;
	private String communityId;
	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, jabGetActionBar(), "管理群成员", false, null, true, true);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getBundle();
		setContentView(R.layout.activity_groupmembersmanagement);
		ViewUtils.inject(this);
		initView();
		initPopWindow();
		initData();
	}
	private void initData() {
		getData();
	}
	@Override
	protected void onStart() {
		super.onStart();
//		groupMembersKeywordEt.clearFocus();
	}
	private void getData() {
		CommunityReqUtil.doGetCommunityMemberList(this, Long.parseLong(communityId), this, null);
	}
	private void initView() {
		group_notice_all.setOnCheckedChangeListener(new  OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				for (int i = 0; i < groupMembersAdapter.getConnectionsMinis().size(); i++) {
					ConnectionsMini connectionsMini = groupMembersAdapter.getConnectionsMinis().get(i);
					if (isChecked) {
						groupMembersAdapter.setBatchAll(true);
						bannedToPostUserIds.add(connectionsMini.getId());
					}else{
						groupMembersAdapter.setBatchAll(false);
						bannedToPostUserIds.remove(connectionsMini.getId());
					}
					
				}
				
			}
		});
		bannedToPostTv.setOnClickListener(this);
		relieveBannedToPostTv.setOnClickListener(this);
		groupMembersOwnerAdapter = new GroupMembersAdapter(this,null,null);
		groupMembersOwnerAdapter.setOwner(true);
		groupMembersOwnerNslv.setAdapter(groupMembersOwnerAdapter);
		groupMembersAdapter = new GroupMembersAdapter(this , new com.tr.ui.communities.adapter.GroupMembersAdapter.GroupMembersOperationListener() {

			@Override
			public void transfer(final String userId,final ConnectionsMini connectionsMini) {
				
				final MessageDialog messageDialog = new MessageDialog(GroupMembersManagementActivity.this);
				final String name = TextUtils.isEmpty(connectionsMini.getShortName())?connectionsMini.getName():connectionsMini.getShortName();
				messageDialog.setContent("是否将群组转让给"+name +"?社群转让后将不会保留社群关联资源！");
				messageDialog.show();
				messageDialog.setOnDialogFinishListener(new OnDialogFinishListener(){
					private CommunityNotify notify;
					

					@Override
					public void onFinish(String content) {
						if ("确定".equals(content)) {
//							CommunityReqUtil.doAssignmentCommunity(GroupMembersManagementActivity.this, userId, communityId, GroupMembersManagementActivity.this, null);
							
							CommunityNotify notify = new CommunityNotify();
							notify.setCommunityId(community.getId());
							notify.setCommunityLogo(community.getPicPath());
							notify.setCommunityName(community.getTitle());
							notify.setApplicantId(Long.valueOf(connectionsMini.getId()));
							notify.setApplicantName(name);
							notify.setUserLogo(connectionsMini.getImage());
							notify.setAttendType(1);
							
							notify.setAcceptStatus(0);
							notify.setApplyReason("申请转让");
							notify.setNoticeType(1);
							notify.setCreatedUserId(Long.valueOf(App.getUserID()));
							notify.setCreatedUserLogo(App.getUserAvatar());
							notify.setCreatedUserName(App.getNick());
							notify.setCreatedTime(new Date().getTime());
							notify.setApplicantReadStatus(0);
							notify.setOwnerReadStatus(1);
							CommunityReqUtil.createNotice(GroupMembersManagementActivity.this, GroupMembersManagementActivity.this, notify, null);
							messageDialog.cancel();
						}}

					@Override
					public void onCancel(String content) {
						messageDialog.cancel();
					}
				});
			
			}
			
			@Override
			public void removeAndReport(final String userId, final ConnectionsMini connectionsMini) {
				final MessageDialog messageDialog = new MessageDialog(GroupMembersManagementActivity.this);
				messageDialog.setContent("确定举报并移除该成员吗？");
				messageDialog.show();
				messageDialog.setOnDialogFinishListener(new OnDialogFinishListener(){
					@Override
					public void onFinish(String content) {
						if ("确定".equals(content)) {
							removeConnectionsMini = connectionsMini;
							CommunityReqUtil.doKickFromMUC(GroupMembersManagementActivity.this, userId, communityId, GroupMembersManagementActivity.this, null);
							CommunityReqUtil.doReport(GroupMembersManagementActivity.this,communityId, App.getUserID(), userId, "2", GroupMembersManagementActivity.this, null);
							messageDialog.cancel();
						}}

					@Override
					public void onCancel(String content) {
						messageDialog.cancel();
					}
				});
				
				
			}
			
			@Override
			public void remove(final String userId, final ConnectionsMini connectionsMini) {
				final MessageDialog messageDialog = new MessageDialog(GroupMembersManagementActivity.this);
				messageDialog.setContent("确定移除该成员吗？");
				messageDialog.show();
				messageDialog.setOnDialogFinishListener(new OnDialogFinishListener(){
					@Override
					public void onFinish(String content) {
						if ("确定".equals(content)) {
							removeConnectionsMini = connectionsMini;
							CommunityReqUtil.doKickFromMUC(GroupMembersManagementActivity.this, userId, communityId, GroupMembersManagementActivity.this, null);
							messageDialog.cancel();
						}}

					@Override
					public void onCancel(String content) {
						messageDialog.cancel();
					}
				});
			}
			@Override
			public void bannedToPost(final String userId,final String status) {
				final MessageDialog messageDialog = new MessageDialog(GroupMembersManagementActivity.this);
				if ("2".equals(status)) {
					messageDialog.setContent("确定禁言该成员吗？");
				}else{
					messageDialog.setContent("确定解除该成员禁言吗？");
				}
				
				messageDialog.show();
				messageDialog.setOnDialogFinishListener(new OnDialogFinishListener(){
					@Override
					public void onFinish(String content) {
						if ("确定".equals(content)) {
							CommunityReqUtil.doGetNoTalk(GroupMembersManagementActivity.this, userId, communityId, status, GroupMembersManagementActivity.this, null);
							messageDialog.cancel();
						}}

					@Override
					public void onCancel(String content) {
						messageDialog.cancel();
					}
				});
				
			}
		},new  GroupMembersBatchListener() {
			
			@Override
			public void batchClick(String userId, boolean isCheck) {
				if (isCheck) {
					bannedToPostUserIds.add(userId);
				}else{
					bannedToPostUserIds.remove(userId);
				}
			}
		});
		groupMembersAdapter.setOwner(false);
		groupMembersNslv.setAdapter(groupMembersAdapter);
		groupMembersNslv.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				groupMembersAdapter.setBatch(true);
				isBatch = true;
				groupMembersAdapter.notifyDataSetChanged();
				editRl.setVisibility(View.VISIBLE);
				delete.setVisible(true);
				more.setVisible(false);
				return true;
			}
		});
		groupMembersNslv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				ConnectionsMini connectionsMini = groupMembersAdapter.getItem(arg2);
				ENavigate.startRelationHomeActivity(GroupMembersManagementActivity.this, connectionsMini.getId(),true,1);
			}
		});
		groupMembersKeywordEt.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if (!TextUtils.isEmpty(s.toString())) {
					groupMembersAdapter.setKeyWord(s.toString());
				}else{
					groupMembersAdapter.setConnectionsMinis(groupMembersList);
					groupMembersAdapter.notifyDataSetChanged();
				}
			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_groupmembers_more, menu);
		more = menu.findItem(R.id.more);
		delete = menu.findItem(R.id.delete);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId()== R.id.more) {
			titlePopup.show(headView);
		}else if (item.getItemId()== android.R.id.home) {
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable(ENavConsts.EMucDetail, mucDetail);
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
		}else if (item.getItemId()== R.id.delete) { //批量移除
			if (bannedToPostUserIds.isEmpty()) {
				ToastUtil.showToast(GroupMembersManagementActivity.this, "请选择成员");
				return false;
			}
			final MessageDialog messageDialog = new MessageDialog(GroupMembersManagementActivity.this);
			messageDialog.setContent("确定移除选中成员吗？");
			messageDialog.show();
			messageDialog.setOnDialogFinishListener(new OnDialogFinishListener(){
				@Override
				public void onFinish(String content) {
					if ("确定".equals(content)) {
						ArrayList<Long> longIds = new ArrayList<Long>(); 
						for (int i = 0; i < bannedToPostUserIds.size(); i++) {
							String string = bannedToPostUserIds.get(i);
							longIds.add(Long.parseLong(string));
						}
						CommunityKickFromForBatchRequest forBatchRequest = new CommunityKickFromForBatchRequest();
						forBatchRequest.mucId =  Long.parseLong(communityId);
						forBatchRequest.operatorUserId = App.getUserID();
						forBatchRequest.userIds = longIds;
						CommunityReqUtil.doKickFromMUCForBatch(GroupMembersManagementActivity.this,forBatchRequest, GroupMembersManagementActivity.this, null);
						messageDialog.cancel();
					}}

				@Override
				public void onCancel(String content) {
					messageDialog.cancel();
				}
			});
		}
		return super.onOptionsItemSelected(item);
	}
	private void initPopWindow() {
		// 实例化标题栏弹窗
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		titlePopup.setItemOnClickListener(onitemClick);
		titlePopup.addAction(new ActionItem(this, "发言时间"));
		titlePopup.addAction(new ActionItem(this, "登录时间"));
		titlePopup.addAction(new ActionItem(this, "加入时间"));

	}
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
	}
	private OnPopuItemOnClickListener onitemClick = new OnPopuItemOnClickListener() {
		
		@Override
		public void onItemClick(ActionItem item, int position) {
			if(item.mTitle.equals("发言时间")){
				
				CommunityReqUtil.doGetMessageTime(GroupMembersManagementActivity.this, communityId, GroupMembersManagementActivity.this, null);
//				//由于后台返回数据问题，导致先将登录时间来模拟发言时间。
//				Collections.sort(groupMembersAdapter.getConnectionsMinis(),new Comparator<ConnectionsMini>() {
//
//					@Override
//					public int compare(ConnectionsMini lhs, ConnectionsMini rhs) {
//						 if(TimeUtil.getCommunitiesTimeMillis(lhs.loginTime)>TimeUtil.getCommunitiesTimeMillis(rhs.loginTime)){
//						        return -1;
//						    }else if(TimeUtil.getCommunitiesTimeMillis(lhs.loginTime)<TimeUtil.getCommunitiesTimeMillis(rhs.loginTime)){
//						        return 1;
//						    }else{
//						        return 0;
//						    }
//					}
//				});
//				groupMembersAdapter.notifyDataSetChanged();
			}else if(item.mTitle.equals("登录时间")){
				Collections.sort(groupMembersAdapter.getConnectionsMinis(),new Comparator<ConnectionsMini>() {

					@Override
					public int compare(ConnectionsMini lhs, ConnectionsMini rhs) {
						 if(TimeUtil.getCommunitiesTimeMillis(lhs.loginTime)>TimeUtil.getCommunitiesTimeMillis(rhs.loginTime)){
						        return -1;
						    }else if(TimeUtil.getCommunitiesTimeMillis(lhs.loginTime)<TimeUtil.getCommunitiesTimeMillis(rhs.loginTime)){
						        return 1;
						    }else{
						        return 0;
						    }
					}
				});
				groupMembersAdapter.notifyDataSetChanged();
			}else if(item.mTitle.equals("加入时间")){
				Collections.sort(groupMembersAdapter.getConnectionsMinis(),new Comparator<ConnectionsMini>() {

					@Override
					public int compare(ConnectionsMini lhs, ConnectionsMini rhs) {
						 if(TimeUtil.getCommunitiesTimeMillis(lhs.joinTime)>TimeUtil.getCommunitiesTimeMillis(rhs.joinTime)){
						        return -1;
						    }else if(TimeUtil.getCommunitiesTimeMillis(lhs.joinTime)<TimeUtil.getCommunitiesTimeMillis(rhs.joinTime)){
						        return 1;
						    }else{
						        return 0;
						    }
					}
				});
				groupMembersAdapter.notifyDataSetChanged();
			}
		}
	};
	
	private FetchFriends ffs;
	private List<String> bannedToPostUserIds = new ArrayList<String>();
	private List<ConnectionsMini> groupMembersList = new ArrayList<ConnectionsMini>();
	private MenuItem more;
	private MenuItem delete;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK &&isBatch) {
			bannedToPostUserIds.clear();
			groupMembersAdapter.setBatch(false);
			groupMembersAdapter.notifyDataSetChanged();
			editRl.setVisibility(View.GONE);
			delete.setVisible(false);
			more.setVisible(true);
			isBatch = false;
			return false;
		} else if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable(ENavConsts.EMucDetail, mucDetail);
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			return super.onKeyDown(keyCode, event);
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}
	private void getBundle() {
		community = (ImMucinfo) getIntent().getSerializableExtra("community");
		communityId = community.getId()+"";
	}
	public JSONArray getFriendIds(MUCDetail mucdetail){
		JSONArray friendIds = new JSONArray();
		for(ConnectionsMini conn:mucdetail.getListConnectionsMini()){
			friendIds.put(Long.valueOf(conn.getId()));
		}
		return friendIds;
	}
	@Override
	public void bindData(int tag, Object object) {
		if (object!=null) {
			switch (tag) {
			case EAPIConsts.CommunityReqType.TYPE_ORDER_USER_BY_TIME: //发言时间
				HashMap<String, List<String> > timeMap = (HashMap<String, List<String>>) object;
				final List<String> timeIdList = timeMap.get("responseData");
				final ArrayList<ConnectionsMini> connectionsMinis = new ArrayList<ConnectionsMini>();
				new AsyncTask<Void, Void, ArrayList<ConnectionsMini>>(){

					@Override
					protected ArrayList<ConnectionsMini> doInBackground(Void... params) {
						for (int j = 0; j < timeIdList.size(); j++) {
							for (int i = 0; i < groupMembersAdapter.getConnectionsMinis().size(); i++) {
								ConnectionsMini connectionsMini = groupMembersAdapter.getConnectionsMinis().get(i);
								String connId = timeIdList.get(j);
								if (connectionsMini.getId().equals(connId)) {
									connectionsMinis.add(connectionsMini);
								}
							}
							}
						return connectionsMinis;
					}
					protected void onPostExecute(ArrayList<ConnectionsMini> result) {
						groupMembersAdapter.setConnectionsMinis(result);
						groupMembersAdapter.notifyDataSetChanged();
					};
				}.execute();
				
				break;
			case EAPIConsts.CommunityReqType.TYPE_KICK_FROM_MUC:	// 12..管理群成员-移除（单个）
				HashMap<String, MUCDetail> kickMap = (HashMap<String, MUCDetail>) object;
				mucDetail = kickMap.get("mucDetail");
				if (mucDetail!=null) {
					ToastUtil.showToast(GroupMembersManagementActivity.this, "移除成功");
					if (removeConnectionsMini!=null) {
							groupMembersAdapter.getConnectionsMinis().remove(removeConnectionsMini);
							groupMembersAdapter.notifyDataSetChanged();
					}
				}
				break;
			case EAPIConsts.CommunityReqType.TYPE_KICK_FROM_MUC_FOR_BATCH:	// 12..管理群成员-移除（批量）
				HashMap<String, ArrayList<CommunityKickFromForBatch>> kickFromForBatchMap = (HashMap<String, ArrayList<CommunityKickFromForBatch>>) object;
				bannedToPostUserIds.clear();
				ArrayList<CommunityKickFromForBatch> communityKickFromForBatchList = kickFromForBatchMap.get("responseData");
				if (communityKickFromForBatchList!=null&&!communityKickFromForBatchList.isEmpty()) {
					if (!TextUtils.isEmpty(communityKickFromForBatchList.get(0).tipMessage)) {
						ToastUtil.showToast(GroupMembersManagementActivity.this, communityKickFromForBatchList.get(0).tipMessage);

					}
				}
				group_notice_all.setChecked(false);
				groupMembersAdapter.setBatchAll(false);
				getData();
				break;
			case EAPIConsts.CommunityReqType.TYPE_MANAGE_COMMUNITY:
			case EAPIConsts.CommunityReqType.TYPE_MANAGE_COMMUNITY_BATCH:
				HashMap<String, Object> bannedToPostMap= (HashMap<String, Object>) object;
				Boolean isBannedToPostResponse = (Boolean) bannedToPostMap.get("isResponse");
				
				bannedToPostUserIds.clear();
				if (isBannedToPostResponse!=null&&isBannedToPostResponse) {
					group_notice_all.setChecked(false);
					groupMembersAdapter.setBatchAll(false);
					getData();
				}
				break;
			case EAPIConsts.CommunityReqType.TYPE_CREATE_COMMUNITY_NOTICE:
				HashMap<String, Object> assignentMap= (HashMap<String, Object>) object;
				String isAssignentResponse = (String) assignentMap.get("notifCode");
				if (isAssignentResponse!=null&&isAssignentResponse.contains("1")) {
					ToastUtil.showToast(GroupMembersManagementActivity.this, "转让通知已经发送");
					
//					groupMembersOwnerAdapter.getConnectionsMinis().clear();
//					groupMembersOwnerAdapter.getConnectionsMinis().add(transferConnectionsMini);
//					groupMembersAdapter.getConnectionsMinis().remove(transferConnectionsMini);
//					groupMembersOwnerAdapter.notifyDataSetChanged();
//					groupMembersAdapter.notifyDataSetChanged();
				}
				break;
			case EAPIConsts.CommunityReqType.TYPE_GET_COMMUNITY_MEMBER_LIST://获取成员详情
				groupMembersList.clear();
				groupMembersOwnerAdapter.getConnectionsMinis().clear();
				HashMap<String, MUCDetail> map = (HashMap<String, MUCDetail>) object;
				mucDetail = map.get("mucDetail");
						 Iterator<ConnectionsMini> iterator = mucDetail.getListConnectionsMini().iterator();
						 while (iterator.hasNext()) {
							 ConnectionsMini connectionsMini2 = iterator.next();
							 if (!App.getUserID().equals( connectionsMini2.getId())) {
									groupMembersList.add(connectionsMini2);
								}else{
									groupMembersOwnerAdapter.getConnectionsMinis().add(connectionsMini2);
								}
						}
						groupMembersAdapter.setConnectionsMinis(groupMembersList);
						groupMembersOwnerAdapter.notifyDataSetChanged();
						groupMembersAdapter.notifyDataSetChanged();
				if (mucDetail!=null) {
					CommunityReqUtil.fetchFirends(this, this, mucDetail.getId(), Long.valueOf(App.getUserID()), getFriendIds(mucDetail), null);
				}
				break;
			case EAPIConsts.IMReqType.IM_REQ_FETCHFIRENDS:
				ffs = (FetchFriends) object;
				if(ffs!=null){
					
					new AsyncTask<Void, Void, List<ConnectionsMini> >() {

						@Override
						protected List<ConnectionsMini> doInBackground(
								Void... params) {
							for(Friend friend:ffs.getFirends()){
								for (int i = 0; i < groupMembersAdapter.getConnectionsMinis().size(); i++) {
									ConnectionsMini connectionsMini = groupMembersAdapter.getConnectionsMinis().get(i);
									if (connectionsMini.getId().equals(friend.getFirendId()+"")) {
										connectionsMini.setFriendState(2);
									}
								}
							}
							return groupMembersAdapter.getConnectionsMinis();
						}
						protected void onPostExecute(java.util.List<ConnectionsMini> result) {
							groupMembersAdapter.setConnectionsMinis(result);
							groupMembersAdapter.notifyDataSetChanged();
						};
					}.execute();
				}
				break;

			default:
				break;
			}
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.relieveBannedToPostTv: //批量操作-解除禁言
			if (bannedToPostUserIds.isEmpty()) {
				ToastUtil.showToast(GroupMembersManagementActivity.this, "请选择成员");
				return ;
			}
			final MessageDialog relieveBannedToPostMessageDialog = new MessageDialog(GroupMembersManagementActivity.this);
			relieveBannedToPostMessageDialog.setContent("是否批量解除禁言选中成员？");
			relieveBannedToPostMessageDialog.show();
			relieveBannedToPostMessageDialog.setOnDialogFinishListener(new OnDialogFinishListener(){
				@Override
				public void onFinish(String content) {
					if ("确定".equals(content)) {
						String bannedToPostUserIdsStr = bannedToPostUserIds.toString();
						String replace = bannedToPostUserIdsStr.replace("[", "");
						String replace2 = replace.replace("]", "");
						String replace3 = replace2.replace(" ", "");
						CommunityReqUtil.doGetNoTalkBatck(GroupMembersManagementActivity.this, replace3 , communityId, "1", GroupMembersManagementActivity.this, null);
						relieveBannedToPostMessageDialog.cancel();
					}}

				@Override
				public void onCancel(String content) {
					relieveBannedToPostMessageDialog.cancel();
				}
			});
			
			
			break;
		case R.id.bannedToPostTv: //批量操作-禁言
			if (bannedToPostUserIds.isEmpty()) {
				ToastUtil.showToast(GroupMembersManagementActivity.this, "请选择成员");
				return ;
			}
			final MessageDialog bannedToPostMessageDialog = new MessageDialog(GroupMembersManagementActivity.this);
			bannedToPostMessageDialog.setContent("是否批量禁言选中成员？");
			bannedToPostMessageDialog.show();
			bannedToPostMessageDialog.setOnDialogFinishListener(new OnDialogFinishListener(){
				@Override
				public void onFinish(String content) {
					if ("确定".equals(content)) {
						String bannedToPostUserIdsStr = bannedToPostUserIds.toString();
						String replace = bannedToPostUserIdsStr.replace("[", "");
						String replace2 = replace.replace("]", "");
						String replace3 = replace2.replace(" ", "");
						CommunityReqUtil.doGetNoTalkBatck(GroupMembersManagementActivity.this, replace3 , communityId, "2", GroupMembersManagementActivity.this, null);
						bannedToPostMessageDialog.cancel();
					}}

				@Override
				public void onCancel(String content) {
					bannedToPostMessageDialog.cancel();
				}
			});
			
			break;

		default:
			break;
		}
	}
}
