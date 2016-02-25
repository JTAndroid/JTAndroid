package com.tr.ui.tongren.model.project;

import java.io.Serializable;
import java.sql.Timestamp;


import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 组织部门
 * @author yanweiqi
 *
 */
public class OrganizationDep implements Serializable,Comparable<OrganizationDep> {

	private static final long serialVersionUID = 2556072002342132424L;
	private long id;
	private String name;
	private String createTime;
	private String updateTime;
	private long createrId;
	private String description;//部门描述
	private long organizationId;//部门组织Id
	private int status;//部门状态
	private long pid;//上级部门Id
	private String level;//部门等级   例 1、1-1、1-2
	
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
	public long getCreaterId() {
		return createrId;
	}
	public void setCreaterId(long createrId) {
		this.createrId = createrId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getPid() {
		return pid;
	}
	public void setPid(long pid) {
		this.pid = pid;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	@Override
	public int compareTo(OrganizationDep o) {
		//return level.compareTo(o.getLevel());
		int left_level  = Integer.valueOf(level.replace("_", ""));
		int right_level = Integer.valueOf(o.getLevel().replace("_", ""));
		if(left_level > right_level){
			return 1;
		}
		else if(left_level == right_level){
			return 0;
		}
		else {
			return -1;
		}
	}
}
