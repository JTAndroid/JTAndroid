package com.tr.ui.communities.home;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tr.App;
import com.tr.R;
import com.tr.api.CommunityReqUtil;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.common.view.XListView;
import com.tr.ui.common.view.XListView.IXListViewListener;
import com.tr.ui.communities.adapter.CommunitiesAdapter;
import com.tr.ui.communities.model.Community;
import com.tr.ui.communities.model.CommunityApply;
import com.tr.ui.communities.model.CommunityLabels;
import com.tr.ui.communities.model.ImMucinfo;
import com.tr.ui.communities.model.Label;
import com.tr.ui.communities.model.Notification;
import com.tr.ui.demand.MyView.CustomView;
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
 * @ClassName: CommunitiesActivity
 * @Description: 社群页面
 * @author cui
 * @date 2015-11-26 上午09:52:20
 * 
 */
public class CommunitiesActivity extends JBaseActivity implements IXListViewListener, IBindData {
	@ViewInject(R.id.new_xlistview)
	private XListView xListView;
	@ViewInject(R.id.text_transparent_line)
	private TextView view_line;
	@ViewInject(R.id.EditTextSearch)
	private EditText editTextSearch;
	
	@ViewInject(R.id.common_text_empty)
	private TextView common_text_empty;
	@ViewInject(R.id.empty)
	private View empty;
	private TitlePopup titlePopup;
	private CommunitiesAdapter mAdapter;
	private List<ImMucinfo> items = new ArrayList<ImMucinfo>();
	private List<ImMucinfo> copyitems = new ArrayList<ImMucinfo>();//为了搜索保存原有的数据
	private int index = 0;// 分页查询 从0开始
	private int size = 9999999;// 分页查询每次查20个,2016/01/21不分页
	private List<Label> hotLabeList = new ArrayList<Label>();// 推荐的热门标签
	private String hotLabeId;// 推荐的热门标签

	private String communityNO;
	private Community community;
	private long communityId;
	private String notice;
	private String key;

	@Override
	public void initJabActionBar() {
		getBundle();
		setContentView(R.layout.activity_communities);
		initTitle();
		ViewUtils.inject(this); // 注入view和事件..
		// geneItems();
		initControl();
		initXlistview();
		initPopWindow();
		showLoadingDialog();
		getData();
	}

