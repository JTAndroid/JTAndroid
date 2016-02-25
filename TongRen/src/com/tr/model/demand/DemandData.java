package com.tr.model.demand; 

import java.io.Serializable;

/**
 * @ClassName:     DemandData.java
 * @author         fxtx
 * @Date           2015年3月24日 下午5:36:32 
 * @Description:   TODO(用一句话描述该文件做什么) 
 */
public class DemandData implements Serializable{
	/**
	 * 
	 */
	public static final long serialVersionUID = 3961111340736886795L;
	public String id;
	public String name;
	public DemandData() {
		// TODO Auto-generated constructor stub
	}
	public DemandData(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public DemandData cloneMe(){
		DemandData clone = new DemandData(id,name) ;
		return clone ;
	}


	//获取最后的名称
	public String lastName(){
		if(null!=name){
			return name.replaceAll("^.*-(.+)$", "$1");
		}
		return "";
	}
	
}
 
