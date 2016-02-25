package com.tr.model.model;

/**
 * 人脉 个人情况 社会关系
 * @author wenbin
 *
 */
public class PeopleCommunityRelationship extends BaseObject {

	public static final long serialVersionUID = -6285194705065611887L;
	
	/**主键*/
	public Long id;
	/**类型：1-配偶，2-父亲，3-母亲，4-兄弟姐妹，5-同居伴侣，6-子女，7-经理，8-上级，9-下级，10-助手，11-合作伙伴，12-介绍人，N-自定义*/
	public PeopleSelectTag typeTag;
	/**内容*/
	public String content;
	
	
	
}
