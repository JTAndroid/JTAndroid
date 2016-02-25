package com.tr.model.joint;

import java.io.Serializable;

public class JointResource_people  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*"locationCity":"",
    "position":"",
    "tags":"zlg,金桐网,",
    "id":"10000013000000000",
    "tagsScores":"zlg=8,金桐网=5,",
    "reason":"同事:金桐网,",
    "email":"zhangliguo@gintong.com",
    "company":"金桐网",
    "name":"zlg",
    "personType":2,
    "telephone":"15110159685",
    "portrait":"http://file.online.gintong.com/web1/90/1469134390993.jpg"*/
	
	private String locationCity;
	private String position;
	private String tags;
	private String id;
	private String tagsScores;
	private String reason;
	private String email;
	private String company;
	private String name;
	private String personType;
	private String telephone;
	private String portrait;

	public String getLocationCity() {
		return locationCity;
	}

	public void setLocationCity(String locationCity) {
		this.locationCity = locationCity;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTagsScores() {
		return tagsScores;
	}

	public void setTagsScores(String tagsScores) {
		this.tagsScores = tagsScores;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPersonType() {
		return personType;
	}

	public void setPersonType(String personType) {
		this.personType = personType;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getPortrait() {
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}
	
}
