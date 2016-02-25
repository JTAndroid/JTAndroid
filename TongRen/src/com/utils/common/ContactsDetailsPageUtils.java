package com.utils.common;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.tr.App;
import com.tr.api.ConnectionsReqUtil;
import com.tr.api.OrganizationReqUtil;
import com.tr.api.PeopleReqUtil;
import com.tr.model.connections.FriendRequest;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.obj.Connections;
import com.tr.model.obj.JTContactMini;
import com.tr.navigate.ENavConsts;
import com.tr.navigate.ENavigate;
import com.tr.ui.connections.revision20150122.detail.RelationHomeActivity;
import com.tr.ui.home.HomePageActivity;
import com.tr.ui.home.frg.HomePageFrag;
import com.tr.ui.organization.create_clientele.ClientDetailsActivity;
import com.tr.ui.organization.model.Collect;
import com.tr.ui.people.contactsdetails.ContactsDetailsActivity;
import com.tr.ui.people.model.PeopleDetails;
import com.tr.ui.people.model.Person;
import com.tr.ui.people.model.PersonCollectId;
import com.tr.ui.widgets.MessageDialog;
import com.tr.ui.widgets.MessageDialog.OnDialogFinishListener;
import com.tr.ui.widgets.title.menu.popupwindow.ActionItem;
import com.tr.ui.widgets.title.menu.popupwindow.TitlePopup;
import com.tr.ui.widgets.title.menu.popupwindow.TitlePopup.OnPopuItemOnClickListener;
import com.utils.http.EAPIConsts.OrganizationReqType;
import com.utils.http.EAPIConsts.PeopleRequestType;
import com.utils.http.IBindData;

public class ContactsDetailsPageUtils {
	public Context mConnext;
	public FragmentActivity mActivity;
	public String userId;
	public TitlePopup titlePopup;
	public IBindData mIBindData;
	public int type;// 1用户 2人脉
	public PeopleDetails peopleDetails = new PeopleDetails();
	public boolean is_good_friend;
	public boolean is_collect_people = false;

	public PeopleDetails getPeopleDetails() {
		return peopleDetails;
	}

	public void setPeopleDetails(PeopleDetails peopleDetails) {
		this.peopleDetails = peopleDetails;
	}

	public ContactsDetailsPageUtils() {
		super();
	}

	public void showDetailsPageUtil() {
		if(titlePopup != null){
			titlePopup.cleanAction();
		}
		if (type == 2 && !userId.equals(App.getUserID()) && is_good_friend) {// 好友人脉
			titlePopup.setItemOnClickListener(onitemClickConnection);
			titlePopup.addAction(new ActionItem(mConnext, "分享名片"));
			titlePopup.addAction(new ActionItem(mConnext, "邀请加入"));
			titlePopup.addAction(new ActionItem(mConnext, "转为事务"));
		} else if (type == 1 && !userId.equals(App.getUserID())
				&& is_good_friend) {// 好友用户
			titlePopup.setItemOnClickListener(onitemClickFriends);
			titlePopup.addAction(new ActionItem(mConnext, "分享名片"));
			titlePopup.addAction(new ActionItem(mConnext, "解除好友关系"));
			titlePopup.addAction(new ActionItem(mConnext, "加入黑名单"));
			titlePopup.addAction(new ActionItem(mConnext, "转为事务"));
			titlePopup.addAction(new ActionItem(mConnext, "转为人脉"));
			titlePopup.addAction(new ActionItem(mConnext, "举报"));
		}
		if (userId.equals(App.getUserID())) {// 自己
			titlePopup.setItemOnClickListener(onitemClickConnection);
			titlePopup.addAction(new ActionItem(mConnext, "分享名片"));
		} else if (!is_good_friend) {// 不是好友人脉 是 人脉详情页
			titlePopup.setItemOnClickListener(onitemClickContactDetails);
			titlePopup.addAction(new ActionItem(mConnext, "分享名片"));
			titlePopup.addAction(new ActionItem(mConnext, "保存"));
			if(is_collect_people || peopleDetails.fromType == 3){
				titlePopup.addAction(new ActionItem(mConnext, "取消收藏"));
			}else {
				titlePopup.addAction(new ActionItem(mConnext, "收藏"));
			}
			titlePopup.addAction(new ActionItem(mConnext, "举报"));
			titlePopup.addAction(new ActionItem(mConnext, "查看发布人"));
		}
	}

	/**
	 * 人脉详情
	 */
	private OnPopuItemOnClickListener onitemClickContactDetails = new OnPopuItemOnClickListener() {

		@Override
		public void onItemClick(ActionItem item, int position) {
			switch (position) {
			case 0:// 分享我的名片
				HomePageFrag homePageFrag = (HomePageFrag) mActivity
						.getSupportFragmentManager().getFragments().get(0);
				homePageFrag.doshare();
				break;
			case 1:// 保存
				ENavigate.startNewConnectionsActivity(mActivity, 4,
						peopleDetails, null);
				break;
			case 2:// 收藏
				collectingContacts();
				break;
			case 3:// 举报
				ENavigate.startReportActivity(mConnext, peopleDetails.people);
				break;
			case 4:// 查看发布人
				viewPublisher();
				break;
			default:
				break;
			}
		}
	};

