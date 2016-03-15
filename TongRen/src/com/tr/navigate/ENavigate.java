package com.tr.navigate;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.R.bool;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.common.category.CategoryActivity;
import com.common.constvalue.EnumConst.ModuleType;
import com.tr.App;
import com.tr.model.conference.JTFile2ForHY;
import com.tr.model.conference.MMeetingPic;
import com.tr.model.conference.MMeetingQuery;
import com.tr.model.conference.MMeetingTopicQuery;
import com.tr.model.demand.AmountData;
import com.tr.model.demand.DemandDetailsData;
import com.tr.model.demand.ImageItem;
import com.tr.model.demand.LableData;
import com.tr.model.demand.Metadata;
import com.tr.model.demand.NoteData;
import com.tr.model.demand.TemplateData;
import com.tr.model.home.MIndustrys;
import com.tr.model.home.MListCountry;
import com.tr.model.im.ChatDetail;
import com.tr.model.im.MNotifyMessageBox;
import com.tr.model.joint.AffairNode;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.joint.KnowledgeNode;
import com.tr.model.joint.ResourceNode;
import com.tr.model.knowledge.Knowledge2;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.knowledge.UserCategory;
import com.tr.model.obj.Connections;
import com.tr.model.obj.ForwardDynamicNews;
import com.tr.model.obj.IMBaseMessage;
import com.tr.model.obj.JTContact2;
import com.tr.model.obj.JTContactMini;
import com.tr.model.obj.JTFile;
import com.tr.model.obj.KnowledgeMini;
import com.tr.model.obj.MUCDetail;
import com.tr.model.obj.ResourceBase;
import com.tr.service.GetConnectionsListService;
import com.tr.service.GetConnectionsListService.RequestType;
import com.tr.ui.cloudDisk.FileManagementActivity;
import com.tr.ui.common.FilePreviewActivity;
import com.tr.ui.common.ImageDetailActivity;
import com.tr.ui.common.JointColumnActivity;
import com.tr.ui.common.JointResourceActivity;
import com.tr.ui.common.JointResourceFragment.ResourceType;
import com.tr.ui.common.MoreJointResourceActivity;
import com.tr.ui.common.NewJointResourceFragment.ResourceType_new;
import com.tr.ui.common.NewJointResourceMainFragment.ResourceSource;
import com.tr.ui.common.RelatedResourceActivity;
import com.tr.ui.common.SearchAffairActivity;
import com.tr.ui.common.SearchRequirementActivity;
import com.tr.ui.common.ViewWebURLActivity;
import com.tr.ui.communities.home.CommunitiesDetailsActivity;
import com.tr.ui.communities.home.CommunityEditSettingActivity;
import com.tr.ui.communities.im.CommunityChatActivity;
import com.tr.ui.communities.model.ImMucinfo;
import com.tr.ui.conference.home.MeetingInvitationCardActivity;
import com.tr.ui.conference.home.MeetingMasterActivity;
import com.tr.ui.conference.home.RTMeetingNoteActivity;
import com.tr.ui.conference.im.MChatFilePreviewActivity;
import com.tr.ui.conference.im.MChatImageBrowserActivity;
import com.tr.ui.conference.im.MChatRecordSearchActivity;
import com.tr.ui.conference.im.MeetingChatActivity;
import com.tr.ui.conference.initiatorhy.EditTextContentActivity;
import com.tr.ui.conference.initiatorhy.InitiatorHYActivity;
import com.tr.ui.conference.initiatorhy.InviteFriendActivity;
import com.tr.ui.conference.initiatorhy.MeetingSpeakerTopicSettingActivity;
import com.tr.ui.conference.myhy.ui.MyMeetingActivity;
import com.tr.ui.conference.square.ImagesBrowseActivity;
import com.tr.ui.conference.square.SquareActivity;
import com.tr.ui.conference.square.TansmitMeetingListActivity;
import com.tr.ui.connections.revision20150122.PushPeople2Activity;
import com.tr.ui.connections.revision20150122.detail.ForwardToFriendActivity;
import com.tr.ui.connections.revision20150122.detail.RelationMoreEvaluationActivity;
import com.tr.ui.customization.CustomizationActivity;
import com.tr.ui.demand.AccessoryActivity;
import com.tr.ui.demand.AmountOfMoneyActivity;
import com.tr.ui.demand.CreateDemandActivity;
import com.tr.ui.demand.CreateLabelActivity;
import com.tr.ui.demand.DemandCategoryActivity;
import com.tr.ui.demand.DemandLableActivity;
import com.tr.ui.demand.FindProjectActivity;
import com.tr.ui.demand.HeadPortraitActivity;
import com.tr.ui.demand.IntroduceActivity;
import com.tr.ui.demand.LimitsActivity;
import com.tr.ui.demand.MeNeedActivity;
import com.tr.ui.demand.NeedDetailsActivity;
import com.tr.ui.demand.NewDemandActivity;
import com.tr.ui.demand.NewDemandActivity.DemandEnum;
import com.tr.ui.demand.RedactLabelActivity;
import com.tr.ui.demand.ReportMessageActivity;
import com.tr.ui.demand.SelectPictureActivity;
import com.tr.ui.demand.TemplateActivity;
import com.tr.ui.demand.choose.ChooseActivity;
import com.tr.ui.flow.CreateFlowActivtiy;
import com.tr.ui.flow.DynamicPermissionsSettingActvitiy;
import com.tr.ui.flow.FlowActivity;
import com.tr.ui.flow.frg.FrgFlow;
import com.tr.ui.home.DemandAndPricesActivity;
import com.tr.ui.home.EditBusinessCardActivity;
import com.tr.ui.home.HomePageActivity;
import com.tr.ui.home.InviteFriendByQRCodeActivity;
import com.tr.ui.home.InviteFriendsActivity;
import com.tr.ui.home.MainActivity;
import com.tr.ui.home.MyQRCodeActivity;
import com.tr.ui.home.OnekeyBackActivity;
import com.tr.ui.im.AtInformFriendsActivity;
import com.tr.ui.im.ChatActivity;
import com.tr.ui.im.ChatImageBrowserActivity;
import com.tr.ui.im.ChatRecordSearchActivity;
import com.tr.ui.im.GroupChatActivity;
import com.tr.ui.im.IMCreateGroupActivity;
import com.tr.ui.im.IMCreateGroupCategoryActivity1;
import com.tr.ui.im.IMEditMemberActivity;
import com.tr.ui.im.IMRelationSelectActivity;
import com.tr.ui.knowledge.CreateKnowledgeActivity;
import com.tr.ui.knowledge.EditKnowledgeTagActivity;
import com.tr.ui.knowledge.GlobalKnowledgeTagActivity;
import com.tr.ui.knowledge.KnowledgeCategoryLabelActivity;
import com.tr.ui.knowledge.KnowledgeContentActivity;
import com.tr.ui.knowledge.KnowledgeOfDetailActivity;
import com.tr.ui.knowledge.KnowledgeSquareActivity;
import com.tr.ui.knowledge.MatchKnowledgeActivity;
import com.tr.ui.knowledge.NeturlKnowledgeActivity;
import com.tr.ui.knowledge.utils.ActivityCollector;
import com.tr.ui.mystart.MyStartActivity;
import com.tr.ui.organization.create_clientele.ClientDetailsActivity;
import com.tr.ui.organization.create_clientele.CreateClienteleActivity;
import com.tr.ui.organization.create_clientele.CreateOrganizationActivity;
import com.tr.ui.organization.firstpage.FindProjectActivityOrg;
import com.tr.ui.organization.firstpage.OrganizationAndCustomerActivity;
import com.tr.ui.organization.firstpage.OrganizationCategoryActivity;
import com.tr.ui.organization.model.RegisteOrgDetail;
import com.tr.ui.organization.model.param.CustomerClientParams;
import com.tr.ui.organization.model.param.CustomerOrganizatianParams;
import com.tr.ui.organization.orgdetails.ClientEditRelationEvaluationTagActivity;
import com.tr.ui.organization.orgdetails.OrgEditRelationEvaluationTagActivity;
import com.tr.ui.organization.orgdetails.OrgMyHomePageActivity;
import com.tr.ui.organization.orgdetails.OrganizationReportActivity;
import com.tr.ui.organization.orgdetails.orgfragment.OrgRelationMoreEvaluationActivity;
import com.tr.ui.organization.orgfragment.OrgClientMoreEvaluationActivity;
import com.tr.ui.people.contactsdetails.ContactsDetailsActivity;
import com.tr.ui.people.contactsdetails.ContactsEditRelationEvaluationTagActivity;
import com.tr.ui.people.contactsdetails.ContactsRelationMoreEvaluationActivity;
import com.tr.ui.people.contactsdetails.ReportActivity;
import com.tr.ui.people.cread.NewConnectionsActivity;
import com.tr.ui.people.homepage.ContactsMainPageActivity;
import com.tr.ui.people.homepage.PeopleCategoryLabelActivity;
import com.tr.ui.people.model.PeopleDetails;
import com.tr.ui.people.model.Person;
import com.tr.ui.relationship.DetailsRelationListActivity;
import com.tr.ui.relationship.GroupListActivity;
import com.tr.ui.relationship.MyFriendAllActivity;
import com.tr.ui.relationship.NewConnectionActivity;
import com.tr.ui.relationship.RelationDetails1Activity;
import com.tr.ui.relationship.SIMContactActivity;
import com.tr.ui.requirement.MatchRequirementActivity;
import com.tr.ui.search.SearchActivity;
import com.tr.ui.search.SearchActivity_more;
import com.tr.ui.search.SearchActivity_new;
import com.tr.ui.share.ShareActivity;
import com.tr.ui.share.ShareDetailActivity;
import com.tr.ui.share.SocialShareActivity;
import com.tr.ui.tongren.adapter.ProjectAdapter.ProjectType;
import com.tr.ui.tongren.home.OrganizationRecordActivity;
import com.tr.ui.tongren.home.ProjectDetailsActivity;
import com.tr.ui.user.AccountInfoManagerActivity;
import com.tr.ui.user.AccountManagerActivity;
import com.tr.ui.user.AgreementActivity;
import com.tr.ui.user.BlackListActivity;
import com.tr.ui.user.UserSettingActivity;
import com.tr.ui.user.modified.BindingEmailActivity;
import com.tr.ui.user.modified.BindingMobileActivity;
import com.tr.ui.user.modified.ChangePasswordActivity;
import com.tr.ui.user.modified.CompleteUserInfoActivity;
import com.tr.ui.user.modified.CountryCodeActivity;
import com.tr.ui.user.modified.InputVerifyCodeActivity;
import com.tr.ui.user.modified.LoadingGuideActivity;
import com.tr.ui.user.modified.LoginActivity;
import com.tr.ui.user.modified.ProfessionActivity;
import com.tr.ui.user.modified.ProfessionAndCustomzationActivity;
import com.tr.ui.user.modified.ProfessionSecondActivity;
import com.tr.ui.user.modified.RegisterActivity;
import com.tr.ui.user.modified.RegisterGintongAccountActivity;
import com.tr.ui.user.modified.RegisterPersonalActivity;
import com.tr.ui.user.modified.ResetPasswordSuccessActivity;
import com.tr.ui.user.modified.UnBindingEmailActivity;
import com.tr.ui.user.modified.UnBindingMobileActivity;
import com.tr.ui.user.modified.WantPeopleActivity;
import com.tr.ui.work.WorkNewActivity;
import com.utils.common.EConsts;
import com.utils.common.GlobalVariable;
import com.utils.string.StringUtils;
import com.utils.time.Util;

/** @des: 导航类，不同acivity之间跳转， 用这个类里的方法来实现，用来限定参数 */
public class ENavigate {
	private final static String TAG = "ENavigate";
	public static final int REQUSET_CODE = 100;
	public static final int REQUSET_CODE_CHAT = 101;
	public static final int REQUSET_CODE_MUC = 102;

	public static void startCustomizationActivity(Activity fromActivity) {
		Intent intent = new Intent(fromActivity, CustomizationActivity.class);
		// intent.putExtra(EConsts.TAG_PROGRAM_SUB_ID, programSubID);
		// intent.putExtra(EConsts.TAG_PROGRTAM_NAME, programName);
		// intent.putExtra(EConsts.TAG_PROGRAM_ID, programID);
		// intent.putExtra(EConsts.TAG_PROGRAM_TYPE, pt.getName());
		// intent.putExtra(EConsts.TAG_IS_FROM_DETAIL, isFromDetail);

		fromActivity.startActivity(intent);
	}

	/**
	 * 登录界面
	 * 
	 * @param fromActivity
	 * @param friendId
	 *            没有就传null
	 */
	public static void startLoginActivity(Activity fromActivity, String friendId) {
		Intent intent = new Intent(fromActivity, LoginActivity.class);
		System.out
				.println("---------------LoginActivity--------Navigate friendId"
						+ friendId);
		intent.putExtra(ENavConsts.EFriendId, friendId);
		fromActivity.startActivity(intent);
	}

