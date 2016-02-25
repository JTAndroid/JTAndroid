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

import com.tr.model.model.PeopleCustomer;
import com.tr.model.model.PeopleGroupTemp;
import com.tr.model.model.PeopleSelectTag;
import com.tr.model.model.PeopleTemp;
import com.tr.model.model.PeopleWorkExperience;
import com.tr.model.model.User;
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
public class JTContact2 implements Serializable{

	public boolean isOnline=false;
	public boolean isOffline=false;
	public int friendState=0;
	public boolean isWorkmate=false;
	public String fromDes="";
	public String image="";
	
	private PeopleTemp people=new PeopleTemp();
	private User user=new User();
	
	
	ArrayList<JTFile2>  jtFileList=new ArrayList<JTFile2>();
	ArrayList<PeopleSelectTag>  myPeopleSelectTagList=new ArrayList<PeopleSelectTag>();
	ArrayList<PeopleGroupTemp>  myPeopleGroupTempList=new ArrayList<PeopleGroupTemp>();
	ArrayList<PeopleCustomer>  myPeopleCustomerList=new ArrayList<PeopleCustomer>();
	
	public JTContact2(){
	}
	
	public String getID(){
		String uid="";
		if(user==null){
			uid= people.id;
		}else{
			uid =user.id;
		}
		if(uid==null){
			uid="";
		}
		return uid;
	}
	
	public ArrayList<JTFile> getJTFileList(){
		ArrayList<JTFile> jtfiles=new ArrayList<JTFile>();
		if(jtFileList!=null){
			for(JTFile2 jTFile2:jtFileList){
				jtfiles.add(jTFile2.toJTfile());
			}
		}
		return  jtfiles;
	}
	
	
	public String getName(){
		if(user==null){
			if(people.peopleNameList!=null&&people.peopleNameList.size()!=0){
				return people.peopleNameList.get(0).name;
			}
			return "";
		}else{
			return user.name;
		}
	}
	
	public String getSex(){
		//：1-男，2-女，3-未知*
		int gender=0;
		if(user==null){
			if(people!=null){
				gender=people.gender;
			}
		}else{
			gender=user.sex;
		}
		if(gender==1){
			return "先生";
		}else if(gender==2){
			return "女士";
		}else{
			return "";
		}
		
	}
	
	public String getCompany(){
		if(user==null){
			if(people.workExperienceList!=null&&people.workExperienceList.size()!=0){
				for(PeopleWorkExperience peopleWorkExperience:people.workExperienceList){
					if(peopleWorkExperience.currentStatus){
						return peopleWorkExperience.company;
					}
				}
			}
			return "";
		}else{
			return user.companyName;
		}
	}
	
	
	
	public String getUserJob(){
		if(user==null){
			if(people.workExperienceList!=null&&people.workExperienceList.size()!=0){
				for(PeopleWorkExperience peopleWorkExperience:people.workExperienceList){
					if(peopleWorkExperience.currentStatus){
						return peopleWorkExperience.position;
					}
				}
			}
			return "";
		}else{
			return user.companyJob;
		}
	}
	
	
	
	public PeopleTemp getPeople(){
		return people;
	}
	
	public void setPeople(PeopleTemp people){
		this.people=people;
	}
	
	public User getUser(){
		return user;
	}
	/**转换为标准的JTFile对象*/
	public JTFile toJTfile(){
		JTFile mShareInfo=new JTFile();
		if(this.isOnline){
			mShareInfo.mType=JTFile.TYPE_JTCONTACT_ONLINE;
		}else {
			mShareInfo.mType=JTFile.TYPE_JTCONTACT_OFFLINE;
		}
		mShareInfo.mTaskId=getID();
		mShareInfo.mFileName=getName();
		mShareInfo.mSuffixName=getCompany();
		mShareInfo.mUrl=getIconUrl();
		mShareInfo.reserved1=getUserJob();
		return mShareInfo;
	}
	
	public String getRemark(){
		if(user!=null){
			return user.remark;
		}else{
			if(people!=null){
				return people.remark;
			}
		}
		return "";
	}
	
	public String getPeopleGroupList(){
		String returnData="";
		if(people.peopleGroupList!=null){
			for(PeopleGroupTemp peopleGroupTemp: people.peopleGroupList ){
				returnData+=peopleGroupTemp.name;
				returnData+=" ";
			}
		}
		return returnData;
	}
	
	public String getPeopleCustomerList(){
		String returnData="";
		if(people.peopleCustomerList!=null){
			for(PeopleCustomer peopleGroupTemp:  people.peopleCustomerList){
				returnData+=peopleGroupTemp.name;
				returnData+=" ";
			}
		}
		return returnData;
	}
	