	private OnPopuItemOnClickListener onitemClickConnection = new OnPopuItemOnClickListener() {

		@Override
		public void onItemClick(ActionItem item, int position) {
			switch (position) {
			case 0:// 分享我的名片
				HomePageFrag homePageFrag = (HomePageFrag) mActivity
						.getSupportFragmentManager().getFragments().get(0);
				homePageFrag.doshare();
				break;
			case 1:// 邀请加入
				ENavigate.startMeetingInviteFriendsActivity(mConnext);
				break;
			case 2:// 转为事务
				turnToTransaction();
				break;
			default:
				break;
			}
		}

	};

	private OnPopuItemOnClickListener onitemClickFriends = new OnPopuItemOnClickListener() {

		@Override
		public void onItemClick(ActionItem item, int position) {
			switch (position) {
			case 0:// 分享我的名片
				HomePageFrag homePageFrag = (HomePageFrag) mActivity
						.getSupportFragmentManager().getFragments().get(0);
				homePageFrag.doshare();
				break;
			case 1:// 解除好友关系
				ConnectionsReqUtil.dodeleteFriend(mConnext, mIBindData,
						ConnectionsReqUtil.getDeleteFriendJson(
								String.valueOf(userId),
								FriendRequest.type_persion), null);
				break;
			case 2:// 加入黑名单
				showAddBlackNumDialog();
				break;
			case 3:// 转为事务
				turnToTransaction();
				break;
			case 4:// 转为人脉
				ENavigate.startNewConnectionsActivity(mActivity, 5,
						peopleDetails, null);
				break;
			case 5:// 举报
				ENavigate.startReportActivity(mConnext, peopleDetails.people);
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 收藏人脉
	 */
	private void collectingContacts() {
		PersonCollectId person_Id = new PersonCollectId();
		person_Id.personId = Long.valueOf(userId);
		if (peopleDetails.fromType == 3 || is_collect_people) {
			PeopleReqUtil.doRequestWebAPI(
					mConnext,
					mIBindData,
					person_Id, null,
					PeopleRequestType.CANCEL_COLLECT);
		} else {
			PeopleReqUtil.doRequestWebAPI(
					mConnext,
					mIBindData,
					person_Id, null,
					PeopleRequestType.COLLECT_PEOPLE);
		}
	}

	/**
	 * 查看发布人
	 */
	private void viewPublisher() {
		int isSelfOrOther = -1;
		if (App.getUserID().equals(userId)) {
			isSelfOrOther = ENavConsts.type_details_member;
		} else {
			isSelfOrOther = ENavConsts.type_details_other;
		}
		ENavigate.startRelationHomeActivity(mConnext, userId + "", true,
				isSelfOrOther);

	}

	/** 加入黑名单 */
	private void showAddBlackNumDialog() {
		new MessageDialog(mActivity, "加入黑名单你将收不到对方的信息,\n并且你们相互看不到对方的主页",
				new OnDialogFinishListener() {
					@Override
					public void onFinish(String content) {
						if (peopleDetails.people != null) {
							ArrayList<String> arrayList = new ArrayList<String>();
							arrayList.add(String.valueOf(userId));
							ConnectionsReqUtil.doEditBlack(mConnext,
									mIBindData, arrayList, "1", null);
						}
					}

					@Override
					public void onCancel(String content) {

					}
				}).show();
	}

	/**
	 * 转为事务
	 */
	private void turnToTransaction() {
		ConnectionNode inPeopleNode = new ConnectionNode();
		ArrayList<Connections> listConnections = new ArrayList<Connections>();
		JTContactMini jtContact = new JTContactMini();
		jtContact.setId(userId);
		Person person = peopleDetails.people;
		String personName = person.peopleNameList != null
				&& person.peopleNameList.size() > 0
				&& person.peopleNameList.get(0) != null ? person.peopleNameList
				.get(0).lastname : "";
		jtContact.setName(personName);
		jtContact.setImage(person.portrait);
		jtContact.setCompany(person.company);
		if (type == 1)
			jtContact.setOnline(true);
		else
			jtContact.setOnline(false);
			
		Connections connections = new Connections(jtContact);
		connections.id = userId;
		listConnections.add(connections);
		inPeopleNode.setListConnections(listConnections);
		inPeopleNode.setMemo("好友");
		ENavigate.startNewAffarActivityByRelation(mActivity, inPeopleNode,
				null, null, null);
	}

}
