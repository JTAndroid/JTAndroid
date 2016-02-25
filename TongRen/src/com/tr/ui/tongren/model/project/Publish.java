package com.tr.ui.tongren.model.project;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;


/**
 * 项目发布
 * @author Administrator
 *
 */
public class Publish implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private long id; //主键
	
	private String startDate; //发布起始日
	
	private String endDate; //发布结束日
	
	private long publisherId; //发布人id
	
	private long projectId; //项目id
	
	private int status; //0发布失败、1发布成功、2屏蔽发布、3已过期
	
	private long organizationId; //组织id
	
	private String createTime;  //创建时间
	
	private String updateTime;  //更新时间
	
	private Project project;       //项目
	
	private Set<Apply> applySet;   //申请

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPublisherId() {
		return publisherId;
	}

	public void setPublisherId(long publisherId) {
		this.publisherId = publisherId;
	}

	public long getProjectId() {
		return projectId;
	}

	public void setProjectId(long projectId) {
		this.projectId = projectId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Set<Apply> getApplySet() {
		return applySet;
	}

	public void setApplySet(Set<Apply> applySet) {
		this.applySet = applySet;
	}
	
	
}