	public static void startLoadingGuideActivity(Activity fromActivity,
			String friendId) {
		Intent intent = new Intent(fromActivity, LoadingGuideActivity.class);
		System.out
				.println("---------------startLoadingGuideActivity--------Navigate friendId"
						+ friendId);
		intent.putExtra(ENavConsts.EFriendId, friendId);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动main界面
	 * 
	 * @param fromActivity
	 * @param iNotifyMessageBox
	 * @param data
	 *            二维码扫描跳转参数传递 可以传空 ：
	 */
	public static void startMainActivity(Activity fromActivity,
			MNotifyMessageBox box, String friendId) {
		Intent intent = new Intent(fromActivity, MainActivity.class);
		if (box != null) {
			intent.putExtra(ENavConsts.ENotifyParam, box);
			// 如果activity在task存在，将Activity之上的所有Activity结束掉
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// 如果activity在task存在，拿到最顶端,不会启动新的Activity
			intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		}
		System.out
				.println("----------MainActivity1-------------Navigate friendId"
						+ friendId);
		intent.putExtra(ENavConsts.EFriendId, friendId);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动main界面
	 * 
	 * @param fromActivity
	 * @param iNotifyMessageBox
	 * @param data
	 *            二维码扫描跳转参数传递 可以传空 ：
	 */
	public static void startIMFaileMainActivity(Activity fromActivity,
			MNotifyMessageBox box, String friendId, boolean isFaile) {
		Intent intent = new Intent(fromActivity, MainActivity.class);
		intent.putExtra("isFaile", isFaile);
		if (box != null) {
			intent.putExtra(ENavConsts.ENotifyParam, box);
			// 如果activity在task存在，将Activity之上的所有Activity结束掉
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// 如果activity在task存在，拿到最顶端,不会启动新的Activity
			intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		}
		System.out
				.println("----------MainActivity1-------------Navigate friendId"
						+ friendId);
		intent.putExtra(ENavConsts.EFriendId, friendId);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动main界面
	 * 
	 * @param fromActivity
	 */
	public static void startMainActivity(Activity fromActivity) {
		Intent intent = new Intent(fromActivity, MainActivity.class);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动Main界面
	 * 
	 * @param fromActivity
	 * @param box
	 * @param pushMessageType
	 */
	public static void startMainActivity(Activity fromActivity,
			MNotifyMessageBox box, int pushMessageType, String friendId) {
		Intent intent = new Intent(fromActivity, MainActivity.class);
		if (box != null) {
			intent.putExtra(ENavConsts.ENotifyParam, box);
			intent.putExtra(ENavConsts.EPushMessageType, pushMessageType);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		}
		System.out
				.println("----------MainActivity2-------------Navigate friendId"
						+ friendId);
		intent.putExtra(ENavConsts.EFriendId, friendId);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动main界面
	 * 
	 * @param fromActivity
	 * @param ShareInfo
	 *            ：
	 */
	public static void startMainActivityEx(Activity fromActivity,
			JTFile shareInfo, String friendId) {
		Intent intent = new Intent(fromActivity, MainActivity.class);
		intent.putExtra(ENavConsts.EShareParam, shareInfo);
		System.out
				.println("----------MainActivityEx-------------Navigate friendId"
						+ friendId);
		intent.putExtra(ENavConsts.EFriendId, friendId);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动分享页面
	 * 
	 * @param fromActivity
	 * @param shareInfo
	 */
	public static void startShareActivity(Activity fromActivity,
			JTFile shareInfo) {
		Intent intent = new Intent(fromActivity, ShareActivity.class);
		intent.putExtra(ENavConsts.EShareParam, shareInfo);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动搜索界面
	 * 
	 * @param fromActivity
	 * 
	 *            type: 1,人脉 2 组织 3,知识 4,社交 5,需求
	 * 
	 */
	public static void startSearchActivity(Activity fromActivity, int type) {
		Intent intent = new Intent(fromActivity, SearchActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra("type", type);
		fromActivity.startActivity(intent);
	}

	public static void startSearchMoreActivity(Activity fromActivity, int type,
			String keyword) {
		Intent intent = new Intent(fromActivity, SearchActivity_more.class);
		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra("keyword", keyword);
		intent.putExtra("type", type);
		fromActivity.startActivity(intent);
	}

	public static void startNewSearchActivity(Activity fromActivity, int type) {
		Intent intent = new Intent(fromActivity, SearchActivity_new.class);
		fromActivity.startActivity(intent);
	}

	// public static void startSearchMoreActivity(Activity fromActivity, int
	// type, String keyword) {
	// Intent intent = new Intent(fromActivity, SearchActivity_more.class);
	// intent.putExtra(ENavConsts.EFromActivityName,
	// fromActivity.getClass().getSimpleName());
	// intent.putExtra("keyword", keyword);
	// intent.putExtra("type", type);
	// fromActivity.startActivity(intent);
	// }

	public static void startNewSearchActivity(Activity fromActivity) {
//<<<<<<< HEAD
//		// Intent intent = new Intent(fromActivity, SearchActivity_new.class);
//		Intent intent = new Intent(fromActivity, NewSearchActivity.class);
//=======
		Intent intent = new Intent(fromActivity, SearchActivity_new.class);
//		Intent intent = new Intent(fromActivity, NewSearchActivity.class);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动新关系界面
	 * 
	 * @param fromActivity
	 */
	public static void startNewConnectionActivity(Activity fromActivity) {
		Intent intent = new Intent(fromActivity, NewConnectionActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass()
				.getSimpleName());
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动一个私聊界面
	 * 
	 * @param fromActivity
	 * @param chatDetail
	 *            私聊对象，包含对方的名称，id，头像等
	 */
	public static void startIMActivity(Activity fromActivity,
			ChatDetail chatDetail) {
		Intent intent = new Intent(fromActivity, ChatActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra(ENavConsts.EChatDetail, chatDetail);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动一个私聊界面
	 * 
	 * @param fromActivity
	 * @param chatDetail
	 * @param requestCode
	 *            私聊对象，包含对方的名称，id，头像等
	 */
	public static void startIMActivity(Activity fromActivity,
			ChatDetail chatDetail, int requestCode) {
		Intent intent = new Intent(fromActivity, ChatActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra(ENavConsts.EChatDetail, chatDetail);
		fromActivity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 启动一个私聊界面
	 * 
	 * @param fromActivity
	 * @param msg
	 */
	public static void startIMActivity(Activity fromActivity,
			ChatDetail chatDetail, IMBaseMessage msg) {
		Intent intent = new Intent(fromActivity, ChatActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra(ENavConsts.EChatDetail, chatDetail);
		intent.putExtra(ENavConsts.EIMBaseMessage, msg);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动一个私聊界面
	 * 
	 * @param fromActivity
	 * @param chatDetail
	 * @param searchMessageId
	 * @param searchKeyword
	 */
	public static void startIMActivity(Activity fromActivity,
			ChatDetail chatDetail, int fromIndex, String searchKeyword) {
		Intent intent = new Intent(fromActivity, ChatActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra(ENavConsts.EChatDetail, chatDetail);
		intent.putExtra(ENavConsts.ESearchKeyword, searchKeyword);
		intent.putExtra(ENavConsts.ESearchFromIndex, fromIndex);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动私聊界面（转发多个消息）
	 * 
	 * @param fromActivity
	 * @param chatDetail
	 * @param listJtFile
	 */
	public static void startIMActivity(Activity fromActivity,
			ChatDetail chatDetail, ArrayList<JTFile> listJtFile) {
		Intent intent = new Intent(fromActivity, ChatActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra(ENavConsts.EChatDetail, chatDetail);
		intent.putExtra(ENavConsts.EShareParamList, listJtFile);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动私聊界面（转发多个消息）
	 * 
	 * @param fromActivity
	 * @param chatDetail
	 * @param listJtFile
	 */
	/*
	 * public static void startIMActivity(Activity fromActivity, ChatDetail
	 * chatDetail, ArrayList<JTFile> listJtFile,HashMap<String,SendMessages>
	 * sendMessagesForwardingList) { Intent intent = new Intent(fromActivity,
	 * ChatActivity.class); intent.putExtra(ENavConsts.EFromActivityName,
	 * fromActivity.getClass() .getSimpleName());
	 * intent.putExtra(ENavConsts.EChatDetail, chatDetail);
	 * intent.putExtra(ENavConsts.EShareParamList, listJtFile);
	 * intent.putExtra(ENavConsts.SendMessagesForwardingList,
	 * sendMessagesForwardingList); fromActivity.startActivity(intent); }
	 */

	/**
	 * 启动私聊界面（转发单个消息）
	 * 
	 * @param fromActivity
	 * @param chatDetail
	 * @param listJtFile
	 */
	public static void startIMActivity(Activity fromActivity,
			ChatDetail chatDetail, JTFile jtFile) {
		Intent intent = new Intent(fromActivity, ChatActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra(ENavConsts.EChatDetail, chatDetail);
		intent.putExtra(ENavConsts.EShareParam, jtFile);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动群聊页面
	 * 
	 * @param fromActivity
	 * @mucID mucID 群聊/会议id
	 */
	public static void startIMGroupActivity(Activity fromActivity, String mucID) {
		Intent intent = new Intent(fromActivity, GroupChatActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra(ENavConsts.EMucID, mucID);
		fromActivity.startActivity(intent);
	}
	/**
	 * 启动社群聊天页面
	 * 
	 * @param fromActivity
	 * @mucID mucID 群聊/会议id
	 */
	public static void startIMCommunityActivity(Activity fromActivity, ImMucinfo community) {
		Intent intent = new Intent(fromActivity, CommunityChatActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass().getSimpleName());
		intent.putExtra("community", community);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动群聊页面
	 * 
	 * @param fromActivity
	 * @mucID mucID 群聊/会议id
	 */
	public static void startIMGroupActivity(Activity fromActivity,
			int requestCode, String mucID) {
		Intent intent = new Intent(fromActivity, GroupChatActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra(ENavConsts.EMucID, mucID);
		fromActivity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 启动群聊页面
	 * 
	 * @param fromActivity
	 * @mucID mucID 群聊/会议id
	 * @type 0正常模式（默认） 1（与从事物中的群聊一致，不能删除成员，不能解散畅聊）
	 */
	public static void startIMGroupActivity(Activity fromActivity,
			String mucID, int type, boolean isMemberO) {
		Intent intent = new Intent(fromActivity, GroupChatActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra(ENavConsts.EMucID, mucID);
		intent.putExtra(ENavConsts.EStartIMGroupChatType, type);
		intent.putExtra("isMemberO", isMemberO);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动群聊页面
	 * 
	 * @param fromActivity
	 * @mucID mucDetail 群聊/会议详情
	 */
	public static void startIMGroupActivity(Activity fromActivity,
			MUCDetail mucDetail) {
		Intent intent = new Intent(fromActivity, GroupChatActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra(ENavConsts.EMucDetail, mucDetail);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动群聊页面
	 * 
	 * @param fromActivity
	 * @param mucDetail
	 * @param searchMessageId
	 * @param searchKeyword
	 */
	public static void startIMGroupActivity(Activity fromActivity,
			MUCDetail mucDetail, int fromIndex, String searchKeyword) {
		Intent intent = new Intent(fromActivity, GroupChatActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra(ENavConsts.EMucDetail, mucDetail);
		intent.putExtra(ENavConsts.ESearchFromIndex, fromIndex);
		intent.putExtra(ENavConsts.ESearchKeyword, searchKeyword);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动群聊页面
	 * 
	 * @param fromActivity
	 * @param mucID
	 * @param msg
	 */
	public static void startIMGroupActivity(Activity fromActivity,
			String mucID, IMBaseMessage msg) {
		Intent intent = new Intent(fromActivity, GroupChatActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra(ENavConsts.EMucID, mucID);
		intent.putExtra(ENavConsts.EIMBaseMessage, msg);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动群聊页面
	 * 
	 * @param fromActivity
	 * @param locateMsgId
	 * @param msg
	 */
	public static void startIMGroupActivity(Activity fromActivity,
			String mucID, String locateMsgId) {
		Intent intent = new Intent(fromActivity, GroupChatActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra(ENavConsts.EMucID, mucID);
		intent.putExtra(ENavConsts.ELocateMessageID, locateMsgId);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动群聊页面
	 * 
	 * @param fromActivity
	 * @param mucDetail
	 * @param msg
	 */
	public static void startIMGroupActivity(Activity fromActivity,
			MUCDetail mucDetail, IMBaseMessage msg) {
		Intent intent = new Intent(fromActivity, GroupChatActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra(ENavConsts.EMucDetail, mucDetail);
		intent.putExtra(ENavConsts.EIMBaseMessage, msg);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动群聊界面
	 * 
	 * @param fromActivity
	 * @param mucID
	 * @param listJtFile
	 */
	public static void startIMGroupActivity(Activity fromActivity,
			String mucID, ArrayList<JTFile> listJtFile) {
		Intent intent = new Intent(fromActivity, GroupChatActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra(ENavConsts.EMucID, mucID);
		intent.putExtra(ENavConsts.EShareParamList, listJtFile);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动群聊页面
	 * 
	 * @param fromActivity
	 * @param mucDetail
	 * @param listJtFile
	 */
	public static void startIMGroupActivity(Activity fromActivity,
			MUCDetail mucDetail, ArrayList<JTFile> listJtFile) {
		Intent intent = new Intent(fromActivity, GroupChatActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra(ENavConsts.EMucDetail, mucDetail);
		intent.putExtra(ENavConsts.EShareParamList, listJtFile);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动群聊页面
	 * 
	 * @param fromActivity
	 * @param mucDetail
	 * @param jtFile
	 */
	public static void startIMGroupActivity(Activity fromActivity,
			MUCDetail mucDetail, JTFile jtFile) {
		Intent intent = new Intent(fromActivity, GroupChatActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra(ENavConsts.EMucDetail, mucDetail);
		intent.putExtra(ENavConsts.EShareParam, jtFile);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动群聊页面
	 * 
	 * @param fromActivity
	 * @param mucID
	 * @param jtFile
	 */
	public static void startIMGroupActivity(Activity fromActivity,
			String mucID, JTFile jtFile) {
		Intent intent = new Intent(fromActivity, GroupChatActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra(ENavConsts.EMucID, mucID);
		intent.putExtra(ENavConsts.EShareParam, jtFile);
		fromActivity.startActivity(intent);
	}

	/**
	 * 私聊记录搜索页面
	 * 
	 * @param fromActivity
	 * @param mucId
	 */
	public static void startChatRecordSearchActivity(Activity fromActivity,
			String chatId, ChatDetail chatDetail) {

		Intent intent = new Intent(fromActivity, ChatRecordSearchActivity.class);
		intent.putExtra(ENavConsts.EMucID, chatId);
		intent.putExtra(ENavConsts.EChatDetail, chatDetail);
		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass()
				.getSimpleName());
		fromActivity.startActivity(intent);
	}

	/**
	 * 群聊记录搜索页面
	 * 
	 * @param fromActivity
	 * @param mucId
	 */
	public static void startMUCRecordSearchActivity(Activity fromActivity,
			String mucId, MUCDetail mucDetail) {

		Intent intent = new Intent(fromActivity, ChatRecordSearchActivity.class);
		intent.putExtra(ENavConsts.EMucID, mucId);
		intent.putExtra(ENavConsts.EMucDetail, mucDetail);
		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass()
				.getSimpleName());
		fromActivity.startActivity(intent);
	}

	/**
	 * 跳转到选择联系人页面，根据不同来与，联系人页面有不同处理方法
	 * 
	 * @param fromActivity
	 * @param detail
	 * @param list
	 *            已经选中的人列表
	 * @requestCode: 请求码,如果需要用forResult启动,则传入请求码, >0; 0->表示直接启动
	 * @listJtFile 默认传null,只有当需要 创建新畅聊后 立即分享Jtfile 时，此集合有值，否则传null。
	 */
	public static void startIMRelationSelectActivity(Activity fromActivity,
			MUCDetail detail, ChatDetail chatDetail, int requestCode,
			ArrayList<Connections> list, ArrayList<JTFile> listJtFile) {
		Intent intent = new Intent();
		intent.setClass(fromActivity, IMRelationSelectActivity.class);

		if (detail != null)
			intent.putExtra(ENavConsts.EMucDetail, detail);
		if (chatDetail != null)
			intent.putExtra(ENavConsts.EChatDetail, chatDetail);
		if (listJtFile != null)
			intent.putExtra(ENavConsts.EListJTFile, listJtFile);

		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass()
				.getSimpleName());

		if (list != null) {
			// intent.putExtra(ENavConsts.datas, list);
		}
		if (requestCode > 0)
			fromActivity.startActivityForResult(intent, requestCode);
		else
			fromActivity.startActivity(intent);
	}

	public static void startIMRelationSelectActivity(Activity fromActivity,
			MUCDetail detail, ImMucinfo community, int applyType, int requestCode) {
		Intent intent = new Intent();
		intent.setClass(fromActivity, IMRelationSelectActivity.class);

		if (detail != null)
			intent.putExtra(ENavConsts.EMucDetail, detail);
		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass().getSimpleName());
		intent.putExtra("community", community);
		intent.putExtra("applyType", applyType);
		if (requestCode > 0)
			fromActivity.startActivityForResult(intent, requestCode);
		else
			fromActivity.startActivity(intent);
	}

	/**
	 * 跳转到选择联系人页面，(新建事务选择人时用,可以和其它页面选择人时共用) 关系数据包括 个人好友和 组织好友 混排的没有分类
	 * 
	 * @param fromActivity
	 *            启动跳转的Activity
	 * @requestCode: 请求码
	 * @param fromActivityType
	 *            关系列表过滤类型
	 * @param connectionsList
	 *            选中的关系列表 或 初始化删除的关系列表
	 * @param singleSelection
	 *            是否单选
	 * @param isInitInDataDel
	 *            是否初始化删除传进来的数据
	 * @param isInitMyself
	 *            是否初始化我自己数据
	 * @param isIndispensableSelect
	 *            是否必需选择
	 * 
	 * @author gushi
	 */
	public static void startIMRelationSelectActivity(Activity fromActivity,
			int requestCode, int fromActivityType,
			ArrayList<Connections> connectionsList, boolean isSingleSelection,
			boolean isInitInDataDel, boolean isInitMyself,
			boolean isIndispensableSelect) {
		Intent intent = new Intent(fromActivity, IMRelationSelectActivity.class);

		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra(ENavConsts.EFromActivityType, fromActivityType);
		intent.putExtra(ENavConsts.IsSingleSelection, isSingleSelection);
		intent.putExtra(ENavConsts.IsInitInDataDel, isInitInDataDel);
		intent.putExtra(ENavConsts.IsInitMyself, isInitMyself);
		intent.putExtra(ENavConsts.isIndispensableSelect, isIndispensableSelect);

		if (connectionsList != null) {
			intent.putExtra(ENavConsts.datas, connectionsList);
		}
		fromActivity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 启动分享页面
	 * 
	 * @param fromActivity
	 * @param type
	 * @param listJtFile
	 */
	public static void startShareActivity(Activity fromActivity, int type,
			ArrayList<JTFile> listJtFile) {
		Intent intent = new Intent(fromActivity, ShareActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra(ENavConsts.EShareType, type);
		intent.putExtra(ENavConsts.EShareParamList, listJtFile);
		fromActivity.startActivity(intent);
	}

	/**
	 * 会议设置页面,包括 会议详情/创建会议/群聊升级为会议都用到
	 * 
	 * @detail: 如果是创建会议,该值为null,如果是群聊升级为会议,或者进入会议详情,则该detail不为空
	 * @param fromActivity
	 */
	public static void startIMCreateGroupActivity(Activity fromActivity,
			MUCDetail detail, int requestCode) {
		Intent intent = new Intent(fromActivity, IMCreateGroupActivity.class);

		intent.putExtra(ENavConsts.EMucDetail, detail);
		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass()
				.getSimpleName());

		if (requestCode > 0) {
			fromActivity.startActivityForResult(intent, requestCode);
		} else {
			fromActivity.startActivity(intent);
		}
	}

	/**
	 * 跳转到选择联系人页面（分享）
	 * 
	 * @param fromActivity
	 * @param jtfile
	 */
	public static void startIMRelationSelectActivityEx(Activity fromActivity,
			JTFile jtfile) {
		Intent intent = new Intent(fromActivity, IMRelationSelectActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra(ENavConsts.EShareParam, jtfile);
		fromActivity.startActivity(intent);
	}

	/**
	 * 跳转到选择联系人页面（分享）
	 * 
	 * @param fromActivity
	 * @param listJtfile
	 */
	public static void startIMRelationSelectActivityEx(Activity fromActivity,
			ArrayList<JTFile> listJtfile) {
		Intent intent = new Intent(fromActivity, IMRelationSelectActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra(ENavConsts.EShareParamList, listJtfile);
		fromActivity.startActivity(intent);
	}

	/**
	 * 跳转到选择联系人页面（分享）
	 * 
	 * @param fromActivity
	 * @param jtfile
	 */
	public static void startIMRelationSelectActivityEx(Activity fromActivity,
			String fromName) {
		Intent intent = new Intent(fromActivity, IMRelationSelectActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, fromName);
		fromActivity.startActivity(intent);
	}

	/**
	 * 跳转到选择联系人页面（分享）
	 * 
	 * @param fromActivity
	 * @param jtfile
	 */
	public static void startIMRelationSelectActivityEx(Activity fromActivity,
			String fromName, int requestCode) {
		Intent intent = new Intent(fromActivity, IMRelationSelectActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, fromName);
		fromActivity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 从私聊或者群聊页面切换到聊天设置页
	 * 
	 * @param fromActivity
	 * @param detail
	 * @param requestCode
	 */
	public static void startIMEditMumberActivity(Activity fromActivity,
			MUCDetail detail, ChatDetail chatDetail, int requestCode,
			boolean enableSearchChatRecord) {
		Intent intent = new Intent(fromActivity, IMEditMemberActivity.class);

		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		if (detail != null) {
			intent.putExtra(ENavConsts.EMucDetail, detail);
		} else {
			intent.putExtra(ENavConsts.EChatDetail, chatDetail);
		}

		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass()
				.getSimpleName());
		// 是否支持清空聊天记录
		intent.putExtra(ENavConsts.EEnableSearchChatRecord,
				enableSearchChatRecord);

		if (requestCode > 0) {
			fromActivity.startActivityForResult(intent, requestCode);
		} else {
			fromActivity.startActivity(intent);
		}

	}

	/**
	 * 从私聊或者群聊页面切换到聊天设置页
	 * 
	 * @param fromActivity
	 * @param detail
	 * @param requestCode
	 * @type 0正常模式（默认） 1（与从事物中的群聊一致，不能删除成员，不能解散畅聊）
	 */
	public static void startIMEditMumberActivity(Activity fromActivity,
			MUCDetail detail, ChatDetail chatDetail, int requestCode,
			boolean enableSearchChatRecord, int type, boolean isMemberO) {
		Intent intent = new Intent(fromActivity, IMEditMemberActivity.class);

		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		if (detail != null) {
			intent.putExtra(ENavConsts.EMucDetail, detail);
		} else {
			intent.putExtra(ENavConsts.EChatDetail, chatDetail);
		}

		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass()
				.getSimpleName());
		// 是否支持清空聊天记录
		intent.putExtra(ENavConsts.EEnableSearchChatRecord,
				enableSearchChatRecord);

		intent.putExtra(ENavConsts.EStartIMGroupChatType, type);
		intent.putExtra("isMemberO", isMemberO);

		if (requestCode > 0) {
			fromActivity.startActivityForResult(intent, requestCode);
		} else {
			fromActivity.startActivity(intent);
		}

	}

	// 启动 我发起的 页面
	public static void startMyStartActivity(Activity fromActivity) {
		Intent intent = new Intent(fromActivity, MyStartActivity.class);
		fromActivity.startActivity(intent);
	}

	/**
	 * 分享详情页面
	 * 
	 * @param context
	 * @param file
	 */
	public static void startShareDetailActivity(Context context, JTFile jtfile) {
		Intent intent = new Intent(context, ShareDetailActivity.class);
		intent.putExtra(EConsts.Key.JT_FILE, jtfile);
		context.startActivity(intent);
	}

	/**
	 * 分享详情页面
	 * 
	 * @param context
	 * @param knoMini
	 */
	public static void startShareDetailActivity(Context context,
			KnowledgeMini knoMini) {
		Intent intent = new Intent(context, ShareDetailActivity.class);
		intent.putExtra(EConsts.Key.KNOWLEDGE, knoMini);
		context.startActivity(intent);
	}

	/**
	 * 匹配需求页面
	 * 
	 * @param context
	 * @param id
	 */
	public static void startMatchRequirementActivity(Context context, int type,
			String id) {
		Intent intent = new Intent(context, MatchRequirementActivity.class);
		intent.putExtra(EConsts.Key.TYPE, type);
		intent.putExtra(EConsts.Key.ID, id);
		context.startActivity(intent);
	}

	/**
	 * 匹配知识页面
	 * 
	 * @param context
	 * @param id
	 */
	public static void startMatchKnowledgeActivity(Context context, int type,
			String id) {
		Intent intent = new Intent(context, MatchKnowledgeActivity.class);
		intent.putExtra(EConsts.Key.TYPE, type);
		intent.putExtra(EConsts.Key.ID, id);
		context.startActivity(intent);
	}

	/**
	 * 选择需求
	 * 
	 * @param context
	 * @param id
	 */
	public static void startSearchRequirementActivity(Activity activity) {
		Intent intent = new Intent(activity, SearchRequirementActivity.class);
		activity.startActivityForResult(intent,
				EConsts.REQ_CODE_SEARCH_REQUIREMENT);
	}

	/**
	 * 选择事务
	 * 
	 * @param context
	 * @param id
	 */
	public static void startSearchAffairActivity(Activity activity, int type) {
		Intent intent = new Intent(activity, SearchAffairActivity.class);
		intent.putExtra(EConsts.Key.TYPE, type);
		activity.startActivityForResult(intent, EConsts.REQ_CODE_SEARCH_AFFAIR);
	}

	/**
	 * 启动用户、人脉详情界面
	 * 
	 * @param context
	 * @param id
	 *            用户id
	 * @param isOnline
	 *            线上还是线下
	 * @datatype:
	 */
	public static void startUserDetailsActivity(Context context, String id,
			boolean isOnline, int dataType) {
		Intent intent = new Intent(context, RelationDetails1Activity.class);
		intent.putExtra(EConsts.Key.ID, id);
		intent.putExtra(EConsts.Key.isOnline, isOnline);
		intent.putExtra(ENavConsts.EFromActivityType, dataType);
		context.startActivity(intent);
	}

	/**
	 * 用户/人脉主页(新)
	 * 
	 * @param context
	 * @param id
	 *            用户id
	 * @param isOnline
	 *            线上还是线下
	 * @datatype:
	 * 
	 */
	public static void startRelationHomeActivity(Context context, String id,
			boolean isOnline, int dataType) {

//		Intent intent = new Intent(context, RelationHomeActivity.class);
		//产品要求更改新的UI，新页面。全局更改导航方法
		Intent intent = new Intent(context, HomePageActivity.class);
		intent.putExtra(EConsts.Key.ID, id);
		intent.putExtra(EConsts.Key.isOnline, isOnline);
		intent.putExtra(ENavConsts.EFromActivityType, dataType);
		context.startActivity(intent);
	}
	
	/**
	 * 用户/人脉主页(新)
	 * 
	 * @param context
	 * @param id
	 *            用户id
	 * @param isOnline
	 *            线上还是线下
	 * @datatype:
	 * 
	 */
	public static void startRelationHomeActivity(Context context, String id,
			boolean isOnline, int dataType,boolean isGoodFriend) {

//		Intent intent = new Intent(context, RelationHomeActivity.class);
		//产品要求更改新的UI，新页面。全局更改导航方法
		Intent intent = new Intent(context, HomePageActivity.class);
		intent.putExtra(EConsts.Key.ID, id);
		intent.putExtra(EConsts.Key.isOnline, isOnline);
		intent.putExtra(EConsts.Key.IS_GOOD_FRIEND, isGoodFriend);
		intent.putExtra(ENavConsts.EFromActivityType, dataType);
		context.startActivity(intent);
	}

	/**
	 * 开启用户详情(新用户人脉，使用person对象)
	 * 
	 * @param context
	 * @param id
	 *            用户id
	 */
	public static void startRelationHomeActivity(Context context, String id) {

//		Intent intent = new Intent(context, RelationHomeActivity.class);
		//产品要求更改新的UI，新页面。全局更改导航方法
		Intent intent = new Intent(context, HomePageActivity.class);
		intent.putExtra(EConsts.Key.ID, id);
		Activity activity = (Activity) context;
		activity.startActivityForResult(intent, REQUSET_CODE);
	}
	
	/**
	 * 开启用户详情(新用户人脉，使用person对象)
	 * 
	 * @param context
	 * @param id
	 *            用户id
	 *  @param  isGoodFriend         
	 */
	public static void startRelationHomeActivity(Context context, String id,boolean isGoodFriend) {

//		Intent intent = new Intent(context, RelationHomeActivity.class);
		//产品要求更改新的UI，新页面。全局更改导航方法
		Intent intent = new Intent(context, HomePageActivity.class);
		intent.putExtra(EConsts.Key.ID, id);
		intent.putExtra(EConsts.Key.IS_GOOD_FRIEND, isGoodFriend);
		Activity activity = (Activity) context;
		activity.startActivityForResult(intent, REQUSET_CODE);
	}
	
	/**
	 * 跳转组织详情界面(旧)
	 * 
	 * @param context
	 * @param id
	 *            用户id
	 * 
	 * @datatype:
	 */
	public static void startRelationHomeActivity(Context context,
			long customerId, long createById, boolean isOnline, int dataType,boolean isGoodFriend) {
		
		Intent intent = new Intent(context, OrgMyHomePageActivity.class);
		intent.putExtra("customerId", customerId);// 组织id
		intent.putExtra("createById", createById);// 创建者id
		intent.putExtra(EConsts.Key.IS_GOOD_FRIEND, isGoodFriend);
		intent.putExtra(EConsts.Key.isOnline, isOnline);
		intent.putExtra(ENavConsts.EFromActivityType, dataType);
		((Activity) context).startActivityForResult(intent, dataType);
	}

	/**
	 * 跳转组织详情界面(旧)
	 * 
	 * @param context
	 * @param id
	 *            用户id
	 * 
	 * @datatype:
	 */
	public static void startOrgMyHomePageActivity(Context context,
			long customerId, long createById, boolean isOnline, int dataType) {

		Intent intent = new Intent(context, OrgMyHomePageActivity.class);
		intent.putExtra("customerId", customerId);// 组织id
		intent.putExtra("createById", createById);// 创建者id
		intent.putExtra(EConsts.Key.isOnline, isOnline);
		intent.putExtra(ENavConsts.EFromActivityType, dataType);
		((Activity) context).startActivityForResult(intent, dataType);
	}

	/**
	 * 跳转组织详情界面(新)
	 * 
	 * @param context
	 * @param id
	 *            组织的orgId
	 * 
	 * @datatype:
	 */
	public static void startOrgMyHomePageActivityByOrgId(Context context,
			long customerId) {

		Intent intent = new Intent(context, OrgMyHomePageActivity.class);
		intent.putExtra("customerId", customerId);// customerId对应请求组织详情页请求参数的orgId
		context.startActivity(intent);
	}

	/**
	 * 跳转组织详情界面(新)
	 * 
	 * @param context
	 * @param id
	 *            组织的userId
	 * 
	 * @datatype:
	 */
	public static void startOrgMyHomePageActivityByUseId(Context context,
			long userId) {

		Intent intent = new Intent(context, OrgMyHomePageActivity.class);
		intent.putExtra("userId", userId);// 对应请求组织详情页请求参数的userId
		context.startActivity(intent);
	}

	/**
	 * 跳转客户详情界面
	 * 
	 * @param context
	 * @param id
	 *            用户id
	 */
	public static void startClientDedailsActivity(Context context,
			long customerId) {

		Intent intent = new Intent(context, ClientDetailsActivity.class);
		intent.putExtra(EConsts.Key.CUSTOMERID, customerId);
		context.startActivity(intent);
	}

	/**
	 * 跳转客户详情界面
	 * 
	 * @param context
	 * @param customerId
	 *            客户ID
	 * @param view
	 *            是否需要进行权限控制 0 受权限控制 1 不受权限控制
	 * @param label
	 *            是否需要进行权限控制 6 受权限控制
	 */
	public static void startClientDedailsActivity(Context context,
			long customerId, int view, int label) {
		Intent intent = new Intent(context, ClientDetailsActivity.class);

		intent.putExtra(EConsts.Key.CUSTOMERID, customerId);
		intent.putExtra(EConsts.Key.CONTROL, view);
		intent.putExtra("label", label);
		context.startActivity(intent);
	}

	/**
	 * 启动更多评价页面(人脉)
	 * 
	 * @param context
	 */
	public static void startRelationMoreEvaluationActivityForResult(
			Context context, String homeUserId) {
		Intent intent = new Intent(context,
				RelationMoreEvaluationActivity.class);
		intent.putExtra(EConsts.Key.ID, homeUserId);
		((Activity) context).startActivityForResult(intent,
				ENavConsts.ActivityReqCode.REQUEST_CODE_FOR_MORE_EVALUATION);
	}

	public static void startRelationMoreEvaluationActivityForResult(
			Context context, String homeUserId, int type) {
		Intent intent = new Intent(context,
				RelationMoreEvaluationActivity.class);
		intent.putExtra(EConsts.Key.ID, homeUserId);
		intent.putExtra(EConsts.Key.TYPE, type);
		((Activity) context).startActivityForResult(intent,
				ENavConsts.ActivityReqCode.REQUEST_CODE_FOR_MORE_EVALUATION);
	}

	/**
	 * 启动更多评价页面(客户)
	 * 
	 * @param context
	 */
	public static void startClientMoreEvaluationActivityForResult(
			Context context, long client_id) {
		Intent intent = new Intent(context,
				OrgClientMoreEvaluationActivity.class);
		intent.putExtra(EConsts.Key.ID, client_id);
		((Activity) context).startActivityForResult(intent,
				ENavConsts.ActivityReqCode.REQUEST_CODE_FOR_MORE_EVALUATION);
	}

	/**
	 * 启动更多评价页面(组织)
	 * 
	 * @param context
	 */
	public static void startOrgRelationMoreEvaluationActivityForResult(
			Context context, String homeUserId) {
		Intent intent = new Intent(context,
				OrgRelationMoreEvaluationActivity.class);
		intent.putExtra(EConsts.Key.ID, homeUserId);
		((Activity) context).startActivityForResult(intent,
				ENavConsts.ActivityReqCode.REQUEST_CODE_FOR_MORE_EVALUATION);
	}

	/**
	 * 启动更多评价页面(人脉详情)
	 * 
	 * @param context
	 */
	public static void startContactsRelationMoreEvaluationActivityForResult(
			Context context, long homeUserId) {
		Intent intent = new Intent(context,
				ContactsRelationMoreEvaluationActivity.class);
		intent.putExtra(EConsts.Key.ID, homeUserId);
		((Activity) context).startActivityForResult(intent,
				ENavConsts.ActivityReqCode.REQUEST_CODE_FOR_MORE_EVALUATION);
	}

	/**
	 * 启动编辑职业标签页面(人脉我的主页)
	 * 
	 * @param context
	 */
	public static void EditRelationEvaluationTagActivity(Context context) {
		Intent intent = new Intent(
				context,
				com.tr.ui.connections.revision20150122.detail.EditRelationEvaluationTagActivity.class);
		((Activity) context).startActivityForResult(intent,
				ENavConsts.ActivityReqCode.REQUEST_CODE_FOR_EDIT_EVALUATION);
	}
	

	public static void EditRelationEvaluationTagActivity(Context context, String homeUserId, int type) {
		Intent intent = new Intent(
				context,
				com.tr.ui.connections.revision20150122.detail.EditRelationEvaluationTagActivity.class);

		intent.putExtra(EConsts.Key.ID, homeUserId);
		intent.putExtra(EConsts.Key.TYPE, type);
		((Activity) context).startActivityForResult(intent,
				ENavConsts.ActivityReqCode.REQUEST_CODE_FOR_EDIT_EVALUATION);
	}

	/**
	 * 启动编辑职业标签页面(人脉详情)
	 * 
	 * @param context
	 */
	public static void ContactsEditRelationEvaluationTagActivity(
			Context context, long id) {
		Intent intent = new Intent(context,
				ContactsEditRelationEvaluationTagActivity.class);
		intent.putExtra("id", id);
		((Activity) context).startActivityForResult(intent,
				ENavConsts.ActivityReqCode.REQUEST_CODE_FOR_EDIT_EVALUATION);

	}

	/**
	 * 启动编辑职业标签页面(组织详情)
	 * 
	 * @param context
	 */
	public static void EditOrgRelationEvaluationTagActivity(Context context) {
		Intent intent = new Intent(context,
				OrgEditRelationEvaluationTagActivity.class);

		((Activity) context).startActivityForResult(intent,
				ENavConsts.ActivityReqCode.REQUEST_CODE_FOR_EDIT_EVALUATION);

	}

	/**
	 * 启动编辑职业标签页面(客户)
	 * 
	 * @param context
	 */
	public static void EditClientRelationEvaluationTagActivity(Context context,
			long id) {
		Intent intent = new Intent(context,
				ClientEditRelationEvaluationTagActivity.class);
		intent.putExtra(EConsts.Key.CLIENTID, id);
		((Activity) context).startActivityForResult(intent,
				ENavConsts.ActivityReqCode.REQUEST_CODE_FOR_EDIT_EVALUATION);

	}

	/**
	 * 启动详情的推荐关系列表
	 * 
	 * @param context
	 * @param id
	 *            用户id
	 * @param isonline
	 *            线上还是线下
	 */
	public static void startMatchConnectionsactivity(Context context,
			String id, int type) {
		Intent intent = new Intent(context, DetailsRelationListActivity.class);
		intent.putExtra(EConsts.Key.ID, id);
		intent.putExtra(EConsts.Key.TYPE, type);
		context.startActivity(intent);
	}

	/**
	 * 启动图片详情页面
	 * 
	 * @param tag
	 *            0-网络图片，1-本地图片
	 * @param context
	 * @param url
	 */
	public static void startImageDetailActivity(Context context, String url,
			int tag) {
		Intent intent = new Intent(context, ImageDetailActivity.class);
		if (tag == 0) {
			intent.putExtra(EConsts.Key.IMAGE_URL, url);
		} else {
			intent.putExtra(EConsts.Key.IMAGE_PATH, url);
		}
		context.startActivity(intent);
	}

	/**
	 * 启动图片浏览页面
	 * 
	 * @param context
	 * @param listMsg
	 * @param msg
	 */
	public static void startImageBrowserActivity(Activity activity,
			String chatId, String msgId) {
		Intent intent = new Intent(activity, ChatImageBrowserActivity.class);
		intent.putExtra(ENavConsts.EMessageID, msgId);
		intent.putExtra(ENavConsts.EMucID, chatId);
		intent.putExtra(ENavConsts.EFromActivityName, activity.getClass()
				.getSimpleName());
		activity.startActivity(intent);
	}

	public static void startImageBrowserActivity(Activity activity,
			String chatId, String msgId, ArrayList<IMBaseMessage> listMessage) {
		Intent intent = new Intent(activity, ChatImageBrowserActivity.class);
		intent.putExtra(ENavConsts.EMessageID, msgId);
		intent.putExtra(ENavConsts.EMucID, chatId);
		intent.putExtra("listMessage", listMessage);
		activity.startActivity(intent);
	}

	/**
	 * 进入群列表界面，展示群列表、选择群
	 * 
	 * @param fromActivity
	 * @param listJtFile
	 */
	public static void startGroupListActivity(Context context,
			String fromActivity, ArrayList<JTFile> listJtFile) {
		Intent intent = new Intent(context, GroupListActivity.class);
		if (!TextUtils.isEmpty(fromActivity)) {
			intent.putExtra(ENavConsts.EFromActivityName, fromActivity);
		}
		if (listJtFile != null) {
			intent.putExtra(ENavConsts.EShareParamList, listJtFile);
		}
		context.startActivity(intent);
	}

	/**
	 * 启动会议畅聊图片浏览页面
	 * 
	 * @param activity
	 * @param meetingId
	 * @param topicId
	 * @param messageId
	 */
	public static void startHyImageBrowserActivity(Activity activity,
			long meetingId, long topicId, String messageId) {
		Intent intent = new Intent(activity, MChatImageBrowserActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, activity.getClass()
				.getSimpleName());
		intent.putExtra(ENavConsts.EMessageID, messageId);
		intent.putExtra(ENavConsts.EMeetingId, meetingId);
		intent.putExtra(ENavConsts.ETopicId, topicId);
		activity.startActivity(intent);
	}

	/**
	 * 启动群聊分享 权限 页面
	 * 
	 * @param 分享页面
	 * @param shareInfo
	 */

	public static void startIMCreateGroupCategoryActivity1(
			Activity fromActivity, String mucID, IMBaseMessage im) {
		Intent intent = new Intent(fromActivity,
				IMCreateGroupCategoryActivity1.class);
		intent.putExtra(ENavConsts.EMucID, mucID);
		intent.putExtra(ENavConsts.EIMBaseMessage, im);
		intent.putExtra(ENavConsts.EFromActivityType,
				IMCreateGroupCategoryActivity1.type_group);
		fromActivity.startActivity(intent);
	}

	/**
	 * 进入群列表界面， 展示群列表 选择群
	 * 
	 * @param context
	 * @param url
	 */
	public static void startGroupListActivity(Context context,
			String fromActivity, JTFile jtFile) {
		Intent intent = new Intent(context, GroupListActivity.class);
		if (StringUtils.isEmpty(fromActivity)) {
			context.startActivity(intent);
		} else if (ENavConsts.EShareActivity.equals(fromActivity)) {
			intent.putExtra(ENavConsts.EFromActivityName, fromActivity);
			if (jtFile != null) {
				intent.putExtra(ENavConsts.EShareParam, jtFile);
			}
		}
		context.startActivity(intent);
	}

	/**
	 * 编辑用户、创建人脉
	 * 
	 * @param context
	 * @param fromActivityType
	 *            1-创建；2-编辑；3-编辑"我"
	 * @param jtContact
	 *            创建传null
	 * @param requestCode
	 *            创建传非0值
	 */

	public static void startNewConnectionsActivity(Context context,
			int fromActivitytype, PeopleDetails jt, Integer requestcode) {
		Intent intent = new Intent(context, NewConnectionsActivity.class);
		intent.putExtra(ENavConsts.EFromActivityType, fromActivitytype);
		if (jt != null) {
			intent.putExtra(ENavConsts.datas, jt);
		}

		if (requestcode != null) {
			((Activity) context).startActivityForResult(intent, requestcode);
		} else {
			context.startActivity(intent);
		}
	}

	/**
	 * 编辑个人资料页面
	 * 
	 * @param context
	 * @param fromActivityType
	 *            1-创建；2-编辑；3-编辑"我"
	 * @param jtContact
	 *            创建传null
	 * @param requestCode
	 *            创建传非0值
	 */

	public static void startEditBusinessCardActivity(Context context,
			PeopleDetails jt, int requestcode,boolean isMySelfBool) {
		Intent intent = new Intent(context, EditBusinessCardActivity.class);
		if (jt != null) {
			intent.putExtra(ENavConsts.datas, jt);
		}
		intent.putExtra(ENavConsts.IS_SELF_BOOL, isMySelfBool);
		((Activity) context).startActivityForResult(intent, requestcode);
	}

	/**
	 * 举报
	 * 
	 * @param context
	 * @param requestCode
	 *            创建传非0值
	 */

	public static void startReportActivity(Context context, Person jt) {
		Intent intent = new Intent(context, ReportActivity.class);
		intent.putExtra(ENavConsts.Report, jt);
		context.startActivity(intent);
	}

	/**
	 * 举报
	 * 
	 * @param context
	 * @param requestCode
	 *            创建传非0值
	 */

	public static void startOrgReportActivity(Context context, Long org_id) {
		Intent intent = new Intent(context, OrganizationReportActivity.class);
		intent.putExtra(ENavConsts.Org_Report, org_id);
		context.startActivity(intent);
	}

	/**
	 * 编辑用户、创建人脉
	 * 
	 * @param context
	 * @param fromActivityType
	 *            1-创建；2-编辑；3-编辑"我"
	 * @param jtContact
	 *            创建传null
	 * @param requestCode
	 *            创建传非0值
	 */

	public static void startNewConnectionsActivity(Context context,
			int fromActivitytype, PeopleDetails jt, int requestcode,
			String removeId) {
		Intent intent = new Intent(context, NewConnectionsActivity.class);
		intent.putExtra(ENavConsts.EFromActivityType, fromActivitytype);
		intent.putExtra(EConsts.Key.RELATE_REMOVE_ID, removeId);
		if (jt != null) {
			intent.putExtra(ENavConsts.datas, jt);
		}
		if (requestcode != 0) {
			((Activity) context).startActivityForResult(intent, requestcode);
		} else {
			context.startActivity(intent);
		}
	}

	/**
	 * 人脉详情
	 * 
	 * @param context
	 * @param fromActivityType
	 *            "type":"1-创建  2-其他"
	 * @param id
	 *            人脉对象id
	 * 
	 *            control 是否需要进行权限控制 0 受权限控制 1 不受权限控制
	 * @param requestCode
	 * 
	 */

	public static void startContactsDetailsActivity(Context context,
			int fromActivitytype, long id, int requestcode) {
		Intent intent = new Intent(context, ContactsDetailsActivity.class);
		intent.putExtra(ENavConsts.EFromActivityType, fromActivitytype);
		intent.putExtra(EConsts.Key.PERSON_ID, id);
		if (requestcode != 0) {
			((Activity) context).startActivityForResult(intent, requestcode);
		} else {
			context.startActivity(intent);
		}
	}

	/**
	 * 人脉详情
	 * 
	 * @param context
	 * @param fromActivityType
	 *            "type":"1-创建  2-其他"
	 * @param id
	 *            人脉对象id
	 * @param view
	 *            1 查看权限 是否需要进行权限控制 0 受权限控制 1 不受权限控制
	 * 
	 * @param requestCode
	 * 
	 */

	public static void startContactsDetailsActivity(Context context,
			int fromActivitytype, long id, int requestcode, int view) {
		Intent intent = new Intent(context, ContactsDetailsActivity.class);
		intent.putExtra(ENavConsts.EFromActivityType, fromActivitytype);
		intent.putExtra(EConsts.Key.PERSON_ID, id);
		intent.putExtra(EConsts.Key.VIEW, view);
		if (requestcode != 0) {
			((Activity) context).startActivityForResult(intent, requestcode);
		} else {
			context.startActivity(intent);
		}
	}

	// /**
	// *
	// * @param context
	// * @param id
	// */
	// public static void startContactsDetailsActivity(Context context, long id)
	// {
	// Intent intent = new Intent(context, ContactsDetailsActivity.class);
	// intent.putExtra(EConsts.Key.PERSON_ID, id);
	// context.startActivity(intent);
	// }
	// public static void startNewConnectionsActivity(Activity context,
	// int fromActivityType, JTContact2 jtContact, int requestCode) {
	// Intent intent = new Intent(context, NewConnectionsActivity.class);
	// intent.putExtra(ENavConsts.EFromActivityType, fromActivityType);
	// if (jtContact != null) {
	// intent.putExtra(ENavConsts.datas, jtContact);
	// }
	// if (requestCode != 0) {
	// context.startActivityForResult(intent, requestCode);
	// } else {
	// context.startActivity(intent);
	// }
	// }

	/**
	 * 启动私聊分享权限页面
	 * 
	 * @param fromActivity
	 * @param chatDetail
	 * @param im
	 */
	public static void startIMCreateGroupCategoryActivity1(
			Activity fromActivity, ChatDetail chatDetail, IMBaseMessage im) {
		Intent intent = new Intent(fromActivity,
				IMCreateGroupCategoryActivity1.class);
		intent.putExtra(ENavConsts.EChatDetail, chatDetail);
		intent.putExtra(ENavConsts.EIMBaseMessage, im);
		intent.putExtra(ENavConsts.EFromActivityType,
				IMCreateGroupCategoryActivity1.type_one);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动群聊分享权限页面
	 * 
	 * @param fromActivity
	 * @param mucID
	 * @param im
	 * @param requestCode
	 */
	public static void startIMCreateGroupCategoryActivity1Result(
			Activity fromActivity, String mucID, IMBaseMessage im,
			int requestCode) {
		Intent intent = new Intent(fromActivity,
				IMCreateGroupCategoryActivity1.class);
		intent.putExtra(ENavConsts.EMucID, mucID);
		intent.putExtra(ENavConsts.EIMBaseMessage, im);
		intent.putExtra(ENavConsts.EFromActivityType,
				IMCreateGroupCategoryActivity1.type_group);
		intent.putExtra(ENavConsts.EFromActivityResult, true);
		fromActivity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 启动会议分享权限选择页面
	 * 
	 * @param 分享页面
	 */
	public static void startIMCreateMeetingCategoryActivity1Result(
			Activity fromActivity, MMeetingQuery meetingDetail,
			IMBaseMessage im, int requestCode) {
		Intent intent = new Intent(fromActivity,
				IMCreateGroupCategoryActivity1.class);
		intent.putExtra(ENavConsts.EMeetingDetail, meetingDetail);
		intent.putExtra(ENavConsts.EIMBaseMessage, im);
		intent.putExtra(ENavConsts.EFromActivityType,
				IMCreateGroupCategoryActivity1.type_meeting);
		intent.putExtra(ENavConsts.EFromActivityResult, true);
		fromActivity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 启动私聊分享 权限 页面
	 * 
	 * @param fromActivity
	 * @param shareInfo
	 */
	public static void startIMCreateGroupCategoryActivity1Result(
			Activity fromActivity, ChatDetail chatDetail, IMBaseMessage im,
			int requestCode) {
		Intent intent = new Intent(fromActivity,
				IMCreateGroupCategoryActivity1.class);
		intent.putExtra(ENavConsts.EChatDetail, chatDetail);
		intent.putExtra(ENavConsts.EIMBaseMessage, im);
		intent.putExtra(ENavConsts.EFromActivityType,
				IMCreateGroupCategoryActivity1.type_one);
		intent.putExtra(ENavConsts.EFromActivityResult, true);
		fromActivity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 会议广场
	 * 
	 * @param context
	 */
	public static void startConferenceSquareActivity(Context context) {

		ComponentName componentName = new ComponentName("com.tr.conference",
				"com.tr.conference.activity.MainActivity");
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		// app之间跳转所需的用户数据参数
		bundle.putString(EConsts.Key.SESSION_ID, App.getSessionID());
		bundle.putString(EConsts.Key.USER_ID, App.getUserID());
		bundle.putString(EConsts.Key.NICK_NAME, App.getNick());
		bundle.putString(EConsts.Key.USER_NAME, App.getUserName());
		bundle.putString(EConsts.Key.USER_AVATAR, App.getUser().getImage());
		intent.putExtras(bundle);
		intent.setComponent(componentName);
		context.startActivity(intent);
	}

	/**
	 * 发起会议
	 * 
	 * @param context
	 */
	public static void startCreateConferenceActivity(Context context) {

		ComponentName componentName = new ComponentName("com.tr.conference",
				"com.tr.conference.activity.InitateConferenceActivity");
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		// app之间跳转所需的用户数据参数
		bundle.putString(EConsts.Key.SESSION_ID, App.getSessionID());
		bundle.putString(EConsts.Key.USER_ID, App.getUserID());
		bundle.putString(EConsts.Key.NICK_NAME, App.getNick());
		bundle.putString(EConsts.Key.USER_NAME, App.getUserName());
		bundle.putString(EConsts.Key.USER_AVATAR, App.getUser().getImage());
		intent.putExtras(bundle);
		intent.setComponent(componentName);
		context.startActivity(intent);
	}

	/** 会议模式相关-begin */

	/**
	 * 进入我的会议
	 * 
	 * @param context
	 */
	// public static void startMyConference(Activity fromActivity) {
	// Intent intent = new Intent(fromActivity, MyHYActivity.class);
	// fromActivity.startActivity(intent);
	// }

	/**
	 * 进入我的会议
	 * 
	 * @serialData 20150705
	 * @author zhongshan
	 */
	public static void startMyMeetingActivity(Activity fromActivity) {
		Intent intent = new Intent(fromActivity, MyMeetingActivity.class);
		fromActivity.startActivity(intent);
	}

	/**
	 * 跳转到会议畅聊页面
	 * 
	 * @param activity
	 * @param query
	 */
	public static void startConferenceIMActivity(Activity activity,
			MMeetingQuery query) {
		Intent intent = new Intent(activity, GroupChatActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, activity.getClass()
				.getSimpleName());
		intent.putExtra(ENavConsts.EMeetingDetail, query);
		activity.startActivity(intent);
	}

	/**
	 * 启动会议分享页面
	 * 
	 * @param activity
	 * @param type
	 */
	public static void startHyShareActivity(Activity activity, int type,
			int requestCode) {
		Intent intent = new Intent(activity,
				com.tr.ui.conference.initiatorhy.ShareActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, activity.getClass()
				.getSimpleName());
		intent.putExtra(ENavConsts.EShareParam, type);
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 启动会议畅聊文件预览页面
	 * 
	 * @param activity
	 * @param jtFile
	 * @param messageId
	 * @param meetingId
	 * @param topicId
	 */
	public static void startMChatFilePreviewActivity(Context activity,
			JTFile jtFile, String messageId, long meetingId, long topicId) {

		Intent intent = new Intent(activity, MChatFilePreviewActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, activity.getClass()
				.getSimpleName());
		intent.putExtra(ENavConsts.EMessageID, messageId);
		intent.putExtra(ENavConsts.EJTFile, jtFile);
		intent.putExtra(ENavConsts.EMeetingId, meetingId);
		intent.putExtra(ENavConsts.ETopicId, topicId);
		activity.startActivity(intent);
	}

	/**
	 * 启动会议详情界面
	 * 
	 * @param mContext
	 * @param meetingId
	 * @param type
	 *            0:会议详情 ； 1:邀请函详情
	 */
	public static void startSquareActivity(Context mContext, long meetingId,
			int type) {
		Intent intent = new Intent(mContext, SquareActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, mContext.getClass()
				.getSimpleName());
		intent.putExtra("meeting_id", meetingId);
		intent.putExtra("type", type);
		mContext.startActivity(intent);
	}

	/**
	 * 跳转到转发列表
	 * 
	 * @param mContext
	 * @param jtFile
	 */
	public static void startTransmitMeetingList(Context mContext,
			ArrayList<JTFile> jtFiles, long meetingId) {
		Intent intent = new Intent(mContext, TansmitMeetingListActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, mContext.getClass()
				.getSimpleName());
		intent.putExtra(ENavConsts.EJTFile, jtFiles);
		intent.putExtra(ENavConsts.EMeetingId, meetingId);
		mContext.startActivity(intent);
	}

	/**
	 * 跳转到会议畅聊页面
	 * 
	 * @param context
	 * @param meetingDetail
	 * @param topicDetail
	 * @param jtFile
	 */
	public static void startMeetingChatActivity(Context context,
			MMeetingQuery meetingDetail, MMeetingTopicQuery topicDetail,
			JTFile jtFile) {
		Intent intent = new Intent(context, MeetingChatActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, context.getClass()
				.getSimpleName());
		intent.putExtra(ENavConsts.EMeetingDetail, meetingDetail);
		intent.putExtra(ENavConsts.EMeetingTopicDetail, topicDetail);
		intent.putExtra(ENavConsts.EJTFile, jtFile);
		context.startActivity(intent);
	}

	/**
	 * 跳转到会议畅聊页面
	 * 
	 * @param context
	 * @param meetingDetail
	 * @param topicDetail
	 * @param listJTFile
	 */
	public static void startMeetingChatActivity(Context context,
			MMeetingQuery meetingDetail, MMeetingTopicQuery topicDetail,
			ArrayList<JTFile> listJtFile) {
		Intent intent = new Intent(context, MeetingChatActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, context.getClass()
				.getSimpleName());
		intent.putExtra(ENavConsts.EMeetingDetail, meetingDetail);
		intent.putExtra(ENavConsts.EMeetingTopicDetail, topicDetail);
		intent.putExtra(ENavConsts.EListJTFile, listJtFile);
		context.startActivity(intent);
	}

	/**
	 * 跳转到会议畅聊页面
	 * 
	 * @param context
	 * @param meetingId
	 */
	public static void startMeetingChatActivity(Context context,
			String meetingId, String topicId) {
		Intent intent = new Intent(context, MeetingChatActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, context.getClass()
				.getSimpleName());
		intent.putExtra(ENavConsts.EMeetingId, meetingId);
		intent.putExtra(ENavConsts.ETopicId, topicId);
		context.startActivity(intent);
	}

	/**
	 * 启动会议主界面
	 * 
	 * @param fromActivity
	 * @param meetingId
	 * @param topicId
	 */
	public static void startMeetingHomeActivity(Activity fromActivity,
			String meetingId, String topicId) {
		Intent intent = new Intent(fromActivity, MainActivity.class);
		intent.putExtra(ENavConsts.EMeetingId, meetingId);
		intent.putExtra(ENavConsts.ETopicId, topicId);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		fromActivity.startActivity(intent);
	}

	/******* 新知识 start *******/

	/**
	 * 创建知识
	 * 
	 * @param context
	 */
	/*
	 * public static void startCreateKnowledgeActivity(Context context) {
	 * ((Activity) context).startActivity(new Intent(context,
	 * CreateKnowledgeActivity.class)); }
	 */

	/**
	 * <<<<<<< HEAD 创建客户 formActicityType : 1 创建 2 编辑 3 保存 4 转为客户
	 * 
	 * @param context
	 */
	public static void startCreateClienteleActivity(Context context,
			Object customer, int formActicityType, Long id) {

		Intent intent = new Intent(context, CreateClienteleActivity.class);
		CustomerClientParams customerClientParams = null;
		if (formActicityType != 4) {
			customerClientParams = (CustomerClientParams) customer;
			if (customerClientParams != null) {
				Bundle bundle = new Bundle();
				bundle.putSerializable(ENavConsts.EClient_Data,
						customerClientParams);
				intent.putExtras(bundle);
			}
		} else {
			CustomerOrganizatianParams organizatianParams = (CustomerOrganizatianParams) customer;
			if (organizatianParams != null) {
				Bundle bundle = new Bundle();
				bundle.putSerializable(ENavConsts.EClient_Data,
						organizatianParams);
				intent.putExtras(bundle);
			}
		}

		if (id != 0) {
			intent.putExtra(ENavConsts.EClient_Id, id);
		}
		intent.putExtra(ENavConsts.EFromActivityName, formActicityType);
		((Activity) context).startActivityForResult(intent, Activity.RESULT_OK);
	}

	/**
	 * 创建客户 formActicityType : 1 创建 2 其他
	 * 
	 * @param context
	 */
	public static void startCreateClienteleActivity(Context context,
			CustomerOrganizatianParams customer, int formActicityType, Long id) {

		Intent intent = new Intent(context, CreateClienteleActivity.class);
		if (customer != null) {
			Bundle bundle = new Bundle();
			bundle.putSerializable(ENavConsts.EClient_Data, customer);
			intent.putExtras(bundle);
		}
		if (id != 0) {
			intent.putExtra(ENavConsts.EClient_Id, id);
		}
		intent.putExtra(ENavConsts.EFromActivityName, formActicityType);
		((Activity) context).startActivityForResult(intent, Activity.RESULT_OK);
	}

	/**
	 * 知识广场
	 * 
	 * @param context
	 */
	public static void startKnowledgeSquareActivity(Context context) {
		((Activity) context).startActivity(new Intent(context,
				KnowledgeSquareActivity.class));
	}

	/******* 新知识 end *******/

	/**
	 * 知识内容（文字和附件）
	 * 
	 * @param activity
	 * @param taskId
	 */
	public static void startKnowledgeContentActivityForResult(
			Activity activity, int requestCode, String taskId, String content,
			ArrayList<JTFile> listJtFile, String pic) {
		Intent intent = new Intent(activity, KnowledgeContentActivity.class);
		intent.putExtra(EConsts.Key.TASK_ID, taskId);
		intent.putExtra("pic", pic);
		if (!TextUtils.isEmpty(content)) {
			intent.putExtra(EConsts.Key.KNOWLEDGE_CONTENT, content);
		}
		if (listJtFile != null && listJtFile.size() > 0) {
			intent.putExtra(EConsts.Key.KNOWLEDGE_LIST_JTFILE, listJtFile);
		}
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 启动关联资源界面
	 * 
	 * @param activity
	 * @param requestCode
	 * @param keyword
	 * @param type
	 * @param tag
	 * @param listResource
	 * @param sourceMap
	 */
	/*
	 * public static void startRelatedResourceActivityForResult(Activity
	 * activity, int requestCode, String keyword, int type, String tag,
	 * ArrayList listResource, HashMap<String, Integer> sourceMap){ Intent
	 * intent = new Intent(activity, RelatedResourceActivity.class);
	 * intent.putExtra(EConsts.Key.KNOWLEDGE_KEYWORD, keyword);
	 * intent.putExtra(EConsts.Key.RELATED_RESOURCE_TYPE, type);
	 * if(!TextUtils.isEmpty(tag)){
	 * intent.putExtra(EConsts.Key.RELATED_RESOURCE_TAG, tag); } if(listResource
	 * != null && listResource.size() > 0){
	 * intent.putExtra(EConsts.Key.RELATED_RESOURCE_LIST, listResource); }
	 * if(sourceMap != null && sourceMap.size() > 0){
	 * intent.putExtra(EConsts.Key.RELATED_RESOURCE_SOURCE_MAP, sourceMap); }
	 * activity.startActivityForResult(intent, requestCode); }
	 */

	/**
	 * 启动关联资源界面
	 * 
	 * @param activity
	 * @param requestCode
	 * @param keyword
	 * @param type
	 * @param tag
	 * @param listResource
	 * @param sourceMap
	 */
	public static void startRelatedResourceActivityForResult(Activity activity,
			int requestCode, String keyword, ResourceType resType,
			ResourceNode resNode) {
		Intent intent = new Intent(activity, RelatedResourceActivity.class);
		intent.putExtra(EConsts.Key.KNOWLEDGE_KEYWORD, keyword); // 关键字
		intent.putExtra(EConsts.Key.RELATED_RESOURCE_TYPE, resType); // 资源类型
		if (resNode != null) {
			intent.putExtra(EConsts.Key.RELATED_RESOURCE_NODE, resNode); // 资源元数据
		}
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 启动关联资源界面
	 * 
	 * @param activity
	 * @param requestCode
	 * @param keyword
	 * @param type
	 * @param tag
	 * @param listResource
	 * @param sourceMap
	 */
	public static void startRelatedResourceActivityForResult(Activity activity,
			int requestCode, String keyword, ResourceType resType,
			ResourceNode resNode, String removeIDString) {
		Intent intent = new Intent(activity, RelatedResourceActivity.class);
		intent.putExtra(EConsts.Key.KNOWLEDGE_KEYWORD, keyword); // 关键字
		intent.putExtra(EConsts.Key.RELATED_RESOURCE_TYPE, resType); // 资源类型
		intent.putExtra(EConsts.Key.RELATE_REMOVE_ID, removeIDString); // 资源类型
		if (resNode != null) {
			intent.putExtra(EConsts.Key.RELATED_RESOURCE_NODE, resNode); // 资源元数据
		}
		activity.startActivityForResult(intent, requestCode);
	}
	/**
	 * 社群启动关联资源界面
	 * 
	 * @param activity
	 * @param requestCode
	 * @param keyword 关键字
	 * @param type
	 * @param tag
	 * @param listResource
	 * @param communityId 社群id
	 * @param createrId 创建者id
	 * @param sourceMap
	 */
	public static void startRelatedResourceActivityForResult(Activity activity,
			int requestCode, String keyword, ResourceType resType,
			Map connectionNodeMap,long communityId,long createrId) {
		Intent intent = new Intent(activity, RelatedResourceActivity.class);
		intent.putExtra(EConsts.Key.KNOWLEDGE_KEYWORD, keyword); // 关键字
		intent.putExtra(EConsts.Key.RELATED_RESOURCE_TYPE, resType); // 资源类型
		intent.putExtra(EConsts.Key.RELATED_RESOURCE_ALL_NODE, (Serializable)connectionNodeMap); // 资源所有数据
		intent.putExtra(GlobalVariable.COMMUNITY_ID, communityId);
		intent.putExtra("createrId", createrId);
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 知识启动目录界面
	 * 
	 * @param activity 当前调用者XXXActivity.this
	 * @param requestCode 请求码
	 * @param listCategory 创建/编辑时选择目录关联时传入，其他为null；
	 * @param isSelect 创建/编辑时选择目录关联时传true
	 * @param activityName 目录列表上的小标题
	 */
	public static void startKnowledgeCategoryActivityForResult(
			Activity activity, int requestCode,
			ArrayList<UserCategory> listCategory, boolean isSelect,
			String activityName) {
		startKnowledgeCategoryActivityForResult(activity, requestCode, listCategory, null, isSelect, activityName);
	}

	/**
	 * 组织/需求启动目录接口 添加 ModuleType 对象判断类型
	 * 
	 * @param activity 当前调用者XXXActivity.this
	 * @param requestCode 请求码
	 * @param listCategory 创建/编辑时选择目录关联时传入，其他为null；
	 * @param categoryType ModuleType对象，类型
	 * @param isSelect 创建/编辑时选择目录关联时传true
	 * @param activityName 目录列表上的小标题
	 */
	public static void startKnowledgeCategoryActivityForResult(
			Activity activity, int requestCode,
			ArrayList<UserCategory> listCategory, ModuleType categoryType,
			boolean isSelect, String activityName) {
		startKnowledgeCategoryActivityForResult(activity, null, requestCode, listCategory, categoryType, isSelect, activityName);
	}

	/**
	 * 人脉启动目录接口 添加 enmu 对象判断类型
	 * @param activity 当前调用者XXXActivity.this
	 * @param fragment 不为null是从fragment页面进行跳转的
	 * @param requestCode 请求码
	 * @param listCategory 创建/编辑时选择目录关联时传入，其他为null；
	 * @param categoryType ModuleType对象，类型
	 * @param isSelect 创建/编辑时选择目录关联时传true
	 * @param activityName 目录列表上的小标题
	 */
	public static void startKnowledgeCategoryActivityForResult(
			Activity activity, Fragment fragment, Integer requestCode,
			ArrayList<UserCategory> listCategory, ModuleType categoryType,
			boolean isSelect, String activityName) {
		Intent intent = new Intent(activity, CategoryActivity.class);
		intent.putExtra(EConsts.Key.FROM_ACTIVITY, activity.getClass()
				.getSimpleName());
		intent.putExtra(EConsts.Key.ACTIVITY_NAME, activityName);
		intent.putExtra(ENavConsts.Category_ENUM_TYPE, categoryType);
		intent.putExtra(ENavConsts.Category_SELECT_ACTION, isSelect);
		if (listCategory != null && listCategory.size() > 0) {
			intent.putExtra(EConsts.Key.KNOWLEDGE_CATEGORY_LIST, listCategory);
		}
		if (requestCode == null) {
			activity.startActivity(intent);
			return;
		}
		if (fragment != null) {
			fragment.startActivityForResult(intent, requestCode);
		} else {
			activity.startActivityForResult(intent, requestCode);
		}

	}

	/**
	 * 启动编辑标签页面
	 * 
	 * @param fromActivity
	 * @param requestCode
	 * @param listKnowledgeId
	 * @param listKnowledgeType
	 */
	public static void startEditKnowledgeTagActivityForResult(
			Activity fromActivity, int requestCode,
			ArrayList<Long> listKnowledgeId,
			ArrayList<Integer> listKnowledgeType) {
		Intent intent = new Intent(fromActivity, EditKnowledgeTagActivity.class);

		// 页面跳转时需要传递的数据
		if (listKnowledgeId != null && listKnowledgeId.size() > 0) {
			intent.putExtra(EConsts.Key.KNOWLEDGE_ID_LIST, listKnowledgeId);
		}
		if (listKnowledgeType != null && listKnowledgeType.size() > 0) {
			intent.putExtra(EConsts.Key.KNOWLEDGE_TYPE_LIST, listKnowledgeType);
		}
		fromActivity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 启动编辑标签页面
	 * 
	 * @param fromActivity
	 * @param requestCode
	 */
	public static void startEditKnowledgeTagActivityForResult(
			Activity fromActivity, int requestCode,
			ArrayList<KnowledgeMini2> miniKnowledgeLists) {
		Intent intent = new Intent(fromActivity, EditKnowledgeTagActivity.class);
		intent.putExtra("miniknowledgelists", miniKnowledgeLists);
		fromActivity.startActivityForResult(intent, requestCode);
	}


	/**
	 * 启动标签页面
	 * 
	 * @param fromActivity
	 * @param requestCode
	 * 
	 */
	public static void startGlobalKnowledgeTagActivity(Activity fromActivity) {
		Intent intent = new Intent(fromActivity,
				GlobalKnowledgeTagActivity.class);
		fromActivity.startActivity(intent);
	}

	/***
	 * 启动标签页面
	 * 
	 * @param fromActivity
	 * @param requestCode
	 * @param operateType
	 *            ：操作类型：创建页面跳转来为MultipleChoice；我页面查看某一标签页面下内容为ClickBack
	 * 
	 */
	public static void startGlobalKnowledgeTagActivityForResult(
			Activity fromActivity, int requestCode,
			GlobalKnowledgeTagActivity.OperateType operateType) {
		Intent intent = new Intent(fromActivity,
				GlobalKnowledgeTagActivity.class);
		intent.putExtra(EConsts.Key.OPERATE_TYPE, operateType);
		intent.putExtra(EConsts.Key.FROM_ACTIVITY, fromActivity.getClass()
				.getSimpleName());
		fromActivity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 启动资源栏目页面
	 * 
	 * @param activity
	 */
	public static void startJointColumnActivity(Activity fromActivity,
			ResourceType resType, ResourceNode resNode,
			ResourceType tarResType, String tarResId, int requestCode) {
		Intent intent = new Intent(fromActivity, JointColumnActivity.class);
		intent.putExtra(EConsts.Key.FROM_ACTIVITY, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra(EConsts.Key.JOINT_RESOURCE_TYPE, resType);
		intent.putExtra(EConsts.Key.JOINT_RESOURCE_NODE, resNode);
		intent.putExtra(EConsts.Key.TARGET_RESOURCE_TYPE, tarResType);
		intent.putExtra(EConsts.Key.TARGET_RESOURCE_ID, tarResId);
		fromActivity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 创建知识
	 * 
	 * @param context
	 */
	public static void startCreateKnowledgeActivity(Context context) {
		((Activity) context).startActivity(new Intent(context,
				CreateKnowledgeActivity.class));
	}

	/**
	 * 启动创建知识页面
	 * 
	 * @param fromActivity
	 * @param requestCode
	 */
	public static void startCreateKnowledgeActivity(Activity fromActivity,
			boolean isCreate, int requestCode) {
		Intent intent = new Intent(fromActivity, CreateKnowledgeActivity.class);
		intent.putExtra(EConsts.Key.FROM_ACTIVITY, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra(EConsts.Key.REQUEST_CODE, requestCode);
		intent.putExtra(EConsts.Key.KNOWLEDGE_IS_CREATE, isCreate);
		fromActivity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 启动创建知识页面
	 * 
	 * @param fromActivity
	 * @param requestCode
	 */
	public static void startCreateKnowledgeActivity(Activity fromActivity,
			boolean isCreate, int requestCode, Knowledge2 knowledge2,
			CreateKnowledgeActivity.OperateType operateType) {
		Intent intent = new Intent(fromActivity, CreateKnowledgeActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putString(EConsts.Key.FROM_ACTIVITY, fromActivity.getClass()
				.getSimpleName());
		mBundle.putInt(EConsts.Key.REQUEST_CODE, requestCode);
		mBundle.putBoolean(EConsts.Key.KNOWLEDGE_IS_CREATE, isCreate);
		mBundle.putSerializable(EConsts.Key.KNOWLEDGE2, knowledge2);
		mBundle.putSerializable(EConsts.Key.OPERATE_TYPE, operateType);
		intent.putExtras(mBundle);
		fromActivity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 启动创建知识页面
	 * 
	 * @param fromActivity
	 * @param externalUrl
	 * @param backtype
	 *            false 表示点击创建知识页面actionBar返回键到知识详情页面 true 关闭页面
	 */
	public static void startCreateKnowledgeActivity(Activity fromActivity,
			boolean isCreate, String externalUrl, boolean backtype) {
		Intent intent = new Intent(fromActivity, CreateKnowledgeActivity.class);
		intent.putExtra(EConsts.Key.KNOWLEDGE_IS_CREATE, isCreate);
		intent.putExtra(EConsts.Key.FROM_ACTIVITY, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra(EConsts.Key.EXTERNAL_URL, externalUrl);
		intent.putExtra("backtype", backtype);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动创建知识页面
	 * 
	 * @param fromActivity
	 * @param knowledgeMini2
	 */
	public static void startCreateKnowledgeActivity(Activity fromActivity,
			boolean isCreate, KnowledgeMini2 knowledgeMini2) {
		Intent intent = new Intent(fromActivity, CreateKnowledgeActivity.class);
		intent.putExtra(EConsts.Key.FROM_ACTIVITY, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra(EConsts.Key.KNOWLEDGE_IS_CREATE, isCreate);
		intent.putExtra(EConsts.Key.KNOWLEDGE_MINI2, knowledgeMini2);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动创建知识页面
	 * 
	 * @param fromActivity
	 * @param knowledge2
	 */
	public static void startCreateKnowledgeActivity(Activity fromActivity,
			boolean isCreate, Knowledge2 knowledge2) {
		Intent intent = new Intent(fromActivity, CreateKnowledgeActivity.class);
		intent.putExtra(EConsts.Key.FROM_ACTIVITY, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra(EConsts.Key.KNOWLEDGE_IS_CREATE, isCreate);
		intent.putExtra(EConsts.Key.KNOWLEDGE2, knowledge2);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动资源对接页面
	 * 
	 * @param fromActivity
	 * @param tarResType
	 * @param res
	 */
	public static void startJointResourceActivity(Activity fromActivity,
			ResourceType tarResType, ResourceBase res) {
		Intent intent = new Intent(fromActivity, JointResourceActivity.class);
		intent.putExtra(EConsts.Key.FROM_ACTIVITY, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra(EConsts.Key.TARGET_RESOURCE_TYPE, tarResType);
		intent.putExtra(EConsts.Key.TARGET_RESOURCE, res);
		fromActivity.startActivity(intent);
	}
	
	/**
	 * 启动资源对接页面
	 * 
	 * @param fromActivity
	 * @param tarResType
	 * @param res
	 * @param type 要显示的对接内容1  知识 2  需求 3  组织  4  人脉
	 */
	public static void startJointResourceActivity(Activity fromActivity, String userid,
			ResourceType_new tarResType, int currentPage) {
		Intent intent = new Intent(fromActivity, JointResourceActivity.class);
		intent.putExtra(EConsts.Key.FROM_ACTIVITY, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra(EConsts.Key.TARGET_RESOURCE_TYPE, tarResType);
		intent.putExtra(EConsts.Key.ID, userid);
		intent.putExtra("currentPage", currentPage);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动知识详情页面
	 * 
	 * @param fromActivity
	 * @param knowledgeId
	 * @param knowledgeType
	 */
	public static void startKnowledgeOfDetailActivity(Activity fromActivity,
			long knowledgeId, int knowledgeType) {
		Intent intent = new Intent(fromActivity,
				KnowledgeOfDetailActivity.class);
		intent.putExtra(EConsts.Key.KNOWLEDGE_DETAIL_ID, knowledgeId);
		intent.putExtra(EConsts.Key.KNOWLEDGE_DETAIL_TYPE, knowledgeType);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动知识详情页面
	 * 
	 * @param fromActivity
	 * @param knowledgeId
	 * @param knowledgeType
	 * @param isShowCollection
	 *            是否显示收藏按钮
	 */
	public static void startCollectionKnowledgeOfDetailActivity(
			Activity fromActivity, long knowledgeId, int knowledgeType,
			boolean isShowCollection) {
		Intent intent = new Intent(fromActivity,
				KnowledgeOfDetailActivity.class);
		intent.putExtra(EConsts.Key.KNOWLEDGE_DETAIL_ID, knowledgeId);
		intent.putExtra(EConsts.Key.KNOWLEDGE_DETAIL_TYPE, knowledgeType);
		intent.putExtra(ENavConsts.KEY_COLLECTION_KNOWLEDGE_TYPE,
				isShowCollection);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动知识详情页面
	 * 
	 * @param fromActivity
	 * @param knowledgeId
	 * @param knowledgeType
	 */
	public static void startKnowledgeOfDetailActivitys(Activity fromActivity,
			long knowledgeId, int knowledgeType, boolean isShowSave) {
		Intent intent = new Intent(fromActivity,
				KnowledgeOfDetailActivity.class);
		intent.putExtra(EConsts.Key.FROM_ACTIVITY, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra(EConsts.Key.KNOWLEDGE_DETAIL_ID, knowledgeId);
		intent.putExtra(EConsts.Key.KNOWLEDGE_DETAIL_TYPE, knowledgeType);
		intent.putExtra(ENavConsts.KEY_FRG_FLOW_FORWARDING_KNOWLEDGE_TYPE,
				isShowSave);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动知识详情页面
	 * 
	 * @param fromActivity
	 * @param knowledgeId
	 * @param knowledgeType
	 * @param isShowSave
	 *            false:跳转到知识详情页不显示保存按钮,true:显示保存按钮
	 */
	public static void startKnowledgeOfDetailActivity(Activity fromActivity,
			long knowledgeId, int knowledgeType, boolean isShowSave) {
		Intent intent = new Intent(fromActivity,
				KnowledgeOfDetailActivity.class);
		intent.putExtra(EConsts.Key.KNOWLEDGE_DETAIL_ID, knowledgeId);
		intent.putExtra(EConsts.Key.KNOWLEDGE_DETAIL_TYPE, knowledgeType);
		intent.putExtra(ENavConsts.KEY_FRG_FLOW_FORWARDING_KNOWLEDGE_TYPE,
				isShowSave);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动知识详情页面
	 * 
	 * @param fromActivity
	 * @param knowledgeId
	 * @param knowledgeType
	 */
	public static void startKnowledgeOfDetailActivity(Activity fromActivity,
			Knowledge2 knowledge2) {
		try {
			Intent intent = new Intent(fromActivity,
					KnowledgeOfDetailActivity.class);
			intent.putExtra(EConsts.Key.KNOWLEDGE2, knowledge2);
			if (ActivityCollector.activities.size() <= 1) {
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			} else {
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			}
			// 传入知识类型
			intent.putExtra(EConsts.Key.KNOWLEDGE_DETAIL_TYPE,
					knowledge2.getType());
			fromActivity.startActivity(intent);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 跳转到展示扫一扫得到新闻链接页面
	 * 
	 * @param fromActivity
	 * @param netUrl
	 *            扫一扫后得到的连接
	 */
	public static void startNeturlKnowledgeActivity(Activity fromActivity,
			String netUrl) {
		Intent intent = new Intent(fromActivity, NeturlKnowledgeActivity.class);
		intent.putExtra("netUrl", netUrl);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动知识详情页面
	 * 
	 * @param fromActivity
	 * @param knowledgeId
	 * @param knowledgeType
	 */
	public static void startKnowledgeOfDetailActivityForResult(
			Activity fromActivity, Knowledge2 knowledge2,
			String requestActivity, int requestCode) {
		Intent intent = new Intent(fromActivity,
				KnowledgeOfDetailActivity.class);
		intent.putExtra(EConsts.Key.KNOWLEDGE2, knowledge2);
		intent.putExtra("requestActivity", requestActivity);
		fromActivity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 会议广场入口
	 * 
	 * @param fromActivity
	 */
	public static void RelationHomepageActivity(Activity fromActivity) {
		/*
		 * Intent intent = new Intent(fromActivity,
		 * com.tr.ui.connections.revision20150122
		 * .detail.RelationHomepageActivity.class);
		 * fromActivity.startActivity(intent);
		 */
	}

	/**
	 * 跳转到 会议详情
	 * 
	 * @param fromActivity
	 */
	public static void startMeetingDetailsSquareActivity(Context fromActivity,
			long meetingId) {
		Intent intent = new Intent(fromActivity, SquareActivity.class);
		intent.putExtra("meeting_id", meetingId);
		fromActivity.startActivity(intent);
	}

	/**
	 * 跳转到 会议详情 加 返回跳转参数
	 * 
	 * @param fromActivity
	 * @param goBackJumpClass
	 *            返回时的跳转类
	 */
	public static void startMeetingDetailsSquareActivity(Context fromActivity,
			long meetingId, Class<?> goBackJumpClass) {
		Intent intent = new Intent(fromActivity, SquareActivity.class);
		intent.putExtra("meeting_id", meetingId);
		if (goBackJumpClass != null) {
			intent.putExtra("goBackJumpClass", goBackJumpClass);
		}
		fromActivity.startActivity(intent);
	}

	/**
	 * 会议广场入口
	 * 
	 * @param fromActivity
	 */
	public static void startImagesBrowseActivity(Activity fromActivity,
			String[] imgs, int currPosition) {
		Intent intent = new Intent(fromActivity, ImagesBrowseActivity.class);
		intent.putExtra("IMAGESBROWSE_IMGS", imgs);
		intent.putExtra("IMAGESBROWSE_CURRPOSITION", currPosition);
		fromActivity.startActivity(intent);
	}

	/**
	 * 跳转到会议聊天记录搜索界面
	 * 
	 * @param context
	 * @param meetingDetail
	 * @param topicDetail
	 */
	public static void startMeetingRecordSearchActivity(Context context,
			MMeetingQuery meetingDetail, MMeetingTopicQuery topicDetail) {
		Intent intent = new Intent(context, MChatRecordSearchActivity.class);
		intent.putExtra(ENavConsts.EMeetingDetail, meetingDetail);
		intent.putExtra(ENavConsts.EMeetingTopicDetail, topicDetail);
		context.startActivity(intent);
	}

	/**
	 * 跳转到我的二维码
	 * 
	 * @param context
	 * @param QRCodeStr
	 */
	public static void startQRCodeActivity(Context context, String QRCodeStr,
			String QRName, String userAvatar) {
		Intent intent = new Intent(context, MyQRCodeActivity.class);
		intent.putExtra(ENavConsts.QRCodeStr, QRCodeStr);
		intent.putExtra(ENavConsts.QRName, QRName);
		intent.putExtra(ENavConsts.userAvatar, userAvatar);
		context.startActivity(intent);
	}

	/**
	 * 跳转到我的二维码(社群二维码)
	 * 
	 * @param context
	 * @param QRCodeStr
	 * isCommunity  true :社群二维码， false :其他模块
	 */
	public static void startQRCodeActivity(Context context, String QRCodeStr,
			String QRName, String userAvatar,boolean isCommunity) {
		Intent intent = new Intent(context, MyQRCodeActivity.class);
		intent.putExtra(ENavConsts.QRCodeStr, QRCodeStr);
		intent.putExtra(ENavConsts.QRName, QRName);
		intent.putExtra(ENavConsts.userAvatar, userAvatar);
		intent.putExtra("isCommunity", isCommunity);
		context.startActivity(intent);
	}
	/**
	 * 跳转到我的二维码(社群二维码)
	 * 
	 * @param context
	 * @param QRCodeStr
	 * isCommunity  true :社群二维码， false :其他模块
	 */
	public static void startQRCodeActivity(Context context, String QRCodeStr,String communityId,
			String QRName, String userAvatar,boolean isCommunity) {
		Intent intent = new Intent(context, MyQRCodeActivity.class);
		intent.putExtra(ENavConsts.QRCodeStr, QRCodeStr);
		intent.putExtra(ENavConsts.QRName, QRName);
		intent.putExtra("communityId", communityId);
		intent.putExtra(ENavConsts.userAvatar, userAvatar);
		intent.putExtra("isCommunity", isCommunity);
		context.startActivity(intent);
	}

	/**
	 * 跳转验证码判断
	 * 
	 * @param context
	 * @param phoneCall
	 */
	public static void startInputVerifyCodeActivity(Context context,
			String phoneCall) {
		Intent intent = new Intent(context, InputVerifyCodeActivity.class);
		intent.putExtra(ENavConsts.phoneCall, phoneCall);
		context.startActivity(intent);
	}

	/**
	 * 跳转到密码修改成功
	 * 
	 * @param context
	 */
	public static void startResetPasswordSuccessActivity(Context context) {
		Intent intent = new Intent(context, ResetPasswordSuccessActivity.class);
		context.startActivity(intent);
	}

	/**
	 * 跳转到注册页面
	 * 
	 * @param context
	 */
	public static void startRegisterGintongAccountActivity(Context context) {
		Intent intent = new Intent(context,
				RegisterGintongAccountActivity.class);
		context.startActivity(intent);
	}

	/**
	 * 跳转到新版注册页面
	 * 
	 * @param context
	 */
	public static void startRegisterGintongAllPathActivity(Context context) {
		// Intent intent = new
		// Intent(context,RegisterGintongAllPathActivity.class);
		Intent intent = new Intent(context, RegisterActivity.class);
		context.startActivity(intent);
	}

	/**
	 * 跳转到金桐网用户协议
	 * 
	 * @param context
	 */
	public static void startAgreementActivity(Context context) {
		Intent intent = new Intent(context, AgreementActivity.class);
		context.startActivity(intent);
	}

	/**
	 * 跳转到 社交右上角的通讯录
	 * 
	 * @param fromActivity
	 * @param type
	 */
	public static void startMyFriendAll(Activity fromActivity, int type) {
		Intent intent = new Intent(fromActivity, MyFriendAllActivity.class);
		intent.putExtra(EConsts.Key.FROM_ACTIVITY, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra(EConsts.Key.TYPE, type);
		fromActivity.startActivity(intent);
	}

	/**
	 * 跳转到 社交右上角的通讯录
	 * 
	 * @param fromActivity
	 * @param type
	 * @param sum
	 *            好友/人脉的数量， 用于比对是否更新数据
	 */
	public static void startMyFriendAll(Activity fromActivity, int type, int sum) {
		Intent intent = new Intent(fromActivity, MyFriendAllActivity.class);
		intent.putExtra(EConsts.Key.FROM_ACTIVITY, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra(EConsts.Key.TYPE, type);
		intent.putExtra(EConsts.Key.SUM, sum);
		fromActivity.startActivity(intent);
	}

	/**
	 * 跳转到 设置页面
	 * 
	 * @param context
	 */
	public static void startSettingActivity(Context context) {
		Intent intent = new Intent(context, UserSettingActivity.class);
		context.startActivity(intent);
	}

	/**
	 * 跳转到 设置页面
	 * 
	 * @param context
	 */
	public static void startRegisterPersonalDetailActivity(Context context) {
		// Intent intent = new Intent(context,
		// RegisterPersonalDetailActivity.class);
		Intent intent = new Intent(context, RegisterPersonalActivity.class);
		context.startActivity(intent);
	}

	/**
	 * 跳转到 邀请好友界面
	 * 
	 * @param context
	 * @param type
	 *            0客户组织 1.好友人脉
	 */
	public static void startInviteFriendByQRCodeActivity(Context context,
			String friendId, int type) {
		Intent intent = new Intent(context, InviteFriendByQRCodeActivity.class);
		intent.putExtra("friendId", friendId);
		intent.putExtra("type", type);
		context.startActivity(intent);
	}

	// /**
	// * 跳转到 社交右上角的通讯录
	// * @param context
	// */
	// public static void startMyFriendAndPeopleListActivity(Context context){
	// Intent intent = new Intent(context, MyFriendAndPeopleListActivity.class);
	// context.startActivity(intent);
	// }

	/**
	 * 跳转到 手机人脉&推荐人脉
	 * 
	 * @param context
	 */
	public static void startWantPeopleActivity(Context context) {
		Intent intent = new Intent(context, WantPeopleActivity.class);
		context.startActivity(intent);
	}

	/**
	 * 跳转到 ,同步手机通讯录页面
	 * 
	 * @param context
	 */
	public static void startSIMContactActivity(Context context, String type) {
		Intent intent = new Intent(context, SIMContactActivity.class);
		intent.putExtra("type", type);
		context.startActivity(intent);
	}

	/**
	 * /** 选择行业
	 * 
	 * @param type
	 *            -0 行业 1 定制
	 * @param fromActivity
	 * @param requestCode
	 */
	public static void startChooseProfessionActivityForResult(
			Activity fromActivity, int requestCode, int type,
			MIndustrys mMIndustrys) {
		Intent intent = new Intent(fromActivity,
				ProfessionAndCustomzationActivity.class);
		intent.putExtra(ENavConsts.EProfessionAndCustomzation, type);
		intent.putExtra(ENavConsts.KEY_FRG_SETTING_MINDUSTRYS, mMIndustrys);
		fromActivity.startActivityForResult(intent, requestCode);
	}
	
	/**
	 * @param 职业一级选择目录
	 * @param requestCode
	 * @param type
	 */
	public static void startProfessionActivityForResult(
			Activity fromActivity, int requestCode) {
		Intent intent = new Intent(fromActivity,
				ProfessionActivity.class);
		fromActivity.startActivityForResult(intent, requestCode);
	}
	/**
	 * @param 职业二级选择目录
	 * @param requestCode
	 * @param type
	 */
	public static void startProfessionSecondActivityForResult(
			Activity fromActivity, int requestCode, int pid) {
		Intent intent = new Intent(fromActivity,
				ProfessionSecondActivity.class);
		intent.putExtra(ENavConsts.EProfessionAndCustomzationSecond, pid);
		fromActivity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 选择行业
	 * 
	 * @param type
	 *            -0 行业 1 定制
	 * @param fromActivity
	 * @param requestCode
	 * @return MIndustrys
	 */
	public static void startChooseProfessionActivity(Context context, int type) {
		Intent intent = new Intent(context,
				ProfessionAndCustomzationActivity.class);
		intent.putExtra(ENavConsts.EProfessionAndCustomzation, type);
		context.startActivity(intent);
	}

	/**
	 * 跳转到 ,帐号管理页面
	 * 
	 * @param context
	 */
	public static void startAccountManagerActivity(Context context) {
		Intent intent = new Intent(context, AccountManagerActivity.class);
		context.startActivity(intent);
	}

	/**
	 * 跳转到 ,帐号信息页面
	 * 
	 * @param context
	 */
	public static void startAccountInformationActivity(Context context) {
		Intent intent = new Intent(context, AccountInfoManagerActivity.class);
		context.startActivity(intent);
	}

	/**
	 * 跳转到 ,黑名单
	 * 
	 * @param context
	 */
	public static void startBlackListActivity(Context context) {
		Intent intent = new Intent(context, BlackListActivity.class);
		context.startActivity(intent);
	}

	/**
	 * 推荐联系人
	 * 
	 * @param context
	 */
	public static void startPushPeople2Activity(Context context,
			ArrayList<Connections> userConnectionsList,
			ArrayList<Connections> unUserConnectionsList) {
		Intent intent = new Intent(context, PushPeople2Activity.class);
		intent.putExtra("listOnLine", userConnectionsList);
		intent.putExtra("listOffLine", unUserConnectionsList);
		context.startActivity(intent);
	}

	/**
	 * 跳转到修改密码
	 * 
	 * @param context
	 */
	public static void startChangePasswordActivity(Context context) {
		Intent intent = new Intent(context, ChangePasswordActivity.class);
		context.startActivity(intent);
	}

	/**
	 * 跳转到邮件发送
	 * 
	 * @param context
	 */
	public static void startSendVerifyEmailActivity(Context context,
			Intent intent) {
		context.startActivity(intent);
	}

	/** @param 跳转到会议邀请函 */
	public static void startMeetingInvitationActivity(Context context) {
		Intent intent = new Intent(context, MeetingInvitationCardActivity.class);
		context.startActivity(intent);
	}

	/** @param 跳转到邀请好友 */
	public static void startMeetingInviteFriendsActivity(Context context) {
		Intent intent = new Intent(context, InviteFriendsActivity.class);
		context.startActivity(intent);
	}

	/**
	 * 转发到好友
	 * 
	 * @param context
	 */
	public static void startForwardToFriendActivity(Context context,
			JTContact2 jtContact) {
		Intent intent = new Intent(context, ForwardToFriendActivity.class);
		intent.putExtra("JTContact2", jtContact);
		context.startActivity(intent);
	}

	/**
	 * 转发到好友(新)
	 * 
	 * @param context
	 */
	public static void startForwardToFriendActivity(Context context,
			JTFile jtFile) {
		Intent intent = new Intent(context, ForwardToFriendActivity.class);
		intent.putExtra("JTFile", jtFile);
		context.startActivity(intent);
	}

	/**
	 * 转发到好友
	 * 
	 * @param context
	 */
	public static void startForwardToFriendActivity(Context context,
			ForwardDynamicNews forwardDynamicNews) {
		Intent intent = new Intent(context, ForwardToFriendActivity.class);
		intent.putExtra("ForwardDynamicNews", forwardDynamicNews);
		context.startActivity(intent);
	}

	/**
	 * 转发到好友 inviteType public final static int TYPE_EXTERNAL_USE = 0; public
	 * final static int TYPE_INVITE_SPEAKER_FRIEND = 1; public final static int
	 * TYPE_INVITE_ATTEND_FRIEND = 2; public final static int
	 * TYPE_INVITE_OTHER_FRIEND = 3; public final static int
	 * TYPE_SHARE_TO_MEETING = 4; // 分享到会议 public final static int
	 * TYPE_INVITE_AT_FRIEND = 5; // @好友 public final static int
	 * TYPE_INVITE_AT_FLOW = 6; // 动态
	 * 
	 * @param context
	 */
	public static void startInviteFriendActivity(Activity activity,
			String fromName, int inviteType, int requestCode) {
		Intent intent = new Intent(activity, InviteFriendActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, fromName);
		intent.putExtra(Util.IK_VALUE, inviteType);
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 跳转到地区选择页面
	 * 
	 * @param activity
	 * @param mListCountry
	 * @param requestCode
	 */
	public static void startCountryCodeActivity(Activity activity,
			MListCountry mListCountry, int requestCode) {
		Intent intent = new Intent(activity, CountryCodeActivity.class);
		intent.putExtra(ENavConsts.EListCountry, mListCountry);
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 转发到社交
	 * 
	 * @param context
	 * @param jtFile
	 */
	public static void startSocialShareActivity(Context context, JTFile jtFile) {
		Intent intent = new Intent(context, SocialShareActivity.class);
		intent.putExtra(ENavConsts.EShareParam, jtFile);
		context.startActivity(intent);
	}

	/**
	 * 转发到社交
	 * 
	 * @param context
	 * @param listJtFile
	 */
	public static void startSocialShareActivity(Context context,
			ArrayList<JTFile> listJtFile) {
		Intent intent = new Intent(context, SocialShareActivity.class);
		intent.putExtra(ENavConsts.EShareParamList, listJtFile);
		context.startActivity(intent);
	}

	/**
	 * 跳转到发起会议
	 * 
	 * @param context
	 * @param jtFile
	 */
	public static void startInitiatorHYActivity(Context context) {
		Intent intent = new Intent(context, InitiatorHYActivity.class);
		context.startActivity(intent);
	}

	/**
	 * 启动会议畅聊界面
	 * 
	 * @param context
	 * @param meetingQuery
	 * @param topicQuery
	 * @param listJtFile
	 */
	public static void startConferenceChatActivity(Context context,
			MMeetingQuery meetingQuery, MMeetingTopicQuery topicQuery,
			ArrayList<JTFile> listJtFile) {
		Intent intent = new Intent(context, MeetingChatActivity.class);
		intent.putExtra(ENavConsts.EMeetingDetail, meetingQuery);
		intent.putExtra(ENavConsts.EMeetingTopicDetail, topicQuery);
		intent.putExtra(ENavConsts.EListJTFile, listJtFile);
		context.startActivity(intent);
	}

	/**
	 * 启动主会场
	 * 
	 * @param context
	 * @param meetingId
	 * @param requestCode
	 */
	public static void startMeetingMasterActivityForResult(Context context,
			long meetingId, int requestCode) {
		Intent intent = new Intent(context, MeetingMasterActivity.class);
		intent.putExtra("meeting_id", meetingId);
		if (requestCode != -1) {
			((Activity) context).startActivityForResult(intent, requestCode);
		} else {
			context.startActivity(intent);
		}
	}

	/**
	 * 启动 获得联系人列表 服务
	 * 
	 * @param context
	 */
	public static void startGetConnectionsListService(Context context,
			RequestType requestType) {
		GetConnectionsListService.startGetConnectionsListService(context,
				requestType);
	}

	/**
	 * 启动 创建需求列表 界面 选择投融资类型
	 */
	public static void startDemandCreateActivity(Context context) {
		Intent intent = new Intent(context, CreateDemandActivity.class);
		context.startActivity(intent);
	}

	/**
	 * 回调 创建需求
	 * 
	 * @param context
	 * @param requestCode
	 */
	public static void startDemandActivityForResult(Activity context,
			int requestCode) {
		Intent intent = new Intent(context, CreateDemandActivity.class);
		intent.putExtra(ENavConsts.DEMAND_FOR_RESULT, true);
		context.startActivityForResult(intent, requestCode);
	}

	/**
	 * 回调 选择模版
	 * 
	 * @param context
	 */
	public static void startDemandTemplateActivityForResult(Activity context,
			int requestCode, int typeid) {
		Intent intent = new Intent(context, TemplateActivity.class);
		intent.putExtra(ENavConsts.DEMAND_TYPE, typeid);
		intent.putExtra(ENavConsts.DEMAND_FOR_RESULT, true);
		context.startActivityForResult(intent, requestCode);
	}

	/**
	 * 回调 创建需求详情
	 * 
	 * @param context
	 * @param bean
	 *            ：新需求来自何处，模版的名称 类型
	 */
	public static void startDemandNewActivityForResult(Activity context,
			int requestCode, TemplateData bean, int typeId) {
		Intent intent = new Intent(context, NewDemandActivity.class);
		intent.putExtra(ENavConsts.DEMAND_NEW, bean);
		intent.putExtra(ENavConsts.DEMAND_FOR_RESULT, DemandEnum.Callback);
		intent.putExtra(ENavConsts.DEMAND_TYPE, typeId);
		context.startActivityForResult(intent, requestCode);
	}

	/**
	 * 启动 选择需求模版界面
	 * 
	 * @param context
	 */
	public static void startDemandTemplateActivity(Context context, int typeid) {
		Intent intent = new Intent(context, TemplateActivity.class);
		intent.putExtra(ENavConsts.DEMAND_TYPE, typeid);
		context.startActivity(intent);
	}

	/**
	 * 创建新需求界面
	 * 
	 * @param context
	 * @param bean
	 *            ：新需求来自何处，模版的名称 类型
	 */
	public static void startDemandNewActivity(Context context,
			TemplateData bean, int typeId) {
		Intent intent = new Intent(context, NewDemandActivity.class);
		intent.putExtra(ENavConsts.DEMAND_NEW, bean);
		intent.putExtra(ENavConsts.DEMAND_FOR_RESULT, DemandEnum.Default);
		intent.putExtra(ENavConsts.DEMAND_TYPE, typeId);
		context.startActivity(intent);
	}

	/**
	 * 编辑需求信息界面
	 * 
	 * @param activity
	 * @param requestCode
	 * @param typeid
	 * @param data
	 */
	public static void startDemandEditActivity(Activity activity,
			int requestCode, int typeid, DemandDetailsData data,
			ArrayList<LableData> lable, ArrayList<UserCategory> category,
			DemandEnum demandEnum) {
		// 需求详情信息
		Intent intent = new Intent(activity, NewDemandActivity.class);
		intent.putExtra(ENavConsts.DEMAND_FOR_RESULT, demandEnum);
		intent.putExtra(ENavConsts.DEMAND_EDIT, data);
		intent.putExtra(ENavConsts.DEMAND_TYPE, typeid);
		intent.putExtra(ENavConsts.DEMAND_LABEL_DATA, lable);
		intent.putExtra(ENavConsts.DEMAND_CATEGORY_DATA, category);
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 查找需求
	 */
	public static void startFindDemandActivity(Context context, int type) {
		Intent intent = new Intent(context, DemandAndPricesActivity.class);
		intent.putExtra(ENavConsts.DEMAND_TYPE, type);
		context.startActivity(intent);
	}

	/**
	 * 查找项目
	 */
	public static void startFindProjectActivity(Context context, int type) {
		Intent intent = new Intent(context, FindProjectActivity.class);
		intent.putExtra(ENavConsts.DEMAND_TYPE, type);
		context.startActivity(intent);
	}

	/**
	 * 查找人脉
	 */
	public static void startFindPeopleActivity(Context context, int type) {
		Intent intent = new Intent(context, ContactsMainPageActivity.class);
		context.startActivity(intent);
	}

	/**
	 * 查找资金
	 */
	public static void startFindPricesActivity(Context context, int type) {
		Intent intent = new Intent(context, FindProjectActivity.class);
		intent.putExtra(ENavConsts.DEMAND_TYPE, type);
		context.startActivity(intent);
	}

	/**
	 * 
	 * 
	 * @param context
	 */
	public static void startReportMessageActivity(Context context,
			String demandid) {
		Intent intent = new Intent(context, ReportMessageActivity.class);
		intent.putExtra(ENavConsts.DEMAND_DETAILS_ID, demandid);
		context.startActivity(intent);
	}

	/**
	 * 我的需求
	 * 
	 * @param context
	 */
	public static void startMeNeedActivity(Context context) {
		Intent intent = new Intent(context, MeNeedActivity.class);
		context.startActivity(intent);
	}

	/**
	 * 启动三级多选回调界面
	 * 
	 * @param activity
	 * @param title
	 * @param type
	 * @param isMultiSelect
	 */
	public static void startChooseActivityForResult(Activity activity,
			boolean isMultiSelect, String title, int type,
			ArrayList<Metadata> data) {
		Intent intent = new Intent(activity, ChooseActivity.class);
		intent.putExtra(ENavConsts.DEMAND_CHOOSE_MULTI, isMultiSelect); // 单选or多选
		intent.putExtra(ENavConsts.DEMAND_CHOOSE_TITLE, title); // 标题
		intent.putExtra(ENavConsts.DEMAND_CHOOSE_TYEP, type); // 类型
		intent.putExtra(ENavConsts.DEMAND_CHOOSE_DATA, data);
		activity.startActivityForResult(intent,
				ENavConsts.ActivityReqCode.REQUEST_CHOOSE_SELECT);

	}

	/**
	 * 启动三级多选回调界面
	 * 
	 * @param activity
	 * @param title
	 * @param type
	 * @param isMultiSelect
	 */
	public static void startChooseActivityForResult(Activity activity,
			boolean isMultiSelect, String title, int type,
			ArrayList<Metadata> data, String id) {
		Intent intent = new Intent(activity, ChooseActivity.class);
		intent.putExtra(ENavConsts.DEMAND_CHOOSE_MULTI, isMultiSelect); // 单选or多选
		intent.putExtra(ENavConsts.DEMAND_CHOOSE_TITLE, title); // 标题
		intent.putExtra(ENavConsts.DEMAND_CHOOSE_TYEP, type); // 类型
		intent.putExtra(ENavConsts.DEMAND_CHOOSE_DATA, data);
		intent.putExtra(ENavConsts.PEOPLE_ID, id);
		activity.startActivityForResult(intent,
				ENavConsts.ActivityReqCode.REQUEST);

	}

	/**
	 * 查询需求详情
	 * 
	 * @param context
	 * @param demandid
	 * @param demandCreate
	 * @param from
	 *            :1-需要权限判断，2-不需要权限判断" 金桐推荐是2都是大乐
	 */
	public static void startNeedDetailsActivity(Context context,
			String demandid, int from) {
		Intent intent = new Intent(context, NeedDetailsActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(ENavConsts.DEMAND_DETAILS_ID, demandid);
		intent.putExtra(ENavConsts.DEMAND_DETAILS_FROM, from);
		context.startActivity(intent);
	}

	/**
	 * 标签_筛选查看和管理
	 * 
	 * @param context
	 * 
	 */
	public static void startCheckLabelActivity(Activity context, int result,
			ArrayList<LableData> lablelist,
			CreateLabelActivity.ModulesType modulesType) {
		Intent intent = new Intent(context, CreateLabelActivity.class);
		intent.putExtra(EConsts.Key.MODULES_TYPE, modulesType);// 模块类型
		intent.putExtra(ENavConsts.DEMAND_LABEL_DATA, lablelist);
		context.startActivityForResult(intent, result);
	}

	/**
	 * 通过标签 查询的时候
	 * 
	 * @param context
	 * @param modulesType
	 *            ：模块类型，兼容其他三个模块
	 * @param context
	 */
	public static void startRedactLabelActivity(Context context,
			RedactLabelActivity.ModulesType modulesType, boolean isType) {
		Intent intent = new Intent(context, RedactLabelActivity.class);
		intent.putExtra(EConsts.Key.MODULES_TYPE, modulesType);// 模块类型
		intent.putExtra(ENavConsts.DEMAND_LABEL_TYPE, isType);// 标签的类型
		context.startActivity(intent);
	}

	/**
	 * 批量打标签的时候
	 * 
	 * @param context
	 * @param result
	 * @param isType
	 *            :true 点击标签直接跳转 false：长按需求 跳转
	 * @param modulesType
	 *            ：模块类型，兼容其他三个模块
	 */
	public static void startRedactLabelActivity(Activity context, int result,
			RedactLabelActivity.ModulesType modulesType, boolean isType) {
		Intent intent = new Intent(context, RedactLabelActivity.class);
		intent.putExtra(EConsts.Key.MODULES_TYPE, modulesType);// 模块类型
		intent.putExtra(ENavConsts.DEMAND_LABEL_TYPE, isType);// 标签的类型
		context.startActivityForResult(intent, result);
	}

	/**
	 * 批量打标签的时候
	 * 
	 * @param context
	 * @param fragment
	 *            启动的 fragment
	 * @param result
	 * @param isType
	 *            :true 点击标签直接跳转 false：长按需求 跳转
	 * @param modulesType
	 *            ：模块类型，兼容其他三个模块
	 */
	public static void startRedactLabelActivityForResult(Activity context,
			Fragment fragment, int result,
			RedactLabelActivity.ModulesType modulesType, boolean isType) {
		Intent intent = new Intent(context, RedactLabelActivity.class);
		intent.putExtra(EConsts.Key.MODULES_TYPE, modulesType);// 模块类型
		intent.putExtra(ENavConsts.DEMAND_LABEL_TYPE, isType);// 标签的类型
		if (fragment != null) {
			fragment.startActivityForResult(intent, result);
		} else {
			context.startActivityForResult(intent, result);
		}
	}

	/**
	 * 跳转到 选择金额信息界面
	 * 
	 * @param context
	 */
	public static void startAmountOfMoneyActivity(Activity context,
			int requestCode, AmountData money) {
		Intent intent = new Intent(context, AmountOfMoneyActivity.class);
		intent.putExtra(ENavConsts.DEMAND_MONEY_TAG, money);
		context.startActivityForResult(intent, requestCode);
	}

	/**
	 * 打开介绍页面
	 */
	public static void startIntroduceActivity(Activity context, NoteData note,
			int requestCode) {
		Intent intent = new Intent(context, IntroduceActivity.class);
		intent.putExtra(ENavConsts.DEMAND_NOTE_DATA, note);
		context.startActivityForResult(intent, requestCode);
	}

	/**
	 * 打开会议介绍页面
	 */
	public static void startIntroduceActivity(Activity context,
			ArrayList<MMeetingPic> mMeetingPicList,
			ArrayList<JTFile2ForHY> mMeetingFileList, int requestCode) {
		Intent intent = new Intent(
				context,
				com.tr.ui.conference.initiatorhy.ConferenceIntroduceActivity.class);
		intent.putExtra("mMeetingFileList", mMeetingFileList);
		intent.putExtra("mMeetingPicList", mMeetingPicList);
		context.startActivityForResult(intent, requestCode);
	}

	/**
	 * 权限控制
	 * 
	 * @param context
	 * @param requestCode
	 */
	public static void startLimitsActivity(Activity context, int requestCode,
			HashMap<String, String> data, ArrayList<String> custom) {
		Intent intent = new Intent(context, LimitsActivity.class);
		intent.putExtra(ENavConsts.DEMAND_PERMISSION_DATA, data);
		intent.putExtra(ENavConsts.DEMAND_PERMISSION_CUSTOM, custom);
		context.startActivityForResult(intent, requestCode);
	}

	/**
	 * 读取系统视频/图片
	 * 
	 * @param context
	 * @param requestCode
	 */
	public static void startSelectPictureActivity(Activity context,
			int requestCode, ArrayList<ImageItem> selectedPicture) {
		Intent intent = new Intent(context, SelectPictureActivity.class);
		intent.putExtra(ENavConsts.DEMAND_INTENT_SELECTED_PICTURE,
				selectedPicture);
		context.startActivityForResult(intent, requestCode);
	}

	/**
	 * 读取系统视频/图片 - 单选
	 * 
	 * @param context
	 * @param requestCode
	 */
	public static void startSelectPictureActivityforSingleSelection(
			Activity context, int requestCode,
			ArrayList<JTFile> selectedPicture, Boolean WenSiHaiHui) {
		Intent intent = new Intent(context, HeadPortraitActivity.class);
		intent.putExtra(ENavConsts.DEMAND_INTENT_SELECTED_PICTURE,
				selectedPicture);
		intent.putExtra("WenSiHaiHui", WenSiHaiHui);
		context.startActivityForResult(intent, requestCode);
	}

	// /**
	// * 读取系统视频/图片 - 文思海辉
	// *
	// * @param context
	// * @param requestCode
	// */
	// public static void startSelectPictureActivity(Activity context,
	// int requestCode, ArrayList<ImageItem> selectedPicture,Boolean WenSiHaiHui
	// ,boolean video) {
	// Intent intent = new Intent(context, SelectPictureActivity.class);
	// intent.putExtra(ENavConsts.DEMAND_INTENT_SELECTED_PICTURE,
	// selectedPicture);
	// intent.putExtra("WenSiHaiHui", WenSiHaiHui);
	// intent.putExtra("video", video);
	// context.startActivityForResult(intent, requestCode);
	// }

	/**
	 * @param context
	 * @param requestCode
	 * @param selectedPicture
	 *            所选图片
	 * @param image
	 *            已 有图片数量
	 * @param video
	 *            是否已有视频
	 */
	public static void startSelectPictureActivity(Activity context,
			int requestCode, ArrayList<JTFile> selectedPicture, boolean video) {
		Intent intent = new Intent(context, SelectPictureActivity.class);
		intent.putExtra(ENavConsts.DEMAND_INTENT_SELECTED_PICTURE,
				selectedPicture);
		intent.putExtra("video", video);
		context.startActivityForResult(intent, requestCode);
	}

	/**
	 * 附件
	 * 
	 * @param context
	 * @param requestCode
	 */
	public static void startAccessoryActivity(Activity context, int requestCode) {
		Intent intent = new Intent(context, AccessoryActivity.class);
		intent.putExtra(ENavConsts.DEMAND_INTENT_SELECTED_ACCESSORY, "");
		context.startActivityForResult(intent, requestCode);
	}

	/**
	 * 附件
	 * 
	 * @param context
	 * @param fileListSD
	 *            已选附件
	 * @param requestCode
	 */
	public static void startAccessoryActivity(Activity context,
			ArrayList<JTFile> fileListSD, int requestCode) {
		Intent intent = new Intent(context, AccessoryActivity.class);
		intent.putExtra(ENavConsts.DEMAND_INTENT_SELECTED_ACCESSORY, fileListSD);
		context.startActivityForResult(intent, requestCode);
	}

	/**
	 * 上传文件
	 * 
	 * @param context
	 * @param fileListSD
	 * @param requestCode
	 */
	public static void startUploadFileAccessoryActivity(Activity context,
			ArrayList<JTFile> fileListSD, int requestCode, boolean isFromUpload) {
		Intent intent = new Intent(context, AccessoryActivity.class);
		intent.putExtra(ENavConsts.DEMAND_INTENT_SELECTED_ACCESSORY, fileListSD);
		intent.putExtra(ENavConsts.ISFROMUPLOAD, isFromUpload);
		context.startActivityForResult(intent, requestCode);
	}

	/**
	 * 根据标签查询 我的需求信息
	 * 
	 * @param context
	 * @param lable
	 * @param isType
	 *            :点击跳转为false, 长按点击为true
	 */
	public static void startDemandLableActivity(Context context,
			LableData lable, DemandLableActivity.ModulesType modulesType,
			boolean isType) {
		Intent intent = new Intent(context, DemandLableActivity.class);
		intent.putExtra(ENavConsts.DEMAND_LABEL_DATA, lable);
		intent.putExtra(EConsts.Key.MODULES_TYPE, modulesType);// 模块类型
		intent.putExtra(ENavConsts.DEMAND_LABEL_TYPE, isType);
		context.startActivity(intent);
	}

	/**
	 * 通过目录 查询我的需求
	 */
	public static void startDemandCategoryActivity(Context context,
			UserCategory cateory, ModuleType categoryType) {
		Intent intent = new Intent(context, DemandCategoryActivity.class);
		intent.putExtra(ENavConsts.DEMAND_LABEL_DATA, cateory);
		intent.putExtra(ENavConsts.DEMAND_LABEL_TYPE, categoryType);
		context.startActivity(intent);
	}

	/**
	 * 通过目录 查询我的组织
	 */
	public static void startOrganizationCategoryActivity(Context context,
			UserCategory cateory, ModuleType categoryType) {
		Intent intent = new Intent(context, OrganizationCategoryActivity.class);
		intent.putExtra(ENavConsts.DEMAND_LABEL_DATA, cateory);
		intent.putExtra(ENavConsts.DEMAND_LABEL_TYPE, categoryType);
		context.startActivity(intent);
	}

	/**
	 * 创建人脉
	 * 
	 * @param context
	 */
	public static void startCreateConnctionsActivity(Context context) {
		Intent intent = new Intent(context, NewConnectionsActivity.class);
		context.startActivity(intent);
	}

	/*
	 * 发现-----组织-----组织首页
	 */
	public static void startOrganizationFirstPageActivity(Context context) {
		// Intent intent = new Intent(context,
		// OrganizationFirstPageActivity.class);
		Intent intent = new Intent(context, FindProjectActivityOrg.class);
		context.startActivity(intent);
	}

	public static void startOrganizationActivity(Context context) {
		Intent intent = new Intent(context,
				OrganizationAndCustomerActivity.class);
		context.startActivity(intent);
	}

	/**
	 * 启动 @ 好友列表
	 * 
	 * @param context
	 * @author SunJianNan
	 */
	public static void startAtInformFriendsActivityForResult(Activity activity,
			int requestCode) {
		Intent intent = new Intent(activity, AtInformFriendsActivity.class);
		activity.startActivityForResult(intent, requestCode);
	}

	/*
	 * 创建新事物
	 */
	public static void startNewAffarActivity(Activity activity) {

		Intent intent = new Intent(activity, WorkNewActivity.class);
		activity.startActivity(intent);
	}

	/*
	 * 创建新事物 关联转 inPeopleNode 人脉节点 可空 null inOrganizNode 组织节点 可空 null
	 * inKnowledgeNode 知识节点 可空 null inAffairNode 事件节点 可空 null
	 */
	public static void startNewAffarActivityByRelation(Activity activity,
			ConnectionNode inPeopleNode, ConnectionNode inOrganizNode,
			KnowledgeNode inKnowledgeNode, AffairNode inAffairNode) {

		Intent intent = new Intent(activity, WorkNewActivity.class);

		if (inPeopleNode != null)
			intent.putExtra(EConsts.Key.RELATED_PEOPLE_NODE, inPeopleNode);
		if (inOrganizNode != null)
			intent.putExtra(EConsts.Key.RELATED_ORGANIZATION_NODE,
					inOrganizNode);
		if (inKnowledgeNode != null)
			intent.putExtra(EConsts.Key.RELATED_KNOWLEDGE_NODE, inKnowledgeNode);
		if (inAffairNode != null)
			intent.putExtra(EConsts.Key.RELATED_AFFAIR_NODE, inAffairNode);

		activity.startActivity(intent);
	}

	/*
	 * 事物详情 inAffairId 事物ID
	 */
	public static void startAffarDeatilActivity(Activity activity,
			String inAffairId) {

		Intent intent = new Intent(activity, WorkNewActivity.class);
		intent.putExtra("AffarId", inAffairId);
		intent.putExtra("OperType", "s");
		activity.startActivity(intent);
	}

	/**
	 * 跳转到创建组织 页面(传对象)
	 * 
	 * @param activity
	 * @param requestCode
	 */
	public static void startCreateOrgActivity(Activity activity,
			RegisteOrgDetail customer) {
		Intent intent = new Intent(activity, CreateOrganizationActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putSerializable(EConsts.Key.CreateOrg, customer);
		intent.putExtras(mBundle);
		activity.startActivity(intent);

	}

	/**
	 * 跳转到创建组织 页面(不传参数)
	 * 
	 * @param activity
	 * @param requestCode
	 */
	public static void startCreateOrgNullActivityForResult(Activity activity) {
		Intent intent = new Intent(
				activity,
				com.tr.ui.organization.create_clientele.CreateOrganizationActivity.class);
		activity.startActivity(intent);
	}

	/**
	 * 人脉目录标签
	 * 
	 * @param activity
	 */
	public static void startPeopleCategoryLabelActivity(Context context,
			Long cid, String categryName, Long tid, String labelName) {
		Intent intent = new Intent(context, PeopleCategoryLabelActivity.class);
		intent.putExtra(EConsts.Key.CATEGORY_KEY, cid);
		intent.putExtra(EConsts.Key.CATEGORY_NAME, categryName);
		intent.putExtra(EConsts.Key.LABEL_KEY, tid);
		intent.putExtra(EConsts.Key.LABEL_NAME, labelName);
		context.startActivity(intent);
	}

	/**
	 * 知识目录标签
	 * 
	 * @param activity
	 */
	public static void startKnowledgeCategoryLabelActivity(Context context,
			Long cid, String categryName, Long tid, String labelName) {
		Intent intent = new Intent(context,
				KnowledgeCategoryLabelActivity.class);
		intent.putExtra(EConsts.Key.CATEGORY_KEY, cid);
		intent.putExtra(EConsts.Key.CATEGORY_NAME, categryName);
		intent.putExtra(EConsts.Key.LABEL_KEY, tid);
		intent.putExtra(EConsts.Key.LABEL_NAME, labelName);
		context.startActivity(intent);
	}

	/**
	 * 进入文件管理 id : 目录ID pid: 父级ID
	 * 
	 * @param context
	 */
	public static void startFileManagementActivity(Activity fromActivity,
			String id, String pid) {
		Intent intent = new Intent(fromActivity, FileManagementActivity.class);
		intent.putExtra(FileManagementActivity.ISCHATSAVEFILE, false);
		intent.putExtra(FileManagementActivity.ISSHOWCHECKBOX, false);
		if (!TextUtils.isEmpty(id)) {
			intent.putExtra(ENavConsts.FILEMANAGEMENT_ID, id);
		}
		if (!TextUtils.isEmpty(pid)) {
			intent.putExtra(ENavConsts.FILEMANAGEMENT_PID, pid);
		}
		fromActivity.startActivity(intent);
		// fromActivity.finish();
	}

	/**
	 * 从畅聊中保存文件
	 * 
	 * @param fromActivity
	 * @param fileIds
	 */
	public static void startFileManagementActivity(Activity fromActivity,
			String savefileId) {
		Intent intent = new Intent(fromActivity, FileManagementActivity.class);
		intent.putExtra(FileManagementActivity.SAVE_FILE_ID, savefileId);
		intent.putExtra(FileManagementActivity.ISCHATSAVEFILE, true);
		intent.putExtra(FileManagementActivity.ISFROMUPLOAD, false);
		fromActivity.startActivity(intent);
	}

	/**
	 * 启动文件预览界面
	 * 
	 * @param activity
	 * @param jtFile
	 * @param messageId
	 * @param meetingId
	 * @param topicId
	 */
	public static void startFilePreviewActivity(Context activity,
			JTFile jtFile, boolean isShowSaveButton) {
		Intent intent = new Intent(activity, FilePreviewActivity.class);
		intent.putExtra(EConsts.Key.JT_FILE, jtFile);
		intent.putExtra("isShowSaveButton", isShowSaveButton);
		activity.startActivity(intent);
	}

	/** @param 跳转到意见反馈界面 */
	public static void startOnekeyBackActivity(Context context) {
		Intent intent = new Intent(context, OnekeyBackActivity.class);
		context.startActivity(intent);
	}

	/**
	 * 第三方登录后未进行绑定的跳转到完善资料页面
	 * 
	 * @param activity
	 */
	public static void startCompleteuserInfoActivity(Activity activity) {
		Intent intent = new Intent(activity, CompleteUserInfoActivity.class);
		activity.startActivity(intent);
	}

	/**
	 * 跳转到绑定手机号
	 * 
	 * @param activity
	 * @param status
	 *            1：直接绑定的 2：解绑后再重新绑定一个新手机号
	 */
	public static void startBindingMobileActivity(Activity activity, int status) {
		Intent intent = new Intent(activity, BindingMobileActivity.class);
		intent.putExtra("status", status);
		activity.startActivity(intent);
	}

	/**
	 * 跳转到解绑绑定手机号
	 * 
	 * @param activity
	 * @param mobile
	 *            手机号
	 */
	public static void startUnBindingMobileActivity(Activity activity,
			String mobile) {
		Intent intent = new Intent(activity, UnBindingMobileActivity.class);
		intent.putExtra("mobile", mobile);
		activity.startActivity(intent);
	}

	/**
	 * 跳转到绑定邮箱
	 * 
	 * @param activity
	 * @param type
	 *            1表示原来就没有邮箱直接绑定 2表示解绑原邮箱后再绑定新邮箱
	 */
	public static void startBindingEmailActivity(Activity activity, int type) {
		Intent intent = new Intent(activity, BindingEmailActivity.class);
		intent.putExtra("type", type);
		activity.startActivity(intent);
	}

	/**
	 * 跳转到解绑邮箱
	 * 
	 * @param activity
	 */
	public static void startUnBindingEmailActivity(Activity activity,
			String email) {
		Intent intent = new Intent(activity, UnBindingEmailActivity.class);
		intent.putExtra("email", email);
		activity.startActivity(intent);
	}

	/**
	 * 启动主讲人设置界面
	 * 
	 * @param item
	 */
	public static void startMeetingSpeakerTopicSettingActivity(
			Activity mContext, JTContactMini item) {
		Intent intent = new Intent(mContext,
				MeetingSpeakerTopicSettingActivity.class);
		intent.putExtra("JTContactMini", item);
		mContext.startActivityForResult(intent, 100);
	}

	/**
	 * 启动EditText界面
	 * 
	 * @param topicName
	 * @param position
	 */
	public static void startEditTextContentActivity(Activity mContext,
			String str, int resultCode) {
		Intent intent = new Intent(mContext, EditTextContentActivity.class);
		intent.putExtra("editTextInfo", str);
		mContext.startActivityForResult(intent, resultCode);
	}

	public static void startMeetingNoteActivity(Activity mContext, int mMid,
			boolean is_edit) {
		Intent intent = new Intent(mContext, RTMeetingNoteActivity.class);
		intent.putExtra("meeting_id", mMid);
		intent.putExtra("is_edit", true);
		mContext.startActivity(intent);
	}

	/**
	 * 发布动态
	 */
	public static void startCreateFlowActivity(Activity mContext, String Userid) {
		Intent intent = new Intent(mContext, CreateFlowActivtiy.class);
		intent.putExtra("Userid", Userid);
		mContext.startActivityForResult(intent, 9);
	}

	/**
	 * 发布动态(分享到动态)
	 * JTFile  代表转发到动态
	 */
	public static void startCreateFlowActivity(Context mContext, JTFile jtFile,
			String Userid) {
		Intent intent = new Intent(mContext, CreateFlowActivtiy.class);
		intent.putExtra("JTFile", jtFile);
		intent.putExtra("Userid", Userid);
		mContext.startActivity(intent);
	}

	/**
	 * 谁可以看
	 */
	public static void startWhoCanActivityForResult(Activity mContext,
			String Userid, int requestCode) {
		Intent intent = new Intent(mContext,
				DynamicPermissionsSettingActvitiy.class);
		intent.putExtra("Userid", Userid);
		mContext.startActivityForResult(intent, requestCode);
	}

	/**
	 * 项目详情
	 * 
	 * @param organizationId
	 *            如果为0则未承接或者人承接
	 * @param recipientName
	 * @param publisherName
	 * @param isApply
	 * @param chewei添加参数applysum传有几人申请
	 * @param "status": "-1未开始,0项目进行中、1完成、2、放弃、3已过期",4 申请中
	 */
	public static void startProjectDetailsActivityForResult(Activity mContext, String projectId, String organizationId, String recipientName, String publisherName, ProjectType projectType, int status, String publisherId,long recipientTime,String projectAcceptId, int applySum,int requestCode) {
		Intent intent = new Intent(mContext, ProjectDetailsActivity.class);
		intent.putExtra("projectId", projectId);
		intent.putExtra("projectType", projectType);
		intent.putExtra("organizationId", organizationId);
		intent.putExtra("status", status);
		intent.putExtra("publisherName", publisherName);
		intent.putExtra("publisherId", publisherId);
		intent.putExtra("recipientName", recipientName);
		intent.putExtra("recipientTime", recipientTime);
		intent.putExtra("projectAcceptId", projectAcceptId);
		intent.putExtra("applysum", applySum);
		mContext.startActivityForResult(intent, requestCode);
	}

	/**
	 * 金桐浏览WEB
	 */
	public static void startWebViewActivity(Context mContext, String webURL,
			String webTitle) {
		Intent paramsIntent = new Intent();
		paramsIntent.putExtra("WEBURL", webURL);
		paramsIntent.putExtra("WEBVIEWTITLE", webTitle);
		paramsIntent.setClass(mContext, ViewWebURLActivity.class);
		mContext.startActivity(paramsIntent);
	}

	/**
	 * 跳转到组织详情中的我的考勤
	 * 
	 * @param mContext
	 * @param oid
	 */
	public static void startRecordActivity(Context mContext, String oid) {
		Intent intent = new Intent(mContext, OrganizationRecordActivity.class);
		intent.putExtra("oid", oid);
		mContext.startActivity(intent);
	}

	/**
	 * 
	 * @param mContext
	 * @param communityId
	 * @param isNumIn 是否群号加群
	 *            社群ID
	 */
	public static void startCommunityDetailsActivity(Context mContext,
			long communityId, boolean isNumIn) {
		Intent intent = new Intent(mContext, CommunitiesDetailsActivity.class);
		intent.putExtra(GlobalVariable.COMMUNITY_ID, communityId);
		intent.putExtra(GlobalVariable.COMMUNITY_ISNUMIN, isNumIn);
		mContext.startActivity(intent);
	}

	/**
	 * 跳转到编辑昵称界面
	 * 
	 * @param mContext
	 * @param mID
	 */
	public static void startCommunityEditSettingActivity(Activity mContext,
			String communityEditSettingData, String communityEditSettingTitle,
			int requestCode) {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("communityEditSettingData", communityEditSettingData);
		bundle.putString("communityEditSettingTitle", communityEditSettingTitle);
		intent.putExtras(bundle);
		intent.setClass(mContext, CommunityEditSettingActivity.class);
		mContext.startActivityForResult(intent, requestCode);
	}

	/**
	 * 启动社群聊天页面
	 * 
	 * @param fromActivity
	 * @param mucDetail
	 * @param searchMessageId
	 * @param searchKeyword
	 */
	public static void startCommunityChartActivity(Activity fromActivity,
			MUCDetail mucDetail, int fromIndex, String searchKeyword) {
		Intent intent = new Intent(fromActivity, CommunityChatActivity.class);
		intent.putExtra(ENavConsts.EFromActivityName, fromActivity.getClass()
				.getSimpleName());
		intent.putExtra(ENavConsts.EMucDetail, mucDetail);
		intent.putExtra(ENavConsts.ESearchFromIndex, fromIndex);
		intent.putExtra(ENavConsts.ESearchKeyword, searchKeyword);
		fromActivity.startActivity(intent);
	}
	
	public static void startFlowActivity(Activity fromActivity,int type, long id){
		Intent intent = new Intent(fromActivity, FlowActivity.class);
		intent.putExtra(FrgFlow.FLOW_TYPE, type);
		intent.putExtra(FrgFlow.FLOW_USERID, id);
		fromActivity.startActivity(intent);
	}
	
	public static void startMoreJointResourceActivity(Activity fromActivity,
			String userId, ResourceType_new resType, ResourceType_new flag, ResourceSource mResSrc) {
		Intent intent = new Intent(fromActivity,
				MoreJointResourceActivity.class);
		intent.putExtra(EConsts.Key.ID, userId);
		intent.putExtra(EConsts.Key.RELATED_RESOURCE_TYPE, resType);
		intent.putExtra(EConsts.Key.TARGET_RESOURCE_TYPE, flag);
		intent.putExtra(EConsts.Key.RELATED_RESOURCE_SOURCE, mResSrc);
		fromActivity.startActivity(intent);
	}

}
