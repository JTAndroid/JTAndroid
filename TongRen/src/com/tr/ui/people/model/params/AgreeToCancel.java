package com.tr.ui.people.model.params;

import java.io.Serializable;

public class AgreeToCancel implements Serializable{

	/**
	 * 赞同与取消赞同参数类
	 */
	private static final long serialVersionUID = 1L;
	
	public long id;
	
	public boolean feedback;

	@Override
	public String toString() {
		return "AgreeToCancel [id=" + id + ", feedback=" + feedback + "]";
	}
	
}
