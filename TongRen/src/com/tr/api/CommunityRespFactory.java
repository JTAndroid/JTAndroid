package com.tr.api;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.tr.model.im.FetchFriends;
import com.tr.model.knowledge.UserCategory;
import com.tr.model.obj.MUCDetail;
import com.tr.ui.communities.model.CommunityApply;
import com.tr.ui.communities.model.CommunityDetailRes;
import com.tr.ui.communities.model.CommunityKickFromForBatch;
import com.tr.ui.communities.model.CommunityLabels;
import com.tr.ui.communities.model.CommunityNotify;
import com.tr.ui.communities.model.CommunitySocialList;
import com.tr.ui.communities.model.CommunityUserSetting;
import com.tr.ui.communities.model.CreatePrecondition;
import com.tr.ui.communities.model.ImMucinfo;
import com.tr.ui.communities.model.MyCommunitListData;
import com.tr.ui.communities.model.Notification;
import com.utils.http.EAPIConsts;

/**
 * 社群数据解析Factory
 * 
 * @author cui
 * 
 */
public class CommunityRespFactory {

	public static Object createMsgObject(int msgId, String response) throws MalformedURLException, JSONException, ParseException {
		// 社群返回结果是否成功以notification中的notifCode=0001 0001为成功0002为失败
		JSONObject obj = new JSONObject(response);
		JSONObject jsonObject = obj.optJSONObject("responseData");
		JSONObject notification = obj.optJSONObject("notification");
		Gson gson = new Gson();
		HashMap<String, Object> dataBox = new HashMap<String, Object>();
		String strKey = "";
		String notifCode = "";
		

		if (msgId == EAPIConsts.CommunityReqType.TYPE_GET_COMMUNITY_BY_COMMUNITYNO) {//社群请求处理
			JSONObject notiObj = obj.optJSONObject("notification");
			
		}
		switch (msgId) {
		// 1.获取社群所有成员详情
		case EAPIConsts.CommunityReqType.TYPE_GET_COMMUNITY_MEMBER_LIST:
			// 这个是不按notifCode提示是否成功
			strKey = "mucDetail";
			MUCDetail detail = gson.fromJson(jsonObject.opt(strKey).toString(), MUCDetail.class);
			if (detail.getListConnectionsMini2()!=null&&!detail.getListConnectionsMini2().isEmpty()) {
				detail.setListConnectionsMini(detail.getListConnectionsMini2());
			}
			if (null != detail) {
				dataBox.put(strKey, detail);
				return dataBox;
			}
			return null;
			// 2.获取畅聊的好友列表+畅聊创建者
		case EAPIConsts.CommunityReqType.TYPE_FETCH_FRIENDS:
			return FetchFriends.createFactory(obj);
			// 3.获取单聊、群聊聊天历史记录
		case EAPIConsts.CommunityReqType.TYPE_FETCH_HISTORY_MESSAGES:
			return null;
			// 4.退出社群群聊
		case EAPIConsts.CommunityReqType.TYPE_EXIT_FROM_MUC:
			return null;
			// 5.社群群聊加人
		case EAPIConsts.CommunityReqType.TYPE_INVITE2MUC:
			strKey = "mucDetail";
			MUCDetail mucDetail = gson.fromJson(jsonObject.opt(strKey).toString(), MUCDetail.class);
			if (null != mucDetail) {
				dataBox.put(strKey, mucDetail);
				return dataBox;
			} else
				return null;
			// 6.社群群聊踢人
		case EAPIConsts.CommunityReqType.TYPE_KICK_FROM_MUC:
			strKey = "mucDetail";
			MUCDetail kickDetail = gson.fromJson(jsonObject.opt(strKey).toString(), MUCDetail.class);
			if (kickDetail.getListConnectionsMini2()!=null&&!kickDetail.getListConnectionsMini2().isEmpty()) {
			kickDetail.setListConnectionsMini(kickDetail.getListConnectionsMini2());
			}
			if (null != kickDetail) {
				dataBox.put(strKey, kickDetail);
				return dataBox;
			}
			return null;
		case EAPIConsts.CommunityReqType.TYPE_KICK_FROM_MUC_FOR_BATCH:	// 12..管理群成员-移除（批量）
			strKey = "responseData";
			String kickstr = obj.optJSONArray(strKey).toString();
			ArrayList<CommunityKickFromForBatch> communityKickFromForBatch = gson.fromJson(kickstr, new TypeToken<ArrayList<CommunityKickFromForBatch>>() {}.getType());
			if (null != communityKickFromForBatch) {
				dataBox.put(strKey, communityKickFromForBatch);
				return dataBox;
			}
			return null;
			// 7.社群群聊修改名称
		case EAPIConsts.CommunityReqType.TYPE_MODIFY_MUC_NAME:
			strKey = "mucDetail";
			MUCDetail newNameDetail = gson.fromJson(jsonObject.opt(strKey).toString(), MUCDetail.class);
			if (newNameDetail.getListConnectionsMini2()!=null&&!newNameDetail.getListConnectionsMini2().isEmpty()) {
			newNameDetail.setListConnectionsMini(newNameDetail.getListConnectionsMini2());
			}
			if (null != newNameDetail) {
				dataBox.put(strKey, newNameDetail);
				return dataBox;
			}
			return null;
			// 8.社群中的解散该群
		case EAPIConsts.CommunityReqType.TYPE_EXIT_COMMUNITY:
			return null;
			// 9.社群属性设置
		case EAPIConsts.CommunityReqType.TYPE_SET_COMMUNITY:
			return null;
			// 10.按照发言时间排序获取用户id集合
		case EAPIConsts.CommunityReqType.TYPE_ORDER_USER_BY_TIME:
				strKey = "responseData";
				String jsonString = obj.getJSONArray(strKey).toString();
				List<String> listTime = gson.fromJson(jsonString,
						new TypeToken<List<String>>() {
						}.getType());
				if (listTime != null) {
					dataBox.put(strKey, listTime);
				}
				return dataBox;
			
			// //11.按照发言时间排序获取用户id集合
			// case EAPIConsts.CommunityReqType.TYPE_ORDER_USER_BY_TIME:
			// return null;
			// 12..管理群成员-禁言（单个）
		case EAPIConsts.CommunityReqType.TYPE_MANAGE_COMMUNITY:
			strKey = "notifCode";
			notifCode = (String) notification.opt(strKey);
			if (notifCode.contains("1")) {
				dataBox.put("isResponse", true);
				return dataBox;
			}
			return null;
			// 13.管理群成员-禁言（批量
		case EAPIConsts.CommunityReqType.TYPE_MANAGE_COMMUNITY_BATCH:
			strKey = "notifCode";
			notifCode = (String) notification.opt(strKey);
			if (notifCode.contains("1")) {
				dataBox.put("isResponse", true);
				return dataBox;
			}
			return null;
			// 14.获取社群属性信息
		case EAPIConsts.CommunityReqType.TYPE_COMMUNITY_SET_DETAIL:
			strKey = "result";
			CommunityUserSetting setting = gson.fromJson(jsonObject.opt(strKey).toString(), CommunityUserSetting.class);
			if (null != setting) {
				dataBox.put(strKey, setting);
				return dataBox;
			}
			return null;
			// 15.创建社群
		case EAPIConsts.CommunityReqType.TYPE_CREATE_COMMUNITY:
			strKey = "notifCode";
			notifCode = (String) notification.opt(strKey);
			if (notifCode.contains("1")) {
				strKey = "communityId";
				long communityId = jsonObject.optLong(strKey);
				dataBox.put(strKey, communityId);
				return dataBox;
			}
			return null;
			// 16.获取社群详情
		case EAPIConsts.CommunityReqType.TYPE_GET_COMMUNITY_DETAIL:
			strKey = "notifCode";
			notifCode = (String) notification.opt(strKey);
			if (notifCode.contains("1")) {
				//  "exist":该成员是否存在该社群中，true存在，false 不存在
				strKey = "exist";
				boolean exist = (Boolean) jsonObject.opt(strKey);
				dataBox.put(strKey, exist);
				
				strKey = "community";
				CommunityDetailRes communityDetailRes = gson.fromJson(jsonObject.opt(strKey).toString(), CommunityDetailRes.class);
				dataBox.put("result", communityDetailRes);
				return dataBox;
			}

			return null;
			// 17.获取社群的设置（权限）信息
		case EAPIConsts.CommunityReqType.TYPE_GET_COMMUNITY_SETPERSSION:

			return null;
			// 18.获取社群标签
		case EAPIConsts.CommunityReqType.TYPE_GET_COMMUNITY_LABELS:
			strKey = "notifCode";
			notifCode = (String) notification.opt(strKey);
			if (notifCode.contains("1")) {
				CommunityLabels communityLabels = gson.fromJson(jsonObject.toString(), CommunityLabels.class);
				dataBox.put("result", communityLabels);
				return dataBox;
			}
			return null;
			// 19.创建标签
		case EAPIConsts.CommunityReqType.TYPE_CREATE_LABEL:
			strKey = "notifCode";
			notifCode = (String) notification.opt(strKey);
			if (notifCode.contains("1")) {
				strKey = "labelId";
				long labelId = (Long) jsonObject.opt(strKey);
				dataBox.put("labelId", labelId);
				return dataBox;
			}
			return null;
			// 20.获取先决条件信息
		case EAPIConsts.CommunityReqType.TYPE_GET_PRECONDITION:
			strKey = "notifCode";
			notifCode = (String) notification.opt(strKey);
			if (notifCode.contains("1")) {
				CreatePrecondition createPrecondition = gson.fromJson(jsonObject.toString(), CreatePrecondition.class);
				if (null != createPrecondition) {
					dataBox.put("result", createPrecondition);
					return dataBox;
				} else
					return null;
			}
			return null;
			// 21.添加社群标签
		case EAPIConsts.CommunityReqType.TYPE_ADD_COMMUNITY_LABEL:
			return null;
			// 22.修改关联关系
		case EAPIConsts.CommunityReqType.TYPE_MODIFY_ASSO:
			strKey = "notifCode";
			notifCode = (String) notification.opt(strKey);
			if (notifCode.contains("1")) {
				dataBox.put(strKey, notifCode);
				return dataBox;
			}
			return null;
			// 23.修改社群权限设置
		case EAPIConsts.CommunityReqType.TYPE_MODIFY_COMMUNITY_PERMISSION:
			strKey = "notifCode";
			notifCode = (String) notification.opt(strKey);
			if (notifCode.contains("1")) {
				dataBox.put(strKey, notifCode);
				return dataBox;
			}
			return null;
			// 24.管理群成员-转让群组
		case EAPIConsts.CommunityReqType.TYPE_ASSIGNMENT_COMUNITY:
			strKey = "notifCode";
			notifCode = (String) notification.opt(strKey);
			if (notifCode.contains("1")) {
				dataBox.put("isResponse", true);
			}else{
				dataBox.put("isResponse", false);
			}
			return dataBox;
			// 25.删除社群标签
		case EAPIConsts.CommunityReqType.TYPE_DELETE_COMMUNITY_LABEL:
			return null;
			// 26.删除标签
		case EAPIConsts.CommunityReqType.TYPE_DELETE_LABEL:
			return null;
			// 27.管理群成员-举报(单)
		case EAPIConsts.CommunityReqType.TYPE_RESPORT_ONE:
			strKey = "notifCode";
			notifCode = (String) notification.opt(strKey);
			if (notifCode.contains("1")) {
				dataBox.put("isResponse", true);
				return dataBox;
			}
			return null;
			// 28.管理群成员-举报(批)
		case EAPIConsts.CommunityReqType.TYPE_RESPORT_BATCH:
			return null;
			// 29.获取社群通知列表根据社群id
		case EAPIConsts.CommunityReqType.TYPE_GET_NOTICE_LIST_BY_COMMUNITYID:
			return null;
			// 30.获取社群通知列表根据用户id
		case EAPIConsts.CommunityReqType.TYPE_GET_NOTICE_LIST_BY_USERID:
			strKey = "notifCode";
			notifCode = (String) notification.opt(strKey);
			if (notifCode.contains("1")) {
				strKey = "list";
				String str = jsonObject.optJSONArray(strKey).toString();
				ArrayList<CommunityNotify> communityNotifylist = gson.fromJson(str, new TypeToken<ArrayList<CommunityNotify>>() {}.getType());
				dataBox.put(strKey, communityNotifylist);
				return dataBox;
			}
			return null;
			// 31.创建社群通知(申请加入社群，转让社群
		case EAPIConsts.CommunityReqType.TYPE_CREATE_COMMUNITY_NOTICE:
			strKey = "notifCode";
			notifCode = (String) notification.opt(strKey);
			if (notifCode.contains("1")) {
				dataBox.put(strKey, notifCode);
				return dataBox;
			}
			return null;
			// 32.处理社群的申请(同意/拒绝)
		case EAPIConsts.CommunityReqType.TYPE_HANDLE_APPLY:
			if (notification != null) {
				Notification notif = gson.fromJson(notification.toString(), Notification.class);
				if (notif != null) {
					dataBox.put("notification", notif);
					return dataBox;
				}
			}
			return null;
			// 33.修改社群基本信息
		case EAPIConsts.CommunityReqType.TYPE_MODIFY_COMMUNITYINFO:
			strKey = "notifCode";
			notifCode = (String) notification.opt(strKey);
			if (notifCode.contains("1")) {
				dataBox.put(strKey, notifCode);
				return dataBox;
			}
			return null;
			// 34.获取社群详详情中的群成员(包含群人员的数量，只获取10个成员，第一位是社群的群主)，接口参考【序号1】
		case EAPIConsts.CommunityReqType.TYPE_GET_COMMUNITY_MEMBER_DETAILS:
			strKey = "notifCode";
			notifCode = (String) notification.opt(strKey);
			if (notifCode.contains("1")) {
				strKey = "count";
				int count = (Integer) jsonObject.opt(strKey);
				dataBox.put(strKey, count);
				strKey = "mucDetail";
				MUCDetail mdetail = gson.fromJson(jsonObject.opt(strKey).toString(), MUCDetail.class);
				if (mdetail.getListConnectionsMini2()!=null&&!mdetail.getListConnectionsMini2().isEmpty()) {
				mdetail.setListConnectionsMini(mdetail.getListConnectionsMini2());
				}
				if (null != mdetail) {
					dataBox.put(strKey, mdetail);
					return dataBox;
				}
			}
			return null;
			// 35.主页-社群列表
		case EAPIConsts.CommunityReqType.TYPE_MAIN_COMMUNITY_LIST:
			strKey = "notifCode";
			notifCode = (String) notification.opt(strKey);
			if (notifCode.contains("1")) {
				strKey = "result";
				String str = jsonObject.optJSONArray(strKey).toString();
				List<ImMucinfo> communityList = gson.fromJson(str, new TypeToken<List<ImMucinfo>>() {
				}.getType());
				dataBox.put(strKey, communityList);
				return dataBox;
			}

			return null;
			// 36.我的社群列表
		case EAPIConsts.CommunityReqType.TYPE_MY_COMMUNITY_LIST:
			strKey = "notifCode";
			notifCode = (String) notification.opt(strKey);
			if (notifCode.contains("1")) {
				strKey = "result";
				String str = jsonObject.optJSONArray(strKey).toString();
				List<MyCommunitListData> imMucinfos = gson.fromJson(str, new TypeToken<List<MyCommunitListData>>() {
				}.getType());
				dataBox.put(strKey, imMucinfos);
				return dataBox;
			}

			return null;
			// //37.获取公告
		case EAPIConsts.CommunityReqType.TYPE_GET_NOTICE:
			strKey = "notifCode";
			notifCode = (String) notification.opt(strKey);
			if (notifCode.contains("1")) {
				strKey = "notice";
				String notice = (String) jsonObject.opt(strKey);
				dataBox.put(strKey, notice);
				return dataBox;
			}
			return null;
			//39.通过社群号加社群
		case EAPIConsts.CommunityReqType.TYPE_GET_COMMUNITY_BY_COMMUNITYNO:
			if(jsonObject==null){
				if (notification != null) {
					Notification notif = gson.fromJson(notification.toString(), Notification.class);
					if (notif != null) {
						dataBox.put("notification", notif);
						return dataBox;
					}
				}
			}else{
				strKey = "notifCode";
				notifCode = (String) notification.opt(strKey);
				if (notifCode.contains("1")) {
					CommunityApply commuapply = gson.fromJson(jsonObject.toString(), CommunityApply.class);
					if(commuapply!=null){
						dataBox.put("result", commuapply);
						return dataBox;
					}else{
						return null;
					}
				}
			}
			break;
			// 40.修改社群标签
		case EAPIConsts.CommunityReqType.TYPE_MODIFY_COMMUNITYLABELS:
			strKey = "notifCode";
			notifCode = (String) notification.opt(strKey);
			if (notifCode.contains("1")) {
				dataBox.put(strKey, notifCode);
				return dataBox;
			}
			return null;
			//41群号是否存在
		case EAPIConsts.CommunityReqType.TYPE_EXIST_COMMUNITYNO:
			strKey = "notifCode";
			notifCode = (String) notification.opt(strKey);
			if (notifCode.contains("1")) {
				strKey = "isExist";
				boolean isExist = jsonObject.optBoolean(strKey);
				dataBox.put(strKey, isExist);
				return dataBox;
			}
			break;
			//42 批量创建通知
		case EAPIConsts.CommunityReqType.TYPE_CREATE_BATCH_COMMUNITY_NOTICES:
			strKey = "notifCode";
			notifCode = (String) notification.opt(strKey);
			if (notifCode.contains("1")) {
				dataBox.put(strKey, notifCode);
				return dataBox;
			}
			return null;
			//45获取社群的未读消息
		case EAPIConsts.CommunityReqType.TYPE_GET_COMMUNITY_NEWCOUNT_BY_USERID:
			break;
			//46获取登录用户的具有未读消息的社群列表
		case EAPIConsts.CommunityReqType.TYPE_GET_COMMUNITY_LIST:
			strKey = "notifCode";
			notifCode = (String) notification.opt(strKey);
			if (notifCode.contains("1")) {
				CommunitySocialList csl = gson.fromJson(jsonObject.toString(), CommunitySocialList.class);
				if(csl !=null){
					dataBox.put("CommunitySocialList", csl);
					return dataBox;
				}
			}
			break;
		}
		return null;
	}
}
