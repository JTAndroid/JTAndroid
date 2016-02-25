package com.tr.ui.organization.model;

import java.io.Serializable;

public class Delete implements Serializable {

	private static final long serialVersionUID = -358422701839422678L;

	public String msg;
	public boolean success;
	
	
	@Override
	public String toString() {
		return "Delete [msg=" + msg + ", success=" + success + "]";
	}
	
	
}