	private void initTitle() {
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "社群", false, null, false, true);
	}

	private void getBundle() {

	}

	/**
	 * 从网络拿数据
	 */
	private void getData() {
		CommunityReqUtil.doGetCommunityList(this, index, size, hotLabeId, this, null);
	}

	private void initPopWindow() {
		// 实例化标题栏弹窗
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		titlePopup.setItemOnClickListener(onitemClick);
		// 给标题栏弹窗添加子类
/*		titlePopup.addAction(new ActionItem(this, R.string.creat_community, R.drawable.chat_launch));
		titlePopup.addAction(new ActionItem(this, R.string.add_group, R.drawable.home_invitation_with_heart));
		titlePopup.addAction(new ActionItem(this, R.string.filter_label, R.drawable.industry_love));*/
		titlePopup.addAction(new ActionItem(this, R.string.creat_community));
		titlePopup.addAction(new ActionItem(this, R.string.add_group));
		titlePopup.addAction(new ActionItem(this, R.string.filter_label));
		titlePopup.addAction(new ActionItem(this, R.string.my_community));
	}

	private OnPopuItemOnClickListener onitemClick = new OnPopuItemOnClickListener() {

		@Override
		public void onItemClick(ActionItem item, int position) {
			switch (position) {
			case 0:// 创建社群
				Intent intent = new Intent(CommunitiesActivity.this, CreateCommunityAvctivity.class);
				startActivity(intent);
				break;
			case 1:// 群号加群
				EditTextAlertDialog etDialog = new EditTextAlertDialog(CommunitiesActivity.this);
				etDialog.setTitle("输入社群号加入社群");
				etDialog.setHint("");
				etDialog.setOkTv("加入");
				etDialog.setOnDialogClickListener(new OnEditDialogClickListener() {
					@Override
					public void onClick(String evaluationValue) {
						if(evaluationValue!=null){
							communityNO = evaluationValue;
							showLoadingDialog();
							CommunityReqUtil.existCommunityNo(CommunitiesActivity.this, CommunitiesActivity.this, communityNO, null);
						}
					}
				});
				etDialog.show();
				break;
			case 2:// 标签筛选
					// TODO 跳转到暗号加群
				showLoadingDialog();
				CommunityReqUtil.getCommunityTagList(CommunitiesActivity.this, CommunitiesActivity.this, null);
				break;
			case 3://我的社群
				startActivity(new Intent(CommunitiesActivity.this, MyCommunitiesActivity.class));
			default:
				break;
			}
		}
	};

	private void initControl() {
		common_text_empty.setText("暂无社群");
		mAdapter = new CommunitiesAdapter(this);
		editTextSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				key=s.toString();
				if (!TextUtils.isEmpty(key)) {
					setKeyWord(key);
				}else{
					items=copyitems;
				}
				mynotify();
			}
		});
	}
	public void setKeyWord(String keyWord) {
		List<ImMucinfo> keyWordImMucinfo = new ArrayList<ImMucinfo>();
		for (int i = 0; i < copyitems.size(); i++) {
			ImMucinfo myImMucinfo = copyitems.get(i);
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
		mAdapter.setCommunities(items);
		mAdapter.notifyDataSetChanged();
	}
	// private void geneItems() {
	// for (int i = 0; i != 20; ++i) {
	// items.add(new ImMucinfo("阿米巴社群 " + (++start), "欢迎大家加入阿米巴  " +
	// (++start)));
	// }
	// }

	private void initXlistview() {
		xListView.setPullLoadEnable(false);
		xListView.setPullRefreshEnable(true);
		xListView.setAdapter(mAdapter);
		xListView.setXListViewListener(this);
		xListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position!=0) {
					ImMucinfo community = (ImMucinfo) mAdapter.getItem(position - 1);
					ENavigate.startCommunityDetailsActivity(CommunitiesActivity.this, community.getId(),false);
				}
				
			}
		});
	}

	@Override
	public void onRefresh() {
		getHandler().postDelayed(new Runnable() {
			@Override
			public void run() {
				index = 0;
				getData();
			}
		}, 2000);
	}

	private void onLoad() {
		xListView.stopRefresh();
		xListView.stopLoadMore();
	}

	@Override
	public void onLoadMore() {
		getHandler().postDelayed(new Runnable() {
			@Override
			public void run() {
				index++;
				getData();
			}
		}, 2000);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_new_main, menu);
		menu.findItem(R.id.home_new_menu_more).setIcon(R.drawable.ic_action_overflow);
		menu.findItem(R.id.home_new_menu_search).setIcon(R.drawable.tongren_message_normal);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.home_new_menu_more:// 更多
			titlePopup.show(view_line);
			break;
		case R.id.home_new_menu_search://社群消息
			Intent intent = new Intent(CommunitiesActivity.this, CommumitiesNotificationActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 标签筛选目前只做单选
	 */
	private void showFilterDialog() {
		LayoutInflater inFlater = LayoutInflater.from(CommunitiesActivity.this);
		View layout = inFlater.inflate(R.layout.layout_filter_label_dialog, null);
		final Dialog dialog = new AlertDialog.Builder(CommunitiesActivity.this).create();
		dialog.show();// 这个方法必须先于在下面的执行
		dialog.setCanceledOnTouchOutside(true);
		dialog.getWindow().setContentView(layout);
		CustomView hot_label_view = (CustomView) layout.findViewById(R.id.hot_label);
		if (null != hotLabeList) {
			hot_label_view.removeAllViews();
			int size = hotLabeList.size();
			for (int i = 0; i < size; i++) {
				View view = View.inflate(this, R.layout.demand_redact_label_tv, null);
				TextView text = (TextView) view.findViewById(R.id.labelTv);
				text.setBackgroundResource(R.drawable.icon_label_blue_bg);
				text.setTextColor(getResources().getColor(R.color.white));
				text.setText(hotLabeList.get(i).getName());
				text.setTag(String.valueOf(hotLabeList.get(i).getId()));
				text.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						TextView tv = (TextView) v;
						String tag = (String) v.getTag().toString();
						if (tag.contains("-1")) {
							hotLabeId = "";// -1表示全部
						} else {
							hotLabeId = tag;
						}
						showLoadingDialog();
						getData();
						dialog.dismiss();
					}
				});
				hot_label_view.addView(view);
			}
		}
	}

	@Override
	public void bindData(int tag, Object object) {
		HashMap<String, Object> dataMap = (HashMap<String, Object>) object;

		switch (tag) {
		case CommunityReqType.TYPE_MAIN_COMMUNITY_LIST:// 所有的社群列表
			onLoad();
			if (dataMap != null) {
				List<ImMucinfo> list = (List<ImMucinfo>) dataMap.get("result");
				if(index == 0){
					items.clear();
				}
				if (null != list && list.size() != 0) {
					empty.setVisibility(View.GONE);
					xListView.setPullLoadEnable(false);
					items.addAll(list);
					copyitems=items;
					if(!TextUtils.isEmpty(key))
						setKeyWord(key);
				} else {
					xListView.setPullLoadEnable(false);
					empty.setVisibility(View.VISIBLE);
				}
				mynotify();
			} else {
				if (index != 0)
					showToast("已到底了");
				xListView.setPullLoadEnable(false);
				empty.setVisibility(View.VISIBLE);
			}

			break;
		case EAPIConsts.CommunityReqType.TYPE_GET_COMMUNITY_LABELS:// 获取标签详情
			if (null != dataMap) {
				CommunityLabels communityLabels = (CommunityLabels) dataMap.get("result");
				hotLabeList = communityLabels.getHotLabel() != null ? communityLabels.getHotLabel() : new ArrayList<Label>();
				Label label = new Label();
				label.setName("全部");
				label.setId(-1);
				hotLabeList.add(label);
				showFilterDialog();
			}
			break;
		case CommunityReqType.TYPE_EXIST_COMMUNITYNO:
			if (dataMap != null) {
				boolean isExist = (Boolean) dataMap.get("isExist");
				if(isExist){
					CommunityReqUtil.getCommunityByCommunityNo(this, this, communityNO, Long.valueOf(App.getUserID()), null);
				}else{
					dismissLoadingDialog();
					showToast("群号错误");
				}
			}else{
				dismissLoadingDialog();
				showToast("根据社群号获取社群基本信息失败！群号是："+communityNO);
			}
			break;
		case CommunityReqType.TYPE_GET_COMMUNITY_BY_COMMUNITYNO:
			if (dataMap != null) {
				CommunityApply commuapply = (CommunityApply) dataMap.get("result");
				Notification noti = (Notification) dataMap.get("notification");
				if(commuapply == null && noti != null){
					dismissLoadingDialog();
					showToast(noti.getNotifInfo());
				}else if(commuapply != null){
					communityId = commuapply.getCommunity().getId();
					community = commuapply.getCommunity();
					if(commuapply.getApplayType().equals(CommunityApply.APPLYTYPE_ALL)){
						List<Long> list = new ArrayList<Long>();
						list.add(Long.parseLong(App.getApp().getUserID()));
						CommunityReqUtil.doInvite2Muc(this, communityId, list, this, null);
					}else if(commuapply.getApplayType().equals(CommunityApply.APPLYTYPE_REQ)){
						CommunityReqUtil.getNotice(this, this, communityId, null);
					}
				}
			}
			break;
		case CommunityReqType.TYPE_INVITE2MUC://创建完后得邀请创建者进来才是真正创建成功
			if (null != object) {
				ENavigate.startCommunityDetailsActivity(this, communityId, true);
				// dismissLoadingDialog();
			} else {
				showToast("加入不成功");
			}
			dismissLoadingDialog();
			break;
		case EAPIConsts.CommunityReqType.TYPE_GET_NOTICE:// 社群公告
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
		default:
			break;
		}
		dismissLoadingDialog();
	}
	
	private void showNoticeDialog() {
		final NoticeDialog dialog = new NoticeDialog(this);
		dialog.setTitle("社群公告");
		dialog.setMessage(notice);
		dialog.setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CommunitiesActivity.this, JoinCommumitiesAvtivity.class);
				intent.putExtra("req_number_community", community);
				startActivity(intent);
				dialog.dismiss();
			}
		});
	}
}
