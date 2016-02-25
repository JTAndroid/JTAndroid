package com.tr.api; 

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.tr.model.home.MSuggestion;
import com.tr.model.obj.DynamicApprove;
import com.tr.model.obj.DynamicComment;
import com.tr.model.obj.DynamicNews;
import com.tr.ui.flow.model.DynamicNewsApprove;
import com.tr.ui.flow.model.DynamicNewsRequest;
import com.tr.ui.search.SearchActivity_new;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * @ClassName:     HomeReqUtil.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * @author         xuxinjian
 * @version        V1.0  
 * @Date           2014-4-18 下午4:41:18
 */
public class HomeReqUtil extends ReqBase {
	
	public static Gson gson = new Gson();

	/**
	 * 获取首页动态
	 * @param context
	 * @param bind
	 * @param handler
	 */ 
	public static boolean getFlow(Context context, int index, int size, IBindData bind, Handler handler,int filterType) {
		try {
			JSONObject inObj = new JSONObject();
			inObj.put("type", filterType);
			inObj.put("index", index);
			inObj.put("size", size);
			String requestStr = inObj.toString();

			String url = getFullUrlWithDir("/feed/getFlow.json");
			doExecute(context, bind, EAPIConsts.HomeReqType.HOME_REQ_GET_FLOW, url, requestStr, handler);
			return true;
		} catch (Exception e) {
			
		}	
		return false;
	}
	/**
	 * 发布动态（新框架）
	 * @param context
	 * @param bind
	 * @param handler
	 */ 
	public static boolean addFlow(Context context, IBindData bind,DynamicNewsRequest dynamicNews, Handler handler) {
		try {
			JSONObject inObj = new JSONObject();
			String requestParam = "";
			Gson gson = new Gson();
			requestParam = gson.toJson(dynamicNews);
			String url = getDynamicNewsURL("/dynamicNews/addDynamic.json");
			doExecute(context, bind, EAPIConsts.HomeReqType.HOME_REQ_ADD_FLOW, url, requestParam, handler);
			return true;
		} catch (Exception e) {
			
		}	
		return false;
	}
	
	/**
	 * 删除动态
	 * @param context
	 * @param bind
	 * @param handler
	 * @param dynamicId
	 * @return
	 */
	public static boolean delFlow(Context context, IBindData bind, Handler handler, long dynamicId){
		try {
			JSONObject inObj = new JSONObject();
			inObj.put("dynamicId", dynamicId);
			String requestStr = inObj.toString();

			String url = getDynamicNewsURL("/dynamicNews/deleteDynamicNews.json");
			doExecute(context, bind, EAPIConsts.HomeReqType.HOME_REQ_DEL_FLOW, url, requestStr, handler);
			return true;
		} catch (Exception e) {
			
		}	
		return false;
	}
	/**
	 * 点赞
	 * @param context
	 * @param bind
	 * @param dynamicNewsApprove
	 * @param handler
	 * @return
	 */
	public static boolean addDynamicApporve(Context context, IBindData bind,DynamicApprove dynamicApprove, Handler handler){
		try {
			JSONObject inObj = new JSONObject();
			String requestParam = "";
			Gson gson = new Gson();
			requestParam = gson.toJson(dynamicApprove);
			inObj.put("dynamicApprove", requestParam);
			String requestStr = inObj.toString();

			String url = getDynamicNewsURL("/dynamicNews/addDynamicApporve.json");
			doExecute(context, bind, EAPIConsts.HomeReqType.HOME_REQ_ADD_APPORVE, url, requestStr, handler);
			return true;
		} catch (Exception e) {
			
		}
		return false;
	}
	
