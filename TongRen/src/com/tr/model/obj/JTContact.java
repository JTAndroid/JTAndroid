package com.tr.model.obj;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tr.ui.home.frg.FrgConnections2;
import com.utils.string.StringUtils;


/**
 * @ClassName:     JTContact.java
 * @Description:   人脉对象
 * @Author         leon
 * @Version        v 1.0  
 * @Create         2014-04-14
 * @Update         2014-04-14
 */
public class JTContact implements Serializable{
	public String mId;
	public String mName="";//名称
	public String mLastName="";
	public String mimage="";
	public String mcompany=""; //公司名称
	public boolean misOnline=false;
	public boolean misOffline=false;
	public int mfriendState=0;
	public boolean misWorkmate=false;
	public String mfromDes="";
	public String mfax="";
	public String maddress="";
	public String murl="";
	public String mcomment="";
	public String mtaskId="";//图片id
	public String phone="";
	public String msn="";
	public String userJob=""; //职务
	public InvestKeyword minInvestKeyword=new InvestKeyword();
	public InvestKeyword moutInvestKeyword=new InvestKeyword();
	public ArrayList<RequirementMini> listRequirementMini=new ArrayList<RequirementMini>();
	public List<ConnectionsMini> listMatchConnectionsMini = new ArrayList<ConnectionsMini>(); // 匹配关系
	
	public ArrayList<RequirementMini> listMatchRequirementMini=new ArrayList<RequirementMini>();
	public ArrayList<KnowledgeMini> listKnowledgeMini=new ArrayList<KnowledgeMini>();
	public ArrayList<JTFile> listJtFile=new ArrayList<JTFile>();
	
	public ArrayList<Integer> listOrganizationID=new ArrayList<Integer>();
	public ArrayList<Connections> listConnections=new ArrayList<Connections>();
	public ArrayList<WorkExperience> listWorkExperience=new ArrayList<WorkExperience>();
	public ArrayList<EduExperience> listEduExperience=new ArrayList<EduExperience>();
	public ArrayList<MobilePhone> listMobilePhone=new ArrayList<MobilePhone>();
	public ArrayList<SNS> listSns=new ArrayList<SNS>();
	public ArrayList<PersonInfo> listPersonInfo=new ArrayList<PersonInfo>();
	public ArrayList<PersonInfo> listBasePersonInfo=new ArrayList<PersonInfo>();
	public ArrayList<InvestType> listInvestType=new ArrayList<InvestType>();
	public ArrayList<Trade> listTrade=new ArrayList<Trade>();
	public ArrayList<InvestKeyword> listInvestKeyword=new ArrayList<InvestKeyword>();
	public ArrayList<String> listEmail=new ArrayList<String>();
	
	
	public Connections toConnections(){
		Connections connections=new Connections();
		connections.type=Connections.type_persion;
		
		connections.setID(this.mId);
		if(!StringUtils.isEmpty(this.mName)){
			connections.setName(this.mName);
			String pinyinName=FrgConnections2.converterToFirstSpell(this.mName);
			if(!StringUtils.isEmpty(pinyinName)){
				connections.setCharName(pinyinName.charAt(0));
			}
		}
		connections.setImage(this.mimage);
		connections.setOnline(this.misOnline);
		connections.setFriendState(this.mfriendState);
		if(listMobilePhone!=null&&listMobilePhone.size()>0){
			if(!StringUtils.isEmpty(listMobilePhone.get(0).mobile)){
				connections.setPhone(listMobilePhone.get(0).mobile);
			}
		}
		if(listEmail!=null&&listEmail.size()>0){
			if(!StringUtils.isEmpty(listEmail.get(0))){
				connections.setEMail(listEmail.get(0));
			}
		}
		return connections;
	}
	
	public JTFile toJTfile(){
		JTFile mShareInfo=new JTFile();
		if(this.misOnline){
			mShareInfo.mType=JTFile.TYPE_JTCONTACT_ONLINE;
		}else {
			mShareInfo.mType=JTFile.TYPE_JTCONTACT_OFFLINE;
		}
		mShareInfo.mTaskId=mId+"";
		mShareInfo.mFileName=mName;
		if(this.misOnline){
			mShareInfo.mSuffixName=mcompany;
		}else{
			if(listBasePersonInfo!=null&&listBasePersonInfo.size()>0){
				for(int i=0;i<listBasePersonInfo.size();i++){
					if(listBasePersonInfo.get(i).mtag.equals("单位名称")){
						mShareInfo.mSuffixName=listBasePersonInfo.get(i).mvalue;
						break;
					}
				}
			}
		}
		//mShareInfo.mSuffixName=mcompany;
	
		mShareInfo.mUrl=mimage;
		mShareInfo.reserved1=userJob;
		return mShareInfo;
	}
	
