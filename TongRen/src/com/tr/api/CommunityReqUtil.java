package com.tr.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tr.App;
import com.tr.model.demand.ASSOData;
import com.tr.model.demand.ASSORPOK;
import com.tr.model.joint.ResourceNode;
import com.tr.model.joint.ResourceNodeMini;
import com.tr.model.obj.IMBaseMessage;
import com.tr.ui.communities.model.Community;
import com.tr.ui.communities.model.CommunityKickFromForBatchRequest;
import com.tr.ui.communities.model.CommunityNotify;
import com.tr.ui.communities.model.CreateSet;
import com.tr.ui.communities.model.Label;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * 社群模式请求接口
 * 
 * @author gintong
 */
public class CommunityReqUtil extends ReqBase {

	private static final String TAG = CommunityReqUtil.class.getSimpleName();

	/**
	 * @param index分页参数从1开始
	 * @param size
	 *            分页一次返回的数目 获取社群列表的请求
	 * @param labelid
	 *            标签id
	 */
	public static void doGetCommunityList(Context context, int index, int size, String labelid, IBindData bind, Handler handler) {
		Gson gson = new Gson();
		// 对于对象转json时
		// ，先把对象传到map中，直接Gson.toJson（对象）会把这个对象直接转为String，即json中key对应的值会带双引号，造成后台解析错误
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("index", index);
		params.put("size", size);
		if (!TextUtils.isEmpty(labelid))
			params.put("labelid", labelid);
		String requestStr = gson.toJson(params);
		String url = EAPIConsts.COMMUNITY_URL + EAPIConsts.CommunityReqUrl.MAIN_COMMUNITY_LIST;
		doExecute(context, bind, EAPIConsts.CommunityReqType.TYPE_MAIN_COMMUNITY_LIST, url, requestStr, handler);
	}

	/**
	 * @param index分页参数从1开始
	 * @param size
	 *            分页一次返回的数目
	 * @param userid
	 *            用户id 获取我的社群列表的请求
	 */
	public static void doGetMyCommunityList(Context context, long userid, int index, int size, IBindData bind, Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("userid", userid);
			objIn.put("index", index);
			objIn.put("size", size);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.COMMUNITY_URL + EAPIConsts.CommunityReqUrl.MY_COMMUNITY_LIST;
		doExecute(context, bind, EAPIConsts.CommunityReqType.TYPE_MY_COMMUNITY_LIST, url, requestStr, handler);
	}

	/**
	 * @param 创建社群的请求
	 */
	public static void doCreateCommunity(Context context, Community community, ASSORPOK asso, List<Long> labels, CreateSet set, IBindData bind, Handler handler) {
		Gson gson = new Gson();
		// 对于对象转json时
		// ，先把对象传到map中，直接Gson.toJson（对象）会把这个对象直接转为String，即json中key对应的值会带双引号，造成后台解析错误
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("community", community);
		params.put("asso", asso);
		params.put("labels", labels);
		params.put("set", set);
		String requestStr = gson.toJson(params);
		String url = EAPIConsts.COMMUNITY_URL + EAPIConsts.CommunityReqUrl.CREATE_COMMUNITY;
		doExecute(context, bind, EAPIConsts.CommunityReqType.TYPE_CREATE_COMMUNITY, url, requestStr, handler);
	}

	/**
	 * @param id
	 *            用户id
	 * @param personType
	 *            类型 1为用户 创建社群的先决条件
	 */
	public static void doGetPrecondition(Context context, long id, int personType, IBindData bind, Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("id", id);
			objIn.put("personType", personType);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.COMMUNITY_URL + EAPIConsts.CommunityReqUrl.GET_PRECONDITION;
		doExecute(context, bind, EAPIConsts.CommunityReqType.TYPE_GET_PRECONDITION, url, requestStr, handler);
	}

