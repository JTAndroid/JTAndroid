package com.tr.ui.relationship;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tr.App;
import com.tr.R;
import com.tr.api.ConnectionsReqUtil;
import com.tr.model.connections.ConnectionsCacheUtils;
import com.tr.model.connections.FriendRequest;
import com.tr.model.im.ChatDetail;
import com.tr.model.model.PeopleAddress;
import com.tr.model.model.PeopleCommunityRelationship;
import com.tr.model.model.PeopleContactComm;
import com.tr.model.model.PeopleCustomer;
import com.tr.model.model.PeopleDemandCommon;
import com.tr.model.model.PeopleEducation;
import com.tr.model.model.PeopleGroupTemp;
import com.tr.model.model.PeopleImportantDate;
import com.tr.model.model.PeopleMetting;
import com.tr.model.model.PeopleName;
import com.tr.model.model.PeoplePersonalLine;
import com.tr.model.model.PeoplePersonalPlate;
import com.tr.model.model.PeopleSelectTag;
import com.tr.model.model.PeopleTemp;
import com.tr.model.model.PeopleWorkExperience;
import com.tr.model.obj.Connections;
import com.tr.model.obj.JTContact2;
import com.tr.model.obj.JTContactMini;
import com.tr.model.obj.JTFile;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.base.JBaseFragmentActivity;
import com.tr.ui.connections.detail.ConnsRequirementActivity;
import com.tr.ui.connections.detail.ConnsResourcesActivity;
import com.tr.ui.home.frg.FrgConnections2;
import com.tr.ui.im.IMRelationSelectActivity;
import com.tr.ui.widgets.BasicGridView;
import com.tr.ui.widgets.BasicListView2;
import com.tr.ui.widgets.FileDownloadLinearLayout;
import com.utils.common.ApolloUtils;
import com.utils.common.EConsts;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
import com.utils.string.StringUtils;

/**
 * RelationDetails1Activity.java
 * @Author huangliang/leon
 * @Date 2014-08-01
 * @description 用户详情（新版）
 * 以下是本页面的几个数据来源和可以进行的操作
 * 我的资料：编辑
 * 好友的资料：删除好友、聊天、推荐、分享到畅聊
 * 我的人脉：编辑、删除人脉、分享到畅聊、邀请加入
 * 分享的人脉：转为我的人脉
 */
public class RelationDetails1Activity extends JBaseFragmentActivity {

	public static final String TAG = RelationDetails1Activity.class.getSimpleName();
	
	public static final int result_recommend = 1;
	public static final int result_edit = 5;

	private MenuItem menuEdit = null; // 编辑
	private MenuItem menuDeleteFriend = null; // 删除好友
	private MenuItem menuDeleteContact = null; // 删除人脉
	private MenuItem menuChat = null; // 聊天
	private MenuItem menuRecommend = null; // 推荐
	private MenuItem menuShare = null; //分享
	private MenuItem menuSave = null; // 保存
	private MenuItem menuInvite = null; // 邀请
	
