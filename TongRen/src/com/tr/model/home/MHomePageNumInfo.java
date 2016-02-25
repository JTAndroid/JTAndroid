package com.tr.model.home;

import java.io.Serializable;

/**
 * 
 * @author sunjianan
 *
 */
public class MHomePageNumInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1962252937734165328L;
	
	private int type;
	private int num;
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
}