	/**
	 * 发布时间
	 * 
	 * 因为后台说此接口为畅聊接口，所以要访问http://192.168.101.131:2244
	 */
	public static void doGetMessageTime(Context context, String mucId, IBindData bind, Handler handler) {
		JSONObject objIn = new JSONObject();
		String requestStr = objIn.toString();
		String url = EAPIConsts.COMMUNITY_IM_URL + EAPIConsts.CommunityReqUrl.ORDER_USER_BY_TIME + mucId;
		doExecute(context, bind, EAPIConsts.CommunityReqType.TYPE_ORDER_USER_BY_TIME, url, requestStr, handler);
	}

	/**
	 * "userId":"用户id，必传，不可为空", "mucId":"社群Id，必传，不可为空", “talkstatus”:“是否被禁言 1否
	 * 2是 必传，不可为空” 为用户 管理群成员-禁言（单）
	 */
	public static void doGetNoTalk(Context context, String userId, String mucId, String talkstatus, IBindData bind, Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("userId", userId);
			objIn.put("mucId", mucId);
			objIn.put("talkstatus", talkstatus);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.COMMUNITY_URL + EAPIConsts.CommunityReqUrl.MANAGE_COMMUNITY;
		doExecute(context, bind, EAPIConsts.CommunityReqType.TYPE_MANAGE_COMMUNITY, url, requestStr, handler);
	}

	/**
	 * "userId":"用户id，必传，不可为空", "mucId":"社群Id，必传，不可为空", “talkstatus”:“是否被禁言 1否
	 * 2是 必传，不可为空” 为用户 管理群成员-禁言（批量）
	 */
	public static void doGetNoTalkBatck(Context context, String userId, String mucId, String talkstatus, IBindData bind, Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("userid", userId);
			objIn.put("mucid", mucId);
			objIn.put("talkstatus", talkstatus);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.COMMUNITY_URL + EAPIConsts.CommunityReqUrl.MANAGE_COMMUNITY_BATCH;
		doExecute(context, bind, EAPIConsts.CommunityReqType.TYPE_MANAGE_COMMUNITY_BATCH, url, requestStr, handler);
	}

