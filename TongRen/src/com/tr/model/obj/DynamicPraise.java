package com.tr.model.obj;

import java.io.Serializable;

import org.json.JSONObject;

/**
 * 点赞对象
 * @author zhongshan
 */
public class DynamicPraise implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private long id;//
	private long pId;//赞同者id
	private String pName;//赞同者名称
	private long dynamicId;//动态id
	private int ptype;//区分用户与组织 1为用户，2为组织
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getpId() {
		return pId;
	}
	public void setpId(long pId) {
		this.pId = pId;
	}
	public String getpName() {
		return pName;
	}
	public void setpName(String pName) {
		this.pName = pName;
	}
	public long getDynamicId() {
		return dynamicId;
	}
	public void setDynamicId(long dynamicId) {
		this.dynamicId = dynamicId;
	}
	public int getPtype() {
		return ptype;
	}
	public void setPtype(int ptype) {
		this.ptype = ptype;
	}
	public static DynamicPraise createFactory(JSONObject jsonObject) {
		try {
			DynamicPraise self = new DynamicPraise();
			self.id = jsonObject.optLong("id");
			self.pId = jsonObject.optLong("pId");
			self.dynamicId = jsonObject.optLong("dynamicId");
			self.pName = jsonObject.optString("name");
			self.ptype = jsonObject.optInt("ptype");
			return self;
		} catch (Exception e) {
			return null;
		}
	}
}
