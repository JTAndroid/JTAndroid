package com.tr.model.home;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author chewei
 * @description TODO
 * @parameter 
 */
public class PeopleIndustry implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2581057162281252349L;

	private int id;
	private String name;
	private int pid;
	private String sortId;
	private int flag;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getSortId() {
		return sortId;
	}

	public void setSortId(String sortId) {
		this.sortId = sortId;
	}

	public JSONObject tojson() throws JSONException{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", id);
		jsonObject.put("name", name);
		jsonObject.put("pid", pid);
		jsonObject.put("sortId", sortId);
		jsonObject.put("flag", flag);
		return jsonObject;
	}
	
}
