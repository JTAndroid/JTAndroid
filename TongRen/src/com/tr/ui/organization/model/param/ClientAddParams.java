package com.tr.ui.organization.model.param;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.tr.ui.organization.model.CustomerTag;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
import com.tr.ui.organization.model.profile.CustomerPersonalPlate;
import com.tr.ui.organization.model.profile.CustomerPhone;

public class ClientAddParams implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Long customerId;
	public String name;
	public String shotName;
	public String type;
	public List<String> industrys;
	public ArrayList<String> industryIds;//行业id，多个行业 
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
//	public boolean black;//是否是黑名单
//	public int userConfig;//用户设置  0对自己可见1 对好友可见2所有人可见
//	public long loginUserId;//当前登陆用户id
	// 自定义板块 
	public List<CustomerPersonalPlate> personalPlateList;
	 public List<CustomerPersonalLine> propertyList; //自定义属性
	public String virtual;//是否是组织 0 客户  1 用户注册组织 2 未注册的组织 
	public List<String> directory;//目录信息
	
	public List<CustomerTag> lableList;//客户标签
	public List<JsonObj> industryObj; //冗余行业备注
	@Override
	public String toString() {
		return "ClientAddParams [customerId=" + customerId + ", name=" + name
				+ ", shotName=" + shotName + ", type=" + type + ", industrys="
				+ industrys + ", industryIds=" + industryIds + ", isListing="
				+ isListing + ", stockNum=" + stockNum + ", linkMobile="
				+ linkMobile + ", linkEmail=" + linkEmail + ", phoneList="
				+ phoneList + ", picLogo=" + picLogo + ", banner=" + banner
				+ ", discribe=" + discribe + ", columns=" + columns
				+ ", userId=" + userId + ", auth=" + auth + ", friends="
				+ friends + ", comeId=" + comeId + ", createById=" + createById
				+ ", personalPlateList=" + personalPlateList
				+ ", propertyList=" + propertyList + ", virtual=" + virtual
				+ ", directory=" + directory + ", lableList=" + lableList
				+ ", industryObj=" + industryObj + "]";
	}
	
	
	
	
	
}
