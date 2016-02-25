package com.tr.ui.communities.home;

import java.io.Serializable;
import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tr.App;
import com.tr.R;
import com.tr.api.CommunityReqUtil;
import com.tr.model.demand.ASSOData;
import com.tr.model.demand.ASSORPOK;
import com.tr.model.demand.DemandASSOData;
import com.tr.model.obj.ConnectionsMini;
import com.tr.model.obj.JTFile;
import com.tr.model.obj.MUCDetail;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseActivity;
import com.tr.ui.communities.ModulesType;
import com.tr.ui.communities.adapter.CommumitiesRelevanceAdapter;
import com.tr.ui.communities.adapter.MemberAdapter;
import com.tr.ui.communities.im.CommunityChatActivity;
import com.tr.ui.communities.model.CommunityDetailRes;
import com.tr.ui.communities.model.CreateSet;
import com.tr.ui.communities.model.ImMucinfo;
import com.tr.ui.communities.model.Label;
import com.tr.ui.demand.MyView.CustomView;
import com.tr.ui.home.FrameWorkUtils;
import com.tr.ui.home.InviteFriendByQRCodeActivity;
import com.tr.ui.home.utils.HomeCommonUtils;
import com.tr.ui.knowledge.MyKnowledgeActivity;
import com.tr.ui.widgets.IMEditMumberGrid.HeadName;
import com.tr.ui.widgets.MessageDialog.OnDialogFinishListener;
import com.tr.ui.widgets.KnoTagGroupView;
import com.tr.ui.widgets.MessageDialog;
import com.tr.ui.widgets.NoticeDialog;
import com.tr.ui.widgets.title.menu.popupwindow.ActionItem;
import com.tr.ui.widgets.title.menu.popupwindow.TitlePopup;
import com.tr.ui.widgets.title.menu.popupwindow.TitlePopup.OnPopuItemOnClickListener;
import com.utils.common.EConsts;
import com.utils.common.EUtil;
import com.utils.common.GlobalVariable;
import com.utils.http.EAPIConsts;
import com.utils.http.EAPIConsts.CommunityReqType;
import com.utils.http.IBindData;
import com.utils.log.ToastUtil;

/**
 * 社群详情页面
 * 
 */
public class CommunitiesDetailsActivity extends JBaseActivity implements OnClickListener, IBindData {
	@ViewInject(R.id.joinCommumities)
	private TextView joinCommumities;// 加入社群/进入聊天
	@ViewInject(R.id.picture_Iv)
	private ImageView picture_Iv;// 群头像
	@ViewInject(R.id.commumitiesNameTv)
	private TextView commumitiesNameTv;// 群名称

	@ViewInject(R.id.memberSumTv)
	private TextView memberSumTv;// 成员数量
	@ViewInject(R.id.commumitiesNumTv)
	private TextView commumitiesNumTv;// 社群号
	@ViewInject(R.id.introduceTv)
	private TextView introduceTv;// 社群介绍
	@ViewInject(R.id.text_notice)
	private TextView text_notice;// 社群公告标题
	@ViewInject(R.id.contentTv)
	private TextView contentTv;// 社群公告
	@ViewInject(R.id.item_maxcard)
	private RelativeLayout item_maxcard;// 二维码item
	@ViewInject(R.id.image_maxcard)
	private ImageView image_maxcard;// 二维码item里的icon，

	@ViewInject(R.id.evaluation_gv)
	private KnoTagGroupView evaluationGv;// 群标签(可以点赞的)隐藏
	@ViewInject(R.id.scrollview_label)
	private ScrollView scrollview_label;// 群标签布局
	@ViewInject(R.id.item_community_label)
	private LinearLayout item_community_label;// 群标签整体布局
	@ViewInject(R.id.community_label)
	private KnoTagGroupView community_label;// 群标签（展示）

	@ViewInject(R.id.item_community_member)
	private LinearLayout item_community_member;// 群成员布局
	@ViewInject(R.id.community_member_sum)
	private TextView community_member_sum;// 群成员数量
	@ViewInject(R.id.see_more_member)
	private TextView see_more_member;// 群成员查看更多
	@ViewInject(R.id.grid_community_member)
	private GridView grid_community_member;// 群成员列表

