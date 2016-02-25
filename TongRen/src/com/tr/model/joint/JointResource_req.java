package com.tr.model.joint;

import java.io.Serializable;

public class JointResource_req implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/*"tags":"深圳中投华大资产管理有限公司,工商业,国内,北京,北京,国内,北京,北京,,",
    "id":"1258",
    "tagsScores":"深圳中投华大资产管理有限公司=8,工商业=5,国内=2,北京=2,北京=4,",
    "friends":"",
    "name":"深圳中投华大资产管理有限公司",
    "demandType":"1",
    "reasons":"需求名:深圳中投华大资产管理有限公司,行业:工商业,地区:国内,地区:北京,地区:北京,",
    "industry":"国内-北京-北京",
    "createUserId":"13363"*/

	private String tags;
	private String id;
	private String tagsScores;
	private String friends;
	private String name;
	private String demandType;
	private String reasons;
	private String industry;
	private String createUserId;

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
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

	public String getFriends() {
		return friends;
	}

	public void setFriends(String friends) {
		this.friends = friends;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDemandType() {
		return demandType;
	}

	public void setDemandType(String demandType) {
		this.demandType = demandType;
	}

	public String getReasons() {
		return reasons;
	}

	public void setReasons(String reasons) {
		this.reasons = reasons;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

}