	/**
	 * 取消点赞
	 * @param context
	 * @param bind
	 * @param dynamicId 动态id
	 * @param approveId 点赞id
	 * @param handler
	 * @return
	 */
	public static boolean cancelDynamicApprove(Context context, IBindData bind,long dynamicId,long approveId, Handler handler){
		try {
			JSONObject inObj = new JSONObject();
			inObj.put("dynamicId", dynamicId);
			inObj.put("approveId", approveId);
			String requestStr = inObj.toString();

			String url = getDynamicNewsURL("/dynamicNews/cancelDynamicApprove.json");
			doExecute(context, bind, EAPIConsts.HomeReqType.HOME_REQ_DEL_APPORVE, url, requestStr, handler);
			return true;
		} catch (Exception e) {
			
		}
		return false;
	}
	/**
	 * 金桐脑 点赞
	 * @param context
	 * @param bind
	 * @param dynamicNewsApprove
	 * @param handler
	 * @return
	 */
	public static boolean addDynamicGTApprove(Context context, IBindData bind,DynamicApprove dynamicApprove, Handler handler){
		try {
			JSONObject inObj = new JSONObject();
			String requestParam = "";
			Gson gson = new Gson();
			requestParam = gson.toJson(dynamicApprove);
			inObj.put("dynamicApprove", requestParam);
			String requestStr = inObj.toString();

			String url = getDynamicNewsURL("/dynamicNews/addDynamicGTApprove.json");
			doExecute(context, bind, EAPIConsts.HomeReqType.HOME_REQ_ADD_GT_APPORVE, url, requestStr, handler);
			return true;
		} catch (Exception e) {
			
		}
		return false;
	}
	
	/**
	 * 取消金桐脑点赞
	 * @param context
	 * @param bind
	 * @param dynamicId 动态id
	 * @param approveId 点赞id
	 * @param handler
	 * @return
	 */
	public static boolean cancelDynamicGTApprove(Context context, IBindData bind,long dynamicId,long approveId, Handler handler){
		try {
			JSONObject inObj = new JSONObject();
			inObj.put("dynamicId", dynamicId);
			inObj.put("approveId", approveId);
			String requestStr = inObj.toString();

			String url = getDynamicNewsURL("/dynamicNews/cancelDynamicGTApprove.json");
			doExecute(context, bind, EAPIConsts.HomeReqType.HOME_REQ_DEL_GT_APPORVE, url, requestStr, handler);
			return true;
		} catch (Exception e) {
			
		}
		return false;
	}
	
	/**
	 * 新接口  添加评论
	 * @param context
	 * @param bind
	 * @param DynamicComment
	 * @param handler
	 * @return
	 */
	public static boolean addDynamicComment(Context context, IBindData bind,DynamicComment DynamicComment, Handler handler){
		try {
			JSONObject inObj = new JSONObject();
			String requestParam = "";
			Gson gson = new Gson();
			requestParam = gson.toJson(DynamicComment);
			inObj.put("dynamicComment", requestParam);
			String requestStr = inObj.toString();

			String url = getDynamicNewsURL("/dynamicNews/addDynamicComment.json");
			doExecute(context, bind, EAPIConsts.HomeReqType.HOME_REQ_ADD_DYNAMIC_COMMENT_NEW, url, requestStr, handler);
			return true;
		} catch (Exception e) {
			
		}
		return false;
	}
	
