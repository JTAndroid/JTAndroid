package com.tr.ui.tongren.model.project;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * 组织角色
 * @author yanweiqi
 * @since 20150930
 */
public class OrganizationRole implements Serializable {

	private static final long serialVersionUID = -51528005506113088L;

	private long id;
	private String roleName;
	private String createTime;
	private String updateTime;
	private long createrId;
	private long organizationId;
	private String description;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
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
	public long getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(long organizationId) {
		this.organizationId = organizationId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
