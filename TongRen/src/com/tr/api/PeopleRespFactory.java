package com.tr.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tr.model.demand.LableData;
import com.tr.model.knowledge.UserCategory;
import com.tr.model.knowledge.UserTag;
import com.tr.ui.organization.model.notice.CustomerNotice;
import com.tr.ui.people.model.BaseResult;
import com.tr.ui.people.model.ConvertToPeople;
import com.tr.ui.people.model.CustomerMeetingDetail_requrst;
import com.tr.ui.people.model.MeetList;
import com.tr.ui.people.model.MeetSave;
import com.tr.ui.people.model.PeopleCodeList;
import com.tr.ui.people.model.PeopleDetails;
import com.tr.ui.people.model.PeopleMergeList;
import com.tr.ui.people.model.Person;
import com.tr.ui.people.model.PersonPage;
import com.tr.ui.people.model.PersonSimpleList;
import com.tr.ui.people.model.UserCommentList;
import com.tr.ui.people.model.CommentApproverLists.CommentApproverLists;
import com.tr.ui.people.model.success.AddBooleanSuccess;
import com.tr.ui.people.model.success.AgreeToCancleBoolean;
import com.tr.ui.people.model.success.ContactsDeleteEvaluateLableParamsSuccess;
import com.utils.http.EAPIConsts;
import com.utils.http.EAPIConsts.PeopleRequestType;

//import com.tr.ui.people.model.PersonPage;
//import com.tr.ui.people.model.PersonPage;

public class PeopleRespFactory {

	private static Gson gson;
	private static String strKey;
	private static String jsonStr;

