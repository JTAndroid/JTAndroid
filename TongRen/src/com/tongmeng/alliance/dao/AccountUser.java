package com.tongmeng.alliance.dao;

public class AccountUser {
//	"id":账号ID,
//	"type":账号类型1:微信 2:支付宝,
//	"name":"实名认证姓名",
//	"account":"账号",
//	"isDefault":是否是默认账号
	private int id;
	private int type;
	private String name;
	private String account;
	private String isDefault;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	public AccountUser() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AccountUser(int id, int type, String name, String account,
			String isDefault) {
		super();
		this.id = id;
		this.type = type;
		this.name = name;
		this.account = account;
		this.isDefault = isDefault;
	}
	
}
