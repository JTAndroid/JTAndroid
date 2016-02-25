package com.tr.api;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tr.model.SimpleResult;
import com.tr.model.api.DataBox;
import com.tr.model.fileManagement.CommonUserCategory;
import com.tr.model.home.MCheckFriend;
import com.tr.model.home.MHomePageNumInfos;
import com.tr.model.home.MIndustrys;
import com.tr.model.home.MListCountry;
import com.tr.model.home.MPageIndustrys;
import com.tr.model.home.MUserQRUrl;
import com.tr.model.joint.AffairNode;
import com.tr.model.joint.ConnectionNode;
import com.tr.model.joint.JointResource;
import com.tr.model.joint.KnowledgeNode;
import com.tr.model.knowledge.Knowledge2;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.obj.AffairsMini;
import com.tr.model.obj.Connections;
import com.tr.model.obj.MobilePhone;
import com.tr.model.user.JTMember;
import com.tr.ui.cloudDisk.model.FileCategoryManager;
import com.tr.ui.cloudDisk.model.FileManagerResponseData;
import com.tr.ui.organization.model.PushKnowledge;
import com.tr.ui.people.model.PeopleDetails;
import com.utils.http.EAPIConsts;

public class CommonRespFactory {

	public static Object createMsgObject(int msgId, JSONObject jsonObject) throws MalformedURLException, JSONException, ParseException {

		if (jsonObject == null) {
			return null;
		}

		HashMap<String, Object> dataBox = new HashMap<String, Object>();
		String strKey = "";

		switch (msgId) {
		case EAPIConsts.CommonReqType.GetInterestIndustry:
			// TODO
			return MPageIndustrys.createFactory(jsonObject);
		case EAPIConsts.CommonReqType.GetRelatedResource: // 获取关联资源

			// strKey = "type";
			// int resType = jsonObject.optInt(strKey);
			// dataBox.put(strKey, resType);
			// switch(resType){
			// case 2: // 人
			strKey = "listPlatformPeople";
			if (!jsonObject.isNull(strKey)) {
				List<Connections> listPlatformPeople = new Gson().fromJson(jsonObject.getJSONArray(strKey).toString(), new TypeToken<List<Connections>>() {
				}.getType());
				if (listPlatformPeople != null) {
					for (Connections connections : listPlatformPeople) {
						connections.doCompatible(); // 兼容性处理
					}
					dataBox.put(strKey, listPlatformPeople);
				}
			}
			strKey = "listUserPeople";
			if (!jsonObject.isNull(strKey)) {
				List<Connections> listUserPeople = new Gson().fromJson(jsonObject.getJSONArray(strKey).toString(), new TypeToken<List<Connections>>() {
				}.getType());
				if (listUserPeople != null) {
					for (Connections connections : listUserPeople) {
						connections.doCompatible();
					}
					dataBox.put(strKey, listUserPeople);
				}
			}
			// break;
			// case 3: // 组织
			strKey = "listPlatformOrganization";
			if (!jsonObject.isNull(strKey)) {
				List<Connections> listPlatformOrg = new Gson().fromJson(jsonObject.getJSONArray(strKey).toString(), new TypeToken<List<Connections>>() {
				}.getType());
				if (listPlatformOrg != null) {
					for (Connections connections : listPlatformOrg) {
						connections.doCompatible();
					}
					dataBox.put(strKey, listPlatformOrg);
				}
			}
			strKey = "listUserOrganization";
			if (!jsonObject.isNull(strKey)) {
				List<Connections> listUserOrg = new Gson().fromJson(jsonObject.getJSONArray(strKey).toString(), new TypeToken<List<Connections>>() {
				}.getType());
				if (listUserOrg != null) {
					for (Connections connections : listUserOrg) {
						connections.doCompatible();
					}
					dataBox.put(strKey, listUserOrg);
				}
			}
			// break;
			// case 4: // 知识
			strKey = "listPlatformKnowledge";
			if (!jsonObject.isNull(strKey)) {
				List<KnowledgeMini2> listPlatformKno = new Gson().fromJson(jsonObject.getJSONArray(strKey).toString(), new TypeToken<List<KnowledgeMini2>>() {
				}.getType());
				if (listPlatformKno != null) {
					dataBox.put(strKey, listPlatformKno);
				}
			}
			strKey = "listUserKnowledge";
			if (!jsonObject.isNull(strKey)) {
				List<KnowledgeMini2> listUserKno = new Gson().fromJson(jsonObject.getJSONArray(strKey).toString(), new TypeToken<List<KnowledgeMini2>>() {
				}.getType());
				if (listUserKno != null) {
					dataBox.put(strKey, listUserKno);
				}
			}
			// break;
			// case 1: // 事件
			strKey = "listPlatformAffair";
			if (!jsonObject.isNull(strKey)) {
				List<AffairsMini> listPlatformAffair = new Gson().fromJson(jsonObject.getJSONArray(strKey).toString(), new TypeToken<List<AffairsMini>>() {
				}.getType());
				if (listPlatformAffair != null) {
					dataBox.put(strKey, listPlatformAffair);
				}
			}
			strKey = "listUserAffair";
			if (!jsonObject.isNull(strKey)) {
				List<AffairsMini> listUserAffair = new Gson().fromJson(jsonObject.getJSONArray(strKey).toString(), new TypeToken<List<AffairsMini>>() {
				}.getType());
				if (listUserAffair != null) {
					dataBox.put(strKey, listUserAffair);
				}
			}
			// break;
			// }
			return dataBox;
		case EAPIConsts.CommonReqType.GetJointResource: // 获取生态对接资源

			strKey = "range";
			int range = jsonObject.optInt(strKey);
			dataBox.put(strKey, range);

			strKey = "listJointAffairNode";
			if (!jsonObject.isNull(strKey)) {
				List<AffairNode> listAffairNode = new Gson().fromJson(jsonObject.getJSONArray(strKey).toString(), new TypeToken<List<AffairNode>>() {
				}.getType());

				// 对事物节点listJointAffairNode下的mobilephone进行解析
				JSONArray listJointAffairNodeJsonArr = jsonObject.getJSONArray("listJointAffairNode");
				for (int i = 0; i < listJointAffairNodeJsonArr.length(); i++) {
					JSONObject jointAffairNodeJsonObj = (JSONObject) listJointAffairNodeJsonArr.get(i);
					JSONArray listAffairMiniJsonArr = jointAffairNodeJsonObj.getJSONArray("listAffairMini");
					if (listAffairMiniJsonArr.length() > 0 && listAffairMiniJsonArr != null) {
						for (int j = 0; j < listAffairMiniJsonArr.length(); j++) {
							JSONObject affairMiniJsonObj = (JSONObject) listAffairMiniJsonArr.get(j);
							JSONObject connectionsJsonObj = affairMiniJsonObj.getJSONObject("connections");
							JSONObject jtContactMiniJsonObj = null;
							if (!connectionsJsonObj.isNull("jtContactMini")) {
								jtContactMiniJsonObj = connectionsJsonObj.getJSONObject("jtContactMini");
								// jiexi id
								if (!connectionsJsonObj.isNull("id")) {
									String id = connectionsJsonObj.getString("id");
									if (listAffairNode.size() > i) {
										listAffairNode.get(i).getListAffairMini().get(j).connections.setID(id);
									}
								}
							}
							JSONObject organizationMiniJsonObj = null;
							if (!connectionsJsonObj.isNull("organizationMini")) {
								organizationMiniJsonObj = connectionsJsonObj.getJSONObject("organizationMini");
								if (!connectionsJsonObj.isNull("id")) {
									String id = connectionsJsonObj.getString("id");
									if (listAffairNode.size() > i) {
										listAffairNode.get(i).getListAffairMini().get(j).connections.setID(id);
									}
								}
							}
							if (jtContactMiniJsonObj != null) {
								JSONArray listMobilePhoneJsonArr = jtContactMiniJsonObj.getJSONArray("listMobilePhone");
								if (listMobilePhoneJsonArr != null && listMobilePhoneJsonArr.length() > 0) {
									ArrayList<MobilePhone> listMobilePhones = new ArrayList<MobilePhone>();
									for (int k = 0; k < listMobilePhoneJsonArr.length(); k++) {
										JSONObject mobilePhoneJsonObj = (JSONObject) listMobilePhoneJsonArr.get(k);
										MobilePhone mobilePhone = new MobilePhone();
										mobilePhone.name = mobilePhoneJsonObj.getString("name");
										mobilePhone.mobile = mobilePhoneJsonObj.getString("mobile");
										listMobilePhones.add(mobilePhone);
									}
									listAffairNode.get(i).getListAffairMini().get(j).connections.jtContactMini.listMobilePhone = listMobilePhones;
								}
							}
							else if (organizationMiniJsonObj != null) {
								JSONArray listMobilePhoneJsonArr = organizationMiniJsonObj.getJSONArray("listMobilePhone");
								if (listMobilePhoneJsonArr != null && listMobilePhoneJsonArr.length() > 0) {
									ArrayList<MobilePhone> listMobilePhones = new ArrayList<MobilePhone>();
									for (int k = 0; k < listMobilePhoneJsonArr.length(); k++) {
										JSONObject mobilePhoneJsonObj = (JSONObject) listMobilePhoneJsonArr.get(k);
										MobilePhone mobilePhone = new MobilePhone();
										mobilePhone.name = mobilePhoneJsonObj.getString("name");
										mobilePhone.mobile = mobilePhoneJsonObj.getString("mobile");
										listMobilePhones.add(mobilePhone);
									}
									listAffairNode.get(i).getListAffairMini().get(j).connections.organizationMini.listMobilePhone = listMobilePhones;
								}
							}

						}
					}

				}
				if (listAffairNode != null) {
					for (AffairNode node : listAffairNode) {
						for (AffairsMini affair : node.getListAffairMini()) {
							if (affair.connections != null) {
								affair.connections.doCompatible();
							}
						}
					}
					dataBox.put(strKey, listAffairNode);
				}
			}

			strKey = "listJointPeopleNode";
			if (!jsonObject.isNull(strKey)) {
				List<ConnectionNode> listPeopleNode = new Gson().fromJson(jsonObject.getJSONArray(strKey).toString(), new TypeToken<List<ConnectionNode>>() {
				}.getType());
				if (listPeopleNode != null) {
					for (ConnectionNode node : listPeopleNode) {
						for (Connections conn : node.getListConnections()) {
							conn.doCompatible();
						}
					}
					dataBox.put(strKey, listPeopleNode);
				}
			}

			strKey = "listJointOrganizationNode";
			if (!jsonObject.isNull(strKey)) {
				List<ConnectionNode> listOrganizationNode = new Gson().fromJson(jsonObject.getJSONArray(strKey).toString(), new TypeToken<List<ConnectionNode>>() {
				}.getType());
				if (listOrganizationNode != null) {
					for (ConnectionNode node : listOrganizationNode) {
						for (Connections conn : node.getListConnections()) {
							conn.doCompatible();
						}
					}
					dataBox.put(strKey, listOrganizationNode);
				}
			}

			strKey = "listJointKnowledgeNode";
			if (!jsonObject.isNull(strKey)) {
				List<KnowledgeNode> listKnowledgeNode = new Gson().fromJson(jsonObject.getJSONArray(strKey).toString(), new TypeToken<List<KnowledgeNode>>() {
				}.getType());

				// 对事物节点listJointKnowledgeNode下的mobilephone进行解析
				JSONArray listJointKnowledgeNodeJsonArr = jsonObject.getJSONArray("listJointKnowledgeNode");
				for (int i = 0; i < listJointKnowledgeNodeJsonArr.length(); i++) {
					JSONObject jointKnowledgeNodeJsonObj = (JSONObject) listJointKnowledgeNodeJsonArr.get(i);
					JSONArray listKnowledgeMini2JsonArr = jointKnowledgeNodeJsonObj.getJSONArray("listKnowledgeMini2");
					if (listKnowledgeMini2JsonArr.length() > 0 && listKnowledgeMini2JsonArr != null) {
						for (int j = 0; j < listKnowledgeMini2JsonArr.length(); j++) {
							JSONObject KnowledgeMini2JsonObj = (JSONObject) listKnowledgeMini2JsonArr.get(j);
							JSONObject connectionsJsonObj = KnowledgeMini2JsonObj.getJSONObject("connections");
							JSONObject jtContactMiniJsonObj = null;
							if (!connectionsJsonObj.isNull("jtContactMini")) {
								jtContactMiniJsonObj = connectionsJsonObj.getJSONObject("jtContactMini");
								if (!connectionsJsonObj.isNull("id")) {
									String id = connectionsJsonObj.getString("id");
									if (listKnowledgeNode.size() > i) {
										listKnowledgeNode.get(i).getListKnowledgeMini2().get(j).connections.setID(id);
									}
								}
							}
							JSONObject organizationMiniJsonObj = null;
							if (!connectionsJsonObj.isNull("organizationMini")) {
								organizationMiniJsonObj = connectionsJsonObj.getJSONObject("organizationMini");
								if (!connectionsJsonObj.isNull("id")) {
									String id = connectionsJsonObj.getString("id");
									if (listKnowledgeNode.size() > i) {
										listKnowledgeNode.get(i).getListKnowledgeMini2().get(j).connections.setID(id);
									}
								}

							}
							if (jtContactMiniJsonObj != null) {
								JSONArray listMobilePhoneJsonArr = jtContactMiniJsonObj.getJSONArray("listMobilePhone");
								if (listMobilePhoneJsonArr != null && listMobilePhoneJsonArr.length() > 0) {
									ArrayList<MobilePhone> listMobilePhones = new ArrayList<MobilePhone>();
									for (int k = 0; k < listMobilePhoneJsonArr.length(); k++) {
										JSONObject mobilePhoneJsonObj = (JSONObject) listMobilePhoneJsonArr.get(k);
										MobilePhone mobilePhone = new MobilePhone();
										mobilePhone.name = mobilePhoneJsonObj.getString("name");
										mobilePhone.mobile = mobilePhoneJsonObj.getString("mobile");
										listMobilePhones.add(mobilePhone);
									}
									listKnowledgeNode.get(i).getListKnowledgeMini2().get(j).connections.jtContactMini.listMobilePhone = listMobilePhones;
								}
							}
							else if (organizationMiniJsonObj != null) {
								JSONArray listMobilePhoneJsonArr = organizationMiniJsonObj.getJSONArray("listMobilePhone");
								if (listMobilePhoneJsonArr != null && listMobilePhoneJsonArr.length() > 0) {
									ArrayList<MobilePhone> listMobilePhones = new ArrayList<MobilePhone>();
									for (int k = 0; k < listMobilePhoneJsonArr.length(); k++) {
										JSONObject mobilePhoneJsonObj = (JSONObject) listMobilePhoneJsonArr.get(k);
										MobilePhone mobilePhone = new MobilePhone();
										mobilePhone.name = mobilePhoneJsonObj.getString("name");
										mobilePhone.mobile = mobilePhoneJsonObj.getString("mobile");
										listMobilePhones.add(mobilePhone);
									}
									listKnowledgeNode.get(i).getListKnowledgeMini2().get(j).connections.organizationMini.listMobilePhone = listMobilePhones;
								}
							}
							//
						}
					}

				}

				if (listKnowledgeNode != null) {
					for (KnowledgeNode node : listKnowledgeNode) {
						for (KnowledgeMini2 kno : node.getListKnowledgeMini2()) {
							if (kno.connections != null) {
								kno.connections.doCompatible();
							}
						}
					}
					dataBox.put(strKey, listKnowledgeNode);
				}
			}
			return dataBox;
		case EAPIConsts.CommonReqType.CorrectJointResult: // 对接纠错
			strKey = "success";
			boolean success = jsonObject.optBoolean(strKey);
			dataBox.put(strKey, success);
			return dataBox;
		case EAPIConsts.CommonReqType.FetchExternalKnowledgeUrl: // 解析url
			strKey = "knowledge2";
			if (!jsonObject.isNull(strKey)) {
				Knowledge2 knowledge2 = new Gson().fromJson(jsonObject.getJSONObject(strKey).toString(), Knowledge2.class);
				if (knowledge2 != null) {
					if (knowledge2.getListRelatedConnectionsNode() != null) {
						for (ConnectionNode listNodes : knowledge2.getListRelatedConnectionsNode()) {
							for (Connections connection : listNodes.getListConnections()) {
								connection.doCompatible();
							}
						}
					}
					if (knowledge2.getListRelatedOrganizationNode() != null) {
						for (ConnectionNode OrgNodes : knowledge2.getListRelatedOrganizationNode()) {
							for (Connections orgItem : OrgNodes.getListConnections()) {
								orgItem.doCompatible();
							}
						}
					}
					dataBox.put(strKey, knowledge2);
				}
			}
			return dataBox;
		case EAPIConsts.CommonReqType.GetMyCountList:
			// TODO
			return MHomePageNumInfos.createFactory(jsonObject);
		case EAPIConsts.CommonReqType.UploadUserProfile:
			// TODO
			DataBox data = new DataBox(); // 返回对象
			String str_key = ""; // 键值
			str_key = "jtMember";
			if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
				JTMember member = new JTMember();
				member.initWithJson(jsonObject.getJSONObject(str_key));
				data.mJTMember = member;
			}
			str_key = "JTMember";
			if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
				JTMember member = new JTMember();
				member.initWithJson(jsonObject.getJSONObject(str_key));
				data.mJTMember = member;
			}
			return data;
		case EAPIConsts.CommonReqType.CHECK_FRIEND:
			return MCheckFriend.createFactory(jsonObject);
		case EAPIConsts.CommonReqType.getUserQRUrl:
			return MUserQRUrl.createFactory(jsonObject);
		case EAPIConsts.CommonReqType.getCountryCode:
			return MListCountry.createFactory(jsonObject);
		case EAPIConsts.CommonReqType.setCustomMade:
			return SimpleResult.createFactory(jsonObject);
		case EAPIConsts.CommonReqType.getCategoryQueryTree:/*查询用户目录（返回整棵树）*/
			strKey = "categorys";
			ArrayList<CommonUserCategory> CommonUserCategory_List = null;
			if (!jsonObject.isNull(strKey)) {
				CommonUserCategory_List = new Gson().fromJson(jsonObject.toString(), new TypeToken<List<CommonUserCategory>>() {
				}.getType());
			}
			return CommonUserCategory_List;
		case EAPIConsts.CommonReqType.GetJointResource_New: // 获取生态对接资源
		case EAPIConsts.CommonReqType.GetJointResource_MY: // 获取生态对接资源
		case EAPIConsts.CommonReqType.GetJointResource_FRIEND: // 获取生态对接资源
		case EAPIConsts.CommonReqType.GetJointResource_GT: // 获取生态对接资源
			strKey = "JointResource";
			JointResource jr = new Gson().fromJson(jsonObject.toString(), JointResource.class);
			dataBox.put(strKey, jr);
			return dataBox;
		}
		return null;
	}
}
