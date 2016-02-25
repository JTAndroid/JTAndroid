package com.tr.api;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.text.ParseException;
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
import com.tr.model.connections.MGetBlackList;
import com.tr.model.demand.LableData;
import com.tr.model.home.MGetDynamic;
import com.tr.model.knowledge.OrganizationCategory;
import com.tr.model.knowledge.UserCategory;
import com.tr.model.knowledge.UserTag;
import com.tr.model.obj.CommentApprover;
import com.tr.model.obj.DynamicComment;
import com.tr.ui.organization.model.CustomerProfileVo;
import com.tr.ui.organization.model.GetId;
import com.tr.ui.organization.model.NewsBean;
import com.tr.ui.organization.model.NoticeBean;
import com.tr.ui.organization.model.PushKnowledge;
import com.tr.ui.organization.model.RegisteOrgDetail;
import com.tr.ui.organization.model.RelatedInformation;
import com.tr.ui.organization.model.ResponseData;
import com.tr.ui.organization.model.cusandorg.CusAndOrg_Page;
import com.tr.ui.organization.model.evaluate.CustomerEvaluate;
import com.tr.ui.organization.model.finance.CustomerFinanceDetail;
import com.tr.ui.organization.model.firstpage.Page;
import com.tr.ui.organization.model.hight.CustomerHight;
import com.tr.ui.organization.model.hight.CustomerHightInfo;
import com.tr.ui.organization.model.industry.CustomerOrgIndustry;
import com.tr.ui.organization.model.meet.CustomerMeet_Re;
import com.tr.ui.organization.model.meet.CustomerMeetingDetail;
import com.tr.ui.organization.model.notice.CustomerNotice;
import com.tr.ui.organization.model.param.ClientDetailsParams;
import com.tr.ui.organization.model.param.CustomerClientParams;
import com.tr.ui.organization.model.param.CustomerOrganizatianParams;
import com.tr.ui.organization.model.peer.CustomerPeerInfo;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.organization.model.resource.CustomerResource;
import com.tr.ui.organization.model.stock.CustomerStock;
import com.tr.ui.organization.model.stock.CustomerStockInfo;
import com.tr.ui.organization.model.template.CustomerColumnVo;
import com.tr.ui.people.model.PeopleDetails;
import com.utils.common.QJsonParser;
import com.utils.common.QJsonParser.DataType;
import com.utils.http.EAPIConsts;
import com.utils.http.EAPIConsts.OrganizationReqType;

public class OrganizationRespFactory {

	private static final String TAG = OrganizationRespFactory.class.getSimpleName();
	private static Gson gson;
	// private static Page page;
	private static CustomerHight customerHight;

