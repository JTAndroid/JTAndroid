package com.tr.model.model;


/**
 * 人脉 社会活动 活动类型
 * @author wenbin
 *
 */
public class PeopleActivity extends BaseObject {

	public static final long serialVersionUID = 7033294052932641457L;
	/**主键*/
	public Long id;
	/**类型：1-社团，2-组织，3-党派，4-政治团体，5-慈善机构，N-自定义*/
	public PeopleSelectTag typeTag;
	/**描述*/
	public String content;
	
	
	
	
}
