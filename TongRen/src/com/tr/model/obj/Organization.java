package com.tr.model.obj;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tr.ui.home.frg.FrgConnections2;
import com.utils.string.StringUtils;


/**
 * @ClassName:     OrganizationMini.java 文档1.20
 * @author         xuxinjian
 * @version        V1.0  
 * @Date           2014-3-28 上午7:54:17
 */
public class Organization implements Serializable{
	private static final long serialVersionUID = 19832888142324422L;
	
//	 "logo":"机构logo图片url",
//  "fullName":"机构全称",
//  "shortName":"客户简称",
//  "guestType":"客户类型，0-一般客户，1-合作客户,2-核心客户",
//  "isOnline":"是否线上机构",
//  "isOffline":"是否线下人脉，也就是是否维护了该用户为人脉",
//  "friendState":"0-好友；1-非好友；2-等待对方验证；3-对方请求我为好友待我通过；4-第三方推荐为我的好友",
//  "joinState":"加入机构状态，0-已加入机构、1-无关系、2-已申请加入机构，待审批、3-机构已邀请加入，待我审批",
//  "fromDes":"人脉来源描述， 如XX引荐、我主要请求、对方请求等",
//  "brief":"机构简介",
//  "listRequirementMini":[],
//  "outInvestKeyword":{},
//  "InInvestKeyword":{},
//
//  "listJTFile":[{}]
	
	
	public String id;
	public String logo="";
	public String fullName="";
	public String shortName="";
	public int guestType=0;
	public boolean isOnline=false;
	public boolean isOffline=false;
	public int friendState=0;
	public int joinState=0;
	public String fromDes="";
	public String brief="";
	public String trade="";
	public String tradeId="";
	public String area="";
	public String paperDocumentNumber="";

	public InvestKeyword inInvestKeyword=new InvestKeyword();
	public InvestKeyword outInvestKeyword=new InvestKeyword();
	public ArrayList<RequirementMini> listRequirementMini=new ArrayList<RequirementMini>();
	public ArrayList<JTFile> listJtFile=new ArrayList<JTFile>();
	
	public List<RequirementMini> listMatchRequirementMini = new ArrayList<RequirementMini>(); // 匹配需求
	public List<ConnectionsMini> listMatchConnectionsMini = new ArrayList<ConnectionsMini>(); // 匹配关系
	public List<KnowledgeMini> listMatchKnowledgeMini = new ArrayList<KnowledgeMini>();// 匹配的知识
	
	public Connections toConnections(){
		Connections connections=new Connections();
		connections.type=Connections.type_org;
		
		connections.setID(this.id);
		if(!StringUtils.isEmpty(this.fullName)){
			connections.setName(this.fullName);
			String pinyinName=FrgConnections2.converterToFirstSpell(this.fullName);
			if(!StringUtils.isEmpty(pinyinName)){
				connections.setCharName(pinyinName.charAt(0));
			}
		}
		connections.setJoinState(this.joinState);
		connections.setImage(this.logo);
		connections.setOnline(this.isOnline);
		connections.setFriendState(this.friendState);
		return connections;
	}
	
	public JSONObject toNewJSONObject() throws JSONException {
		JSONObject jObject1 = new JSONObject();
		JSONObject jObject = new JSONObject();
		jObject.put("orginazition", jObject1);
		jObject1.put("id", id);
		jObject1.put("logo", logo);
		jObject1.put("fullName", fullName);
		jObject1.put("shortName", shortName);
		jObject1.put("guestType", guestType);
		jObject1.put("isOnline", isOnline);
		jObject1.put("isOffline", isOffline);
		jObject1.put("friendState", friendState);
		jObject1.put("joinState", joinState);
		jObject1.put("fromDes", fromDes);
		jObject1.put("brief", brief);
		jObject1.put("trade", trade);
		jObject1.put("tradeId", tradeId);
		jObject1.put("area", area);
		jObject1.put("paperDocumentNumber", paperDocumentNumber);
		
		jObject1.put("outInvestKeyword",outInvestKeyword.toJSONObject() );
		jObject1.put("inInvestKeyword",inInvestKeyword.toJSONObject());
			
		return jObject;
	}
	
