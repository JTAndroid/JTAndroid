package com.tr.api;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.wifi.WifiEnterpriseConfig.Eap;

import cn.sharesdk.framework.statistics.NewAppReceiver;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tr.App;
import com.tr.model.CommunityStateResult;
import com.tr.model.SimpleResult;
import com.tr.model.conference.ConferenceAndChat;
import com.tr.model.conference.MListFileIndex;
import com.tr.model.conference.MListMeetingQuery;
import com.tr.model.conference.MListMeetinglabel;
import com.tr.model.conference.MListSociality;
import com.tr.model.conference.MMeetingDetail;
import com.tr.model.conference.MMeetingMemberListQuery;
import com.tr.model.conference.MMeetingNoteObj;
import com.tr.model.conference.MMeetingNoticeRelation;
import com.tr.model.conference.MMeetingQuery;
import com.tr.model.conference.MMeetingRequiredSignupInfo;
import com.tr.model.conference.MMeetingTopicList;
import com.tr.model.conference.MMyMeeting;
import com.tr.model.home.MainPageList;
import com.tr.model.im.MSendMessage;
import com.tr.model.obj.KnowledgeMini;
import com.tr.model.obj.RequirementMini;
import com.utils.common.GlobalVariable;
import com.utils.http.EAPIConsts;

/**
 * 会议模式数据解析接口
 * 
 * @author gintong
 */
public class ConferenceRespFactory {

