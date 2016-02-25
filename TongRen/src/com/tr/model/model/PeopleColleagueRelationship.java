package com.tr.model.model;


/**
 * 人脉 工作经历 同事关系
 * @author wenbin
 *
 */
public class PeopleColleagueRelationship extends BaseObject {

	public static final long serialVersionUID = -414530209944087623L;
	
	/**主键*/
	public Long id;
	/**类型：1-同事，2-上级，3-下级，N-自定义*/
	public PeopleSelectTag typeTag;
	/**关系描述*/
	public String content;

	
	
	
}
