package com.tr.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.tr.App;
import com.tr.model.SendMessages;
import com.tr.model.obj.ConnectionsMini;
import com.tr.model.obj.IMBaseMessage;
import com.tr.model.obj.JTFile;
import com.tr.model.obj.MUCDetail;
import com.utils.common.EUtil;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/** @author gintong
 * @version 1.0
 * @类说明 im模块api请求统一入口 */

public class IMReqUtil extends ReqBase {

	public static final String TAG = IMReqUtil.class.getSimpleName();
	public static final int MSG_CHAT = 0;
	public static final int MSG_MUC = 1;

	// 将百度云推送的channelID发送到im服务器
	public static boolean setChannelID(Context context, IBindData bind, Handler handler, String userID, String channelID, String baiduUserID, String appID) {

		try {
			JSONObject objIn = new JSONObject();
			objIn.put("userID", userID);
			objIn.put("channelID", channelID);
			objIn.put("baiduUserID", baiduUserID);
			objIn.put("appID", appID);
			objIn.put("clientType", 3);

			String requestStr = objIn.toString();

			String url = getFullUrl("push/public/setChannelID.action");
			doExecute(context, bind, EAPIConsts.IMReqType.IM_REQ_SET_CHANNELID, url, requestStr, handler);
			return true;
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
			return false;
		}
	}

	// 获取畅聊列表, bind返回 List<MIMRecord>
	public static boolean getListIMRecord(Context context, IBindData bind, Handler handler, int index, int pagesize) {
		try {
			JSONObject objIn = new JSONObject();
			objIn.put("index", index);
			objIn.put("size", pagesize);

			String requestStr = objIn.toString();
			String url = getFullUrl("mobile/im/getListIMRecord.action");
			doExecute(context, bind, EAPIConsts.IMReqType.IM_REQ_GET_LISTIM, url, requestStr, handler);
			return true;
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
			return false;
		}
	}

