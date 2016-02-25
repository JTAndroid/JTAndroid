package com.tr.ui.tongren.model.project;

import java.io.Serializable;
import java.sql.Timestamp;

import com.tr.model.model.User;


/**
 * 组织成员表
 * @author yanweiqi
 */
public class OrganizationMember implements Serializable{
	
	private static final long serialVersionUID = 2209748151579526684L;
	private long id;
	private long userId;         
	private long organizationId;  
	private String createTime;
	private String updateTime;
	private long createrId;    
	private int joinWay;
	private String applyTime;
	private int status;
	private User user;
	private OrganizationRole organizationRole;
	private OrganizationDep organizationDep;
	private Organization organization;
	
	private String addTime;
	private long depId;
	private String logo;
	private long memberId;
	private long roleId;
	private String roleName;
	private String userName;
	
	
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	public long getDepId() {
		return depId;
	}
	public void setDepId(long depId) {
		this.depId = depId;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public long getMemberId() {
		return memberId;
	}
	public void setMemberId(long memberId) {
		this.memberId = memberId;
	}
	public long getRoleId() {
		return roleId;
	}
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
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
	public long getCreaterId() {
		return createrId;
	}
	public void setCreaterId(long createrId) {
		this.createrId = createrId;
	}
	public int getJoinWay() {
		return joinWay;
	}
	public void setJoinWay(int joinWay) {
		this.joinWay = joinWay;
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
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public OrganizationRole getOrganizationRole() {
		return organizationRole;
	}
	public void setOrganizationRole(OrganizationRole organizationRole) {
		this.organizationRole = organizationRole;
	}
	
	public OrganizationDep getOrganizationDep() {
		return organizationDep;
	}
	public void setOrganizationDep(OrganizationDep organizationDep) {
		this.organizationDep = organizationDep;
	}
	
	public Organization getOrganization() {
		return organization;
	}
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	
	
}
