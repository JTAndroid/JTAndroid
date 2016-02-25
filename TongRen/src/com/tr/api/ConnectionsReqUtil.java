package com.tr.api;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.tr.model.model.PeopleForm;
import com.tr.model.obj.ForwardDynamicNews;
import com.tr.service.GetConnectionsListService.RequestType;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * @ClassName: UserReqUtil.java
 * @Description: 用户相关的接口
 * @Author leon
 * @Version v 1.0
 * @Date 2014-04-11
 * @LastEdit 2014-04-11
 */
public class ConnectionsReqUtil extends ReqBase {

	/** 获得 我的好友和人脉 列表 */
	public static void doGetConnectionsList(Context context, IBindData bind, JSONObject jsonObject, Handler handler) {

		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.im_CONNECTIONSLIST;
		/** 8个参数的 顺序 context 数据上下文 bind 返回接口 联网类型 请求地址 JSON串 错误处理hander */
		doExecute(context, bind, EAPIConsts.concReqType.CONNECTIONSLIST, url, requestStr, handler);
	}
	
	/** 获得 通讯录( 人好友 和 组织好友 ) 列表  */
	public static void doGetFriends(Context context, IBindData bind, JSONObject jsonObject, Handler handler) {
		
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.getFriends;
		doExecute(context, bind, EAPIConsts.concReqType.getFriends, url, requestStr, handler);
	}
	
