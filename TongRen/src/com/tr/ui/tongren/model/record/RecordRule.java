package com.tr.ui.tongren.model.record;

import java.io.Serializable;

public class RecordRule implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// "createId": 126852,
	// "createTime": 1446098306000,
	// "description": "testdes",
	// "elasticityMinutes": 10,
	// "id": 3903281198006277,
	// "name": "test",
	// "organizationId": 3899738856620077,
	// "startWorkTime": "09:20",
	// "updateTime": 1446098306000,
	// "weekWorkingDays": 0,
	// "workTimeOut": "18:30",
	// "workingHours": 0
	private String createId;
	private String createTime;
	private String description;
	private String elasticityMinutes;
	private String id;
	private String name;
	private String organizationId;
	private String startWorkTime;
	private String updateTime;
	private String weekWorkingDays;
	private String workTimeOut;
	private String workingHours;

	public String getCreateId() {
		return createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getElasticityMinutes() {
		return elasticityMinutes;
	}

	public void setElasticityMinutes(String elasticityMinutes) {
		this.elasticityMinutes = elasticityMinutes;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	public String getStartWorkTime() {
		return startWorkTime;
	}

	public void setStartWorkTime(String startWorkTime) {
		this.startWorkTime = startWorkTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getWeekWorkingDays() {
		return weekWorkingDays;
	}

	public void setWeekWorkingDays(String weekWorkingDays) {
		this.weekWorkingDays = weekWorkingDays;
	}

	public String getWorkTimeOut() {
		return workTimeOut;
	}

	public void setWorkTimeOut(String workTimeOut) {
		this.workTimeOut = workTimeOut;
	}

	public String getWorkingHours() {
		return workingHours;
	}

	public void setWorkingHours(String workingHours) {
		this.workingHours = workingHours;
	}

}
