package com.tr.ui.communities.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tr.App;
import com.tr.R;
import com.tr.api.CommunityReqUtil;
import com.tr.api.ConferenceReqUtil;
import com.tr.model.CommunityStateResult;
import com.tr.model.obj.MUCDetail;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.communities.adapter.MyCommunitiesAdapter;
import com.tr.ui.communities.model.Community;
import com.tr.ui.communities.model.CommunityApply;
import com.tr.ui.communities.model.ImMucinfo;
import com.tr.ui.communities.model.MyCommunitListData;
import com.tr.ui.communities.model.Notification;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.widgets.EditTextAlertDialog;
import com.tr.ui.widgets.NoticeDialog;
import com.tr.ui.widgets.EditTextAlertDialog.OnEditDialogClickListener;
import com.tr.ui.widgets.title.menu.popupwindow.ActionItem;
import com.tr.ui.widgets.title.menu.popupwindow.TitlePopup;
import com.tr.ui.widgets.title.menu.popupwindow.TitlePopup.OnPopuItemOnClickListener;
import com.utils.common.GlobalVariable;
import com.utils.http.EAPIConsts.CommunityReqType;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * @ClassName: MyCommunitiesActivity
 * @Description: 我的社群列表页面
 * @author cui
 * 
 */
public class MyCommunitiesActivity extends JBaseActivity implements
		IXListViewListener, IBindData {
	@ViewInject(R.id.new_xlistview)
	private XListView xListView;
	@ViewInject(R.id.text_transparent_line)
	private TextView view_line;
	private TitlePopup titlePopup;
	@ViewInject(R.id.EditTextSearch)
	private EditText EditTextSearch;

	private MyCommunitiesAdapter mAdapter;
	private List<MyCommunitListData> items = new ArrayList<MyCommunitListData>();
	private List<MyCommunitListData> copyitems = new ArrayList<MyCommunitListData>();//为了搜索保存原有的数据
	private int mycommunity = 1;// 默认为0；0代表是首页过来所有社群 1代表是我的社群
	private int index = 0;// 分页查询 从0开始
	private int size = 20;// 分页查询每次查20个
	private String communityNO;
	private Community community;
	private long communityId;
	private String notice;
	private ArrayList<CommunityStateResult> mCommunityStateResultList = new ArrayList<CommunityStateResult>(); // 返回数据
	private String communityShowRedStr = "";
	private String key;

	@Override
	public void initJabActionBar() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "我的社群",
				false, null, false, true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getBundle();
		setContentView(R.layout.activity_communities);
		ViewUtils.inject(this); // 注入view和事件..
		initControl();
		initXlistview();
		initPopWindow();
		getData();
	}

	@SuppressWarnings("unchecked")
	private void getBundle() {
		mCommunityStateResultList = (ArrayList<CommunityStateResult>) this
				.getIntent().getSerializableExtra("mCommunityStateResultList");
		if (mCommunityStateResultList != null) {
			communityShowRedStr = "";
			for (CommunityStateResult iterable_element : mCommunityStateResultList) {
				if (iterable_element.getNewCount() > 0
						&& iterable_element.getNewMessageRemind() == 0) {
					communityShowRedStr += iterable_element.getMucId() + "|";
				}
			}
		}
	}

	/**
	 * 从网络拿数据
	 */
	private void getData() {
		showLoadingDialog();
		CommunityReqUtil.doGetMyCommunityList(this,
				Long.parseLong(String.valueOf(App.getUserID())), index, size,
				this, null);
	}

	private void initPopWindow() {
		// 实例化标题栏弹窗
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		titlePopup.setItemOnClickListener(onitemClick);
		// 给标题栏弹窗添加子类
		// titlePopup.addAction(new ActionItem(this, R.string.creat_community,
		// R.drawable.chat_launch));
		// titlePopup.addAction(new ActionItem(this, R.string.add_group,
		// R.drawable.home_invitation_with_heart));
		titlePopup.addAction(new ActionItem(this, R.string.creat_community));
		titlePopup.addAction(new ActionItem(this, R.string.add_group));
	}

	private OnPopuItemOnClickListener onitemClick = new OnPopuItemOnClickListener() {

		@Override
		public void onItemClick(ActionItem item, int position) {
			switch (position) {
			case 0:// 创建社群
				Intent intent = new Intent(MyCommunitiesActivity.this,
						CreateCommunityAvctivity.class);
				startActivity(intent);
				break;
			case 1:// 群号加群
				EditTextAlertDialog etDialog = new EditTextAlertDialog(
						MyCommunitiesActivity.this);
				etDialog.setTitle("输入社群号加入社群");
				etDialog.setHint("");
				etDialog.setOkTv("加入");
				etDialog.setOnDialogClickListener(new OnEditDialogClickListener() {
					@Override
					public void onClick(String evaluationValue) {
						if (evaluationValue != null) {
							communityNO = evaluationValue;
							showLoadingDialog();
							CommunityReqUtil.existCommunityNo(
									MyCommunitiesActivity.this,
									MyCommunitiesActivity.this, communityNO,
									null);
						}
					}
				});
				etDialog.show();
				break;
			default:
				break;
			}
		}
	};
	private View empty;

	private void initControl() {
		mAdapter = new MyCommunitiesAdapter(this, items);
		empty = findViewById(R.id.empty);
		TextView common_text_empty = (TextView) findViewById(R.id.common_text_empty);
		common_text_empty.setText("暂无社群");
		EditTextSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				key=s.toString();
				if (!TextUtils.isEmpty(key)) {
					setKeyWord(key);
				} else {
					items=copyitems;
				}
				mynotify();
			}
		});
	}
	public void setKeyWord(String keyWord) {
		List<MyCommunitListData> keyWordImMucinfo = new ArrayList<MyCommunitListData>();
		for (int i = 0; i < copyitems.size(); i++) {
			MyCommunitListData myImMucinfo = copyitems.get(i);
			if (myImMucinfo.getTitle().contains(keyWord)) {
				keyWordImMucinfo.add(myImMucinfo);
			}
		}
//		if(null!=keyWordImMucinfo&&!keyWordImMucinfo.isEmpty()){
			items=keyWordImMucinfo;
			if(null!=items&&!items.isEmpty())
				empty.setVisibility(View.GONE);
			else
				empty.setVisibility(View.VISIBLE);
				
//		}
	}
	private void mynotify(){
		mAdapter.setList(items);
		mAdapter.notifyDataSetChanged();
	}
	private void initXlistview() {
		xListView.setPullLoadEnable(false);
		xListView.setPullRefreshEnable(true);
		xListView.setAdapter(mAdapter);
		xListView.setXListViewListener(this);
		xListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position==0) {
					return;
				}
				MyCommunitListData myCommunitListData = mAdapter . getItem(position - 1);
				Intent intent = new Intent(MyCommunitiesActivity.this,
						CommunitiesDetailsActivity.class);
				intent.putExtra(GlobalVariable.COMMUNITY_ID,
						myCommunitListData.getId());
				startActivityForResult(intent, ENavigate.REQUSET_CODE_MUC);
			}
		});
	}
	
	@Override
	public void onResume() {
		ConferenceReqUtil.getCommunityState(this, this, null, App.getUserID());
		super.onResume();
	}
	
	@Override
	public void onRefresh() {
		index = 0;
		getData();
	}

	private void onLoad() {
		xListView.stopRefresh();
		xListView.stopLoadMore();
	}

	@Override
	public void onLoadMore() {
		index++;
		getData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_new_main, menu);
		menu.findItem(R.id.home_new_menu_more).setIcon(
				R.drawable.ic_action_overflow);
		menu.findItem(R.id.home_new_menu_search).setVisible(false);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.home_new_menu_more:// 更多
			titlePopup.show(view_line);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void bindData(int tag, Object object) {
		HashMap<String, Object> dataMap;
		switch (tag) {
		case CommunityReqType.TYPE_MY_COMMUNITY_LIST:// 我的社群列表
			dataMap = (HashMap<String, Object>) object;
			onLoad();
			if (dataMap != null) {
				items = (List<MyCommunitListData>) dataMap.get("result");
				if (!items.isEmpty()) {
					xListView.setPullLoadEnable(false);
					mAdapter.setCommunityShowRedStr(communityShowRedStr);
					copyitems=items;
					if(!TextUtils.isEmpty(key))
						setKeyWord(key);
					mynotify();
					xListView.setVisibility(View.VISIBLE);
					empty.setVisibility(View.GONE);
				} else {
					xListView.setPullLoadEnable(false);
					xListView.setVisibility(View.GONE);
					empty.setVisibility(View.VISIBLE);
				}
			} else {
				xListView.setPullLoadEnable(false);
				xListView.setVisibility(View.GONE);
				empty.setVisibility(View.VISIBLE);
			}
			dismissLoadingDialog();
			break;
		case CommunityReqType.TYPE_EXIST_COMMUNITYNO:
			dataMap = (HashMap<String, Object>) object;
			if (dataMap != null) {
				boolean isExist = (Boolean) dataMap.get("isExist");
				if (isExist) {
					CommunityReqUtil.getCommunityByCommunityNo(this, this,
							communityNO, Long.valueOf(App.getUserID()), null);
				} else {
					dismissLoadingDialog();
					showToast("群号错误");
				}
			} else {
				dismissLoadingDialog();
				showToast("根据社群号获取社群基本信息失败！群号是：" + communityNO);
			}
			break;
		case CommunityReqType.TYPE_GET_COMMUNITY_BY_COMMUNITYNO:
			dataMap = (HashMap<String, Object>) object;
			if (dataMap != null) {
				CommunityApply commuapply = (CommunityApply) dataMap
						.get("result");
				Notification noti = (Notification) dataMap.get("notification");
				if (commuapply == null && noti != null) {
					dismissLoadingDialog();
					showToast(noti.getNotifInfo());
				} else if (commuapply != null) {
					communityId = commuapply.getCommunity().getId();
					community = commuapply.getCommunity();
					if (commuapply.getApplayType().equals(
							CommunityApply.APPLYTYPE_ALL)) {
						List<Long> list = new ArrayList<Long>();
						list.add(Long.parseLong(App.getApp().getUserID()));
						CommunityReqUtil.doInvite2Muc(this, communityId, list,
								this, null);
					} else if (commuapply.getApplayType().equals(
							CommunityApply.APPLYTYPE_REQ)) {
						CommunityReqUtil.getNotice(this, this, communityId,
								null);
					}
				}
			}
			break;
		case CommunityReqType.TYPE_INVITE2MUC:// 创建完后得邀请创建者进来才是真正创建成功
			dataMap = (HashMap<String, Object>) object;
			if (null != object) {
				ENavigate.startCommunityDetailsActivity(this, communityId, true);
				// dismissLoadingDialog();
			} else {
				showToast("加入不成功");
			}
			dismissLoadingDialog();
			break;
		case EAPIConsts.CommunityReqType.TYPE_GET_NOTICE:// 社群公告
			dataMap = (HashMap<String, Object>) object;
			dismissLoadingDialog();
			if (null != dataMap) {
				notice = (String) dataMap.get("notice");
				showNoticeDialog();
			} else {
				Intent intent = new Intent(this, JoinCommumitiesAvtivity.class);
				intent.putExtra(GlobalVariable.COMMUNITY_ID, communityId);
				intent.putExtra("req_number_community", community);
				startActivity(intent);
			}
			break;
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_COMMUNITY_STATE:
			mCommunityStateResultList = (ArrayList<CommunityStateResult>) object;
			if (mCommunityStateResultList != null) {
				communityShowRedStr = "";
				for (CommunityStateResult iterable_element : mCommunityStateResultList) {
					if (iterable_element.getNewCount() > 0
							&& iterable_element.getNewMessageRemind() == 0) {
						communityShowRedStr += iterable_element.getMucId()
								+ "|";
					}
				}
				mAdapter.setCommunityShowRedStr(communityShowRedStr);
				mAdapter.notifyDataSetChanged();
			}
			break;
		default:
			break;
		}
	}

	private void showNoticeDialog() {
		final NoticeDialog dialog = new NoticeDialog(this);
		dialog.setTitle("社群公告");
		dialog.setMessage(notice);
		dialog.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MyCommunitiesActivity.this,
						JoinCommumitiesAvtivity.class);
				intent.putExtra("req_number_community", community);
				startActivity(intent);
				dialog.dismiss();
			}
		});
	}

	 @Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent
	 data) {
	 if (ENavigate.REQUSET_CODE_MUC == requestCode) {
	 if (resultCode == RESULT_FIRST_USER) {
	 // 退出群聊成功,刷新页面
	 onRefresh();
	 }
	 }
	 }

}
