package com.tr.api;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.tr.ui.organization.model.OrgInfoVo;
import com.utils.http.EAPIConsts;
import com.utils.http.EAPIConsts.OrganizationReqType;
import com.utils.http.EAPIConsts.OrganizationUrl;
import com.utils.http.IBindData;

public class OrganizationReqUtil extends ReqBase {

	private final static String TAG = OrganizationReqUtil.class.getSimpleName();

	/**
	 * 请求接口
	 * 
	 * @param context
	 * @param bind
	 * @param obj
	 * @param handler
	 * @param orgReqType
	 */
	public static void doRequestWebAPI(Context context, IBindData bind, Object obj, Handler handler, int orgReqType) {
		// Log.v("TAG", "进入请求数据类 Get Code for password 11111" + obj.toString());
		String requestStr = "";
		Gson gson = new Gson();

		requestStr = gson.toJson(obj);

		String url = EAPIConsts.TMS_URL;
		switch (orgReqType) {
		
		case OrganizationReqType.ORGANIZATION_REQ_CREATE_ORG://上传图片
			url+=OrganizationUrl.UPLOADIDCARDIMG;
			break;
		
		case OrganizationReqType.ORGANIZATION_REQ_FINDHEGHTONE: // 查询高层治理
			url += OrganizationUrl.HEGHTONE_STR;
			break;
		case OrganizationReqType.ORGANIZATION_REQ_GETDISCOVERLIST: // 获得组织列表数据
			url += OrganizationUrl.DISCOVERLIST_STR;
			break;
		case OrganizationReqType.ORGANIZATION_REQ_GETVCODEFORPASSWORD: // 获取验证码
			url += OrganizationUrl.GETVCODEFORPASSWORD_STR;
			break;
		case OrganizationReqType.ORGANIZATION_REQ_FORWARDORG: // 转发组织动态
			url += OrganizationUrl.FORWARDORG_STR;
			break;
		case OrganizationReqType.ORGANIZATION_REQ_REGISTRATION: // 组织注册
			url += OrganizationUrl.ORGSAVE_STR;
			break;
		case OrganizationReqType.ORGANIZATION_REQ_CUSTOMER_SAVECUSPROFILE: // 4.5添加客户详情
			url += OrganizationUrl.SAVECUSPROFILE_STR;
			break;
		case OrganizationReqType.ORGANIZATION_REQ_GETORGNOTICESLIST: // 获得最新公告列表
			url += OrganizationUrl.ORGNOTICESLIST_STR;
			break;
		case OrganizationReqType.ORGANIZATION_REQ_GETNEWSLIST: // 获得最新资讯
			url += OrganizationUrl.ORGNEWSLIST_STR;
			break;
		case OrganizationReqType.ORGANIZATION_REQ_FINDRESOUTCE: // 查询资源需求
			url += OrganizationUrl.FINDRESOURCE_STR;
			break;
		case OrganizationReqType.ORGANIZATION_REQ_GETMODLELIST: // 获得权限模块查询
			url += OrganizationUrl.GETMODELLIST_STR;
			break;
		case OrganizationReqType.ORGANIZATION_REQ_GROUP: // 客户标签分组列表
			url += OrganizationUrl.GROUP_STR;
			break;
		case OrganizationReqType.ORGANIZATION_REQ_ADD_HIGHLEVEL: // 添加高层治理
			url += OrganizationUrl.HIGHTSAVE_STR;
			break;
		case OrganizationReqType.ORGANIZATION_REQ_FINDSTOCKONE: // 4.14查询股东研究
			url += OrganizationUrl.FINDSTOCKONE_STR;
			break;

		case OrganizationReqType.ORGANIZATION_REQ_ORGANDPROINFO:// 4.4组织详情

			url += OrganizationUrl.ORGANIZATION_DETAILS_STR;
			break;
		case OrganizationReqType.ORGANIZATION_REQ_DETAILS:// 4.11财务分析
			url += OrganizationUrl.FINANCE_DETAILS_STR;
			break;
		case OrganizationReqType.ORGANIZATION_REQ_ORGDYNAMICLIST:// 4.1财务分析

			url += OrganizationUrl.DYNAMICNEWSLIST_STR;

			break;
		case OrganizationReqType.ORGANIZATION_REQ_FINDINDUSTRY:// 4.16 查询行业动态

			url += OrganizationUrl.FINDINDUSTRY_DYN_STR;

			break;
		case OrganizationReqType.ORGANIZATION_REQ_FINDPER:// 4.19 查询同业竞争

			url += OrganizationUrl.CUSTOMER_STR;

			break;

		case OrganizationReqType.ORGANIZATION_REQ_FINDREPORT:// 4.22 查询研究报告

			url += OrganizationUrl.FINDREPORT_STR;

			break;

		case OrganizationReqType.ORGANIZATION_CUSTOMER_COLUMNLIST:// 6.3
																	// 查询模板下的栏目
																	// /customer/column/list.json

			url += OrganizationUrl.COLUMNLIST_STR;

			break;
		case OrganizationReqType.ORGANIZATION_CUSTOMER_SAVERELATION:// 6.4
																	// 保存所选模块和栏目
																	// /customer/column/saveRelation.json

			url += OrganizationUrl.SAVERELATION_STR;

			break;
		case OrganizationReqType.ACCESS_TO_THE_PRIMARY_KEY:// 4.30 得到客户主键Id

			url += OrganizationUrl.ACCESS_STR;

			break;
		case OrganizationReqType.COLUMN_SAVE:// 6.5 新增和修改自定义栏目
												// /customer/column/save.json

			url += OrganizationUrl.COLUMN_SAVE_STR;

			break;
		case OrganizationReqType.COLUMN_DELETE:// 6.6 删除自定义栏目
												// /customer/column/delete.json
			url += OrganizationUrl.COLUMN_DELETE_STR;

			break;
		case OrganizationReqType.ORGANIZATION_CUSTOMER_DETILS:// 6.1 查询客户详情

			url += OrganizationUrl.CUSTOMER_DETILS;

			break;

		case OrganizationReqType.ORGANIZATION_REQ_ADD_MAIN_DEPARTMENT: // 4.26
																		// 添加主要职能部门
																		// /customer/departMents/save.json¶
			url += OrganizationUrl.DEPARTMENTS_SAVE_STR;
			break;
		case OrganizationReqType.ORGANIZATION_REQ_ADD_GOVERNMENTAREASURVEY: // 4.25
																			// 添加政府地区概况
																			// /customer/areaInfo/save.json
			url += OrganizationUrl.AREAINFO_SAVE_STR;
			break;

		case OrganizationReqType.ORGANIZATION_REQ_ORG_SAVECUSPROFILE:
			url += OrganizationUrl.ORGSAVEDETAIL_STR;
			break;
		case OrganizationReqType.MEET_SAVE: // 7.1 增加会面情况
											// /customer/meet/save.json¶
			url += OrganizationUrl.MEET_SAVE_STR;
			break;

		case OrganizationReqType.MEET_FINDLIST: // 7.3 查询会面情况列表
												// /customer/meet/findList.json¶
			url += OrganizationUrl.MEET_FINDLIST_STR;
			break;

		case OrganizationReqType.MEET_FINDONE: // 7.4 查询单个会面情况对象
												// /customer/meet/findOne.json
			url += OrganizationUrl.MEET_FINDONE_STR;
			break;

		case OrganizationReqType.MEET_DELETEBYID: // 7.5 删除会面情况对象
													// /customer/meet/deleteById.json¶
			url += OrganizationUrl.MEET_DELETEBYID_STR;
			break;
		case OrganizationReqType.CUSTOMER_INFORM_SAVE : //4.35 举报组织客户 /customer/inform/save.json¶
			url += OrganizationUrl.CUSTOMER_INFORM_SAVE_STR;
			break;
		case OrganizationReqType.CUSTOMER_COLLECT_PERATE  ://4.34 收藏或取消收藏组织客户 /customer/collect/operate.json
			url += OrganizationUrl.CUSTOME_COLLECT_OPERATE_STR;
			break;
		default:
			break;
		}

		doExecute(context, bind, orgReqType, url, requestStr, handler);
	}

