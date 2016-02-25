package com.tr.ui.organization.model.profile;

import java.io.Serializable;
import java.util.List;


import com.tr.ui.organization.model.Relation;
import com.tr.ui.organization.model.profile.CustomerPersonalLine;

/**
 * 上市信息
 * @author liubang
 *
 */
public class CustomerListing implements Serializable{
	/**
	 * 
	 */
	public static final long serialVersionUID = 5572172113354767796L;
	public String id;
	public String beginTime;//上市时间
	public String shares;//股票发行数量
	public String price;//股票发行价格
	public String profit;//发行市盈率
	public String type;//发行方式
	public String underwriter;//主承销商
	public Relation referee;//上市推荐人
	public Relation sponsor;//保荐机构
	public String taskId;//招股说明
	public List<CustomerPersonalLine> propertyList; //自定义属性
	
}
