package com.tr.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tr.App;
import com.tr.model.home.GetMyAffairs;
import com.tr.model.home.MGetDynamic;
import com.tr.model.home.MGetFlow;
import com.tr.model.home.MGetMyKnowledge;
import com.tr.model.home.MGetMyRequirement;
import com.tr.model.home.MGetSearchIndexList;
import com.tr.model.home.MGetSearchList;
import com.tr.model.home.MSuggestionType;
import com.tr.model.home.MainPageList;
import com.tr.model.obj.Comment;
import com.tr.model.obj.Connections;
import com.tr.model.obj.DynamicComment;
import com.tr.ui.flow.model.FlowASSO;
import com.tr.ui.flow.model.FlowResult;
import com.tr.ui.organization.model.notice.CustomerNotice;
import com.utils.common.GlobalVariable;
import com.utils.http.EAPIConsts;

/**
 * @author 作者姓名 E-mail: email地址
 * @version 创建时间：2014-4-11 上午10:14:20 类说明 im api请求返回数据后的解析处理在这里
 */
public class HomeRespFactory {
	private static SharedPreferences firstuse_sp_json;
	private static SharedPreferences.Editor firstuse_edtior_json;
	
	public static Object createMsgObject(int msgId, JSONObject response) {
		switch (msgId) {
		case EAPIConsts.HomeReqType.HOME_REQ_GET_FLOW: {
			return MGetFlow.createFactory(response);
		}
		case EAPIConsts.HomeReqType.HOME_REQ_ADD_COMMENT: {
			return Comment.createFactoryWithName(response);
		}
		case EAPIConsts.HomeReqType.HOME_REQ_ADD_FLOW: {// 新增和修改自定义栏目
			// /customer/column/save.json
		if (response == null) {
		return null;
		}
		Long v = response.optLong("dynamicId");
		System.out.println(v + "vvvvvvvvvvvvvvvvvvvvvvvvvvv");
		return v;

		}
		case EAPIConsts.HomeReqType.HOME_REQ_ADD_DYNAMIC_COMMENT: {
			return DynamicComment.createFactoryWithName(response);
		}
		case EAPIConsts.HomeReqType.HOME_REQ_GET_MY_REQUIREMENT: {
			return MGetMyRequirement.createFactory(response);
		}
		case EAPIConsts.HomeReqType.HOME_REQ_GET_MY_AFFAIR: {
			return GetMyAffairs.createFactory(response);
		}
		case EAPIConsts.HomeReqType.HOME_REQ_GET_MY_KNOWLEDGE: {
			return MGetMyKnowledge.createFactory(response);
		}
		case EAPIConsts.HomeReqType.HOME_REQ_GET_SEARCH_INDEX_LIST:{
			return MGetSearchIndexList.createFactory(response);
		}
		case EAPIConsts.HomeReqType.HOME_REQ_GET_SEARCH_LIST:
		case EAPIConsts.HomeReqType.HOME_REQ_GET_SEARCH_LIST_PERSON:
		case EAPIConsts.HomeReqType.HOME_REQ_GET_SEARCH_LIST_ORG:
		case EAPIConsts.HomeReqType.HOME_REQ_GET_SEARCH_LIST_KNOWLEDGE:
		case EAPIConsts.HomeReqType.HOME_REQ_GET_SEARCH_LIST_MEET:
		case EAPIConsts.HomeReqType.HOME_REQ_GET_SEARCH_LIST_DEMEND:
		{
			return MGetSearchList.createFactory(response);
		}
		case EAPIConsts.HomeReqType.HOME_REQ_GET_SEARCH_LIST_MEETING: {
			return MGetSearchList.createFactoryforMeeting(response);
		}
		case EAPIConsts.HomeReqType.HOME_REQ_GET_MY_DYNAMIC:
		case EAPIConsts.HomeReqType.HOME_REQ_GET_GT_DYNAMIC:
		case EAPIConsts.HomeReqType.HOME_REQ_GET_DYNAMIC: { //获取动态
			return MGetDynamic.createNewFactory(response);
		}
		case EAPIConsts.HomeReqType.HOME_REQ_GETCONNECTORORG: { //获取对象是人还是组织
			if (response.has("type")) {
				int type = 0 ;
				try {
					type = response.getInt("type");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return type;
			}
		}
		case EAPIConsts.HomeReqType.HOME_REQ_DELETE_DYNAMIC_COMMENT: {
			if (response.has("result")) {
				String success = null;
				try {
					success = response.getString("result");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return success;
			}
		}
		// 添加赞同
		case EAPIConsts.HomeReqType.HOME_REQ_ADD_DYNAMIC_PRAISE: {
			if (response.has("id")) {
				long id = 0 ;
				try {
					id = response.getLong("id");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return id;
			}
		}
		// 取消赞同
		case EAPIConsts.HomeReqType.HOME_REQ_CANCEL_DYNAMIC_PRAISE: {
			if (response.has("succeed")) {
				String success = null;
				try {
					success = response.getString("succeed");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return success;
			}
		}
		// 增加意见建议
		case EAPIConsts.HomeReqType.HOME_REQ_ADD_SUGGESTION: {
			if (response.has("succeed")) {
				String success = null;
				try {
					success = response.getString("succeed");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return success;
			}
		}
		//获取建议类型
		case EAPIConsts.HomeReqType.HOME_REQ_GET_SUGGESTION_TYPE: {
			Gson gson = new Gson();
			Map<String, Object> dataMap = new HashMap<String, Object>(); // 返回数据
			if (response.has("succeed")) {
				if (response.has("list")) {
					try {
						String jsonStr = response.getJSONArray("list").toString();
						List<MSuggestionType> customerNoticeList = gson.fromJson(jsonStr, new TypeToken<List<MSuggestionType>>() {}.getType());
						if (customerNoticeList != null) {
							dataMap.put("list", customerNoticeList);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					return dataMap;
				}
			} 
		}
		// 添加赞同
		case EAPIConsts.HomeReqType.HOME_REQ_ADD_APPORVE: 
		case EAPIConsts.HomeReqType.HOME_REQ_ADD_GT_APPORVE:{
			if (response.has("approveId")) {
				long id = 0 ;
				try {
					id = response.getLong("approveId");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return id;
			}
		}
		// 添加评论
		case EAPIConsts.HomeReqType.HOME_REQ_ADD_DYNAMIC_COMMENT_NEW: 
		case EAPIConsts.HomeReqType.HOME_REQ_ADD_GT_DYNAMIC_COMMENT: {
			if (response.has("commentId")) {
				long id = 0 ;
				try {
					id = Long.parseLong(response.getString("commentId"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return id;
			}
		}
		case EAPIConsts.HomeReqType.HOME_REQ_DEL_FLOW:
		case EAPIConsts.HomeReqType.HOME_REQ_DEL_APPORVE:
		case EAPIConsts.HomeReqType.HOME_REQ_DELETE_DYNAMIC_COMMENT_NEW: 
		case EAPIConsts.HomeReqType.HOME_REQ_DEL_GT_APPORVE:
		case EAPIConsts.HomeReqType.HOME_REQ_DELETE_GT_DYNAMIC_COMMENT: {
			if (response.has("code")) {
				String code = null;
				try {
					code = response.getString("code");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return code;
			}
		}
		case EAPIConsts.HomeReqType.HOME_REQ_GET_DYNAMIC_ASSO:{
			Gson gson = new Gson();
			FlowResult reslut = gson.fromJson(response.toString(), FlowResult.class);
			return reslut;
		}
		case EAPIConsts.HomeReqType.HOME_PAGE_LIST: {
			Gson gson = new Gson();
			String jsonStr = response.toString();
			try {
				if(jsonStr==null){
					return null;
				}
				firstuse_sp_json = App.getApplicationConxt().getSharedPreferences(
						GlobalVariable.SHARED_PREFERENCES_INDEX_JSON,
						App.getApplicationConxt().MODE_PRIVATE);
				firstuse_edtior_json = firstuse_sp_json.edit();
				firstuse_edtior_json.putString(GlobalVariable.MAIN_INDEX_JSON, jsonStr);
				firstuse_edtior_json.commit();
				ArrayList<MainPageList> mainPageLists = new ArrayList<MainPageList>(); // 返回数据
				if(response.has("indexAdvertiseList")){
					JSONArray connJsArr=response.getJSONArray("indexAdvertiseList");
					mainPageLists = gson.fromJson(connJsArr.toString(), new TypeToken<List<MainPageList>>() {}.getType());
				}
				return mainPageLists;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	return null;
	}
	
}
