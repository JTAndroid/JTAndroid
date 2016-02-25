package com.tr.model.model;


/**
 * 人脉 教育经历 同学关系
 * @author wenbin
 *
 */
public class PeopleStudentsRelationship extends BaseObject {

	/**主键*/
	public Long id;
	/**类型：1-同学，2-校友，3-老师，4-校长，N-自定义*/
	public PeopleSelectTag typeTag;
	/**关系描述*/
	public String content;
	
	
	
	
}
