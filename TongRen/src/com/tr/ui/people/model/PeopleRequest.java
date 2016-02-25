package com.tr.ui.people.model;

import java.util.ArrayList;

import com.tr.model.demand.ASSORPOK;

public class PeopleRequest {
	public Person people = new Person() ;
	public ArrayList<Long> tid  = new  ArrayList<Long>();
	public ArrayList<Long> categoryList = new ArrayList<Long>() ;
	public PermIds permIds = new PermIds();
	public String  opType ;  //"1-创建新人脉;2-保存他人人脉为自己人脉;3-个人好友转人脉是保存人脉;4-编辑人脉;5-编辑个人资料;",
	public ASSORPOK asso = new ASSORPOK();
}
 