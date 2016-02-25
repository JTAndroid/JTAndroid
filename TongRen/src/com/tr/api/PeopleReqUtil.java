package com.tr.api;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.utils.http.EAPIConsts.PeopleRequestType;
import com.utils.http.EAPIConsts;
import com.utils.http.EAPIConsts.PeopleRequestUrl;
import com.utils.http.IBindData;
import com.utils.log.KeelLog;

public class PeopleReqUtil extends ReqBase {
	
	/**
	 * 人脉请求接口
	 * 
	 * @param context
	 * @param bind
	 * @param obj	请求参数
	 * @param handler
	 * @param peopleReqType	人脉请求类型
	 */
	public static void doRequestWebAPI(Context context, IBindData bind,
			Object obj, Handler handler, int peopleReqType) {
		
		String requestParam = "";
		Gson gson = new Gson();
		
		requestParam = gson.toJson(obj);
		String url = EAPIConsts.TMS_URL;
		switch (peopleReqType) {
			case PeopleRequestType.PEOPLE_REQ_GETPEOPLE:	//获取人脉详情
				url += PeopleRequestUrl.PEOPLE_DETAIL_STR;
//				url = "http://192.168.171.106:81/" + PeopleRequestUrl.PEOPLE_DETAIL_STR;
				KeelLog.d("==>>url", url);
				
				break;
			case PeopleRequestType.PEOPLE_REQ_CREATE: // 新增/修改人脉
				url += PeopleRequestUrl.CREATE_PEOPLE_STR;
				break;
			case PeopleRequestType.PEOPLE_REQ_PEOPLELIST: // 人脉列表
				url += PeopleRequestUrl.PEOPLE_LIST_STR;
				break;
			case PeopleRequestType.PEOPLE_REQ_REMOVE: // 删除人脉
				url += PeopleRequestUrl.REMOVE_PEOPLE_STR;
				break;
			case PeopleRequestType.PEOPLE_REQ_HOME://人脉首页列表
				url += PeopleRequestUrl.PEOPLE_HOMELIST_STR;
				System.out.println("人脉首页列表请求了数据");
				break;
			
			case PeopleRequestType.PEOPLE_REQ_CONVERTPEOPLE:		//转为人脉
				url+=PeopleRequestUrl.PEOPLE_CONVERT_STR;
				break;
			case PeopleRequestType.PEOPLE_REQ_MERGELIST: // 可合并资料的人脉列表
				url += PeopleRequestUrl.PEOPLE_MERGELIST_STR;
				break;
			case PeopleRequestType.PEOPLE_REQ_MERGE: // 合并人脉资料
				url += PeopleRequestUrl.PEOPLE_MERGE_STR;
				break;
			case PeopleRequestType.PEOPLE_REQ_MEET_SAVE: // 保存会面记录
				url += PeopleRequestUrl.PEOPLE_MEET_SAVE_STR;
				break;
			case PeopleRequestType.PEOPLE_REQ_MEET_UPDATE: // 修改会面记录
				url += PeopleRequestUrl.PEOPLE_MEET_UPDATE_STR;
				break;
			case PeopleRequestType.PEOPLE_REQ_MEET_FINDLIST: // 查询会面情况列表
				url += PeopleRequestUrl.PEOPLE_MEET_FINDLIST_STR;
				break;
			case PeopleRequestType.PEOPLE_REQ_MEET_FINDONE: // 查询会面情况
				url += PeopleRequestUrl.PEOPLE_MEET_FINDONE_STR;
				break;
			case PeopleRequestType.PEOPLE_REQ_MEET_DELETE:		//删除会面情况
				url+=PeopleRequestUrl.PEOPLE_MEET_DELETE_STR;
				break;
			case PeopleRequestType.PEOPLE_REQ_SAVEORUPDATECATEGORY: // 新增、修改目录
				url += PeopleRequestUrl.PEOPLE_SAVEORUPDATECATEGORY_STR;
				break;
			case PeopleRequestType.PEOPLE_REQ_FINDCATEGORY: // 查询目录
				url += PeopleRequestUrl.PEOPLE_FINDCATEGORY_STR;
				break;
			case PeopleRequestType.PEOPLE_REQ_REMOVECATEGORY: // 删除目录
				url += PeopleRequestUrl.PEOPLE_REMOVECATEGORY_STR;
				break;
			case PeopleRequestType.PEOPLE_REQ_COLLECTPEOPLE: // 收藏人脉
				url += PeopleRequestUrl.PEOPLE_COLLECTPEOPLE_STR;
				break;
			case PeopleRequestType.PEOPLE_REQ_CANCELCOLLECT: // 取消收藏
				url += PeopleRequestUrl.PEOPLE_CANCELCOLLECT_STR;
				break;
			case PeopleRequestType.PEOPLE_REQ_PEOPLECODELIST: // 职业列表查询、分类列表查询
				url += PeopleRequestUrl.PEOPLE_PEOPLECODELIST_STR;
				break;
			case PeopleRequestType.PEOPLE_REQ_FINDEVALUTE: // 获取该用户的评价
				url += PeopleRequestUrl.PEOPLE_FINDEVALUATE_STR;
				break;
			case PeopleRequestType.PEOPLE_REQ_ADDEVALUATE: // 添加评价
				url += PeopleRequestUrl.PEOPLE_ADDEVALUATE_STR;
				break;
			case PeopleRequestType.PEOPLE_REQ_FEEDBACKEVALUATE: // 赞同与取消赞同
				url += PeopleRequestUrl.PEOPLE_FEEDBACKEVALUATE_STR;
				break;
			case PeopleRequestType.PEOPLE_REQ_DELETEEVALUATE: // 删除评价
				url += PeopleRequestUrl.PEOPLE_DELETEEVALUATE_STR;
				break;
			case PeopleRequestType.PEOPLE_REQ_MOREEVALUATE: // 更多评价
				url += PeopleRequestUrl.PEOPLE_MOREEVALUATE_STR;
				break;
			case PeopleRequestType.REPORT_SAVE:  	// 41 人脉举报person/report/save.json
				url += PeopleRequestUrl.REPORT_SAVE_STR;
				break;
			case PeopleRequestType.COLLECT_PEOPLE:  		//13 收藏人脉        /categoryRelation/collectPeople.json
				url += PeopleRequestUrl.COLLECT_PEOPLE_STR;
				break;
			case PeopleRequestType.CANCEL_COLLECT:  		//14 取消收藏人脉  /categoryRelation/cancelCollect.json
				url += PeopleRequestUrl.CANCEL_COLLECT_STR;
				break;
			default:
				break;
		}
		doExecute(context, bind, peopleReqType, url, requestParam, handler);
	}
	
