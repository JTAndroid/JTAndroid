package com.tr.ui.communities.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tr.App;
import com.tr.R;
import com.tr.api.CommunityReqUtil;
import com.tr.api.IMReqUtil;
import com.tr.db.SocialityDBManager;
import com.tr.model.SimpleResult;
import com.tr.model.im.FetchFriends;
import com.tr.model.im.Friend;
import com.tr.model.obj.Connections;
import com.tr.model.obj.ConnectionsMini;
import com.tr.model.obj.MUCDetail;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.communities.adapter.MemberAdapter;
import com.tr.ui.communities.model.CommunityUserSetting;
import com.tr.ui.communities.model.ImMucinfo;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.widgets.MessageDialog;
import com.tr.ui.widgets.IMEditMumberGrid.HeadName;
import com.tr.ui.widgets.MessageDialog.OnDialogFinishListener;
import com.tr.ui.im.IMEditMemberActivity;
import com.tr.ui.widgets.IMEditMumberGrid.HeadName;
import com.tr.ui.widgets.MessageDialog.OnDialogFinishListener;
import com.tr.ui.widgets.MessageDialog;
import com.utils.common.EUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * 社群聊天设置
 * 
 * @author cui
 * 
 */
public class CommunityChatSettingActivity extends JBaseActivity implements OnClickListener, IBindData {
	private static final int GROUPMEMBERSMANAGEMENT = 2001;

	@ViewInject(R.id.grid_community_chat_set)
	private GridView grid_community_chat_set;// 显示群成员头像及昵称列表

	@ViewInject(R.id.group_notice)
	private RadioGroup group_notice;// 群消息提醒
	@ViewInject(R.id.group_notice_open)
	private RadioButton group_notice_open;// 群消息提醒开启
	@ViewInject(R.id.group_notice_silent)
	private RadioButton group_notice_silent;// 群消息提醒无声
	@ViewInject(R.id.group_notice_close)
	private RadioButton group_notice_close;// 群消息提醒关闭

	@ViewInject(R.id.item_my_nickname_ingroup)
	private LinearLayout item_my_nickname_ingroup;// 我在本群的昵称item
	@ViewInject(R.id.text_my_nickname_ingroup)
	private TextView text_my_nickname_ingroup;// 我在本群的昵称name

	@ViewInject(R.id.item_group_show_member_name)
	private RelativeLayout item_group_show_member_name;// 显示群的昵称item
	@ViewInject(R.id.text_group_show_member_name_cb)
	private CheckBox text_group_show_member_name_cb;// 显示群的昵称-显示

	@ViewInject(R.id.item_management_group_members)
	private RelativeLayout item_management_group_members;// 管理群成员item

	@ViewInject(R.id.item_group_chat_name)
	private LinearLayout item_group_chat_name;// 群聊的名称item
	@ViewInject(R.id.text_group_chat_name)
	private TextView text_group_chat_name;// 群聊的名称-name
	@ViewInject(R.id.text_group_chat_arrawIv)
	private ImageView text_group_chat_arrawIv;// 群聊的名称-箭头

	@ViewInject(R.id.text_empty_chat_record)
	private TextView text_empty_chat_record;// 清空聊天记录
	@ViewInject(R.id.text_find_chat_record)
	private TextView text_find_chat_record;// 查找聊天记录

	@ViewInject(R.id.dissolve_group)
	private TextView dissolve_group;// 解散该群
	/**
	 * 社群的社群实体
	 */
	private ImMucinfo community;
	private String communityId;
	private int applyType;
	private MemberAdapter adapter;
	private MUCDetail mucDetail;
	private FetchFriends ffs;
	private ArrayList<String> friendIds = new ArrayList<String>();
	private String createdUserId;

	public final static int REQUEST_MUC_SET = 200;

	private static final int EDITSETTINGCHATNAME = 1001;
	
	private CommunityUserSetting setting;

	private String communityEditSettingNickname;

	private String communityEditSettingChatname;

	private int EDITSETTINGNICKNAME = 1000;

	private String newMessageRemind;

	private String showNickname;

	private Boolean cleanRecord;
	
