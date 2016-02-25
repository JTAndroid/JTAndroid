package com.tr.api;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tr.model.demand.DemandASSO;
import com.tr.model.demand.DemandDetailsData;
import com.tr.model.demand.TemplateData;
import com.utils.http.EAPIConsts;
import com.utils.http.IBindData;

/**
 * @ClassName: DemandReqUtil.java
 * @Description: 需求相关的接口
 * @Author fxtx
 * @Version v 1.0
 * @Date 2015-03-05
 * @LastEdit 2014-03-05
 */
public class DemandReqUtil extends ReqBase {

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
	 * 获取模版列表信息接口
	 * 
	 * @param context
	 * @param bind
	 * @param index
	 *            :页码
	 * @param size
	 *            ：每页大小
	 * @param dempandType
	 *            ：模版类型
	 * @param handler
	 * @return
	 */
	public static boolean getTemplatelist(Context context, IBindData bind,
			int index, int size, int dempandType, Handler handler) {
		JSONObject json = new JSONObject();
		try {
			json.put("index", index);
			json.put("size", size);
			json.put("demandType", dempandType);
			String requestStr = json.toString();
			String url = getFullUrl(EAPIConsts.ReqUrl.Demand_GetTemplatelist);
			doExecute(context, bind,
					EAPIConsts.demandReqType.demand_gettemplatelist, url,
					requestStr, handler);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 将对象转换成json 对象
	 * 
	 * @param object
	 * @return
	 */
	public static JsonElement getJsonElement(Object object) {
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		return parser.parse(gson.toJson(object));
	}

	/**
	 * 保存自定义模版
	 * 
	 * @param context
	 * @param bind
	 * @param template
	 * @param handler
	 * @return
	 */
	public static boolean saveTemplatelist(Context context, IBindData bind,
			TemplateData template, Handler handler) {
		JsonObject json = new JsonObject();
		json.add("demandTemplate", getJsonElement(template));
		String requestStr = json.toString();
		String url = getFullUrl(EAPIConsts.ReqUrl.Demand_SaveTemplate);
		doExecute(context, bind, EAPIConsts.demandReqType.demand_saveTemplate,
				url, requestStr, handler);
		return true;

	}

	/**
	 * 找项目/找投资
	 * 
	 * @param context
	 * @param bind
	 * @param index
	 *            起始页，第一页为0
	 * @param size
	 *            页大小，默认为20
	 * @param industryId
	 *            行业ID 可以为null
	 * @param typeId
	 *            类型ID 可以为null
	 * @param areaId
	 *            地区ID 可以为null
	 * @param beginAmount
	 *            需求开始金额
	 * @param endAmount
	 *            需求结束金额 默认和不选择起始和结束都传0即可
	 * @param unit
	 *            金额类型字段
	 * @param demandType
	 *            需求类型（1-投资 2-融资）
	 * @param handler
	 * @return
	 */
	public static boolean getProjectList(Context context, IBindData bind,
			int index, int size, String industryId, String typeId,
			String areaId, String unit, float beginAmount, float endAmount,
			int demandType, Handler handler) {
		JSONObject json = new JSONObject();
		try {
			json.put("index", index);
			json.put("size", size);
			json.put("industry", industryId); //
			json.put("beginAmount", beginAmount);// 起始金额
			json.put("endAmount", endAmount);// 结束金额
			json.put("unit", unit);// 金额种类
			json.put("type", typeId); // 类型id
			json.put("area", areaId);// 地址id
			json.put("demandType", demandType); // 需求种类
			String requestStr = json.toString();
			String url = getFullUrl(EAPIConsts.ReqUrl.Demand_GetProjectList);
			doExecute(context, bind,
					EAPIConsts.demandReqType.demand_getProjectList, url,
					requestStr, handler);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 查询我的需求列表接口
	 * 
	 * @param context
	 * @param bind
	 * @param index
	 *            起始页，第一页为0
	 * @param size
	 *            页大小，默认为20
	 * @param optType
	 *            查询类型（0全部1新增2保存3收藏） 全局搜索也是一样         1新增与2保存 合并了，都是2
	 * @param type
	 *            查询类型1 全局搜索，2我的需求搜索
	 * @param handler
	 * @return
	 */
	public static boolean getMyNeedList(Context context, IBindData bind,
			int index, int size, int optType, int type, String keyword,
			Handler handler) {
		JSONObject json = new JSONObject();
		try {
			json.put("optType", optType);
			json.put("type", type);
			json.put("size", size);
			json.put("page", index);
			json.put("keyWord", keyword);
			String requestStr = json.toString();
			String url = getFullUrl(EAPIConsts.ReqUrl.Demand_GetMyNeedList);
			doExecute(context, bind,
					EAPIConsts.demandReqType.demand_getMyNeedList, url,
					requestStr, handler);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * 删除模版接口
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @return
	 */
	public static boolean deleteTemplate(Context context, IBindData bind,
			String id, Handler handler) {
		JSONObject json = new JSONObject();
		try {
			json.put("id", id);
			String requestStr = json.toString();
			String url = getFullUrl(EAPIConsts.ReqUrl.Demand_deletedemandtemplate);
			doExecute(context, bind,
					EAPIConsts.demandReqType.demand_deletedtemplate, url,
					requestStr, handler);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * 删除我的需求 多条批量操作
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @return
	 */
	public static boolean deleteDemandList(Context context, IBindData bind,
			Handler handler, String demandIds) {
		JSONObject json = new JSONObject();
		try {
			json.put("demandIds", demandIds);
			String requestStr = json.toString();
			String url = getFullUrl(EAPIConsts.ReqUrl.Demand_mydemandDelete);
			doExecute(context, bind,
					EAPIConsts.demandReqType.demand_mydemandDelete, url,
					requestStr, handler);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 删除我的需求，在需求详情中点击删除
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @param demandIds
	 * @return
	 */
	public static boolean deleteMyDemand(Context context, IBindData bind,
			Handler handler, String demandId) {
		JSONObject json = new JSONObject();
		try {
			json.put("demandId", demandId);
			String requestStr = json.toString();
			String url = getFullUrl(EAPIConsts.ReqUrl.Demand_deleteMyDemand);
			doExecute(context, bind,
					EAPIConsts.demandReqType.demand_deleteMyDemand, url,
					requestStr, handler);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取需求详情界面
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @return
	 */
	public static boolean getDemandDetail(Context context, IBindData bind,
			Handler handler, String demandId, int from) {
		JSONObject json = new JSONObject();
		try {
			json.put("demandId", demandId);
			json.put("from", from);
			String requestStr = json.toString();
			String url = getFullUrl(EAPIConsts.ReqUrl.Demand_getDemandDetail);
			doExecute(context, bind,
					EAPIConsts.demandReqType.demand_getDemandDetail, url,
					requestStr, handler);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取目录信息接口 （多级信息）
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @return
	 */
	public static boolean getTreeCategory(Context context, IBindData bind,
			String userId, Handler handler) {
		JSONObject json = new JSONObject();
		try {
			json.put("userId", userId);
			String requestStr = json.toString();
			String url = getFullUrl(EAPIConsts.ReqUrl.Demand_categoryQueryTree);
			doExecute(context, bind,
					EAPIConsts.demandReqType.demand_categoryQueryTree, url,
					requestStr, handler);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 添加或修改用户目录接口
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @return
	 */
	public static boolean createCategory(Context context, IBindData bind,
			Handler handler, String categoryName, long pid, long id) {
		JSONObject json = new JSONObject();
		try {
			json.put("categoryName", categoryName);
			json.put("pid", pid);
			json.put("id", id);
			String requestStr = json.toString();
			String url = getFullUrl(EAPIConsts.ReqUrl.Demand_createCategory);
			doExecute(context, bind,
					EAPIConsts.demandReqType.demand_createCategory, url,
					requestStr, handler);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 删除用户目录
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @return
	 */
	public static boolean deletCategory(Context context, IBindData bind,
			long id, Handler handler) {
		JSONObject json = new JSONObject();
		try {
			json.put("id", id);
			String requestStr = json.toString();
			String url = getFullUrl(EAPIConsts.ReqUrl.Demand_deleteCategory);
			doExecute(context, bind,
					EAPIConsts.demandReqType.demand_deleteCategory, url,
					requestStr, handler);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 保存需求标签关系（批量打标签）
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @return
	 */
	public static boolean createDemandTag(Context context, IBindData bind,
			String demandIds, String tagIds, Handler handler) {
		JSONObject json = new JSONObject();
		try {
			json.put("demandIds", demandIds);
			json.put("tagIds", tagIds);
			String requestStr = json.toString();
			String url = getFullUrl(EAPIConsts.ReqUrl.Demand_rTagSave);
			doExecute(context, bind, EAPIConsts.demandReqType.demand_rTagSave,
					url, requestStr, handler);
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
	public static boolean deleteDemandTag(Context context, IBindData bind,
			String demandIds, int tagid, Handler handler) {
		JSONObject json = new JSONObject();
		try {
			json.put("demandIds", demandIds);
			json.put("tagId", tagid);
			String requestStr = json.toString();
			String url = getFullUrl(EAPIConsts.ReqUrl.Demand_rTagDelete);
			doExecute(context, bind,
					EAPIConsts.demandReqType.demand_rTagDelete, url,
					requestStr, handler);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 通过标签获取需求信息列表数据
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @return
	 */
	public static boolean getTagDemandList(Context context, IBindData bind,
			int size, int page, int tagid, Handler handler) {
		JSONObject json = new JSONObject();
		try {
			json.put("size", size);
			json.put("page", page);
			json.put("tagId", tagid);
			String requestStr = json.toString();
			String url = getFullUrl(EAPIConsts.ReqUrl.Demand_rTagQueryByTagId);
			doExecute(context, bind,
					EAPIConsts.demandReqType.demand_rTagQueryByTagId, url,
					requestStr, handler);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
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
			String url = getFullUrl(EAPIConsts.ReqUrl.Demand_deleteTag);
			doExecute(context, bind, EAPIConsts.demandReqType.demand_deleteTag,
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
		String url = getFullUrl(EAPIConsts.ReqUrl.Demand_saveTag);
		doExecute(context, bind, EAPIConsts.demandReqType.demand_saveTag, url,
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
	public static boolean getDemandTag(Context context, IBindData bind,
			Handler handler) {
		JsonObject json = new JsonObject();
		json.addProperty("page", 1);
		json.addProperty("size", 20); // 页数和大小没有用
		String requestStr = json.toString();
		String url = getFullUrl(EAPIConsts.ReqUrl.Demand_getTagQuery);
		doExecute(context, bind, EAPIConsts.demandReqType.demand_getTagQuery,
				url, requestStr, handler);
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
		json.addProperty("index", 1);
		String requestStr = json.toString();
		String url = getFullUrl(EAPIConsts.ReqUrl.Demand_tagList);
		doExecute(context, bind, EAPIConsts.demandReqType.demand_tag_list, url,
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
		JSONObject json = new JSONObject();
		try {
			json.put("index", index);
			json.put("size", size);
			String requestStr = json.toString();
			String url = getFullUrl(EAPIConsts.ReqUrl.Demand_QueryTag);
			doExecute(context, bind, EAPIConsts.demandReqType.demand_QueryTag,
					url, requestStr, handler);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 保存需求目录关系（批量设置目录）
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @return
	 */
	public static boolean createDemandCategory(Context context, IBindData bind,
			Handler handler, String demandIds, String categoryIds) {
		JSONObject json = new JSONObject();
		try {
			json.put("demandIds", demandIds);
			json.put("categoryIds", categoryIds);
			String requestStr = json.toString();
			String url = getFullUrl(EAPIConsts.ReqUrl.Demand_rCategorySave);
			doExecute(context, bind,
					EAPIConsts.demandReqType.demand_rCategorySave, url,
					requestStr, handler);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 查询需求目录关系
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @return
	 */
	public static boolean getDemandCategory(Context context, IBindData bind,
			int size, int page, long category, Handler handler) {
		JSONObject json = new JSONObject();
		try {
			json.put("page", page);
			json.put("size", size);
			json.put("id", category);
			String requestStr = json.toString();
			String url = getFullUrl(EAPIConsts.ReqUrl.Demand_rCategoryQuery);
			doExecute(context, bind,
					EAPIConsts.demandReqType.demand_rCategoryQuery, url,
					requestStr, handler);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 创建需求信息
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @return
	 */
	public static boolean createDemand(Context context, IBindData bind,
			String code, DemandDetailsData demanddetails, DemandASSO asso,
			Handler handler) {
		JsonObject json = new JsonObject();
		json.add("demand", getJsonElement(demanddetails));
		JsonElement assoJsonElement = getJsonElement(asso);
		if (assoJsonElement == null) {
			json.addProperty("asso", "");// 列表数据
		} else {
			json.add("asso", assoJsonElement);// 列表数据
		}
		json.addProperty("vCode", code);// 验证码
		String requestStr = json.toString();
		String url = getFullUrl(EAPIConsts.ReqUrl.Demand_createDemand);
		doExecute(context, bind, EAPIConsts.demandReqType.demand_createDemand,
				url, requestStr, handler);
		return true;
	}

	/**
	 * 编辑需求详情
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @return
	 */
	public static boolean updateDemand(Context context, IBindData bind,
			String code, DemandDetailsData demanddetails, DemandASSO asso,
			Handler handler) {
		JsonObject json = new JsonObject();
		json.add("demand", getJsonElement(demanddetails));
		JsonElement assoJsonElement = getJsonElement(asso);
		if (assoJsonElement == null) {
			json.addProperty("asso", "");// 列表数据
		} else {
			json.add("asso", assoJsonElement);// 列表数据
		}
		json.addProperty("vCode", code);// 验证码
		String requestStr = json.toString();
		String url = getFullUrl(EAPIConsts.ReqUrl.Demand_updateDemand);
		doExecute(context, bind, EAPIConsts.demandReqType.demand_updateDemand,
				url, requestStr, handler);
		return true;
	}

	/**
	 * 查看需求介绍内容信息
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @return
	 */
	public static boolean getDemandFile(Context context, IBindData bind,
			String taskId, Handler handler) {
		JSONObject json = new JSONObject();
		try {
			json.put("taskId", taskId);
			String requestStr = json.toString();
			String url = getFullUrl(EAPIConsts.ReqUrl.Demand_findDemandFile);
			doExecute(context, bind,
					EAPIConsts.demandReqType.demand_findDemandFile, url,
					requestStr, handler);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 保存需求详情接口，将当前需求保存为用户已保存需求
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @return
	 */
	public static boolean othersSaveDemand(Context context, IBindData bind,
			Handler handler, String demandId) {
		JSONObject json = new JSONObject();
		try {
			json.put("demandId", demandId);
			String requestStr = json.toString();
			String url = getFullUrl(EAPIConsts.ReqUrl.Demand_saveOthersDemand);
			doExecute(context, bind,
					EAPIConsts.demandReqType.demand_saveOthersDemand, url,
					requestStr, handler);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 收藏需求详情接口，将当前需求收藏为用户已收藏需求
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @return
	 */
	public static boolean othersCollectDemand(Context context, IBindData bind,
			Handler handler, String demandId) {
		JSONObject json = new JSONObject();
		try {
			json.put("demandId", demandId);
			String requestStr = json.toString();
			String url = getFullUrl(EAPIConsts.ReqUrl.Demand_collectOthersDemand);
			doExecute(context, bind,
					EAPIConsts.demandReqType.demand_collectOthersDemand, url,
					requestStr, handler);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取需求的关联信息数据 ASSO 信息
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @return
	 */
	public static boolean getDemandASSO(Context context, IBindData bind,
			Handler handler, String demandId) {
		JSONObject json = new JSONObject();
		try {
			json.put("demandId", "");
			json.put("index", ""); // 当前页从0开始",
			json.put("size", "");
			String requestStr = json.toString();
			String url = getFullUrl(EAPIConsts.ReqUrl.Demand_getDemandASSO);
			doExecute(context, bind,
					EAPIConsts.demandReqType.demand_getDemandASSO, url,
					requestStr, handler);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}


	/**
	 * 删除需求附件功能
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @return
	 */
	public static boolean deleteDemandFile(Context context, IBindData bind,
			String id, Handler handler) {
		JSONObject json = new JSONObject();
		try {
			json.put("id", id);
			String requestStr = json.toString();
			String url = getFullUrl(EAPIConsts.ReqUrl.Demand_deleteFile);
			doExecute(context, bind,
					EAPIConsts.demandReqType.demand_deleteFile, url,
					requestStr, handler);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 举报信息提交
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @return
	 */
	public static boolean getReportMessageList(Context context, IBindData bind,
			String content, String phone, String reason, String demandId,
			Handler handler) {
		JSONObject json = new JSONObject();
		try {
			json.put("content", content);
			json.put("contact", phone);
			json.put("reason", reason);
			json.put("demandId", demandId);
			String requestStr = json.toString();
			String url = getFullUrl(EAPIConsts.ReqUrl.Demand_reportDemand);
			doExecute(context, bind,
					EAPIConsts.demandReqType.demand_reportDemand, url,
					requestStr, handler);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 删除需求附件功能
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * 
	 *            "demandId":需求id, "creatorId":需求创建人id, "index":起始页，第一页为0,
	 *            "size":页大小，默认为20
	 * 
	 * @return
	 */
	public static boolean getDemandCommentList(Context context, IBindData bind,
			String demandId, int index, int size, Handler handler) {
		JSONObject json = new JSONObject();
		try {
			json.put("demandId", demandId);
			json.put("index", index);
			json.put("size", size);
			String requestStr = json.toString();
			String url = getFullUrl(EAPIConsts.ReqUrl.Demand_DemandCommentList);
			doExecute(context, bind,
					EAPIConsts.demandReqType.demand_DemandCommentList, url,
					requestStr, handler);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 发布评论
	 * 
	 * @param context
	 * @param bind
	 * @param handler
	 * @return "demandId":"需求id", "visable":"是1否0", "content":"评论内容";
	 *
	 */
	public static boolean addDemandComment(Context context, IBindData bind,
			String demandId, int visable, String content, Handler handler) {
		JSONObject json = new JSONObject();
		try {
			json.put("demandId", demandId);
			json.put("visable", visable);
			json.put("content", content);
			String requestStr = json.toString();
			String url = getFullUrl(EAPIConsts.ReqUrl.Demand_addDemandComment);
			doExecute(context, bind,
					EAPIConsts.demandReqType.demand_addDemandComment, url,
					requestStr, handler);
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}

}
