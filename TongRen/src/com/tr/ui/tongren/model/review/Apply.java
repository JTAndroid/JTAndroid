package com.tr.ui.tongren.model.review;

import java.io.Serializable;
import java.util.List;

public class Apply implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String applicationNo;
	private String applyDate;
	private String applyId;
	private String applyRereason;
	private String applyUserName;
	private String endTime;
	private String id;
	private String organizationId;
	private String progress;//审核进度: 0 新申请 1 审核中（没有走到最后一步审核） 2 审核完毕
	private List<ApplyPerson> reviewDetail;
	private String reviewGenreId;
	private String reviewGenreName;
	private String reviewProcessId;
	private String startTime;
	private String status;//审核状态 0 审核中 1 撤回申请 2审核通过 3审核拒绝

	public String getApplicationNo() {
		return applicationNo;
	}

	public void setApplicationNo(String applicationNo) {
		this.applicationNo = applicationNo;
	}

	public String getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}

	public String getApplyId() {
		return applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public String getApplyRereason() {
		return applyRereason;
	}

	public void setApplyRereason(String applyRereason) {
		this.applyRereason = applyRereason;
	}

	public String getApplyUserName() {
		return applyUserName;
	}

	public void setApplyUserName(String applyUserName) {
		this.applyUserName = applyUserName;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
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

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		this.progress = progress;
	}

	public List<ApplyPerson> getReviewDetail() {
		return reviewDetail;
	}

	public void setReviewDetail(List<ApplyPerson> reviewDetail) {
		this.reviewDetail = reviewDetail;
	}

	public String getReviewGenreId() {
		return reviewGenreId;
	}

	public void setReviewGenreId(String reviewGenreId) {
		this.reviewGenreId = reviewGenreId;
	}

	public String getReviewGenreName() {
		return reviewGenreName;
	}

	public void setReviewGenreName(String reviewGenreName) {
		this.reviewGenreName = reviewGenreName;
	}

	public String getReviewProcessId() {
		return reviewProcessId;
	}

	public void setReviewProcessId(String reviewProcessId) {
		this.reviewProcessId = reviewProcessId;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
