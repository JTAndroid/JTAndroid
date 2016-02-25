package com.tr.model.model;


/**
 * 人脉关联客户
 * @author wenbin
 *
 */
public class PeopleCustomer extends BaseObject {

	public static final long serialVersionUID = 626789426972267039L;
	
	/**主键*/
	public Long id;
	/**名称*/
	public String name;
	
	public PeopleCustomer(){
		
	}
	
	public PeopleCustomer(String name){
		this.name = name;
	}
	
	

	
}
