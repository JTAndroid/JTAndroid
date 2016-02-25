package com.tr.ui.people.model.success;

import java.io.Serializable;

public class AgreeToCancleBoolean implements Serializable{

	/**
	 * 响应赞同与取消赞同的数据类
	 */
	private static final long serialVersionUID = 1L;
	
	public boolean success;

	@Override
	public String toString() {
		return "AgreeToCancleBoolean [success=" + success + "]";
	}
	
	

}