	private ArrayList<Connections> Connections;

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "社群设置", false, null, true, true);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_community_chat_set);
		ViewUtils.inject(this);
		getBundle();
		initViews();
		refreshNotice();
		initData();
		grid_community_chat_set.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				if(position == adapter.getCount()-1){
					// 跳到加人界面
					gotoInvite();
				}else{
					HeadName headname = (HeadName) adapter.getItem(position);
//					ENavigate.startHomePageActivity(CommunityChatSettingActivity.this, headname.getUserID());
					ENavigate.startRelationHomeActivity(CommunityChatSettingActivity.this, headname.getUserID(),true,1);
				}
			}
		});
		if (mucDetail!=null) {
			CommunityReqUtil.fetchFirends(this, this, mucDetail.getId(), Long.valueOf(App.getUserID()), getFriendIds(mucDetail), null);
	
		}
	}
	
	/**
	 * 刷新消息提醒设置
	 */
	private void refreshNotice() {
		// TODO Auto-generated method stub

	}

	private void getBundle() {
		community = (ImMucinfo) getIntent().getSerializableExtra("community");
		communityId = community.getId()+"";
		mucDetail = (MUCDetail) getIntent().getSerializableExtra(ENavConsts.EMucDetail);
		applyType = getIntent().getIntExtra("applyType", 1);
	}

	private void initViews() {
		// TODO Auto-generated method stub
		text_group_show_member_name_cb.setChecked(true);
		group_notice_open.setChecked(true);
		setMyOnClickAction(item_my_nickname_ingroup);
		setMyOnClickAction(item_group_show_member_name);
		setMyOnClickAction(item_management_group_members);
		setMyOnClickAction(item_group_chat_name);
		setMyOnClickAction(text_empty_chat_record);
		setMyOnClickAction(text_find_chat_record);
		setMyOnClickAction(dissolve_group);
	}

	private void setMyOnClickAction(View v) {
		v.setOnClickListener(this);
	}
	
	private void initData(){
	
		if(setting!=null){
			if(setting.getNewMessageRemind().equals("0")){
				group_notice_open.setChecked(true);
			}else if(setting.getNewMessageRemind().equals("2")){
				group_notice_close.setChecked(true);
			}
			text_my_nickname_ingroup.setText(setting.getNickname());
			communityEditSettingNickname = setting.getNickname();
			if(setting.getShowNickname().equals("0")){
				text_group_show_member_name_cb.setChecked(true);
			}else{
				text_group_show_member_name_cb.setChecked(false);
			}
		}
		if (App.getUserID().equals(createdUserId)) {
			item_management_group_members.setVisibility(View.VISIBLE);
			dissolve_group.setText("解散社群");
			text_group_chat_arrawIv.setVisibility(View.VISIBLE);
		}else{
			item_management_group_members.setVisibility(View.GONE);
			dissolve_group.setText("退出社群");
			text_group_chat_arrawIv.setVisibility(View.GONE);
		}
		if (mucDetail!=null) {
			
		text_group_chat_name.setText(mucDetail.getTitle());
		communityEditSettingChatname = mucDetail.getTitle();
		
		ArrayList<HeadName> data = new ArrayList<HeadName>();
		List<ConnectionsMini> listConnectionsMini = mucDetail.getListConnectionsMini();
		for (int i = 0; i < listConnectionsMini.size(); i++) {
			ConnectionsMini mini = listConnectionsMini.get(i % listConnectionsMini.size());
			HeadName headname = new HeadName(mini);
			if(friendIds.contains(mini.getId())){
				headname.setIsFriend(true);
			}
			if(mini.getId().equals(createdUserId)){
				headname.setCreater(true);
				data.add(0, headname);
				
			}else{
				data.add(headname);
			}
		}
		adapter = new MemberAdapter(this);
		adapter.setData(data);
		grid_community_chat_set.setAdapter(adapter);
		}
		
	}
	@Override
	protected void onDestroy() {
		
		super.onDestroy();
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.item_my_nickname_ingroup://我在本群的昵称
			ENavigate.startCommunityEditSettingActivity(CommunityChatSettingActivity.this, communityEditSettingNickname, "编辑本群昵称",EDITSETTINGNICKNAME );
			break;
		case R.id.item_group_show_member_name://显示群成员昵称
			
			break;
		case R.id.item_management_group_members://管理群成员
			Intent groupMembersIntent = new Intent(CommunityChatSettingActivity.this, GroupMembersManagementActivity.class);
			groupMembersIntent.putExtra("community",community);
			startActivityForResult(groupMembersIntent,  GROUPMEMBERSMANAGEMENT);
			break;
		case R.id.item_group_chat_name://群聊名称
			if (App.getUserID().equals(createdUserId)) {
				ENavigate.startCommunityEditSettingActivity(CommunityChatSettingActivity.this, communityEditSettingChatname, "编辑群聊名称",EDITSETTINGCHATNAME);
			}
			break;
		case R.id.text_empty_chat_record://清空聊天记录
			cleanRecord();
			break;
		case R.id.text_find_chat_record://查询
			getRecord();
			break;
		case R.id.dissolve_group://解散该群
			MessageDialog messageDialog = new MessageDialog(this);
			messageDialog.setTitle("");
			if (App.getUserID().equalsIgnoreCase(createdUserId)) {
				// 如果自己是主持人,显示 退出并解散当前讨论/ 否则显示 退出当前讨论
				messageDialog.setContent("确定解散该群吗？");
			} else {
				messageDialog.setContent("确定退出该群吗？");
			}
			messageDialog.show();
			messageDialog.setOnDialogFinishListener(new OnDialogFinishListener(){
				@Override
				public void onFinish(String content) {
					// 退出
					CommunityReqUtil.exitFromMUC(
							CommunityChatSettingActivity.this,
							CommunityChatSettingActivity.this, null,
							App.getUserID(), "" + mucDetail.getId());
				}

				@Override
				public void onCancel(String content) {
					
				}
			});
			
			break;

		default:
			break;
		}
	}

	@Override
	public void bindData(int tag, Object object) {
		if(hasDestroy()){
			return;
		}
		if (object!=null) {
			
		switch(tag){
		case EAPIConsts.IMReqType.IM_REQ_FETCHFIRENDS:
			ffs = (FetchFriends) object;
			if(ffs!=null){
				createdUserId = ffs.getCreatedUserId()+"";
				for(Friend friend:ffs.getFirends()){
					friendIds.add(friend.getFirendId()+"");
				}
			}
			
			if(createdUserId.equals(App.getUserID())){
				dissolve_group.setText("解散该群");
			}else{
				dissolve_group.setText("退出该群");
			}

			CommunityReqUtil.getSetDetail(this, this, communityId, createdUserId, null);
			break;
		case EAPIConsts.CommunityReqType.TYPE_MODIFY_MUC_NAME://修改社群名称
			HashMap<String, MUCDetail> newName =  (HashMap<String, MUCDetail>) object;
			if (null != newName) {
				mucDetail = (MUCDetail) newName.get("mucDetail");
				initData();
			}
			break;
		case EAPIConsts.CommunityReqType.TYPE_COMMUNITY_SET_DETAIL:
			HashMap<String, CommunityUserSetting>  hashMap= (HashMap<String, CommunityUserSetting>) object;
		
			if (hashMap.get("result") != null) {
				setting = hashMap.get("result");
				initData();
			}
			
			break;
		case EAPIConsts.IMReqType.IM_REQ_EXIT_MUC:
			if (object != null) {
				SimpleResult result = (SimpleResult) object;
				if (result.isSucceed()) {
					// 退出成功，退出当前畅谈设置页，并且要进一步退出畅谈页
					Intent intent = new Intent();
					setResult(RESULT_FIRST_USER, intent);
					finish();
				}
			}
			break;
		case EAPIConsts.IMReqType.IM_REQ_CLEAN_MESSAGE:
			if (object != null) {
				SimpleResult result = (SimpleResult) object;
				if (result.isSucceed()) {
					EUtil.showToast("清空聊天记录成功");
				}
			}
			break;
		}
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putSerializable(ENavConsts.EMucDetail, mucDetail);
			bundle.putSerializable(ENavConsts.redatas, Connections);
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			
			if (group_notice_open.isChecked()) {
				newMessageRemind = "0";
			}else if(group_notice_close.isChecked()){
				newMessageRemind = "2";
			}
			if (text_group_show_member_name_cb.isChecked()) {
				showNickname = "0";
			}else{
				showNickname = "1";
			}
			CommunityReqUtil.setSettingDetail(this, this, communityId, createdUserId, newMessageRemind, text_my_nickname_ingroup.getText().toString(), showNickname, null);
			return super.onKeyDown(keyCode, event);
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		 if (item.getItemId()== android.R.id.home) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable(ENavConsts.EMucDetail, mucDetail);
				bundle.putSerializable(ENavConsts.redatas, Connections);
				intent.putExtras(bundle);
				setResult(RESULT_OK, intent);
				
				if (group_notice_open.isChecked()) {
					newMessageRemind = "0";
				}else if(group_notice_close.isChecked()){
					newMessageRemind = "2";
				}
				if (text_group_show_member_name_cb.isChecked()) {
					showNickname = "0";
				}else{
					showNickname = "1";
				}
				CommunityReqUtil.setSettingDetail(this, this, communityId, createdUserId, newMessageRemind, text_my_nickname_ingroup.getText().toString(), showNickname, null);
				
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data!=null) {
		if (REQUEST_MUC_SET == requestCode) {
			if (resultCode == RESULT_OK) {
				MUCDetail nowDetail = (MUCDetail) data.getSerializableExtra(ENavConsts.EMucDetail);
				if (nowDetail != null) {
					Connections = (ArrayList<Connections>) data.getSerializableExtra(ENavConsts.redatas);
					mucDetail = nowDetail;
					CommunityReqUtil.fetchFirends(this, this, mucDetail.getId(), Long.valueOf(App.getUserID()), getFriendIds(mucDetail), null);
				}
			}
		}
		else if (EDITSETTINGNICKNAME == requestCode  ) {
			if (resultCode == RESULT_OK) {
				communityEditSettingNickname = data.getStringExtra("communityEditSettingData");
				text_my_nickname_ingroup.setText(communityEditSettingNickname);
			}
		}else if(EDITSETTINGCHATNAME == requestCode){
			if (resultCode == RESULT_OK) {
				communityEditSettingChatname = data.getStringExtra("communityEditSettingData");
				text_group_chat_name.setText(communityEditSettingChatname);
				CommunityReqUtil.setSettingTitle(this, this, communityId, text_group_chat_name.getText().toString(), null, null, cleanRecord, null);
			}
		}else if(GROUPMEMBERSMANAGEMENT == requestCode){
			if (resultCode == RESULT_OK) {
				MUCDetail managementDetail = (MUCDetail) data.getSerializableExtra(ENavConsts.EMucDetail);
				if (managementDetail != null) {
					mucDetail = managementDetail;
					initData();
				}
			}
		}
		}
	}
	
	public JSONArray getFriendIds(MUCDetail mucdetail){
		JSONArray friendIds = new JSONArray();
		List<ConnectionsMini> listConnectionsMini = mucdetail.getListConnectionsMini();
		for(ConnectionsMini conn:listConnectionsMini){
			friendIds.put(Long.valueOf(conn.getId()));
		}
		return friendIds;
	}
	
	public void gotoInvite() {
		ENavigate.startIMRelationSelectActivity(this, mucDetail, community, applyType, REQUEST_MUC_SET);
	}
	

	// 清除聊天记录
	public void cleanRecord() {
		MessageDialog messageDialog = new MessageDialog(this);
		messageDialog.setContent("你确定清空聊天记录？清空后将不能被找回。");
		messageDialog.show();
		messageDialog.setOnDialogFinishListener(new OnDialogFinishListener(){
			@Override
			public void onFinish(String content) {
				// 清空聊天记录
				requestCleanRecord();
			}

			@Override
			public void onCancel(String content) {
				
			}
		});
	}

	public void requestCleanRecord() {
		String mucID = null;
		mucID = mucDetail.getId() + "";
		// 清除线上
		CommunityReqUtil.cleanIMRecord(this, this, null, null, mucID);
	}
	
	public void getRecord(){
		// 查找记录
		String mucId = "";
		if (mucDetail != null) {
			mucId = mucDetail.getId() + "";
			ENavigate.startMUCRecordSearchActivity(this, mucId, mucDetail);
		}
	}

}
