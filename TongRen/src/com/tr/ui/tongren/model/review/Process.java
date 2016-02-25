package com.tr.ui.tongren.model.review;

import java.io.Serializable;
import java.util.List;

public class Process implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String createId;
	private String createTime;
	private String description;
	private List<ProcessType> genreList;
	private String id;
	private List<ProcessPerson> objectList;
	private String organizationId;
	private String reviewName;
	private String reviewNo;
	private String updateTime;

	public List<ProcessType> getGenreList() {
		return genreList;
	}

	public void setGenreList(List<ProcessType> genreList) {
		this.genreList = genreList;
	}

	public List<ProcessPerson> getObjectList() {
		return objectList;
	}

	public void setObjectList(List<ProcessPerson> objectList) {
		this.objectList = objectList;
	}

	public String getCreateId() {
		return createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getReviewName() {
		return reviewName;
	}

	public void setReviewName(String reviewName) {
		this.reviewName = reviewName;
	}

	public String getReviewNo() {
		return reviewNo;
	}

	public void setReviewNo(String reviewNo) {
		this.reviewNo = reviewNo;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

}
