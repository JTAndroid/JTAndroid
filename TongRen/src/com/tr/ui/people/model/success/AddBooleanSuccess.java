package com.tr.ui.people.model.success;

import java.io.Serializable;

public class AddBooleanSuccess implements Serializable{

	/**
	 * 添加评价是否成功
	 */
	private static final long serialVersionUID = 1L;
	
	public long id;
	
	public boolean success;

	@Override
	public String toString() {
		return "AddBooleanSuccess [id=" + id + ", success=" + success + "]";
	}
	
	

}
