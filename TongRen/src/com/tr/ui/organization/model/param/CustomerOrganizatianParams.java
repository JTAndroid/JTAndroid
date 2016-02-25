package com.tr.ui.organization.model.param;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.tr.model.demand.ASSORPOK;
import com.tr.ui.organization.model.Customer;
import com.tr.ui.organization.model.CustomerProfileVo;
import com.tr.ui.organization.model.permission.CustomerPermission_Bean;
import com.tr.ui.organization.model.resource.CustomerResource;
import com.tr.ui.people.model.Category;

public class CustomerOrganizatianParams implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2353779355020572796L;
	
	public  ASSORPOK relevance;
	public CustomerProfileVo customer;
	public Boolean 	success;
	public CustomerPermission_Bean customerPermissions;//权限信息
	public ArrayList<Category> directory;
	public ArrayList<PushKnowledge> orgNewList;
	public ArrayList<NoticeList> noticeList;
	public CustomerResource investmentdemandList; 
}
