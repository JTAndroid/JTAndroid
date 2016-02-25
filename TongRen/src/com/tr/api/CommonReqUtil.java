package com.tr.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.tr.model.home.MCustomzation;
import com.tr.model.home.MUserProfile;
import com.tr.model.joint.ResourceNode;
import com.tr.model.joint.ResourceNodeMini;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;
/**
 * 通用请求工具类 用于 
 * @author gushi
 *
 */
public class CommonReqUtil extends ReqBase {

	private final static String TAG = CommonReqUtil.class.getSimpleName();

	/** 获取关联资源
	 * 
	 * @param context
	 * @param bind
	 * @param keyword
	 * @param type
	 * @param page  2015/10/19新增分页加载的页数
	 * @param size  2015/10/19新增分页加载的数量
	 * @param handler */
	public static void doRelatedResource(Context context, IBindData bind, String keyword, int type,int page,int size, Handler handler) {

		String requestStr = "";
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("keyword", keyword);
			jObject.put("type", type);
			jObject.put("page", page);
			jObject.put("size", size);
			requestStr = jObject.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.GetRelatedResource;
		doExecute(context, bind, EAPIConsts.CommonReqType.GetRelatedResource, url, requestStr, handler);
	}

	/** 获取对接资源
	 * 
	 * @param context
	 * @param bind
	 * @param handler 
	 * "targetId":对接的物料id,
        "targetType":[
                1  知识
                2  需求
                3  组织
                4  人脉
                5  会议],
        "page":页数,
        "rows":条数,
        "scope":[范围
                1  我的
                2  好友的
                3  金桐脑的]
	 * */
	public static void doGetJointResource(Context context, IBindData bind, String targetId, int targetType, int scope, int rows,int page, Handler handler) {

		String requestStr = "";
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("targetId", targetId);
			jObject.put("targetType", targetType);
			jObject.put("scope", scope);
			jObject.put("rows", rows);
			jObject.put("page", page);
			requestStr = jObject.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.GetJointResources;
		doExecute(context, bind, EAPIConsts.CommonReqType.GetJointResource, url, requestStr, handler);
	}
	
	public static void doGetJointResource_new(Context context, IBindData bind, String targetId, int targetType, int scope, int rows,int page, Handler handler) {

		String requestStr = "";
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("targetId", targetId);
			jObject.put("targetType", targetType);
			jObject.put("scope", scope);
			jObject.put("rows", rows);
			jObject.put("page", page);
			requestStr = jObject.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.GetJointResources;
		int type = EAPIConsts.CommonReqType.GetJointResource_New;
		switch(scope){
		case 1:
			type = EAPIConsts.CommonReqType.GetJointResource_MY;
			break;
		case 2:
			type = EAPIConsts.CommonReqType.GetJointResource_FRIEND;
			break;
		case 3:
			type = EAPIConsts.CommonReqType.GetJointResource_GT;
			break;
		}
		doExecute(context, bind, type, url, requestStr, handler);
	}

	/** 生态对接纠错
	 * 
	 * @param context
	 * @param bind
	 * @param targetResourceType
	 *            目标资源类型
	 * @param targetResourceId
	 *            目标资源id
	 * @param listResourceNode
	 * @param handler */
	public static void doCorrectJointResource(Context context, IBindData bind, int targetResourceType, String targetResourceId, List<ResourceNode> listResourceNode, Handler handler) {

		String requestStr = "";

		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("targetType", targetResourceType);
		params.put("targetId", targetResourceId);
		// params.put("targetSubType", targetResourceSubType);
		List<ResourceNodeMini> listResourceNodeMini = new ArrayList<ResourceNodeMini>();
		for (ResourceNode node : listResourceNode) {
			listResourceNodeMini.add(node.toResourceNodeMini());
		}
		params.put("listJointResourceColumn", listResourceNodeMini);// 对接资源栏目
		requestStr = new Gson().toJson(params);

		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.CorrectJointResult;
		doExecute(context, bind, EAPIConsts.CommonReqType.CorrectJointResult, url, requestStr, handler);
	}

	/** 解析外网url
	 * 
	 * @param context
	 * @param bind
	 * @param externalUrl
	 * @param isCreate  "创建知识-true,不创建知识-false"
	 * @param handler */
	public static void doFetchExternalKnowledgeUrl(Context context, IBindData bind, String externalUrl, boolean isCreate, Handler handler) {

		String requestStr = "";
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("externalUrl", externalUrl);
		params.put("isCreate", isCreate);
		requestStr = new Gson().toJson(params);
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.FetchExternalKnowledgeUrl;
		doExecute(context, bind, EAPIConsts.CommonReqType.FetchExternalKnowledgeUrl, url, requestStr, handler);
	}