	private FileDownloadLinearLayout downloaderLl;
	private int type = 0;
	private String userId;
	private boolean isonline;
	private JTContact2 onlineTContact;
	private JTContact2 offlineTContact;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.relationdetails1);
		userId = getIntent().getStringExtra(EConsts.Key.ID);
		type = getIntent().getIntExtra(ENavConsts.EFromActivityType, ENavConsts.type_details_other);
		isonline = getIntent().getBooleanExtra(EConsts.Key.isOnline, false); // online
		if (ENavConsts.type_details_share == type) { // 分享的人脉
			// 先读本地缓存
			Object obj = ConnectionsCacheUtils.readConnectionObj(type, isonline, userId);
			if (obj != null) {
				initViewDetail(obj);
			}
			// 再联网取数据
			JSONObject jsonObj = new JSONObject();
			try {
				jsonObj.put("jtContactID", userId);
			} 
			catch (JSONException e) {
				Log.d(TAG, e.getMessage() + "");
			}
			showLoadingDialog();
			ConnectionsReqUtil.getShareDetail(this, ib, jsonObj, null);
		} 
		else { // 其它用户或人脉
			// 先读本地缓存
			Object obj = ConnectionsCacheUtils.readConnectionObj(type, isonline, userId);
			if (obj != null) {
				initViewDetail(obj);
			}
			// 再联网取数据
			showLoadingDialog();
			JSONObject jsonObj = ConnectionsReqUtil.getContactDetailJson(userId, isonline);
			ConnectionsReqUtil.doContactDetail(this, ib, jsonObj, null);
		}
	}

	
	IBindData ib = new IBindData() {
		@Override
		public void bindData(int tag, Object object) {
			// 隐藏加载框
			dismissLoadingDialog();
			if (tag == EAPIConsts.concReqType.ContactDetail) { // 人脉详情
				if (object != null) {
					// 刷新页面
					initViewDetail(object);
					// 数据返回，写文件缓存
					ConnectionsCacheUtils.writeConnectionObj(type, isonline, userId, object);
				}
			} 
			else if (tag == EAPIConsts.concReqType.im_addFriend) { // 添加好友
				if (object != null) {
					String sur = (String) object;
					if (sur.equals("true")) {
						showToast("请求发送成功");
					} 
					else {
						showToast("请求发送失败");
					}
				}
			} 
			else if (tag == EAPIConsts.concReqType.im_delJtContact) { // 删除人脉
				if (object != null) {
					String sur = (String) object;
					if (sur.equals("true")) {
						/*
						if (onlineTContact != null) { // 删除用户
							FrgConnections.cnsCacheData.delete(
									onlineTContact.getID(),
									Connections.type_persion,
									onlineTContact.isOnline);
						}
						*/
						if (offlineTContact != null) { // 删除人脉
							FrgConnections2.cnsCacheData.delete(
									offlineTContact.getID(),
									Connections.type_persion,
									offlineTContact.isOnline);
						}
						FrgConnections2.contactAdapter.dataChange();
						showToast("删除成功");
						finish();
					}
				}
				else{
					showToast("删除失败，请重试");
				}
			} 
			else if (tag == EAPIConsts.concReqType.im_deleteFriend) { // 删除好友
				if (object != null) {
					String sur = (String) object;
					if (sur.equals("true")) {
						FrgConnections2.cnsCacheData.delete(
								onlineTContact.getID(),
								Connections.type_persion,
								onlineTContact.isOnline);
						FrgConnections2.contactAdapter.dataChange();
						showToast("删除成功");
						finish();
					}
				}
				else{
					showToast("删除失败");
				}
			} 
			else if (tag == EAPIConsts.concReqType.getShareDetail) { // 获取分享的人脉详情
				if (object != null) {
					// 刷新页面
					initViewDetail(object);
					// 返回分享的人脉详情对象，写文件缓存
					ConnectionsCacheUtils.writeConnectionObj(type, isonline, userId, object);
				}
			} 
			else if (tag == EAPIConsts.concReqType.im_holdJTContact) { // 保存分享的人脉
				if (object == null) {
					showToast("操作失败");
				} 
				else {
					Connections saveConnections = (Connections) object;
					FrgConnections2.cnsCacheData.updataDel(
							saveConnections.getId(), saveConnections.type,
							saveConnections.isOnline(), saveConnections);
					FrgConnections2.contactAdapter.dataChange();
					showToast("操作成功");
					finish();
				}
			} 
			else if (tag == EAPIConsts.concReqType.im_inviteJoinGinTong) { //  邀请加入
				if (object != null) {
					String sur = (String) object;
					if (sur.equals("true")) {
						showToast("邀请邮件已发送");
					}
					else{
						showToast("邀请邮件发送失败");
					}
				}
			}
		}
	};

	// 因为更新的特殊性，要单独写一个IBindData
	IBindData updataib = new IBindData() {
		@Override
		public void bindData(int tag, Object object) {
			dismissLoadingDialog();
			if (object != null) {
				// 刷新页面
				RelationDetails1Activity.this.setContentView(R.layout.relationdetails1);
				initViewDetail(object);
			}
		}
	};

	public void initViewDetail(Object object) {
		ArrayList returnData = (ArrayList) object;
		for (int i = 0; returnData.size() > i; i = i + 2) {
			int type = (Integer) returnData.get(i);
			if (type == 0) { // 用户
				onlineTContact = (JTContact2) returnData.get(i + 1);
				initView(onlineTContact);
				if (this.type == ENavConsts.type_details_share) { // 分享的人脉
					
				} 
				else if (onlineTContact.getID().equals(App.getUserID())) { // “我”的资料
					if (menuEdit != null) { // 可编辑
						menuEdit.setVisible(true);
					}
				}
				else if (onlineTContact.friendState == JTContactMini.type_friend) { // 好友的资料
					// 离线读缓存的时候，在oncreat()方法时调菜单里的布局控件
					// 由于生命周期，menu的控件还没有初使化，只能判断控件为空
					// 就不操作，导致菜单都为默认状态
					if (menuDeleteFriend != null) { // 删除好友
						menuDeleteFriend.setVisible(true);
					}
					if (menuChat != null) { // 聊天
						menuChat.setVisible(true);
					}
					if (menuRecommend != null) { // 推荐
						menuRecommend.setVisible(true);
					}
					if (menuShare != null) { // 分享到畅聊
						menuShare.setVisible(true);
					}
				}
			} 
			else { // 人脉
				offlineTContact = (JTContact2) returnData.get(i + 1);
				initView(offlineTContact);
				if (this.type == ENavConsts.type_details_share) { // 转为“我”的人脉
					if (menuSave != null) {
						menuSave.setVisible(true);
					}
				} 
				else if(this.type == ENavConsts.type_details_guest){ // 游客浏览（不能进行任何操作）
					
				}
				else { // “我”的人脉
					if (menuEdit != null) { // 编辑
						menuEdit.setVisible(true);
					}
					if (menuDeleteContact != null) { // 删除
						menuDeleteContact.setVisible(true);
					}
					if (menuShare != null) { // 分享到畅聊
						menuShare.setVisible(true);
					}
					if (menuInvite != null) { // 邀请
						menuInvite.setVisible(true);
					}
				}
			}
		}
	}

	/**
	 * 初始化页面
	 * @param onlineTContact1
	 */
	private void initView(final JTContact2 onlineTContact1) {
		fillBasicInfomation2Template(onlineTContact1);
		fillContactInfomation2Template(onlineTContact1);
		fillPersonalInfomation2Template(onlineTContact1);
		fillMeeting2Template(onlineTContact1);
		ImageView iconmageView = ((ImageView) this.findViewById(R.id.usericon));
		if (!StringUtils.isEmpty(onlineTContact1.getIconUrl())) {
			try {
				ApolloUtils.getImageFetcher(this).loadHomeImage(
						onlineTContact1.getIconUrl(), iconmageView);
			} catch (Exception e) {
				Log.v("createRelationfrg", "jtContact.mimage err");
			}
		}

		// 附件
		downloaderLl = (FileDownloadLinearLayout) this
				.findViewById(R.id.downloaderLl);
		this.setOnFileDownloadListener(downloaderLl);

		if (onlineTContact1.getJTFileList() != null
				&& onlineTContact1.getJTFileList().size() != 0) {
			downloaderLl.setJTFiles(onlineTContact1.getJTFileList());
		} else {
			if (findViewById(R.id.downloaderLlcontainer) != null) {
				findViewById(R.id.downloaderLlcontainer).setVisibility(
						View.GONE);
			}
		}

		TextView companyPositiontextView = (TextView) this
				.findViewById(R.id.companyPosition);
		companyPositiontextView.setText(onlineTContact1.getCompany());
		TextView companyJobTv = (TextView) this.findViewById(R.id.companyJobTv);
		companyJobTv.setText(onlineTContact1.getUserJob());

		TextView nametextView = (TextView) this.findViewById(R.id.name);
		nametextView.setText(onlineTContact1.getName());

		TextView sextextView = (TextView) this.findViewById(R.id.sex);
		sextextView.setText(onlineTContact1.getSex());

		ArrayList<ShowData> baseInfoList = new ArrayList<ShowData>();
		if (this.onlineTContact != null) {
			ShowData showData = new ShowData();
			showData.key = "个人简介";
			showData.value = onlineTContact1.getRemark();
			baseInfoList.add(showData);

			showData = new ShowData();
			showData.key = "所在地区";
			showData.value = onlineTContact1.getUserAddress();
			baseInfoList.add(showData);

		} else {
			ShowData showData = new ShowData();
			showData.key = "所在分组";
			showData.value = onlineTContact1.getPeopleGroupList();
			baseInfoList.add(showData);

			showData = new ShowData();
			showData.key = "关联客户";
			showData.value = onlineTContact1.getPeopleCustomerList();
			baseInfoList.add(showData);

			showData = new ShowData();
			showData.key = "备注";
			showData.value = onlineTContact1.getRemark();
			baseInfoList.add(showData);
		}

		BasicListView2 baseInfo = (BasicListView2) this
				.findViewById(R.id.baseInfo);
		KvAdpter basekvAdpter = new KvAdpter(RelationDetails1Activity.this,
				baseInfoList);
		baseInfo.setAdapter(basekvAdpter);

		View addfriendView = this.findViewById(R.id.addfriend);
		if (onlineTContact1.isOnline) {
			if (onlineTContact1.getID().equals(App.getUserID())) {
				addfriendView.setVisibility(View.GONE);
			} else {
				if (onlineTContact1.friendState == JTContactMini.type_friend) {
					addfriendView.setVisibility(View.GONE);
				} else {
					addfriendView.setVisibility(View.VISIBLE);
				}
			}

			addfriendView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					JSONObject jb = ConnectionsReqUtil.getReqNewFriend(
							onlineTContact1.getID(), FriendRequest.type_persion);
					ConnectionsReqUtil.doReqNewFriend(
							RelationDetails1Activity.this, ib, jb, null);
					showLoadingDialog();
				}
			});
			findViewById(R.id.newbtn).setVisibility(View.VISIBLE);
		} else {
			addfriendView.setVisibility(View.GONE);
			findViewById(R.id.newbtn).setVisibility(View.GONE);
		}

		// 资源
		findViewById(R.id.resBtn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(RelationDetails1Activity.this,
						ConnsResourcesActivity.class);
				Bundle bundle = new Bundle();
				if (isonline) { // 用户
					if (onlineTContact1.getPeople()
							.expertIdentitydemandList != null) {
						for (PeopleDemandCommon item : onlineTContact1
								.getPeople().expertIdentitydemandList) {
							item.parentType= (4);
						}
					}
					bundle.putSerializable("listDemand", onlineTContact1
							.getPeople().expertIdentitydemandList);
					bundle.putBoolean("isOnline", isonline);
					bundle.putString("id", onlineTContact1.getUser().id
							+ "");
				} else { // 人脉
					if (offlineTContact.getPeople()
							.expertIdentitydemandList != null) {
						for (PeopleDemandCommon item : offlineTContact
								.getPeople().expertIdentitydemandList) {
							item.parentType=(4);
						}
					}
					bundle.putSerializable("listDemand", offlineTContact
							.getPeople().expertIdentitydemandList);
					bundle.putBoolean("isOnline", isonline);
					bundle.putString("id", offlineTContact.getPeople().id);
				}
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		// 需求
		findViewById(R.id.reqBtn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(RelationDetails1Activity.this,
						ConnsRequirementActivity.class);
				Bundle bundle = new Bundle();
				ArrayList<PeopleDemandCommon> listDemand = new ArrayList<PeopleDemandCommon>();

				if (isonline) { // 用户
					if (onlineTContact1.getPeople().investmentdemandList != null) {
						for (PeopleDemandCommon item : onlineTContact1
								.getPeople().investmentdemandList) {
							item.parentType =(1);
						}
						listDemand.addAll(onlineTContact1.getPeople()
								.investmentdemandList);
					}
					if (onlineTContact1.getPeople().financingdemandList != null) {
						for (PeopleDemandCommon item : onlineTContact1
								.getPeople().financingdemandList) {
							item.parentType = (2);
						}
						listDemand.addAll(onlineTContact1.getPeople()
								.financingdemandList);
					}
					if (onlineTContact1.getPeople().expertdemandList != null) {
						for (PeopleDemandCommon item : onlineTContact1
								.getPeople().expertdemandList) {
							item.parentType =(3);
						}
						listDemand.addAll(onlineTContact1.getPeople()
								.expertdemandList);
					}
					bundle.putSerializable("listDemand", listDemand);

					bundle.putBoolean("isOnline", isonline);
					bundle.putString("id", onlineTContact1.getUser().id
							+ "");
				} else { // 人脉
					if (offlineTContact.getPeople().investmentdemandList != null) {
						for (PeopleDemandCommon item : offlineTContact
								.getPeople().investmentdemandList) {
							item.parentType = (1);
						}
						listDemand.addAll(offlineTContact.getPeople()
								.investmentdemandList);
					}
					if (offlineTContact.getPeople().financingdemandList != null) {
						for (PeopleDemandCommon item : offlineTContact
								.getPeople().financingdemandList) {
							item.parentType = (2);
						}
						listDemand.addAll(offlineTContact.getPeople()
								.financingdemandList);
					}
					if (offlineTContact.getPeople().expertdemandList != null) {
						for (PeopleDemandCommon item : offlineTContact
								.getPeople().expertdemandList) {
							item.parentType =(3);
						}
						listDemand.addAll(offlineTContact.getPeople()
								.expertdemandList);
					}
					bundle.putSerializable("listDemand", listDemand);
					bundle.putBoolean("isOnline", isonline);
					bundle.putString("id", offlineTContact.getPeople().id);
				}
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		// 会面
		findViewById(R.id.metBtn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {}
		});
		// 活动
		findViewById(R.id.actBtn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {}
		});

		// 资源
		/*
		 * findViewById(R.id.resBtn).setOnClickListener(new OnClickListener(){
		 * 
		 * @Override public void onClick(View arg0) { // TODO Auto-generated
		 * method stub Intent intent = new
		 * Intent(RelationDetails1Activity.this,ConnsResourcesActivity.class);
		 * Bundle bundle = new Bundle(); if(isonline){ // 用户
		 * bundle.putSerializable("listDemand",
		 * onlineTContact.getPeople().getExpertdemandList());
		 * bundle.putBoolean("isOnline", isonline); bundle.putString("id",
		 * onlineTContact.getUser().getId()+""); } else{ // 人脉
		 * bundle.putSerializable("listDemand",
		 * offlineTContact.getPeople().getExpertdemandList());
		 * bundle.putBoolean("isOnline", isonline); bundle.putString("id",
		 * offlineTContact.getPeople().getId()); } intent.putExtras(bundle);
		 * startActivity(intent); } });
		 */

		// 动态
		findViewById(R.id.newbtn).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {}
		});

		ArrayList<ShowData> contactInfoList = new ArrayList<ShowData>();
		if (onlineTContact1.getPeople().contactMobileList != null) {
			for (PeopleContactComm peopleContactComm : onlineTContact1
					.getPeople().contactMobileList) {
				ShowData showData = new ShowData();
				if (peopleContactComm.typeTag != null) {
					showData.key = peopleContactComm.typeTag.name;
				}
				if (!StringUtils.isEmpty(showData.key)) {
					showData.value = peopleContactComm.content;
					contactInfoList.add(showData);
				}
			}
		}
		if (onlineTContact1.getPeople().contactFaxList != null) {
			for (PeopleContactComm peopleContactComm : onlineTContact1
					.getPeople().contactFixedList) {
				ShowData showData = new ShowData();
				if (peopleContactComm.typeTag != null) {
					showData.key = peopleContactComm.typeTag.name;
				}
				if (!StringUtils.isEmpty(showData.key)) {
					showData.value = peopleContactComm.content;
					contactInfoList.add(showData);
				}
			}
		}
		if (onlineTContact1.getPeople().contactFaxList != null) {
			for (PeopleContactComm peopleContactComm : onlineTContact1
					.getPeople().contactFaxList) {
				ShowData showData = new ShowData();
				if (peopleContactComm.typeTag != null) {
					showData.key = peopleContactComm.typeTag.name;
				}
				if (!StringUtils.isEmpty(showData.key)) {
					showData.value = peopleContactComm.content;
					contactInfoList.add(showData);
				}
			}
		}
		if (onlineTContact1.getPeople().contactMailList != null) {
			for (PeopleContactComm peopleContactComm : onlineTContact1
					.getPeople().contactMailList) {
				ShowData showData = new ShowData();
				if (peopleContactComm.typeTag != null) {
					showData.key = peopleContactComm.typeTag.name;
				}
				if (!StringUtils.isEmpty(showData.key)) {
					showData.value = peopleContactComm.content;
					contactInfoList.add(showData);
				}
			}
		}
		BasicListView2 contactInfo = (BasicListView2) this
				.findViewById(R.id.contactInfo);
		KvAdpter kvAdpter = new KvAdpter(RelationDetails1Activity.this,
				contactInfoList);
		contactInfo.setAdapter(kvAdpter);

		if (contactInfoList.size() == 0) {
			contactInfo.setVisibility(View.GONE);
		}

		ArrayList<ShowData> addressInfoList = new ArrayList<ShowData>();
		if (onlineTContact1.getPeople().contactHomeList != null) {
			for (PeopleContactComm peopleContactComm : onlineTContact1
					.getPeople().contactHomeList) {
				ShowData showData = new ShowData();
				if (peopleContactComm.typeTag != null) {
					showData.key = peopleContactComm.typeTag.name;
					if (StringUtils.isEmpty(showData.key)) {
						showData.key = "  ";
					}
				}
				if (!StringUtils.isEmpty(showData.key)) {
					showData.value = peopleContactComm.content;
					addressInfoList.add(showData);
				}
			}
		}

		if (onlineTContact1.getPeople().contactAddressList != null) {
			for (PeopleAddress peopleAddress : onlineTContact1.getPeople()
					.contactAddressList) {
				ShowData showData = new ShowData();
				if (peopleAddress.typeTag != null) {
					showData.key = peopleAddress.typeTag.name;
				}
				if (!StringUtils.isEmpty(showData.key)) {
					showData.value = peopleAddress
							.getAddressString(PeopleAddress.type_peopleAddress);
					addressInfoList.add(showData);
				}
			}
		}

		if (onlineTContact1.getPeople().personalLineList != null) {
			for (PeoplePersonalLine peopleAddress : onlineTContact1.getPeople()
					.personalLineList) {
				ShowData showData = new ShowData();
				showData.key = peopleAddress.name;
				if (!StringUtils.isEmpty(showData.key)) {
					showData.value = peopleAddress.content;
					addressInfoList.add(showData);
				}
			}
		}

		BasicListView2 addressInfoListView = (BasicListView2) this
				.findViewById(R.id.addressInfo);
		KvAdpter kvAdpter2 = new KvAdpter(RelationDetails1Activity.this,
				addressInfoList);
		addressInfoListView.setAdapter(kvAdpter2);

		if (addressInfoList.size() == 0) {
			addressInfoListView.setVisibility(View.GONE);
		}

		ArrayList<ShowData> imList = new ArrayList<ShowData>();

		if (onlineTContact1.getPeople().contactCommunicationList != null) {

			boolean isnobody = false;
			if (onlineTContact1.getPeople().contactCommunicationList
					.size() == 1) {
				if (StringUtils.isEmpty(onlineTContact1.getPeople()
						.contactCommunicationList.get(0).content)) {
					isnobody = true;
				}
			}
			if (!isnobody) {
				for (PeopleContactComm peopleAddress : onlineTContact1
						.getPeople().contactCommunicationList) {
					ShowData showData = new ShowData();
					if (peopleAddress.typeTag != null) {
						showData.key = peopleAddress.typeTag.name;
					}
					showData.value = peopleAddress.content;
					imList.add(showData);
				}
			}

		}
		BasicGridView imInfoListView = (BasicGridView) this
				.findViewById(R.id.imInfo);
		IMAdpter kvAdpter12 = new IMAdpter(RelationDetails1Activity.this,
				imList);
		imInfoListView.setAdapter(kvAdpter12);

		if (imList.size() == 0) {
			imInfoListView.setVisibility(View.GONE);
		}

		ArrayList<ShowData> countryInfoList = new ArrayList<ShowData>();

		ShowData raceNameShowData = new ShowData();
		raceNameShowData.key = "民族";
		raceNameShowData.value = onlineTContact1.getPeople().raceName;
		countryInfoList.add(raceNameShowData);

		ShowData nationalityNameShowData = new ShowData();
		nationalityNameShowData.key = "国籍";
		nationalityNameShowData.value = onlineTContact1.getPeople()
				.nationalityName;
		countryInfoList.add(nationalityNameShowData);

		ShowData birthPlaceShowData = new ShowData();
		birthPlaceShowData.key = "籍贯";
		birthPlaceShowData.value = onlineTContact1.getPeople().getBirthPlace();
		countryInfoList.add(birthPlaceShowData);

		ShowData ommunityRelationshipListShowData = new ShowData();
		ommunityRelationshipListShowData.key = "社会关系";
		ommunityRelationshipListShowData.value = onlineTContact1.getPeople()
				.getCommunityRelationshipListStr();
		countryInfoList.add(ommunityRelationshipListShowData);

		BasicListView2 countryInfoListView = (BasicListView2) this
				.findViewById(R.id.countryInfo);
		KvAdpter kvAdpter3 = new KvAdpter(RelationDetails1Activity.this,
				countryInfoList);
		countryInfoListView.setAdapter(kvAdpter3);

		if (countryInfoList.size() == 0) {
			countryInfoListView.setVisibility(View.GONE);
		}

		ArrayList<ShowData> bodyInfoList = new ArrayList<ShowData>();

		ShowData faithNameShowData = new ShowData();
		faithNameShowData.key = "信仰";
		faithNameShowData.value = onlineTContact1.getPeople().faithName;
		bodyInfoList.add(faithNameShowData);

		if (onlineTContact1.getPeople().importantDateList != null) {
			for (PeopleImportantDate peopleImportantDate : onlineTContact1
					.getPeople().importantDateList) {
				ShowData showData = new ShowData();
				if (peopleImportantDate.typeTag != null) {
					showData.key = peopleImportantDate.typeTag.name;
				}
				showData.value = peopleImportantDate.date;
				bodyInfoList.add(showData);
			}
		}

		ShowData bodySituationShowData = new ShowData();
		bodySituationShowData.key = "身体状况";
		bodySituationShowData.value = StringUtils
				.replaceAllComma2Spacing(onlineTContact1.getPeople()
						.bodySituation);
		bodyInfoList.add(bodySituationShowData);

		ShowData hobbyShowData = new ShowData();
		hobbyShowData.key = "爱好";
		hobbyShowData.value = StringUtils
				.replaceAllComma2Spacing(onlineTContact1.getPeople().hobby);
		bodyInfoList.add(hobbyShowData);

		ShowData habitShowData = new ShowData();
		habitShowData.key = "生活习惯";
		habitShowData.value = StringUtils
				.replaceAllComma2Spacing(onlineTContact1.getPeople().habit);
		bodyInfoList.add(habitShowData);

		if (onlineTContact1.getPeople().situationPersonalLineList != null) {
			for (PeoplePersonalLine peopleAddress : onlineTContact1.getPeople()
					.situationPersonalLineList) {
				ShowData showData = new ShowData();
				showData.key = peopleAddress.name;
				showData.value = peopleAddress.content;
				bodyInfoList.add(showData);
			}
		}

		BasicListView2 bodyInfoListView = (BasicListView2) this
				.findViewById(R.id.bodyInfo);
		KvAdpter kvAdpter4 = new KvAdpter(RelationDetails1Activity.this,
				bodyInfoList);
		bodyInfoListView.setAdapter(kvAdpter4);

		if (bodyInfoList.size() == 0) {
			bodyInfoListView.setVisibility(View.GONE);
		}

		if (onlineTContact1.getPeople().workExperienceList != null
				&& onlineTContact1.getPeople().workExperienceList.size() != 0) {

			BasicListView2 workListView = (BasicListView2) this
					.findViewById(R.id.workinfo);
			WorkAdpter workAdpter = new WorkAdpter(
					RelationDetails1Activity.this, (ArrayList) onlineTContact1
							.getPeople().workExperienceList,
					WorkAdpter.type_work);
			workListView.setAdapter(workAdpter);
		}

		if (onlineTContact1.getPeople().educationList != null
				&& onlineTContact1.getPeople().educationList.size() != 0) {

			BasicListView2 workListView = (BasicListView2) this
					.findViewById(R.id.eduInfo);
			WorkAdpter workAdpter = new WorkAdpter(
					RelationDetails1Activity.this, (ArrayList) onlineTContact1
							.getPeople().educationList,
					WorkAdpter.type_edu);
			workListView.setAdapter(workAdpter);
		}

		if (onlineTContact1.getPeople().personalPlateList != null
				&& onlineTContact1.getPeople().personalPlateList.size() != 0) {

			BasicListView2 workListView = (BasicListView2) this
					.findViewById(R.id.custominfo);
			CustomAdpter workAdpter = new CustomAdpter(
					RelationDetails1Activity.this, onlineTContact1.getPeople()
							.personalPlateList);
			workListView.setAdapter(workAdpter);
		}
	}
	
	/**
	 * Actionbar 菜单点击事件
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.connection_edit: // 编辑资料
			if (offlineTContact != null) { // 编辑人脉信息
//				ENavigate.startNewConnectionsActivity(
//						this, NewConnectionsActivity.type_edit_contact,
//						offlineTContact, result_edit);
			} 
			else if (onlineTContact != null) { // 编辑“我”的资料
//				ENavigate.startNewConnectionsActivity(
//						this, NewConnectionsActivity.type_edit_me, 
//						onlineTContact, result_edit);
			}
			break;
		case R.id.connection_deletefri: { // 删除好友
			showLoadingDialog();
			ConnectionsReqUtil.dodeleteFriend(
					this, ib, 
					ConnectionsReqUtil.getDeleteFriendJson(onlineTContact.getID(), FriendRequest.type_persion), null);
			break;
		}
		case R.id.connection_deletecontact: { // 删除人脉
			showLoadingDialog();
			JSONObject jb = new JSONObject();
			try {
				jb.put("id", offlineTContact.getID());
			} 
			catch (JSONException e) {
				Log.d(TAG, e.getMessage() + "");
			}
			ConnectionsReqUtil.getdelJtContact(this, ib, jb, null);
			break;
		}
		case R.id.connection_recommend: { // 推荐好友
			Intent intent = new Intent(this, IMRelationSelectActivity.class);
			intent.putExtra(ENavConsts.EFromActivityName, "");
			Connections conns = new Connections();
			conns.type = Connections.type_persion;
			conns.setID(onlineTContact.getID());
			conns.setOnline(true);
			intent.putExtra(ENavConsts.datas, conns);
			intent.putExtra(ENavConsts.EFromActivityType, IMRelationSelectActivity.FT_recommend);
			startActivityForResult(intent, result_recommend);
			break;
		}
		case R.id.connection_chat: { // 发起聊天
			ChatDetail chatDetail = new ChatDetail();
			chatDetail.setThatID(onlineTContact.getID() + "");
			chatDetail.setThatImage(onlineTContact.getIconUrl());
			chatDetail.setThatName(onlineTContact.getName());
			ENavigate.startIMActivity(this, chatDetail);
			break;
		}
		case R.id.connection_share: { // 分享到畅聊
			JTFile shareInfo = new JTFile();
			if (onlineTContact != null) {
				shareInfo = onlineTContact.toJTfile();
			} 
			else if (offlineTContact != null) {
				shareInfo = offlineTContact.toJTfile();
			}
			ENavigate.startShareActivity(this, shareInfo);
			break;
		}
		case R.id.connection_hold: { // 转为“我”的人脉
			showLoadingDialog();
			JSONObject jb = new JSONObject();
			try {
				jb.put("id", offlineTContact.getID());
			} 
			catch (JSONException e) {
				Log.d(TAG, e.getMessage() + "");
			}
			ConnectionsReqUtil.im_holdJTContact(this, ib, jb, null);
			break;
		}
		case R.id.connection_yaoqing: { // 邀请加入
			Connections yaoqingConnections = offlineTContact.toConnections();
			String mobile = yaoqingConnections.getMobilePhone();
			if (!StringUtils.isEmpty(mobile)) {
				sendSMS(mobile);
			} 
			else {
				String email = yaoqingConnections.getEmail();
				if (!StringUtils.isEmpty(email)) {
					JSONObject jo = new JSONObject();
					JSONArray ja = new JSONArray();
					ja.put(email);
					try {
						jo.put("listEmail", ja);
					} 
					catch (JSONException e) {
						Log.d(TAG, e.getMessage() + "");
					}
					if (jo != null) {
						ConnectionsReqUtil.getInviteJoinGinTong(this, ib, jo, null);
					}
				} 
				else {
					if (yaoqingConnections.getmType() == Connections.type_org) {
						showToast("该客户没有邮箱和电话，请完善客户资料");
					} 
					else {
						showToast("该人脉没有邮箱和电话，请完善人脉资料");
					}
				}
			}
			break;
		}
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 发送短信
	 * @param smsnumber
	 */
	private void sendSMS(String smsnumber) {
		String str = App.getApp().getAppData().mInviteJoinGinTongInfo;
		Uri smsToUri = Uri.parse("smsto:" + smsnumber);
		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
		intent.putExtra("sms_body", str);
		startActivity(intent);
	}

	/**
	 * 填充模版中的基本信息 
	 * @param jtContactTemplate
	 */
	private void fillBasicInfomation2Template(JTContact2 jtContactTemplate) {

		if (jtContactTemplate.getMyPeopleGroupTempList() == null) {
			jtContactTemplate.setMyPeopleGroupTempList(new ArrayList<PeopleGroupTemp>());
		}
		if (jtContactTemplate.getMyPeopleCustomerList() == null) {
			jtContactTemplate.setMyPeopleCustomerList(new ArrayList<PeopleCustomer>());
		}
		if (jtContactTemplate.getMyPeopleSelectTagList() == null) {
			jtContactTemplate.setMyPeopleSelectTagList(new ArrayList<PeopleSelectTag>());
		}
		PeopleTemp peopleTemp = jtContactTemplate.getPeople();
		if (peopleTemp.peopleNameList == null) {
			peopleTemp.peopleNameList =(new ArrayList<PeopleName>());
		}
		if (peopleTemp.peopleNameList.size() == 0) {
			PeopleName peopleName = new PeopleName();
			PeopleSelectTag peopleSelectTag = new PeopleSelectTag(0, "1", "中文名");
			peopleName.name =("");
			peopleName.typeTag =(peopleSelectTag);
			peopleTemp.peopleNameList.add(peopleName);
		}
		if (peopleTemp.gender== 0) {
			Integer gender = Integer.valueOf(3);
			peopleTemp.gender = (gender);
		}
		if (peopleTemp.peopleGroupList == null) {
			peopleTemp.peopleGroupList=(new ArrayList<PeopleGroupTemp>());
		}
		if (peopleTemp.peopleCustomerList == null) {
			peopleTemp.peopleCustomerList =(new ArrayList<PeopleCustomer>());
		}
	}

	/**
	 * 填充模版中的联系方式
	 * @param jtContactTemplate
	 */
	private void fillContactInfomation2Template(JTContact2 jtContactTemplate) {
		PeopleTemp peopleTemp = jtContactTemplate.getPeople();
		if (peopleTemp.contactMobileList == null) { // 手机
			peopleTemp.contactMobileList=(new ArrayList<PeopleContactComm>());
		}
		if (peopleTemp.contactMobileList.size() == 0) {
			PeopleContactComm contactComm = new PeopleContactComm();
			contactComm.content=("");
			PeopleSelectTag typeTag = new PeopleSelectTag(0, "1", "手机");
			contactComm.typeTag=(typeTag);
			peopleTemp.contactMobileList.add(contactComm);
		}
		if (peopleTemp.contactFixedList == null) { // 固话
			peopleTemp.contactFixedList = (new ArrayList<PeopleContactComm>());
		}
		if (peopleTemp.contactFixedList.size() == 0) {
			PeopleContactComm contactComm = new PeopleContactComm();
			contactComm.content = ("");
			PeopleSelectTag typeTag = new PeopleSelectTag(0, "1", "固话");
			contactComm.typeTag = (typeTag);
			peopleTemp.contactFixedList .add(contactComm);
		}
		if (peopleTemp.contactFaxList == null) { // 传真
			peopleTemp.contactFaxList =(new ArrayList<PeopleContactComm>());
		}
		if (peopleTemp.contactFaxList.size() == 0) {
			PeopleContactComm contactComm = new PeopleContactComm();
			contactComm.content =("");
			PeopleSelectTag typeTag = new PeopleSelectTag(0, "1", "住宅传真");
			contactComm.typeTag = (typeTag);
			peopleTemp.contactFaxList.add(contactComm);
		}
		if (peopleTemp.contactMailList == null) { // 邮箱类型
			ArrayList<PeopleContactComm> mailList = new ArrayList<PeopleContactComm>();
			peopleTemp.contactMailList = (mailList);
		}
		if (peopleTemp.contactMailList.size() == 0) {
			for (int i = 0; i < 1; i++) {
				PeopleContactComm contactComm = new PeopleContactComm();
				contactComm.content = ("");
				PeopleSelectTag typeTag = new PeopleSelectTag(0, "1", "主要邮箱");
				contactComm.typeTag = (typeTag);
				peopleTemp.contactMailList.add(contactComm);
			}
		}
		if (peopleTemp.contactHomeList == null) { // 主页类型
			peopleTemp.contactHomeList=(new ArrayList<PeopleContactComm>());
		}
		if (peopleTemp.contactHomeList.size() == 0) {
			PeopleContactComm contactComm = new PeopleContactComm();
			contactComm.content =("");
			PeopleSelectTag typeTag = new PeopleSelectTag(0, "1", "个人主页");
			contactComm.typeTag = (typeTag);
			peopleTemp.contactHomeList.add(contactComm);
		}
		if (peopleTemp.contactCommunicationList == null) { // 通讯类型
			peopleTemp.contactCommunicationList = (new ArrayList<PeopleContactComm>());
		}
		if (peopleTemp.contactCommunicationList.size() == 0) {
			for (int i = 0; i < 1; i++) {
				PeopleContactComm contactComm = new PeopleContactComm();
				contactComm.content =("");
				PeopleSelectTag typeTag = new PeopleSelectTag(0, "1", "QQ");
				contactComm.typeTag =(typeTag);
				peopleTemp.contactCommunicationList.add(contactComm);
			}
		}
		if (peopleTemp.contactAddressList == null) { // 地址
			peopleTemp.contactAddressList = (new ArrayList<PeopleAddress>());
		}
		if (peopleTemp.contactAddressList.size() == 0) {
			PeopleAddress address = new PeopleAddress();
			PeopleSelectTag typeTag = new PeopleSelectTag(0, "1", "住宅地址");
			address.typeTag = (typeTag);
			address.areaType = (0);
			address.stateName = ("");
			address.cityName =("");
			address.countyName =("");
			peopleTemp.contactAddressList.add(address);
		}
		if (peopleTemp.personalLineList == null) { // 自定义字段
			peopleTemp.personalLineList=(new ArrayList<PeoplePersonalLine>());
		}
	}

	/**
	 * 填充模版中的个人情况
	 * @param jtContactTemplate
	 */
	private void fillPersonalInfomation2Template(JTContact2 jtContactTemplate) {
		PeopleTemp peopleTemp = jtContactTemplate.getPeople();
		if (peopleTemp.raceName== null){
			peopleTemp.raceName = ("");
		}
		if (peopleTemp.nationalityName  == null){
			peopleTemp.nationalityName = ("");
		}
		if (peopleTemp.birthPlaceCountryName  == null){
			peopleTemp.birthPlaceCountryName = ("");
		}
		if (peopleTemp.birthPlaceStateName == null){
			peopleTemp.birthPlaceStateName = ("");
		}
		if (peopleTemp.birthPlaceCityName == null){
			peopleTemp.birthPlaceCityName=("");
		}
		if (peopleTemp.birthPlaceCountyName == null){
			peopleTemp.birthPlaceCountyName =("");
		}
		if (peopleTemp.faithName == null){
			peopleTemp.faithName = ("");
		}
		if (peopleTemp.bodySituation == null){
			peopleTemp.bodySituation = ("");
		}
		if (peopleTemp.hobby == null){
			peopleTemp.hobby = ("");
		}
		if (peopleTemp.habit == null){
			peopleTemp.habit = ("");
		}
		// 重要日期
		if (peopleTemp.importantDateList == null) {
			peopleTemp.importantDateList=(new ArrayList<PeopleImportantDate>());
		}
		if (peopleTemp.importantDateList.size() == 0) {
			PeopleImportantDate peopleImportantDate = new PeopleImportantDate();
			PeopleSelectTag typeTag = new PeopleSelectTag(0, "1", "生日");
			peopleImportantDate.typeTag = (typeTag);
			peopleImportantDate.date = ("");
			peopleTemp.importantDateList.add(peopleImportantDate);
		}
		if (peopleTemp.communityRelationshipList == null) {
			peopleTemp.communityRelationshipList=(new ArrayList<PeopleCommunityRelationship>());
		}
		if (peopleTemp.communityRelationshipList.size() == 0) {
			PeopleCommunityRelationship communityRelationship = new PeopleCommunityRelationship();
			PeopleSelectTag typeTag = new PeopleSelectTag(0, "1", "配偶");
			communityRelationship.typeTag = (typeTag);
			communityRelationship.content = ("");
			peopleTemp.communityRelationshipList.add(communityRelationship);
		}
		// 自定义字段
		if (peopleTemp.situationPersonalLineList == null) {
			peopleTemp.situationPersonalLineList = (new ArrayList<PeoplePersonalLine>());
		}
	}

	/**
	 * 填充模版中的会面情况
	 * @param jtContactTemplate
	 */
	private void fillMeeting2Template(JTContact2 jtContactTemplate) {

		PeopleTemp peopleTemp = jtContactTemplate.getPeople();
		if (peopleTemp.meetingList == null) {
			ArrayList<PeopleMetting> peopleMettingList = new ArrayList<PeopleMetting>();
			PeopleMetting peopleMetting = new PeopleMetting();
			peopleMettingList.add(peopleMetting);
			peopleTemp.meetingList=(peopleMettingList);
		}
		PeopleMetting peopleMetting = peopleTemp.meetingList.get(0);
		if (peopleMetting.areaType == null){ // 地区类型：0-国内，1-国外
			peopleMetting.areaType = (0);
		}
		if (peopleMetting.personalLineList == null) { // 自定义字段
			ArrayList<PeoplePersonalLine> customList = new ArrayList<PeoplePersonalLine>();
			peopleMetting.personalLineList = (customList);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		if(type == ENavConsts.type_details_guest){ // 游客身份浏览人脉详情，不能进行任何操作
			return true;
		}
		
		getMenuInflater().inflate(R.menu.relationdetails, menu);

		menuEdit = menu.findItem(R.id.connection_edit);
		menuEdit.setVisible(false);

		menuDeleteFriend = menu.findItem(R.id.connection_deletefri);
		menuDeleteFriend.setVisible(false);

		menuDeleteContact = menu.findItem(R.id.connection_deletecontact);
		menuDeleteContact.setVisible(false);

		menuChat = menu.findItem(R.id.connection_chat);
		menuChat.setVisible(false);

		menuRecommend = menu.findItem(R.id.connection_recommend);
		menuRecommend.setVisible(false);

		menuShare = menu.findItem(R.id.connection_share);
		menuShare.setVisible(false);

		menuSave = menu.findItem(R.id.connection_hold);
		menuSave.setVisible(false);

		menuInvite = menu.findItem(R.id.connection_yaoqing);
		menuInvite.setVisible(false);

		return true;
	}

	@Override
	public void initJabActionBar() {
		getActionBar().setDisplayShowTitleEnabled(true);
	}

	// 工作经历、教育经历列表适配器
	public static class KvAdpter extends BaseAdapter {
		
		public static final String TAG_TIME = "time";
		private Context context;
		private ArrayList<ShowData> contactMobileList;

		public KvAdpter(Context context, ArrayList<ShowData> contactMobileList) {
			this.context = context;
			this.contactMobileList = contactMobileList;
		}

		@Override
		public int getCount() {
			if (contactMobileList == null || contactMobileList.size() == 0) {
				return 0;
			}
			return contactMobileList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ShowData peopleContactComm = contactMobileList.get(position);
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			if (!StringUtils.isEmpty(peopleContactComm.key)) {
				if (peopleContactComm.key.equalsIgnoreCase(KvAdpter.TAG_TIME)) {
					convertView = inflater.inflate(R.layout.relationdetails_item_time, parent, false);
					TextView key1 = (TextView) convertView.findViewById(R.id.key1);
					key1.setText(peopleContactComm.value);
					TextView key2 = (TextView) convertView.findViewById(R.id.key2);
					key2.setText(peopleContactComm.value1);
				} 
				else {
					convertView = inflater.inflate(R.layout.relationdetails_item1, parent, false);
					TextView key = (TextView) convertView.findViewById(R.id.key);
					if (peopleContactComm.key != null) {
						key.setText(peopleContactComm.key);
					}
					((TextView) convertView.findViewById(R.id.value)).setText(peopleContactComm.value);
				}
			}
			return convertView;
		}
	}

	public static class ShowData {
		public String key = "";
		public String value = "";
		public String value1 = "";
	}

	// 联系方式列表适配器
	class IMAdpter extends BaseAdapter {
		
		private Context context;
		private ArrayList<ShowData> contactMobileList;

		public IMAdpter(Context context, ArrayList<ShowData> contactMobileList) {
			this.context = context;
			this.contactMobileList = contactMobileList;
		}

		@Override
		public int getCount() {
			if (contactMobileList == null || contactMobileList.size() == 0) {
				return 0;
			}
			return contactMobileList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ShowData peopleContactComm = contactMobileList.get(position);
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.relationdetails_item2, parent, false);
			}
			if (peopleContactComm.key != null) {
				ImageView imageView = ((ImageView) convertView.findViewById(R.id.key));
				if (peopleContactComm.key.equalsIgnoreCase("QQ")) {
					imageView.setImageResource(R.drawable.icon_qq);
				} 
				else if (peopleContactComm.key.equalsIgnoreCase("微信")) {
					imageView.setImageResource(R.drawable.icon_weixin);
				} 
				else if (peopleContactComm.key.equalsIgnoreCase("MSN")) {
					imageView.setImageResource(R.drawable.icon_msn);
				} 
				else if (peopleContactComm.key.equalsIgnoreCase("Skype")) {
					imageView.setImageResource(R.drawable.icon_skype);
				} 
				else if (peopleContactComm.key.equalsIgnoreCase("微博")) {
					imageView.setImageResource(R.drawable.icon_weibo);
				} 
				else if (peopleContactComm.key.equalsIgnoreCase("facebook")) {
					imageView.setImageResource(R.drawable.icon_facebook);
				} 
				else if (peopleContactComm.key.equalsIgnoreCase("twitter")) {
					imageView.setImageResource(R.drawable.icon_twitter);
				} 
				else {
					imageView.setImageResource(R.drawable.im_icon_default);
				}
			}
			((TextView) convertView.findViewById(R.id.value)).setText(peopleContactComm.value);
			return convertView;
		}

	}

	// 工作经历、教育经历列表适配器
	class WorkAdpter extends BaseAdapter {
		
		public static final int type_work = 0;
		public static final int type_edu = 1;
		private Context context;
		private ArrayList workExperienceList;
		private int type = 0;

		public WorkAdpter(Context context, ArrayList workExperienceList, int type) {
			this.context = context;
			this.workExperienceList = workExperienceList;
			this.type = type;
		}

		@Override
		public int getCount() {
			if (workExperienceList == null || workExperienceList.size() == 0) {
				return 0;
			}
			return workExperienceList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.relationdetails_itemlist, parent, false);
			}
			BasicListView2 basicListView2 = (BasicListView2) convertView.findViewById(R.id.list);
			if (type == type_work) {
				PeopleWorkExperience peopleContactComm = (PeopleWorkExperience) workExperienceList.get(position);
				KvAdpter kvAdpter = new KvAdpter(RelationDetails1Activity.this, peopleContactComm.getListData());
				basicListView2.setAdapter(kvAdpter);
			} 
			else {
				PeopleEducation peopleContactComm = (PeopleEducation) workExperienceList.get(position);
				KvAdpter kvAdpter = new KvAdpter(RelationDetails1Activity.this, peopleContactComm.getListData());
				basicListView2.setAdapter(kvAdpter);
			}
			return convertView;
		}
	}

	// 人脉自定义模块列表适配器
	class CustomAdpter extends BaseAdapter {
		
		private Context context;
		private ArrayList<PeoplePersonalPlate> personalPlateList;

		public CustomAdpter(Context context, ArrayList<PeoplePersonalPlate> personalPlateList) {
			this.context = context;
			this.personalPlateList = personalPlateList;
		}

		@Override
		public int getCount() {
			if (personalPlateList == null || personalPlateList.size() == 0) {
				return 0;
			}
			return personalPlateList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.relationdetails_itemlist2, parent, false);
			}
			BasicListView2 basicListView2 = (BasicListView2) convertView.findViewById(R.id.list);
			TextView textview = (TextView) convertView.findViewById(R.id.text);
			PeoplePersonalPlate peopleContactComm = (PeoplePersonalPlate) personalPlateList.get(position);
			KvAdpter kvAdpter = new KvAdpter(RelationDetails1Activity.this,peopleContactComm.getListData());
			basicListView2.setAdapter(kvAdpter);
			textview.setText(peopleContactComm.name);
			return convertView;
		}
	}

	// 
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == result_edit) { // 编辑人脉
			if (resultCode == Activity.RESULT_OK) {
				showLoadingDialog();
				JSONObject jsonObj = ConnectionsReqUtil.getContactDetailJson(userId, isonline);
				ConnectionsReqUtil.doContactDetail(this, updataib, jsonObj, null);
			}
		} 
		else if (requestCode == result_recommend) { // 推荐的人脉
			if (resultCode == Activity.RESULT_OK) {
				Object obj = intent.getSerializableExtra(ENavConsts.redatas);
				if (obj != null) {
					@SuppressWarnings("unchecked")
					ArrayList<Connections> temps = (ArrayList<Connections>) obj;
					if (temps != null && temps.size() != 0) {
						JSONObject jo = new JSONObject();
						JSONArray js = new JSONArray();
						try {
							for (Connections connections : temps) {
								js.put(connections.getId());
							}
							jo.put("destJTContactID", js);
							jo.put("jtContactID", userId);
						} 
						catch (JSONException e) {
							Log.d(TAG, e.getMessage() + "");
						}
						ConnectionsReqUtil.getRecommend2Friend(this, ib, jo, null);
						return;
					}
				}
				showToast("人员选择为空");
			}
		}
	}

}