	/*
	 * 请求组织首页的列表数据
	 */
	public static void doOrganizationPage(Context context, IBindData bind, String area, int type, String industry, String index, String size, Handler handler) {
		String requestStr = "";
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("area", area);
			jObj.put("type", type);
			jObj.put("industry", industry);
			jObj.put("index", index);
			jObj.put("size", size);
			requestStr = jObj.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String url = EAPIConsts.TMS_URL + EAPIConsts.OrganizationUrl.DISCOVERLIST_STR;

		doExecute(context, bind, EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_GETDISCOVERLIST, url, requestStr, handler);

	}

	/*
	 * 获取组织/客户的列表数据
	 *   "tagId":"标签id",
        "groupId":"分组id",
        "type":"类型 -1:全部客户 1:我创建的 2:我收藏的",
        "index":"当前页 默认值为0",
        "size":"每页记录条数 默认20"
         "name":"搜索字符",
	 */
	public static void doGetCusAndOrg(Context context, IBindData bind, String groupId, String index,String type, String size,String name, String tagId, Handler handler) {
		String requestStr = "";
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("groupId", groupId);
			jObj.put("index", index);
			jObj.put("size", size);
			jObj.put("name", name);
			jObj.put("tagId", tagId);
			jObj.put("type", type);
			requestStr = jObj.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String url = EAPIConsts.TMS_URL + EAPIConsts.OrganizationUrl.CUSANDORG_STR;

		doExecute(context, bind, EAPIConsts.OrganizationReqType.ORAGANIZATION_REQ_GETCUSTOMANDORG, url, requestStr, handler);

	}

