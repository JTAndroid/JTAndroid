package com.tr.ui.people.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PersonalInformation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8166888040426283114L;
	/**
	 * 籍贯所在国家
	 */
	public String birthCountry;
	/**
	 * 籍贯所在省
	 */
	public String birthCounty;
	/**
	 * 籍贯所在城市
	 */
	public String birthCity;
	/**
	 * 信仰
	 */
	public String faithName;
	/**
	 * 身体状态
	 */
	public String health;
	/**
	 * 民族
	 */
	public String raceName;
	/**
	 * 爱好
	 */
	public String hobby;
	/**
	 * 擅长s
	 */
	public String goodAt;
	
	
	public String getGoodAt() {
		return goodAt;
	}
	public void setGoodAt(String goodAt) {
		this.goodAt = goodAt;
	}
	public String getBirthCountry() {
		return birthCountry;
	}
	public void setBirthCountry(String birthCountry) {
		this.birthCountry = birthCountry;
	}
	public String getBirthCounty() {
		return birthCounty;
	}
	public void setBirthCounty(String birthCounty) {
		this.birthCounty = birthCounty;
	}
	public String getBirthCity() {
		return birthCity;
	}
	public void setBirthCity(String birthCity) {
		this.birthCity = birthCity;
	}
	public String getFaithName() {
		return faithName;
	}
	public void setFaithName(String faithName) {
		this.faithName = faithName;
	}
	public String getHealth() {
		return health;
	}
	public void setHealth(String health) {
		this.health = health;
	}
	public String getRaceName() {
		return raceName;
	}
	public void setRaceName(String raceName) {
		this.raceName = raceName;
	}
	public String getHobby() {
		return hobby;
	}
	public void setHobby(String hobby) {
		this.hobby = hobby;
	}
	public String getHabit() {
		return habit;
	}
	public void setHabit(String habit) {
		this.habit = habit;
	}
	public String getBirthPlaceCountryName() {
		return birthPlaceCountryName;
	}
	public void setBirthPlaceCountryName(String birthPlaceCountryName) {
		this.birthPlaceCountryName = birthPlaceCountryName;
	}
	public List<Basic> getKeyDate() {
		if (keyDate==null) {
			keyDate =new ArrayList<Basic>();
		}
		return keyDate;
	}
	public void setKeyDate(List<Basic> keyDate) {
		this.keyDate = keyDate;
	}
	public List<Basic> getSocialRelations() {
		return socialRelations;
	}
	public void setSocialRelations(List<Basic> socialRelations) {
		this.socialRelations = socialRelations;
	}
	public List<Basic> getCustom() {
		return custom;
	}
	public void setCustom(List<Basic> custom) {
		this.custom = custom;
	}
	/**
	 * 习惯
	 */
	public String habit;
	
	/**籍贯国  国内、国外*/ 
	public String birthPlaceCountryName;
	/**
	 * 重要日期，改为数组
	 */
	public List<Basic> keyDate;
	/**
	 * 社会关系，改为数组
	 */
	public List<Basic> socialRelations;
	/**
	 * 自定义，改为数组
	 */
	public List<Basic> custom;

	
}
