package com.tr.model.joint;

import java.io.Serializable;

/**
 * 生态对接实体类
 */
public class JointResource_org implements Serializable {

	private static final long serialVersionUID = 1L;
	/*
	 * "tags":"金桐网", "virtual":"0", "id":"259378", "tagsScores":"金桐网=8,",
	 * "area":Object{...}, "isListing":"", "shotName":"", "name":"金桐网",
	 * "reasons":"公司:金桐网,", "picLogo":"http://file.online.gintong.com",
	 * "industrys":"", "createUserId":"0"
	 */
	private String tags;
	private String virtual;
	private String id;
	private String tagsScores;
	private JointResource_org_area area;
	private String isListing;
	private String shotName;
	private String name;
	private String reasons;
	private String picLogo;
	private String industrys;
	private String createUserId;

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getVirtual() {
		return virtual;
	}

	public void setVirtual(String virtual) {
		this.virtual = virtual;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTagsScores() {
		return tagsScores;
	}

	public void setTagsScores(String tagsScores) {
		this.tagsScores = tagsScores;
	}

	public JointResource_org_area getArea() {
		return area;
	}

	public void setArea(JointResource_org_area area) {
		this.area = area;
	}

	public String getIsListing() {
		return isListing;
	}

	public void setIsListing(String isListing) {
		this.isListing = isListing;
	}

	public String getShotName() {
		return shotName;
	}

	public void setShotName(String shotName) {
		this.shotName = shotName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReasons() {
		return reasons;
	}

	public void setReasons(String reasons) {
		this.reasons = reasons;
	}

	public String getPicLogo() {
		return picLogo;
	}

	public void setPicLogo(String picLogo) {
		this.picLogo = picLogo;
	}

	public String getIndustrys() {
		return industrys;
	}

	public void setIndustrys(String industrys) {
		this.industrys = industrys;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

}
