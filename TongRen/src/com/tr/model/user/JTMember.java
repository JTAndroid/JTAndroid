package com.tr.model.user;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tr.model.home.MIndustry;
import com.tr.model.obj.Connections;
import com.tr.model.obj.ConnectionsMini;
import com.tr.model.obj.EduExperience;
import com.tr.model.obj.InvestKeyword;
import com.tr.model.obj.JTContact;
import com.tr.model.obj.JTContactMini;
import com.tr.model.obj.KnowledgeMini;
import com.tr.model.obj.RequirementMini;
import com.tr.model.obj.WorkExperience;

/**
 * @ClassName: JTMember.java
 * @Description: 金桐会员对象
 * @Author leon
 * @Version v 1.0
 * @Date 2014-04-09
 * @LastEdit 2014-04-09
 */
public class JTMember implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public final static int UT_PERSON = 1; // 个人用户
	public final static int UT_ORGANIZATION = 2;// 机构用户

	public final static int ROLE_PERSON = 0; // 用户
	public final static int ROLE_ORGANIZATION = 2;// 机构用户

	// 个人信息
	public String mID = "";
	public String mNick = "";
	public String mUserName = "";
	public String mImage = "";
	public String mMobile = "";
	public String mEmail = "";
	public String mSinalogin = "";//sina微博账号
	public String mQqlogin = "";//QQ账号
	public boolean mMobileAuth = false;
	public boolean mEmailAuth = false;
	public int userStatus;// 用户状态 0:邮箱未验证 1:信息未完善 2已完善个人信息
	public int mRole = 0;
	public int mUserType = 1;
	/* 允许非好友浏览我的主页 1 不允许 2 允许 */
	public int homePageVisible;
	/* 允许非好友对我评价 1 不允许 2 允许 */
	public int evaluateVisible;
	/* 行业 */
	private List<MIndustry> listIndustry;

	// 投融资关键字
	private InvestKeyword outInvestKeyword = new InvestKeyword();
	private InvestKeyword InInvestKeyword = new InvestKeyword();

	// 关联的需求和知识等信息
	private List<RequirementMini> listRequirementMini = new ArrayList<RequirementMini>();
	private List<KnowledgeMini> listKnowledgeMini = new ArrayList<KnowledgeMini>();
	private List<Connections> listConnections = new ArrayList<Connections>();
	private List<WorkExperience> listWorkExperience = new ArrayList<WorkExperience>();
	private List<EduExperience> listEduExperience = new ArrayList<EduExperience>();

	// 个人或机构信息
	public Person mPerson = new Person();
	public OrganizationInfo mOrganizationInfo = new OrganizationInfo();

	public JTContact jTContact = null;

	public JTContact getJTContact() {
		if (jTContact == null) {
			jTContact = new JTContact();
			jTContact.mId = mID;

			jTContact.mName = getmNick();

			if (mUserType == UT_PERSON && mPerson != null) {
				jTContact.mcompany = mPerson.mCompany;
			} else if (mUserType == UT_ORGANIZATION && mOrganizationInfo != null) {
				//
				jTContact.mcompany = mOrganizationInfo.mShortName;

			}

			jTContact.mimage = getImage();
			jTContact.misOnline = true;
			jTContact.misOffline = false;//
			jTContact.mfriendState = 0;//
			jTContact.misWorkmate = false;//
			jTContact.mfromDes = "";//
			jTContact.mfax = "";//
			jTContact.maddress = "";//
			jTContact.murl = "";//
			jTContact.mcomment = "";//
			jTContact.minInvestKeyword = InInvestKeyword;
			jTContact.moutInvestKeyword = outInvestKeyword;
			jTContact.listRequirementMini = (ArrayList<RequirementMini>) listRequirementMini;
			jTContact.listKnowledgeMini = (ArrayList<KnowledgeMini>) listKnowledgeMini;
			jTContact.listConnections = (ArrayList<Connections>) listConnections;
			jTContact.listWorkExperience = (ArrayList<WorkExperience>) listWorkExperience;
			jTContact.listEduExperience = (ArrayList<EduExperience>) listEduExperience;
		}
		return jTContact;
	}

	public JTMember() {

	}

	public String getImage() {
		return mImage;
	}

	public void initWithJson(JSONObject jsonObject) throws JSONException, MalformedURLException, ParseException {

		String str_key;

		// 金桐网会员ID
		str_key = "id";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mID = jsonObject.getString(str_key);
		}

		str_key = "image";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mImage = jsonObject.getString(str_key);
		}

		// 昵称
		str_key = "nick";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mNick = jsonObject.getString(str_key);
		}

		// 用户名
		str_key = "username";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mUserName = jsonObject.getString(str_key);
		}

		// 手机号
		str_key = "mobile";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mMobile = jsonObject.getString(str_key);
		}

		// 手机号是否验证
		str_key = "mobileAuth";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mMobileAuth = jsonObject.getBoolean(str_key);
		}

		// 当前用户的邮箱注册情况 TODO add by Sunjianan
		str_key = "userStatus";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			userStatus = jsonObject.getInt(str_key);
		}

		// 邮箱
		str_key = "email";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mEmail = jsonObject.getString(str_key);
		}

		// 邮箱是否验证
		str_key = "emailAuth";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mEmailAuth = jsonObject.getBoolean(str_key);
		}

		// 用户角色
		str_key = "role";
		mRole = jsonObject.optInt(str_key);

		// 用户类型
		str_key = "userType";
		if (jsonObject.has(str_key)) {
			mUserType = jsonObject.optInt(str_key);
		}
		;

		// 个人用户
		str_key = "person";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mPerson.initWithJson(jsonObject.getJSONObject(str_key));
		}
		;

		// 机构用户
		str_key = "organizationInfo";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mOrganizationInfo.initWithJson(jsonObject.getJSONObject(str_key));
		}
		;

		str_key = "homePageVisible";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			homePageVisible = jsonObject.getInt(str_key);
		}

		str_key = "evaluateVisible";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			evaluateVisible = jsonObject.getInt(str_key);
		}

		str_key = "listIndustry";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			// String jsonStr = jsonObject.getJSONArray(str_key).toString();
			// listIndustry = JSON.parseArray(jsonStr,MIndustry.class);
			Gson gson = new Gson();
			// 这里将lsit<javabean>转化成json字符串
			String jsonString = jsonObject.getJSONArray(str_key).toString();
			// 解析json字符串
			listIndustry = gson.fromJson(jsonString, new TypeToken<List<MIndustry>>() {
			}.getType());
		}
		//绑定QQ的数据标志 不为空即绑定
		str_key = "qqlogin";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mQqlogin = jsonObject.getString(str_key);
		}
		//绑定Sina微博的数据标志 不为空即为绑定
		str_key = "sinalogin";
		if (jsonObject.has(str_key) && !jsonObject.isNull(str_key)) {
			mSinalogin = jsonObject.getString(str_key);
		}

	}

	public List<MIndustry> getListIndustry() {
		return listIndustry;
	}

	public void setListIndustry(List<MIndustry> listIndustry) {
		this.listIndustry = listIndustry;
	}

	public String getmID() {
		return mID;
	}

	public void setmID(String mID) {
		this.mID = mID;
	}

	public String getmNick() {
		if (this.mUserType == UT_PERSON) {
			return mPerson.mName;
		} else {
			return mOrganizationInfo.mFullName;
		}
	}

	public void setNick(String name) {
		if (this.mUserType == UT_PERSON) {
			mPerson.mName = name;
		} else {
			mOrganizationInfo.mFullName = name;
		}
		this.mNick= name;
	}

	public void setImage(String image) {
		if (this.mUserType == UT_PERSON) {
			mPerson.mImage = image;
		} else {
			mOrganizationInfo.mLogo = image;
		}
		this.mImage = image;
	}

	public void setmNick(String mNick) {
		this.mNick = mNick;
	}

	public String getmUserName() {
		return mUserName;
	}

	public void setmUserName(String mUserName) {
		this.mUserName = mUserName;
	}

	public String getmMobile() {
		return mMobile;
	}

	public void setmMobile(String mMobile) {
		this.mMobile = mMobile;
	}

	public String getmEmail() {
		return mEmail;
	}

	public void setmEmail(String mEmail) {
		this.mEmail = mEmail;
	}
	public String getmSinalogin() {
		return mSinalogin;
	}
	
	public void setmSinalogin(String mSinalogin) {
		this.mSinalogin = mSinalogin;
	}
	public String getmQqlogin() {
		return mQqlogin;
	}
	
	public void setmQqlogin(String mQqlogin) {
		this.mQqlogin = mQqlogin;
	}

	public boolean ismMobileAuth() {
		return mMobileAuth;
	}

	public void setmMobileAuth(boolean mMobileAuth) {
		this.mMobileAuth = mMobileAuth;
	}

	public boolean ismEmailAuth() {
		return mEmailAuth;
	}

	public void setmEmailAuth(boolean mEmailAuth) {
		this.mEmailAuth = mEmailAuth;
	}

	public int getmRole() {
		return mRole;
	}

	public void setmRole(int mRole) {
		this.mRole = mRole;
	}

	public int getmUserType() {
		return mUserType;
	}

	public void setmUserType(int mUserType) {
		this.mUserType = mUserType;
	}

	public Person getmPerson() {
		return mPerson;
	}

	public void setmPerson(Person mPerson) {
		this.mPerson = mPerson;
	}

	public InvestKeyword getOutInvestKeyword() {
		return outInvestKeyword;
	}

	public void setOutInvestKeyword(InvestKeyword outInvestKeyword) {
		this.outInvestKeyword = outInvestKeyword;
	}

	public OrganizationInfo getmOrganizationInfo() {
		return mOrganizationInfo;
	}

	public void setmOrganizationInfo(OrganizationInfo mOrganizationInfo) {
		this.mOrganizationInfo = mOrganizationInfo;
	}

	public InvestKeyword getInInvestKeyword() {
		return InInvestKeyword;
	}

	public void setInInvestKeyword(InvestKeyword inInvestKeyword) {
		InInvestKeyword = inInvestKeyword;
	}

	public List<RequirementMini> getListRequirementMini() {
		return listRequirementMini;
	}

	public void setListRequirementMini(List<RequirementMini> listRequirementMini) {
		this.listRequirementMini = listRequirementMini;
	}

	public List<KnowledgeMini> getListKnowledgeMini() {
		return listKnowledgeMini;
	}

	public void setListKnowledgeMini(List<KnowledgeMini> listKnowledgeMini) {
		this.listKnowledgeMini = listKnowledgeMini;
	}

	public List<WorkExperience> getListWorkExperience() {
		return listWorkExperience;
	}

	public void setListWorkExperience(List<WorkExperience> listWorkExperience) {
		this.listWorkExperience = listWorkExperience;
	}

	public List<EduExperience> getListEduExperience() {
		return listEduExperience;
	}

	public void setListEduExperience(List<EduExperience> listEduExperience) {
		this.listEduExperience = listEduExperience;
	}

	public List<Connections> getListConnections() {
		return listConnections;
	}

	public void setListConnections(List<Connections> listConnections) {
		this.listConnections = listConnections;
	}

	public int getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}

	// 转为Connections对象
	public Connections toConnections() {
		Connections connections = new Connections();
		connections.type = mUserType;
		if (mUserType == UT_PERSON) {
			connections.jtContactMini = new JTContactMini();
			connections.jtContactMini.setId(mID);
			connections.jtContactMini.setImage(mImage);
			connections.jtContactMini.setName(mUserName);
		} else {
			connections.organizationMini = new OrganizationMini();
			connections.organizationMini.setId(mID);
			connections.organizationMini.setLogo(mImage);
			connections.organizationMini.setFullName(mUserName);
		}
		return connections;
	}

	// 转为ConnectionsMini对象
	public ConnectionsMini toConnectionsMini() {
		ConnectionsMini connsMini = new ConnectionsMini();
		connsMini.setId(mID);
		connsMini.setType(mUserType);
		connsMini.setName(mUserName);
		connsMini.setImage(mImage);
		return connsMini;
	}

	public int getHomePageVisible() {
		return homePageVisible;
	}

	public void setHomePageVisible(int homePageVisible) {
		this.homePageVisible = homePageVisible;
	}

	public int getEvaluateVisible() {
		return evaluateVisible;
	}

	public void setEvaluateVisible(int evaluateVisible) {
		this.evaluateVisible = evaluateVisible;
	}
}
