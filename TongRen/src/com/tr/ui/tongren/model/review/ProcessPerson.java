package com.tr.ui.tongren.model.review;

import java.io.Serializable;

public class ProcessPerson implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String backupReviewUserId;
	private String createId;
	private String createTime;
	private String id;
	private String organizationId;
	private String reviewLevel;
	private String reviewProcess;
	private String reviewRoleId;
	private String reviewUserId;
	private String roleId;
	private String upateTime;

	public String getBackupReviewUserId() {
		return backupReviewUserId;
	}

	public void setBackupReviewUserId(String backupReviewUserId) {
		this.backupReviewUserId = backupReviewUserId;
	}

	public String getCreateId() {
		return createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getReviewLevel() {
		return reviewLevel;
	}

	public void setReviewLevel(String reviewLevel) {
		this.reviewLevel = reviewLevel;
	}

	public String getReviewProcess() {
		return reviewProcess;
	}

	public void setReviewProcess(String reviewProcess) {
		this.reviewProcess = reviewProcess;
	}

	public String getReviewRoleId() {
		return reviewRoleId;
	}

	public void setReviewRoleId(String reviewRoleId) {
		this.reviewRoleId = reviewRoleId;
	}

	public String getReviewUserId() {
		return reviewUserId;
	}

	public void setReviewUserId(String reviewUserId) {
		this.reviewUserId = reviewUserId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getUpateTime() {
		return upateTime;
	}

	public void setUpateTime(String upateTime) {
		this.upateTime = upateTime;
	}

}
