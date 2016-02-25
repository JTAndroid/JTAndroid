package com.tr.model.model;


/**
 * 人脉分组
 * @author wenbin
 *
 */
public class PeopleGroupTemp extends BaseObject {

	public static final long serialVersionUID = -1702145300615502322L;
	
	/**主键*/
	public Long id;
	/**名称*/
	public String name;
	
	public PeopleGroupTemp(){
		
	}
	
	public PeopleGroupTemp(String name){
		this.name = name;
	}
	
	

	
}