	/** 获取感兴趣的行业列表
	 * 
	 * @param context
	 * @param pageIndex
	 * @param pageSize */
	public static void doGetInterestIndustry(Context context, IBindData bind, int pageIndex, int pageSize, Handler handler) {
		String requestStr = "";
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("index", pageIndex);
		params.put("size", pageSize);
		requestStr = new Gson().toJson(params);
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.GetInterestIndustry;
		doExecute(context, bind, EAPIConsts.CommonReqType.GetInterestIndustry, url, requestStr, handler);
	}

	/** 获取首页数量信息
	 * 
	 * @param context
	 * @param bind
	 * @param type
	 * @param handler */
	public static void doGetMyCountList(Context context, IBindData bind, int type, Handler handler) {
		String requestStr = "";
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("type", type);
		requestStr = new Gson().toJson(params);
		String url = EAPIConsts.getTMSUrl() + EAPIConsts.ReqUrl.GetMyCountList;
		doExecute(context, bind, EAPIConsts.CommonReqType.GetMyCountList, url, requestStr, handler);
	}

	/** 上传用户信息
	 * 
	 * @param context
	 * @param bind
	 * @param projectType
	 * @param handler */
	public static void doUploadUserProfile(Context context, IBindData bind, MUserProfile mUserProfile, Handler handler) {
/*		ValueFilter filter = new ValueFilter() {
			@Override
			public Object process(Object obj, String s, Object v) {
				if (v == null || (v instanceof String && StringUtils.isEmpty((String) v))) {
					return "";
				}
				return v;
			}
		};*/
//		String jsonStr = JSON.toJSONString(mUserProfile, filter);
		
		Gson gson = new Gson();
		/*这里将javabean转化成json字符串*/
		String jsonStr = gson.toJson(mUserProfile);

		String url = EAPIConsts.getTMSUrl() + EAPIConsts.ReqUrl.uploadUserProfile;
		doExecute(context, bind, EAPIConsts.CommonReqType.UploadUserProfile, url, jsonStr, handler);
	}
	
	/**
	 * 检查与friendId是否为好友
	 * @param context
	 * @param bind
	 * @param type 0 好友  1组织
	 * @param projectType
	 * @param handler
	 */
	public static void doGetCheckFriend(Context context, IBindData bind, String friendId, int type, Handler handler) {
		String requestStr = "";
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("friendId", friendId);
		params.put("type", type);
		requestStr = new Gson().toJson(params);
		String url = EAPIConsts.getTMSUrl() + EAPIConsts.ReqUrl.checkFriend;
		doExecute(context, bind, EAPIConsts.CommonReqType.CHECK_FRIEND, url, requestStr, handler);
	}
	
	/**
	 * 从服务器获取二维码
	 * @param context
	 * @param bind
	 * @param userId
	 * @param handler
	 */
	public static void doGetUserQRUrl(Context context, IBindData bind, String userId, Handler handler) {
		String requestStr = "";
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		requestStr = new Gson().toJson(params);
		String url = EAPIConsts.getTMSUrl() + EAPIConsts.ReqUrl.getUserQRUrl;
		doExecute(context, bind, EAPIConsts.CommonReqType.getUserQRUrl, url, requestStr, handler);
	}
	
	/**
	 * 获取城市
	 * @param context
	 * @param bind
	 * @param userId
	 * @param handler
	 */
	public static void doGetCountryCode(Context context, IBindData bind, Handler handler) {
		String requestStr = "";
		//HashMap<String, Object> params = new HashMap<String, Object>();
		//requestStr = new Gson().toJson(params);
		String url = EAPIConsts.getTMSUrl() + EAPIConsts.ReqUrl.getCountryCode;
		doExecute(context, bind, EAPIConsts.CommonReqType.getCountryCode, url, requestStr, handler);
	}
	
	/**
	 * 定制行业
	 * @param context
	 * @param bind
	 * @param customzation
	 * @param handler
	 */
	public static void doSetCustomMade(Context context, IBindData bind,MCustomzation customzation, Handler handler) {
		String requestStr = "";
/*		ValueFilter filter = new ValueFilter() {
			@Override
			public Object process(Object obj, String s, Object v) {
				if (v == null || v instanceof String && StringUtils.isEmpty((String) v)) {
					return null;
				}
				return v;
			}
		};*/
//		requestStr = JSON.toJSONString(customzation, filter);
		Gson gson = new Gson();
		/*这里将javabean转化成json字符串*/
		requestStr = gson.toJson(customzation);
		String url = EAPIConsts.getTMSUrl() + EAPIConsts.ReqUrl.setCustomMade;
		doExecute(context, bind, EAPIConsts.CommonReqType.setCustomMade, url, requestStr, handler);
	}
	
	/**
	 * 查询用户目录
	 * @param context
	 * @param bind
	 */
	public static void getCategoryQueryTree(Context context, IBindData bind, Handler handler) {
		String requestStr = "";
		
		String url = EAPIConsts.getTMSUrl() + EAPIConsts.ReqUrl.CATEGORY_REQ_TAG_QUERY;
		doExecute(context, bind, EAPIConsts.CommonReqType.getCategoryQueryTree, url, requestStr, handler);
	}
	
}