	/**
	 * 新接口  删除评论
	 * @param context
	 * @param bind
	 * @param handler
	 * @param dynamicId
	 * @param commentId
	 * @return
	 */
	public static boolean deleteNewDynamicComment(Context context, IBindData bind, Handler handler, long dynamicId, long commentId) {
		try {
			JSONObject inObj = new JSONObject();
			inObj.put("dynamicId", dynamicId);
			inObj.put("commentId", commentId);
			String requestStr = inObj.toString();
			
			String url = getDynamicNewsURL("/dynamicNews/deleteDynamicComment.json");
			doExecute(context, bind, EAPIConsts.HomeReqType.HOME_REQ_DELETE_DYNAMIC_COMMENT_NEW,
					url, requestStr, handler);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return false;
	}

	/**
	 * 新接口  添加评论
	 * @param context
	 * @param bind
	 * @param DynamicComment
	 * @param handler
	 * @return
	 */
	public static boolean addDynamicGTComment(Context context, IBindData bind,DynamicComment DynamicComment, Handler handler){
		try {
			JSONObject inObj = new JSONObject();
			String requestParam = "";
			Gson gson = new Gson();
			requestParam = gson.toJson(DynamicComment);
			inObj.put("dynamicComment", requestParam);
			String requestStr = inObj.toString();

			String url = getDynamicNewsURL("/dynamicNews/addDynamicGTComment.json");
			doExecute(context, bind, EAPIConsts.HomeReqType.HOME_REQ_ADD_GT_DYNAMIC_COMMENT, url, requestStr, handler);
			return true;
		} catch (Exception e) {
			
		}
		return false;
	}
	
	/**
	 * 新接口  删除评论
	 * @param context
	 * @param bind
	 * @param handler
	 * @param dynamicId
	 * @param commentId
	 * @return
	 */
	public static boolean deleteDynamicGTComment(Context context, IBindData bind, Handler handler, long dynamicId, long commentId) {
		try {
			JSONObject inObj = new JSONObject();
			inObj.put("dynamicId", dynamicId);
			inObj.put("commentId", commentId);
			String requestStr = inObj.toString();
			
			String url = getDynamicNewsURL("/dynamicNews/deleteDynamicGTComment.json");
			doExecute(context, bind, EAPIConsts.HomeReqType.HOME_REQ_DELETE_GT_DYNAMIC_COMMENT,
					url, requestStr, handler);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return false;
	}
	/**
	 * 获取首页动态 （新框架）
	 * @param context
	 * @param bind
	 * @param handler
	 * @param userId 主页用户id,传入userId查看他人资料页，不传则表示查看自己的动态
	 * @param modelType 1为首页动态，2为查看他人资料页动态，3为查看自己资料页动态
	 * @param index 起始页，第一页为0
	 * @param size 页大小，默认为20
	 * @param type 筛选类型
	 */ 
	public static boolean getDynamicNewList(Context context, IBindData bind, Handler handler,int index, int size) {
		try {
			JSONObject inObj = new JSONObject();
			
			inObj.put("index", index);
			inObj.put("size", size);
			
			String requestStr = inObj.toString();
			String url = getDynamicNewsURL("/dynamicNews/getListDynamicNews.json");
			doExecute(context, bind, EAPIConsts.HomeReqType.HOME_REQ_GET_DYNAMIC, url, requestStr, handler);
			return true;
		} catch (Exception e) {
			
		}	
		return false;
	}

	/**
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @param userId
	 * @param type，":"筛选类型(web端传:"type":"筛选类型0：全部，1:知识，2：需求，3：人脉，4：会议，5，用户名片，6：组织客户")"i
	 * @param index
	 * @param size
	 * @return
	 */
	public static boolean getListMyDynamicNews(Context context, IBindData bind, Handler handler,long userId, int index, int size){
		try {
			JSONObject inObj = new JSONObject();
			
			inObj.put("userId", userId);
			inObj.put("index", index);
			inObj.put("size", size);
			
			String requestStr = inObj.toString();
			String url = getDynamicNewsURL("/dynamicNews/getListMyDynamicNews.json");
			doExecute(context, bind, EAPIConsts.HomeReqType.HOME_REQ_GET_MY_DYNAMIC, url, requestStr, handler);
			return true;
		} catch (Exception e) {
			
		}	
		return false;
	}
	
	/**
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @param userId
	 * @param type
	 * @param index
	 * @param size
	 * @return
	 */
	public static boolean getListGTDynamicNews(Context context, IBindData bind, Handler handler,long industryCodes, int index, int size){
		try {
			JSONObject inObj = new JSONObject();
			JSONArray jarray = new JSONArray();
//			inObj.put("industryCodes", jarray);
			inObj.put("index", index);
			inObj.put("size", size);
			
			String requestStr = inObj.toString();
			String url = getDynamicNewsURL("/dynamicNews/getListGTDynamicNews.json");
			doExecute(context, bind, EAPIConsts.HomeReqType.HOME_REQ_GET_GT_DYNAMIC, url, requestStr, handler);
			return true;
		} catch (Exception e) {
			
		}	
		return false;
	}
	
	public static boolean getAssoByDynamicId(Context context, IBindData bind, Handler handler,long dynamicId){
		try {
			JSONObject inObj = new JSONObject();
			
			inObj.put("dynamicId", dynamicId);
			
			String requestStr = inObj.toString();
			String url = getDynamicNewsURL("/dynamicNews/getAssoByDynamicId.json");
			doExecute(context, bind, EAPIConsts.HomeReqType.HOME_REQ_GET_DYNAMIC_ASSO, url, requestStr, handler);
			return true;
		} catch (Exception e) {
			
		}	
		return false;
	}


	/**
	 * 判断是否是人还是组织
	 * @param context
	 * @param bind
	 * @param handler
	 * @param userId
	 * @return
	 */
	public static boolean getDynamicNewLowType(Context context, IBindData bind, Handler handler,long userId) {
		try {
			JSONObject inObj = new JSONObject();
			
			inObj.put("userId", userId);
			String requestStr = inObj.toString();
			
			String url = getFullUrlWithDir("/customer/isUserOrOrg.json");
			doExecute(context, bind, EAPIConsts.HomeReqType.HOME_REQ_GETCONNECTORORG, url, requestStr, handler);
			return true;
		} catch (Exception e) {
			
		}	
		return false;
	}
	
	/**
	 * 修改密码
	 * @param context
	 * @param bind
	 * @param handler
	 * @param newPassword
	 * @param oldPassword
	 * @return
	 */
	public static boolean changeUserPwd(Context context, IBindData bind, Handler handler,String newPassword,String oldPassword) {
		try {
			JSONObject inObj = new JSONObject();
			
			inObj.put("oldPassword", oldPassword);
			inObj.put("newPassword", newPassword);
			
			String requestStr = inObj.toString();
			String url = getFullUrlWithDir("/user/set/updatePassword.json");
			doExecute(context, bind, EAPIConsts.ReqType.CHANGE_USER_PWD, url, requestStr, handler);
			return true;
		} catch (Exception e) {
		}	
		return false;
	}
	
	/**
	 * 发表评论
	 * @param type 评论对象类型 1-需求；2-业务需求；3-任务；4-项目
	 * @param id 评论对象id
	 * @param content 评论内容
	 */
	public static boolean addComment(Context context, IBindData bind, Handler handler, int type, String id, String content) {
		try {
			JSONObject inObj = new JSONObject();
			inObj.put("type", type);
			inObj.put("id", id);
			inObj.put("content", content);
			String requestStr = inObj.toString();

			String url = getFullUrlWithDir("/reply/addComment.json");
			doExecute(context, bind, EAPIConsts.HomeReqType.HOME_REQ_ADD_COMMENT,
					url, requestStr, handler);
			return true;

		} catch (Exception e) {
			
		}	
			return false;
	}
	
	/**
	 * 发表评论
	 * @param type 评论对象类型 1-需求；2-业务需求；3-任务；4-项目
	 * @param newsId 动态id
	 * @param content 评论内容
	 */
	public static boolean addDynamicComment(Context context, IBindData bind, Handler handler, int type, long id, String content) {
		try {
			JSONObject inObj = new JSONObject();
			inObj.put("newsId", id);
			inObj.put("content", content);
			String requestStr = inObj.toString();
			
			String url = getFullUrlWithDir("/dynamicNews/setDynamicComment.json");
			doExecute(context, bind, EAPIConsts.HomeReqType.HOME_REQ_ADD_DYNAMIC_COMMENT,
					url, requestStr, handler);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return false;
	}
	/**
	 * 意见反馈
	 * @param type 反馈类型
	 * @param content 反馈内容
	 * @param contact 反馈人联系方式
	 */
	public static boolean addSuggestion(Context context, IBindData bind, Handler handler, MSuggestion suggestion) {
		String requestStr = gson.toJson(suggestion, MSuggestion.class);
		String url = getFullUrlWithDir("/common/addSuggestion.json");
		doExecute(context, bind, EAPIConsts.HomeReqType.HOME_REQ_ADD_SUGGESTION,url, requestStr, handler);
//		try {
//			JSONObject inObj = new JSONObject();
//			inObj.put("dicName", suggestion.getDicName());
//			inObj.put("problemContent", suggestion.getProblemContent());
//			inObj.put("contact", suggestion.getContact());
//			inObj.put("id", suggestion.getDicId());
//			inObj.put("source", suggestion.getSource());
//			String requestStr = inObj.toString();
//			
//			String url = getFullUrlWithDir("/common/addSuggestion.json");
//			doExecute(context, bind, EAPIConsts.HomeReqType.HOME_REQ_ADD_SUGGESTION,url, requestStr, handler);
//			return true;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}	
		return false;
	}
	
	/**
	 * 获取意见类型
	 * @param context
	 * @param bind
	 * @param handler
	 * @param id   评论id
	 * @return
	 */
	public static boolean getSuggestionType(Context context, IBindData bind, Handler handler) {
		try {
			JSONObject inObj = new JSONObject();
			String requestStr = inObj.toString();
			
			String url = getFullUrlWithDir("/common/queryDic.json");
			doExecute(context, bind, EAPIConsts.HomeReqType.HOME_REQ_GET_SUGGESTION_TYPE,
					url, requestStr, handler);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return false;
	}
	
	
	/**
	 * 删除评论
	 * @param context
	 * @param bind
	 * @param handler
	 * @param id   评论id
	 * @return
	 */
	public static boolean deleteDynamicComment(Context context, IBindData bind, Handler handler, long id) {
		try {
			JSONObject inObj = new JSONObject();
			inObj.put("id", id);
			String requestStr = inObj.toString();
			
			String url = getFullUrlWithDir("/dynamicNews/deleteDynamicComment.json");
			doExecute(context, bind, EAPIConsts.HomeReqType.HOME_REQ_DELETE_DYNAMIC_COMMENT,
					url, requestStr, handler);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return false;
	}
	
	/**
	 * 添加动态赞同
	 * @param context
	 * @param bind
	 * @param handler
	 * @param pId 赞同者id
	 * @param dynamicId 动态id
	 * @param ptype 区分用户与组织 1为用户，2为组织
	 */
	public static void addDynamicPraise(Context context, IBindData bind, Handler handler,/* long pId,*/long dynamicId,int ptype){

		try {
			JSONObject inObj = new JSONObject();
//			inObj.put("pId", pId);
			inObj.put("dynamicId", dynamicId);
			inObj.put("ptype", ptype);
			String requestStr = inObj.toString();
			
			String url = getFullUrlWithDir("/dynamicNews/addDynamicPraise.json");
			doExecute(context, bind, EAPIConsts.HomeReqType.HOME_REQ_ADD_DYNAMIC_PRAISE,
					url, requestStr, handler);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 取消动态赞同
	 * @param context
	 * @param bind
	 * @param handler
	 * @param id 赞同id
	 */
	public static void cancelDynamicPraise(Context context, IBindData bind, Handler handler, long id){
		try {
			JSONObject inObj = new JSONObject();
			inObj.put("id", id);
			String requestStr = inObj.toString();
			String url = getFullUrlWithDir("/dynamicNews/cancelDynamicPraise.json");
			doExecute(context, bind, EAPIConsts.HomeReqType.HOME_REQ_CANCEL_DYNAMIC_PRAISE,
					url, requestStr, handler);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取搜索结果， 2.38
	 * @param context
	 * @param bind
	 * @param handler
	 * @param keyword 搜索关键字，如果为“”， 表示查询
	 * @param type 类型
	 * @param index
	 * @param pagesize
	 * @return
	 */
	public static boolean getSearchList(Context context, IBindData bind, Handler handler, String keyword, int type, int index, int pagesize) {
		try {
			JSONObject inObj = new JSONObject();
			inObj.put("keyword", keyword);
			inObj.put("type", type);
			inObj.put("index", index);
			inObj.put("size", pagesize);
			String requestStr = inObj.toString();
			
			int reqType = EAPIConsts.HomeReqType.HOME_REQ_GET_SEARCH_LIST;
			switch(type){
			case SearchActivity_new.TYPE_MEMBER:
				reqType = EAPIConsts.HomeReqType.HOME_REQ_GET_SEARCH_LIST_PERSON;
				break;
			case SearchActivity_new.TYPE_ORGANDCUSTOMER:
				reqType = EAPIConsts.HomeReqType.HOME_REQ_GET_SEARCH_LIST_ORG;
				break;
			case SearchActivity_new.TYPE_KNOWLEDGE:
				reqType = EAPIConsts.HomeReqType.HOME_REQ_GET_SEARCH_LIST_KNOWLEDGE;
				break;
			case SearchActivity_new.TYPE_METTING:
				reqType = EAPIConsts.HomeReqType.HOME_REQ_GET_SEARCH_LIST_MEET;
				break;
			case SearchActivity_new.TYPE_DEMAND:
				reqType = EAPIConsts.HomeReqType.HOME_REQ_GET_SEARCH_LIST_DEMEND;
				break;
			}

			String url = getFullUrlWithDir("/search/getSearchList.json");
			doExecute(context, bind, reqType, url, requestStr, handler);
			return true;

		} catch (Exception e) {
			
		}	
			return false;
	}
	
	/**
	 * 展示形搜索
	 * @param context
	 * @param bind
	 * @param handler
	 * @param keyword 关键字
	 * @param index 起始1（默认1）
	 * @param size
	 * @return
	 */
	public static boolean getSearchIndexList(Context context, IBindData bind, Handler handler, String keyword, int index, int size) {
		try {
			JSONObject inObj = new JSONObject();
			inObj.put("keyword", keyword);
			inObj.put("index", index);
			inObj.put("size", size);
			String requestStr = inObj.toString();

			String url = getFullUrlWithDir("search/getSearchIndexList.json");
			doExecute(context, bind, EAPIConsts.HomeReqType.HOME_REQ_GET_SEARCH_INDEX_LIST,
					url, requestStr, handler);
			return true;

		} catch (Exception e) {
			
		}
		return false;
	}
	
	
	public static boolean getSearchList(Context context, IBindData bind, Handler handler, String keyword, int index, int pagesize) {
		try {
			JSONObject inObj = new JSONObject();
			inObj.put("keyword", keyword);
			inObj.put("index", index);
			inObj.put("size", pagesize);
			String requestStr = inObj.toString();
			
			String url = getFullUrlWithDirForMeeting("/meeting/seach.json");
			doExecute(context, bind, EAPIConsts.HomeReqType.HOME_REQ_GET_SEARCH_LIST_MEETING,
					url, requestStr, handler);
			return true;
			
		} catch (Exception e) {
			
		}	
		return false;
	}
	
	private static String getFullUrlWithDirForMeeting(String method) {
		return EAPIConsts.CONFERENCE_URL + method;
	}

	/**
	 * 获取根据用户id获取需求列表 ， 2.45
	 */
	public static boolean getListRequirementByUserID(Context context, IBindData bind, Handler handler, String userID, int index, int pagesize) {
		try {
			JSONObject inObj = new JSONObject();
			inObj.put("userID", userID);
			inObj.put("index", index);
			inObj.put("size", pagesize);
			String requestStr = inObj.toString();

			String url = getFullUrlWithDir("/requirement/getListRequirementByUserID.json");
			doExecute(context, bind, EAPIConsts.HomeReqType.HOME_REQ_GET_MY_REQUIREMENT,
					url, requestStr, handler);
			return true;

		} catch (Exception e) {
			
		}	
			return false;
	}
	
	/**
	 * 获取我 的事务 ， 2.46
	 */
	public static boolean getMyAffair(Context context, IBindData bind, Handler handler, int index, int pagesize) {
		try {
			JSONObject inObj = new JSONObject();
			inObj.put("index", index);
			inObj.put("size", pagesize);
			String requestStr = inObj.toString();

			String url = getFullUrlWithDir("/affair/getMyAffair.json");
			doExecute(context, bind, EAPIConsts.HomeReqType.HOME_REQ_GET_MY_AFFAIR,
					url, requestStr, handler);
			return true;

		} catch (Exception e) {
			
		}	
			return false;
	}
	
	/**
	 * 获取我 的需求 ， 2.47
	 */
	public static boolean getMyKnowledge(Context context, IBindData bind, Handler handler, int index, int pagesize) {
		try {
			JSONObject inObj = new JSONObject();
			inObj.put("index", index);
			inObj.put("size", pagesize);
			String requestStr = inObj.toString();

			String url = getFullUrlWithDir("/knowledge/getMyKnowledge.json");
			doExecute(context, bind, EAPIConsts.HomeReqType.HOME_REQ_GET_MY_KNOWLEDGE,
					url, requestStr, handler);
			return true;

		} catch (Exception e) {
			
		}	
			return false;
	}
	
	private static String getFullUrlWithDir(String method){
		return EAPIConsts.TMS_URL + method;
	}
	
	private static String getDynamicNewsURL(String method){
		return EAPIConsts.DynamicNews_URL + method;
	}
	//获取首页数据
	public static void getMainPageList(Context context, IBindData bind, Handler handler){
		String requestStr = "";
		String url = getFullUrlWithDir("advertise/getStaticAdvertiseList.json");
//		String url = "http://test.online.gintong.com/cross/advertise/getStaticAdvertiseList.json";
		doExecute(context, bind, EAPIConsts.HomeReqType.HOME_PAGE_LIST,
				url, requestStr, handler);
	}
}
