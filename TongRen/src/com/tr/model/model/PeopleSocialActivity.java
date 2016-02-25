package com.tr.model.model;

import java.util.ArrayList;
import java.util.List;


/**
 * 人脉 工作经历 社会活动
 * @author wenbin
 *
 */
public class PeopleSocialActivity extends BaseObject {

	public static final long serialVersionUID = -7931904431419730697L;
	
	/**主键*/
	public Long id;
	/**活动类型*/
	public ArrayList<PeopleActivity> activityList;
	/**介绍人*/
	public String referrals;
	/**老乡*/
	public String fellow;
	/**描述*/
	public String description;
	/**自定义字段*/
	public ArrayList<PeoplePersonalLine> personalLineList;
	
	public PeopleSocialActivity(){
		
	}
	
	public PeopleSocialActivity(String referrals, String fellow,
			String descirption) {
		this.referrals = referrals;
		this.fellow = fellow;
		this.description = descirption;
	}
	
	
	
}
