package com.tr.model.joint;

import java.util.ArrayList;

import com.tr.model.demand.ASSOData;
import com.tr.model.demand.DemandASSOData;
import com.tr.model.knowledge.KnowledgeMini2;
import com.tr.model.obj.AffairsMini;

/**
 * 生态对接实体类
 */
public class JointResource_org_area {

	private static final long serialVersionUID = 1L;
	/*
	 * "id":"", "country":"", "province":"", "city":"", "county":"",
	 * "address":""
	 */
	private String id;
	private String country;
	private String province;
	private String city;
	private String county;
	private String address;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
