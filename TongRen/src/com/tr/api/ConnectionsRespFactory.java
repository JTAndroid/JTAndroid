package com.tr.api;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tr.App;
import com.tr.db.ConnectionsCacheData;
import com.tr.db.ConnectionsDBManager;
import com.tr.db.DBHelper;
import com.tr.model.connections.FriendRequest;
import com.tr.model.connections.MGetBlackList;
import com.tr.model.home.MGetDynamic;
import com.tr.model.model.PeopleRelatedResource;
import com.tr.model.model.ResourceMini;
import com.tr.model.model.UserConfig;
import com.tr.model.model.UserFeed;
import com.tr.model.obj.CommentApprover;
import com.tr.model.obj.Connections;
import com.tr.model.obj.ConnectionsMini;
import com.tr.model.obj.JTContact2;
import com.tr.model.obj.JTContactMini;
import com.tr.model.obj.Organization;
import com.tr.model.obj.UserComment;
import com.tr.model.user.OrganizationMini;
import com.tr.ui.home.frg.FrgConnections2;
import com.utils.common.QJsonParser;
import com.utils.common.QJsonParser.DataType;
import com.utils.http.EAPIConsts;

/**
 * @ClassName: UserRespFactory.java
 * @Description: 用户相关的接口回调
 * @Author leon
 * @Version v 1.0
 * @Created 2014-04-11
 * @Updated 2014-04-14
 */
public class ConnectionsRespFactory {

	public static final String TAG = ConnectionsRespFactory.class.getSimpleName();