	/**
	 * 组织详情添加好友返回JSONObject值
	 * 
	 * @return
	 */
	public static JSONObject getReqNewFriend(long id) {
		JSONObject jObject = new JSONObject();
		try {
			jObject.put("id", id);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jObject;
	}

	/** 得到组织详情添加好友返回JSONObject值，执行添加好友 */
	public static void doReqNewFriend(Context context, IBindData bind, JSONObject jsonObject, Handler handler) {

		String requestStr = jsonObject.toString();
		String url = EAPIConsts.TMS_URL + EAPIConsts.OrganizationUrl.ADD_FRIENDS_STR;
		/** 8个参数的 顺序 context 数据上下文 bind 返回接口 联网类型 请求地址 JSON串 错误处理hander */
		doExecute(context, bind, EAPIConsts.OrganizationReqType.ORAGANIZATION_REQ_ADD_FRIENDS, url, requestStr, handler);
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
	public static void doFindEvaluate(Context context, IBindData bind, long id, boolean isSelf, String type, Handler handler) {
		String requestStr = "";
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("userId", id);
			jObject.put("isSelf", isSelf);
			jObject.put("type", type);
			requestStr = jObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = EAPIConsts.TMS_URL + EAPIConsts.OrganizationUrl.FIND_Evaluate_STR;
		doExecute(context, bind, EAPIConsts.OrganizationReqType.GET_ORG_HOME_Evaluate, url, requestStr, handler);

	}

	/**
	 * 赞同/取消赞同评价
	 * 
	 * @param context
	 * @param bind
	 * @param homeUserId
	 *            主页君id
	 * @param id
	 *            评价id
	 * @param feedback
	 *            true赞同 false 不赞同
	 * @param handler
	 */
	public static void doFeedbackEvaluate(Context context, IBindData bind, long homeUserId, long id, boolean feedback, String type, Handler handler) {
		String requestStr = "";
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("id", id);
			jObject.put("homeUserId", homeUserId);
			jObject.put("feedback", feedback);
			jObject.put("type", type);
			requestStr = jObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = EAPIConsts.TMS_URL + EAPIConsts.OrganizationUrl.FEEDBACK_Evaluate_STR;
		doExecute(context, bind, EAPIConsts.OrganizationReqType.FEEDBACK_EVALUATE, url, requestStr, handler);
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
	public static void doGetMoreEvaluate(Context context, IBindData bind, long id, String type, Handler handler) {
		String requestStr = "";
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("userId", id);
			jObject.put("type", type);
			requestStr = jObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = EAPIConsts.TMS_URL + EAPIConsts.OrganizationUrl.FIND_MORE_Evaluate_STR;
		doExecute(context, bind, EAPIConsts.OrganizationReqType.FIND_MORE_EVALUATE, url, requestStr, handler);
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
	public static void doAddEvaluate(Context context, IBindData bind, long userId, String comment, String type, Handler handler) {
		String requestStr = "";
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("userId", userId);
			jObject.put("comment", comment);
			jObject.put("type", type);
			requestStr = jObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = EAPIConsts.TMS_URL + EAPIConsts.OrganizationUrl.ADD_Evaluate_STR;
		doExecute(context, bind, EAPIConsts.OrganizationReqType.ADD_ORG_HOME_Evaluate, url, requestStr, handler);
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
	public static void doDeleteEvaluate(Context context, IBindData bind, long ueid, String type, Handler handler) {
		String requestStr = "";
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("id", ueid);
			jObject.put("type", type);
			requestStr = jObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = EAPIConsts.TMS_URL + EAPIConsts.OrganizationUrl.DELETE_Evaluate_STR;
		doExecute(context, bind, EAPIConsts.OrganizationReqType.DELETE_EVALUATE, url, requestStr, handler);
	}

	/*
	 * 请求组织列表常用栏目-----zq
	 */
	public static void doGetModAndCol(Context context, IBindData bind, long templateId, long orgId, Handler handler) {
		String requestStr = "";
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("templateId", templateId);
			jObj.put("orgId", orgId);
			requestStr = jObj.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String url = EAPIConsts.TMS_URL + EAPIConsts.OrganizationUrl.COLUMNLIST_STR;
		doExecute(context, bind, EAPIConsts.OrganizationReqType.ORGANIZATION_CUSTOMER_COLUMNLIST, url, requestStr, handler);
	}

	/**
	 * 获取首页动态 （新框架）
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @param userId
	 *            主页用户id,传入userId查看他人资料页，不传则表示查看自己的动态
	 * @param modelType
	 *            1为首页动态，2为查看他人资料页动态，3为查看自己资料页动态
	 * @param index
	 *            起始页，第一页为0
	 * @param size
	 *            页大小，默认为20
	 * @param type
	 *            筛选类型
	 */
	public static boolean getDynamicNewList(Context context, IBindData bind, Handler handler, long userId, String modelType, int index, int size) {
		try {
			JSONObject inObj = new JSONObject();

			inObj.put("userId", userId);
			inObj.put("modelType", modelType);
			inObj.put("index", index);
			inObj.put("size", size);

			String requestStr = inObj.toString();
			String url = getFullUrlWithDir("/dynamicNews/getListDynamicNews.json");
			doExecute(context, bind, EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_DYNAMICNEWSLIST, url, requestStr, handler);
			return true;
		} catch (Exception e) {

		}
		return false;
	}

	/**
	 * 发表评论
	 * 
	 * @param type
	 *            评论对象类型 1-需求；2-业务需求；3-任务；4-项目
	 * @param newsId
	 *            动态id
	 * @param content
	 *            评论内容
	 */
	public static boolean addDynamicComment(Context context, IBindData bind, Handler handler, int type, long id, String content) {
		try {
			JSONObject inObj = new JSONObject();
			inObj.put("newsId", id);
			inObj.put("content", content);
			String requestStr = inObj.toString();

			String url = getFullUrlWithDir("/dynamicNews/setDynamicComment.json");
			doExecute(context, bind, EAPIConsts.OrganizationReqType.ORG_COMMENT, url, requestStr, handler);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String getFullUrlWithDir(String method) {
		return EAPIConsts.TMS_URL + method;
	}

	/***
	 * 编辑黑名单
	 * 
	 * @param context
	 * @param bind
	 * @param listUserId
	 *            用户id列表
	 * @param type
	 *            1,加入黑名单;2,移出黑名单
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
		String url = EAPIConsts.TMS_URL + EAPIConsts.OrganizationUrl.EDITBLACKLIST_STR;
		doExecute(context, bind, EAPIConsts.OrganizationReqType.EDITBLACKLIST, url, requestStr, handler);
	}

	/**
	 * 获取黑名单列表
	 * 
	 * @param context
	 * @param bind
	 * @param index
	 * @param size
	 * @param handler
	 */
	public static void doGetBlacklist(Context context, IBindData bind, int index, int size, Handler handler) {
		String requestStr = "";

		try {
			JSONObject jObject = new JSONObject();
			jObject.put("index", index);
			jObject.put("size", size);
			requestStr = jObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		String url = EAPIConsts.TMS_URL + EAPIConsts.OrganizationUrl.BLACKLIST_STR;
		doExecute(context, bind, EAPIConsts.OrganizationReqType.BLACKLIST, url, requestStr, handler);
	}

	/** 正式解除好友关系 */
	public static void dodeleteFriend(Context context, IBindData bind, long createById, int userType, Handler handler) {

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("id", createById);
			jsonObject.put("userType", userType);
			String requestStr = jsonObject.toString();
			String url = EAPIConsts.TMS_URL + EAPIConsts.OrganizationUrl.DELETEFRIEND_STR;
			/** 8个参数的 顺序 context 数据上下文 bind 返回接口 联网类型 请求地址 JSON串 错误处理hander */
			doExecute(context, bind, EAPIConsts.OrganizationReqType.ORG_DELETEFRIED, url, requestStr, handler);
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/*
	 * 获取人脉首页的列表数据
	 */
	public static void doGetPeople(Context context, IBindData bind, long typeId, long regionId, long careerId, int index, int size, Handler handler) {
		String requestStr = "";
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("typeId", typeId);
			jObj.put("regionId", regionId);
			jObj.put("careerId", careerId);
			jObj.put("index", index);
			jObj.put("size", size);
			requestStr = jObj.toString();
			System.out.println("requestStr===" + requestStr);
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String url = EAPIConsts.TMS_URL + EAPIConsts.PeopleRequestUrl.PEOPLE_HOMELIST_STR;

		doExecute(context, bind, EAPIConsts.PeopleRequestType.PEOPLE_REQ_HOME, url, requestStr, handler);

	}

	/*
	 * 删除组织客户5.3
	 */
	public static void doDeleteOrgAndCustomer(Context context, IBindData bind, long customerId, Handler handler) {
		String requestStr = "";
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("customerId", customerId);
			requestStr = jObj.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		String url = EAPIConsts.TMS_URL + EAPIConsts.OrganizationUrl.DELETEORGANDCUSTOMER;

		doExecute(context, bind, EAPIConsts.OrganizationReqType.ORAGANIZATION_REQ_DELCUSANDORG, url, requestStr, handler);
		System.out.println("organizationReqUtil  5。3删除人脉/组织走了");
	}

	/**
	 * 客户分组列表查询
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 */
	public static void doCustomerGroupListQuery(Context context, IBindData bind, Handler handler) {
		String requestStr = "";
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("","");
			requestStr = jObj.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}

		String url = EAPIConsts.TMS_URL + EAPIConsts.OrganizationUrl.ORGANIZATION_REQ_CUSTOMER_LIST_QUERY;
		doExecute(context, bind, EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_CUSTOMER_LIST_QUERY, url, requestStr, handler);

	}

	/**
	 * 客户分组列表 添加分组
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 */
	public static void doCustomerAddGroup(Context context, IBindData bind, String name, String parentId, Handler handler) {
		String requestStr = "";
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("name", name);
			jObj.put("parentId", parentId);
			requestStr = jObj.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}

		String url = EAPIConsts.TMS_URL + EAPIConsts.OrganizationUrl.ORGANIZATION_REQ_ADD_GROUPING;
		doExecute(context, bind, EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_ADD_GROUPING, url, requestStr, handler);

	}

	/**
	 * 更新客户分组
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * 
	 */
	public static void doCustomerUpdaetGroup(Context context, IBindData bind, String id, String name, Handler handler) {
		String requestStr = "";
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("id", id);
			jObj.put("name", name);
			requestStr = jObj.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}

		String url = EAPIConsts.TMS_URL + EAPIConsts.OrganizationUrl.ORGANIZATION_REQ_UPDAET_GROUPING;
		doExecute(context, bind, EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_UPDAET_GROUPING, url, requestStr, handler);

	}

	/**
	 * 删除客户分组
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * 
	 */
	public static void doCustomerDeleteGroup(Context context, IBindData bind, String id, Handler handler) {
		String requestStr = "";
		try {
			JSONObject jObj = new JSONObject();
			jObj.put("id", id);
			requestStr = jObj.toString();
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}

		String url = EAPIConsts.TMS_URL + EAPIConsts.OrganizationUrl.ORGANIZATION_REQ_DELETE_GROUPING;
		doExecute(context, bind, EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_DELETE_GROUPING, url, requestStr, handler);

	}

	/**
	 * 查询用户的标签使用个数
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @return
	 */
	public static boolean getOrgAndCustTag(Context context, IBindData bind, Handler handler) {
		JsonObject json = new JsonObject();
		json.addProperty("index", 0);
		json.addProperty("size", 999); // 页数和大小没有用
		String requestStr = json.toString();
		String url = EAPIConsts.getTMSUrl() + EAPIConsts.OrganizationUrl.ORGANIZATION_REQ_TAG_QUERY;
		doExecute(context, bind, EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_TAG_QUERY, url, requestStr, handler);
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
	public static boolean deleteTag(Context context, IBindData bind, int id, Handler handler) {
		JSONObject json = new JSONObject();
		try {
			json.put("id", id);
			String requestStr = json.toString();
			String url = EAPIConsts.getTMSUrl() + EAPIConsts.OrganizationUrl.ORGANIZATION_REQ_DELETE_TAG;
			doExecute(context, bind, EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_DELETE_TAG, url, requestStr, handler);
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
	public static boolean saveTag(Context context, IBindData bind, String tag, int id, Handler handler) {
		JsonObject json = new JsonObject();
		json.addProperty("tag", tag);
		json.addProperty("id", id);
		String requestStr = json.toString();
		String url = EAPIConsts.getTMSUrl() + EAPIConsts.OrganizationUrl.ORGANIZATION_REQ_SAVE_TAG;
		doExecute(context, bind, EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_SAVE_TAG, url, requestStr, handler);
		return true;
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
		json.addProperty("tag", tag);
		String requestStr = json.toString();
		String url = EAPIConsts.getTMSUrl() + EAPIConsts.OrganizationUrl.ORGANIZATION_REQ_SAVE_TAG;
		doExecute(context, bind, EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_SAVE_TAG, url, requestStr, handler);
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
	public static boolean getTagList(Context context, IBindData bind, Handler handler) {
		JsonObject json = new JsonObject();
		json.addProperty("type", 1);
		json.addProperty("size", 0);
		json.addProperty("index", 0);
		String requestStr = json.toString();
		String url = EAPIConsts.getTMSUrl() + EAPIConsts.OrganizationUrl.ORGANIZATION_REQ_TAG_LIST;
		doExecute(context, bind, EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_TAG_LIST_GINTONG, url,requestStr, handler);

		return true;
	}
	/**
	 * 获取注册组织的注册信息
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @return
	 */
	public static boolean doGetRegistOrgDetail(Context context, IBindData bind, String orgId, Handler handler) {
		JsonObject json = new JsonObject();
		json.addProperty("orgId", orgId); 
		String requestStr = json.toString();
		String url = EAPIConsts.getTMSUrl() + EAPIConsts.OrganizationUrl.ORGANIZATION_REQ_REGIST_ORG_DETAIL;
		doExecute(context, bind, EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_REGIST_ORG_DETAIL, url, requestStr, handler);
		return true;
	}
	/**
	 * 上传图片
	 * @param context
	 * @param bind
	 * @param type
	 * @param handler
	 */
	public static void doGetuploadIdCardImg(Context context, IBindData bind,  String type, Handler handler) {
		String requestStr = "";
		try {
			JSONObject jObject = new JSONObject();
			jObject.put("type", type);
			requestStr = jObject.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = EAPIConsts.TMS_URL + EAPIConsts.OrganizationUrl.UPLOADIDCARDIMG;
		doExecute(context, bind, EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_CREATE_ORG, url, requestStr, handler);
	}
	
	/**
	 * 完善组织信息
	 * @param context
	 * @param bind
	 * @param object
	 * @param handler
	 */
	public static void doSaveOrganization(Context context, IBindData bind,  Object object, Handler handler) {
		 
		Gson gson = new Gson();
		String requestStr = gson.toJson((OrgInfoVo)object);
		
		String url = EAPIConsts.TMS_URL + EAPIConsts.OrganizationUrl.SAVE_ORGANIZATION;
		doExecute(context, bind, EAPIConsts.OrganizationReqType.SAVE_ORGANIZATION, url, requestStr, handler);
	}

}