	/**
	 * "id":"社群Id，必传，不可为空", "ownerId":"用户id，必传，不可为空", 为用户 管理群成员-转让（单）
	 */
	public static void doAssignmentCommunity(Context context, String id, String ownerId, IBindData bind, Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("id", id);
			objIn.put("ownerId", ownerId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.COMMUNITY_URL + EAPIConsts.CommunityReqUrl.ASSIGNMENT_COMUNITY;
		doExecute(context, bind, EAPIConsts.CommunityReqType.TYPE_ASSIGNMENT_COMUNITY, url, requestStr, handler);
	}

	/**
	 * "userID":"用户id，必传，不可为空", "mucID":"社群Id，必传，不可为空", 为用户 管理群成员-移除（单）
	 */
	public static void doKickFromMUC(Context context, String userID, String mucID, IBindData bind, Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("userID", userID);
			objIn.put("mucID", mucID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.COMMUNITY_IM_URL + EAPIConsts.CommunityReqUrl.KICK_FROM_MUC;
		doExecute(context, bind, EAPIConsts.CommunityReqType.TYPE_KICK_FROM_MUC, url, requestStr, handler);
	}
	/**
	 *      "mucId": 社群id,
           "userIds": [ //批量删除的用户的id数组
                    12,
                    34,
                   56
             ],
          "operatorUserId": "当前操作者的id" 
           	为用户 管理群成员-移除（批量）
	 */
	public static void doKickFromMUCForBatch(Context context,CommunityKickFromForBatchRequest kickRequest, IBindData bind, Handler handler) {
		String requestParam = "";
		Gson gson = new Gson();
		
		requestParam = gson.toJson(kickRequest);
		String url = EAPIConsts.COMMUNITY_IM_URL + EAPIConsts.CommunityReqUrl.KICK_FROM_MUC_FOR_BATCH;
		doExecute(context, bind, EAPIConsts.CommunityReqType.TYPE_KICK_FROM_MUC_FOR_BATCH, url, requestParam, handler);
	}
	
	/**
	 * "communityId":"社群id，必传，不可为空", "userId":"被举报的用户id，如果举报类型为2必传，不可为空",
	 * "type":"举报类型: 1社群 2成员" "memeberId":"举报的成员id" 为用户 管理群成员-举报（单）
	 */
	public static void doReport(Context context, String communityId, String memeberId, String userID, String type, IBindData bind, Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("userID", userID);
			objIn.put("communityId", communityId);
			objIn.put("memeberId", memeberId);
			objIn.put("type", type);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.COMMUNITY_URL + EAPIConsts.CommunityReqUrl.RESPORT_ONE;
		doExecute(context, bind, EAPIConsts.CommunityReqType.TYPE_RESPORT_ONE, url, requestStr, handler);
	}

	/**
	 * @param communityId
	 *            社群id
	 * @param userId
	 *            用户id 获取社群详情
	 */
	public static void doGetCommunityDetail(Context context, long communityId, long userId, IBindData bind, Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("communityId", communityId);
			objIn.put("userId", userId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.COMMUNITY_URL + EAPIConsts.CommunityReqUrl.GET_COMMUNITY_DETAIL;
		doExecute(context, bind, EAPIConsts.CommunityReqType.TYPE_GET_COMMUNITY_DETAIL, url, requestStr, handler);
	}

	/**
	 * @param id
	 *            社群id 获取社群所有成员详情
	 */
	public static void doGetCommunityMemberList(Context context, long id, IBindData bind, Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("id", id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.COMMUNITY_IM_URL + EAPIConsts.CommunityReqUrl.GET_COMMUNITY_MEMBER_LIST;
		doExecute(context, bind, EAPIConsts.CommunityReqType.TYPE_GET_COMMUNITY_MEMBER_LIST, url, requestStr, handler);
	}

	/**
	 * @param id
	 *            社群id 获取社群详情里的成员详情
	 */
	public static void doGetCommunityDetailsMemberList(Context context, long id, IBindData bind, Handler handler) {
		JSONObject objIn = new JSONObject();
		try {
			objIn.put("id", id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = objIn.toString();
		String url = EAPIConsts.COMMUNITY_IM_URL + EAPIConsts.CommunityReqUrl.GET_COMMUNITY_MEMBER_DETAILS;
		doExecute(context, bind, EAPIConsts.CommunityReqType.TYPE_GET_COMMUNITY_MEMBER_DETAILS, url, requestStr, handler);
	}

	/**
	 * @param mucID群聊id
	 * @param listID
	 *            "listID":["与会好友用户id，包含个人用户和机构用户"], 社群群聊加人
	 */
	public static void doInvite2Muc(Context context, long mucID, List<Long> listID, IBindData bind, Handler handler) {
		Gson gson = new Gson();
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("mucID", mucID);
		params.put("listID", listID);
		String requestStr = gson.toJson(params);
		String url = EAPIConsts.COMMUNITY_IM_URL + EAPIConsts.CommunityReqUrl.INVITE2MUC;
		doExecute(context, bind, EAPIConsts.CommunityReqType.TYPE_INVITE2MUC, url, requestStr, handler);
	}

	// 发送消息，false表示发送失败
	public static boolean sendMessage(Context context, IBindData bind, Handler handler, IMBaseMessage msg) {
		try {
			JSONObject obj = new JSONObject();
			if (msg.getImtype() == IMBaseMessage.IM_TYPE_CHAT) {
				obj.put("jtContactID", msg.getRecvID());
			} else {
				obj.put("mucID", msg.getRecvID());
			}
			obj.put("senderName", msg.getSenderName());
			obj.put("text", msg.getContent());
			obj.put("fromTime", msg.getTime());
			obj.put("fromIndex", msg.getIndex());
			obj.put("type", msg.getType());
			obj.put("messageID", msg.getMessageID());
			obj.put("moduleType", "1");
			// TODO
			JSONArray array = new JSONArray();
			if (msg.getAtIds() != null) {
				for (int id : msg.getAtIds()) {
					array.put(id);
				}
			}
			obj.put("ats", array);

			if (msg.getJtFile() != null) {
				JSONObject objJTFile = msg.getJtFile().toJson();
				obj.put("jtFile", objJTFile);
			}

			String requestStr = obj.toString();
			String url = EAPIConsts.COMMUNITY_IM_URL + "mobile/im/sendMessage.action";
			doExecute(context, bind, EAPIConsts.IMReqType.IM_REQ_SEND_MESSAGE, url, requestStr, handler); // handler置为null，可以弹出其他类型消息
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// 获取聊天记录
	public static boolean getMUCMessage(Context context, IBindData bind, Handler handler, String mucID, int fromIndex, int size, boolean isBackward) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("id", mucID);
			obj.put("fromIndex", fromIndex);
			obj.put("size", size);
			obj.put("isBackward", isBackward);
			String requestStr = obj.toString();
			String url = EAPIConsts.COMMUNITY_IM_URL + "mobile/im/getMUCMessage.action";
			doExecute(context, bind, EAPIConsts.IMReqType.IM_REQ_GET_MUC_MESSAGE, url, requestStr, handler);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// 获取群聊详情， false表示发送失败
	public static boolean getMUCDetail(Context context, IBindData bind, Handler handler, String mucID) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("id", mucID);
			String requestStr = obj.toString();
			String url = EAPIConsts.COMMUNITY_IM_URL + "mobile/im/getMUCDetail.action";
			doExecute(context, bind, EAPIConsts.IMReqType.IM_REQ_GET_MUC_DETAIL, url, requestStr, handler);
			return true;
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
		}
		return false;
	}

	public static void fetchFirends(Context context, IBindData bind, long mucId, long currentUserId, JSONArray firendIds, Handler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("mucId", mucId);// 畅聊id
			jsonObject.put("currentUserId", currentUserId);// 当前用户的id
			jsonObject.put("firendIds", firendIds);// 畅聊的成员id，在响应数据结构的firends中，如果没有出现，就说明不是好友
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.COMMUNITY_IM_URL + "mobile/im/fetchFirends.action";
		doExecute(context, bind, EAPIConsts.IMReqType.IM_REQ_FETCHFIRENDS, url, requestStr, handler);
	}

	/**
	 * 获取社群标签信息
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @param "userId":"用户id" 当userId不为空时返回热门标签和自定义标签，当用户id为空时只返回热门标签。
	 * @return
	 */
	public static void getCommunityTagList(Context context, IBindData bind, Handler handler) {
		JSONObject json = new JSONObject();
		try {
			json.put("userId", Long.parseLong(String.valueOf(App.getApp().getUserID())));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = json.toString();
		String url = EAPIConsts.COMMUNITY_URL + EAPIConsts.CommunityReqUrl.GET_COMMUNITY_LABELS;
		doExecute(context, bind, EAPIConsts.CommunityReqType.TYPE_GET_COMMUNITY_LABELS, url, requestStr, handler);
	}

	/**
	 * 创建标签
	 * 
	 * @param context
	 * @param bind
	 * @param tag
	 *            :标签值
	 */
	public static void createCommunityTag(Context context, IBindData bind, String name, Long createdUserId, Handler handler) {
		JsonObject json = new JsonObject();
		json.addProperty("name", name);
		json.addProperty("createdUserId", createdUserId);
		String requestStr = json.toString();
		String url = EAPIConsts.COMMUNITY_URL + EAPIConsts.CommunityReqUrl.CREATE_LABEL;
		doExecute(context, bind, EAPIConsts.CommunityReqType.TYPE_CREATE_LABEL, url, requestStr, handler);
	}

	/**
	 * 获取社群公告
	 * 
	 * @param context
	 * @param bind
	 * @param communityId
	 *            :社群id
	 */
	public static void getNotice(Context context, IBindData bind, Long communityId, Handler handler) {
		JsonObject json = new JsonObject();
		json.addProperty("communityId", communityId);
		String requestStr = json.toString();
		String url = EAPIConsts.COMMUNITY_URL + EAPIConsts.CommunityReqUrl.GET_NOTICE;
		doExecute(context, bind, EAPIConsts.CommunityReqType.TYPE_GET_NOTICE, url, requestStr, handler);
	}

	/**
	 * 创建社群通知(申请加入社群，转让社群)
	 * 
	 * @param context
	 * @param bind
	 * @param notify
	 *            :CommunityNotify
	 */
	public static void createNotice(Context context, IBindData bind, CommunityNotify notify, Handler handler) {
		JsonObject json = new JsonObject();
		json.addProperty("communityId", notify.getCommunityId());
		json.addProperty("communityLogo", notify.getCommunityLogo());
		json.addProperty("communityName", notify.getCommunityName());
		json.addProperty("applicantId", notify.getApplicantId());
		json.addProperty("applicantName", notify.getApplicantName());
		json.addProperty("userLogo", notify.getUserLogo());
		json.addProperty("attendType", notify.getAttendType());
		json.addProperty("acceptStatus", notify.getAcceptStatus());
		json.addProperty("applyReason", notify.getApplyReason());
		json.addProperty("noticeType", notify.getNoticeType());
		json.addProperty("createdUserId", notify.getCreatedUserId());
		json.addProperty("createdUserName", notify.getCreatedUserName());
		json.addProperty("createdUserLogo", notify.getCreatedUserLogo());
		json.addProperty("createdTime", notify.getCreatedTime());
		json.addProperty("applicantReadStatus", notify.getApplicantReadStatus());
		json.addProperty("ownerReadStatus", notify.getOwnerReadStatus());
		String requestStr = json.toString();
		String url = EAPIConsts.COMMUNITY_URL + EAPIConsts.CommunityReqUrl.CREATE_COMMUNITY_NOTICE;
		doExecute(context, bind, EAPIConsts.CommunityReqType.TYPE_CREATE_COMMUNITY_NOTICE, url, requestStr, handler);
	}
	


	/* 获取群设置 */
	public static void getSetDetail(Context context, IBindData bind, String communityId, String createdUserId, Handler handler) {
		JsonObject json = new JsonObject();
		json.addProperty("communityId", communityId);
		json.addProperty("createdUserId", createdUserId);
		String requestStr = json.toString();
		String url = EAPIConsts.COMMUNITY_URL + EAPIConsts.CommunityReqUrl.COMMUNITY_SET_DETAIL;
		doExecute(context, bind, EAPIConsts.CommunityReqType.TYPE_COMMUNITY_SET_DETAIL, url, requestStr, handler);
	}

	/**
	 * 社群属性设置 "communityId":"社群id，      必传，不可为空",
	 * "createdUserId":"社群创建用户Id，     必传，不可为空"，
	 * "newMessageRemind":"群消息提醒:0开启 1无声 2 关闭      可选",
	 * “nickname”:"社群里的昵称       可选", “showNickname”:"是否显示群成员昵称：0是 1否       可选"
	 */
	public static void setSettingDetail(Context context, IBindData bind, String communityId, String createdUserId, String newMessageRemind, String nickname, String showNickname, Handler handler) {
		JsonObject json = new JsonObject();
		json.addProperty("communityId", communityId);
		json.addProperty("createdUserId", createdUserId);
		json.addProperty("newMessageRemind", newMessageRemind);
		json.addProperty("nickname", nickname);
		json.addProperty("showNickname", showNickname);
		String requestStr = json.toString();
		String url = EAPIConsts.COMMUNITY_URL + EAPIConsts.CommunityReqUrl.SET_COMMUNITY;
		doExecute(context, bind, EAPIConsts.CommunityReqType.TYPE_SET_COMMUNITY, url, requestStr, handler);
	}

	/**
	 * "id":"群聊/会议id", "title":"群聊名称", "stick":"是否置顶，true-false",
	 * "notifyNewMessage":"是否推送新消息，true-false",
	 * "cleanRecord":"清除聊天记录，true-清除； 字段不存在或者为false不清除" 如果不修改的参数，客户端不传
	 */
	public static void setSettingTitle(Context context, IBindData bind, String id, String title, Boolean stick, Boolean notifyNewMessage, Boolean cleanRecord, Handler handler) {
		JsonObject json = new JsonObject();
		json.addProperty("id", id);
		json.addProperty("title", title);
		if (stick != null) {
			json.addProperty("stick", stick);
		}
		if (notifyNewMessage != null) {
			json.addProperty("notifyNewMessage", notifyNewMessage);
		}
		if (cleanRecord != null) {
			json.addProperty("cleanRecord", cleanRecord);
		}

		String requestStr = json.toString();
		String url = EAPIConsts.COMMUNITY_IM_URL + EAPIConsts.CommunityReqUrl.MODIFY_MUC_NAME;
		doExecute(context, bind, EAPIConsts.CommunityReqType.TYPE_MODIFY_MUC_NAME, url, requestStr, handler);
	}

	/**
	 * 修改社群基本信息
	 * 
	 * @param context
	 * @param bind
	 * @param communityId
	 *            社群id
	 * @param “key”:社群的属性:头像，名称，公告，社群介绍
	 * @param "value":属性值,
	 * @param handler
	 */
	public static void doModifyCommunityInfo(Context context, IBindData bind, long communityId, String key, String value, Handler handler) {
		JsonObject json = new JsonObject();
		json.addProperty("communityId", communityId);
		json.addProperty("key", key);
		json.addProperty("value", value);
		String requestStr = json.toString();
		String url = EAPIConsts.COMMUNITY_URL + EAPIConsts.CommunityReqUrl.MODIFY_COMMUNITYINFO;
		doExecute(context, bind, EAPIConsts.CommunityReqType.TYPE_MODIFY_COMMUNITYINFO, url, requestStr, handler);
	}

	// 退出会议， false表示发送失败
	public static boolean exitFromMUC(Context context, IBindData bind, Handler handler, String userID, String mucID) {
		try {
			JSONObject obj = new JSONObject();

			obj.put("userID", userID);
			obj.put("mucID", mucID);

			String requestStr = obj.toString();
			String url = EAPIConsts.COMMUNITY_IM_URL + "mobile/im/exitFromMUC.action";
			doExecute(context, bind, EAPIConsts.IMReqType.IM_REQ_EXIT_MUC, url, requestStr, handler);
			return true;
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
		}
		return false;
	}

	/**
	 * 清空聊天记录
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @param thatID
	 *            私聊， 对方的用户id
	 * @param mucID
	 *            群聊， 会议id
	 */
	public static boolean cleanIMRecord(Context context, IBindData bind, Handler handler, String thatID, String mucID) {
		try {
			JSONObject obj = new JSONObject();

			obj.put("userID", thatID);
			obj.put("mucID", mucID);
			String requestStr = obj.toString();
			String url = EAPIConsts.COMMUNITY_IM_URL + "mobile/im/cleanIMRecord.action";
			doExecute(context, bind, EAPIConsts.IMReqType.IM_REQ_CLEAN_MESSAGE, url, requestStr, handler);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void fetchHistoryMessages(Context context, IBindData bind, String keyWord, long id, int messageType, Handler handler) {
		JSONObject jsonObject = new JSONObject();
		int req_type = 0;
		try {
			jsonObject.put("keyWord", keyWord);// 需要查询的聊天消息
			jsonObject.put("messageType", messageType);// 0表示查单聊,1表示查群聊
			jsonObject.put("id", id);// 群聊id或对方的用户id
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (messageType == 0) {
			req_type = EAPIConsts.IMReqType.IM_REQ_FETCHHISTORYMESSAGE_CHAT;
		} else {
			req_type = EAPIConsts.IMReqType.IM_REQ_FETCHHISTORYMESSAGE_MUC;
		}
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.COMMUNITY_IM_URL + "mobile/im/fetchHistoryMessages.action";
		doExecute(context, bind, req_type, url, requestStr, handler);
	}
	//群号是否存在
	public static void existCommunityNo(Context context, IBindData bind, String communityNo, Handler handler){
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("communityNo", communityNo);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.COMMUNITY_URL + EAPIConsts.CommunityReqUrl.EXIST_COMMUNITYNO;
		doExecute(context, bind, EAPIConsts.CommunityReqType.TYPE_EXIST_COMMUNITYNO, url, requestStr, handler);
	}
	
	public static void getCommunityByCommunityNo(Context context, IBindData bind, String communityNo, long user_id, Handler handler){
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("communityNo", communityNo);
			jsonObject.put("user_id", user_id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.COMMUNITY_URL + EAPIConsts.CommunityReqUrl.GET_COMMUNITY_BY_COMMUNITYNO;
		doExecute(context, bind, EAPIConsts.CommunityReqType.TYPE_GET_COMMUNITY_BY_COMMUNITYNO, url, requestStr, handler);
	}

	/**
	 * 修改社群+权限
	 * 
	 * @param context
	 * @param bind
	 * @param set
	 *            社群权限
	 * @param handler
	 */
	public static void doModifyCommunityPermission(Context context, IBindData bind, CreateSet set, Handler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("communityId", String.valueOf(set.getCommunityId()));
			jsonObject.put("applayType", String.valueOf(set.getApplayType()));
			jsonObject.put("memberShowType", String.valueOf(set.getMemberShowType()));
			jsonObject.put("activityShowType", String.valueOf(set.getActivityShowType()));
			jsonObject.put("companyShowType", String.valueOf(set.getCompanyShowType()));
			jsonObject.put("knowledgeShowType", String.valueOf(set.getKnowledgeShowType()));
			jsonObject.put("demandShowType", String.valueOf(set.getDemandShowType()));
			jsonObject.put("peopleShowType", String.valueOf(set.getPeopleShowType()));
			jsonObject.put("communityShowType", String.valueOf(set.getCommunityShowType()));
			jsonObject.put("createdUserId", String.valueOf(set.getCreatedUserId()));
			jsonObject.put("createdTime", String.valueOf(set.getCreatedTime()));
			jsonObject.put("updatedTime", String.valueOf(set.getUpdatedTime()));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.COMMUNITY_URL + EAPIConsts.CommunityReqUrl.MODIFY_COMMUNITY_PERMISSION;
		doExecute(context, bind, EAPIConsts.CommunityReqType.TYPE_MODIFY_COMMUNITY_PERMISSION, url, requestStr, handler);
	}

	/**
	 * 获取社群通知列表根据用户id
	 * 
	 * @param context
	 * @param bind
	 * @param communityId
	 * @param handler
	 */
	public static void doGetCommunityListByUserId(Context context, IBindData bind, String currentUserId, Handler handler) {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("currentUserId", currentUserId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.COMMUNITY_URL + EAPIConsts.CommunityReqUrl.GET_NOTICE_LIST_BY_USERID;
		doExecute(context, bind, EAPIConsts.CommunityReqType.TYPE_GET_NOTICE_LIST_BY_USERID, url, requestStr, handler);
	}
	
	/**
	 * 获取登录用户的具有未读消息的社群列表
	 * 
	 * @param context
	 * @param bind
	 * @param currentUserId
	 * @param handler
	 */
	public static void doGetCommunityList(Context context, IBindData bind, String currentUserId, Handler handler){
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("user_id", currentUserId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.CONFERENCE_URL + EAPIConsts.CommunityReqUrl.GET_COMMUNITY_LIST;
		doExecute(context, bind, EAPIConsts.CommunityReqType.TYPE_GET_COMMUNITY_LIST, url, requestStr, handler);
	}
	
	/**
	 * 
	 * @param context
	 * @param bind
	 * @param id
	 * @param acceptStatus 1接受 2拒绝
	 * @param handler
	 */
	public static void doHandleApply(Context context, IBindData bind, long id, int acceptStatus, long updatedTime, Handler handler){
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("id", id);
			jsonObject.put("acceptStatus", acceptStatus);
			jsonObject.put("updatedTime", updatedTime);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.COMMUNITY_URL + EAPIConsts.CommunityReqUrl.HANDLE_APPLY;
		doExecute(context, bind, EAPIConsts.CommunityReqType.TYPE_HANDLE_APPLY, url, requestStr, handler);
	}
	/**
	 * 编辑社群详情的标签
	 * @param context
	 * @param bind
	 * @param communityId 社群id
	 * @param  "labelIds":标签id 数组
	 * @param handler
	 */
	public static void doModifyCommunityLabels(Context context, IBindData bind, String communityId, List<String> labelIds, Handler handler){
		Gson gson = new Gson();
		// 对于对象转json时
		// ，先把对象传到map中，直接Gson.toJson（对象）会把这个对象直接转为String，即json中key对应的值会带双引号，造成后台解析错误
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("communityId", communityId);
		params.put("labelIds", labelIds);
		String requestStr = gson.toJson(params);
		String url = EAPIConsts.COMMUNITY_URL + EAPIConsts.CommunityReqUrl.MODIFY_COMMUNITYLABELS;
		doExecute(context, bind, EAPIConsts.CommunityReqType.TYPE_MODIFY_COMMUNITYLABELS, url, requestStr, handler);
	}
	/**
	 * 编辑社群详情的关联关系
	 * @param context
	 * @param bind
	 * @param communityId 社群id
	 * @param     ”createrId“:社群创建者id
	 * @param handler
	 */
	public static void doModifyAsso(Context context, IBindData bind, String communityId,String createrId,  List<ASSOData> r,List<ASSOData> p ,List<ASSOData> o,List<ASSOData> k, Handler handler){
		Gson gson = new Gson();
		// 对于对象转json时
		// ，先把对象传到map中，直接Gson.toJson（对象）会把这个对象直接转为String，即json中key对应的值会带双引号，造成后台解析错误
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("communityId", communityId);
		params.put("createrId", createrId);
		params.put("r", r);
		params.put("p", p);
		params.put("o", o);
		params.put("k", k);
		String requestStr = gson.toJson(params);
		String url = EAPIConsts.COMMUNITY_URL + EAPIConsts.CommunityReqUrl.MODIFY_ASSO;
		doExecute(context, bind, EAPIConsts.CommunityReqType.TYPE_MODIFY_ASSO, url, requestStr, handler);
	}
	
	public static boolean createNotices(Context context, IBindData bind, List<CommunityNotify> notifys, Handler handler) {
		try {
			JSONArray jarray = new JSONArray();
			for (CommunityNotify notify : notifys) {
				JSONObject json = new JSONObject();
				json.put("communityId", notify.getCommunityId());
				json.put("communityLogo", notify.getCommunityLogo());
				json.put("communityName", notify.getCommunityName());
				json.put("applicantId", notify.getApplicantId());
				json.put("applicantName", notify.getApplicantName());
				json.put("userLogo", notify.getUserLogo());
				json.put("attendType", notify.getAttendType());
				json.put("acceptStatus", notify.getAcceptStatus());
				json.put("applyReason", notify.getApplyReason());
				json.put("noticeType", notify.getNoticeType());
				json.put("createdUserId", notify.getCreatedUserId());
				json.put("createdUserName", notify.getCreatedUserName());
				json.put("createdUserLogo", notify.getCreatedUserLogo());
				json.put("createdTime", notify.getCreatedTime());
				json.put("applicantReadStatus", notify.getApplicantReadStatus());
				json.put("ownerReadStatus", notify.getOwnerReadStatus());
				jarray.put(json);
			}
			String requestStr = jarray.toString();
			String url = EAPIConsts.COMMUNITY_URL + EAPIConsts.CommunityReqUrl.CREATE_BATCH_COMMUNITY_NOTICES;
			doExecute(context, bind, EAPIConsts.CommunityReqType.TYPE_CREATE_BATCH_COMMUNITY_NOTICES, url, requestStr, handler);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void clientDeleteMessage(Context context, IBindData bind, long userId, String messageId, int type, long topicId, long mucId, long senderId, int status, Handler handler){
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("userId", userId);
			jsonObject.put("messageId", messageId);
			jsonObject.put("type", type);
			jsonObject.put("topicId", topicId);
			jsonObject.put("mucId", mucId);
			jsonObject.put("senderId", senderId);
			jsonObject.put("status", status);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = jsonObject.toString();
		String url = getFullUrl("/mobile/im/clientDeleteMessage.action");
		doExecute(context, bind, EAPIConsts.IMReqType.IM_REQ_CLIENTDELETEMESSAGE, url, requestStr, handler);
	}
	
	/**
	 * 社群畅聊地址
	 * @param method
	 * @return
	 */
	private static String getFullUrl(String method) {
		return EAPIConsts.COMMUNITY_IM_URL + method;
	}
}