	public static Object createMsgObject(int msgId, JSONObject jsonObject) {
		
//		if(jsonObject==null){
//			return null;
//		}
		
		Map<String, Object> dataMap = new HashMap<String, Object>(); // 返回数据
		String strKey = "";
		String jsonStr = "";
		
		try {
			String str_key = ""; // 键值
			GsonBuilder builder = new GsonBuilder();  
			Gson gson = builder.serializeNulls().create(); 
			switch (msgId) {
			
				case EAPIConsts.concReqType.getAllRelations:  // 根据类型获得指定类型关系及 全部类型关系 列表
				case EAPIConsts.concReqType.getFriends:  // 获得 通讯录( 人好友 和 组织好友 ) 列表 
				case EAPIConsts.concReqType.CONNECTIONSLIST: { // 获得 我的好友和人脉 列表 
					if(jsonObject==null){
						return null;
					}
					ArrayList<Connections> connArr=new ArrayList<Connections>();
					if(jsonObject.has("listConnections")){
						JSONArray connJsArr=jsonObject.getJSONArray("listConnections");
						Connections connections;
						for(int i=0;i<connJsArr.length();i++){
							connections=new Connections();
							connections.initWithJson(connJsArr.getJSONObject(i));
							connArr.add(connections);
						}
					}
					return connArr;
				}
				
				case EAPIConsts.concReqType.CheckMobiles:
					if(jsonObject==null){
						return null;
					}
					QJsonParser.parse("success", DataType.BOOLEAN, dataMap, jsonObject);
					strKey = "listOnLine";
					if(jsonObject.has(strKey)){
						ArrayList<Connections> connArr=new ArrayList<Connections>();
						JSONArray connJsArr=jsonObject.getJSONArray(strKey);
						Connections connections;
						for(int i=0;i<connJsArr.length();i++){
							connections=new Connections();
							connections.initWithJson(connJsArr.getJSONObject(i));
							connArr.add(connections);
						}
						dataMap.put(strKey, connArr);
					}
					
					strKey = "listOffLine";
					if(jsonObject.has(strKey)){
						ArrayList<Connections> connArr=new ArrayList<Connections>();
						JSONArray connJsArr=jsonObject.getJSONArray(strKey);
						Connections connections;
						for(int i=0;i<connJsArr.length();i++){
							connections=new Connections();
							connections.initWithJson(connJsArr.getJSONObject(i));
							connArr.add(connections);
						}
						dataMap.put(strKey, connArr);
					}
					
					break;
					
				case EAPIConsts.concReqType.im_upphonebook:{
					if(jsonObject==null){
						return null;
					}
					String v=jsonObject.optString("succeed", "");
					return v;
				}
				case EAPIConsts.concReqType.ContactDetail:{
					if(jsonObject==null){
						return null;
					}
					/*
					try {
						File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile(),"gintong_response.txt");    
				        FileOutputStream fos;
						fos = new FileOutputStream(file);
						byte [] bytes = jsonObject.toString().getBytes();   
					    fos.write(bytes);   
					    fos.close();
					} 
					catch (FileNotFoundException e) {
						e.printStackTrace();
					}    
					catch(IOException e){
						e.printStackTrace();
					}
					*/
					ArrayList data=new ArrayList();
					// 知识id
					str_key = "onlineJTContact";
					if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
						JTContact2 onlineJTContact= gson.fromJson(jsonObject.getJSONObject(str_key).toString(), JTContact2.class); 
						data.add(0);
						data.add(onlineJTContact);
					}
					// 知识id
					str_key = "offlineJTContact";
					if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
						JTContact2 onlineJTContact= gson.fromJson(jsonObject.getJSONObject(str_key).toString(), JTContact2.class); 
						data.add(1);
						data.add(onlineJTContact);
					}
					return data;
				}
				case EAPIConsts.concReqType.im_getOrganizationDetail:{
					if(jsonObject==null){
						return null;
					}
					ArrayList data=new ArrayList();
					str_key = "onlineOrganization";
					if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
						Organization onlineJTContact=new Organization();
						onlineJTContact.initWithJson(jsonObject.getJSONObject(str_key));
						data.add(0);
						data.add(onlineJTContact);
					}
					str_key = "offlineOrganization";
					if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
						Organization onlineJTContact=new Organization();
						onlineJTContact.initWithJson(jsonObject.getJSONObject(str_key));
						data.add(1);
						data.add(onlineJTContact);
					}
					return data;
				}
				case EAPIConsts.concReqType.im_addJTContact:{
					if(jsonObject==null){
						return null;
					}
					Connections connections=null;
					str_key = "connections";
					if( jsonObject.has(str_key) && !jsonObject.isNull(str_key)){
						connections=new Connections();
						connections.initWithJson(jsonObject.getJSONObject(str_key));
					}
					
					String v=jsonObject.optString("succeed", "");
					ArrayList arr=new ArrayList();
					arr.add(v);
					arr.add(connections);
					return arr;
				}
				//获取新关系列表
				case EAPIConsts.concReqType.im_getnewConnections:{
					if(jsonObject==null){
						return null;
					}
					if(jsonObject.has("listFriendRequest")){
						JSONArray jSONArray=jsonObject.getJSONArray("listFriendRequest");
						ArrayList<FriendRequest> arr=new ArrayList<FriendRequest>();
						for(int i=0;i<jSONArray.length();i++){
							FriendRequest newConnectionItem=new FriendRequest();
							newConnectionItem.initWithJson(jSONArray.getJSONObject(i));
							arr.add(newConnectionItem);
						}
						return arr;
					}
				}
				case EAPIConsts.concReqType.im_addFriend:{
					if(jsonObject==null){
						return null;
					}
					String v=jsonObject.optString("succeed", "");
					return v;
				}
				case EAPIConsts.concReqType.im_deleteFriend:{//解除好友关系
					if(jsonObject==null){
						return null;
					}
					String v=jsonObject.optString("succeed", "");
					return v;
				}
				case EAPIConsts.concReqType.im_allowConnectionsRequest:{
					if(jsonObject==null){
						return null;
					}
					String v=jsonObject.optString("succeed", "");
					return v;
				}
				case EAPIConsts.concReqType.im_getWorkmate:{
					if(jsonObject==null){
						return null;
					}
					ArrayList<ArrayList<Connections>> contents=new ArrayList<ArrayList<Connections>>();
					ArrayList<Connections> title= new ArrayList<Connections>();
					if(jsonObject.has("listWorkmate")){
						JSONArray datas=jsonObject.getJSONArray("listWorkmate");
						for(int i=0;i<datas.length();i++){
							JSONObject data=datas.getJSONObject(i);
							if(data.has("organizationMini")){
								Connections newConnectionItem=new Connections();
								newConnectionItem.type=Connections.type_org;
								OrganizationMini newConnectionItem1=new OrganizationMini();
								newConnectionItem1.initWithJson(data.getJSONObject("organizationMini"));
								newConnectionItem.organizationMini=newConnectionItem1;
								title.add(newConnectionItem);
							}
							if(data.has("listJTContactMini")){
								JSONArray jsonArray=data.getJSONArray("listJTContactMini");
								ArrayList<Connections> arr= new ArrayList<Connections>();
								for(int j=0;j<jsonArray.length();j++){
									Connections newConnectionItem=new Connections();
									JTContactMini jmin=new JTContactMini();
									jmin.initWithJson(jsonArray.getJSONObject(j));
									newConnectionItem.jtContactMini=jmin;
									newConnectionItem.type=Connections.type_persion;
									arr.add(newConnectionItem);
								}
								contents.add(arr);
							}
							
						}
					}
					ArrayList returndata=new ArrayList();
					returndata.add(title);
					returndata.add(contents);
					return returndata;
				}
				case EAPIConsts.concReqType.im_delJtContact:{
					if(jsonObject==null){
						return null;
					}
					String v=jsonObject.optString("succeed", "");
					return v;
				}
				case EAPIConsts.concReqType.im_getMatchConnectionsMini:{
					if(jsonObject==null){
						return null;
					}
					ArrayList<ConnectionsMini> listConnectionsMini=null;
					if(jsonObject.has("listConnectionsMini")){
						JSONArray jSONArray=jsonObject.getJSONArray("listConnectionsMini");
						 listConnectionsMini=new ArrayList<ConnectionsMini>();
						for(int i=0;i<jSONArray.length();i++){
							ConnectionsMini connectionsMini=ConnectionsMini.createFactory(jSONArray.getJSONObject(i));
							listConnectionsMini.add(connectionsMini);
						}
					}
					return listConnectionsMini;
				}
				case EAPIConsts.concReqType.im_inviteJoinGinTong:{
					if(jsonObject==null){
						return null;
					}
					String v=jsonObject.optString("succeed","");
					return v;
				}
				case EAPIConsts.concReqType.im_recommend2Friend:{
					if(jsonObject==null){
						return null;
					}
					String v=jsonObject.optString("succeed","");
					return v;
				}
				case EAPIConsts.concReqType.im_relevantPeopleAndCustomer:{
					if(jsonObject==null){
						return null;
					}
					ArrayList<ArrayList<Connections>> contents=new ArrayList<ArrayList<Connections>>();
					ArrayList<Connections> title= new ArrayList<Connections>();
					
					Connections connections=new Connections();
					OrganizationMini organizationMini=new OrganizationMini();
					organizationMini.fullName="所有关系";
					connections.type=Connections.type_org;
					connections.organizationMini=organizationMini;
					title.add(connections);
					
					ArrayList<Connections> listConnectionsMini=null;
					if(jsonObject.has("listConnectionsMini")){
						JSONArray jSONArray=jsonObject.getJSONArray("listConnectionsMini");
						 listConnectionsMini=new ArrayList<Connections>();
						for(int i=0;i<jSONArray.length();i++){
							ConnectionsMini connectionsMini=ConnectionsMini.createFactory(jSONArray.getJSONObject(i));
							listConnectionsMini.add(connectionsMini.toConnections());
						}
					}
					contents.add(listConnectionsMini);
					ArrayList returndata=new ArrayList();
					returndata.add(title);
					returndata.add(contents);
					
					return returndata;
				}
			
				case EAPIConsts.concReqType.im_getNewConnectionsCount: {
					int count=0;
					if (jsonObject != null) {
						count=jsonObject.optInt("count");
					}
					return count;
				}
				case EAPIConsts.concReqType.im_getNewDynamicCount: {
					int count=0;
					if (jsonObject != null) {
						count=jsonObject.optInt("count");
					}
					return count;
				}
				case EAPIConsts.concReqType.im_holdJTContact: {
					if(jsonObject==null){
						return null;
					}
					Connections connections = null;
					str_key = "connections";
					if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
						connections=new Connections();
						connections.initWithJson(jsonObject.getJSONObject(str_key));
						//connections= gson.fromJson(jsonObject.getJSONObject(str_key).toString(), Connections.class); 
					}
					return connections;
				}
				case EAPIConsts.concReqType.im_holdOrginazitionGuest: {
					if(jsonObject==null){
						return null;
					}
					String v=jsonObject.optString("succeed","");
					return v;
				}
				case EAPIConsts.concReqType.im_delOrganization: {
					if(jsonObject==null){
						return null;
					}
					String v=jsonObject.optString("succeed","");
					return v;
				} 
				case EAPIConsts.concReqType.im_getJTContactTemplet: {	
					if(jsonObject==null){
						return null;
					}
					JTContact2 onlineJTContact = null;
					str_key = "jtContact";
					if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
						onlineJTContact= gson.fromJson(jsonObject.getJSONObject(str_key).toString(), JTContact2.class); 
					}
					return onlineJTContact;
				} 
				case EAPIConsts.concReqType.getPeopleRelatedResources:{ // 相关资源
					if(jsonObject == null){
						return null;
					}
					PeopleRelatedResource peopleRes = new PeopleRelatedResource();
					str_key = "listPeople";
					if(jsonObject.has(str_key) && !jsonObject.isNull(str_key)){
						JSONArray jArray = jsonObject.getJSONArray(str_key);
						if(jArray != null){
							List<ResourceMini> listPeople = new ArrayList<ResourceMini>();
							for (int i = 0; i < jArray.length(); i++) {	
								ResourceMini res = new ResourceMini();
								res.initWithJson(jArray.getJSONObject(i));
								listPeople.add(res);
							}
							peopleRes.listPeople = (listPeople);
						}
					}
					str_key = "listOrganization";
					if(jsonObject.has(str_key) && !jsonObject.isNull(str_key)){
						JSONArray jArray = jsonObject.getJSONArray(str_key);
						if(jArray != null){
							List<ResourceMini> listOrganization = new ArrayList<ResourceMini>();
							for (int i = 0; i < jArray.length(); i++) {	
								ResourceMini res = new ResourceMini();
								res.initWithJson(jArray.getJSONObject(i));
								listOrganization.add(res);
							}
							peopleRes.listOrganization=(listOrganization);
						}
					}
					str_key = "listRequirement";
					if(jsonObject.has(str_key) && !jsonObject.isNull(str_key)){
						JSONArray jArray = jsonObject.getJSONArray(str_key);
						if(jArray != null){
							List<ResourceMini> listRequirement = new ArrayList<ResourceMini>();
							for (int i = 0; i < jArray.length(); i++) {	
								ResourceMini res = new ResourceMini();
								res.initWithJson(jArray.getJSONObject(i));
								listRequirement.add(res);
							}
							peopleRes.listRequirement=(listRequirement);
						}
					}
					str_key = "listKnowledge";
					if(jsonObject.has(str_key) && !jsonObject.isNull(str_key)){
						JSONArray jArray = jsonObject.getJSONArray(str_key);
						if(jArray != null){
							List<ResourceMini> listKnowledge = new ArrayList<ResourceMini>();
							for (int i = 0; i < jArray.length(); i++) {	
								ResourceMini res = new ResourceMini();
								res.initWithJson(jArray.getJSONObject(i));
								listKnowledge.add(res);
							}
							peopleRes.listKnowledge = (listKnowledge);
						}
					}
					return peopleRes;
				}
				case EAPIConsts.concReqType.getActionList:{
					if(jsonObject==null){
						return null;
					}
					ArrayList<UserFeed> data=null;
					str_key = "userFeedList";
					if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
						JSONArray jArray=jsonObject.getJSONArray(str_key);
						if(jArray != null){
							data = new ArrayList<UserFeed>();
							for (int i = 0; i < jArray.length(); i++) {	
								UserFeed res = gson.fromJson(jArray.getJSONObject(i).toString(), UserFeed.class); 
								data.add(res);
							}
						}
					}
					return data;
				}
				
				// 获取用户 查看权限
				case EAPIConsts.concReqType.getVisible:{
					if(jsonObject==null){
						return null;
					}
					UserConfig data=null;
					str_key = "userConfig";
					
					if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
						data = gson.fromJson(jsonObject.getJSONObject(str_key).toString(), UserConfig.class); 
					}
					return data;
				}
				// 设置 用户查看权限
				case EAPIConsts.concReqType.setVisible:{
					if(jsonObject==null){
						return null;
					}
					String v=jsonObject.optString("succeed","");
					return v;
				}
				
				case EAPIConsts.concReqType.getSharePart:{
					if(jsonObject==null){
						return null;
					}
					String v=jsonObject.optString("id", "");
					return v;
				}
				
				case EAPIConsts.concReqType.getShareDetail: {
					if(jsonObject==null){
						return null;
					}
					JTContact2 onlineJTContact = null;
					str_key = "offlineJTContact";
					if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) { 
						onlineJTContact= gson.fromJson(jsonObject.getJSONObject(str_key).toString(), JTContact2.class); 
					}
					if(onlineJTContact!=null){
						ArrayList arr=new ArrayList();
						arr.add(1);
						arr.add(onlineJTContact);
						return arr;
					}
					return null;
				}
				
				case EAPIConsts.concReqType.findEvaluate:{//获取评价
					if(jsonObject==null){
						return null;
					}
					ArrayList arr=new ArrayList();
					String v=jsonObject.optString("isEvaluated", "");//非好友是否可以评价
					arr.add(v);
					ArrayList<UserComment> userCommentList=new ArrayList<UserComment>();
					if(jsonObject.has("listUserComment")&&!jsonObject.isNull("listUserComment")){
						JSONArray userCommentJsArr=jsonObject.getJSONArray("listUserComment");
						UserComment userComment;
						for(int i=0;i<userCommentJsArr.length();i++){
							userComment=new UserComment();
							userComment.initWithJson(userCommentJsArr.getJSONObject(i));
							userCommentList.add(userComment);
						}
//						return userCommentList;
					}
					arr.add(userCommentList);
					return arr;
				}
				
				case EAPIConsts.concReqType.feedbackEvaluate:{//赞同/取消赞同评价
					if(jsonObject==null){
						return null;
					}
					String v=jsonObject.optString("success", "");
					return v;
				}
				
				case EAPIConsts.concReqType.moreEvaluate:{ //获取更多评价
					if(jsonObject==null){
						return null;
					}
					ArrayList<CommentApprover> commentApproverList=new ArrayList<CommentApprover>();
					if (jsonObject.has("listCommentApprover")&&!jsonObject.isNull("listCommentApprover")) {
						JSONArray commentApproverJsArr=jsonObject.getJSONArray("listCommentApprover");
						CommentApprover commentApprover;
						for (int i = 0; i < commentApproverJsArr.length(); i++) {
							commentApprover = new CommentApprover();
							commentApprover.initWithJson(commentApproverJsArr.getJSONObject(i));
							commentApproverList.add(commentApprover);
						}
						return commentApproverList;
					}
				}
				
				case EAPIConsts.concReqType.addEvaluate:{ //添加评价
					if(jsonObject==null){
						return null;
					}
					ArrayList arr=new ArrayList();
					str_key = "id";
					String v=jsonObject.optString("success", "");
					arr.add(v);
					if( jsonObject.has(str_key) && !jsonObject.isNull(str_key)){
						Long id = jsonObject.getLong(str_key);
						arr.add(id);
					}
					return arr;
					
				}
				
				case EAPIConsts.concReqType.deleteEvaluate:{//删除评价
					if(jsonObject==null){
						return null;
					}
					String v=jsonObject.optString("success", "");
					return v;
				}
				
				case EAPIConsts.concReqType.editBlack:{//编辑黑名单
					if(jsonObject==null){
						return null;
					}
					String v=jsonObject.optString("succeed", "");
					return v;
				}
				
				case EAPIConsts.concReqType.addDynamic:{//转发为动态
					if(jsonObject==null){
						return null;
					}
					String v=jsonObject.optString("succeed", "");
					return v;
				}
				//根据感兴趣的行业推送用户列表
				case EAPIConsts.concReqType.pushPeopleList:
					if(jsonObject==null){
						return null;
					}
					strKey = "listUser";
					if (jsonObject.has(strKey)) {
						JSONArray jSONArray = jsonObject.getJSONArray(strKey);
						ArrayList<ConnectionsMini> listConnectionsMini = new ArrayList<ConnectionsMini>();
						for (int i = 0; i < jSONArray.length(); i++) {
							ConnectionsMini connectionsMini = ConnectionsMini.createFactory(jSONArray.getJSONObject(i));
							listConnectionsMini.add(connectionsMini);
						}
						dataMap.put(strKey, listConnectionsMini);
					}
				break;
				// 发送邀请加入金桐短信 
				case EAPIConsts.concReqType.sendSMS:
					if(jsonObject==null){
						return null;
					}
					QJsonParser.parse("success", DataType.BOOLEAN, dataMap, jsonObject);
					QJsonParser.parse("msg", DataType.STRING, dataMap, jsonObject);
					break;
					
				case EAPIConsts.concReqType.blacklist://获取黑名单列表
					if (jsonObject==null) {
						return null;
					}
					return MGetBlackList.createFactory(jsonObject);
					
				//  批量添加好友
				case EAPIConsts.concReqType.addFriends:
					if(jsonObject==null){
						return null;
					}
					QJsonParser.parse("success", DataType.BOOLEAN, dataMap, jsonObject);
					QJsonParser.parse("msg", DataType.STRING, dataMap, jsonObject);
					break;
					
				default:
					return null;
			}
		} 
		catch (JSONException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		return dataMap;
	}
}
