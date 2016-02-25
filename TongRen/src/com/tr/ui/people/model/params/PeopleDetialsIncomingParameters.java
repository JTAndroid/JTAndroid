package com.tr.ui.people.model.params;

import java.io.Serializable;

public class PeopleDetialsIncomingParameters implements Serializable{

	/**
	 * 
	 */
	public static final long serialVersionUID = 100L;
	
	public long id;
	/**1.用户  2.人脉*/
	public int personType;		
	/**有权限 1 没有权限 0*/
	public int view;
}
