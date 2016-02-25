package com.tr.ui.organization.firstpage;

import java.io.Serializable;
import java.util.List;
//编辑基本信息---编辑页面
public class BasicData implements Serializable{

	private static final long serialVersionUID = 1L;

	private String name;//编辑基本信息页面---组织简称
	private String industry;//编辑基本信息页面----行业
	private String email;//编辑基本信息页面----邮箱
	private String dial;//编辑基本信息页面---联系电话
	private String introdution;//编辑基本信息页面----组织简介
	private String type;//编辑基本信息页面----客户类型
	private String orgmsg;//编辑基本信息页面----上市信息
	private String orgnum;//编辑基本信息页面----证劵代码
	private List<String> industryIds;//行业id
	
	
	
	public List<String> getIndustryIds() {
		return industryIds;
	}
	public void setIndustryIds(List<String> industryIds) {
		this.industryIds = industryIds;
	}
	private String urlToSql;//编辑基本信息页面----头像url，相对路径
	private String avatar_urlComplete;//编辑基本信息页面----头像url，绝对路径
	
	public String getAvatar_urlComplete() {
		return avatar_urlComplete;
	}
	public void setAvatar_urlComplete(String avatar_urlComplete) {
		this.avatar_urlComplete = avatar_urlComplete;
	}
	public String getUrlToSql() {
		return urlToSql;
	}
	public void setUrlToSql(String urlToSql) {
		this.urlToSql = urlToSql;
	}
	public String getOrgnum(){
		return orgnum;
	}
	public void setOrgnum(String orgnum){
		this.orgnum = orgnum;
	}
	
	public String getOrgmsg(){
		return orgmsg;
	}
	public void setOrgmsg(String orgmsg){
		this.orgmsg = orgmsg;
	}
	
	public String getType(){
		return type;
	}
	public void setType(String type){
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDial() {
		return dial;
	}
	public void setDial(String dial) {
		this.dial = dial;
	}
	public String getIntrodution() {
		return introdution;
	}
	public void setIntrodution(String introdution) {
		this.introdution = introdution;
	}
	
	
}
