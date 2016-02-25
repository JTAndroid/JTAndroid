package com.tr.model.model;


/**
 * 人脉通用自定义标签
 * @author wenbin
 *
 */
public class PeoplePersonalLine extends BaseObject {

	public static final long serialVersionUID = -689880968897406943L;
	
	
	/**主键*/
	public Long id;
	/**所属模块： 1-联系方式，2-个人情况，3-投资需求，4-融资需求，5-专家需求，6-专家身份，7-教育经历，8-工作经历，9-社会活动，10-会面情况，99-自定义板块*/
	public Integer parentType;
	/**自定义名称代码*/
	public String code;
	/**自定义名称*/
	public String name;
	/**内容*/
	public String content;
	
	public static final int PARENTTYPE_CONTACT = 1;
	public static final int PARENTTYPE_SITUATIONPERSONAL = 2;
	public static final int PARENTTYPE_INVESTMENTDEMAND = 3;
	public static final int PARENTTYPE_FINANCINGDEMAND = 4;
	public static final int PARENTTYPE_EXPERTDEMAND = 5;
	public static final int PARENTTYPE_EXPERTIDENTITYDEMAND = 6;
	public static final int PARENTTYPE_EDUCATION = 7;
	public static final int PARENTTYPE_WORKEXPERIENCE = 8;
	public static final int PARENTTYPE_SOCIALACTIVITY = 9;
	public static final int PARENTTYPE_MEETING = 10;
	public static final int PARENTTYPE_PERSONALPLATE = 99;
	
	
	
}