	/**
	 * 确定当前接口的地址
	 * 
	 * @param method
	 * @return
	 */
	public static String getFullUrl(String method) {
		return EAPIConsts.getTMSUrl() + method;
	}
	
	/**
	 * 添加标签
	 * 
	 * @param context
	 * @param bind
	 * @param tag
	 *            :标签值
	 */
	public static boolean addTag(Context context, IBindData bind, String tag, Handler handler) {
		JsonObject json = new JsonObject();
		json.addProperty("tagName", tag);
		String requestStr = json.toString();
		String url = EAPIConsts.getTMSUrl() + EAPIConsts.PeopleRequestUrl.PEOPLE_SAVE_TAG;
		doExecute(context, bind, EAPIConsts.PeopleRequestType.PEOPLE_REQ_TAG_SAVE, url,
				requestStr, handler);
		return true;
	}
	
	/**
	 * 查询用户的标签使用个数
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @return
	 */
	public static boolean getPeopleTag(Context context, IBindData bind,
			Handler handler) {
		JsonObject json = new JsonObject();
		json.addProperty("index", 1);
		json.addProperty("size", 20); // 页数和大小没有用
		String requestStr = json.toString();
		String url = getFullUrl(EAPIConsts.PeopleRequestUrl.PEOPLE_QUERY_TAG);
		doExecute(context, bind, EAPIConsts.PeopleRequestType.PEOPLE_REQ_TAG_QUERY,
				url, requestStr, handler);
		return true;
	}
	
