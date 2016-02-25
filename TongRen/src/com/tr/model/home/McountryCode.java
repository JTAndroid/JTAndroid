package com.tr.model.home;

import java.io.Serializable;

import com.utils.pinyin.PingYinUtil;

public class McountryCode implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 519572578675919516L;

	private String code;
	private String country;
	private long id;
	private String nameFirst;
	private String spell;

	public McountryCode(MCountry country) {
		super();
		this.country = country.getCountry();
		this.code = country.getCode();
		this.id = country.getId();
		this.nameFirst = country.getNameFirst();
		spell = PingYinUtil.getPingYin(country.getCountry()).toUpperCase();
	}

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

	public String getSpell() {
		return spell;
	}

	public void setSpell(String spell) {
		this.spell = spell;
	}

}