	public static Object doPeopleFromAPI(int msgId, JSONObject jsonObject)
			throws JSONException {
		gson = new Gson();
		strKey = "";
		if (jsonObject == null) {
			return null;
		}
		Object obj = null;
		switch (msgId) {
		case PeopleRequestType.PEOPLE_REQ_GETPEOPLE: // 获取人脉详情
			obj = gson.fromJson(jsonObject.toString(), PeopleDetails.class);
			break;
		case PeopleRequestType.PEOPLE_REQ_CREATE: // 新增/修改人脉
			obj = gson.fromJson(jsonObject.toString(), BaseResult.class);
			// obj = jsonObject.optLong("id");
			break;

		case PeopleRequestType.PEOPLE_REQ_PEOPLELIST: // 人脉列表
			obj = gson.fromJson(jsonObject.toString(), PersonSimpleList.class);
			PersonSimpleList p = (PersonSimpleList) obj;
			// System.out.println("p.success==="+p.success);
			// System.out.println("请求返回了数据");
			break;
		case PeopleRequestType.PEOPLE_REQ_REMOVE: // 删除人脉
			break;
		case PeopleRequestType.PEOPLE_REQ_HOME: // 人脉首页
			obj = gson.fromJson(jsonObject.toString(), PersonPage.class);
			System.out.println("人脉首页请求到了数据");
			break;
		case PeopleRequestType.PEOPLE_REQ_CONVERTPEOPLE: // 转为人脉
			Log.i("People", "转为人脉：" + jsonObject);
			obj = gson.fromJson(jsonObject.toString(), ConvertToPeople.class);
			break;
		case PeopleRequestType.PEOPLE_REQ_MERGELIST: // 可合并资料的人脉列表
			// Log.i("People","可合并资料的人脉列表="+jsonObject);
			obj = gson.fromJson(jsonObject.toString(), PeopleMergeList.class);
			break;
		case PeopleRequestType.PEOPLE_REQ_MERGE: // 合并人脉资料
			Log.i("People", "合并人脉资料：" + jsonObject);
			obj = gson.fromJson(jsonObject.toString(), BaseResult.class);
			break;
		case PeopleRequestType.PEOPLE_REQ_MEET_SAVE: // 保存会面记录
			// obj =jsonObject.optLong("id");
			obj = gson.fromJson(jsonObject.toString(), MeetSave.class);
			break;
		case PeopleRequestType.PEOPLE_REQ_MEET_UPDATE: // 修改会面记录
			break;
		case PeopleRequestType.PEOPLE_REQ_MEET_FINDLIST: // 查询会面情况列表
			// Log.i("People", "查询会面情况列表=" + jsonObject);
			obj = gson.fromJson(jsonObject.toString(), MeetList.class);
			// strKey = "meetList";
			// Log.v("TAG", " 查询会面情况列表");
			// if (!jsonObject.isNull(strKey)) {
			//
			// jsonStr = jsonObject.getJSONArray(strKey).toString();
			//
			// obj = gson.fromJson(jsonStr, new
			// TypeToken<List<CustomerMeetingDetail>>() {
			// }.getType());
			// }
			break;
		case PeopleRequestType.PEOPLE_REQ_MEET_FINDONE: // 查询会面情况
			obj = gson.fromJson(jsonObject.toString(),
					CustomerMeetingDetail_requrst.class);
			break;
		case PeopleRequestType.PEOPLE_REQ_MEET_DELETE: // 删除会面情况
			obj = jsonObject.optBoolean("success");

			break;

		case PeopleRequestType.PEOPLE_REQ_REMOVECATEGORY: // 删除目录
		case PeopleRequestType.PEOPLE_REQ_SAVEORUPDATECATEGORY: // 新增、修改目录
		case PeopleRequestType.PEOPLE_REQ_FINDCATEGORY: // 查询目录

			Map<String, Object> dataMap = new HashMap<String, Object>(); // 返回数据
			String strKey = "";
			String jsonStr = "";

			strKey = "success";
			if (!jsonObject.isNull(strKey)) {
				dataMap.put(strKey, jsonObject.optBoolean(strKey));
			}
			strKey = "listUserCategory";
			if (!jsonObject.isNull(strKey)) {
				// List<UserCategory> listCategory =
				// JSON.parseArray(jsonObject.getJSONArray(strKey).toString(),
				// UserCategory.class);
				String jsonString = jsonObject.getJSONArray(strKey).toString();
				List<UserCategory> listCategory = gson.fromJson(jsonString,
						new TypeToken<List<UserCategory>>() {
						}.getType());
				if (listCategory != null) {
					dataMap.put(strKey, listCategory);
				}
			}
			return dataMap;

		case PeopleRequestType.PEOPLE_REQ_COLLECTPEOPLE: // 收藏人脉
			break;
		case PeopleRequestType.PEOPLE_REQ_CANCELCOLLECT: // 取消收藏
			break;
		case PeopleRequestType.PEOPLE_REQ_PEOPLECODELIST: // 职业列表查询、分类列表查询
			// Log.i("People","职业列表查询、分类列表查询："+jsonObject);
			obj = gson.fromJson(jsonObject.toString(), PeopleCodeList.class);
			break;
		case PeopleRequestType.PEOPLE_REQ_FINDEVALUTE: // 获取该用户的评价
			Log.v("ADD", "进入获取该用户的评价啦啦啦");
			Log.v("ADD", "进入获取该用户的评价啦啦啦--->" + jsonObject.toString());
			obj = gson.fromJson(jsonObject.toString(), UserCommentList.class);
			Log.v("ADD", "获取该用户的评价" + obj.toString());

			break;
		case PeopleRequestType.PEOPLE_REQ_ADDEVALUATE: // 添加评价
			obj = gson.fromJson(jsonObject.toString(), AddBooleanSuccess.class);
			Log.v("ADD", "添加评价---->" + obj.toString());
			break;
		case PeopleRequestType.PEOPLE_REQ_FEEDBACKEVALUATE: // 赞同与取消赞同
			obj = gson.fromJson(jsonObject.toString(),
					AgreeToCancleBoolean.class);
			Log.v("ADD", "更多评价---->" + obj.toString());
			break;
		case PeopleRequestType.PEOPLE_REQ_DELETEEVALUATE: // 删除评价

			obj = gson.fromJson(jsonObject.toString(),
					ContactsDeleteEvaluateLableParamsSuccess.class);
			Log.v("WC", "返回评价成功了吗---->" + obj.toString());
			break;
		case PeopleRequestType.PEOPLE_REQ_MOREEVALUATE: // 更多评价
			obj = gson.fromJson(jsonObject.toString(),
					CommentApproverLists.class);
			// Log.v("ADD", "更多评价---->"+obj.toString());
			// Log.i("People","更多评价："+jsonObject);
			break;
		case PeopleRequestType.REPORT_SAVE: //举报
			// 删除标签接口
			obj = jsonObject.optBoolean("success");// 返回成功和失败的boolean
			break;
		case PeopleRequestType.COLLECT_PEOPLE: //收藏

			obj = jsonObject.optBoolean("success");// 返回成功和失败的boolean
			break;
		case PeopleRequestType.CANCEL_COLLECT: //取消收藏

			obj = jsonObject.optBoolean("success");// 返回成功和失败的boolean
			break;
		// ////////////////////////////////////////////////////////////////////////////////////////
		case EAPIConsts.PeopleRequestType.PEOPLE_REQ_TAG_SAVE:
			// 获取标签接口
			// 创建标签
			Map<String, Object> dataMap2 = new HashMap<String, Object>();
			ArrayList<LableData> lableDatas3 = new ArrayList<LableData>();
			if (jsonObject == null) {
				return null;
			}
			strKey = "success";
			if (!jsonObject.isNull(strKey)) {
				boolean flag = jsonObject.optBoolean(strKey);
				dataMap2.put(strKey, flag);
				if (!flag) {
						dataMap2.put("obj", null);
						return dataMap2;
				}
			}
			strKey = "tags";
			if (!jsonObject.isNull(strKey)) {
				JSONArray jsonArray = jsonObject.getJSONArray("tags");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsObject = (JSONObject) jsonArray.get(i);
					if (jsObject != null) {
						LableData lableData = new LableData();
						long tagid = jsObject.getLong("tagId");
						long numid = jsObject.getLong("num");
						String tagname = jsObject.getString("tagName");
						lableData.id = (int) tagid;
						lableData.num = (int) numid;
						lableData.tag = tagname;
						lableDatas3.add(lableData);
					}
				}
				dataMap2.put("obj", lableDatas3);
			}
			return dataMap2;
		case EAPIConsts.PeopleRequestType.PEOPLE_REQ_TAG_MY: // 查询用户的所有标签
			ArrayList<LableData> lableDatas = new ArrayList<LableData>();
			strKey = "success";
			if (!jsonObject.isNull(strKey)) {
				boolean boo = jsonObject.getBoolean(strKey);
				if (boo) {
					strKey = "page";
					if (!jsonObject.isNull(strKey)) {
						JSONObject json = jsonObject.optJSONObject(strKey);
						if (json != null) {
							strKey = "list";
							if (!json.isNull(strKey)) {
								JSONArray jsonArray = json.getJSONArray("list");
								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject jsObject = (JSONObject) jsonArray
											.get(i);
									if (jsObject != null) {
										LableData lableData = new LableData();
										long tagid = jsObject.getLong("tagId");
										long numid = jsObject.getLong("userId");
										String tagname = jsObject
												.getString("tagName");
										lableData.id = (int) tagid;
										lableData.num = 0;
										lableData.tag = tagname;
										lableDatas.add(lableData);
									}
								}
							}
						}
					}
				}
			}
			return lableDatas;
		case EAPIConsts.PeopleRequestType.PEOPLE_REQ_TAG_LIST:// 查询金桐推荐标签
			ArrayList<LableData> lableDatas2 = new ArrayList<LableData>();
			strKey = "success";
			if (!jsonObject.isNull(strKey)) {
				boolean boo = jsonObject.getBoolean(strKey);
				if (boo) {
					strKey = "page";
					if (!jsonObject.isNull(strKey)) {
						JSONObject json = jsonObject.optJSONObject(strKey);
						if (json != null) {
							strKey = "list";
							if (!json.isNull(strKey)) {
								JSONArray jsonArray = json.getJSONArray("list");
								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject jsObject = (JSONObject) jsonArray
											.get(i);
									if (jsObject != null) {
										LableData lableData = new LableData();
										long tagid = jsObject.getLong("id");
										long numid = jsObject.getLong("userId");
										String tagname = jsObject
												.getString("tag");
										lableData.id = (int) tagid;
										lableData.num = (int) numid;
										lableData.tag = tagname;
										lableDatas2.add(lableData);
									}
								}
							}
						}
					}
				}
			}
			return lableDatas2;
		case EAPIConsts.PeopleRequestType.PEOPLE_REQ_TAG_QUERY:// 查询用户的标签使用个数
			ArrayList<LableData> lableDatas4 = new ArrayList<LableData>();
			strKey = "success";
			if (!jsonObject.isNull(strKey)) {
				boolean boo = jsonObject.getBoolean(strKey);
				if (boo) {
					strKey = "list";
					if (!jsonObject.isNull(strKey)) {
						JSONArray jsonArray = jsonObject.getJSONArray("list");
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsObject = (JSONObject) jsonArray.get(i);
							if (jsObject != null) {
								LableData lableData = new LableData();
								long tagid = jsObject.getLong("tagId");
								long numid = jsObject.getLong("num");
								String tagname = jsObject.getString("tagName");
								lableData.id = (int) tagid;
								lableData.num = (int) numid;
								lableData.tag = tagname;
								lableDatas4.add(lableData);
							}
						}
					}
				}
			}
			return lableDatas4;
		case EAPIConsts.PeopleRequestType.PEOPLE_REQ_TAG_DELETE:
			// 删除标签接口
			obj = jsonObject.optBoolean("success");// 返回成功和失败的boolean
			break;
		case EAPIConsts.PeopleRequestType.PEOPLE_DELETE_TAG:// /删除标签关系
			obj = jsonObject.optBoolean("success");// 返回成功和失败的boolean
			break;
		case EAPIConsts.PeopleRequestType.PEOPLE_REQ_LIST_TAG://通过标签查询
			Map<String, Object> peopledataMap = new HashMap<String, Object>(); // 返回数据
			strKey = "list";

			if (jsonObject.has(strKey)) {

				jsonStr = jsonObject.getJSONArray(strKey).toString();
				// Log.e("TAG", "最新公告"+jsonStr.toString());
				List<Person> peopleList = gson.fromJson(jsonStr, new TypeToken<List<Person>>() {
				}.getType());
				if (peopleList != null) {
					peopledataMap.put("obj", peopleList);
				}
			}
			return peopledataMap;
		}
		return obj;
	}
}
