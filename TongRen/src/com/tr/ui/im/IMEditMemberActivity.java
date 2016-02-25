package com.tr.ui.im;

/** 聊天设置页面，聊天详情页 */

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import com.tr.App;
import com.tr.R;
import com.tr.api.IMReqUtil;
import com.tr.db.ChatRecordDBManager;
import com.tr.db.SocialityDBManager;
import com.tr.model.SimpleResult;
import com.tr.model.im.ChatDetail;
import com.tr.model.im.FetchFriends;
import com.tr.model.im.Friend;
import com.tr.model.obj.Connections;
import com.tr.model.obj.ConnectionsMini;
import com.tr.model.obj.MUCDetail;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.widgets.EditTextAlertDialog;
import com.tr.ui.widgets.EditTextAlertDialog.OnEditDialogClickListener;
import com.tr.ui.widgets.IMEditMumberGrid;
import com.tr.ui.widgets.IMEditMumberGrid.HeadName;
import com.tr.ui.widgets.IMEditMumberGrid.ImageAdapter;
import com.tr.ui.widgets.MessageDialog;
import com.tr.ui.widgets.MessageDialog.OnDialogFinishListener;
import com.utils.common.EUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class IMEditMemberActivity extends JBaseActivity implements IBindData,
		OnClickListener {
	private final static int FROM_MUC_SET2MODIFY = 2000;

	private Integer[] mThumbIds = { R.drawable.face0, R.drawable.face0,
			R.drawable.face0, R.drawable.face0 };
	IMEditMumberGrid mGridView;
	// boolean removeState = false;
	private MUCDetail mucDetail;
	private ChatDetail chatDetail;
	private String fromActivityName;

	private View mViewTitleBG;// 畅谈标题背景
	private TextView mTextViewTitle;// 畅谈标题
	private View mViewSubjectBG;// 畅谈主题背景
	private TextView mTextViewSubject;// 会议主题
	private TextView mTextViewSubjectTitle;// 会议主题标题

	private View mViewMaxBG;// 人数上限背景
	private View mViewStickBG;// 置顶背景
	private View mViewNewMessageBG;// 新消息通知背景
	// private View mViewExitBG;//退出按钮背景

	private View mViewCleanBG;// 清除记录背景
	private View mViewSearchBG; // 查找聊天记录
	private ToggleButton mTBStick;// 置顶开关
	private ToggleButton mTBNotify;// 新消息通知开关
	private Button mBtnExit;
	private boolean mBlnCleanHistory = false; // 是否清空聊天记录
	private ChatRecordDBManager chatRecordDBManager; // 本地数据库管理
	private final static int REQUEST_MUC_SET = 200;
	private FetchFriends ffs;
	private ArrayList<String> friendIds = new ArrayList<String>();
	private String createdUserId;
	ImageAdapter imageAdapter;

	private List<ConnectionsMini> listConnectionsMini = new ArrayList<ConnectionsMini>();// 用户列表

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.im_editmumberview);
		getParam();
		initView();

		mGridView = (IMEditMumberGrid) findViewById(R.id.myGrid);
		ArrayList<HeadName> data = new ArrayList<HeadName>();

		imageAdapter = new ImageAdapter(this, data, 4, eStartIMGroupChatType);
		mGridView.setAdapter(imageAdapter);

		// grid长按事件
		mGridView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (mucDetail == null || eStartIMGroupChatType == 1)
					return true;

				int len = imageAdapter.getCount()-1;
				//这时不显示最后的添加
				if (1==eStartIMGroupChatType) {
					len = imageAdapter.getCount();
				}
				if (arg2 < (len)) {

					String myID = App.getUserID();
					String compereID = mucDetail.getCompereID();
					if(myID.equalsIgnoreCase(compereID))
					{
						// 自己是主持人才能移除

						imageAdapter.setRemoveState(true);
						imageAdapter.notifyDataSetChanged();
					}
				}
				return true;
			}
		});

		// 点击事件
		mGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// Toast.makeText(IMEditMumberActivity.this, "onItemClcik",
				// Toast.LENGTH_SHORT)
				// .show();
				int len = imageAdapter.getCount()-1;
				//这时不显示最后的添加
				if (1==eStartIMGroupChatType) {
					len = imageAdapter.getCount();
				}
				if (imageAdapter.getRemoveState()) {
					if (arg2 >= (len)) {
						imageAdapter.setRemoveState(!imageAdapter
								.getRemoveState());
						imageAdapter.notifyDataSetChanged();
					} else {
						// 踢出一个人
						// adapter.remove(arg2);
						// showToast("delete:" + arg2);
						if (chatDetail != null) {
							// 私聊，不处理
						} else {
							// 群聊，踢人
							HeadName item = (HeadName) imageAdapter.getItem(arg2);
							String destID = item.getUserID();
							String myID = App.getUserID();
							String compereID = mucDetail.getCompereID();
							if (myID.equalsIgnoreCase(destID)) {
								showToast("不允许移除自己");
							} else if (destID.equalsIgnoreCase(compereID)) {
								showToast("不允许移除会议主持人");
							} else {
								kickFromMuc(destID);
							}
						}
					}

				} else { //
					if (arg2 == (len)) {
						// adapter.add();
						// 跳到加人界面
						gotoInvite();
					} else {
						// 跳转到某个用户的具体人脉界面
//						String destID = listConnectionsMini.get(arg2).getId();
						HeadName item = (HeadName) imageAdapter.getItem(arg2);
						// 用户
						if (item.getType() == 0) {

							ENavigate.startRelationHomeActivity(
									IMEditMemberActivity.this, item.getUserID());
						}
						// 组织
						else {
							// ENavigate.startUserDetailsActivity(IMEditMemberActivity.this,
							// destID, true, 0);
							ENavigate.startOrgMyHomePageActivityByUseId(
									IMEditMemberActivity.this,
									Long.parseLong(item.getUserID()));
						}
					}
				}
			}
		});

