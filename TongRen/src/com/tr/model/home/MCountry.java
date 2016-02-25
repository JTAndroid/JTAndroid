package com.tr.model.home;

import java.io.Serializable;

import com.utils.pinyin.PingYinUtil;

public class MCountry implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3035481958269158528L;

	private String code;
	private String country;
	private long id;
	private String nameFirst;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNameFirst() {
		return nameFirst;
	}

	public void setNameFirst(String nameFirst) {
		this.nameFirst = nameFirst;
	}

}
