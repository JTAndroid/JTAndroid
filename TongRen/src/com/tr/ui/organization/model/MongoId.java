package com.tr.ui.organization.model;

import java.io.Serializable;
/**
 * 获取组织在mongo中的最新主键
 * <p>  </p>
 * @author liubang
 * @date 2014-12-18
 */
public class MongoId implements Serializable{
	/**
	 * 
	 */
	public static final long serialVersionUID = 8724959156505418442L;

	public String _id;
	
	public String name;
	
	public Long cid;

}