	/**
	 * 删除标签信息接口
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @return
	 */
	public static boolean deleteTag(Context context, IBindData bind, int id,
			Handler handler) {
		JSONObject json = new JSONObject();
		try {
			json.put("id", id);
			String requestStr = json.toString();
			String url = getFullUrl(EAPIConsts.PeopleRequestUrl.PEOPLE_DELETE_TAG);
			doExecute(context, bind, EAPIConsts.PeopleRequestType.PEOPLE_REQ_TAG_DELETE,
					url, requestStr, handler);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 修改和保存标签
	 * 
	 * @param context
	 * @param bind
	 * @param tag
	 *            :标签值
	 * @param id
	 *            : 修改时 传id 保存 时 不用传递id 默认为0
	 * @param handler
	 * @return
	 */
	public static boolean saveTag(Context context, IBindData bind, String tag,
			int id, Handler handler) {
		JsonObject json = new JsonObject();
		json.addProperty("tag", tag);
		json.addProperty("id", id);
		String requestStr = json.toString();
		String url = getFullUrl(EAPIConsts.PeopleRequestUrl.PEOPLE_SAVE_TAG);
		doExecute(context, bind, EAPIConsts.PeopleRequestType.PEOPLE_REQ_TAG_SAVE, url,
				requestStr, handler);
		return true;
	}
	
	/**
	 * 获取金桐网推荐标签信息
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @return
	 */
	public static boolean getTagList(Context context, IBindData bind,
			Handler handler) {
		JsonObject json = new JsonObject();
		json.addProperty("type", 1);
		json.addProperty("size", 0);
		json.addProperty("index", 50);
		String requestStr = json.toString();
		String url = getFullUrl(EAPIConsts.PeopleRequestUrl.PEOPLE_LIST_TAG);
		doExecute(context, bind, EAPIConsts.PeopleRequestType.PEOPLE_REQ_TAG_LIST, url,
				requestStr, handler);
		return true;
	}
	
	
	/**
	 * 查询我的标签信息
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @return
	 */
	public static boolean queryMyTag(Context context, IBindData bind,
			int index, int size, Handler handler) {
		JsonObject json = new JsonObject();
//			json.put("index", index);
//			json.put("size", size);
			json.addProperty("type", 2);
			json.addProperty("size", 0);
			json.addProperty("index", 999999999);
			String requestStr = json.toString();
			String url = getFullUrl(EAPIConsts.PeopleRequestUrl.PEOPLE_MY_TAG);
			doExecute(context, bind, EAPIConsts.PeopleRequestType.PEOPLE_REQ_TAG_MY,
					url, requestStr, handler);
			return true;
	}
	
	/**
	 * 通过标签获取需求信息列表数据
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @return
	 */
	public static boolean getTagPeopleList(Context context, IBindData bind,
			int size, int page, int tagid, Handler handler) {
		JSONObject json = new JSONObject();
		try {
			json.put("size", size);
			json.put("page", page);
			json.put("tagId", tagid);
			String requestStr = json.toString();
			String url = getFullUrl(EAPIConsts.PeopleRequestUrl.PEOPLE_REQ_LIST_TAG);
			doExecute(context, bind,
					EAPIConsts.PeopleRequestType.PEOPLE_REQ_LIST_TAG, url,
					requestStr, handler);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 删除标签关系
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @return
	 */
	public static boolean deletePeopleTag(Context context, IBindData bind,
			String demandIds, int tagid, Handler handler) {
		JSONObject json = new JSONObject();
		try {
			json.put("demandIds", demandIds);
			json.put("tagId", tagid);
			String requestStr = json.toString();
			String url = getFullUrl(EAPIConsts.PeopleRequestUrl.PEOPLE_DELETE_MY_TAG);
			doExecute(context, bind,
					EAPIConsts.PeopleRequestType.PEOPLE_DELETE_TAG, url,
					requestStr, handler);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}
}
