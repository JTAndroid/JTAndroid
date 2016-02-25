package com.tr.model.model;

import java.io.Serializable;

public class PeopleForm implements Serializable{
	
	public static final long serialVersionUID = 6330672715556532578L;

	
	/********************基本信息start*****************************/
	/**姓名*/
	public String peopleNameList="姓名";
	/**所在分组*/
	public String peopleGroupList;
	/**关联客户*/
	public String peopleCustomerList;
	/**性别：1-男，2-女，3-未知*/
	public String gender;
	/**头像*/
	public String portrait;
	/**备注*/
	public String remark;
	/********************基本信息end*****************************/
	
	
	/********************联系方式start*****************************/
	/**手机*/
	public String contactMobileList;
	/**固话*/
	public String contactFixedList;
	/**传真*/
	public String contactFaxList;
	/**邮箱*/
	public String contactMailList;
	/**主页*/
	public String contactHomeList;
	/**通讯*/
	public String contactCommunicationList;
	/**地址*/
	public String contactAddressList;
	/**自定义字段*/
	public String personalLineList;
	/********************联系方式end*****************************/
	
	
	/********************个人情况start*****************************/
//	/**民族：1-汉族，2-满族····*/
//	public String race;
//	/**国籍：1-中国，2-美国····*/
//	public String nationality;
//	/**信仰：1-佛教，2-道教，3-基督教，4-天主教，5-犹太教，6-伊斯兰教，7-印度教，8-无神论*/
//	public String faith;
//	/**籍贯省*/
//	public String birthPlaceState;
//	/**籍贯市*/
//	public String birthPlaceCity;
//	/**籍贯县*/
//	public String birthPlaceCounty;
	/**籍贯地址*/
	public String birthPlaceAddress;
	/**重要日期*/
	public String importantDateList;
	/**社会关系*/
	public String communityRelationshipList;
	/**身体情况*/
	public String bodySituation;
	/**生活习惯*/
	public String habit;
	/**爱好*/
	public String hobby;
	/**自定义字段*/
	public String situationPersonalLineList;
	/********************个人情况end*****************************/
	
	
	/********************投融资需求start*****************************/
	public String demand;
	/**投资需求*/
	public String investmentdemandList;
	/**融资需求*/
	public String financingdemandList;
	/**专家需求*/
	public String expertdemandList;
	/**专家身份*/
	public String expertIdentitydemandList;
	/********************投融资需求end*****************************/
	
	/**教育经历*/
	public String educationList;
	
	/**工作经历*/
	public String workExperienceList;
	
	/**社会活动*/
	public String socialActivityList;

	/**会面情况*/
	public String meetingList;
	
	/**自定义板块*/
	public String personalPlateList;
	
	/**文件ID 上传附件用*/
	public String taskId;
	
	
	/**************** 人脉详情需要显示字段start*******************/
	/**性别：1-男，2-女，3-未知*/
	public String genderName;
	/**民族：1-汉族，2-满族····名称*/  
	public String raceName;
	/**国籍：1-中国，2-美国····名称*/
	public String nationalityName;
	/**信仰：1-佛教，2-道教，3-基督教，4-天主教，5-犹太教，6-伊斯兰教，7-印度教，8-无神论  名称 */
	public String faithName;
	/**籍贯省 名称*/
	public String birthPlaceStateName;
	/**籍贯市 名称*/
	public String birthPlaceCityName;
	/**籍贯县 名称*/
	public String birthPlaceCountyName;
	/**传附件用*/
	public String fileIndexs;
	
	/**************** 人脉详情需要显示字段end*******************/
	
	
}
