package com.tr.model.model;


/**
 * 人脉 select自定义标签
 * @author wenbin
 *
 */
public class PeopleSelectTag extends BaseObject {

	public static final long serialVersionUID = 3507293970531998126L;
	
	public final static int type_custom=1; 
	public final static int type_default=0; 
	
	/**主键*/
	public Long id;
	/**类型编码*/
	public String code;
	/**内容*/
	public String name;
	/**类型，0-默认，1-自定义*/
	public Integer type;
	
	public PeopleSelectTag(int type,String code,String name){
		this.code = code;
		this.name = name;
		this.type=type;
	}
	
	
}
