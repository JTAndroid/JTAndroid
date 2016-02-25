package com.tr.api;

import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tr.model.conference.MMeetingMember;
import com.tr.model.conference.MMeetingNoteObj;
import com.tr.model.conference.MMeetingQuery;
import com.tr.model.conference.MMeetingRequiredSignupInfo;
import com.tr.model.conference.MMeetingTopic;
import com.tr.model.conference.MMeetingTopicQuery;
import com.tr.model.obj.MeetingMessage;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * 会议模式请求接口
 * 
 * @author gintong
 */
public class ConferenceReqUtil extends ReqBase {

	private static final String TAG = ConferenceReqUtil.class.getSimpleName();

	// 删除图片接口
	public static void doDeletePhoto(Context context, IBindData bind,
			String fileId, Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("fileId", fileId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.CONFERENCE_URL
				+ EAPIConsts.ReqUrl.hy_deletePhoto;
		doExecute(context, bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_DELETE_PHOTO, url,
				requestStr, handler);
	}

	// 进入会议通知接口
	public static void doEntryMeetingNotice(Context context, IBindData bind,
			String mid, String tid, Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("meetingId", mid);
			objIn.put("topicId", tid);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.CONFERENCE_URL
				+ EAPIConsts.ReqUrl.hy_doEntryMeetingNotice;
		doExecute(
				context,
				bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_doEntryMeetingNotice,
				url, requestStr, handler);
	}

	// 获取需求列表
	public static void getListRequirementByUserID(Context context,
			IBindData bind, String userID, int index, int size, Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("userID", userID);
			objIn.put("index", index);
			objIn.put("size", size);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.hy_getrequirement;
		doExecute(context, bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GETREQUIREMENTLIST,
				url, requestStr, handler);
	}

	public static void getListKnowleadgeByTypeAndKeywordByUserID(
			Context context, IBindData bind, String userID, int index,
			int size, Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("userID", userID);
			objIn.put("type", 3);
			objIn.put("index", index);
			objIn.put("size", size);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.hy_getknowleadge;

		doExecute(context, bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GETKNOWLEADGELIST,
				url, requestStr, handler);
	}

	// 创建会议6.3
	public static void doCreateMeeting(Context context, IBindData bind,
			MMeetingQuery aObj, Handler handler) {
		JSONObject objIn;
		try {
			objIn = aObj.toJSONObject();
			String requestStr = objIn.toString();
			String url = EAPIConsts.CONFERENCE_URL
					+ EAPIConsts.ReqUrl.hy_create_meeting;
			doExecute(context, bind,
					EAPIConsts.ConferenceReqType.CONFERENCE_REQ_CREATE_MEETING,
					url, requestStr, handler);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/** 获得我的会议首页列表6.1 */
	public static void doGetMeetingHomeList(Context context, IBindData bind,
			String memberId, Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("memberId", memberId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.CONFERENCE_URL
				+ EAPIConsts.ReqUrl.hy_meetingList;
		doExecute(context, bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_MEETING_HOME, url,
				requestStr, handler);
	}

	/** 获得我的邀请列表6.2 */
	public static void doGetMeetingInvitionList(Context context,
			IBindData bind, String memberId, Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("memberId", memberId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.CONFERENCE_URL
				+ EAPIConsts.ReqUrl.hy_getMyInvitation;
		doExecute(context, bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_MY_INVITION, url,
				requestStr, handler);
	}

	/** 获取会议通知列表6.27 */
	public static void doGetMeetingNoticeList(Context context, IBindData bind,
			String memberId, Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("memberId", memberId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.CONFERENCE_URL
				+ EAPIConsts.ReqUrl.hy_getNoticeList;
		doExecute(context, bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_MY_NOYICE, url,
				requestStr, handler);
	}

	/** 6.51 更新通知为已读状态 /notice/updateNoticeReadState.json */
	public static void doUpdateNoticeReadState(Context context, IBindData bind,
			long meetingId, long noticeId, Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("meetingId", meetingId);
			/** 只传noticeId就好 */
			objIn.put("noticeId", noticeId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.CONFERENCE_URL
				+ EAPIConsts.ReqUrl.hy_updateNoticeReadState;
		doExecute(
				context,
				bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_UPDATE_NOTICE_READSTATE,
				url, requestStr, handler);
	}

	/**
	 * 接受邀请、拒绝邀请、取消报名 6.6
	 * 
	 * @param context
	 * @param bind
	 * @param meetingId
	 * @param memberId
	 * @param type操作类型
	 *            0：未答复，1：接受邀请，2：拒绝邀请，5：取消报名，6：退出会议
	 * @param handler
	 */
	public static void doSetMeetingMemberReport(Context context,
			IBindData bind, long meetingId, String memberId, int type,
			Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("meetingId", meetingId);
			objIn.put("memberId", memberId);
			objIn.put("type", type);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.CONFERENCE_URL
				+ EAPIConsts.ReqUrl.hy_setMeetingMemberReport;
		doExecute(
				context,
				bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_MEETING_MEMBER_REPORT,
				url, requestStr, handler);
	}

	/** 获得会议广场列表6.23 */
	public static void doGetMeetingPlazaList(Context context, IBindData bind,
			String memberId, String time, String location, String industry,
			String position, String keyword, int index, int size,
			Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("memberId", memberId);
			objIn.put("time", time);
			objIn.put("location", location);
			objIn.put("industry", industry);
			objIn.put("position", position);

			objIn.put("client", "ios");
			objIn.put("keyword", keyword);

			objIn.put("index", index);
			objIn.put("size", size);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.CONFERENCE_URL
				+ EAPIConsts.ReqUrl.hy_getMeetingSquare;
		doExecute(context, bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_MEETING_PLAZA, url,
				requestStr, handler);
	}

	/**
	 * 获取会议详情 6.5
	 * 
	 * @author sunjianan
	 * @param context
	 *            上下文
	 * @param bind
	 *            实现IbindData的类
	 * @param id
	 *            id
	 * @param memberId
	 *            成员id
	 * @param handler
	 */
	public static void doGetMeetingDetail(Context context, IBindData bind,
			long id, String memberId, Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("id", id);
			objIn.put("memberId", memberId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.CONFERENCE_URL
				+ EAPIConsts.ReqUrl.hy_GET_MEETING_DETAIL_BY_ID;
		doExecute(context, bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_MEETING_DETAIL,
				url, requestStr, handler);
	}

	/**
	 * 报名6.7
	 * 
	 * @author sunjianan
	 * @param context
	 * @param bind
	 * @param meetingId
	 * @param memberId
	 * @param memberName
	 * @param memberPhoto
	 * @param handler
	 */
	public static void doGetSignUpMeeting(Context context, IBindData bind,
			long meetingId, String memberId, String memberName,
			String memberPhoto, Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("meetingId", meetingId);
			objIn.put("memberId", memberId);
			objIn.put("memberName", memberName);
			objIn.put("memberPhoto", memberPhoto);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.CONFERENCE_URL
				+ EAPIConsts.ReqUrl.hy_SING_UP_MEETING;
		doExecute(context, bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_SIGN_UP_MEETING,
				url, requestStr, handler);
	}

	/**
	 * 获取我的会议6.15
	 * 
	 * @author sunjianan
	 * @param context
	 *            上下文
	 * @param bind
	 *            实现IBindData的对象
	 * @param memberId
	 * @param year
	 * @param month
	 * @param handler
	 */
	public static void doGetMyMeeting(Context context, IBindData bind,
			String memberId, String year, String month, Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("memberId", memberId);
			objIn.put("year", year);
			objIn.put("month", month);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.CONFERENCE_URL
				+ EAPIConsts.ReqUrl.hy_MY_MEETING;
		doExecute(context, bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_MY_MEETING, url,
				requestStr, handler);
	}

	/**
	 * 新版会议——获取我的会议
	 * 
	 * @author zhongshan
	 * @param context
	 * @param bind
	 * @param memberId
	 * @param index
	 * @param size
	 * @param keyword
	 * @param type
	 *            "type":"0：全查 1：我的会议 2：人脉 3：组织 4：知识 5：事件 6：笔记"
	 * @param handler
	 */
	public static void doNewGetMyMeeting(Context context, IBindData bind,
			String memberId, int index, int size, String keyword, int type,
			Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("memberId", memberId);
			objIn.put("client", "ios");
			objIn.put("index", index);
			objIn.put("size", size);
			objIn.put("keyword", keyword);
			objIn.put("type", type);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.CONFERENCE_URL
				+ EAPIConsts.ReqUrl.hy_MY_MEETING;
		doExecute(context, bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_MY_MEETING, url,
				requestStr, handler);
	}

	/**
	 * 获取会议必填信息6.31
	 * 
	 * @author sunjianan
	 * @param context
	 * @param bind
	 *            实现IBindData的对象
	 * @param meetingId
	 *            会议Id
	 * @param handler
	 */
	public static void doGetRequiredSignupInfo(Context context, IBindData bind,
			long meetingId, Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("meetingId", meetingId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.CONFERENCE_URL
				+ EAPIConsts.ReqUrl.hy_RequiredSignupInfo;
		doExecute(context, bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_REQUIREDSIGNUPINFO,
				url, requestStr, handler);
	}

	/**
	 * 完善报名信息6.29
	 * 
	 * @author sunjianan
	 * @param context
	 * @param bind
	 * @param MMeetingRequiredSignupInfo
	 * @param handler
	 */
	public static void doImproveSignInformation(Context context,
			IBindData bind, MMeetingRequiredSignupInfo signInfoResult,
			Handler handler) {
		/*
		 * ValueFilter filter = new ValueFilter() {
		 * 
		 * @Override public Object process(Object obj, String s, Object v) { if
		 * (v == null || v instanceof String && StringUtils.isEmpty((String) v))
		 * { return null; } return v; } }; String jsonStr =
		 * JSON.toJSONString(signInfoResult, filter);
		 */
		String jsonStr = getJsonStr(signInfoResult);
		String url = EAPIConsts.CONFERENCE_URL
				+ EAPIConsts.ReqUrl.hy_improveSignInformation;
		doExecute(
				context,
				bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_IMPROVESIGNINFORMATION,
				url, jsonStr, handler);
	}

	private static String getJsonStr(Object jsonStr) {
		Gson gson = new Gson();
		/* 这里将javabean转化成json字符串 */
		return gson.toJson(jsonStr);
	}

	/**
	 * 当面邀请6.4 参数皆为MeetingMember中字段
	 * 
	 * @author sunjianan
	 * @param context
	 * @param bind
	 * @param attendMeetStatus
	 * @param attendMeetType
	 * @param meetingId
	 * @param memberId
	 * @param memberMeetStatus
	 * @param memberName
	 * @param memberPhoto
	 * @param memberType
	 * @param handler
	 * 
	 * @return tag:
	 *         EAPIConsts.ConferenceReqType.CONFERENCE_REQ_INVITATIONBYFACETOFACE
	 *         object: SimpleResult
	 */
	public static void doInvitationFaceToFace(Context context, IBindData bind,
			MMeetingMember aObj, Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("attendMeetStatus", aObj.getAttendMeetStatus());
			objIn.put("attendMeetType", aObj.getAttendMeetType());
			objIn.put("meetingId", aObj.getMeetingId());
			objIn.put("memberId", aObj.getMemberId());
			objIn.put("memberMeetStatus", aObj.getMemberMeetStatus());
			objIn.put("memberName", aObj.getMemberName());
			objIn.put("memberPhoto", aObj.getMemberPhoto());
			objIn.put("memberType", aObj.getMemberType());

			// objIn.put("isSign", aObj.getIsSign());
			// objIn.put("signDistance", aObj.getSignDistance());
			// objIn.put("excuteMeetSign", aObj.getExcuteMeetSign());

			String requestStr = objIn.toString();
			String url = EAPIConsts.CONFERENCE_URL
					+ EAPIConsts.ReqUrl.hy_invitationByFaceToFace;
			doExecute(
					context,
					bind,
					EAPIConsts.ConferenceReqType.CONFERENCE_REQ_INVITATIONBYFACETOFACE,
					url, requestStr, handler);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据会议ID获取会议议题聊天数据 6.11
	 * 
	 * @anthur sunjianan
	 * @param context
	 * @param bind
	 * @param meetingId
	 * @param handler
	 * 
	 * @return tag:
	 *         EAPIConsts.ConferenceReqType.CONFERENCE_REQ_DOGETMEETINGTOPICLIST
	 *         object: MMeetingTopicList
	 */
	/*
	 * public static void doGetMeetingTopicList(Context context, IBindData bind,
	 * long meetingId, Handler handler) { JSONObject objIn = new JSONObject();
	 * try { objIn.put("meetingId", meetingId); } catch (JSONException e) {
	 * e.printStackTrace(); } String requestStr = objIn.toString(); String url =
	 * EAPIConsts.CONFERENCE_URL + EAPIConsts.ReqUrl.hy_getMeetingTopicListById;
	 * doExecute(context, bind,
	 * EAPIConsts.ConferenceReqType.CONFERENCE_REQ_DOGETMEETINGTOPICLIST, url,
	 * requestStr, handler); }
	 */

	/**
	 * 修改议题
	 * 
	 * @param context
	 * @param bind
	 * @param meetingTopicQuery
	 * @param handler
	 */
	public static void doUpdateTopic(Context context, IBindData bind,
			MMeetingTopicQuery meetingTopicQuery, Handler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject = meetingTopicQuery.toJSONObject();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String jobjStr = jsonObject.toString();
		String url = EAPIConsts.CONFERENCE_URL
				+ EAPIConsts.ReqUrl.hy_updateTopic;
		doExecute(context, bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_UPDATETOPIC, url,
				jobjStr, handler);
	}

	/**
	 * 会议签到 6.16
	 * 
	 * @author sunjianan
	 * @param context
	 * @param bind
	 * @param meetingId
	 * @param memberId
	 * @param signDistance
	 * @param handler
	 * @return tag: EAPIConsts.ConferenceReqType.CONFERENCE_REQ_SIGNINMEETING
	 *         object: SimpleResult
	 */
	public static void doSignInMeeting(Context context, IBindData bind,
			long meetingId, String memberId, String signDistance,
			Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("meetingId", meetingId);
			objIn.put("memberId", memberId);
			objIn.put("signDistance", signDistance);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.CONFERENCE_URL
				+ EAPIConsts.ReqUrl.hy_signInMeeting;
		doExecute(context, bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_SIGNINMEETING, url,
				requestStr, handler);
	}

	/**
	 * 报名审核 6.17
	 * 
	 * @author sunjianan
	 * @param context
	 * @param bind
	 * @param meetingId
	 * @param memberId
	 * @param reviewStatus
	 * @param handler
	 * @return tag: EAPIConsts.ConferenceReqType.CONFERENCE_REQ_SIGNUPREVIEW
	 *         object: SimpleResult
	 */
	public static void doSignUpReview(Context context, IBindData bind,
			long meetingId, long memberId, int reviewStatus, Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("meetingId", meetingId);
			objIn.put("memberId", memberId);
			objIn.put("reviewStatus", reviewStatus);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.CONFERENCE_URL
				+ EAPIConsts.ReqUrl.hy_signUpReview;
		doExecute(context, bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_SIGNUPREVIEW, url,
				requestStr, handler);
	}

	/**
	 * 删除我创建的会议 6.13（2015.9.20：修改接口支持删除我保存的会议）
	 * 
	 * @param context
	 * @param bind
	 * @param meetingId meetingId 会议ID，多个用逗号隔开
	 * @param memberId
	 * @param handler
	 * @return tag:
	 *         EAPIConsts.ConferenceReqType.CONFERENCE_REQ_DELETENOTBEGINMEETING
	 *         object: SimpleResult
	 */
	public static void doDeleteNotBeginMeeting(Context context, IBindData bind,
			long meetingId, long memberId, Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("meetingId", meetingId);
			objIn.put("memberId", memberId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.CONFERENCE_URL
				+ EAPIConsts.ReqUrl.hy_deleteNotBeginMeeting;
		doExecute(
				context,
				bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_DELETENOTBEGINMEETING,
				url, requestStr, handler);
	}

	/**
	 * 归档删除我的会议 6.14
	 * 
	 * @author sunjianan
	 * @param context
	 * @param bind
	 * @param meetingId
	 * @param memberId
	 * @param type
	 * @param handler
	 * @return tag:
	 *         EAPIConsts.ConferenceReqType.CONFERENCE_REQ_CHANGEMYMEMBERMEETSTATUS
	 *         object: SimpleResult
	 */
	public static void doChangeMyMemberMeetStatus(Context context,
			IBindData bind, long meetingId, long memberId, int type,
			Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("meetingId", meetingId);
			objIn.put("memberId", memberId);
			objIn.put("type", type);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.CONFERENCE_URL
				+ EAPIConsts.ReqUrl.hy_changeMyMemberMeetStatus;
		doExecute(
				context,
				bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_CHANGEMYMEMBERMEETSTATUS,
				url, requestStr, handler);
	}

	/**
	 * 删除会议笔记 6.18
	 * 
	 * @author sunjianan
	 * @param context
	 * @param bind
	 * @param noteId
	 * @param handler
	 * @return tag: EAPIConsts.ConferenceReqType.CONFERENCE_REQ_DELETENOTE
	 *         object: SimpleResult
	 */
	public static void doDeleteNote(Context context, IBindData bind,
			long noteId, Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("noteId", noteId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.CONFERENCE_URL
				+ EAPIConsts.ReqUrl.hy_deleteNote;
		doExecute(context, bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_DELETENOTE, url,
				requestStr, handler);
	}

	/**
	 * 查询会议笔记 6.32
	 * 
	 * @author sunjianan
	 * @param context
	 * @param bind
	 * @param noteId
	 * @param handler
	 * @return tag:
	 *         EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GETNOTEBYMEETINGID
	 *         object: MMeetingNote
	 */
	public static void doGetNoteByMeetingId(Context context, IBindData bind,
			long meetingId, Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("meetingId", meetingId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.CONFERENCE_URL
				+ EAPIConsts.ReqUrl.hy_getNoteByMeetingId;
		doExecute(context, bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GETNOTEBYMEETINGID,
				url, requestStr, handler);
	}

	/**
	 * 修改会议状态 6.20
	 * 
	 * @author sunjianan
	 * @param context
	 * @param bind
	 * @param meetingId
	 * @param meetingStatus
	 * @param handler
	 * @return tag:
	 *         EAPIConsts.ConferenceReqType.CONFERENCE_REQ_CHANGEMEETINGSTATUS
	 *         object: SimpleResult
	 */
	public static void doChangeMeetingStatus(Context context, IBindData bind,
			long meetingId, int meetingStatus, Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("meetingId", meetingId);
			objIn.put("meetingStatus", meetingStatus);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.CONFERENCE_URL
				+ EAPIConsts.ReqUrl.hy_changeMeetingStatus;
		doExecute(
				context,
				bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_CHANGEMEETINGSTATUS,
				url, requestStr, handler);
	}

	/**
	 * 获取上传文件接口6.21
	 * 
	 * @author sunjianan
	 * @param context
	 * @param bind
	 * @param taskId
	 * @param handler
	 * @return tag:
	 *         EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GETFILELISTBYTASKID
	 *         object: MListFileIndex
	 */
	/*
	 * public static void doGetFileListByTaskId(Context context, IBindData bind,
	 * String taskId, Handler handler) { JSONObject objIn = new JSONObject();
	 * try { objIn.put("taskId", taskId); } catch (JSONException e) {
	 * e.printStackTrace(); } String requestStr = objIn.toString(); String url =
	 * EAPIConsts.CONFERENCE_URL + EAPIConsts.ReqUrl.hy_getFileListByTaskId;
	 * doExecute(context, bind,
	 * EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GETFILELISTBYTASKID, url,
	 * requestStr, handler); }
	 */

	/**
	 * 新建自定义会议标签6.24
	 * 
	 * @author sunjianan
	 * @param context
	 * @param bind
	 * @param labelName
	 * @param createId
	 * @param createName
	 * @param createTime
	 * @param handler
	 * @return tag: EAPIConsts.ConferenceReqType.CONFERENCE_REQ_ADDMEETINGLABEL
	 *         object: SimpleResult
	 */
	public static void doAddMeetingLabel(Context context, IBindData bind,
			String labelName, long createId, String createName,
			String createTime, Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("labelName", labelName);
			objIn.put("createId", createId);
			objIn.put("createName", createName);
			objIn.put("createTime", createTime);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.CONFERENCE_URL
				+ EAPIConsts.ReqUrl.hy_addMeetingLabel;
		doExecute(context, bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_ADDMEETINGLABEL,
				url, requestStr, handler);
	}

	/**
	 * 获取用户自定义标签列表 6.25
	 * 
	 * @author sunjianan
	 * @param context
	 * @param bind
	 * @param memberId
	 * @param handler
	 * @return tag:
	 *         EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GETMEETINGLABELBYCREATEID
	 *         object: MListMeetinglabel
	 */
	public static void doGetMeetingLabelByCreateId(Context context,
			IBindData bind, long memberId, Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("memberId", memberId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.CONFERENCE_URL
				+ EAPIConsts.ReqUrl.hy_getMeetingLabelByCreateId;
		doExecute(
				context,
				bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GETMEETINGLABELBYCREATEID,
				url, requestStr, handler);
	}

	/**
	 * 修改会议 6.22 --> 未测试
	 * 
	 * @author sunjianan
	 * @param context
	 * @param bind
	 * @param MMeetingDetail
	 * @param handler
	 * @return tag: EAPIConsts.ConferenceReqType.CONFERENCE_REQ_UPDATE object:
	 *         SimpleResult
	 */
	public static void doUpdate(Context context, IBindData bind,
			MMeetingQuery aObj, Handler handler) {
		JSONObject objIn;
		try {
			objIn = aObj.toJSONObject();
			String requestStr = objIn.toString();
			String url = EAPIConsts.CONFERENCE_URL
					+ EAPIConsts.ReqUrl.hy_update;
			doExecute(context, bind,
					EAPIConsts.ConferenceReqType.CONFERENCE_REQ_UPDATE, url,
					requestStr, handler);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存会议笔记 6.28 -- 未测试
	 * 
	 * @author sunjianan
	 * @param context
	 * @param bind
	 * @param MMeetingNoteObj
	 * @param handler
	 * @return tag: EAPIConsts.ConferenceReqType.CONFERENCE_REQ_SAVEMEETINGNOTE
	 *         object: SimpleResult
	 */
	public static void doSaveMeetingNote(Context context, IBindData bind,
			MMeetingNoteObj signInfoResult, Handler handler) {
		/*
		 * ValueFilter filter = new ValueFilter() {
		 * 
		 * @Override public Object process(Object obj, String s, Object v) { if
		 * (v == null || v instanceof String && StringUtils.isEmpty((String) v))
		 * { return null; } return v; } }; String jsonStr =
		 * JSON.toJSONString(signInfoResult, filter);
		 */

		try {
			JSONObject obj = new JSONObject(getJsonStr(signInfoResult));
			if (null != obj) {
				JSONObject respObj = obj.optJSONObject("meetingNoteQuery");
				if (null != respObj) {
					String url = EAPIConsts.CONFERENCE_URL
							+ EAPIConsts.ReqUrl.hy_saveMeetingNote;
					System.out.println("save meeting note request json:"
							+ respObj.toString());
					// System.out.println("save meeting note request  url:" +
					// url);
					doExecute(
							context,
							bind,
							EAPIConsts.ConferenceReqType.CONFERENCE_REQ_SAVEMEETINGNOTE,
							url, respObj.toString(), handler);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送会议消息
	 * 
	 * @param context
	 * @param bindData
	 * @param msg
	 * @param handler
	 */
	public static void doSendMeetingChat(Context context, IBindData bindData,
			MeetingMessage msg, Handler handler) {

		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("mucID", msg.getRecvID());
			jsonObj.put("topicId", msg.getTopicID());
			jsonObj.put("type", msg.getType());
			jsonObj.put("text", msg.getContent());
			jsonObj.put("senderName", msg.getRecvID());
			if (msg.getJtFile() != null) {
				JSONObject jsonJtFile = msg.getJtFile().toJson();
				jsonObj.put("jtFile", jsonJtFile);
			}
			jsonObj.put("messageID", msg.getMessageID());
			jsonObj.put("fromTime", msg.getTime());
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String requestStr = jsonObj.toString();
		String url = EAPIConsts.getIMUrl()
				+ EAPIConsts.ReqUrl.hy_SendMeetingChat;
		doExecute(context, bindData,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_SEND_MEETING_CHAT,
				url, requestStr, handler);
	}

	/**
	 * 获取会议聊天记录
	 * 
	 * @param context
	 * @param bindData
	 * @param id
	 * @param topicId
	 * @param fromTime
	 * @param isBackward
	 */
	public static void doGetMeetingMessage(Context context, IBindData bindData,
			long id, long topicId, String fromTime, boolean isBackward,
			Handler handler) {
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("id", id);
			jsonObj.put("topicId", topicId);
			jsonObj.put("fromTime", fromTime);
			jsonObj.put("isBackward", isBackward);
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String requestStr = jsonObj.toString();
		String url = EAPIConsts.getIMUrl()
				+ EAPIConsts.ReqUrl.hy_GetMeetingMessage;
		doExecute(
				context,
				bindData,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GET_MEETING_MESSAGE,
				url, requestStr, handler);
	}

	/**
	 * 获取用户可转发的会议列表6.33
	 * 
	 * @author sunjianan
	 * @param context
	 * @param bind
	 * @param handler
	 * @return tag: EAPIConsts.ConferenceReqType.CONFERENCE_REQ_SAVEMEETINGNOTE
	 *         object : MListMeetingQuery
	 */
	public static void doGetMyForwardingMeeting(Context context,
			IBindData bind, Handler handler) {
		JSONObject objIn = new JSONObject();
		String requestStr = objIn.toString();
		String url = EAPIConsts.CONFERENCE_URL
				+ EAPIConsts.ReqUrl.hy_getMyForwardingMeeting;
		doExecute(
				context,
				bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GETMYFORWARDINGMEETING,
				url, requestStr, handler);
	}

	/**
	 * 收藏知识 3.10
	 * 
	 * @param context
	 * @param bind
	 * @param id
	 * @param type
	 * @param handler
	 *            tag:
	 *            EAPIConsts.ConferenceReqType.CONFERENCE_REQ_COLLECTKNOWLEDGE
	 *            object: SimpleResult
	 */
	public static void doCollectKnowledge(Context context, IBindData bind,
			long id, int type, boolean isCancelled, Handler handler) {
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("id", id);
			jsonObj.put("type", type);
			jsonObj.put("isCancelled", isCancelled);
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String requestStr = jsonObj.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.hy_collectKnowledge;
		doExecute(context, bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_COLLECTKNOWLEDGE,
				url, requestStr, handler);
	}

	/**
	 * 关注需求 2.56
	 * 
	 * @param context
	 * @param bind
	 * @param id
	 * @param projectType
	 * @param handler
	 *            tag:
	 *            EAPIConsts.ConferenceReqType.CONFERENCE_REQ_FOCUSREQUIREMENT
	 *            object: SimpleResult
	 */
	public static void doFocusRequirement(Context context, IBindData bind,
			long requirementID, Handler handler) {
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("requirementID", requirementID);
			jsonObj.put("isFocus", true);
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String requestStr = jsonObj.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.hy_focusRequirement;
		doExecute(context, bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_FOCUSREQUIREMENT,
				url, requestStr, handler);
	}

	/**
	 * 会议畅聊添加会议笔记明细
	 * 
	 * @param context
	 * @param bind
	 * @param meetingId
	 * @param messageId
	 * @param handler
	 */
	public static void doAddNoteDetailByChat(Context context, IBindData bind,
			long meetingId, String messageId, String taskId, Handler handler) {
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("meetingId", meetingId);
			jsonObj.put("messageId", messageId);
			jsonObj.put("taskId", taskId);
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String requestStr = jsonObj.toString();
		String url = EAPIConsts.getConferenceUrl()
				+ EAPIConsts.ReqUrl.hy_addNoteDetailByChat;
		doExecute(
				context,
				bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_ADD_NOTE_DETAIL_BY_CHAT,
				url, requestStr, handler);
	}

	public static void doGlobalSearchByMeeting(Context context, IBindData bind,
			String keyword, Handler handler) {
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("keyword", keyword);
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String requestStr = jsonObj.toString();
		String url = EAPIConsts.getConferenceUrl()
				+ EAPIConsts.ReqUrl.hy_globalSearchByMeeting;
		doExecute(
				context,
				bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GLOBALSEARCHBYMEETING,
				url, requestStr, handler);
	}

	/**
	 * 社交列表
	 * 
	 * @param context
	 * @param bind
	 * @param userId
	 * @param handler
	 */
	public static void doGetConferenceAndChat(Context context, IBindData bind,
			long userId, Handler handler) {
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("user_id", userId);
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String requestStr = jsonObj.toString();
		String url = EAPIConsts.getConferenceUrl()
				+ EAPIConsts.ReqUrl.hy_getMeetingAndChat;
		doExecute(context, bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GETMEETINGANDCHAT,
				url, requestStr, handler);
	}

	/**
	 * 新社交列表
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 */
	public static void doGetSocialList(Context context, IBindData bind,
			Handler handler) {
		JSONObject jsonObj = new JSONObject();
		try {
	        /*"withNewRelation":"true：同步查询出去新的关系 false：不查询新的关系"*/
			jsonObj.put("withNewRelation", "true");
			String requestStr = jsonObj.toString();
			String url = EAPIConsts.getConferenceUrl()
					+ EAPIConsts.ReqUrl.hy_doGetSocialList2;
			doExecute(context, bind,
					EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GETSOCIALLIST,
					url, requestStr, handler);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 新社交列表
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @throws JSONException
	 */
	public static void doGetSocialList(Context context, IBindData bind,
			Handler handler, int index, int size) {
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("index", index);
			jsonObj.put("size", size);
			String requestStr = jsonObj.toString();
			String url = EAPIConsts.getConferenceUrl()
					+ EAPIConsts.ReqUrl.hy_doGetSocialList3;
			doExecute(context, bind,
					EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GETSOCIALLIST,
					url, requestStr, handler);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 社交会议列表
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 */
	public static void doGetSocialMeetList(Context context, IBindData bind,
			Handler handler) {
		JSONObject jsonObj = new JSONObject();
		String requestStr = jsonObj.toString();
		String url = EAPIConsts.getConferenceUrl()
				+ EAPIConsts.ReqUrl.hy_doGetMeetList;
		// String url = "http://192.168.120.144:8080//meeting/meetingList.json";
		doExecute(context, bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GETMEETLIST, url,
				requestStr, handler);
	}

	/**
	 * 在社交列表中移除单聊、群聊、会议
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 */
	public static void doRemoveSocial(Context context, IBindData bind,
			long mucId, long topicId, int type, long userId, String createTime,
			Handler handler) {
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("mucId", mucId);
			jsonObj.put("topicId", topicId);
			jsonObj.put("type", type);
			jsonObj.put("userId", userId);
			jsonObj.put("createdTime", createTime);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = jsonObj.toString();

		String url = EAPIConsts.getConferenceUrl()
				+ EAPIConsts.ReqUrl.hy_removeSocial;
		doExecute(context, bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_DELETE_SOCIAL, url,
				requestStr, handler);
	}

	/**
	 * 获取我的转发社交列表
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 */
	public static void getMyForwardingSocial(Context context, IBindData bind,
			Handler handler) {
		String url = EAPIConsts.getConferenceUrl()
				+ EAPIConsts.ReqUrl.GetMyForwardingSocial;
		doExecute(
				context,
				bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GET_MY_FORWARDING_SOCIAL,
				url, "", handler);
	}

	/**
	 * 获取会议详情
	 * 
	 * @param context
	 * @param bind
	 * @param meetingId
	 * @param topicId
	 * @param handler
	 */
	public static void getForwardingMeetingData(Context context,
			IBindData bind, long meetingId, long topicId, Handler handler) {
		JSONObject jsongObject = new JSONObject();
		try {
			jsongObject.put("meetingId", meetingId);
			jsongObject.put("topicId", topicId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = jsongObject.toString();
		String url = EAPIConsts.getConferenceUrl()
				+ EAPIConsts.ReqUrl.GetForwardMeetingData;
		doExecute(
				context,
				bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_GET_FORWARD_MEETING_DATA,
				url, requestStr, handler);
	}

	/**
	 * 6.49 删除通知 /notice/deleteNotice.json
	 * 
	 * @param context
	 * @param bind
	 * @param meetingId
	 * @param noticeId
	 *            （可以不传）
	 * @param handler
	 */
	public static void doDeleteNotice(Context context, IBindData bind,
			String meetingId, long noticeId, Handler handler) {
		JSONObject jsongObject = new JSONObject();
		try {
			jsongObject.put("meetingId", meetingId);
			jsongObject.put("noticeId", noticeId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = jsongObject.toString();
		String url = EAPIConsts.getConferenceUrl()
				+ EAPIConsts.ReqUrl.deleteNotice;
		doExecute(context, bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_DELETE_NOTICE, url,
				requestStr, handler);
	}

	/**
	 * 6.49 删除邀请函 /notice/deleteNotice.json
	 * 
	 * @param context
	 * @param bind
	 * @param meetingId
	 * @param memberId
	 * @param handler
	 */
	public static void doDeleteMyInvitation(Context context, IBindData bind,
			String meetingId, long memberId, Handler handler) {
		JSONObject jsongObject = new JSONObject();
		try {
			jsongObject.put("meetingId", meetingId);
			jsongObject.put("memberId", memberId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = jsongObject.toString();
		String url = EAPIConsts.getConferenceUrl()
				+ EAPIConsts.ReqUrl.deleteMyInvitation;
		doExecute(
				context,
				bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_DELETE_INVITION_CARD,
				url, requestStr, handler);
	}

	/**
	 * 6.53 删除会议成员 /member/deleteMeetingMember.json
	 * 
	 * @param context
	 * @param bind
	 * @param meetingId
	 * @param memberId
	 * @param handler
	 */
	public static void doDeleteMeetingMember(Context context, IBindData bind,
			long meetingId, long memberId, Handler handler) {
		JSONObject jsongObject = new JSONObject();
		try {
			jsongObject.put("meetingId", meetingId);
			jsongObject.put("memberId", memberId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = jsongObject.toString();
		String url = EAPIConsts.getConferenceUrl()
				+ EAPIConsts.ReqUrl.deleteMeetingMember;
		doExecute(
				context,
				bind,
				EAPIConsts.ConferenceReqType.CONFERENCE_REQ_DELETE_MEETING_MEMBER,
				url, requestStr, handler);
	}
	/**
	 * 6.62 结束议题 /topic/finishTopic.json
	 * @param context
	 * @param bind
	 * @param topicId  是议题ID，多个用逗号隔开
	 * @param handler
	 */
	public static void doFinishTopic(Context context ,IBindData bind,long topicId,Handler handler){
		JSONObject jsongObject = new JSONObject();
		try {
			jsongObject.put("topicId", topicId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = jsongObject.toString();
		String url = EAPIConsts.getConferenceUrl() + EAPIConsts.ReqUrl.finishMeetingTopic;
		doExecute(context, bind, EAPIConsts.ConferenceReqType.CONFERENCE_REQ_FINISH_TOPIC, url, requestStr, handler);
	}
	
	/**
	 * 获取社群状态
	 * @param context
	 * @param bind
	 * @param topicId
	 * @param handler
	 * @param userId
	 */
	public static void getCommunityState(Context context ,IBindData bind,Handler handler,String userId){
		try {
			JSONObject jsongObject = new JSONObject();
			String requestStr = jsongObject.toString();
			String url = EAPIConsts.getConferenceUrl() + "/meeting/getCommunityNewCountByUserId/"+userId;
			doExecute(context, bind, EAPIConsts.ConferenceReqType.CONFERENCE_REQ_COMMUNITY_STATE, url,requestStr, handler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
