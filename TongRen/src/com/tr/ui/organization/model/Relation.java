package com.tr.ui.organization.model;

import java.io.Serializable;
/**
 * 关联的人脉/客户
 * @author liubang
 *
 */
public class Relation implements Serializable{
	/**
	 * 
	 */
	public static final long serialVersionUID = 6300076440066851816L;
	/**
	 * 序号
	 */
	public int id =0;
	/**
	 * 关联人脉/客户名称
	 */
	public String relation;
	/**
	 * 关联人脉/客户的id
	 */
	public String relationId;
	/**
	 * 类型 0 人脉 1客户 2好友 3组织
	 */
	public int type =0;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public String getRelationId() {
		return relationId;
	}
	public void setRelationId(String relationId) {
		this.relationId = relationId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	@Override
	public String toString() {
		return "Relation [id=" + id + ", relation=" + relation
				+ ", relationId=" + relationId + ", type=" + type + "]";
	}
	
	
	
	
	
}
