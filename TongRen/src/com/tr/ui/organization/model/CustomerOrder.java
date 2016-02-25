package com.tr.ui.organization.model;

import java.io.Serializable;

/**
 * 客户模块实体类
* <p>Title: CustomerOrder.java<／p> 
* <p>Description: <／p> 

* @author fuliwen 
* @date 2014-12-17 
* @version 1.0
 */
public class CustomerOrder implements Serializable{
	
	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;
	
	// 客户模块id,作为主键
	public long id;
	// 客户模块排序 
	public String modelOrder;
	// 客户id，mongo库中customerId
	public long customerId;
}
