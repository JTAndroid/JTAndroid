package com.tr.model.joint;

import java.io.Serializable;

/**
 * 生态对接实体类
 */
public class JointResource_kno implements Serializable{

	private static final long serialVersionUID = 1L;
	/*"tags":"北京",
    "id":"1927613",
    "tagsScores":"北京=10",
    "title":"北京控烟首日：1人被罚、146家单位被责令整改",
    "knowledgeType":"1",
    "createUserId":"0"*/
	
	private String tags;
	private String id;
	private String tagsScores;
	private String title;
	private String knowledgeType;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getKnowledgeType() {
		return knowledgeType;
	}

	public void setKnowledgeType(String knowledgeType) {
		this.knowledgeType = knowledgeType;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

}
