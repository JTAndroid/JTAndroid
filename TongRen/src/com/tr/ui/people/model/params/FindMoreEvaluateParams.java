package com.tr.ui.people.model.params;

import java.io.Serializable;

public class FindMoreEvaluateParams implements Serializable{

	/**
	 * 获取更多评价参数类
	 */
	private static final long serialVersionUID = 1L;
	
	public long userId;

	@Override
	public String toString() {
		return "FindMoreEvaluateParams [userId=" + userId + "]";
	}
	

}