	public String getUserAddress(){
		if(null == user){
			return null;
		}
		return user.getAddress();
	}
	
	
//	public Connections toConnections(){
//		Connections connections=new Connections();
//		connections.mType=Connections.type_persion;
//		
//		connections.setID(this.mId);
//		if(!StringUtils.isEmpty(this.mName)){
//			connections.setName(this.mName);
//			String pinyinName=FrgConnections.converterToFirstSpell(this.mName);
//			if(!StringUtils.isEmpty(pinyinName)){
//				connections.setCharName(pinyinName.charAt(0));
//			}
//		}
//		connections.setImage(this.mimage);
//		connections.setOnline(this.misOnline);
//		connections.setFriendState(this.mfriendState);
//		if(listMobilePhone!=null&&listMobilePhone.size()>0){
//			if(!StringUtils.isEmpty(listMobilePhone.get(0).mmobile)){
//				connections.setPhone(listMobilePhone.get(0).mmobile);
//			}
//		}
//		if(listEmail!=null&&listEmail.size()>0){
//			if(!StringUtils.isEmpty(listEmail.get(0))){
//				connections.setEMail(listEmail.get(0));
//			}
//		}
//		return connections;
//	}
	
//	public JTFile toJTfile(){
//		JTFile mShareInfo=new JTFile();
//		if(this.misOnline){
//			mShareInfo.mType=JTFile.TYPE_JTCONTACT_ONLINE;
//		}else {
//			mShareInfo.mType=JTFile.TYPE_JTCONTACT_OFFLINE;
//		}
//		mShareInfo.mTaskId=mId+"";
//		mShareInfo.mFileName=mName;
//		if(this.misOnline){
//			mShareInfo.mSuffixName=mcompany;
//		}else{
//			if(listBasePersonInfo!=null&&listBasePersonInfo.size()>0){
//				for(int i=0;i<listBasePersonInfo.size();i++){
//					if(listBasePersonInfo.get(i).mtag.equals("单位名称")){
//						mShareInfo.mSuffixName=listBasePersonInfo.get(i).mvalue;
//						break;
//					}
//				}
//			}
//		}
//		//mShareInfo.mSuffixName=mcompany;
//	
//		mShareInfo.mUrl=mimage;
//		mShareInfo.reserved1=userJob;
//		return mShareInfo;
//	}
	
	public ArrayList<PeopleSelectTag> getMyPeopleSelectTagList() {
		return myPeopleSelectTagList;
	}

	public void setMyPeopleSelectTagList(
			ArrayList<PeopleSelectTag> myPeopleSelectTagList) {
		this.myPeopleSelectTagList = myPeopleSelectTagList;
	}

	public ArrayList<PeopleGroupTemp> getMyPeopleGroupTempList() {
		return myPeopleGroupTempList;
	}

	public void setMyPeopleGroupTempList(
			ArrayList<PeopleGroupTemp> myPeopleGroupTempList) {
		this.myPeopleGroupTempList = myPeopleGroupTempList;
	}

	public ArrayList<PeopleCustomer> getMyPeopleCustomerList() {
		return myPeopleCustomerList;
	}

	public void setMyPeopleCustomerList(
			ArrayList<PeopleCustomer> myPeopleCustomerList) {
		this.myPeopleCustomerList = myPeopleCustomerList;
	}
	
	public Connections toConnections(){
		Connections connections=new Connections();
		connections.type=Connections.type_persion;
		
		if(this.isOnline){
			connections.setID(user.id);
		}else{
			connections.setID(people.id);
		}
		
		if(!StringUtils.isEmpty(this.getName())){
			connections.setName(this.getName());
			String pinyinName=FrgConnections2.converterToFirstSpell(this.getName());
			if(!StringUtils.isEmpty(pinyinName)){
				connections.setCharName(pinyinName.charAt(0));
			}
		}
		connections.setImage(this.getIconUrl());
		connections.setOnline(this.isOnline);
		connections.setFriendState(this.friendState);
		
		String mobile="";
		if(this.isOnline&&user!=null){
			mobile=user.mobile;
			if(StringUtils.isEmpty(mobile)){
				if(people.contactMobileList!=null&&people.contactMobileList.size()!=0){
					mobile=people.contactMobileList.get(0).content;
				}
			}
		}else{
			if(people.contactMobileList!=null&&people.contactMobileList.size()!=0){
				mobile=people.contactMobileList.get(0).content;
			}
		}
		connections.setPhone(mobile);
		
		String email="";
		if(this.isOnline&&user!=null){
			email=user.email;
			if(StringUtils.isEmpty(email)){
				if(people.contactMailList!=null&&people.contactMailList.size()!=0){
					email=people.contactMailList.get(0).content;
				}
			}
		}else{
			if(people.contactMailList!=null&&people.contactMailList.size()!=0){
				email=people.contactMailList.get(0).content;
			}
		}
		connections.setEMail(email);
		return connections;
	}
	
//	public String getCompanyPosition(){
//		String returnData="";
//		
//		String company = getCompany();
//		if(!StringUtils.isEmpty(company)){
//			returnData+=company;
//		}
//		String userJob = getUserJob();
//		if(!StringUtils.isEmpty(userJob)){
//			returnData+=userJob;
//		}
//		return returnData;
//	}
	
	public String getIconUrl(){
		return image;
	}
	
	
	
	
	public void setIconUrlSave(String url){
		if(this.isOnline){
			if(user!=null){
				user.picPath = (url);
			}
			
		}else {
			if(people!=null){
				people.portrait = (url);
			}
		}
	}
	
	public void setIconUrl(String url){
		this.image=url;
	}
	
}
