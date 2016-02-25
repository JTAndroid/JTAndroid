package com.tr.api; 

import org.json.JSONArray;
import org.json.JSONObject;

import com.tr.model.SimpleResult;
import com.tr.model.im.FetchFriends;
import com.tr.model.im.MCreateMUC;
import com.tr.model.im.MGetChatMessage;
import com.tr.model.im.MGetListIMRecord;
import com.tr.model.im.MGetListMUC;
import com.tr.model.im.MGetMUCMessage;
import com.tr.model.im.MSendMessage;
import com.tr.model.im.MSetChannelIDResp;
import com.tr.model.obj.MUCDetail;
import com.utils.http.EAPIConsts;

/** 
 * @author 作者姓名  
 * @E-mail: email地址 
 * @version 创建时间：2014-4-11 上午10:14:20 
 * 类说明  im api请求返回数据后的解析处理在这里
 */
public class IMRespFactory {
	public static Object createMsgObject(int msgId, JSONObject response) {
		switch (msgId) {
		case EAPIConsts.IMReqType.IM_REQ_SET_CHANNELID: { 
			// 设置channelID
			return MSetChannelIDResp.createFactory(response);
		}
		case EAPIConsts.IMReqType.IM_REQ_GET_LISTIM: { // 畅聊列表
			return MGetListIMRecord.createFactory(response);
		}
		case EAPIConsts.IMReqType.IM_REQ_SEND_MESSAGE: { // 发送消息
			return MSendMessage.createFactory(response);
		}
		case EAPIConsts.IMReqType.IM_REQ_SEND_MESSAGE_FOR_FORWARD_SOCIAL:{/*(多选)转发到社交*/
			return MSendMessage.createFactoryForwardSocial(response);
		}
		case EAPIConsts.IMReqType.IM_REQ_CREATE_MUC: {
			return MCreateMUC.createFactory(response);
		}
		case EAPIConsts.IMReqType.IM_REQ_GET_LIST_MUC: {
			return MGetListMUC.createFactory(response);
		}
		case EAPIConsts.IMReqType.IM_REQ_CLEAN_MESSAGE:
		case EAPIConsts.IMReqType.IM_REQ_EXIT_MUC: {
			return SimpleResult.createFactory(response);
		}
		case EAPIConsts.IMReqType.IM_REQ_KICKFROMMUC:
		case EAPIConsts.IMReqType.IM_REQ_INVITE2MUC:
		case EAPIConsts.IMReqType.IM_REQ_GET_MUC_DETAIL: 
		case EAPIConsts.IMReqType.IM_REQ_MODIFY_MUC:
		case EAPIConsts.IMReqType.IM_REQ_MODIFY_CONFERENCE:{
			return MUCDetail.createFactory(response);
		}
		case EAPIConsts.IMReqType.IM_REQ_FETCHHISTORYMESSAGE_MUC:
		case EAPIConsts.IMReqType.IM_REQ_GET_MUC_MESSAGE: { // 群聊消息记录
			return MGetMUCMessage.createFactory(response);
		}
		case EAPIConsts.IMReqType.IM_REQ_FETCHHISTORYMESSAGE_CHAT:
		case EAPIConsts.IMReqType.IM_REQ_GET_CHAT_MESSAGE: { // 私聊消息记录
			return MGetChatMessage.createFactory(response);
		}
		case EAPIConsts.IMReqType.IM_DELETE_IM_FROM_LIST:{ // 删除畅聊
			if (response != null) {
				return response.optBoolean("succeed");
			}
		}	
		case EAPIConsts.IMReqType.IM_GET_NEW_GROUP_COUNT: {
			int count=0;
			if (response != null) {
				count=response.optInt("count");
			}
			return count;
		}
		case EAPIConsts.IMReqType.IM_REQ_CLIENTDELETEMESSAGE:
		case EAPIConsts.IMReqType.IM_REQ_CLEAR_UNREAD_MESSAGENUMBER:
			// "responseCode":"响应的结果码，0成功 -1失败",
			int responseCode= -1;
			if (response != null) {
				responseCode=response.optInt("responseCode");
			}
			return responseCode;
		case EAPIConsts.IMReqType.IM_REQ_FETCHFIRENDS:
			return FetchFriends.createFactory(response);
		default:
			return null;
		}
	}
}
 