	public static Object createMsgObject(int msgId, JSONObject jsonObject) {
		if (jsonObject == null) {
			return null;
		}
		switch (msgId) {
		//进入删除图片接口
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_DELETE_PHOTO: {
			return SimpleResult.createFactory(jsonObject);
		}
		//进入会议通知接口
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_doEntryMeetingNotice: {
			return SimpleResult.createFactory(jsonObject);
		}
		// 创建会议
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_CREATE_MEETING: {
			return SimpleResult.createFactory(jsonObject);
		}
		// 获得我的会议列表 6.1
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_MEETING_HOME: {
			return MMeetingMemberListQuery.createFactory(jsonObject);
		}
		// 获得我的邀请函列表 6.2
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_MY_INVITION: {
			return MMeetingMemberListQuery.createFactory(jsonObject);
		}
		// 我的通知列表 6.27
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_MY_NOYICE: {
			return MMeetingNoticeRelation.createFactory(jsonObject);
		}
		// 获得会议广场列表 6.23
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_MEETING_PLAZA: {
			JSONObject jsonPageObject = jsonObject.optJSONObject("page");
			if (null != jsonPageObject) {
				return new MMyMeeting().createFactory(jsonPageObject);
			} else {
				return null;
			}
		}
		// 成员报名 6.12
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_MEETING_MEMBER_REPORT: {
			return SimpleResult.createFactory(jsonObject);
		}

		// 获取会议详情 6.5
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_MEETING_DETAIL: {
			return MMeetingDetail.createFactory(jsonObject);
		}

		// 获取我的会议6.15
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_MY_MEETING: {
			return new MMyMeeting().createFactory(jsonObject);
		}
		// 获取我的会议必填选项 6.31
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_REQUIREDSIGNUPINFO: {
			return MMeetingRequiredSignupInfo.createFactory(jsonObject);
		}
		// 报名6.7
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_SIGN_UP_MEETING: {
			return SimpleResult.createFactory(jsonObject);
		}
		// 完善报名信息6.29
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_IMPROVESIGNINFORMATION: {
			return SimpleResult.createFactory(jsonObject);
		}
		// 当面邀请6.4
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_INVITATIONBYFACETOFACE: {
			return SimpleResult.createFactory(jsonObject);
		}
		// 根据会议ID获取会议议题聊天数据 6.11
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_DOGETMEETINGTOPICLIST: {
			return MMeetingTopicList.createFactory(jsonObject);
		}
		// 修改议题 6.9
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_UPDATETOPIC: {
			return jsonObject.optBoolean("succeed");
		}
		// 会议签到 6.16
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_SIGNINMEETING: {
			return SimpleResult.createFactory(jsonObject);
		}
		// 报名审核 6.17
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_SIGNUPREVIEW: {
			return SimpleResult.createFactory(jsonObject);
		}
		// 删除我创建的会议 6.13
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_DELETENOTBEGINMEETING: {
			return jsonObject.optBoolean("succeed");
		}
		// 归档删除我的会议 6.14
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_CHANGEMYMEMBERMEETSTATUS: {
			return SimpleResult.createFactory(jsonObject);
		}
		// 删除会议笔记 6.18
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_DELETENOTE: {
			return SimpleResult.createFactory(jsonObject);
		}
		// 查询会议笔记 6.19
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GETNOTEBYMEETINGID: {
			return MMeetingNoteObj.createFactory(jsonObject);
		}
		// 修改会议状态 6.20
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_CHANGEMEETINGSTATUS: {
			return SimpleResult.createFactory(jsonObject);
		}
		// 修改会议状态 6.21
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GETFILELISTBYTASKID: {
			return MListFileIndex.createFactory(jsonObject);
		}
		// 新建自定义会议标签 6.24
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_ADDMEETINGLABEL: {
			return SimpleResult.createFactory(jsonObject);
		}
		// 获取用户自定义标签列表 6.25
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GETMEETINGLABELBYCREATEID: {
			return MListMeetinglabel.createFactory(jsonObject);
		}
		// 修改会议 6.22
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_UPDATE: {
			return SimpleResult.createFactory(jsonObject);
		}
		// 保存会议笔记 6.28
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_SAVEMEETINGNOTE: {
			return SimpleResult.createFactory(jsonObject);
		}
		// 获取用户可转发的会议列表6.33
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GETMYFORWARDINGMEETING: {
			return MListMeetingQuery.createFactory(jsonObject);
		}
		// 收藏知识 3.10
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_COLLECTKNOWLEDGE: {
			return SimpleResult.createFactory(jsonObject);
		}
		// 关注需求 2.56
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_FOCUSREQUIREMENT: {
			return SimpleResult.createFactory(jsonObject);
		}
		// 搜索会议6.26
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GLOBALSEARCHBYMEETING: {
			return MListMeetingQuery.createFactory(jsonObject);
		}
		// 社交6.31
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GETMEETINGANDCHAT: {
			return ConferenceAndChat.createFactory(jsonObject);
		}
		// 新社交6.31
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GETMEETLIST: {
			return MListSociality.createFactory(jsonObject,new Object());
		}
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GETSOCIALLIST: {
			return MListSociality.createFactory(jsonObject,new Object());
		}
		// 获取我的转发列表
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GET_MY_FORWARDING_SOCIAL:{
			return MListSociality.createFactory(jsonObject,new Object());
		}
		// 转发会议前获取转发会议的详细信息接口
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GET_FORWARD_MEETING_DATA:{
			return MMeetingDetail.createFactory(jsonObject);
		}
		// 获取需求列表
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GETREQUIREMENTLIST: {
			JSONObject jsonPageObject = jsonObject.optJSONObject("page");
			if (null == jsonPageObject) {
				return null;
			}
			ArrayList<RequirementMini> arraylistRet = new ArrayList<RequirementMini>();
			if (!jsonPageObject.has("listRequirementMini")) {
				return null;
			}
			JSONArray joReqArry = null;
			try {
				joReqArry = jsonPageObject.getJSONArray("listRequirementMini");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (null == joReqArry) {
				return null;
			}

			int iArrSize = joReqArry.length();
			if (iArrSize <= 0) {
				return null;
			}
			RequirementMini aObject = null;
			JSONObject aObj = null;
			for (int i = 0; i < iArrSize; i++) {
				try {
					aObj = joReqArry.getJSONObject(i);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (null == aObj) {
					continue;
				}
				aObject = RequirementMini.createFactory(aObj);
				if (null == aObject) {
					continue;
				}

				arraylistRet.add(aObject);
			}
			return arraylistRet;
		}
		// 获取知识列表
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GETKNOWLEADGELIST: {
			JSONObject jsonPageObject = jsonObject.optJSONObject("page");
			if (null == jsonPageObject) {
				return null;
			}
			ArrayList<KnowledgeMini> arraylistRet = new ArrayList<KnowledgeMini>();
			if (!jsonPageObject.has("listKnowledgeMini")) {
				return null;
			}
			JSONArray joReqArry = null;
			try {
				joReqArry = jsonPageObject.getJSONArray("listKnowledgeMini");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (null == joReqArry) {
				return null;
			}

			int iArrSize = joReqArry.length();
			if (iArrSize <= 0) {
				return null;
			}
			KnowledgeMini aObject = null;
			JSONObject aObj = null;
			for (int i = 0; i < iArrSize; i++) {
				try {
					aObj = joReqArry.getJSONObject(i);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (null == aObj) {
					continue;
				}
				aObject = KnowledgeMini.createFactory(aObj);
				if (null == aObject) {
					continue;
				}

				arraylistRet.add(aObject);
			}
			return arraylistRet;
		}
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_SEND_MEETING_CHAT: // 发送会议消息
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GET_MEETING_MESSAGE: // 获取会议消息
			return MSendMessage.createFactory(jsonObject);
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_ADD_NOTE_DETAIL_BY_CHAT: // 会议畅聊添加会议笔记明细
			return jsonObject.optBoolean("succeed");
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_UPDATE_NOTICE_READSTATE: // 更新通知为已读状态
			return jsonObject.optBoolean("succeed");
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_DELETE_NOTICE: // 删除通知
			return jsonObject.optBoolean("succeed");
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_DELETE_INVITION_CARD: // 删除邀请函
			return jsonObject.optBoolean("succeed");
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_DELETE_MEETING_MEMBER: // 删除会议成员
			return jsonObject.optBoolean("succeed");
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_DELETE_SOCIAL: // 删除社交私聊群聊会议
			return jsonObject.optBoolean("succeed");
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_FINISH_TOPIC: // 结束议题
			return jsonObject.optBoolean("succeed");
		case EAPIConsts.ConferenceReqType.CONFERENCE_REQ_COMMUNITY_STATE: //获取畅聊状态
		{

			Gson gson = new Gson();
			String jsonStr = jsonObject.toString();
			try {
				if(jsonStr==null){
					return null;
				}
				List<CommunityStateResult> mCommunityStateResultList = new ArrayList<CommunityStateResult>(); // 返回数据
				if(jsonObject.has("responseData")){
					JSONArray connJsArr=jsonObject.getJSONArray("responseData");
					mCommunityStateResultList = gson.fromJson(connJsArr.toString(), new TypeToken<List<CommunityStateResult>>() {}.getType());
				}
				return mCommunityStateResultList;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		}
		return null;
	}
}
