package com.tr.ui.organization.model.government;

import java.io.Serializable;
import java.util.List;

import com.tr.ui.organization.model.Relation;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;
/**
* <p>Title: DepartMentsInfo.java<／p> 
* <p>Description: 职能部门详情<／p> 
* @author wfl
* @date 2015-1-6 
* @version 1.0
 */
public class DepartMentsInfo implements Serializable {

	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;
	public long id;//主键
	public String departName;//职能部门名称
	public String address;//地址
	public String website;//网址
	public String phone;//电话
	public String fax;//传真
	public List<Relation> list;//主要领导
	public List<CustomerPersonalLine> propertyList; //自定义属性
	
	
}