	public JTFile toJTFile(){
		JTFile jtFile=new JTFile();
		if(this.isOnline){
			jtFile.mType=JTFile.TYPE_ORG_ONLINE;
		}else{
			jtFile.mType=JTFile.TYPE_ORG_OFFLINE;
		}
		jtFile.mTaskId=id+"";
		jtFile.mFileName=fullName;
	//	jtFile.mSuffixName=fullName;
		String replaceTrade=null;
		if(!StringUtils.isEmpty(trade)){
			replaceTrade=trade.replace(',', ' ');
			if(!StringUtils.isEmpty(replaceTrade)){
				replaceTrade=replaceTrade.trim();
			}
		}
		jtFile.mSuffixName=replaceTrade;
		jtFile.mUrl=logo;
		return jtFile;
	}


	public void initWithJson(JSONObject jsonObject) throws JSONException, MalformedURLException, ParseException
			 {
			String str_key = null;
				str_key = "id";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					id = jsonObject.getString(str_key);
				}
				str_key = "logo";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					logo = jsonObject.getString(str_key);
				}
				str_key = "fullName";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					fullName = jsonObject.getString(str_key);
				}
				str_key = "shortName";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					shortName = jsonObject.getString(str_key);
				}
				str_key = "isOnline";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					isOnline = jsonObject.getBoolean(str_key);
				}
				str_key = "isOffline";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					isOffline = jsonObject.getBoolean(str_key);
				}
				str_key = "friendState";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					friendState = jsonObject.getInt(str_key);
				}
				str_key = "joinState";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					joinState = jsonObject.getInt(str_key);
				}
				
				str_key = "fromDes";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					fromDes = jsonObject.getString(str_key);
				}
				
				str_key = "brief";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					brief = jsonObject.getString(str_key);
				}
			
				str_key = "trade";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					trade = jsonObject.getString(str_key);
				}
				
				str_key = "tradeId";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					tradeId = jsonObject.getString(str_key);
				}
				
				str_key = "area";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					area = jsonObject.getString(str_key);
				}
				
				str_key = "paperDocumentNumber";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					paperDocumentNumber = jsonObject.getString(str_key);
				}
				
				str_key = "outInvestKeyword";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					outInvestKeyword.initWithJson(jsonObject.getJSONObject(str_key));
				}
				
				str_key = "inInvestKeyword";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					inInvestKeyword.initWithJson(jsonObject.getJSONObject(str_key));
				}
				
				str_key = "listJTFile";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					JSONArray jArray = jsonObject.getJSONArray(str_key);
					if (jArray != null && jArray.length() > 0) {
						for (int i = 0; i < jArray.length(); i++) {
							JTFile jTFile=new JTFile() ;
							jTFile.initWithJson(jArray.getJSONObject(i));
							listJtFile.add(jTFile);
						}
					}
				}
				
				str_key = "listRequirementMini";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					JSONArray jArray = jsonObject.getJSONArray(str_key);
					if (jArray != null && jArray.length() > 0) {
						for (int i = 0; i < jArray.length(); i++) {
							RequirementMini personInfo=new RequirementMini() ;
							personInfo.initWithJson(jArray.getJSONObject(i));
							listRequirementMini.add(personInfo);
						}
					}
				}
				
				// 匹配需求
				str_key = "listMatchRequirementMini";
				if(jsonObject.has(str_key) && !jsonObject.isNull(str_key)){
					JSONArray jArray = jsonObject.getJSONArray(str_key);
					if(jArray != null && jArray.length() >0){
						for (int i = 0; i < jArray.length(); i++) {
							RequirementMini reqMini = new RequirementMini();
							reqMini.initWithJson(jArray.getJSONObject(i));
							listMatchRequirementMini.add(reqMini);
						}
					}
				}
				
				// 匹配关系
				str_key = "listMatchConnectionsMini";
				if(jsonObject.has(str_key) && !jsonObject.isNull(str_key)){
					JSONArray jArray = jsonObject.getJSONArray(str_key);
					if(jArray != null && jArray.length() >0){
						for (int i = 0; i < jArray.length(); i++) {
							ConnectionsMini conMini = ConnectionsMini
									.createFactory(jArray.getJSONObject(i));
							if (conMini != null) {
								listMatchConnectionsMini.add(conMini);
							}
						}
					}
				}
				
				// 匹配知识
				str_key = "listKnowledgeMini";
				if(jsonObject.has(str_key) && !jsonObject.isNull(str_key)){
					JSONArray jArray = jsonObject.getJSONArray(str_key);
					if(jArray != null && jArray.length() >0){
						for (int i = 0; i < jArray.length(); i++) {
							KnowledgeMini knoMini = new KnowledgeMini();
							knoMini.initWithJson(jArray.getJSONObject(i));
							listMatchKnowledgeMini.add(knoMini);
						}
					}
				}
				
				
	}
	
	
}
