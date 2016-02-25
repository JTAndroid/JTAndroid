package com.tr.ui.tongren.model.project;

import java.io.Serializable;
import java.sql.Timestamp;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 组织
 * @author yanweiqi
 */
public class Organization implements Serializable{
	
	private static final long serialVersionUID = 2209748151579526684L;
	private long id;
	private String name;         //组织名称
	private int classification;  //组织类型
	private String introduction; //组织简介
	private String logo;         //组织LOGO的taskId
	private String createTime;//组织创建时间
	private String updateTime;//更新时间
	private long createrId;      //创建者ID
	private int area;	//地区
	private int industry;//行业
	private int status ;//状态  0：正常   1：解散
	private int memberSize;// 成员个数
	private String path;
	
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
	
	public int getClassification() {
		return classification;
	}
	public void setClassification(int classification) {
		this.classification = classification;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	public long getCreaterId() {
		return createrId;
	}
	public void setCreaterId(long createrId) {
		this.createrId = createrId;
	}
	public int getArea() {
		return area;
	}
	public void setArea(int area) {
		this.area = area;
	}
	public int getIndustry() {
		return industry;
	}
	public void setIndustry(int industry) {
		this.industry = industry;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public int getMemberSize() {
		return memberSize;
	}

	public void setMemberSize(int memberSize) {
		this.memberSize = memberSize;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
	
}
