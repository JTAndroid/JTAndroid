package com.tr.ui.communities.model;

import java.io.Serializable;

public class CreatePrecondition implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6567569329036064788L;
	/**
	 * ”userInfoFlag“:详情是否完善 true已完善 false未完善
	 */
	private Boolean userInfoFlag;
	/**
	 * ”regFlag“:注册是不是满3天 true已满，false未满
	 */
	private Boolean regFlag;
	
	public Boolean getUserInfoFlag() {
		return userInfoFlag;
	}
	public void setUserInfoFlag(Boolean userInfoFlag) {
		this.userInfoFlag = userInfoFlag;
	}
	public Boolean getRegFlag() {
		return regFlag;
	}
	public void setRegFlag(Boolean regFlag) {
		this.regFlag = regFlag;
	}
	
}
