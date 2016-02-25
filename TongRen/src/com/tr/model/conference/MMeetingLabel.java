package com.tr.model.conference;

import java.io.Serializable;

public class MMeetingLabel implements Serializable{

	private static final long serialVersionUID = 4087262089509370152L;
	
	/**
	  	"id":"主键",
        "labelName":"标签名字",
        "createId":创建者ID",
        "createName":"创建者名字",
        "createTime":"创建时间"
	 */
	
	 /**
     * 标签主键       db_column: id 
     */	
	
	private java.lang.Long id;
    /**
     * 标签名字       db_column: label_name 
     */	
	private java.lang.String labelName;
    /**
     * 标签创建者ID       db_column: create_id 
     */	
	
	private java.lang.Long createId;
    /**
     * 标签创建者名字       db_column: create_Name 
     */	
	
	private java.lang.String createName;
    /**
     * 标签创建时间       db_column: create_time 
     */	
	private String createTime;
	public java.lang.Long getId() {
		return id;
	}
	public void setId(java.lang.Long id) {
		this.id = id;
	}
	public java.lang.String getLabelName() {
		return labelName;
	}
	public void setLabelName(java.lang.String labelName) {
		this.labelName = labelName;
	}
	public java.lang.Long getCreateId() {
		return createId;
	}
	public void setCreateId(java.lang.Long createId) {
		this.createId = createId;
	}
	public java.lang.String getCreateName() {
		return createName;
	}
	public void setCreateName(java.lang.String createName) {
		this.createName = createName;
	}
	public String getCreateTime() {
		if(createTime == null) 
			return "";
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
}
