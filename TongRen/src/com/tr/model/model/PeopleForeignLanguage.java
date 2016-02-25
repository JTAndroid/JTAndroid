package com.tr.model.model;


/**
 * 人脉 教育经历 外语语种
 * @author wenbin
 *
 */
public class PeopleForeignLanguage extends BaseObject {

	public static final long serialVersionUID = 1142114251549329786L;
	
	/**主键*/
	public Long id;
	/**类型：1-英语，2-日语，3-法语，4-德语，5-西班牙语···*/
	public String type;
	/**级别类型：1-四级，2-六级，3-八级···*/
	public String levelType;
	
	
	
	
}