	@ViewInject(R.id.item_community_people)
	private LinearLayout item_community_people;// 群人脉布局
	@ViewInject(R.id.community_people_sum)
	private TextView community_people_sum;// 群人脉数
	@ViewInject(R.id.see_more_people)
	private TextView see_more_people;// 群人脉查看更多
	@ViewInject(R.id.list_community_people)
	private ListView list_community_people;// 群人脉列表

	@ViewInject(R.id.item_community_company)
	private LinearLayout item_community_company;// 群企业布局
	@ViewInject(R.id.community_company_sum)
	private TextView community_company_sum;// 群企业数
	@ViewInject(R.id.see_more_company)
	private TextView see_more_company;// 群企业查看更多
	@ViewInject(R.id.list_community_company)
	private ListView list_community_company;// 群企业列表

	@ViewInject(R.id.item_community_knowledge)
	private LinearLayout item_community_knowledge;// 群知识布局
	@ViewInject(R.id.see_more_knowledge)
	private TextView see_more_knowledge;// 群知识查看更多
	@ViewInject(R.id.list_community_knowledge)
	private ListView list_community_knowledge;// 群知识列表

	@ViewInject(R.id.item_community_demand)
	private LinearLayout item_community_demand;// 群需求布局
	@ViewInject(R.id.see_more_demand)
	private TextView see_more_demand;// 群需求查看更多
	@ViewInject(R.id.list_community_demand)
	private ListView list_community_demand;// 群需求列表

	@ViewInject(R.id.text_transparent_line)
	private TextView view_line;

	private MemberAdapter memberAdapter;
	private CommumitiesRelevanceAdapter peopleAdapter;// 人脉
	private CommumitiesRelevanceAdapter companyAdapter;// 企业即组织
	private CommumitiesRelevanceAdapter knowledgeAdapter;// 知识
	private CommumitiesRelevanceAdapter demandAdapter;// 需求
	// private CommumitiesPeopleAdapter peopleAdapter;//人脉
	// private CommumitiesCompanyAdapter companyAdapter;//企业即组织
	// private CommumitiesKnowledgeAdapter knowledgeAdapter;//知识
	// private CommumitiesDemandAdapter demandAdapter;//需求

	private long communityId;// 社群id
	private String communityTitle;// 社群名称.创建社群成功后带过来的数据
	private String userId;// 用户id,扫描二维码带过来的
	private boolean isNumIn;// 是否群号加群
	// private TitlePopup titlePopup;

	private CommunityDetailRes communityDetailRes = new CommunityDetailRes();// 社群详情实体对象
	/**
	 * 社群的社群实体
	 */
	private ImMucinfo community;
	/**
	 * 关联
	 */
	private ASSORPOK asso;
	/**
	 * 标签id
	 */
	private List<Label> labels;
	/**
	 * 社群设置
	 */
	private CreateSet set;

	private List<ASSOData> r = new ArrayList<ASSOData>(); // 事件
	private List<ASSOData> p = new ArrayList<ASSOData>();// 人
	public List<ASSOData> o = new ArrayList<ASSOData>();// 组织
	public List<ASSOData> k = new ArrayList<ASSOData>();// 知识

	private List<DemandASSOData> connR = new ArrayList<DemandASSOData>();
	private List<DemandASSOData> connO = new ArrayList<DemandASSOData>();
	private List<DemandASSOData> connK = new ArrayList<DemandASSOData>();
	private List<DemandASSOData> connP = new ArrayList<DemandASSOData>();
	/**
	 * 成员详情实体
	 */
	private MUCDetail mucDetail;
	private int count;// 社群所有成员数量
	private boolean isOwner = false;// 社群是否是拥有着
	private boolean exist = false;// "exist":该成员是否存在该社群中，true存在，false 不存在

	private String notice;// 获取到的社群公告通知信息
	private TitlePopup titlePopup;

	/**
	 * 现有成员
	 */
	private List<ConnectionsMini> listConnectionsMini = new ArrayList<ConnectionsMini>();

