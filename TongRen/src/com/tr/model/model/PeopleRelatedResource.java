package com.tr.model.model;

import java.util.List;

/**
 * 人脉相关资源
 * @author leon
 *
 */
public class PeopleRelatedResource extends BaseObject {

	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;

	public String range;
	public List<ResourceMini> listPeople;
	public List<ResourceMini> listOrganization;
	public List<ResourceMini> listRequirement;
	public List<ResourceMini> listKnowledge;
	
	
}
