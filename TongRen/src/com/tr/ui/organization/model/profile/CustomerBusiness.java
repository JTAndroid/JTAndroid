package com.tr.ui.organization.model.profile;

import java.io.Serializable;
import java.util.List;

import com.tr.ui.organization.model.Relation;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;

/**
 * 业务分析
 * @author liubang
 *
 */
public class CustomerBusiness implements Serializable{
	/**
	 * 
	 */
	public static final long serialVersionUID = 5405815631394110664L;
	public String id;//序号
	public String type;//业务类别
	public String remark;//业务简介
	public Relation leader;//负责人
	public String phone;//联系方式
	public List<CustomerPersonalLine> propertyList; //自定义属性
	
}
