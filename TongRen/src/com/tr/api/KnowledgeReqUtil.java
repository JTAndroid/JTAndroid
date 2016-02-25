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
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.knowledge.Knowledge2;
import com.tr.model.obj.Connections;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

public class KnowledgeReqUtil extends ReqBase {

	private final static String TAG = KnowledgeReqUtil.class.getSimpleName();

	/**
	 * 发布知识
	 * @param context
	 * @param bind
	 * @param knowledge
	 * @param handler
	 */
	public static void doCreateKnowledge(Context context, IBindData bind,
			Knowledge2 knowledge, Handler handler) {
		
		String requestStr = Knowledge2.knowledge2JsonString(knowledge);
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.CreateKnowledge;
		doExecute(context, bind, EAPIConsts.KnoReqType.CreateKnowledge, url,
				requestStr, handler);
	}
	
	
	/**
	 * 更新知识
	 * @param context
	 * @param bind
	 * @param knowledge
	 * @param handler
	 */
	public static void doUpdateKnowledge(Context context, IBindData bind,
			Knowledge2 knowledge, Handler handler) {
		
		String requestStr = Knowledge2.knowledge2JsonString(knowledge);
		
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.updateKnowledge;
		doExecute(context, bind, EAPIConsts.KnoReqType.updateKnowledge, url,
				requestStr, handler);
	}
	
	

	

	/**
	 * 获取知识目录
	 * 
	 * @param context
	 * @param bind
	 * @param userId
	 * @param handler
	 */
	public static void doGetKnoCategory(Context context, IBindData bind,
			String userId, Handler handler) {

		String requestStr = "";
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("userId", userId);
			requestStr = jObj.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.GetUserCategory;
		doExecute(context, bind, EAPIConsts.KnoReqType.GetUserCategory, url,
				requestStr, handler);
	}

	/**
	 * 添加知识目录
	 * 
	 * @param context
	 * @param bind
	 * @param parentId
	 * @param categoryName
	 * @param handler
	 */
	public static void doAddKnoCategory(Context context, IBindData bind,
			long parentId, String categoryName, Handler handler) {

		String requestStr = "";
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("parentId", parentId);
			jObj.put("categoryName", categoryName);
			requestStr = jObj.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.AddUserCategory;
		doExecute(context, bind, EAPIConsts.KnoReqType.AddUserCategory, url,
				requestStr, handler);
	}

	/**
	 * 编辑知识目录
	 * 
	 * @param context
	 * @param bind
	 * @param parentId
	 * @param categoryName
	 * @param handler
	 */
	public static void doEditKnoCategory(Context context, IBindData bind,
			long parentId, String categoryName, Handler handler) {

		String requestStr = "";
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("categoryId", parentId);
			jObj.put("name", categoryName);
			requestStr = jObj.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.EditUserCategory;
		doExecute(context, bind, EAPIConsts.KnoReqType.EditUserCategory, url,
				requestStr, handler);
	}

	/**
	 * 删除知识目录
	 * 
	 * @param context
	 * @param bind
	 * @param categoryId
	 * @param handler
	 */
	public static void doDelKnoCategory(Context context, IBindData bind,
			long categoryId, Handler handler) {

		String requestStr = "";
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("categoryId", categoryId);
			requestStr = jObj.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.DelUserCategory;
		doExecute(context, bind, EAPIConsts.KnoReqType.DelUserCategory, url,
				requestStr, handler);
	}

