package com.tr.ui.tongren.model.project;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.tr.model.model.User;



/**
 * 项目申请
 * @author Administrator
 *
 */
public class Apply implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private long id; //主键
	
	public long proposerId; //申请人 user_id
	
	private long organizationId; //申请人所属组织ID
	
	private String  applyTime; //申请时间
	
	private long projectId; //申请项目ID
	
	private int status; //0申请失败、1审核中、2申请成功
	
	private long  reviewTime; //审核日期
	
	private long reviewerId; //审核人Id
	
	private String  completedTime; //完成时间
	
	private OrganizationMember  organizationMember;
	
	private User user;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
    
	public long getProposerId() {
		return proposerId;
	}

	public void setProposerId(long proposerId) {
		this.proposerId = proposerId;
	}

	public String getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(long reviewTime) {
		this.reviewTime = reviewTime;
	}

	public long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	public long getReviewerId() {
		return reviewerId;
	}

	public void setReviewerId(long reviewerId) {
		this.reviewerId = reviewerId;
	}

	public String getCompletedTime() {
		return completedTime;
	}

//	public void setCompletedTime(int days,Timestamp reviewTime) {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//		Date startDate = new Date(reviewTime.getTime());
//		Calendar calendar = new GregorianCalendar();
//		calendar.setTime(startDate);
//		calendar.add(Calendar.DAY_OF_MONTH, days);
//		Date endDate = calendar.getTime();
//		this.completedTime = Timestamp.valueOf(sdf.format(endDate));
//	}

	public OrganizationMember getOrganizationMember() {
		return organizationMember;
	}

	public void setOrganizationMember(OrganizationMember organizationMember) {
		this.organizationMember = organizationMember;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
