package com.tr.ui.organization.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.organization.model.profile.CustomerPersonalPlate;
import com.tr.ui.organization.model.profile.CustomerPhone;

public class CustomerProfileVo implements Serializable{

	/**
	 * 组织详情的请求类
	 */
	public static final long serialVersionUID = 1L;
	
	public Long customerId;
	public String name;
	public String shotName;
	public String type;
	public List<String> industrys = new ArrayList<String>();
	public String isListing;
	public String stockNum;
	public String linkMobile;
	public String linkEmail;
	public List<CustomerPhone> phoneList;//客户电话
	public String picLogo;//客户头像logo
	public String banner;//顶部图片路径
	public String discribe;//客户描述
	public List<Long> columns;
	public Long userId;//用户id
	public String auth;
	public String friends;//1  等待同意  2 是好友  3不是好友
	public long comeId;//来源组织id
	public long   createById;//创建者id
	public boolean black;//是否是黑名单
	public int userConfig;//用户设置  0对自己可见1 对好友可见2所有人可见
	public long loginUserId;//当前登陆用户id
	public String virtual;// 是否是组织 0 客户 1 用户注册组织 2 未注册的组织(或者是大数据推送过来的组织（主页底部不显示任何操作）)
	public ArrayList<CustomerPersonalPlate> personalPlateList;//自定义模块
	public ArrayList<CustomerPersonalLine> propertyList; //自定义属性
	public Area area;//客户所属地区
	@Override
	public String toString() {
		return "CustomerProfileVo [customerId=" + customerId + ", name=" + name
				+ ", shotName=" + shotName + ", type=" + type + ", industrys="
				+ industrys + ", isListing=" + isListing + ", stockNum="
				+ stockNum + ", linkMobile=" + linkMobile + ", linkEmail="
				+ linkEmail + ", phoneList=" + phoneList + ", picLogo="
				+ picLogo + ", banner=" + banner + ", discribe=" + discribe
				+ ", columns=" + columns + ", userId=" + userId + ", auth="
				+ auth + ", friends=" + friends + ", comeId=" + comeId
				+ ", createById=" + createById + ", black=" + black
				+ ", userConfig=" + userConfig + ", loginUserId=" + loginUserId
				+ ", virtual=" + virtual + ", personalPlateList="
				+ personalPlateList + ", area=" + area + "]";
	}
	
}
