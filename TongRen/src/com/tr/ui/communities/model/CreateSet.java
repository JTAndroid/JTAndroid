package com.tr.ui.communities.model;

import java.io.Serializable;

public class CreateSet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3796180581757613571L;
	/**
	 * 社群id 创建不用传.
	 */
	private long communityId;
	/**
	 * 设置加入社群权限:1是所有人 2申请加入. 默认传1.
	 */
	private int applayType;
	/**
	 * 群成员对外是否可见：1可见 2是不可见. 默认传1.
	 */
	private int memberShowType;
	/**
	 * 群活动对外是否可见：1可见 2是不可见. 默认传1.
	 */
	private int activityShowType;
	/**
	 * 群企业对外是否可见：1可见 2是不可见. 默认传1.
	 */
	private int companyShowType;
	/**
	 * 群知识对外是否可见：1可见 2是不可见. 默认传1.
	 */
	private int knowledgeShowType;
	/**
	 * 群需求对外是否可见：1可见 2是不可见. 默认传1.
	 */
	private int demandShowType;
	/**
	 * 隐身状态：1不隐身 2是隐身. 默认传1.
	 */
	private int communityShowType;

	/**
	 * 群人脉显示 1可见 2是不可见. 默认传1.
	 */
	private int peopleShowType;
	/**
	 * 创建者id.
	 */
	private Long createdUserId;
	/**
	 * 创建时间 创建时可以不传
	 */
	private long createdTime;
	/**
	 * 跟新时间 创建时可以不传
	 */
	private long updatedTime;

	public  CreateSet(){
		
	}
	public CreateSet(Long createdUserId){
		this.createdUserId=createdUserId;
		this.applayType=1;
		this.memberShowType=1;
		this.activityShowType=1;
		this.companyShowType=1;
		this.knowledgeShowType=1;
		this.demandShowType=1;
		this.communityShowType=1;
		this.peopleShowType=1;
	}
	
	public long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(long communityId) {
		this.communityId = communityId;
	}

	public int getApplayType() {
		return applayType;
	}

	public void setApplayType(int applayType) {
		this.applayType = applayType;
	}

	public int getMemberShowType() {
		return memberShowType;
	}

	public void setMemberShowType(int memberShowType) {
		this.memberShowType = memberShowType;
	}

	public int getActivityShowType() {
		return activityShowType;
	}

	public void setActivityShowType(int activityShowType) {
		this.activityShowType = activityShowType;
	}

	public int getCompanyShowType() {
		return companyShowType;
	}

	public void setCompanyShowType(int companyShowType) {
		this.companyShowType = companyShowType;
	}

	public int getKnowledgeShowType() {
		return knowledgeShowType;
	}

	public void setKnowledgeShowType(int knowledgeShowType) {
		this.knowledgeShowType = knowledgeShowType;
	}

	public int getDemandShowType() {
		return demandShowType;
	}

	public void setDemandShowType(int demandShowType) {
		this.demandShowType = demandShowType;
	}

	public int getCommunityShowType() {
		return communityShowType;
	}

	public void setCommunityShowType(int communityShowType) {
		this.communityShowType = communityShowType;
	}

	public int getPeopleShowType() {
		return peopleShowType;
	}

	public void setPeopleShowType(int peopleShowType) {
		this.peopleShowType = peopleShowType;
	}

	public Long getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(Long createdUserId) {
		this.createdUserId = createdUserId;
	}

	public long getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}

	public long getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(long updatedTime) {
		this.updatedTime = updatedTime;
	}

}
