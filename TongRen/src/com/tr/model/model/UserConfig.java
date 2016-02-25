package com.tr.model.model;

import java.io.Serializable;




public class UserConfig implements Serializable {
	public static final long serialVersionUID = 1L; 
	public long id;//id 
	public User user;//会员Id 
	public Integer baseVisible;//基本信息 0对自己可见1 对好友可见2所有人可见 
	public Integer contactVisible;//联系方式 
	public Integer educationVisible;//教育经历可见性 
	public Integer jobVisible;//工作可见性 
	public Integer xqVisible;//投资意向可见性 
	public Integer zyVisible;//融资意向可见性 

	public Integer personalInfoVisible;//个人情况可见性 
	public Integer zjxqVisible;//专家需求可见性 
	public Integer socialaActVisible;//社会活动可见性 
	public Integer mettingVisible;//会面情况可见性 
	public Integer specialVisible;//专长是否可见 
	public String ctime;//创建时间 
	public String utime;//更新时间
	
	
}