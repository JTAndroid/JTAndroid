package com.tr.ui.people.model.params;

import java.io.Serializable;

public class FindEvaluateParams implements Serializable{

	/**
	 * 查询人脉评价的参数
	 */
	private static final long serialVersionUID = 1L;
	
	public long userId;
	
	public boolean isSelf;

	@Override
	public String toString() {
		return "FindEvaluateParams [userId=" + userId + ", isSelf=" + isSelf
				+ "]";
	}
	

}
