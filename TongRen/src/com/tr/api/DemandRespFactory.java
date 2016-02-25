package com.tr.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tr.model.demand.DemandASSO;
import com.tr.model.demand.DemandCommentListItem;
import com.tr.model.demand.DemandDetailsData;
import com.tr.model.demand.IntroduceData;
import com.tr.model.demand.LableData;
import com.tr.model.demand.NeedItemListItem;
import com.tr.model.demand.TemplateList;
import com.tr.model.demand.TemplateListItem;
import com.tr.model.knowledge.UserCategory;
import com.utils.http.EAPIConsts;

/**
 * @ClassName: DemandRespFactory.java
 * @Description: 需求相关的接口的回调
 * @Author fxtx
 * @Version v 1.0
 * @Date 2015-03-05
 * @LastEdit 2014-03-05
 */
public class DemandRespFactory {

	public static Object createMsgObject(int msgId, JSONObject response,
			JSONObject errorJson) throws JSONException {
		Gson gson = new Gson();
		Object obj = null;
		if (errorJson == null) {
			errorJson = new JSONObject();
			errorJson.put("notifCode", "-1");
			errorJson.put("notifInfo", "未知错误");
		}
		switch (msgId) {
		case EAPIConsts.demandReqType.demand_gettemplatelist: // 获取模版列表接口
			obj = gson.fromJson(response.toString(), TemplateList.class);
			break;
		case EAPIConsts.demandReqType.demand_saveTemplate: // 创建模版接口 保存模版
			Map<Integer, Object> template = new HashMap<Integer, Object>();
			template.put(1, response.optBoolean("succeed"));
			if (errorJson != null && !errorJson.isNull("notifInfo")) {
				template.put(-1, errorJson.opt("notifInfo")); // 错误原因 创建错误的原因
			}
			obj = template;
			break;
		case EAPIConsts.demandReqType.demand_getMyNeedList: // 获取我的需求列表接口
			obj = NeedItemListItem.createFactory(response, "list");
//			obj = gson.fromJson(response.toString(), NeedItemListItem.class);
			break;
		case EAPIConsts.demandReqType.demand_getProjectList:// 找项目找资金 列表获取接口
			obj = NeedItemListItem.createFactory(response, "list");
			break;
		case EAPIConsts.demandReqType.demand_deletedtemplate:
			// 删除模版接口
			obj = response.optBoolean("succeed");// 返回成功和失败的boolean
			break;
		case EAPIConsts.demandReqType.demand_mydemandDelete:
			obj = response.optBoolean("succeed");// 返回成功和失败的boolean
			// 删除我的需求接口
			break;
		case EAPIConsts.demandReqType.demand_getDemandDetail:
			// 获取需求详情界面
			Map<Integer, Object> map = new HashMap<Integer, Object>();
			try {

				if (response.optBoolean("succeed")) {
					map.put(1, true);

					// 获取asso信息对象
					if (!response.isNull("demandAsso")) {
						String asso = response.optJSONObject("demandAsso")
								.toString();
						map.put(4, gson.fromJson(asso, DemandASSO.class));
					}
					if (!response.isNull("categorys")) {
						// 获取目录信息
						map.put(5, gson.fromJson(
								response.optJSONArray("categorys").toString(),
								new TypeToken<List<UserCategory>>() {
								}.getType()));
					}
					if (!response.isNull("tags")) {
						// 获取列表对象数组
						map.put(6, gson.fromJson(response.optJSONArray("tags")
								.toString(), new TypeToken<List<LableData>>() {
						}.getType()));
					}
					if (!response.isNull("demand")) {
						map.put(2, gson.fromJson(
								response.optJSONObject("demand").toString(),
								DemandDetailsData.class));
					}
					map.put(3, response.optInt("dule"));// "dule":" -1——本人创建,1-独乐,2-大乐,3-中乐,4-小乐"
					map.put(7, response.optString("virtual"));//"virtual":1——组织，2——用户 
				} else {
					map.put(-1, errorJson.opt("notifInfo")); // 错误原因
					map.put(1, false);
				}
			} catch (Exception e) {
				map.put(1, false);
			}
			obj = map;
			break;
		case EAPIConsts.demandReqType.demand_saveTag:
			// 获取标签接口
			// 创建标签
			int type = response.optInt("resultType");
			if (type == 0) {
				obj = gson.fromJson(response.getJSONObject("obj").toString(),
						LableData.class);
			} else {
				obj = gson.fromJson(response.getString("resultMessage"),
						String.class);
			}
			break;

		case EAPIConsts.demandReqType.demand_QueryTag: // 查询用户的所有标签
		case EAPIConsts.demandReqType.demand_tag_list:// 查询金桐推荐标签
		case EAPIConsts.demandReqType.demand_getTagQuery:// 查询用户的标签使用个数
															// 在我的需求列表中进行查询
			// 查询我的标签 和金桐推荐标签
			obj = LableData.createFactory(response);
			break;
		case EAPIConsts.demandReqType.demand_categoryQueryTree:// 获取目录
		case EAPIConsts.demandReqType.demand_deleteCategory: // 删除目录
		case EAPIConsts.demandReqType.demand_createCategory:// 添加或修改用户
			// 获取目录信息接口
			Map<String, Object> dataMap = new HashMap<String, Object>(); // 返回数据
			String strKey = "";
			strKey = "succeed";
			if (!response.isNull(strKey)) {
				dataMap.put(strKey, response.optBoolean(strKey));
			}
			strKey = "categorys";
			if (!response.isNull(strKey)) {
				dataMap.put("listUserCategory", gson.fromJson(
						response.optString(strKey),
						new TypeToken<List<UserCategory>>() {
						}.getType()));
			}
			obj = dataMap;
			break;
		case EAPIConsts.demandReqType.demand_deleteTag:
			// 删除标签接口
			obj = response.optBoolean("succeed");// 返回成功和失败的boolean
			break;
		case EAPIConsts.demandReqType.demand_rTagSave:// 保存需求标签关系(批量打标签)
		case EAPIConsts.demandReqType.demand_rCategorySave: // 保存需求目录关系（批量设置目录）

			obj = response.optBoolean("succeed");// 返回成功和失败的boolean
			break;
		case EAPIConsts.demandReqType.demand_rTagDelete:// /删除标签关系
		case EAPIConsts.demandReqType.demand_rCategoryDelete:// 删除需求目录关系

			obj = response.optBoolean("succeed");// 返回成功和失败的boolean
			break;
		case EAPIConsts.demandReqType.demand_rTagQueryByTagId: // 查询需求标签关系
		case EAPIConsts.demandReqType.demand_rCategoryQuery: // 查询需求目录关系
			obj = NeedItemListItem.createFactory(response, "list");
			break;
		case EAPIConsts.demandReqType.demand_createDemand: // 创建需求详情
		case EAPIConsts.demandReqType.demand_updateDemand:// 编辑需求详情
			boolean isSuccess = response.optBoolean("succeed");// 返回成功和失败的boolean
			Map<Integer, String> msp = new HashMap<Integer, String>();
			if (isSuccess) {
				msp.put(1, response.getString("demandId"));
			} else {
				msp.put(2, errorJson.getString("notifInfo"));// 打印错误原因
			}
			obj = msp;
			break;
		case EAPIConsts.demandReqType.demand_findDemandFile:
			// 查询 介绍内容
			boolean isboolean = response.optBoolean("succeed");
			if (isboolean) {
				obj = IntroduceData.createFactory(response
						.getJSONObject("demandFileVO"));
			}
			break;
		case EAPIConsts.demandReqType.demand_DemandDetail:
			// 编辑时查询需求详情 接口 废弃
			break;
		case EAPIConsts.demandReqType.demand_saveOthersDemand://保存
		case EAPIConsts.demandReqType.demand_deleteMyDemand://删除
		case EAPIConsts.demandReqType.demand_collectOthersDemand://收藏
			// 保存需求接口
			Map<Integer,Object> otherDemand = new HashMap<Integer, Object>();
			if (!response.isNull("succeed")) {
				otherDemand.put(1, response.getBoolean("succeed"));
			}else{
				otherDemand.put(1, false);
				if (!errorJson.isNull("notifInfo")) {
					otherDemand.put(2, errorJson.getString("notifInfo"));// 获取错误信息
				}
			}
			obj = otherDemand;
			break;
		case EAPIConsts.demandReqType.demand_DemandCommentList:
			// 需求评论列表
			Map<Integer, Object> comment = new HashMap<Integer, Object>();
			if (!response.isNull("succeed")) {
				comment.put(1, response.getBoolean("succeed"));
			}else{
				comment.put(1, false);
			}
			comment.put(2, DemandCommentListItem.createFactory(response));
			obj = comment;
			break;
		case EAPIConsts.demandReqType.demand_reportDemand:
			// 需求举报接口

			boolean isboop = response.optBoolean("succeed");// 返回成功和失败的boolean
			if (isboop) {// 举报成功了
				obj = true;
			} else {
				String errorInfo = "失败";
				if (!errorJson.isNull("notifInfo")) {
					errorInfo = errorJson.getString("notifInfo");// 获取错误信息
				}
				obj = errorInfo;
			}
			break;
		case EAPIConsts.demandReqType.demand_getDemandASSO:
			// 需求详情页获取关联信息
			break;
		case EAPIConsts.demandReqType.demand_findDemand:
			// 查看需求
			break;
		case EAPIConsts.demandReqType.demand_deleteFile:
			// 删除附件
			obj = response.optBoolean("succeed");
			break;
		case EAPIConsts.demandReqType.demand_addDemandComment:// 需求评论
			boolean succeed = response.optBoolean("succeed");
			if (succeed) { // 发送成功
				obj = true;
			} else {
				obj = errorJson.getString("notifInfo");// 获取错误信息
			}
			break;
		}
		return obj;
	}
}
