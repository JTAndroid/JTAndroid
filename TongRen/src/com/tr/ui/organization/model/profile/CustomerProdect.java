package com.tr.ui.organization.model.profile;

import java.io.Serializable;
import java.util.List;


import com.tr.ui.organization.model.profile.CustomerPersonalLine;
/**
 * 金融产品
 * @author liubang
 *
 */
public class CustomerProdect implements Serializable{
	/**
	 * 
	 */
	public static final long serialVersionUID = 1381333679030820126L;
	public String id;//序号
	public String name;//产品名称
	public String funds;//资金规模
	public String startTime;//成立日期
	public String limit;//期限
	public String profit;//收益分配
	public String status;//状态 推介中 开放中 运行中 已终止
	public String taskId;//附件信息
	public List<CustomerPersonalLine> propertyList; //自定义属性

	
}
