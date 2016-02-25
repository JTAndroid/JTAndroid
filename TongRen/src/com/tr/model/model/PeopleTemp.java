package com.tr.model.model;

import java.util.ArrayList;
import java.util.List;

import com.utils.string.StringUtils;

/**
 * 人脉
 * @author wenbin
 *
 */
public class PeopleTemp extends BaseObject {

	public static final long serialVersionUID = -2592010496949013425L;

	/**主键*/
	public String id;//主键
	
	/********************基本信息start*****************************/
	/**姓名*/
	public ArrayList<PeopleName> peopleNameList;
	/**所在分组*/
	public ArrayList<PeopleGroupTemp> peopleGroupList;
	/**关联客户*/
	public ArrayList<PeopleCustomer> peopleCustomerList;
	/**性别：1-男，2-女，3-未知*/
	public int gender;
	/**头像*/
	public String portrait;
	/**备注*/
	public String remark;
	
	/********************基本信息end*****************************/
	
	
	/********************联系方式start*****************************/
	/**手机*/
	public ArrayList<PeopleContactComm> contactMobileList;
	/**固话*/
	public ArrayList<PeopleContactComm> contactFixedList;
	/**传真*/
	public ArrayList<PeopleContactComm> contactFaxList;
	/**邮箱*/
	public ArrayList<PeopleContactComm> contactMailList;
	/**主页*/
	public ArrayList<PeopleContactComm> contactHomeList;
	/**通讯*/
	public ArrayList<PeopleContactComm> contactCommunicationList;
	/**地址*/
	public ArrayList<PeopleAddress> contactAddressList;
	/**自定义字段*/
	public ArrayList<PeoplePersonalLine> personalLineList;
	/********************联系方式end*****************************/
	
	
	/********************个人情况start*****************************/

	/**民族：1-汉族，2-满族····名称*/  
	public String raceName;
	/**国籍：1-中国，2-美国····名称*/
	public String nationalityName;
	/**信仰：1-佛教，2-道教，3-基督教，4-天主教，5-犹太教，6-伊斯兰教，7-印度教，8-无神论  名称 */
	public String faithName;
	/**籍贯国  0 国内、1国外*/
	public String birthPlaceCountryName;
	/**籍贯省 名称*/
	public String birthPlaceStateName;
	/**籍贯市 名称*/
	public String birthPlaceCityName;
	/**籍贯县 名称*/
	public String birthPlaceCountyName;
//	/**民族：1-汉族，2-满族····*/
//	public Integer race;
//	/**国籍：1-中国，2-美国····*/
//	public Integer nationality;
//	/**信仰：1-佛教，2-道教，3-基督教，4-天主教，5-犹太教，6-伊斯兰教，7-印度教，8-无神论*/
//	public Integer faith;
//	/**籍贯省*/
//	public Integer birthPlaceState;
//	/**籍贯市*/
//	public Integer birthPlaceCity;
//	/**籍贯县*/
//	public Integer birthPlaceCounty;
	/**籍贯地址*/
	public String birthPlaceAddress;
	/**重要日期*/
	public ArrayList<PeopleImportantDate> importantDateList;
	/**社会关系*/
	public ArrayList<PeopleCommunityRelationship> communityRelationshipList;
	/**身体情况*/
	public String bodySituation;
	/**生活习惯*/
	public String habit;
	/**爱好*/
	public String hobby;
	/**自定义字段*/
	public ArrayList<PeoplePersonalLine> situationPersonalLineList;
	/********************个人情况end*****************************/
	
	
	/********************投融资需求start*****************************/
	/**投资需求*/
	public ArrayList<PeopleDemandCommon> investmentdemandList;
	/**融资需求*/
	public ArrayList<PeopleDemandCommon> financingdemandList;
	/**专家需求*/
	public ArrayList<PeopleDemandCommon> expertdemandList;
	/**专家身份*/
	public ArrayList<PeopleDemandCommon> expertIdentitydemandList;
	/********************投融资需求end*****************************/
	
	/**教育经历*/
	public ArrayList<PeopleEducation> educationList;
	
	/**工作经历*/
	public ArrayList<PeopleWorkExperience> workExperienceList;
	
	/**社会活动*/
	public ArrayList<PeopleSocialActivity> socialActivityList;

	/**会面情况*/
	public ArrayList<PeopleMetting> meetingList;
	
	/**自定义板块*/
	public ArrayList<PeoplePersonalPlate> personalPlateList;
	
	/**文件ID 上传附件用*/
	public String taskId;
	
	/**传附件用*/
	public List<FileIndex> fileIndexs;
	
	/**************** 人脉详情需要显示字段start*******************/
	/**性别：1-男，2-女，3-未知*/
	public String genderName;
	
	/**************** 人脉详情需要显示字段end*******************/
	
	

	public String getBirthPlace(){
		if(StringUtils.isEmpty(birthPlaceCountryName)){
			return "";
		}
		
		StringBuffer sb=new StringBuffer("");
		if (!StringUtils.isEmpty(birthPlaceCountryName)) {

			if (birthPlaceCountryName.equals("0")) {
				if (!StringUtils.isEmpty(birthPlaceCountryName)) {

					String countryName = "";
					if (birthPlaceCountryName.equals("0")) {
						countryName = "中国";
					} else if (birthPlaceCountryName.equals("1")) {
						countryName = "国外";
					}

					sb.append(countryName);
					sb.append(" ");
				}
				if (!StringUtils.isEmpty(birthPlaceStateName)) {
					sb.append(birthPlaceStateName);
					sb.append(" ");
				}
				if (!StringUtils.isEmpty(birthPlaceCityName)) {
					sb.append(birthPlaceCityName);
					sb.append(" ");
				}
				if (!StringUtils.isEmpty(birthPlaceCountyName)) {
					sb.append(birthPlaceCountyName);
					sb.append(" ");
				}
				if (!StringUtils.isEmpty(birthPlaceAddress)) {
					sb.append(birthPlaceAddress);
				}
			} else if (birthPlaceCountryName.equals("1")) {
				if (!StringUtils.isEmpty(birthPlaceCountryName)) {
					String countryName = "";
					if (birthPlaceCountryName.equals("0")) {
						countryName = "中国";
					} else if (birthPlaceCountryName.equals("1")) {
						countryName = "国外";
					}
					
					sb.append(countryName);
					sb.append(" ");
				}
				if (!StringUtils.isEmpty(birthPlaceStateName)) {
					sb.append(birthPlaceStateName);
				}
			}
		}
		return sb.toString();
	}
	
	public String getCommunityRelationshipListStr(){
		if(communityRelationshipList==null||communityRelationshipList.size()==0){
			return "";
		}
		
		StringBuffer sb=new StringBuffer("");
		for(PeopleCommunityRelationship peopleCommunityRelationship: communityRelationshipList){
			if(peopleCommunityRelationship.typeTag!=null){
				sb.append(peopleCommunityRelationship.typeTag.name);
				sb.append(":");
				sb.append(peopleCommunityRelationship.content);
			}
			if(communityRelationshipList.indexOf(peopleCommunityRelationship)!=communityRelationshipList.size()-1){//最后一个
				sb.append("\n");
			}
		}
		return sb.toString();
	}


}