	@Override
	public void initJabActionBar() {
		getBundle();
		HomeCommonUtils.initLeftCustomActionBar(this, getActionBar(), "社群详情", false, null, true, true);
		setContentView(R.layout.activity_communitiesdedails);
		ViewUtils.inject(this);
		initView();
		refreshOwnerUi();
		initData();
		initNotice();
	}

	@Override
	public void onResume() {
		super.onResume();
		showLoadingDialog();
		getCommunityDetail();
		getCommunityMemberList();
	}

	/**
	 * 获取成员详情
	 */
	private void getCommunityMemberList() {
		CommunityReqUtil.doGetCommunityDetailsMemberList(this, communityId, this, null);
	}

	/**
	 * 获取社群详情
	 */
	private void getCommunityDetail() {
		if (TextUtils.isEmpty(userId))
			CommunityReqUtil.doGetCommunityDetail(this, communityId, Long.parseLong(App.getApp().getUserID()), this, null);
		else
			CommunityReqUtil.doGetCommunityDetail(this, communityId, Long.parseLong(userId), this, null);
			
	}

	/**
	 * 创建社群成功后弹一个土司
	 */
	private void initNotice() {
		if (!TextUtils.isEmpty(communityTitle))
			showToast(communityTitle + "社群创建成功!");
	}

