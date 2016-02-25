package com.tr.ui.organization.model.profile;

import java.io.Serializable;

import com.tr.ui.organization.model.Relation;

/**
 * 客户电话
 * @author tanghh
 *
 */
public class CustomerPhone implements Serializable{
	/**
	 * 
	 */
	public static final long serialVersionUID = 5405815631394110664L;
	public String id;//序号
	public String type;//电话类型  联系电话，商务电话
	public String areaCode;//区号
    public String phone;	//客户电话
    public String extension;//分机号
	
    
}