	// 发送消息，false表示发送失败
	public static boolean sendMessageForForwardingSocial(Context context, IBindData bind, Handler handler,HashMap<String,SendMessages>  sendMessagesList,
			HashMap<String,SendMessages>  sendMessagesForwardingTopicList ,
			ArrayList<JTFile> jtFile) {
		try {
			JSONObject obj = new JSONObject();
			JSONArray sendMessagesJsonArray = new JSONArray();
			Iterator iterators = sendMessagesList.entrySet().iterator();
			while (iterators.hasNext()) {
				Map.Entry entry = (Map.Entry) iterators.next();
				Object key = entry.getKey();
				SendMessages sendMessages = (SendMessages) entry.getValue();
				JSONObject sendMessagesJson = sendMessages.toJson();
				sendMessagesJsonArray.put(sendMessagesJson);
			}
			
			Iterator iteratorsTopic = sendMessagesForwardingTopicList.entrySet().iterator();
			while (iteratorsTopic.hasNext()) {
				Map.Entry entry = (Map.Entry) iteratorsTopic.next();
				Object key = entry.getKey();
				SendMessages sendMessages = (SendMessages) entry.getValue();
				JSONObject sendMessagesJson = sendMessages.toJson();
				sendMessagesJsonArray.put(sendMessagesJson);
			}
			
			
			obj.put("sendMessages", sendMessagesJsonArray);		
			String url;
			if(jtFile.size()==1){
				JSONObject objJTFile = jtFile.get(0).toJson();
				obj.put("jtFile", objJTFile);
				 url = getFullUrl("mobile/im/sendMessageForForwardingSocial.action");
			}else{
				JSONArray topicsJsonArray = new JSONArray();
				if (jtFile != null) {
					for(int i=0;i<jtFile.size();i++){
						JSONObject objJTFile = jtFile.get(i).toJson();
						topicsJsonArray.put(objJTFile);
					}
					obj.put("jtFiles", topicsJsonArray);
				}
				url = getFullUrl("mobile/im/sendMutiKnowledgesForForwardingSocial.action");
			}
			
			String requestStr = obj.toString();
//			String url = getFullUrl("mobile/im/sendMessageForForwardingSocial.action");
//			String url = getFullUrl("mobile/im/sendMutiKnowledgesForForwardingSocial.action");
			doExecute(context, bind, EAPIConsts.IMReqType.IM_REQ_SEND_MESSAGE_FOR_FORWARD_SOCIAL, url, requestStr, handler); // handler置为null，可以弹出其他类型消息
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	// 发送消息，false表示发送失败
		public static boolean sendMessage(Context context, IBindData bind, Handler handler, IMBaseMessage msg) {
			try {
				JSONObject obj = new JSONObject();
				if (msg.getImtype() == IMBaseMessage.IM_TYPE_CHAT) {
					obj.put("jtContactID", msg.getRecvID());
				}
				else {
					obj.put("mucID", msg.getRecvID());
				}
				obj.put("senderName", msg.getSenderName());
				obj.put("text", msg.getContent());
				obj.put("fromTime", msg.getTime());
				obj.put("fromIndex", msg.getIndex());
				obj.put("type", msg.getType());
				obj.put("messageID", msg.getMessageID());
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
				String url = getFullUrl("mobile/im/sendMessage.action");
//				String url = EAPIConsts.TMS_URL_PERSON_XKH + "mobile/im/sendMessage.action";
				doExecute(context, bind, EAPIConsts.IMReqType.IM_REQ_SEND_MESSAGE, url, requestStr, handler); // handler置为null，可以弹出其他类型消息
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}

	// 创建会议， false表示发送失败
	public static boolean createMUC(Context context, IBindData bind, Handler handler, List<ConnectionsMini> listMini) {
		try {
			JSONObject obj = new JSONObject();

			// 添加与会好友id列表
			String title = App.getNick() + ",";
			JSONArray arrJTContactID = new JSONArray();
			for (int i = 0; i < listMini.size(); i++) {
				arrJTContactID.put(i, listMini.get(i).getId());
				title += listMini.get(i).getName();
				if (i < listMini.size() - 1)
					title += ",";
			}
			obj.put("title", title);
			obj.put("listJTContactID", arrJTContactID);

			String requestStr = obj.toString();
			String url = getFullUrl("mobile/im/createMUC.action");
			doExecute(context, bind, EAPIConsts.IMReqType.IM_REQ_CREATE_MUC, url, requestStr, handler);
			return true;
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
		}
		return false;
	}

	// 创建会议， false表示发送失败
	public static boolean createConference(Context context, IBindData bind, Handler handler, MUCDetail detail) {
		try {
			JSONObject obj = new JSONObject();

			obj.put("subject", detail.getSubject());
			obj.put("title", detail.getTitle());
			// 添加与会好友id列表
			obj.put("orderTime", detail.getOrderTime());
			JSONArray arrJTContactID = new JSONArray();
			List<ConnectionsMini> listConnections = detail.getListConnectionsMini();
			if (listConnections != null) {
				for (int i = 0; i < listConnections.size(); i++) {
					ConnectionsMini con = listConnections.get(i);
					arrJTContactID.put(i, con.getId());
				}
			}
			obj.put("listJTContactID", arrJTContactID);

			// 添加与会机构id列表
			JSONArray arr = new JSONArray();
			obj.put("listOrganizationID", arr);
			obj.put("content", detail.getContent());

			if (detail.getListJTFile() != null) {
				arr = new JSONArray();
				for (int i = 0; i < detail.getListJTFile().size(); i++) {
					JTFile file = detail.getListJTFile().get(i);
					JSONObject objFile = file.toJson();
					if (objFile != null)
						arr.put(i, objFile);
				}
				obj.put("listJTFile", arr);
			}

			String requestStr = obj.toString();
			String url = getFullUrl("mobile/im/createMUC.action");
			doExecute(context, bind, EAPIConsts.IMReqType.IM_REQ_CREATE_MUC, url, requestStr, handler);
			return true;
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
		}
		return false;
	}

	// 邀请好友参加会议， false表示发送失败
	public static boolean invite2MUC(Context context, IBindData bind, Handler handler, List<String> listID, int mucID) {
		try {
			JSONObject obj = new JSONObject();

			// 添加与会好友id列表
			JSONArray arrJTContactID = new JSONArray();
			for (int i = 0; i < listID.size(); i++) {
				arrJTContactID.put(i, listID.get(i));
			}
			obj.put("listID", arrJTContactID);
			obj.put("mucID", mucID);

			String requestStr = obj.toString();
			String url = getFullUrl("mobile/im/invite2MUC.action");
			doExecute(context, bind, EAPIConsts.IMReqType.IM_REQ_INVITE2MUC, url, requestStr, handler);
			return true;
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
		}
		return false;
	}

	// 获取我的畅聊列表 getListMUC 3.5
	public static boolean getListMUC(Context context, IBindData bind, Handler handler) {
		try {
			String requestStr = "{}";
			String url = getFullUrl("mobile/im/getListMUC.action");
			doExecute(context, bind, EAPIConsts.IMReqType.IM_REQ_GET_LIST_MUC, url, requestStr, handler);
			return true;
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
		}
		return false;
	}

	// 踢出会议， false表示发送失败
	public static boolean kickFromMUC(Context context, IBindData bind, Handler handler, String userID, String mucID) {
		try {
			JSONObject obj = new JSONObject();

			obj.put("userID", userID);
			obj.put("mucID", mucID);

			String requestStr = obj.toString();
			String url = getFullUrl("mobile/im/kickFromMUC.action");
			doExecute(context, bind, EAPIConsts.IMReqType.IM_REQ_KICKFROMMUC, url, requestStr, handler);
			return true;
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
		}
		return false;
	}

	// 退出会议， false表示发送失败
	public static boolean exitFromMUC(Context context, IBindData bind, Handler handler, String userID, String mucID) {
		try {
			JSONObject obj = new JSONObject();

			obj.put("userID", userID);
			obj.put("mucID", mucID);

			String requestStr = obj.toString();
			String url = getFullUrl("mobile/im/exitFromMUC.action");
			doExecute(context, bind, EAPIConsts.IMReqType.IM_REQ_EXIT_MUC, url, requestStr, handler);
			return true;
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
		}
		return false;
	}

	// 获取会议详情， false表示发送失败
	public static boolean getMUCDetail(Context context, IBindData bind, Handler handler, String mucID) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("id", mucID);
			String requestStr = obj.toString();
			String url = getFullUrl("mobile/im/getMUCDetail.action");
			doExecute(context, bind, EAPIConsts.IMReqType.IM_REQ_GET_MUC_DETAIL, url, requestStr, handler);
			return true;
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
		}
		return false;
	}

	// 获取会议详情， false表示发送失败
	/*
	 * public static boolean getMeetingDetail(Context context, IBindData bind,
	 * Handler handler, String mucID, String topicID) { try { JSONObject obj =
	 * new JSONObject(); obj.put("id", mucID); obj.put("topicID", topicID);
	 * String requestStr = obj.toString(); String url =
	 * getFullUrl("mobile/im/getMUCDetail.action"); doExecute(context, bind,
	 * EAPIConsts.IMReqType.IM_REQ_GET_MUC_DETAIL, url, requestStr, handler);
	 * return true; } catch (Exception e) { Log.d(TAG, e.getMessage()); } return
	 * false; }
	 */

	/** 获取会议聊天记录，true-成功，false失败
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @param mucID
	 * @param fromIndex
	 * @param size
	 * @param isBackward
	 *            true-获取时间点以后的聊天记录；false-获取时间点以前，也就是更早的聊天记录
	 * @return */
	public static boolean getMUCMessage(Context context, IBindData bind, Handler handler, String mucID, int fromIndex, int size, boolean isBackward) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("id", mucID);
			obj.put("fromIndex", fromIndex);
			obj.put("size", size);
			obj.put("isBackward", isBackward);
			String requestStr = obj.toString();
			String url = getFullUrl("mobile/im/getMUCMessage.action");
			doExecute(context, bind, EAPIConsts.IMReqType.IM_REQ_GET_MUC_MESSAGE, url, requestStr, handler);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/** 获取私聊聊天记录， false表示发送失败
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @param id
	 * @param fromIndex
	 * @param size
	 * @param isBackward
	 *            true-获取时间点以后的聊天记录；false-获取时间点以前，也就是更早的聊天记录
	 * @return */
	public static boolean getChatMessage(Context context, IBindData bind, Handler handler, String id, int fromIndex, int size, boolean isBackward) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("id", id);
			obj.put("isBackward", isBackward);
			obj.put("fromIndex", fromIndex);
			obj.put("size", size);
			String requestStr = obj.toString();
			String url = getFullUrl("mobile/im/getChatMessage.action");
			doExecute(context, bind, EAPIConsts.IMReqType.IM_REQ_GET_CHAT_MESSAGE, url, requestStr, handler);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/** 修改群聊详情，参考 3.12
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @param obj
	 *            要修改的json对象， 参考协议文档，要修改的设置值，不修改的为空
	 * @return */
	public static void modifyMuc(Context context, IBindData bind, Handler handler, JSONObject obj) {
		String requestStr = obj.toString();
		String url = getFullUrl("mobile/im/modifyMuc.action");
		doExecute(context, bind, EAPIConsts.IMReqType.IM_REQ_MODIFY_MUC, url, requestStr, handler);
		return;
	}

	/** 修改会议详情，参考协议文档3.12
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @param obj
	 *            具体内容参考协议文档3.12 */
	public static void modifyConference(Context context, IBindData bind, Handler handler, JSONObject obj) {
		String requestStr = obj.toString();
		String url = getFullUrl("mobile/im/modifyConference.action");
		doExecute(context, bind, EAPIConsts.IMReqType.IM_REQ_MODIFY_CONFERENCE, url, requestStr, handler);
		return;
	}

	/** 清空聊天记录，参见协议文档3.19
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @param thatID
	 *            私聊， 对方的用户id
	 * @param mucID
	 *            群聊， 会议id */
	public static boolean cleanIMRecord(Context context, IBindData bind, Handler handler, String thatID, String mucID) {
		try {
			JSONObject obj = new JSONObject();

			obj.put("userID", thatID);
			obj.put("mucID", mucID);
			String requestStr = obj.toString();
			String url = getFullUrl("mobile/im/cleanIMRecord.action");
			doExecute(context, bind, EAPIConsts.IMReqType.IM_REQ_CLEAN_MESSAGE, url, requestStr, handler);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/** 删除畅聊列表中的畅聊
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @param obj
	 *            具体内容参考协议文档3.12 */
	public static void deleteIMFromList(Context context, IBindData bind, Handler handler, String userID, String mucID) {
		try {
			JSONObject jObject = new JSONObject();
			if (userID.length() > 0) {
				jObject.put("userID", userID);
			}
			if (mucID.length() > 0) {
				jObject.put("mucID", mucID);
			}
			String requestStr = jObject.toString();
			String url = getFullUrl("mobile/im/deleteIMFromList.action");
			doExecute(context, bind, EAPIConsts.IMReqType.IM_DELETE_IM_FROM_LIST, url, requestStr, handler);
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
	}

	// 获取新群的个数
	public static void getNewGroupCount(Context context, IBindData bind, JSONObject jsonObject, Handler handler) {
		String requestStr = jsonObject.toString();
		String url = getFullUrl("mobile/im/getNewGroupCount.action");
		doExecute(context, bind, EAPIConsts.IMReqType.IM_GET_NEW_GROUP_COUNT, url, requestStr, handler);
	}

	private static String getFullUrl(String method) {
		return EAPIConsts.IM_URL + method;
	}
	/**
	 *3.1.2  删除时清除未读消息数(包含单聊、群聊、会议、邀请函和通知5种类型) 
	 * 单聊传userId，userId2，type ；群聊和会议传userId，mucId，type；邀请函和通知传userId和type
	 * @param context
	 * @param bind
	 * @param userId 用户id
	 * @param userId2 单聊时的对方用户id
	 * @param mucId 群聊或会议id
	 * @param type 社交列表中的类型，1单聊 2畅聊 3会议 4邀请函 5通知
	 * @param handler
	 * @param isClosedConference 是否在结束会议时调用清空未读消息接口,true:清空会议的未读条数,false:清空自己的未读条数
	 */
	public static void doclearUnreadMessageNumber(Context context, IBindData bind, long userId, long userId2,long mucId,int type,Handler handler, boolean isClosedConference){
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("userId", userId);
			jsonObject.put("userId2", userId2);
			jsonObject.put("mucId", mucId);
			jsonObject.put("type", type);
			if(isClosedConference){
				jsonObject.put("status", 1);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = jsonObject.toString();
		String url = getFullUrl("mobile/im/clearUnreadMessageNumber.action");
		doExecute(context, bind, EAPIConsts.IMReqType.IM_REQ_CLEAR_UNREAD_MESSAGENUMBER, url, requestStr, handler);
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
		String url = getFullUrl("mobile/im/clientDeleteMessage.action");
//		String url = "http://192.168.120.144:8080/mobile/im/clientDeleteMessage.action";
		doExecute(context, bind, EAPIConsts.IMReqType.IM_REQ_CLIENTDELETEMESSAGE, url, requestStr, handler);
	}
	
	public static void fetchHistoryMessages(Context context, IBindData bind, String keyWord, long id, int messageType, Handler handler){
		JSONObject jsonObject = new JSONObject();
		int req_type = 0;
		try {
			jsonObject.put("keyWord", keyWord);//需要查询的聊天消息
			jsonObject.put("messageType", messageType);//0表示查单聊,1表示查群聊
			jsonObject.put("id", id);//群聊id或对方的用户id
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if(messageType==0){
			req_type = EAPIConsts.IMReqType.IM_REQ_FETCHHISTORYMESSAGE_CHAT;
		}else{
			req_type = EAPIConsts.IMReqType.IM_REQ_FETCHHISTORYMESSAGE_MUC;
		}
		String requestStr = jsonObject.toString();
		String url = getFullUrl("mobile/im/fetchHistoryMessages.action");
		doExecute(context, bind, req_type, url, requestStr, handler);
	}
	
	public static void fetchFirends(Context context, IBindData bind, long mucId, long currentUserId, JSONArray firendIds, Handler handler){
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("mucId", mucId);//畅聊id
			jsonObject.put("currentUserId", currentUserId);//当前用户的id
			jsonObject.put("firendIds", firendIds);//畅聊的成员id，在响应数据结构的firends中，如果没有出现，就说明不是好友
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String requestStr = jsonObject.toString();
		String url = getFullUrl("mobile/im/fetchFirends.action");
		doExecute(context, bind, EAPIConsts.IMReqType.IM_REQ_FETCHFIRENDS, url, requestStr, handler);
	}
}