	/**
	 * 获取用户知识标签
	 * 
	 * @param context
	 * @param bind
	 * @param userId
	 * @param handler
	 */
	public static void doGetKnowledgeTagList(Context context, IBindData bind,
			String userId, Handler handler) {

		String requestStr = "";
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("userId", userId);
			requestStr = jObj.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}

		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.GetKnowledgeTagList;
		doExecute(context, bind, EAPIConsts.KnoReqType.GetKnowledgeTagList,
				url, requestStr, handler);
	}

	/**
	 * 编辑用户知识标签
	 * 
	 * @param context
	 * @param bind
	 * @param listTag
	 * @param handler
	 */
	public static void doEditUserKnowledgeTag(Context context, IBindData bind,String userId, 
			ArrayList<String> listTag, Handler handler) {

		String requestStr = "";
		try {
			JSONArray jsonArray = new JSONArray();
			for (int i = 0; i < listTag.size(); i++) {
				jsonArray.put(i, listTag.get(i));
			}

			JSONObject jObj = new JSONObject();
			jObj.put("userId", userId);
			jObj.put("listTag", jsonArray);
			requestStr = jObj.toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.EditUserKnowledgeTag;
		doExecute(context, bind, EAPIConsts.KnoReqType.EditUserKnowledgeTag, url,
				requestStr, handler);
	}

	/**
	 * 编辑指定的知识的标签
	 * 
	 * @param context
	 * @param bind
	 * @param userId
	 * @param listKnowledgeId
	 * @param listTag
	 * @param handler
	 */
	public static void doEditTagByKnoId(Context context, IBindData bind,
			String userId, ArrayList<Long> listKnowledgeId, ArrayList<Integer> listKnowledgeType, ArrayList<String> listTag,
			Handler handler) {
		String requestStr = "";
		try {
			JSONArray jsonArray1 = new JSONArray();
			JSONArray jsonArray2 = new JSONArray();
			JSONArray jsonArray3 = new JSONArray();
			if (listKnowledgeId!=null) {
				for (int i = 0; i < listKnowledgeId.size(); i++) {
					jsonArray1.put(i, listKnowledgeId.get(i));
				}
			}
			if (listKnowledgeType!=null) {
				for (int i = 0; i < listKnowledgeType.size(); i++) {
					jsonArray2.put(i, listKnowledgeType.get(i));
				}
			}
			if (listTag!=null) {
				for (int i = 0; i < listTag.size(); i++) {
					jsonArray3.put(i, listTag.get(i));
				}
			}
			JSONObject jObj = new JSONObject();
			jObj.put("userId", userId);
			jObj.put("listKnowledgeId", jsonArray1);
			jObj.put("listKnowledgeType", jsonArray2);
			jObj.put("listTag", jsonArray3);
			requestStr = jObj.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.EditKnoTagByKnoId;
		doExecute(context, bind, EAPIConsts.KnoReqType.EditKnoTagByKnoId, url,
				requestStr, handler);
	}

	/**
	 * 获取用户的栏目
	 * 
	 * @param context
	 * @param bind
	 * @param userId
	 * @param handler
	 */
	public static void doGetColumnByUserId(Context context, IBindData bind,
			String userId, Handler handler) {

		String requestStr = "";
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("userId", userId);
			requestStr = jObj.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.GetColumnByUserId;
		doExecute(context, bind, EAPIConsts.KnoReqType.GetColumnByUserId, url,
				requestStr, handler);
	}

	/**
	 * 获取用户订阅的栏目
	 * 
	 * @param context
	 * @param bind
	 * @param userId
	 * @param handler
	 */
	public static void doGetSubscribedColumnByUserId(Context context,
			IBindData bind, String userId, Handler handler) {
		String MSG = "doGetSubscribedColumnByUserId()";
		
		String requestStr = "";
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("userId", userId);
			requestStr = jObj.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String url = EAPIConsts.TMS_URL
				+ EAPIConsts.ReqUrl.GetSubscribedColumnByUserId;
		doExecute(context, bind,
				EAPIConsts.KnoReqType.GetSubscribedColumnByUserId, url,
				requestStr, handler);
	}

	/**
	 * 订阅/取消订阅
	 * 
	 * @param context
	 * @param bind
	 * @param type
	 * @param columnId
	 * @param handler
	 */
	public static void doEditSubscribedColumn(Context context, IBindData bind,
			int type, long columnId, Handler handler) {

		String requestStr = "";
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("type", type);
			jObj.put("columnId", columnId);
			
			requestStr = jObj.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String url = EAPIConsts.TMS_URL
				+ EAPIConsts.ReqUrl.EditSubscribedColumn;
		doExecute(context, bind, EAPIConsts.KnoReqType.EditSubscribedColumn,
				url, requestStr, handler);
	}

	/**
	 * 更新订阅的栏目
	 * 
	 * @param context
	 * @param bind
	 * @param columnIds
	 * @param handler
	 */
	public static void doUpdateSubscribedColumn(Context context,
			IBindData bind, ArrayList<Long> listColumnId, Handler handler) {

		String requestStr = "";
		try {
			JSONArray columnIdJsonArray = new JSONArray();
			
			JSONObject jObj = new JSONObject();
			if (listColumnId!=null) {
				for (int i = 0; i < listColumnId.size(); i++) {
					columnIdJsonArray.put(i, listColumnId.get(i));
				}
			}
			jObj.put("listColumnId", columnIdJsonArray);
			
			requestStr = jObj.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		
		String url = EAPIConsts.TMS_URL
				+ EAPIConsts.ReqUrl.UpdateSubscribedColumn;
		doExecute(context, bind, EAPIConsts.KnoReqType.UpdateSubscribedColumn,
				url, requestStr, handler);
	}

	/**
	 * 获取知识详情
	 * 
	 * @param context
	 * @param bind
	 * @param knowledgeId
	 *            知识id
	 * @param knowledgeType
	 *            知识类型
	 * @param handler
	 */
	public static void doGetKnoDetails(Context context, IBindData bind,
			long knowledgeId, int knowledgeType, Handler handler) {
		String requestStr = "";
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("knowledgeId", knowledgeId);
			jObj.put("knowledgeType", knowledgeType);
			requestStr = jObj.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.GetKnoDetails;
		doExecute(context, bind, EAPIConsts.KnoReqType.GetKnoDetails, url,
				requestStr, handler);
	}
	/**
	 * 获取知识详情(保存知识调用  以区分不同的调用地点做不同的处理)
	 * 
	 * @param context
	 * @param bind
	 * @param knowledgeId
	 *            知识id
	 * @param knowledgeType
	 *            知识类型
	 * @param handler
	 */
	public static void doGetKnoDetailsBySaveKno(Context context, IBindData bind,
			long knowledgeId, int knowledgeType, Handler handler) {
		String requestStr = "";
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("knowledgeId", knowledgeId);
			jObj.put("knowledgeType", knowledgeType);
			requestStr = jObj.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.GetKnoDetails;
		doExecute(context, bind, EAPIConsts.KnoReqType.doGetKnoDetailsBySaveKno, url,
				requestStr, handler);
	}

	/**
	 * 根据类别获取相应的集合
	 * 
	 * @param context
	 * @param bind
	 * @param type
	 * @param handler
	 */
	public static void doGetlistByType(Context context, IBindData bind,
			String type, Handler handler) {
		String requestStr = "";
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("type", type);
			requestStr = jObj.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}

	}

	/**
	 * 根据类型（全部、我收藏的、分享给我的、我创建的）和关键字分页获取知识列表
	 * 
	 * @param context
	 * @param bind
	 * @param userId
	 *            用户id
	 * @param type
	 *            0-全部;1-我收藏的;2-分享给我的;3-我创建的
	 * @param keyword
	 *            关键字
	 * @param index
	 *            当前页(默认从0开始)
	 * @param size
	 *            每页项数(默认20)
	 * @param handler
	 */
	public static void doGetKnowledgeByTypeAndKeyword(Context context,
			IBindData bind, String userId, Integer type, String keyword,
			int index, int size, Handler handler) {
		String requestStr = "";
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("userId", userId);
			jObj.put("type", type);
			jObj.put("keyword", keyword);
			jObj.put("index", index);
			jObj.put("size", size);
			requestStr = jObj.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String url = EAPIConsts.TMS_URL
				+ EAPIConsts.ReqUrl.GetKnowledgeByTypeAndKeyword;
		doExecute(context, bind,
				EAPIConsts.KnoReqType.GetKnowledgeByTypeAndKeyword, url,
				requestStr, handler);
	}

	/**
	 * 删除指定的知识
	 * 
	 * @param context
	 * @param bind
	 * @param userId
	 *            用户id
	 * @param listKnowledgeId
	 *            知识id列表
	 * @param handler
	 */
	public static void doDeleteKnowledgeById(Context context, IBindData bind,
			String userId, ArrayList<Long> listKnowledgeId,  ArrayList<Integer> listKnowledgeType,int source,Handler handler) {
		String requestStr = "";
		try {
			JSONArray jsonArray1 = new JSONArray();
			JSONArray jsonArray2 = new JSONArray();
			for (int i = 0; i < listKnowledgeId.size(); i++) {
				jsonArray1.put(i, listKnowledgeId.get(i));
			}
			for (int i = 0; i < listKnowledgeType.size(); i++) {
				jsonArray2.put(i, listKnowledgeType.get(i));
			}

			JSONObject jObj = new JSONObject();
			jObj.put("userId", userId);
			jObj.put("listKnowledgeId", jsonArray1);
			jObj.put("listKnowledgeType", jsonArray2);
			jObj.put("source", source);
			requestStr = jObj.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String url = EAPIConsts.TMS_URL + EAPIConsts.ReqUrl.DeleteKnowledgeById;
		doExecute(context, bind, EAPIConsts.KnoReqType.DeleteKnowledgeById,
				url, requestStr, handler);
	}

	/**
	 * 获取各种详情页的评论列表， 主要包括需求、业务需求、任务、项目
	 * 
	 * @param context
	 * @param bind
	 * @param type
	 *            "0-全部；1-我的机构客户；2-我的人脉；3-所有机构；4-所有用户；5-我的好友"
	 * @param id
	 *            "需求id"
	 * @param index
	 *            "起始页，第一页为0"
	 * @param size
	 *            "页大小，默认为20"
	 * @param handler
	 */
	public static void doGetKnoCommentsByType(Context context, IBindData bind,
			String type, String id, int index, int size, Handler handler) {
		String requestStr = "";
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("type", type);
			jObj.put("id", id);
			jObj.put("index", index);
			jObj.put("size", size);
			requestStr = jObj.toString();
			String url = EAPIConsts.TMS_URL
					+ EAPIConsts.ReqUrl.GetKnoCommentsByType;
			doExecute(context, bind, EAPIConsts.KnoReqType.GetKnoCommentsByType,
					url, requestStr, handler);
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
	}

	/**
	 * 收藏知识
	 * 
	 * @param context
	 * @param bind
	 * @param id
	 *            知识id
	 * @param type
	 *            知识类型
	 * @param handler
	 */
	public static void doUpdateCollectKnowledge(Context context,
			IBindData bind, long id, int type,String bool , Handler handler) {
		String requestStr = "";
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("id", id);
			jObj.put("type", type);
			jObj.put("isCancelled", bool);
			requestStr = jObj.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String url = EAPIConsts.TMS_URL
				+ EAPIConsts.ReqUrl.UpdateCollectKnowledge;
		doExecute(context, bind, EAPIConsts.KnoReqType.UpdateCollectKnowledge,
				url, requestStr, handler);
	}

	/**
	 * 获取该用户收藏知识的状态
	 * 
	 * @param context
	 * @param bind
	 * @param id
	 * @param type
	 * @param handler
	 */
	public static void doGetCollectKnoState(Context context, IBindData bind,
			long id, int type, Handler handler) {
		String requestStr = "";
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("id", id);
			jObj.put("type", type);
			requestStr = jObj.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String url = EAPIConsts.TMS_URL
				+ EAPIConsts.ReqUrl.GetCollectKnowledgeState;
		doExecute(context, bind,
				EAPIConsts.KnoReqType.GetCollectKnowledgeState, url,
				requestStr, handler);
	}

	/**
	 * 根据栏目和来源获取知识列表
	 * 
	 * @param context
	 * @param bind
	 * @param columnId   用户id
	 * @param source   0-全部;1-金桐脑;2-全平台;3-好友;4-自己
	 * @param index   当前页,默认从0开始
	 * @param size     每页数据项,默认20
	 * @param handler
	 */
	public static void doGetKnowledgeByColumnAndSource(Context context,
			IBindData bind, long columnId, int columnType, String source, int index, int size,
			Handler handler) {
		String requestStr = "";
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("columnId", columnId);
			jObj.put("columnType", columnType);
			jObj.put("source", source);
			jObj.put("index", index);
			jObj.put("size", size);
			requestStr = jObj.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String url = EAPIConsts.TMS_URL
				+ EAPIConsts.ReqUrl.GetKnowledgeByColumnAndSource;
		doExecute(context, bind,
				EAPIConsts.KnoReqType.GetKnowledgeByColumnAndSource, url,
				requestStr, handler);
	}

	/**
	 * 对知识或评论发表评论
	 * 
	 * @param context
	 * @param bind
	 * @param knowledgeId	知识id
	 * @param parentId		 父评论id
	 * @param comment		评论内容
	 * @param index			当前页(默认从0开始)
	 * @param size			每页项数(默认20)
	 * @param handler
	 */
	public static boolean addKnowledgeComment(Context context, IBindData bind,
			long knowledgeId, long parentId, String comment, int index,
			int size, Handler handler) {
		String requestStr = "";
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("knowledgeId", knowledgeId);
			jObj.put("parentId", parentId);
			jObj.put("comment", comment);
			jObj.put("index", index);
			jObj.put("size", size);
			requestStr = jObj.toString();
		    String url = EAPIConsts.TMS_URL
					+ EAPIConsts.ReqUrl.AddKnowledgeComment;
			doExecute(context, bind,
					EAPIConsts.KnoReqType.AddKnowledgeComment, url,
					requestStr, handler);
			return true;
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
			return false;
		}
	}
	
	/**
	 * 获取知识详情的评论
	 * 
	 * @param context
	 * @param bind
	 * @param knowledgeId
	 * @param parentId
	 * @param index
	 * @param size
	 * @param handler
	 * @return
	 */
	public static boolean doGetKnowledgeComment(Context context, IBindData bind,
			long knowledgeId, long parentId, int index,
			int size, Handler handler) {
		String requestStr = "";
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("knowledgeId", knowledgeId);
			jObj.put("parentId", parentId);
			jObj.put("index", index);
			jObj.put("size", size);
			requestStr = jObj.toString();
			String url = EAPIConsts.TMS_URL
					+ EAPIConsts.ReqUrl.GetKnowledgeComment;
			doExecute(context, bind,
					EAPIConsts.KnoReqType.GetKnowledgeComment, url,
					requestStr, handler);
			return true;
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
			return false;
		}
	}
	/**
	 * 根据标签名和关键字获取知识列表
	 * 
	 * @param context
	 * @param bind
	 * @param userId
	 *            用户id
	 * @param Tag
	 *           	标签名
	 * @param keyword
	 *            关键字
	 * @param index
	 *            当前页(默认从0开始)
	 * @param size
	 *            每页项数(默认20)
	 * @param handler
	 */
	public static void doGetKnowledgeByTagAndKeyword(Context context,
			IBindData bind, String userId, String Tag, String keyword,
			int index, int size, Handler handler) {
		String requestStr = "";
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("userId", userId);
			jObj.put("tag", Tag);
			jObj.put("keyword", keyword);
			jObj.put("index", index);
			jObj.put("size", size);
			requestStr = jObj.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String url = EAPIConsts.TMS_URL
				+ EAPIConsts.ReqUrl.GetKnowledgeByTagAndKeyword;
		doExecute(context, bind,
				EAPIConsts.KnoReqType.GetKnowledgeByTagAndKeyword, url,
				requestStr, handler);
	}
	
	public static void doGetKnowledgeByUserCategoryAndKeyword(Context context,
			IBindData bind, String userId, Long categoryId, String keyword,
			int index, int size, Handler handler) {
		String requestStr = "";
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("userId", userId);
			jObj.put("categoryId", categoryId);
			jObj.put("keyword", keyword);
			jObj.put("index", index);
			jObj.put("size", size);
			requestStr = jObj.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String url = EAPIConsts.TMS_URL
				+ EAPIConsts.ReqUrl.GetKnowledgeByUserCategoryAndKeyword;
		doExecute(context, bind,
				EAPIConsts.KnoReqType.GetKnowledgeByUserCategoryAndKeyword, url,
				requestStr, handler);
	}
	
	/**
	 * 解析Url类型的知识
	 * @param context
	 * @param bind
	 * @param externalUrl
	 * @param handler
	 */
	public static void doFetchExternalKnowledgeUrl(Context context, IBindData bind, String externalUrl, Handler handler ){
		
		String requestStr = "";
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("externalUrl", externalUrl);
			requestStr = jObj.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String url = EAPIConsts.TMS_URL
				+ EAPIConsts.ReqUrl.FetchExternalKnowledgeUrl;
		doExecute(context, bind,
				EAPIConsts.KnoReqType.FetchExternalKnowledgeUrl, url,
				requestStr, handler);
	}

	/**
	 * 查询用户的标签使用个数
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @return
	 */
	public static boolean getKnowledgeTag(Context context, IBindData bind, Handler handler) {
		JsonObject json = new JsonObject();
		json.addProperty("index", 0);
		json.addProperty("size", 999999999); // 页数和大小没有用
		String requestStr = json.toString();
		String url = EAPIConsts.getTMSUrl() + EAPIConsts.ReqUrl.KNOWLEDGE_REQ_TAG_QUERY;
		doExecute(context, bind, EAPIConsts.KnoReqType.KNOWLEDGE_REQ_TAG_QUERY, url, requestStr, handler);
		return true;
	}
}
