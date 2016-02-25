package com.tr.ui.tongren.model.project;

import java.io.Serializable;
import java.sql.Timestamp;



/**
 * 项目承接表
 * @author Administrator
 *
 */
public class Undertaken implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public long id; //主键
	
	public long projectId; //承接项目ID
	public String name ; 
	public long recipientId; //承接人
	public String organizationName ; //承接组织名称
	public long organizationId; //

	public Long startTime; //承接项目开始时间
	
	public Long endTime; //承接项目结束时间
	
	public int status; //0项目进行中、1完成、2、放弃、3已过期
	
	public long publishId; //发布人ID
	
	public long publishOrganizationId; //发布人所属组织ID
	
	public Publish publish;//项目相关发布和创建
	public String lDays ; //剩余天数
	
	/**
                        "recipientName":"承接人姓名",
                        "recipientPicUrl":"承接人头像",
                        "createProjectId":"项目创建人ID",
                        "createProjectName":"项目创建人姓名",
                        "createProjectPicUrl":"项目创建人头像"
	 * @return
	 */
	public String recipientName;
	public String recipientPicUrl;
	public String createProjectId;
	public String createProjectName;
	public String createProjectPicUrl;
	/** 确认合作接口所特有的属性*/
	public long recipientOrganizationId;
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}
	public long getRecipientId() {
		return recipientId;
	}

	public long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}

	public void setRecipientId(long recipientId) {
		this.recipientId = recipientId;
	}
	
	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	public long getPublishId() {
		return publishId;
	}

	public void setPublishId(long publishId) {
		this.publishId = publishId;
	}
	public long getPublishOrganizationId() {
		return publishOrganizationId;
	}

	public void setPublishOrganizationId(long publishOrganizationId) {
		this.publishOrganizationId = publishOrganizationId;
	}
	/**
	 * @return 返回 publish。
	 */
	public Publish getPublish() {
		return publish;
	}

	/**
	 * ---@param publish 要设置的 publish。
	 */
	public void setPublish(Publish publish) {
		this.publish = publish;
	}

	
}
