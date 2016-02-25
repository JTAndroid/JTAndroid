package com.tr.ui.communities.model;

import java.io.Serializable;

/**
 * 社群通知(申请加入社群，转让社群)
 * 
 */
public class CommunityNotify implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4158221961998804059L;
	/**
	 * 主键.
	 */
	private Long id;
	/**
	 * “communityId”:"社群id",
	 */
	private Long communityId;
	/**
	 * 社群logo
	 */
	private String communityLogo;
	/**
	 * "communityName":"社群的名称",
	 */
	private String communityName;
	/**
	 * "applicantId":"申请人id",
	 */
	private Long applicantId;
	/**
	 * "applicantName":"申请人姓名",
	 */
	private String applicantName;
	/**
	 * "userLogo":"申请者的用户头像",
	 */
	private String userLogo;
	/**
	 * "attendType":"加入的方式：0邀请，1申请",
	 */
	private int attendType;
	/**
	 * "acceptStatus":"0未答复",
	 */
	private int acceptStatus;
	/**
	 * "applyReason":"申请加入社群的理由",
	 */
	private String applyReason;
	/**
	 * "noticeType":"通知类型：0加群 1转让群组",
	 */
	private int noticeType;
	/**
	 * "createdTime":“申请加入的时间”，
	 */
	private Long createdTime;
	/**
	 * "createdUserId":"创建者的id",
	 */
	private Long createdUserId;
	/**
	 * "createdUserName":"创建者的名称",
	 */
	private String createdUserName;
	/**
	 * "createdUserLogo":"创建者的头像",
	 */
	private String createdUserLogo;
	/**
	 * "applicantReadStatus":"申请者是否已读的标识：0未读，1已读",
	 */
	private int applicantReadStatus;
	/**
	 * "ownerReadStatus":"群主是否已读的标识：0未读，1已读"
	 */
	private int ownerReadStatus;
	
	private Long updatedTime;

	public Long getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Long updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Long getCommunityId() {
		return communityId;
	}

	public void setCommunityId(Long communityId) {
		this.communityId = communityId;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public Long getApplicantId() {
		return applicantId;
	}

	public void setApplicantId(Long applicantId) {
		this.applicantId = applicantId;
	}

	public String getApplicantName() {
		return applicantName;
	}

	public void setApplicantName(String applicantName) {
		this.applicantName = applicantName;
	}

	public String getUserLogo() {
		return userLogo;
	}

	public void setUserLogo(String userLogo) {
		this.userLogo = userLogo;
	}

	public int getAttendType() {
		return attendType;
	}

	public void setAttendType(int attendType) {
		this.attendType = attendType;
	}

	public int getAcceptStatus() {
		return acceptStatus;
	}

	public void setAcceptStatus(int acceptStatus) {
		this.acceptStatus = acceptStatus;
	}

	public String getApplyReason() {
		return applyReason;
	}

	public void setApplyReason(String applyReason) {
		this.applyReason = applyReason;
	}

	public int getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(int noticeType) {
		this.noticeType = noticeType;
	}

	public Long getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Long createdTime) {
		this.createdTime = createdTime;
	}

	public Long getCreatedUserId() {
		return createdUserId;
	}

	public void setCreatedUserId(Long createdUserId) {
		this.createdUserId = createdUserId;
	}

	public String getCreatedUserName() {
		return createdUserName;
	}

	public void setCreatedUserName(String createdUserName) {
		this.createdUserName = createdUserName;
	}

	public String getCreatedUserLogo() {
		return createdUserLogo;
	}

	public void setCreatedUserLogo(String createdUserLogo) {
		this.createdUserLogo = createdUserLogo;
	}

	public int getApplicantReadStatus() {
		return applicantReadStatus;
	}

	public void setApplicantReadStatus(int applicantReadStatus) {
		this.applicantReadStatus = applicantReadStatus;
	}

	public int getOwnerReadStatus() {
		return ownerReadStatus;
	}

	public void setOwnerReadStatus(int ownerReadStatus) {
		this.ownerReadStatus = ownerReadStatus;
	}

	public String getCommunityLogo() {
		return communityLogo;
	}

	public void setCommunityLogo(String communityLogo) {
		this.communityLogo = communityLogo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
