package com.tr.model.obj;

import net.sourceforge.pinyin4j.PinyinHelper;

import org.json.JSONException;
import org.json.JSONObject;

import com.utils.string.StringUtils;

/**
 * @ClassName: OrganizationMini.java 文档1.20
 * @author xuxinjian
 * @version V1.0
 * @Date 2014-3-28 上午7:54:17
 */
public class ConnectionsMini extends ResourceBase implements
		Comparable<ConnectionsMini> {

	private static final long serialVersionUID = 198328812344324422L;
	public final static int UT_PERSON = 1;// 个人
	public final static int UT_ORG = 2;// 机构

	private String id;

	/** 0用户好友 1机构组织 */
	private int type;
	private String image;
	private String name;

	/** add by SunJiaNan */
	private String firstIndex = "a";

	/** 昵称 */
	private String shortName;
	/** 公司名 */
	private String companyName;
	/** 职务 */
	private String companyJob;
	/** 好友状态 0=不是好友 or 1=好友等待中 or 2=已是好友 */
	private int friendState;
	private Boolean friendRelation;// 社群新加的friendRelation(是否是好友关系 true 是 false否
									// ； 默认认为不是好友)，用来表明是否是好友关系
	/**
	 * "loginTime":"登录时间", "joinTime":"加入时间",
	 */
	public String loginTime;
	public String joinTime;
	/**社群中成员是否被禁言 1否 2是 */
	public String talkStatus;
	
	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public String getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(String joinTime) {
		this.joinTime = joinTime;
	}

	public static ConnectionsMini createFactory(JSONObject jsonObject) {
		try {
			ConnectionsMini self = new ConnectionsMini();
			self.id = jsonObject.optString("id");
			self.type = jsonObject.optInt("type");
			self.image = jsonObject.optString("image");
			self.name = jsonObject.optString("name");
			self.shortName = jsonObject.optString("shortName");
			self.companyName = jsonObject.optString("companyName");
			self.companyJob = jsonObject.optString("companyJob");
			return self;
		} catch (Exception e) {
			return null;
		}
	}

	public JSONObject toJSONObject() throws JSONException {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("type", type);
		jsonObject.put("image", image);
		jsonObject.put("name", name);
		jsonObject.put("shortName", shortName);
		jsonObject.put("companyName", companyName);
		jsonObject.put("companyJob", companyJob);
		return jsonObject;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCompanyJob() {
		return companyJob;
	}

	public void setCompanyJob(String companyJob) {
		this.companyJob = companyJob;
	}

	public int getFriendState() {
		return friendState;
	}

	public void setFriendState(int friendState) {
		this.friendState = friendState;
	}

	public Boolean getFriendRelation() {
		return friendRelation;
	}

	public void setFriendRelation(Boolean friendRelation) {
		this.friendRelation = friendRelation;
	}

	public Connections toConnections() {
		Connections conns = new Connections();
		if (type == UT_PERSON) { // 个人
			conns.jtContactMini.id = id;
			conns.jtContactMini.name = name;
			conns.type = UT_PERSON;
		} else { // 机构
			conns.organizationMini.mID = id;
			conns.organizationMini.fullName = name;
			conns.type = UT_ORG;
		}
		return conns;
	}

	/**
	 * 获取首字母索引
	 * 
	 * @return
	 */
	public String getFirstIndex() {
		return obtainFirstIndex(name);
	}

	public void setFirstIndex(String firstIndex) {
		this.firstIndex = firstIndex;
	}

	@Override
	public int compareTo(ConnectionsMini another) {
		if (another == null || name == null) {
			return 0;
		}
		int result = obtainFirstIndex(name).compareTo(
				obtainFirstIndex(another.getName()));
		if (result == 0) {
			return this.name.compareTo(another.name);
		}
		return result;
	}

	/**
	 * 通过汉语截取首字母
	 * 
	 * @param name
	 * @return
	 */
	private String obtainFirstIndex(String name) {
		if (StringUtils.isEmpty(name) || name.length() == 0) {
			return "z";
		}
		char ch = name.toCharArray()[0];
		if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
			return ch + "";
		}
		String[] strs = (PinyinHelper.toHanyuPinyinStringArray(name
				.toCharArray()[0]));
		if (strs != null && strs.length > 0) {
			if (StringUtils.isEmpty(strs[0])) {
				return "z";
			} else {
				return strs[0].substring(0, 1);
			}
		}
		return "z";

	}

}
