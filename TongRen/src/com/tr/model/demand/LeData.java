package com.tr.model.demand; 

import java.io.Serializable;


/**
 * @ClassName:     LeData.java
 * @author         fxtx
 * @Date           2015年3月25日 上午10:50:57 
 * @Description:   保存大乐独乐 小乐 等信息
 */
public class LeData implements Serializable{
	/**
	 * 
	 */
	public static final long serialVersionUID = -1885003197959446216L;
	public String id;
	public String name;
	public LeData() {
		// TODO Auto-generated constructor stub
	}
	
	public LeData(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	
}
 