	public JSONObject toNewJSONObject() throws JSONException {
		JSONObject jObject1 = new JSONObject();
		JSONObject jObject = new JSONObject();
		jObject1.put("jtContact", jObject);
		jObject.put("id",mId);
		jObject.put("misOffline",misOffline);
		jObject.put("isOnline",misOnline);
		jObject.put("name",mName);
		jObject.put("lastName",mLastName);
		jObject.put("image",mimage);
		jObject.put("comment",mcomment);
		jObject.put("company",mcompany);
		jObject.put("fax",mfax);
		jObject.put("taskId",mtaskId);
		jObject.put("address",maddress);
		jObject.put("phone",phone);
		jObject.put("msn",msn);
		jObject.put("userJob",userJob);
		
			JSONArray jsonArray=new JSONArray();
			for(WorkExperience workExperience :listWorkExperience){
				jsonArray.put(workExperience.toJSONObject());
			}
			jObject.put("listWorkExperience", jsonArray);
			
			jsonArray=new JSONArray();
			for(RequirementMini requirementMini :listMatchRequirementMini){
				jsonArray.put(requirementMini.toJSONObject());
			}
			jObject.put("listMatchRequirementMini", jsonArray);
			
			jsonArray=new JSONArray();
			for(ConnectionsMini connectionsMini :listMatchConnectionsMini){
				jsonArray.put(connectionsMini.toJSONObject());
			}
			jObject.put("listMatchConnectionsMini", jsonArray);
			
	
			 jsonArray=new JSONArray();
			for(EduExperience workExperience :listEduExperience){
				jsonArray.put(workExperience.toJSONObject());
			}
			jObject.put("listEduExperience", jsonArray);

			 jsonArray=new JSONArray();
			for(PersonInfo workExperience :listPersonInfo){
				jsonArray.put(workExperience.toJSONObject());
			}
			jObject.put("listPersonInfo", jsonArray);
		
			jsonArray = new JSONArray();
			for (MobilePhone workExperience : listMobilePhone) {
				jsonArray.put(workExperience.toJSONObject());
			}
			jObject.put("listMobilePhone", jsonArray);
	
			jsonArray = new JSONArray();
			for (String email : listEmail) {
				jsonArray.put(email);
			}
			jObject.put("listEmail", jsonArray);
	
			jObject.put("outInvestKeyword",moutInvestKeyword.toJSONObject() );
				jObject.put("inInvestKeyword",minInvestKeyword.toJSONObject());
				
		return jObject1;
	}
	
	
	
	public JTContact(){
		
	}
	