//		updateData();

		// 本地数据库管理
		chatRecordDBManager = new ChatRecordDBManager(this);
/*<<<<<<< HEAD
		if (mucDetail != null) {
			IMReqUtil.fetchFirends(this, this, mucDetail.getId(), Long.valueOf(App.getUserID()), getFriendIds(mucDetail), null);
			showLoadingDialog();
=======*/
		if(mucDetail!=null){
			IMReqUtil.fetchFirends(this, this, mucDetail.getId(), Long.valueOf(App.getUserID()), getFriendIds(mucDetail), null);
		}else{
			updateData();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home: {
			doFinish();
			return true;
		}
		}
		return super.onOptionsItemSelected(item);
	}

	public void gotoInvite() {
		ArrayList<Connections> listConnections = null;
		if (mucDetail != null) {
			List<ConnectionsMini> tempListConnectionsMini = mucDetail
					.getListConnectionsMini();
			if (tempListConnectionsMini != null
					&& tempListConnectionsMini.size() > 0) {
				listConnections = new ArrayList<Connections>();
				for (ConnectionsMini connectionsMini : tempListConnectionsMini) {
					listConnections.add(connectionsMini.toConnections());
				}
			}
		} else if (chatDetail != null) {
			listConnections = new ArrayList<Connections>();
			Connections connections = new Connections();
			connections.setID(chatDetail.getThatID());
			connections.setName(chatDetail.getThatName());
			listConnections.add(connections);
		}
		ENavigate.startIMRelationSelectActivity(this, mucDetail, chatDetail,
				REQUEST_MUC_SET, listConnections,null);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 可以根据多个请求代码来作相应的操作 从选择联系人页面返回群聊设置页
		if (REQUEST_MUC_SET == requestCode) {
			if (resultCode == RESULT_OK) {
				// 创建群聊成功后返回
				if (chatDetail != null) {
					// 之前是私聊， 创建群聊成功后，关闭当前页面，还要关闭前一个私聊界面，然后跳转到群聊界面
					MUCDetail nowDetail = (MUCDetail) data
							.getSerializableExtra(ENavConsts.EMucDetail);
					Intent intent = new Intent();
					intent.putExtra(ENavConsts.EMucDetail, nowDetail);
					setResult(RESULT_OK, intent);
					finish();
				} else {
					// 之前是群聊界面，继续保留在当前页面，读取出加人页面传递过来的mucdetail，并更新
					MUCDetail nowDetail = (MUCDetail) data
							.getSerializableExtra(ENavConsts.EMucDetail);
					if (nowDetail != null) {
						mucDetail = nowDetail;
					}
//					updateData();
					IMReqUtil.fetchFirends(this, this, mucDetail.getId(), Long.valueOf(App.getUserID()), getFriendIds(mucDetail), null);
				}
			} else {
				// 在群聊页面没完成操作，继续保留在该页面
			}
		} else if (FROM_MUC_SET2MODIFY == requestCode) {
			if (resultCode == RESULT_OK) {
				// 去修改会议主题界面, 修改成功后返回
				mucDetail = (MUCDetail) data
						.getSerializableExtra(ENavConsts.EMucDetail);
				updateData();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// 读取传入的参数
	public void getParam() {
		Intent intent = getIntent();
		fromActivityName = intent.getStringExtra(ENavConsts.EFromActivityName);
		mucDetail = (MUCDetail) intent
				.getSerializableExtra(ENavConsts.EMucDetail);
		chatDetail = (ChatDetail) intent
				.getSerializableExtra(ENavConsts.EChatDetail);

		eStartIMGroupChatType = intent.getIntExtra(
				ENavConsts.EStartIMGroupChatType, 0);
		isMemberO = intent.getBooleanExtra("isMemberO", false);
		
		if (chatDetail != null) {
			ConnectionsMini mini = new ConnectionsMini();
			mini.setId(chatDetail.getThatID());
			mini.setImage(chatDetail.getThatImage());
			mini.setName(chatDetail.getThatName());
			mini.setType(chatDetail.getType());
			listConnectionsMini.add(mini);
		} else if (mucDetail != null) {
			listConnectionsMini = mucDetail.getListConnectionsMini();
		} else {
			finish();// 传入参数不对， 直接退出当前页面
		}
	}

	public void showModifyTitle(String msg) {
		EditTextAlertDialog editTextAlertDialog = new EditTextAlertDialog(IMEditMemberActivity.this, false);
		editTextAlertDialog.setTitle("畅聊名称");
		editTextAlertDialog.setInput(msg);
		editTextAlertDialog.show();
		editTextAlertDialog.setOnDialogClickListener(new OnEditDialogClickListener() {
			@Override
			public void onClick(String evaluationValue) {
				if(!TextUtils.isEmpty(evaluationValue)){
					modifyMUCTitle(evaluationValue);
				}
			}
		});
	}

	OnClickListener mHandlerViewListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == mViewTitleBG.getId()) {
				// 群聊标题修改
				if((eStartIMGroupChatType==1 && isMemberO) || eStartIMGroupChatType!=1){
					showModifyTitle(mTextViewTitle.getText().toString());
				}
			} else if (v.getId() == mViewSubjectBG.getId()) {
				if((eStartIMGroupChatType==1 && isMemberO) || eStartIMGroupChatType!=1){
					EditTextAlertDialog editTextAlertDialog = new EditTextAlertDialog(IMEditMemberActivity.this, false);
					editTextAlertDialog.setTitle("畅聊名称");
					editTextAlertDialog.setInput(mTextViewSubject.getText().toString());
					editTextAlertDialog.show();
					editTextAlertDialog.setOnDialogClickListener(new OnEditDialogClickListener() {
						@Override
						public void onClick(String evaluationValue) {
							if(!TextUtils.isEmpty(evaluationValue)){
								mTextViewSubject.setText(evaluationValue);
								modifyMUCTitle(evaluationValue);
							}
						}
					});
				}

			} else if (v.getId() == mViewCleanBG.getId()) {
				// 清除记录
				cleanRecord();
			} else if (v.getId() == mViewSearchBG.getId()) {
				// 查找记录
				String mucId = "";
				if (chatDetail != null) {
					mucId = chatDetail.getThatID();
					ENavigate.startChatRecordSearchActivity(
							IMEditMemberActivity.this, mucId, chatDetail);
				} else if (mucDetail != null) {
					mucId = mucDetail.getId() + "";
					ENavigate.startMUCRecordSearchActivity(
							IMEditMemberActivity.this, mucId, mucDetail);
				}
			} else if (v.getId() == mBtnExit.getId()) {
				MessageDialog messageDialog = new MessageDialog(IMEditMemberActivity.this);
				if (App.getUserID().equalsIgnoreCase(mucDetail.getCompereID())) {
					// 如果自己是主持人,显示 退出并解散当前讨论/ 否则显示 退出当前讨论
					messageDialog.setContent("确定退出并解散畅聊吗？");
				} else {
					messageDialog.setContent("确定退出畅聊吗？");
				}
				messageDialog.show();
				messageDialog.setOnDialogFinishListener(new OnDialogFinishListener(){
					@Override
					public void onFinish(String content) {
						// 退出
						exitFromMuc(App.getUserID());
						SocialityDBManager dbManager = SocialityDBManager
								.getInstance(IMEditMemberActivity.this);
						dbManager.delete(App.getUserID(), mucDetail.getId() + "", 2);
					}

					@Override
					public void onCancel(String content) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		}
	};

	private int eStartIMGroupChatType;
	private boolean isMemberO;

	private void initView() {
		mViewTitleBG = findViewById(R.id.im_detail_layout_title);
		mViewTitleBG.setOnClickListener(mHandlerViewListener);
		mTextViewTitle = (TextView) findViewById(R.id.im_detail_txt_title);
		mViewSubjectBG = findViewById(R.id.im_detail_layout_subject);
		mViewSubjectBG.setOnClickListener(mHandlerViewListener);

		mViewMaxBG = findViewById(R.id.im_detail_layout_max);
		mViewStickBG = findViewById(R.id.im_detail_layout_stick);
		mViewNewMessageBG = findViewById(R.id.im_detail_layout_notify);
		mViewCleanBG = findViewById(R.id.im_detail_layout_clean);
		// mViewExitBG = findViewById(R.id.im_detail_layout_exit);

		mViewCleanBG.setOnClickListener(mHandlerViewListener);

		mViewSearchBG = findViewById(R.id.im_detail_layout_search);
		mViewSearchBG.setOnClickListener(mHandlerViewListener);

		// if (getIntent().hasExtra(ENavConsts.EEnableSearchChatRecord) &&
		// getIntent().getBooleanExtra(ENavConsts.EEnableSearchChatRecord,
		// true)) {
		// mViewSearchBG.setVisibility(View.GONE);
		// }

		mTextViewSubjectTitle = (TextView) findViewById(R.id.im_detail_subject_title);
		mTextViewSubject = (TextView) findViewById(R.id.im_detail_txt_subject);
		mTBStick = (ToggleButton) findViewById(R.id.im_detail_tbtn_stick);
		mTBNotify = (ToggleButton) findViewById(R.id.im_detail_tbtn_notify);
		mBtnExit = (Button) findViewById(R.id.im_detail_exit_btn);
		if(eStartIMGroupChatType != 1) {
			mBtnExit.setOnClickListener(mHandlerViewListener);
		} else {
			this.mBtnExit.setVisibility(View.GONE);
		}

		mTBStick.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				setMucStick(isChecked);
			}
		});

		mTBNotify.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// showToast("新消息通知:" + isChecked);
				setNewMessageNotify(isChecked);
			}
		});
	}

	private void updateData() {
		// 设置几个栏目
		if (chatDetail != null) {
			// 从私聊进来
			this.mViewTitleBG.setVisibility(View.GONE);
			this.mViewSubjectBG.setVisibility(View.GONE);
			this.mViewMaxBG.setVisibility(View.GONE);
			this.mBtnExit.setVisibility(View.GONE);

			mViewCleanBG.setVisibility(View.VISIBLE);
		} else {
			// 从群聊进来
			if (TextUtils.isEmpty(mucDetail.getSubject())) {
				// 普通群聊
				this.mViewTitleBG.setVisibility(View.VISIBLE);
				this.mViewSubjectBG.setVisibility(View.VISIBLE);
				// this.mTextViewSubjectTitle.setText("升级为会议");
				mViewSubjectBG.setVisibility(View.GONE);
			} else {
				// 会议
				this.mViewTitleBG.setVisibility(View.GONE);
				this.mViewSubjectBG.setVisibility(View.VISIBLE);
				// this.mTextViewSubjectTitle.setText("会议主题");
				this.mTextViewSubject.setVisibility(View.VISIBLE);
				if (!StringUtils.isEmpty(mucDetail.getTitle())) {
					this.mTextViewSubject.setText(mucDetail.getTitle());
				}else{
					this.mTextViewSubject.setText(mucDetail.getSubject());
				}
			}

			this.mViewMaxBG.setVisibility(View.GONE);
			
			if(eStartIMGroupChatType == 1) {
				this.mBtnExit.setVisibility(View.GONE);
			} else {
				this.mBtnExit.setVisibility(View.VISIBLE);
			}

			mViewCleanBG.setVisibility(View.VISIBLE);

//			ActionBar actionbar = getActionBar();
//			actionbar.setTitle("畅聊信息" + getMemberCount());
			HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), mucDetail.getTitle() + getMemberCount(), false, null, false, true);
			jabGetActionBar().setDisplayShowHomeEnabled(true);
			this.mTextViewTitle.setText(mucDetail.getTitle());

			if (App.getUserID().equalsIgnoreCase(mucDetail.getCompereID())) {
				// 如果自己是主持人,显示 退出并解散当前讨论/ 否则显示 退出当前讨论
				mBtnExit.setText("退出并解散畅聊");
			} else {
				mBtnExit.setText("退出畅聊");
			}
		}

		// 设置参与人
		if (chatDetail != null) {
			listConnectionsMini.clear();
			ConnectionsMini mini = new ConnectionsMini();
			mini.setId(chatDetail.getThatID());
			mini.setImage(chatDetail.getThatImage());
			mini.setName(chatDetail.getThatName());
			mini.setType(chatDetail.getType());
			listConnectionsMini.add(mini);
		} else if (mucDetail != null) {
			listConnectionsMini = mucDetail.getListConnectionsMini();
		}

		ArrayList<HeadName> data = new ArrayList<HeadName>();
		for (int i = 0; i < this.listConnectionsMini.size(); i++) {
			ConnectionsMini mini = listConnectionsMini.get(i
					% listConnectionsMini.size());
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
		this.mGridView.setData(data);

	}

	public String getMemberCount() {
		if (chatDetail != null)
			return "";
		else
			return "(" + mucDetail.getListConnectionsMini().size() + ")";
	}

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "畅聊信息", false, null, false, true);
		jabGetActionBar().setDisplayShowHomeEnabled(true);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	// 请求网络数据相关=========================================
	@Override
	public void bindData(int tag, Object object) {
		if (hasDestroy()) {
			return;
		}
		dismissLoadingDialog();

		switch (tag) {
		case EAPIConsts.IMReqType.IM_REQ_KICKFROMMUC:
		case EAPIConsts.IMReqType.IM_REQ_MODIFY_MUC: {
			// 踢人，修改参数，成功后更新界面
			if (object != null) {
				imageAdapter.setRemoveState(false);
				imageAdapter.notifyDataSetChanged();
				mucDetail = (MUCDetail) object;
				updateData();
			}
		}
			break;
		case EAPIConsts.IMReqType.IM_REQ_EXIT_MUC: {
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
		}
		case EAPIConsts.IMReqType.IM_REQ_CLEAN_MESSAGE: {
			if (object != null) {
				SimpleResult result = (SimpleResult) object;
				if (result.isSucceed()) {
					EUtil.showToast("清空聊天记录成功");
					// 清除本地聊天记录
					mBlnCleanHistory = true;
					if (chatDetail != null) {
						chatRecordDBManager.deleteAll(App.getUserID(),
								chatDetail.getThatID());
					} else {
						chatRecordDBManager.deleteAll(App.getUserID(),
								mucDetail.getId() + "");
					}
				}
			}
			break;
		}
		case EAPIConsts.IMReqType.IM_REQ_FETCHFIRENDS:
			ffs = (FetchFriends) object;
			if(ffs!=null){
				createdUserId = ffs.getCreatedUserId()+"";
				for(Friend friend:ffs.getFirends()){
					friendIds.add(friend.getFirendId()+"");
				}
			}
			updateData();
			break;
		default:
			break;
		}

	}

	// 将一个人从会议中踢出
	public void kickFromMuc(String connectionsID) {
		showLoadingDialog();
		IMReqUtil.kickFromMUC(App.getApp().getApplicationContext(), this, null,
				connectionsID, "" + mucDetail.getId());
	}

	// 从一个会议中退出
	public void exitFromMuc(String userID) {
		showLoadingDialog();
		IMReqUtil.exitFromMUC(App.getApp().getApplicationContext(), this, null,
				userID, "" + mucDetail.getId());
	}

	// 修改群聊标题
	public void modifyMUCTitle(String newTitle) {
		modifyMUC("title", newTitle);
	}

	// 设置群聊是否置顶
	public void setMucStick(boolean isStick) {
		modifyMUC("stick", isStick);
	}

	// 设置是否推送新消息
	public void setNewMessageNotify(boolean isNotify) {
		modifyMUC("notifyNewMessage", isNotify);
	}

	// 清除聊天记录
	public void cleanRecord() {
		MessageDialog messageDialog = new MessageDialog(IMEditMemberActivity.this);
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
				// TODO Auto-generated method stub
				
			}
		});
	}

	public void requestCleanRecord() {
		showLoadingDialog();
		String thatID = null;
		String mucID = null;
		if (chatDetail != null) {
			thatID = chatDetail.getThatID();
		} else {
			mucID = mucDetail.getId() + "";
		}
		// 清除线上
		IMReqUtil.cleanIMRecord(this, this, null, thatID, mucID);
	}

	// 修改群聊信息
	public void modifyMUC(String key, Object value) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("id", mucDetail.getId());
			obj.put("groupId", mucDetail.getGroupId());
			obj.put(key, value);
			showLoadingDialog();
			IMReqUtil.modifyMuc(App.getApp().getApplicationContext(), this,
					null, obj);
		} catch (Exception e) {
			return;
		}
	}

	@Override
	public void onBackPressed() {
		doFinish();
	}

	/** 自定义finish,如果是muc,则需要 将修改后的detail返回给之前的群聊页面 */
	private void doFinish() {
		if (mucDetail != null) {
			Intent intent = new Intent();
			intent.putExtra(ENavConsts.EMucDetail, mucDetail);
			if (mBlnCleanHistory) {
				intent.putExtra(ENavConsts.EIMNotifyCleanHistory, "true");
			}
			setResult(RESULT_OK, intent);
		} else {
			if (mBlnCleanHistory) {
				// 如果清空当前聊天记录，则通知聊天详情页重新加载聊天记录
				Intent intent = new Intent();
				intent.putExtra(ENavConsts.EIMNotifyCleanHistory, "true");
				setResult(RESULT_OK, intent);
			}
		}
		finish();
	}
	


	public JSONArray getFriendIds(MUCDetail mucdetail){
		JSONArray friendIds = new JSONArray();
		for(ConnectionsMini conn:mucdetail.getListConnectionsMini()){
			friendIds.put(Long.valueOf(conn.getId()));
		}
		return friendIds;
	}

}