	public static Object createMsgObject(int msgId, JSONObject jsonObject) throws JSONException {

		// Log.v("TAG", "响应获取json数据");
		gson = new Gson();
		Object obj = null;
		if (jsonObject == null) {

			// Log.v("TAG", "jsonObject == null");
			return null;
		}

		Map<String, Object> dataMap = new HashMap<String, Object>(); // 返回数据

		String strKey = "";

		String jsonStr = "";

		switch (msgId) {

		case OrganizationReqType.ORGANIZATION_REQ_ORGANDPROINFO:// 组织详情

			// Log.v("TAG", "响应类获取到数据");
//
//			strKey = "customer";
//			if (jsonObject.has(strKey)) {
//				jsonStr = jsonObject.getJSONObject(strKey).toString();
//				CustomerProfileVo customer = gson.fromJson(jsonStr, CustomerProfileVo.class);
//
//				if (customer != null) {
//					dataMap.put(strKey, customer);
//				}
//
//			}
//
//			strKey = "noticeList";
//
//			if (jsonObject.has(strKey)) {
//
//				jsonStr = jsonObject.getJSONArray(strKey).toString();
//				// Log.e("TAG", "最新公告"+jsonStr.toString());
//				List<CustomerNotice> customerNoticeList = gson.fromJson(jsonStr, new TypeToken<List<CustomerNotice>>() {
//				}.getType());
//				if (customerNoticeList != null) {
//					dataMap.put(strKey, customerNoticeList);
//				}
//				// Log.v("TAG",
//				// "customerNoticeList--->"+customerNoticeList.toString());
//
//			}
//
//			strKey = "orgNewList";
//
//			if (jsonObject.has(strKey)) {
//
//				jsonStr = jsonObject.getJSONArray(strKey).toString();
//				// Log.e("TAG", "最新资讯"+jsonStr.toString());
//				List<PushKnowledge> pushKnowledgeNoticeList = gson.fromJson(jsonStr, new TypeToken<List<PushKnowledge>>() {
//				}.getType());
//
//				if (pushKnowledgeNoticeList != null) {
//					dataMap.put(strKey, pushKnowledgeNoticeList);
//				}
//
//				// Log.v("TAG",
//				// "pushKnowledgeNoticeList--->"+pushKnowledgeNoticeList.toString());
//			}
//
//			strKey = "result";
//
//			if (jsonObject.has(strKey)) {
//
//				jsonStr = jsonObject.getJSONObject(strKey).toString();
//
//				CustomerResource resource = gson.fromJson(jsonStr, CustomerResource.class);
//
//				if (resource != null) {
//
//					for (int i = 0; i < resource.investmentdemandList.size(); i++) {
//
//						// Log.v("TAG", "investmentdemandList====="
//						// + resource.investmentdemandList.get(i)
//						// .getTypeNames());
//
//					}
//
//					dataMap.put(strKey, resource);
//
//				}
//
//			}
//
//			strKey = "relevance";
//
//			if (jsonObject.has(strKey)) {
//
//				jsonStr = jsonObject.getJSONObject(strKey).toString();
//
//				RelatedInformation relatedInformation = gson.fromJson(jsonStr, RelatedInformation.class);
//
//				if (relatedInformation != null) {
//
//					// Log.v("TAG", "----->relatedInformation----->"
//					// + relatedInformation.toString());
//
//					dataMap.put(strKey, relatedInformation);
//
//				}
//
//			}
			obj = gson.fromJson(jsonObject.toString(), CustomerOrganizatianParams.class);
			Log.e("TAG", "客户详情响应类---->" + obj.toString());
			dataMap.put("customer_organization_params", obj);
			break;

		// strKey = "gczlList";
		// Log.v("TAG", "响应类1234567");
		//
		// jsonStr = jsonObject.getJSONArray(strKey).toString();
		//
		//
		// dataMap.put(strKey, CustomerHight); }
		//
		// }
		// break;
		case OrganizationReqType.ORGANIZATION_REQ_DETAILS:// 4.11财务分析

			// Log.v("TAG", "响应类1234567");

			strKey = "listRow";

			if (jsonObject.has(strKey)) {
				jsonStr = jsonObject.getJSONObject(strKey).toString();

				CustomerFinanceDetail customerFinanceDetail = gson.fromJson(jsonStr, CustomerFinanceDetail.class);

				if (customerFinanceDetail != null) {
					dataMap.put(strKey, customerFinanceDetail);
					// Log.v("TAG", "响应类" + customerFinanceDetail.toString());
				}
			}
			break;

		case OrganizationReqType.ORGANIZATION_REQ_FINDSTOCKONE: // 4.14查询股东研究

			strKey = "stock";

			if (jsonObject.has(strKey)) {
				jsonStr = jsonObject.getJSONObject(strKey).toString();

				CustomerStock customerStock = gson.fromJson(jsonStr, CustomerStock.class);

				if (customerStock != null) {
					dataMap.put(strKey, customerStock);
				}

			}

			strKey = "tenStockList";

			if (jsonObject.has(strKey)) {

				jsonStr = jsonObject.getJSONArray(strKey).toString();

				List<CustomerStockInfo> customerTenStockList = gson.fromJson(

				jsonStr, new TypeToken<List<CustomerStockInfo>>() {
				}.getType());

				if (customerTenStockList != null) {
					dataMap.put(strKey, customerTenStockList);
				}

				// Log.v("TAG",
				// "customerTenStockList--->"+customerTenStockList.toString());
			}

			strKey = "ltStockList";

			if (jsonObject.has(strKey)) {

				jsonStr = jsonObject.getJSONArray(strKey).toString();

				List<CustomerStockInfo> customerLtStockList = gson.fromJson(jsonStr, new TypeToken<List<CustomerStockInfo>>() {
				}.getType());

				if (customerLtStockList != null) {
					dataMap.put(strKey, customerLtStockList);
				}

				// Log.v("TAG",
				// "pushKnowledgeNoticeList--->"+pushKnowledgeNoticeList.toString());
			}

			break;

		case OrganizationReqType.ORGANIZATION_REQ_FINDHEGHTONE: // 4.12查询高层治理

			strKey = "dshList";

			if (jsonObject.has(strKey)) {
				jsonStr = jsonObject.getJSONArray(strKey).toString();

				List<CustomerHightInfo> dshList = gson.fromJson(jsonStr, new TypeToken<List<CustomerHightInfo>>() {
				}.getType());

				if (dshList != null) {
					dataMap.put(strKey, dshList);
					// Log.v("TAG", "响应类" + dshList.toString());
				}

			}
			strKey = "jshList";

			if (jsonObject.has(strKey)) {
				jsonStr = jsonObject.getJSONArray(strKey).toString();

				List<CustomerHightInfo> dshList = gson.fromJson(jsonStr, new TypeToken<List<CustomerHightInfo>>() {
				}.getType());

				if (dshList != null) {
					dataMap.put(strKey, dshList);
					// Log.v("TAG", "响应类" + dshList.toString());
				}

			}
			strKey = "ggList";

			if (jsonObject.has(strKey)) {
				jsonStr = jsonObject.getJSONArray(strKey).toString();

				List<CustomerHightInfo> dshList = gson.fromJson(jsonStr, new TypeToken<List<CustomerHightInfo>>() {
				}.getType());

				if (dshList != null) {
					dataMap.put(strKey, dshList);
					// Log.v("TAG", "响应类" + dshList.toString());
				}

			}
			strKey = "ggjzList";

			if (jsonObject.has(strKey)) {
				jsonStr = jsonObject.getJSONArray(strKey).toString();

				List<CustomerHightInfo> dshList = gson.fromJson(jsonStr, new TypeToken<List<CustomerHightInfo>>() {
				}.getType());

				if (dshList != null) {
					dataMap.put(strKey, dshList);
					// Log.v("TAG", "响应类" + dshList.toString());
				}
			}

			break;
		case EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_GETDISCOVERLIST:// 组织首页请求
			strKey = "page";
			if (!jsonObject.isNull(strKey)) {
				jsonStr = jsonObject.getJSONObject(strKey).toString();
				Page page = gson.fromJson(jsonStr, Page.class);
				System.out.println("page.listResults.size()===" + page.listResults.size());
				System.out.println("page.total==" + page.total);
				if (page != null) {
					dataMap.put(strKey, page);
				}
			}
			break;

		case EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_ORG_SAVECUSPROFILE:// 添加组织详情
			strKey = "responseData";
			String name = (String) jsonObject.get("msg");
			dataMap.put(strKey, name);
			break;

		case EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_GETVCODEFORPASSWORD: // 获取验证码

			strKey = "responseData";
			Log.v("TAG", "验证码");
			if (!jsonObject.isNull(strKey)) {

				jsonStr = jsonObject.getJSONObject(strKey).toString();

				ResponseData data = gson.fromJson(jsonStr, ResponseData.class);
				Log.v("TAG", "验证码" + data.toString());
				if (data != null) {

					dataMap.put(strKey, data);

				}

			}

			break;
		case EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_REGISTRATION: // 组织注册

			strKey = "responseData";
			Log.v("TAG", "组织注册");
			if (!jsonObject.isNull(strKey)) {

				jsonStr = jsonObject.getJSONObject(strKey).toString();

				ResponseData data = gson.fromJson(jsonStr, ResponseData.class);
				Log.v("TAG", "组织注册" + data.toString());
				if (data != null) {

					dataMap.put(strKey, data);

				}
			}
			break;
		case EAPIConsts.OrganizationReqType.SAVE_ORGANIZATION: // 完善组织信息
			
			strKey = "success";
			if (jsonObject.has(strKey)) {
				boolean success = jsonObject.getBoolean(strKey);
				dataMap.put(strKey, success);
			}
			break;

		// 组织/客户列表5.2 获得组织/客户列表 /org/getOrgAndCustomer.json
		case EAPIConsts.OrganizationReqType.ORAGANIZATION_REQ_GETCUSTOMANDORG:

			strKey = "page";
			System.out.println("jsonObject.toString==" + jsonObject.toString());
			if (!jsonObject.isNull(strKey)) {
				jsonStr = jsonObject.getJSONObject(strKey).toString();
				System.out.println("组织/客户列表jsonStr===" + jsonStr);
				CusAndOrg_Page cusandorg_page = gson.fromJson(jsonStr, CusAndOrg_Page.class);

				if (cusandorg_page != null) {
					dataMap.put(strKey, cusandorg_page);
				}
			}

			break;

		case OrganizationReqType.ORGANIZATION_CUSTOMER_COLUMNLIST: // 6.3查询模板下的栏目
			System.out.println("Customer column list------>" + jsonObject.toString());
			CustomerColumnVo column = gson.fromJson(jsonObject.toString(), CustomerColumnVo.class);
			dataMap.put("customerColumn", column);
			break;
		case OrganizationReqType.ORGANIZATION_CUSTOMER_SAVERELATION: // 6.4
																		// 保存所选模块和栏目
			System.out.println("Customer save Relation------->" + jsonObject.toString());
			break;
		case OrganizationReqType.ACCESS_TO_THE_PRIMARY_KEY:// 4.30 得到客户主键Id
															// /customer/getId.json
			// strKey = "responseData";
			// Log.v("TAG", "得到客户主键Id");
			// if (!jsonObject.isNull(strKey)) {
			//
			// jsonStr = jsonObject.getJSONObject(strKey).toString();
			//
			// GetId getId = gson.fromJson(jsonStr,
			// GetId.class);
			// Log.v("TAG", "得到客户主键Id" + getId.toString());
			// if (getId != null) {
			//
			// dataMap.put(strKey, getId);
			//
			// }
			// }
			System.out.println("GetId GetId GetId------>" + jsonObject.toString());
			GetId getId = gson.fromJson(jsonObject.toString(), GetId.class);
			dataMap.put("getId", getId);
			break;

		case OrganizationReqType.ORGANIZATION_REQ_FINDPER:// 4.19 查询同业竞争列表

			strKey = "peerList";
			if (jsonObject.has(strKey)) {
				jsonStr = jsonObject.getJSONArray(strKey).toString();
				// Log.v("TAG", "同业竞争 json string -------------- " + jsonStr);

				List<List<CustomerPeerInfo>> peerList = gson.fromJson(jsonStr, new TypeToken<List<List<CustomerPeerInfo>>>() {
				}.getType());

				if (peerList != null) {
					dataMap.put(strKey, peerList);
					// Log.v("TAG", "同业竞争详情------->" + peerList.toString());
				}
			}

			// dataMap.put("peerList", peerList);
			break;
		case OrganizationReqType.ORGANIZATION_CUSTOMER_DETILS:// 6.1查看客户详情

//			strKey = "customer";
//			if (jsonObject.has(strKey)) {
//				jsonStr = jsonObject.getJSONObject(strKey).toString();
//				Log.i("LOG", "mClientDetailsParams后台返回" + jsonStr.toString());
//				CustomerClientParams mClientDetailsParams = gson.fromJson(jsonStr, CustomerClientParams.class);
//
//				if (mClientDetailsParams != null) {
//					dataMap.put("customer", mClientDetailsParams);
//					Log.v("LOG", "客户详情响应类---->" + mClientDetailsParams.toString());
//				}
//
//			}
//
//			strKey = "noticeList";
//
//			if (jsonObject.has(strKey)) {
//
//				jsonStr = jsonObject.getJSONArray(strKey).toString();
//				Log.e("TAG", "客户详情最新公告" + jsonStr.toString());
//				List<NoticeBean> clientrNoticeList = gson.fromJson(jsonStr, new TypeToken<List<CustomerNotice>>() {
//				}.getType());
//				if (clientrNoticeList != null) {
//					dataMap.put(strKey, clientrNoticeList);
//				}
//				// Log.v("TAG",
//				// "customerNoticeList--->"+customerNoticeList.toString());
//
//			}
//
//			strKey = "orgNewList";
//
//			if (jsonObject.has(strKey)) {
//
//				jsonStr = jsonObject.getJSONArray(strKey).toString();
//				Log.e("TAG", "客户详情最新资讯" + jsonStr.toString());
//				List<NewsBean> clientpushKnowledgeNoticeList = gson.fromJson(jsonStr, new TypeToken<List<NewsBean>>() {
//				}.getType());
//
//				if (clientpushKnowledgeNoticeList != null) {
//					dataMap.put(strKey, clientpushKnowledgeNoticeList);
//				}
//
//				Log.v("TAG", "pushKnowledgeNoticeList--->" + clientpushKnowledgeNoticeList.toString());
//			}
//
//			strKey = "result";
//
//			if (jsonObject.has(strKey)) {
//
//				jsonStr = jsonObject.getJSONObject(strKey).toString();
//
//				CustomerResource resource = gson.fromJson(jsonStr, CustomerResource.class);
//
//				if (resource != null) {
//
//					dataMap.put(strKey, resource);
//
//				}
//
//			}
//
//			strKey = "relevance";
//
//			if (jsonObject.has(strKey)) {
//
//				jsonStr = jsonObject.getJSONObject(strKey).toString();
//				Log.e("TAG", "四大组件" + jsonStr.toString());
//
//				RelatedInformation relatedInformation = gson.fromJson(jsonStr, RelatedInformation.class);
//				Log.e("TAG", "解析四大组件" + relatedInformation.toString());
//				if (relatedInformation != null) {
//
//					dataMap.put("relevance", relatedInformation);
//
//				}
//
//			}
			obj = gson.fromJson(jsonObject.toString(), CustomerClientParams.class);
			Log.e("TAG", "客户详情响应类---->" + obj.toString());
			dataMap.put("customerclientparams", obj);
			break;

		case OrganizationReqType.ORAGANIZATION_REQ_ADD_FRIENDS:// 加好友

			Log.v("TAG", "加好友");

			if (jsonObject != null) {

				boolean flag = jsonObject.optBoolean("succeed");

				dataMap.put("succeed", flag);

				Log.v("TAG", "加好友成功---->" + flag);

			}

			break;
		case EAPIConsts.OrganizationReqType.GET_ORG_HOME_Evaluate: {// 获取评价
			if (jsonObject == null) {
				return null;
			}
			ArrayList arr = new ArrayList();
			boolean isEvaluated = jsonObject.optBoolean("isEvaluated");// 非好友是否可以评价
			arr.add(isEvaluated);
			ArrayList<CustomerEvaluate> userCommentList = new ArrayList<CustomerEvaluate>();
			if (jsonObject.has("listUserComment") && !jsonObject.isNull("listUserComment")) {
				JSONArray userCommentJsArr = jsonObject.getJSONArray("listUserComment");
				CustomerEvaluate customerEvaluate;
				for (int i = 0; i < userCommentJsArr.length(); i++) {
					customerEvaluate = new CustomerEvaluate();
					try {
						customerEvaluate.initWithJson(userCommentJsArr.getJSONObject(i));
					} catch (MalformedURLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					userCommentList.add(customerEvaluate);
				}
				// return userCommentList;
			}
			arr.add(userCommentList);
			return arr;
		}

		case EAPIConsts.OrganizationReqType.FEEDBACK_EVALUATE: {// 赞同/取消赞同评价
			if (jsonObject == null) {
				return null;
			}
			boolean flag = jsonObject.optBoolean("success");

			dataMap.put("FLAG", flag);

			break;
		}

		case EAPIConsts.OrganizationReqType.FIND_MORE_EVALUATE: { // 获取更多评价
			if (jsonObject == null) {
				return null;
			}
			ArrayList<CommentApprover> customerApproverList = new ArrayList<CommentApprover>();
			if (jsonObject.has("listCommentApprover") && !jsonObject.isNull("listCommentApprover")) {
				JSONArray commentApproverJsArr = jsonObject.getJSONArray("listCommentApprover");
				CommentApprover commentApprover;
				for (int i = 0; i < commentApproverJsArr.length(); i++) {
					commentApprover = new CommentApprover();
					try {
						commentApprover.initWithJson(commentApproverJsArr.getJSONObject(i));
						customerApproverList.add(commentApprover);

						// Log.v("TAG", "customerApproverList======"
						// + customerApproverList);

						dataMap.put("customerApproverList", customerApproverList);
					} catch (MalformedURLException e) {
						e.printStackTrace();
					} catch (ParseException e) {
						e.printStackTrace();
					}

				}

				break;

			}
		}

		case EAPIConsts.OrganizationReqType.ADD_ORG_HOME_Evaluate: { // 添加评价
			if (jsonObject == null) {

				// Log.v("TAG", "空空空空");

				return null;
			}
			strKey = "id";
			boolean flag = jsonObject.optBoolean("success");
			// Log.v("TAG", "flag---->" + flag);

			dataMap.put("success", flag);

			if (jsonObject.has(strKey) && !jsonObject.isNull(strKey)) {
				Long id = jsonObject.getLong(strKey);
				dataMap.put("ID", id);
				// Log.v("TAG", "id---->" + id + "");
			}

			break;

		}

		case EAPIConsts.OrganizationReqType.DELETE_EVALUATE: {// 删除评价
			if (jsonObject == null) {
				return null;
			}
			boolean isFlag = jsonObject.optBoolean("success");

			dataMap.put("success", isFlag);

			break;
		}

		case EAPIConsts.OrganizationReqType.COLUMN_SAVE: {// 新增和修改自定义栏目
															// /customer/column/save.json
			if (jsonObject == null) {
				return null;
			}
			Long v = jsonObject.optLong("columnId");
			System.out.println(v + "vvvvvvvvvvvvvvvvvvvvvvvvvvv");
			return v;

		}
		case EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_ADD_MAIN_DEPARTMENT: {// 4.26
																					// 添加主要职能部门
																					// /customer/departMents/save.json¶
			if (jsonObject == null) {
				return null;
			}
			boolean v = jsonObject.optBoolean("success");
			System.out.println(v + "success-success-success");
			return v;
		}
		case EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_ADD_GOVERNMENTAREASURVEY: {// 4.25
																						// 添加政府地区概况
																						// /customer/areaInfo/save.json
			if (jsonObject == null) {
				return null;
			}
			boolean v = jsonObject.optBoolean("success");
			System.out.println(v + "success-success-success");
			return v;
		}

		case EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_DYNAMICNEWSLIST: {// 2.1
																				// 获取动态列表
			Log.v("ADD", "动态返回的数据" + jsonObject.toString());
			return MGetDynamic.createFactory(jsonObject);
		}

		case EAPIConsts.OrganizationReqType.ORG_COMMENT: {// 发表评论(用人脉的接口)
			return DynamicComment.createFactoryWithName(jsonObject);
		}
		case EAPIConsts.OrganizationReqType.MEET_SAVE: // 保存会面情况
			boolean v = jsonObject.optBoolean("success");
			long id = jsonObject.optLong("id");
			System.out.println(v + "success-success-success");
			return id;

		case EAPIConsts.OrganizationReqType.MEET_FINDLIST: // 7.3 查询会面情况列表
															// /customer/meet/findList.json

			strKey = "responseData";
			Log.v("TAG", " 查询会面情况列表");
			if (!jsonObject.isNull(strKey)) {

				jsonStr = jsonObject.getJSONObject(strKey).toString();

				CustomerMeet_Re data = gson.fromJson(jsonStr, CustomerMeet_Re.class);
				Log.v("TAG", " 查询会面情况列表" + data.toString());
				if (data != null) {

					dataMap.put(strKey, data);

				}
			}
			strKey = "meetList";
			Log.v("TAG", " 查询会面情况列表");
			if (!jsonObject.isNull(strKey)) {

				jsonStr = jsonObject.getJSONArray(strKey).toString();

				ArrayList<CustomerMeetingDetail> detail = gson.fromJson(jsonStr, new TypeToken<List<CustomerMeetingDetail>>() {
				}.getType());
				Log.v("TAG", " 查询会面情况列表" + detail.toString());
				if (detail != null) {

					dataMap.put(strKey, detail);

				}
			}

			strKey = "meetid";
			Log.v("TAG", " 查询会面情况列表");
			if (!jsonObject.isNull(strKey)) {

				long meetid = jsonObject.optLong("meetid");

				Log.v("TAG", " 查询会面情况列表Id" + meetid);
				if (meetid != 0) {

					dataMap.put(strKey, meetid);

				}
			}
			break;
		case OrganizationReqType.MEET_FINDONE:
			strKey = "result";
			if (!jsonObject.isNull(strKey)) {

				jsonStr = jsonObject.getJSONObject(strKey).toString();

				CustomerMeetingDetail data = gson.fromJson(jsonStr, CustomerMeetingDetail.class);
				dataMap.put(strKey, data);
			}
			break;
		case OrganizationReqType.MEET_DELETEBYID:
			strKey = "success";
			if (!jsonObject.isNull(strKey)) {
				boolean v1 = jsonObject.optBoolean("success");
				System.out.println(v1 + "success-success-success");
				return v1;
			}
			break;

		case OrganizationReqType.ORGANIZATION_REQ_FINDINDUSTRY:// 4.16 查询行业动态

			strKey = "peerList";

			if (jsonObject.has(strKey)) {

				jsonStr = jsonObject.getJSONArray(strKey).toString();
				ArrayList<CustomerOrgIndustry> customerOrgIndustryList = gson.fromJson(jsonStr, new TypeToken<ArrayList<CustomerOrgIndustry>>() {
				}.getType());

				if (customerOrgIndustryList != null) {
					dataMap.put(strKey, customerOrgIndustryList);
				}

				// Log.v("TAG",
				// "查询行业动态--->customerOrgIndustryList"+customerOrgIndustryList.toString());

			}

			break;

		case OrganizationReqType.ORGANIZATION_REQ_FINDREPORT:// 4.22 查询研究报告

			strKey = "personalLineList";

			if (jsonObject.has(strKey)) {

				jsonStr = jsonObject.getJSONArray(strKey).toString();
				ArrayList<CustomerPersonalLine> customerPersonalLineList = gson.fromJson(jsonStr, new TypeToken<ArrayList<CustomerPersonalLine>>() {
				}.getType());

				if (customerPersonalLineList != null) {
					dataMap.put(strKey, customerPersonalLineList);
				}

				Log.v("TAG", "查询研究报告--->" + customerPersonalLineList.toString());

			}

			break;

		case EAPIConsts.OrganizationReqType.EDITBLACKLIST:// 编辑黑名单
			if (jsonObject == null) {
				return null;
			}

			String success = jsonObject.optString("succeed");

			return success;

		case EAPIConsts.OrganizationReqType.BLACKLIST:// 获取黑名单列表
			if (jsonObject == null) {
				return null;
			}
			return MGetBlackList.createFactory(jsonObject);

		case EAPIConsts.OrganizationReqType.ORG_DELETEFRIED:// 解除好友关系
			if (jsonObject == null) {
				return null;
			}
			boolean succeed = jsonObject.optBoolean("succeed");

			dataMap.put("success", succeed);
			break;

		case EAPIConsts.OrganizationReqType.ORAGANIZATION_REQ_DELCUSANDORG:// 5.3删除组织.客户
			if (jsonObject == null) {
				return null;
			}
			boolean result = jsonObject.optBoolean("success");
			Log.e("MSG", "删除是否成功："+result);
			dataMap.put("success", result);
			break;
			
			
		case OrganizationReqType.ORGANIZATION_REQ_CREATE_ORG:// 1.1上传图片

			strKey = "responseData";
			if (jsonObject.has(strKey)) {
				jsonStr = jsonObject.getJSONObject(strKey).toString();
				Log.i("LOG", "responseData头像后台返回" + jsonStr.toString());
//				ClientDetailsParams mClientDetailsParams = gson.fromJson(jsonStr, ClientDetailsParams.class);
//
//				if (mClientDetailsParams != null) {
//					dataMap.put("customer", mClientDetailsParams);
//					Log.v("LOG", "客户详情响应类---->" + mClientDetailsParams.toString());
//				}

			}
			break;
			

		case EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_CUSTOMER_LIST_QUERY: // 客户分组列表查询

			QJsonParser.parse("success", DataType.BOOLEAN, dataMap, jsonObject);

			strKey = "results";
			if (!jsonObject.isNull(strKey)) {
				jsonStr = jsonObject.getJSONArray(strKey).toString();
				ArrayList<OrganizationCategory> organizationList = gson.fromJson(jsonStr, new TypeToken<ArrayList<OrganizationCategory>>() {
				}.getType());
				// 转成通用知识目录结构

				if (organizationList != null & organizationList.size() > 0) {

					ArrayList<UserCategory> knowledgeList = new ArrayList<UserCategory>();

					for (OrganizationCategory organizationCategory : organizationList) {
						knowledgeList.add(organizationCategory.ToKnowledgeCategory());
					}

					if (knowledgeList != null) {
						dataMap.put("listUserCategory", knowledgeList);
					}

				}
			}
			
			break;

		case EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_ADD_GROUPING: // 组织 添加 目录
		case EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_UPDAET_GROUPING: // 组织 更新 目录
		case EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_DELETE_GROUPING: // 删除客户分组目录

			QJsonParser.parse("success", DataType.BOOLEAN, dataMap, jsonObject);

			strKey = "categorys";
			if (!jsonObject.isNull(strKey)) {
				jsonStr = jsonObject.getJSONArray(strKey).toString();
				ArrayList<OrganizationCategory> organizationList = gson.fromJson(jsonStr, new TypeToken<ArrayList<OrganizationCategory>>() {
				}.getType());
				// 转成通用知识目录结构

				if (organizationList != null & organizationList.size() > 0) {

					ArrayList<UserCategory> knowledgeList = new ArrayList<UserCategory>();

					for (OrganizationCategory organizationCategory : organizationList) {
						knowledgeList.add(organizationCategory.ToKnowledgeCategory());
					}

					if (knowledgeList != null) {
						dataMap.put("listUserCategory", knowledgeList);
					}

				}
			}
			
			break;
			
		case EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_TAG_LIST_GINTONG://金桐脑推荐标签
			obj = LableData.createFactory(jsonObject);
			return obj;
		case EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_TAG_QUERY:// 查询标签使用个数:数据结构不匹配，单独解析
			ArrayList<LableData> lableDatas = new ArrayList<LableData>();
			strKey = "success";
			if (!jsonObject.isNull(strKey)) {
				boolean boo = jsonObject.getBoolean(strKey);
				if (boo) {
					strKey = "page";
					if (!jsonObject.isNull(strKey)) {
						JSONObject json = jsonObject.optJSONObject(strKey);
						if (json!=null) {
							strKey = "list";
							if (!json.isNull(strKey)) {
								JSONArray jsonArray = json.getJSONArray("list");
								for (int i = 0; i < jsonArray.length(); i++) {
									JSONObject jsObject = (JSONObject) jsonArray.get(i);
									if (jsObject!=null) {
										LableData lableData = new LableData();
										long tagid = jsObject.getLong("id");
										long numid = jsObject.getLong("num");
										String tagname = jsObject.getString("name");
										lableData.id = (int) tagid;
										lableData.num = (int) numid;
										lableData.tag = tagname;
										lableDatas.add(lableData);
									}
								}
							}
						}
					}
				}
			}
//			obj = LableData.createFactory(jsonObject);//返回list
			return lableDatas;
		case EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_DELETE_TAG:// 删除标签接口
			return jsonObject.optBoolean("success");// 返回成功和失败的boolean
		case EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_SAVE_TAG:// 添加标签
			if (jsonObject == null) {
				return null;
			}
			strKey = "success";
			if (!jsonObject.isNull(strKey)) {
				boolean flag = jsonObject.optBoolean(strKey);
				dataMap.put(strKey, flag);
			}
			strKey = "obj";
			if (!jsonObject.isNull(strKey)) {
				jsonStr = jsonObject.getJSONObject(strKey).toString();
				UserTag userTag = gson.fromJson(jsonStr, UserTag.class);
				if (userTag != null) {
					//将返回的usertag转为labledata返回
					LableData lableData = new LableData();
					lableData.id = userTag.getId();
					lableData.num = 0;
					lableData.tag = userTag.getName();
					dataMap.put(strKey, lableData);
				}
			}
			break;   												
		// 获取注册的组织详情
		case EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_REGIST_ORG_DETAIL:
			return RegisteOrgDetail.createFactory(jsonObject);
			// 获取注册的客户详情
		case EAPIConsts.OrganizationReqType.ORGANIZATION_REQ_CUSTOMER_SAVECUSPROFILE:
					return jsonObject.optBoolean("success");
		case EAPIConsts.OrganizationReqType.CUSTOMER_INFORM_SAVE://举报
			return jsonObject.optBoolean("success");
		case EAPIConsts.OrganizationReqType.CUSTOMER_COLLECT_PERATE://收藏
			return jsonObject.optBoolean("success");
		}
	
		return dataMap;
	}
}
