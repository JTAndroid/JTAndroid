package com.tr.ui.organization.model.firstpage;

import java.io.Serializable;

public class Item implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3275214128689812107L;
	public String address;
	public String city;
	public String country;
	public String county;
	public long createById;
	public String ctime;
	public long customerId;
	public long id;
	public String industryIds;
	public String industrys;
	public String linkMobile;
	public String name;
	public String nameFirst;
	public String nameIndex;
	public String nameIndexAll;
	public String picLogo;
	public String province;
	public String remark;
	public String shotName;
	public String type;
	public String utime;
	public int virtual;
	@Override
	public String toString() {
		return "Item [address=" + address + ", city=" + city + ", country="
				+ country + ", county=" + county + ", createById=" + createById
				+ ", ctime=" + ctime + ", customerId=" + customerId + ", id="
				+ id + ", industryIds=" + industryIds + ", industrys="
				+ industrys + ", linkMobile=" + linkMobile + ", name=" + name
				+ ", nameFirst=" + nameFirst + ", nameIndex=" + nameIndex
				+ ", nameIndexAll=" + nameIndexAll + ", picLogo=" + picLogo
				+ ", province=" + province + ", remark=" + remark
				+ ", shotName=" + shotName + ", type=" + type + ", utime="
				+ utime + ", virtual=" + virtual + "]";
	}
	
	
}
