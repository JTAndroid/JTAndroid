package com.tr.model.joint;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 生态对接实体类
 */
public class JointResource implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/*"orgs":Array[1],
    "total":37202,
    "knos":Array[20],
    "scope":"3",
    "reqsTotal":0,
    "peoples":Array[1],
    "page":"1",
    "peoplesTotal":5,
    "knosTotal":37196,
    "orgsTotal":1,
    "reqs":Array[0],
    "rows":"20"*/
	
	private ArrayList<JointResource_org> orgs;
	private int total;
	private ArrayList<JointResource_kno> knos;
	private String scope;
	private int reqsTotal;
	private ArrayList<JointResource_people> peoples;
	private String page;
	private int peoplesTotal;
	private int knosTotal;
	private int orgsTotal;
	private ArrayList<JointResource_req> reqs;
	private String rows;

	public ArrayList<JointResource_org> getOrgs() {
		return orgs;
	}

	public void setOrgs(ArrayList<JointResource_org> orgs) {
		this.orgs = orgs;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public ArrayList<JointResource_kno> getKnos() {
		return knos;
	}

	public void setKnos(ArrayList<JointResource_kno> knos) {
		this.knos = knos;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public int getReqsTotal() {
		return reqsTotal;
	}

	public void setReqsTotal(int reqsTotal) {
		this.reqsTotal = reqsTotal;
	}

	public ArrayList<JointResource_people> getPeoples() {
		return peoples;
	}

	public void setPeoples(ArrayList<JointResource_people> peoples) {
		this.peoples = peoples;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public int getPeoplesTotal() {
		return peoplesTotal;
	}

	public void setPeoplesTotal(int peoplesTotal) {
		this.peoplesTotal = peoplesTotal;
	}

	public int getKnosTotal() {
		return knosTotal;
	}

	public void setKnosTotal(int knosTotal) {
		this.knosTotal = knosTotal;
	}

	public int getOrgsTotal() {
		return orgsTotal;
	}

	public void setOrgsTotal(int orgsTotal) {
		this.orgsTotal = orgsTotal;
	}

	public ArrayList<JointResource_req> getReqs() {
		return reqs;
	}

	public void setReqs(ArrayList<JointResource_req> reqs) {
		this.reqs = reqs;
	}

	public String getRows() {
		return rows;
	}

	public void setRows(String rows) {
		this.rows = rows;
	}

}
