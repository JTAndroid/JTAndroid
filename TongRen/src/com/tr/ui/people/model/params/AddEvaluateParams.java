package com.tr.ui.people.model.params;

import java.io.Serializable;

public class AddEvaluateParams implements Serializable{

	/**
	 * 添加评价的请求参数类
	 */
	private static final long serialVersionUID = 1L;
	
	public String comment;
	
	public long userId;

	@Override
	public String toString() {
		return "AddEvaluateParams [comment=" + comment + ", userId=" + userId
				+ "]";
	}
	
	

}
