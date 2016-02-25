package com.tr.ui.organization.model.profile;

import java.io.Serializable;
import java.util.ArrayList;
/**
* <p>Title: CustomerPersonalPlate.java<／p> 
* <p>Description:客户 自定义板块 <／p> 
* @author wfl
* @date 2014-12-30 
* @version 1.0
 */
public class CustomerPersonalPlate implements Serializable{

	
	/**自定义板块名称*/
	public String name;
	/**内容*/
	public ArrayList<CustomerPersonalLine> personalLineList;
	@Override
	public String toString() {
		return "CustomerPersonalPlate [name=" + name + ", personalLineList="
				+ personalLineList + "]";
	}
	
	
	
}
