package com.tr.ui.tongren.model.project;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.utils.time.TimeUtil;


/**
 * 项目创建
 * @author Administrator
 *
 */
public class Project implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private long id; //主键
	
	private String name;//项目名称

	private String introduction;//项目介绍
	
	private long validityStartTime;//有效开始时间
	
	public long validityEndTime;//有效结束时间     
	
	private int cycle;//项目周期,单位天
	
	private String area;//地区
	
	private String industry;//行业
	
	private String document; //文档
	
	private long createrId;//创建人id
	
	private long createTime;//创建日期
	
	private long updateTime;//更新时间
	
	private long organizationId; //创建者所属组织
	public String organizationName; //创建者所属组织名称
	public List<ProjectFile> resourceAttachments;
	
	private double remuneration;//项目酬劳
	
	private int status; //状态 0草稿、1正式

	
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	
	public int getCycle() {
		return cycle;
	}

	public void setCycle(int cycle) {
		this.cycle = cycle;
	}
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}
	

	public String getCreateTime() {
		String createTimeeStr =  TimeUtil.TimeMillsToString(createTime);
		return createTimeeStr;
	}

	public long getCreaterId() {
		return createrId;
	}

	public void setCreaterId(long createrId) {
		this.createrId = createrId;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		String updateTimeStr = TimeUtil.TimeMillsToString(updateTime);
		return updateTimeStr;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	public long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}
	
	public double getRemuneration() {
		return remuneration;
	}

	public void setRemuneration(double remuneration) {
		this.remuneration = remuneration;
	}
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	public String getValidityStartTime() {
		String validityStartTimeStr = TimeUtil.TimeMillsToString(validityStartTime);
		return validityStartTimeStr;
	}

	public void setValidityStartTime(long validityStartTime) {
		this.validityStartTime = validityStartTime;
	}
	public String getValidityEndTime() {
		String validityEndTimeStr = TimeUtil.TimeMillsToString(validityEndTime);
		return validityEndTimeStr;
	}

	public void setValidityEndTime(long validityEndTime) {
		this.validityEndTime = validityEndTime;
	}
	
}
