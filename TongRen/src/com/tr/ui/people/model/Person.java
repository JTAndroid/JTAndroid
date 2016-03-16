package com.tr.ui.people.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.tr.model.demand.ASSORPOK;


/**
 * 人脉对象 
 * 
 * @author gushi
 *
 */
public class Person  implements Serializable {
	public static final long serialVersionUID = 6123642579309829629L;
	public static final int PARENTTYPE_CONTACT = 1;

	public static enum ModelType {
		BASIC_INFO(10), CONTACT(11), SITUATION(12), INVESTMENT_INTENTION(13), FINANCING_INTENTION(
				14), EXPERTS_DEMAND(15), EXPERTS_IDENTITY(16), EDUCAT_EXPERIEN(
				17), WORK_EXPERIEN(18), SOCIAL_ACTIVITIES(19), CUSTOM(99);
		public Integer code;

		ModelType(Integer code) {
			this.code = code;
		}
	}

	/**
	 * 人脉对象ID
	 */
	public Long id;

	public Long getId() {
		if (id == null)
			return 0L;
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 更新时间
	 */
	public Long updateTime;
	/**
	 * 创建时间
	 */
	public Long createTime;
	/**
	 * 创建人ID
	 */
	public Long createUserId;
	
	/** 转换用户id（用户转为人脉后的人脉对象此字段有值）*/
	public Long fromUserId;
	
	/**	来源人脉id：将其他用户通过大乐或转发等操作分享的人脉保存为自己的人脉时，此字段保存原来的人脉id	*/
	public Long fromPersonId;
	
	/**
	 * 原始用户ID
	 */
	public Long oldPeopleId;
	/**
	 * 职位
	 */
	public String position;
	/**
	 * 汉语拼音
	 */
	public String pinyin;
	/**
	 * 人脉附件ID
	 */
	public String taskId;
	/**
	 * 拼音缩写
	 */
	public String nameIndex;
	/**
	 * 拼音首字母
	 */
	public String nameFirst;
	/**
	 * 性别 0女 1男
	 */
	public Byte gender;
	/**
	 * 分类
	 */
	public String peopleType;
	/**
	 * 电话
	 */
	public String telephone;
	/**
	 * 邮箱
	 */
	public String email;
	/**
	 * 公司
	 */
	public String company;
	/**
	 * 现住地址
	 */
	public String address;
	/**
	 * 人脉头像地址
	 */
	
	public String portrait;
	public String country;  // 0 国内// 1-国外
	/**
	 * 所在城市
	 */
	public String locationCity;
	/**
	 * 所在省
	 */
	public String locationCounty;
	public String remark;
	/**
	 * 所在国家
	 */
	public String locationCountry;
	/**
	 * 职业代码ID
	 */
//	public Long careerId;
	/**
	 * 区域代码
	 */
	public Long regionId;
	/**
	 * 分类代码ID
	 */
	public Integer typeId;
	/**
	 * 自定义项
	 */
	public List<Basic> customTagList;
	/**
	 * 个人情况
	 */
	public List<PersonalInformation> personalInformationList;
	/**
	 * 投资意向
	 */
	public List<Intention> investmentdemandList;
	/**
	 * 融资意向
	 */
	public List<Intention> financingdemandList;
	/**
	 * 专家需求
	 */
	public List<Intention> expertdemandList;
	/**
	 * 专家身份
	 */
	public List<Intention> expertIdentityList;
	/**
	 * 教育
	 */
	public List<Education> educationList;
	/**
	 * 工作经历
	 */
	public List<WorkExperience> workExperienceList;
	/**
	 * 社会活动
	 */
	public List<SocialActivity> socialActivityList;
	/**
	 * 联系方式
	 */
	public List<Basic> contactInformationList;
	/**
	 * 人脉的名称
	 */
	public List<PersonName> peopleNameList;
	/**
	 * 人脉权限
	 */
	public PermIds permIds;
	//关联的对象
	public ASSORPOK asso ;
	/**0用户 1人脉*/
	public String virtual;
	
	/** 权限模块	*/
	public String modelType;
	
	/** 职能模块，一级和二级*/
	public String firstIndustryDirection;
	
	public int firstIndustryDirectionId;
	
	public String secondIndustryDirection;
	
	public int secondIndustryDirectionId;

	public PermIds getPermIds() {
		if (permIds == null)
			permIds = new PermIds();
		return permIds;
	}

	public void setPermIds(PermIds permIds) {
		this.permIds = permIds;
	}

	public String getModelType() {
		if (modelType == null)
			return "";
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	/**
	 * @return 是否是系统用户对应的用于资料保存的人脉对象
	 */
	public boolean isUser() {
		if (null != this.getFromUserId() && null != this.getCreateUserId()
				&& this.getCreateUserId().equals(this.getFromUserId())) {
			return true;
		}
		return false;
	}

	

	public Long getCreateUserId() {
		if (createUserId == null)
			return 0L;
		return createUserId;
	}


	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	public Long getFromUserId() {
		if (fromUserId == null)
			return 0L;
		return fromUserId;
	}

	public void setFromUserId(Long fromUserId) {
		this.fromUserId = fromUserId;
	}

	public Long getOldPeopleId() {
		if (oldPeopleId == null)
			return 0L;
		return oldPeopleId;
	}

	public void setOldPeopleId(Long oldPeopleId) {
		this.oldPeopleId = oldPeopleId;
	}

	public String getPosition() {
		if (position == null)
			return "";
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getPinyin() {
		if (pinyin == null)
			return "";
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getTaskId() {
		if (taskId == null)
			return "";
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getNameIndex() {
		if (nameIndex == null)
			return "";
		return nameIndex;
	}

	public void setNameIndex(String nameIndex) {
		this.nameIndex = nameIndex;
	}

	public String getNameFirst() {
		if (nameFirst == null)
			return "";
		return nameFirst;
	}

	public void setNameFirst(String nameFirst) {
		this.nameFirst = nameFirst;
	}

	public Byte getGender() {
		if (gender == null)
			return 0;
		return gender;
	}

	public void setGender(Byte gender) {
		this.gender = gender;
	}

	public String getTelephone() {
		if (telephone == null)
			return "";
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getEmail() {
		if (email == null)
			return "";
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCompany() {
		if (company == null)
			return "";
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getAddress() {
		if (address == null)
			return "";
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPortrait() {
		if (portrait == null)
			return "";
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public String getLocationCity() {
		if (locationCity == null)
			return "";
		return locationCity;
	}

	public void setLocationCity(String locationCity) {
		this.locationCity = locationCity;
	}

	public String getLocationCounty() {
		if (locationCounty == null)
			return "";
		return locationCounty;
	}

	public void setLocationCounty(String locationCounty) {
		this.locationCounty = locationCounty;
	}

	public String getLocationCountry() {
		if (locationCountry == null)
			return "";
		return locationCountry;
	}

	public void setLocationCountry(String locationCountry) {
		this.locationCountry = locationCountry;
	}

	public List<Basic> getCustomTagList() {
		if (customTagList == null)
			customTagList = new ArrayList<Basic>();
		return customTagList;
	}

	public void setCustomTagList(List<Basic> customTagList) {
		this.customTagList = customTagList;
	}

	public List<PersonalInformation> getPersonalInformationList() {
		if (personalInformationList == null)
			personalInformationList = new ArrayList<PersonalInformation>();
		return personalInformationList;
	}

	public void setPersonalInformationList(
			List<PersonalInformation> personalInformationList) {
		this.personalInformationList = personalInformationList;
	}

	public List<Intention> getInvestmentdemandList() {
		if (investmentdemandList == null)
			investmentdemandList = new ArrayList<Intention>();
		return investmentdemandList;
	}

	public void setInvestmentdemandList(List<Intention> investmentdemandList) {
		this.investmentdemandList = investmentdemandList;
	}

	public List<Intention> getFinancingdemandList() {
		if (financingdemandList == null)
			financingdemandList = new ArrayList<Intention>();
		return financingdemandList;
	}

	public void setFinancingdemandList(List<Intention> financingdemandList) {
		this.financingdemandList = financingdemandList;
	}

	public List<Intention> getExpertdemandList() {
		if (expertdemandList == null)
			expertdemandList = new ArrayList<Intention>();
		return expertdemandList;
	}

	public void setExpertdemandList(List<Intention> expertdemandList) {
		this.expertdemandList = expertdemandList;
	}

	public List<Intention> getExpertIdentityList() {
		if (expertIdentityList == null)
			expertIdentityList = new ArrayList<Intention>();
		return expertIdentityList;
	}

	public void setExpertIdentityList(List<Intention> expertIdentityList) {
		this.expertIdentityList = expertIdentityList;
	}

	public List<Education> getEducationList() {
		if (educationList == null)
			educationList = new ArrayList<Education>();
		return educationList;
	}

	public void setEducationList(List<Education> educationList) {
		this.educationList = educationList;
	}

	public List<WorkExperience> getWorkExperienceList() {
		if (workExperienceList == null)
			workExperienceList = new ArrayList<WorkExperience>();
		return workExperienceList;
	}

	public void setWorkExperienceList(List<WorkExperience> workExperienceList) {
		this.workExperienceList = workExperienceList;
	}

	public List<SocialActivity> getSocialActivityList() {
		if (socialActivityList == null)
			socialActivityList = new ArrayList<SocialActivity>();
		return socialActivityList;
	}

	public void setSocialActivityList(List<SocialActivity> socialActivityList) {
		this.socialActivityList = socialActivityList;
	}

	public List<Basic> getContactInformationList() {
		if (contactInformationList == null)
			contactInformationList = new ArrayList<Basic>();
		return contactInformationList;
	}

	public void setContactInformationList(List<Basic> contactInformationList) {
		this.contactInformationList = contactInformationList;
	}

	public List<PersonName> getPeopleNameList() {
		if (peopleNameList == null)
			peopleNameList = new ArrayList<PersonName>();
		return peopleNameList;
	}

	public void setPeopleNameList(List<PersonName> peopleNameList) {
		this.peopleNameList = peopleNameList;
	}

//	public Long getCareerId() {
//		if (careerId == null)
//			return 0L;
//		return careerId;
//	}
//
//	public void setCareerId(Long careerId) {
//		this.careerId = careerId;
//	}

	public Long getRegionId() {
		if (regionId == null)
			return 0L;
		return regionId;
	}

	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}

	public String getPeopleType() {
		if (peopleType == null)
			return "";
		return peopleType;
	}

	public void setPeopleType(String peopleType) {
		this.peopleType = peopleType;
	}

	public Integer getTypeId() {
		if (typeId == null)
			return 0;
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public void setFromPersonId(long fromPersonId) {
		this.fromPersonId = fromPersonId;
	}
	
	public void setFromPersonId() {
		this.fromPersonId = id;
	}

	public String getFirstIndustryDirection() {
		return firstIndustryDirection;
	}

	public void setFirstIndustryDirection(String firstIndustryDirection) {
		this.firstIndustryDirection = firstIndustryDirection;
	}

	public int getFirstIndustryDirectionId() {
		return firstIndustryDirectionId;
	}

	public void setFirstIndustryDirectionId(int firstIndustryDirectionId) {
		this.firstIndustryDirectionId = firstIndustryDirectionId;
	}

	public String getSecondIndustryDirection() {
		return secondIndustryDirection;
	}

	public void setSecondIndustryDirection(String secondIndustryDirection) {
		this.secondIndustryDirection = secondIndustryDirection;
	}

	public int getSecondIndustryDirectionId() {
		return secondIndustryDirectionId;
	}

	public void setSecondIndustryDirectionId(int secondIndustryDirectionId) {
		this.secondIndustryDirectionId = secondIndustryDirectionId;
	}
	
}
