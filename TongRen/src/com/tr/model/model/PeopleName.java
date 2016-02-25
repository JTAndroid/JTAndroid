package com.tr.model.model;

/**
 * 人脉名称
 * @author wenbin
 *
 */
public class PeopleName extends BaseObject{

	public static final long serialVersionUID = -1776727966678686811L;
	/**主键*/
	public Long id;
	/**姓名类型：1-中文名，2-英文名，N-自定义*/
	public PeopleSelectTag typeTag;
	/**姓名*/
	public String name;
	
	
	
}