	/** 根据类型获得指定类型关系及 全部类型关系 列表    {'organizationFriend':true, 'personFriend':true, 'customer':false, 'person':false} */
	public static void doGetAllRelations(Context context, IBindData bind, RequestType requestType, Handler handler) {
		
		String requestStr = "";
		try {
			JSONObject jObject = new JSONObject();
			if(requestType == RequestType.All){
				jObject.put("organizationFriend", true);
				jObject.put("personFriend", true);
				jObject.put("customer", true);
				jObject.put("person", true);
			}
			requestStr = jObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.getAllRelations;
		doExecute(context, bind, EAPIConsts.concReqType.getAllRelations, url, requestStr, handler);
	}
	
	/** 根据类型获得指定类型关系及 全部类型关系 列表    {'organizationFriend':true, 'personFriend':true, 'customer':false, 'person':false} */
	public static void doGetOrgRelations(Context context, IBindData bind, RequestType requestType, Handler handler) {
		
		String requestStr = "";
		try {
			JSONObject jObject = new JSONObject();
			if(requestType == RequestType.All){
				jObject.put("organizationFriend", true);
				jObject.put("personFriend", false);
				jObject.put("customer", false);
				jObject.put("person", false);
			}
			requestStr = jObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.getAllRelations;
		doExecute(context, bind, EAPIConsts.concReqType.getAllRelations, url, requestStr, handler);
	}

	/**
	 * 获得人脉详情
	 * 
	 * @return
	 */
	public static void doContactDetail(Context context, IBindData bind, JSONObject jsonObject, Handler handler) {
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.im_ContactDetail;
		doExecute(context, bind, EAPIConsts.concReqType.ContactDetail, url, requestStr, handler);
	}

	/**
	 * 获得机构详情
	 * 
	 * @return
	 */
	public static void dogetOrganizationDetail(Context context, IBindData bind, JSONObject jsonObject, Handler handler) {
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.im_getOrganizationDetail;
		doExecute(context, bind, EAPIConsts.concReqType.im_getOrganizationDetail, url, requestStr, handler);
	}

	/**
	 * 添加新人脉
	 * 
	 * @return
	 */
	public static void donewContact(Context context, IBindData bind, String jsonObject, Handler handler) {
		// String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.im_addJTContact;
		doExecute(context, bind, EAPIConsts.concReqType.im_addJTContact, url, jsonObject, handler);
	}

	/**
	 * 新关系列表
	 * 
	 * @return
	 */
	public static void dogetnewConnections(Context context, IBindData bind, JSONObject jsonObject, Handler handler) {
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.im_getnewConnections;
		doExecute(context, bind, EAPIConsts.concReqType.im_getnewConnections, url, requestStr, handler);
	}

	/**
	 * 上传本地电话本
	 * 
	 * @return
	 */
	public static void uploadSim(Context context, IBindData bind, JSONObject jsonObject, Handler handler) {
		String requestStr = jsonObject.toString();
		Log.v("SIMContactActivity", "sim size: " + requestStr.length());
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.im_upphonebook;
		doExecute(context, bind, EAPIConsts.concReqType.im_upphonebook, url, requestStr, handler);
	}

	/**
	 * * 新上传电话本获得推荐人脉列表
	 * 
	 * @param context
	 * @param bind
	 * @param jsonObject
	 * @param handler
	 */
	public static void doCheckMobiles(Context context, IBindData bind, JSONObject jsonObject, Handler handler) {
		String requestStr = jsonObject.toString();

		Log.v("SIMContactActivity", "sim size: " + requestStr.length());
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.CheckMobiles;
		doExecute(context, bind, EAPIConsts.concReqType.CheckMobiles, url, requestStr, handler);
	}

	/** 添加好友 */
	public static void doReqNewFriend(Context context, IBindData bind, JSONObject jsonObject, Handler handler) {

		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.im_addFriend;
		/** 8个参数的 顺序 context 数据上下文 bind 返回接口 联网类型 请求地址 JSON串 错误处理hander */
		doExecute(context, bind, EAPIConsts.concReqType.im_addFriend, url, requestStr, handler);
	}

	/** 删除好友 */
	public static void dodeleteFriend(Context context, IBindData bind, JSONObject jsonObject, Handler handler) {

		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.im_deleteFriend;
		/** 8个参数的 顺序 context 数据上下文 bind 返回接口 联网类型 请求地址 JSON串 错误处理hander */
		doExecute(context, bind, EAPIConsts.concReqType.im_deleteFriend, url, requestStr, handler);
	}

	/** 通过好友请求 */
	public static void doallowConnectionsRequest(Context context, IBindData bind, JSONObject jsonObject, Handler handler) {

		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.im_allowConnectionsRequest;
		/** 8个参数的 顺序 context 数据上下文 bind 返回接口 联网类型 请求地址 JSON串 错误处理hander */
		doExecute(context, bind, EAPIConsts.concReqType.im_allowConnectionsRequest, url, requestStr, handler);
	}

	/** 通过好友请求 */
	public static void dogetWorkmate(Context context, IBindData bind, JSONObject jsonObject, Handler handler) {

		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.im_getWorkmate;
		doExecute(context, bind, EAPIConsts.concReqType.im_getWorkmate, url, requestStr, handler);
	}

	/** 获取详情的关系列表 */
	public static void getMatchConnectionsMini(Context context, IBindData bind, JSONObject jsonObject, Handler handler) {

		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.im_getMatchConnectionsMini;
		/** 8个参数的 顺序 context 数据上下文 bind 返回接口 联网类型 请求地址 JSON串 错误处理hander */
		doExecute(context, bind, EAPIConsts.concReqType.im_getMatchConnectionsMini, url, requestStr, handler);
	}

	/** 删除人脉（名片） */
	public static void getdelJtContact(Context context, IBindData bind, JSONObject jsonObject, Handler handler) {

		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.im_delJtContact;
		/** 8个参数的 顺序 context 数据上下文 bind 返回接口 联网类型 请求地址 JSON串 错误处理hander */
		doExecute(context, bind, EAPIConsts.concReqType.im_delJtContact, url, requestStr, handler);
	}

	/** 邀请加入金桐 */
	public static void getInviteJoinGinTong(Context context, IBindData bind, JSONObject jsonObject, Handler handler) {

		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.im_inviteJoinGinTong;
		/** 8个参数的 顺序 context 数据上下文 bind 返回接口 联网类型 请求地址 JSON串 错误处理hander */
		doExecute(context, bind, EAPIConsts.concReqType.im_inviteJoinGinTong, url, requestStr, handler);
	}

	/** 推荐给好友 */
	public static void getRecommend2Friend(Context context, IBindData bind, JSONObject jsonObject, Handler handler) {
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.im_recommend2Friend;
		/** 8个参数的 顺序 context 数据上下文 bind 返回接口 联网类型 请求地址 JSON串 错误处理hander */
		doExecute(context, bind, EAPIConsts.concReqType.im_recommend2Friend, url, requestStr, handler);
	}

	/** 获取相关的人脉和客户 */
	public static void getRelevantPeopleAndCustomer(Context context, IBindData bind, JSONObject jsonObject, Handler handler) {
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.im_relevantPeopleAndCustomer;
		/** 8个参数的 顺序 context 数据上下文 bind 返回接口 联网类型 请求地址 JSON串 错误处理hander */
		doExecute(context, bind, EAPIConsts.concReqType.im_relevantPeopleAndCustomer, url, requestStr, handler);
	}

	/** 获取新关系的个数 */
	public static void getNewConnectionsCount(Context context, IBindData bind, JSONObject jsonObject, Handler handler) {
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.im_getNewConnectionsCount;
		/** 8个参数的 顺序 context 数据上下文 bind 返回接口 联网类型 请求地址 JSON串 错误处理hander */
		doExecute(context, bind, EAPIConsts.concReqType.im_getNewConnectionsCount, url, requestStr, handler);
	}

	/** 获取动态的个数 */
	public static void getNewDynamicCount(Context context, IBindData bind, JSONObject jsonObject, Handler handler) {
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.im_getNewDynamicCount;
		/** 8个参数的 顺序 context 数据上下文 bind 返回接口 联网类型 请求地址 JSON串 错误处理hander */
		doExecute(context, bind, EAPIConsts.concReqType.im_getNewDynamicCount, url, requestStr, handler);
	}

	/** 添加机构 */
	public static void addOrginazitionGuest(Context context, IBindData bind, JSONObject jsonObject, Handler handler) {
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.im_addOrginazitionGuest;
		/** 8个参数的 顺序 context 数据上下文 bind 返回接口 联网类型 请求地址 JSON串 错误处理hander */
		doExecute(context, bind, EAPIConsts.concReqType.im_addOrginazitionGuest, url, requestStr, handler);
	}

	/** 存为本地人脉 */
	public static void im_holdJTContact(Context context, IBindData bind, JSONObject jsonObject, Handler handler) {
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.im_holdJTContact;
		/** 8个参数的 顺序 context 数据上下文 bind 返回接口 联网类型 请求地址 JSON串 错误处理hander */
		doExecute(context, bind, EAPIConsts.concReqType.im_holdJTContact, url, requestStr, handler);
	}

	/** 存为本地人脉 */
	public static void im_holdOrginazitionGuest(Context context, IBindData bind, JSONObject jsonObject, Handler handler) {
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.im_holdOrginazitionGuest;
		/** 8个参数的 顺序 context 数据上下文 bind 返回接口 联网类型 请求地址 JSON串 错误处理hander */
		doExecute(context, bind, EAPIConsts.concReqType.im_holdOrginazitionGuest, url, requestStr, handler);
	}

	/** 存为本地客户 */
	public static void im_delOrganization(Context context, IBindData bind, JSONObject jsonObject, Handler handler) {
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.im_delOrganization;
		/** 8个参数的 顺序 context 数据上下文 bind 返回接口 联网类型 请求地址 JSON串 错误处理hander */
		doExecute(context, bind, EAPIConsts.concReqType.im_delOrganization, url, requestStr, handler);
	}

	/** 存为本地客户 */
	public static void im_getJTContactTemplet(Context context, IBindData bind, JSONObject jsonObject, Handler handler) {
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.im_getJTContactTemplet;
		/** 8个参数的 顺序 context 数据上下文 bind 返回接口 联网类型 请求地址 JSON串 错误处理hander */
		doExecute(context, bind, EAPIConsts.concReqType.im_getJTContactTemplet, url, requestStr, handler);
	}

	/** 获取人脉资源 */
	public static void getPeopleRelatedResources(Context context, IBindData bind, JSONObject jsonObject, Handler handler) {
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.getPeopleRelatedResources;
		/** 8个参数的 顺序 context 数据上下文 bind 返回接口 联网类型 请求地址 JSON串 错误处理hander */
		doExecute(context, bind, EAPIConsts.concReqType.getPeopleRelatedResources, url, requestStr, handler);
	}

	/** 获取用户动态 */
	public static void getActionList(Context context, IBindData bind, JSONObject jsonObject, Handler handler) {
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.getActionList;
		doExecute(context, bind, EAPIConsts.concReqType.getActionList, url, requestStr, handler);
	}

	/** 获取用户 查看权限 */
	public static void getVisible(Context context, IBindData bind, JSONObject jsonObject, Handler handler) {
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.getVisible;
		doExecute(context, bind, EAPIConsts.concReqType.getVisible, url, requestStr, handler);
	}

	/** 设置 用户 查看权限 */
	public static void setVisible(Context context, IBindData bind, String jsonObject, Handler handler) {
		// String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.setVisible;
		doExecute(context, bind, EAPIConsts.concReqType.setVisible, url, jsonObject, handler);
	}

	/** 分享权限设置 */
	public static void getSharePart(Context context, IBindData bind, String jsonObject, Handler handler) {
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.getSharePart;
		doExecute(context, bind, EAPIConsts.concReqType.getSharePart, url, jsonObject, handler);
	}

	/** 分享详情获得 */
	public static void getShareDetail(Context context, IBindData bind, JSONObject jsonObject, Handler handler) {
		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.getShareDetail;
		doExecute(context, bind, EAPIConsts.concReqType.getShareDetail, url, requestStr, handler);
	}

	/**
	 * 获得人脉详情
	 * 
	 * @return
	 */
	public static JSONObject getContactDetailJson(String id, boolean isOnline) {
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("isOnline", isOnline);
			jObject.put("jtContactID", id + "");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}

	/**
	 * 获得机构详情
	 * 
	 * @return
	 */
	public static JSONObject getOrganizationDetailJson(String id, boolean isOnline) {
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("isOnline", isOnline);
			jObject.put("organizationID", id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}

	/**
	 * 添加好友
	 * 
	 * @return
	 */
	public static JSONObject getReqNewFriend(String id, int type) {
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("userType", type);
			jObject.put("id", id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}

	/**
	 * 删除好友
	 * 
	 * @return
	 */
	public static JSONObject getDeleteFriendJson(String id, int type) {
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("userType", type);
			jObject.put("id", id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}

	/**
	 * 通过好友
	 * 
	 * @return
	 */
	public static JSONObject getallowConnectionsRequestJson(String id, String userID,  int type) {
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("type", type);
			jObject.put("id", id);
			jObject.put("userID", userID);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}

	/**
	 * 通过好友
	 * 
	 * @return
	 */
	public static JSONObject getMatchConnectionsMiniJson(String id, int type) {
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("type", type);
			jObject.put("id", id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}

	/**
	 * 获取人脉相关资源
	 * 
	 * @return
	 */
	public static JSONObject getPeopleRelatedResourcesJson(String id, String forwhat, String range) {
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("id", id);
			jObject.put("forwhat", forwhat);
			jObject.put("range", range);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}

	/**
	 * 获取人脉相关资源
	 * 
	 * @return
	 */
	public static JSONObject getSharePartJson(String id, PeopleForm peopleForm, String ids[]) {
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("id", id);
			jObject.put("peopleForm", peopleForm);
			JSONArray jsa = new JSONArray("ids");
			for (String oneID : ids) {
				jsa.put(oneID);
			}
			jObject.put("ids", jsa);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}

	/**
	 * 获取所有评价
	 * 
	 * @param id
	 *            被评价人id
	 * @param isSelf
	 *            被评价人是否是当前用户
	 * @return
	 */
	public static void doFindEvaluate(Context context, IBindData bind, long id, boolean isSelf, Handler handler) {
		String requestStr = "";
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("userId", id);
			jObject.put("isSelf", isSelf);
			requestStr = jObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.findEvaluate;
		doExecute(context, bind, EAPIConsts.concReqType.findEvaluate, url, requestStr, handler);

	}

	public static void doFindEvaluate(Context context, IBindData bind, long id, /*boolean isSelf,*/ Handler handler, int type) {
		String requestStr = "";
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("userId", id);
//			jObject.put("isSelf", isSelf);
			requestStr = jObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.findEvaluate;//好友
		if(type == 2){
			url = EAPIConsts.TMS_URL + EAPIConsts.PeopleRequestUrl.PEOPLE_FINDEVALUATE_STR;//人脉
		}
		
		doExecute(context, bind, EAPIConsts.concReqType.findEvaluate, url, requestStr, handler);

	}

	/**
	 * 赞同/取消赞同评价
	 * 
	 * @param context
	 * @param bind
	 * @param homeUserId 主页君id
	 * @param id
	 *            评价id
	 * @param feedback
	 *            true赞同 false 不赞同
	 * @param handler
	 */
	public static void doFeedbackEvaluate(Context context, IBindData bind, long homeUserId,long id, boolean feedback, Handler handler) {
		String requestStr = "";
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("homeUserId", homeUserId);
			jObject.put("id", id);
			jObject.put("feedback", feedback);
			requestStr = jObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.feedbackEvaluate;
		doExecute(context, bind, EAPIConsts.concReqType.feedbackEvaluate, url, requestStr, handler);
	}

	/**
	 * 查询当前是否为好友 TODO
	 * 
	 * @param context
	 * @param bind
	 * @param friendId
	 * @param handler
	 */
	public static void doCheckFriend(Context context, IBindData bind, String friendId, Handler handler) {
		String requestStr = "";
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("friendId", friendId);
		requestStr = new Gson().toJson(params);
		String url = "http://192.168.120.206:3333" + EAPIConsts.ReqUrl.checkFriend;
		System.out.println("requeststr --- " + requestStr + " request------------------" + url);
		doExecute(context, bind, EAPIConsts.concReqType.checkFriend, url, requestStr, handler);
	}

	/**
	 * 获取更多评价
	 * 
	 * @param context
	 * @param bind
	 * @param id
	 *            主页君id
	 * @param handler
	 */
	public static void doGetMoreEvaluate(Context context, IBindData bind, long id, Handler handler) {
		String requestStr = "";
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("userId", id);
			requestStr = jObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.moreEvaluate;
		doExecute(context, bind, EAPIConsts.concReqType.moreEvaluate, url, requestStr, handler);
	}
	

	public static void doGetMoreEvaluate(Context context, IBindData bind, long id, Handler handler, int type) {
		String requestStr = "";
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("userId", id);
			requestStr = jObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.moreEvaluate;
		if(type == 2){
			url = EAPIConsts.TMS_URL + EAPIConsts.PeopleRequestUrl.PEOPLE_MOREEVALUATE_STR;
		}
		doExecute(context, bind, EAPIConsts.concReqType.moreEvaluate, url, requestStr, handler);
	}

	/**
	 * 添加评价
	 * 
	 * @param context
	 * @param bind
	 * @param userId
	 *            主页君id
	 * @param comment
	 *            评价内容
	 * @param handler
	 */
	public static void doAddEvaluate(Context context, IBindData bind, long userId, String comment, Handler handler) {
		String requestStr = "";
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("userId", userId);
			jObject.put("comment", comment);
			requestStr = jObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.addEvaluate;
		doExecute(context, bind, EAPIConsts.concReqType.addEvaluate, url, requestStr, handler);
	}
	
	public static void doAddEvaluate(Context context, IBindData bind, long userId, String comment, Handler handler, int type) {
		String requestStr = "";
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("userId", userId);
			jObject.put("comment", comment);
			requestStr = jObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.addEvaluate;//好友
		if(type == 2){//人脉
			url = EAPIConsts.TMS_URL + EAPIConsts.PeopleRequestUrl.PEOPLE_ADDEVALUATE_STR;
		}
		doExecute(context, bind, EAPIConsts.concReqType.addEvaluate, url, requestStr, handler);
	}

	/**
	 * 删除评价标签
	 * 
	 * @param context
	 * @param bind
	 * @param ueid
	 *            评价id
	 * @param handler
	 */
	public static void doDeleteEvaluate(Context context, IBindData bind, long ueid, Handler handler) {
		String requestStr = "";
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("id", ueid);
			requestStr = jObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.deleteEvaluate;
		doExecute(context, bind, EAPIConsts.concReqType.deleteEvaluate, url, requestStr, handler);
	}
	
	public static void doDeleteEvaluate(Context context, IBindData bind, long ueid, Handler handler, int type) {
		String requestStr = "";
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("id", ueid);
			requestStr = jObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.deleteEvaluate;
		if(type == 2){
			url = EAPIConsts.TMS_URL + EAPIConsts.PeopleRequestUrl.PEOPLE_DELETEEVALUATE_STR;
		}
		doExecute(context, bind, EAPIConsts.concReqType.deleteEvaluate, url, requestStr, handler);
	}

	/***
	 * 编辑黑名单
	 * @param context
	 * @param bind
	 * @param listUserId 用户id列表
	 * @param type 1,加入黑名单;2,移出黑名单
	 * @param handler
	 */
	public static void doEditBlack(Context context, IBindData bind, ArrayList<String> listUserId, String type, Handler handler) {
		String requestStr = "";
		JSONArray array = new JSONArray();
		try {
			for (int i = 0; i < listUserId.size(); i++) {
				array.put(listUserId.get(i));
			}
			JSONObject object = new JSONObject();
			object.put("listUserId", array);
			object.put("type", type);
			requestStr = object.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.editBlack;
		doExecute(context, bind, EAPIConsts.concReqType.editBlack, url, requestStr, handler);
	}
	
	/**
	 * 根据感兴趣的行业推送用户列表 
	 * @param context
	 * @param bind
	 * @param handler
	 */
	public static void doPushPeopleList(Context context, IBindData bind, Handler handler) {
		String requestStr = "";
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("", "");
			requestStr = jObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.pushPeopleList;
		doExecute(context, bind, EAPIConsts.concReqType.pushPeopleList, url, requestStr, handler);
	}
	/**
	 * 转发
	 * @param context
	 * @param bind
	 * @param forwardDynamicNews
	 * @param handler
	 */
	public static void doAddDynamic(Context context, IBindData bind, ForwardDynamicNews forwardDynamicNews, Handler handler){
		String requestStr = "";
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("forwardDynamicNews", forwardDynamicNews.toJSONObject());
			requestStr = jObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.addDynamic;
		doExecute(context, bind, EAPIConsts.concReqType.addDynamic, url, requestStr, handler);
	}
	
	/**
	 *  发送邀请加入金桐短信
	 * @param context
	 * @param bind
	 * @param listMobile
	 * @param handler
	 */
	public static void doSendSMS(Context context, IBindData bind, ArrayList<String> listMobile , Handler handler){
		String requestStr = "";
		JSONArray array = new JSONArray();
		try {
			for (int i = 0; i < listMobile.size(); i++) {
				array.put(listMobile.get(i));
			}
			JSONObject object = new JSONObject();
			object.put("listMobile", array);
			requestStr = object.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.sendSMS;
		doExecute(context, bind, EAPIConsts.concReqType.sendSMS, url, requestStr, handler);
	}
	
	/**
	 * 获取黑名单列表
	 * @param context
	 * @param bind
	 * @param index
	 * @param size
	 * @param handler
	 */
	public static void doGetBlacklist(Context context, IBindData bind, int index,int size, Handler handler){
		String requestStr = "";
		
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("index", index);
			jObject.put("size", size);
			requestStr = jObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.blacklist;
		doExecute(context, bind, EAPIConsts.concReqType.blacklist, url, requestStr, handler);
	}	
	/**
	 *  批量添加好友 
	 * @param context
	 * @param bind
	 * @param listId
	 * @param handler
	 */
	public static void doAddFriends(Context context, IBindData bind, ArrayList<String> listId , Handler handler){
		String requestStr = "";
		JSONArray array = new JSONArray();
		try {
			for (int i = 0; i < listId.size(); i++) {
				array.put(listId.get(i));
			}
			JSONObject object = new JSONObject();
			object.put("listId", array);
			requestStr = object.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.addFriends;
		doExecute(context, bind, EAPIConsts.concReqType.addFriends, url, requestStr, handler);
	}

}
