package com.tr.ui.organization.model.param;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tr.model.demand.ASSORPOK;
import com.tr.ui.organization.model.Customer;
import com.tr.ui.organization.model.permission.CustomerPermission_Bean;
import com.tr.ui.people.model.Category;

public class CustomerClientParams implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2353779355020572796L;
	
	public  ASSORPOK relevance;
	public ClientDetailsParams customer;
	public Boolean 	success;
	public CustomerPermission_Bean customerPermissions;//权限信息
	public ArrayList<Category> directory;
	public ArrayList<PushKnowledge> orgNewList;
	public ArrayList<NoticeList> noticeList;
	public List<JsonObj> industryObj; //冗余行业备注
}
