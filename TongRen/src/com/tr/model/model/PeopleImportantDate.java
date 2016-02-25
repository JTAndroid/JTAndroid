package com.tr.model.model;

/**
 * 人脉 个人情况 重要日期
 * @author wenbin
 *
 */
public class PeopleImportantDate extends BaseObject {

	public static final long serialVersionUID = -8248869086739542704L;
	
	/**主键*/
	public Long id;
	/**类型：1-生日，2-周年纪念日*/
	public PeopleSelectTag typeTag;
	/**日期*/
	public String date;
	
	
}