	private void initPopWindow() {
		// 实例化标题栏弹窗
		titlePopup = new TitlePopup(this, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		titlePopup.setItemOnClickListener(onitemClick);
		// 给标题栏弹窗添加子类
		titlePopup.addAction(new ActionItem(this, R.string.share));// 分享
		titlePopup.addAction(new ActionItem(this, "举报"));// 分享
		if (isOwner)//
			titlePopup.addAction(new ActionItem(this, R.string.edit));// 编辑
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_new_main, menu);
		menu.findItem(R.id.home_new_menu_more).setIcon(R.drawable.ic_action_overflow);
		menu.findItem(R.id.home_new_menu_search).setVisible(false);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.home_new_menu_more:// 更多
			if (null != titlePopup)
				titlePopup.show(view_line);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private OnPopuItemOnClickListener onitemClick = new OnPopuItemOnClickListener() {

		@Override
		public void onItemClick(ActionItem item, int position) {
			if ("分享".equals(item.mTitle)) {
				// TODO 分享action
				if (null != community) {
					JTFile file = community.toJTFile();
					file.virtual=App.getApp().getUserID();
//					ENavigate.startSocialShareActivity(CommunitiesDetailsActivity.this, file);
					FrameWorkUtils.showSharePopupWindow2(CommunitiesDetailsActivity.this, file);
				}
			}
			else if ("编辑".equals(item.mTitle)) {
				Intent intent = new Intent(CommunitiesDetailsActivity.this, CreateCommunityAvctivity.class);
				intent.putExtra("CREAT_SATATE", 1);// CREAT_SATATE=1为编辑
				intent.putExtra(GlobalVariable.COMMUNITY_ID, communityId);
				startActivity(intent);
			}else if ("举报".equals(item.mTitle)) {
				final MessageDialog messageDialog = new MessageDialog(CommunitiesDetailsActivity.this);
				messageDialog.setContent("确定举报该社群吗？");
				messageDialog.show();
				messageDialog.setOnDialogFinishListener(new OnDialogFinishListener(){
					@Override
					public void onFinish(String content) {
						if ("确定".equals(content)) {
							CommunityReqUtil.doReport(CommunitiesDetailsActivity.this,communityId+"", App.getUserID(), "", "1", CommunitiesDetailsActivity.this, null);
							messageDialog.cancel();
							ToastUtil.showToast(CommunitiesDetailsActivity.this, "举报成功");
						}}

					@Override
					public void onCancel(String content) {
						messageDialog.cancel();
					}
				});
			}
		}
	};

	private void getBundle() {
		communityId = getIntent().getLongExtra(GlobalVariable.COMMUNITY_ID, -1);// Long型
		communityTitle = getIntent().getStringExtra(GlobalVariable.COMMUNITY_TITLE);
		userId = getIntent().getStringExtra("userId");
		isNumIn = getIntent().getBooleanExtra(GlobalVariable.COMMUNITY_ISNUMIN, false);
	}

	private void initData() {
		memberAdapter = new MemberAdapter(this);
		grid_community_member.setAdapter(memberAdapter);
		grid_community_member .setOnItemClickListener(MemberItemClick);

		peopleAdapter = new CommumitiesRelevanceAdapter(this, ModulesType.PeopleModules);
		list_community_people.setAdapter(peopleAdapter);
		list_community_people.setOnItemClickListener(peopleItemClick);

		companyAdapter = new CommumitiesRelevanceAdapter(this, ModulesType.OrgAndCustomModules);
		list_community_company.setAdapter(companyAdapter);
		list_community_company.setOnItemClickListener(companyItemClick);

		knowledgeAdapter = new CommumitiesRelevanceAdapter(this, ModulesType.KnowledgeModules);
		list_community_knowledge.setAdapter(knowledgeAdapter);
		list_community_knowledge.setOnItemClickListener(KnowledgeItemClick);

		demandAdapter = new CommumitiesRelevanceAdapter(this, ModulesType.DemandModules);
		list_community_demand.setAdapter(demandAdapter);
		list_community_demand.setOnItemClickListener(demandItemClick);
	}

	private OnItemClickListener peopleItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (connP.get(position).type == 3)// 好友
				ENavigate.startRelationHomeActivity(CommunitiesDetailsActivity.this, connP.get(position).id, true, ENavConsts.type_details_other);
			else
				ENavigate.startRelationHomeActivity(CommunitiesDetailsActivity.this, connP.get(position).id,false,ENavConsts.TYPE_CONNECTIONS_HOME_PAGE);
		}
	};
	private OnItemClickListener MemberItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			HeadName headName = (HeadName) memberAdapter.getItem(position);
			ENavigate.startRelationHomeActivity(CommunitiesDetailsActivity.this, headName.getUserID(),true,1);
		}
	};
	private OnItemClickListener companyItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			long orgId = Long.valueOf(connO.get(position).id);
			long creaetById = EUtil.isEmpty(connO.get(position).ownerid) ?  0L :Long.valueOf(connO.get(position).ownerid);
			if (connO.get(position).type==4)// 组织
				ENavigate.startOrgMyHomePageActivity(CommunitiesDetailsActivity.this, orgId, creaetById, true, ENavConsts.type_details_org);
			else
				ENavigate.startClientDedailsActivity(CommunitiesDetailsActivity.this, orgId);
		}
	};
	private OnItemClickListener KnowledgeItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//			ENavigate.startKnowledgeOfDetailActivity(CommunitiesDetailsActivity.this, Long.parseLong(connK.get(position).id), connK.get(position).type);
			ENavigate.startCollectionKnowledgeOfDetailActivity(CommunitiesDetailsActivity.this, Long.parseLong(connK.get(position).id), 1, true);

		}
	};
	private OnItemClickListener demandItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			ENavigate.startNeedDetailsActivity(CommunitiesDetailsActivity.this, connR.get(position).id, 1);
		}
	};

	private void initView() {
		joinCommumities.setVisibility(View.GONE);
		evaluationGv.setVisibility(View.GONE);
		scrollview_label.setVisibility(View.VISIBLE);
		item_maxcard.setOnClickListener(this);// 二维码名片
		see_more_member.setOnClickListener(this);
		see_more_company.setOnClickListener(this);
		see_more_people.setOnClickListener(this);
		see_more_knowledge.setOnClickListener(this);
		see_more_demand.setOnClickListener(this);

		joinCommumities.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.joinCommumities:
			if (exist) {
				intent = new Intent(CommunitiesDetailsActivity.this, CommunityChatActivity.class);
				intent.putExtra("community", community);
				intent.putExtra("applyType", set.getApplayType());
				intent.putExtra(GlobalVariable.COMMUNITY_ISNUMIN, isNumIn);
				// intent.putExtra(ENavConsts.EMucDetail, mucDetail);
				startActivityForResult(intent, ENavigate.REQUSET_CODE_MUC);
			}else if(null != set && set.getApplayType() == 1){//set.getApplayType()加入社群权限:1是所有人 2申请加入. 默认传1.
				List<Long> listID = new ArrayList<Long>();
				listID.add(Long.valueOf(App.getUserID()));
				CommunityReqUtil.doInvite2Muc(this, community.getId(), listID, this, null);
			} else {
				showLoadingDialog();
				CommunityReqUtil.getNotice(CommunitiesDetailsActivity.this, this, communityId, null);

			}
			break;
		case R.id.item_maxcard:// 二维码
			// http://test.online.gintong.com/html/social.html?communityId={{communityId}}&userId={{userId}}
//			String qrStr = EAPIConsts.getTMSUrl() + "community/qr?communityId=" + communityId;
			String qrStr = EAPIConsts.getCommunityTMSUrl() + "html/social.html?communityId=" + communityId+"&userId="+App.getUserID()+"";
			if (communityDetailRes != null && communityDetailRes.getCommunity() != null) {
				ENavigate.startQRCodeActivity(CommunitiesDetailsActivity.this, qrStr,String.valueOf(communityId), communityDetailRes.getCommunity().getTitle(), communityDetailRes.getCommunity().getPicPath(),true);
			}
			break;
		case R.id.see_more_member:// 群成员查看更多
			intent = new Intent(CommunitiesDetailsActivity.this, CommunityMembersActivity.class);
			intent.putExtra(GlobalVariable.COMMUNITY_ID, communityId);
			startActivity(intent);
			break;
		case R.id.see_more_people:// 群人脉查看更多
			intent = new Intent(CommunitiesDetailsActivity.this, CommunityRelevanceActivity.class);
			intent.putExtra(EConsts.Key.MODULES_TYPE, ModulesType.PeopleModules);
			intent.putExtra("conn", (Serializable) connP);
			startActivity(intent);
			break;
		case R.id.see_more_company:// 群公司查看更多
			intent = new Intent(CommunitiesDetailsActivity.this, CommunityRelevanceActivity.class);
			intent.putExtra(EConsts.Key.MODULES_TYPE, ModulesType.OrgAndCustomModules);
			intent.putExtra("conn", (Serializable) connO);
			startActivity(intent);
			break;
		case R.id.see_more_demand:// 群需求查看更多
			intent = new Intent(CommunitiesDetailsActivity.this, CommunityRelevanceActivity.class);
			intent.putExtra(EConsts.Key.MODULES_TYPE, ModulesType.DemandModules);
			intent.putExtra("conn", (Serializable) connR);
			startActivity(intent);
			break;
		case R.id.see_more_knowledge:// 群知识查看更多
			intent = new Intent(CommunitiesDetailsActivity.this, CommunityRelevanceActivity.class);
			intent.putExtra(EConsts.Key.MODULES_TYPE, ModulesType.KnowledgeModules);
			intent.putExtra("conn", (Serializable) connK);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	public void bindData(int tag, Object object) {
		HashMap<String, Object> dataMap = (HashMap<String, Object>) object;
		switch (tag) {
		case CommunityReqType.TYPE_GET_COMMUNITY_MEMBER_DETAILS:// 详情里的前10个成员详情
			if (null != dataMap) {
				mucDetail = (MUCDetail) dataMap.get("mucDetail");
				count = (Integer) dataMap.get("count");
				// getCommunityDetail();
				upDateMenberUi();
			}
			break;
		case CommunityReqType.TYPE_GET_COMMUNITY_DETAIL:// 社群详情
			if (null != dataMap) {
				exist = (Boolean) dataMap.get("exist");
				communityDetailRes = (CommunityDetailRes) dataMap.get("result");
				upDateDetailUi();
			}
			break;
		case EAPIConsts.CommunityReqType.TYPE_GET_NOTICE:// 社群公告
			if (null != dataMap) {
				notice = (String) dataMap.get("notice");
				showNoticeDialog();
			} else {
				Intent intent = new Intent(CommunitiesDetailsActivity.this, JoinCommumitiesAvtivity.class);
				intent.putExtra(GlobalVariable.COMMUNITY_ID, communityId);
				intent.putExtra("community", community);
				startActivity(intent);
			}

			break;
		case EAPIConsts.CommunityReqType.TYPE_INVITE2MUC:
			if (null != dataMap) {
//				MUCDetail mucDetail = (MUCDetail) dataMap.get("mucDetail");
				Intent intent = new Intent(CommunitiesDetailsActivity.this, CommunityChatActivity.class);
				intent.putExtra("community", community);
				intent.putExtra("applyType", set.getApplayType());
				intent.putExtra(ENavConsts.EFromActivityName, "CommunitiesDetailsActivity");
				startActivityForResult(intent, ENavigate.REQUSET_CODE_MUC);
			} else {
				showToast("进入失败");
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
				Intent intent = new Intent(CommunitiesDetailsActivity.this, JoinCommumitiesAvtivity.class);
				intent.putExtra(GlobalVariable.COMMUNITY_ID, communityId);
				intent.putExtra("community", community);
				startActivity(intent);
				dialog.dismiss();
			}
		});
	}

	/**
	 * 社群详情页面刷新（除成员列表）
	 */
	private void upDateDetailUi() {
		joinCommumities.setVisibility(View.VISIBLE);
		community = communityDetailRes.getCommunity();
		asso = communityDetailRes.getAsso();
		labels = communityDetailRes.getLabels();
		set = communityDetailRes.getSet();
		if (exist)
			joinCommumities.setText("进入群聊");
		else if (null != set && set.getApplayType() == 1)//加入社群权限:1是所有人 2申请加入. 默认传1.
			joinCommumities.setText("进入群聊");
		else
			joinCommumities.setText("加入社群");
			
		if (null != asso) {
			r = asso.r;// 事件
			k = asso.k;// 知识
			o = asso.o;// 组织即企业
			p = asso.p;// 人脉
			connR = new ArrayList<DemandASSOData>();
			for (int i = 0; null != r && i < r.size(); i++) {
				ASSOData assoData = r.get(i);
				List<DemandASSOData> conn = assoData.conn;
				if (null != conn && conn.size() > 0) {
					connR.addAll(conn);
				}
			}
			connK = new ArrayList<DemandASSOData>();
			for (int i = 0; null != k && i < k.size(); i++) {
				ASSOData assoData = k.get(i);
				List<DemandASSOData> conn = assoData.conn;
				if (null != conn && conn.size() > 0) {
					connK.addAll(conn);
				}
			}
			connO = new ArrayList<DemandASSOData>();
			for (int i = 0; null != o && i < o.size(); i++) {
				ASSOData assoData = o.get(i);
				List<DemandASSOData> conn = assoData.conn;
				if (null != conn && conn.size() > 0) {
					connO.addAll(conn);
				}
			}
			connP = new ArrayList<DemandASSOData>();
			for (int i = 0; null != p && i < p.size(); i++) {
				ASSOData assoData = p.get(i);
				List<DemandASSOData> conn = assoData.conn;
				if (null != conn && conn.size() > 0) {
					connP.addAll(conn);
				}
			}
		}
		if (null != community) {
			if(!TextUtils.isEmpty(community.getPicPath())){
				ImageLoader.getInstance().displayImage(community.getPicPath(), picture_Iv);// 头像
			}
			commumitiesNameTv.setText(community.getTitle());// 群名称
			commumitiesNumTv.setText(community.getCommunityNo());// 社群号
			// 社群介绍
			if(!TextUtils.isEmpty(community.getSubject())){
				introduceTv.setText(community.getSubject());// 社群介绍
				introduceTv.setVisibility(View.VISIBLE);
			}else{
				introduceTv.setVisibility(View.GONE);
			}
			if (!TextUtils.isEmpty(community.getContent())) {
				contentTv.setVisibility(View.VISIBLE);
				text_notice.setVisibility(View.VISIBLE);
				contentTv.setText(community.getContent());// 社群公告
			} else{
				text_notice.setVisibility(View.GONE);
				contentTv.setVisibility(View.GONE);
			}
			community_people_sum.setText("(" + connP.size() + ")");
			community_company_sum.setText("(" + connO.size() + ")");
			if (App.getApp().getUserID().contains(String.valueOf(community.getOwnerId())))// 判断是否是群主
				isOwner = true;
			else
				isOwner = false;
			if (isOwner) {
				refreshOwnerUi();

			} else {
				refreshNotOwnerUi();

			}
		}
		if (null != labels) {
			community_label.removeAllViews();
			int size = labels.size();
			if(size==0)
				item_community_label.setVisibility(View.GONE);
			else{
				community_label.addTagViews(labels, null, null, false, true);
			}
		}
		initPopWindow();
		peopleAdapter.setData(getThreeConn(connP));
		companyAdapter.setData(getThreeConn(connO));
		knowledgeAdapter.setData(getThreeConn(connK));
		demandAdapter.setData(getThreeConn(connR));

	}

	private void refreshNotOwnerUi() {
		if (connP.size() == 0 | set.getPeopleShowType() == 2)
			item_community_people.setVisibility(View.GONE);
		else
			item_community_people.setVisibility(View.VISIBLE);

		if (connO.size() == 0 | set.getCompanyShowType() == 2)
			item_community_company.setVisibility(View.GONE);
		else
			item_community_company.setVisibility(View.VISIBLE);

		if (connK.size() == 0 | set.getKnowledgeShowType() == 2)
			item_community_knowledge.setVisibility(View.GONE);
		else
			item_community_knowledge.setVisibility(View.VISIBLE);

		if (connR.size() == 0 | set.getDemandShowType() == 2)
			item_community_demand.setVisibility(View.GONE);
		else
			item_community_demand.setVisibility(View.VISIBLE);

		if (set.getMemberShowType() == 2)
			item_community_member.setVisibility(View.GONE);
		else
			item_community_member.setVisibility(View.VISIBLE);
	}

	private void refreshOwnerUi() {
		if (connP.size() == 0)
			item_community_people.setVisibility(View.GONE);
		else
			item_community_people.setVisibility(View.VISIBLE);

		if (connO.size() == 0)
			item_community_company.setVisibility(View.GONE);
		else
			item_community_company.setVisibility(View.VISIBLE);

		if (connK.size() == 0)
			item_community_knowledge.setVisibility(View.GONE);
		else
			item_community_knowledge.setVisibility(View.VISIBLE);

		if (connR.size() == 0)
			item_community_demand.setVisibility(View.GONE);
		else
			item_community_demand.setVisibility(View.VISIBLE);
	}

	/**
	 * 成员列表数据刷新
	 */
	private void upDateMenberUi() {
		if (null != mucDetail) {
			listConnectionsMini = mucDetail.getListConnectionsMini2();
		}
		if (null != listConnectionsMini) {
			int size = listConnectionsMini.size();
			ArrayList<HeadName> data = new ArrayList<HeadName>();
			for (int i = 0; i < size&&i<10; i++) {
				ConnectionsMini mini = listConnectionsMini.get(i);
				HeadName headname = new HeadName(mini);
				if (i == 0)
					headname.setCreater(true);// 因为返回的结果后台已经把第一个设为创建者
				data.add(headname);
			}

			memberAdapter.setDataNoAdd(data);
		}
		memberSumTv.setText("共" + count + "人");
		community_member_sum.setText("(" + count + ")");

	}

	/**
	 * 只获取三个
	 * 
	 * @param conn
	 */
	private List<DemandASSOData> getThreeConn(List<DemandASSOData> conn) {
		List<DemandASSOData> mConn = new ArrayList<DemandASSOData>();
		if (conn.size() > 3) {
			for (int i = 0; i < 3; i++) {
				mConn.add(conn.get(i));
			}
			return mConn;
		} else
			return conn;

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (ENavigate.REQUSET_CODE_MUC == requestCode) {
			if (resultCode == RESULT_FIRST_USER) {
				// 退出群聊成功,继续退出当前群聊页面
				Intent intent = new Intent();
				setResult(RESULT_FIRST_USER, intent);
				finish();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
