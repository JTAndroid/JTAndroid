package com.tr.ui.people.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 标签和人的关系表
 */
public class PersonTagRelation implements Serializable {
	/** 
	* 标签ID 
	*/ 
	public Long tagId; 
	/** 
	* 用户ID 
	*/ 
	public Long userId; 
	/** 
	* 标签名称 
	*/ 
	public String tagName; 

	public Long num ;


}