	public void initWithJson(JSONObject jsonObject) throws JSONException,
			MalformedURLException, ParseException {
		String str_key = null;
				str_key = "id";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					mId = jsonObject.getString(str_key);
				}
				str_key = "taskId";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					mtaskId = jsonObject.getString(str_key);
				}
				
				str_key = "name";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					mName = jsonObject.getString(str_key);
				}
				str_key = "lastName";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					mLastName = jsonObject.getString(str_key);
				}
				str_key = "image";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					mimage = jsonObject.getString(str_key);
				}
				str_key = "company";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					mcompany = jsonObject.getString(str_key);
				}
				str_key = "isOnline";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					misOnline = jsonObject.getBoolean(str_key);
				}
				str_key = "isOffline";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					misOffline = jsonObject.getBoolean(str_key);
				}
				str_key = "friendState";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					mfriendState = jsonObject.getInt(str_key);
				}
				str_key = "isWorkmate";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					misWorkmate = jsonObject.getBoolean(str_key);
				}
				str_key = "fromDes";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					mfromDes = jsonObject.getString(str_key);
				}
				str_key = "fax";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					mfax = jsonObject.getString(str_key);
				}
				str_key = "address";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					maddress = jsonObject.getString(str_key);
				}
				str_key = "phone";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					phone = jsonObject.getString(str_key);
				}
				str_key = "msn";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					msn = jsonObject.getString(str_key);
				}
				str_key = "url";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					murl = jsonObject.getString(str_key);
				}
				str_key = "comment";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					mcomment = jsonObject.getString(str_key);
				}
				str_key = "userJob";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					userJob = jsonObject.getString(str_key);
				}
				str_key = "outInvestKeyword";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					moutInvestKeyword.initWithJson(jsonObject.getJSONObject(str_key));
				}
				
				str_key = "inInvestKeyword";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					minInvestKeyword.initWithJson(jsonObject.getJSONObject(str_key));
				}
				
				str_key = "listOrganizationID";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					JSONArray jArray = jsonObject.getJSONArray(str_key);
					if (jArray != null && jArray.length() > 0) {
						for (int i = 0; i < jArray.length(); i++) {
							int orgid=jArray.getInt(i);
							listOrganizationID.add(orgid);
						}
					}
				}
				
				str_key = "listMobilePhone";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					JSONArray jArray = jsonObject.getJSONArray(str_key);
					if (jArray != null && jArray.length() > 0) {
						for (int i = 0; i < jArray.length(); i++) {
							MobilePhone mobilePhone=new MobilePhone() ;
							mobilePhone.initWithJson(jArray.getJSONObject(i));
							listMobilePhone.add(mobilePhone);
						}
					}
				}
				
				str_key = "listSns";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					JSONArray jArray = jsonObject.getJSONArray(str_key);
					if (jArray != null && jArray.length() > 0) {
						for (int i = 0; i < jArray.length(); i++) {
							SNS sns=new SNS() ;
							sns.initWithJson(jArray.getJSONObject(i));
							listSns.add(sns);
						}
					}
				}
				
				str_key = "listEmail";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					JSONArray jArray = jsonObject.getJSONArray(str_key);
					if (jArray != null && jArray.length() > 0) {
						for (int i = 0; i < jArray.length(); i++) {
							String email=jArray.getString(i);
							listEmail.add(email);
						}
					}
				}
				
				str_key = "listPersonInfo";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					JSONArray jArray = jsonObject.getJSONArray(str_key);
					if (jArray != null && jArray.length() > 0) {
						for (int i = 0; i < jArray.length(); i++) {
							PersonInfo personInfo=new PersonInfo() ;
							personInfo.initWithJson(jArray.getJSONObject(i));
							listPersonInfo.add(personInfo);
						}
					}
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
				
				str_key = "listKnowledgeMini";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					JSONArray jArray = jsonObject.getJSONArray(str_key);
					if (jArray != null && jArray.length() > 0) {
						for (int i = 0; i < jArray.length(); i++) {
							KnowledgeMini personInfo=new KnowledgeMini() ;
							personInfo.initWithJson(jArray.getJSONObject(i));
							listKnowledgeMini.add(personInfo);
						}
					}
				}

				str_key = "listConnections";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					JSONArray jArray = jsonObject.getJSONArray(str_key);
					if (jArray != null && jArray.length() > 0) {
						for (int i = 0; i < jArray.length(); i++) {
							Connections personInfo=new Connections() ;
							personInfo.initWithJson(jArray.getJSONObject(i));
							listConnections.add(personInfo);
						}
					}
				}
				
				str_key = "listWorkExperience";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					JSONArray jArray = jsonObject.getJSONArray(str_key);
					if (jArray != null && jArray.length() > 0) {
						for (int i = 0; i < jArray.length(); i++) {
							WorkExperience personInfo=new WorkExperience() ;
							personInfo.initWithJson(jArray.getJSONObject(i));
							listWorkExperience.add(personInfo);
						}
					}
				}
				
				str_key = "listMatchRequirementMini";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					JSONArray jArray = jsonObject.getJSONArray(str_key);
					if (jArray != null && jArray.length() > 0) {
						for (int i = 0; i < jArray.length(); i++) {
							RequirementMini requirementMini=new RequirementMini() ;
							requirementMini.initWithJson(jArray.getJSONObject(i));
							listMatchRequirementMini.add(requirementMini);
						}
					}
				}
				
				str_key = "listMatchConnectionsMini";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					JSONArray jArray = jsonObject.getJSONArray(str_key);
					if (jArray != null && jArray.length() > 0) {
						for (int i = 0; i < jArray.length(); i++) {
							ConnectionsMini connectionsMini=ConnectionsMini.createFactory(jArray.getJSONObject(i));
							listMatchConnectionsMini.add(connectionsMini);
						}
					}
				}
				
				str_key = "listEduExperience";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					JSONArray jArray = jsonObject.getJSONArray(str_key);
					if (jArray != null && jArray.length() > 0) {
						for (int i = 0; i < jArray.length(); i++) {
							EduExperience personInfo=new EduExperience() ;
							personInfo.initWithJson(jArray.getJSONObject(i));
							listEduExperience.add(personInfo);
						}
					}
				}
				
				str_key = "listBasePersonInfo";
				if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
					JSONArray jArray = jsonObject.getJSONArray(str_key);
					if (jArray != null && jArray.length() > 0) {
						for (int i = 0; i < jArray.length(); i++) {
							PersonInfo personInfo=new PersonInfo() ;
							personInfo.initWithJson(jArray.getJSONObject(i));
							listBasePersonInfo.add(personInfo);
						}
					}
				}
				
				
	}
}
